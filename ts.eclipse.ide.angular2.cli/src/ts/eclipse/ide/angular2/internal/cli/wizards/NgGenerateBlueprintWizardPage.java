/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIProject;
import ts.utils.StringUtils;

/**
 * Generic wizard page which generates Angular2 resources by using "ng
 * generate".
 *
 */
public class NgGenerateBlueprintWizardPage extends WizardPage implements Listener {

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	private final NgBlueprint blueprint;
	private Text resourceNameField;
	private IProject project;
	private Combo projectCombo;

	protected NgGenerateBlueprintWizardPage(String pageName, String title, ImageDescriptor titleImage,
			NgBlueprint blueprint, IProject project) {
		super(pageName, title, titleImage);
		setPageComplete(false);
		this.blueprint = blueprint;
		this.project = project;
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
		
		// initialize project based on the current selection
		setProject(project);

		validatePage();
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(topLevel);
	}

	public void handleEvent(Event event) {
		setPageComplete(validatePage());
	}
	
	protected void createNameControl(Composite parent) {
		Font font = parent.getFont();
		// resource name group
		Composite nameGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		nameGroup.setLayout(layout);
		nameGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		nameGroup.setFont(font);

		// Project
		Label label = new Label(nameGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_projectName);
		label.setFont(font);

		// project selection combo
		projectCombo = new Combo(nameGroup, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectCombo.setLayoutData(gd);
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (AngularCLIProject.isAngularCLIProject(project)) {
				projectCombo.add(project.getName());
			}
		}
		projectCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!"".equals(projectCombo.getText())) {
					setProject(ResourcesPlugin.getWorkspace().getRoot().getProject(projectCombo.getText()));
				} else {
					setProject(null);
				}
			}
		});
		projectCombo.addListener(SWT.Modify, this);

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
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		resourceNameField.setLayoutData(data);
		resourceNameField.setFont(font);
		// validateControls();

	}

	protected void createParamsControl(Composite parent) {
		
	}

	private void setProject(IProject project) {
		IProject previousProject = this.project;
		this.project = project;

		// select correct project in the combo box
		int index;
		if (project != null) {
			index = projectCombo.indexOf(project.getName());
		} else {
			index = -1;
		}
		if (index == -1) {
			index = projectCombo.indexOf("");
		}
		// on Linux, Combo.select(int) always causes a ModifyEvent
		if (index != projectCombo.getSelectionIndex()) {
			projectCombo.select(index);
		}
	}

	protected boolean validatePage() {
		if (StringUtils.isEmpty(projectCombo.getText())) {
			setErrorMessage(AngularCLIMessages.NgGenerateBlueprintWizardPage_select_ngProject_error);
			return false;
		} else if (StringUtils.isEmpty(resourceNameField.getText())) {
			setErrorMessage(AngularCLIMessages.NgGenerateBlueprintWizardPage_select_name_required_error);
			return false;
		}
		setErrorMessage(null);
		return true;
	}

	public IProject getProject() {
		return project;
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
}
