/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.internal.cli.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;

/**
 * {@link IStreamListener} implementation to track angular-cli messages.
 *
 */
public class AngularCliStreamListener implements IStreamListener {

	private final IProject project;
	
	public AngularCliStreamListener(IProject project) throws CoreException {
		this.project = project;
	}

	@Override
	public void streamAppended(String text, IStreamMonitor monitor) {

	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
