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
package ts.eclipse.ide.angular.internal.cli.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import ts.eclipse.ide.angular.internal.cli.AngularCLIMessages;

/**
 * Wizard to generate Angular Guard with "ng generate Guard $name".
 *
 */
public class NewNgGuardWizard extends AbstractNewNgGenerateWizard {

	public NewNgGuardWizard() {
	}

	@Override
	protected NgGenerateBlueprintWizardPage createMainPage(IContainer folder) {
		return new NewNgGuardWizardPage(folder);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		super.setWindowTitle(AngularCLIMessages.NewNgGuardWizard_windowTitle);
	}

	@Override
	protected void appendOperationParameters(StringBuilder sb) {
		super.appendOperationParameters(sb);
		NewNgGuardWizardPage mainPage = (NewNgGuardWizardPage)getMainPage();
		sb.append(' ').append("--flat ").append(mainPage.isFlat());
		sb.append(' ').append("--spec ").append(mainPage.isSpec());
	}
}
