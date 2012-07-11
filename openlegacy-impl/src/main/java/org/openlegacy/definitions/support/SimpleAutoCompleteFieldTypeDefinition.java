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
package org.openlegacy.definitions.support;

import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;
import org.openlegacy.definitions.AutoCompleteFieldTypeDefinition;

public class SimpleAutoCompleteFieldTypeDefinition implements AutoCompleteFieldTypeDefinition {

	private RecordsProvider<? extends Session, Object> recordsProvider;
	private Class<?> sourceEntityClass;
	private boolean collectAll;
	private String sourceEntityClassName;

	public void setRecordsProvider(RecordsProvider<? extends Session, Object> recordsProvider) {
		this.recordsProvider = recordsProvider;
	}

	@SuppressWarnings("unchecked")
	public <S extends Session, T> RecordsProvider<S, T> getRecordsProvider() {
		return (RecordsProvider<S, T>)recordsProvider;
	}

	public Class<?> getSourceEntityClass() {
		return sourceEntityClass;
	}

	public void setSourceScreenEntityClass(Class<?> sourceScreenEntityClass) {
		this.sourceEntityClass = sourceScreenEntityClass;
	}

	/**
	 * Required for design-time where class doesn't exists yet
	 * 
	 * @param sourceScreenEntityClassName
	 */
	public void setSourceScreenEntityClassName(String sourceScreenEntityClassName) {
		this.sourceEntityClassName = sourceScreenEntityClassName;
	}

	/**
	 * Required for design-time where class doesn't exists yet
	 * 
	 * @return
	 */
	public String getSourceEntityClassName() {
		if (sourceEntityClass != null) {
			return sourceEntityClass.getSimpleName();
		}
		return sourceEntityClassName;
	}

	public boolean isCollectAll() {
		return collectAll;
	}

	public void setCollectAllRecords(boolean collectAllRecords) {
		this.collectAll = collectAllRecords;
	}

	public String getTypeName() {
		return "autocomplete";
	}

}
