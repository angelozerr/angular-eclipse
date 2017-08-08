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
package ts.eclipse.ide.angular.internal.cli;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * angular-cli messages.
 *
 */
public class AngularCLIMessages extends NLS {

	private static final String BUNDLE_NAME = "ts.eclipse.ide.angular.internal.cli.AngularCLIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// Preferences
	public static String AngularCLIConfigurationBlock_cli_group_label;
	public static String AngularCLIConfigurationBlock_ngUseGlobalInstallation_label;
	public static String AngularCLIConfigurationBlock_ngUseCustomFile_label;
	public static String AngularCLIConfigurationBlock_executeNgWithFile_label;
	public static String AngularCLIConfigurationBlock_cliPath_label;
	public static String AngularCLIConfigurationBlock_cliVersion_label;
	public static String AngularCLIConfigurationBlock_ngGlobal_notFound_error;
	public static String AngularCLIConfigurationBlock_ngCustomFile_required_error;
	public static String AngularCLIConfigurationBlock_ngCustomFile_exists_error;
	public static String AngularCLIConfigurationBlock_ngCustomFile_invalid_error;
	public static String AngularCLIConfigurationBlock_ngCustomFile_notDir_error;
	public static String AngularCLIConfigurationBlock_ValidatingNgCli_jobName;

	// Interpreter
	public static String NgServeJob_jobName;
	public static String NgServeJob_error;
	public static String NgGenerateJob_jobName;
	public static String NgGenerateJob_error;
	public static String NgBuildCommandInterpreter_jobName;
	public static String NgBuildCommandInterpreter_error;
	public static String AbstractProjectCommandInterpreter_jobName;
	public static String AbstractProjectCommandInterpreter_error;
	public static String AbstractProjectCommandInterpreter_anotherProjectWithSameNameExists_description;

	// Wizard
	public static String NewAngularProjectWizard_windowTitle;
	public static String NewAngularProjectWizard_newProjectTitle;
	public static String NewAngularProjectWizard_newProjectDescription;
	public static String NewAngularProjectWizard_invalidProjectName;
	public static String NewAngularProjectWizard_unsupportedProjectNames;

	public static String WizardNewNgProjectCreationPage_angular_cli_group_label;
	public static String WizardNewNgProjectCreationPage_useGlobalAngularCLI;
	public static String WizardNewNgProjectCreationPage_useInstallAngularCLI;
	public static String WizardNewNgProjectCreationPage_searchingForGlobalAngularCLI;
	public static String WizardNewNgProjectCreationPage_noGlobalAngularCLI;

	public static String NewAngularProjectParamsWizardPage_title;
	public static String NewAngularProjectParamsWizardPage_prefix;
	public static String NewAngularProjectParamsWizardPage_sourceDir;
	public static String NewAngularProjectParamsWizardPage_style;
	public static String NewAngularProjectParamsWizardPage_skip;
	public static String NewAngularProjectParamsWizardPage_install;
	public static String NewAngularProjectParamsWizardPage_git;
	public static String NewAngularProjectParamsWizardPage_tests;
	public static String NewAngularProjectParamsWizardPage_commit;
	public static String NewAngularProjectParamsWizardPage_routing;
	public static String NewAngularProjectParamsWizardPage_inline;
	public static String NewAngularProjectParamsWizardPage_inlineStyle;
	public static String NewAngularProjectParamsWizardPage_inlineTemplate;

	public static String NgGenerateBlueprintWizardPage_location;
	public static String NgGenerateBlueprintWizardPage_browse_location;
	public static String NgGenerateBlueprintWizardPage_browse_location_title;
	public static String NgGenerateBlueprintWizardPage_browse_location_message;
	public static String NgGenerateBlueprintWizardPage_bluePrintName;
	public static String NgGenerateBlueprintWizardPage_generated_files;
	public static String NgGenerateBlueprintWizardPage_select_name_required_error;
	public static String NgGenerateBlueprintWizardPage_invalid_location_error;
	public static String NgGenerateBlueprintWizardPage_invalid_project_location_error;
	public static String NgGenerateBlueprintWizardPage_invalid_apps_location_error;
	public static String NgGenerateBlueprintWizardPage_file_already_exist;

	public static String NgGenerateBlueprintWizardPage_generate_spec;
	public static String NgGenerateBlueprintWizardPage_prefix;
	public static String NgGenerateBlueprintWizardPage_flat;
	public static String NgGenerateBlueprintWizardPage_skipImport;
	public static String NgGenerateBlueprintWizardPage_export;

	public static String NewNgModuleWizard_windowTitle;
	public static String NewNgModuleWizardPage_title;
	public static String NewNgModuleWizardPage_description;
	public static String NewNgModuleWizardPage_routing;

	public static String NewNgComponentWizard_windowTitle;
	public static String NewNgComponentWizardPage_title;
	public static String NewNgComponentWizardPage_description;
	public static String NewNgComponentWizardPage_inlineTemplate;
	public static String NewNgComponentWizardPage_inlineStyle;
	public static String NewNgComponentWizardPage_viewEncapsulation;
	public static String NewNgComponentWizardPage_changeDetection;

	public static String NewNgDirectiveWizard_windowTitle;
	public static String NewNgDirectiveWizardPage_title;
	public static String NewNgDirectiveWizardPage_description;

	public static String NewNgPipeWizard_windowTitle;
	public static String NewNgPipeWizardPage_title;
	public static String NewNgPipeWizardPage_description;

	public static String NewNgServiceWizard_windowTitle;
	public static String NewNgServiceWizardPage_title;
	public static String NewNgServiceWizardPage_description;

	public static String NewNgGuardWizard_windowTitle;
	public static String NewNgGuardWizardPage_title;
	public static String NewNgGuardWizardPage_description;

	public static String NewNgClassWizard_windowTitle;
	public static String NewNgClassWizardPage_title;
	public static String NewNgClassWizardPage_description;

	public static String NewNgInterfaceWizard_windowTitle;
	public static String NewNgInterfaceWizardPage_title;
	public static String NewNgInterfaceWizardPage_description;

	public static String NewNgEnumWizard_windowTitle;
	public static String NewNgEnumWizardPage_title;
	public static String NewNgEnumWizardPage_description;

	// AngularCLI editor
	public static String AngularCLIEditor_OverviewPage_title;
	public static String AngularCLIEditor_OverviewPage_GeneralInformationSection_title;
	public static String AngularCLIEditor_OverviewPage_GeneralInformationSection_desc;
	public static String AngularCLIEditor_OverviewPage_projectName_label;
	public static String AngularCLIEditor_OverviewPage_projectVersion_label;

	public static String AngularCLIEditor_NgServeAction_text;
	public static String AngularCLIEditor_NgBuildAction_text;
	public static String AngularCLIEditor_NgTestAction_text;
	public static String AngularCLIEditor_NgE2eAction_text;
	public static String AngularCLIEditor_NgCommand_dialog_title;
	public static String AngularCLIEditor_NgCommand_failed;

	// AngularCLI Launch
	public static String AngularCLILaunchTabGroup_MainTab_command;
	public static String AngularCLILaunchTabGroup_MainTab_arguments;

	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null)
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (MissingResourceException x) {
			fResourceBundle = null;
		}
		return fResourceBundle;
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, AngularCLIMessages.class);
	}
}