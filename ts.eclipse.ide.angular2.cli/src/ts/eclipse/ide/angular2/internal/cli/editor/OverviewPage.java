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
package ts.eclipse.ide.angular2.internal.cli.editor;

import org.eclipse.ui.forms.IManagedForm;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.json.ui.AbstractFormPage;

/**
 * Overview page for angular-cli.json editor.
 *
 */
public class OverviewPage extends AbstractFormPage {

	private static final String ID = "overview";

	public OverviewPage(AngularCLIEditor editor) {
		super(editor, ID, AngularCLIMessages.AngularCLIEditor_OverviewPage_title);
	}

	@Override
	protected void createUI(IManagedForm managedForm) {

	}

}
