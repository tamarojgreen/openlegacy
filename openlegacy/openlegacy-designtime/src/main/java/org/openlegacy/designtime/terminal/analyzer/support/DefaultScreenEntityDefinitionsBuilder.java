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
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldMappingDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultScreenEntityDefinitionsBuilder implements ScreenEntityDefinitionsBuilder {

	private int maxIdentifiers = 3;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityDefinitionsBuilder.class);

	public void addIdentifier(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {

		ScreenIdentification identification = screenEntityDefinition.getScreenIdentification();

		// if the field was removed from the snapshot (convert to entity field/column) ignore it
		// drools analyze the fields in advance, and ignore fields removal done during execution
		if (!screenEntityDefinition.getSnapshot().getFields().contains(field)) {
			return;
		}

		List<ScreenIdentifier> screenIdentifiers = identification.getScreenIdentifiers();
		if (screenIdentifiers.size() >= maxIdentifiers) {
			return;
		}
		ScreenIdentifier identifier = new SimpleScreenIdentifier(field.getPosition(), field.getValue());
		screenIdentifiers.add(identifier);

		Collections.sort(screenIdentifiers, ScreenPositionContainerComparator.instance());

		logger.info(MessageFormat.format("Added identifier \"{0}\" at position {1} to screen {2}", field.getValue(),
				field.getPosition(), screenEntityDefinition.getEntityName()));
	}

	public void setScreenEntityName(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {

		if (screenEntityDefinition.getEntityName() == null) {
			String entityName = StringUtil.toClassName(field.getValue());
			screenEntityDefinition.setEntityName(entityName);
			snapshotsAnalyzerContext.addEntityDefinition(screenEntityDefinition);
			logger.info(MessageFormat.format("New screen entity add: {0}", entityName));
		}

	}

	public void addField(ScreenEntityDefinition screenEntityDefinition, TerminalField field, String leadingLabel) {

		String fieldName = StringUtil.toJavaFieldName(leadingLabel);
		SimpleFieldMappingDefinition fieldMappingDefinition = new SimpleFieldMappingDefinition(fieldName, null);
		fieldMappingDefinition.setPosition(field.getPosition());
		fieldMappingDefinition.setLength(field.getLength());
		fieldMappingDefinition.setEditable(field.isEditable());
		fieldMappingDefinition.setDisplayName(StringUtil.toDisplayName(leadingLabel));
		fieldMappingDefinition.setSampleValue("");

		screenEntityDefinition.getFieldsDefinitions().put(fieldName, fieldMappingDefinition);

		// remove the field from the snapshot
		screenEntityDefinition.getSnapshot().getFields().remove(field);

		String fieldTypeText = field.isEditable() ? "Editable" : "Readonly";
		logger.info(MessageFormat.format("Added {0} field {1} to screen entity {2}", fieldTypeText, fieldName,
				screenEntityDefinition.getEntityName()));
	}

	public void addTableDefinition(ScreenEntityDefinition screenEntityDefinition, List<TableColumn> tableColumns) {
		TableBuilder.addTableDefinition(screenEntityDefinition, tableColumns);
	}

	public void setMaxIdentifiers(int maxIdentifiers) {
		this.maxIdentifiers = maxIdentifiers;
	}

	@SuppressWarnings("unchecked")
	public void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, String text, ScreenPosition position,
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

		Collections.sort(actions, ScreenPositionContainerComparator.instance());
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

		logger.info(MessageFormat.format("Recognized column \n{0} in screen entity {1}", tableColumn,
				screenEntityDefinition.getEntityName()));

		return tableColumn;

	}

}
