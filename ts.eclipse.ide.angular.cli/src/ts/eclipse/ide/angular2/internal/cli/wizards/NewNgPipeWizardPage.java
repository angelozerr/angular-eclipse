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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.json.AngularCLIJson;
import ts.eclipse.ide.angular2.internal.cli.json.GenerateDefaults;

/**
 * Wizard page for Angular2 Pipe.
 *
 */
public class NewNgPipeWizardPage extends NgGenerateBlueprintWizardPage {

	private static final String PAGE_NAME = "ngPipe";

	private Button chkFlat;
	private Button chkSpec;
	private Button chkSkipImport;
	private Button chkExport;

	protected NewNgPipeWizardPage(IContainer folder) {
		super(PAGE_NAME, AngularCLIMessages.NewNgPipeWizardPage_title, null, NgBlueprint.PIPE, folder);
		super.setDescription(AngularCLIMessages.NewNgPipeWizardPage_description);
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

		// Checkbox for skip-import
		chkSkipImport = new Button(paramsGroup, SWT.CHECK);
		chkSkipImport.addListener(SWT.Selection, this);
		chkSkipImport.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_skipImport);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSkipImport.setLayoutData(data);

		// Checkbox for export
		chkExport = new Button(paramsGroup, SWT.CHECK);
		chkExport.addListener(SWT.Selection, this);
		chkExport.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_export);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkExport.setLayoutData(data);
	}

	@Override
	protected void initializeDefaultValues() {
		super.initializeDefaultValues();
		GenerateDefaults gDefaults = getAngularCLIJson().getGenerateDefaults(NgBlueprint.PIPE);
		chkFlat.setSelection(gDefaults != null ? gDefaults.isFlat() : true);
		chkSpec.setSelection(gDefaults != null ? gDefaults.isSpec() : false);
	}

	@Override
	protected String[] getGeneratedFilesImpl() {
		AngularCLIJson cliJson = getAngularCLIJson();
		String name = getBlueprintName();
		int cnt = isSpec() ? 2 : 1;
		String[] files = new String[cnt];
		String folderName = isFlat() ? "" : cliJson.getFolderName(name);
		files[0] = folderName.concat(cliJson.getPipeFileName(name));
		if (isSpec())
			files[1] = folderName.concat(cliJson.getPipeSpecFileName(name));
		return files;
	}

	public boolean isFlat() {
		return chkFlat.getSelection();
	}

	public boolean isSpec() {
		return chkSpec.getSelection();
	}

	public boolean isSkipImport() {
		return chkSkipImport.getSelection();
	}

	public boolean isExport() {
		return chkExport.getSelection();
	}

}