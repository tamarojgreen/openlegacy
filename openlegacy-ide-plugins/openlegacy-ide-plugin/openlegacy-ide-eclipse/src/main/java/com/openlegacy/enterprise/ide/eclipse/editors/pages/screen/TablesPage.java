package com.openlegacy.enterprise.ide.eclipse.editors.pages.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.TablesMasterBlock;

import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openlegacy.ide.eclipse.Activator;

/**
 * @author Ivan Bort
 * 
 */
public class TablesPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.screen.pages.tables"; //$NON-NLS-1$

	private TablesMasterBlock tablesBlock;
	private boolean needRefreshWhenActivate = false;

	public TablesPage(ScreenEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("TablesPage.title"));//$NON-NLS-1$
		tablesBlock = new TablesMasterBlock(this);
	}

	@Override
	public void createFormContent() {
		ScrolledForm form = managedForm.getForm();
		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("TablesPage.form.title")); //$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));
		tablesBlock.createContent(managedForm);
	}

	@Override
	public void refresh() {
		if (isActive()) {
			tablesBlock.refresh();
		} else {
			needRefreshWhenActivate = true;
		}
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (needRefreshWhenActivate) {
			tablesBlock.refresh();
			needRefreshWhenActivate = false;
		}
	}

}
