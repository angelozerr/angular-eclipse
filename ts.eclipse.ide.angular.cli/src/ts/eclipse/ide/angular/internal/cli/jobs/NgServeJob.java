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
package ts.eclipse.ide.angular.internal.cli.jobs;

import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.angular.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular.internal.cli.AngularCLIMessages;

/**
 * Open a Web Browser with the given server URL.
 *
 */
public class NgServeJob extends UIJob {

	private final String serverURL;

	public NgServeJob(String serverURL) {
		super(AngularCLIMessages.NgServeJob_jobName);
		this.serverURL = serverURL;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = browserSupport.createBrowser(
					IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, null, null, null);
			browser.openURL(new URL(serverURL));
		} catch (Exception e) {
			return new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID, AngularCLIMessages.NgServeJob_error, e);
		}
		return Status.OK_STATUS;
	}
}
