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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;

import ts.eclipse.ide.angular2.cli.launch.AngularCliLaunchConstants;

/**
 * Launch configuration which consumes angular-cli to generate project, component, etc.
 *
 */
public class AngularCliLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String arg1, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		String workingDir = configuration.getAttribute(AngularCliLaunchConstants.WORKING_DIR, (String) null);
		String operation = configuration.getAttribute(AngularCliLaunchConstants.OPERATION, (String) null);
		String operationName = configuration.getAttribute(AngularCliLaunchConstants.OPERATION_NAME, (String) null);
		if (monitor.isCanceled()) {
			return;
		}

		List<String> cmds = new ArrayList<String>();
		
//		IProject cli = ResourcesPlugin.getWorkspace().getRoot().getProject("test-angular-cli");
//		cmds.add("node");
//		cmds.add(new File(cli.getLocation().toFile(), "node_modules/angular-cli/bin/ng").toString());
//		cmds.add(operation);
//		if (operationName != null) {
//			cmds.add(operationName);
//		}

		Process p = DebugPlugin.exec(cmds.toArray(new String[0]), new File(workingDir), null);
		IProcess process = null;

		Map<String, String> processAttributes = new HashMap<String, String>();
		processAttributes.put(IProcess.ATTR_PROCESS_TYPE, "ng");

		if (p != null) {
			monitor.beginTask("ng...", -1);
			process = DebugPlugin.newProcess(launch, p, "TODO", processAttributes);
		}

		AngularCliStreamListener reporter = new AngularCliStreamListener(null);
		process.getStreamsProxy().getOutputStreamMonitor().addListener(reporter);

		// if (!reporter.isWatch()) {
		while (!process.isTerminated()) {
			try {
				if (monitor.isCanceled()) {
					process.terminate();
					break;
				}
				Thread.sleep(50L);
			} catch (InterruptedException localInterruptedException) {
			}
		}
		// project.refreshLocal(1, monitor);
		// reporter.onCompilationCompleteWatchingForFileChanges();
		// }
		// } catch (TypeScriptException e) {
		// throw new CoreException(new Status(IStatus.ERROR,
		// TypeScriptCorePlugin.PLUGIN_ID, "Error while tsc", e));
		// }
	}

}
