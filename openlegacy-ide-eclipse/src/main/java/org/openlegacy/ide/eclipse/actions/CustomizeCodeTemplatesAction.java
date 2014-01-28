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
package org.openlegacy.ide.eclipse.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IActionDelegate;
import org.openlegacy.ide.eclipse.Messages;

public class CustomizeCodeTemplatesAction extends AbstractAction {

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		boolean cont = MessageDialog.openConfirm(getShell(), Messages.getString("title_openlegacy"),
				Messages.getString("message_customize_templates"));
		if (cont) {
			IProject project = (IProject)((TreeSelection)getSelection()).getFirstElement();
			EclipseDesignTimeExecuter.instance().copyCodeGenerationTemplates(project);
		}

	}

}
