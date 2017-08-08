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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.angular.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular.internal.cli.Trace;
import ts.eclipse.ide.terminal.interpreter.UIInterpreterHelper;

/**
 * Refresh generated files and select it in the Project Explorer.
 *
 */
public class NgGenerateJob extends UIJob {

	private final Collection<String> fileNames;
	private final IContainer parent;
	private final String blueprint;

	public NgGenerateJob(Collection<String> fileNames, IContainer parent, String blueprint) {
		super(AngularCLIMessages.NgGenerateJob_jobName);
		this.fileNames = fileNames;
		this.parent = parent;
		this.blueprint = blueprint;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			List<IResource> resources = getResources(parent, monitor);
			if (resources.size() > 0) {
				// Open the generated blueprint files in an editor
				openGeneratedFiles(resources);
				// Select in the Project Explorer the generated files.
				UIInterpreterHelper.selectRevealInDefaultViews(resources);
			}
		} catch (CoreException e) {
			return new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID, AngularCLIMessages.NgGenerateJob_error, e);
		}
		return Status.OK_STATUS;
	}

	private void openGeneratedFiles(List<IResource> resources) {
		//String suffix = "." + blueprint.toLowerCase() + ".ts";
		for (IResource resource : resources) {
			//if (resource.getName().endsWith(suffix)) {
				try {
					UIInterpreterHelper.openFile((IFile) resource);
				} catch (PartInitException e) {
					Trace.trace(Trace.SEVERE, "Cannot open generated file", e);
				}
			//}
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
	private List<IResource> getResources(IContainer parent, IProgressMonitor monitor) throws CoreException {
		List<IResource> resources = new ArrayList<IResource>();
		for (String filename : fileNames) {
			IPath path = new Path(filename);
			// new version of angular-cli display in the console
			// create src\app\a.ts => search in the project
			if (!addResource(parent.getProject(), path, resources, monitor)) {
				// old version of angular-cli display in the console
				// create a.ts => search in the folder
				addResource(parent, path, resources, monitor);
			}
		}
		return resources;
	}

	private boolean addResource(IContainer parent, IPath path, List<IResource> resources, IProgressMonitor monitor)
			throws CoreException {
		IFile file = parent.getFile(path);
		file.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		if (parent.exists(path)) {
			resources.add(file);
			return true;
		}
		return false;
	}

}
