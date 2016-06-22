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
package ts.eclipse.ide.angular2.internal.cli;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import ts.eclipse.ide.core.TypeScriptCorePlugin;
import ts.eclipse.ide.core.resources.watcher.ProjectWatcherListenerAdapter;

/**
 * Angular2 project.
 *
 */
public class AngularCLIProject {

	private final static Map<IProject, AngularCLIProject> ngProjects = new HashMap<IProject, AngularCLIProject>();

	private final AngularCLIProjectSettings settings;

	AngularCLIProject(IProject project) {
		this.settings = new AngularCLIProjectSettings(project);
		synchronized (ngProjects) {
			ngProjects.put(project, this);
		}
		TypeScriptCorePlugin.getResourcesWatcher().addProjectWatcherListener(project,
				new ProjectWatcherListenerAdapter() {

					@Override
					public void onClosed(IProject project) {
						dispose(project);
					}

					@Override
					public void onDeleted(IProject project) {
						dispose(project);

					}

					private void dispose(IProject project) {
						synchronized (ngProjects) {
							ngProjects.remove(project);
						}
					}

				});
	}

	public static AngularCLIProject getAngularCLIProject(IProject project) throws CoreException {
		synchronized (ngProjects) {
			AngularCLIProject ngProject = ngProjects.get(project);
			if (ngProject == null) {
				ngProject = new AngularCLIProject(project);
			}
			return ngProject;
		}
	}

	public AngularCLIProjectSettings getSettings() {
		return settings;
	}
}
