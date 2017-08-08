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
package ts.eclipse.ide.angular.internal.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Angular UI messages.
 *
 */
public class AngularUIMessages extends NLS {

	private static final String BUNDLE_NAME = "ts.eclipse.ide.angular.internal.ui.AngularUIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// Preferences
	public static String AngularGlobalPreferencesPage_desc;
	public static String HTMLAngularGlobalPreferencesPage_desc;
	public static String HTMLAngularEditorPreferencesPage_desc;
	
	public static String AngularTyping_Auto_Complete;
	public static String AngularTyping_Close_EL;

	public static String Sample_HTMLAngular_doc;

	// Tags
	public static String Angular_expression_border;
	public static String Angular_expression;
	public static String Angular_directive_name;

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
		NLS.initializeMessages(BUNDLE_NAME, AngularUIMessages.class);
	}
}
