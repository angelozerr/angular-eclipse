/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.internal.cli.wizards;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.osgi.service.prefs.BackingStoreException;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.cli.preferences.AngularCLIPreferenceConstants;
import ts.eclipse.ide.angular2.cli.utils.CLIProcessHelper;
import ts.eclipse.ide.angular2.cli.utils.CLIStatus;
import ts.eclipse.ide.angular2.cli.utils.NgVersionJob;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.core.utils.WorkbenchResourceUtil;
import ts.eclipse.ide.terminal.interpreter.EnvPath;
import ts.eclipse.ide.terminal.interpreter.LineCommand;
import ts.eclipse.ide.terminal.interpreter.TerminalCommandAdapter;
import ts.eclipse.ide.ui.utils.StatusUtil;
import ts.eclipse.ide.ui.widgets.IStatusChangeListener;
import ts.eclipse.ide.ui.widgets.NpmInstallWidget;
import ts.eclipse.ide.ui.wizards.AbstractWizardNewTypeScriptProjectCreationPage;
import ts.utils.FileUtils;

/**
 * Main wizard page to create an Angular2 project.
 *
 */
public class WizardNewNgProjectCreationPage extends AbstractWizardNewTypeScriptProjectCreationPage {

	public static final String projectNameRegexp = "^[a-zA-Z][.0-9a-zA-Z]*(-[.0-9a-zA-Z]*)*$";
	public static final String[] unsupportedProjectNames = new String[] { "test", "ember", "ember-cli", "vendor",
			"app" };

	// Angular CLI
	private Boolean hasGlobalAngularCLI;
	private Button useGlobalAngularCLIButton;
	private Button useInstallAngularCLIButton;
	private Text globalAngularCLIVersion;
	private NpmInstallWidget installAngularCLI;
	private boolean useGlobalAngularCLI;

	private NgVersionJob ngVersionJob;

	public WizardNewNgProjectCreationPage(String pageName, BasicNewResourceWizard wizard) {
		super(pageName, wizard);
	}

	@Override
	protected void createPageBody(Composite parent) {
		super.createPageBody(parent);
		Group group = new Group(parent, SWT.NONE);
		group.setFont(parent.getFont());
		group.setText(AngularCLIMessages.WizardNewNgProjectCreationPage_angular_cli_group_label);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		int nColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		group.setLayout(layout);

		// Global Angular CLI
		createGlobalAngularCLIField(group);

		// Install Angular CLI
		createInstallAngularCLIField(group);
	}

