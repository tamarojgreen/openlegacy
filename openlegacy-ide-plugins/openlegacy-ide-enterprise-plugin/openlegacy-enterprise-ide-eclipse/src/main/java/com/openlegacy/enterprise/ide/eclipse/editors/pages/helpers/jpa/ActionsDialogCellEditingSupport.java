/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.AbstractViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.JpaActionViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.AbstractActionsDialogCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.db.actions.DbAction;
import org.openlegacy.db.actions.DbActions;
import org.openlegacy.designtime.generators.AnnotationConstants;

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
			return new JpaActionViewerFilter();
		}
		return null;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (this.fieldName.equals(AnnotationConstants.ACTION)) {
			return ((ActionModel)element).getActionName();
		} else if (fieldName.equals(AnnotationConstants.TARGET_ENTITY)) {
			return ((ActionModel)element).getTargetEntityClassName();
		}
		return Messages.getString("unknown.field");//$NON-NLS-1$
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (value instanceof IType) {
			IType res = (IType)value;
			ActionModel model = (ActionModel)element;
			try {
				if (res.isClass()) {
					if (this.fieldName.equals(AnnotationConstants.ACTION)) {
						model.setActionName(res.getElementName());
						DbAction action = DbActions.newAction(model.getActionName().toUpperCase());
						if (action != null) {
							model.setAction(action);
						} else {
							// Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName('.'));
							Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName());
							model.setAction((DbAction)clazz.newInstance());
						}
						model.setActionName(((IType)value).getElementName());
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
