/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support.binders;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ExpressionUtils;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class ScreenEntityTablesBinder implements ScreenEntityBinder {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private FieldFormatter fieldFormatter;

	private final static Log logger = LogFactory.getLog(ScreenEntityTablesBinder.class);

	@Inject
	private ExpressionParser expressionParser;

	@Override
	public void populateEntity(Object screenEntity, TerminalSnapshot terminalSnapshot) {

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		Map<String, ScreenTableDefinition> tableDefinitions = tablesDefinitionProvider.getTableDefinitions(screenEntity.getClass());

		Set<String> tableFieldNames = tableDefinitions.keySet();

		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("Found {0} tables to bind to entity {1}: {2}", tableFieldNames.size(),
					screenEntity.getClass().getName(), ArrayUtils.toString(tableFieldNames.toArray())));
		}

		for (String tableFieldName : tableFieldNames) {

			ScreenTableDefinition tableDefinition = tableDefinitions.get(tableFieldName);
			List<Object> rows = new ArrayList<Object>();

			List<ScreenColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
			int startRow = tableDefinition.getStartRow();
			int endRow = tableDefinition.getEndRow();
			for (int currentRow = startRow; currentRow <= endRow; currentRow += tableDefinition.getRowGaps()) {

				Object row = ReflectionUtil.newInstance(tableDefinition.getTableClass());
				ScreenPojoFieldAccessor rowAccessor = new SimpleScreenPojoFieldAccessor(row);

				// 3 states boolean - null - no keys found
				Boolean allKeysAreEmpty = null;

				for (ScreenColumnDefinition columnDefinition : columnDefinitions) {
					final TerminalPosition position = SimpleTerminalPosition.newInstance(
							currentRow + columnDefinition.getRowsOffset(), columnDefinition.getStartColumn());
					final TerminalField terminalField = terminalSnapshot.getField(position);
					if (columnDefinition.getAttribute() == FieldAttributeType.Value) {
						if (terminalField != null && terminalField.isHidden()) {
							continue;
						}
						final String cellText = getCellContent(terminalSnapshot, position, columnDefinition);
						if (columnDefinition.isKey()) {
							if (cellText.length() == 0) {
								if (logger.isDebugEnabled()) {
									logger.debug(MessageFormat.format(
											"Key field {0} is empty in row {1}. Aborting table rows collecting",
											columnDefinition.getName(), position.getRow()));
								}
								allKeysAreEmpty = true;
							} else {
								allKeysAreEmpty = false;
							}
						}
						final String expression = columnDefinition.getExpression();
						if (!StringUtil.isEmpty(expression)) {
							if (ExpressionUtils.isRegularExpression(expression)) {
								final Object value = ExpressionUtils.applyRegularExpression(expression.trim(), cellText);
								rowAccessor.setFieldValue(columnDefinition.getName(), value);
							} else {
								final Expression expr = expressionParser.parseExpression(expression);
								final Map<String, Object> expressionVars = new HashMap<String, Object>();
								expressionVars.put("row", row);
								expressionVars.put("field", terminalField);
								expressionVars.put("entity", screenEntity);
								expressionVars.put("cellText", cellText);
								final EvaluationContext evaluationContext = ExpressionUtils.createEvaluationContext(row,
										expressionVars);
								final Object value = expr.getValue(evaluationContext, columnDefinition.getJavaType());
								rowAccessor.setFieldValue(columnDefinition.getName(), value);
							}
						} else {
							rowAccessor.setFieldValue(columnDefinition.getName(), cellText);
						}
						rowAccessor.setTerminalField(columnDefinition.getName(), terminalField);
					} else {
						if (terminalField == null) {
							logger.warn(MessageFormat.format("Unable to find field in position {0} for table:{1}", position,
									tableDefinition.getTableEntityName()));
							break;
						}

						if (columnDefinition.getAttribute() == FieldAttributeType.Editable) {
							rowAccessor.setFieldValue(columnDefinition.getName(), terminalField.isEditable());
						} else if (columnDefinition.getAttribute() == FieldAttributeType.Color) {
							rowAccessor.setFieldValue(columnDefinition.getName(), terminalField.getColor());
						}
					}
				}
				boolean filter = false;
				if (allKeysAreEmpty == null || allKeysAreEmpty == false) {
					if (tableDefinition.getStopExpression() != null) {
						final Map<String, Object> expressionVars = new HashMap<String, Object>();
						expressionVars.put("row", row);
						expressionVars.put("text", terminalSnapshot.getRow(currentRow).getText());
						final EvaluationContext evaluationContext = ExpressionUtils.createEvaluationContext(row, expressionVars);
						Expression expr = expressionParser.parseExpression(tableDefinition.getStopExpression());
						Boolean stop = expr.getValue(evaluationContext, Boolean.class);
						if (stop) {
							break;
						}
					}
					if (tableDefinition.getFilterExpression() != null) {
						StandardEvaluationContext evaluationContext = new StandardEvaluationContext(row);
						Expression expr = expressionParser.parseExpression(tableDefinition.getFilterExpression());
						filter = expr.getValue(evaluationContext, Boolean.class);
					}
					if (!filter) {
						rows.add(row);
					}
				}
			}
			fieldAccessor.setFieldValue(tableFieldName, rows);
		}
	}

	private String getCellContent(TerminalSnapshot terminalSnapshot, TerminalPosition position,
			ScreenColumnDefinition columnDefinition) {
		int length = columnDefinition.getEndColumn() - columnDefinition.getStartColumn() + 1;
		String columnText = terminalSnapshot.getLogicalText(position, length);
		columnText = fieldFormatter.format(columnText);
		return columnText;
	}

	public void setFieldFormatter(FieldFormatter fieldFormatter) {
		this.fieldFormatter = fieldFormatter;
	}

	@Override
	public void populateAction(TerminalSendAction sendAction, TerminalSnapshot terminalScreen, Object entity) {
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);

		Map<String, ScreenTableDefinition> tableDefinitions = tablesDefinitionProvider.getTableDefinitions(entity.getClass());

		Set<String> tableFieldNames = tableDefinitions.keySet();

		for (String tableFieldName : tableFieldNames) {
			ScreenTableDefinition tableDefinition = tableDefinitions.get(tableFieldName);
			List<?> rows = (List<?>)fieldAccessor.getFieldValue(tableFieldName);
			// TODO send tables values
			if (rows == null) {
				continue;
			}
			int rowCount = 0;
			for (Object row : rows) {
				List<ScreenColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
				ScreenPojoFieldAccessor rowAccessor = new SimpleScreenPojoFieldAccessor(row);
				for (ScreenColumnDefinition columnDefinition : columnDefinitions) {
					if (columnDefinition.isEditable() && columnDefinition.getAttribute() == FieldAttributeType.Value
							&& StringUtil.isEmpty(columnDefinition.getExpression())) {
						Object value = rowAccessor.getFieldValue(columnDefinition.getName());
						if (value == null) {
							continue;
						}
						String valueString = value.toString();
						if (valueString != null) {
							int screenRow = tableDefinition.getStartRow() + (rowCount * tableDefinition.getRowGaps());
							TerminalField terminalField = terminalScreen.getField(SimpleTerminalPosition.newInstance(screenRow,
									columnDefinition.getStartColumn()));
							if (terminalField != null && terminalField.isEditable()) {
								if (!terminalField.getValue().equals(valueString)) {
									terminalField.setValue(valueString);
									sendAction.setCursorPosition(terminalField.getPosition());
									sendAction.getFields().add(terminalField);
								}
							}
						}
					}

				}
				rowCount++;
			}

		}

	}
}
