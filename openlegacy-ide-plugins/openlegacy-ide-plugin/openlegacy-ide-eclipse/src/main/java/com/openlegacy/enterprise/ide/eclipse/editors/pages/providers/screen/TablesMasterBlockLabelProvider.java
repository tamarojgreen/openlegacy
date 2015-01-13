package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openlegacy.ide.eclipse.Activator;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class TablesMasterBlockLabelProvider extends LabelProvider implements ITableLabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		ScreenTableModel model = (ScreenTableModel)element;
		if (!model.getValidationMessages().isEmpty()) {
			return Activator.getDefault().getImage(Activator.ICON_TABLE_ERR);
		}
		List<TableActionModel> sortedActions = model.getSortedActions();
		for (TableActionModel actionModel : sortedActions) {
			if (!actionModel.getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_TABLE_ERR);
			}
		}
		List<ScreenColumnModel> sortedColumns = model.getSortedColumns();
		for (ScreenColumnModel columnModel : sortedColumns) {
			if (!columnModel.getValidationMessages().isEmpty()) {
				return Activator.getDefault().getImage(Activator.ICON_TABLE_ERR);
			}
		}
		return Activator.getDefault().getImage(Activator.ICON_TABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		ScreenTableModel model = (ScreenTableModel)element;
		return model.getClassName();
	}

}
