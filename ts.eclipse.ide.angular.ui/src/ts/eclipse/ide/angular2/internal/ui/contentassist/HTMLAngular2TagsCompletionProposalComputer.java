package ts.eclipse.ide.angular2.internal.ui.contentassist;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.DefaultXMLCompletionProposalComputer;
import org.w3c.dom.Node;

import ts.TypeScriptNoContentAvailableException;
import ts.eclipse.ide.angular2.core.Angular2CorePlugin;
import ts.eclipse.ide.angular2.core.Angular2Project;
import ts.eclipse.ide.angular2.core.utils.DOMUtils;
import ts.eclipse.ide.angular2.internal.ui.Trace;
import ts.eclipse.ide.core.resources.IIDETypeScriptProject;
import ts.eclipse.ide.core.utils.TypeScriptResourceUtil;
import ts.eclipse.ide.jsdt.ui.editor.contentassist.JSDTCompletionProposalFactory;
import ts.resources.ITypeScriptFile;

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

	@Override
	public List computeCompletionProposals(CompletionProposalInvocationContext context, IProgressMonitor monitor) {
		ITextViewer textViewer = context.getViewer();
		int documentPosition = context.getInvocationOffset();

		setErrorMessage(null);
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, documentPosition);

		Node node = (Node) treeNode;
		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getParentNode() != null)) {
			node = node.getParentNode();
		}
		IDOMNode xmlnode = (IDOMNode) node;

		ContentAssistRequest contentAssistRequest = null;

		// IStructuredDocumentRegion sdRegion =
		// getStructuredDocumentRegion(documentPosition);
		// ITextRegion completionRegion = getCompletionRegion(documentPosition,
		// node);
		//
		// String matchString = getMatchString(sdRegion, completionRegion,
		// documentPosition);
		//
		IResource resource = DOMUtils.getFile(xmlnode);
		if (resource != null) {
			try {
				if (TypeScriptResourceUtil.canConsumeTsserver(resource)) {
					// Here Angular2 Language Service is available, consume it.
					IProject project = resource.getProject();
					IIDETypeScriptProject tsProject = TypeScriptResourceUtil.getTypeScriptProject(project);
					if (tsProject != null) {

						int position = context.getInvocationOffset();

						IDocument document = context.getDocument();
						ITypeScriptFile tsFile = tsProject.openFile(resource, document);
						CharSequence prefix = ""; // context.computeIdentifierPrefix();

						String p = prefix != null ? prefix.toString() : "";
						return tsFile.completions(position, new JSDTCompletionProposalFactory(position, p, textViewer))
								.get(5000, TimeUnit.MILLISECONDS).stream().filter(entry -> entry.updatePrefix(p))
								.collect(Collectors.toList());
					}
				}
			} catch (ExecutionException e) {
				if (e.getCause() instanceof TypeScriptNoContentAvailableException) {
					// Ignore "No content available" error.
					return Collections.EMPTY_LIST;
				}
				Trace.trace(Trace.SEVERE, "Error while TypeScript completion", e);
			} catch (Exception e) {
				Trace.trace(Trace.SEVERE, "Error while TypeScript completion", e);
			}
		}
		return super.computeCompletionProposals(context, monitor);
	}

	// /**
	// * StructuredTextViewer must be set before using this.
	// */
	// private IStructuredDocumentRegion getStructuredDocumentRegion(int pos) {
	// return null;
	// //ContentAssistUtils.getStructuredDocumentRegion(fTextViewer, pos);
	// }
}
