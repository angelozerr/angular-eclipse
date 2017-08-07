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
package ts.eclipse.ide.angular2.internal.cli.launch.ui;

import org.eclipse.core.externaltools.internal.IExternalToolConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;
import ts.eclipse.ide.angular2.cli.NgCommand;
import ts.eclipse.ide.angular2.cli.launch.AngularCLILaunchConstants;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.ui.launch.AbstractMainTab;

/**
 * Main tab for ng launch which is composed with:
 * 
 * <ul>
 * <li>a text field to select the project.</li>
 * <li>a combo to select the ng command.</li>
 * </ul>
 *
 */
public class MainTab extends AbstractMainTab {

	protected Combo commandsCommbo;
	private Text argumentsField;

	@Override
	protected void createBodyComponents(Composite parent) {
		createNgCommandComponent(parent);
		super.createBodyComponents(parent);
		createArguments(parent);
	}

	private void createNgCommandComponent(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		String groupName = AngularCLIMessages.AngularCLILaunchTabGroup_MainTab_command;
		group.setText(groupName);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayout(layout);
		group.setLayoutData(gridData);

		commandsCommbo = new Combo(group, SWT.BORDER | SWT.H_SCROLL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		commandsCommbo.setLayoutData(data);
		String[] items = new String[NgCommand.values().length];
		for (int i = 0; i < items.length; i++) {
			items[i] = NgCommand.values()[i].getAliases()[0];
		}
		commandsCommbo.setItems(items);
		commandsCommbo.addModifyListener(fListener);
	}

	private void createArguments(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		String groupName = AngularCLIMessages.AngularCLILaunchTabGroup_MainTab_arguments;
		group.setText(groupName);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayout(layout);
		group.setLayoutData(gridData);

		argumentsField = new Text(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		argumentsField.setLayoutData(data);
		argumentsField.addModifyListener(fListener);
	}

	@Override
	protected void doInitializeFrom(ILaunchConfiguration configuration) {
		super.doInitializeFrom(configuration);
		updateNgCommand(configuration);
		updateArguments(configuration);
	}

	protected void updateNgCommand(ILaunchConfiguration configuration) {
		String command = IExternalToolConstants.EMPTY_STRING;
		try {
			command = configuration.getAttribute(AngularCLILaunchConstants.OPERATION,
					IExternalToolConstants.EMPTY_STRING);
		} catch (CoreException ce) {
			AngularCLIPlugin.logError(ce, "Error while reading ng configuration");
		}
		commandsCommbo.setText(command);
	}

	private void updateArguments(ILaunchConfiguration configuration) {
		String arguments = IExternalToolConstants.EMPTY_STRING;
		try {
			arguments = configuration.getAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS,
					IExternalToolConstants.EMPTY_STRING);
		} catch (CoreException ce) {
			AngularCLIPlugin.logError(ce, "Error while reading ng configuration");
		}
		this.argumentsField.setText(arguments);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		super.performApply(configuration);
		String command = commandsCommbo.getText().trim();
		if (command.length() == 0) {
			configuration.setAttribute(AngularCLILaunchConstants.OPERATION, (String) null);
		} else {
			configuration.setAttribute(AngularCLILaunchConstants.OPERATION, command);
		}
		String arguments = this.argumentsField.getText().trim();
		if (arguments.length() == 0) {
			configuration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS, (String) null);
		} else {
			configuration.setAttribute(AngularCLILaunchConstants.OPERATION_PARAMETERS, arguments);
		}
	}

	@Override
	protected boolean validate(boolean newConfig) {
		return super.validate(newConfig);
	}

}
