package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;

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
}
