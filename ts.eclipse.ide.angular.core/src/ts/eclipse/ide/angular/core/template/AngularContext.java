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
package ts.eclipse.ide.angular.core.template;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.templates.TemplateContextType;

import ts.eclipse.ide.jsdt.core.template.AbstractTypeScriptContext;

/**
 * Angular template context.
 *
 */
public class AngularContext extends AbstractTypeScriptContext {

	public AngularContext(TemplateContextType type, IDocument document, int offset, int length) {
		super(type, document, offset, length);
	}

	public AngularContext(TemplateContextType type, IDocument document, Position position) {
		super(type, document, position);
	}

}
