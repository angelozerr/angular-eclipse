/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package ts.eclipse.ide.angular2.internal.cli.jsdt.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
//import org.eclipse.wst.jsdt.js.cli.CLIPlugin;
import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;

/**
 * Utilities for calling and processing the output from external executables.
 * 
 * @author Gorkem Ercan
 *
 */
public class ExternalProcessUtility {

	/**
	 * Executes the given commands asynchronously.
	 *
	 * <p>
	 * If the workingDirectory is null, the current directory for process is used.
	 * </p>
	 * @param command the command line can not be null or empty
	 * @param workingDirectory working directory for the executed command can be null
	 * @param outStreamListener  listener for output, can be null
	 * @param errorStreamListene listener for error output, can be null
	 * @param envp environment variables to set in the process can be null
	 * @throws CoreException if execution fails
	 * @throws IllegalArgumentException 
	 *        <ul>
	 *        <li>If command is null or empty</li>
	 *        <li>If specified workingDirectory does not exist or not a directory</li>
	 *        </ul> 
	 */
	public void execAsync (String [] command, File workingDirectory, 
			IStreamListener outStreamListener,
			IStreamListener errorStreamListener, String[] envp) throws CoreException{
		AngularCLIPlugin.logInfo("Async Execute command line: " + Arrays.toString(command)); //$NON-NLS-1$
		IProcess prcs = exec(command, workingDirectory, new NullProgressMonitor(), envp, null);
		setTracing(command, outStreamListener, errorStreamListener, prcs);
	}
	
	/**
	 * Convenience method to specify command line as a string for {@link #execAsync(String[], File, IStreamListener, IStreamListener, String[])}
	 */
	public void execAsync ( String commandLine, File workingDirectory, 
			IStreamListener outStreamListener, 
			IStreamListener errorStreamListener, String[] envp) throws CoreException{
		checkCommandLine(commandLine);
		this.execAsync(DebugPlugin.parseArguments(commandLine),
				workingDirectory, outStreamListener, errorStreamListener, envp);
	}

	/**
	 * Executes the given commands synchronously.
	 *
	 * <p>
	 * If the workingDirectory is null, the current directory for process is used.
	 * </p>
	 * @param command the command line can not be null or empty
	 * @param workingDirectory working directory for the executed command, can be null
	 * @param outStreamListener  listener for output, can be null
	 * @param errorStreamListene listener for error output, can be null
	 * @param envp environment variables to set in the process, can be null
	 * @param launchConfiguration the launch to add as part of this call, can be null
	 * @return the exit code for the process
	 * @throws CoreException if the execution fails
	 */
	public int execSync ( String[] command, File workingDirectory, 
			IStreamListener outStreamListener, 
			IStreamListener errorStreamListener, IProgressMonitor monitor, 
			String[] envp, ILaunchConfiguration launchConfiguration) throws CoreException{
		if(monitor == null){
			monitor = new NullProgressMonitor();
		}
		AngularCLIPlugin.logInfo("Sync Execute command line: " + Arrays.toString(command)); //$NON-NLS-1$
		IProcess prcs = exec(command, workingDirectory, monitor, envp, launchConfiguration);
		if(prcs == null ){
			return 0;
		}
		setTracing(command, outStreamListener, errorStreamListener, prcs);
		
		while (!prcs.isTerminated()) {
			try {
				if (monitor.isCanceled()) {
					prcs.terminate();
					break;
				}
				Thread.sleep(50);
			} catch (InterruptedException e) {
				AngularCLIPlugin.logError(e, "Exception waiting for process to terminate"); //$NON-NLS-1$
			}
		}
		return prcs.getExitValue();
	}
	
