package com.openlegacy.enterprise.ide.eclipse.editors.pages.rpc;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.RpcEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.rpc.GeneralMasterBlock;

import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.rpc.pages.generalpage"; //$NON-NLS-1$

	private GeneralMasterBlock block;

	public GeneralPage(RpcEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("rpc.general.page.title"));
		block = new GeneralMasterBlock(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage#createFormContent()
	 */
	@Override
	public void createFormContent() {
		final ScrolledForm form = managedForm.getForm();
		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("rpc.general.page.form.title")); //$NON-NLS-1$
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
		}
	}

}
