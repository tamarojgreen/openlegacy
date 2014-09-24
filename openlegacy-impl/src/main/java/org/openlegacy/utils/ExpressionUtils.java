package org.openlegacy.utils;

import org.openlegacy.support.expressions.EnumExpressionTypeComparator;
import org.springframework.expression.TypeComparator;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to encapsulate common code related to the use of Spring Expressions.
 * 
 * @author ryan eberly
 */
public class ExpressionUtils {

	/**
	 * Create the Spring Expression evaluation context.
	 * 
	 * @param entity
	 *            - Root context for expression evaluation.
	 * @param field
	 *            - object to expose via the #field variable.
	 * @return
	 */
	public static StandardEvaluationContext createEvaluationContext(final Object entity, final Object field) {
		final StandardEvaluationContext evaluationContext = new StandardEvaluationContext(entity);
		final TypeComparator typeComparator = new EnumExpressionTypeComparator();
		evaluationContext.setTypeComparator(typeComparator);
		if (field != null) {
			evaluationContext.setVariable("field", field);
		}
		return evaluationContext;
	}

	/**
	 * Create the Spring Expression evaluation context.
	 * 
	 * @param entity
	 *            - Root context for expression evaluation.
	 * @param variables
	 *            - Map containing variables to make accessible within the expression
	 * @return
	 */
	public static StandardEvaluationContext createEvaluationContext(final Object entity, final Map<String, Object> variables) {
		final StandardEvaluationContext evaluationContext = new StandardEvaluationContext(entity);
		final TypeComparator typeComparator = new EnumExpressionTypeComparator();
		evaluationContext.setTypeComparator(typeComparator);
		if (variables != null) {
			evaluationContext.setVariables(variables);
		}
		return evaluationContext;
	}

	/**
	 * Utility function that checks whether the String conforms to the pattern "/<expression>/expression/"
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean isRegularExpression(String expression) {
		return expression != null && expression.trim().startsWith("/") && expression.trim().endsWith("/");
	}

	/**
	 * Applies a regular expression string using {@link java.util.regex.Pattern} and {@link java.lang.String#replaceAll()}
	 * 
	 * @param expression
	 * @param value
	 * @return value string transformed by expression
	 */

	final static Pattern REGEX_PATTERN = Pattern.compile("\\/(.*[^\\/])\\/(.*)\\/");

	public static String applyRegularExpression(String expression, Object value) {
		Matcher matcher = REGEX_PATTERN.matcher(expression.trim());
		if (value == null) {
			return null;
		}
		if (!matcher.matches()) {
			throw new RuntimeException(String.format("Expression [%s] must have the form [/<expr>/<replace>/]", expression));
		} else {
			return value.toString().replaceAll(matcher.group(1), matcher.group(2));
		}
	}
}
