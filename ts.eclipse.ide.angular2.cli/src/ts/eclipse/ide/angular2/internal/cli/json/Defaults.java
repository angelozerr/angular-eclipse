/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *
 */
package ts.eclipse.ide.angular2.internal.cli.json;

import com.google.gson.annotations.SerializedName;

/**
 * Pojo for "defaults" of angular-cli.json
 *
 */
public class Defaults {

	private String styleExt;

	@SerializedName("class")
	private GenerateDefaults cliClass;

	private GenerateDefaults component;

	private GenerateDefaults directive;

	@SerializedName("enum")
	private GenerateDefaults cliEnum;

	private GenerateDefaults guard;

	@SerializedName("interface")
	private GenerateDefaults cliInterface;

	private GenerateDefaults module;

	private GenerateDefaults pipe;

	private GenerateDefaults service;

	public String getStyleExt() {
		return styleExt;
	}

	public GenerateDefaults getCliClass() {
		return cliClass;
	}

	public GenerateDefaults getComponent() {
		return component;
	}

	public GenerateDefaults getDirective() {
		return directive;
	}

	public GenerateDefaults getCliEnum() {
		return cliEnum;
	}

	public GenerateDefaults getGuard() {
		return guard;
	}

	public GenerateDefaults getCliInterface() {
		return cliInterface;
	}

	public GenerateDefaults getModule() {
		return module;
	}

	public GenerateDefaults getPipe() {
		return pipe;
	}

	public GenerateDefaults getService() {
		return service;
	}
}