package ts.eclipse.ide.angular.internal.cli;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import ts.eclipse.ide.angular.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular.cli.preferences.AngularCLIPreferenceConstants;
import ts.eclipse.ide.angular.cli.utils.CLIProcessHelper;
import ts.eclipse.ide.core.resources.AbstractTypeScriptSettings;
import ts.utils.StringUtils;

public class AngularCLIProjectSettings extends AbstractTypeScriptSettings {

	public AngularCLIProjectSettings(IProject project) {
		super(project, AngularCLIPlugin.PLUGIN_ID);
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {

	}

	/** Use the global CLI-Installation? */
	public boolean useGlobalCLIInstallation() {
		return super.getBooleanPreferencesValue(AngularCLIPreferenceConstants.NG_USE_GLOBAL_INSTALLATION, false);
	}

	/** Returns the directory, which contains the custom ng-file. */
	public File getCustomNgLocation() {
		String path = super.getStringPreferencesValue(AngularCLIPreferenceConstants.NG_CUSTOM_FILE_PATH, null);
		if (!StringUtils.isEmpty(path)) {
			return resolvePath(path);
		}
		return null;
	}

	/** Returns the custom ng-file. */
	public File getCustomNgFile() {
		File ngFile = getCustomNgLocation();
		if (ngFile != null && ngFile.isDirectory())
			return new File(ngFile, CLIProcessHelper.getNgFileName());
		else
			return null;
	}

	/** Returns the ng-file ("ng"/"ng.cmd"). */
	public File getNgFile() {
		if (useGlobalCLIInstallation())
			return CLIProcessHelper.findNg();
		else
			return getCustomNgFile();
	}

	public boolean isExecuteNgWithFile() {
		return super.getBooleanPreferencesValue(AngularCLIPreferenceConstants.EXECUTE_NG_WITH_FILE, false);
	}

}
