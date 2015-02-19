package com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBigIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcNamedObject;

public class RpcFieldConverter {

	public static RpcFieldModel getNewFieldModel(RpcFieldModel model, Class<?> toClass) {
		RpcFieldModel convertedField = null;
		if (RpcBooleanFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new RpcBooleanFieldModel((RpcNamedObject)model.getParent());
		} else if (RpcDateFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new RpcDateFieldModel((RpcNamedObject)model.getParent());
		} else if (RpcIntegerFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new RpcIntegerFieldModel((RpcNamedObject)model.getParent());
		} else if (RpcEnumFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new RpcEnumFieldModel((RpcNamedObject)model.getParent());
		} else if (RpcBigIntegerFieldModel.class.isAssignableFrom(toClass)) {
			convertedField = new RpcBigIntegerFieldModel((RpcNamedObject)model.getParent());
		} else {
			convertedField = new RpcFieldModel((RpcNamedObject)model.getParent());
		}
		return convertedField;
	}
}
