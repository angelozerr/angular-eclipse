package ts.eclipse.ide.angular2.internal.cli.launch;

import java.util.ArrayList;
import java.util.Collection;

public class NgGenerateCLIStreamListener extends ExtendedCLIStreamListener {

	private static final String CREATE = "create";

	private final Collection<String> fileNames;

	public NgGenerateCLIStreamListener() {
		this.fileNames = new ArrayList<String>();
	}

	public Collection<String> getFileNames() {
		return fileNames;
	}

	@Override
	protected void appendLine(String line) {
		super.appendLine(line);
		if (line.startsWith(CREATE)) {
			fileNames.add(line.substring(line.indexOf(CREATE) + CREATE.length(), line.length()).trim());
		}
	}
}
