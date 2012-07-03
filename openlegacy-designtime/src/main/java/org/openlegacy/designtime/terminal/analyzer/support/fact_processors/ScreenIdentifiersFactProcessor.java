package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ScreenIdentifiersFactProcessor implements ScreenFactProcessor {

	private int maxIdentifiers;

	@Inject
	private ScreenEntityDefinitionsBuilderUtils screenEntityDefinitionsBuilderUtils;

	public boolean accept(ScreenFact screenFact) {
		return (screenFact instanceof ScreenIdentifiersFact);
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {

		ScreenIdentifiersFact screenIdentifiersFact = (ScreenIdentifiersFact)screenFact;
		ScreenIdentification identification = screenEntityDefinition.getScreenIdentification();

		Collections.sort(screenIdentifiersFact.getIdentifiers(), TerminalPositionContainerComparator.instance());

		List<ScreenIdentifier> screenIdentifiers = identification.getScreenIdentifiers();

		for (TerminalField field : screenIdentifiersFact.getIdentifiers()) {

			screenEntityDefinitionsBuilderUtils.addIdentifier(screenEntityDefinition, field, true);

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
