package ts.eclipse.ide.angular2.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import ts.eclipse.ide.core.resources.AbstractTypeScriptSettings;

public class Angular2ProjectSettings extends AbstractTypeScriptSettings {

	public static final String DEFAULT_START_SYMBOL = "{{";
	public static final String DEFAULT_END_SYMBOL = "}}";

	public Angular2ProjectSettings(IProject project) {
		super(project, Angular2CorePlugin.PLUGIN_ID);
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {

	}

	public String getStartSymbol() {
		return DEFAULT_START_SYMBOL;
	}

	public String getEndSymbol() {
		return DEFAULT_END_SYMBOL;
	}

}
