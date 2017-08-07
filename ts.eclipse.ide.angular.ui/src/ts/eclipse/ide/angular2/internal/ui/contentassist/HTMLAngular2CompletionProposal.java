/**
 *  Copyright (c) 2014-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.internal.ui.contentassist;

import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;

/**
 * 
 * Extends WTP {@link CustomCompletionProposal} to use
 * {@link BrowserInformationControl}.
 *
 */
public class HTMLAngular2CompletionProposal extends CustomCompletionProposal
		/*implements ICompletionProposalExtension3/*, ITernHoverInfoProvider*/ {

//	private final IIDETernProject ternProject;
	private IInformationControlCreator ternControlCreator;

	public HTMLAngular2CompletionProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString,
			String alternateMatch,
			IContextInformation contextInformation,
			String additionalProposalInfo, int relevance) {		
		super(replacementString, replacementOffset, replacementLength,
				cursorPosition, image, displayString, alternateMatch, contextInformation,
				additionalProposalInfo, relevance, true);
	}
//
//	@Override
//	public IInformationControlCreator getInformationControlCreator() {
//		Shell shell = TernUIPlugin.getActiveWorkbenchShell();
//		if (shell == null || !BrowserInformationControl.isAvailable(shell))
//			return null;
//
//		if (ternControlCreator == null) {
//			IInformationControlCreator presenterControlCreator = new IDEPresenterControlCreator(
//					this);
//			ternControlCreator = new IDEHoverControlCreator(
//					presenterControlCreator, true, this);
//		}
//		return ternControlCreator;
//	}
//
//	@Override
//	public int getPrefixCompletionStart(IDocument document, int completionOffset) {
//		return getReplacementOffset();
//	}
//
//	@Override
//	public CharSequence getPrefixCompletionText(IDocument document,
//			int completionOffset) {
//		return null;
//	}

//	@Override
//	public IIDETernProject getTernProject() {
//		return ternProject;
//	}
//
//	@Override
//	public ITernFile getFile() {
//		return null;
//	}
//
//	@Override
//	public Integer getOffset() {
//		return null;
//	}
}
