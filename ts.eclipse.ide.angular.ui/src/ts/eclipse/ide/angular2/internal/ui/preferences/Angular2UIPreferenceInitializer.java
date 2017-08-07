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
package ts.eclipse.ide.angular2.internal.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.wst.html.ui.internal.HTMLUIPlugin;
import org.eclipse.wst.html.ui.internal.preferences.HTMLUIPreferenceNames;

/**
 * Angular2 UI preference initializer.
 *
 */
public class Angular2UIPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore store = PreferenceConstants.getPreferenceStore();
		EditorsUI.useAnnotationsPreferencePage(store);
		EditorsUI.useQuickDiffPreferencePage(store);
		PreferenceConstants.initializeDefaultValues();

		// Override HTML Editor content assist sort to set at first the HTML
		// Angular2 proposal in the completion.
		IPreferenceStore htmlStore = HTMLUIPlugin.getDefault().getPreferenceStore();
		String htmlContentAssistSort = htmlStore
				.getDefaultString(HTMLUIPreferenceNames.CONTENT_ASSIST_DEFAULT_PAGE_SORT_ORDER);
		if (htmlContentAssistSort != null
				&& !htmlContentAssistSort.contains("ts.eclipse.ide.angular2.ui.proposalCategory.htmlTags\0")) {
			htmlStore.setDefault(HTMLUIPreferenceNames.CONTENT_ASSIST_DEFAULT_PAGE_SORT_ORDER,
					"ts.eclipse.ide.angular2.ui.proposalCategory.htmlTags\0" + htmlContentAssistSort);
		}
	}

}
