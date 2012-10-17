package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.ConnectionPropertiesProvider;

public class SimpleConnectionPropertiesProvider implements ConnectionPropertiesProvider {

	public ConnectionProperties getConnectionProperties() {
		return new SimpleConnectionProperties();
	}

}
