package ts.eclipse.ide.angular2.internal.ui.contentassist;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.ui.text.IJavaScriptPartitions;

import ts.eclipse.ide.angular2.core.Angular2Project;
import ts.eclipse.ide.angular2.core.template.Angular2ContextType;
import ts.eclipse.ide.angular2.internal.ui.Angular2ImageResource;
import ts.eclipse.ide.jsdt.ui.editor.contentassist.TypeScriptContentAssistInvocationContext;
import ts.eclipse.ide.jsdt.ui.template.ITemplateImageProvider;
import ts.eclipse.ide.jsdt.ui.template.TemplateEngine;
import ts.eclipse.ide.jsdt.ui.template.contentassist.AbstractTemplateCompletionProposalComputer;

public class TypeScriptAngular2TemplateCompletionProposalComputer extends AbstractTemplateCompletionProposalComputer
		implements ITemplateImageProvider {

	private final TemplateEngine ng2TemplateEngine;

	public TypeScriptAngular2TemplateCompletionProposalComputer() {
		ng2TemplateEngine = createTemplateEngine(Angular2ContextType.NAME, this);
	}

	@Override
	protected TemplateEngine computeCompletionEngine(TypeScriptContentAssistInvocationContext context) {
		try {
			IResource resource = context.getResource();
			if (resource == null || !Angular2Project.isAngular2Project(resource.getProject())) {
				return null;
			}
			String partition = TextUtilities.getContentType(context.getDocument(),
					IJavaScriptPartitions.JAVA_PARTITIONING, context.getInvocationOffset(), true);
			if (partition.equals(IJavaScriptPartitions.JAVA_DOC)) {
				return null;
			} else {
				return ng2TemplateEngine;
			}
		} catch (BadLocationException x) {
			return null;
		}
	}

	@Override
	public Image getImage(Template template) {
		return Angular2ImageResource.getImage(Angular2ImageResource.IMG_ANGULAR2);
	}
}
