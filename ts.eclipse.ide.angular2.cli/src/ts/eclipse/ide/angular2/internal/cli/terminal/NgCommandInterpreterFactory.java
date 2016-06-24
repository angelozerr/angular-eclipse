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
 */
package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.util.List;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.terminal.interpreter.ICommandInterpreter;
import ts.eclipse.ide.terminal.interpreter.ICommandInterpreterFactory;

public class NgCommandInterpreterFactory implements ICommandInterpreterFactory {

	@Override
	public ICommandInterpreter create(List<String> parameters, String workingDir) {
		NgCommand command = getCommand(parameters);
		if (command == null) {
			return null;
		}
		switch (command) {
		case NEW:
			return new NgNewCommandInterpreter(parameters, workingDir);
		case INIT:
			return new NgInitCommandInterpreter(parameters, workingDir);
		case GENERATE:
			return new NgGenerateCommandInterpreter(parameters, workingDir);
		case SERVE:
			return new NgServeCommandInterpreter(parameters, workingDir);
		case BUILD:
			return new NgBuildCommandInterpreter(parameters, workingDir);
		default:
			return null;
		}
	}

	private NgCommand getCommand(List<String> parameters) {
		if (parameters.size() < 1) {
			return null;
		}
		return NgCommand.getCommand(parameters.get(0));
	}
}
