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

import org.eclipse.wst.html.core.validate.extension.IHTMLCustomAttributeValidator;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import ts.eclipse.ide.angular2.core.Angular2CorePlugin;
import ts.eclipse.ide.angular2.core.html.INgBindingType;

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
		// '[', '*', 'bind-' or 'on-', etc.
		return Angular2CorePlugin.getBindingManager().isNgBindingType(attrName);
	}

	@Override
	public ValidationMessage validateAttribute(IDOMElement target, String attrName) {
		// WTP do a lower case to the attrName, retrieve the real attribute
		// name.
		String name = target.getAttributeNode(attrName).getName();
		INgBindingType type = Angular2CorePlugin.getBindingManager().getType(name);
		return type.validate(target, name, getFile());
	}

}
