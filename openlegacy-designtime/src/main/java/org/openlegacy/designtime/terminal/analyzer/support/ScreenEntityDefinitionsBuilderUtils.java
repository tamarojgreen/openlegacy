package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldType;
import org.openlegacy.designtime.analyzer.TextTranslator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ScreenEntityDefinitionsBuilderUtils {

	private final static Log logger = LogFactory.getLog(ScreenEntityDefinitionsBuilderUtils.class);
	private static final String FIELD = "field";

	@Inject
	private TextTranslator textTranslator;

	public static void defineFieldType(ScreenEntityDesigntimeDefinition screenEntityDefinition,
			ScreenFieldDefinition fieldDefinition, Class<? extends FieldType> clazz) {
		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(clazz));
		((SimpleScreenFieldDefinition)fieldDefinition).setType(clazz);
	}

	/**
	 * 
	 * @param screenEntityDefinition
	 * @param field
	 * @param verifyExistance
	 *            check if the field exists on the snapshot
	 */
	public void addIdentifier(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field,
			boolean verifyExistance) {
		ScreenIdentifier identitifer = createIdentifier(screenEntityDefinition, field, verifyExistance);
		if (identitifer == null) {
			return;
		}
		List<ScreenIdentifier> screenIdentifiers = screenEntityDefinition.getScreenIdentification().getScreenIdentifiers();
		if (screenIdentifiers.contains(identitifer)) {
			return;
		}
		screenIdentifiers.add(identitifer);
		Collections.sort(screenIdentifiers, TerminalPositionContainerComparator.instance());

		logger.info(MessageFormat.format("Added identifier \"{0}\" at position {1} to screen", field.getValue(),
				field.getPosition()));

	}

	private ScreenIdentifier createIdentifier(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field,
			boolean verifyExistance) {

		if (verifyExistance && isFieldRemovedFromSnapshot(screenEntityDefinition, field)) {
			return null;
		}

		// ignore the identifier if it's outside a defined window border. On border is OK (true param)
		if (!screenEntityDefinition.getSnapshotBorders().contains(field.getPosition(), true)) {
			return null;
		}

		ScreenIdentifier identifier = new SimpleScreenIdentifier(field.getPosition(), field.getValue());
		return identifier;
	}

	/**
	 * if the field was removed from the snapshot (convert to entity field/column) ignore it drools analyze the fields in advance,
	 * and ignore fields removal done during execution
	 */
	public boolean isFieldRemovedFromSnapshot(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {
		return !screenEntityDefinition.getSnapshot().getFields().contains(field);
	}

	public ScreenFieldDefinition addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field,
			TerminalField labelField) {
		SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)addField(screenEntityDefinition, field,
				labelField.getValue());
		if (fieldDefinition != null && labelField != null) {
			fieldDefinition.setLabelPosition(labelField.getPosition());
			fieldDefinition.setTerminalLabelField(labelField);
		}
		return fieldDefinition;
	}

	public ScreenFieldDefinition addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field,
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
		label = textTranslator.translate(label);

		String fieldName = StringUtil.toJavaFieldName(label);
		if (StringUtil.isEmpty(fieldName)) {
			fieldName = FIELD;
		}

		SimpleScreenFieldDefinition screenFieldDefinition = new SimpleScreenFieldDefinition(fieldName, null);
		screenFieldDefinition.setPosition(field.getPosition());
		screenFieldDefinition.setLength(field.getLength());
		screenFieldDefinition.setEditable(field.isEditable());
		String displayName = StringUtil.toDisplayName(label);
		screenFieldDefinition.setDisplayName(displayName);
		screenFieldDefinition.setTerminalField(field);
		screenFieldDefinition.setSampleValue(StringUtil.toSampleValue(field.getValue()));
		screenFieldDefinition.setJavaType(field.getType());

		fieldName = findFreeFieldName(fieldName, fieldsDefinitions);
		screenFieldDefinition.setName(fieldName);
		fieldsDefinitions.put(fieldName, screenFieldDefinition);

		// remove the field from the snapshot
		screenEntityDefinition.getSnapshot().getFields().remove(field);

		String fieldTypeText = field.isEditable() ? "Editable" : "Readonly";
		logger.info(MessageFormat.format("Added {0} field {1} at position {2} to screen entity", fieldTypeText, fieldName,
				field.getPosition()));

		return screenFieldDefinition;
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
		String tempFieldName = fieldName;
		String baseFieldName = fieldName;
		while (fieldsDefinitions.get(tempFieldName) != null) {
			tempFieldName = baseFieldName + fieldNameCount++;
		}
		return tempFieldName;
	}
}
