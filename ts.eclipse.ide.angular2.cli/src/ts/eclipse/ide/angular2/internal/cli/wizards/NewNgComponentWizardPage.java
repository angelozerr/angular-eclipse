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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular2.internal.cli.json.AngularCLIJson;
import ts.eclipse.ide.angular2.internal.cli.json.GenerateDefaults;

/**
 * Wizard page for Angular2 Component.
 *
 */
public class NewNgComponentWizardPage extends NgGenerateBlueprintWizardPage {

	private static final String PAGE_NAME = "ngComponent";

	private Text txtPrefix;
	private Button chkFlat;
	private Button chkInlineTemplate;
	private Button chkInlineStyle;
	private Button chkSpec;
	private Button chkSkipImport;
	private Button chkExport;
	private Combo cbViewEncapsulation;
	private Combo cbChangeDetection;

	protected NewNgComponentWizardPage(IContainer folder) {
		super(PAGE_NAME, AngularCLIMessages.NewNgComponentWizardPage_title, null, NgBlueprint.COMPONENT, folder);
		super.setDescription(AngularCLIMessages.NewNgComponentWizardPage_description);
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

		// Container
		Composite subGroup = new Composite(paramsGroup, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		subGroup.setLayout(layout);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.horizontalSpan = 2;
		subGroup.setLayoutData(data);
		subGroup.setFont(font);

		// Label for prefix
		Label label = new Label(subGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_prefix);
		label.setFont(font);

		// Textfield for prefix
		txtPrefix = new Text(subGroup, SWT.BORDER);
		txtPrefix.addListener(SWT.Modify, this);
		data = new GridData(GridData.FILL_HORIZONTAL);
		txtPrefix.setLayoutData(data);
		txtPrefix.setFont(font);

		// View encapsulation
		label = new Label(subGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NewNgComponentWizardPage_viewEncapsulation);
		label.setFont(font);

		// Combobox for view encapsulation
		cbViewEncapsulation = new Combo(subGroup, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cbViewEncapsulation.addListener(SWT.Modify, this);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		cbViewEncapsulation.setLayoutData(data);

		// Data for view encapsulation
		cbViewEncapsulation.add(GenerateDefaults.VE_EMULATED);
		cbViewEncapsulation.add(GenerateDefaults.VE_NATIVE);
		cbViewEncapsulation.add(GenerateDefaults.VE_NONE);

		// Change detection
		label = new Label(subGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NewNgComponentWizardPage_changeDetection);
		label.setFont(font);

		// Combobox for change detection
		cbChangeDetection = new Combo(subGroup, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cbChangeDetection.addListener(SWT.Modify, this);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		cbChangeDetection.setLayoutData(data);

		// Data for change detection
		cbChangeDetection.add(GenerateDefaults.CD_ON_PUSH);
		cbChangeDetection.add(GenerateDefaults.CD_DEFAULT);

		// Checkbox for flat
		chkFlat = new Button(paramsGroup, SWT.CHECK);
		chkFlat.addListener(SWT.Selection, this);
		chkFlat.setText(AngularCLIMessages.NgGenerateBlueprintWizardPage_flat);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkFlat.setLayoutData(data);

		// Checkbox for inline template
		chkInlineTemplate = new Button(paramsGroup, SWT.CHECK);
		chkInlineTemplate.addListener(SWT.Selection, this);
		chkInlineTemplate.setText(AngularCLIMessages.NewNgComponentWizardPage_inlineTemplate);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkInlineTemplate.setLayoutData(data);

		// Checkbox for inline Style
		chkInlineStyle = new Button(paramsGroup, SWT.CHECK);
		chkInlineStyle.addListener(SWT.Selection, this);
		chkInlineStyle.setText(AngularCLIMessages.NewNgComponentWizardPage_inlineStyle);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkInlineStyle.setLayoutData(data);

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
		GenerateDefaults gDefaults = getAngularCLIJson().getGenerateDefaults(NgBlueprint.COMPONENT);
		String prefix = getAngularCLIJson().getPrefix(NgBlueprint.COMPONENT);
		if (prefix != null)
			txtPrefix.setText(prefix);
		chkFlat.setSelection(gDefaults != null ? gDefaults.isFlat() : false);
		chkSpec.setSelection(gDefaults != null ? gDefaults.isInlineStyle() : true);
		chkInlineStyle.setSelection(gDefaults != null ? gDefaults.isInlineStyle() : false);
		chkInlineTemplate.setSelection(gDefaults != null ? gDefaults.isInlineTemplate() : false);
		cbViewEncapsulation.select(cbViewEncapsulation.indexOf(gDefaults != null ? gDefaults.getViewEncapsulation() : GenerateDefaults.VE_EMULATED));
		cbChangeDetection.select(cbChangeDetection.indexOf(gDefaults != null ? gDefaults.getChangeDetection() : GenerateDefaults.CD_DEFAULT));
	}

	@Override
	protected String[] getGeneratedFilesImpl() {
		AngularCLIJson cliJson = getAngularCLIJson();
		String name = getBlueprintName();
		int cnt = isSpec() ? 4 : 3;
		if (isInlineTemplate())
			cnt--;
		if (isInlineStyle())
			cnt--;
		String[] files = new String[cnt];
		String folderName = isFlat() ? "" : cliJson.getFolderName(name);
		files[0] = folderName.concat(cliJson.getComponentTsFileName(name));
		int i = 1;
		if (isSpec())
			files[i++] = folderName.concat(cliJson.getComponentSpecFileName(name));
		if (!isInlineTemplate())
			files[i++] = folderName.concat(cliJson.getComponentTemplateFileName(name));
		if (!isInlineStyle())
			files[i++] = folderName.concat(cliJson.getComponentStyleFileName(name));
		return files;
	}

	public String getPrefix() {
		return txtPrefix.getText();
	}

	public boolean isInlineTemplate() {
		return chkInlineTemplate.getSelection();
	}

	public boolean isInlineStyle() {
		return chkInlineStyle.getSelection();
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

	public String getViewEncapsulation() {
		return cbViewEncapsulation.getText();
	}

	public String getChangeDetection() {
		return cbChangeDetection.getText();
	}

}