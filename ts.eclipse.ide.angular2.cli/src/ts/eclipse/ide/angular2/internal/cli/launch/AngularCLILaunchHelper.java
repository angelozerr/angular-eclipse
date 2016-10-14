package ts.eclipse.ide.angular2.internal.cli.launch;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;

import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIProject;
import ts.eclipse.ide.angular2.internal.cli.Trace;
import ts.utils.FileUtils;

public class AngularCLILaunchHelper {

	public static void launch(NgCommand ngCommand, IProject project) throws CoreException {
		launch(ngCommand, project, ILaunchManager.RUN_MODE);
	}

	public static void launch(NgCommand ngCommand, IProject project, String mode) throws CoreException {
		try {
			ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration();
			File ngFile = AngularCLIProject.getAngularCLIProject(project).getSettings().getNgFile();
			if (ngFile != null) {
				newConfiguration.setAttribute(AngularCLILaunchConstants.NG_FILE_PATH, FileUtils.getPath(ngFile));
			}
			newConfiguration.setAttribute(AngularCLILaunchConstants.WORKING_DIR, project.getLocation().toString());
			newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION, ngCommand.name());
			// newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS,
			// "--live-reload-port 65535");
			DebugUITools.launch(newConfiguration, mode);
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, e.getLocalizedMessage());
		}
	}

	private static ILaunchConfigurationWorkingCopy createEmptyLaunchConfiguration() throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager
				.getLaunchConfigurationType(AngularCLILaunchConstants.LAUNCH_CONFIGURATION_ID);
		ILaunchConfigurationWorkingCopy launchConfiguration = launchConfigurationType.newInstance(null,
				launchManager.generateLaunchConfigurationName("angular-cli"));
		return launchConfiguration;
	}
}
