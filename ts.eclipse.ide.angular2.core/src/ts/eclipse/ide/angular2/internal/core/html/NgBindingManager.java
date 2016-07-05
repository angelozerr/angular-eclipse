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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;

import ts.eclipse.ide.angular2.core.Angular2CorePlugin;
import ts.eclipse.ide.angular2.core.html.INgBindingManager;
import ts.eclipse.ide.angular2.core.html.INgBindingType;
import ts.utils.StringUtils;

/**
 * ng binding manager.
 *
 */
public class NgBindingManager implements INgBindingManager {

	public static final INgBindingManager INSTANCE = new NgBindingManager();

	private final List<INgBindingType> bindingTypes;

	public NgBindingManager() {
		this.bindingTypes = new ArrayList<INgBindingType>();
		bindingTypes.add(new PropertyAndEventBinding());
		bindingTypes.add(new PropertyBinding());
		bindingTypes.add(new PropertyBindingCanonicalSyntax());
		bindingTypes.add(new EventBinding());
		bindingTypes.add(new EventBindingCanonicalSyntax());
		bindingTypes.add(new VarBindingCanonicalSyntax());
		bindingTypes.add(new LetBindingCanonicalSyntax());
		bindingTypes.add(new RefBindingCanonicalSyntax());
		bindingTypes.add(new HashBindingCanonicalSyntax());
		bindingTypes.add(new PropertyAndEventBindingCanonicalSyntax());
		bindingTypes.add(new TemplateDirective());
	}

	@Override
	public INgBindingType getType(String attrName) {
		if (StringUtils.isEmpty(attrName)) {
			return null;
		}
		for (INgBindingType bindingType : bindingTypes) {
			if (bindingType.match(attrName)) {
				return bindingType;
			}
		}
		return null;
	}
	
	@Override
	public boolean isNgBindingType(String attrName) {
		return getType(attrName) != null;
	}
}
