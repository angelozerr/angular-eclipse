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

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.terminal.interpreter.LineCommand;
import ts.eclipse.ide.ui.wizards.AbstractWizardPage;

/**
 * Wizard page for the optional Project-Parameters.
 *
 */
public class NewAngular2ProjectParamsWizardPage extends AbstractWizardPage {

	private static final String PAGE_NAME = "newProjectParamsPage";

	// TODO usefull?
	// --dry-run (Boolean) (Default: false)
	// aliases: -d
	// --verbose (Boolean) (Default: false)
	// aliases: -v
	// --link-cli (Boolean) (Default: false)
	// aliases: -lc
	// --directory (String)
	// aliases: -dir <value>
	private boolean skipInstall;
	private boolean skipGit;
	private boolean skipTests;
	private boolean skipCommit;
	private String sourceDir;
	private String style;
	private String prefix;
	private boolean routing;
	private boolean inlineStyle;
	private boolean inlineTemplate;

	/**
	 * --skip-install (Boolean) (Default: false) aliases: -si, --skipInstall
	 */
	private Button chkSkipInstall;

	/**
	 * --skip-git (Boolean) (Default: false) aliases: -sg
	 */
	private Button chkSkipGit;

	/**
	 * --skip-tests (Boolean) (Default: false) aliases: -st
	 */
	private Button chkSkipTests;

	/**
	 * --skip-commit (Boolean) (Default: false) aliases: -sc
	 */
	private Button chkSkipCommit;

	/**
	 * --source-dir (String) (Default: src) aliases: -sd <value>
	 */
	private Text txtSourceDir;

	/** --style (String) (Default: css) */
	private Combo cbStyle;

	/**
	 * --prefix (String) (Default: app) aliases: -p <value>
	 */
	private Text txtPrefix;

	// --mobile (Boolean) (Default: false) TODO disabled temporarily

	/** --routing (Boolean) (Default: false) */
	private Button chkRouting;

	/**
	 * --inline-style (Boolean) (Default: false) aliases: -is
	 */
	private Button chkInlineStyle;

	/**
	 * --inline-template (Boolean) (Default: false) aliases: -it
	 */
	private Button chkInlineTemplate;

	protected NewAngular2ProjectParamsWizardPage() {
		super(PAGE_NAME, AngularCLIMessages.NewAngular2ProjectParamsWizardPage_title, null);
	}

	@Override
	public void createBody(Composite parent) {
		Font font = parent.getFont();

		// params group
		Composite paramsGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = 0;
		paramsGroup.setLayout(layout);
		paramsGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		paramsGroup.setFont(font);

		Label label;
		GridData data;
		GC gc;
		FontMetrics fm;

		// Source dir
		label = new Label(paramsGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_sourceDir);
		label.setFont(font);

		// Text for source dir
		txtSourceDir = new Text(paramsGroup, SWT.BORDER);
		txtSourceDir.addListener(SWT.Modify, this);
		gc = new GC(txtSourceDir);
		fm = gc.getFontMetrics();
		txtSourceDir.setSize(10 * fm.getAverageCharWidth(), fm.getHeight());
		gc.dispose();
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.horizontalSpan = 2;
		txtSourceDir.setLayoutData(data);

		// Prefix
		label = new Label(paramsGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_prefix);
		label.setFont(font);

		// Text for prefix
		txtPrefix = new Text(paramsGroup, SWT.BORDER);
		txtPrefix.addListener(SWT.Modify, this);
		gc = new GC(txtPrefix);
		fm = gc.getFontMetrics();
		txtPrefix.setSize(10 * fm.getAverageCharWidth(), fm.getHeight());
		gc.dispose();
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.horizontalSpan = 2;
		txtPrefix.setLayoutData(data);

		// Style
		label = new Label(paramsGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_style);
		label.setFont(font);

		// Combobox for style
		cbStyle = new Combo(paramsGroup, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cbStyle.addListener(SWT.Modify, this);
		gc = new GC(cbStyle);
		fm = gc.getFontMetrics();
		cbStyle.setSize(10 * fm.getAverageCharWidth(), fm.getHeight());
		gc.dispose();
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.horizontalSpan = 2;
		cbStyle.setLayoutData(data);

		// Data for Combobox style
		cbStyle.add("css");
		cbStyle.add("scss");
		cbStyle.add("sass");
		cbStyle.add("less");
		cbStyle.add("styl");

		// Separator
		label = new Label(paramsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		label.setLayoutData(data);

		// Skip
		label = new Label(paramsGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_skip);
		label.setFont(font);

		// Checkbox for Skip install
		chkSkipInstall = new Button(paramsGroup, SWT.CHECK);
		chkSkipInstall.addListener(SWT.Selection, this);
		chkSkipInstall.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_install);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSkipInstall.setLayoutData(data);

		// Checkbox for Skip Git
		chkSkipGit = new Button(paramsGroup, SWT.CHECK);
		chkSkipGit.addListener(SWT.Selection, this);
		chkSkipGit.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_git);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSkipGit.setLayoutData(data);

		// Fill empty column
		label = new Label(paramsGroup, SWT.NONE);

		// Checkbox for Skip tests
		chkSkipTests = new Button(paramsGroup, SWT.CHECK);
		chkSkipTests.addListener(SWT.Selection, this);
		chkSkipTests.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_tests);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSkipTests.setLayoutData(data);

