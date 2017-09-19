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
package ts.eclipse.ide.angular.internal.cli.launch;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;

import ts.eclipse.ide.angular.cli.NgCommand;
import ts.eclipse.ide.angular.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular.internal.cli.AngularCLIProject;
import ts.eclipse.ide.angular.internal.cli.AngularCLIProjectSettings;
import ts.eclipse.ide.angular.internal.cli.Trace;
import ts.eclipse.ide.core.TypeScriptCorePlugin;
import ts.eclipse.ide.core.utils.TypeScriptResourceUtil;
import ts.utils.FileUtils;

/**
 * ng launch helper.
 *
 */
public class AngularCLILaunchHelper {

	public static String getWorkingDir(IContainer folder) {
		return new StringBuilder("${workspace_loc:/").append(folder.getFullPath()).append("}").toString();
	}

	public static String getWorkingDir(IProject project) {
		return new StringBuilder("${workspace_loc:/").append(project.getName()).append("}").toString();
	}

	public static void launch(NgCommand ngCommand, IProject project) throws CoreException {
		launch(ngCommand, project, ILaunchManager.RUN_MODE);
	}

	public static void launch(NgCommand ngCommand, IProject project, String mode) throws CoreException {
		String workingDir = AngularCLILaunchHelper.getWorkingDir(project);
		String operation = ngCommand.name().toLowerCase();

		// Check if configuration already exists
		ILaunchConfiguration ngConfiguration = chooseLaunchConfiguration(workingDir, operation);
		if (ngConfiguration != null) {
			ILaunchConfigurationWorkingCopy wc = ngConfiguration.getWorkingCopy();
			// Update nodejs file path if needed
			if (wc.getAttribute(AngularCLILaunchConstants.NODE_FILE_PATH, (String) null) == null) {
				updateNodeFilePath(project, wc);
			}
			// Update ng file path
			if (wc.getAttribute(AngularCLILaunchConstants.NG_FILE_PATH, (String) null) == null) {
				updateNgFilePath(project, wc);
			}
			ngConfiguration = wc.doSave();
			DebugUITools.launch(ngConfiguration, mode);
		} else {
			// Creating Launch Configuration from scratch
			ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration(project.getName(),
					operation);
			// nodejs file to use
			updateNodeFilePath(project, newConfiguration);
			// ng file to use
			updateNgFilePath(project, newConfiguration);
			newConfiguration.setAttribute(AngularCLILaunchConstants.WORKING_DIR, workingDir);
			newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION, operation);
			// newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS,
			// "--live-reload-port 65535");
			newConfiguration.doSave();
			DebugUITools.launch(newConfiguration, mode);
		}
	}

	public static void updateNodeFilePath(IProject project, ILaunchConfigurationWorkingCopy newConfiguration) {
		File nodeFile = getNodeFile(project);
		if (nodeFile != null) {
			newConfiguration.setAttribute(AngularCLILaunchConstants.NODE_FILE_PATH, FileUtils.getPath(nodeFile));
		}
	}

	public static void updateNgFilePath(IProject project, ILaunchConfigurationWorkingCopy newConfiguration)
			throws CoreException {
		AngularCLIProjectSettings  settings = AngularCLIProject.getAngularCLIProject(project).getSettings();
		File ngFile = settings.getCustomNgLocation();
		if (ngFile != null) {
			newConfiguration.setAttribute(AngularCLILaunchConstants.NG_FILE_PATH, FileUtils.getPath(ngFile));
			newConfiguration.setAttribute(AngularCLILaunchConstants.EXECUTE_NG_WITH_FILE, settings.isExecuteNgWithFile());
		}
	}

	public static File getNodeFile(IProject project) {
		try {
			return TypeScriptResourceUtil.getNodejsInstallPath(project);
		} catch (Exception e) {
			Trace.trace(Trace.SEVERE, "Error while getting node file", e);
		}
		return null;
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
