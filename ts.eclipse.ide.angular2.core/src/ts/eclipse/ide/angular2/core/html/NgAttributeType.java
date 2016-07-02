package ts.eclipse.ide.angular2.core.html;

import ts.utils.StringUtils;

public enum NgAttributeType {

	PropertyBinding, PropertyBindingCanonicalSyntax, EventBinding, EventBindingCanonicalSyntax, PropertyAndEventBinding, TemplateDirective;

	private static final String BIND_SUFFIX = "bind-";
	private static final String ON_SUFFIX = "on-";

	public static NgAttributeType getType(String attrName) {
		if (StringUtils.isEmpty(attrName)) {
			return null;
		}
		// It's an Angular2 project, check if attribute name starts with '(',
		// '[', '*', 'bind-' or 'on-'.
		char startChar = attrName.charAt(0);
		switch (startChar) {
		case '[':
			if (attrName.length() > 1) {
				char secondChar = attrName.charAt(1);
				if (secondChar == '(') {
					return NgAttributeType.PropertyAndEventBinding;
				}
			}
			return NgAttributeType.PropertyBinding;
		case '(':			
			return NgAttributeType.EventBinding;
		case '*':
			return TemplateDirective;
		}
		if (attrName.startsWith(BIND_SUFFIX)) {
			return PropertyBindingCanonicalSyntax;
		} else if (attrName.startsWith(ON_SUFFIX)) {
			return EventBindingCanonicalSyntax;
		}
		return null;
	}

	public static String extractAttrName(String attrName, NgAttributeType type) throws NgAttributeSyntaxException {
		if (type == null) {
			return attrName;
		}
		switch (type) {
		case EventBinding:
			// must be ended with ')'
			if (attrName.charAt(attrName.length() - 1) != ')') {
				throw new NgAttributeSyntaxException(attrName + " must be closed with ')'");
			}
			return attrName.substring(1, attrName.length() - 1);
		case EventBindingCanonicalSyntax:
			return attrName.substring(ON_SUFFIX.length(), attrName.length());
		case PropertyBinding:
			// must be ended with ']'
			if (attrName.charAt(attrName.length() - 1) != ']') {
				throw new NgAttributeSyntaxException(attrName + " must be closed with ']'");
			}
			return attrName.substring(1, attrName.length() - 1);
		case PropertyBindingCanonicalSyntax:
			return attrName.substring(BIND_SUFFIX.length(), attrName.length());
		case PropertyAndEventBinding:
			// must be ended with ']'
			if (!attrName.endsWith(")]")) {
				throw new NgAttributeSyntaxException(attrName + " must be closed with ')]'");
			}
			return attrName.substring(2, attrName.length() - 2);
		case TemplateDirective:
			return attrName.substring(1, attrName.length());
		}
		return attrName;
	}
}
