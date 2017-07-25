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

import java.net.URL;

import ts.eclipse.ide.angular2.internal.cli.jobs.NgServeJob;
import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

/**
 * "ng serve" interpreter to open a WebBrowser when trace "NG Live Development
 * Server is running on http://localhost:4200. **" appears.
 *
 */
public class NgServeCommandInterpreter extends AbstractCommandInterpreter {

	private static final String HTTP = "http";

	private boolean open;						// Has the browser already been opened?

	public NgServeCommandInterpreter(String workingDir) {
		super(workingDir);
	}

	@Override
	public void execute(String newWorkingDir) {
		// Do nothing, the open of web browser is done when trace "Serving on
		// http://localhost:4200/"
		// is found.
	}

	@Override
	public void onTrace(String line) {
		// track
		// ** NG Live Development Server is running on http://localhost:4200. **
		// ** NG Live Development Server is listening on localhost:4200, open your browser on http://localhost:4200 **
		if (!open) {
			int startIndex = line.indexOf(HTTP);
			if (startIndex != -1) {
				int endIndex = line.indexOf(".", startIndex);
				if (endIndex == -1) {
					endIndex = line.indexOf(" ", startIndex);
				}
				final String serverURL = line.substring(startIndex, endIndex != -1 ? endIndex : line.length()).trim();

				// Basic URL-Validation
				URL u;
				try {
					u = new URL(serverURL);		// this would check for the protocol
					u.toURI(); 					// extra checking required for validation of URI

					// Open a Web Browser with the given server URL
					new NgServeJob(serverURL).schedule();
					open = true;
				} catch (Exception ignore) {
				}
			}
		}
	}

}
