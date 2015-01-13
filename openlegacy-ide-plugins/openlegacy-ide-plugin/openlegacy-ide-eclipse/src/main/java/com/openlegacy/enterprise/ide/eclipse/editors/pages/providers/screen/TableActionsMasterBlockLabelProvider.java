package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openlegacy.ide.eclipse.Activator;

import java.text.MessageFormat;

/**
 * @author Ivan Bort
 * 
 */
public class TableActionsMasterBlockLabelProvider extends LabelProvider implements ITableLabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof NamedObject) {
			NamedObject model = (NamedObject)element;
			if (!model.getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_ACTION_ERR);
			}
		}
		return Activator.getDefault().getImage(Activator.ICON_ACTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		return MessageFormat.format("{0}-{1}", ((TableActionModel)element).getActionValue(),//$NON-NLS-1$ 
				((TableActionModel)element).getDisplayName());
	}

}
