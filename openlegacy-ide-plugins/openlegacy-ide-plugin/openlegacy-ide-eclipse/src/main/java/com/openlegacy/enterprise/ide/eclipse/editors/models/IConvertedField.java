package com.openlegacy.enterprise.ide.eclipse.editors.models;


/**
 * @author Ivan Bort
 * 
 */
public interface IConvertedField {

	public NamedObject convertFrom(NamedObject model);

	public void fillFieldsMap();

}
