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
package ts.eclipse.ide.angular.internal.core.html;

import org.eclipse.core.resources.IFile;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.html.core.internal.validate.Segment;
import org.eclipse.wst.html.core.validate.extension.CustomValidatorUtil;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

import ts.eclipse.ide.angular.core.html.INgBindingCollector;
import ts.eclipse.ide.angular.core.html.INgBindingType;
import ts.eclipse.ide.angular.core.html.NgBindingTypeException;
import ts.eclipse.ide.angular.internal.core.AngularCoreMessages;

/**
 * Abstract class for ng binding type.
 *
 */
public abstract class AbstractNgBindingType implements INgBindingType {

	private static final String DATA_SUFFIX = "data-";
	private static final String TEMPLATE_ELEMENT = "template";
	private final String startsWith;
	private final String endsWith;

	protected AbstractNgBindingType(String startsWith) {
		this(startsWith, null);
	}

	protected AbstractNgBindingType(String startsWith, String endsWith) {
		this.startsWith = startsWith;
		this.endsWith = endsWith;
	}

	@Override
	public boolean match(String attrName) {
		return attrName.startsWith(startsWith);
	}

	@Override
	public String formatAttr(String name) {
		StringBuilder attr = new StringBuilder();
		if (!name.startsWith(startsWith)) {
			attr.append(startsWith);
		}
		attr.append(name);
		if (endsWith != null && !name.endsWith(endsWith)) {
			attr.append(endsWith);
		}
		return attr.toString();
	}

	@Override
	public ValidationMessage validate(IDOMElement target, String attrName, IFile file) {
		try {
			// See normalize attribute rule in
			// @angular/compiler/src/template_parser.ts
			String name = extractName(this.normalizeAttributeName(attrName));
			return validate(name, target, attrName, file);
		} catch (NgBindingTypeException e) {
			return createValidationMessage(target, attrName, e.getMessage(), e.getSeverity());
		}
	}

	protected String extractName(String attrName) throws NgBindingTypeException {
		if (endsWith == null) {
			return attrName.substring(startsWith.length(), attrName.length());
		} else {
			if (!attrName.endsWith(endsWith)) {
				throw new NgBindingTypeException(
						NLS.bind(AngularCoreMessages.AttributeBindingSyntax_error, attrName, endsWith),
						ValidationMessage.ERROR);
			}
			return attrName.substring(startsWith.length(), attrName.length() - endsWith.length());
		}
	}

	protected ValidationMessage createValidationMessage(IDOMElement target, String attrName, String message,
			int severity) {
		String tagName = target.getTagName();
		Segment segment = CustomValidatorUtil.getAttributeSegment((IDOMNode) target.getAttributeNode(attrName),
				CustomValidatorUtil.ATTR_REGION_NAME);
		return new ValidationMessage(message, segment.getOffset(), segment.getLength(), severity);
	}

	protected abstract ValidationMessage validate(String name, IDOMElement target, String attrName, IFile file);

	private String normalizeAttributeName(String attrName) {
		return attrName.toLowerCase().startsWith(DATA_SUFFIX) ? attrName.substring(DATA_SUFFIX.length()) : attrName;
	}

	protected boolean isTemplateElement(String tagName) {
		return TEMPLATE_ELEMENT.equals(tagName);
	}

	public String getStartsWith() {
		return startsWith;
	}

	public String getEndsWith() {
		return endsWith;
	}

	@Override
	public void collect(IDOMElement target, String attrName, boolean fullMatch, IFile file, INgBindingCollector collector) {
		String name = extractName2(this.normalizeAttributeName(attrName));
		doCollect(target, name, file, collector);
	}

	private String extractName2(String attrName) {
		if (endsWith != null && attrName.endsWith(endsWith)) {
			return attrName.substring(startsWith.length(), attrName.length() - endsWith.length());
		}
		return attrName.substring(startsWith.length(), attrName.length());
	}

	protected abstract void doCollect(IDOMElement target, String attrName, IFile file, INgBindingCollector collector);
}
