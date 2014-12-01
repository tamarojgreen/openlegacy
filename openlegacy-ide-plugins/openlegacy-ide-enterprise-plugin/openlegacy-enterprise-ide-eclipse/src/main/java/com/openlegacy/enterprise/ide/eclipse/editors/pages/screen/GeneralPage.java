package com.openlegacy.enterprise.ide.eclipse.editors.pages.screen;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.GeneralMasterBlock;

import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralPage extends AbstractPage {

	public final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.screen.pages.generalpage"; //$NON-NLS-1$

	private GeneralMasterBlock block;

	public GeneralPage(ScreenEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("GeneralPage.title"));
		block = new GeneralMasterBlock(this);
	}

	@Override
	public void createFormContent() {
		final ScrolledForm form = managedForm.getForm();
		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("GeneralPage.form.title")); //$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));
		block.createContent(managedForm);
	}

	@Override
	public void refresh() {
		if (isActive()) {
			block.refresh();
		}
	}

	public ScreenNamedObject getEditableNamedObject(Class<?> clazz) {
		return block.getNamedObjectFromDetailsPage(clazz);
	}

}
