package org.openlegacy.terminal.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.DirectFieldAccessor;

import java.text.MessageFormat;

public class ScreenEntityDirectFieldAccessor implements ScreenEntityFieldAccessor {

	private DirectFieldAccessor directFieldAccessor;

	protected static final String FIELD_SUFFIX = "Field";

	private final static Log logger = LogFactory.getLog(ScreenEntityDirectFieldAccessor.class);

	private static final String TERMINAL_SCREEN = "terminalScreen";

	public ScreenEntityDirectFieldAccessor(Object target) {
		try {
			target = ProxyUtil.getTargetObject(target);
		} catch (Exception e) {
			throw (new IllegalArgumentException(e));
		}
		directFieldAccessor = new DirectFieldAccessor(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#isReadableProperty(java.lang.String)
	 */
	public boolean isReadableProperty(String fieldName) {
		return directFieldAccessor.isReadableProperty(fieldName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#isWritableProperty(java.lang.String)
	 */
	public boolean isWritableProperty(String fieldName) {
		return directFieldAccessor.isWritableProperty(fieldName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#setTerminalField(java.lang.String,
	 * org.openlegacy.terminal.TerminalField)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#setFieldValue(java.lang.String, java.lang.Object)
	 */
	public void setFieldValue(String fieldName, Object value) {
		directFieldAccessor.setPropertyValue(fieldName, value);
		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("Field {0} was set with value \"{1}\"", fieldName, value));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#setTerminalScreen(org.openlegacy.terminal.TerminalScreen)
	 */
	public void setTerminalScreen(TerminalScreen terminalScreen) {
		if (directFieldAccessor.isWritableProperty(TERMINAL_SCREEN)) {
			directFieldAccessor.setPropertyValue(TERMINAL_SCREEN, terminalScreen);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Terminal screen was set to screen entity");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#getFieldType(java.lang.String)
	 */
	public Class<?> getFieldType(String fieldName) {
		return directFieldAccessor.getPropertyType(fieldName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#getFieldValue(java.lang.String)
	 */
	public Object getFieldValue(String fieldName) {
		return directFieldAccessor.getPropertyValue(fieldName);
	}
}
