package org.openlegacy.terminal.utils;

import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.utils.ProxyUtil;

import java.text.MessageFormat;

/**
 * Utility class for checking terminal state against the expected state
 * 
 */
public class ScreenNavigationUtil {

	public static <T> void validateCurrentScreen(Class<T> expectedScreenEntity, Class<?> matchedScreenEntity) {
		if (matchedScreenEntity == null) {
			throw (new HostEntityNotFoundException("Current host screen wasn''t found in the screens registry"));
		}
		if (!ProxyUtil.isClassesMatch(matchedScreenEntity, expectedScreenEntity)) {
			throw (new HostEntityNotAccessibleException(MessageFormat.format(
					"Current host screen {0} wasn''t matched to the requested host entity:{1}", matchedScreenEntity,
					expectedScreenEntity)));
		}
	}

}
