package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.JpaEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractJpaDetailsPage extends AbstractDetailsPage {

	public AbstractJpaDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	protected JpaEntity getEntity() {
		return ((JpaEntityEditor)master.getAbstractPage().getEntityEditor()).getEntity();
	}

}
