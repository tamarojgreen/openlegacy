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

package com.openlegacy.enterprise.ide.eclipse.ws.generator.dialogs.jpa;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.dialogs.GenerateServiceDialog;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog.TreeViewerModel;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.designtime.enums.BackendSolution;

/**
 * @author Ivan Bort
 * 
 */
public class GenerateJpaServiceDialog extends GenerateServiceDialog {

	public GenerateJpaServiceDialog(Shell parentShell, TreeViewerModel model, IProject project, BackendSolution serviceType) {
		super(parentShell, model, project, serviceType);
	}

	@Override
	protected boolean isSupportGeneratePool() {
		return false;
	}

}
