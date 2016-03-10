package ts.eclipse.ide.angular2.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class Angular2Project {

	public static final String DEFAULT_START_SYMBOL = "{{";
	public static final String DEFAULT_END_SYMBOL = "}}";

	public static boolean hasAngular2Nature(IProject project) {
		// TODO: implement that
		return true;
	}

	public static Angular2Project getAngular2Project(IProject project) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStartSymbol() {
		return DEFAULT_START_SYMBOL;
	}

	public String getEndSymbol() {
		return DEFAULT_END_SYMBOL;
	}

}
