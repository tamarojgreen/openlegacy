package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.helpers;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenFieldValuesAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.screen.ScreenEntityUtils;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.screen.ScreenFieldConverter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openlegacy.ide.eclipse.util.PopupUtil;

import java.util.Iterator;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
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
			if ((model != null) && (model instanceof ScreenFieldModel)
					&& !(targetClass.getName().equals(model.getClass().getName()))) {
				ScreenFieldModel newModel = ScreenFieldConverter.getNewFieldModel((ScreenFieldModel)model, targetClass);
				// remove old field
				ScreenEntity entity = ((ScreenEntity)callback.getAbstractEntity());
				NamedObject parent = ((ScreenFieldModel)model).getParent();
				if (parent instanceof ScreenEntityModel) {
					entity.removeScreenFieldModel((ScreenFieldModel)model);
				} else if (parent instanceof ScreenPartModel) {
					entity.removeScreenFieldModelFromPart((ScreenFieldModel)model);
				}
				// remove validation markers
				callback.removeValidationMarkers(model.getUUID());

				// remove actions
				entity.removeActionsSet(model.getUUID());

				if (((ScreenFieldModel)model).isInitialized()) {
					entity.addAction(new ScreenFieldAction(model.getUUID(), (ScreenFieldModel)model, ActionType.REMOVE,
							ASTNode.FIELD_DECLARATION, Constants.FIELD_DECLARATION, null));
					if (model instanceof ScreenEnumFieldModel) {
						entity.addAction(new ScreenEnumFieldAction(model.getUUID(), (ScreenEnumFieldModel)model,
								ActionType.REMOVE, ASTNode.ENUM_DECLARATION, Constants.ENUM_DECLARATION, null));
					}
				}
				// add converted. add filled model to entity, because models from enityt will be displayed in tree viewer
				if (parent instanceof ScreenEntityModel) {
					entity.addConvertedScreenFieldModel(newModel.clone(), newModel.convertFrom((ScreenFieldModel)model));
				} else if (parent instanceof ScreenPartModel) {
					entity.addConvertedScreenFieldModelToPart(newModel.clone(), newModel.convertFrom((ScreenFieldModel)model));
				} else {
					PopupUtil.error(Messages.getString("error.convert.field.missing.parent"),
							Messages.getString("error.convert.field.title"));
					return;
				}
				// add actions
				if (ScreenBooleanFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new ScreenBooleanFieldAction(newModel.getUUID(), (ScreenBooleanFieldModel)newModel,
							ActionType.ADD, ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel, false);
					ScreenEntityUtils.ActionGenerator.generateScreenBooleanFieldActions(entity,
							(ScreenBooleanFieldModel)newModel, false);
				} else if (ScreenDateFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new ScreenDateFieldAction(newModel.getUUID(), (ScreenDateFieldModel)newModel,
							ActionType.ADD, ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel, false);
					ScreenEntityUtils.ActionGenerator.generateScreenDateFieldActions(entity, (ScreenDateFieldModel)newModel,
							false);
				} else if (ScreenIntegerFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new ScreenIntegerFieldAction(newModel.getUUID(), (ScreenIntegerFieldModel)newModel,
							ActionType.ADD, ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel, false);
				} else if (ScreenEnumFieldModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new ScreenEnumFieldAction(newModel.getUUID(), (ScreenEnumFieldModel)newModel,
							ActionType.ADD, ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					// add action that responsible for creating a new enum class
					entity.addAction(new ScreenEnumFieldAction(newModel.getUUID(), (ScreenEnumFieldModel)newModel,
							ActionType.ADD, ASTNode.ENUM_DECLARATION, Constants.ENUM_FIELD_NEW_TYPE_DECLARATION, null));
					ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel, false);
					ScreenEntityUtils.ActionGenerator.generateScreenEnumFieldActions(entity, (ScreenEnumFieldModel)newModel,
							false);
				} else if (ScreenFieldValuesModel.class.isAssignableFrom(targetClass)) {
					entity.addAction(new ScreenFieldValuesAction(newModel.getUUID(), (ScreenFieldValuesModel)newModel,
							ActionType.ADD, ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel, false);
				} else {
					entity.addAction(new ScreenFieldAction(newModel.getUUID(), newModel, ActionType.ADD,
							ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
					ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel, false);
				}
				selectUuid = newModel.getUUID();
			}
		}
		callback.reassignMasterBlockViewerInput(selectUuid);
	}

}
