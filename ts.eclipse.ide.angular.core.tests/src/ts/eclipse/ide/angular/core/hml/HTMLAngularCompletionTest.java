package ts.eclipse.ide.angular.core.hml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.junit.Assert;
import org.junit.Test;

import ts.eclipse.ide.angular.core.AngularCorePlugin;
import ts.eclipse.ide.angular.core.html.AbstractNgBindingCollector;
import ts.eclipse.ide.angular.core.html.INgBindingType;
import ts.eclipse.ide.angular.core.utils.DOMUtils;
import ts.utils.IOUtils;

public class HTMLAngularCompletionTest {

	@Test
	public void domPropertyBinding() throws CoreException {
		List<CompletionItem> messages = completion("<input [va />", 10);
		Assert.assertEquals(3, messages.size());
		// valueAsNumber
		Assert.assertEquals("valueAsNumber", messages.get(0).name);
		Assert.assertEquals("value", messages.get(1).name);
		Assert.assertEquals("valueAsDate", messages.get(2).name);
	}

	private class CompletionItem {
		private final String name;
		private final String description;
		private final INgBindingType bindingType;

		public CompletionItem(String name, String description, INgBindingType bindingType) {
			this.name = name;
			this.description = description;
			this.bindingType = bindingType;
		}
	}

	private class CompletionCollector extends AbstractNgBindingCollector {

		private final List<CompletionItem> list;

		public CompletionCollector(IDOMElement element, IDOMAttr attr) {
			super(element, attr, false);
			this.list = new ArrayList<CompletionItem>();
		}

		@Override
		protected void doCollect(String name, String description, INgBindingType bindingType) {
			list.add(new CompletionItem(name, description, bindingType));
		}

		public List<CompletionItem> getList() {
			return list;
		}
	}

	private List<CompletionItem> completion(String html, int offset) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
		if (!project.exists()) {
			project.create(new NullProgressMonitor());
			project.open(new NullProgressMonitor());
		}
		IFile file = project.getFile("test.html");
		if (file.exists()) {
			file.setContents(IOUtils.toInputStream(html), 1, new NullProgressMonitor());
		} else {
			file.create(IOUtils.toInputStream(html), 1, new NullProgressMonitor());
		}

		IDOMModel model = DOMUtils.getModel(project, file);
		IDOMElement element = (IDOMElement) DOMUtils.getNodeByOffset(model, offset);
		IDOMAttr attr = DOMUtils.getAttrByOffset(element, offset);
		String attrName = attr.getName();

		CompletionCollector collector = new CompletionCollector(element, attr);
		AngularCorePlugin.getBindingManager().collect(element, attrName, file, collector);
		return collector.getList();
	}
}
