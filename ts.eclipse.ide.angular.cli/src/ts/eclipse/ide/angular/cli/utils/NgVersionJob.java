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
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

import ts.eclipse.ide.angular.internal.cli.AngularCLIMessages;
import ts.utils.FileUtils;
import ts.utils.StringUtils;

/** Class for executing "ng --version" Command. */
public class NgVersionJob extends Job {

	private File nodeFile;
	private File ngFile;

	public NgVersionJob() {
		super(AngularCLIMessages.AngularCLIConfigurationBlock_ValidatingNgCli_jobName);
	}

	/** Sets the NodeJS-File for this Job. */
	public void setNodeFile(File nodeFile) {
		this.nodeFile = nodeFile;
	}

	/** Sets the Ng-File for this Job. */
	public void setNgFile(File ngFile) {
		this.ngFile = ngFile;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		File currentNgFile = ngFile;
		if (currentNgFile == null) {
			return Status.CANCEL_STATUS;
		}
		String version = null;
		String errorMessage = null;
		try {
			version = CLIProcessHelper.getNgVersion(currentNgFile, nodeFile);
		} catch (IOException e) {
			errorMessage = e.getMessage();
		}
		final CLIStatus status = StringUtils.isEmpty(version) ? new CLIStatus(null,
				errorMessage != null ? errorMessage
						: NLS.bind(AngularCLIMessages.AngularCLIConfigurationBlock_ngCustomFile_invalid_error,
								FileUtils.getPath(currentNgFile)))
				: new CLIStatus(currentNgFile, null);
		status.setVersion(version);
		if (monitor.isCanceled() || !currentNgFile.equals(ngFile)) {
			return Status.CANCEL_STATUS;
		}
		return status;
	}
}
