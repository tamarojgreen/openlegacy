package org.openlegacy.ide.eclipse.components.support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.definitions.support.SimpleBooleanFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;

import java.util.Date;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ComboBoxEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private List<String> items;

	public ComboBoxEditingSupport(TableViewer viewer, List<String> items) {
		super(viewer);
		this.viewer = viewer;
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new ComboBoxCellEditor(viewer.getTable(), items.toArray(new String[] {}));
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
			return items.indexOf(((ScreenFieldDefinition)element).getJavaType().getSimpleName());
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
		if (element instanceof SimpleScreenFieldDefinition && (Integer)value >= 0) {
			SimpleScreenFieldDefinition definition = (SimpleScreenFieldDefinition)element;
			String type = items.get((Integer)value);
			if (Boolean.class.getSimpleName().equals(type)) {
				definition.setJavaType(Boolean.class);
				definition.setFieldTypeDefinition(new SimpleBooleanFieldTypeDefinition("", "", false));
			} else if (Date.class.getSimpleName().equals(type)) {
				definition.setJavaType(Date.class);
				definition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition(0, 0, 0, ""));
			} else if (Enum.class.getSimpleName().equals(type)) {
				definition.setJavaType(Enum.class);
				definition.setFieldTypeDefinition(new SimpleEnumFieldTypeDefinition());
			} else if (Integer.class.getSimpleName().equals(type)) {
				definition.setJavaType(Integer.class);
				definition.setFieldTypeDefinition(null);
			} else {
				definition.setJavaType(String.class);
				definition.setFieldTypeDefinition(null);
			}
		}
		viewer.update(element, null);
	}

}
