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

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class ScreenBinderLogic implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FieldFormatter fieldFormatter;

	@Inject
	private FieldComparator fieldComparator;

	private String descriptionFieldSuffix = "Description";

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
			boolean bind = isBindText(fieldMappingDefinition, text);
			if (bind && fieldAccessor.isWritable(fieldName)) {
				Class<?> javaType = fieldMappingDefinition.getJavaType();
				if (TypesUtil.isNumberOrString(javaType)) {
					String content = fieldFormatter.format(text);
					fieldAccessor.setFieldValue(fieldName, content);
				}
				fieldAccessor.setTerminalField(fieldName, terminalField);
				handleDescriptionField(fieldAccessor, terminalSnapshot, fieldMappingDefinition, fieldName);
			}
			TerminalPosition cursorPosition = terminalSnapshot.getCursorPosition();
			if (cursorPosition != null && cursorPosition.equals(position)) {
				fieldAccessor.setFocusField(fieldMappingDefinition.getName());
			}

		}
	}

	private void handleDescriptionField(ScreenPojoFieldAccessor fieldAccessor, TerminalSnapshot terminalSnapshot,
			ScreenFieldDefinition fieldMappingDefinition, String fieldName) {
		ScreenFieldDefinition descriptionFieldDefinition = fieldMappingDefinition.getDescriptionFieldDefinition();
		if (descriptionFieldDefinition != null) {
			String descriptionText = getText(descriptionFieldDefinition, terminalSnapshot);
			String content = fieldFormatter.format(descriptionText);
			fieldAccessor.setFieldValue(fieldName + descriptionFieldSuffix, content);
		}
	}

	/**
	 * Grab text from the snapshot according to the field definition Considers multy-line field as rectangle/line breakings
	 * 
	 * @param fieldMappingDefinition
	 * @param terminalSnapshot
	 * @return
	 */
	private String getText(ScreenFieldDefinition fieldMappingDefinition, TerminalSnapshot terminalSnapshot) {

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
				String newLine = "\n";
				if (fieldMappingDefinition.isRectangle()) {
					terminalField = terminalSnapshot.getField(currentRow, startColumn);
					if (fieldMappingDefinition.getLength() > 0 && fieldColumnLength != terminalField.getLength()) {
						text = text + newLine + fieldFormatter.format(row.getText(startColumn, fieldColumnLength));
					} else {
						text = text + newLine + fieldFormatter.format(terminalField.getValue());
					}
					// breaking lines
				} else {
					// 1st row = grab until the end of the row
					if (currentRow == startRow) {
						text = text
								+ newLine
								+ fieldFormatter.format(row.getText(startColumn, terminalSnapshot.getSize().getColumns()
										- fieldColumnLength));
						// last row - grab until end column
					} else if (currentRow == endRow) {
						text = text + newLine + fieldFormatter.format(row.getText(1, endColumn));
						// middle row - grab all line
					} else {
						text = text + newLine + fieldFormatter.format(row.getText());
					}
				}
				// single line
			} else {
				terminalField = terminalSnapshot.getField(currentRow, startColumn);
				if (fieldMappingDefinition.getLength() > 0 && fieldColumnLength != terminalField.getLength()) {
					text += row.getText(startColumn, fieldColumnLength);
				} else {
					text += fieldFormatter.format(terminalField.getValue());
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

		List<TerminalField> modifiedfields = sendAction.getFields();

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

			String leftoverValue = initalValue;
			// iterate through the field rows - typically 1 round (endRow = startRow)
			for (int currentRow = startRow; currentRow <= endRow; currentRow++) {

				String value = leftoverValue;
				if (endRow != startRow) {
					if (fieldMappingDefinition.isRectangle()) {
						int delta = endColumn - startColumn;
						if (leftoverValue.length() >= delta) {
							value = leftoverValue.substring(0, delta);
							leftoverValue = leftoverValue.substring(delta);
						} else {
							value = leftoverValue;
							leftoverValue = "";
						}
					} else {
						// 1st row
						if (currentRow == startRow) {
							value = leftoverValue.substring(0, screenColumns - startColumn + 2);
							leftoverValue = leftoverValue.substring(screenColumns - startColumn + 2);
						}
						// last row
						else if (currentRow == endRow) {
							value = leftoverValue;
							currentColumn = 1;
						}
						// middle row
						else {
							value = leftoverValue.substring(0, screenColumns);
							leftoverValue = leftoverValue.substring(screenColumns);
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
					String terminalFieldValue = terminalField.getValue();
					// in case the field is numeric, compare the terminal field old value in as number (in case of leading spaces)
					if (fieldMappingDefinition.getJavaType() == Integer.class && !terminalField.isEmpty()) {
						terminalFieldValue = Integer.valueOf(fieldFormatter.format(terminalFieldValue)).toString();
					}
					boolean fieldModified = fieldComparator.isFieldModified(screenPojo, fieldName, terminalFieldValue, value);
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

	private static boolean isBindText(ScreenFieldDefinition screenFieldDefinition, String text) {
		String when = screenFieldDefinition.getWhenFilter();
		String unless = screenFieldDefinition.getUnlessFilter();
		if (when != null) {
			if (text.matches(when) == false) {
				return false;
			}
		}
		if (unless != null) {
			if (text.matches(unless) == true) {
				return false;
			}
		}
		return true;
	}

	public void setDescriptionFieldSuffix(String descriptionFieldSuffix) {
		this.descriptionFieldSuffix = descriptionFieldSuffix;
	}
}
