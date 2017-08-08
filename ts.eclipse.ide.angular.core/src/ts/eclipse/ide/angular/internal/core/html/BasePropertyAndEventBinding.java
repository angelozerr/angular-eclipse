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
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import ts.eclipse.ide.angular.core.html.INgBindingCollector;
import ts.eclipse.ide.angular.internal.core.AngularCoreMessages;
import ts.eclipse.ide.angular.internal.core.html.directives.NgDirectiveRegistry;

/**
 * Property and event binding: [($name)].
 *
 */
public class BasePropertyAndEventBinding extends AbstractNgBindingType {

	public BasePropertyAndEventBinding(String startsWith, String endsWith) {
		super(startsWith, endsWith);
	}

	@Override
	protected ValidationMessage validate(String name, IDOMElement target, String attrName, IFile file) {
		String tagName = target.getTagName();
		// Directive
		if (NgDirectiveRegistry.INSTANCE.hasProperty(tagName, name, file)
				|| NgDirectiveRegistry.INSTANCE.hasEvent(tagName, name, file)) {
			return null;
		}
		return createValidationMessage(target, attrName,
				NLS.bind(AngularCoreMessages.UndefinedPropertyAndEventBinding_error, name), ValidationMessage.WARNING);
	}

	@Override
	protected void doCollect(IDOMElement target, String attrName, IFile file, INgBindingCollector collector) {

	}
}
