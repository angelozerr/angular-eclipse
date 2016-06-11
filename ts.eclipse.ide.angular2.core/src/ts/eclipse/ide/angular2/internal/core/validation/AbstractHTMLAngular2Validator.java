package ts.eclipse.ide.angular2.internal.core.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;

import ts.eclipse.ide.angular2.core.Angular2Project;
import ts.eclipse.ide.angular2.core.utils.DOMUtils;

/**
 * Abstract class for custom Angular2 validator.
 *
 */
public class AbstractHTMLAngular2Validator {

	private IProject project;

	/**
	 * Cache the project of the given document if project has angular2 nature.
	 * 
	 * @param doc
	 */
	public void init(IStructuredDocument doc) {
		this.project = null;
		if (doc instanceof IDocument) {
			IFile file = DOMUtils.getFile((IDocument) doc);
			IProject project = file.getProject();
			if (Angular2Project.hasAngular2Nature(project)) {
				// project has angular2 nature, cache the project
				this.project = project;
			}
		}
	}

	/**
	 * Returns true if the project has angular2 nature and false otherwise.
	 * 
	 * @return true if the project has angular2 nature and false otherwise.
	 */
	protected boolean hasAngular2Nature() {
		return project != null && Angular2Project.hasAngular2Nature(project);
	}
}
