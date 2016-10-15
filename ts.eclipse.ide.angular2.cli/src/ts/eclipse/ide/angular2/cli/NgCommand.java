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
package ts.eclipse.ide.angular2.cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Available default ng commands.
 *
 */
public enum NgCommand {

	NEW("new"), INIT("init"), GENERATE("generate", "g"), SERVE("serve", "server",
			"s"), BUILD("build"), TEST("test"), E2E("e2e");

	private static final Map<String, NgCommand> commandMaps;

	static {
		commandMaps = new HashMap<String, NgCommand>();
		NgCommand[] commands = NgCommand.values();
		for (NgCommand command : commands) {
			for (String alias : command.getAliases()) {
				commandMaps.put(alias, command);
			}
		}
	}

	private final String[] aliases;

	private NgCommand(String... aliases) {
		this.aliases = aliases;
	}

	public String[] getAliases() {
		return aliases;
	}

	public static NgCommand getCommand(String cmd) {
		return commandMaps.get(cmd.toLowerCase());
	}
}
