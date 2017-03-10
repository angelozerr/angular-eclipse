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

import ts.eclipse.ide.angular2.internal.cli.jobs.NgServeJob;
import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

/**
 * "ng serve" interpreter to open a WebBrowser when trace "NG Live Development
 * Server is running on http://localhost:4200. **" appears.
 *
 */
public class NgServeCommandInterpreter extends AbstractCommandInterpreter {

	private static final String SERVING_ON = "Server is running on ";

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
		int startIndex = line.indexOf(SERVING_ON);
		if (startIndex != -1) {
			// ** NG Live Development Server is running on http://localhost:4200. **
			startIndex += SERVING_ON.length();
			int endIndex = line.indexOf(".", startIndex);
			if (endIndex == -1) {
				endIndex = line.indexOf(" ", startIndex);
			}
			final String serverURL = line.substring(startIndex, endIndex != -1 ? endIndex : line.length())
					.trim();
			// Open a Web Browser with the given server URL
			new NgServeJob(serverURL).schedule();

		}
	}

}
