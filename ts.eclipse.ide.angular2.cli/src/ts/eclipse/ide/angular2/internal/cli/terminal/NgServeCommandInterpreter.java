/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
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

import java.util.List;

import ts.eclipse.ide.angular2.internal.cli.jobs.NgServeJob;
import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

/**
 * "ng serve" interpreter to open a WebBrowser when trace "Serving on
 * http://localhost:4200/" appears.
 *
 */
public class NgServeCommandInterpreter extends AbstractCommandInterpreter {

	private static final String SERVING_ON = "Serving on";

	public NgServeCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
	}

	@Override
	public void execute(List<String> parameters, String workingDir) {
		// Do nothing, the open of web browser is done when trace "Serving on
		// http://localhost:4200/"
		// is found.
	}

	@Override
	public void onTrace(String line) {
		if (line.startsWith(SERVING_ON)) {
			final String serverURL = line.substring(SERVING_ON.length(), line.length()).trim();
			// Open a Web Browser with the given server URL
			new NgServeJob(serverURL).schedule();

		}
	}

}
