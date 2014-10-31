package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

/**
 * @author Ivan Bort
 * 
 */
public interface IConvertedField {

	public ScreenFieldModel convertFrom(ScreenFieldModel model);

	public void fillFieldsMap();

}