		// Checkbox for Skip commit
		chkSkipCommit = new Button(paramsGroup, SWT.CHECK);
		chkSkipCommit.addListener(SWT.Selection, this);
		chkSkipCommit.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_commit);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSkipCommit.setLayoutData(data);

		// Separator
		label = new Label(paramsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		label.setLayoutData(data);

		// Inline
		label = new Label(paramsGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_inline);
		label.setFont(font);

		// Checkbox for inline style
		chkInlineStyle = new Button(paramsGroup, SWT.CHECK);
		chkInlineStyle.addListener(SWT.Selection, this);
		chkInlineStyle.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_inlineStyle);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkInlineStyle.setLayoutData(data);

		// Fill empty columns
		label = new Label(paramsGroup, SWT.NONE);
		label = new Label(paramsGroup, SWT.NONE);

		// Checkbox for inline tempalte
		chkInlineTemplate = new Button(paramsGroup, SWT.CHECK);
		chkInlineTemplate.addListener(SWT.Selection, this);
		chkInlineTemplate.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_inlineTemplate);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkInlineTemplate.setLayoutData(data);

		// Fill empty columns
		label = new Label(paramsGroup, SWT.NONE);

		// Separator
		label = new Label(paramsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		label.setLayoutData(data);

		// Routing
		label = new Label(paramsGroup, SWT.NONE);
		label.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_routing);
		label.setFont(font);

		// Checkbox for Routing
		chkRouting = new Button(paramsGroup, SWT.CHECK);
		chkRouting.addListener(SWT.Selection, this);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkRouting.setLayoutData(data);

		// Fill empty columns
		label = new Label(paramsGroup, SWT.NONE);
	}

	@Override
	protected void initializeDefaultValues() {
		// chkSkipInstall.setSelection(false);
		// chkSkipGit.setSelection(false);
		// chkSkipTests.setSelection(false);
		// chkSkipCommit.setSelection(false);
		txtSourceDir.setText("src");
		cbStyle.select(cbStyle.indexOf("css"));
		txtPrefix.setText("app");
		// chkRouting.setSelection(false);
		// chkInlineStyle.setSelection(false);
		// chkInlineTemplate.setSelection(false);
	}

	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);
		Widget item = event != null ? event.item : null;
		if (event != null && item == null)
			item = event.widget;
		if (item == chkSkipInstall)
			skipInstall = chkSkipInstall.getSelection();
		else if (item == chkSkipGit)
			skipGit = chkSkipGit.getSelection();
		else if (item == chkSkipTests)
			skipTests = chkSkipTests.getSelection();
		else if (item == chkSkipCommit)
			skipCommit = chkSkipCommit.getSelection();
		else if (item == txtSourceDir)
			sourceDir = txtSourceDir.getText();
		else if (item == cbStyle)
			style = cbStyle.getText();
		else if (item == txtPrefix)
			prefix = txtPrefix.getText();
		else if (item == chkRouting)
			routing = chkRouting.getSelection();
		else if (item == chkInlineStyle)
			inlineStyle = chkInlineStyle.getSelection();
		else if (item == chkInlineTemplate)
			inlineTemplate = chkInlineTemplate.getSelection();
	}

	protected IStatus[] validatePage() {
		return null;
	}

	public String getParamsString() {
		StringBuilder sbParams = new StringBuilder();

		if (skipInstall)
			sbParams.append("-si").append(' ');

		if (skipGit)
			sbParams.append("-sg").append(' ');

		if (skipTests)
			sbParams.append("-st").append(' ');

		if (skipCommit)
			sbParams.append("-sc").append(' ');

		if (sourceDir != null && sourceDir.length() > 0 && !sourceDir.equals("src"))
			sbParams.append("-sd").append(' ').append(sourceDir).append(' ');

		if (style != null && style.length() > 0 && !style.equalsIgnoreCase("CSS"))
			sbParams.append("--style").append(' ').append(style).append(' ');

		if (prefix != null && prefix.length() > 0 && !prefix.equals("app"))
			sbParams.append("-p").append(' ').append(prefix).append(' ');

		if (routing)
			sbParams.append("--routing").append(' ');

		if (inlineStyle)
			sbParams.append("-is").append(' ');

		if (inlineTemplate)
			sbParams.append("-it").append(' ');

		sbParams.append("-dir ./"); // Directory is already created

		return sbParams.toString();
	}

	@Override
	public void updateCommand(List<LineCommand> commands, IProject project) {
		StringBuilder sbCommand = new StringBuilder().append("ng new ").append(project.getName()).append(" ")
				.append(getParamsString());
		commands.add(new LineCommand(sbCommand.toString()));
		if (!skipInstall) {
			// by waiting for https://github.com/angular/angular-cli/issues/6125
			// force the install of tslint-language-service.
			commands.add(new LineCommand("npm install tslint-language-service"));
		}
	}
}
