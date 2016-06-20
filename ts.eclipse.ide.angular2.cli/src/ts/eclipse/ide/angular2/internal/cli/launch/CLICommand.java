/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package ts.eclipse.ide.angular2.internal.cli.launch;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class CLICommand {
	
	private String toolName;
	private String commandName;
	private String subCommand;
	private String[] options;
	
	public CLICommand(String toolName, String commandName, String subCommand, String[] options) {
		this.toolName = toolName;
		this.commandName = commandName;
		this.subCommand = subCommand;
		this.options = options;
	}
		
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getToolName());

		if (getCommandName() != null) {
			builder.append(" "); //$NON-NLS-1$
			builder.append(getCommandName());
		}

		if (getSubCommand() != null) {
			builder.append(" "); //$NON-NLS-1$
			builder.append(getSubCommand());
		}

		if (options != null) {
			for (String option : getOptions()) {
				if (option != null && !option.isEmpty()) {
					builder.append(" "); //$NON-NLS-1$
					builder.append(option);
				}
			}
		}

		builder.append("\n"); //$NON-NLS-1$
		return builder.toString();
	}
	
	public String getToolName() {
		return toolName;
	}

	public String getCommandName() {
		return commandName;
	}

	public String getSubCommand() {
		return subCommand;
	}

	public String[] getOptions() {
		return options;
	}

}
