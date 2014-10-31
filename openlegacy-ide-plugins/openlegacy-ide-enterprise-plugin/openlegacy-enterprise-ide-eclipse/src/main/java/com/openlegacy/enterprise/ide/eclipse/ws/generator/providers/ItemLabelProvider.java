package com.openlegacy.enterprise.ide.eclipse.ws.generator.providers;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcBigIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenValuesFieldModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Ivan Bort
 * 
 */
public class ItemLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof AbstractNamedModel) {
			return ((AbstractNamedModel)element).getName();
		}
		return "unknown";//$NON-NLS-1$
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ScreenEntityModel) {
			// for ScreenEntity
			return Activator.getDefault().getImage(Activator.ICON_SCREEN_ENTITY);
		} else if (element instanceof ScreenPartModel) {
			return Activator.getDefault().getImage(Activator.ICON_PART);
		} else if (element instanceof ScreenBooleanFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_BOOLEAN);
		} else if (element instanceof ScreenDateFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_DATE);
		} else if (element instanceof ScreenEnumFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_ENUM);
		} else if (element instanceof ScreenValuesFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_VALUES);
		} else if (element instanceof ScreenIntegerFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_INTEGER);
		} else if (element instanceof ScreenFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_STRING);
		} else if (element instanceof RpcEntityModel) {
			// for RpcEntity
			return Activator.getDefault().getImage(Activator.ICON_RPC_ENTITY);
		} else if (element instanceof RpcPartModel) {
			return Activator.getDefault().getImage(Activator.ICON_PART);
		} else if (element instanceof RpcBooleanFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_BOOLEAN);
		} else if (element instanceof RpcBigIntegerFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_BIG_INTEGER);
		} else if (element instanceof RpcIntegerFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_INTEGER);
		} else if (element instanceof RpcFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_STRING);
		}
		return null;
	}

}
