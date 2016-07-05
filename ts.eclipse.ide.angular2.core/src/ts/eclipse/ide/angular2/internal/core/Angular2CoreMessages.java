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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Angular2 Core messages.
 *
 */
public class Angular2CoreMessages extends NLS {

	private static final String BUNDLE_NAME = "ts.eclipse.ide.angular2.internal.core.Angular2CoreMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// HTML Angular2 Bindings errors
	public static String AttributeBindingSyntax_error;
	public static String UndefinedPropertyBinding_error;
	public static String UndefinedEventBinding_error;
	public static String UndefinedPropertyAndEventBinding_error;
	public static String LetOnlySupportedOnTemplateElements_error;
	public static String VarDontAllow_error;
	public static String VarDeprecatedOnTemplate_error;
	public static String VarDeprecatedOnNonTemplate_error;
	public static String RefDontAllow_error;
	
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
		NLS.initializeMessages(BUNDLE_NAME, Angular2CoreMessages.class);
	}
}
