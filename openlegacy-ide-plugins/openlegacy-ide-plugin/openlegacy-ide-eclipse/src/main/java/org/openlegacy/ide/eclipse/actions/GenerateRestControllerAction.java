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

package org.openlegacy.ide.eclipse.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeSelection;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.PopupUtil;

/**
 * @author Ivan Bort
 * 
 */
public class GenerateRestControllerAction extends AbstractAction {

	@Override
	public void run(IAction action) {
		IFile file = (IFile)((ICompilationUnit)((TreeSelection)selection).getFirstElement()).getResource();
		if (!EclipseDesignTimeExecuter.instance().isSupportRestControllerGeneration(file)) {
			PopupUtil.warn(Messages.getString("warn_controller_generation_not_supported"));
			return;
		}

		GenerateRestControllerDialog dialog = new GenerateRestControllerDialog(getShell(), getSelection());
		dialog.open();
	}

}
