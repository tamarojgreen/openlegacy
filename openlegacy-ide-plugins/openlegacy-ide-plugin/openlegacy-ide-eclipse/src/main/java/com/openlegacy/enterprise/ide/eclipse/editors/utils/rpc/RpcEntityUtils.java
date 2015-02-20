package com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcNumericFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartListAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.EnumEntryModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBigIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.openlegacy.FieldType;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.definitions.BooleanFieldTypeDefinition;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.definitions.EnumFieldTypeDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcPartDefinition;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntityUtils {

	/* ********************************************************* */
	/* Action Generator */
	/* ********************************************************* */
	public final static class ActionGenerator {

		/**
		 *
		 */
		public static void generateRpcEntityActions(RpcEntity entity, RpcEntityModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// @RpcEntity.name: default ""
			isPrevious = model.getName().equals(entity.getEntityModel().getName());
			isDefault = model.getName().isEmpty() || model.getName().equals(model.getClassName());
			PrivateMethods.addRemoveRpcEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.NAME, model.getName());
			// @RpcEntity.displayName: default ""
			isPrevious = model.getDisplayName().equals(entity.getEntityModel().getDisplayName());
			isDefault = model.getDisplayName().isEmpty() || model.getDisplayName().equals(model.getClassName());
			PrivateMethods.addRemoveRpcEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @RpcEntity.language: default Languages.UNDEFINED
			isPrevious = model.getLanguage().equals(entity.getEntityModel().getLanguage());
			isDefault = model.getLanguage().equals(Languages.UNDEFINED);
			PrivateMethods.addRemoveRpcEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.LANGUAGE, model.getLanguage());
		}

		public static void generateRpcNavigationActions(RpcEntity entity, RpcNavigationModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// add @RpcNavigation annotation
			if (StringUtils.isEmpty(entity.getNavigationModel().getCategory()) && !StringUtils.isEmpty(model.getCategory())) {
				entity.addAction(new RpcNavigationAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						RpcAnnotationConstants.RPC_NAVIGATION_ANNOTATION, null));
			}
			// remove @RpcNavigation annotation
			if (!StringUtils.isEmpty(entity.getNavigationModel().getCategory()) && StringUtils.isEmpty(model.getCategory())) {
				entity.addAction(new RpcNavigationAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						RpcAnnotationConstants.RPC_NAVIGATION_ANNOTATION, null));
			}
			if (entity.getNavigationModel().getCategory().equals(model.getCategory())) {
				// remove add/remove actions
				entity.removeAction(model.getUUID(), RpcAnnotationConstants.RPC_NAVIGATION_ANNOTATION);
			}
			// @RpcNavigation.category: default ""
			isPrevious = model.getCategory().equals(entity.getNavigationModel().getCategory());
			isDefault = StringUtils.isEmpty(model.getCategory());
			PrivateMethods.addRemoveRpcNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.CATEGORY, model.getCategory());
		}

		public static void generateRpcBooleanFieldActions(RpcEntity entity, RpcBooleanFieldModel model, boolean checkPrevious) {
			boolean isPrevious = true;
			boolean isDefault = true;
			RpcBooleanFieldModel entityModel = (RpcBooleanFieldModel)entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = (RpcBooleanFieldModel)entity.getPartByUUID(model.getParent().getUUID()).getFields().get(
						model.getUUID());
			}
			// @RpcBooleanField.trueValue: default none
			if (checkPrevious) {
				isPrevious = model.getTrueValue().equals(entityModel.getTrueValue());
			}
			isDefault = false;
			PrivateMethods.addRemoveRpcBooleanFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.TRUE_VALUE, model.getTrueValue());
			// @RpcBooleanField.falseValue: default none
			if (checkPrevious) {
				isPrevious = model.getFalseValue().equals(entityModel.getFalseValue());
			}
			isDefault = false;
			PrivateMethods.addRemoveRpcBooleanFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.FALSE_VALUE, model.getFalseValue());
			// @RpcBooleanField.treatEmptyasNull: default false
			if (checkPrevious) {
				isPrevious = model.isTreatEmptyAsNull() == entityModel.isTreatEmptyAsNull();
			}
			isDefault = !model.isTreatEmptyAsNull();
			PrivateMethods.addRemoveRpcBooleanFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.TREAT_EMPTY_AS_NULL, model.isTreatEmptyAsNull());
		}

		public static void generateRpcNumericFieldActions(RpcEntity entity, RpcIntegerFieldModel model, boolean checkPrevious) {
			boolean isPrevious = true;
			boolean isDefault = true;
			RpcIntegerFieldModel entityModel = (RpcIntegerFieldModel)entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = (RpcIntegerFieldModel)entity.getPartByUUID(model.getParent().getUUID()).getFields().get(
						model.getUUID());
			}
			// @RpcNumericField.minimumValue: default 0.0
			if (checkPrevious) {
				isPrevious = Double.compare(model.getMinimumValue(), entityModel.getMinimumValue()) == 0;
			}
			isDefault = Double.compare(model.getMinimumValue(), 0.0) == 0;
			PrivateMethods.addRemoveRpcNumericFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.MINIMUM_VALUE, model.getMinimumValue());
			// @RpcNumericField.maximumValue: default 0.0
			if (checkPrevious) {
				isPrevious = Double.compare(model.getMaximumValue(), entityModel.getMaximumValue()) == 0;
			}
			isDefault = Double.compare(model.getMaximumValue(), 0.0) == 0;
			PrivateMethods.addRemoveRpcNumericFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.MAXIMUM_VALUE, model.getMaximumValue());
			// @RpcNumericField.decimalPlaces: default 0
			if (checkPrevious) {
				isPrevious = model.getDecimalPlaces() == entityModel.getDecimalPlaces();
			}
			isDefault = model.getDecimalPlaces() == 0;
			PrivateMethods.addRemoveRpcNumericFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.DECIMAL_PLACES, model.getDecimalPlaces());
		}

		public static void generateRpcFieldActions(RpcEntity entity, RpcFieldModel model, boolean checkPrevious) {
			boolean isPrevious = true;
			boolean isDefault = true;
			RpcFieldModel entityModel = entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = entity.getPartByUUID(model.getParent().getUUID()).getFields().get(model.getUUID());
			}
			// @RpcField -> fieldName: default equals to field name from entityModel
			if (checkPrevious) {
				isPrevious = model.getFieldName().equals(entityModel.getFieldName());
			}
			isDefault = entityModel.getPreviousFieldName().equals(model.getFieldName());
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.FIELD_DECLARATION,
					Constants.FIELD_NAME, model.getFieldName());
			// @RpcField.originalName: default ""
			if (checkPrevious) {
				isPrevious = model.getOriginalName().equals(entityModel.getOriginalName());
			}
			isDefault = model.getOriginalName().isEmpty();
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.ORIGINAL_NAME, model.getOriginalName());
			// @RpcField.key: default false
			if (checkPrevious) {
				isPrevious = model.isKey() == entityModel.isKey();
			}
			isDefault = !model.isKey();
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.KEY, model.isKey());
			// @RpcField.direction: default Direction.INPUT_OUTPUT
			if (checkPrevious) {
				isPrevious = model.getDirection().equals(entityModel.getDirection());
			}
			isDefault = model.getDirection().equals(Direction.INPUT_OUTPUT);
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.DIRECTION, model.getDirection());
			// @RpcField.length: default none;
			if (checkPrevious) {
				isPrevious = model.getLength() == entityModel.getLength();
			}
			isDefault = false;
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.LENGTH, model.getLength());
			// @RpcField.fieldType: default RpcFieldTypes.General.class
			if (checkPrevious) {
				isPrevious = model.getFieldTypeName().equals(entityModel.getFieldTypeName());
			}
			isDefault = model.getFieldType().equals(FieldType.General.class);
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.FIELD_TYPE, model.getFieldType());
			// @RpcField.displayName: default none
			if (checkPrevious) {
				isPrevious = model.getDisplayName().equals(entityModel.getDisplayName());
			}
			isDefault = false;
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @RpcField.sampleValue: default ""
			if (checkPrevious) {
				isPrevious = model.getSampleValue().equals(entityModel.getSampleValue());
			}
			isDefault = model.getSampleValue().isEmpty();
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.SAMPLE_VALUE, model.getSampleValue());
			// @RpcField.helpText: default ""
			if (checkPrevious) {
				isPrevious = model.getHelpText().equals(entityModel.getHelpText());
			}
			isDefault = model.getHelpText().isEmpty();
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.HELP_TEXT, model.getHelpText());
			// @RpcField.editable: default true
			if (checkPrevious) {
				isPrevious = model.isEditable() == entityModel.isEditable();
			}
			isDefault = model.isEditable();
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.EDITABLE, model.isEditable());
			// @RpcField.defaultValue: default ""
			if (checkPrevious) {
				isPrevious = model.getDefaultValue().equals(entityModel.getDefaultValue());
			}
			isDefault = model.getDefaultValue().isEmpty();
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.DEFAULT_VALUE, model.getDefaultValue());
			// @RpcField.expression: default ""
			if (checkPrevious) {
				isPrevious = StringUtils.equals(entityModel.getExpression(), model.getExpression());
			}
			isDefault = StringUtils.isEmpty(model.getExpression());
			PrivateMethods.addRemoveRpcFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.EXPRESSION, model.getExpression());
		}

		public static void generateRpcActionsAction(RpcEntity entity, RpcActionsModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// add @RpcActions annotation
			if (entity.getActionsModel().getActions().isEmpty() && (model.getActions() != null)
					&& (!model.getActions().isEmpty())) {
				entity.addAction(new RpcActionsAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						RpcAnnotationConstants.RPC_ACTIONS_ANNOTATION, null));
			}
			// remove @RpcActions annotation
			if (!entity.getActionsModel().getActions().isEmpty()
					&& ((model.getActions() == null) || (model.getActions().isEmpty()))) {
				entity.addAction(new RpcActionsAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						RpcAnnotationConstants.RPC_ACTIONS_ANNOTATION, null));
			}
			// remove addAnnotation action if all of actions were deleted
			if ((entity.getActionsModel().getActions().isEmpty() && (model.getActions() == null || model.getActions().isEmpty()))
					|| (!entity.getActionsModel().getActions().isEmpty() && model.getActions() != null && !model.getActions().isEmpty())) {
				entity.removeAction(model.getUUID(), RpcAnnotationConstants.RPC_ACTIONS_ANNOTATION);
			}
			// @RpcActions.actions: default {}
			isPrevious = PrivateMethods.compareActionsModels(model.getActions(), entity.getActionsModel().getActions());
			isDefault = ((model.getActions() == null) || (model.getActions().isEmpty()));
			PrivateMethods.addRemoveRpcActionsAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ACTIONS, model.getActions());
		}

		public static void generateRpcPartAction(RpcEntity entity, RpcPartModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			RpcPartModel entityModel = entity.getPartByUUID(model.getUUID());
			// @RpcPart className: default none
			isPrevious = entityModel.getClassName().equals(model.getClassName());
			isDefault = false;
			PrivateMethods.addRemoveRpcPartAction(entity, model, isPrevious, isDefault, ASTNode.TYPE_DECLARATION
					| ASTNode.SIMPLE_NAME, Constants.JAVA_TYPE_NAME, model.getClassName());
			// @RpcPart.displayName: default ""
			isPrevious = entityModel.getDisplayName().equals(model.getDisplayName());
			isDefault = model.getDisplayName().isEmpty();
			PrivateMethods.addRemoveRpcPartAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @RpcPart.name: default "";
			isPrevious = entityModel.getName().equals(model.getName());
			isDefault = model.getName().isEmpty() || model.getClassName().equals(model.getName());
			PrivateMethods.addRemoveRpcPartAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.NAME, model.getName());
		}

		public static void generateRpcPartListAction(RpcEntity entity, RpcPartModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// try to find proper part
			RpcPartModel entityModel = entity.getPartByUUID(model.getUUID());
			// @RpcPartList.count: default none
			isPrevious = entityModel.getCount() == model.getCount();
			isDefault = false;
			PrivateMethods.addRemoveRpaPartListAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, RpcAnnotationConstants.COUNT, model.getCount());
		}

		public static void generateRpcPartActionsAction(RpcEntity entity, RpcActionsModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			if (!(model.getParent() instanceof RpcPartModel)) {
				return;
			}
			RpcPartModel partModel = (RpcPartModel)model.getParent();
			RpcPartModel entityModel = entity.getPartByUUID(partModel.getUUID());
			if (entityModel == null) {
				return;
			}
			// add @RpcActions annotation
			if (entityModel.getActionsModel().getActions().isEmpty() && (model.getActions() != null)
					&& (!model.getActions().isEmpty())) {
				entity.addAction(new RpcPartActionsAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						RpcAnnotationConstants.RPC_ACTIONS_ANNOTATION, null));
			}
			// remove @RpcActions annotation
			if (!entityModel.getActionsModel().getActions().isEmpty()
					&& ((model.getActions() == null) || (model.getActions().isEmpty()))) {
				entity.addAction(new RpcPartActionsAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						RpcAnnotationConstants.RPC_ACTIONS_ANNOTATION, null));
			}
			// remove addAnnotation action if all of actions were deleted
			if (entityModel.getActionsModel().getActions().isEmpty() && model.getActions().isEmpty()) {
				entity.removeAction(model.getUUID(), RpcAnnotationConstants.RPC_ACTIONS_ANNOTATION);
			}
			// @RpcActions.actions: default {}
			isPrevious = PrivateMethods.compareActionsModels(model.getActions(), entityModel.getActionsModel().getActions());
			isDefault = ((model.getActions() == null) || (model.getActions().isEmpty()));
			PrivateMethods.addRemoveRpcPartActionsAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ACTIONS, model.getActions());
		}

		public static void generateRpcDateFieldActions(RpcEntity entity, RpcDateFieldModel model, boolean checkPrevious) {
			boolean isPrevious = true;
			boolean isDefault = true;
			RpcDateFieldModel entityModel = (RpcDateFieldModel)entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = (RpcDateFieldModel)entity.getPartByUUID(model.getParent().getUUID()).getFields().get(
						model.getUUID());
			}
			// @RpcDateField.pattern: default none;
			if (checkPrevious) {
				isPrevious = StringUtils.equals(entityModel.getPattern(), model.getPattern());
			}
			isDefault = false;
			PrivateMethods.addRemoveRpcDateFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.PATTERN, model.getPattern());
		}

		public static void generateRpcEnumFieldActions(RpcEntity entity, RpcEnumFieldModel model, boolean checkPrevious) {
			boolean isPrevious = false;
			boolean isDefault = true;
			RpcEnumFieldModel entityModel = (RpcEnumFieldModel)entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = (RpcEnumFieldModel)entity.getParts().get(model.getParent().getUUID()).getFields().get(
						model.getUUID());
			}
			// @RpcField java type: default none
			if (checkPrevious) {
				isPrevious = model.getJavaTypeName().equals(entityModel.getJavaTypeName());
			}
			isDefault = false;
			PrivateMethods.addRemoveRpcEnumFieldAction(entity, model, isPrevious, isDefault, ASTNode.FIELD_DECLARATION
					| ASTNode.SIMPLE_TYPE, Constants.JAVA_TYPE, model.getType());
			// Enum properties: default none;
			if (checkPrevious) {
				isPrevious = PrivateMethods.compareEnumEntries(model.getEntries(), entityModel.getEntries());
			}
			isDefault = false;
			PrivateMethods.addRemoveRpcEnumFieldAction(entity, model, isPrevious, isDefault, ASTNode.ENUM_CONSTANT_DECLARATION,
					Constants.ENUM_FIELD_ENTRIES, model.getEntries());
		}
	}

	private final static class PrivateMethods {

		private static void addRemoveRpcEntityAction(RpcEntity entity, RpcEntityModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcEntityAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcEntityAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcNavigationAction(RpcEntity entity, RpcNavigationModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcNavigationAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcNavigationAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcBooleanFieldAction(RpcEntity entity, RpcBooleanFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcBooleanFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcBooleanFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcNumericFieldAction(RpcEntity entity, RpcIntegerFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcNumericFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcNumericFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcFieldAction(RpcEntity entity, RpcFieldModel model, boolean isPrevious, boolean isDefault,
				int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcActionsAction(RpcEntity entity, RpcActionsModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcActionsAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcActionsAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcPartAction(RpcEntity entity, RpcPartModel model, boolean isPrevious, boolean isDefault,
				int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcPartAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcPartAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpaPartListAction(RpcEntity entity, RpcPartModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcPartListAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcPartListAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcPartActionsAction(RpcEntity entity, RpcActionsModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcPartActionsAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcPartActionsAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcDateFieldAction(RpcEntity entity, RpcDateFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcDateFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcDateFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveRpcEnumFieldAction(RpcEntity entity, RpcEnumFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new RpcEnumFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new RpcEnumFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static boolean compareActionsModels(List<ActionModel> a, List<ActionModel> b) {
			if (a.size() != b.size()) {
				return false;
			}
			for (int i = 0; i < a.size(); i++) {
				ActionModel aClass = a.get(i);
				ActionModel bClass = b.get(i);
				if (!aClass.getActionName().equals(bClass.getActionName()) || !aClass.getAlias().equals(bClass.getAlias())
						|| !aClass.getDisplayName().equals(bClass.getDisplayName())
						|| !aClass.getProgramPath().equals(bClass.getProgramPath()) || (aClass.isGlobal() != bClass.isGlobal())
						|| !StringUtils.equals(aClass.getTargetEntityClassName(), bClass.getTargetEntityClassName())) {
					return false;
				}
			}
			return true;
		}

		private static boolean compareEnumEntries(List<EnumEntryModel> a, List<EnumEntryModel> b) {
			if (a.size() != b.size()) {
				return false;
			}
			for (int i = 0; i < a.size(); i++) {
				EnumEntryModel aClass = a.get(i);
				EnumEntryModel bClass = b.get(i);
				if (!aClass.getName().equals(bClass.getName()) || (!aClass.getValue().equals(bClass.getValue()))
						|| (!aClass.getDisplayName().equals(bClass.getDisplayName()))) {
					return false;
				}
			}
			return true;
		}
	}

	public static RpcEntityModel getRpcEntityModel(RpcEntityDefinition entityDefinition) {
		RpcEntityModel model = new RpcEntityModel();
		model.init((CodeBasedRpcEntityDefinition)entityDefinition);
		return model;
	}

	public static RpcNavigationModel getRpcNavigationModel(RpcEntityDefinition entityDefinition) {
		RpcNavigationModel model = new RpcNavigationModel();
		model.init((CodeBasedRpcEntityDefinition)entityDefinition);
		return model;
	}

	public static Map<UUID, RpcFieldModel> getRpcFieldsModels(RpcNamedObject parent,
			Map<String, RpcFieldDefinition> fieldsDefinitions, List<RpcFieldModel> sortedFields) {
		Map<UUID, RpcFieldModel> map = new HashMap<UUID, RpcFieldModel>();
		if (fieldsDefinitions == null) {
			return map;
		}
		sortedFields.clear();
		Set<String> keySet = fieldsDefinitions.keySet();
		for (String key : keySet) {
			RpcFieldModel model = null;
			RpcFieldDefinition fieldDefinition = fieldsDefinitions.get(key);
			if (fieldDefinition.getFieldTypeDefinition() instanceof BooleanFieldTypeDefinition) {
				model = new RpcBooleanFieldModel(parent);
			} else if (fieldDefinition.getFieldTypeDefinition() instanceof DateFieldTypeDefinition) {
				model = new RpcDateFieldModel(parent);
			} else if (fieldDefinition.getFieldTypeDefinition() instanceof EnumFieldTypeDefinition) {
				model = new RpcEnumFieldModel(parent);
			} else {
				if (((SimpleRpcFieldDefinition)fieldDefinition).getJavaTypeName().equalsIgnoreCase(Integer.class.getSimpleName())) {
					model = new RpcIntegerFieldModel(parent);
				} else if (((SimpleRpcFieldDefinition)fieldDefinition).getJavaTypeName().equalsIgnoreCase(
						BigInteger.class.getSimpleName())) {
					model = new RpcBigIntegerFieldModel(parent);
				} else {
					// string field by default
					model = new RpcFieldModel(parent);
				}
			}
			if (model != null) {
				model.init(fieldDefinition);
				map.put(model.getUUID(), model);
				sortedFields.add(model.clone());
			}
		}
		return map;
	}

	public static <T extends PartEntityDefinition<?>> Map<UUID, RpcPartModel> getRpcPartsModels(RpcNamedObject parent,
			Map<String, T> partsDefinitions, List<RpcPartModel> sortedParts) {

		Map<UUID, RpcPartModel> map = new HashMap<UUID, RpcPartModel>();
		Collection<T> values = partsDefinitions.values();
		sortedParts.clear();
		for (T part : values) {
			if (part instanceof CodeBasedRpcPartDefinition) {
				RpcPartModel model = new RpcPartModel(parent);
				model.init((CodeBasedRpcPartDefinition)part);
				map.put(model.getUUID(), model);
				sortedParts.add(model.clone());
			}
		}
		return map;
	}

	public static RpcActionsModel getRpcActionsModel(RpcEntityDefinition entityDefinition) {
		RpcActionsModel model = new RpcActionsModel();
		model.init((CodeBasedRpcEntityDefinition)entityDefinition);
		return model;
	}

	/**
	 * Returns list of actions that equal to one of action type and target
	 * 
	 * @param entity
	 * @param target
	 *            (e.g. ASTNode.NORMAL_ANNOTATION)
	 * @param actionTypes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getActionList(RpcEntity entity, int target, ActionType[] actionTypes) {
		List<T> list = new ArrayList<T>();
		Map<UUID, Map<String, AbstractAction>> actions = entity.getActions();
		if (actions.isEmpty()) {
			return list;
		}
		Collection<Map<String, AbstractAction>> values = actions.values();
		for (Map<String, AbstractAction> map : values) {
			if (!map.isEmpty()) {
				Collection<AbstractAction> values2 = map.values();
				for (AbstractAction action : values2) {
					for (ActionType actionType : actionTypes) {
						if (action.getActionType().equals(actionType) && (action.getTarget() == target)) {
							list.add((T)action);
							break;
						}
					}
				}
			}
		}
		return (List<T>)RpcEntityActionsSorter.INSTANCE.sort((List<AbstractAction>)list);
	}

	/**
	 * Returns list of actions that equal to <b>actionClass</b>
	 * 
	 * @param entity
	 * @param actionClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getActionList(RpcEntity entity, Class<T> actionClass) {
		List<T> list = new ArrayList<T>();
		Map<UUID, Map<String, AbstractAction>> actions = entity.getActions();
		if (actions.isEmpty()) {
			return list;
		}
		Collection<Map<String, AbstractAction>> values = actions.values();
		for (Map<String, AbstractAction> map : values) {
			if (!map.isEmpty()) {
				Collection<AbstractAction> values2 = map.values();
				for (AbstractAction action : values2) {
					if (actionClass.isAssignableFrom(action.getClass())) {
						list.add((T)action);
					}
				}
			}
		}
		return (List<T>)RpcEntityActionsSorter.INSTANCE.sort((List<AbstractAction>)list);
	}

	public static void reorderFieldsAndParts(RpcEntity entity) {
		int treeLevel = 0;
		int treeBranch = 0;
		int branchCount = 0;
		List<RpcFieldModel> fields = entity.getSortedFields();
		List<RpcPartModel> parts = entity.getSortedParts();
		for (RpcFieldModel field : fields) {
			field.setTreeLevel(++treeLevel);
			branchCount++;
			field.setTreeBranch(treeBranch * 10 + branchCount);
		}
		for (RpcPartModel part : parts) {
			part.setTreeLevel(++treeLevel);
			branchCount++;
			part.setTreeBranch(treeBranch * 10 + branchCount);
			reorderPart(part);
		}
		entity.getEntityModel().setInnerBranchesCount(branchCount);
	}

	private static void reorderPart(RpcPartModel model) {
		model.setInnerBranchesCount(0);
		List<RpcFieldModel> fields = model.getSortedFields();
		List<RpcPartModel> parts = model.getSortedParts();
		for (RpcFieldModel field : fields) {
			field.setTreeLevel(model.getTreeLevel() + 1);
			model.incrementInnerBranchesCount();
			field.setTreeBranch(model.getTreeBranch() * 10 + model.getInnerBranchesCount());
		}
		for (RpcPartModel part : parts) {
			part.setTreeLevel(model.getTreeLevel() + 1);
			model.incrementInnerBranchesCount();
			part.setTreeBranch(model.getTreeBranch() * 10 + model.getInnerBranchesCount());
			reorderPart(part);
		}
	}
}
