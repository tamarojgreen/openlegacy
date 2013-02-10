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
import org.openlegacy.definitions.FieldWithValuesTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.Serializable;

public class SimpleFieldWthValuesTypeDefinition implements FieldWithValuesTypeDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private RecordsProvider<? extends Session, Object> recordsProvider;
	private Class<?> sourceEntityClass;
	private boolean collectAll;
	private ScreenEntityDefinition sourceEntityDefinition;
	private String sourceEntityClassName;

	private String displayFieldName;

	private boolean asWindow;

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

	/**
	 * Required for design-time where class doesn't exists yet
	 * 
	 * @param sourceEntityClassName
	 */

	public void setSourceEntityClass(Class<?> sourceEntityClass) {
		this.sourceEntityClass = sourceEntityClass;
	}

	/**
	 * Required for design-time where class doesn't exists yet
	 * 
	 * @return
	 */

	public ScreenEntityDefinition getSourceEntityDefinition() {
		return sourceEntityDefinition;
	}

	public void setSourceEntityDefinition(ScreenEntityDefinition sourceEntityDefinition) {
		this.sourceEntityDefinition = sourceEntityDefinition;
	}

	public boolean isCollectAll() {
		return collectAll;
	}

	public void setCollectAllRecords(boolean collectAllRecords) {
		this.collectAll = collectAllRecords;
	}

	public String getTypeName() {
		return "fieldWithValues";
	}

	public void setSourceEntityClassName(String sourceEntityClassName) {
		this.sourceEntityClassName = sourceEntityClassName;
	}

	public String getSourceEntityClassName() {
		if (sourceEntityClassName != null) {
			return sourceEntityClassName;
		}
		return sourceEntityClass.getSimpleName();
	}

	public String getDisplayFieldName() {
		return displayFieldName;
	}

	public void setDisplayFieldName(String displayFieldName) {
		this.displayFieldName = displayFieldName;
	}

	public void setAsWindow(boolean asWindow) {
		this.asWindow = asWindow;
	}

	public boolean isAsWindow() {
		return asWindow;
	}
}
