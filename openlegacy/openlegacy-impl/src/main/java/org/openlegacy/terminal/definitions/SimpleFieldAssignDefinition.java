package org.openlegacy.terminal.definitions;

public class SimpleFieldAssignDefinition implements FieldAssignDefinition {

	private String name;
	private String value;

	public SimpleFieldAssignDefinition(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
