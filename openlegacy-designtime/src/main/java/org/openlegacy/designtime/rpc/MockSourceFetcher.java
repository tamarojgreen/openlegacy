package org.openlegacy.designtime.rpc;

import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.rpc.SourceFetcher;

public class MockSourceFetcher implements SourceFetcher {

	@Override
	public byte[] fetch(String host, String user, String password, String legacyFile) throws OpenLegacyException {
		return new byte[0];
	}

	@Override
	public String convertExtension(String legacyName) {
		return legacyName;
	}

}
