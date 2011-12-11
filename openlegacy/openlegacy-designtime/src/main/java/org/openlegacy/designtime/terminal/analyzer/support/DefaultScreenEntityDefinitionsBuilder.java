package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.analyzer.ScreenEntityDefinitionsBuilder;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.terminal.model.TableColumn;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalRectangle;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultScreenEntityDefinitionsBuilder implements ScreenEntityDefinitionsBuilder {

	private int maxIdentifiers = 3;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityDefinitionsBuilder.class);

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
		if (isFieldRemovedFromSnapshot(screenEntityDefinition, field)) {
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

		String posibleEntityName = null;
		String bestMatchEntityName = null;
		TerminalField bestMatchEntityField = null;

		for (TerminalField field : possibleFields) {
			if (isFieldRemovedFromSnapshot(screenEntityDefinition, field)) {
				continue;
			}

			posibleEntityName = StringUtil.toClassName(field.getValue());

			if (bestMatchEntityName == null || isMoreMatchedName(posibleEntityName, bestMatchEntityName)) {
				bestMatchEntityName = posibleEntityName;
				bestMatchEntityField = field;
			}
		}

		String existingEntityName = screenEntityDefinition.getEntityName();

		if (existingEntityName == null) {
			screenEntityDefinition.setEntityName(bestMatchEntityName);

			snapshotsAnalyzerContext.addEntityDefinition(screenEntityDefinition);
			logger.info(MessageFormat.format("New screen entity add: {0}", posibleEntityName));

			// add the field which the entity name is based on as one of the identifiers
			addIdentifier(screenEntityDefinition, bestMatchEntityField);
		} else {
			logger.info(MessageFormat.format("Ignoring potential screen entity name {0}. Name already present:{1}",
					posibleEntityName, existingEntityName));
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

	protected boolean isMoreMatchedName(String posibleEntityName, String bestMatchEntityName) {
		return posibleEntityName.length() > bestMatchEntityName.length();
	}

	public void addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field, TerminalField labelField) {

		if (isFieldRemovedFromSnapshot(screenEntityDefinition, field)) {
			return;
		}

		// ignore the field if it's outside a defined window border
		String label = labelField.getValue();
		if (!screenEntityDefinition.getSnapshotBorders().contains(field.getPosition(), false)) {
			logger.info(MessageFormat.format("Field {0} at position {1} is outside window", label, field.getPosition()));
			return;
		}

		String fieldName = StringUtil.toJavaFieldName(label);
		SimpleScreenFieldDefinition fieldMappingDefinition = new SimpleScreenFieldDefinition(fieldName, null);
		fieldMappingDefinition.setPosition(field.getPosition());
		fieldMappingDefinition.setLength(field.getLength());
		fieldMappingDefinition.setEditable(field.isEditable());
		fieldMappingDefinition.setDisplayName(StringUtil.toDisplayName(label));
		fieldMappingDefinition.setSampleValue(StringUtil.toSampleValue(field.getValue()));

		Map<String, ScreenFieldDefinition> fieldsDefinitions = screenEntityDefinition.getFieldsDefinitions();

		fieldName = findFreeFieldName(fieldName, fieldsDefinitions);
		fieldMappingDefinition.setName(fieldName);
		fieldsDefinitions.put(fieldName, fieldMappingDefinition);

		// remove the field from the snapshot
		screenEntityDefinition.getSnapshot().getFields().remove(field);

		String fieldTypeText = field.isEditable() ? "Editable" : "Readonly";
		logger.info(MessageFormat.format("Added {0} field {1} at position {2} to screen entity", fieldTypeText, fieldName,
				field.getPosition()));
	}

	/**
	 * Look from free field name. Relevant when in the following use case: Field A: [XXX] Some description <br/>
	 * - [XXX] - will be fieldA <br/>
	 * - Some description - will be fieldA1 <br/>
	 * 
	 * @param fieldName
	 * @param fieldsDefinitions
	 * @return
	 */
	private static String findFreeFieldName(String fieldName, Map<String, ScreenFieldDefinition> fieldsDefinitions) {
		int fieldNameCount = 1;
		while (fieldsDefinitions.get(fieldName) != null) {
			fieldName = fieldName + fieldNameCount++;
		}
		return fieldName;
	}

	/**
	 * if the field was removed from the snapshot (convert to entity field/column) ignore it drools analyze the fields in advance,
	 * and ignore fields removal done during execution
	 */
	private static boolean isFieldRemovedFromSnapshot(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {
		return !screenEntityDefinition.getSnapshot().getFields().contains(field);
	}

	public void addTableDefinition(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TableColumn> tableColumns) {
		TableBuilder.addTableDefinition(screenEntityDefinition, tableColumns);
	}

	public void setMaxIdentifiers(int maxIdentifiers) {
		this.maxIdentifiers = maxIdentifiers;
	}

	@SuppressWarnings("unchecked")
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

		Class<? extends SessionAction<Session>> actionClass = null;
		try {
			actionClass = (Class<? extends SessionAction<Session>>)Class.forName(MessageFormat.format("{0}{1}{2}",
					TerminalActions.class.getName(), ClassUtils.INNER_CLASS_SEPARATOR, match.group(1)));
		} catch (ClassNotFoundException e) {
			logger.warn(MessageFormat.format("Could not found class for Action {0} in screen {1}", text,
					screenEntityDefinition.getEntityName()));
			return;
		}

		ActionDefinition actionDefinition = new SimpleActionDefinition(actionClass, position, match.group(2));
		List<ActionDefinition> actions = screenEntityDefinition.getActions();

		actions.add(actionDefinition);

		Collections.sort(actions, TerminalPositionContainerComparator.instance());
		logger.info(MessageFormat.format("Added action {0}:{1} to screen entity {2}", actionDefinition.getAction().getName(),
				actionDefinition.getDisplayName(), screenEntityDefinition.getEntityName()));
	}

	public TableColumn addTableColumn(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields) {
		TerminalSnapshot snapshot = screenEntityDefinition.getSnapshot();
		// if the fields were removed from the snapshot, don't create a column
		if (!snapshot.getFields().contains(fields.get(0))) {
			return null;
		}

		TableColumn tableColumn = new TableColumn(screenEntityDefinition, fields);

		logger.info(MessageFormat.format("Recognized column \n{0} to screen entity", tableColumn));

		return tableColumn;

	}

	public void setSnapshotBorders(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField topBorderField,
			TerminalField buttomBorderField) {
		TerminalPosition buttomLeftPosition = new SimpleTerminalPosition(buttomBorderField.getPosition().getRow(),
				buttomBorderField.getPosition().getColumn() + buttomBorderField.getLength());
		TerminalRectangle borders = new SimpleTerminalRectangle(topBorderField.getPosition(), buttomLeftPosition);
		screenEntityDefinition.setSnapshotBorders(borders);
	}
}
