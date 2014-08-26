package org.openlegacy.ide.eclipse.wizards.project.organized;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.newproject.organized.NewProjectMetadataRetriever;
import org.openlegacy.ide.eclipse.Messages;

/**
 * @author Ivan Bort
 * 
 */
public class OpenLegacyWizardHostPage extends AbstractOpenLegacyWizardPage {

	protected OpenLegacyWizardHostPage() {
		super("wizardProviderPage"); //$NON-NLS-1$
		setTitle(Messages.getString("title_ol_project_wizard"));
		setDescription(Messages.getString("info_ol_project_wizard"));
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_host_type"));

		Combo hostTypeCombo = new Combo(container, SWT.SINGLE | SWT.READ_ONLY);
		hostTypeCombo.setItems(new String[] { "Pending..." });
		hostTypeCombo.select(0);

		// stub first column in grid layout
		label = new Label(container, SWT.NONE);

		Label hostTypeDescriptionLabel = new Label(container, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		hostTypeDescriptionLabel.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_host_ip"));//$NON-NLS-1$

		Text hostText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 200;
		hostText.setLayoutData(gd);

		label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_host_port"));//$NON-NLS-1$

		Text hostPortText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 40;
		hostPortText.setLayoutData(gd);
		hostPortText.setText(String.valueOf(DesignTimeExecuter.DEFAULT_PORT));

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_code_page"));

		Text codePageText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 40;
		codePageText.setLayoutData(gd);
		codePageText.setText(String.valueOf(DesignTimeExecuter.DEFAULT_CODE_PAGE));

		setControl(container);
		setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete() && getWizardModel().isProjectSupportTheme();
	}

	@Override
	public void updateControlsData(NewProjectMetadataRetriever retriever) {
		// XXX Ivan: Auto-generated method stub

	}

}
