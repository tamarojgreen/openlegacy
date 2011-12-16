package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldType;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

public class ScreenEntityDefinitionsBuilderUtils {

	private final static Log logger = LogFactory.getLog(ScreenEntityDefinitionsBuilderUtils.class);

	public static void defineFieldType(ScreenEntityDesigntimeDefinition screenEntityDefinition,
			ScreenFieldDefinition fieldDefinition, Class<? extends FieldType> clazz) {
		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(clazz));
		((SimpleScreenFieldDefinition)fieldDefinition).setType(clazz);
	}

	/**
	 * if the field was removed from the snapshot (convert to entity field/column) ignore it drools analyze the fields in advance,
	 * and ignore fields removal done during execution
	 */
	public static boolean isFieldRemovedFromSnapshot(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {
		return !screenEntityDefinition.getSnapshot().getFields().contains(field);
	}

	public static ScreenFieldDefinition addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field,
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
}
