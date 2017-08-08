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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.html.core.internal.validate.Segment;
import org.eclipse.wst.html.core.validate.extension.CustomValidatorUtil;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

import ts.eclipse.ide.angular.core.html.INgBindingCollector;
import ts.eclipse.ide.angular.internal.core.AngularCoreMessages;
import ts.eclipse.ide.angular.internal.core.html.directives.NgDirectiveRegistry;
import ts.eclipse.ide.angular.internal.core.html.schema.DomElementSchemaRegistry;

/**
 * Base class for Event binding: ($name), on-$name.
 *
 */
public class BaseEventBinding extends AbstractNgBindingType {

	private final List<String> keyParts;

	public BaseEventBinding(String startsWith, String endsWith) {
		super(startsWith, endsWith);
		keyParts = createKeyParts();
	}

	private List<String> createKeyParts() {
		List<String> parts = new ArrayList<String>();
		parts.add("dot");
		parts.add("space");
		parts.add("enter");
		parts.add("esc");
		parts.add("alt");
		parts.add("control");
		parts.add("meta");
		parts.add("shift");
		return parts;
	}

	@Override
	protected ValidationMessage validate(String name, IDOMElement target, String attrName, IFile file) {
		// Key events
		if (name.indexOf('.') != -1) {
			// event binding with '.' are valid only with 'keyup' and 'keydown'
			// see "parseEventName" in
			// https://github.com/angular/angular/blob/master/modules/%40angular/platform-browser/src/dom/events/key_events.ts
			String[] parts = name.split("[.]");
			if (parts.length > 1 && ("keyup".equals(parts[0]) || "keydown".equals(parts[0]))) {
				// Validate each parts
				String part = null;
				for (int i = 1; i < parts.length; i++) {
					part = parts[i];
					if (!validateKeyPart(part)) {
						int partOffset = getPartOffset(parts, i);
						Segment segment = CustomValidatorUtil.getAttributeSegment(
								(IDOMNode) target.getAttributeNode(attrName), CustomValidatorUtil.ATTR_REGION_NAME);
						return new ValidationMessage(
								NLS.bind(AngularCoreMessages.UndefinedKeyEventBinding_error, part),
								segment.getOffset() + partOffset + getStartsWith().length(), part.length(),
								ValidationMessage.WARNING);
					}
				}
				return null;
			}
		} else {
			String tagName = target.getTagName();
			if (DomElementSchemaRegistry.INSTANCE.hasEvent(tagName, name)) {
				return null;
			}
			// Directive
			if (NgDirectiveRegistry.INSTANCE.hasEvent(tagName, name, file)) {
				return null;
			}
		}
		return createValidationMessage(target, attrName,
				NLS.bind(AngularCoreMessages.UndefinedEventBinding_error, name), ValidationMessage.WARNING);
	}

	@Override
	protected void doCollect(IDOMElement target, String name, IFile file, INgBindingCollector collector) {
		String tagName = target.getTagName();
		int index = name.lastIndexOf('.');
		if (index != -1) {
			String[] parts = name.split("[.]");
			if (parts.length > 0 && ("keyup".equals(parts[0]) || "keydown".equals(parts[0]))) {
				// Validate each parts
				List<String> keyParts = createKeyParts();
				for (int i = 1; i < parts.length; i++) {
					keyParts.remove(parts[i]);
				}
				for (String keyPart : keyParts) {
					collector.collect(name, name.substring(0, index) + "." + keyPart, null, this);
				}
			}
		} else {
			DomElementSchemaRegistry.INSTANCE.collectEvent(tagName, name, this, collector);
		}
	}

	private int getPartOffset(String[] parts, int length) {
		int offset = 0;
		for (int i = 0; i < length; i++) {
			if (i < length) {
				offset++;
			}
			offset += parts[i].length();
		}
		return offset;
	}

	private boolean validateKeyPart(String part) {
		if (part.length() == 1) {
			return true;
		}
		return keyParts.contains(part);
	}
}
