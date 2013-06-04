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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;

import java.text.MessageFormat;

public class SimpleScreenPojoFieldAccessor extends SimplePojoFieldAccessor implements ScreenPojoFieldAccessor {

	protected static final String FIELD_SUFFIX = "Field";

	private final static Log logger = LogFactory.getLog(SimpleScreenPojoFieldAccessor.class);

	private static final String TERMINAL_SNAPSHOT = "terminalSnapshot";
	private static final String FOCUS_FIELD = "focusField";

	public SimpleScreenPojoFieldAccessor(Object target) {
		super(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#setTerminalField(java.lang.String,
	 * org.openlegacy.terminal.TerminalField)
	 */
	public void setTerminalField(String fieldName, TerminalField terminalField) {
		fieldName = getFieldPojoName(fieldName);
		String terminalFieldName = fieldName + FIELD_SUFFIX;
		if (directFieldAccessor.isReadableProperty(terminalFieldName)) {
			directFieldAccessor.setPropertyValue(terminalFieldName, terminalField);
			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format("Terminal Field {0} was set", fieldName, terminalField));
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
	public void setFocusField(String fieldName) {
		if (directFieldAccessor.isWritableProperty(FOCUS_FIELD)) {
			directFieldAccessor.setPropertyValue(FOCUS_FIELD, fieldName);
		}
	}

}
