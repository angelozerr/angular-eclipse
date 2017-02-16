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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.utils.StringUtils;

/**
 * Pojo for angular-cli.json
 *
 */
public class AngularCLIJson {

	private static final String STRING_DASHERIZE_REGEXP = "[ _]";
	private static final String STRING_DECAMELIZE_REGEXP = "([a-z\\d])([A-Z])";

	private static final String DEFAULT_ROOT = "src";
	private static final String APP = "app";

	private Defaults defaults;
	private List<App> apps;

	public static AngularCLIJson load(IFile angularCliFile) throws CoreException {
		Reader json = null;
		try {
			json = new InputStreamReader(angularCliFile.getContents());
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			return gson.fromJson(json, AngularCLIJson.class);
		} finally {
			if (json != null) {
				try {
					json.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public Defaults getDefaults() {
		return defaults;
	}

	public List<App> getApps() {
		return apps;
	}

	public IPath getRootPath(IProject project) {
		// Search root from the angular-cli.json apps[0].root
		String root = getRootFromApps();
		if (StringUtils.isEmpty(root)) {
			// Not found, search root from the angular-cli.json
			// defaults.sourceDir
			root = getSourceDirFromDefaults();
			if (StringUtils.isEmpty(root)) {
				// Not found, use default "src" value
				root = DEFAULT_ROOT;
			}
		}
		return new Path("/").append(project.getName()).append(root).append(APP);
	}

	private String getRootFromApps() {
		List<App> apps = getApps();
		if (apps == null || apps.size() < 1) {
			return null;
		}
		return apps.get(0).getRoot();
	}

	private String getSourceDirFromDefaults() {
		Defaults defaults = getDefaults();
		if (defaults == null) {
			return null;
		}
		return defaults.getSourceDir();
	}

	public String getPrefix() {
		// Search prefix from the angular-cli.json apps[0].prefix
		String prefix = getPrefixFromApps();
		if (StringUtils.isEmpty(prefix)) {
			// Not found, search prefix from the angular-cli.json
			// defaults.prefix
			prefix = getPrefixFromDefaults();
			if (StringUtils.isEmpty(prefix)) {
				// Not found, use default "app" value
				prefix = APP;
			}
		}
		return prefix;
	}

	private String getPrefixFromApps() {
		List<App> apps = getApps();
		if (apps == null || apps.size() < 1) {
			return null;
		}
		return apps.get(0).getPrefix();
	}

	public String getPrefixFromDefaults() {
		Defaults defaults = getDefaults();
		if (defaults == null)
			return null;
		else
			return defaults.getPrefix();
	}

	public String getStylesExt() {
		Defaults defaults = getDefaults();
		return defaults != null ? defaults.getStyleExt() : null;
	}

	public boolean isInlineTempalte() {
		Defaults defaults = getDefaults();
		Inline inline = defaults != null ? defaults.getInline() : null;
		if (inline == null)
			return false;
		else
			return inline.isTemplate();
	}

	public boolean isInlineStyle() {
		Defaults defaults = getDefaults();
		Inline inline = defaults != null ? defaults.getInline() : null;
		if (inline == null)
			return false;
		else
			return inline.isStyle();
	}

	public boolean isSpec(NgBlueprint blueprint) {
		Defaults defaults = getDefaults();
		Spec spec = defaults != null ? defaults.getSpec() : null;
		if (spec == null)
			return false;
		else {
			switch (blueprint) {
			case MODULE:
				return spec.isModule();
			case COMPONENT:
				return spec.isComponent();
			case DIRECTIVE:
				return spec.isDirective();
			case PIPE:
				return spec.isPipe();
			case SERVICE:
				return spec.isService();
			case CLASS:
				return spec.isClass();
			//case INTERFACE:
			//case ENUM:
			default:
				return false;
			}
		}
	}

	public static String decamelize(String str) {
		return str.replaceAll(STRING_DECAMELIZE_REGEXP, "$1_$2").toLowerCase();
	}

	public static String normalize(String name) {
		return decamelize(name).replaceAll(STRING_DASHERIZE_REGEXP, "-");
	}

	public String getTsFileName(String name) {
		return normalize(name).concat(".ts");
	}

	public String getSpecFileName(String name) {
		return normalize(name).concat(".spec.ts");
	}

	public String getFolderName(String name) {
		return normalize(name).concat("/");
	}

	public String getComponentTsFileName(String name) {
		return normalize(name).concat(".component.ts");
	}

	public String getComponentSpecFileName(String name) {
		return normalize(name).concat(".component.spec.ts");
	}

	public String getComponentTemplateFileName(String name) {
		return normalize(name).concat(".component.html");
	}

	public String getComponentStyleFileName(String name) {
		return normalize(name).concat(".component.").concat(getStylesExt());
	}

	public String getDirectiveFileName(String name) {
		return normalize(name).concat(".directive.ts");
	}

	public String getDirectiveSpecFileName(String name) {
		return normalize(name).concat(".directive.spec.ts");
	}

	public String getEnumFileName(String name) {
		return normalize(name).concat(".enum.ts");
	}

	public String getModuleFileName(String name) {
		return normalize(name).concat(".module.ts");
	}

	public String getModuleSpecFileName(String name) {
		return normalize(name).concat(".module.spec.ts");
	}

	public String getRoutingModuleFileName(String name) {
		return normalize(name).concat("-routing.module.ts");
	}

	public String getPipeFileName(String name) {
		return normalize(name).concat(".pipe.ts");
	}

	public String getPipeSpecFileName(String name) {
		return normalize(name).concat(".pipe.spec.ts");
	}

	public String getServiceFileName(String name) {
		return normalize(name).concat(".service.ts");
	}

	public String getServiceSpecFileName(String name) {
		return normalize(name).concat(".service.spec.ts");
	}
}
