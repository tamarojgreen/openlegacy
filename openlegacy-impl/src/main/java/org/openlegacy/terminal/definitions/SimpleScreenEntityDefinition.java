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
package org.openlegacy.terminal.definitions;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.support.AbstractEntityDefinition;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.services.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentification;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleScreenEntityDefinition extends AbstractEntityDefinition<ScreenFieldDefinition> implements ScreenEntityDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private ScreenIdentification screenIdentification = new SimpleScreenIdentification();
	private NavigationDefinition navigationDefinition;
	private Map<String, ScreenTableDefinition> tableDefinitions = new HashMap<String, ScreenTableDefinition>();
	private TerminalSnapshot snapshot;
	private boolean window;
	private boolean child;

	private TerminalSnapshot accessedFromSnapshot;
	private ScreenEntityDefinition accessedFromScreenDefinition;
	private ScreenSize screenSize;

	private List<ScreenEntityBinder> binders;

	private boolean performDefaultBinding = true;

	private List<ScreenFieldDefinition> keyFields;

	private final static Log logger = LogFactory.getLog(SimpleScreenEntityDefinition.class);

	public SimpleScreenEntityDefinition() {
		// for serialization purposes
		super();
	}

	public SimpleScreenEntityDefinition(String entityName, Class<?> entityClass) {
		super(entityName, entityClass);
	}

	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	public void setScreenIdentification(ScreenIdentification screenIdentification) {
		this.screenIdentification = screenIdentification;
	}

	public NavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	public void setNavigationDefinition(NavigationDefinition navigationDefinition) {
		this.navigationDefinition = navigationDefinition;
	}

	public Map<String, ScreenTableDefinition> getTableDefinitions() {
		return tableDefinitions;
	}

	public TerminalSnapshot getSnapshot() {
		return this.snapshot;
	}

	public void setSnapshot(TerminalSnapshot snapshot) {
		this.snapshot = snapshot;
	}

	public boolean isWindow() {
		return window;
	}

	public void setWindow(boolean window) {
		this.window = window;
	}

	public TerminalSnapshot getOriginalSnapshot() {
		return getSnapshot();
	}

	public TerminalSnapshot getAccessedFromSnapshot() {
		return accessedFromSnapshot;
	}

	public void setAccessedFromSnapshot(TerminalSnapshot accessedFromSnapshot) {
		this.accessedFromSnapshot = accessedFromSnapshot;
	}

	public ScreenEntityDefinition getAccessedFromScreenDefinition() {
		return this.accessedFromScreenDefinition;
	}

	public void setAccessedFromScreenDefinition(ScreenEntityDefinition accessedFromScreenDefinition) {
		this.accessedFromScreenDefinition = accessedFromScreenDefinition;
	}

	public ScreenSize getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(ScreenSize screenSize) {
		this.screenSize = screenSize;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public boolean isChild() {
		return child;
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

	public List<ScreenFieldDefinition> getSortedFields() {
		Collection<ScreenFieldDefinition> fields = getFieldsDefinitions().values();

		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
		return sortedFields;
	}

	public List<ScreenEntityBinder> getBinders() {
		return binders;
	}

	public void setBinders(List<ScreenEntityBinder> binders) {
		this.binders = binders;
	}

	public boolean isPerformDefaultBinding() {
		return performDefaultBinding;
	}

	public void setPerformDefaultBinding(boolean performDefaultBinding) {
		this.performDefaultBinding = performDefaultBinding;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<? extends FieldDefinition> getKeys() {
		if (keyFields == null) {
			keyFields = (List<ScreenFieldDefinition>)super.getKeys();
			Collection<PartEntityDefinition<ScreenFieldDefinition>> parts = getPartsDefinitions().values();
			for (PartEntityDefinition<ScreenFieldDefinition> partDefinition : parts) {
				Collection<ScreenFieldDefinition> fields = partDefinition.getFieldsDefinitions().values();
				for (ScreenFieldDefinition fieldDefinition : fields) {
					if (fieldDefinition.isKey()) {
						keyFields.add(fieldDefinition);
					}
				}
			}
		}
		Collections.sort(keyFields, new Comparator<ScreenFieldDefinition>() {

			public int compare(ScreenFieldDefinition o1, ScreenFieldDefinition o2) {
				return o1.getKeyIndex() - o2.getKeyIndex();
			}
		});
		return keyFields;
	}

	@Override
	public String getPackageName() {
		return getEntityClass().getPackage().getName();
	}

}
