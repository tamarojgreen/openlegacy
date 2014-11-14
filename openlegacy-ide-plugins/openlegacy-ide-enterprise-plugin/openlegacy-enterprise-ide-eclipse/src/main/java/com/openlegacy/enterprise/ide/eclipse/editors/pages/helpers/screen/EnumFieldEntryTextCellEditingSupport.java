package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.EnumEntryModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;

/**
 * @author Ivan Bort
 * 
 */
public class EnumFieldEntryTextCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;

	/**
	 * @param viewer
	 */
	public EnumFieldEntryTextCellEditingSupport(TableViewer viewer, String fieldName) {
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
		if (fieldName.equals(AnnotationConstants.NAME)) {
			return ((EnumEntryModel)element).getName();
		} else if (fieldName.equals(ScreenAnnotationConstants.VALUE)) {
			return ((EnumEntryModel)element).getValue();
		} else if (fieldName.equals(AnnotationConstants.DISPLAY_NAME)) {
			return ((EnumEntryModel)element).getDisplayName();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		String val = ((String)value).trim();
		if (fieldName.equals(AnnotationConstants.NAME)) {
			((EnumEntryModel)element).setName(val);
		} else if (fieldName.equals(ScreenAnnotationConstants.VALUE)) {
			((EnumEntryModel)element).setValue(val);
		} else if (fieldName.equals(AnnotationConstants.DISPLAY_NAME)) {
			((EnumEntryModel)element).setDisplayName(val);
		}
		this.viewer.update(element, null);
	}

}
