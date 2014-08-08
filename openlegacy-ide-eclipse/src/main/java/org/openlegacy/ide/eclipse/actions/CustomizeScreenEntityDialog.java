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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.components.TablesComposite;
import org.openlegacy.ide.eclipse.components.screen.SnapshotComposite;
import org.openlegacy.ide.eclipse.util.PopupUtil;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.text.MessageFormat;

public class CustomizeScreenEntityDialog extends Dialog {

	private static final int DIALOG_WIDTH = 825;
	private static final int DIALOG_HEIGHT = 800;

	private Text entityNameTxt;
	private ScreenEntityDefinition screenEntityDefinition;
	private SnapshotComposite snapshotComposite;

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
		gridLayout.numColumns = 1;
		GridData gd = new GridData();
		gd.widthHint = DIALOG_WIDTH;
		gd.heightHint = DIALOG_HEIGHT;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		// entity level
		createEntityLevelControls(parent);
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalIndent = gd.horizontalIndent = 0;
		separator.setLayoutData(gd);

		// fields/identifiers level
		TablesComposite tablesComposite = new TablesComposite(parent, SWT.NONE, screenEntityDefinition);
		// image level
		snapshotComposite = new SnapshotComposite(parent, screenEntityDefinition.getOriginalSnapshot());
		snapshotComposite.setIsScalable(true);

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
		super.okPressed();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		// Set the size of the parent shell
		parent.getShell().setSize(DIALOG_WIDTH + 5, DIALOG_HEIGHT + 75);
		// Set the dialog position in the middle of the monitor
		setDialogLocation();
	}

	/**
	 * Method used to set the dialog in the centre of the monitor
	 */
	private void setDialogLocation() {
		Rectangle monitorArea = getShell().getDisplay().getPrimaryMonitor().getBounds();
		Rectangle shellArea = getShell().getBounds();
		int x = monitorArea.x + (monitorArea.width - shellArea.width) / 2;
		int y = monitorArea.y + (monitorArea.height - shellArea.height) / 2;
		getShell().setLocation(x, y);
	}

	private void createEntityLevelControls(Composite parent) {
		Label labelPackage = new Label(parent, SWT.NULL);
		labelPackage.setText(Messages.getString("field_entity_name"));
		entityNameTxt = new Text(parent, SWT.SINGLE | SWT.BORDER);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		entityNameTxt.setLayoutData(gd);
		entityNameTxt.setText(screenEntityDefinition.getEntityName());

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.marginWidth = gl.marginHeight = 0;
		gl.numColumns = 6;
		composite.setLayout(gl);
		// Label for CCombo
		Label label = new Label(composite, SWT.NONE);
		// gd = new GridData();
		// label.setLayoutData(gd);
		label.setText(Messages.getString("label_screen_type"));
		// CCombo
		CCombo combo = new CCombo(composite, SWT.FLAT | SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 210;
		combo.setLayoutData(gd);
		// XXX Ivan: add items

		// Browse button
		Button browse = new Button(composite, SWT.PUSH);
		browse.setText(Messages.getString("btn_browse"));

		Label emptylabel = new Label(composite, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 50;
		emptylabel.setLayoutData(gd);
		emptylabel.setText(" ");
		// label for Checkbox
		Label checkboxLabel = new Label(composite, SWT.NONE);
		// gd = new GridData();
		// gd.widthHint = 140;
		// checkboxLabel.setLayoutData(gd);
		checkboxLabel.setText(Messages.getString("label_is_window"));
		// checkbox
		Button checkbox = new Button(composite, SWT.CHECK);
	}

}
