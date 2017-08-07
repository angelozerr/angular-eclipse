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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tm.terminal.view.core.interfaces.constants.ITerminalsConnectorConstants;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.wizards.newresource.ResourceMessages;
import org.osgi.service.prefs.BackingStoreException;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIImageResource;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.core.TypeScriptCorePlugin;
import ts.eclipse.ide.core.utils.TypeScriptResourceUtil;
import ts.eclipse.ide.terminal.interpreter.EnvPath;
import ts.eclipse.ide.terminal.interpreter.LineCommand;
import ts.eclipse.ide.ui.wizards.AbstractNewProjectWizard;
import ts.eclipse.ide.ui.wizards.AbstractWizardNewTypeScriptProjectCreationPage;
import ts.utils.StringUtils;

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

	private NewAngular2ProjectParamsWizardPage paramsPage;

	/**
	 * Creates a wizard for creating a new project resource in the workspace.
	 */
	public NewAngular2ProjectWizard() {
		super(WIZARD_NAME, AngularCLIMessages.NewAngular2ProjectWizard_newProjectTitle,
				AngularCLIMessages.NewAngular2ProjectWizard_newProjectDescription);
	}

	@Override
	protected AbstractWizardNewTypeScriptProjectCreationPage createMainPage() {
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
		 * if (ResourcesPlugin.getWorkspace().getRoot().getProjects().length > 0) {
		 * referencePage = new
		 * WizardNewProjectReferencePage("basicReferenceProjectPage");//$NON-NLS -1$
		 * referencePage.setTitle(ResourceMessages.NewProject_referenceTitle);
		 * referencePage.setDescription(ResourceMessages.
		 * NewProject_referenceDescription); this.addPage(referencePage); }
		 */

		paramsPage = new NewAngular2ProjectParamsWizardPage();
		this.addPage(paramsPage);
	}

	@Override
	protected IRunnableWithProgress getRunnable(final IProject newProjectHandle, final IProjectDescription description,
			final IPath projectLocation) {
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

				// Add TypeScript builder
				try {
					TypeScriptResourceUtil.addTypeScriptBuilder(newProjectHandle);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				}

				IEclipsePreferences preferences = new ProjectScope(newProjectHandle)
						.getNode(TypeScriptCorePlugin.PLUGIN_ID);

				// Update node.js preferences
				mainPage.updateNodeJSPreferences(preferences);

				// Get the commands to run
				List<LineCommand> commands = new ArrayList<>();
				Map<String, Object> properties = new HashMap<String, Object>();
				String nodeFilePath = getNodeFilePath();
				mainPage.updateCommand(commands, newProjectHandle, nodeFilePath);
				paramsPage.updateCommand(commands, newProjectHandle);

				if (!commands.isEmpty()) {
					// Prepare terminal properties
					properties.put(ITerminalsConnectorConstants.PROP_PROCESS_WORKING_DIR, projectLocation.toString());
					if (!StringUtils.isEmpty(nodeFilePath)) {
						EnvPath.insertToEnvPath(properties, nodeFilePath);
					}
					String terminalId = "Angular Projects";
					executeCommandsInTerminal(terminalId, commands, properties);
				}
				try {
					preferences.flush();
				} catch (BackingStoreException e) {
					e.printStackTrace();
				}
			}
		};
	}
}