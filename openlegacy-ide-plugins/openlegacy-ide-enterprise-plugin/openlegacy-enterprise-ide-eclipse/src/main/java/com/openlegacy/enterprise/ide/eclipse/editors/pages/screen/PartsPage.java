package com.openlegacy.enterprise.ide.eclipse.editors.pages.screen;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.PartsMasterBlock;

import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * Parts page displays as Fields page. Difference is in master view, for Parts page master is TreeViewer
 * 
 * @author Ivan Bort
 * 
 */
public class PartsPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.screen.pages.partspage"; //$NON-NLS-1$

	private PartsMasterBlock block;
	private boolean needRefreshWhenActivate = false;

	public PartsPage(ScreenEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("FieldsPage.title"));
		this.block = new PartsMasterBlock(this);
	}

	@Override
	public void createFormContent() {
		ScrolledForm form = managedForm.getForm();
		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("FieldsPage.form.title")); //$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));
		block.createContent(managedForm);
	}

	@Override
	public void refresh() {
		if (isActive()) {
			block.refresh();
		} else {
			needRefreshWhenActivate = true;
		}
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (needRefreshWhenActivate) {
			block.refresh();
			needRefreshWhenActivate = false;
		}
	}

}
