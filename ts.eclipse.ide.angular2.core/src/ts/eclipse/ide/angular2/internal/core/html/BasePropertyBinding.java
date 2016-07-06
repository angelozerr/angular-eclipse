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
package ts.eclipse.ide.angular2.internal.core.html;

import org.eclipse.core.resources.IFile;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import ts.eclipse.ide.angular2.internal.core.Angular2CoreMessages;
import ts.eclipse.ide.angular2.internal.core.html.schema.DomElementSchemaRegistry;

/**
 * Property binding: [$name].
 *
 */
public class BasePropertyBinding extends AbstractNgBindingType {

	public BasePropertyBinding(String startsWith, String endsWith) {
		super(startsWith, endsWith);
	}

	@Override
	protected ValidationMessage validate(String name, IDOMElement target, String attrName, IFile file) {
		String tagName = target.getTagName();
		if (DomElementSchemaRegistry.INSTANCE.hasProperty(tagName,
				DomElementSchemaRegistry.INSTANCE.getMappedPropName(name))) {
			return null;
		}
		int index = name.indexOf('.');
		if (index != -1 && index + 1< name.length() ) {
			String part = name.substring(0, index);
			if ("attr".equals(part) || "class".equals(part) || "style".equals(part)) {
				// It's attribubte, class or style binding (ex: <button
				// [style.color] = "" >)
				// its'a valid binding.
				return null;
			}
		}
		return createValidationMessage(target, attrName,
				NLS.bind(Angular2CoreMessages.UndefinedPropertyBinding_error, name), ValidationMessage.WARNING);
	}

}
