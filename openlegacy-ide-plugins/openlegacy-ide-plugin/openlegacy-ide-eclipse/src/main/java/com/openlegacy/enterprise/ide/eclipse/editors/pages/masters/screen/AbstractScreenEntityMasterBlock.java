package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractScreenEntityMasterBlock extends AbstractMasterBlock {

	public AbstractScreenEntityMasterBlock(AbstractPage page) {
		super(page);
	}

	protected ScreenEntity getEntity() {
		return ((ScreenEntityEditor)page.getEntityEditor()).getEntity();
	}

	@Override
	public void updateLabels() {}

}
