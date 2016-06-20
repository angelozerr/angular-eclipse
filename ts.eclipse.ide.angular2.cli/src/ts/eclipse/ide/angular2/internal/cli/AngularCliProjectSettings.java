package ts.eclipse.ide.angular2.internal.cli;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import ts.eclipse.ide.angular2.cli.AngularCliPlugin;
import ts.eclipse.ide.angular2.cli.preferences.AngularCliPreferenceConstants;
import ts.eclipse.ide.core.resources.AbstractTypeScriptSettings;
import ts.utils.StringUtils;

public class AngularCliProjectSettings extends AbstractTypeScriptSettings {

	public AngularCliProjectSettings(IProject project) {
		super(project, AngularCliPlugin.PLUGIN_ID);
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {

	}

	public File getNgFile() {
		String path = super.getStringPreferencesValue(AngularCliPreferenceConstants.NG_CUSTOM_FILE_PATH, null);
		if (!StringUtils.isEmpty(path)) {
			File resolvedPath = resolvePath(path);
			return resolvedPath != null ? getNgFile(resolvedPath) : null;
		}
		return null;
	}

	private File getNgFile(File ngDir) {
		if (ngDir.getName().equals("ng")) {
			return ngDir;
		}
		return new File(ngDir, "bin/ng");
	}

}
