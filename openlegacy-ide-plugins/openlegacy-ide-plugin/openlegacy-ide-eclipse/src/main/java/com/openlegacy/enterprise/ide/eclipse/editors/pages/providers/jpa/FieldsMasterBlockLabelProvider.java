package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaByteFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaManyToOneFieldModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openlegacy.ide.eclipse.Activator;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsMasterBlockLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof JpaBooleanFieldModel) {
			boolean isKey = ((JpaBooleanFieldModel) element).isKey();
			if (!((JpaBooleanFieldModel) element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BOOLEAN_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_BOOLEAN_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BOOLEAN_KEY) : Activator.getDefault().getImage(
					Activator.ICON_BOOLEAN);
		} else if (element instanceof JpaDateFieldModel) {
			boolean isKey = ((JpaDateFieldModel) element).isKey();
			if (!((JpaDateFieldModel) element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_DATE_KEY_ERR) : Activator.getDefault().getImage(
						Activator.ICON_DATE_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_DATE_KEY) : Activator.getDefault().getImage(
					Activator.ICON_DATE);
		} else if (element instanceof JpaIntegerFieldModel) {
			boolean isKey = ((JpaIntegerFieldModel) element).isKey();
			if (!((JpaIntegerFieldModel) element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_INTEGER_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_INTEGER_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_INTEGER_KEY) : Activator.getDefault().getImage(
					Activator.ICON_INTEGER);
		} else if (element instanceof JpaByteFieldModel) {
			boolean isKey = ((JpaByteFieldModel) element).isKey();
			if (!((JpaByteFieldModel) element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BYTE_KEY_ERR) : Activator.getDefault().getImage(
						Activator.ICON_BYTE_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BYTE_KEY) : Activator.getDefault().getImage(
					Activator.ICON_BYTE);
		} else if (element instanceof JpaListFieldModel) {
			boolean isKey = ((JpaListFieldModel) element).isKey();
			if (!((JpaListFieldModel) element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_LIST_KEY_ERR) : Activator.getDefault().getImage(
						Activator.ICON_LIST_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_LIST_KEY) : Activator.getDefault().getImage(
					Activator.ICON_LIST);
		} else if (element instanceof JpaManyToOneFieldModel) {
			if (!((JpaManyToOneFieldModel) element).getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_MANY_TO_ONE_ERR);
			}
			return Activator.getDefault().getImage(Activator.ICON_MANY_TO_ONE);

		} else if (element instanceof JpaEnumFieldModel) {
			boolean isKey = ((JpaEnumFieldModel) element).isKey();
			if (!((JpaEnumFieldModel) element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_ENUM_KEY_ERR) : Activator.getDefault().getImage(
						Activator.ICON_ENUM_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_ENUM_KEY) : Activator.getDefault().getImage(
					Activator.ICON_ENUM);
		} else if (element instanceof JpaFieldModel) {
			boolean isKey = ((JpaFieldModel) element).isKey();
			if (!((JpaFieldModel) element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_STRING_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_STRING_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_STRING_KEY) : Activator.getDefault().getImage(
					Activator.ICON_STRING);
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		return ((JpaFieldModel) element).getFieldName();
	}

}
