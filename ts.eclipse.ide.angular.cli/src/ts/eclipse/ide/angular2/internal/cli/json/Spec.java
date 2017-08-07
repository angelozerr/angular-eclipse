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

import com.google.gson.annotations.SerializedName;

/**
 * Pojo for "spec" of angular-cli.json
 *
 */
public class Spec {

	@SerializedName("class")
	private boolean cls;

	private boolean component;

	private boolean directive;

	private boolean module;

	private boolean pipe;

	private boolean service;

	private boolean guard;

	public boolean isClass() {
		return cls;
	}

	public boolean isComponent() {
		return component;
	}

	public boolean isDirective() {
		return directive;
	}

	public boolean isModule() {
		return module;
	}

	public boolean isPipe() {
		return pipe;
	}

	public boolean isService() {
		return service;
	}

	public boolean isGuard() {
		return guard;
	}

}
