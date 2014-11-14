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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

public class DateFieldFactProcessor implements ScreenFactProcessor {

	private final static Log logger = LogFactory.getLog(DateFieldFactProcessor.class);

	@Override
	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof DateFieldFact;
	}

	@Override
	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		DateFieldFact dateFieldFact = (DateFieldFact)screenFact;

		SimpleScreenFieldDefinition leftFieldDefinition = (SimpleScreenFieldDefinition)dateFieldFact.getLeftField();
		ScreenFieldDefinition middleFieldDefinition = dateFieldFact.getMiddleField();
		ScreenFieldDefinition rightFieldDefinition = dateFieldFact.getRightField();

		Map<String, ScreenFieldDefinition> fieldsDefinitions = screenEntityDefinition.getFieldsDefinitions();

		SimpleDateFieldTypeDefinition fieldTypeDefinition = new SimpleDateFieldTypeDefinition(
				dateFieldFact.getLeftField().getPosition().getColumn(), middleFieldDefinition.getPosition().getColumn(),
				rightFieldDefinition.getPosition().getColumn());
		leftFieldDefinition.setFieldTypeDefinition(fieldTypeDefinition);
		leftFieldDefinition.setJavaType(Date.class);

		screenEntityDefinition.getReferredClasses().add(Date.class.getName());

		// remove all 3 fields date fields and add with the correct name. The middle/last date fields may take the label field
		// name as drools as can't verify analysis order
		fieldsDefinitions.remove(leftFieldDefinition.getName());
		if (middleFieldDefinition != null) {
			fieldsDefinitions.remove(middleFieldDefinition.getName());
		}
		if (rightFieldDefinition != null) {
			fieldsDefinitions.remove(rightFieldDefinition.getName());
		}

		// set the length as all 3 - as place holder
		leftFieldDefinition.setLength(rightFieldDefinition.getEndPosition().getColumn()
				- leftFieldDefinition.getPosition().getColumn());

		// re-add the field
		String fieldName = StringUtil.toJavaFieldName(dateFieldFact.getLabelField().getValue());
		leftFieldDefinition.setName(fieldName);
		fieldsDefinitions.put(fieldName, leftFieldDefinition);

		logger.info(MessageFormat.format("Set field {0} to be date field", leftFieldDefinition.getName()));
	}
}
