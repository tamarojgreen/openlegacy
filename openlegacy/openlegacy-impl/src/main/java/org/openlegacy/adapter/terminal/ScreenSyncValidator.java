package org.openlegacy.adapter.terminal;

import java.text.MessageFormat;

import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.exceptions.HostEntityNotAccessibleException;

/**
 * Utility class for checking terminal state against the expected state 
 *
 */
public class ScreenSyncValidator {

	public static <T> void validateCurrentScreen(Class<T> expectedScreenEntity,
			Class<?> matchedScreenEntity) {
		if (matchedScreenEntity == null) {
			throw (new HostEntityNotFoundException("Current host screen wasn''t found in the screens registry"));
		}
		if (matchedScreenEntity != expectedScreenEntity) {
			throw (new HostEntityNotAccessibleException(MessageFormat
					.format("Current host screen wasn''t matched to the requested host entity:{0}",
							expectedScreenEntity)));
		}
	}

}
