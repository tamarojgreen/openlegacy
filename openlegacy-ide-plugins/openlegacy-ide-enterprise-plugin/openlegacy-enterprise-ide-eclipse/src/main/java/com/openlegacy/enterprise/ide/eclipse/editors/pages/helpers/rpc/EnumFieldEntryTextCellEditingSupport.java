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

package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.EnumEntryModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.openlegacy.designtime.generators.AnnotationConstants;

/**
 * @author Ivan Bort
 * 
 */
public class EnumFieldEntryTextCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;

	public EnumFieldEntryTextCellEditingSupport(TableViewer viewer, String fieldName) {
		super(viewer);
		this.viewer = viewer;
		this.fieldName = fieldName;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(this.viewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (fieldName.equals(AnnotationConstants.NAME)) {
			return ((EnumEntryModel)element).getName();
		} else if (fieldName.equals(AnnotationConstants.VALUE)) {
			return ((EnumEntryModel)element).getValue();
		} else if (fieldName.equals(AnnotationConstants.DISPLAY_NAME)) {
			return ((EnumEntryModel)element).getDisplayName();
		}
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		String val = ((String)value).trim();
		if (fieldName.equals(AnnotationConstants.NAME)) {
			((EnumEntryModel)element).setName(val);
		} else if (fieldName.equals(AnnotationConstants.VALUE)) {
			((EnumEntryModel)element).setValue(val);
		} else if (fieldName.equals(AnnotationConstants.DISPLAY_NAME)) {
			((EnumEntryModel)element).setDisplayName(val);
		}
		this.viewer.update(element, null);
	}

}