	/** Creates the field for install Angular CLI. */
	private void createGlobalAngularCLIField(Composite parent) {
		useGlobalAngularCLIButton = new Button(parent, SWT.RADIO);
		useGlobalAngularCLIButton.setText(AngularCLIMessages.WizardNewNgProjectCreationPage_useGlobalAngularCLI);
		useGlobalAngularCLIButton.addListener(SWT.Selection, this);
		useGlobalAngularCLIButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateAngularCLIMode();
			}
		});
		globalAngularCLIVersion = new Text(parent, SWT.WRAP | SWT.READ_ONLY);
		globalAngularCLIVersion.setText(""); //$NON-NLS-1$
	}

	/** Creates the field for install Angular CLI. */
	private void createInstallAngularCLIField(Composite parent) {
		useInstallAngularCLIButton = new Button(parent, SWT.RADIO);
		useInstallAngularCLIButton.setText(AngularCLIMessages.WizardNewNgProjectCreationPage_useInstallAngularCLI);
		useInstallAngularCLIButton.addListener(SWT.Selection, this);
		useInstallAngularCLIButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateAngularCLIMode();
			}
		});
		installAngularCLI = new NpmInstallWidget("@angular/cli", new IStatusChangeListener() {
			@Override
			public void statusChanged(IStatus status) {
				setPageComplete(validatePage());
			}
		}, parent, SWT.NONE);
		installAngularCLI.getVersionText().addListener(SWT.Modify, this);
	}

	@Override
	protected void initializeDefaultValues() {
		super.initializeDefaultValues();
		useGlobalAngularCLIButton.setSelection(true);
	}

	@Override
	protected void nodeJsChanged(File nodeFile) {
		super.nodeJsChanged(nodeFile);
		if (hasGlobalAngularCLI == null && nodeFile != null && ngVersionJob == null) {
			ngVersionJob = new NgVersionJob();
			ngVersionJob.setNodeFile(nodeFile);
			ngVersionJob.setNgFile(CLIProcessHelper.findNg());
			ngVersionJob.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {
					super.done(event);
					IStatus status = event.getResult();
					final String version;
					if (status instanceof CLIStatus) {
						final CLIStatus s = (CLIStatus) status;
						version = s.getVersion();
						if (s.isOK())
							hasGlobalAngularCLI = Boolean.TRUE;
					} else {
						hasGlobalAngularCLI = Boolean.FALSE;
						version = "";
					}
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (!globalAngularCLIVersion.isDisposed())
								globalAngularCLIVersion.setText(version);
							validatePage();
						}
					});
				}
			});
			ngVersionJob.schedule();
		}
	}

	@Override
	protected void updateComponents(Event event) {
		super.updateComponents(event);
		Widget item = event != null ? event.item : null;
		if (item == null || item == useGlobalAngularCLIButton)
			updateAngularCLIMode();
	}

	private void updateAngularCLIMode() {
		useGlobalAngularCLI = useGlobalAngularCLIButton.getSelection();
		installAngularCLI.setEnabled(!useGlobalAngularCLI);
	}

	@Override
	protected IStatus validatePageImpl() {
		IStatus status = StatusUtil.getMoreSevere(super.validatePageImpl(), validateNgProjectName(getProjectName()));
		return StatusUtil.getMoreSevere(status, validateAngularCLI());
	}

	/** Validates the name of the Angular-CLI Project. */
	private IStatus validateNgProjectName(String name) {
		IStatus status = Status.OK_STATUS;
		for (int i = 0, cnt = unsupportedProjectNames.length; i < cnt; i++) {
			if (unsupportedProjectNames[i].equals(name)) {
				status = new Status(IStatus.WARNING, AngularCLIPlugin.PLUGIN_ID,
						NLS.bind(AngularCLIMessages.NewAngular2ProjectWizard_unsupportedProjectNames, name));
				break;
			}
		}
		if (status.isOK()) {
			String[] parts = name.split("-");
			for (int i = 0, cnt = parts.length; i < cnt; i++) {
				if (!parts[i].matches(projectNameRegexp)) {
					status = new Status(IStatus.WARNING, AngularCLIPlugin.PLUGIN_ID,
							NLS.bind(AngularCLIMessages.NewAngular2ProjectWizard_invalidProjectName, name));
					break;
				}
			}
		}
		return status;
	}

	/** Validates the Angular CLI. */
	private IStatus validateAngularCLI() {
		if (useGlobalAngularCLI) {
			if (hasGlobalAngularCLI == null)
				return new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID,
						AngularCLIMessages.WizardNewNgProjectCreationPage_searchingForGlobalAngularCLI);
			else if (!hasGlobalAngularCLI.booleanValue())
				return new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID,
						AngularCLIMessages.WizardNewNgProjectCreationPage_noGlobalAngularCLI);
			else
				return Status.OK_STATUS;
		}
		return installAngularCLI.getStatus();
	}

	@Override
	public void updateCommand(List<LineCommand> commands, final IProject project, String nodeFilePath) {
		if (!useGlobalAngularCLI) {
			// when Angular CLI is installed, update the project Eclipse preferences
			// to consume this installed Angular CLI.
			commands.add(new LineCommand(installAngularCLI.getNpmInstallCommand(), new TerminalCommandAdapter() {
				@Override
				public void onTerminateCommand(LineCommand lineCommand) {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							IEclipsePreferences preferences = new ProjectScope(project)
									.getNode(AngularCLIPlugin.PLUGIN_ID);
							preferences.putBoolean(AngularCLIPreferenceConstants.NG_USE_GLOBAL_INSTALLATION, false);
							preferences.put(AngularCLIPreferenceConstants.NG_CUSTOM_FILE_PATH,
									"${project_loc:node_modules/.bin}");
							try {
								preferences.flush();
							} catch (BackingStoreException e) {
								e.printStackTrace();
							}
						}
					});

				}
			}));
			commands.add(EnvPath.createSetPathCommand(EnvPath.insertToEnvPath(nodeFilePath, FileUtils
					.getPath(WorkbenchResourceUtil.resolvePath("${project_loc:node_modules/.bin}", project)))));
		}
	}

}
