package org.openlegacy.applinx;

import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.baseobject.internal.GXClientScreen;
import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;

import org.openlegacy.adapter.terminal.TerminalSessionAdapter;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalScreen;

public class ApxTerminalSession extends TerminalSessionAdapter {

	private final GXIClientBaseObject baseObject;

	public ApxTerminalSession(GXIClientBaseObject baseObject) {
		this.baseObject = baseObject;
	}

	public TerminalScreen getSnapshot() {
		return newHostScreen();
	}

	private TerminalScreen newHostScreen() {
		try {
			GXRuntimeScreen screen = ((GXClientScreen)baseObject.getScreen()).getRuntimeScreen();
			return new ApxTerminalScreen(screen);
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public void disconnect() {
		try {
			GXClientBaseObjectFactory.detachSession(baseObject);
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public Object getDelegate() {
		return baseObject;
	}

}
