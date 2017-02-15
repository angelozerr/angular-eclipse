/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *
 */
package ts.eclipse.ide.angular2.internal.cli.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIImageResource;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIProject;
import ts.eclipse.ide.angular2.internal.cli.launch.AngularCLILaunchHelper;
import ts.utils.FileUtils;

/**
 * Abstract class for wizard which generates Angular2 resources by using "ng
 * generate".
 *
 */
public abstract class AbstractNewNgGenerateWizard extends Wizard implements INewWizard {

	private IStructuredSelection selection;

	private NgGenerateBlueprintWizardPage mainPage;

	public void addPages() {
		super.addPages();
		mainPage = createMainPage(getSelectedFolder());
		addPage(mainPage);
	}

	protected NgGenerateBlueprintWizardPage getMainPage() {
		return mainPage;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		initializeDefaultPageImageDescriptor();
	}

	protected void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = AngularCLIImageResource.getImageDescriptor(AngularCLIImageResource.IMG_ANGULAR2_WIZBAN);
		setDefaultPageImageDescriptor(desc);
	}

	@Override
	public boolean performFinish() {
		final IContainer folder = mainPage.getFolder();
		final NgBlueprint blueprint = mainPage.getNgBluePrint();
		final String name = mainPage.getBlueprintName();

		StringBuilder sbOptionParameters = new StringBuilder();
		appendOperationParameters(sbOptionParameters);
		final String operationParams = sbOptionParameters.toString();
		IRunnableWithProgress op = new IRunnableWithProgress() {

			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration();
					File ngFile = AngularCLIProject.getAngularCLIProject(folder.getProject()).getSettings().getNgFile();
					if (ngFile != null) {
						newConfiguration.setAttribute(AngularCLILaunchConstants.NG_FILE_PATH,
								FileUtils.getPath(ngFile));
					}
					newConfiguration.setAttribute(AngularCLILaunchConstants.WORKING_DIR,
							AngularCLILaunchHelper.getWorkingDir(folder));
					newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION,
							NgCommand.GENERATE.name().toLowerCase());
					newConfiguration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS,
							blueprint.name().toLowerCase() + " " + name + " " + operationParams);
					DebugUITools.launch(newConfiguration, ILaunchManager.RUN_MODE);
				} catch (CoreException e) {

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

	protected void appendOperationParameters(StringBuilder sb) {
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

	protected IContainer getSelectedFolder() {
		if (selection == null)
			return null;
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IContainer)
			return (IContainer) firstElement;
		else if (firstElement instanceof IResource)
			return ((IResource) firstElement).getParent();
		else
			return null;
	}

	protected abstract NgGenerateBlueprintWizardPage createMainPage(IContainer folder);

}
