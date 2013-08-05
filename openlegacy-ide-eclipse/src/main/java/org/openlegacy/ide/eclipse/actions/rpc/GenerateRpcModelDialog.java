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
package org.openlegacy.ide.eclipse.actions.rpc;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.EntityDefinition;
import org.openlegacy.ide.eclipse.actions.AbstractGenerateModelDialog;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;

public class GenerateRpcModelDialog extends AbstractGenerateModelDialog {

	public GenerateRpcModelDialog(Shell shell, IFile file) {
		super(shell, file);
	}

	@Override
	protected void generate() {
		EclipseDesignTimeExecuter.instance().generateRpcModel(getFile(), getSourceFolder(), getPackageValue(), this, isUseAj());
	}

	public boolean customizeEntity(EntityDefinition<?> entityDefinition) {
		return true;
	}

}
