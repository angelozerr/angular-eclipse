/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Springrbua - initial API and implementation
 *
 */
package ts.eclipse.ide.angular2.internal.cli.json;

/**
 * Pojo for "inline" of angular-cli.json
 *
 */
public class Inline {

	private boolean template;

	private boolean style;

	public boolean isTemplate() {
		return template;
	}

	public boolean isStyle() {
		return style;
	}
}
