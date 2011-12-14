package org.openlegacy.terminal.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.DirectFieldAccessor;

import java.text.MessageFormat;

public class SimpleScreenPojoFieldAccessor implements ScreenPojoFieldAccessor {

	private DirectFieldAccessor directFieldAccessor;

	protected static final String FIELD_SUFFIX = "Field";

	private final static Log logger = LogFactory.getLog(SimpleScreenPojoFieldAccessor.class);

	private static final String TERMINAL_SNAPSHOT = "terminalSnapshot";
	private static final String FOCUS_FIELD = "focusField";

	public SimpleScreenPojoFieldAccessor(Object target) {
		target = ProxyUtil.getTargetObject(target);
		directFieldAccessor = new DirectFieldAccessor(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#isReadableProperty(java.lang.String)
	 */
	public boolean isExists(String fieldName) {
		return directFieldAccessor.isReadableProperty(fieldName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#isWritableProperty(java.lang.String)
	 */
	public boolean isWritable(String fieldName) {
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
		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("Terminal Field {0} was set", fieldName, terminalField));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#setFieldValue(java.lang.String, java.lang.Object)
	 */
	public void setFieldValue(String fieldName, Object value) {
		directFieldAccessor.setPropertyValue(fieldName, value);
		if (logger.isDebugEnabled()) {
			if (value instanceof String) {
				String message = MessageFormat.format("Field {0} was set with value \"{1}\"", fieldName, value);
				if (!StringUtils.isEmpty(((String)value))) {
					logger.debug(message);
				} else {
					// print empty value assignment only in trace mode
					logger.trace(message);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#setTerminalSnapshot(org.openlegacy.terminal.TerminalSnapshot)
	 */
	public void setTerminalSnapshot(TerminalSnapshot terminalSnapshot) {
		if (directFieldAccessor.isWritableProperty(TERMINAL_SNAPSHOT)) {
			directFieldAccessor.setPropertyValue(TERMINAL_SNAPSHOT, terminalSnapshot);
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

	public void setFocusField(String fieldName) {
		directFieldAccessor.setPropertyValue(FOCUS_FIELD, fieldName);
	}
}
