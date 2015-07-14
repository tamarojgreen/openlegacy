package org.openlegacy.types;

import javax.xml.bind.DatatypeConverter;

public class HexArray extends BinaryArray {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setValue(String value) {
		setValue(DatatypeConverter.parseHexBinary(value));
	}

	@Override
	public BinaryArray valueOf(String value) {
		HexArray array = new HexArray();
		array.setValue(value);
		return array;
	}

	@Override
	public String getValue() {
		return DatatypeConverter.printHexBinary(value);
	}
}
