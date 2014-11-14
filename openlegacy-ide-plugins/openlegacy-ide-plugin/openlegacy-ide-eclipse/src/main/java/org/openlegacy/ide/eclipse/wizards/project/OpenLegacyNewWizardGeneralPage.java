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
package org.openlegacy.ide.eclipse.wizards.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.newproject.model.ProjectType;
import org.openlegacy.ide.eclipse.Messages;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class OpenLegacyNewWizardGeneralPage extends WizardPage {

	private Combo templateName;
	private Text projectNameTxt;
	private Label templateDescription;
	private Text defaultPackageTxt;

	private String projectName;
	private String defaultPackage;
	private List<ProjectType> projectTypes = null;
	private Button rightTotLeftCb;
	private boolean rightTotLeft;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public OpenLegacyNewWizardGeneralPage(ISelection selection) {
		super("wizardGeneralPage"); //$NON-NLS-1$
		setTitle(Messages.getString("title_ol_project_wizard"));
		setDescription(Messages.getString("info_ol_project_wizard"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_template"));

		templateName = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		templateName.setItems(new String[] { "Pending..." });//$NON-NLS-1$
		templateName.select(0);

		label = new Label(container, SWT.NULL);
		label.setText(""); //$NON-NLS-1$
		templateDescription = label = new Label(container, SWT.NULL);
		templateDescription.setText("");//$NON-NLS-1$

		GridData gd = new GridData();
		gd.widthHint = 600;
		gd.grabExcessHorizontalSpace = true;
		templateDescription.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_project_name"));

		projectNameTxt = new Text(container, SWT.BORDER | SWT.SINGLE);

		projectNameTxt.setText(""); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectNameTxt.setLayoutData(gd);
		projectNameTxt.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		projectNameTxt.setFocus();

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_default_project"));

		defaultPackageTxt = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		defaultPackageTxt.setLayoutData(gd);
		defaultPackageTxt.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		defaultPackageTxt.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {}

			@Override
			public void focusGained(FocusEvent arg0) {
				if (defaultPackageTxt.getText().isEmpty() && !projectNameTxt.getText().isEmpty()) {
					defaultPackageTxt.setText(MessageFormat.format("com.{0}.openlegacy", projectNameTxt.getText().toLowerCase()));
				}
			}
		});

		rightTotLeftCb = new Button(container, SWT.CHECK);
		rightTotLeftCb.setText(Messages.getString("label_right_to_left"));
		gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true, 2, 2);
		gd.widthHint = 400;
		rightTotLeftCb.setLayoutData(gd);
		rightTotLeftCb.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setRightTotLeft(!isRightTotLeft());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				setRightTotLeft(true);
			}
		});
		setControl(container);
		this.setEnabled(false);
		setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
		return this.isPageComplete() && (!isDemo() || isProjectSupportTheme()) && isProviderRequired();
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {

		templateDescription.setText(projectTypes.get(templateName.getSelectionIndex()).getDescription());

		// trigger change event on next page to set enable or disable status for provider
		((OpenLegacyNewWizardProviderPage)getNextPage()).triggerChange();

		if (isDemo()) {
			projectName = templateName.getText();
			defaultPackage = null;
			setEnabled(false);
			updateStatus(null);
			return;
		} else {
			defaultPackage = defaultPackageTxt.getText();
			projectName = projectNameTxt.getText();
			setEnabled(true);
		}

		boolean isClientSide = this.projectTypes.get(templateName.getSelectionIndex()).isClientSideProject();

		defaultPackageTxt.setEnabled(!isClientSide);

		IProject[] project = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject iProject : project) {
			if (iProject.getName().equalsIgnoreCase(projectName)) {
				updateStatus(Messages.getString("error_project_already_exists"));
				return;
			}
		}

		if (projectName.length() == 0) {
			updateStatus(Messages.getString("error_project_name_not_specified"));
			return;
		}
		if (defaultPackageTxt.getText().length() == 0 && !isClientSide) {
			updateStatus(Messages.getString("error_package_not_specified"));
			return;
		}
		if (projectName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus(Messages.getString("error_project_name_oinvalid"));
			return;
		}

		updateStatus(null);
	}

	private void setEnabled(boolean enabled) {
		this.projectNameTxt.setEnabled(enabled);
		this.defaultPackageTxt.setEnabled(enabled);
		this.rightTotLeftCb.setEnabled(enabled);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getTemplateName() {
		return templateName.getText();
	}

	public boolean isRightTotLeft() {
		return rightTotLeft;
	}

	public void setRightTotLeft(boolean rightTotLeft) {
		this.rightTotLeft = rightTotLeft;
		((OpenLegacyNewProjectWizard)getWizard()).setRightTotLeft(rightTotLeft);
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

	public boolean isProjectSupportTheme() {
		if ((this.projectTypes == null) || this.projectTypes.isEmpty()) {
			return false;
		}
		return this.projectTypes.get(templateName.getSelectionIndex()).isSupportTheme();
	}

	public boolean isDemo() {
		if ((this.projectTypes == null) || this.projectTypes.isEmpty()) {
			return templateName.getText().endsWith("sample") || templateName.getText().endsWith("demo");
		}
		return this.projectTypes.get(templateName.getSelectionIndex()).isDemo();
	}

	public boolean isProviderRequired() {
		if ((this.projectTypes == null) || this.projectTypes.isEmpty()) {
			return true;
		}
		return this.projectTypes.get(templateName.getSelectionIndex()).isProviderRequired();
	}

	public String getZipFile() {
		if (this.projectTypes != null) {
			return this.projectTypes.get(templateName.getSelectionIndex()).getZipFile();
		}
		return null;
	}

	public void updateControls(List<ProjectType> list) {
		if (getControl().isDisposed()) {
			return;
		}
		if (list == null || list.isEmpty()) {
			getControl().getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					setEnabled(false);
					updateStatus(Messages.getString("error_new_project_metadata_not_found"));
				}
			});
			return;
		}

		this.projectTypes = list;
		ArrayList<String> templates = new ArrayList<String>();

		for (ProjectType projectType : list) {
			templates.add(projectType.getTemplateName());
		}
		final String[] projectTemplates = new String[templates.size()];
		templates.toArray(projectTemplates);

		getControl().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				setEnabled(true);
				templateName.setItems(projectTemplates);
				templateName.pack();
				templateName.select(1);
				templateName.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						dialogChanged();
					}
				});

				templateDescription.setText(projectTypes.get(templateName.getSelectionIndex()).getDescription());
			}
		});
	}

}