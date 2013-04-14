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
package org.openlegacy.designtime.generators;

import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedDefinitionUtils;
import org.openlegacy.terminal.PositionedPart;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;
import org.openlegacy.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CodeBasedScreenPartDefinition implements ScreenPartEntityDefinition, PositionedPart {

	private ScreenPojoCodeModel codeModel;
	private Map<String, ScreenFieldDefinition> fields;

	private int width;
	private TerminalPosition partPosition;

	public CodeBasedScreenPartDefinition(ScreenPojoCodeModel codeModel) {
		this.codeModel = codeModel;
	}

	public Class<?> getPartClass() {
		throw (new UnsupportedOperationException("Code based screen part does not support this method"));
	}

	public Map<String, ScreenFieldDefinition> getFieldsDefinitions() {
		if (fields == null) {
			String fieldName = StringUtil.toJavaFieldName(codeModel.getEntityName());
			fields = CodeBasedDefinitionUtils.getFieldsFromCodeModel(codeModel, fieldName);
		}
		return fields;
	}

	public List<ScreenFieldDefinition> getSortedFields() {
		Collection<ScreenFieldDefinition> fields = getFieldsDefinitions().values();
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
		return sortedFields;
	}

	public String getPartName() {
		return codeModel.getEntityName();
	}

	public TerminalPosition getPartPosition() {
		if (partPosition == null) {
			partPosition = codeModel.getPartPosition();
		}
		return partPosition;
	}

	public int getWidth() {
		if (width == 0) {
			width = codeModel.getPartWidth();
		}
		return width;
	}

	public String getDisplayName() {
		return codeModel.getDisplayName();
	}

	public void setWidth(int width) {
		this.width = width;

	}

	public void setPartPosition(TerminalPosition partPosition) {
		this.partPosition = partPosition;
	}

	public boolean isSupportTerminalData() {
		return codeModel.isSupportTerminalData();
	}

	public String getClassName() {
		return codeModel.getClassName();
	}

	public int getTopRow() {
		throw (new UnsupportedOperationException("Code based screen part does not support this method"));
	}
}
