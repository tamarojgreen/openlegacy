package org.openlegacy.applinx;

import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ApxTerminalSessionFactory implements TerminalSessionFactory {

	@Autowired
	private ApxServerLoader apxServerLoader;

	public TerminalSession getSession() {
		try {
			return new ApxTerminalSession(apxServerLoader.getSession());
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public void disconnect(TerminalSession terminalSession) {
		try {
			GXClientBaseObjectFactory.endSession((GXIClientBaseObject)terminalSession.getDelegate());
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}
}
