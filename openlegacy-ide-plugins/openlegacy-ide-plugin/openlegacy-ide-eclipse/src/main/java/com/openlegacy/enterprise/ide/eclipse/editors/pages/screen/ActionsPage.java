package com.openlegacy.enterprise.ide.eclipse.editors.pages.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.AbstractEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.ActionsMasterBlock;

import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.ide.eclipse.Activator;

public class ActionsPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.screen.pages.actions"; //$NON-NLS-1$

	private ActionsMasterBlock block;

	public ActionsPage(AbstractEditor editor) {
		super(editor, PAGE_ID, Messages.getString("ActionsPage.title"));//$NON-NLS-1$
		block = new ActionsMasterBlock(this);
	}

	@Override
	public void createFormContent() {
		final ScrolledForm form = managedForm.getForm();
		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("ActionsPage.form.title")); //$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));
		block.createContent(managedForm);
	}

	@Override
	public void refresh() {
		if (isActive()) {
			block.refresh();
		}
	}

	@Override
	public void performSubscription() {
		ScreenEntityEditor editor = (ScreenEntityEditor) getEntityEditor();

		GeneralPage generalPage = (GeneralPage) editor.findPage(GeneralPage.PAGE_ID);
		generalPage.addSubscriber(ScreenAnnotationConstants.COLUMNS, this);
		generalPage.addSubscriber(ScreenAnnotationConstants.ROWS, this);
	}

	@Override
	public void revalidatePage(String key) {
		refresh();
	}

}
