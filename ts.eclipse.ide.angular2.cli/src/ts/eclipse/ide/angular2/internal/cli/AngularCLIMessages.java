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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * angular-cli messages.
 *
 */
public class AngularCLIMessages extends NLS {

	private static final String BUNDLE_NAME = "ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// Preferences
	public static String AngularCLIConfigurationBlock_cli_group_label;
	public static String AngularCLIConfigurationBlock_ngUseGlobalInstallation_label;
	public static String AngularCLIConfigurationBlock_ngUseCustomFile_label;

	public static String Error_FatalInvokingCLI;

	// Interpreter
	public static String NgServeJob_jobName;
	public static String NgServeJob_error;
	public static String NgGenerateJob_jobName;
	public static String NgGenerateJob_error;
	public static String NgBuildCommandInterpreter_jobName;
	public static String NgBuildCommandInterpreter_error;
	public static String AbstractProjectCommandInterpreter_jobName;
	public static String AbstractProjectCommandInterpreter_error;
	public static String AbstractProjectCommandInterpreter_anotherProjectWithSameNameExists_description;

	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null)
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (MissingResourceException x) {
			fResourceBundle = null;
		}
		return fResourceBundle;
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, AngularCLIMessages.class);
	}
}
