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
package org.openlegacy.utils;

import com.ibm.icu.text.Bidi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.MessageFormat;

public class BidiUtil {

	private final static Log logger = LogFactory.getLog(BidiUtil.class);

	public static String convertToLogical(String value, boolean isRtl) {
		if (isRtl) {
			value = value.trim();
		}
		Bidi bidi = new Bidi();
		byte direction = isRtl ? Bidi.RTL : Bidi.LTR;
		bidi.setPara(value, direction, null);
		bidi.setReorderingMode(Bidi.REORDER_INVERSE_LIKE_DIRECT);
		String newValue = bidi.writeReordered(Bidi.DO_MIRRORING);

		if (logger.isDebugEnabled() && !value.equals(newValue)) {
			// logger.debug(MessageFormat.format("Converted to logical value:{0} to new value:{1}", value, newValue));
		}
		value = newValue;
		return value;
	}

	public static String convertToVisual(String value) {
		if (!containsRTLChar(value)) {
			return value;
		}
		Bidi bidi = new Bidi();
		bidi.setPara(value, Bidi.RTL, null);
		String newValue = bidi.writeReordered(Bidi.DO_MIRRORING);
		if (logger.isDebugEnabled() && !value.equals(newValue)) {
			logger.debug(MessageFormat.format("Converted back to visual value:{0} to new value:{1}", value, newValue));
		}
		value = newValue;
		return value;
	}

	public static boolean containsRTLChar(String str) {
		return containsArabicChar(str) || containsHebrewChar(str);
	}

	public static boolean containsHebrewChar(String str) {
		boolean reply = false;
		for (int i = 0; i < str.length(); i++) {
			if (isHebrewChar(str.charAt(i))) {
				reply = true;
				break;
			}
		}
		return reply;
	}

	public static boolean containsArabicChar(String str) {
		boolean reply = false;
		for (int i = 0; i < str.length(); i++) {
			if (isArabicChar(str.charAt(i))) {
				reply = true;
				break;
			}
		}
		return reply;
	}

	public static boolean isHebrewChar(char inputChar) {
		if (inputChar >= 0x5D0 && inputChar <= 0x5EA) { // HEBREW ALEF TO TAV
			return true;
		} else {
			return false;
		}
	}

	public static boolean isArabicChar(char inputChar) {
		if ((inputChar >= 0x0600 && inputChar <= 0x06FF) // 0600-06FF Arabic
				|| (inputChar >= 0xFB50 && inputChar <= 0xFDFD) // FB50-FDFD Unicode Characters in the Arabic Presentation Forms-A
																// Block
				|| (isArabicCharFormB(inputChar)) // FE70-FEFC Unicode Characters in the Arabic Presentation Forms-B Block
		) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isArabicCharFormB(char inputChar) {
		if (inputChar >= 0xFE70 && inputChar <= 0xFEFC) // FE70-FEFC Unicode Characters in the Arabic Presentation Forms-B Block
		{
			return true;
		} else {
			return false;
		}
	}

	public static boolean isArabicNumber(char inputChar) {
		if (inputChar >= 0x0660 && inputChar <= 0x0669) {
			return true;
		} else {
			return false;
		}
	}

}
