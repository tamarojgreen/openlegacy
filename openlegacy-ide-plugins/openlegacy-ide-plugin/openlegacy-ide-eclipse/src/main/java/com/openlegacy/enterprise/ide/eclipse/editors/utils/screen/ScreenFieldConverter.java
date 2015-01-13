package com.openlegacy.enterprise.ide.eclipse.editors.utils.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenFieldConverter {

	public static ScreenFieldModel getNewFieldModel(ScreenFieldModel model, Class<?> toClass) {
		ScreenFieldModel convertedField = null;
		if (ScreenBooleanFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new ScreenBooleanFieldModel(model.getParent());
		} else if (ScreenDateFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new ScreenDateFieldModel(model.getParent());
		} else if (ScreenIntegerFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new ScreenIntegerFieldModel(model.getParent());
		} else if (ScreenEnumFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new ScreenEnumFieldModel(model.getParent());
		} else if (ScreenFieldValuesModel.class.isAssignableFrom(toClass)) {
			convertedField = new ScreenFieldValuesModel(model.getParent());
		} else {
			convertedField = new ScreenFieldModel(model.getParent());
		}
		return convertedField;
	}

}
