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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ts.eclipse.ide.angular2.internal.cli.json.AngularCLIJson;
import ts.eclipse.ide.core.TypeScriptCorePlugin;
import ts.eclipse.ide.core.resources.watcher.FileWatcherListenerAdapter;
import ts.eclipse.ide.core.resources.watcher.ProjectWatcherListenerAdapter;

/**
 * Angular CLI project.
 *
 */
public class AngularCLIProject {

	public static final IPath ANGULAR_CLI_JSON_PATH = new Path("angular-cli.json");
	public static final IPath DOT_ANGULAR_CLI_JSON_PATH = new Path(".angular-cli.json");

	private final static Map<IProject, AngularCLIProject> ngProjects = new HashMap<IProject, AngularCLIProject>();

	private final AngularCLIProjectSettings settings;

	private AngularCLIJson angularCLIJson;

	private final FileWatcherListenerAdapter refreshAngularCliJson = new FileWatcherListenerAdapter() {
		@Override
		public void onAdded(IFile file) {
			AngularCLIProject.this.angularCLIJson = null;
		}

		@Override
		public void onChanged(IFile file) {
			AngularCLIProject.this.angularCLIJson = null;
		}

		@Override
		public void onDeleted(IFile file) {
			AngularCLIProject.this.angularCLIJson = null;
		}
	};

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
		TypeScriptCorePlugin.getResourcesWatcher().addFileWatcherListener(project, ANGULAR_CLI_JSON_PATH.toString(),
				refreshAngularCliJson);
		TypeScriptCorePlugin.getResourcesWatcher().addFileWatcherListener(project, DOT_ANGULAR_CLI_JSON_PATH.toString(),
				refreshAngularCliJson);
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

	/**
	 * Returns the settings if the Angular CLI project.
	 * 
	 * @return the settings if the Angular CLI project.
	 */
	public AngularCLIProjectSettings getSettings() {
		return settings;
	}

	/**
	 * Returns true if the given project is an angular CLI project (contains
	 * "angular-cli.json" or ".angular-cli.json" file in the root of the
	 * project) and false otherwise.
	 * 
	 * @param project
	 * @return true if the given project is an angular CLI project (contains
	 *         "angular-cli.json" or ".angular-cli.json" file in the root of the
	 *         project) and false otherwise.
	 */
	public static boolean isAngularCLIProject(IProject project) {
		return project.exists(DOT_ANGULAR_CLI_JSON_PATH) || project.exists(ANGULAR_CLI_JSON_PATH);
	}

	/**
	 * Returns the Pojo for angular-cli.json
	 * 
	 * @return the Pojo for angular-cli.json
	 * @throws CoreException
	 */
	public AngularCLIJson getAngularCLIJson() throws CoreException {
		if (angularCLIJson != null) {
			return angularCLIJson;
		}
		IProject project = settings.getProject();
		IFile cliFile = getAngularCliJsonFile(project);
		if (cliFile != null && cliFile.exists()) {
			angularCLIJson = AngularCLIJson.load(cliFile);
		}
		return angularCLIJson;
	}

	/**
	 * Returns the ".angular-cli.json" or "angular-cli.json" from the given
	 * project.
	 * 
	 * @param project
	 * @return the ".angular-cli.json" or "angular-cli.json" from the given
	 *         project.
	 */
	public static IFile getAngularCliJsonFile(IProject project) {
		if (project.exists(DOT_ANGULAR_CLI_JSON_PATH)) {
			// since Angular-CLI beta 32, the file was renamed to
			// .angular-cli.json
			return project.getFile(DOT_ANGULAR_CLI_JSON_PATH);
		}
		return project.getFile(ANGULAR_CLI_JSON_PATH);
	}

}
