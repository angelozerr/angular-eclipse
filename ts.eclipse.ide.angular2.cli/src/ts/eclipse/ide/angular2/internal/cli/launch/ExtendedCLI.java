package ts.eclipse.ide.angular2.internal.cli.launch;

import org.eclipse.core.externaltools.internal.IExternalToolConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.wst.jsdt.js.cli.core.CLI;
import org.eclipse.wst.jsdt.js.cli.core.CLICommand;
import org.eclipse.wst.jsdt.js.cli.core.CLIResult;
import org.eclipse.wst.jsdt.js.cli.core.CLIStreamListener;

public class ExtendedCLI extends CLI {

	private IProject project;
	private CLICommand command;

	public ExtendedCLI(IProject project, IPath workingDir, CLICommand command) {
		super(project, workingDir, command);
		this.project = project;
		this.command = command;
	}

	public CLIResult execute(IProgressMonitor monitor) throws CoreException {
		return execute(new CLIStreamListener(), monitor);
	}

	public CLIResult execute(CLIStreamListener streamListener, IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		IProcess process = startShell(streamListener, monitor, getLaunchConfiguration(command));
		sendCLICommand(process, command, monitor);
		CLIResult result = new CLIResult(streamListener.getErrorMessage(), streamListener.getMessage());
		throwExceptionIfError(result);
		return result;
	}

	private ILaunchConfiguration getLaunchConfiguration(CLICommand command) {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager
				.getLaunchConfigurationType(IExternalToolConstants.ID_PROGRAM_LAUNCH_CONFIGURATION_TYPE);
		try {
			ILaunchConfiguration cfg = type.newInstance(null, command.getToolName());
			ILaunchConfigurationWorkingCopy wc = cfg.getWorkingCopy();
			wc.setAttribute(IProcess.ATTR_PROCESS_LABEL, command.getToolName() + " " + command.getCommandName()); //$NON-NLS-1$
			wc.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING, "UTF-8"); //$NON-NLS-1$
			cfg = wc.doSave();
			return cfg;
		} catch (CoreException e) {
			// ExCLIPlugin.logError(e);
		}
		return null;
	}

}
