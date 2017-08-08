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

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.document.CMNodeUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import ts.eclipse.ide.angular.core.html.INgBindingCollector;
import ts.eclipse.ide.angular.internal.core.AngularCoreMessages;
import ts.eclipse.ide.angular.internal.core.html.directives.NgDirectiveRegistry;
import ts.eclipse.ide.angular.internal.core.html.schema.DomElementSchemaRegistry;

/**
 * Property binding: [$name].
 *
 */
public class BasePropertyBinding extends AbstractNgBindingType {

	private static final String ATTR_PROP = "attr";
	private static final String CLASS_PROP = "class";
	private static final String STYLE_PROP = "style";

	public BasePropertyBinding(String startsWith, String endsWith) {
		super(startsWith, endsWith);
	}

	@Override
	protected ValidationMessage validate(String name, IDOMElement target, String attrName, IFile file) {
		String tagName = target.getTagName();
		int index = name.indexOf('.');
		if (index != -1) {
			if (index + 1 < name.length()) {
				String part = name.substring(0, index);
				// see
				// https://angular.io/docs/ts/latest/guide/template-syntax.html#!#attribute-binding
				// https://angular.io/docs/ts/latest/guide/template-syntax.html#!#class-binding
				// https://angular.io/docs/ts/latest/guide/template-syntax.html#!#style-binding
				if (ATTR_PROP.equals(part) || CLASS_PROP.equals(part) || STYLE_PROP.equals(part)) {
					// It's attribubte, class or style binding (ex: <button
					// [style.color] = "" >)
					// its'a valid binding.
					return null;
				}
			}
		} else {
			if (DomElementSchemaRegistry.INSTANCE.hasProperty(tagName,
					DomElementSchemaRegistry.INSTANCE.getMappedPropName(name))) {
				return null;
			}
			// Directive
			if (NgDirectiveRegistry.INSTANCE.hasProperty(tagName, name, file)) {
				return null;
			}
		}
		return createValidationMessage(target, attrName,
				NLS.bind(AngularCoreMessages.UndefinedPropertyBinding_error, name), ValidationMessage.WARNING);
	}

	@Override
	protected void doCollect(IDOMElement target, String name, IFile file, INgBindingCollector collector) {
		String tagName = target.getTagName();
		int index = name.lastIndexOf('.');
		if (index != -1) {
			String part = name.substring(0, index);
			if (ATTR_PROP.equals(part)) {
				// completion for attributes
				CMElementDeclaration eltDecl = CMNodeUtil.getElementDeclaration(target);
				if (eltDecl != null) {
					Iterator it = eltDecl.getAttributes().iterator();
					CMAttributeDeclaration attr = null;
					while (it.hasNext()) {
						attr = (CMAttributeDeclaration) it.next();
						collector.collect(name, part + "." + attr.getAttrName(),
								null, this);
					}
				}
			}
		} else {
			DomElementSchemaRegistry.INSTANCE.collectProperty(tagName,
					DomElementSchemaRegistry.INSTANCE.getMappedPropName(name), this, collector);
			collector.collect(name, ATTR_PROP, formatAttr(ATTR_PROP), this);
			collector.collect(name, CLASS_PROP, formatAttr(CLASS_PROP), this);
		}
		// style already exists
	}
}
