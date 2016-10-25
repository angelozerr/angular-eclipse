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

import org.eclipse.core.externaltools.internal.launchConfigurations.ExternalToolsCoreUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.wst.jsdt.js.cli.core.CLICommand;
import org.eclipse.wst.jsdt.js.cli.core.CLIStreamListener;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.jobs.NgProjectJob;
import ts.eclipse.ide.angular2.internal.cli.jsdt.CLI;

/**
 * Launch configuration which consumes angular-cli to generate project,
 * component, etc.
 *
 */
public class AngularCLILaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String arg1, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		String ngFilePath = configuration.getAttribute(AngularCLILaunchConstants.NG_FILE_PATH, (String) null);
		String nodeFilePath = configuration.getAttribute(AngularCLILaunchConstants.NODE_FILE_PATH, (String) null);
		IPath workingDir = ExternalToolsCoreUtil.getWorkingDirectory(configuration);
		String operation = configuration.getAttribute(AngularCLILaunchConstants.OPERATION, (String) null);
		String[] options = configuration.getAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS, "").split(" ");
		if (monitor.isCanceled()) {
			return;
		}

		CLICommand command = createCommand(ngFilePath, nodeFilePath, operation, options);
		IPath wd = workingDir;

		NgCommand ngCommand = NgCommand.getCommand(operation);
		boolean waitForTerminate = isWaitForTerminate(ngCommand);

		IProject project = (IProject) ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(wd);
		CLIStreamListener listener = createListener(ngCommand, project, options);
		try {
			new CLI(null, wd, command).execute(listener, waitForTerminate, monitor);
		} finally {
			if (ngCommand != null) {
				File projectDir = workingDir.toFile();
				UIJob job = null;
				switch (ngCommand) {
				case NEW:
				case INIT:
					// Refresh Eclipse project and open angular-cli.json
					job = new NgProjectJob(projectDir);
					job.setRule(ResourcesPlugin.getWorkspace().getRoot());
					job.schedule();
					break;
				default:
					break;
				}
			}
		}
	}

	private boolean isWaitForTerminate(NgCommand ngCommand) {
		if (ngCommand != null) {
			switch (ngCommand) {
			case SERVE:
			case TEST:
				return false;
			default:
				return true;
			}
		}
		return true;
	}

	private CLICommand createCommand(String ngFilePath, String nodeFilePath, String operation, String[] options) {
		if (ngFilePath != null) {
			if (nodeFilePath != null) {
				return new CLICommand(nodeFilePath, ngFilePath, operation.toLowerCase(), options);
			}
			return new CLICommand("node", ngFilePath, operation.toLowerCase(), options);
		}
		return new CLICommand("ng", operation.toLowerCase(), null, options);
	}

	private CLIStreamListener createListener(NgCommand ngCommand, IProject project, String[] options) {
		if (ngCommand == null) {
			return new CLIStreamListener();
		}
		switch (ngCommand) {
		case GENERATE:
			if (options != null && options.length > 1) {
				return new NgGenerateCLIStreamListener(options[0], project);
			}
		case SERVE:
			return new NgServeCLIStreamListener();
		default:
			return new CLIStreamListener();
		}
	}

}
