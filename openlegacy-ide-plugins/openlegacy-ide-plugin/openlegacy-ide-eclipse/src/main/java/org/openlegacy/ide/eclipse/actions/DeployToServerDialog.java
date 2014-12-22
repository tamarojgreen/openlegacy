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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.ide.eclipse.Messages;

/**
 * @author Ivan Bort
 * 
 */
public class DeployToServerDialog extends TitleAreaDialog {

	private static final int DIALOG_HEIGHT = 170;

	private static final int DEFAULT_LABEL_WIDTH = 100;
	private static final int DEFAULT_WIDTH = 200;

	protected DeployToServerDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.getString("title_ol_deploy_to_server"));
		setMessage(Messages.getString("title_ol_deploy_to_server_message"));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.getString("btn_deploy"), true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite)super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.NONE);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginLeft = 85;
		gl.marginTop = 30;
		container.setLayout(gl);
		GridData gd = new GridData();
		gd.heightHint = DIALOG_HEIGHT;
		container.setLayoutData(gd);

		createPropertiesControls(container);

		return area;
	}

	private void createPropertiesControls(Composite container) {
		// create row for "Server name/IP"
		Label snLabel = new Label(container, SWT.NONE);
		snLabel.setText(Messages.getString("label_server_name"));
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = DEFAULT_LABEL_WIDTH;
		snLabel.setLayoutData(gd);

		CCombo snCombo = new CCombo(container, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH + 7;
		snCombo.setLayoutData(gd);

		// create row for "Server port"
		Label spLabel = new Label(container, SWT.NONE);
		spLabel.setText(Messages.getString("label_server_port"));
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = DEFAULT_LABEL_WIDTH;
		spLabel.setLayoutData(gd);

		Text spText = new Text(container, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		spText.setLayoutData(gd);

		// create row for "User"
		Label uLabel = new Label(container, SWT.NONE);
		uLabel.setText(Messages.getString("lable_user_name"));
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = DEFAULT_LABEL_WIDTH;
		uLabel.setLayoutData(gd);

		Text uText = new Text(container, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		uText.setLayoutData(gd);

		// create row for "Password"
		Label pLabel = new Label(container, SWT.NONE);
		pLabel.setText(Messages.getString("label_password"));
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = DEFAULT_LABEL_WIDTH;
		pLabel.setLayoutData(gd);

		Text pText = new Text(container, SWT.PASSWORD | SWT.BORDER);
		gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		pText.setLayoutData(gd);

	}

}
