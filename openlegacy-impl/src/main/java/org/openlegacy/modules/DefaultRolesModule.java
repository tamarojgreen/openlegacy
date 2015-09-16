/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.modules;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.User;
import org.openlegacy.modules.roles.Roles;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.support.SessionModuleAdapter;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.utils.StringUtil;
import org.springframework.beans.DirectFieldAccessor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
public class DefaultRolesModule extends SessionModuleAdapter<Session> implements Roles {

	private static final long serialVersionUID = 1L;
	private final static Log logger = LogFactory.getLog(DefaultRolesModule.class);

	@Inject
	private EntitiesRegistry<?, ?, ?> entitiesRegistry;

	@Override
	public void destroy() {}

	@Override
	public boolean isActionPermitted(SessionAction<?> action, Object entity, User loggedInUser) {
		if (loggedInUser == null || entity == null) {
			return true;
		}
		// find action definition
		ActionDefinition actionDefinition = null;
		EntityDefinition<?> entityDefinition = entitiesRegistry.get(entity.getClass());
		List<ActionDefinition> actions = entityDefinition.getActions();
		for (ActionDefinition actionDef : actions) {
			if (action.equals(actionDef.getAction())) {
				actionDefinition = actionDef;
				break;
			}
		}
		if (actionDefinition != null) {
			if (actionDefinition.isRolesRequired()) {
				List<String> actionRoles = actionDefinition.getRoles();
				String userRole = (String) loggedInUser.getProperties().get(Login.USER_ROLE_PROPERTY);
				String[] userRoles = userRole.split(",");
				return CollectionUtils.containsAny(actionRoles, Arrays.asList(userRoles));
			}
		}
		return true;
	}

	@Override
	public boolean isEntityPermitted(Class<?> entityClass, String[] userRoles) {
		EntityDefinition<?> entityDefinition = entitiesRegistry.get(entityClass);
		return isEntityPermitted(entityDefinition, userRoles);
	}

	@Override
	public boolean isEntityPermitted(String entityName, String[] userRoles) {
		EntityDefinition<?> entityDefinition = entitiesRegistry.get(entityName);
		return isEntityPermitted(entityDefinition, userRoles);
	}

	@Override
	public void populateEntity(Object entity, Login loginModule) {
		if (loginModule == null || loginModule.getLoggedInUser() == null) {
			return;
		}
		String userRole = (String) loginModule.getLoggedInUser().getProperties().get(Login.USER_ROLE_PROPERTY);
		if (userRole != null) {
			String[] userRoles = userRole.split(",");
			if (Collection.class.isAssignableFrom(entity.getClass())) {
				Collection<?> collection = (Collection<?>) entity;
				for (Object item : collection) {
					populateEntity(item, userRoles);
				}
			} else if (Map.class.isAssignableFrom(entity.getClass())) {
				Map<?, ?> map = (Map<?, ?>) entity;
				for (Object item : map.values()) {
					populateEntity(item, userRoles);
				}
			} else {
				populateEntity(entity, userRoles);
			}

		}
	}

	@SuppressWarnings("unchecked")
	private void populateEntity(Object entity, String[] userRoles) {
		if (entity == null) {
			return;
		}
		EntityDefinition<?> entityDefinition = entitiesRegistry.get(entity.getClass());
		if (entityDefinition == null) {
			return;
		}

		Collection<?> fieldDefinitions = (Collection<?>) entityDefinition.getFieldsDefinitions().values();

		if (entityDefinition instanceof DbEntityDefinition) {
			DbEntityDefinition dbEntityDefinition = (DbEntityDefinition) entityDefinition;
			fieldDefinitions = dbEntityDefinition.getColumnFieldsDefinitions().values();
		}
		// entity level fields
		for (Object definition : fieldDefinitions) {
			FieldDefinition fieldDefinition = (FieldDefinition) definition;
			List<String> fieldDefinitionRoles = fieldDefinition.getRoles();
			if (fieldDefinitionRoles != null && !fieldDefinitionRoles.isEmpty()) {
				if (!CollectionUtils.containsAny(fieldDefinitionRoles, Arrays.asList(userRoles))) {
					String fieldName = fieldDefinition.getName();
					try {
						DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(entity);
						directFieldAccessor.setPropertyValue(StringUtil.removeNamespace(fieldName), null);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}

			// if field it is another entity (in DB for example)
			Class<?> fieldJavaType = fieldDefinition.getJavaType();
			if (!fieldJavaType.isPrimitive()) {
				String fieldName = fieldDefinition.getName();
				DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(entity);
				Object value = directFieldAccessor.getPropertyValue(StringUtil.removeNamespace(fieldName));
				if (value == null) {
					continue;
				}
				if (Collection.class.isAssignableFrom(value.getClass())) {
					Collection<?> collection = (Collection<?>) value;
					for (Object item : collection) {
						populateEntity(item, userRoles);
					}
				} else if (Map.class.isAssignableFrom(value.getClass())) {
					Map<?, ?> map = (Map<?, ?>) value;
					for (Object item : map.values()) {
						populateEntity(item, userRoles);
					}
				} else {
					populateEntity(value, userRoles);
				}
			}

		}
		// part level fields
		Map<String, ?> partsDefinitions = entityDefinition.getPartsDefinitions();
		for (Object part : partsDefinitions.values()) {
			PartEntityDefinition<FieldDefinition> partDefinition = (PartEntityDefinition<FieldDefinition>) part;
			String partName = partDefinition.getPartName();
			DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(entity);
			Object partObject = directFieldAccessor.getPropertyValue(partName);
			Map<String, FieldDefinition> definitions = partDefinition.getFieldsDefinitions();
			for (Object definition : definitions.values()) {
				FieldDefinition fieldDefinition = (FieldDefinition) definition;
				List<String> fieldDefinitionRoles = fieldDefinition.getRoles();
				if (fieldDefinitionRoles != null && !fieldDefinitionRoles.isEmpty()) {
					if (!CollectionUtils.containsAny(fieldDefinitionRoles, Arrays.asList(userRoles))) {
						String fieldName = fieldDefinition.getName();
						try {
							directFieldAccessor = new DirectFieldAccessor(partObject);
							directFieldAccessor.setPropertyValue(StringUtil.removeNamespace(fieldName), null);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
			}
		}
	}

	private boolean isEntityPermitted(EntityDefinition<?> entityDefinition, String[] userRoles) {
		if (entityDefinition == null) {
			return false;
		}
		ActionDefinition actionDefinition = null;
		if (entityDefinition instanceof ScreenEntityDefinition) {
			actionDefinition = entityDefinition.getAction(org.openlegacy.terminal.actions.TerminalActions.SHOW.class);
		} else if (entityDefinition instanceof RpcEntityDefinition) {
			actionDefinition = entityDefinition.getAction(org.openlegacy.rpc.actions.RpcActions.SHOW.class);
		} else if (entityDefinition instanceof DbEntityDefinition) {
			actionDefinition = entityDefinition.getAction(org.openlegacy.db.actions.DbActions.SHOW.class);
		}
		if (actionDefinition != null && actionDefinition.isRolesRequired()) {
			if (actionDefinition.getRoles() != null && actionDefinition.getRoles().size() > 0) {
				if (!CollectionUtils.containsAny(actionDefinition.getRoles(), Arrays.asList(userRoles))) {
					return false;
				}
			}
		}
		return true;
	}

}
