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
package ts.eclipse.ide.angular2.internal.cli.editor;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.json.jsonpath.JSONPath;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;
import ts.eclipse.ide.json.ui.AbstractFormPage;
import ts.eclipse.ide.json.ui.FormLayoutFactory;

/**
 * Overview page for angular-cli.json editor.
 *
 */
public class OverviewPage extends AbstractFormPage {

	private static final String ID = "overview";

	public OverviewPage(AngularCLIEditor editor) {
		super(editor, ID, AngularCLIMessages.AngularCLIEditor_OverviewPage_title);
	}

	@Override
	protected boolean contributeToToolbar(IToolBarManager manager) {
		manager.add(new NgServeAction(getEditor()));
		manager.add(new NgTestAction(getEditor()));
		manager.add(new NgE2eAction(getEditor()));
		manager.add(new NgBuildAction(getEditor()));
		return true;
	}

	@Override
	protected String getFormTitleText() {
		return AngularCLIMessages.AngularCLIEditor_OverviewPage_title;
	}

	@Override
	protected void createUI(IManagedForm managedForm) {
		Composite body = managedForm.getForm().getBody();
		body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));
		createLeftContent(body);
		createRightContent(body);
	}

	private void createLeftContent(Composite parent) {
		FormToolkit toolkit = super.getToolkit();
		Composite left = toolkit.createComposite(parent);
		left.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		left.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		// General Information
		createGeneralInformationSection(left);
	}
	
	private void createGeneralInformationSection(Composite parent) {
		FormToolkit toolkit = super.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR);
		section.setDescription(AngularCLIMessages.AngularCLIEditor_OverviewPage_GeneralInformationSection_desc);
		section.setText(AngularCLIMessages.AngularCLIEditor_OverviewPage_GeneralInformationSection_title);
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(data);

		Composite body = createBody(section);

		// Project name
		createText(body, AngularCLIMessages.AngularCLIEditor_OverviewPage_projectName_label, new JSONPath("project.name"));
		// project version
		createText(body, AngularCLIMessages.AngularCLIEditor_OverviewPage_projectVersion_label, new JSONPath("project.version"));

	}

	private void createRightContent(Composite parent) {

	}
	
	private Composite createBody(Section section) {
		FormToolkit toolkit = super.getToolkit();
		Composite body = toolkit.createComposite(section);
		section.setClient(body);

		GridLayout glayout = new GridLayout();
		glayout.numColumns = 1;
		body.setLayout(glayout);
		return body;
	}
}
