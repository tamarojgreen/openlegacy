/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support.binders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.FieldComparator;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.exceptions.TerminalActionException;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.TypesUtil;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

@Component
public class ScreenBinderLogic implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FieldFormatter fieldFormatter;

	@Inject
	private FieldComparator fieldComparator;

	private final static Log logger = LogFactory.getLog(ScreenBinderLogic.class);

	public void populatedFields(ScreenPojoFieldAccessor fieldAccessor, TerminalSnapshot terminalSnapshot,
			Collection<ScreenFieldDefinition> fieldMappingDefinitions) {

		for (ScreenFieldDefinition fieldMappingDefinition : fieldMappingDefinitions) {

			TerminalPosition position = fieldMappingDefinition.getPosition();
			TerminalField terminalField = terminalSnapshot.getField(position);
			if (terminalField == null) {
				continue;
			}

			String text = getText(fieldMappingDefinition, terminalSnapshot);

			String fieldName = fieldMappingDefinition.getName();
			if (fieldAccessor.isWritable(fieldName)) {
				Class<?> javaType = fieldMappingDefinition.getJavaType();
				if (TypesUtil.isNumberOrString(javaType)) {
					String content = fieldFormatter.format(text);
					fieldAccessor.setFieldValue(fieldName, content);
				}

				fieldAccessor.setTerminalField(fieldName, terminalField);
			}
			TerminalPosition cursorPosition = terminalSnapshot.getCursorPosition();
			if (cursorPosition != null && cursorPosition.equals(position)) {
				fieldAccessor.setFocusField(fieldMappingDefinition.getName());
			}

		}
	}

	/**
	 * Grab text from the snapshot according to the field definition Considers multy-line field as rectangle/line breakings
	 * 
	 * @param fieldMappingDefinition
	 * @param terminalSnapshot
	 * @return
	 */
	private static String getText(ScreenFieldDefinition fieldMappingDefinition, TerminalSnapshot terminalSnapshot) {

		int startRow = fieldMappingDefinition.getPosition().getRow();
		int endRow = fieldMappingDefinition.getEndPosition().getRow();
		int startColumn = fieldMappingDefinition.getPosition().getColumn();
		int endColumn = fieldMappingDefinition.getEndPosition().getColumn();
		int fieldColumnLength = (endColumn - startColumn) + 1;

		if (endRow == 0) {
			endRow = startRow;
		}
		String text = "";
		for (int currentRow = startRow; currentRow <= endRow; currentRow++) {
			TerminalRow row = terminalSnapshot.getRow(currentRow);
			TerminalField terminalField = null;

			// multy line
			if (currentRow > startRow) {
				if (fieldMappingDefinition.isRectangle()) {
					terminalField = terminalSnapshot.getField(currentRow, startColumn);
					if (fieldMappingDefinition.getLength() > 0 && fieldColumnLength != terminalField.getLength()) {
						text += row.getText(startColumn, fieldColumnLength);
					} else {
						text += terminalField.getValue();
					}
					// breaking lines
				} else {
					// 1st row = grab until the end of the row
					if (currentRow == startRow) {
						text += row.getText(startColumn, terminalSnapshot.getSize().getColumns() - fieldColumnLength);
						// last row - grab until end column
					} else if (currentRow == endRow) {
						text += row.getText(1, endColumn);
						// middle row - grab all line
					} else {
						text += row.getText();
					}
				}
				// single line
			} else {
				terminalField = terminalSnapshot.getField(currentRow, startColumn);
				if (fieldMappingDefinition.getLength() > 0 && fieldColumnLength != terminalField.getLength()) {
					text += row.getText(startColumn, fieldColumnLength);
				} else {
					text += terminalField.getValue();
				}

			}
		}

		return text;
	}

	/**
	 * Convert a pojo to a sendAction modified fields. Main complexity is around breaking multy line fields to single terminal
	 * fields
	 * 
	 * @param sendAction
	 * @param terminalSnapshot
	 * @param screenPojo
	 * @param fieldMappingsDefinitions
	 */
	public void populateSendAction(TerminalSendAction sendAction, TerminalSnapshot terminalSnapshot, Object screenPojo,
			Collection<ScreenFieldDefinition> fieldMappingsDefinitions) {
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenPojo);

		List<TerminalField> modifiedfields = sendAction.getModifiedFields();

		for (ScreenFieldDefinition fieldMappingDefinition : fieldMappingsDefinitions) {

			TerminalPosition fieldPosition = fieldMappingDefinition.getPosition();
			String fieldName = fieldMappingDefinition.getName();

			if (!fieldAccessor.isExists(fieldName)) {
				continue;
			}

			if (screenPojo instanceof ScreenEntity) {
				ScreenEntity screenEntity = (ScreenEntity)screenPojo;
				if (fieldName.equalsIgnoreCase(screenEntity.getFocusField())) {
					sendAction.setCursorPosition(fieldPosition);
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Cursor was set at position {0} from field {1}", fieldPosition,
								screenEntity.getFocusField()));
					}
				}
			}

			if (!fieldMappingDefinition.isEditable()) {
				continue;
			}

			Object fieldValue = fieldAccessor.getFieldValue(fieldName);

			// null - skip field assignment
			if (fieldValue == null) {
				continue;
			}

			// don't handle none string or number
			if (!TypesUtil.isNumberOrString(fieldValue.getClass())) {
				continue;
			}

			String initalValue = String.valueOf(fieldValue);

			// short-cuts..
			int startRow = fieldMappingDefinition.getPosition().getRow();
			int endRow = fieldMappingDefinition.getEndPosition().getRow();
			if (endRow == 0) {
				endRow = startRow;
			}
			int startColumn = fieldMappingDefinition.getPosition().getColumn();
			int endColumn = fieldMappingDefinition.getEndPosition().getColumn();

			int currentColumn = startColumn;

			int screenColumns = terminalSnapshot.getSize().getColumns();

			String leftValue = initalValue;
			// iterate through the field rows - typically 1 round (endRow = startRow)
			for (int currentRow = startRow; currentRow <= endRow; currentRow++) {

				String value = leftValue;
				if (endRow != startRow) {
					if (fieldMappingDefinition.isRectangle()) {
						value = leftValue.substring(0, endColumn - startColumn);
						leftValue = leftValue.substring(endColumn - startColumn);
					} else {
						// 1st row
						if (currentRow == startRow) {
							value = leftValue.substring(0, screenColumns - startColumn + 2);
							leftValue = leftValue.substring(screenColumns - startColumn + 2);
						}
						// last row
						else if (currentRow == endRow) {
							value = leftValue;
							currentColumn = 1;
						}
						// middle row
						else {
							value = leftValue.substring(0, screenColumns);
							leftValue = leftValue.substring(screenColumns);
							currentColumn = 1;
						}
					}
				}

				// find the terminal field in this position
				TerminalField terminalField = terminalSnapshot.getField(currentRow, currentColumn);

				if (terminalField == null) {
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format(
								"Unable to find terminal field in position {0},{1} which matchees field {2} in {3}", currentRow,
								currentColumn, fieldName, ProxyUtil.getObjectRealClass(screenPojo)));
					}
					continue;
				}

				if (terminalField.isEditable() && value != null) {
					boolean fieldModified = fieldComparator.isFieldModified(screenPojo, fieldName, terminalField.getValue(),
							value);
					if (fieldModified) {
						if (fieldMappingDefinition.isEditable()) {
							terminalField.setValue(value);
							modifiedfields.add(terminalField);

							if (logger.isDebugEnabled()) {
								logger.debug(MessageFormat.format(
										"Field {0} was set with value \"{1}\" to send fields for screen {2}", fieldName, value,
										screenPojo.getClass()));
							}
						} else {
							throw (new TerminalActionException(MessageFormat.format(
									"Field {0} in screen {1} was modified with value {2}, but is not defined as editable",
									fieldName, screenPojo, value)));

						}
					}
				}
			}

		}
	}
}
