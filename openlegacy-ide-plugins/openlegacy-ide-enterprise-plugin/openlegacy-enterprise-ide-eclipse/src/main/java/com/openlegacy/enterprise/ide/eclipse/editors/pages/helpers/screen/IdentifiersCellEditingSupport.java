package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.IdentifierModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.exceptions.OpenLegacyValidationException;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;

/**
 * @author Ivan Bort
 * 
 */
public class IdentifiersCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;
	private ScreenEntity entity;

	/**
	 * @param viewer
	 */
	public IdentifiersCellEditingSupport(TableViewer viewer, String fieldName, ScreenEntity entity) {
		super(viewer);
		this.viewer = viewer;
		this.fieldName = fieldName;
		this.entity = entity;
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
		if (this.fieldName.equals(ScreenAnnotationConstants.ROW)) {
			return Integer.toString(((IdentifierModel)element).getRow());
		}
		if (this.fieldName.equals(ScreenAnnotationConstants.COLUMN)) {
			return Integer.toString(((IdentifierModel)element).getColumn());
		}
		return ((IdentifierModel)element).getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		try {
			if (this.fieldName.equals(ScreenAnnotationConstants.ROW)) {
				validateRow(value);
				((IdentifierModel)element).setRow(Integer.parseInt((String)value));
			} else if (this.fieldName.equals(ScreenAnnotationConstants.COLUMN)) {
				validateColumn(value);
				((IdentifierModel)element).setColumn(Integer.parseInt((String)value));
			} else {
				((IdentifierModel)element).setText((String)value);
			}
			this.viewer.update(element, null);
		} catch (OpenLegacyValidationException e) {
			this.viewer.update(element, null);
			return;
		}
	}

	private void validateRow(Object value) throws OpenLegacyValidationException {
		try {
			int intValue = Integer.parseInt((String)value);
			if ((intValue < Constants.MIN_ROW_COLUMN) || (intValue > entity.getEntityModel().getRows())) {
				throw new OpenLegacyValidationException(Messages.getString("validation.is.out.of.range"));//$NON-NLS-1$
			}
		} catch (NumberFormatException e) {
			throw new OpenLegacyValidationException(Messages.getString("validation.is.out.of.range"));//$NON-NLS-1$
		}
	}

	private void validateColumn(Object value) throws OpenLegacyValidationException {
		try {
			int intValue = Integer.parseInt((String)value);
			if ((intValue < Constants.MIN_ROW_COLUMN) || (intValue > entity.getEntityModel().getColumns())) {
				throw new OpenLegacyValidationException(Messages.getString("validation.is.out.of.range"));//$NON-NLS-1$
			}
		} catch (NumberFormatException e) {
			throw new OpenLegacyValidationException(Messages.getString("validation.is.out.of.range"));//$NON-NLS-1$
		}
	}
}
