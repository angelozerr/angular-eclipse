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

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;

/**
 * Wizard to generate Angular2 Class with "ng generate class $name".
 *
 */
public class NewNgClassWizard extends AbstractNewNgGenerateWizard {

	public NewNgClassWizard() {
	}

	@Override
	protected NgGenerateBlueprintWizardPage createMainPage(IProject project) {
		return new NewNgClassWizardPage(project);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		super.setWindowTitle(AngularCLIMessages.NewNgClassWizard_windowTitle);
	}

}
