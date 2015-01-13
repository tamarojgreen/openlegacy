package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;
import org.openlegacy.db.definitions.SimpleDbTableUniqueConstraintDefinition;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class UniqueConstraintsCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;

	public UniqueConstraintsCellEditingSupport(TableViewer viewer, String fieldName) {
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
		if (fieldName.equals(DbAnnotationConstants.COLUMN_NAMES)) {
			return StringUtils.join(((UniqueConstraintDefinition)element).getColumnNames(), ",");
		}
		return ((UniqueConstraintDefinition)element).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (fieldName.equals(DbAnnotationConstants.COLUMN_NAMES)) {
			String[] split = StringUtils.split((String)value, ",");
			List<String> list = Arrays.asList(split);
			((UniqueConstraintDefinition)element).getColumnNames().clear();
			((UniqueConstraintDefinition)element).getColumnNames().addAll(list);
		} else {
			((SimpleDbTableUniqueConstraintDefinition)element).setName((String)value);
		}
		this.viewer.update(element, null);
	}

}
