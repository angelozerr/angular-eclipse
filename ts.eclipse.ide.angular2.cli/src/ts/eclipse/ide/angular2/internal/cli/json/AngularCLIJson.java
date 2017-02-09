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
package ts.eclipse.ide.angular2.internal.cli.json;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Pojo for angular-cli.json
 *
 */
public class AngularCLIJson {

	private Defaults defaults;
	private List<App> apps;

	public static AngularCLIJson load(IFile angularCliFile) throws CoreException {
		Reader json = null;
		try {
			json = new InputStreamReader(angularCliFile.getContents());
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			return gson.fromJson(json, AngularCLIJson.class);
		} finally {
			if (json != null) {
				try {
					json.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public Defaults getDefaults() {
		return defaults;
	}

	public List<App> getApps() {
		return apps;
	}
}
