package com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
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

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsTreeViewerDropHelper {

	public static void performDrop(ScreenEntity entity, NamedObject namedObject, NamedObject target) {
		if (namedObject instanceof ScreenFieldModel) {
			ScreenFieldModel transferedField = (ScreenFieldModel)namedObject;
			ScreenFieldModel clone = getCloneField(transferedField, target);

			if (target instanceof ScreenEntityModel && (!(transferedField.getParent() instanceof ScreenEntityModel))) {
				// remove all actions for field
				entity.removeActionsSet(clone.getUUID());
				// add field to entity model
				entity.getFields().put(clone.getUUID(), getEmptyField(clone));
				entity.getSortedFields().add(clone);
				// remove field from part model
				entity.removeScreenFieldModelFromPart(transferedField);
				// generate actions
				generateModifyActions(entity, clone, target);
				generateRemoveActions(entity, transferedField);
			} else if (target instanceof ScreenPartModel) {
				// check if user tries to add field into his current parent
				if ((transferedField.getParent() instanceof ScreenPartModel)
						&& ((ScreenPartModel)transferedField.getParent()).getUUID().equals(target.getUUID())) {
					return;
				}
				// remove all actions for field
				entity.removeActionsSet(clone.getUUID());
				// add field to part model
				entity.getParts().get(clone.getParent().getUUID()).getFields().put(clone.getUUID(), getEmptyField(clone));
				entity.getParts().get(clone.getParent().getUUID()).getSortedFields().add(clone);
				// add field to sorted part model
				List<ScreenPartModel> sortedParts = entity.getSortedParts();
				for (ScreenPartModel partModel : sortedParts) {
					if (partModel.getUUID().equals(clone.getParent().getUUID())) {
						partModel.getFields().put(clone.getUUID(), getEmptyField(clone));
						partModel.getSortedFields().add(clone);
						break;
					}
				}
				// remove field from parent (entity or another part)
				if (transferedField.getParent() instanceof ScreenEntityModel) {
					entity.removeScreenFieldModel(transferedField);
				} else if (transferedField.getParent() instanceof ScreenPartModel) {
					entity.removeScreenFieldModelFromPart(transferedField);
				}
				// generate actions
				generateModifyActions(entity, clone, target);
				generateRemoveActions(entity, transferedField);
			}
		}

	}

	private static ScreenFieldModel getCloneField(ScreenFieldModel model, NamedObject target) {
		ScreenFieldModel clone = model.clone();
		if (model instanceof ScreenBooleanFieldModel) {
			clone = ((ScreenBooleanFieldModel)model).clone();
		} else if (model instanceof ScreenDateFieldModel) {
			clone = ((ScreenDateFieldModel)model).clone();
		} else if (model instanceof ScreenEnumFieldModel) {
			clone = ((ScreenEnumFieldModel)model).clone();
		} else if (model instanceof ScreenFieldValuesModel) {
			clone = ((ScreenFieldValuesModel)model).clone();
		} else if (model instanceof ScreenIntegerFieldModel) {
			clone = ((ScreenIntegerFieldModel)model).clone();
		}
		clone.setParent(target);
		return clone;
	}

	private static ScreenFieldModel getEmptyField(ScreenFieldModel model) {
		if (model instanceof ScreenBooleanFieldModel) {
			return new ScreenBooleanFieldModel(model.getUUID(), model.getParent());
		} else if (model instanceof ScreenDateFieldModel) {
			return new ScreenDateFieldModel(model.getUUID(), model.getParent());
		} else if (model instanceof ScreenEnumFieldModel) {
			return new ScreenEnumFieldModel(model.getUUID(), model.getParent());
		} else if (model instanceof ScreenFieldValuesModel) {
			return new ScreenFieldValuesModel(model.getUUID(), model.getParent());
		} else if (model instanceof ScreenIntegerFieldModel) {
			return new ScreenIntegerFieldModel(model.getUUID(), model.getParent());
		} else {
			return new ScreenFieldModel(model.getUUID(), model.getParent());
		}
	}

	private static AbstractAction getAddAction(ScreenFieldModel model) {
		if (model instanceof ScreenBooleanFieldModel) {
			return new ScreenBooleanFieldAction(model.getUUID(), (ScreenBooleanFieldModel)model, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, model.getFieldName(), null);
		} else if (model instanceof ScreenDateFieldModel) {
			return new ScreenDateFieldAction(model.getUUID(), (ScreenDateFieldModel)model, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, model.getFieldName(), null);
		} else if (model instanceof ScreenEnumFieldModel) {
			return new ScreenEnumFieldAction(model.getUUID(), (ScreenEnumFieldModel)model, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, model.getFieldName(), null);
		} else if (model instanceof ScreenFieldValuesModel) {
			return new ScreenFieldValuesAction(model.getUUID(), (ScreenFieldValuesModel)model, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, model.getFieldName(), null);
		} else if (model instanceof ScreenIntegerFieldModel) {
			return new ScreenIntegerFieldAction(model.getUUID(), (ScreenIntegerFieldModel)model, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, model.getFieldName(), null);
		} else {
			return new ScreenFieldAction(model.getUUID(), model, ActionType.ADD, ASTNode.FIELD_DECLARATION, model.getFieldName(),
					null);
		}
	}

	private static void generateModifyActions(ScreenEntity entity, ScreenFieldModel model, NamedObject target) {
		// check if field already exist in compilation unit
		if (!isFieldExistInEntity(entity, model, target)) {
			entity.addAction(getAddAction(model));
		}
		if (model instanceof ScreenBooleanFieldModel) {
			ScreenEntityUtils.ActionGenerator.generateScreenBooleanFieldActions(entity, (ScreenBooleanFieldModel)model);
		} else if (model instanceof ScreenDateFieldModel) {
			ScreenEntityUtils.ActionGenerator.generateScreenDateFieldActions(entity, (ScreenDateFieldModel)model);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, model);
		} else if (model instanceof ScreenEnumFieldModel) {
			ScreenEntityUtils.ActionGenerator.generateScreenEnumFieldActions(entity, (ScreenEnumFieldModel)model);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, model);
		} else if (model instanceof ScreenFieldValuesModel) {
			ScreenEntityUtils.ActionGenerator.generateScreenFieldValuesActions(entity, (ScreenFieldValuesModel)model);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, model);
		} else if (model instanceof ScreenIntegerFieldModel) {
			ScreenEntityUtils.ActionGenerator.generateScreenNumericFieldActions(entity, (ScreenIntegerFieldModel)model);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, model);
		} else {
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, model);
		}
		ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, model);
		entity.setDirty(true);
	}

	private static void generateRemoveActions(ScreenEntity entity, ScreenFieldModel model) {
		// try to remove action for new field
		entity.removeAction(model.getUUID(), Messages.getString("Field.new"));//$NON-NLS-1$
		entity.addAction(new ScreenFieldAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.FIELD_DECLARATION, null, null));
	}

	private static boolean isFieldExistInEntity(ScreenEntity entity, ScreenFieldModel model, NamedObject target) {
		if (target instanceof ScreenEntityModel && (entity.getInitialFields().get(model.getUUID()) != null)) {
			return true;
		} else if (target instanceof ScreenPartModel) {
			Map<UUID, ScreenPartModel> mParts = entity.getInitialParts();
			if (mParts.isEmpty()) {
				return false;
			}
			Collection<ScreenPartModel> parts = mParts.values();
			for (ScreenPartModel part : parts) {
				if (part.getFields().get(model.getUUID()) != null && part.getUUID().equals(model.getParent().getUUID())) {
					return true;
				}
			}
		}
		return false;
	}
}
