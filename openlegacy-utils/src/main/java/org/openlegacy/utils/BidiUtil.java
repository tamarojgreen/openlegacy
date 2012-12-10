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

	public static String convertToLogical(String value) {
		Bidi bidi = new Bidi();
		bidi.setPara(value, Bidi.LTR, null);
		bidi.setReorderingMode(Bidi.REORDER_INVERSE_LIKE_DIRECT);
		String newValue = bidi.writeReordered(Bidi.DO_MIRRORING);

		if (logger.isDebugEnabled() && !value.equals(newValue)) {
			// logger.debug(MessageFormat.format("Converted to logical value:{0} to new value:{1}", value, newValue));
		}
		value = newValue;
		return value;
	}

	public static String convertToVisual(String value) {
		Bidi bidi = new Bidi();
		bidi.setPara(value, Bidi.RTL, null);
		String newValue = bidi.writeReordered(Bidi.DO_MIRRORING);
		if (logger.isDebugEnabled() && !value.equals(newValue)) {
			logger.debug(MessageFormat.format("Converted back to visual value:{0} to new value:{1}", value, newValue));
		}
		value = newValue;
		return value;
	}
}
