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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.components.SnapshotComposite;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.text.MessageFormat;

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

		parent.getShell().setText(MessageFormat.format(Messages.title_ol_generate_screens_api, PluginConstants.TITLE));

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		GridData gd = new GridData();
		gd.widthHint = 850;
		gd.heightHint = 480;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		Label labelPackage = new Label(parent, SWT.NULL);
		labelPackage.setText(Messages.field_entity_name);
		entityNameTxt = new Text(parent, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		entityNameTxt.setLayoutData(gd);

		entityNameTxt.setText(screenEntityDefinition.getEntityName());

		// space
		Label label = new Label(parent, SWT.NONE);
		label.setText(" "); //$NON-NLS-1$

		snapshotComposite = new SnapshotComposite(parent, screenEntityDefinition.getOriginalSnapshot());

		new Label(parent, SWT.NONE);
		return parent;
	}

	@Override
	protected void okPressed() {
		((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setEntityName(entityNameTxt.getText());
		super.okPressed();
	}
}
