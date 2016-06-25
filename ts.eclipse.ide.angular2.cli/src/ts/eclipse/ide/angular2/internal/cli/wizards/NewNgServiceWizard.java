package ts.eclipse.ide.angular2.internal.cli.wizards;

import org.eclipse.core.resources.IProject;

import ts.eclipse.ide.angular2.cli.NgBlueprint;

public class NewNgServiceWizard extends AbstractNewNgGenerateWizard {

	@Override
	protected NgGenerateBlueprintWizardPage createMainPage(IProject project) {
		return new NgGenerateBlueprintWizardPage("ngService", "Service", null, NgBlueprint.SERVICE, project);
	}

}
