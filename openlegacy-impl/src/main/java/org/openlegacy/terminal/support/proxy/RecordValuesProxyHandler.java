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
package org.openlegacy.terminal.support.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;
import org.openlegacy.definitions.FieldWithValuesTypeDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.Map;

import javax.inject.Inject;

public class RecordValuesProxyHandler implements ScreenEntityProxyHandler, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String VALUES = "Values";

	@Inject
	private transient ApplicationContext applicationContext;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	public Object invoke(TerminalSession terminalSession, MethodInvocation invocation) throws OpenLegacyRuntimeException {
		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		Object target = invocation.getThis();
		String methodName = invocation.getMethod().getName();
		if (methodName.endsWith(VALUES)) {
			methodName = methodName.substring(0, methodName.length() - VALUES.length());
		}
		String propertyName = PropertyUtil.getPropertyNameIfGetter(methodName);

		Class<?> entityClass = target.getClass();
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(entityClass);
		ScreenFieldDefinition fieldDefinition = screenEntityDefinition.getFieldsDefinitions().get(propertyName);
		if (!(fieldDefinition.getFieldTypeDefinition() instanceof FieldWithValuesTypeDefinition)) {
			return null;
		}
		FieldWithValuesTypeDefinition fieldTypeDefinition = (FieldWithValuesTypeDefinition)fieldDefinition.getFieldTypeDefinition();
		RecordsProvider<Session, Object> recordsProvider = fieldTypeDefinition.getRecordsProvider();

		ScreenTableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				fieldTypeDefinition.getSourceEntityClass()).getValue();

		@SuppressWarnings("unchecked")
		Map<Object, Object> records = recordsProvider.getRecords(terminalSession, fieldTypeDefinition.getSourceEntityClass(),
				(Class<Object>)tableDefinition.getTableClass(), fieldTypeDefinition.isCollectAll(), null);
		return records;
	}

}
