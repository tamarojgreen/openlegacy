package org.openlegacy.designtime.rpc.source;

import org.openlegacy.rpc.definitions.RpcEntityDefinition;

public interface CodeParser {

	RpcEntityDefinition parse(String source);
}
