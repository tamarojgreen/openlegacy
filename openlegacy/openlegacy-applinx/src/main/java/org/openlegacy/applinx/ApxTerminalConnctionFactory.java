package org.openlegacy.applinx;

import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;

import javax.inject.Inject;

public class ApxTerminalConnctionFactory implements TerminalConnectionFactory {

	@Inject
	private ApxServerLoader apxServerLoader;

	public TerminalConnection getConnection() {
		try {
			return new ApxTerminalConnection(apxServerLoader.getSession());
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public void disconnect(TerminalConnection terminalConnection) {
		try {
			GXClientBaseObjectFactory.endSession((GXIClientBaseObject)terminalConnection.getDelegate());
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}
}
