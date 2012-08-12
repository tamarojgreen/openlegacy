package org.openlegacy.providers.tn5250j;

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
			logger.debug(MessageFormat.format("Converted to logical value:{0} to new value:{1}", value, newValue));
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
