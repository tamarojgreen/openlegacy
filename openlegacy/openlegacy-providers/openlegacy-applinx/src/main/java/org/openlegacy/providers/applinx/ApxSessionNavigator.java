/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.baseobject.GXNavigateException;
import com.sabratec.applinx.baseobject.GXNavigateRequest;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.SessionNavigator;

import javax.inject.Inject;

public class ApxSessionNavigator implements SessionNavigator {

	@Inject
	ScreenEntitiesRegistry screenEntitiesRegistry;

	public void navigate(TerminalSession terminalSession, Class<?> targetScreenEntity, Object... keys)
			throws ScreenEntityNotAccessibleException {

		String screenName = screenEntitiesRegistry.getEntityName(targetScreenEntity);
		GXIClientBaseObject apxSession = (GXIClientBaseObject)terminalSession.getDelegate();

		try {
			if (screenName.equals(apxSession.getScreen().getName())) {
				return;
			}
			GXNavigateRequest navigateRequest = new GXNavigateRequest();
			navigateRequest.setDestinationScreenName(screenName);
			apxSession.navigateTo(navigateRequest);
			terminalSession.fetchSnapshot();
		} catch (GXNavigateException e) {
			throw (new ScreenEntityNotAccessibleException(e));
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

}
