package org.openlegacy.support;

import org.openlegacy.SessionProperties;
import org.openlegacy.SessionPropertiesProvider;
import org.openlegacy.support.SimpleSessionProperties;

import java.io.Serializable;

public class SimpleSessionPropertiesProvider implements SessionPropertiesProvider, Serializable {

	private static final long serialVersionUID = 1L;

	public SessionProperties getSessionProperties() {
		return new SimpleSessionProperties();
	}

}
