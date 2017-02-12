/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *
 */
package ts.eclipse.ide.angular2.internal.cli.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;

/**
 * Wizard to generate Angular2 Component with "ng generate component $name".
 *
 */
public class NewNgComponentWizard extends AbstractNewNgGenerateWizard {

	public NewNgComponentWizard() {
	}

	@Override
	protected NgGenerateBlueprintWizardPage createMainPage(IContainer folder) {
		return new NewNgComponentWizardPage(folder);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		super.setWindowTitle(AngularCLIMessages.NewNgComponentWizard_windowTitle);
	}

	@Override
	protected void appendOperationParameters(StringBuilder sb) {
		super.appendOperationParameters(sb);
		NewNgComponentWizardPage mainPage = (NewNgComponentWizardPage)getMainPage();
		sb.append(' ').append("--prefix ").append(mainPage.getPrefix());
		if (mainPage.isInlineTempalte())
			sb.append(' ').append("--inline-template");
		if (mainPage.isInlineStyle())
			sb.append(' ').append("--inline-style");
		if (mainPage.isSkipImport())
			sb.append(' ').append("--skip-import");
		if (mainPage.isExport())
			sb.append(' ').append("--export");
		sb.append(' ').append("--spec ").append(mainPage.isSpec());
		sb.append(' ').append("--flat ").append(mainPage.isFlat());
		String viewEncapsulation = mainPage.getViewEncapsulation();
		if (!NewNgComponentWizardPage.VE_EMULATED.equals(viewEncapsulation))
			sb.append(' ').append("--view-encapsulation ").append(viewEncapsulation);
		String changeDetection = mainPage.getChangeDetection();
		if (!NewNgComponentWizardPage.CD_DEFAULT.equals(changeDetection))
			sb.append(' ').append("--change-detection ").append(changeDetection);
	}
}
