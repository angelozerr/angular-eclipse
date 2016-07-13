package ts.eclipse.ide.angular2.internal.core.html.schema;

import ts.eclipse.ide.angular2.core.html.INgBindingCollector;
import ts.eclipse.ide.angular2.core.html.INgBindingType;

public interface IElementSchemaRegistry {

	boolean hasProperty(String tagName, String propName);

	boolean hasEvent(String tagName, String eventName);

	Object securityContext(String tagName, String propName);

	String getMappedPropName(String propName);

	void collectProperty(String tagName, String attrName, INgBindingType bindingType, INgBindingCollector collector);

	void collectEvent(String tagName, String attrName, INgBindingType bindingType, INgBindingCollector collector);
}
