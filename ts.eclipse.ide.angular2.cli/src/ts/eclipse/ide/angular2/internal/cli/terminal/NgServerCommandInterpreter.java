package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.net.URL;
import java.util.List;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import ts.eclipse.ide.terminal.interpreter.ICommandInterpreter;

public class NgServerCommandInterpreter implements ICommandInterpreter {

	@Override
	public void execute(List<String> parameters, String workingDir) {
		// Do nothing, the open of web browser is done when trace "Serving on"
		// is found.
	}

	@Override
	public void addLine(String line) {
		if (line.startsWith("Serving on")) {
			String serverURL = line.substring("Serving on".length(), line.length()).trim();

			try {
				IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
				IWebBrowser browser = browserSupport.createBrowser(
						IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, null, null,
						null);
				browser.openURL(new URL(serverURL));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
