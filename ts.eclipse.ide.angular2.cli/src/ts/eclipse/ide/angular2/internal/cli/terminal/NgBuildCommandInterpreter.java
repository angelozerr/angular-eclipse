/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *  
 */
package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIProject;
import ts.eclipse.ide.angular2.internal.cli.json.AngularCLIJson;
import ts.eclipse.ide.core.utils.WorkbenchResourceUtil;
import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;
import ts.eclipse.ide.terminal.interpreter.UIInterpreterHelper;

/**
 * "ng build" interpreter to refresh the "dist" folder.
 *
 */
public class NgBuildCommandInterpreter extends AbstractCommandInterpreter {

	public NgBuildCommandInterpreter(List<String> parameters, String workingDir) {
		super(workingDir);
	}

	@Override
	public void execute(String newWorkingDir) {
		IContainer container = WorkbenchResourceUtil.findContainerFromWorkspace(getWorkingDir());
		if (container != null) {
			IProject project = container.getProject();
			new UIJob(AngularCLIMessages.NgBuildCommandInterpreter_jobName) {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						AngularCLIJson cliJson = AngularCLIProject.getAngularCLIProject(project).getAngularCLIJson();
						IFolder distFolder = project.getFolder(cliJson.getOutDir());
						distFolder.refreshLocal(IResource.DEPTH_INFINITE, monitor);
						if (distFolder.exists()) {
							// Select dist folder in the Project Explorer.
							UIInterpreterHelper.selectRevealInDefaultViews(distFolder);
						}
					} catch (CoreException e) {
						return new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID,
								AngularCLIMessages.NgBuildCommandInterpreter_error, e);
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}

	@Override
	public void onTrace(String trace) {
	}

}
