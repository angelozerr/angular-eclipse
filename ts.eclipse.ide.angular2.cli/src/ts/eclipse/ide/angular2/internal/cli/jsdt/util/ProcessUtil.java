/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package ts.eclipse.ide.angular2.internal.cli.jsdt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.jsdt.js.cli.core.CLICommand;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ProcessUtil {

	private ProcessUtil() {
	}

	public static boolean isWindows() {
		String OS = System.getProperty("os.name", "unknown"); //$NON-NLS-1$//$NON-NLS-2$
		return OS.toLowerCase().indexOf("win") > -1; //$NON-NLS-1$
	}
	
	/**
	 * Kills a process by its id.
	 * For windows it uses "taskkill" command, i.e.
	 * - Kill a process and its children processes forcefully:
	 * "taskkill /PID <pid> /T /F"
	 * 
	 * - Kill a process normally without its children processes:
	 * "taskkill /PID <pid>"
	 * 
	 * For Linux & Mac it uses "pkill" and "kill" command, i.e.
	 * - Kill a process and its children processes forcefully:
	 * "pkill -9 -P <pid>"
	 * 
	 * NOTE: The <pid> needs to be alive in order to kill it's 
	 * children processes.
	 * 
	 * - Kill a process normally without its children processes:
	 * "kill <pid>"
	 * 
	 * @param pid the process id to be kill.
	 * @param includeChildProcesses whether the process will be kill with it's 
	 * children processes.
	 * @param terminateProcessForcefully whether the process will forcefully kill.
	 * @return the exit value of the command. By convention, the value 0 indicates
	 * normal termination.
	 */
	public static int terminateProcessById(String pid, Boolean includeChildProcesses, Boolean terminateProcessForcefully){
		List<String> command = new ArrayList<String>();
		if(isWindows()){
			command.add("taskkill"); //$NON-NLS-1$
			command.add("/PID"); //$NON-NLS-1$
			command.add(pid);
			
			if(includeChildProcesses){
				command.add("/T"); //$NON-NLS-1$
			}
			
			if(terminateProcessForcefully){
				command.add("/F"); //$NON-NLS-1$
			}
		} else {			
			if(includeChildProcesses){
				command.add("pkill"); //$NON-NLS-1$
				if(terminateProcessForcefully){
					command.add("-9"); //$NON-NLS-1$
				}
				command.add("-P"); //$NON-NLS-1$
			} else {
				command.add("kill"); //$NON-NLS-1$
				if(terminateProcessForcefully){
					command.add("-9"); //$NON-NLS-1$
				}
			}
			
			command.add(pid);
		}
		
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		int exit = 1;
		try {
			exit = processBuilder.start().waitFor();
		} catch (InterruptedException | IOException e) {
			exit = 1;
			AngularCLIPlugin.logError(e);
		}
		return exit;
	}
	
	/**
	 * Returns the PID of the process which matches certain parameters depending on OS 
	 * 
	 * @param CLICommand
	 *            command that was used to launch the process
	 *            
	 * @param path
	 *            the absolute path of the location from where the process was started
	 *                     
	 * @return the pid matched or null if no match.
	 */
	public static String getPID(CLICommand command, String path) {
		return isWindows() ? getPIDonWindows(command.getCommandName())
				: getPIDonUnix(command.getToolName(), path);
	}
	
	/**
	 * Returns the PID of the node process with the matched CommandLine pattern 
	 * (command name e.g gulp serve, grunt connect etc). 
	 * On windows gulp / grunt processes are defined as node.js ones
	 * The next command, i.e. "wmic process where
	 * 'caption^=\"node.exe\" and CommandLine like \"<pattern>\"' get processid"
	 * 
	 * @param commandName
	 *            the name of command e.g. serve, connect
	 *            
	 * @return the first pid matched or null if no match.
	 */
	private static String getPIDonWindows(String commandName) {
		String pid = null;
		String wmicCommand = "wmic process where 'caption^=\"node.exe\" and CommandLine like \"%" +  commandName //$NON-NLS-1$
				+ "%\"' get processid"; //$NON-NLS-1$
		String[] command = new String[] { "cmd", "/c", //$NON-NLS-1$ //$NON-NLS-2$
				"for /f \"usebackq skip=1\" %F in (`" + wmicCommand + "`) do @echo %F" }; //$NON-NLS-1$ //$NON-NLS-2$
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		try {
			Process process = processBuilder.start();
			InputStream inputStream = process.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
			int exit = process.waitFor();
			if (exit == 0) {
				String line = buffer.readLine();
				if (line != null && !line.equals("ECHO is on.")) { //$NON-NLS-1$
					pid = line;
				}
			}
		} catch (IOException | InterruptedException e) {
			AngularCLIPlugin.logError(e);
		}
		return pid;
	}
	
	/**
	 * Returns the PID of the Node.js based tool (grunt / gulp) process with the matched path
	 *
	 * For Linux & Mac it uses the next command, i.e.
	 * ps axwww | grep -i <toolName>.* | grep -v grep | awk '{print $1}'
	 * 
	 * 
	 * @param toolName
	 *            the name of Node.js based tool e.g. grunt / gulp
	 * @param path
	 *            the absolute path of the location from where the process was started
	 *            
	 * @return the first pid matched or null if no match.
	 */
	private static String getPIDonUnix(String toolName, String path) {
		List<String> mathingPIDs = new ArrayList<String>();
		String[] command = new String[] { "/bin/sh", "-c", //$NON-NLS-1$ //$NON-NLS-2$
				"ps axwww | grep -i " + toolName + ".* | grep -v grep | awk '{print $1}'" }; //$NON-NLS-1$ //$NON-NLS-2$

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		try {
			Process process = processBuilder.start();
			InputStream inputStream = process.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
			int exit = process.waitFor();
			if (exit == 0) {
				String line;
				while ((line = buffer.readLine()) != null && !line.equals("ECHO is on.")) { //$NON-NLS-1$
					mathingPIDs.add(line);
				}
			}
		} catch (IOException | InterruptedException e) {
			AngularCLIPlugin.logError(e);
		}
		// if there is only one match - just return it, otherwise need to perform path match
		return (mathingPIDs.size() == 1) ?  mathingPIDs.get(0) : findPID(mathingPIDs, path); 
	}
	
	/*
	 * @return the first pid matched path or null if no match.
	 */
	private static String findPID(List<String> mathingPIDs, String path) {
		String pid = null;
		for (String p : mathingPIDs) {
			ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "pwdx", p }); //$NON-NLS-1$
			try {
				Process process = processBuilder.start();
				InputStream inputStream = process.getInputStream();
				BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
				int exit = process.waitFor();
				if (exit == 0) {
					String line = buffer.readLine();
					if (line != null && line.endsWith(path)) {
						pid = p;
						break;
					}
				}
			} catch (IOException | InterruptedException e) {
				AngularCLIPlugin.logError(e);
			}
		}
		return pid;
	}


}
