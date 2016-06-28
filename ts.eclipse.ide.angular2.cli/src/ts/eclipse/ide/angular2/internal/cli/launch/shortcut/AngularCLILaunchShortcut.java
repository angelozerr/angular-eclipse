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
package ts.eclipse.ide.angular2.internal.cli.launch.shortcut;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIProject;
import ts.eclipse.ide.angular2.internal.cli.Trace;
import ts.utils.FileUtils;

/**
 * Base class for Launch configuration shortcut for angular-cli
 */
public class AngularCLILaunchShortcut implements ILaunchShortcut {

	private final NgCommand ngCommand;

	public AngularCLILaunchShortcut(NgCommand ngCommand) {
		this.ngCommand = ngCommand;
	}

	@Override
	public void launch(ISelection selection, String mode) {
		try {
			Object objSelected = ((IStructuredSelection) selection).getFirstElement();
			if (objSelected instanceof IResource) {
				launchHelper(((IResource) objSelected).getProject(), mode);
			}
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, e.getLocalizedMessage());
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		try {
			IEditorInput editorInput = editor.getEditorInput();
			if (editorInput instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) editorInput).getFile();
				launchHelper(file.getProject(), mode);
			}
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, e.getLocalizedMessage());
		}
	}

	protected void launchHelper(IProject project, String mode) throws CoreException {
		try {
			ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration();
			File ngFile = AngularCLIProject.getAngularCLIProject(project).getSettings().getNgFile();
			if (ngFile != null) {
				newConfiguration.setAttribute(AngularCLILaunchConstants.NG_FILE_PATH, FileUtils.getPath(ngFile));
			}
			newConfiguration.setAttribute(AngularCLILaunchConstants.WORKING_DIR, project.getLocation().toString());
			newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION, ngCommand.name());
			//newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS, "--live-reload-port 65535");
			DebugUITools.launch(newConfiguration, mode);
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, e.getLocalizedMessage());
		}
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
