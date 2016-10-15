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
package ts.eclipse.ide.angular2.internal.cli.jsdt;

import java.io.IOException;
import java.util.ArrayList;

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
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
//import org.eclipse.wst.jsdt.js.cli.Messages;
import org.eclipse.wst.jsdt.js.cli.core.CLICommand;
//import org.eclipse.wst.jsdt.js.cli.core.CLIResult;
import org.eclipse.wst.jsdt.js.cli.core.CLIStreamListener;

//import org.eclipse.wst.jsdt.js.cli.CLIPlugin;
import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
//import org.eclipse.wst.jsdt.js.cli.internal.util.ExternalProcessUtility;
import ts.eclipse.ide.angular2.internal.cli.jsdt.util.ExternalProcessUtility;
//import org.eclipse.wst.jsdt.js.cli.internal.util.ProcessUtil;
import ts.eclipse.ide.angular2.internal.cli.jsdt.util.ProcessUtil;

/**
 * Wrapper around CLI. Provides low level 
 * access to CLI.
 *
 *@author Gorkem Ercan
 *@author "Ilya Buziuk (ibuziuk)"
 *
 */
@SuppressWarnings("restriction")
public class CLI {
	
	//Store locks for the projects.
	//private static Map<String, Lock> projectLock = Collections.synchronizedMap(new HashMap<String,Lock>());
	private IProject project;
	private IPath workingDir;
	private CLICommand command;

		
	public CLI( IProject project, IPath workingDir, CLICommand command) {
		// AZERR: remove this condition, because I don't want to refresh project when command is finished
//		if (project == null) {
//			throw new IllegalArgumentException(Messages.Error_NoProjectSpecified);
//		}
		
		this.project = project;
		// Use the project's location as the working directory if dir is null
		this.workingDir = (workingDir == null) ? project.getLocation() : workingDir;
		
		this.command = command;
	}
	
	public CLIResult execute(IProgressMonitor monitor) throws CoreException {
		return execute(new CLIStreamListener(), monitor);
	}

	// AZERR : defines an execute method to customize streamListener
	public CLIResult execute(CLIStreamListener streamListener, IProgressMonitor monitor) throws CoreException {
		return execute(streamListener, true, monitor);
	}
	
	public CLIResult execute(CLIStreamListener streamListener, boolean waitForTerminate, IProgressMonitor monitor) throws CoreException {	
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		IProcess process = startShell(streamListener, monitor, getLaunchConfiguration(command));
		sendCLICommand(process, command, waitForTerminate, monitor);
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
			//CLIPlugin.logError(e);
		}
		return null;
	}
	
	protected void sendCLICommand(final IProcess process, final CLICommand command,
			boolean waitForTerminate, final IProgressMonitor monitor) throws CoreException {
		try {
			DebugPlugin.getDefault().addDebugEventListener(processTerminateListener);
			final IStreamsProxy streamProxy = process.getStreamsProxy();
			streamProxy.write(command.toString());
			if (waitForTerminate) { 
				while (!process.isTerminated()) {
					//exit the shell after sending the command
					streamProxy.write("exit\n"); //$NON-NLS-1$
					if (monitor.isCanceled()) {
						process.terminate();
						break;
					}
					Thread.sleep(100);
				}
			}
		} catch (IOException | InterruptedException e) {
			throw new CoreException(new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID, /*Messages.Error_FatalInvokingCLI*/ "Fatal error invoking CLI", e));
		} finally {
			if (waitForTerminate) {
				// Something went wrong during termination - need to kill process
				killProcessIfNeeded(process, command);
			}
		}
	}

	private void killProcessIfNeeded(final IProcess process, final CLICommand command) throws DebugException {
		if (process.getExitValue() != 0) {
			String pid = ProcessUtil.getPID(command, workingDir.toOSString());
			if (pid != null) {
				ProcessUtil.terminateProcessById(pid, false, true);						
			}
		}
	}
	
	//public visibility to support testing
	public IProcess startShell(final IStreamListener listener, final IProgressMonitor monitor, 
			final ILaunchConfiguration launchConfiguration) throws CoreException{
		ArrayList<String> commandList = new ArrayList<String>();
		if(ProcessUtil.isWindows()){
			commandList.add("cmd"); //$NON-NLS-1$
		}else{
			commandList.add("/bin/bash"); //$NON-NLS-1$
			commandList.add("-l"); //$NON-NLS-1$
		}
		ExternalProcessUtility ep = new ExternalProcessUtility();
		IProcess process = ep.exec(commandList.toArray(new String[commandList.size()]), workingDir.toFile(), 
				monitor, null, launchConfiguration);
		 if(listener != null){
			 process.getStreamsProxy().getOutputStreamMonitor().addListener(listener);
			 process.getStreamsProxy().getErrorStreamMonitor().addListener(listener);
		 }
		 return process;
	}
	
	// AZERR: remove this method because it is defined in ProcessUtil
//	private boolean isWindows(){
//		String OS = System.getProperty("os.name","unknown");  //$NON-NLS-1$//$NON-NLS-2$
//		return OS.toLowerCase().indexOf("win")>-1; //$NON-NLS-1$
//	}
	
	
	// AZERR: remove this method because it seems that it is not used?
//	private Lock projectLock(){
//		final String projectName = project.getProject().getName();
//		Lock l = projectLock.get(project.getProject().getName());
//		if(l == null){
//			// Use reentrant locks
//			l = new ReentrantLock();
//			projectLock.put(projectName, l);
//		}
//		return l;
//	}
	
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
						IProcess process = (IProcess) source;
						ILaunch launch = process.getLaunch();
						if (launch != null) {
							ILaunchConfiguration lc = launch.getLaunchConfiguration();														
							// Something went wrong during termination - need to kill process
							try {
								killProcessIfNeeded(process, command);
							} catch (DebugException e) {
								AngularCLIPlugin.logError(e);
							}	
							
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

}
