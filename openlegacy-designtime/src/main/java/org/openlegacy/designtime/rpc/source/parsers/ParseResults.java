package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.rpc.definitions.RpcEntityDefinition;

import java.util.List;

public interface ParseResults {

	List<String> getErrors();

	List<String> getWarnings();

	RpcEntityDefinition getEntityDefinition();

}
