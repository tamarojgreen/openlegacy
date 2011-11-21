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
