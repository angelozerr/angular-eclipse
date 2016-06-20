package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

public class NgServeCommandInterpreter extends AbstractCommandInterpreter {

	public NgServeCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
	}

	@Override
	public void execute(List<String> parameters, String workingDir) {
		// Do nothing, the open of web browser is done when trace "Serving on"
		// is found.
	}

	@Override
	public void onTrace(String line) {
		if (line.startsWith("Serving on")) {
			final String serverURL = line.substring("Serving on".length(), line.length()).trim();
			new UIJob("Start ng server") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
						IWebBrowser browser = browserSupport.createBrowser(
								IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, null,
								null, null);
						browser.openURL(new URL(serverURL));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return Status.OK_STATUS;
				}
			}.schedule();

		}
	}

}
