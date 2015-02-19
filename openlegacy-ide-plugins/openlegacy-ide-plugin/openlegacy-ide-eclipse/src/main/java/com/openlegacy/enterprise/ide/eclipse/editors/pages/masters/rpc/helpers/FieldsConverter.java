package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.rpc.helpers;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBigIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBigIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.helpers.IPartsMasterBlockCallback;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcEntityUtils;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcFieldConverter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openlegacy.ide.eclipse.util.PopupUtil;

import java.util.Iterator;
import java.util.UUID;

public class FieldsConverter {

	private IPartsMasterBlockCallback callback;

	public FieldsConverter(IPartsMasterBlockCallback callback) {
		this.callback = callback;
	}

	@SuppressWarnings("unchecked")
	public void convertTo(Class<?> targetClass) {
		IStructuredSelection selection = callback.getMasterBlockViewerSelection();
		if (selection.isEmpty()) {
			return;
		}
		UUID selectUuid = null;
		Iterator<NamedObject> iterator = selection.iterator();

		while (iterator.hasNext()) {
			NamedObject model = iterator.next();
			if ((model != null) && (model instanceof RpcFieldModel)
					&& !(targetClass.getName().equals(model.getClass().getName()))) {
				RpcFieldModel newModel = RpcFieldConverter.getNewFieldModel((RpcFieldModel)model, targetClass);

				// remove old field
				RpcEntity entity = ((RpcEntity)callback.getAbstractEntity());
				NamedObject parent = ((RpcFieldModel)model).getParent();
				if (parent instanceof RpcEntityModel) {
					entity.removeRpcFieldModel((RpcFieldModel)model);
				} else if (parent instanceof ScreenPartModel) {
					entity.removeRpcFieldModelFromPart((RpcFieldModel)model);
				}

				// remove validation markers
				callback.removeValidationMarkers(model.getUUID());

				// remove actions
				entity.removeActionsSet(model.getUUID());

				if (((RpcFieldModel)model).isInitialized()) {
					entity.addAction(new RpcFieldAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.FIELD_DECLARATION,
							Constants.FIELD_DECLARATION, null));
					if (model instanceof RpcEnumFieldModel) {
						entity.addAction(new RpcEnumFieldAction(model.getUUID(), (RpcEnumFieldModel)model, ActionType.REMOVE,
								ASTNode.ENUM_DECLARATION, Constants.ENUM_DECLARATION, null));
					}
				}

				// add converted. add filled model to entity, because models from entity will be displayed in tree viewer
				if (parent instanceof RpcEntityModel) {
					entity.addConvertedRpcFieldModel(newModel.clone(), (RpcFieldModel)newModel.convertFrom(model));
				} else if (parent instanceof RpcPartModel) {
					entity.addConvertedRpcFieldModelToPart(newModel.clone(), (RpcFieldModel)newModel.convertFrom(model));
				} else {
					PopupUtil.error(Messages.getString("error.convert.field.missing.parent"),
							Messages.getString("error.convert.field.title"));
					return;
				}

				// add actions
				if (RpcBooleanFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new RpcBooleanFieldAction(newModel.getUUID(), newModel, ActionType.ADD,
							ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
					RpcEntityUtils.ActionGenerator.generateRpcBooleanFieldActions(entity, (RpcBooleanFieldModel)newModel);
				} else if (RpcDateFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new RpcDateFieldAction(newModel.getUUID(), newModel, ActionType.ADD,
							ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
					RpcEntityUtils.ActionGenerator.generateRpcDateFieldActions(entity, (RpcDateFieldModel)newModel);
					// RpcEntityUtils.ActionGenerator.generateRpcDescriptionFieldActions(entity, newModel, false);
				} else if (RpcIntegerFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new RpcIntegerFieldAction(newModel.getUUID(), newModel, ActionType.ADD,
							ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
					// RpcEntityUtils.ActionGenerator.generateRpcDescriptionFieldActions(entity, newModel, false);
				} else if (RpcEnumFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new RpcEnumFieldAction(newModel.getUUID(), (RpcEnumFieldModel)newModel, ActionType.ADD,
							ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					// add action that responsible for creating a new enum class
					entity.addAction(new RpcEnumFieldAction(newModel.getUUID(), (RpcEnumFieldModel)newModel, ActionType.ADD,
							ASTNode.ENUM_DECLARATION, Constants.ENUM_FIELD_NEW_TYPE_DECLARATION, null));
					RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
					RpcEntityUtils.ActionGenerator.generateRpcEnumFieldActions(entity, (RpcEnumFieldModel)newModel);
					// RpcEntityUtils.ActionGenerator.generateRpcDescriptionFieldActions(entity, newModel, false);
				} else if (RpcBigIntegerFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new RpcBigIntegerFieldAction(newModel.getUUID(), newModel, ActionType.ADD,
							ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
					// RpcEntityUtils.ActionGenerator.generateRpcBigIntegerActions(entity, (RpcBigIntegerFieldModel)newModel,
					// false);
					// RpcEntityUtils.ActionGenerator.generateRpcDescriptionFieldActions(entity, newModel, false);
				} else {
					entity.addAction(new RpcFieldAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.FIELD_DECLARATION,
							newModel.getFieldName(), null));
					RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
					// RpcEntityUtils.ActionGenerator.generateRpcDescriptionFieldActions(entity, newModel, false);
				}
				selectUuid = newModel.getUUID();
			}
		}
	}
}
