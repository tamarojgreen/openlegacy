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

package org.openlegacy.ide.eclipse.wizards.project.organized;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.newproject.organized.NewProjectMetadataRetriever;
import org.openlegacy.designtime.newproject.organized.model.DbType;
import org.openlegacy.ide.eclipse.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan Bort
 * 
 */
public class OpenLegacyWizardDbPage extends AbstractOpenLegacyWizardPage {

	private List<DbType> dbTypes = new ArrayList<DbType>();
	private List<String> dbDdlAuto = new ArrayList<String>();
	private Combo dbTypeCombo;
	private Text dbUrl;
	private Text dbUser;
	private Text dbPass;
	private Combo dbDdlAutoCombo;
	private Map<String, String> dbTypesUrls = new HashMap<String, String>();

	protected OpenLegacyWizardDbPage() {
		super("wizardDbPage"); //$NON-NLS-1$
		setTitle(Messages.getString("title_ol_project_wizard"));
		setDescription(Messages.getString("info_ol_project_wizard"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		// db type
		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_db_type"));
		dbTypeCombo = new Combo(container, SWT.SINGLE | SWT.READ_ONLY);
		GridData gd = new GridData();
		gd.widthHint = 100;
		dbTypeCombo.setLayoutData(gd);
		dbTypeCombo.setItems(new String[] { "Pending..." });
		dbTypeCombo.select(0);
		dbTypeCombo.addSelectionListener(getDefaultSelectionListener());

		// db url
		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_db_url"));//$NON-NLS-1$
		dbUrl = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 250;
		dbUrl.setLayoutData(gd);
		dbUrl.addModifyListener(getDefaultModifyListener());

		// db user
		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_db_user"));//$NON-NLS-1$
		dbUser = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 250;
		dbUser.setLayoutData(gd);
		dbUser.addModifyListener(getDefaultModifyListener());

		// db password
		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_db_password"));//$NON-NLS-1$
		dbPass = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 250;
		dbPass.setLayoutData(gd);
		dbPass.addModifyListener(getDefaultModifyListener());

		// ddl auto
		label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_db_ddl_auto"));
		dbDdlAutoCombo = new Combo(container, SWT.SINGLE | SWT.READ_ONLY);
		gd = new GridData();
		gd.widthHint = 100;
		dbDdlAutoCombo.setLayoutData(gd);
		dbDdlAutoCombo.setItems(new String[] { "Pending..." });
		dbDdlAutoCombo.select(0);
		dbDdlAutoCombo.addSelectionListener(getDdlAutoSelectionListener());

		setControl(container);
		setPageComplete(false);
	}

	@Override
	public void updateControlsData(NewProjectMetadataRetriever retriever) {
		// if dialog was closed before coming here
		if (getControl().isDisposed()) {
			return;
		}
		dbTypes.clear();
		dbTypes.addAll(retriever.getDbTypes());
		dbDdlAuto.clear();
		dbDdlAuto.addAll(retriever.getDdlAuto());

		final List<String> dbTypeNames = new ArrayList<String>();
		if (!dbTypes.isEmpty()) {
			for (DbType dbType : dbTypes) {
				dbTypesUrls.put(dbType.getName(), dbType.getDatabaseUrl());

			}
		}
		getControl().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				dbDdlAutoCombo.setItems(dbDdlAuto.toArray(new String[] {}));
				dbTypeCombo.setItems(dbTypesUrls.keySet().toArray(new String[] {}));
				dbTypeCombo.select(0);
				dbTypeCombo.notifyListeners(SWT.Selection, new Event());
			}
		});
		checkDbTypes();

	}

	/**
	 * Must be called only from General Page
	 */
	public void updateControlsData(String projectName) {
		if (!StringUtils.isEmpty(projectName)) {
			if (!dbTypes.isEmpty()) {
				for (DbType dbType : dbTypes) {
					if (dbType.getName().equals("H2")) {
						dbTypesUrls.put(dbType.getName(), dbType.getDatabaseUrl() + projectName);
					}
				}
			}
		}
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete() && getWizardModel().isProjectSupportTheme();
	}

	private void checkDbTypes() {
		if (!dbTypes.isEmpty()) {
			getControl().getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					updateStatus(Messages.getString("error_new_project_metadata_not_found"));
				}
			});
		}
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private SelectionListener getDefaultSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DbType dbType = getSelectedDbType();
				if (dbType != null) {
					dbUrl.setText(dbTypesUrls.get(dbType.getName()));
					int idx = dbDdlAutoCombo.indexOf(dbType.getDdlAuto());
					if (idx >= -1) {
						dbDdlAutoCombo.select(idx);
					} else {
						dbDdlAutoCombo.select(0);
					}
					dbDdlAutoCombo.notifyListeners(SWT.Selection, new Event());

				} else {
					dbUrl.setText("");
				}

				getWizardModel().update(dbType);
				validateControls();
			}

		};
	}

	private SelectionListener getDdlAutoSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getWizardModel().setDbDdlAuto(dbDdlAutoCombo.getText());
			}
		};
	}

	private ModifyListener getDefaultModifyListener() {
		return new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (validateControls()) {
					if (dbTypesUrls.containsKey(dbTypeCombo.getText())) {
						dbTypesUrls.put(dbTypeCombo.getText(), dbUrl.getText());
					}
					getWizardModel().setDbUrl(dbUrl.getText());
					getWizardModel().setDbUser(dbUser.getText());
					getWizardModel().setDbPass(dbPass.getText());
				} else {
					getWizardModel().setDbUrl("");
					getWizardModel().setDbUser("");
					getWizardModel().setDbPass("");
				}
			}
		};
	}

	private DbType getSelectedDbType() {
		for (DbType dbType : dbTypes) {
			if (StringUtils.equals(dbType.getName(), dbTypeCombo.getText())) {
				return dbType;
			}
		}
		return null;
	}

	private boolean validateControls() {
		if (StringUtils.isBlank(dbUrl.getText()) || StringUtils.equals(dbUrl.getText(), getSelectedDbType().getDatabaseUrl())) {
			updateStatus(Messages.getString("error_db_url_not_specified"));
			return false;
		}
		updateStatus(null);
		return true;
	}

}
