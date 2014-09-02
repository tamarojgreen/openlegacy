package org.openlegacy.ide.eclipse.wizards.project.organized;

import org.eclipse.jface.wizard.WizardPage;
import org.openlegacy.designtime.newproject.organized.NewProjectMetadataRetriever;
import org.openlegacy.ide.eclipse.wizards.project.organized.model.WizardModel;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractOpenLegacyWizardPage extends WizardPage {

	protected AbstractOpenLegacyWizardPage(String pageName) {
		super(pageName);
	}

	protected WizardModel getWizardModel() {
		return ((IOpenLegacyWizard)getWizard()).getWizardModel();
	}

	public abstract void updateControlsData(NewProjectMetadataRetriever retriever);
}
