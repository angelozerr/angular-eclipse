/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular.internal.core.html;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import ts.eclipse.ide.angular.core.AngularProject;
import ts.eclipse.ide.core.resources.ITypeScriptResourceParticipant;

/**
 * {@link ITypeScriptResourceParticipant} implementation for HTML Angular
 * Template. The HTML editor should consumes tsserver command to benefit with
 * completion, hover etc from the @angular/language-service.
 *
 */
public class HTMLAngularTemplateParticipant implements ITypeScriptResourceParticipant {

	@Override
	public boolean canConsumeTsserver(IProject project, Object fileObject) {
		// HTML editor can consume tsserver if:
		// - the html file belongs to an Angular project
		// - TypeScript 2.4.0 is available
		// - @angular/language-service is available
		return (isHTMLFile(fileObject) && AngularProject.canSupportAngularLanguageService(project));
	}

	private boolean isHTMLFile(Object fileObject) {
		if (!(fileObject instanceof IResource)) {
			return false;
		}
		String extension = ((IResource) fileObject).getFileExtension();
		return "html".equals(extension) || "htm".equals(extension);
	}

}
