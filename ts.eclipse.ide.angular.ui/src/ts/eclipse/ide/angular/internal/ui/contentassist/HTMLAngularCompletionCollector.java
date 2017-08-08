package ts.eclipse.ide.angular.internal.ui.contentassist;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLRelevanceConstants;

import ts.eclipse.ide.angular.core.html.AbstractNgBindingCollector;
import ts.eclipse.ide.angular.core.html.INgBindingType;
import ts.eclipse.ide.angular.internal.ui.AngularImageResource;

public class HTMLAngularCompletionCollector extends AbstractNgBindingCollector {

	private final ContentAssistRequest contentAssistRequest;
	private final boolean hasValue;
	private final HTMLAngularTagsCompletionProposalComputer computer;

	public HTMLAngularCompletionCollector(IDOMElement element, IDOMAttr attr,
			ContentAssistRequest contentAssistRequest, HTMLAngularTagsCompletionProposalComputer computer) {
		super(element, attr, false);
		this.computer = computer;
		this.contentAssistRequest = contentAssistRequest;
		this.hasValue = attr != null && attr.getValueRegionText() != null;
	}

	@Override
	protected void doCollect(String name, String description, INgBindingType bindingType) {
		addProposal(contentAssistRequest, name, bindingType, name,
				AngularImageResource.getImage(AngularImageResource.IMG_ANGULAR), description);
	}

	private void addProposal(ContentAssistRequest contentAssistRequest, String name, INgBindingType bindingType,
			String displayString, Image image, String additionalProposalInfo) {
		String alternateMatch = bindingType.formatAttr(name);
		StringBuilder replacementString = new StringBuilder(alternateMatch);
		if (!hasValue) {
			replacementString.append("=\"\"");
		}
		StringBuilder replacementStringCursor = new StringBuilder(alternateMatch);
		replacementStringCursor.append("=\"\"");

		int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
		int replacementLength = contentAssistRequest.getReplacementLength();
		int cursorPosition = getCursorPositionForProposedText(replacementStringCursor.toString());

		IContextInformation contextInformation = null;

		int relevance = XMLRelevanceConstants.R_XML_ATTRIBUTE_NAME;

		ICompletionProposal proposal = new HTMLAngularCompletionProposal(replacementString.toString(),
				replacementOffset, replacementLength, cursorPosition, image, displayString, alternateMatch,
				contextInformation, additionalProposalInfo, relevance);
		contentAssistRequest.addProposal(proposal);
	}

	/**
	 * <p>
	 * this is the position the cursor should be in after the proposal is
	 * applied
	 * </p>
	 * 
	 * @param proposedText
	 * @return the position the cursor should be in after the proposal is
	 *         applied
	 */
	private static int getCursorPositionForProposedText(String proposedText) {
		int cursorAdjustment;
		cursorAdjustment = proposedText.indexOf("\"\"") + 1; //$NON-NLS-1$
		// otherwise, after the first tag
		if (cursorAdjustment == 0) {
			cursorAdjustment = proposedText.indexOf('>') + 1;
		}
		if (cursorAdjustment == 0) {
			cursorAdjustment = proposedText.length() + 1;
		}

		return cursorAdjustment;
	}

}
