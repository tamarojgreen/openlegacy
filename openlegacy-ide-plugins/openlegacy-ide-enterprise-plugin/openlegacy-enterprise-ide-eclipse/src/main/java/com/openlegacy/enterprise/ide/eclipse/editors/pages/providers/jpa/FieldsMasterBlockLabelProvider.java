package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaByteFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsMasterBlockLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof JpaBooleanFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_BOOLEAN);
		} else if (element instanceof JpaDateFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_DATE);
		} else if (element instanceof JpaIntegerFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_INTEGER);
		} else if (element instanceof JpaByteFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_BYTE);
		} else if (element instanceof JpaListFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_LIST);
		} else if (element instanceof JpaFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_STRING);
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		return ((JpaFieldModel)element).getFieldName();
	}

}
