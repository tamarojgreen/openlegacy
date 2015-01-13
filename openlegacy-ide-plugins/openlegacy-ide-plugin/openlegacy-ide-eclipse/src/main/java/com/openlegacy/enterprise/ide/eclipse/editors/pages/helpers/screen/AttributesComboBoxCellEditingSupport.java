package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.IdentifierModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.FieldAttributeType;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class AttributesComboBoxCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;
	private List<String> items;

	public AttributesComboBoxCellEditingSupport(TableViewer viewer, String fieldName, List<String> items) {
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
		if (this.fieldName.equals(ScreenAnnotationConstants.ATTRIBUTE) && ((IdentifierModel)element).getAttribute() != null) {
			return this.items.indexOf(((IdentifierModel)element).getAttribute().toString());
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
		if (this.fieldName.equals(ScreenAnnotationConstants.ATTRIBUTE) && ((Integer)value >= 0)) {
			((IdentifierModel)element).setAttribute(FieldAttributeType.valueOf(this.items.get((Integer)value)));
			this.viewer.update(element, null);
		} else if (this.fieldName.equals(ScreenAnnotationConstants.ATTRIBUTE) && ((Integer)value == -1)) {
			// set default value
			((IdentifierModel)element).setAttributeDefaultValue();
			this.viewer.update(element, null);
		}
	}

}
