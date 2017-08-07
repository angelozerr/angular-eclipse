package ts.eclipse.ide.angular2.core.html;

import ts.TypeScriptException;

public class NgBindingTypeException extends TypeScriptException {

	private final int severity;

	public NgBindingTypeException(String message, int severity) {
		super(message);
		this.severity = severity;
	}

	public int getSeverity() {
		return severity;
	}

}
