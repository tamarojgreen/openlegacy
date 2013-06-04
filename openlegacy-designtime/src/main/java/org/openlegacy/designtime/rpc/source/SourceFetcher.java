package org.openlegacy.designtime.rpc.source;

import org.openlegacy.rpc.RpcConnection;

public interface SourceFetcher {

	String fetchSource(String path, RpcConnection rpcConnection);
}
