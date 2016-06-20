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
public class AngularCliProject {

	private static final String NG_PROJECT = AngularCliProject.class.getName();
	private final static Map<IProject, AngularCliProject> ngProjects = new HashMap<IProject, AngularCliProject>();

	private final AngularCliProjectSettings settings;

	AngularCliProject(IProject project) {
		this.settings = new AngularCliProjectSettings(project);
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

	public static AngularCliProject getAngularCliProject(IProject project) throws CoreException {
		synchronized (ngProjects) {
			AngularCliProject ngProject = ngProjects.get(project);
			if (ngProject == null) {
				ngProject = new AngularCliProject(project);
			}
			return ngProject;
		}
	}

	public AngularCliProjectSettings getSettings() {
		return settings;
	}
}
