package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.analyzer.support.TerminalPositionContainerComparator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;

import java.util.Collections;
import java.util.List;

public class ScreenIdentifiersFactProcessor implements ScreenFactProcessor {

	private int maxIdentifiers;

	public boolean accept(ScreenFact screenFact) {
		return (screenFact instanceof ScreenIdentifiersFact);
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {

		ScreenIdentifiersFact screenIdentifiersFact = (ScreenIdentifiersFact)screenFact;
		ScreenIdentification identification = screenEntityDefinition.getScreenIdentification();

		Collections.sort(screenIdentifiersFact.getIdentifiers(), TerminalPositionContainerComparator.instance());

		List<ScreenIdentifier> screenIdentifiers = identification.getScreenIdentifiers();

		for (TerminalField field : screenIdentifiersFact.getIdentifiers()) {

			ScreenEntityDefinitionsBuilderUtils.addIdentifier(screenEntityDefinition, field);

			// -1 -> one identifier is based on the screen entity field name - added at the end of the identification process
			if (screenIdentifiers.size() >= maxIdentifiers) {
				break;
			}
		}
	}

	public void setMaxIdentifiers(int maxIdentifiers) {
		this.maxIdentifiers = maxIdentifiers;
	}
}
