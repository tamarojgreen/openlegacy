package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBigIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsRpcBigIntegerFieldDetailsPage extends FieldsRpcIntegerFieldDetailsPage {

	public FieldsRpcBigIntegerFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public Class<?> getDetailsModel() {
		return RpcBigIntegerFieldModel.class;
	}

}
