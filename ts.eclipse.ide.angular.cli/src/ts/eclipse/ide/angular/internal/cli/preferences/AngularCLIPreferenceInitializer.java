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
package ts.eclipse.ide.angular.internal.cli.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import ts.eclipse.ide.angular.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular.cli.preferences.AngularCLIPreferenceConstants;
import ts.eclipse.ide.core.utils.PreferencesHelper;

/**
 * Angular cli preferences initializer.
 *
 */
public class AngularCLIPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = PreferencesHelper
				.getWorkspaceDefaultPreferences(AngularCLIPlugin.PLUGIN_ID);

		// initialize properties for direct access of node.js server (start an
		// internal process)
		initializeCliPreferences(node);
	}

	private void initializeCliPreferences(IEclipsePreferences node) {
		node.putBoolean(AngularCLIPreferenceConstants.NG_USE_GLOBAL_INSTALLATION, true);
		node.put(AngularCLIPreferenceConstants.NG_CUSTOM_FILE_PATH, "");
		node.putBoolean(AngularCLIPreferenceConstants.EXECUTE_NG_WITH_FILE, false);
	}
}
