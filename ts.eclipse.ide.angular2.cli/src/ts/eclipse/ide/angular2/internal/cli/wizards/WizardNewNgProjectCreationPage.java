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
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.ui.utils.StatusUtil;
import ts.eclipse.ide.ui.wizards.AbstractWizardNewTypeScriptProjectCreationPage;

/**
 * Main wizard page to create an Angular2 project.
 *
 */
public class WizardNewNgProjectCreationPage extends AbstractWizardNewTypeScriptProjectCreationPage {

	public static final String projectNameRegexp = "^[a-zA-Z][.0-9a-zA-Z]*(-[.0-9a-zA-Z]*)*$";
	public static final String[] unsupportedProjectNames = new String[] {
		"test",
		"ember",
		"ember-cli",
		"vendor",
		"app"
	};

	public WizardNewNgProjectCreationPage(String pageName, BasicNewResourceWizard wizard) {
		super(pageName, wizard);
	}

	@Override
	protected IStatus validatePageImpl() {
		return StatusUtil.getMoreSevere(super.validatePageImpl(), validateNgProjectName(getProjectName()));
	}

	/** Validates the name of the Angular-CLI Project. */
	private IStatus validateNgProjectName(String name) {
		IStatus status = Status.OK_STATUS;
		for (int i = 0, cnt = unsupportedProjectNames.length; i < cnt; i++) {
			if (unsupportedProjectNames[i].equals(name)) {
				status = new Status(IStatus.WARNING, AngularCLIPlugin.PLUGIN_ID,
						NLS.bind(AngularCLIMessages.NewAngular2ProjectWizard_unsupportedProjectNames, name));
				break;
			}
		}
		if (status.isOK()) {
			String[] parts = name.split("-");
			for (int i = 0, cnt = parts.length; i < cnt; i++) {
				if (!parts[i].matches(projectNameRegexp)) {
					status = new Status(IStatus.WARNING, AngularCLIPlugin.PLUGIN_ID,
							NLS.bind(AngularCLIMessages.NewAngular2ProjectWizard_invalidProjectName, name));
					break;
				}
			}
		}
		return status;
	}

}
