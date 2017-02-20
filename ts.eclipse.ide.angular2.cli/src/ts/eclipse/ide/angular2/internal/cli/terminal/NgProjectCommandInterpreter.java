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
 *  some code copied/pasted from org.eclipse.ui.internal.wizards.datatransfer.SmartImportJob
 */
package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;

import ts.eclipse.ide.angular2.internal.cli.jobs.NgProjectJob;
import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

/**
 * (ng new or ng init) interpreter to create an Eclipse project at the end of
 * the process and open the generated angular-cli.json file.
 *
 */
public class NgProjectCommandInterpreter extends AbstractCommandInterpreter {

	private final File projectDir;

	public NgProjectCommandInterpreter(File projectDir, String workingDir) {
		super(workingDir);
		this.projectDir = projectDir;
	}

	@Override
	public void execute(String newWorkingDir) {
		// Refresh Eclipse project and open angular-cli.json
		NgProjectJob job = new NgProjectJob(projectDir);
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.schedule();
	}
}
