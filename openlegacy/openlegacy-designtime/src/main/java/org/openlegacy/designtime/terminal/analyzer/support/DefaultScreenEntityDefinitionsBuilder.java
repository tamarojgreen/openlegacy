package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldType;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.designtime.terminal.analyzer.ScreenEntityDefinitionsBuilder;
import org.openlegacy.designtime.terminal.analyzer.TerminalActionAnalyzer;
import org.openlegacy.designtime.terminal.analyzer.modules.login.LoginScreenFact;
import org.openlegacy.designtime.terminal.analyzer.modules.menu.MenuItemFact;
import org.openlegacy.designtime.terminal.analyzer.modules.table.TableColumnFact;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalRectangle;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collection;
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
		addField(screenEntityDefinition, field, labelField.getValue());
	}

	private static ScreenFieldDefinition addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field,
			String label) {

		if (isFieldRemovedFromSnapshot(screenEntityDefinition, field)) {
			return null;
		}

		if (!screenEntityDefinition.getSnapshotBorders().contains(field.getPosition(), true)) {
			logger.info(MessageFormat.format("Field {0} at position {1} is outside window", label, field.getPosition()));
			return null;
		}

		Map<String, ScreenFieldDefinition> fieldsDefinitions = screenEntityDefinition.getFieldsDefinitions();
		Collection<ScreenFieldDefinition> fields = fieldsDefinitions.values();
		for (ScreenFieldDefinition screenFieldDefinition : fields) {
			if (screenFieldDefinition.getPosition().equals(field.getPosition())) {
				logger.info(MessageFormat.format(
						"An existing field {0} at position already exists. Field with label {1} will not be added",
						screenFieldDefinition.getName(), label));
				return screenFieldDefinition;
			}
		}

		String fieldName = StringUtil.toJavaFieldName(label);
		SimpleScreenFieldDefinition fieldMappingDefinition = new SimpleScreenFieldDefinition(fieldName, null);
		fieldMappingDefinition.setPosition(field.getPosition());
		fieldMappingDefinition.setLength(field.getLength());
		fieldMappingDefinition.setEditable(field.isEditable());
		fieldMappingDefinition.setDisplayName(StringUtil.toDisplayName(label));
		fieldMappingDefinition.setSampleValue(StringUtil.toSampleValue(field.getValue()));

		fieldName = findFreeFieldName(fieldName, fieldsDefinitions);
		fieldMappingDefinition.setName(fieldName);
		fieldsDefinitions.put(fieldName, fieldMappingDefinition);

		// remove the field from the snapshot
		screenEntityDefinition.getSnapshot().getFields().remove(field);

		String fieldTypeText = field.isEditable() ? "Editable" : "Readonly";
		logger.info(MessageFormat.format("Added {0} field {1} at position {2} to screen entity", fieldTypeText, fieldName,
				field.getPosition()));

		return fieldMappingDefinition;
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

	public void addMenuScreenEntity(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<MenuItemFact> menuItems,
			TerminalField menuSelectionField) {
		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(Menu.MenuEntity.class));
		screenEntityDefinition.setType(Menu.MenuEntity.class);
		TerminalSnapshot snapshot = screenEntityDefinition.getSnapshot();

		ScreenFieldDefinition fieldDefinition = addField(screenEntityDefinition, menuSelectionField, Menu.SELECTION_LABEL);
		if (fieldDefinition == null) {
			logger.warn("Menu selection field not added to screen entity");
			return;
		}

		defineFieldType(screenEntityDefinition, fieldDefinition, Menu.MenuSelectionField.class);

		for (MenuItemFact menuItem : menuItems) {
			snapshot.getFields().remove(menuItem.getCodeField());
			snapshot.getFields().remove(menuItem.getCaptionField());
		}

	}

	private static void defineFieldType(ScreenEntityDesigntimeDefinition screenEntityDefinition,
			ScreenFieldDefinition fieldDefinition, Class<? extends FieldType> clazz) {
		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(clazz));
		((SimpleScreenFieldDefinition)fieldDefinition).setType(clazz);
	}

	public void addLoginScreenEntity(ScreenEntityDesigntimeDefinition screenEntityDefinition, LoginScreenFact loginScreenFact) {

		screenEntityDefinition.setType(Login.LoginEntity.class);

		ScreenFieldDefinition userFieldDefinition = addField(screenEntityDefinition, loginScreenFact.getUserField(),
				loginScreenFact.getUserLabelField().getValue());
		defineFieldType(screenEntityDefinition, userFieldDefinition, Login.UserField.class);

		ScreenFieldDefinition passwordFieldDefinition = addField(screenEntityDefinition, loginScreenFact.getPasswordField(),
				loginScreenFact.getPasswordLabelField().getValue());
		defineFieldType(screenEntityDefinition, passwordFieldDefinition, Login.PasswordField.class);

		ScreenFieldDefinition errorFieldDefinition = addField(screenEntityDefinition, loginScreenFact.getErrorField(),
				Login.ERROR_MESSAGE_LABEL);
		defineFieldType(screenEntityDefinition, errorFieldDefinition, Login.ErrorField.class);
	}
}
