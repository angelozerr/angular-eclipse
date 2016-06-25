package ts.eclipse.ide.angular2.internal.cli.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIProject;
import ts.utils.FileUtils;

public abstract class AbstractNewNgGenerateWizard extends Wizard implements INewWizard {

	private IStructuredSelection selection;

	private NgGenerateBlueprintWizardPage mainPage;

	public void addPages() {
		super.addPages();
		mainPage = createMainPage(getSelectedProject());// $NON-NLS-1$
		addPage(mainPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		final IProject project = mainPage.getProject();
		final NgBlueprint blueprint = mainPage.getNgBluePrint();
		final String name = mainPage.getBluepringName();
		IRunnableWithProgress op = new IRunnableWithProgress() {

			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration();
					File ngFile = AngularCLIProject.getAngularCLIProject(project).getSettings().getNgFile();
					if (ngFile != null) {
						newConfiguration.setAttribute(AngularCLILaunchConstants.NG_FILE_PATH,
								FileUtils.getPath(ngFile));
					}
					newConfiguration.setAttribute(AngularCLILaunchConstants.WORKING_DIR,
							project.getLocation().toString());
					newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION,
							NgCommand.GENERATE.name().toLowerCase());
					newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS,
							blueprint.name().toLowerCase() + " " + name);
					DebugUITools.launch(newConfiguration, ILaunchManager.RUN_MODE);
				} catch (CoreException e) {
					// super.setError(e);
				}
			}
		};

		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		}
		return true;
	}

	private ILaunchConfigurationWorkingCopy createEmptyLaunchConfiguration() throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager
				.getLaunchConfigurationType(AngularCLILaunchConstants.LAUNCH_CONFIGURATION_ID);
		ILaunchConfigurationWorkingCopy launchConfiguration = launchConfigurationType.newInstance(null,
				launchManager.generateLaunchConfigurationName("angular-cli"));
		return launchConfiguration;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}

	protected IProject getSelectedProject() {
		if (selection == null) {
			return null;
		}
		Object element = selection.getFirstElement();
		if (element instanceof IProject) {
			return ((IProject) element);
		} else if (element instanceof IResource) {
			return ((IResource) element).getProject();
		}
		return null;
	}

	protected abstract NgGenerateBlueprintWizardPage createMainPage(IProject project);

}
