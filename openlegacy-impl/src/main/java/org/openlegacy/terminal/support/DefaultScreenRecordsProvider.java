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
package org.openlegacy.terminal.support;

import org.openlegacy.DisplayItem;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.table.Table;
import org.openlegacy.support.SimpleDisplayItem;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.ScreenRecordsProvider;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefaultScreenRecordsProvider implements ScreenRecordsProvider, ApplicationContextAware, Serializable {

	private static final long serialVersionUID = 1L;

	private transient ApplicationContext applicationContext;

	public Map<Object, Object> getRecords(TerminalSession session, Class<?> entityClass, Class<Object> rowClass,
			boolean collectAll, String searchText) {
		List<Object> records = null;
		if (collectAll) {
			records = session.getModule(Table.class).collectAll(entityClass, rowClass);
		} else {
			records = session.getModule(Table.class).collectOne(entityClass, rowClass);
		}
		Map<Object, Object> recordsMap = new TreeMap<Object, Object>();

		if (records.size() == 0) {
			return recordsMap;
		}

		for (Object record : records) {
			DisplayItem displayItem = toDisplayItem(record);
			recordsMap.put(displayItem.getValue(), displayItem.getDisplay());
		}
		return recordsMap;
	}

	public DisplayItem toDisplayItem(Object record) {
		ScreenEntitiesRegistry screenEntitiesRegistry = applicationContext.getBean(ScreenEntitiesRegistry.class);
		ScreenTableDefinition tableDefinition = screenEntitiesRegistry.getTable(record.getClass());
		List<String> names = tableDefinition.getKeyFieldNames();
		Class<?> clazz = ProxyUtil.getObjectRealClass(record);
		if (names.size() == 0) {
			throw (new RegistryException(MessageFormat.format(
					"No key defined for table class:{0}. Unable to determine field value", clazz.getName())));
		}
		if (names.size() > 1) {
			throw (new RegistryException(MessageFormat.format(
					"More then one key fields defined for table class:{0}. Unable to determine field value", clazz.getName())));
		}

		ScreenPojoFieldAccessor screenPojoFieldAccessor = new SimpleScreenPojoFieldAccessor(record);
		String key = names.get(0);
		String keyValue = String.valueOf(screenPojoFieldAccessor.getFieldValue(key));

		List<String> mainDisplayFields = tableDefinition.getMainDisplayFields();
		String mainDisplayValue = record.toString();
		if (mainDisplayFields.size() > 0) {
			mainDisplayValue = screenPojoFieldAccessor.getConcatFieldsValue(mainDisplayFields);
		}
		return new SimpleDisplayItem(keyValue, mainDisplayValue);
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
