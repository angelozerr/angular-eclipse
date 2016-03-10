/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular2.internal.ui.preferences.html;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractPreferencePage;

import ts.eclipse.ide.angular2.internal.ui.Angular2UIMessages;
import ts.eclipse.ide.angular2.internal.ui.preferences.Angular2UIPreferenceNames;
import ts.eclipse.ide.angular2.ui.Angular2UIPlugin;

/**
 * HTML Angular Typing preferences page.
 * 
 */
public class HTMLAngular2TypingPreferencePage extends AbstractPreferencePage {

	private Button fCloseEndEL;

	protected Control createContents(Composite parent) {
		Composite composite = super.createComposite(parent, 1);

		createEndELGroup(composite);

		setSize(composite);
		loadPreferences();

		return composite;
	}

	private void createEndELGroup(Composite parent) {
		Group group = createGroup(parent, 2);

		group.setText(Angular2UIMessages.Angular2Typing_Auto_Complete);

		fCloseEndEL = createCheckBox(group,
				Angular2UIMessages.Angular2Typing_Close_EL);
		((GridData) fCloseEndEL.getLayoutData()).horizontalSpan = 2;
	}

	@Override
	public boolean performOk() {
		boolean result = super.performOk();

		Angular2UIPlugin.getDefault().savePluginPreferences();

		return result;
	}

	@Override
	protected void initializeValues() {
		initCheckbox(fCloseEndEL,
				Angular2UIPreferenceNames.TYPING_COMPLETE_END_EL);
	}

	@Override
	protected void performDefaults() {
		defaultCheckbox(fCloseEndEL,
				Angular2UIPreferenceNames.TYPING_COMPLETE_END_EL);
	}

	@Override
	protected void storeValues() {
		getPreferenceStore().setValue(
				Angular2UIPreferenceNames.TYPING_COMPLETE_END_EL,
				(fCloseEndEL != null) ? fCloseEndEL.getSelection() : false);
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Angular2UIPlugin.getDefault().getPreferenceStore();
	}

}
