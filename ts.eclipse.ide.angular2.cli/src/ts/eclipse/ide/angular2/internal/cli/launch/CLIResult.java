/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package ts.eclipse.ide.angular2.internal.cli.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ts.eclipse.ide.angular2.cli.AngularCLIPlugin;

/**
 * Generic execution result for Cordova CLI.
 * 
 * @author Gorkem Ercan
 *
 */
public class CLIResult {
	private final String errorMessage;
	private final String message;
	
	public CLIResult(String error, String message){
		this.errorMessage = error;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	String getErrorMessage() {
		return errorMessage;
	}


	public boolean hasError() {
		return errorMessage != null && !errorMessage.isEmpty();
	}
	
	IStatus asStatus(){
		if(hasError()){
			return new CLIStatus(IStatus.ERROR, AngularCLIPlugin.PLUGIN_ID, 500, getErrorMessage(), null);
		}
		return Status.OK_STATUS;
	}
	
	CoreException asCoreException(){
		if(hasError()){
			return new CoreException(asStatus());
		}
		return null;
	}

}
