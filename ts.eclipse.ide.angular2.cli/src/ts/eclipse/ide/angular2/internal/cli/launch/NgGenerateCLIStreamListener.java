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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.jsdt.js.cli.core.CLIStreamListener;

import ts.eclipse.ide.angular2.internal.cli.jobs.NgGenerateJob;

/**
 * {@link CLIStreamListener} extension to track message of "ng generate" and
 * refresh the generated resources.
 *
 */
public class NgGenerateCLIStreamListener extends ExtendedCLIStreamListener {

	private static final String CREATE = "create";

	private final IProject project;
	private boolean created;

	private final Collection<String> fileNames;
	private final String blueprint;

	public NgGenerateCLIStreamListener(String blueprint, IProject project) {
		this.fileNames = new ArrayList<String>();
		this.created = false;
		this.project = project;
		this.blueprint = blueprint;
	}

	public Collection<String> getFileNames() {
		return fileNames;
	}

	@Override
	protected void appendLine(String line) {
		super.appendLine(line);
		boolean create = line.startsWith(CREATE);
		if (create) {
			String filename = line.substring(line.indexOf(CREATE) + CREATE.length(), line.length()).trim();
			fileNames.add(filename);
			created = true;
		}
		if (created && !create) {
			created = false;
			NgGenerateJob job = new NgGenerateJob(fileNames, project, blueprint);
			job.setRule(ResourcesPlugin.getWorkspace().getRoot());
			job.schedule();
		}
	}
}
