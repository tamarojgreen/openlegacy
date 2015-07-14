package org.openlegacy.types;

import javax.xml.bind.DatatypeConverter;

public class Base64Array extends BinaryArray {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setValue(String value) throws IllegalArgumentException {
		setValue(DatatypeConverter.parseBase64Binary(value));
	}

	@Override
	public Base64Array valueOf(String value) {
		Base64Array array = new Base64Array();
		array.setValue(value);
		return array;
	}

	@Override
	public String getValue() {
		return DatatypeConverter.printBase64Binary(value);
	}
}
