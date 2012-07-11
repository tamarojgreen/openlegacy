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
package org.openlegacy.ide.eclipse.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IActionDelegate;

public class CustomizeCodeTemplatesAction extends AbstractAction {

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		boolean cont = MessageDialog.openConfirm(
				getShell(),
				"OpenLegacy",
				"Customize templates will create a directory names \"templates\" under your project, and will allow you to customize OpenLegacy generated code. Continue?");
		if (cont) {
			IProject project = (IProject)((TreeSelection)getSelection()).getFirstElement();
			EclipseDesignTimeExecuter.instance().copyCodeGenerationTemplates(project);
		}

	}

}
