package com.openlegacy.enterprise.ide.eclipse.editors.pages.rpc;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.RpcEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.rpc.FieldsMasterBlock;

import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.rpc.pages.fields"; //$NON-NLS-1$

	private FieldsMasterBlock block;
	private boolean needRefreshWhenActivate = false;

	public FieldsPage(RpcEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("rpc.fields.page.title"));//$NON-NLS-1$
		block = new FieldsMasterBlock(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage#createFormContent()
	 */
	@Override
	public void createFormContent() {
		ScrolledForm form = managedForm.getForm();
		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("rpc.fields.page.form.title")); //$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));
		block.createContent(managedForm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage#refresh()
	 */
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
