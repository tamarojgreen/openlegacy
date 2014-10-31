package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldAssignDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class AssignedFieldsCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;

	/**
	 * @param viewer
	 */
	public AssignedFieldsCellEditingSupport(TableViewer viewer, String fieldName) {
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
		if (this.fieldName.equals(ScreenAnnotationConstants.FIELD)) {
			return ((FieldAssignDefinition)element).getName();
		}
		String value = ((FieldAssignDefinition)element).getValue();
		return value == null ? "" : value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (this.fieldName.equals(ScreenAnnotationConstants.FIELD)) {
			((SimpleFieldAssignDefinition)element).setName((String)value);
		} else {
			((SimpleFieldAssignDefinition)element).setValue((String)value);
		}
		this.viewer.update(element, null);
	}

}
