/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *  Springrbua
 */
package ts.eclipse.ide.angular.cli.utils;

import java.io.File;

import ts.eclipse.ide.ui.preferences.StatusInfo;

/** Class which holds the status of a specific Angular-CLI installation. */
public class CLIStatus extends StatusInfo {

	private final File ngFile;
	private String version;

	public CLIStatus(File ngFile, String errorMessage) {
		if (errorMessage != null) {
			setError(errorMessage);
		}
		this.ngFile = ngFile;
	}

	public File getNgFile() {
		return ngFile;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
}
