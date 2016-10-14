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
package ts.eclipse.ide.angular2.internal.cli.launch;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIProject;
import ts.eclipse.ide.core.TypeScriptCorePlugin;
import ts.utils.FileUtils;

/**
 * ng launch helper.
 *
 */
public class AngularCLILaunchHelper {

	public static void launch(NgCommand ngCommand, IProject project) throws CoreException {
		launch(ngCommand, project, ILaunchManager.RUN_MODE);
	}

	public static void launch(NgCommand ngCommand, IProject project, String mode) throws CoreException {
		String workingDir = project.getLocation().toString();
		String operation = ngCommand.name().toLowerCase();

		// Check if configuration already exists
		ILaunchConfiguration ngConfiguration = chooseLaunchConfiguration(workingDir, operation);
		if (ngConfiguration != null) {
			ILaunchConfigurationWorkingCopy wc = ngConfiguration.getWorkingCopy();
			ngConfiguration = wc.doSave();
			DebugUITools.launch(ngConfiguration, mode);
		} else {
			// Creating Launch Configuration from scratch
			ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration(project.getName(),
					operation);
			File ngFile = AngularCLIProject.getAngularCLIProject(project).getSettings().getNgFile();
			if (ngFile != null) {
				newConfiguration.setAttribute(AngularCLILaunchConstants.NG_FILE_PATH, FileUtils.getPath(ngFile));
			}
			newConfiguration.setAttribute(AngularCLILaunchConstants.WORKING_DIR, workingDir);
			newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION, operation);
			// newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS,
			// "--live-reload-port 65535");
			newConfiguration.doSave();
			DebugUITools.launch(newConfiguration, mode);
		}
	}

	private static ILaunchConfiguration chooseLaunchConfiguration(String workingDir, String operation) {
		try {
			ILaunchConfigurationType ngLaunchConfigurationType = DebugPlugin.getDefault().getLaunchManager()
					.getLaunchConfigurationType(AngularCLILaunchConstants.LAUNCH_CONFIGURATION_ID);
			ILaunchConfiguration[] ngConfigurations = DebugPlugin.getDefault().getLaunchManager()
					.getLaunchConfigurations(ngLaunchConfigurationType);
			for (ILaunchConfiguration conf : ngConfigurations) {
				if (workingDir.equals(conf.getAttribute(AngularCLILaunchConstants.WORKING_DIR, (String) null))
						&& operation.equals(conf.getAttribute(AngularCLILaunchConstants.OPERATION, (String) null))) {
					return conf;
				}
			}
		} catch (CoreException e) {
			TypeScriptCorePlugin.logError(e, e.getMessage());
		}
		return null;
	}

	private static ILaunchConfigurationWorkingCopy createEmptyLaunchConfiguration(String projectName, String operation)
			throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager
				.getLaunchConfigurationType(AngularCLILaunchConstants.LAUNCH_CONFIGURATION_ID);
		ILaunchConfigurationWorkingCopy launchConfiguration = launchConfigurationType.newInstance(null,
				launchManager.generateLaunchConfigurationName(projectName + " [" + operation + "]"));
		return launchConfiguration;
	}
}
