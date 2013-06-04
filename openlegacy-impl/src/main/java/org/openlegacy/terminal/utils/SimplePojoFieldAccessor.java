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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.PojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.beans.DirectFieldAccessor;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePojoFieldAccessor implements PojoFieldAccessor {

	protected DirectFieldAccessor directFieldAccessor;

	private Object target;

	private final static Log logger = LogFactory.getLog(SimplePojoFieldAccessor.class);

	private Map<String, DirectFieldAccessor> partAccessors;

	private String concatSeperator = " - ";

	public SimplePojoFieldAccessor(Object target) {
		target = ProxyUtil.getTargetObject(target);
		directFieldAccessor = new DirectFieldAccessor(target);
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#isReadableProperty(java.lang.String)
	 */
	public boolean isExists(String fieldName) {
		return directFieldAccessor.isReadableProperty(getFieldPojoName(fieldName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#isWritableProperty(java.lang.String)
	 */
	public boolean isWritable(String fieldName) {
		return directFieldAccessor.isWritableProperty(getFieldPojoName(fieldName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.terminal.utils.ScreenEntityFieldAccessor#setFieldValue(java.lang.String, java.lang.Object)
	 */
	public void setFieldValue(String fieldName, Object value) {
		try {
			directFieldAccessor.setPropertyValue(getFieldPojoName(fieldName), value);
		} catch (Exception e) {
			logger.error(MessageFormat.format("Unable to update entity field: {0}.{1}", target.getClass().getSimpleName(),
					fieldName, e));
			return;
		}
		if (logger.isDebugEnabled()) {
			if (value instanceof String) {
				String message = MessageFormat.format("Field {0} was set with value \"{1}\"", fieldName, value);
				if (!StringUtils.isEmpty(((String)value))) {
					logger.debug(message);
				} else {
					// print empty value assignment only in trace mode
					logger.trace(message);
				}
			} else {
				String message = MessageFormat.format("Field {0} was set with value \"{1}\"", fieldName, value);
				logger.debug(message);
			}
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
		return directFieldAccessor.getPropertyValue(getFieldPojoName(fieldName));
	}

	public Object evaluateFieldValue(String fieldName) {
		if (fieldName.contains(".")) {
			String partName = fieldName.substring(0, fieldName.indexOf("."));
			fieldName = getFieldPojoName(fieldName);
			DirectFieldAccessor partAccesor = getPartAccessor(partName);
			return partAccesor.getPropertyValue(fieldName);
		}
		return getFieldValue(fieldName);

	}

	public DirectFieldAccessor getPartAccessor(String partName) {
		if (partAccessors == null) {
			partAccessors = new HashMap<String, DirectFieldAccessor>();
		}
		DirectFieldAccessor partAccessor = partAccessors.get(partName);
		if (partAccessor == null) {
			partAccessor = new DirectFieldAccessor(directFieldAccessor.getPropertyValue(partName));
			partAccessors.put(partName, partAccessor);
		}
		return partAccessor;
	}

	protected static String getFieldPojoName(String fieldName) {
		return StringUtil.removeNamespace(fieldName);
	}

	/**
	 * Concatenate the given field values
	 */
	public String getConcatFieldsValue(List<String> mainDisplayFields) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < mainDisplayFields.size(); i++) {
			result.append(getFieldValue(mainDisplayFields.get(i)));
			if (i < mainDisplayFields.size() - 1) {
				result.append(concatSeperator);
			}
		}
		return result.toString();
	}

	public void setConcatSeperator(String concatSeperator) {
		this.concatSeperator = concatSeperator;
	}
}
