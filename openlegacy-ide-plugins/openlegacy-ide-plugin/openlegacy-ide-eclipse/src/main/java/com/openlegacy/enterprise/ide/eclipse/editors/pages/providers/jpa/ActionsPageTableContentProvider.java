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

package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaActionsModel;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsPageTableContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof JpaActionsModel) {
			JpaActionsModel model = (JpaActionsModel)inputElement;
			if (!model.getActions().isEmpty()) {
				return model.getActions().toArray();
			}
		}
		return new Object[] {};
	}

}
