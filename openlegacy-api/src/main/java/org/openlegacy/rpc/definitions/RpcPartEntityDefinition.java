package org.openlegacy.rpc.definitions;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.PartEntityDefinition;

import java.util.List;
import java.util.Map;

public interface RpcPartEntityDefinition extends PartEntityDefinition<RpcFieldDefinition> {

	public int getOrder();

	public int getOccur();

	public Map<String, RpcPartEntityDefinition> getInnerPartsDefinitions();

	String getRuntimeName();

	List<ActionDefinition> getActions();

	List<RpcFieldDefinition> getKeys();

}
