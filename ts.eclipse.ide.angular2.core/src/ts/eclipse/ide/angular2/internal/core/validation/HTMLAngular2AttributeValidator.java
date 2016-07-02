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
package ts.eclipse.ide.angular2.internal.core.validation;

import org.eclipse.wst.html.core.internal.validate.Segment;
import org.eclipse.wst.html.core.validate.extension.CustomValidatorUtil;
import org.eclipse.wst.html.core.validate.extension.IHTMLCustomAttributeValidator;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.document.CMNodeUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

import ts.eclipse.ide.angular2.core.html.NgAttributeSyntaxException;
import ts.eclipse.ide.angular2.core.html.NgAttributeType;

/**
 * Ignore HTML error for Angular2 attributes.
 *
 */
public class HTMLAngular2AttributeValidator extends AbstractHTMLAngular2Validator
		implements IHTMLCustomAttributeValidator {

	@Override
	public boolean canValidate(IDOMElement target, String attrName) {
		if (!super.hasAngular2Nature()) {
			return false;
		}
		// It's an Angular2 project, check if attribute name starts with '(',
		// '[', '*', 'bind-' or 'on-'.
		return NgAttributeType.getType(attrName) != null;
	}

	@Override
	public ValidationMessage validateAttribute(IDOMElement target, String attrName) {
		try {
			NgAttributeType type = NgAttributeType.getType(attrName);
			String name = NgAttributeType.extractAttrName(attrName, type);
			// check if it's a known HTML5 attribute
			if (isKnownHTML5Attribute(target, name, type)) {
				return null;
			}
		} catch (NgAttributeSyntaxException e) {
			String tagName = target.getTagName();
			Segment segment = CustomValidatorUtil.getAttributeSegment((IDOMNode) target.getAttributeNode(attrName),
					CustomValidatorUtil.ATTR_REGION_NAME);
			return new ValidationMessage(e.getMessage(), segment.getOffset(), segment.getLength(),
					ValidationMessage.ERROR);
		}
		String tagName = target.getTagName();
		Segment segment = CustomValidatorUtil.getAttributeSegment((IDOMNode) target.getAttributeNode(attrName),
				CustomValidatorUtil.ATTR_REGION_NAME);
		return new ValidationMessage("Undefined ng attribute name " + attrName + ".", segment.getOffset(),
				segment.getLength(), ValidationMessage.WARNING);
	}

	private boolean isKnownHTML5Attribute(IDOMElement target, String name, NgAttributeType type) {
		CMElementDeclaration declaration = CMNodeUtil.getElementDeclaration(target);
		if (declaration == null) {
			return false;
		}
		switch (type) {
		case EventBinding:
		case EventBindingCanonicalSyntax:
			return declaration.getAttributes().getNamedItem("on" + name) != null;
		case PropertyBinding:
		case PropertyBindingCanonicalSyntax:
			return declaration.getAttributes().getNamedItem(name) != null;
		case PropertyAndEventBinding:
			return declaration.getAttributes().getNamedItem(name) != null
					|| declaration.getAttributes().getNamedItem("on" + name) != null;
		}
		return false;
	}

}
