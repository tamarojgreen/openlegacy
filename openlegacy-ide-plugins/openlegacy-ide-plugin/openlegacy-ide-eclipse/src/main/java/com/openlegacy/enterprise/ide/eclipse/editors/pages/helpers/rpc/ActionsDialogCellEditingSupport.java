package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.AbstractViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.RpcActionViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.AbstractActionsDialogCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.actions.RpcActions;

import java.net.MalformedURLException;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsDialogCellEditingSupport extends AbstractActionsDialogCellEditingSupport {

	public ActionsDialogCellEditingSupport(TableViewer viewer, String fieldName) {
		super(viewer, fieldName);
	}

	@Override
	public AbstractViewerFilter getViewerFilter() {
		if (fieldName.equals(AnnotationConstants.ACTION)) {
			return new RpcActionViewerFilter();
		}
		return null;
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
		if (this.fieldName.equals(AnnotationConstants.ACTION)) {
			return ((ActionModel)element).getActionName();
		} else if (fieldName.equals(AnnotationConstants.TARGET_ENTITY)) {
			return ((ActionModel)element).getTargetEntityClassName();
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
		if (value instanceof IType) {
			IType res = (IType)value;
			ActionModel model = (ActionModel)element;
			try {
				if (res.isClass()) {
					if (this.fieldName.equals(AnnotationConstants.ACTION)) {
						model.setActionName(res.getElementName());
						RpcAction action = RpcActions.newAction(model.getActionName().toUpperCase());
						if (action != null) {
							model.setAction(action);
						} else {
							// Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName('.'));
							Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName());
							model.setAction((RpcAction)clazz.newInstance());
						}
						((ActionModel)element).setActionName(((IType)value).getElementName());
					} else if (this.fieldName.equals(AnnotationConstants.TARGET_ENTITY)) {
						model.setTargetEntityClassName(res.getElementName());
						Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName());
						model.setTargetEntity(clazz);
					}
				}
			} catch (JavaModelException e) {
			} catch (MalformedURLException e) {
			} catch (CoreException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
		this.viewer.update(element, null);
	}

}
