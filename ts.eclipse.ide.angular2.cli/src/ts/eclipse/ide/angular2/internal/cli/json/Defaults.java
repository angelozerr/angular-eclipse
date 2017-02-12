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

/**
 * Pojo for "defaults" of angular-cli.json
 *
 */
public class Defaults {

	private String prefix;

	private String sourceDir;

	private String styleExt;

	private boolean prefixInterfaces;

	private String lazyRoutePrefix;

	private Spec spec;

	private Inline inline;

	public String getPrefix() {
		return prefix;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public String getStyleExt() {
		return styleExt;
	}

	public boolean isPrefixInterfaces() {
		return prefixInterfaces;
	}

	public String getLazyRoutePrefix() {
		return lazyRoutePrefix;
	}

	public Spec getSpec() {
		return spec;
	}

	public Inline getInline() {
		return inline;
	}
}
