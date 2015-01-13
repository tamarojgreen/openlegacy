package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openlegacy.ide.eclipse.Activator;

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
			boolean isKey = ((ScreenBooleanFieldModel)element).isKey();
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BOOLEAN_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_BOOLEAN_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BOOLEAN_KEY) : Activator.getDefault().getImage(
					Activator.ICON_BOOLEAN);
		} else if (element instanceof ScreenDateFieldModel) {
			boolean isKey = ((ScreenDateFieldModel)element).isKey();
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_DATE_KEY_ERR) : Activator.getDefault().getImage(
						Activator.ICON_DATE_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_DATE_KEY) : Activator.getDefault().getImage(
					Activator.ICON_DATE);
		} else if (element instanceof ScreenEnumFieldModel) {
			boolean isKey = ((ScreenEnumFieldModel)element).isKey();
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_ENUM_KEY_ERR) : Activator.getDefault().getImage(
						Activator.ICON_ENUM_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_ENUM_KEY) : Activator.getDefault().getImage(
					Activator.ICON_ENUM);
		} else if (element instanceof ScreenFieldValuesModel) {
			boolean isKey = ((ScreenFieldValuesModel)element).isKey();
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_VALUES_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_VALUES_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_VALUES_KEY) : Activator.getDefault().getImage(
					Activator.ICON_VALUES);
		} else if (element instanceof ScreenIntegerFieldModel) {
			boolean isKey = ((ScreenIntegerFieldModel)element).isKey();
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_INTEGER_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_INTEGER_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_INTEGER_KEY) : Activator.getDefault().getImage(
					Activator.ICON_INTEGER);
		} else if (element instanceof ScreenPartModel) {
			List<ScreenFieldModel> sortedFields = ((ScreenPartModel)element).getSortedFields();
			for (ScreenFieldModel screenFieldModel : sortedFields) {
				if (!screenFieldModel.getValidationMessages().isEmpty()) {
					return Activator.getDefault().getImage(Activator.ICON_PART_ERR);
				}
			}
			return Activator.getDefault().getImage(Activator.ICON_PART);
		} else if (element instanceof ScreenFieldModel) {
			boolean isKey = ((ScreenFieldModel)element).isKey();
			if (!((ScreenFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_STRING_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_STRING_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_STRING_KEY) : Activator.getDefault().getImage(
					Activator.ICON_STRING);
		}
		return null;
	}
}
