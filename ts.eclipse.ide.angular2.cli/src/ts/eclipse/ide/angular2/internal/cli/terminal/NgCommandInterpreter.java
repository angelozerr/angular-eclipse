package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.net.URL;
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
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.terminal.interpreter.ICommandInterpreter;
import ts.eclipse.ide.terminal.interpreter.ICommandInterpreterFactory;

public class NgCommandInterpreter implements ICommandInterpreter, ICommandInterpreterFactory {

	private String serverURL;

	@Override
	public void process(List<String> parameters, String workingDir) {
		if (parameters.contains("new")) {
			processNew(parameters, workingDir);
		} else if (parameters.contains("server")) {
			processServer(parameters, workingDir);
		}

	}

	private void processServer(List<String> parameters, String workingDir) {
		try {
			IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = browserSupport.createBrowser(
					IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, null, null, null);
			browser.openURL(new URL(this.serverURL));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processNew(List<String> parameters, String workingDir) {
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

		// final IContainer[] c =
		// ResourcesPlugin.getWorkspace().getRoot().findContainersForLocation(new
		// Path(workingDir + "/" + projectName));
		// if (c != null && c.length > 0) {
		//
		// }
	}

	@Override
	public void addLine(String line) {
		if (line.startsWith("Serving on")) {
			this.serverURL = line.substring("Serving on".length(), line.length()).trim();
			
			try {
				IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
				IWebBrowser browser = browserSupport.createBrowser(
						IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, null, null, null);
				browser.openURL(new URL(this.serverURL));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ICommandInterpreter create() {
		return this;
	}

}