package ts.eclipse.ide.angular2.internal.core.html;

import org.eclipse.core.resources.IFile;

public interface INgAttrRegistry {

	boolean hasProperty(String tagName, String propName, IFile file);

	boolean hasEvent(String tagName, String eventName, IFile file);
}
