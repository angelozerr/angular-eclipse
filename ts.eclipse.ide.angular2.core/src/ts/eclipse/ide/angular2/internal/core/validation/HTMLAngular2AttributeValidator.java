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
		return false;
	}

	@Override
	public ValidationMessage validateAttribute(IDOMElement target, String attrName) {		
		return null;
	}

}
