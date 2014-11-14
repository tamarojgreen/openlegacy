package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBigIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsMasterBlockLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof RpcBooleanFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_BOOLEAN);
		} else if (element instanceof RpcBigIntegerFieldModel) {
			// BigInteger must be before Integer, because BigInteger model extends Integer model
			return Activator.getDefault().getImage(Activator.ICON_BIG_INTEGER);
		} else if (element instanceof RpcIntegerFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_INTEGER);
		} else if (element instanceof RpcPartModel) {
			return Activator.getDefault().getImage(Activator.ICON_PART);
		} else if (element instanceof RpcFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_STRING);
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof RpcFieldModel) {
			return ((RpcFieldModel)element).getFieldName();
		}
		return ((RpcPartModel)element).getClassName();
	}

}
