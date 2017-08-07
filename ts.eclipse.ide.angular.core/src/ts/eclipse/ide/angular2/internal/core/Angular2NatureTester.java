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
package ts.eclipse.ide.angular2.internal.core;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;

import ts.eclipse.ide.angular2.core.Angular2Project;

/**
 * Angular2 nature tester.
 *
 */
public class Angular2NatureTester extends PropertyTester {

	private static final String IS_ANGULAR2_PROJECT_PROPERTY = "isAngular2Project";

	public Angular2NatureTester() {
		// Default constructor is required for property tester
	}

	/**
	 * Tests if the receiver object is a project is a Angular2 project
	 * 
	 * @return true if the receiver object is a Project that has a nature that
	 *         is treated as Angular2 nature, otherwise false is returned
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (IS_ANGULAR2_PROJECT_PROPERTY.equals(property)) {
			return testIsTypeScriptProject(receiver);
		}
		return false;
	}

	private boolean testIsTypeScriptProject(Object receiver) {
		if (receiver instanceof IAdaptable) {
			IProject project = (IProject) ((IAdaptable) receiver).getAdapter(IProject.class);
			if (project != null) {
				return Angular2Project.isAngular2Project(project);
			}
		}
		return false;
	}
}
