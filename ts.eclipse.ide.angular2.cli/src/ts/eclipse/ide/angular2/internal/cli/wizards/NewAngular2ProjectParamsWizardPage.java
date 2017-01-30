package ts.eclipse.ide.angular2.internal.cli.wizards;

import org.eclipse.jface.wizard.WizardPage;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;

public class NewAngular2ProjectParamsWizardPage extends WizardPage implements Listener {

	private static final String PAGE_NAME	= "newProjectParamsPage";

	
	// TODO usefull?
	//--dry-run (Boolean) (Default: false)
	//  aliases: -d
	//--verbose (Boolean) (Default: false)
	//  aliases: -v
	//--link-cli (Boolean) (Default: false)
	//  aliases: -lc
	//--directory (String)
	//  aliases: -dir <value>
	
	/**
	 * --skip-npm (Boolean) (Default: false)
	 * aliases: -sn
	 */
	private Button chkSkipNpm;
	
	/**
	 * --skip-git (Boolean) (Default: false)
	 * aliases: -sg
	 */
	private Button chkSkipGit;
	
	/**
	 * --skip-tests (Boolean) (Default: false)
	 * aliases: -st
	 */
	private Button chkSkipTests;
	
	/**
	 * --skip-commit (Boolean) (Default: false)
	 * aliases: -sc
	 */
	private Button chkSkipCommit;
	
	/**
	 * --source-dir (String) (Default: src)
	 * aliases: -sd <value>
	 */
	private Text txtSourceDir;
	
	/** --style (String) (Default: css) */
	private Combo cbStyle;
	
	/**
	 * --prefix (String) (Default: app)
	 * aliases: -p <value>
	 */
	private Text txtPrefix;
	
	//--mobile (Boolean) (Default: false) TODO disabled temporarily
	
	/** --routing (Boolean) (Default: false) */
	private Button chkRouting;
	
	/**
	 * --inline-style (Boolean) (Default: false)
	 * aliases: -is
	 */
	private Button chkInlineStyle;
	
	/**
	 * --inline-template (Boolean) (Default: false)
	 * aliases: -it
	 */
	private Button chkInlineTemplate;
	
	
	protected NewAngular2ProjectParamsWizardPage() {
		super(PAGE_NAME, AngularCLIMessages.NewAngular2ProjectParamsWizardPage_title, null);
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite topLevel = new Composite(parent, SWT.NONE);
		topLevel.setLayout(new GridLayout());
		topLevel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		topLevel.setFont(parent.getFont());

		createParamsControl(topLevel);

		// initialize page with default values
		initializeDefaultValues();
		
		validatePage();
		
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(topLevel);
	}
	
	protected void createParamsControl(Composite parent) {
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
		
		// Checkbox for Skip npm
		chkSkipNpm = new Button(paramsGroup, SWT.CHECK);
		chkSkipNpm.addListener(SWT.Modify, this);
		chkSkipNpm.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_npm);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSkipNpm.setLayoutData(data);
		
		// Checkbox for Skip Git
		chkSkipGit = new Button(paramsGroup, SWT.CHECK);
		chkSkipGit.addListener(SWT.Modify, this);
		chkSkipGit.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_git);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSkipGit.setLayoutData(data);
		
		// Fill empty column
		label = new Label(paramsGroup, SWT.NONE);
		
		// Checkbox for Skip tests
		chkSkipTests = new Button(paramsGroup, SWT.CHECK);
		chkSkipTests.addListener(SWT.Modify, this);
		chkSkipTests.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_tests);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkSkipTests.setLayoutData(data);
		
		// Checkbox for Skip commit
		chkSkipCommit = new Button(paramsGroup, SWT.CHECK);
		chkSkipCommit.addListener(SWT.Modify, this);
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
		chkInlineStyle.addListener(SWT.Modify, this);
		chkInlineStyle.setText(AngularCLIMessages.NewAngular2ProjectParamsWizardPage_inlineStyle);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkInlineStyle.setLayoutData(data);
		
		// Fill empty columns
		label = new Label(paramsGroup, SWT.NONE);
		label = new Label(paramsGroup, SWT.NONE);
		
		// Checkbox for inline tempalte
		chkInlineTemplate = new Button(paramsGroup, SWT.CHECK);
		chkInlineTemplate.addListener(SWT.Modify, this);
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
		chkRouting.addListener(SWT.Modify, this);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		chkRouting.setLayoutData(data);
		
		// Fill empty columns
		label = new Label(paramsGroup, SWT.NONE);
	}

	private void initializeDefaultValues() {
		//chkSkipNpm.setSelection(false);
		//chkSkipGit.setSelection(false);
		//chkSkipTests.setSelection(false);
		//chkSkipCommit.setSelection(false);
		txtSourceDir.setText("src");
		cbStyle.select(cbStyle.indexOf("css"));
		txtPrefix.setText("app");
		//chkRouting.setSelection(false);
		//chkInlineStyle.setSelection(false);
		//chkInlineTemplate.setSelection(false);
	}
	
	@Override
	public void handleEvent(Event event) {
		setPageComplete(validatePage());
	}

	protected boolean validatePage() {
		// TODO Validation required?
		return true;
	}

	public boolean isSkipNpm() {
		return chkSkipNpm.getSelection();
	}

	public boolean isSkipGit() {
		return chkSkipGit.getSelection();
	}

	public boolean isSkipTests() {
		return chkSkipTests.getSelection();
	}

	public boolean isSkipCommit() {
		return chkSkipCommit.getSelection();
	}

	public String getSourceDir() {
		return txtSourceDir.getText();
	}

	public String getStyle() {
		return cbStyle.getText();
	}

	public String getPrefix() {
		return txtPrefix.getText();
	}

	public boolean isRouting() {
		return chkRouting.getSelection();
	}

	public boolean isInlineStyle() {
		return chkInlineStyle.getSelection();
	}

	public boolean isInlineTemplate() {
		return chkInlineTemplate.getSelection();
	}

	public String getParamsString() {
		StringBuilder sbParams = new StringBuilder();
		
		if (isSkipNpm())
			sbParams.append("-sn").append(' ');
		
		if (isSkipGit())
			sbParams.append("-sg").append(' ');
		
		if (isSkipTests())
			sbParams.append("-st").append(' ');
		
		if (isSkipCommit())
			sbParams.append("-sc").append(' ');
		
		String sourceDir = getSourceDir();
		if (sourceDir != null && sourceDir.length() > 0 && !sourceDir.equals("src"))
			sbParams.append("-sd").append(' ').append(sourceDir).append(' ');
			
		String style = getStyle();
		if (style != null && style.length() > 0 && !style.equalsIgnoreCase("CSS"))
			sbParams.append("--style").append(' ').append(style).append(' ');
		
		String prefix = getPrefix();
		if (prefix != null && prefix.length() > 0 && !prefix.equals("app"))
			sbParams.append("-p").append(' ').append(prefix).append(' ');
		
		if (isRouting())
			sbParams.append("--routing").append(' ');
		
		if (isInlineStyle())
			sbParams.append("-is").append(' ');
		
		if (isInlineTemplate())
			sbParams.append("-it").append(' ');
		
		return sbParams.toString();
	}
	
}
