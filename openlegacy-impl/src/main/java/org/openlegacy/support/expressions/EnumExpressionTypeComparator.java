package org.openlegacy.support.expressions;

import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.support.StandardTypeComparator;

/*
 * Type comparator for Spring expressions that supports equality of enums and Strings
 * All other comparisons are deferred to StandardTypeComparator
 */
public class EnumExpressionTypeComparator extends StandardTypeComparator {

	@Override
	public int compare(Object left, Object right) throws SpelEvaluationException {
		if (isEnumAndString(left, right)) {
			return super.compare(left.toString(), right.toString());
		}
		return super.compare(left, right);
	}

	@Override
	public boolean canCompare(Object left, Object right) {
		if (isEnumAndString(left, right)) {
			return true;
		}
		return super.canCompare(left, right);
	}

	/**
	 * @param left
	 * @param right
	 * @return true if one object is enum and the other is string
	 */
	private static boolean isEnumAndString(Object left, Object right) {
		if (left == null || right == null) {
			return false;
		} else if (left.getClass().isEnum() || right.getClass().equals(String.class)) {
			return true;
		} else if (right.getClass().isEnum() || left.getClass().equals(String.class)) {
			return true;
		}
		return false;
	}

}
