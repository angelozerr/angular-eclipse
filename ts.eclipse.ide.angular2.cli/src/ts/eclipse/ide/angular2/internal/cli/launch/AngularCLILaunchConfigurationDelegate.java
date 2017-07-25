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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.externaltools.internal.launchConfigurations.ExternalToolsCoreUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.tm.terminal.view.core.interfaces.ITerminalService;
import org.eclipse.tm.terminal.view.core.interfaces.constants.ITerminalsConnectorConstants;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.cli.utils.CLIProcessHelper;
import ts.eclipse.ide.core.utils.WorkbenchResourceUtil;
import ts.eclipse.ide.terminal.interpreter.CommandTerminalService;
import ts.eclipse.ide.terminal.interpreter.EnvPath;
import ts.utils.FileUtils;
import ts.utils.StringUtils;

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
		boolean executeNgWithFile = configuration.getAttribute(AngularCLILaunchConstants.EXECUTE_NG_WITH_FILE,
				(Boolean) false);
		String nodeFilePath = configuration.getAttribute(AngularCLILaunchConstants.NODE_FILE_PATH, (String) null);
		IPath workingDir = ExternalToolsCoreUtil.getWorkingDirectory(configuration);
		String operation = configuration.getAttribute(AngularCLILaunchConstants.OPERATION, (String) null);
		String[] options = getOptions(
				configuration.getAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS, (String) null));
		if (monitor.isCanceled()) {
			return;
		}
		openWithTerminal(ngFilePath, executeNgWithFile, nodeFilePath, workingDir, operation, options, monitor);
	}

	private String[] getOptions(String options) {
		if (options == null) {
			return StringUtils.EMPTY_STRING;
		}
		return options.split(" ");
	}

	private void openWithTerminal(String ngFilePath, boolean executeNgWithFile, String nodeFilePath, IPath workingDir,
			String operation, String[] options, IProgressMonitor monitor) throws CoreException {

		// Prepare terminal properties
		String terminalId = getTerminalId(workingDir, operation);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ITerminalsConnectorConstants.PROP_TITLE, terminalId);
		properties.put(ITerminalsConnectorConstants.PROP_ENCODING, StandardCharsets.UTF_8.name());
		properties.put(ITerminalsConnectorConstants.PROP_PROCESS_WORKING_DIR, workingDir.toOSString());
		properties.put(ITerminalsConnectorConstants.PROP_DELEGATE_ID,
				"ts.eclipse.ide.terminal.interpreter.LocalInterpreterLauncherDelegate");
		properties.put(ITerminalsConnectorConstants.PROP_TERMINAL_CONNECTOR_ID,
				"org.eclipse.tm.terminal.connector.local.LocalConnector");

		// Create ng command
		StringBuilder command = new StringBuilder(getNg(ngFilePath, executeNgWithFile));
		command.append(" ");
		command.append(operation);
		for (int i = 0; i < options.length; i++) {
			command.append(" ");
			command.append(options[i]);
		}

		// Prepare environnement Path:
		// - add nodejs directory
		// - add ng directories in the env PATH.
		nodeFilePath = getDirPath(nodeFilePath);
		EnvPath.insertToEnvPath(properties, nodeFilePath, ngFilePath);

		// Create the done callback object
		ITerminalService.Done done = new ITerminalService.Done() {
			public void done(IStatus done) {
				// Place any post processing here
			}
		};

		// Open terminal and execute ng command.
		CommandTerminalService.getInstance().executeCommand(command.toString(), terminalId, properties, done);
	}

	private String getNg(String ngFilePath, boolean executeNgWithFile) {
		if (executeNgWithFile) {
			StringBuilder ng = new StringBuilder(ngFilePath);
			if ((!ngFilePath.endsWith("/") || ngFilePath.endsWith("\\"))) {
				if (ngFilePath.contains("/")) {
					ng.append("/");
				} else if (ngFilePath.contains("\\")) {
					ng.append("\\");
				} else {
					ng.append(File.separatorChar);
				}
			}
			ng.append(CLIProcessHelper.getNgFileName());
			return ng.toString();
		}
		return CLIProcessHelper.NG_FILENAME;
	}

	private String getTerminalId(IPath workingDir, String operation) {
		IContainer container = WorkbenchResourceUtil.findContainerFromWorkspace(workingDir);

		StringBuilder id = new StringBuilder("@angular/cli - [");
		if (container != null && container.getType() != IResource.ROOT) {
			id.append(container.getProject().getName());
		} else {
			id.append(workingDir.lastSegment());
		}
		if (isOpenNewTerminal(operation)) {
			id.append(" - (");
			id.append(operation);
			id.append(")");
		}
		id.append("]");
		return id.toString();
	}

	private boolean isOpenNewTerminal(String operation) {
		NgCommand ngCommand = NgCommand.getCommand(operation);
		if (ngCommand == null) {
			return false;
		}
		return NgCommand.SERVE.equals(ngCommand);
	}

	/**
	 * The env Path works with directory and not file.
	 * 
	 * @param fileOrDir
	 * @return
	 */
	private String getDirPath(String fileOrDir) {
		if (fileOrDir == null) {
			return fileOrDir;
		}
		File file = new File(fileOrDir);
		if (file.exists() && file.isFile()) {
			return FileUtils.getPath(file.getParentFile());
		}
		return fileOrDir;
	}
}
