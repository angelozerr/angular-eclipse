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
package ts.eclipse.ide.angular2.internal.cli.terminal;

import java.io.File;
import java.util.List;

/**
 * "ng init" interpreter to create an Eclipse project at the end of the process
 * and open the generated angular-cli.json file.
 *
 */
public class NgInitCommandInterpreter extends AbstractProjectCommandInterpreter {

	public NgInitCommandInterpreter(List<String> parameters, String workingDir) {
		super(parameters, workingDir);
	}

	@Override
	protected File getProjectDir(List<String> parameters, String workingDir) {
		return new File(workingDir);
	}
}
