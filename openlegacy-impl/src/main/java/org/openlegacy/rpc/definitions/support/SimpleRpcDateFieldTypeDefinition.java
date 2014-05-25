package org.openlegacy.rpc.definitions.support;

import org.openlegacy.definitions.RpcDateFieldTypeDefinition;

import java.io.Serializable;

public class SimpleRpcDateFieldTypeDefinition implements Serializable, RpcDateFieldTypeDefinition {

	private static final long serialVersionUID = 1L;

	private String pattern;
	private String locale;

	public SimpleRpcDateFieldTypeDefinition() {}

	public SimpleRpcDateFieldTypeDefinition(String pattern, String locale) {
		this.pattern = pattern;
		this.locale = locale;
	}

	public String getTypeName() {
		return "date";
	}

	public String getPattern() {
		return pattern;
	}

	public String getLocale() {
		return locale;
	}
}
