package org.openlegacy.terminal.definitions;

import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;

public class SimpleColumnDefinition implements ColumnDefinition {

	private String name;
	private int startColumn;
	private int endColumn;

	private boolean key;
	private boolean editable;
	private String sampleValue;
	private String displayName;
	private boolean selectionField;
	private Class<?> javaType;

	public SimpleColumnDefinition(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isSelectionField() {
		return selectionField;
	}

	public void setSelectionField(boolean selectionField) {
		this.selectionField = selectionField;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public String getJavaTypeName() {
		return javaType.getSimpleName();
	}

	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}
}
