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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.jobs.NgProjectJob;

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
		String workingDir = configuration.getAttribute(AngularCLILaunchConstants.WORKING_DIR, (String) null);
		String operation = configuration.getAttribute(AngularCLILaunchConstants.OPERATION, (String) null);
		String operationName = configuration.getAttribute(AngularCLILaunchConstants.OPERATION_NAME, (String) null);
		if (monitor.isCanceled()) {
			return;
		}

		CLICommand command = new CLICommand("ng", operation.toLowerCase(), null, null);
		IStreamListener streamListener = null;
		IPath wd = new Path(workingDir);

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(wd.segment(0));
		try {
			new AngularCLI(project, wd, command).execute(monitor);
		} finally {
			NgCommand ngCommand = NgCommand.getCommand(operation);
			if (ngCommand != null) {
				switch (ngCommand) {
				case NEW:
				case INIT:
					// Refresh Eclipse project and open angular-cli.json
					File projectDir = new File(workingDir);
					// Refresh Eclipse project and open angular-cli.json
					NgProjectJob job = new NgProjectJob(projectDir);
					job.setRule(ResourcesPlugin.getWorkspace().getRoot());
					job.schedule();
					break;
				default:
					break;
				}
			}
		}

		// IProcess process = startShell(streamListener, monitor,
		// getLaunchConfiguration(command), wd);
		// sendCLICommand(process, command, monitor);
		//
		// List<String> cmds = new ArrayList<String>();
		//// if (isWindows()) {
		//// cmds.add("cmd"); //$NON-NLS-1$
		//// } else {
		//// cmds.add("/bin/bash"); //$NON-NLS-1$
		//// cmds.add("-l"); //$NON-NLS-1$
		//// }
		// if (ngFilePath == null) {
		// cmds.add("grunt.cmd");
		// } else {
		// cmds.add(ngFilePath);
		// }
		// cmds.add(operation);
		// if (operationName != null) {
		// cmds.add(operationName);
		// }
		//
		// Process p = DebugPlugin.exec(cmds.toArray(new String[0]), new
		// File(workingDir), null);
		// IProcess process = null;
		//
		// Map<String, String> processAttributes = new HashMap<String,
		// String>();
		// processAttributes.put(IProcess.ATTR_PROCESS_TYPE, "ng");
		//
		// if (p != null) {
		// monitor.beginTask("ng...", -1);
		// process = DebugPlugin.newProcess(launch, p, "TODO",
		// processAttributes);
		// }
		//
		// AngularCliStreamListener reporter = new
		// AngularCliStreamListener(null);
		// process.getStreamsProxy().getOutputStreamMonitor().addListener(reporter);
		//
		// while (!process.isTerminated()) {
		// try {
		// if (monitor.isCanceled()) {
		// process.terminate();
		// break;
		// }
		// Thread.sleep(50L);
		// } catch (InterruptedException localInterruptedException) {
		// }
	}
	// project.refreshLocal(1, monitor);

}
