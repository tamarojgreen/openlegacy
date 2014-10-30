package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class PartsMasterBlockLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof ScreenFieldModel) {
			return ((ScreenFieldModel)element).getFieldName();
		}
		return ((ScreenPartModel)element).getClassName();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ScreenBooleanFieldModel) {
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_BOOLEAN_ERR);
			}
			return Activator.getDefault().getImage(Activator.ICON_BOOLEAN);
		} else if (element instanceof ScreenDateFieldModel) {
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_DATE_ERR);
			}
			return Activator.getDefault().getImage(Activator.ICON_DATE);
		} else if (element instanceof ScreenEnumFieldModel) {
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_ENUM_ERR);
			}
			return Activator.getDefault().getImage(Activator.ICON_ENUM);
		} else if (element instanceof ScreenFieldValuesModel) {
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_VALUES_ERR);
			}
			return Activator.getDefault().getImage(Activator.ICON_VALUES);
		} else if (element instanceof ScreenIntegerFieldModel) {
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_INTEGER_ERR);
			}
			return Activator.getDefault().getImage(Activator.ICON_INTEGER);
		} else if (element instanceof ScreenPartModel) {
			List<ScreenFieldModel> sortedFields = ((ScreenPartModel)element).getSortedFields();
			for (ScreenFieldModel screenFieldModel : sortedFields) {
				if (!screenFieldModel.getValidationMessages().isEmpty()) {
					return Activator.getDefault().getImage(Activator.ICON_PART_ERR);
				}
			}
			return Activator.getDefault().getImage(Activator.ICON_PART);
		} else if (element instanceof ScreenFieldModel) {
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_STRING_ERR);
			}
			return Activator.getDefault().getImage(Activator.ICON_STRING);
		}
		return null;
	}

}
