package org.openlegacy.designtime.rpc.source.parsers;

/**
 * Fetch FieldInformation from Cobol variable declaration.
 * 
 */

public class CobolFieldInformationFactory implements FieldInformationFactory {

	private static final char SIGN_SYMBOL = 'S';
	private static final char DIGIT_SYMBOL = '9';
	private static final char SCALE_SYMBOL = 'P';

	private enum CobolFieldTypes {
		SIMPLE,
		NUMERIC,
		HIERARCHY;
	}

	private static boolean isNumber(char firstChar) {

		if (firstChar == SIGN_SYMBOL || firstChar == DIGIT_SYMBOL || firstChar == SCALE_SYMBOL) {
			return true;
		}
		return false;
	}

	private static CobolFieldTypes getCobolFieldTypes(String flatPicture) {
		if (flatPicture == null) {
			return CobolFieldTypes.HIERARCHY;
		}

		if (isNumber(flatPicture.charAt(0))) {
			return CobolFieldTypes.NUMERIC;
		}

		return CobolFieldTypes.SIMPLE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.FieldInformation#getObject(java.lang.String)
	 */
	public FieldInformation getObject(String flatPicture, int occurs) {
		CobolFieldTypes cobolFieldType = getCobolFieldTypes(flatPicture);
		if (occurs > 1) {
			return new CobolListInformation(getObject(flatPicture, 1), occurs);
		}

		if (CobolFieldTypes.NUMERIC == cobolFieldType) {
			return new CobolNumberInformation(flatPicture);
		}
		if (CobolFieldTypes.SIMPLE == cobolFieldType) {
			return new CobolTextInformation(flatPicture);
		}
		return null;

	}
}
