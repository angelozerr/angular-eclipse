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
package ts.eclipse.ide.angular.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ts.client.ISupportable;
import ts.eclipse.ide.core.resources.IIDETypeScriptProject;
import ts.eclipse.ide.core.utils.TypeScriptResourceUtil;
import ts.utils.VersionHelper;

/**
 * Angular project.
 *
 */
public class AngularProject {

	private static final String ANGULAR_PROJECT = AngularProject.class.getName();

	private static final ISupportable ANGULAR_LANGUAGE_SERVICE_CAPABILITY = new ISupportable() {
		
		@Override
		public boolean canSupport(String version) {
			return VersionHelper.canSupport(version, "2.4.0");
		}
	};
	
	private final AngularProjectSettings settings;

	AngularProject(IIDETypeScriptProject tsProject) {
		tsProject.setData(ANGULAR_PROJECT, this);
		this.settings = new AngularProjectSettings(tsProject.getProject());
	}

	public static boolean isAngularProject(IProject project) {
		if (!TypeScriptResourceUtil.isTypeScriptProject(project)) {
			return false;
		}
		return true;
	}

	public static AngularProject getAngularProject(IProject project) throws CoreException {
		if (!isAngularProject(project)) {
			throw new CoreException(new Status(IStatus.ERROR, AngularCorePlugin.PLUGIN_ID,
					"The project " + project.getName() + " is not an angular project."));
		}
		IIDETypeScriptProject tsProject = TypeScriptResourceUtil.getTypeScriptProject(project);
		AngularProject angularProject = tsProject.getData(ANGULAR_PROJECT);
		if (angularProject == null) {
			angularProject = new AngularProject(tsProject);
		}
		return angularProject;
	}

	public AngularProjectSettings getSettings() {
		return settings;
	}

	public static boolean canSupportAngularLanguageService(IProject project) {
		if (isAngularProject(project)) {
			try {
				IIDETypeScriptProject tsProject = TypeScriptResourceUtil.getTypeScriptProject(project);
				return tsProject.canSupport(ANGULAR_LANGUAGE_SERVICE_CAPABILITY);
			} catch (CoreException e) {
				return false;
			}			
		}
		return false;
	}
}
