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

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;

/**
 * Angular CLI Property tester.
 *
 */
public class AngularCLIPropertyTester extends PropertyTester {

	private static final String IS_ANGULAR_CLI_PROJECT_PROPERTY = "isAngularCLIProject";

	public AngularCLIPropertyTester() {
		// Default constructor is required for property tester
	}

	/**
	 * Tests if the receiver object is a project is a Angular CLI project and
	 * false otherwise.
	 * 
	 * @return true if the receiver object is a project is a Angular CLI project
	 *         and false otherwise.
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (IS_ANGULAR_CLI_PROJECT_PROPERTY.equals(property)) {
			return testIsAngularCLIProject(receiver);
		}
		return false;
	}

	private boolean testIsAngularCLIProject(Object receiver) {
		if (receiver instanceof IAdaptable) {
			IProject project = (IProject) ((IAdaptable) receiver).getAdapter(IProject.class);
			if (project != null) {
				return AngularCLIProject.isAngularCLIProject(project);
			}
		}
		return false;
	}
}
