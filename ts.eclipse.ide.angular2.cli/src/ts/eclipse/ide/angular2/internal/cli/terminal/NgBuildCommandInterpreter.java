package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.progress.UIJob;

import ts.eclipse.ide.terminal.interpreter.AbstractCommandInterpreter;

public class NgBuildCommandInterpreter extends AbstractCommandInterpreter {

	private String folder;

	public NgBuildCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
	}

	@Override
	protected void execute(List<String> parameters, String workingDir) {

		final IContainer[] c = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocation(new Path(workingDir));
		if (c != null && c.length > 0) {

			new UIJob("Refresh dist folder") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					final IFolder f = c[0].getFolder(new Path(folder));
					try {
						f.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (f.exists()) {
						IWorkbenchPage page = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
						final IViewPart view = page.findView(IPageLayout.ID_PROJECT_EXPLORER);
						((ISetSelectionTarget) view).selectReveal(new StructuredSelection(f));
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}

	@Override
	public void onTrace(String trace) {
		if (trace.startsWith("Built project successfully. Stored in \"")) {
			folder = trace.substring("Built project successfully. Stored in \"".length(), trace.length());
			folder = folder.substring(0, folder.indexOf("\""));

		}

	}

}
