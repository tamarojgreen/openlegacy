package org.openlegacy.rpc.recognizers;

import org.openlegacy.rpc.RpcSnapshot;

public interface RpcRecognizer {

	Class<?> match(RpcSnapshot rpcSnapshot);
}