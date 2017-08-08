package ts.eclipse.ide.angular.internal.core.html.directives;

import org.eclipse.core.resources.IFile;

public interface INgDirectiveRegistry {

	boolean hasProperty(String tagName, String name, IFile file);
	
	boolean hasEvent(String tagName, String name, IFile file);

}
