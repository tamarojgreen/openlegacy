package org.openlegacy.ide.eclipse.components.support;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;

/**
 * @author Ivan Bort
 * 
 */
public class TextEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String oldFieldName;

	public TextEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTable());
	}

	@Override
	protected void initializeCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		super.initializeCellEditorValue(cellEditor, cell);
		Object element = cell.getElement();
		if (element instanceof ScreenFieldDefinition) {
			oldFieldName = ((ScreenFieldDefinition)element).getName();
		} else {
			oldFieldName = null;
		}
	}

	@Override
	protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		super.saveCellEditorValue(cellEditor, cell);
		Object element = cell.getElement();
		if (element instanceof ScreenFieldDefinition && !StringUtils.isEmpty(oldFieldName)) {
			if (viewer.getData("screenEntityDefinition") != null) {
				ScreenEntityDefinition entityDefinition = (ScreenEntityDefinition)viewer.getData("screenEntityDefinition");
				ScreenFieldDefinition fieldDefinition = entityDefinition.getFieldsDefinitions().get(oldFieldName);
				String newFieldName = ((ScreenFieldDefinition)element).getName();
				entityDefinition.getFieldsDefinitions().remove(oldFieldName);
				entityDefinition.getFieldsDefinitions().put(newFieldName, fieldDefinition);
			}
		}
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
		if (element instanceof ScreenFieldDefinition) {
			return ((ScreenFieldDefinition)element).getName();
		} else if (element instanceof SimpleScreenIdentifier) {
			return ((SimpleScreenIdentifier)element).getText();
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		String val = StringUtils.isEmpty((String)value) ? "" : (String)value;
		if (element instanceof SimpleScreenFieldDefinition) {
			((SimpleScreenFieldDefinition)element).setName(val);
		} else if (element instanceof SimpleScreenIdentifier) {
			((SimpleScreenIdentifier)element).setText(val);
		}
		viewer.update(element, null);
	}

}