	/**
	 * Executes the given command and returns a handle to the 
	 * process. Should only be used if access to {@link IProcess}
	 * instance is desired. 
	 * 
	 * @param command
	 * @param workingDirectory
	 * @param monitor
	 * @param envp
	 * @param launchConfiguration
	 * @return the process
	 * @throws CoreException
	 */
	public IProcess exec(String[] command, File workingDirectory, 
			IProgressMonitor monitor, String[] envp, 
			ILaunchConfiguration launchConfiguration ) throws CoreException{
		
		checkCommands(command);
		checkWorkingDirectory(workingDirectory);
		if(monitor == null ){
			monitor = new NullProgressMonitor();
		}
		if (envp == null && launchConfiguration != null ){
			envp = DebugPlugin.getDefault().getLaunchManager().getEnvironment(launchConfiguration);
		}
		if (monitor.isCanceled()) {
			return null;
		}
		Process process = DebugPlugin.exec(command, workingDirectory, envp);
		Map<String, String> processAttributes = generateProcessAttributes(command, launchConfiguration);
		Launch launch = new Launch(launchConfiguration, "run", null); //$NON-NLS-1$
		IProcess prcs = DebugPlugin.newProcess(launch, process, command[0], processAttributes);
		DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
	
		return prcs;
	}
	
	/**
	 * Convenience method to specify command line as a String 
	 * for {@link #execSync(String[], File, IStreamListener, IStreamListener, IProgressMonitor, String[], ILaunchConfiguration)} 
	 */
	public int execSync ( String commandLine, File workingDirectory, 
			IStreamListener outStreamListener, 
			IStreamListener errorStreamListener, IProgressMonitor monitor, String[] envp, ILaunchConfiguration launchConfiguration) throws CoreException{
		String[] cmd = DebugPlugin.parseArguments(commandLine);
		return this.execSync(cmd, workingDirectory, outStreamListener, errorStreamListener, monitor, envp, launchConfiguration);
	}	

	private Map<String, String> generateProcessAttributes(String[] command, ILaunchConfiguration launchConfiguration)
			throws CoreException {
		Map<String, String> processAttributes = new HashMap<String, String>();
		processAttributes.put(IProcess.ATTR_PROCESS_TYPE, command[0]);
		processAttributes.put(IProcess.ATTR_CMDLINE, generateCommandLine(command));
		if(launchConfiguration != null ){
			processAttributes.put(IProcess.ATTR_PROCESS_LABEL,
					launchConfiguration.getAttribute(IProcess.ATTR_PROCESS_LABEL, command[0]));
		}
		return processAttributes;
	}

	
	private void checkWorkingDirectory(File workingDirectory) {
		if(workingDirectory != null && !workingDirectory.isDirectory()){
			throw new IllegalArgumentException(workingDirectory.toString()+ " is not a valid directory"); //$NON-NLS-1$
		}
	}

	private void setTracing(String[] command, IStreamListener outStreamListener, IStreamListener errorStreamListener,
			IProcess prcs) {

// TODO: Handle Debug later
//		if(HybridCore.DEBUG){
//			HybridCore.trace("Creating TracingStreamListeners for " + Arrays.toString(command));
//			outStreamListener = new TracingStreamListener(outStreamListener);
//			errorStreamListener = new TracingStreamListener(outStreamListener);
//		}
		
		if( outStreamListener != null ){
			prcs.getStreamsProxy().getOutputStreamMonitor().addListener(outStreamListener);
		}

		if( errorStreamListener != null ){
			prcs.getStreamsProxy().getErrorStreamMonitor().addListener(errorStreamListener);
		}
	}	

	private void checkCommandLine(String commandLine) {
		if(commandLine == null || commandLine.isEmpty()){
			throw new IllegalArgumentException("Missing command line"); //$NON-NLS-1$
		}
	}
	
	private void checkCommands(String[] command) {
		if(command == null || command.length <1 ){
			throw new IllegalArgumentException("Empty commands array"); //$NON-NLS-1$
		}
	}
	
	private String generateCommandLine(final String[] commandLine) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < commandLine.length; i++) {
			buf.append(' ');
			char[] characters = commandLine[i].toCharArray();
			StringBuffer command = new StringBuffer();
			boolean containsSpace = false;
			for (int j = 0; j < characters.length; j++) {
				char character = characters[j];
				if (character == '\"') {
					command.append('\\');
				} else if (character == ' ') {
					containsSpace = true;
				}
				command.append(character);
			}
			if (containsSpace) {
				buf.append('\"');
				buf.append(command);
				buf.append('\"');
			} else {
				buf.append(command);
			}
		}
		return buf.toString();
	}
	
}
