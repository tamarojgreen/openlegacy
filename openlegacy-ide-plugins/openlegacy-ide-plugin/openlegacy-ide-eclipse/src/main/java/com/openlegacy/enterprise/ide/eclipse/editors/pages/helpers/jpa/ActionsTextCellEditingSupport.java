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
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.AbstractActionsTextCellEditingSupport;

import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.designtime.generators.AnnotationConstants;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsTextCellEditingSupport extends AbstractActionsTextCellEditingSupport {

	public ActionsTextCellEditingSupport(TableViewer viewer, String fieldName) {
		super(viewer, fieldName);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (this.fieldName.equals(AnnotationConstants.DISPLAY_NAME)) {
			return ((ActionModel)element).getDisplayName();
		} else if (this.fieldName.equals(AnnotationConstants.ALIAS)) {
			return ((ActionModel)element).getAlias();
		}
		return Messages.getString("unknown.field");//$NON-NLS-1$
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (this.fieldName.equals(AnnotationConstants.DISPLAY_NAME)) {
			((ActionModel)element).setDisplayName((String)value);
		} else if (this.fieldName.equals(AnnotationConstants.ALIAS)) {
			((ActionModel)element).setAlias((String)value);
		}
		this.viewer.update(element, null);
	}

}
