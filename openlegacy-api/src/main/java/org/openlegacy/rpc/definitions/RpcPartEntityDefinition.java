package org.openlegacy.rpc.definitions;

import org.openlegacy.definitions.PartEntityDefinition;

import java.util.Map;

public interface RpcPartEntityDefinition extends PartEntityDefinition<RpcFieldDefinition> {

	public String getOriginalName();

	public int getOrder();

	public int getOccur();

	public Map<String, RpcPartEntityDefinition> getInnerPartsDefinitions();

}
