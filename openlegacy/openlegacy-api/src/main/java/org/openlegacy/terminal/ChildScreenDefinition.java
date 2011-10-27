package org.openlegacy.terminal;

import org.openlegacy.FetchMode;
import org.openlegacy.HostAction;

public class ChildScreenDefinition {

	private Class<? extends HostAction> stepInto;
	private FetchMode fetchMode;

	public Class<? extends HostAction> getStepInto() {
		return stepInto;
	}

	public FetchMode getFetchMode() {
		return fetchMode;
	}

}
