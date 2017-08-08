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

import ts.eclipse.ide.jsdt.core.template.AbstractTypeScriptContextType;

/**
 * Angular template context type.
 *
 */
public class AngularContextType extends AbstractTypeScriptContextType {

	public static final String NAME = "Angular"; //$NON-NLS-1$

	public AngularContextType() {
		super(NAME);
	}

	@Override
	public AngularContext createContext(IDocument document, Position position) {
		return new AngularContext(this, document, position);
	}
}
