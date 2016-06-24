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
 *  some code copied/pasted from org.eclipse.ui.internal.wizards.datatransfer.SmartImportJob
 */
package ts.eclipse.ide.angular2.internal.cli.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
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

/**
 * Refresh generated files and select it in the Project Explorer.
 *
 */
public class NgGenerateJob extends UIJob {

	private final Collection<String> fileNames;
	private final IContainer parent;

	public NgGenerateJob(Collection<String> fileNames, IContainer parent) {
		super(AngularCLIMessages.NgGenerateJob_jobName);
		this.fileNames = fileNames;
		this.parent = parent;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			List<IResource> resources = getResources(parent, monitor);
			if (resources.size() > 0) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				final IViewPart view = page.findView(IPageLayout.ID_PROJECT_EXPLORER);
				((ISetSelectionTarget) view).selectReveal(new StructuredSelection(resources));
			}
		} catch (CoreException e) {
			return new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID, AngularCLIMessages.NgGenerateJob_error, e);
		}
		return Status.OK_STATUS;
	}

	/**
	 * Collect the generated resources.
	 * 
	 * @param parents
	 * @param monitor
	 * @return the generated resources.
	 * @throws CoreException
	 */
	private List<IResource> getResources(IContainer parent, IProgressMonitor monitor) throws CoreException {
		List<IResource> resources = new ArrayList<IResource>();
		for (String filename : fileNames) {
			IPath path = new Path(filename);
			IFile file = parent.getFile(path);
			file.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			if (parent.exists(path)) {
				resources.add(file);
			}
		}
		return resources;
	}
}
