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
package ts.eclipse.ide.angular.cli.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.tm.terminal.view.core.utils.Env;

import ts.OS;
import ts.eclipse.ide.core.utils.OSHelper;
import ts.eclipse.ide.terminal.interpreter.EnvPath;
import ts.utils.FileUtils;
import ts.utils.IOUtils;
import ts.utils.ProcessHelper;

/**
 * Helper for ng process.
 *
 */
public class CLIProcessHelper {

	private static final String NG_VERSION_CMD = "ng --version";

	public static final String NG_FILENAME = "ng";

	private static final String OLD_ANGULAR_CLI = "@angular/cli:";
	private static final String NEW_ANGULAR_CLI = "Angular CLI:";

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
	 * @param nodeFile
	 * @return the ng version and null otherwise.
	 * @throws IOException
	 */
	public static String getNgVersion(File ngFile, File nodeFile) throws IOException {
		if (ngFile != null) {
			BufferedReader reader = null;
			try {
				String ngDir = FileUtils.getPath(ngFile.getParentFile());
				// node file is set, add the directory of this node file in
				// the Path env to consume it when "ng -- version" is
				// executed.
				String nodeDir = nodeFile != null ? FileUtils.getPath(nodeFile.getParentFile()) : null;
				String[] envPath = new String[] { EnvPath.insertToEnvPath(ngDir, nodeDir) };
				String[] envp = Env.getEnvironment(envPath, true);
				String[] cmd = ProcessHelper.getCommand(NG_VERSION_CMD, OSHelper.getOs());

				Process p = Runtime.getRuntime().exec(cmd, envp);
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					int index = line.indexOf(OLD_ANGULAR_CLI);
					if (index != -1) {
						return line.substring(index + OLD_ANGULAR_CLI.length()).trim();
					} else
						index = line.indexOf(NEW_ANGULAR_CLI);
					if (index != -1) {
						return line.substring(index + NEW_ANGULAR_CLI.length()).trim();
					}
				}
				return null;
			} finally {
				IOUtils.closeQuietly(reader);
			}
		}
		return null;
	}

}
