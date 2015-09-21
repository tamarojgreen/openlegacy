package org.openlegacy.mfutils;

public class OLBasicConvert {

	private int binId = ICopybookDialects.FMT_MAINFRAME;
	private boolean usePositiveInteger;

	public String getTypeName(String usage, String picture, boolean signed, boolean signSeperate, String signPosition) {
		String result = "";

		switch (getTypeIdentifier(usage, picture, signed, signSeperate, signPosition)) {
			case Type.ftChar:
				result = "Char";
				break;
			case Type.ftNumRightJustified:
				result = "Num (Right Justified space padded)";

				break;
			case Type.ftPositiveNumAnyDecimal:
				result = "Postive Decimal";
				break;
			case Type.ftAssumedDecimalPositive:
				result = "Num Assumed Decimal (+ve)";
				break;
			case Type.ftNumZeroPaddedPositive:
				result = "Positive Zero Padded Number";
				break;

			case Type.ftPackedDecimal:
				result = "Mainframe Packed Decimal (comp-3)";
				break;

			case Type.ftZonedNumeric:
				result = "Signed Decimal";
				break;
			case Type.ftBinaryBigEndian:
				result = "Binary Integer";
				break;
			case Type.ftBinaryBigEndianPositive:
				result = "Binary Integer (only +ve)";
				break;

		}
		return result;
	}

	private int getTypeIdentifier(String usage, String picture, boolean signed, boolean signSeperate, String signPosition) {
		int lType = -121;

		picture = picture.toUpperCase();
		boolean positive = !(signed || picture.startsWith("S"));

		if (picture.startsWith("-9(") || picture.startsWith("+++9") || picture.startsWith("+(2)9")) {
			lType = -121;
		}
		if (Cb2xmlConstants.COMP.equalsIgnoreCase(usage) || Cb2xmlConstants.COMP_4.equalsIgnoreCase(usage)
				|| Cb2xmlConstants.COMP_5.equalsIgnoreCase(usage) || Cb2xmlConstants.COMP_6.equalsIgnoreCase(usage)
				|| Cb2xmlConstants.BINARY.equalsIgnoreCase(usage)) {
			if (binId == ICopybookDialects.FMT_MAINFRAME || binId == ICopybookDialects.FMT_FUJITSU
					|| binId == ICopybookDialects.FMT_BIG_ENDIAN) {
				lType = Type.ftBinaryBigEndian;
				if (positive) {
					lType = Type.ftBinaryBigEndianPositive;
					if (usePositiveInteger) {
						lType = Type.ftPositiveBinaryBigEndian;
					}
				}
			} else {
				lType = Type.ftBinaryInt;
				if (positive) {
					lType = Type.ftBinaryIntPositive;
					if (usePositiveInteger) {
						lType = Type.ftPostiveBinaryInt;
					}
				}
			}
		} else if (Cb2xmlConstants.COMP_3.equalsIgnoreCase(usage) || Cb2xmlConstants.PACKED_DECIMAL.equalsIgnoreCase(usage)) {
			lType = Type.ftPackedDecimal;
			if (positive) {
				lType = Type.ftPackedDecimalPostive;
			}
		} else if (Cb2xmlConstants.COMP_1.equalsIgnoreCase(usage)) {
			lType = Type.ftFloat;
		} else if (Cb2xmlConstants.COMP_2.equalsIgnoreCase(usage)) {
			lType = Type.ftDouble;
		} else if (!CommonCode.checkPictureNumeric(picture, '.')) {
			return Type.ftChar;
		} else if (picture.indexOf('9') >= 0
				// && picture.indexOf('V') < 0
				// && picture.indexOf('Z') < 0
				// && picture.indexOf(',') < 0
				// && (! picture.startsWith("S"))
				&& (picture.startsWith("-") || picture.startsWith("+") || picture.startsWith("9"))
				&& CommonCode.checkPicture(picture, '9', '.')) {
			if (picture.startsWith("-")) {
				lType = Type.ftNumZeroPadded;
			} else if (picture.startsWith("9")) {
				lType = Type.ftNumZeroPaddedPositive;
			} else if (picture.startsWith("+")) {
				lType = Type.ftNumZeroPaddedPN;
			} else {
				lType = chkRest(lType, usage, picture, signed, signSeperate, signPosition);
			}
		} else {
			lType = chkRest(lType, usage, picture, signed, signSeperate, signPosition);
		}
		return lType;
	}

	private int chkRest(int lType, String usage, String picture, boolean signed, boolean signSeperate, String signPosition) {
		if (picture.startsWith("+") && CommonCode.checkPicture(picture, '+', '.')) {
			lType = Type.ftNumRightJustifiedPN;
		} else if (picture.indexOf('Z') >= 0 || picture.indexOf('-') >= 0 || picture.indexOf('+') >= 0
				|| picture.indexOf('.') >= 0) {
			lType = Type.ftNumRightJustified;
		} else {
			lType = CommonCode.commonTypeChecks(binId, usage, picture, signed, signSeperate, signPosition);
		}

		return lType;
	}
}
