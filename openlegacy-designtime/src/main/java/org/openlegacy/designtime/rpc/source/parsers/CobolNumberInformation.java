package org.openlegacy.designtime.rpc.source.parsers;

/**
 * Fetch FieldInformation from Cobol numeric variable declaration.
 * 
 */

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleRpcNumericFieldTypeDefinition;

import java.math.BigInteger;

public class CobolNumberInformation implements FieldInformation {

	private static final char DIGIT_SYMBOL = '9';
	private static final char DOT_SYMBOL = 'V';
	private static final char SIGHN_SYMBOL = 'S';
	private static final char EXP_SYMBOL = 'E';
	private static final char SCAL_SYMBOL = 'P';

	private boolean signed = false;
	private int scale = 0;
	private int digitBeforeDot = 0;
	private int digitAfterDot = 0;
	private int exponentDigits = 0;

	CobolNumberInformation(String flatPicture) {
		int idx = 0;
		int lastCharIdx = flatPicture.length() - 1;
		if (flatPicture.charAt(0) == SIGHN_SYMBOL) {
			signed = true;
			idx++;
		}
		for (; flatPicture.charAt(idx) == SCAL_SYMBOL; idx++) {
			scale++;
		}

		for (; idx <= lastCharIdx && flatPicture.charAt(idx) == DIGIT_SYMBOL; idx++) {
			digitBeforeDot++;
		}

		if (idx < lastCharIdx && flatPicture.charAt(idx) == DOT_SYMBOL) {
			idx++;
			for (; idx <= lastCharIdx && flatPicture.charAt(idx) == DIGIT_SYMBOL; idx++) {
				digitAfterDot++;
			}
		}

		if (idx < lastCharIdx && flatPicture.charAt(idx) == EXP_SYMBOL) {
			idx++;
			for (; idx <= lastCharIdx && flatPicture.charAt(idx) == DIGIT_SYMBOL; idx++) {
				exponentDigits++;
			}
		}
		if (idx < lastCharIdx) {
			for (; idx <= lastCharIdx && flatPicture.charAt(idx) == SCAL_SYMBOL; idx++) {
				scale--;
			}
		}

	}

	public boolean isSigned() {
		return signed;
	}

	public int getScale() {
		return scale;
	}

	public int getDigitBeforeDot() {
		return digitBeforeDot;
	}

	public int getDigitAfterDot() {
		return digitAfterDot;
	}

	public int getExponentDigits() {
		return exponentDigits;
	}

	public int getLength() {

		return digitBeforeDot;
	}

	public Class<?> getJavaType() {
		if (digitAfterDot > 0) {
			return Double.class;
		}
		if (digitBeforeDot <= 9) {
			return Integer.class;
		} else {
			return BigInteger.class;
		}
	}

	public FieldTypeDefinition getType() {
		double maxVal = Math.pow(10, scale + digitBeforeDot) - 1;
		if (digitAfterDot > 0) {
			maxVal += (1 - Math.pow(10, scale - digitAfterDot));
		}
		return new SimpleRpcNumericFieldTypeDefinition(-maxVal, maxVal, digitAfterDot);
	}
}
