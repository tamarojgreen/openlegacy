package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.designtime.terminal.analyzer.ScreenEntityDefinitionsBuilder;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactAnalyzer;
import org.openlegacy.designtime.terminal.analyzer.TerminalActionAnalyzer;
import org.openlegacy.designtime.terminal.analyzer.modules.table.TableColumnFact;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalRectangle;
import org.openlegacy.utils.StringUtil;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class DefaultScreenEntityDefinitionsBuilder implements ScreenEntityDefinitionsBuilder {

	private int maxIdentifiers = 3;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityDefinitionsBuilder.class);

	@Inject
	private TerminalActionAnalyzer terminalActionAnalyzer;

	private Map<Class<? extends ScreenFact>, ScreenFactAnalyzer> screenFactAnalyzers;

	@Inject
	private BestEntityNameFieldComparator bestEntityNameFieldComparator;

	public void addIdentifiers(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields) {

		ScreenIdentification identification = screenEntityDefinition.getScreenIdentification();

		Collections.sort(fields, TerminalPositionContainerComparator.instance());

		List<ScreenIdentifier> screenIdentifiers = identification.getScreenIdentifiers();

		for (TerminalField field : fields) {

			addIdentifier(screenEntityDefinition, field);

			// -1 -> one identifier is based on the screen entity field name - added at the end of the identification process
			if (screenIdentifiers.size() >= maxIdentifiers) {
				break;
			}
		}
	}

	private static ScreenIdentifier createIdentifier(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {
		if (ScreenEntityBuilderUtils.isFieldRemovedFromSnapshot(screenEntityDefinition, field)) {
			return null;
		}

		// ignore the identifier if it's outside a defined window border. On border is OK (true param)
		if (!screenEntityDefinition.getSnapshotBorders().contains(field.getPosition(), true)) {
			return null;
		}

		ScreenIdentifier identifier = new SimpleScreenIdentifier(field.getPosition(), field.getValue());
		return identifier;
	}

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
			addIdentifier(screenEntityDefinition, bestMatchEntityField);
		} else {
			logger.error(MessageFormat.format("Ignoring potential screen entity name {0}. Name already present:{1}",
					bestMatchEntityName, existingEntityName));
		}

	}

	private static void addIdentifier(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {
		ScreenIdentifier identitifer = createIdentifier(screenEntityDefinition, field);
		if (identitifer == null) {
			return;
		}
		List<ScreenIdentifier> screenIdentifiers = screenEntityDefinition.getScreenIdentification().getScreenIdentifiers();
		if (screenIdentifiers.contains(identitifer)) {
			return;
		}
		screenIdentifiers.add(identitifer);
		Collections.sort(screenIdentifiers, TerminalPositionContainerComparator.instance());

		logger.info(MessageFormat.format("Added identifier \"{0}\" at position {1} to screen {2}", field.getValue(),
				field.getPosition(), screenEntityDefinition.getEntityName()));

	}

	public void addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field, TerminalField labelField) {
		ScreenEntityBuilderUtils.addField(screenEntityDefinition, field, labelField.getValue());
	}

	public void addTableDefinition(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TableColumnFact> TableColumnFacts) {
		TableBuilder.addTableDefinition(screenEntityDefinition, TableColumnFacts);
	}

	public void setMaxIdentifiers(int maxIdentifiers) {
		this.maxIdentifiers = maxIdentifiers;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, String text, TerminalPosition position,
			String regex) {

		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(text);

		match.find();
		if (match.groupCount() < 2) {
			logger.warn(MessageFormat.format("text is not in the format of: action -> displayName", text,
					screenEntityDefinition.getEntityName()));
			return;
		}

		TerminalActionDefinition actionDefinition = terminalActionAnalyzer.toTerminalActionDefinition(match.group(1),
				match.group(2), position);

		List actions = screenEntityDefinition.getActions();

		actions.add(actionDefinition);

		Collections.sort(actions, TerminalPositionContainerComparator.instance());

		logger.info(MessageFormat.format("Added action {0}:{1} to screen entity {2}",
				actionDefinition.getAction().getClass().getName(), actionDefinition.getDisplayName(),
				screenEntityDefinition.getEntityName()));
	}

	public TableColumnFact addTableColumn(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields) {
		TerminalSnapshot snapshot = screenEntityDefinition.getSnapshot();
		// if the fields were removed from the snapshot, don't create a column
		if (!snapshot.getFields().contains(fields.get(0))) {
			return null;
		}

		TableColumnFact TableColumnFact = new TableColumnFact(screenEntityDefinition, fields);

		logger.info(MessageFormat.format("Recognized column \n{0} to screen entity", TableColumnFact));

		return TableColumnFact;

	}

	public void setSnapshotBorders(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField topBorderField,
			TerminalField buttomBorderField) {
		TerminalPosition buttomLeftPosition = new SimpleTerminalPosition(buttomBorderField.getPosition().getRow(),
				buttomBorderField.getPosition().getColumn() + buttomBorderField.getLength());
		TerminalRectangle borders = new SimpleTerminalRectangle(topBorderField.getPosition(), buttomLeftPosition);
		screenEntityDefinition.setSnapshotBorders(borders);
	}

	public void addColumnHeaders(TableColumnFact TableColumnFact, List<TerminalField> fields) {
		TableColumnFact.getHeaderFields().addAll(fields);
	}

	public void analyzeFact(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ScreenFactAnalyzer factAnalyzer = screenFactAnalyzers.get(screenFact.getClass());
		Assert.notNull(factAnalyzer, "Could not find mapped fact analyzer for fact type" + screenFact.getClass().getName());
		factAnalyzer.analyze(screenEntityDefinition, screenFact);
	}

	public void setScreenFactAnalyzers(Map<Class<? extends ScreenFact>, ScreenFactAnalyzer> screenFactAnalyzers) {
		this.screenFactAnalyzers = screenFactAnalyzers;
	}
}
