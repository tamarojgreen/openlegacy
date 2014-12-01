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
			boolean isKey = ((RpcBooleanFieldModel)element).isKey();
			if (!((RpcBooleanFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BOOLEAN_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_BOOLEAN_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BOOLEAN_KEY) : Activator.getDefault().getImage(
					Activator.ICON_BOOLEAN);
		} else if (element instanceof RpcBigIntegerFieldModel) {
			// BigInteger must be before Integer, because BigInteger model extends Integer model
			boolean isKey = ((RpcBigIntegerFieldModel)element).isKey();
			if (!((RpcBigIntegerFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BIG_INTEGER_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_BIG_INTEGER_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_BIG_INTEGER_KEY) : Activator.getDefault().getImage(
					Activator.ICON_BIG_INTEGER);
		} else if (element instanceof RpcIntegerFieldModel) {
			boolean isKey = ((RpcIntegerFieldModel)element).isKey();
			if (!((RpcIntegerFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_INTEGER_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_INTEGER_ERR);
			}
			return (isKey) ? Activator.getDefault().getImage(Activator.ICON_INTEGER_KEY) : Activator.getDefault().getImage(
					Activator.ICON_INTEGER);
		} else if (element instanceof RpcPartModel) {
			return Activator.getDefault().getImage(Activator.ICON_PART);
		} else if (element instanceof RpcFieldModel) {
			boolean isKey = ((RpcFieldModel)element).isKey();
			if (!((RpcFieldModel)element).getValidationMessages().isEmpty()) {
				return (isKey) ? Activator.getDefault().getImage(Activator.ICON_STRING_KEY_ERR)
						: Activator.getDefault().getImage(Activator.ICON_STRING_ERR);
			}
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
