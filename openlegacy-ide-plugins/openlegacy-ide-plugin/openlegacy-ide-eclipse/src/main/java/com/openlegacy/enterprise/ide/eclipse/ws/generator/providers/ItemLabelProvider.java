package com.openlegacy.enterprise.ide.eclipse.ws.generator.providers;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaByteFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaManyToOneFieldModel;
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
import org.openlegacy.ide.eclipse.Activator;

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
		} else if (element instanceof JpaEntityModel) {
			// for Jpa Entity
			return Activator.getDefault().getImage(Activator.ICON_JPA_ENTITY);
		} else if (element instanceof JpaBooleanFieldModel) {
			boolean key = ((JpaFieldModel)element).getDefinition().isKey();
			return Activator.getDefault().getImage(key ? Activator.ICON_BOOLEAN_KEY : Activator.ICON_BOOLEAN);
		} else if (element instanceof JpaByteFieldModel) {
			boolean key = ((JpaFieldModel)element).getDefinition().isKey();
			return Activator.getDefault().getImage(key ? Activator.ICON_BYTE_KEY : Activator.ICON_BYTE);
		} else if (element instanceof JpaDateFieldModel) {
			boolean key = ((JpaFieldModel)element).getDefinition().isKey();
			return Activator.getDefault().getImage(key ? Activator.ICON_DATE_KEY : Activator.ICON_DATE);
		} else if (element instanceof JpaIntegerFieldModel) {
			boolean key = ((JpaFieldModel)element).getDefinition().isKey();
			return Activator.getDefault().getImage(key ? Activator.ICON_INTEGER_KEY : Activator.ICON_INTEGER);
		} else if (element instanceof JpaListFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_LIST);
		} else if (element instanceof JpaManyToOneFieldModel) {
			return Activator.getDefault().getImage(Activator.ICON_MANY_TO_ONE);
		} else if (element instanceof JpaFieldModel) {
			boolean key = ((JpaFieldModel)element).getDefinition().isKey();
			return Activator.getDefault().getImage(key ? Activator.ICON_STRING_KEY : Activator.ICON_STRING);
		}
		return null;
	}

}
