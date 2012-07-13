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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.components.SnapshotComposite;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;

public class CustomizeScreenEntityDialog extends Dialog {

	private Text entityNameTxt;
	private ScreenEntityDefinition screenEntityDefinition;
	@SuppressWarnings("unused")
	private SnapshotComposite snapshotComposite;

	protected CustomizeScreenEntityDialog(Shell parentShell, ScreenEntityDefinition screenEntityDefinition) {
		super(parentShell);
		this.screenEntityDefinition = screenEntityDefinition;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent = new Composite(parent, SWT.NONE);

		parent.getShell().setText(PluginConstants.TITLE + "- Generate screens API");

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		GridData gd = new GridData();
		gd.widthHint = 850;
		gd.heightHint = 480;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		Label labelPackage = new Label(parent, SWT.NULL);
		labelPackage.setText("Entity Name:");
		entityNameTxt = new Text(parent, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		entityNameTxt.setLayoutData(gd);

		entityNameTxt.setText(screenEntityDefinition.getEntityName());

		// space
		Label label = new Label(parent, SWT.NONE);
		label.setText(" ");

		snapshotComposite = new SnapshotComposite(parent, screenEntityDefinition.getSnapshot());

		entityNameTxt.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent paramModifyEvent) {
				((SimpleScreenEntityDefinition)screenEntityDefinition).setEntityName(entityNameTxt.getText());
			}
		});

		new Label(parent, SWT.NONE);
		return parent;
	}
}
