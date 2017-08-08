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
package ts.eclipse.ide.angular.internal.cli.editor;

import org.eclipse.ui.PartInitException;

import ts.eclipse.ide.json.ui.AbstractFormEditor;

/**
 * angular-cli.json editor composed with multiple page:
 * 
 * <ul>
 * <li>Overview page.</li>
 * <li>Source page.</li>
 * </ul>
 *
 */
public class AngularCLIEditor extends AbstractFormEditor {

	@Override
	protected void doAddPages() throws PartInitException {
		addPage(new OverviewPage(this));
	}

}
