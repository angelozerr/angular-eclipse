/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.internal.cli.preferences;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ControlEnableState;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.cli.preferences.AngularCLIPreferenceConstants;
import ts.eclipse.ide.angular2.cli.utils.CLIProcessHelper;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.core.utils.WorkbenchResourceUtil;
import ts.eclipse.ide.ui.preferences.BrowseButtonsComposite;
import ts.eclipse.ide.ui.preferences.IStatusChangeListener;
import ts.eclipse.ide.ui.preferences.OptionsConfigurationBlock;
import ts.eclipse.ide.ui.preferences.ScrolledPageContent;
import ts.eclipse.ide.ui.preferences.StatusInfo;
import ts.utils.FileUtils;
import ts.utils.StringUtils;

/**
 * @angular/cli configuration block.
 *
 */
public class AngularCLIConfigurationBlock extends OptionsConfigurationBlock {

	private ControlEnableState blockEnableState;
	private Composite controlsComposite;

	private Button ngUseGlobalInstallation;
	private Combo ngCustomFilePath;
	private BrowseButtonsComposite browseButtons;

	private static final String[] DEFAULT_PATHS = new String[] { "${project_loc:node_modules/.bin}" };

	private static final Key PREF_NG_USE_GLOBAL_INSTALLATION = getAngularCliKey(
			AngularCLIPreferenceConstants.NG_USE_GLOBAL_INSTALLATION);
	private static final Key PREF_NG_CUSTOM_FILE_PATH = getAngularCliKey(
			AngularCLIPreferenceConstants.NG_CUSTOM_FILE_PATH);

	private Text cliPath;
	private Text cliVersion;
	private final UIJob ngVersionJob;
	private File ngFile;

