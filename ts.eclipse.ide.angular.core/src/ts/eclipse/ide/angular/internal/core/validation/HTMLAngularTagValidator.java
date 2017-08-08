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

import org.eclipse.wst.html.core.validate.extension.IHTMLCustomTagValidator;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

/**
 * Ignore HTML error for Angular tags.
 *
 */
public class HTMLAngularTagValidator extends AbstractHTMLAngularValidator implements IHTMLCustomTagValidator {

	private static final String TEMPLATE_ELEMENT = "template";

	@Override
	public boolean canValidate(IDOMElement target) {
		if (!super.hasAngularNature()) {
			return false;
		}
		if (TEMPLATE_ELEMENT.equals(target.getTagName())) {
			return true;
		}
		// TODO: search definition of directive inside *.ts files
		return false;
	}

	@Override
	public ValidationMessage validateTag(IDOMElement target) {
		return null;
	}

}
