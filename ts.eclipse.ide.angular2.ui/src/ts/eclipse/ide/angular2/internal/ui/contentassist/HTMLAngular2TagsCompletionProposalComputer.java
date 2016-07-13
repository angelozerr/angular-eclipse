package ts.eclipse.ide.angular2.internal.ui.contentassist;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.DefaultXMLCompletionProposalComputer;

import ts.eclipse.ide.angular2.core.Angular2CorePlugin;
import ts.eclipse.ide.angular2.core.Angular2Project;
import ts.eclipse.ide.angular2.core.utils.DOMUtils;

public class HTMLAngular2TagsCompletionProposalComputer extends DefaultXMLCompletionProposalComputer {

	@Override
	protected void addAttributeNameProposals(final ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		// Check if project has angular2 nature
		final IDOMElement element = (IDOMElement) contentAssistRequest.getNode();
		IFile file = DOMUtils.getFile(element);
		if (Angular2Project.isAngular2Project(file.getProject())) {
			String attrName = contentAssistRequest.getMatchString();
			IDOMAttr attr = DOMUtils.getAttrByRegion(element, contentAssistRequest.getRegion());
			Angular2CorePlugin.getBindingManager().collect(element, attrName, file,
					new HTMLAngular2CompletionCollector(element, attr, contentAssistRequest, this));
		}
	}
}
