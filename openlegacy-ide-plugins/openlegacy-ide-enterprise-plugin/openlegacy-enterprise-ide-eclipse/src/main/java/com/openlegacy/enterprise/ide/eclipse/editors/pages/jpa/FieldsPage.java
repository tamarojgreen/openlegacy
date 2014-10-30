package com.openlegacy.enterprise.ide.eclipse.editors.pages.jpa;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.AbstractEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.jpa.FieldsMasterBlock;

import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.jpa.pages.fieldspage"; //$NON-NLS-1$

	private FieldsMasterBlock block;

	public FieldsPage(AbstractEditor editor) {
		super(editor, PAGE_ID, Messages.getString("jpa.fields.page.title"));
		block = new FieldsMasterBlock(this);
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
		form.setText(Messages.getString("jpa.fields.page.form.title")); //$NON-NLS-1$
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
