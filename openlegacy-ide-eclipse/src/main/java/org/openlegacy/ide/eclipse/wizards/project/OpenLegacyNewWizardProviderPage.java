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
package org.openlegacy.ide.eclipse.wizards.project;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.newproject.model.ProjectProvider;
import org.openlegacy.ide.eclipse.Messages;

import java.util.ArrayList;
import java.util.List;

public class OpenLegacyNewWizardProviderPage extends WizardPage {

	private Combo providerName;
	private Label providerDescription;
	private Text hostName;
	private Text hostPort;
	private Text codePage;

	private List<ProjectProvider> projectProviders = null;

	protected OpenLegacyNewWizardProviderPage(String pageName) {
		super("wizardProviderPage"); //$NON-NLS-1$
		setTitle(Messages.getString("title_ol_project_wizard"));
		setDescription(Messages.getString("info_ol_project_wizard"));
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_provider"));

		providerName = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		providerName.setItems(new String[] { "Pending..." });//$NON-NLS-1$
		providerName.select(0);

		label = new Label(container, SWT.NULL);
		label.setText(""); //$NON-NLS-1$
		providerDescription = label = new Label(container, SWT.NULL);
		providerDescription.setText("");//$NON-NLS-1$

		GridData gd = new GridData();
		gd.widthHint = 600;
		gd.grabExcessHorizontalSpace = true;
		providerDescription.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_host_ip"));

		hostName = new Text(container, SWT.BORDER | SWT.SINGLE);

		hostName.setText(""); //$NON-NLS-1$
		gd = new GridData();
		gd.widthHint = 200;
		hostName.setLayoutData(gd);

		hostName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_host_port"));

		hostPort = new Text(container, SWT.BORDER | SWT.SINGLE);

		hostPort.setText(String.valueOf(DesignTimeExecuter.DEFAULT_PORT));
		hostPort.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_code_page"));

		codePage = new Text(container, SWT.BORDER | SWT.SINGLE);

		codePage.setText(String.valueOf(DesignTimeExecuter.DEFAULT_CODE_PAGE));
		codePage.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		setControl(container);
		setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
		return this.isPageComplete() && ((OpenLegacyNewWizardGeneralPage)this.getPreviousPage()).isProjectSupportTheme();
	}

	/**
	 * Ensures that all fields are set.
	 */

	private void dialogChanged() {
		if (projectProviders == null) {
			setEnabled(false);
			updateStatus(Messages.getString("error_new_project_metadata_not_found"));
			return;
		}

		providerDescription.setText(projectProviders.get(providerName.getSelectionIndex()).getDescription());

		if (isDemo()) {
			if (!this.providerName.getText().equals(projectProviders.get(0).getDisplayName())) {
				this.providerName.setText(projectProviders.get(0).getDisplayName()); // AS/400
				setEnabled(false);
				updateStatus(null);
			}
			return;
		} else {
			setEnabled(true);
		}

		boolean isMock = projectProviders.get(providerName.getSelectionIndex()).getDisplayName().equals("mock-up");
		hostName.setEnabled(!isMock);
		codePage.setEnabled(!isMock);
		hostPort.setEnabled(!isMock);

		if (!isMock) {
			if (hostName.getText().length() == 0) {
				updateStatus(Messages.getString("error_host_name_not_specified"));
				return;
			}
			if (hostPort.getText().length() == 0) {
				updateStatus(Messages.getString("errror_host_port_not_specified"));
				return;
			}
			if (!NumberUtils.isNumber(hostPort.getText())) {
				updateStatus(Messages.getString("error_port_not_numeric"));
				return;
			}
			if (codePage.getText().length() == 0) {
				updateStatus(Messages.getString("error_code_page_not_specified"));
				return;
			}
		}

		this.updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private void setEnabled(boolean enabled) {
		this.hostName.setEnabled(enabled);
		this.codePage.setEnabled(enabled);
		this.hostPort.setEnabled(enabled);
		this.providerName.setEnabled(enabled);
	}

	private boolean isDemo() {
		return ((OpenLegacyNewWizardGeneralPage)getPreviousPage()).isDemo();
	}

	public void triggerChange() {
		dialogChanged();
	}

	public String getProvider() {
		return providerName.getText();
	}

	public String getHostName() {
		return hostName.getText();
	}

	public String getHostPort() {
		return hostPort.getText();
	}

	public String getCodePage() {
		return codePage.getText();
	}

	public void setCodePage(String codePage) {
		this.codePage.setText(codePage);
	}

	public ProjectProvider getProjectProvider() {
		if ((this.projectProviders == null) || this.projectProviders.isEmpty()) {
			return null;
		}
		return this.projectProviders.get(providerName.getSelectionIndex());
	}

	public void updateControls(List<ProjectProvider> list) {
		if (getControl().isDisposed()) {
			return;
		}
		if (list == null || list.isEmpty()) {
			getControl().getDisplay().syncExec(new Runnable() {

				public void run() {
					setEnabled(false);
					updateStatus(Messages.getString("error_new_project_metadata_not_found"));
				}
			});
			return;
		}

		this.projectProviders = list;
		ArrayList<String> names = new ArrayList<String>();

		for (ProjectProvider projectProvider : list) {
			names.add(projectProvider.getDisplayName());
		}
		final String[] providerNames = new String[names.size()];
		names.toArray(providerNames);

		getControl().getDisplay().syncExec(new Runnable() {

			public void run() {
				setEnabled(true);
				providerName.setItems(providerNames);
				providerName.pack();
				providerName.select(0);
				providerName.addModifyListener(new ModifyListener() {

					public void modifyText(ModifyEvent e) {
						dialogChanged();
					}
				});

				providerDescription.setText(projectProviders.get(providerName.getSelectionIndex()).getDescription());
			}
		});
	}
}
