package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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

public class NgGenerateCommandInterpreter extends AbstractCommandInterpreter {

	private final List<String> filenames;

	private boolean lastLineCreate;

	public NgGenerateCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
		this.filenames = new ArrayList<String>();
		this.lastLineCreate = false;
	}

	@Override
	public void execute(List<String> parameters, String workingDir) {
		final IContainer[] c = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocation(new Path(workingDir));
		if (c != null && c.length > 0) {
			new UIJob("Refresh ng generate resources") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					final List<IResource> resources = getResources(c, monitor);
					if (resources.size() > 0) {
						IWorkbenchPage page = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
						final IViewPart view = page.findView(IPageLayout.ID_PROJECT_EXPLORER);
						((ISetSelectionTarget) view).selectReveal(new StructuredSelection(resources));
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}

	}

	private List<IResource> getResources(IContainer[] parents, IProgressMonitor monitor) {
		List<IResource> resources = new ArrayList<IResource>();
		for (String filename : filenames) {
			IPath path = new Path(filename);
			for (IContainer parent : parents) {
				IFile file = parent.getFile(path);
				try {
					file.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					if (parent.exists(path)) {
						resources.add(file);
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return resources;
	}

	@Override
	public void onTrace(String line) {
		line = line.trim();
		if (lastLineCreate) {
			String filename = line.trim();
			filenames.add(filename);
			lastLineCreate = false;
		} else {
			lastLineCreate = line.startsWith("create");
		}
	}

}
