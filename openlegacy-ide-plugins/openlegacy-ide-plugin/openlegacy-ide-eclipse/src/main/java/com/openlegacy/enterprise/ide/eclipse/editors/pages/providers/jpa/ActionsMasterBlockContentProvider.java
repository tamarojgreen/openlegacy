/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 *
 */
public class ActionsMasterBlockContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	@Override
	public Object[] getElements(Object inputElement) {
		if (!(inputElement instanceof JpaActionsModel)) {
			return new Object[] {};
		}
		JpaActionsModel actionsModel = (JpaActionsModel) inputElement;
		List<Object> list = new ArrayList<Object>();
		list.addAll(actionsModel.getActions());
		return list.toArray();
	}

}
