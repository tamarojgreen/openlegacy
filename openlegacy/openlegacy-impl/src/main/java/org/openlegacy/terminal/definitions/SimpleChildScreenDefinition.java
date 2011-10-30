package org.openlegacy.terminal.definitions;

import org.openlegacy.FetchMode;
import org.openlegacy.HostAction;
import org.openlegacy.terminal.ChildScreenDefinition;

public class SimpleChildScreenDefinition implements ChildScreenDefinition {

	private Class<? extends HostAction> stepInto;
	private FetchMode fetchMode;
	private String fieldName;

	public SimpleChildScreenDefinition(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class<? extends HostAction> getStepInto() {
		return stepInto;
	}

	public FetchMode getFetchMode() {
		return fetchMode;
	}

	public void setFetchMode(FetchMode fetchMode) {
		this.fetchMode = fetchMode;
	}

	public void setStepInto(Class<? extends HostAction> stepInto) {
		this.stepInto = stepInto;
	}

	public String getFieldName() {
		return fieldName;
	}
}
