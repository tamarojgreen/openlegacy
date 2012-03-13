package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.definitions.support.SimpleAutoCompleteFieldTypeDefinition;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;

public class FieldValuesFactProcessor implements ScreenFactProcessor {

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof FieldValuesFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		FieldValuesFact fieldValuesFact = (FieldValuesFact)screenFact;

		String fieldName = fieldValuesFact.getFieldAssignDefinition().getName();
		SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinition.getFieldsDefinitions().get(
				fieldName);
		SimpleAutoCompleteFieldTypeDefinition fieldTypeDefinition = new SimpleAutoCompleteFieldTypeDefinition();
		fieldTypeDefinition.setSourceScreenEntityClassName(fieldValuesFact.getLookupWindowScreenDefinition().getEntityName());
		fieldDefinition.setFieldTypeDefinition(fieldTypeDefinition);

	}
}
