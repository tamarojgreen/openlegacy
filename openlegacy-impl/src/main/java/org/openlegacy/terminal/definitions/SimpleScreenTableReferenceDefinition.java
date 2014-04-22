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
package org.openlegacy.terminal.definitions;

import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenTableReferenceDefinition;

import java.io.Serializable;

/**
 * Represents a reference from a screen record class to another screen record class
 * 
 * @author Roi Mor
 * 
 */
public class SimpleScreenTableReferenceDefinition implements ScreenTableReferenceDefinition, Serializable {

	private static final long serialVersionUID = 1L;
	private String fieldName;
	private Class<?> relatedTableRecord;
	private Class<?> relatedScreen;

	public SimpleScreenTableReferenceDefinition(String fieldName, Class<?> relatedScreen, Class<?> relatedTableRecord) {
		this.fieldName = fieldName;
		this.relatedScreen = relatedScreen;
		this.relatedTableRecord = relatedTableRecord;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Class<?> getRelatedTableRecord() {
		return relatedTableRecord;
	}

	public Class<?> getRelatedScreen() {
		return relatedScreen;
	}
}
