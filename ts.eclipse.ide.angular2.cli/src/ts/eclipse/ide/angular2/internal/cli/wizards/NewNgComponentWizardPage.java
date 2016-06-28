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

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;

/**
 * Wizard page for Angular2 Component.
 *
 */
public class NewNgComponentWizardPage extends NgGenerateBlueprintWizardPage {

	private static final String PAGE_NAME = "ngComponent";

	protected NewNgComponentWizardPage(IProject project) {
		super(PAGE_NAME, AngularCLIMessages.NewNgComponentWizardPage_title, null, NgBlueprint.COMPONENT, project);
		super.setDescription(AngularCLIMessages.NewNgComponentWizardPage_description);
	}

}
