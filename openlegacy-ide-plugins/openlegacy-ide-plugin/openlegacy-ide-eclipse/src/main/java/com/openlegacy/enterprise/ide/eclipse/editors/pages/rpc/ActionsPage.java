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

package com.openlegacy.enterprise.ide.eclipse.editors.pages.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.AbstractEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.rpc.ActionsMasterBlock;

import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openlegacy.ide.eclipse.Activator;

/**
 * @author Ivan Bort
 */
public class ActionsPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.rpc.pages.actions"; //$NON-NLS-1$

	private ActionsMasterBlock block;

	public ActionsPage(AbstractEditor editor) {
		super(editor, PAGE_ID, Messages.getString("ActionsPage.title"));//$NON-NLS-1$
		block = new ActionsMasterBlock(this);
	}

	@Override
	public void createFormContent() {
		final ScrolledForm form = managedForm.getForm();
		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("ActionsPage.form.title")); //$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));
		block.createContent(managedForm);
	}

	@Override
	public void refresh() {
		if (isActive()) {
			block.refresh();
		}
	}

}
