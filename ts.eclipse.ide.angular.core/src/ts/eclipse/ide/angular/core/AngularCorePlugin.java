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

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import ts.eclipse.ide.angular.core.html.INgBindingManager;
import ts.eclipse.ide.angular.internal.core.html.NgBindingManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class AngularCorePlugin extends Plugin {

	public static final String PLUGIN_ID = "ts.eclipse.ide.angular.core"; //$NON-NLS-1$

	// The shared instance.
	private static AngularCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public AngularCorePlugin() {
		super();
		plugin = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static AngularCorePlugin getDefault() {
		return plugin;
	}

	public static INgBindingManager getBindingManager() {
		return NgBindingManager.INSTANCE;
	}
}
