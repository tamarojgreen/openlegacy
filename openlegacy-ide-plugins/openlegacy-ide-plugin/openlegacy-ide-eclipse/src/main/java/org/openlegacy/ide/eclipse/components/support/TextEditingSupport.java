package org.openlegacy.ide.eclipse.components.support;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

/**
 * @author Ivan Bort
 * 
 */
public class TextEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String oldFieldName;
	private String columnName;
	private ScreenSize screenSize;

	public TextEditingSupport(TableViewer viewer, String columnName, ScreenSize screenSize) {
		super(viewer);
		this.viewer = viewer;
		this.columnName = columnName;
		this.screenSize = screenSize;
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
		if (StringUtils.equals(columnName, ScreenAnnotationConstants.FIELD)) {
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
			if (StringUtils.equals(columnName, ScreenAnnotationConstants.FIELD)) {
				return ((ScreenFieldDefinition)element).getName();
			} else if (StringUtils.equals(columnName, ScreenAnnotationConstants.ROW)) {
				return String.valueOf(((ScreenFieldDefinition)element).getPosition().getRow());
			} else if (StringUtils.equals(columnName, ScreenAnnotationConstants.COLUMN)) {
				return String.valueOf(((ScreenFieldDefinition)element).getPosition().getColumn());
			}
		} else if (element instanceof SimpleScreenIdentifier) {
			if (StringUtils.equals(columnName, ScreenAnnotationConstants.IDENTIFIERS)) {
				return ((SimpleScreenIdentifier)element).getText();
			} else if (StringUtils.equals(columnName, ScreenAnnotationConstants.ROW)) {
				return String.valueOf(((SimpleScreenIdentifier)element).getPosition().getRow());
			} else if (StringUtils.equals(columnName, ScreenAnnotationConstants.COLUMN)) {
				return String.valueOf(((SimpleScreenIdentifier)element).getPosition().getColumn());
			}
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
			if (StringUtils.equals(columnName, ScreenAnnotationConstants.FIELD)) {
				((SimpleScreenFieldDefinition)element).setName(val);
			} else if (StringUtils.equals(columnName, ScreenAnnotationConstants.ROW)) {
				int column = ((SimpleScreenFieldDefinition)element).getPosition().getColumn();
				int row = StringUtils.isNumeric(val) && screenSize != null && screenSize.getRows() >= Integer.valueOf(val)
						&& Integer.valueOf(val) > 0 ? Integer.valueOf(val) : 0;
				((SimpleScreenFieldDefinition)element).setPosition(new SimpleTerminalPosition(row, column));
			} else if (StringUtils.equals(columnName, ScreenAnnotationConstants.COLUMN)) {
				int row = ((SimpleScreenFieldDefinition)element).getPosition().getRow();
				int column = StringUtils.isNumeric(val) && screenSize != null && screenSize.getColumns() >= Integer.valueOf(val)
						&& Integer.valueOf(val) > 0 ? Integer.valueOf(val) : 0;
				((SimpleScreenFieldDefinition)element).setPosition(new SimpleTerminalPosition(row, column));
			}
		} else if (element instanceof SimpleScreenIdentifier) {
			if (StringUtils.equals(columnName, ScreenAnnotationConstants.IDENTIFIERS)) {
				((SimpleScreenIdentifier)element).setText(val);
			} else if (StringUtils.equals(columnName, ScreenAnnotationConstants.ROW)) {
				int column = ((SimpleScreenIdentifier)element).getPosition().getColumn();
				int row = StringUtils.isNumeric(val) && screenSize != null && screenSize.getRows() >= Integer.valueOf(val)
						&& Integer.valueOf(val) > 0 ? Integer.valueOf(val) : 0;
				((SimpleScreenIdentifier)element).setPosition(new SimpleTerminalPosition(row, column));
			} else if (StringUtils.equals(columnName, ScreenAnnotationConstants.COLUMN)) {
				int row = ((SimpleScreenIdentifier)element).getPosition().getRow();
				int column = StringUtils.isNumeric(val) && screenSize != null && screenSize.getColumns() >= Integer.valueOf(val)
						&& Integer.valueOf(val) > 0 ? Integer.valueOf(val) : 0;
				((SimpleScreenIdentifier)element).setPosition(new SimpleTerminalPosition(row, column));
			}
		}
		viewer.update(element, null);
	}

}
