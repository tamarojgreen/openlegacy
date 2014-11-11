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

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

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
		if (!model.getValidationMessages().isEmpty()) {
			return String.class.getSimpleName().equals(javaTypeName) ? Activator.getDefault().getImage(Activator.ICON_STRING_ERR)
					: Activator.getDefault().getImage(Activator.ICON_INTEGER_ERR);
		}
		return String.class.getSimpleName().equals(javaTypeName) ? Activator.getDefault().getImage(Activator.ICON_STRING)
				: Activator.getDefault().getImage(Activator.ICON_INTEGER);
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
