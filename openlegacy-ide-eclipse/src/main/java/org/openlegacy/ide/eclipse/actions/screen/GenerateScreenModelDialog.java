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
package org.openlegacy.ide.eclipse.actions.screen;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.terminal.model.support.SimpleScreenEntityDesigntimeDefinition;
import org.openlegacy.ide.eclipse.actions.AbstractGenerateModelDialog;
import org.openlegacy.ide.eclipse.actions.CustomizeScreenEntityDialog;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

public class GenerateScreenModelDialog extends AbstractGenerateModelDialog {

	private TerminalSnapshot[] terminalSnapshots;
	protected boolean emptyModel;

	public GenerateScreenModelDialog(Shell shell, IFile file, boolean emptyModel, TerminalSnapshot... terminalSnapshots) {
		super(shell, file);
		this.emptyModel = emptyModel;
		this.terminalSnapshots = terminalSnapshots;
	}

	@Override
	protected void generate() {
		IFile trailPath = getFile();
		if (emptyModel) {
			SimpleScreenEntityDesigntimeDefinition entityDefinition = new SimpleScreenEntityDesigntimeDefinition();
			entityDefinition.setEntityName("");
			entityDefinition.setSnapshot(terminalSnapshots[0]);
			EclipseDesignTimeExecuter.instance().generateScreenEntityDefinition(trailPath, getSourceFolder(), getPackageValue(),
					this, isUseAj(), entityDefinition);
		} else {
			EclipseDesignTimeExecuter.instance().generateScreenModel(trailPath, getSourceFolder(), getPackageValue(), this,
					isUseAj(), terminalSnapshots);
		}
	}

	public boolean customizeEntity(final EntityDefinition<?> screenEntityDefinition) {

		final BooleanContainer generate = new BooleanContainer();
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				CustomizeScreenEntityDialog customizeDialog = new CustomizeScreenEntityDialog(getShell(),
						(ScreenEntityDefinition)screenEntityDefinition);
				int result = customizeDialog.open();
				if (result == Window.CANCEL) {
					generate.setBooleanValue(false);
				}
			}
		});
		boolean result = generate.getBooleanValue();

		return result;
	}

	private static class BooleanContainer {

		boolean booleanValue = true;

		public void setBooleanValue(boolean booleanValue) {
			this.booleanValue = booleanValue;
		}

		public boolean getBooleanValue() {
			return booleanValue;

		}
	}

}
