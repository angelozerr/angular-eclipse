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

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.ui.wizards.WizardNewTypeScriptProjectCreationPage;

/**
 * Main wizard page to create an Angular2 project.
 *
 */
public class WizardNewNgProjectCreationPage extends WizardNewTypeScriptProjectCreationPage {

	public static final String projectNameRegexp = "^[a-zA-Z][.0-9a-zA-Z]*(-[.0-9a-zA-Z]*)*$";
	public static final String[] unsupportedProjectNames = new String[] { "test", "ember", "ember-cli", "vendor", "app" };

	private static final Status ERROR_STATUS = new Status(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID, AngularCLIMessages.NewAngular2ProjectWizard_invalidProjectName);

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
		return true;
	}

	private IStatus validateNgProjectName(String name) {
		IStatus status = name != null && name.length() > 0 ? Status.OK_STATUS : ERROR_STATUS;
		for (int i = 0, cnt = unsupportedProjectNames.length; i < cnt; i++) {
			if (unsupportedProjectNames[i].equals(name)) {
				status = ERROR_STATUS;
				break;
			}
		}
		if (status.isOK()) {
			String[] parts = name.split("-");
			for (int i = 0, cnt = parts.length; i < cnt; i++) {
				if (!parts[i].matches(projectNameRegexp)) {
					status = ERROR_STATUS;
					break;
				}
			}
		}
		return status;
	}

}
