package org.openlegacy.rpc.modules.table;

import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RpcTableUtil {

	public static List<?> findTopPartList(RpcEntity rpcEntity, RpcEntityDefinition rpcEntityDefinition) {

		for (Entry<String, PartEntityDefinition<RpcFieldDefinition>> item : rpcEntityDefinition.getPartsDefinitions().entrySet()) {
			RpcPartEntityDefinition rpcPart = (RpcPartEntityDefinition)item.getValue();

			Object obj = null;
			try {
				if (rpcPart.getCount() > 0) {
					SimpleRpcPojoFieldAccessor fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
					obj = fieldAccessor.getFieldValue(rpcPart.getPartName());
					if (obj instanceof List<?>) {
						return (List<?>)obj;
					} else {
						obj = findInnerPartList(obj, rpcPart.getInnerPartsDefinitions());
						if (obj != null) {
							return (List<?>)obj;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static List<?> findInnerPartList(Object parent, Map<String, RpcPartEntityDefinition> inner) {
		for (Entry<String, RpcPartEntityDefinition> item : inner.entrySet()) {
			RpcPartEntityDefinition rpcPart = item.getValue();
			Object obj = null;
			try {
				if (rpcPart.getCount() > 0) {
					SimpleRpcPojoFieldAccessor fieldAccessor = new SimpleRpcPojoFieldAccessor(parent);
					obj = fieldAccessor.getFieldValue(rpcPart.getPartName());
					if (obj instanceof List<?>) {
						return (List<?>)obj;
					} else {
						obj = findInnerPartList(obj, rpcPart.getInnerPartsDefinitions());
						if (obj != null) {
							return (List<?>)obj;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
