/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.modules.globals;

import org.openlegacy.ApplicationConnection;
import org.openlegacy.RemoteAction;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.globals.Globals;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DefaultGlobalsModule extends TerminalSessionModuleAdapter implements Globals {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> globals = new HashMap<String, Object>();

	@Inject
	private transient ApplicationContext applicationContext;

	@Override
	public Map<String, Object> getGlobals() {
		return globals;
	}

	@Override
	public Object getGlobal(String name) {
		return globals.get(name);
	}

	@Override
	public void afterConnect(ApplicationConnection<?, ?> terminalConnection) {
		collectGlobals(null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void beforeAction(ApplicationConnection<?, ?> cconnection, RemoteAction action) {
		collectGlobals(action);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterAction(ApplicationConnection<?, ?> connection, RemoteAction action, Snapshot result) {
		collectGlobals(action);
	}

	@SuppressWarnings("rawtypes")
	private void collectGlobals(RemoteAction action) {
		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		if (!getSession().isConnected()) {
			return;
		}
		ScreenEntity currentEntity = getSession().getEntity();
		if (currentEntity == null) {
			return;
		}

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);

		ScreenEntityDefinition entityDefinitions = screenEntitiesRegistry.get(currentEntity.getClass());

		Collection<ScreenFieldDefinition> fields = entityDefinitions.getAllFieldsDefinitions().values();

		for (ScreenFieldDefinition fieldDefinition : fields) {
			if (!fieldDefinition.isGlobal()) {
				continue;
			}
			String globalFieldName = fieldDefinition.getName();
			Object globalFieldValue = null;
			if (action != null && fieldDefinition.isEditable()) {
				@SuppressWarnings("unchecked")
				List<TerminalField> modifiedFields = action.getFields();
				for (TerminalField modifiedField : modifiedFields) {
					if (modifiedField.getPosition().equals(fieldDefinition.getPosition())) {
						globalFieldValue = modifiedField.getValue();
						break;
					}
				}
			} else {
				globalFieldValue = fieldAccessor.getFieldValue(globalFieldName);
			}
			if (globalFieldValue != null) {
				globals.put(globalFieldName, globalFieldValue);
			}
		}
	}

	@Override
	public void beforeConnect(ApplicationConnection<?, ?> applicationConnection) {
		// TODO Auto-generated method stub

	}
}
