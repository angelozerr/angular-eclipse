/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Springrbua - initial implementation
 *
 */
package ts.eclipse.ide.angular2.internal.cli.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;

/**
 * Wizard to generate Angular2 Module with "ng generate module $name".
 *
 */
public class NewNgModuleWizard extends AbstractNewNgGenerateWizard {

	public NewNgModuleWizard() {
	}

	@Override
	protected NgGenerateBlueprintWizardPage createMainPage(IContainer folder) {
		return new NewNgModuleWizardPage(folder);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		super.setWindowTitle(AngularCLIMessages.NewNgModuleWizard_windowTitle);
	}

	@Override
	protected void appendOperationParameters(StringBuilder sb) {
		super.appendOperationParameters(sb);
		NewNgModuleWizardPage mainPage = (NewNgModuleWizardPage)getMainPage();
		if (mainPage.isRouting())
			sb.append(' ').append("--routing");
	}
}
