package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.AbstractActionsTextCellEditingSupport;

import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsTextCellEditingSupport extends AbstractActionsTextCellEditingSupport {

	public ActionsTextCellEditingSupport(TableViewer viewer, String fieldName) {
		super(viewer, fieldName);
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
		if (this.fieldName.equals(AnnotationConstants.DISPLAY_NAME)) {
			return ((ActionModel)element).getDisplayName();
		} else if (this.fieldName.equals(AnnotationConstants.ALIAS)) {
			return ((ActionModel)element).getAlias();
		} else if (this.fieldName.equals(AnnotationConstants.WHEN)) {
			String when = ((ActionModel)element).getWhen();
			return when == null ? "" : when;
		} else if (this.fieldName.equals(AnnotationConstants.ROW)) {
			return Integer.toString(((ActionModel)element).getRow());
		} else if (this.fieldName.equals(AnnotationConstants.COLUMN)) {
			return Integer.toString(((ActionModel)element).getColumn());
		} else if (this.fieldName.equals(AnnotationConstants.LENGTH)) {
			return Integer.toString(((ActionModel)element).getLength());
		} else if (this.fieldName.equals(ScreenAnnotationConstants.FOCUS_FIELD)) {
			return ((ActionModel)element).getFocusField() == null ? "" : ((ActionModel)element).getFocusField();
		} else if (this.fieldName.equals(ScreenAnnotationConstants.SLEEP)) {
			return Integer.toString(((ActionModel)element).getSleep());
		}

		return Messages.getString("unknown.field");//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (this.fieldName.equals(AnnotationConstants.DISPLAY_NAME)) {
			((ActionModel)element).setDisplayName((String)value);
		} else if (this.fieldName.equals(AnnotationConstants.ALIAS)) {
			((ActionModel)element).setAlias((String)value);
		} else if (this.fieldName.equals(AnnotationConstants.WHEN)) {
			((ActionModel)element).setWhen(value.equals("") ? null : (String)value);
		} else if (this.fieldName.equals(AnnotationConstants.ROW)) {
			((ActionModel)element).setRow(parseInt(value));
		} else if (this.fieldName.equals(AnnotationConstants.COLUMN)) {
			((ActionModel)element).setColumn(parseInt(value));
		} else if (this.fieldName.equals(AnnotationConstants.LENGTH)) {
			((ActionModel)element).setLength(parseInt(value));
		} else if (this.fieldName.equals(ScreenAnnotationConstants.FOCUS_FIELD)) {
			((ActionModel)element).setFocusField((String)value);
		} else if (this.fieldName.equals(ScreenAnnotationConstants.SLEEP)) {
			((ActionModel)element).setSleep(parseInt(value));
		}

		this.viewer.update(element, null);
	}

	private static int parseInt(Object value) {
		int val = 0;
		if (!(value == null || "".equals(value))) {
			try {
				val = Integer.parseInt((String)value);
			} catch (NumberFormatException e) {
			}
		}
		return val;
	}

}
