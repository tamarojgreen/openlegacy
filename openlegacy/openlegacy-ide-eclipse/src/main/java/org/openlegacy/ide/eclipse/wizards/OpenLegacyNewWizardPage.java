package org.openlegacy.ide.eclipse.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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

public class OpenLegacyNewWizardPage extends WizardPage {

	private Combo templateName;

	private Text projectName;

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public OpenLegacyNewWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenLegacy project wizard");
		setDescription("This wizard creates a new OpenLegacy project which enables you to integrate and modernize your Legacy system");
		this.selection = selection;
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
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectName.setLayoutData(gd);
		projectName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Template:");

		templateName = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		templateName.setItems(new String[] { "openlegacy-mvc-sample" });
		templateName.select(0);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		templateName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection)selection;
			if (ssel.size() > 1) {
				return;
			}
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer) {
					container = (IContainer)obj;
				} else {
					container = ((IResource)obj).getParent();
				}
				templateName.setText(container.getFullPath().toString());
			}
		}
		projectName.setText("OpenLegacyProject1");
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
}