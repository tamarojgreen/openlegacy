package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.rpc.definitions.RpcEntityDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimpleParseResults implements ParseResults {

	private List<String> errors = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	private RpcEntityDefinition entityDefinition;

	public SimpleParseResults(RpcEntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	public List<String> getErrors() {
		return errors;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public RpcEntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

}
