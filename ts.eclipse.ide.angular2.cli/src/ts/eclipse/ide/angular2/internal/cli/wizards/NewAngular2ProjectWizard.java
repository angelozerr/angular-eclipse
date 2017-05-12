/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Remy Chi Jian Suen <remy.suen@gmail.com>
 *     		- Bug 44162 [Wizards]  Define constants for wizard ids of new.file, new.folder, and new.project
 *     Angelo Zerr <angelo.zerr@gmail.com> - adapt for Angular2 cli init project.
 *******************************************************************************/
package ts.eclipse.ide.angular2.internal.cli.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIImageResource;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.launch.AngularCLILaunchHelper;
import ts.eclipse.ide.core.TypeScriptCorePlugin;
import ts.eclipse.ide.ui.wizards.AbstractNewProjectWizard;
import ts.eclipse.ide.ui.wizards.WizardNewTypeScriptProjectCreationPage;

/**
 * Standard workbench wizard that creates a new angular-cli project resource in
 * the workspace.
 * <p>
 * This class may be instantiated and used without further configuration; this
 * class is not intended to be subclassed.
 * </p>
 * <p>
 * Example:
 *
 * <pre>
 * IWorkbenchWizard wizard = new NewAngular2ProjectWizard();
 * wizard.init(workbench, selection);
 * WizardDialog dialog = new WizardDialog(shell, wizard);
 * dialog.open();
 * </pre>
 *
 * During the call to <code>open</code>, the wizard dialog is presented to the
 * user. When the user hits Finish, a project resource with the user-specified
 * name is created, the dialog closes, and the call to <code>open</code>
 * returns.
 * </p>
 *
 * @noextend This class is not intended to be subclassed by clients.
 */
public class NewAngular2ProjectWizard extends AbstractNewProjectWizard {

	private static final String WIZARD_NAME = "NewAngular2ProjectWizard";

	private static final String ANGULAR_CLI_LAUNCH_NAME = "angular-cli";

	private NewAngular2ProjectParamsWizardPage paramsPage;

	/**
	 * Creates a wizard for creating a new project resource in the workspace.
	 */
	public NewAngular2ProjectWizard() {
		super(WIZARD_NAME, AngularCLIMessages.NewAngular2ProjectWizard_newProjectTitle,
				AngularCLIMessages.NewAngular2ProjectWizard_newProjectDescription);
	}

	@Override
	protected WizardNewTypeScriptProjectCreationPage createMainPage() {
		return new WizardNewNgProjectCreationPage("NgMainPage", this);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setNeedsProgressMonitor(true);
		setWindowTitle(AngularCLIMessages.NewAngular2ProjectWizard_windowTitle);
	}

	@Override
	protected void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = AngularCLIImageResource.getImageDescriptor(AngularCLIImageResource.IMG_ANGULAR2_WIZBAN);
		setDefaultPageImageDescriptor(desc);
	}

	@Override
	public void addPages() {
		super.addPages();

		// only add page if there are already projects in the workspace
		/*
		 * if (ResourcesPlugin.getWorkspace().getRoot().getProjects().length >
		 * 0) { referencePage = new
		 * WizardNewProjectReferencePage("basicReferenceProjectPage");//$NON-NLS
		 * -1$
		 * referencePage.setTitle(ResourceMessages.NewProject_referenceTitle);
		 * referencePage.setDescription(ResourceMessages.
		 * NewProject_referenceDescription); this.addPage(referencePage); }
		 */

		paramsPage = new NewAngular2ProjectParamsWizardPage();
		this.addPage(paramsPage);
	}

	private abstract class ErrorRunnable implements Runnable {

		private Throwable error;

		public Throwable getError() {
			return error;
		}

		private void setError(Throwable error) {
			this.error = error;
		}
	}

	@Override
	protected IRunnableWithProgress getRunnable(final IProject newProjectHandle, final IProjectDescription description,
			final IPath projectLocation) {
		return new IRunnableWithProgress() {

			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				// Execute @angular/cli "ng new $project-name"
				ErrorRunnable runnable = new ErrorRunnable() {
					@Override
					public void run() {
						try {
							final String operationParams = paramsPage.getParamsString();
							IEclipsePreferences preferences = new ProjectScope(newProjectHandle)
									.getNode(TypeScriptCorePlugin.PLUGIN_ID);
							mainPage.updateNodeJSPreferences(preferences);
							ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration();
							AngularCLILaunchHelper.updateNodeFilePath(newProjectHandle, newConfiguration);
							AngularCLILaunchHelper.updateNgFilePath(newProjectHandle, newConfiguration);
							newConfiguration.setAttribute(AngularCLILaunchConstants.WORKING_DIR,
									projectLocation.removeLastSegments(1).toString());
							newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION,
									NgCommand.NEW.name().toLowerCase());
							newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS,
									newProjectHandle.getName() + " " + operationParams);
							DebugUITools.launch(newConfiguration, ILaunchManager.RUN_MODE);
						} catch (CoreException e) {
							super.setError(e);
						}
					}
				};
				getShell().getDisplay().syncExec(runnable);
				if (runnable.getError() != null) {
					throw new InvocationTargetException(runnable.getError());
				}
			}
		};
	}

	private ILaunchConfigurationWorkingCopy createEmptyLaunchConfiguration() throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager
				.getLaunchConfigurationType(AngularCLILaunchConstants.LAUNCH_CONFIGURATION_ID);
		ILaunchConfigurationWorkingCopy launchConfiguration = launchConfigurationType.newInstance(null,
				launchManager.generateLaunchConfigurationName(ANGULAR_CLI_LAUNCH_NAME));
		return launchConfiguration;
	}

}