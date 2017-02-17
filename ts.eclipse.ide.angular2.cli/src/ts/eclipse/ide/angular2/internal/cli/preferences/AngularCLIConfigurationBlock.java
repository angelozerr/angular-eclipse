/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.internal.cli.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.ControlEnableState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.cli.preferences.AngularCLIPreferenceConstants;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.ui.preferences.BrowseButtonsComposite;
import ts.eclipse.ide.ui.preferences.OptionsConfigurationBlock;
import ts.eclipse.ide.ui.preferences.ScrolledPageContent;

/**
 * angular-cli configuration block.
 *
 */
public class AngularCLIConfigurationBlock extends OptionsConfigurationBlock {

	private ControlEnableState blockEnableState;
	private Composite controlsComposite;

	private Button ngUseGlobalInstallation;
	private Combo ngCustomFilePath;
	private BrowseButtonsComposite browseButtons;

	private static final String[] DEFAULT_PATHS = new String[] { "${project_loc:node_modules/.bin}" };

	private static final Key PREF_NG_USE_GLOBAL_INSTALLATION = getAngularCliKey(
			AngularCLIPreferenceConstants.NG_USE_GLOBAL_INSTALLATION);
	private static final Key PREF_NG_CUSTOM_FILE_PATH = getAngularCliKey(
			AngularCLIPreferenceConstants.NG_CUSTOM_FILE_PATH);

	public AngularCLIConfigurationBlock(IProject project, IWorkbenchPreferenceContainer container) {
		super(project, getKeys(), container);
		blockEnableState = null;
	}

	private static Key[] getKeys() {
		return new Key[] { PREF_NG_USE_GLOBAL_INSTALLATION, PREF_NG_CUSTOM_FILE_PATH };
	}

	protected final static Key getAngularCliKey(String key) {
		return getKey(AngularCLIPlugin.PLUGIN_ID, key);
	}

	@Override
	protected Composite createUI(Composite parent) {
		final ScrolledPageContent pageContent = new ScrolledPageContent(parent);
		Composite composite = pageContent.getBody();
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);

		controlsComposite = new Composite(composite, SWT.NONE);
		controlsComposite.setFont(composite.getFont());
		controlsComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 1;
		controlsComposite.setLayout(layout);

		createBody(controlsComposite);
		return pageContent;

	}

	private void createBody(Composite parent) {
		int nColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;

		Group group = new Group(parent, SWT.NONE);
		group.setFont(parent.getFont());
		group.setText(AngularCLIMessages.AngularCLIConfigurationBlock_cli_group_label);
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		group.setLayout(layout);

		createNgUseGlobalInstallation(group);
		createNgUseCustomFile(group);
		updateComboBoxes();
	}

	private void createNgUseGlobalInstallation(Composite parent) {
		// Create "Use ng global installation" checkbox
		ngUseGlobalInstallation = addRadioBox(parent,
				AngularCLIMessages.AngularCLIConfigurationBlock_ngUseGlobalInstallation_label,
				PREF_NG_USE_GLOBAL_INSTALLATION, new String[] { "true", "true" }, 1);
		ngUseGlobalInstallation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateComboBoxes();
			}
		});

		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		ngUseGlobalInstallation.setLayoutData(gd);
	}

	private void createNgUseCustomFile(Composite parent) {
		// Create "Installed TypeScript" checkbox
		Button ngUseCustomFile = addRadioBox(parent,
				AngularCLIMessages.AngularCLIConfigurationBlock_ngUseCustomFile_label, PREF_NG_USE_GLOBAL_INSTALLATION,
				new String[] { "false", "false" }, 0);
		ngUseCustomFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateComboBoxes();
			}
		});

		ngCustomFilePath = newComboControl(parent, PREF_NG_CUSTOM_FILE_PATH, getDefaultPaths(), getDefaultPaths(),
				false);
		ngCustomFilePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create Browse buttons.
		browseButtons = new BrowseButtonsComposite(parent, ngCustomFilePath, getProject(), SWT.NONE);
	}

	private String[] getDefaultPaths() {
		return DEFAULT_PATHS;
	}

	@Override
	protected void validateSettings(Key changedKey, String oldValue, String newValue) {

	}

	@Override
	protected String[] getFullBuildDialogStrings(boolean workspaceSettings) {
		return null;
	}

	public void enablePreferenceContent(boolean enable) {
		if (controlsComposite != null && !controlsComposite.isDisposed()) {
			if (enable) {
				if (blockEnableState != null) {
					blockEnableState.restore();
					blockEnableState = null;
				}
			} else {
				if (blockEnableState == null) {
					blockEnableState = ControlEnableState.disable(controlsComposite);
				}
			}
		}
	}

	private void updateComboBoxes() {
		boolean global = ngUseGlobalInstallation.getSelection();
		ngCustomFilePath.setEnabled(!global);
		browseButtons.setEnabled(!global);
	}

}
