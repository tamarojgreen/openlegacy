package org.openlegacy.rpc.modules.table;

import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;

import java.util.List;
import java.util.Map.Entry;

public class RpcTableUtil {

	public static List<?> findTopPartList(RpcEntity rpcEntity, RpcEntityDefinition rpcEntityDefinition) {

		for (Entry<String, PartEntityDefinition<RpcFieldDefinition>> item : rpcEntityDefinition.getPartsDefinitions().entrySet()) {
			RpcPartEntityDefinition rpcPart = (RpcPartEntityDefinition)item.getValue();

			if (rpcPart.getCount() > 0) {
				SimpleRpcPojoFieldAccessor fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
				return (List<?>)fieldAccessor.getFieldValue(rpcPart.getPartName());
			}
		}

		return null;
	}
}
