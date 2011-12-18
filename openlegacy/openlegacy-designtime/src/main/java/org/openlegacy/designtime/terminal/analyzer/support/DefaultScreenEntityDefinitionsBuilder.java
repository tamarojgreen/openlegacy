package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.designtime.terminal.analyzer.ScreenEntityDefinitionsBuilder;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultScreenEntityDefinitionsBuilder implements ScreenEntityDefinitionsBuilder {

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityDefinitionsBuilder.class);

	private List<ScreenFactProcessor> screenFactProcessors;

	@Inject
	private BestEntityNameFieldComparator bestEntityNameFieldComparator;

	public void selectPotentialScreenEntityName(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> possibleFields) {

		String bestMatchEntityName = null;

		Collections.sort(possibleFields, bestEntityNameFieldComparator);

		TerminalField bestMatchEntityField = possibleFields.get(0);
		bestMatchEntityName = StringUtil.toClassName(bestMatchEntityField.getValue());

		String existingEntityName = screenEntityDefinition.getEntityName();

		if (existingEntityName == null) {
			snapshotsAnalyzerContext.addEntityDefinition(bestMatchEntityName, screenEntityDefinition);
			logger.info(MessageFormat.format("New potential screen entity add: {0}", bestMatchEntityName));

			// add the field which the entity name is based on as one of the identifiers
			ScreenEntityDefinitionsBuilderUtils.addIdentifier(screenEntityDefinition, bestMatchEntityField);
		} else {
			logger.error(MessageFormat.format("Ignoring potential screen entity name {0}. Name already present:{1}",
					bestMatchEntityName, existingEntityName));
		}

	}

	public void addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field, TerminalField labelField) {
		ScreenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition, field, labelField.getValue());
	}

	public void processFact(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		for (ScreenFactProcessor screenFactProcessor : screenFactProcessors) {
			if (screenFactProcessor.accept(screenFact)) {
				screenFactProcessor.process(screenEntityDefinition, screenFact);
			}
		}
	}

	public void setScreenFactProcessors(List<ScreenFactProcessor> screenFactProcessors) {
		this.screenFactProcessors = screenFactProcessors;
	}
}
