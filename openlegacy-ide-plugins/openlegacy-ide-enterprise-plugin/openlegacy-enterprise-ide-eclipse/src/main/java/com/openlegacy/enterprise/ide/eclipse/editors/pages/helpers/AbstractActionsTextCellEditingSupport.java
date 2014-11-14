package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractActionsTextCellEditingSupport extends EditingSupport {

	protected TableViewer viewer;
	protected String fieldName;

	public AbstractActionsTextCellEditingSupport(TableViewer viewer, String fieldName) {
		super(viewer);
		this.viewer = viewer;
		this.fieldName = fieldName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(this.viewer.getTable());
	}

}
