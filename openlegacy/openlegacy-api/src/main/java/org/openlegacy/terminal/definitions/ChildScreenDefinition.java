package org.openlegacy.terminal.definitions;

import org.openlegacy.FetchMode;
import org.openlegacy.HostAction;

/**
 * Contains information about the relationship between a parent screen entity and child screen entity
 * 
 */
public interface ChildScreenDefinition {

	String getFieldName();

	Class<? extends HostAction> getStepInto();

	FetchMode getFetchMode();

}
