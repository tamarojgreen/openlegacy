package org.openlegacy.terminal.support.binders;

import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.utils.ExpressionUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Expression binder implementation for applying expressions to fields of a screenEntity based on expression in
 * {@link ScreenField) annotation settings
 * 
 * @author Ryan Eberly
 */
public class ScreenFieldsExpressionBinder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ExpressionParser expressionParser;

	public void populatedFields(Object screenEntity, ScreenPojoFieldAccessor fieldAccessor, TerminalSnapshot terminalSnapshot,
			Collection<ScreenFieldDefinition> fieldMappingDefinitions) {

		for (ScreenFieldDefinition fieldMappingDefinition : fieldMappingDefinitions) {
			final String expression = fieldMappingDefinition.getExpression();
			if (!StringUtil.isEmpty(expression)) {
				final TerminalField terminalField = terminalSnapshot.getField(fieldMappingDefinition.getPosition());
				if (ExpressionUtils.isRegularExpression(expression)) {
					final Object value = ExpressionUtils.applyRegularExpression(expression.trim(), terminalField.getValue());
					fieldAccessor.setFieldValue(fieldMappingDefinition.getName(), value);
				} else {
					final Expression expr = expressionParser.parseExpression(expression);
					final EvaluationContext evaluationContext = ExpressionUtils.createEvaluationContext(screenEntity,
							terminalField);
					final Object value = expr.getValue(evaluationContext, fieldMappingDefinition.getJavaType());
					fieldAccessor.setFieldValue(fieldMappingDefinition.getName(), value);
				}
			}
		}
	}
}
