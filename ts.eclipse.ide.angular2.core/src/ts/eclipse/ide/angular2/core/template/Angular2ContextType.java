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
package ts.eclipse.ide.angular2.core.template;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

import ts.eclipse.ide.jsdt.core.template.AbstractTypeScriptContextType;

/**
 * Angular2 template context type.
 *
 */
public class Angular2ContextType extends AbstractTypeScriptContextType {

	public static final String NAME = "Angular2"; //$NON-NLS-1$

	public Angular2ContextType() {
		super(NAME);
	}

	@Override
	public Angular2Context createContext(IDocument document, Position position) {
		return new Angular2Context(this, document, position);
	}
}
