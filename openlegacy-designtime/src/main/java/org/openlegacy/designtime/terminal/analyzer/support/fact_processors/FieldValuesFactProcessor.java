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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.definitions.support.SimpleFieldWthValuesTypeDefinition;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.table.LookupEntity;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;

public class FieldValuesFactProcessor implements ScreenFactProcessor {

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof FieldValuesFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		FieldValuesFact fieldValuesFact = (FieldValuesFact)screenFact;

		String fieldName = fieldValuesFact.getFieldAssignDefinition().getName();
		SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinition.getFieldsDefinitions().get(
				fieldName);
		SimpleFieldWthValuesTypeDefinition fieldTypeDefinition = new SimpleFieldWthValuesTypeDefinition();
		ScreenEntityDesigntimeDefinition lookupWindowScreenDefinition = (ScreenEntityDesigntimeDefinition)fieldValuesFact.getLookupWindowScreenDefinition();
		lookupWindowScreenDefinition.setType(LookupEntity.class);
		lookupWindowScreenDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(LookupEntity.class));
		// link the field type to the lookup screen class
		fieldTypeDefinition.setSourceEntityDefinition(lookupWindowScreenDefinition);

		fieldDefinition.setFieldTypeDefinition(fieldTypeDefinition);

	}
}
