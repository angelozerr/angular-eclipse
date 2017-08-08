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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import ts.eclipse.ide.angular.cli.NgBlueprint;
import ts.eclipse.ide.angular.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular.internal.cli.json.AngularCLIJson;
import ts.eclipse.ide.angular.internal.cli.json.GenerateDefaults;

/**
 * Wizard page for Angular Module.
 *
 */
public class NewNgModuleWizardPage extends NgGenerateBlueprintWizardPage {

	private static final String PAGE_NAME = "ngModule";

	private Button chkRouting;
	private Button chkSpec;
	private Button chkFlat;

	protected NewNgModuleWizardPage(IContainer folder) {
		super(PAGE_NAME, AngularCLIMessages.NewNgModuleWizardPage_title, null, NgBlueprint.MODULE, folder);
		super.setDescription(AngularCLIMessages.NewNgModuleWizardPage_description);
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

		// Checkbox for routing
		chkRouting = new Button(paramsGroup, SWT.CHECK);
		chkRouting.addListener(SWT.Selection, this);
		chkRouting.setText(AngularCLIMessages.NewNgModuleWizardPage_routing);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkRouting.setLayoutData(data);

		// Checkbox for spec
		chkSpec = new Button(paramsGroup, SWT.CHECK);
		chkSpec.addListener(SWT.Selection, this);
		chkSpec.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_generate_spec);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSpec.setLayoutData(data);

		// Checkbox for flat
		chkFlat = new Button(paramsGroup, SWT.CHECK);
		chkFlat.addListener(SWT.Selection, this);
		chkFlat.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_flat);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkFlat.setLayoutData(data);
	}

	@Override
	protected void initializeDefaultValues() {
		super.initializeDefaultValues();
		GenerateDefaults gDefaults = getAngularCLIJson().getGenerateDefaults(NgBlueprint.MODULE);
		chkSpec.setSelection(gDefaults != null ? gDefaults.isSpec() : false);
		chkFlat.setSelection(gDefaults != null ? gDefaults.isFlat() : false);
	}

	@Override
	protected String[] getGeneratedFilesImpl() {
		AngularCLIJson cliJson = getAngularCLIJson();
		String name = getBlueprintName();
		int cnt = isSpec() ? 2 : 1;
		if (isRouting())
			cnt ++;
		String[] files = new String[cnt];
		String folderName = isFlat() ? "" : cliJson.getFolderName(name);
		int i = 0;
		files[i++] = folderName.concat(cliJson.getModuleFileName(name));
		if (isSpec())
			files[i++] = folderName.concat(cliJson.getModuleSpecFileName(name));
		if (isRouting())
			files[i++] = folderName.concat(cliJson.getRoutingModuleFileName(name));
		return files;
	}

	public boolean isFlat() {
		return chkFlat.getSelection();
	}

	public boolean isRouting() {
		return chkRouting.getSelection();
	}

	public boolean isSpec() {
		return chkSpec.getSelection();
	}
}