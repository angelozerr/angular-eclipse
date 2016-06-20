package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.terminal.interpreter.ICommandInterpreter;

public class NgNewCommandInterpreter implements ICommandInterpreter {

	@Override
	public void execute(List<String> parameters, String workingDir) {
		if (parameters.size() < 2) {
			return;
		}
		final String projectName = parameters.get(1);

		final IContainer[] c = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocation(new Path(workingDir));
		if (c != null && c.length > 0) {
			if (c[0].getType() == IResource.ROOT) {
				final IWorkspaceRoot root = (IWorkspaceRoot) c[0];

				new UIJob("Refresh npm project") {

					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						try {
							IProject project = root.getProject(projectName);
							if (!project.exists()) {
								project.create(monitor);
								project.open(monitor);
							} else {

							}

							IFile packagejsonFile = project.getFile("package.json");
							if (!packagejsonFile.exists()) {
								packagejsonFile.refreshLocal(IResource.DEPTH_INFINITE, monitor);
							}
							if (packagejsonFile.exists()) {

								IWorkbenchPage page = PlatformUI.getWorkbench().getWorkbenchWindows()[0]
										.getActivePage();
								final IViewPart view = page.findView(IPageLayout.ID_PROJECT_EXPLORER);
								((ISetSelectionTarget) view).selectReveal(new StructuredSelection(packagejsonFile));

								IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
										.getDefaultEditor(packagejsonFile.getName());

								page.openEditor(new FileEditorInput(packagejsonFile), desc.getId());
							}
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return Status.OK_STATUS;
					}
				}.schedule();

			}
		}
	}
	
	@Override
	public void addLine(String line) {
		
	}
}
