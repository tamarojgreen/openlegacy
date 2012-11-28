package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.ConnectionPropertiesProvider;

import java.io.Serializable;

public class SimpleConnectionPropertiesProvider implements ConnectionPropertiesProvider, Serializable {

	private static final long serialVersionUID = 1L;

	public ConnectionProperties getConnectionProperties() {
		return new SimpleConnectionProperties();
	}

}
