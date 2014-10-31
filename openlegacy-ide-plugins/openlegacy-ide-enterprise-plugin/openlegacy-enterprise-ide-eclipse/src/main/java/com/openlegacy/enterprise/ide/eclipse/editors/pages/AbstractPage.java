package com.openlegacy.enterprise.ide.eclipse.editors.pages;

import com.openlegacy.enterprise.ide.eclipse.editors.AbstractEditor;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractPage extends FormPage implements IOpenLegacyPage {

	protected IManagedForm managedForm;
	private boolean isDirty = false;

	private AbstractEditor entityEditor;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public AbstractPage(AbstractEditor editor, String id, String title) {
		super(editor, id, title);
		this.entityEditor = editor;
	}

	public abstract void createFormContent();

	public abstract void refresh();

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		this.managedForm = managedForm;
		createFormContent();
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		managedForm.dirtyStateChanged();
	}

	public AbstractEditor getEntityEditor() {
		return entityEditor;
	}

}
