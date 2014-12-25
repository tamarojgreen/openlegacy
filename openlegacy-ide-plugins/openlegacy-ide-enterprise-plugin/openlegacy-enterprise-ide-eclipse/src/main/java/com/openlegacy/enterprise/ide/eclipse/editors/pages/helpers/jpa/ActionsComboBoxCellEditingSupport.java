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

import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.designtime.generators.AnnotationConstants;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsComboBoxCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;
	private List<String> items;

	public ActionsComboBoxCellEditingSupport(TableViewer viewer, String fieldName, List<String> items) {
		super(viewer);
		this.viewer = viewer;
		this.fieldName = fieldName;
		this.items = items;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new ComboBoxCellEditor(this.viewer.getTable(), this.items.toArray(new String[] {}));
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (this.fieldName.equals(AnnotationConstants.GLOBAL)) {
			return this.items.indexOf(String.valueOf(((ActionModel)element).isGlobal()).toLowerCase());
		}
		return 0;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (this.fieldName.equals(AnnotationConstants.GLOBAL) && ((Integer)value >= 0)) {
			((ActionModel)element).setGlobal(Boolean.valueOf(this.items.get((Integer)value).toLowerCase()));
			this.viewer.update(element, null);
		}
	}

}
