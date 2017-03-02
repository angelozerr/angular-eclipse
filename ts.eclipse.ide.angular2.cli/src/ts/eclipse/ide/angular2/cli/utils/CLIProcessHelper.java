/**
 *  Copyright (c) 2016-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.cli.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import ts.OS;
import ts.eclipse.ide.core.utils.OSHelper;
import ts.utils.FileUtils;
import ts.utils.IOUtils;
import ts.utils.ProcessHelper;

/**
 * Helper for ng process.
 *
 */
public class CLIProcessHelper {

	public static final String NG_FILENAME = "ng";

	private static final String ANGULAR_CLI = "@angular/cli:";

	public static File findNg() {
		return findNg(OSHelper.getOs());
	}

	private static File findNg(OS os) {
		String extension = getNgFileExtension(os);
		return ProcessHelper.findLocation(NG_FILENAME, os, extension);
	}

	public static String getNgFileExtension() {
		return getNgFileExtension(OSHelper.getOs());
	}

	private static String getNgFileExtension(OS os) {
		return os == OS.Windows ? ".cmd" : null;
	}

	public static String getNgFileName() {
		OS os = OSHelper.getOs();
		if (os == OS.Windows) {
			return "ng.cmd";
		}
		return "ng";
	}

	/**
	 * Returns the ng version and null otherwise.
	 * 
	 * @param ngFile
	 * @return the ng version and null otherwise.
	 */
	public static String getNgVersion(File ngFile) {
		if (ngFile != null) {
			BufferedReader reader = null;
			try {
				String command = FileUtils.getPath(ngFile) + " --version";
				Process p = Runtime.getRuntime().exec(command);
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					int index = line.indexOf(ANGULAR_CLI);
					if (index != -1) {
						return line.substring(index + ANGULAR_CLI.length()).trim();
					}
				}
				return null;
			} catch (IOException e) {
				return null;
			} finally {
				IOUtils.closeQuietly(reader);
			}
		}
		return null;
	}
}
