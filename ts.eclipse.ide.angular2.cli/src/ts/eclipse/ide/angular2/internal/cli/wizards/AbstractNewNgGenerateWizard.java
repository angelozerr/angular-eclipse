package ts.eclipse.ide.angular2.internal.cli.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public abstract class AbstractNewNgGenerateWizard extends Wizard implements INewWizard {

	private IStructuredSelection selection;

	private NgGenerateBlueprintWizardPage mainPage;

	public void addPages() {
		super.addPages();
		mainPage = createMainPage();// $NON-NLS-1$
		addPage(mainPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}

	protected abstract NgGenerateBlueprintWizardPage createMainPage();

}
