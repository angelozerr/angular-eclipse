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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
 * "ng generate ..." interpreter to refresh the generated resources.
 *
 */
public class NgGenerateCommandInterpreter extends AbstractCommandInterpreter {

	private static final String CREATE = "create";

	private final List<String> filenames;

	private boolean lastLineCreate;

	public NgGenerateCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
		this.filenames = new ArrayList<String>();
		this.lastLineCreate = false;
	}

	@Override
	public void execute(List<String> parameters, String workingDir) {
		final IContainer[] c = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocation(new Path(workingDir));
		if (c != null && c.length > 0) {
			new UIJob(AngularCLIMessages.NgGenerateCommandInterpreter_jobName) {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						List<IResource> resources = getResources(c, monitor);
						if (resources.size() > 0) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							final IViewPart view = page.findView(IPageLayout.ID_PROJECT_EXPLORER);
							((ISetSelectionTarget) view).selectReveal(new StructuredSelection(resources));
						}
					} catch (CoreException e) {
						return new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID,
								AngularCLIMessages.NgGenerateCommandInterpreter_error, e);
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}

	}

	/**
	 * Collect the generated resources.
	 * 
	 * @param parents
	 * @param monitor
	 * @return the generated resources.
	 * @throws CoreException
	 */
	private List<IResource> getResources(IContainer[] parents, IProgressMonitor monitor) throws CoreException {
		List<IResource> resources = new ArrayList<IResource>();
		for (String filename : filenames) {
			IPath path = new Path(filename);
			for (IContainer parent : parents) {
				IFile file = parent.getFile(path);
				file.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				if (parent.exists(path)) {
					resources.add(file);
				}
			}
		}
		return resources;
	}

	@Override
	public void onTrace(String line) {
		line = line.trim();
		if (lastLineCreate) {
			String filename = line.trim();
			filenames.add(filename);
			lastLineCreate = false;
		} else {
			lastLineCreate = line.startsWith(CREATE);
		}
	}

}
