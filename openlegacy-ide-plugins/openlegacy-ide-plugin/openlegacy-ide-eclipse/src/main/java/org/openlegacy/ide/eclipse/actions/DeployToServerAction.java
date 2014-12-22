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
import org.eclipse.jface.viewers.TreeSelection;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.PopupUtil;

/**
 * @author Ivan Bort
 * 
 */
public class DeployToServerAction extends AbstractAction {

	@Override
	public void run(IAction action) {
		IProject project = (IProject)((TreeSelection)getSelection()).getFirstElement();
		EclipseDesignTimeExecuter.instance().copyCodeGenerationTemplates(project);

		if (!EclipseDesignTimeExecuter.instance().isSupportDirectDeployment(project)) {
			PopupUtil.warn(Messages.getString("warn_direct_deployment_not_supported"));
			return;
		}
		DeployToServerDialog dialog = new DeployToServerDialog(getShell());
		dialog.open();
	}

}