	public AngularCLIConfigurationBlock(IStatusChangeListener context, IProject project,
			IWorkbenchPreferenceContainer container) {
		super(context, project, getKeys(), container);
		ngVersionJob = new UIJob("") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				String version = CLIProcessHelper.getNgVersion(ngFile);
				CLIStatus status = null;
				if (StringUtils.isEmpty(version)) {
					// ERROR: the file path is not a ng
					status = new CLIStatus(null,
							NLS.bind(AngularCLIMessages.AngularCLIConfigurationBlock_ngCustomFile_invalid_error,
									FileUtils.getPath(ngFile)));
					cliPath.setText("");
					cliVersion.setText("");
				} else {
					status = new CLIStatus(ngFile, null);
					cliVersion.setText(version);
				}
				fContext.statusChanged(status);
				return Status.OK_STATUS;
			}
		};
		blockEnableState = null;
	}

	private static Key[] getKeys() {
		return new Key[] { PREF_NG_USE_GLOBAL_INSTALLATION, PREF_NG_CUSTOM_FILE_PATH };
	}

	protected final static Key getAngularCliKey(String key) {
		return getKey(AngularCLIPlugin.PLUGIN_ID, key);
	}

	@Override
	protected Composite createUI(Composite parent) {
		final ScrolledPageContent pageContent = new ScrolledPageContent(parent);
		Composite composite = pageContent.getBody();
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);

		controlsComposite = new Composite(composite, SWT.NONE);
		controlsComposite.setFont(composite.getFont());
		controlsComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 1;
		controlsComposite.setLayout(layout);

		createBody(controlsComposite);
		createCLIInfo(composite);
		return pageContent;

	}

	private void createBody(Composite parent) {
		int nColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;

		Group group = new Group(parent, SWT.NONE);
		group.setFont(parent.getFont());
		group.setText(AngularCLIMessages.AngularCLIConfigurationBlock_cli_group_label);
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		group.setLayout(layout);

		createNgUseGlobalInstallation(group);
		createNgUseCustomFile(group);

		updateComboBoxes();
	}

	private void createNgUseGlobalInstallation(Composite parent) {
		// Create "Use ng global installation" checkbox
		ngUseGlobalInstallation = addRadioBox(parent,
				AngularCLIMessages.AngularCLIConfigurationBlock_ngUseGlobalInstallation_label,
				PREF_NG_USE_GLOBAL_INSTALLATION, new String[] { "true", "true" }, 1);
		ngUseGlobalInstallation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateComboBoxes();
			}
		});

		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		ngUseGlobalInstallation.setLayoutData(gd);
	}

	private void createNgUseCustomFile(Composite parent) {
		// Create "Installed TypeScript" checkbox
		Button ngUseCustomFile = addRadioBox(parent,
				AngularCLIMessages.AngularCLIConfigurationBlock_ngUseCustomFile_label, PREF_NG_USE_GLOBAL_INSTALLATION,
				new String[] { "false", "false" }, 0);
		ngUseCustomFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateComboBoxes();
			}
		});

		ngCustomFilePath = newComboControl(parent, PREF_NG_CUSTOM_FILE_PATH, getDefaultPaths(), getDefaultPaths(),
				false);
		ngCustomFilePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create Browse buttons.
		browseButtons = new BrowseButtonsComposite(parent, ngCustomFilePath, getProject(), SWT.NONE);
	}

	private void createCLIInfo(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		GridData gridData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gridData);

		// CLI version
		Label cliVersionTitle = new Label(composite, SWT.NONE);
		cliVersionTitle.setText(AngularCLIMessages.AngularCLIConfigurationBlock_cliVersion_label);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		cliVersionTitle.setLayoutData(gridData);

		cliVersion = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
		cliVersion.setText(""); //$NON-NLS-1$
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		gridData.widthHint = 200;
		cliVersion.setLayoutData(gridData);

		// CLI path
		Label cliPathTitle = new Label(composite, SWT.NONE);
		cliPathTitle.setText(AngularCLIMessages.AngularCLIConfigurationBlock_cliPath_label);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		cliPathTitle.setLayoutData(gridData);

		cliPath = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
		cliPath.setText(""); //$NON-NLS-1$
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		gridData.widthHint = 200;
		cliPath.setLayoutData(gridData);
	}

	private String[] getDefaultPaths() {
		return DEFAULT_PATHS;
	}

	@Override
	protected void validateSettings(Key changedKey, String oldValue, String newValue) {
		cliVersion.setText("");
		cliPath.setText("");
		CLIStatus status = validateCLIPath();
		if (status.isOK()) {
			cliPath.setText(FileUtils.getPath(status.getNgFile()));
			cliVersion.setText("Executing 'ng --version'...");
			ngFile = status.getNgFile();
			ngVersionJob.cancel();
			ngVersionJob.schedule();
		} else {
			fContext.statusChanged(status);
		}
	}

	private class CLIStatus extends StatusInfo {

		private final File ngFile;

		public CLIStatus(File ngFile, String errorMessage) {
			if (errorMessage != null) {
				setError(errorMessage);
			}
			this.ngFile = ngFile;
		}

		public File getNgFile() {
			return ngFile;
		}

	}

	/**
	 * Returns the status of the ng path.
	 * 
	 * @return the status of the ng path.
	 */
	private CLIStatus validateCLIPath() {
		File ngFile = null;
		boolean useGlobal = ngUseGlobalInstallation.getSelection();
		if (useGlobal) {
			ngFile = CLIProcessHelper.findNg();
			if (ngFile == null) {
				// ERROR: ng was not installed with "npm install @angular/cli
				// -g"
				return new CLIStatus(null, AngularCLIMessages.AngularCLIConfigurationBlock_ngGlobal_notFound_error);
			}
		} else {
			String ngPath = ngCustomFilePath.getText();
			if (StringUtils.isEmpty(ngPath)) {
				// ERROR: the installed path is empty
				return new CLIStatus(null, AngularCLIMessages.AngularCLIConfigurationBlock_ngCustomFile_required_error);
			} else {
				ngFile = WorkbenchResourceUtil.resolvePath(ngPath, getProject());
				if (!ngFile.isDirectory()) {
					// ERROR: the ng path must be a directory which contains the
					// ng/ng.cmd
					return new CLIStatus(null,
							NLS.bind(AngularCLIMessages.AngularCLIConfigurationBlock_ngCustomFile_notDir_error,
									FileUtils.getPath(ngFile)));
				}
				ngFile = new File(ngFile, CLIProcessHelper.getNgFileName());
			}
		}

		if (!ngFile.exists()) {
			// ERROR: ng file doesn't exists
			return new CLIStatus(null,
					NLS.bind(AngularCLIMessages.AngularCLIConfigurationBlock_ngCustomFile_exists_error,
							FileUtils.getPath(ngFile)));
		} else {

		}
		// ng path is valid
		return new CLIStatus(ngFile, null);
	}

	@Override
	protected String[] getFullBuildDialogStrings(boolean workspaceSettings) {
		return null;
	}

	public void enablePreferenceContent(boolean enable) {
		if (controlsComposite != null && !controlsComposite.isDisposed()) {
			if (enable) {
				if (blockEnableState != null) {
					blockEnableState.restore();
					blockEnableState = null;
				}
			} else {
				if (blockEnableState == null) {
					blockEnableState = ControlEnableState.disable(controlsComposite);
				}
			}
		}
	}

	private void updateComboBoxes() {
		boolean global = ngUseGlobalInstallation.getSelection();
		ngCustomFilePath.setEnabled(!global);
		browseButtons.setEnabled(!global);
	}

}
