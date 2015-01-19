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
package org.openlegacy.ide.eclipse.actions.screen;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IActionDelegate;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.actions.AbstractAction;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;

public class ObfuscateTrailAction extends AbstractAction {

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		Object firstElement = ((TreeSelection)getSelection()).getFirstElement();
		if (!(firstElement instanceof IFile)) {
			MessageDialog.openError(getShell(), PluginConstants.TITLE, Messages.getString("error_invalid_trail_file_selection"));
		}
		EclipseDesignTimeExecuter.instance().obfuscateTrail((IFile)firstElement);

		MessageDialog.openInformation(getShell(), PluginConstants.TITLE, Messages.getString("trail_obfuscated"));

	}
}
