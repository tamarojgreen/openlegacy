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

import com.sabratec.applinx.baseobject.GXIScreen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.ScreensRecognizer;

import javax.inject.Inject;

public class ApxScreensRecognizer implements ScreensRecognizer {

	private static final Object UNKNOWN = "UNKNOWN";

	@Inject
	private ScreenEntitiesRegistry screensEntitiesRegistry;

	private final static Log logger = LogFactory.getLog(ApxScreensRecognizer.class);

	public Class<?> match(TerminalSnapshot terminalSnapshot) {
		GXIScreen apxScreen = (GXIScreen)terminalSnapshot.getDelegate();
		if (apxScreen.getName().equals(UNKNOWN)) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Screen matched by ApplinX:" + apxScreen.getName());
		}
		return screensEntitiesRegistry.getEntityClass(apxScreen.getName());
	}

}
