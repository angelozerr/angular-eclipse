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
package ts.eclipse.ide.angular.internal.cli.launch.shortcut;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import ts.eclipse.ide.angular.cli.NgCommand;
import ts.eclipse.ide.angular.internal.cli.Trace;
import ts.eclipse.ide.angular.internal.cli.launch.AngularCLILaunchHelper;

/**
 * Base class for Launch configuration shortcut for angular-cli
 */
public class AngularCLILaunchShortcut implements ILaunchShortcut {

	private final NgCommand ngCommand;

	public AngularCLILaunchShortcut(NgCommand ngCommand) {
		this.ngCommand = ngCommand;
	}

	@Override
	public void launch(ISelection selection, String mode) {
		try {
			Object objSelected = ((IStructuredSelection) selection).getFirstElement();
			if (objSelected instanceof IResource) {
				AngularCLILaunchHelper.launch(ngCommand, ((IResource) objSelected).getProject(), mode);
			}
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, e.getLocalizedMessage());
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		try {
			IEditorInput editorInput = editor.getEditorInput();
			if (editorInput instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) editorInput).getFile();
				AngularCLILaunchHelper.launch(ngCommand, file.getProject(), mode);
			}
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, e.getLocalizedMessage());
		}
	}

}
