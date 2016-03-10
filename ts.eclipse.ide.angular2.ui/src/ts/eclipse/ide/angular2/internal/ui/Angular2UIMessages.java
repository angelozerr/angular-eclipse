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
package ts.eclipse.ide.angular2.internal.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Angular2 UI messages.
 *
 */
public class Angular2UIMessages extends NLS {

	private static final String BUNDLE_NAME = "ts.eclipse.ide.angular2.internal.ui.Angular2UIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// Preferences
	public static String Angular2GlobalPreferencesPage_desc;
	public static String HTMLAngular2GlobalPreferencesPage_desc;
	public static String HTMLAngular2EditorPreferencesPage_desc;
	
	public static String Angular2Typing_Auto_Complete;
	public static String Angular2Typing_Close_EL;

	public static String Sample_HTMLAngular2_doc;

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
		NLS.initializeMessages(BUNDLE_NAME, Angular2UIMessages.class);
	}
}
