package org.openlegacy.rpc.convector;

import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFieldConverter;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.support.RpcOrderFieldComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualRpcConverter implements RpcFieldConverter {

	@Override
	public void toLegacy(List<RpcField> fields) {
		List<RpcField> result = new ArrayList<RpcField>();
		for (RpcField rpcField : fields) {
			if (rpcField instanceof RpcStructureField) {
				convertChildsToLegacy((RpcStructureField)rpcField);
			}
			if (Boolean.TRUE.equals(rpcField.isVirtual())) {
				RpcStructureField rpcStructureField = (RpcStructureField)rpcField;
				List<RpcField> childs = rpcStructureField.getChildrens();

				for (RpcField child : childs) {
					child.setVirtualGroup(rpcField.getName());
				}

				result.addAll(childs);
				rpcStructureField.getChildrens().clear();
			}

			result.add(rpcField);

		}
		fields.clear();
		fields.addAll(result);

	}

	private void convertChildsToLegacy(RpcStructureField rpcStructureField) {
		toLegacy(rpcStructureField.getChildrens());

	}

	@Override
	public void toApi(List<RpcField> fields) {
		List<RpcField> result = new ArrayList<RpcField>();
		Map<String, Map<String, RpcField>> fieldCollector = new HashMap<String, Map<String, RpcField>>();

		for (RpcField rpcField : fields) {

			String virtualGroupName = rpcField.getVirtualGroup();
			if (!fieldCollector.containsKey(virtualGroupName)) {
				fieldCollector.put(virtualGroupName, new HashMap<String, RpcField>());
			}

			fieldCollector.get(virtualGroupName).put(rpcField.getName(), rpcField);

			if (rpcField instanceof RpcStructureField) {
				convertChildsToApi((RpcStructureField)rpcField);
			}
		}

		for (String virtualGroupName : fieldCollector.keySet()) {
			if (virtualGroupName == null || virtualGroupName.equals("")) {
				result.addAll(fieldCollector.get(virtualGroupName).values());
			} else {
				// only simple structures can be virtual
				RpcStructureField virtualPart = (RpcStructureField)fieldCollector.get("").get(virtualGroupName);
				virtualPart.getChildrens().addAll(fieldCollector.get(virtualGroupName).values());

			}
		}
		Collections.sort(result, new RpcOrderFieldComparator());
		fields.clear();
		fields.addAll(result);
	}

	private void convertChildsToApi(RpcStructureField rpcStructureField) {
		toApi(rpcStructureField.getChildrens());

	}

}
