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
package ts.eclipse.ide.angular2.cli.launch;

import org.eclipse.core.externaltools.internal.IExternalToolConstants;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.core.TypeScriptCorePlugin;

/**
 * Angular2 cli launch constants.
 *
 */
public class AngularCLILaunchConstants {

	// Launch constants
	public static final String LAUNCH_CONFIGURATION_ID = AngularCLIPlugin.PLUGIN_ID
			+ ".angularCLILaunchConfigurationType"; //$NON-NLS-1$
	public static final String NODE_FILE_PATH = TypeScriptCorePlugin.PLUGIN_ID + ".NODE_FILE_PATH"; //$NON-NLS-1$
	public static final String NG_FILE_PATH = TypeScriptCorePlugin.PLUGIN_ID + ".NG_FILE_PATH"; //$NON-NLS-1$
	public static final String EXECUTE_NG_WITH_FILE = TypeScriptCorePlugin.PLUGIN_ID + ".EXECUTE_NG_WITH_FILE"; //$NON-NLS-1$
	public static final String WORKING_DIR = IExternalToolConstants.ATTR_WORKING_DIRECTORY; //$NON-NLS-1$
	public static final String OPERATION = TypeScriptCorePlugin.PLUGIN_ID + ".OPERATION"; //$NON-NLS-1$
	public static final String OPERATION_PARAMETERS = TypeScriptCorePlugin.PLUGIN_ID + ".OPERATION_PARAMETERS"; //$NON-NLS-1$
}
