package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.designtime.generators.AnnotationConstants;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsComboBoxCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;
	private List<String> items;

	public ActionsComboBoxCellEditingSupport(TableViewer viewer, String fieldName, List<String> items) {
		super(viewer);
		this.viewer = viewer;
		this.fieldName = fieldName;
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new ComboBoxCellEditor(this.viewer.getTable(), this.items.toArray(new String[] {}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		if (this.fieldName.equals(AnnotationConstants.GLOBAL)) {
			return this.items.indexOf(String.valueOf(((ActionModel)element).isGlobal()).toLowerCase());
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (this.fieldName.equals(AnnotationConstants.GLOBAL) && ((Integer)value >= 0)) {
			((ActionModel)element).setGlobal(Boolean.valueOf(this.items.get((Integer)value).toLowerCase()));
			this.viewer.update(element, null);
		}
	}

}
