package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.AbstractActionsTextCellEditingSupport;

import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

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
		} else if (this.fieldName.equals(RpcAnnotationConstants.PATH)) {
			return ((ActionModel)element).getProgramPath();
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
		} else if (this.fieldName.equals(RpcAnnotationConstants.PATH)) {
			((ActionModel)element).setProgramPath((String)value);
		}
		this.viewer.update(element, null);
	}

}
