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
package ts.eclipse.ide.angular2.internal.cli.launch.shortcut;

import ts.eclipse.ide.angular2.cli.NgCommand;

/**
 * Launch configuration shortcut for "ng e2e".
 *
 */
public class NgE2eLaunchShortcut extends AngularCLILaunchShortcut {

	public NgE2eLaunchShortcut() {
		super(NgCommand.E2E);
	}

}
