package ts.eclipse.ide.angular2.internal.core.html.schema;

public interface IElementSchemaRegistry {
	
	boolean hasProperty(String tagName, String propName);

	boolean hasEvent(String tagName, String eventName);
	
	Object securityContext(String tagName, String propName);

	String getMappedPropName(String propName);
}
