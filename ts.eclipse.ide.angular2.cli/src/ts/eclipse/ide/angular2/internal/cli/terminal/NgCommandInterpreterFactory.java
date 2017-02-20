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

import java.io.File;
import java.util.List;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.terminal.interpreter.ICommandInterpreter;
import ts.eclipse.ide.terminal.interpreter.ICommandInterpreterFactory;

/**
 * Angular command interpeter factory.
 *
 */
public class NgCommandInterpreterFactory implements ICommandInterpreterFactory {

	@Override
	public ICommandInterpreter create(List<String> parameters, String workingDir) {
		NgCommand command = getCommand(parameters);
		if (command == null) {
			return null;
		}
		File projectDir = null;
		switch (command) {
		case NEW:
			projectDir = getNgNewProjectDir(parameters, workingDir);
			if (projectDir == null) {
				return null;
			}
			return new NgProjectCommandInterpreter(projectDir, workingDir);
		case INIT:
			projectDir = new File(workingDir);
			return new NgProjectCommandInterpreter(projectDir, workingDir);
		case GENERATE:
			String blueprint = getBluePrint(parameters);
			if (blueprint == null) {
				return null;
			}
			return new NgGenerateCommandInterpreter(blueprint, workingDir);
		case SERVE:
			return new NgServeCommandInterpreter(workingDir);
		case BUILD:
			return new NgBuildCommandInterpreter(parameters, workingDir);
		default:
			return null;
		}
	}

	private String getBluePrint(List<String> parameters) {
		if (parameters.size() < 2) {
			return null;
		}
		return parameters.get(1);
	}

	private NgCommand getCommand(List<String> parameters) {
		if (parameters.size() < 1) {
			return null;
		}
		return NgCommand.getCommand(parameters.get(0));
	}

	private File getNgNewProjectDir(List<String> parameters, String workingDir) {
		if (parameters.size() < 2) {
			return null;
		}
		String projectName = parameters.get(1);
		return new File(workingDir, projectName);
	}
}
