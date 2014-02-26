package org.openlegacy.rpc;

import java.util.List;

public interface RpcFieldConverter {

	void toLegacy(List<RpcField> fields);

	void toApi(List<RpcField> fields);
}
