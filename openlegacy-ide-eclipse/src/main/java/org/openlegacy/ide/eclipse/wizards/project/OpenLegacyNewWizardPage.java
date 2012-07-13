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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
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
import org.openlegacy.ide.eclipse.Messages;

public class OpenLegacyNewWizardPage extends WizardPage {

	private Combo templateName;

	private Text projectNameTxt;
	private String projectName;

	private String[] projectTemplates = new String[] { Messages.label_dropdown_select, "openlegacy-new-java-template", "openlegacy-mvc-new",  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			"openlegacy-mvc-sample", "openlegacy-mobile-sample" }; //$NON-NLS-1$ //$NON-NLS-2$

	private String[] projectTemplatesDescriptions = new String[] { "", //$NON-NLS-1$
			Messages.info_ol_java_api_project,
			Messages.info_ol_new_web_mvc_project,
			Messages.info_ol_sample_web_mvc_project,
			Messages.info_ol_sample_mobile_mvc_project };

	private Label templateDescription;

	private Text defaultPackageTxt;

	private String defaultPackage;

	private String[] providers = new String[] { Messages.label_dropdown_select2, "tn5250j", "h3270", "applinx" };  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private String[] providersDescription = new String[] { "", //$NON-NLS-1$
			Messages.info_provider_tn5250j,
			Messages.info_provider_h3270,
			Messages.info_provider_applinx };

	private Combo providerName;

	private Label providerDescription;

	private Text hostName;
	private Text hostPort;
	private Text codePage;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public OpenLegacyNewWizardPage(ISelection selection) {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.title_ol_project_wizard);
		setDescription(Messages.info_ol_project_wizard);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.label_template);

		templateName = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		templateName.setItems(projectTemplates);
		templateName.select(2);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		templateName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(""); //$NON-NLS-1$
		templateDescription = label = new Label(container, SWT.NULL);
		templateDescription.setText(projectTemplatesDescriptions[templateName.getSelectionIndex()]);

		gd = new GridData();
		gd.widthHint = 600;
		gd.grabExcessHorizontalSpace = true;
		templateDescription.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText(Messages.label_project_name);

		projectNameTxt = new Text(container, SWT.BORDER | SWT.SINGLE);

		projectNameTxt.setText(""); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectNameTxt.setLayoutData(gd);
		projectNameTxt.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		projectNameTxt.setFocus();

		label = new Label(container, SWT.NULL);
		label.setText(Messages.label_default_project);

		defaultPackageTxt = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		defaultPackageTxt.setLayoutData(gd);
		defaultPackageTxt.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(Messages.label_provider);

		providerName = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		providerName.setItems(providers);
		providerName.select(0);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		providerName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		setControl(container);

		label = new Label(container, SWT.NULL);
		label.setText(""); //$NON-NLS-1$
		providerDescription = label = new Label(container, SWT.NULL);

		gd = new GridData();
		gd.widthHint = 600;
		gd.grabExcessHorizontalSpace = true;
		providerDescription.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText(Messages.label_host_ip);

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
		label.setText(Messages.label_host_port);

		hostPort = new Text(container, SWT.BORDER | SWT.SINGLE);

		hostPort.setText(String.valueOf(DesignTimeExecuter.DEFAULT_PORT));
		hostPort.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(Messages.label_code_page);

		codePage = new Text(container, SWT.BORDER | SWT.SINGLE);

		codePage.setText(String.valueOf(DesignTimeExecuter.DEFAULT_CODE_PAGE));
		codePage.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		setPageComplete(false);

	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {

		providerDescription.setText(providersDescription[providerName.getSelectionIndex()]);
		templateDescription.setText(projectTemplatesDescriptions[templateName.getSelectionIndex()]);

		if (templateName.getText().endsWith("sample")) { //$NON-NLS-1$
			projectName = templateName.getText();
			defaultPackage = null;
			setEnabled(false);
			this.providerName.setText(providers[1]); // AS/400
			updateStatus(null);
			return;
		} else {
			defaultPackage = defaultPackageTxt.getText();
			projectName = projectNameTxt.getText();
			setEnabled(true);
		}

		IProject[] project = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject iProject : project) {
			if (iProject.getName().equalsIgnoreCase(projectName)) {
				updateStatus(Messages.error_project_already_exists);
				return;
			}
		}

		if (templateName.getSelectionIndex() == 0) {
			updateStatus(Messages.error_template_not_specified);
			return;
		}
		if (projectName.length() == 0) {
			updateStatus(Messages.error_project_name_not_specified);
			return;
		}
		if (defaultPackageTxt.getText().length() == 0) {
			updateStatus(Messages.error_package_not_specified);
			return;
		}
		if (projectName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus(Messages.error_project_name_oinvalid);
			return;
		}
		if (providerName.getSelectionIndex() == 0) {
			updateStatus(Messages.error_provider_not_selected);
			return;
		}
		if (hostName.getText().length() == 0) {
			updateStatus(Messages.error_host_name_not_specified);
			return;
		}
		if (hostPort.getText().length() == 0) {
			updateStatus(Messages.errror_host_port_not_specified);
			return;
		}
		if (!NumberUtils.isNumber(hostPort.getText())) {
			updateStatus(Messages.error_port_not_numeric);
			return;
		}
		if (codePage.getText().length() == 0) {
			updateStatus(Messages.error_code_page_not_specified);
			return;
		}

		updateStatus(null);
	}

	private void setEnabled(boolean enabled) {
		this.projectNameTxt.setEnabled(enabled);
		this.hostName.setEnabled(enabled);
		this.codePage.setEnabled(enabled);
		this.hostPort.setEnabled(enabled);
		this.defaultPackageTxt.setEnabled(enabled);
		this.providerName.setEnabled(enabled);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getTemplateName() {
		return templateName.getText();
	}

	public String getProjectName() {
		if (projectName == null) {
			projectName = projectNameTxt.getText();
		}
		return projectName;
	}

	public String getDefaultPackageName() {
		if (defaultPackage == null) {
			defaultPackage = defaultPackageTxt.getText();
		}
		return defaultPackage;
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
}