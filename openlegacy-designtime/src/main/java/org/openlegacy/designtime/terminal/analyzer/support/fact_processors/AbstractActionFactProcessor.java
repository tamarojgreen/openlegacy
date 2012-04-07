package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.TerminalActionAnalyzer;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public abstract class AbstractActionFactProcessor implements ScreenFactProcessor {

	@Inject
	private TerminalActionAnalyzer terminalActionAnalyzer;

	private final static Log logger = LogFactory.getLog(AbstractActionFactProcessor.class);

	public boolean accept(ScreenFact screenFact) {
		return screenFact.getClass() == ScreenActionFact.class;
	}

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

		TerminalActionDefinition actionDefinition = buildActionDefinition(match.group(1), match.group(2),
				screenActionFact.getTerminalPosition());

		addAction(screenEntityDefinition, screenActionFact, actionDefinition);

	}

	protected abstract TerminalActionDefinition buildActionDefinition(String action, String caption,
			TerminalPosition terminalPosition);

	protected abstract void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenActionFact screenActionFact,
			TerminalActionDefinition actionDefinition);

	protected TerminalActionAnalyzer getTerminalActionAnalyzer() {
		return terminalActionAnalyzer;
	}
}
