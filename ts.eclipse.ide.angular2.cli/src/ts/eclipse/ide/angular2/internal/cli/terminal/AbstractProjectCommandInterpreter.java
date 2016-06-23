/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *  
 *  some code copied/pasted from org.eclipse.ui.internal.wizards.datatransfer.SmartImportJob
 */
package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIUtils;
import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

/**
 * Abstract class used to create an Angular2 project (ng new or ng init).
 *
 */
public abstract class AbstractProjectCommandInterpreter extends AbstractCommandInterpreter {

	private static final String ANGULAR_CLI_JSON = "angular-cli.json";

	private IWorkspaceRoot workspaceRoot;

	public AbstractProjectCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
		workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	}

	@Override
	public void execute(List<String> parameters, String workingDir) {
		final File projectDir = getProjectDir(parameters, workingDir);
		if (projectDir == null) {
			return;
		}
		UIJob job = new UIJob(AngularCLIMessages.AbstractProjectCommandInterpreter_jobName) {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				return AngularCLIUtils.refreshProjectAndOpenAngularCLIJson(projectDir, monitor);
			}
		};
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.schedule();

	}

	protected abstract File getProjectDir(List<String> parameters, String workingDir);
}
