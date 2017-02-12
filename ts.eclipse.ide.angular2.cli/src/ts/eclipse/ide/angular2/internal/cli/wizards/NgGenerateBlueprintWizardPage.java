/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *
 */
package ts.eclipse.ide.angular2.internal.cli.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIProject;
import ts.eclipse.ide.angular2.internal.cli.Trace;
import ts.eclipse.ide.angular2.internal.cli.json.AngularCLIJson;
import ts.eclipse.ide.ui.utils.DialogUtils;
import ts.utils.StringUtils;

/**
 * Generic wizard page which generates Angular2 resources by using "ng
 * generate".
 *
 */
public class NgGenerateBlueprintWizardPage extends WizardPage implements Listener {

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	private final NgBlueprint blueprint;
	private Text location;
	private Text resourceNameField;

	private IContainer folder;

	protected NgGenerateBlueprintWizardPage(String pageName, String title, ImageDescriptor titleImage,
			NgBlueprint blueprint, IContainer folder) {
		super(pageName, title, titleImage);
		setPageComplete(false);
		this.blueprint = blueprint;
		this.folder = folder;
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		// top level group
		Composite topLevel = new Composite(parent, SWT.NONE);
		topLevel.setLayout(new GridLayout());
		topLevel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		topLevel.setFont(parent.getFont());

		createNameControl(topLevel);

		// Separator
		Label line = new Label(topLevel, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		line.setLayoutData(gridData);

		createParamsControl(topLevel);

		// Initialize fields.
		initializePage();

		validatePage();
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(topLevel);
	}

	protected void initializePage() {
		if (folder == null) {
			return;
		}
		IProject project = folder.getProject();
		IPath folderPath = new Path(project.getName()).append(folder.getProjectRelativePath());
		if (!isBelongToAngularClIProject(folder)) {
			location.setText(folder.getFullPath().toString());
			return;
		}
		// Compute root path
		IPath rootPath = getAngularCLIJson().getRootPath(project);
		if (rootPath.isPrefixOf(folderPath)) {
			// Initialize location with selected folder which is included in the
			// /$project/$src/app folder
			location.setText(folder.getFullPath().toString());
		} else {
			// Initialize location with root path /$project/$src/app
			location.setText(rootPath.toString());
		}
	}

	public void handleEvent(Event event) {
		setPageComplete(validatePage());
	}

	protected void createNameControl(Composite parent) {

		Font font = parent.getFont();
		// resource name group
		Composite nameGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = 0;
		nameGroup.setLayout(layout);
		nameGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		nameGroup.setFont(font);

		// Location
		Label label = new Label(nameGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_location);
		label.setFont(font);

		// Location entry field and "Browse"-button
		location = new Text(nameGroup, SWT.BORDER);
		location.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String location = getLocation();
				if (!"".equals(location)) {
					IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(location);
					setFolder(res instanceof IContainer ? (IContainer) res : null);
				} else {
					setFolder(null);
				}
			}
		});
		location.addListener(SWT.Modify, this);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		location.setLayoutData(data);
		location.setFont(font);

		Button browseButton = new Button(nameGroup, SWT.PUSH);
		browseButton.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_browse_location);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				handleLocationBrowseButtonPressed();
			}
		});

		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		// data.widthHint = IDialogConstants.BUTTON_WIDTH;
		browseButton.setLayoutData(data);
		browseButton.setFont(font);

		// Blueprint name
		label = new Label(nameGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_bluePrintName);
		label.setFont(font);

		// resource name entry field
		resourceNameField = new Text(nameGroup, SWT.BORDER);
		resourceNameField.addListener(SWT.Modify, this);
		// resourceNameField.addFocusListener(new FocusAdapter() {
		// public void focusLost(FocusEvent e) {
		// handleResourceNameFocusLostEvent();
		// }
		// });
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		data.horizontalSpan = 2;
		resourceNameField.setLayoutData(data);
		resourceNameField.setFont(font);

		// validateControls();

	}

	protected void createParamsControl(Composite parent) {
	}

	private void setFolder(IContainer folder) {
		this.setFolder(folder, false);
	}

	private void setFolder(IContainer folder, boolean updateText) {
		this.folder = folder;
		if (updateText) {
			this.location.setText(folder.getFullPath().toString());
		}
	}

	private String getLocation() {
		return location.getText();
	}

	private void handleLocationBrowseButtonPressed() {
		IContainer folder = getFolder();
		String initialFolder = folder != null ? folder.getProjectRelativePath().toString() : null;
		final IProject project = folder != null ? folder.getProject() : null;
		ElementTreeSelectionDialog dialog = DialogUtils.createFolderDialog(initialFolder, project, project == null,
				true, getShell());
		dialog.addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IProject) {
					// Show only project which are angular-cli project
					IProject p = (IProject) element;
					return AngularCLIProject.isAngularCLIProject(p);
				} else if (element instanceof IContainer) {
					// Check if the given container is included in the
					// angular-cli root-path
					IContainer container = (IContainer) element;
					return isValidAppsLocation(container, true);
				}
				return false;
			}
		});
		dialog.setTitle(AngularCLIMessages.NgGenerateBlueprintWizardPage_browse_location_title);
		if (dialog.open() == Window.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				Object selectedFolder = result[0];
				if (selectedFolder instanceof IContainer) {
					setFolder((IContainer) selectedFolder, true);
				}
			}
		}
	}

	protected boolean validatePage() {
		IContainer folder = getFolder();
		if (folder == null) {
			setErrorMessage(AngularCLIMessages.NgGenerateBlueprintWizardPage_invalid_location_error);
			return false;
		} else if (!isBelongToAngularClIProject(folder)) {
			setErrorMessage(AngularCLIMessages.NgGenerateBlueprintWizardPage_invalid_project_location_error);
			return false;
		} else if (!isValidAppsLocation(folder, false)) {
			IPath rootPath = getAngularCLIJson().getRootPath(folder.getProject());
			setErrorMessage(NLS.bind(AngularCLIMessages.NgGenerateBlueprintWizardPage_invalid_apps_location_error,
					rootPath.toString()));
			return false;
		} else if (StringUtils.isEmpty(resourceNameField.getText())) {
			setErrorMessage(AngularCLIMessages.NgGenerateBlueprintWizardPage_select_name_required_error);
			return false;
		}
		setErrorMessage(null);
		return true;
	}

	public IContainer getFolder() {
		return folder;
	}

	public NgBlueprint getNgBluePrint() {
		return blueprint;
	}

	public String getBluepringName() {
		return resourceNameField.getText();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setFocus();
		}
	}

	/**
	 * Gives focus to the resource name field and selects its contents
	 */
	private void setFocus() {
		// select the whole resource name.
		resourceNameField.setSelection(0, resourceNameField.getText().length());
		resourceNameField.setFocus();
	}

	/**
	 * Returns the Pojo of the angular-cli.json from the selected project.
	 *
	 * @return
	 */
	protected AngularCLIJson getAngularCLIJson() {
		try {
			return AngularCLIProject.getAngularCLIProject(folder.getProject()).getAngularCLIJson();
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, "Error while loading angular-cli.json", e);
			return null;
		}
	}

	/**
	 * Returns true if the given folder is a valid "apps" location and false
	 * otherwise.
	 *
	 * @param folder
	 *            the container to validate
	 * @param acceptParent
	 *            true if it accept parent and false otherwise.
	 * @return true if the given folder is a valid "apps" location and false
	 *         otherwise.
	 */
	private boolean isValidAppsLocation(IContainer folder, boolean acceptParent) {
		IProject project = folder.getProject();
		if (project == null) {
			return false;
		}
		IPath rootPath = getAngularCLIJson().getRootPath(folder.getProject());
		IPath folderPath = new Path(project.getName()).append(folder.getProjectRelativePath());
		if (acceptParent && folderPath.isPrefixOf(rootPath)) {
			return true;
		}
		return (rootPath.isPrefixOf(folderPath));
	}

	/**
	 * Returns true if the given folder belongs to an angular cli project and
	 * false otherwise.
	 *
	 * @param folder
	 * @return true if the given folder belongs to an angular cli project and
	 *         false otherwise.
	 */
	private boolean isBelongToAngularClIProject(IContainer folder) {
		if (folder == null) {
			return false;
		}
		return AngularCLIProject.isAngularCLIProject(folder.getProject());
	}

}
