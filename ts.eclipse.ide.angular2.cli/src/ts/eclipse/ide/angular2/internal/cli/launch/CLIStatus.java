/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package ts.eclipse.ide.angular2.internal.cli.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;

/**
 * @author Gorkem Ercan
 */
public class CLIStatus extends Status {
	private IProject project;

	public CLIStatus(int severity, String pluginId, int code,
			String message, Throwable exception) {
		super(severity, pluginId, code, message, exception);
	}

	public IProject getProject() {
		return project;
	}
	
	public void setProject(IProject project) {
		this.project = project;
	}
}
