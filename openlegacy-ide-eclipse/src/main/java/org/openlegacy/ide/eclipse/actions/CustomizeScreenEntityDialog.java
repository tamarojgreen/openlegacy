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
import org.openlegacy.ide.eclipse.components.TablesCompositeImpl;
import org.openlegacy.ide.eclipse.components.screen.SnapshotComposite;
import org.openlegacy.ide.eclipse.util.PopupUtil;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.text.MessageFormat;

public class CustomizeScreenEntityDialog extends Dialog {

	private Text entityNameTxt;
	private ScreenEntityDefinition screenEntityDefinition;
	private SnapshotComposite snapshotComposite;
	private TablesCompositeImpl tablesComposite;

	public CustomizeScreenEntityDialog(Shell parentShell, ScreenEntityDefinition screenEntityDefinition) {
		super(parentShell);
		this.screenEntityDefinition = screenEntityDefinition;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent = new Composite(parent, SWT.NONE);

		parent.getShell().setText(
				MessageFormat.format(Messages.getString("title_ol_generate_screens_api"), PluginConstants.TITLE));

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gd = new GridData();
		gd.widthHint = 1160;
		gd.heightHint = 500;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		tablesComposite = new TablesCompositeImpl(parent, SWT.NONE, 280, gd.heightHint);
		tablesComposite.fillTables(screenEntityDefinition.getSortedFields(),
				screenEntityDefinition.getScreenIdentification().getScreenIdentifiers());

		Composite composite = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label labelPackage = new Label(composite, SWT.NULL);
		labelPackage.setText(Messages.getString("field_entity_name"));
		entityNameTxt = new Text(composite, SWT.SINGLE | SWT.BORDER);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		entityNameTxt.setLayoutData(gd);

		entityNameTxt.setText(screenEntityDefinition.getEntityName());

		// space
		Label label = new Label(composite, SWT.NONE);
		label.setText(" "); //$NON-NLS-1$

		snapshotComposite = new SnapshotComposite(composite, screenEntityDefinition.getOriginalSnapshot());

		tablesComposite.setPaintedControl(snapshotComposite);

		return parent;
	}

	@Override
	protected void okPressed() {
		if (entityNameTxt.getText().length() == 0) {
			PopupUtil.error(Messages.getString("error_entity_name_not_specified"));
			return;
		}
		((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setEntityName(entityNameTxt.getText());
		this.tablesComposite.cleanupScreenentityDefinition(screenEntityDefinition.getFieldsDefinitions(),
				screenEntityDefinition.getScreenIdentification().getScreenIdentifiers());
		super.okPressed();
	}
}
