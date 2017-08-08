package ts.eclipse.ide.angular.core.html;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

public interface INgBindingType {

	String formatAttr(String name);

	boolean match(String attrName);

	ValidationMessage validate(IDOMElement target, String attrName, IFile file);

	void collect(IDOMElement target, String attrName, boolean fullMatch, IFile file, INgBindingCollector collector);

}
