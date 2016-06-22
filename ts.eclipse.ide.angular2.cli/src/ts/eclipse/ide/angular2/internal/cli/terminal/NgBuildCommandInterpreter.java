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
package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

/**
 * "ng build" interpreter to refresh the "dist" folder.
 *
 */
public class NgBuildCommandInterpreter extends AbstractCommandInterpreter {

	private static final String BUILT_PROJECT_SUCCESSFULLY_STORED_IN = "Built project successfully. Stored in \"";
	private String distDir;

	public NgBuildCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
	}

	@Override
	protected void execute(List<String> parameters, String workingDir) {
		final IContainer[] c = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocation(new Path(workingDir));
		if (c != null && c.length > 0) {
			new UIJob(AngularCLIMessages.NgBuildCommandInterpreter_jobName) {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						IFolder distFolder = c[0].getFolder(new Path(distDir));
						distFolder.refreshLocal(IResource.DEPTH_INFINITE, monitor);
						if (distFolder.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							final IViewPart view = page.findView(IPageLayout.ID_PROJECT_EXPLORER);
							((ISetSelectionTarget) view).selectReveal(new StructuredSelection(distFolder));
						}
					} catch (CoreException e) {
						return new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID,
								AngularCLIMessages.NgBuildCommandInterpreter_error, e);
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}

	@Override
	public void onTrace(String trace) {
		if (trace.startsWith(BUILT_PROJECT_SUCCESSFULLY_STORED_IN)) {
			distDir = trace.substring(BUILT_PROJECT_SUCCESSFULLY_STORED_IN.length(), trace.length());
			distDir = distDir.substring(0, distDir.indexOf("\""));
		}
	}

}
