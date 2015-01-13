package com.openlegacy.enterprise.ide.eclipse.editors.pages;

import com.openlegacy.enterprise.ide.eclipse.editors.AbstractEditor;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractPage extends FormPage implements IOpenLegacyPage {

	protected IManagedForm managedForm;
	private boolean isDirty = false;

	private AbstractEditor entityEditor;

	private Map<String, List<IOpenLegacyPage>> subscribers = new HashMap<String, List<IOpenLegacyPage>>();

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

	@Override
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

	@Override
	public List<IOpenLegacyPage> getSubscribers(String key) {
		if (!subscribers.isEmpty()) {
			return subscribers.get(key);
		}
		return null;
	}

	@Override
	public void addSubscriber(String key, IOpenLegacyPage page) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		if (subscribers.get(key) == null) {
			subscribers.put(key, new ArrayList<IOpenLegacyPage>());
		}
		subscribers.get(key).add(page);

	}

	public void performSubscription() {
		// leave empty to allow override for children
	}

	@Override
	public void revalidatePage(String key) {}
}
