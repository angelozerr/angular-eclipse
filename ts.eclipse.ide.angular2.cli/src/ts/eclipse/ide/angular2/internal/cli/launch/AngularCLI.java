/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package ts.eclipse.ide.angular2.internal.cli.launch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.externaltools.internal.IExternalToolConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;

/**
 * Wrapper around CLI. Provides low level 
 * access to CLI.
 *
 *@author Gorkem Ercan
 *@author "Ilya Buziuk (ibuziuk)"
 *
 */
@SuppressWarnings("restriction")
public class AngularCLI {
	
	//Store locks for the projects.
	private static Map<String, Lock> projectLock = Collections.synchronizedMap(new HashMap<String,Lock>());
	private IProject project;
	private IPath workingDir;
	private CLICommand command;

		
	public AngularCLI( IProject project, IPath workingDir, CLICommand command) {
//		if (project == null) {
//			throw new IllegalArgumentException(Messages.Error_NoProjectSpecified);
//		}
		
		this.project = project;
		// Use the project's location as the working directory if dir is null
		this.workingDir = (workingDir == null) ? project.getLocation() : workingDir;
		
		this.command = command;
	}
	
	public CLIResult execute(IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		final AngularCLIStreamListener streamListener = new AngularCLIStreamListener(project); // choose your stream listner
		IProcess process = startShell(streamListener, monitor, getLaunchConfiguration(command));
		sendCLICommand(process, command, monitor);
		CLIResult result = new CLIResult(streamListener.getErrorMessage(), streamListener.getMessage());
		throwExceptionIfError(result);
		return result;
	}
	
	private ILaunchConfiguration getLaunchConfiguration(CLICommand command) {	
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(IExternalToolConstants.ID_PROGRAM_LAUNCH_CONFIGURATION_TYPE);
		try {
			ILaunchConfiguration cfg = type.newInstance(null, command.getToolName());
			ILaunchConfigurationWorkingCopy wc = cfg.getWorkingCopy();
			wc.setAttribute(IProcess.ATTR_PROCESS_LABEL, command.getToolName() + " " + command.getCommandName()); //$NON-NLS-1$
			wc.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING, "UTF-8"); //$NON-NLS-1$
			cfg = wc.doSave();
			return cfg;
		} catch (CoreException e) {
			AngularCLIPlugin.logError(e);
		}
		return null;
	}
	
	protected void sendCLICommand(final IProcess process, final CLICommand command,
			final IProgressMonitor monitor) throws CoreException {
		try {
			DebugPlugin.getDefault().addDebugEventListener(processTerminateListener);
			final IStreamsProxy streamProxy = process.getStreamsProxy();
			streamProxy.write(command.toString());
			while (!process.isTerminated()) {
				//exit the shell after sending the command
				streamProxy.write("exit\n"); //$NON-NLS-1$
				if (monitor.isCanceled()) {
					process.terminate();
					break;
				}
				Thread.sleep(100);
			}
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID, AngularCLIMessages.Error_FatalInvokingCLI, e));
		} catch (InterruptedException e) {
			throw new CoreException(new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID, AngularCLIMessages.Error_FatalInvokingCLI, e));
		} finally {
			// Something went wrong during termination - need to kill process
			if (process.getExitValue() != 0) {
				String pid = ProcessUtil.getPID(command, workingDir.toOSString());
				if (pid != null) {
					ProcessUtil.terminateProcessById(pid, false, true);						
				}
			}	
		}
	}
	
	//public visibility to support testing
	public IProcess startShell(final IStreamListener listener, final IProgressMonitor monitor, 
			final ILaunchConfiguration launchConfiguration) throws CoreException{
		ArrayList<String> commandList = new ArrayList<String>();
		if(isWindows()){
			commandList.add("cmd"); //$NON-NLS-1$
		}else{
			commandList.add("/bin/bash"); //$NON-NLS-1$
			commandList.add("-l"); //$NON-NLS-1$
		}
		//ExternalProcessUtility ep = new ExternalProcessUtility();
		IProcess process = exec(commandList.toArray(new String[commandList.size()]), workingDir.toFile(), 
				monitor, null, launchConfiguration);
		 if(listener != null){
			 process.getStreamsProxy().getOutputStreamMonitor().addListener(listener);
			 process.getStreamsProxy().getErrorStreamMonitor().addListener(listener);
		 }
		 return process;
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
	
	private void checkCommands(String[] command) {
		if(command == null || command.length <1 ){
			throw new IllegalArgumentException("Empty commands array"); //$NON-NLS-1$
		}
	}
	
	private void checkWorkingDirectory(File workingDirectory) {
		if(workingDirectory != null && !workingDirectory.isDirectory()){
			throw new IllegalArgumentException(workingDirectory.toString()+ " is not a valid directory"); //$NON-NLS-1$
		}
	}

	
	private boolean isWindows(){
		String OS = System.getProperty("os.name","unknown");  //$NON-NLS-1$//$NON-NLS-2$
		return OS.toLowerCase().indexOf("win")>-1; //$NON-NLS-1$
	}
	
	
	private Lock projectLock(){
		final String projectName = project.getProject().getName();
		Lock l = projectLock.get(project.getProject().getName());
		if(l == null){
			// Use reentrant locks
			l = new ReentrantLock();
			projectLock.put(projectName, l);
		}
		return l;
	}
	
	protected void throwExceptionIfError(CLIResult result) throws CoreException {
		if(result.hasError()){
			throw result.asCoreException();
		}
	}
	
	IDebugEventSetListener processTerminateListener = new IDebugEventSetListener() {

		@Override
		public void handleDebugEvents(DebugEvent[] events) {
			for (DebugEvent event : events) {
				if (event.getKind() == DebugEvent.TERMINATE) {
					Object source = event.getSource();
					if (source instanceof IProcess) {
						ILaunch launch = ((IProcess) source).getLaunch();
						if (launch != null) {
							ILaunchConfiguration lc = launch.getLaunchConfiguration();
							// TODO: need to write smarter conditions
							if (lc != null && project != null && project.exists()) {
								try {
									project.refreshLocal(IResource.DEPTH_INFINITE, null);
								} catch (CoreException e) {
									AngularCLIPlugin.logError(e);
								} finally {
									DebugPlugin.getDefault().removeDebugEventListener(this);
								}
							}
						}
					}
				}
			}
		}
	};

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
