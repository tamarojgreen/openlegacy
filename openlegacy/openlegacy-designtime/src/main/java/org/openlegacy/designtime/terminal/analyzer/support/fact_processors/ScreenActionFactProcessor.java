package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.TerminalActionAnalyzer;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class ScreenActionFactProcessor implements ScreenFactProcessor {

	@Inject
	private TerminalActionAnalyzer terminalActionAnalyzer;

	private final static Log logger = LogFactory.getLog(ScreenActionFactProcessor.class);

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof ScreenActionFact;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ScreenActionFact screenActionFact = (ScreenActionFact)screenFact;

		Pattern pattern = Pattern.compile(screenActionFact.getRegex());
		Matcher match = pattern.matcher(screenActionFact.getCaptionAction());

		match.find();
		if (match.groupCount() < 2) {
			logger.warn(MessageFormat.format("text is not in the format of: action -> displayName",
					screenActionFact.getCaptionAction(), screenEntityDefinition.getEntityName()));
			return;
		}

		TerminalActionDefinition actionDefinition = terminalActionAnalyzer.toTerminalActionDefinition(match.group(1),
				match.group(2), screenActionFact.getTerminalPosition());

		List actions = screenEntityDefinition.getActions();

		actions.add(actionDefinition);

		Collections.sort(actions, TerminalPositionContainerComparator.instance());

		logger.info(MessageFormat.format("Added action {0}:{1} to screen entity {2}",
				actionDefinition.getAction().getClass().getName(), actionDefinition.getDisplayName(),
				screenEntityDefinition.getEntityName()));

	}

}
