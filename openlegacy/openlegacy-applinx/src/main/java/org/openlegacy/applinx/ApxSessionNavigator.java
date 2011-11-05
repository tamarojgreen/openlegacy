package org.openlegacy.applinx;

import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.baseobject.GXNavigateException;
import com.sabratec.applinx.baseobject.GXNavigateRequest;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.SessionNavigator;

import javax.inject.Inject;

public class ApxSessionNavigator implements SessionNavigator {

	@Inject
	ScreenEntitiesRegistry screenEntitiesRegistry;

	public void navigate(TerminalSession terminalSession, Class<?> targetScreenEntity) throws ScreenEntityNotAccessibleException {

		String screenName = screenEntitiesRegistry.getEntityName(targetScreenEntity);
		GXIClientBaseObject apxSession = (GXIClientBaseObject)terminalSession.getDelegate();
		GXNavigateRequest navigateRequest = new GXNavigateRequest();
		navigateRequest.setDestinationScreenName(screenName);
		try {
			apxSession.navigateTo(navigateRequest);
		} catch (GXNavigateException e) {
			throw (new ScreenEntityNotAccessibleException(e));
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

}
