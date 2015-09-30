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
package org.openlegacy.definitions.support;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.annoation.support.LifeCycle;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.terminal.ScreenEntityType;
import org.openlegacy.utils.StringUtil;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractEntityDefinition<F extends FieldDefinition> implements EntityDefinition<F>, Serializable {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(AbstractEntityDefinition.class);

	private String entityName;
	private Class<?> entityClass;

	private Class<? extends EntityType> entityType;

	// LinkedHashMap preserve insert order
	private final Map<String, F> fieldsDefinitions = new LinkedHashMap<String, F>();
	private Map<String, F> allFieldsDefinitions = null;

	private String displayName;
	private List<F> keys; // change name to math getter
	private List<ActionDefinition> actions = new ArrayList<ActionDefinition>();

	private List<EntityDefinition<?>> childEntitiesDefinitions = new ArrayList<EntityDefinition<?>>();

	private Map<String, PartEntityDefinition<F>> partsDefinitions = new LinkedHashMap<String, PartEntityDefinition<F>>();

	private boolean validateKeys = true;

	private List<String> roles;

	private LifeCycle lifeCycle;

	public AbstractEntityDefinition() {
		// for serialization purposes
	}

	public AbstractEntityDefinition(String entityName, Class<?> screenEntityClass) {
		this.entityName = entityName;
		this.entityClass = screenEntityClass;
	}

	@Override
	public String getEntityName() {
		return entityName;
	}

	@Override
	public Class<?> getEntityClass() {
		return entityClass;
	}

	@Override
	public String getEntityClassName() {
		return getEntityClass().getSimpleName();
	}

	@Override
	public Map<String, F> getFieldsDefinitions() {
		return fieldsDefinitions;
	}

	@Override
	public Map<String, F> getAllFieldsDefinitions() {
		if (allFieldsDefinitions == null) {
			allFieldsDefinitions = new LinkedHashMap<String, F>();
			allFieldsDefinitions.putAll(getFieldsDefinitions());
			Collection<PartEntityDefinition<F>> parts = getPartsDefinitions().values();
			for (PartEntityDefinition<F> part : parts) {
				allFieldsDefinitions.putAll(part.getFieldsDefinitions());
			}
		}
		return allFieldsDefinitions;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public Class<? extends EntityType> getType() {
		return entityType;
	}

	@Override
	public String getTypeName() {
		if (getType() == null) {
			return ScreenEntityType.General.class.getSimpleName();
		}
		return getType().getSimpleName();
	}

	public void setType(Class<? extends EntityType> entityType) {
		this.entityType = entityType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public F getFirstFieldDefinition(Class<? extends FieldType> fieldType) {
		Collection<? extends FieldDefinition> fieldValues = fieldsDefinitions.values();
		FieldDefinition matchedFieldDefinition = null;
		for (FieldDefinition fieldDefinition : fieldValues) {
			if (fieldDefinition.getType() == fieldType) {
				if (matchedFieldDefinition != null) {
					throw (new RegistryException(MessageFormat.format("Found 2 field of type {0} in class {1}", fieldType,
							getEntityClass())));
				}
				matchedFieldDefinition = fieldDefinition;
			}
		}
		return (F)matchedFieldDefinition;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setEntityName(String entityName) {

		this.entityName = StringUtil.toClassName(entityName);

	}

	@Override
	public List<? extends FieldDefinition> getKeys() {
		if (keys == null) {
			Collection<F> allFields = fieldsDefinitions.values();
			keys = new ArrayList<F>();
			for (F field : allFields) {
				if (field.isKey()) {
					keys.add(field);
				}
			}
		}
		return keys;
	}

	@Override
	public List<EntityDefinition<?>> getChildEntitiesDefinitions() {
		return childEntitiesDefinitions;
	}

	@Override
	public String getPackageName() {
		return getEntityClass().getPackage().getName();
	}

	@Override
	public Set<EntityDefinition<?>> getAllChildEntitiesDefinitions() {
		@SuppressWarnings("unchecked")
		Set<EntityDefinition<?>> childs = new ListOrderedSet();
		childs.addAll(getChildEntitiesDefinitions());
		for (EntityDefinition<?> childScreenDefinition : childs) {
			Set<EntityDefinition<?>> childScreensDefinitions = childScreenDefinition.getAllChildEntitiesDefinitions();
			if (childScreensDefinitions.size() > 0) {
				logger.info(MessageFormat.format("Adding child screens to list all child screens. Adding: {0}",
						childScreensDefinitions));
				childs.addAll(childScreensDefinitions);
			}
		}
		return childs;
	}

	@Override
	public List<ActionDefinition> getActions() {
		return actions;
	}

	@Override
	public ActionDefinition getAction(Class<?> actionClass) {
		for (ActionDefinition actionDefinition : actions) {
			if (actionDefinition.getAction().getClass() == actionClass) {
				return actionDefinition;
			}
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<F> getFieldDefinitions(Class<? extends FieldType> fieldType) {
		Collection<? extends FieldDefinition> fieldValues = fieldsDefinitions.values();
		List<FieldDefinition> matchedFieldsDefinitions = new ArrayList<FieldDefinition>();
		for (FieldDefinition fieldDefinition : fieldValues) {
			if (fieldDefinition.getType() == fieldType) {
				matchedFieldsDefinitions.add(fieldDefinition);
			}
		}
		return (List<F>)matchedFieldsDefinitions;
	}

	@Override
	public Map<String, PartEntityDefinition<F>> getPartsDefinitions() {
		return partsDefinitions;
	}

	@Override
	public boolean isValidateKeys() {
		return validateKeys;
	}

	public void setValidateKeys(boolean validateKeys) {
		this.validateKeys = validateKeys;
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public boolean isEmpty() {
		return getFieldsDefinitions().size() == 0 && getPartsDefinitions().size() == 0;
	}

	@Override
	public LifeCycle getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(LifeCycle lifeCycle) {
		this.lifeCycle = lifeCycle;
	}
}
