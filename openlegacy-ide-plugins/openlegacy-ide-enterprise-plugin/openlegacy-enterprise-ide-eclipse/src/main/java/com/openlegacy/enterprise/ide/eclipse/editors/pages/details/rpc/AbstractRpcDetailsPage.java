package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.RpcEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractRpcDetailsPage extends AbstractDetailsPage {

	public AbstractRpcDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	protected RpcEntity getEntity() {
		return ((RpcEntityEditor)master.getAbstractPage().getEntityEditor()).getEntity();
	}
}
