package ts.eclipse.ide.angular2.core.html;

public interface INgBindingCollector {

	void collect(String matchingString, String name, String description, INgBindingType bindingType);
}
