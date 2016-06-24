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

import org.eclipse.core.resources.ResourcesPlugin;

import ts.eclipse.ide.angular2.internal.cli.jobs.NgProjectJob;
import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

/**
 * Abstract class used to create an Angular2 project (ng new or ng init).
 *
 */
public abstract class AbstractProjectCommandInterpreter extends AbstractCommandInterpreter {

	public AbstractProjectCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
	}

	@Override
	public void execute(List<String> parameters, String workingDir) {
		final File projectDir = getProjectDir(parameters, workingDir);
		if (projectDir == null) {
			return;
		}
		// Refresh Eclipse project and open angular-cli.json
		NgProjectJob job = new NgProjectJob(projectDir);
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.schedule();
	}

	protected abstract File getProjectDir(List<String> parameters, String workingDir);
}
