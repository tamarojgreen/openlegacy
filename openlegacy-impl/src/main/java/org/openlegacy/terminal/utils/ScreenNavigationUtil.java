/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.utils;

import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.utils.ProxyUtil;

import java.text.MessageFormat;

/**
 * Utility class for checking terminal state against the expected state
 * 
 */
public class ScreenNavigationUtil {

	public static <T> void validateCurrentScreen(Class<T> expectedScreenEntity, Class<?> matchedScreenEntity) {
		if (matchedScreenEntity == null) {
			throw (new EntityNotFoundException("Current screen wasn''t found in the screens registry"));
		}
		if (!ProxyUtil.isClassesMatch(matchedScreenEntity, expectedScreenEntity)) {
			throw (new ScreenEntityNotAccessibleException(MessageFormat.format(
					"Current screen entity {0} wasn''t matched to the requested screen entity:{1}", matchedScreenEntity,
					expectedScreenEntity)));
		}
	}

}
