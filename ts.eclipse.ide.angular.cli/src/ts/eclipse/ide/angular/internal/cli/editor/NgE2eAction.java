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
package ts.eclipse.ide.angular.internal.cli.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorPart;

import ts.eclipse.ide.angular.cli.NgCommand;
import ts.eclipse.ide.angular.internal.cli.AngularCLIImageResource;
import ts.eclipse.ide.angular.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.angular.internal.cli.launch.AngularCLILaunchHelper;
import ts.eclipse.ide.json.ui.actions.AbstractFileAction;

/**
 * "ng e2e" action.
 *
 */
public class NgE2eAction extends AbstractFileAction {

	public NgE2eAction(IEditorPart editor) {
		super(editor);
		super.setText(AngularCLIMessages.AngularCLIEditor_NgE2eAction_text);
		super.setImageDescriptor(AngularCLIImageResource.getImageDescriptor(AngularCLIImageResource.IMG_NG_E2E));
	}

	@Override
	public void run() {
		IFile file = getFile();
		if (file != null) {
			try {
				AngularCLILaunchHelper.launch(NgCommand.E2E, file.getProject());
			} catch (CoreException e) {
				ErrorDialog.openError(getEditor().getSite().getShell(),
						AngularCLIMessages.AngularCLIEditor_NgCommand_dialog_title,
						AngularCLIMessages.AngularCLIEditor_NgCommand_failed, e.getStatus());
			}
		}
	}

}
