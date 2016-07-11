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
package ts.eclipse.ide.angular2.core.html;

public interface INgBindingManager {

	/**
	 * Returns the ng binding type for the given attribute name and null
	 * otherwise.
	 * 
	 * @param attrName
	 * @return the ng binding type for the given attribute name and null
	 *         otherwise.
	 */
	INgBindingType getType(String attrName);
	
	boolean isNgBindingType(String attrName);
}
