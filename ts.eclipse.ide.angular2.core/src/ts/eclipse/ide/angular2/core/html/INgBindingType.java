package ts.eclipse.ide.angular2.core.html;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

public interface INgBindingType {

	boolean match(String attrName);

	String extractName(String attrName) throws NgBindingTypeException;

	ValidationMessage validate(IDOMElement target, String attrName, IFile file);

}
