package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.RpcEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractRpcEntityMasterBlock extends AbstractMasterBlock {

	public AbstractRpcEntityMasterBlock(AbstractPage page) {
		super(page);
	}

	protected RpcEntity getEntity() {
		return ((RpcEntityEditor)page.getEntityEditor()).getEntity();
	}

	@Override
	public void updateLabels() {}

}
