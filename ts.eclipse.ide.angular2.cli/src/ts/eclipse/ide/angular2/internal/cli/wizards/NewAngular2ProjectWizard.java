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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.wizards.newresource.ResourceMessages;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.launch.AngularCLILaunchHelper;
import ts.eclipse.ide.ui.wizards.AbstractNewProjectWizard;

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

	private NewAngular2ProjectParamsWizardPage paramsPage;

	/**
	 * Creates a wizard for creating a new project resource in the workspace.
	 */
	public NewAngular2ProjectWizard() {
		super("NewAngular2ProjectWizard");
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
	protected IRunnableWithProgress getRunnable(final IProject newProjectHandle, final IProjectDescription description) {
		return new IRunnableWithProgress() {

			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				final CreateProjectOperation op1 = new CreateProjectOperation(description,
						ResourceMessages.NewProject_windowTitle);
				try {
					// see bug
					// https://bugs.eclipse.org/bugs/show_bug.cgi?id=219901
					// directly execute the operation so that the undo state is
					// not preserved. Making this undoable resulted in too many
					// accidental file deletions.
					op1.execute(monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
				} catch (ExecutionException e) {
					throw new InvocationTargetException(e);
				}

				ErrorRunnable runnable = new ErrorRunnable() {
					@Override
					public void run() {
						try {
							final String operationParams = paramsPage.getParamsString();
							ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration();
							AngularCLILaunchHelper.updateNodeFilePath(newProjectHandle, newConfiguration);
							AngularCLILaunchHelper.updateNgFilePath(newProjectHandle, newConfiguration);
							newConfiguration.setAttribute(AngularCLILaunchConstants.WORKING_DIR,
									newProjectHandle.getLocationURI().getPath());
							newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION, NgCommand.NEW.name().toLowerCase());
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
					try {
						op1.undo(monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
					} catch (ExecutionException e) {
						throw new InvocationTargetException(e);
					}
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
				launchManager.generateLaunchConfigurationName("angular-cli"));
		return launchConfiguration;
	}

}