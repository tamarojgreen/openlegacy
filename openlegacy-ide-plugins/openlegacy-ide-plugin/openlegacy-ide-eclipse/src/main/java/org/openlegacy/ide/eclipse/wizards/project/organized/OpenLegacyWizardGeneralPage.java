package org.openlegacy.ide.eclipse.wizards.project.organized;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.newproject.organized.NewProjectMetadataRetriever;
import org.openlegacy.designtime.newproject.organized.model.ProjectType;
import org.openlegacy.ide.eclipse.Messages;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class OpenLegacyWizardGeneralPage extends AbstractOpenLegacyWizardPage {

	private static final String JDBC_BACKEND_SOLUTION = "JDBC";

	private Text projectNameText;
	private Text defaultPackageText;
	private Button rightToLeftButton;
	private Combo backendCombo;
	private Combo frontendCombo;
	private List<ProjectType> projectTypes = new ArrayList<ProjectType>();
	private Button newRadioButton;
	private Button demoRadioButton;

	public static final String FRONTENT_SOLUTION_REST = "REST/Mobile";
	private List<String> backendSolutions;
	private List<String> frontendSolutions;

	private Composite serviceTypeArea;

	private Button webServiceRadioButton;

	private Button restServiceRadioButton;

	protected OpenLegacyWizardGeneralPage() {
		super("wizardFirstPage");//$NON-NLS-1$
		setTitle(Messages.getString("title_ol_project_wizard"));//$NON-NLS-1$
		setDescription(Messages.getString("info_ol_project_wizard"));//$NON-NLS-1$
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		gl.verticalSpacing = 9;
		container.setLayout(gl);

		// Backend solution type
		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_backend_solution_type"));//$NON-NLS-1$

		backendCombo = new Combo(container, SWT.SINGLE | SWT.READ_ONLY);
		GridData gd = new GridData();
		gd.widthHint = 100;
		gd.horizontalSpan = 2;
		backendCombo.setLayoutData(gd);
		backendCombo.setItems(new String[] { "Pending..." });//$NON-NLS-1$
		backendCombo.select(0);
		backendCombo.addSelectionListener(getBackendSelectionListener());

		// Frontend solution type
		label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_frontend_solution_type"));//$NON-NLS-1$

		frontendCombo = new Combo(container, SWT.SINGLE | SWT.READ_ONLY);
		gd = new GridData();
		gd.widthHint = 100;
		frontendCombo.setLayoutData(gd);
		frontendCombo.setItems(new String[] { "Pending..." });//$NON-NLS-1$
		frontendCombo.select(0);
		frontendCombo.addSelectionListener(getFrontendSelectionListener());

		createServiceTypeArea(container);

		newRadioButton = new Button(container, SWT.RADIO);
		newRadioButton.setText(Messages.getString("btn_new_project"));//$NON-NLS-1$
		newRadioButton.setSelection(true);
		newRadioButton.addSelectionListener(getDefaultSelectionListener());

		demoRadioButton = new Button(container, SWT.RADIO);
		demoRadioButton.setText(Messages.getString("btn_demo_project"));
		demoRadioButton.addSelectionListener(getDefaultSelectionListener());
		gd = new GridData();
		gd.horizontalSpan = 2;
		demoRadioButton.setLayoutData(gd);

		// project name
		label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_project_name"));//$NON-NLS-1$

		projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		projectNameText.setLayoutData(gd);
		projectNameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				validatePage();
			}
		});

		// default package
		label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_default_package"));//$NON-NLS-1$

		defaultPackageText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		defaultPackageText.setLayoutData(gd);
		defaultPackageText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				validatePage();
			}
		});
		defaultPackageText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {}

			@Override
			public void focusGained(FocusEvent e) {
				if (StringUtils.isEmpty(defaultPackageText.getText()) && !StringUtils.isEmpty(projectNameText.getText())) {
					defaultPackageText.setText(MessageFormat.format("com.{0}.openlegacy", projectNameText.getText().toLowerCase()));
				}
			}
		});

		rightToLeftButton = new Button(container, SWT.CHECK);
		rightToLeftButton.setText(Messages.getString("label_right_to_left"));//$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 3;
		rightToLeftButton.setLayoutData(gd);
		rightToLeftButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getWizardModel().setRightToLeft(((Button)e.widget).getSelection());
				getWizardModel().setCodePage(
						getWizardModel().isRightToLeft() ? DesignTimeExecuter.DEFAULT_RTL_CODE_PAGE
								: DesignTimeExecuter.DEFAULT_CODE_PAGE);
				((OpenLegacyWizardHostPage)getNextPage()).updateControlsData(null, getWizardModel().getCodePage());
				updateStatus(null);
			}

		});

		setControl(container);
		setControlsEnabled(false);
		setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete() && (!getWizardModel().isDemo() || getWizardModel().isProjectSupportTheme());
	}

	@Override
	public IWizardPage getNextPage() {
		OpenLegacyWizardHostPage hostPage = (OpenLegacyWizardHostPage)getWizard().getPage("wizardProviderPage");
		OpenLegacyWizardDbPage dbPage = (OpenLegacyWizardDbPage)getWizard().getPage("wizardDbPage");
		if (StringUtils.equals(getWizardModel().getBackendSolution(), JDBC_BACKEND_SOLUTION)) {
			hostPage.setPageComplete(true);
			dbPage.setPageComplete(false);
			return dbPage;
		}
		dbPage.setPageComplete(true);
		hostPage.setPageComplete(false);
		return hostPage;
	}

	@Override
	public void updateControlsData(NewProjectMetadataRetriever retriever) {
		// if dialog was closed before coming here
		if (getControl().isDisposed()) {
			return;
		}
		backendSolutions = retriever.getBackendSolutions();
		frontendSolutions = retriever.getFrontendSolutions();
		projectTypes.clear();
		projectTypes.addAll(retriever.getProjectTypes());

		if (backendSolutions.isEmpty() || frontendSolutions.isEmpty() || projectTypes.isEmpty()) {
			getControl().getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					setControlsEnabled(false);
					updateStatus(Messages.getString("error_new_project_metadata_not_found"));
				}
			});
			return;
		}
		getControl().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				backendCombo.setItems(backendSolutions.toArray(new String[] {}));
				backendCombo.select(0);
				backendCombo.notifyListeners(SWT.Selection, new Event());

				fillFrontendCombo(frontendSolutions);
			}

		});
	}

	private void createServiceTypeArea(Composite parent) {
		serviceTypeArea = new Composite(parent, SWT.NONE);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		// gd.horizontalSpan = 2;
		serviceTypeArea.setLayoutData(gd);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.verticalSpacing = 9;
		gl.marginWidth = 0;
		gl.marginLeft = 50;
		serviceTypeArea.setLayout(gl);
		serviceTypeArea.setVisible(false);

		Label label = new Label(serviceTypeArea, SWT.NONE);
		label.setText(Messages.getString("label_service_type"));//$NON-NLS-1$

		Composite radioContainer = new Composite(serviceTypeArea, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		radioContainer.setLayoutData(gd);
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.marginLeft = 35;
		radioContainer.setLayout(rl);
		webServiceRadioButton = new Button(radioContainer, SWT.RADIO);
		webServiceRadioButton.setText(Messages.getString("btn_web_service"));
		webServiceRadioButton.setSelection(true);
		webServiceRadioButton.addSelectionListener(getServiceTypeSelectionListener());

		restServiceRadioButton = new Button(radioContainer, SWT.RADIO);
		restServiceRadioButton.setText(Messages.getString("btn_rest_service"));
		restServiceRadioButton.addSelectionListener(getServiceTypeSelectionListener());
	}

	private void fillFrontendCombo(List<String> items) {
		if (items != null && frontendCombo != null) {
			if (items.size() > 0) {
				frontendCombo.setItems(items.toArray(new String[] {}));
				frontendCombo.select(0);
				frontendCombo.notifyListeners(SWT.Selection, new Event());
			} else {
				frontendCombo.removeAll();
			}
		}
	}

	private void setControlsEnabled(boolean enabled) {
		projectNameText.setEnabled(enabled);
		defaultPackageText.setEnabled(enabled);
		rightToLeftButton.setEnabled(enabled);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private SelectionListener getDefaultSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				checkAvailableProjects(backendCombo.getText(), frontendCombo.getText());
				showHideServiceTypeArea();
				getWizardModel().update(
						getProjectType(backendCombo.getText(), frontendCombo.getText(), demoRadioButton.getSelection()));
				// validatePage method calls setPageComplete() and after that canFlipToNextPage() will be called
				validatePage();
			}

		};
	}

	private SelectionListener getServiceTypeSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				checkAvailableProjects(backendCombo.getText(), frontendCombo.getText());
				getWizardModel().setRestFulService(restServiceRadioButton.getSelection());
				// validatePage method calls setPageComplete() and after that canFlipToNextPage() will be called
				validatePage();
			}

		};
	}

	private SelectionListener getBackendSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				fillFrontendCombo(calculateFrontendSolutions(backendCombo.getText()));
				checkAvailableProjects(backendCombo.getText(), frontendCombo.getText());
				getWizardModel().update(
						getProjectType(backendCombo.getText(), frontendCombo.getText(), demoRadioButton.getSelection()));
				// validatePage method calls setPageComplete() and after that canFlipToNextPage() will be called
				validatePage();
			}

		};
	}

	private SelectionListener getFrontendSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				checkAvailableProjects(backendCombo.getText(), frontendCombo.getText());

				showHideServiceTypeArea();

				getWizardModel().update(
						getProjectType(backendCombo.getText(), frontendCombo.getText(), demoRadioButton.getSelection()));
				// validatePage method calls setPageComplete() and after that canFlipToNextPage() will be called
				validatePage();

				OpenLegacyWizardThemePage themePage = (OpenLegacyWizardThemePage)getWizard().getPage(
						OpenLegacyWizardThemePage.PAGE_ID);
				themePage.updateThemesData(frontendCombo.getText());

			}

		};
	}

	private void checkAvailableProjects(String backendSolution, String frontendSolution) {
		Assert.isTrue(!projectTypes.isEmpty(), "Retrieved metadata is empty.");
		Assert.isTrue(!StringUtils.isEmpty(backendSolution), "Backend solution is empty.");
		Assert.isTrue(!StringUtils.isEmpty(frontendSolution), "Frontend solution is empty.");

		boolean isNewAvailable = false;
		boolean isDemoAvailable = false;

		for (ProjectType projectType : projectTypes) {
			if (backendSolution.equals(projectType.getBackendSolution())
					&& frontendSolution.equals(projectType.getFrontendSolution())) {
				isNewAvailable = isNewAvailable ? true : !projectType.isDemo();
				isDemoAvailable = isDemoAvailable ? true : projectType.isDemo();
			}
		}

		if (isNewAvailable ^ isDemoAvailable) {
			newRadioButton.setSelection(isNewAvailable && !isDemoAvailable);
			demoRadioButton.setSelection(!isNewAvailable && isDemoAvailable);
		}

		newRadioButton.setEnabled(isNewAvailable);
		demoRadioButton.setEnabled(isDemoAvailable);

		if (demoRadioButton.getSelection()) {
			setControlsEnabled(false);
			updateStatus(null);
			return;
		} else if (newRadioButton.getSelection()) {
			setControlsEnabled(true);
		}

		if (!isNewAvailable && !isDemoAvailable) {
			setControlsEnabled(false);
			updateStatus(MessageFormat.format(Messages.getString("error_no_available_projects"), backendSolution,
					frontendSolution));
			getWizardModel().clear();
		} else {
			updateStatus(null);
		}
	}

	private ProjectType getProjectType(String backendSolution, String frontendSolution, boolean isDemo) {
		Assert.isTrue(!projectTypes.isEmpty(), "Retrieved metadata is empty.");
		Assert.isTrue(!StringUtils.isEmpty(backendSolution), "Backend solution is empty.");
		Assert.isTrue(!StringUtils.isEmpty(frontendSolution), "Frontend solution is empty.");

		for (ProjectType projectType : projectTypes) {
			if (backendSolution.equals(projectType.getBackendSolution())
					&& frontendSolution.equals(projectType.getFrontendSolution()) && isDemo == projectType.isDemo()) {
				return projectType;
			}
		}
		return null;
	}

	private boolean validateProjectName(String projectName) {
		IStatus status = ResourcesPlugin.getWorkspace().validateName(projectName, IResource.PROJECT);
		if (status.isOK()) {
			getWizardModel().setProjectName(projectName);
			updateStatus(null);
		} else {
			getWizardModel().setProjectName(null);
			updateStatus(status.getMessage());
		}
		IProject[] project = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject iProject : project) {
			if (iProject.getName().equalsIgnoreCase(projectName)) {
				updateStatus(Messages.getString("error_project_already_exists"));
				return false;
			}
		}
		String[] reservedNames = getReservedNames();
		for (String reservedName : reservedNames) {
			if (projectName.replace('\\', '/').toLowerCase().indexOf(reservedName) >= 0) {
				updateStatus(Messages.getString("error_project_name_reserved"));
				return false;
			}
		}
		return status.isOK();
	}

	private boolean validateDefaultPackage(String defaultPackage) {
		IStatus status = JavaConventions.validatePackageName(defaultPackage, JavaCore.VERSION_1_5, JavaCore.VERSION_1_5);
		if (status.isOK()) {
			getWizardModel().setDefaultPackageName(defaultPackage);
			updateStatus(null);
		} else {
			getWizardModel().setDefaultPackageName(null);
			updateStatus(status.getMessage());
		}
		String[] reservedNames = getReservedNames();
		for (String reservedName : reservedNames) {
			if (defaultPackage.replace('\\', '/').toLowerCase().indexOf(reservedName) >= 0) {
				updateStatus(Messages.getString("error_package_name_reserved"));
				return false;
			}
		}
		return status.isOK();
	}

	private void validatePage() {
		// NOTE: first of all, we must update next pages. In other case canFinish() will return incorrect result
		IWizardPage nextPage = getNextPage();
		if (nextPage instanceof OpenLegacyWizardHostPage) {
			((OpenLegacyWizardHostPage)getNextPage()).updateControlsData(backendCombo.getText(), null);
		}

		if (nextPage instanceof OpenLegacyWizardDbPage) {
			((OpenLegacyWizardDbPage)getNextPage()).updateControlsData(projectNameText.getText());
		}

		if (!demoRadioButton.getSelection() && validateProjectName(projectNameText.getText())) {
			validateDefaultPackage(defaultPackageText.getText());
		} else if (demoRadioButton.getSelection()) {
			updateStatus(null);
		}
	}

	private List<String> calculateFrontendSolutions(String backendSolution) {
		Assert.isTrue(!projectTypes.isEmpty(), "Retrieved metadata is empty.");
		Assert.isTrue(!StringUtils.isEmpty(backendSolution), "Backend solution is empty.");

		List<String> list = new ArrayList<String>();
		for (ProjectType projectType : projectTypes) {
			if (backendSolution.equals(projectType.getBackendSolution()) && !list.contains(projectType.getFrontendSolution())) {
				list.add(projectType.getFrontendSolution());
			}
		}
		return list.isEmpty() ? null : list;
	}

	private void showHideServiceTypeArea() {
		// display Service Type Area if frontend solution is Integration and project is new otherwise hide
		serviceTypeArea.setVisible(StringUtils.equals(frontendCombo.getText(), "Integration") && newRadioButton.getSelection());
	}
	
	private static String[] getReservedNames() {
		List<String> names = new ArrayList<String>();
		names.add("test");
		names.add("target");
		names.add("src");
		names.add("package");
		return names.toArray(new String[] {});
	}

}
