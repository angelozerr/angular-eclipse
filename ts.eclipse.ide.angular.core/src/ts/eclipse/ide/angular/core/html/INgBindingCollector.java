package ts.eclipse.ide.angular.core.html;

public interface INgBindingCollector {

	void collect(String matchingString, String name, String description, INgBindingType bindingType);
}
