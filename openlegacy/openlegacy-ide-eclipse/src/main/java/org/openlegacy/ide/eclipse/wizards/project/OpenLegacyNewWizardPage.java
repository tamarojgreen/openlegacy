package org.openlegacy.ide.eclipse.wizards.project;

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

public class OpenLegacyNewWizardPage extends WizardPage {

	private Combo templateName;

	private Text projectName;

	private String[] projectTemplates = new String[] { "openlegacy-new-java-template", "openlegacy-mvc-new",
			"openlegacy-mvc-sample" };

	private Text defaultPackageName;

	private String[] providers = new String[] { "openlegacy-tn5250j", "openlegacy-h3270", "openlegacy-applinx",
			DesignTimeExecuter.MOCK_PROVIDER };

	private Combo providerName;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public OpenLegacyNewWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenLegacy project wizard");
		setDescription("This wizard creates a new OpenLegacy project which enables you to integrate and modernize your Legacy system");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText("&Project name:");

		projectName = new Text(container, SWT.BORDER | SWT.SINGLE);
		projectName.setText("OpenLegacyProject1");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectName.setLayoutData(gd);
		projectName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Default package:");

		defaultPackageName = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		defaultPackageName.setLayoutData(gd);
		defaultPackageName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Template:");

		templateName = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		templateName.setItems(projectTemplates);
		templateName.select(0);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		templateName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Provider:");

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
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		String projectName = getProjectName();

		if (getTemplateName().length() == 0) {
			updateStatus("Template must be specified");
			return;
		}
		if (projectName.length() == 0) {
			updateStatus("Project name must be specified");
			return;
		}
		if (defaultPackageName.getText().length() == 0) {
			updateStatus("Default package must be specified");
			return;
		}
		if (projectName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("Project name must be valid");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getTemplateName() {
		return templateName.getText();
	}

	public String getProjectName() {
		return projectName.getText();
	}

	public String getDefaultPackageName() {
		return defaultPackageName.getText();
	}

	public String getProvider() {
		return providerName.getText();
	}
}