package ts.eclipse.ide.angular2.internal.cli.wizards;

import org.eclipse.core.resources.IProject;

import ts.eclipse.ide.angular2.cli.NgBlueprint;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIImageResource;
import ts.eclipse.ide.angular2.internal.cli.AngularCLIMessages;

public class NewNgServiceWizard extends AbstractNewNgGenerateWizard {

	private static final String PAGE_NAME = "ngService";

	public NewNgServiceWizard() {
		super.setWindowTitle(AngularCLIMessages.NewNgServiceWizardPage_title);
		super.setDefaultPageImageDescriptor(
				AngularCLIImageResource.getImageDescriptor(AngularCLIImageResource.IMG_SERVICE));
	}

	@Override
	protected NgGenerateBlueprintWizardPage createMainPage(IProject project) {
		NgGenerateBlueprintWizardPage page = new NgGenerateBlueprintWizardPage(PAGE_NAME,
				AngularCLIMessages.NewNgServiceWizardPage_title, null, NgBlueprint.SERVICE, project);
		page.setDescription(AngularCLIMessages.NewNgServiceWizardPage_description);
		return page;
	}

}
