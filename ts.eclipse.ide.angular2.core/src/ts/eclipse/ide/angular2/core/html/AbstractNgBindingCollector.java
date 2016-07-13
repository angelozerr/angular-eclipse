package ts.eclipse.ide.angular2.core.html;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.w3c.dom.Attr;

public abstract class AbstractNgBindingCollector implements INgBindingCollector {

	private final IDOMElement element;
	private final IDOMAttr attr;
	private final boolean fullMatch;

	public AbstractNgBindingCollector(IDOMElement element, IDOMAttr attr, boolean fullMatch) {
		this.element = element;
		this.attr = attr;
		this.fullMatch = fullMatch;
	}

	@Override
	public final void collect(String matchingString, String name, String description, INgBindingType bindingType) {
		if (isMatch(name, matchingString) && isDontExists(element, name, bindingType)) {
			doCollect(name, getDescription(name, description, bindingType), bindingType);
		}
	}

	private String getDescription(String name, String description, INgBindingType bindingType) {
		if (description != null) {
			return description;
		}
		return bindingType.formatAttr(name);
	}

	protected boolean isDontExists(IDOMElement element, String name, INgBindingType bindingType) {
		Attr existingAttr = element.getAttributeNode(bindingType.formatAttr(name));
		if (existingAttr == null) {
			return true;
		}
		return existingAttr.equals(attr);
	}

	protected abstract void doCollect(String name, String description, INgBindingType bindingType);

	protected boolean isMatch(String name, String matchingString) {
		if (fullMatch) {
			return matchingString.equals(name);
		}
		return name.startsWith(matchingString);
	}

}
