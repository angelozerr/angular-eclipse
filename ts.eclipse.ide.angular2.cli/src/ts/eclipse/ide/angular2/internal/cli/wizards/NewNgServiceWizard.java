package ts.eclipse.ide.angular2.internal.cli.wizards;

import ts.eclipse.ide.angular2.cli.NgBlueprint;

public class NewNgServiceWizard extends AbstractNewNgGenerateWizard {

	@Override
	protected NgGenerateBlueprintWizardPage createMainPage() {
		return new NgGenerateBlueprintWizardPage("ngService", "Service", null, NgBlueprint.SERVICE);
	}

}
