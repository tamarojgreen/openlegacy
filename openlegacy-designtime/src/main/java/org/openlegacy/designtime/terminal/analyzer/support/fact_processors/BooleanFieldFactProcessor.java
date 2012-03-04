package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.support.SimpleBooleanFieldDefinition;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;

public class BooleanFieldFactProcessor implements ScreenFactProcessor {

	private final static Log logger = LogFactory.getLog(BooleanFieldFactProcessor.class);

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof BooleanFieldFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		BooleanFieldFact booleanFieldFact = (BooleanFieldFact)screenFact;

		String regex = booleanFieldFact.getRegex();
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(booleanFieldFact.getTrueFalseTextField().getValue());

		match.find();
		if (match.groupCount() < 2) {
			logger.warn(MessageFormat.format("text is not in the format of: true/false value: {0}",regex));
			return;
		}
		String trueValue = match.group(1);
		String falseValue = match.group(2);
		
		screenEntityDefinition.getSnapshot().getFields().remove(booleanFieldFact.getTrueFalseTextField());
		
		SimpleScreenFieldDefinition screenFieldDefinition = (SimpleScreenFieldDefinition)booleanFieldFact.getScreenFieldDefinition();
		
		screenFieldDefinition.setFieldTypeDefinition(new SimpleBooleanFieldDefinition(trueValue, falseValue));
		screenFieldDefinition.setJavaType(Boolean.class);
		logger.info(MessageFormat.format("Set field {0} to be boolean", screenFieldDefinition.getName()));

	}

}
