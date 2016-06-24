package ts.eclipse.ide.angular2.cli;

public enum NgBlueprint {

	COMPONENT, DIRECTIVE, PIPE, SERVICE, CLASS, INTERFACE, ENUM;

	private final String blueprint;

	private NgBlueprint() {
		this.blueprint = name().toLowerCase();
	}

	public String getBlueprint() {
		return blueprint;
	}
}
