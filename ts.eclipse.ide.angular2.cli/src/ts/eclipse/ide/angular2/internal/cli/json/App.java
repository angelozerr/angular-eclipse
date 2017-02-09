/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *
 */
package ts.eclipse.ide.angular2.internal.cli.json;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Pojo for "app" of angular-cli.json
 *
 */
public class App {

	private static final String DEFAULT_ROOT = "src";
	private static final String APP = "app";

	private String root;

	private String prefix;

	public String getRoot() {
		return root;
	}

	public String getPrefix() {
		return prefix;
	}

	public IPath getRootPath(IProject project) {
		return getRootPath(project, root);
	}

	public static IPath getDefaultRootPath(IProject project) {
		return getRootPath(project, DEFAULT_ROOT);
	}

	private static IPath getRootPath(IProject project, String root) {
		return new Path(project.getName()).append(root).append(APP);
	}
}
