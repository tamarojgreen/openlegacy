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

package org.openlegacy.ide.eclipse.misc.editors.pages.general;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openlegacy.ide.eclipse.misc.Activator;
import org.openlegacy.ide.eclipse.misc.Messages;

/**
 * @author Imivan
 * 
 */
public class GeneralPage extends FormPage {

	private final static String PAGE_ID = "org.openlegacy.ide.eclipse.misc.pages.generalpage"; //$NON-NLS-1$

	private GeneralScrolledBlock block;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public GeneralPage(FormEditor editor) {
		super(editor, PAGE_ID, Messages.getString("GeneralPage.title"));
		block = new GeneralScrolledBlock(this);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		form.setText(Messages.getString("GeneralPage.form.title")); //$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));
		block.createContent(managedForm);
	}

}
