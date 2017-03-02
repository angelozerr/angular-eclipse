package ts.eclipse.ide.angular2.internal.cli;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.cli.preferences.AngularCLIPreferenceConstants;
import ts.eclipse.ide.core.resources.AbstractTypeScriptSettings;
import ts.utils.StringUtils;

public class AngularCLIProjectSettings extends AbstractTypeScriptSettings {

	public AngularCLIProjectSettings(IProject project) {
		super(project, AngularCLIPlugin.PLUGIN_ID);
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {

	}

	public File getNgFile() {
		String path = super.getStringPreferencesValue(AngularCLIPreferenceConstants.NG_CUSTOM_FILE_PATH, null);
		if (!StringUtils.isEmpty(path)) {
			return resolvePath(path);
		}
		return null;
	}

}
