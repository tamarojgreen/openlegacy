package org.openlegacy.rpc;

import org.openlegacy.exceptions.OpenLegacyException;

public interface SourceFetcher {

	byte[] fetch(String host, String user, String password, String legacyFile) throws OpenLegacyException;

	String convertExtension(String legacyName);
}
