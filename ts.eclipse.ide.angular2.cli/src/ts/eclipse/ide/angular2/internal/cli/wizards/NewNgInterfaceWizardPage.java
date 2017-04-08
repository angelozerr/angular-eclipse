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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.json.AngularCLIJson;

/**
 * Wizard page for Angular2 Interface.
 *
 */
public class NewNgInterfaceWizardPage extends NgGenerateBlueprintWizardPage {

	private static final String PAGE_NAME = "ngInterface";

	private Text txtPrefix;

	protected NewNgInterfaceWizardPage(IContainer folder) {
		super(PAGE_NAME, AngularCLIMessages.NewNgInterfaceWizardPage_title, null, NgBlueprint.INTERFACE, folder);
		super.setDescription(AngularCLIMessages.NewNgInterfaceWizardPage_description);
	}

	@Override
	protected void createParamsControl(Composite parent) {
		super.createParamsControl(parent);
		Font font = parent.getFont();

		// params group
		Composite paramsGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		paramsGroup.setLayout(layout);
		paramsGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		paramsGroup.setFont(font);

		// Label for prefix
		Label label = new Label(paramsGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_prefix);
		label.setFont(font);

		// Textfield for prefix
		txtPrefix = new Text(paramsGroup, SWT.BORDER);
		txtPrefix.addListener(SWT.Modify, this);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		txtPrefix.setLayoutData(data);
		txtPrefix.setFont(font);
	}

	@Override
	protected void initializeDefaultValues() {
		super.initializeDefaultValues();
		String prefix = getAngularCLIJson().getPrefix(NgBlueprint.INTERFACE);
		if (prefix != null)
			txtPrefix.setText(prefix);
	}

	@Override
	protected String[] getGeneratedFilesImpl() {
		AngularCLIJson cliJson = getAngularCLIJson();
		String name = getBlueprintName();
		return new String[] { cliJson.getInterfaceFileName(name, getPrefix()) };
	}

	public String getPrefix() {
		return txtPrefix.getText();
	}

}
