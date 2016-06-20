package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.util.List;

import ts.eclipse.ide.terminal.interpreter.ICommandInterpreter;
import ts.eclipse.ide.terminal.interpreter.ICommandInterpreterFactory;

public class NgCommandInterpreterFactory implements ICommandInterpreterFactory {

	private static final ICommandInterpreter NG_NEW_INTERPRETER = new NgNewCommandInterpreter();
	private static final ICommandInterpreter NG_SERVER_INTERPRETER = new NgServerCommandInterpreter();

	@Override
	public ICommandInterpreter create(List<String> parameters) {
		if (parameters.contains("new")) {
			return NG_NEW_INTERPRETER;
		} else if (parameters.contains("server")) {
			return NG_SERVER_INTERPRETER;
		}
		return null;
	}
}
