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
 * Pojo for different "defaults" for the generate commands of angular-cli.json.
 * Note, that not every "defaults" supports all of the fields.
 *
 */
public class GenerateDefaults {

	// Constants for ViewEncapsulation
	public static final String VE_EMULATED	= "Emulated";
	public static final String VE_NATIVE	= "Native";
	public static final String VE_NONE		= "None";

	// Constants for ChangeDetectionStrategy
	public static final String CD_ON_PUSH	= "OnPush";
	public static final String CD_DEFAULT	= "Default";

	@SerializedName("change-detection")
	private String changeDetection;

	private boolean flat;

	@SerializedName("inline-style")
	private boolean inlineStyle;

	@SerializedName("inline-template")
	private boolean inlineTemplate;

	private String prefix;

	private boolean spec;

	@SerializedName("view-encapsulation")
	private String viewEncapsulation;

	public String getChangeDetection() {
		return checkChangeDetection(changeDetection);
	}

	public static String checkChangeDetection(String cd) {
		if (cd != null) {
			switch (cd) {
			case CD_DEFAULT:
			case CD_ON_PUSH:
				return cd;
			default:
				return CD_DEFAULT;
			}
		}
		return CD_DEFAULT;
	}

	public boolean isFlat() {
		return flat;
	}

	public boolean isInlineStyle() {
		return inlineStyle;
	}

	public boolean isInlineTemplate() {
		return inlineTemplate;
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean isSpec() {
		return spec;
	}

	public String getViewEncapsulation() {
		return checkViewEncapsulation(viewEncapsulation);
	}

	public static String checkViewEncapsulation(String ve) {
		if (ve != null) {
			switch (ve) {
			case VE_EMULATED:
			case VE_NATIVE:
			case VE_NONE:
				return ve;
			default:
				return VE_EMULATED;
			}
		}
		return VE_EMULATED;
	}

}