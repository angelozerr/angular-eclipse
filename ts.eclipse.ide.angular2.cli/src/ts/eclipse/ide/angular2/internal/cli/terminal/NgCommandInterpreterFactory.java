package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.util.List;

import ts.eclipse.ide.terminal.interpreter.ICommandInterpreter;
import ts.eclipse.ide.terminal.interpreter.ICommandInterpreterFactory;

public class NgCommandInterpreterFactory implements ICommandInterpreterFactory {

	private static final String NEW = "new";
	private static final String GENERATE = "generate";
	private static final String G = "g";
	private static final String SERVE = "serve";
	private static final String BUILD = "build";

	@Override
	public ICommandInterpreter create(List<String> parameters, String workingDir) {
		if (parameters.contains(NEW)) {
			return new NgNewCommandInterpreter(parameters, workingDir);
		} else if (parameters.contains(GENERATE) || parameters.contains(G)) {
			return new NgGenerateCommandInterpreter(parameters, workingDir);
		} else if (parameters.contains(SERVE)) {
			return new NgServeCommandInterpreter(parameters, workingDir);
		} else if (parameters.contains(BUILD)) {
			return new NgBuildCommandInterpreter(parameters, workingDir);
		}
		return null;
	}
}
