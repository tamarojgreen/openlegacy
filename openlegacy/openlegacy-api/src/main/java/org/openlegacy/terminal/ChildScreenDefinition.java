package org.openlegacy.terminal;

import org.openlegacy.FetchMode;
import org.openlegacy.HostAction;

public interface ChildScreenDefinition {

	String getFieldName();

	Class<? extends HostAction> getStepInto();

	FetchMode getFetchMode();

}
