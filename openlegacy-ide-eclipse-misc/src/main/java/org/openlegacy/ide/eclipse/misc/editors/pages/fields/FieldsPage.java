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

package org.openlegacy.ide.eclipse.misc.editors.pages.fields;

import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.openlegacy.ide.eclipse.misc.Messages;

/**
 * @author Imivan
 * 
 */
public class FieldsPage extends FormPage {

	private final static String PAGE_ID = "org.openlegacy.ide.eclipse.misc.pages.fieldspage"; //$NON-NLS-1$

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public FieldsPage(FormEditor editor) {
		super(editor, PAGE_ID, Messages.getString("FieldsPage.title"));
	}

}
