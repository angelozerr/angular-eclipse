/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.internal.cli.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import ts.eclipse.ide.ui.wizards.WizardNewTypeScriptProjectCreationPage;

/**
 * Main wizard page to create an Angular2 project.
 *
 */
public class WizardNewNgProjectCreationPage extends WizardNewTypeScriptProjectCreationPage {

	public WizardNewNgProjectCreationPage(String pageName, BasicNewResourceWizard wizard) {
		super(pageName, wizard);
	}

	@Override
	protected boolean validatePage() {
		if (super.validatePage()) {
			IStatus nameStatus = validateNgProjectName(getProjectName());
			if (!nameStatus.isOK()) {
				setErrorMessage(nameStatus.getMessage());
				return false;
			}
		}
		return false;
	}

	private IStatus validateNgProjectName(String name) {
		// TODO: validate name and returns Error status if error.
		return Status.OK_STATUS;
	}

}
