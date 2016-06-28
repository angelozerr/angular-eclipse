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
package ts.eclipse.ide.angular2.internal.cli.launch;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.wst.jsdt.js.cli.core.CLIStreamListener;

import ts.eclipse.ide.angular2.internal.cli.jobs.NgServeJob;

/**
 * {@link CLIStreamListener} extension to track message of "ng serve" and open a
 * Web Browser.
 *
 */
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
