/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openlegacy.ide.eclipse.Activator;

/**
 * @author Ivan Bort
 * 
 */
public class TableColumnsMasterBlockLabelProvider extends LabelProvider implements ITableLabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		ScreenColumnModel model = (ScreenColumnModel)element;
		String javaTypeName = model.getJavaTypeName();
		boolean isKey = model.isKey();

		if (String.class.getSimpleName().equals(javaTypeName)) {
			if (!model.getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_STRING_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_STRING_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_STRING_KEY) : Activator.getDefault().getImage(
					Activator.ICON_STRING);
		} else {
			if (!model.getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_INTEGER_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_INTEGER_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_INTEGER_KEY) : Activator.getDefault().getImage(
					Activator.ICON_INTEGER);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		return ((ScreenColumnModel)element).getFieldName();
	}

}
