package org.openlegacy.terminal.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.DirectFieldAccessor;

import java.text.MessageFormat;

public class ScreenEntityDirectFieldAccessor {

	private DirectFieldAccessor directFieldAccessor;

	protected static final String FIELD_SUFFIX = "Field";

	private final static Log logger = LogFactory.getLog(ScreenEntityDirectFieldAccessor.class);

	public ScreenEntityDirectFieldAccessor(Object target) {
		try {
			target = ProxyUtil.getTargetObject(target);
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}
		directFieldAccessor = new DirectFieldAccessor(target);
	}

	public boolean isReadableProperty(String fieldName) {
		return directFieldAccessor.isReadableProperty(fieldName);
	}

	public boolean isWritableProperty(String fieldName) {
		return directFieldAccessor.isWritableProperty(fieldName);
	}

	public void setTerminalField(String fieldName, TerminalField terminalField) {
		String terminalFieldName = fieldName + FIELD_SUFFIX;
		if (directFieldAccessor.isReadableProperty(terminalFieldName)) {
			directFieldAccessor.setPropertyValue(terminalFieldName, terminalField);
		}
		String content = formatContent(terminalField);
		directFieldAccessor.setPropertyValue(fieldName, content);

		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("Field {0} was set with value \"{1}\"", fieldName, content));
		}

	}

	private static String formatContent(TerminalField terminalField) {
		// TODO should be done in configuration
		return terminalField.getValue().trim();
	}

	public void setFieldValue(String fieldName, Object value) {
		directFieldAccessor.setPropertyValue(fieldName, value);
		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("Field {0} was set with value \"{1}\"", fieldName, value));
		}
	}

	public Class<?> getFieldType(String fieldName) {
		return directFieldAccessor.getPropertyType(fieldName);
	}

	public Object getFieldValue(String fieldName) {
		return directFieldAccessor.getPropertyValue(fieldName);
	}
}
