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

public class DateFieldFactProcessor implements ScreenFactProcessor {

	private final static Log logger = LogFactory.getLog(DateFieldFactProcessor.class);

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof DateFieldFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		DateFieldFact dateFieldFact = (DateFieldFact)screenFact;

		SimpleScreenFieldDefinition leftFieldDefinition = (SimpleScreenFieldDefinition)dateFieldFact.getLeftField();

		ScreenFieldDefinition middleField = dateFieldFact.getMiddleField();
		ScreenFieldDefinition rightField = dateFieldFact.getRightField();

		leftFieldDefinition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition(
				dateFieldFact.getLeftField().getPosition().getColumn(), middleField.getPosition().getColumn(),
				rightField.getPosition().getColumn()));

		// remove all 3 fields date fields and add with the correct name. The middle/last date fields may take the label field
		// name as drools as can't verify analysis order
		screenEntityDefinition.getFieldsDefinitions().remove(leftFieldDefinition.getName());
		screenEntityDefinition.getFieldsDefinitions().remove(middleField.getName());
		screenEntityDefinition.getFieldsDefinitions().remove(rightField.getName());

		String fieldName = StringUtil.toJavaFieldName(dateFieldFact.getLabelField().getValue());
		// re-add the field
		leftFieldDefinition.setName(fieldName);
		screenEntityDefinition.getFieldsDefinitions().put(fieldName, leftFieldDefinition);

		logger.info(MessageFormat.format("Set field {0} to be date field", leftFieldDefinition.getName()));
	}
}
