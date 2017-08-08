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
package ts.eclipse.ide.angular.internal.core.validation;

import org.eclipse.wst.html.core.validate.extension.IHTMLCustomAttributeValidator;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import ts.eclipse.ide.angular.core.AngularCorePlugin;
import ts.eclipse.ide.angular.core.html.INgBindingType;

/**
 * Ignore HTML error for Angular attributes.
 *
 */
public class HTMLAngularAttributeValidator extends AbstractHTMLAngularValidator
		implements IHTMLCustomAttributeValidator {

	@Override
	public boolean canValidate(IDOMElement target, String attrName) {
		if (!super.hasAngularNature()) {
			return false;
		}
		// It's an Angular project, check if attribute name starts with '(',
		// '[', '*', 'bind-' or 'on-', etc.
		return AngularCorePlugin.getBindingManager().isNgBindingType(attrName);
	}

	@Override
	public ValidationMessage validateAttribute(IDOMElement target, String attrName) {		
		return AngularCorePlugin.getBindingManager().validate(target, attrName, getFile());
	}

}
