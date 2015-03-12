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

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IActionDelegate;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.PopupUtil;

public class GenerateServiceAction extends AbstractAction {

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		IFile file = (IFile)((ICompilationUnit)((TreeSelection)selection).getFirstElement()).getResource();
		String supportRestfulService = EclipseDesignTimeExecuter.instance().getPreference(file.getProject(),
				PreferencesConstants.SUPPORT_RESTFUL_SERVICE);
		if (!EclipseDesignTimeExecuter.instance().isSupportServiceGeneration(file.getProject(),
				Boolean.valueOf(supportRestfulService))) {
			PopupUtil.warn(Messages.getString("warn_service_generation_not_supported"));
			return;
		}
		GenerateServiceDialog dialog = new GenerateServiceDialog(getShell(), getSelection());
		dialog.open();

	}

}
