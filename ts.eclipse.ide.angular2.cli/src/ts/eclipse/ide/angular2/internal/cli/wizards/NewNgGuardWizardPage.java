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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.json.AngularCLIJson;

/**
 * Wizard page for Angular2 Guard.
 *
 */
public class NewNgGuardWizardPage extends NgGenerateBlueprintWizardPage {

	private static final String PAGE_NAME = "ngGuard";

	private Button chkFlat;
	private Button chkSpec;

	protected NewNgGuardWizardPage(IContainer folder) {
		super(PAGE_NAME, AngularCLIMessages.NewNgGuardWizardPage_title, null, NgBlueprint.GUARD, folder);
		super.setDescription(AngularCLIMessages.NewNgGuardWizardPage_description);
	}

	@Override
	public void createParamsControl(Composite parent) {
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

		// Checkbox for flat
		chkFlat = new Button(paramsGroup, SWT.CHECK);
		chkFlat.addListener(SWT.Selection, this);
		chkFlat.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_flat);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkFlat.setLayoutData(data);

		// Checkbox for spec
		chkSpec = new Button(paramsGroup, SWT.CHECK);
		chkSpec.addListener(SWT.Selection, this);
		chkSpec.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_generate_spec);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSpec.setLayoutData(data);
	}

	@Override
	protected void initializeDefaultValues() {
		super.initializeDefaultValues();
		chkSpec.setSelection(getAngularCLIJson().isSpec(NgBlueprint.GUARD));
		chkFlat.setSelection(true);
	}

	@Override
	protected String[] getGeneratedFilesImpl() {
		AngularCLIJson cliJson = getAngularCLIJson();
		String name = getBlueprintName();
		int cnt = isSpec() ? 2 : 1;
		String[] files = new String[cnt];
		String folderName = isFlat() ? "" : cliJson.getFolderName(name);
		files[0] = folderName.concat(cliJson.getGuardFileName(name));
		if (isSpec())
			files[1] = folderName.concat(cliJson.getGuardSpecFileName(name));
		return files;
	}

	public boolean isFlat() {
		return chkFlat.getSelection();
	}

	public boolean isSpec() {
		return chkSpec.getSelection();
	}

}
