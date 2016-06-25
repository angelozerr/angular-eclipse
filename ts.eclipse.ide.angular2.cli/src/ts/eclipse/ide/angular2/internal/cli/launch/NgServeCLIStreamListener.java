package ts.eclipse.ide.angular2.internal.cli.launch;

import java.util.ArrayList;
import java.util.Collection;

import ts.eclipse.ide.angular2.internal.cli.jobs.NgServeJob;

public class NgServeCLIStreamListener extends ExtendedCLIStreamListener {

	private static final String SERVING_ON = "Serving on";

	private final Collection<String> fileNames;

	public NgServeCLIStreamListener() {
		this.fileNames = new ArrayList<String>();
	}

	public Collection<String> getFileNames() {
		return fileNames;
	}

	@Override
	protected void appendLine(String line) {
		super.appendLine(line);
		if (line.startsWith(SERVING_ON)) {
			final String serverURL = line.substring(SERVING_ON.length(), line.length()).trim();
			// Open a Web Browser with the given server URL
			new NgServeJob(serverURL).schedule();
		}

	}
}
