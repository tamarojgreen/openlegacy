package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractScreenDetailsPage extends AbstractDetailsPage {

	public AbstractScreenDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	protected ScreenEntity getEntity() {
		return ((ScreenEntityEditor)master.getAbstractPage().getEntityEditor()).getEntity();
	}

	protected ScreenEntityEditor getEntityEditor() {
		return (ScreenEntityEditor)master.getAbstractPage().getEntityEditor();
	}

	protected AbstractPage getPage() {
		return master.getAbstractPage();
	}

	public abstract ScreenNamedObject getPageScreenNamedObject();
}
