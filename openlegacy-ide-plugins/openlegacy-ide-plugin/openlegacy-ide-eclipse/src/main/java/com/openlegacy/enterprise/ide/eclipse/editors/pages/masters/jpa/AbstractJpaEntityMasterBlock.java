package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.JpaEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractJpaEntityMasterBlock extends AbstractMasterBlock {

	public AbstractJpaEntityMasterBlock(AbstractPage page) {
		super(page);
	}

	protected JpaEntity getEntity() {
		return ((JpaEntityEditor)page.getEntityEditor()).getEntity();
	}

	@Override
	public void updateLabels() {}

}
