package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.rpc.definitions.RpcEntityDefinition;

import java.util.List;

public class RpgParseResults implements ParseResults {

	private RpcEntityDefinition rpcEntityDefinition;

	@Override
	public List<String> getErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getWarnings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RpcEntityDefinition getEntityDefinition() {

		return rpcEntityDefinition;
	}

	public void setRpcEntityDefinition(RpcEntityDefinition rpcEntityDefinition) {
		this.rpcEntityDefinition = rpcEntityDefinition;
	}

}
