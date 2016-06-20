/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.ui.style;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Position;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.w3c.dom.NamedNodeMap;

import ts.eclipse.ide.angular2.core.Angular2Project;
import ts.eclipse.ide.angular2.core.Angular2ProjectSettings;
import ts.eclipse.ide.angular2.internal.ui.preferences.Angular2UIPreferenceNames;

/**
 * Base class to compute positions for Angular Expression (border and expression
 * content).
 *
 */
public abstract class AbstractAngularExpressionSemanticHighlighting extends AbstractAngularSemanticHighlighting {

	@Override
	public String getEnabledPreferenceKey() {
		return Angular2UIPreferenceNames.HIGHLIGHTING_EXPRESSION_ENABLED;
	}

	@Override
	protected List<Position> consumes(IDOMNode node, IFile file, IStructuredDocumentRegion documentRegion) {
		if (!(DOMRegionContext.XML_CONTENT.equals(documentRegion.getType())
				|| DOMRegionContext.XML_TAG_NAME.equals(documentRegion.getType()))) {
			return null;
		}
		String startSymbol = Angular2ProjectSettings.DEFAULT_START_SYMBOL;
		String endSymbol = Angular2ProjectSettings.DEFAULT_END_SYMBOL;
		if (file != null) {
			try {
				Angular2Project angularProject = Angular2Project.getAngular2Project(file.getProject());
				startSymbol = angularProject.getSettings().getStartSymbol();
				endSymbol = angularProject.getSettings().getEndSymbol();
			} catch (Throwable e) {
			}
		}
		if (DOMRegionContext.XML_CONTENT.equals(documentRegion.getType())) {
			// text node, check if this node contains {{ and }}.
			// ex : <span>{{remaining()}} of {{todos.length}} remaining</span>
			List<Position> positions = new ArrayList<Position>();
			String regionText = documentRegion.getText();
			int startOffset = documentRegion.getStartOffset();
			fillPositions(positions, startSymbol, endSymbol, regionText, startOffset);
			return positions;

		} else if (DOMRegionContext.XML_TAG_NAME.equals(documentRegion.getType())) {
			// element node, check if this node contains attributes which
			// contains {{ and }}.
			// ex : <span class="done-{{todo.done}}">
			NamedNodeMap attributes = node.getAttributes();
			if (attributes != null) {
				List<Position> positions = new ArrayList<Position>();
				IDOMAttr attr = null;
				for (int i = 0; i < attributes.getLength(); i++) {
					attr = (IDOMAttr) attributes.item(i);
					String regionText = attr.getValue();
					int startOffset = attr.getValueRegionStartOffset() + 1;
					fillPositions(positions, startSymbol, endSymbol, regionText, startOffset);
				}
				return positions;
			}
		}
		return null;
	}

	/**
	 * Fill positions.
	 * 
	 * @param positions
	 * @param startExpression
	 * @param endExpression
	 * @param regionText
	 * @param startOffset
	 */
	private void fillPositions(List<Position> positions, String startExpression, String endExpression,
			String regionText, int startOffset) {
		int startIndex = regionText.indexOf(startExpression);
		int endIndex = -1;
		while (startIndex != -1) {
			endIndex = fillPosition(positions, startExpression, endExpression, regionText, startIndex, startOffset);
			if (endIndex == -1) {
				break;
			} else {
				endIndex = endIndex + endExpression.length();
				startIndex = regionText.indexOf(startExpression, endIndex);
			}
		}
	}

	/**
	 * Fill positions.
	 * 
	 * @param positions
	 * @param startExpression
	 * @param endExpression
	 * @param regionText
	 * @param startIndex
	 * @param startOffset
	 * @return
	 */
	protected abstract int fillPosition(List<Position> positions, String startExpression, String endExpression,
			String regionText, int startIndex, int startOffset);
}
