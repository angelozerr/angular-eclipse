package ts.eclipse.ide.angular.internal.core.html.directives;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ts.utils.IOUtils;

public class NgDirectiveRegistry implements INgDirectiveRegistry {

	public static final INgDirectiveRegistry INSTANCE = new NgDirectiveRegistry();

	private final Map<String, Directive> directiveMap;

	public NgDirectiveRegistry() {
		this.directiveMap = new HashMap<String, Directive>();
		loadDirective(NgDirectiveRegistry.class.getResourceAsStream("core_directives.json"));
		loadDirective(NgDirectiveRegistry.class.getResourceAsStream("form_directives.json"));
	}

	@Override
	public boolean hasProperty(String tagName, String name, IFile file) {
		return directiveMap.containsKey(name);
	}

	@Override
	public boolean hasEvent(String tagName, String name, IFile file) {
		return directiveMap.containsKey(name);
	}

	private void loadDirective(InputStream input) {
		try {
			Type listType = new TypeToken<ArrayList<Directive>>() {
			}.getType();
			List<Directive> directives = new GsonBuilder().create().fromJson(IOUtils.toString(input), listType);
			for (Directive directive : directives) {
				this.directiveMap.put(directive.getName(), directive);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
