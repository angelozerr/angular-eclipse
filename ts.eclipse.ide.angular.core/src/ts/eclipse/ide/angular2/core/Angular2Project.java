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
package ts.eclipse.ide.angular2.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ts.client.ISupportable;
import ts.eclipse.ide.core.resources.IIDETypeScriptProject;
import ts.eclipse.ide.core.utils.TypeScriptResourceUtil;
import ts.utils.VersionHelper;

/**
 * Angular2 project.
 *
 */
public class Angular2Project {

	private static final String ANGULAR2_PROJECT = Angular2Project.class.getName();

	private static final ISupportable ANGULAR_LANGUAGE_SERVICE_CAPABILITY = new ISupportable() {
		
		@Override
		public boolean canSupport(String version) {
			return VersionHelper.canSupport(version, "2.4.0");
		}
	};
	
	private final Angular2ProjectSettings settings;

	Angular2Project(IIDETypeScriptProject tsProject) {
		tsProject.setData(ANGULAR2_PROJECT, this);
		this.settings = new Angular2ProjectSettings(tsProject.getProject());
	}

	public static boolean isAngular2Project(IProject project) {
		if (!TypeScriptResourceUtil.isTypeScriptProject(project)) {
			return false;
		}
		return true;
	}

	public static Angular2Project getAngular2Project(IProject project) throws CoreException {
		if (!isAngular2Project(project)) {
			throw new CoreException(new Status(IStatus.ERROR, Angular2CorePlugin.PLUGIN_ID,
					"The project " + project.getName() + " is not an angular2 project."));
		}
		IIDETypeScriptProject tsProject = TypeScriptResourceUtil.getTypeScriptProject(project);
		Angular2Project angularProject = tsProject.getData(ANGULAR2_PROJECT);
		if (angularProject == null) {
			angularProject = new Angular2Project(tsProject);
		}
		return angularProject;
	}

	public Angular2ProjectSettings getSettings() {
		return settings;
	}

	public static boolean canSupportAngularLanguageService(IProject project) {
		if (isAngular2Project(project)) {
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
