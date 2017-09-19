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
package ts.eclipse.ide.angular.internal.cli.preferences;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

import ts.eclipse.ide.angular.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular.cli.preferences.AngularCLIPreferenceConstants;
import ts.eclipse.ide.angular.cli.utils.CLIProcessHelper;
import ts.eclipse.ide.angular.cli.utils.CLIStatus;
import ts.eclipse.ide.angular.cli.utils.NgVersionJob;
import ts.eclipse.ide.angular.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.core.utils.TypeScriptResourceUtil;
import ts.eclipse.ide.core.utils.WorkbenchResourceUtil;
import ts.eclipse.ide.ui.preferences.BrowseButtonsComposite;
import ts.eclipse.ide.ui.preferences.OptionsConfigurationBlock;
import ts.eclipse.ide.ui.preferences.ScrolledPageContent;
import ts.eclipse.ide.ui.preferences.StatusInfo;
import ts.eclipse.ide.ui.widgets.IStatusChangeListener;
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

	private static final Key PREF_EXECUTE_NG_WITH_FILE = getAngularCliKey(
			AngularCLIPreferenceConstants.EXECUTE_NG_WITH_FILE);

	private Text cliPath;
	private Text cliVersion;
	private NgVersionJob ngVersionJob;

	public AngularCLIConfigurationBlock(IStatusChangeListener context, IProject project,
			IWorkbenchPreferenceContainer container) {
		super(context, project, getKeys(), container);
		ngVersionJob = new NgVersionJob();
		ngVersionJob.setNodeFile(getNodejsPath(project));
		ngVersionJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				IStatus status = event.getResult();
				if (!(status instanceof CLIStatus)) {
					return;
				}
				final CLIStatus s = (CLIStatus) status;
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						if (cliVersion.isDisposed()) {
							return;
						}
						if (!StringUtils.isEmpty(s.getVersion())) {
							cliVersion.setText(s.getVersion());
						} else {
							cliPath.setText("");
							cliVersion.setText("");

						}
						fContext.statusChanged(status);
					}
				});
			}
		});
		blockEnableState = null;
	}

	/** Gets the NodeJS-File for this Project. */
	private File getNodejsPath(IProject project) {
		try {
			return TypeScriptResourceUtil.getTypeScriptProject(project).getProjectSettings().getNodejsInstallPath();
		} catch (Throwable e) {
			return null;
		}
	}

	private static Key[] getKeys() {
		return new Key[] { PREF_NG_USE_GLOBAL_INSTALLATION, PREF_NG_CUSTOM_FILE_PATH, PREF_EXECUTE_NG_WITH_FILE };
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
		createExecuteNgWithFile(group);

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
		// Create "Use ng custom file" field
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

	private void createExecuteNgWithFile(Composite parent) {
		// Create "Execute Ng With File" checkbox
		addCheckBox(parent, AngularCLIMessages.AngularCLIConfigurationBlock_executeNgWithFile_label,
				PREF_EXECUTE_NG_WITH_FILE, new String[] { "true", "false" }, 0);
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
		ngVersionJob.cancel();
		IStatus status = validateCLIPath();
		if (status.isOK() && status instanceof CLIStatus) {
			File ngFile = ((CLIStatus)status).getNgFile();
			cliPath.setText(FileUtils.getPath(ngFile));
			cliVersion.setText("Executing 'ng --version'...");
			ngVersionJob.setNgFile(ngFile);
			ngVersionJob.schedule();
		} else {
			fContext.statusChanged(status);
		}
	}

	/**
	 * Returns the status of the ng path.
	 *
	 * @return the status of the ng path.
	 */
	private IStatus validateCLIPath() {
		File ngFile = null;
		boolean useGlobal = ngUseGlobalInstallation.getSelection();
		boolean globalPrefs = getProject() == null;
		if (useGlobal) {
			ngFile = CLIProcessHelper.findNg();
			if (ngFile == null) {
				// ERROR: ng was not installed with "npm install -g @angular/cli"
				return new CLIStatus(null, AngularCLIMessages.AngularCLIConfigurationBlock_ngGlobal_notFound_error);
			}
		} else {
			String ngPath = ngCustomFilePath.getText();
			if (StringUtils.isEmpty(ngPath)) {
				if (!globalPrefs) {
					// ERROR: the installed path is empty
					return new CLIStatus(null, AngularCLIMessages.AngularCLIConfigurationBlock_ngCustomFile_required_error);
				}
				else
					return StatusInfo.OK_STATUS;
			} else {
				ngFile = WorkbenchResourceUtil.resolvePath(ngPath, getProject());
				if (ngFile == null || !ngFile.isDirectory()) {
					if (!globalPrefs) {
						// ERROR: the ng path must be a directory which contains the
						// ng/ng.cmd
						return new CLIStatus(null,
								NLS.bind(AngularCLIMessages.AngularCLIConfigurationBlock_ngCustomFile_notDir_error,
										FileUtils.getPath(ngFile)));
					}
					else
						return StatusInfo.OK_STATUS;
				}
				ngFile = new File(ngFile, CLIProcessHelper.getNgFileName());
			}
		}
		if (!ngFile.exists()) {
			if (!globalPrefs) {
				// ERROR: ng file doesn't exists
				return new CLIStatus(null,
						NLS.bind(AngularCLIMessages.AngularCLIConfigurationBlock_ngCustomFile_exists_error,
								FileUtils.getPath(ngFile)));
			}
			else
				return StatusInfo.OK_STATUS;
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

	@Override
	public void dispose() {
		super.dispose();
		if (ngVersionJob != null) {
			ngVersionJob.cancel();
			ngVersionJob = null;
		}
	}
}
