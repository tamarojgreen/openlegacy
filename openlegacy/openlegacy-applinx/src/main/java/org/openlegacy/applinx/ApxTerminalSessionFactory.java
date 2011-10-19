package org.openlegacy.applinx;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sabratec.applinx.baseobject.GXGeneralException;

public class ApxTerminalSessionFactory implements TerminalSessionFactory{

	@Autowired
	private ApxServerLoader apxServerLoader;
	
	public TerminalSession getSession() {
		try {
			return new ApxTerminalSession(apxServerLoader.getSession());
		} catch (GXGeneralException e) {
			throw(new OpenLegacyProviderException(e));
		}
	}
}
