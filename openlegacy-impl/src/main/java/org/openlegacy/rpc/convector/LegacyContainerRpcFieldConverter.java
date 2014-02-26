package org.openlegacy.rpc.convector;

import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFieldConverter;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureField;

import java.util.ArrayList;
import java.util.List;

public class LegacyContainerRpcFieldConverter implements RpcFieldConverter {

	public void toLegacy(List<RpcField> fields) {
		List<RpcField> result = new ArrayList<RpcField>();
		for (RpcField rpcField : fields) {
			if (rpcField.getLegacyContainerName() != null) {
				SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
				rpcStructureField.setName(rpcField.getLegacyContainerName());
				rpcStructureField.setOrder(0);
				rpcStructureField.setIsContainer(true);
				rpcStructureField.getChildren().add(rpcField);
				result.add(rpcStructureField);
			} else {
				result.add(rpcField);
			}
		}
		fields.clear();
		fields.addAll(result);

	}

	public void toApi(List<RpcField> fields) {
		List<RpcField> result = new ArrayList<RpcField>();
		for (RpcField rpcField : fields) {
			if (rpcField.isContainer() == true) {
				result.add(((RpcStructureField)rpcField).getChildren().get(0));

			} else {
				result.add(rpcField);
			}

		}
		fields.clear();
		fields.addAll(result);
	}
}
