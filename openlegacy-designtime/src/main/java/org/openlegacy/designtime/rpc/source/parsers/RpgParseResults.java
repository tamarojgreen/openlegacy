package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.rpc.definitions.RpcEntityDefinition;

import java.util.List;

public class RpgParseResults implements ParseResults {

	private RpcEntityDefinition rpcEntityDefinition;

	public List<String> getErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getWarnings() {
		// TODO Auto-generated method stub
		return null;
	}

	public RpcEntityDefinition getEntityDefinition() {

		return rpcEntityDefinition;
	}

	public void setRpcEntityDefinition(RpcEntityDefinition rpcEntityDefinition) {
		this.rpcEntityDefinition = rpcEntityDefinition;
	}

}
