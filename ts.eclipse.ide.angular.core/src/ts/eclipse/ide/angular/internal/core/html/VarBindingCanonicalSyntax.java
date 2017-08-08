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
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import ts.eclipse.ide.angular.core.html.INgBindingCollector;
import ts.eclipse.ide.angular.internal.core.AngularCoreMessages;

/**
 * Property binding: var-$name.
 *
 */
public class VarBindingCanonicalSyntax extends AbstractNgBindingType {

	public VarBindingCanonicalSyntax() {
		super("var-");
	}

	@Override
	protected ValidationMessage validate(String name, IDOMElement target, String attrName, IFile file) {
		String tagName = target.getTagName();
		if (name.startsWith("-")) {
			return createValidationMessage(target, attrName, AngularCoreMessages.VarDontAllow_error,
					ValidationMessage.ERROR);
		} else if (isTemplateElement(tagName)) {
			return createValidationMessage(target, attrName, AngularCoreMessages.VarDeprecatedOnTemplate_error,
					ValidationMessage.WARNING);
		} else {
			return createValidationMessage(target, attrName, AngularCoreMessages.VarDeprecatedOnNonTemplate_error,
					ValidationMessage.WARNING);
		}
	}

	@Override
	protected void doCollect(IDOMElement target, String attrName, IFile file, INgBindingCollector collector) {

	}
}
