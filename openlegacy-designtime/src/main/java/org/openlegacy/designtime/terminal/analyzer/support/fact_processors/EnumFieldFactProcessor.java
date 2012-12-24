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

import org.apache.commons.lang.StringUtils;
import org.openlegacy.DisplayItem;
import org.openlegacy.definitions.EnumFieldTypeDefinition;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.designtime.analyzer.TextTranslator;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.support.SimpleDisplayItem;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.StringUtil;

import javax.inject.Inject;

public class EnumFieldFactProcessor implements ScreenFactProcessor {

	@Inject
	private TextTranslator textTranslator;

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof EnumFieldFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		EnumFieldFact enumFieldFact = (EnumFieldFact)screenFact;

		String enumText = enumFieldFact.getEnumText();
		String[] pairs = StringUtils.split(enumText, enumFieldFact.getEntrySeperators());

		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(EnumGetValue.class));

		EnumFieldTypeDefinition enumFieldTypeDefinition = new SimpleEnumFieldTypeDefinition();

		for (String pair : pairs) {
			String[] keyValue = StringUtils.split(pair, enumFieldFact.getPairSeperators());
			if (keyValue.length == 2) {
				DisplayItem displayItem = new SimpleDisplayItem(keyValue[0], keyValue[1]);
				String key = textTranslator.translate(keyValue[1]);
				key = StringUtil.toVariableName(key, true);
				enumFieldTypeDefinition.getEnums().put(key, displayItem);
			}
		}
		SimpleScreenFieldDefinition enumFieldDefinition = (SimpleScreenFieldDefinition)enumFieldFact.getEnumFieldDefinition();
		enumFieldDefinition.setFieldTypeDefinition(enumFieldTypeDefinition);
		enumFieldDefinition.setJavaTypeName(StringUtil.toClassName(enumFieldDefinition.getName()));
	}
}
