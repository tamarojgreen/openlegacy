package com.openlegacy.enterprise.ide.eclipse.editors.utils.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.PartPositionAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenDescriptionFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenFieldValuesAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenIdentifiersAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenPartRemoveAspectAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.TableActionAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.EnumEntryModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ChildEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.IdentifierModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.PartPositionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDescriptionFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIdentifiersModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.holders.screen.ScreenEntityRole;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.openlegacy.EntityDefinition;
import org.openlegacy.FieldType;
import org.openlegacy.definitions.BooleanFieldTypeDefinition;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.definitions.EnumFieldTypeDefinition;
import org.openlegacy.definitions.FieldWithValuesTypeDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenTableDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenEntityType;
import org.openlegacy.terminal.ScreenRecordsProvider;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions.EnterDrilldownAction;
import org.openlegacy.terminal.table.ScreenTableCollector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenEntityUtils {

	/* ********************************************************* */
	/* Action Generator */
	/* ********************************************************* */
	public final static class ActionGenerator {

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenActionsActions(ScreenEntity entity, ScreenActionsModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// add @ScreenActions annotation
			if (entity.getActionsModel().getActions().isEmpty() && (model.getActions() != null)
					&& (!model.getActions().isEmpty())) {
				entity.addAction(new ScreenActionsAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						ScreenAnnotationConstants.SCREEN_ACTIONS_ANNOTATION, null));
			}
			// remove @ScreenActions annotation
			if (!entity.getActionsModel().getActions().isEmpty()
					&& ((model.getActions() == null) || (model.getActions().isEmpty()))) {
				entity.addAction(new ScreenActionsAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						ScreenAnnotationConstants.SCREEN_ACTIONS_ANNOTATION, null));
			}
			// remove addAnnotation action if all of actions were deleted
			if ((entity.getActionsModel().getActions().isEmpty() && (model.getActions() == null || model.getActions().isEmpty()))
					|| (!entity.getActionsModel().getActions().isEmpty() && model.getActions() != null && !model.getActions().isEmpty())) {
				entity.removeAction(model.getUUID(), ScreenAnnotationConstants.SCREEN_ACTIONS_ANNOTATION);
			}
			// @ScreenActions.actions: default {}
			isPrevious = PrivateMethods.compareActionsModels(model.getActions(), entity.getActionsModel().getActions());
			isDefault = ((model.getActions() == null) || (model.getActions().isEmpty()));
			PrivateMethods.addRemoveScreenActionsAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ACTIONS, model.getActions());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenBooleanFieldActions(ScreenEntity entity, ScreenBooleanFieldModel model,
				boolean checkPrevious) {
			boolean isPrevious = false;
			boolean isDefault = true;
			ScreenBooleanFieldModel entityModel = (ScreenBooleanFieldModel)entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = (ScreenBooleanFieldModel)entity.getParts().get(model.getParent().getUUID()).getFields().get(
						model.getUUID());
			}
			// @ScreenBooleanField.trueValue: default none
			if (checkPrevious) {
				isPrevious = model.getTrueValue().equals(entityModel.getTrueValue());
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenBooleanFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.TRUE_VALUE, model.getTrueValue());
			// @ScreenBooleanField.falseValue: default none
			if (checkPrevious) {
				isPrevious = model.getFalseValue().equals(entityModel.getFalseValue());
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenBooleanFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.FALSE_VALUE, model.getFalseValue());
			// @ScreenBooleanField.treatEmptyasNull: default false
			if (checkPrevious) {
				isPrevious = model.isTreatEmptyAsNull() == entityModel.isTreatEmptyAsNull();
			}
			isDefault = !model.isTreatEmptyAsNull();
			PrivateMethods.addRemoveScreenBooleanFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.TREAT_EMPTY_AS_NULL, model.isTreatEmptyAsNull());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenColumnActions(ScreenEntity entity, ScreenColumnModel model, boolean checkPrevious) {
			boolean isPrevious = false;
			boolean isDefault = true;
			ScreenColumnModel entityModel = entity.getTables().get(model.getParent().getUUID()).getColumns().get(model.getUUID());
			// @ScreenColumn -> fieldName: default none
			if (checkPrevious) {
				isPrevious = model.getFieldName().equals(entityModel.getFieldName());
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.FIELD_DECLARATION
					| ASTNode.MEMBER_VALUE_PAIR, Constants.FIELD_NAME, model.getFieldName());
			// @ScreenColumn.key: default false;
			if (checkPrevious) {
				isPrevious = entityModel.isKey() == model.isKey();
			}
			isDefault = !model.isKey();
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.KEY, model.isKey());
			// @ScreenColumn.selectionField: default false;
			if (checkPrevious) {
				isPrevious = entityModel.isSelectionField() == model.isSelectionField();
			}
			isDefault = !model.isSelectionField();
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SELECTION_FIELD, model.isSelectionField());
			// @ScreenColumn.mainDisplayField: default false;
			if (checkPrevious) {
				isPrevious = entityModel.isMainDisplayField() == model.isMainDisplayField();
			}
			isDefault = !model.isMainDisplayField();
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.MAIN_DISPLAY_FIELD, model.isMainDisplayField());
			// @ScreenColumn.startColumn: default none;
			if (checkPrevious) {
				isPrevious = entityModel.getStartColumn() == model.getStartColumn();
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.START_COLUMN, model.getStartColumn());
			// @ScreenColumn.endColumn: default none;
			if (checkPrevious) {
				isPrevious = entityModel.getEndColumn() == model.getEndColumn();
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.END_COLUMN, model.getEndColumn());
			// @ScreenColumn.editable: default false;
			if (checkPrevious) {
				isPrevious = entityModel.isEditable() == model.isEditable();
			}
			isDefault = !model.isEditable();
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.EDITABLE, model.isEditable());
			// @ScreenColumn.displayName: default null;
			if (checkPrevious) {
				isPrevious = entityModel.getDisplayName().equals(model.getDisplayName());
			}
			isDefault = model.getDisplayName() == null;
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @ScreenColumn.sampleValue: default "";
			if (checkPrevious) {
				isPrevious = entityModel.getSampleValue().equals(model.getSampleValue());
			}
			isDefault = model.getSampleValue().isEmpty();
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.SAMPLE_VALUE, model.getSampleValue());
			// @ScreenColumn.defaulValue: default "";
			if (checkPrevious) {
				isPrevious = entityModel.getDefaultValue().equals(model.getDefaultValue());
			}
			isDefault = model.getDefaultValue().isEmpty();
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DEFAULT_VALUE, model.getDefaultValue());
			// @ScreenColumn.rowsOffset: default 0;
			if (checkPrevious) {
				isPrevious = entityModel.getRowsOffset() == model.getRowsOffset();
			}
			isDefault = model.getRowsOffset() == 0;
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ROWS_OFFSET, model.getRowsOffset());
			// @ScreenColumn.helpText: default ""
			if (checkPrevious) {
				isPrevious = entityModel.getHelpText().equals(model.getHelpText());
			}
			isDefault = model.getHelpText().isEmpty();
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.HELP_TEXT, model.getHelpText());
			// @ScreenColumn.colSpan: default 1
			if (checkPrevious) {
				isPrevious = entityModel.getColSpan() == model.getColSpan();
			}
			isDefault = model.getColSpan() == 1;
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.COL_SPAN, model.getColSpan());
			// @ScreenColumn.sortIndex: default -1
			if (checkPrevious) {
				isPrevious = entityModel.getSortIndex() == model.getSortIndex();
			}
			isDefault = model.getSortIndex() == -1;
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SORT_INDEX, model.getSortIndex());
			// @ScreenColumn.attribute: default FieldAttributeType.Value
			if (checkPrevious) {
				isPrevious = model.getAttribute().equals(entityModel.getAttribute());
			}
			isDefault = model.getAttribute().equals(FieldAttributeType.Value);
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ATTRIBUTE, model.getAttribute());
			// @ScreenColumn.targetEntity: default org.openlegacy.terminal.ScreenEntity.NONE.class
			if (checkPrevious) {
				isPrevious = entityModel.getTargetEntityClassName().equals(model.getTargetEntityClassName());
			}
			isDefault = model.getTargetEntity().equals(org.openlegacy.terminal.ScreenEntity.NONE.class);
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.TARGET_ENTITY, model.getTargetEntity());
			// @ScreenColumn.expression: default ""
			if (checkPrevious) {
				isPrevious = StringUtils.equals(entityModel.getExpression(), model.getExpression());
			}
			isDefault = StringUtils.isEmpty(model.getExpression());
			PrivateMethods.addRemoveScreenColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.EXPRESSION, model.getExpression());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenDateFieldActions(ScreenEntity entity, ScreenDateFieldModel model, boolean checkPrevious) {
			boolean isPrevious = false;
			boolean isDefault = true;
			ScreenDateFieldModel entityModel = (ScreenDateFieldModel)entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = (ScreenDateFieldModel)entity.getParts().get(model.getParent().getUUID()).getFields().get(
						model.getUUID());
			}
			// @ScreenDateField.yearColumn: default 0
			if (checkPrevious) {
				isPrevious = (entityModel.getYear() == model.getYear());
			}
			isDefault = model.getYear() == null || model.getYear().equals(0);
			PrivateMethods.addRemoveScreenDateFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.YEAR_COLUMN, model.getYear());
			// @ScreenDateField.monthColumn: default 0
			if (checkPrevious) {
				isPrevious = entityModel.getMonth() == model.getMonth();
			}
			isDefault = model.getMonth() == null || model.getMonth().equals(0);
			PrivateMethods.addRemoveScreenDateFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.MONTH_COLUMN, model.getMonth());
			// @ScreenDateField.dayColumn: default 0
			if (checkPrevious) {
				isPrevious = entityModel.getDay() == model.getDay();
			}
			isDefault = model.getDay() == null || model.getDay().equals(0);
			PrivateMethods.addRemoveScreenDateFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DAY_COLUMN, model.getDay());
			// @ScreenDateField.pattern: default ""
			if (checkPrevious) {
				isPrevious = entityModel.getPattern().equals(model.getPattern());
			}
			isDefault = model.getPattern().isEmpty();
			PrivateMethods.addRemoveScreenDateFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.PATTERN, model.getPattern());
		}

		public static void generateScreenEntityActions(ScreenEntity entity, ScreenEntityModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// @ScreenEntity.child: default false
			isPrevious = model.isChild() == entity.getEntityModel().isChild();
			isDefault = !model.isChild();
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.CHILD, model.isChild());
			// @ScreenEntity.supportTerminalData: default false
			isPrevious = model.isSupportTerminalData() == entity.getEntityModel().isSupportTerminalData();
			isDefault = !model.isSupportTerminalData();
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA, model.isSupportTerminalData());
			// @ScreenEntity.window: default false
			isPrevious = model.isWindow() == entity.getEntityModel().isWindow();
			isDefault = !model.isWindow();
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.WINDOW, model.isWindow());
			// @ScreenEntity.name: default ""
			isPrevious = model.getName().equals(entity.getEntityModel().getName());
			isDefault = model.getName().isEmpty() || model.getName().equals(model.getClassName());
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.NAME, model.getName());
			// @ScreenEntity.displayName: default ""
			isPrevious = model.getDisplayName().equals(entity.getEntityModel().getDisplayName());
			isDefault = model.getDisplayName().isEmpty() || model.getDisplayName().equals(model.getClassName());
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @ScreenEntity.rows: default ScreenSize.DEFAULT_ROWS
			isPrevious = model.getRows() == entity.getEntityModel().getRows();
			isDefault = model.getRows() == ScreenSize.DEFAULT_ROWS;
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ROWS, model.getRows());
			// @ScreenEntity.columns: default ScreenSize.DEFAULT_COLUMN
			isPrevious = model.getColumns() == entity.getEntityModel().getColumns();
			isDefault = model.getColumns() == ScreenSize.DEFAULT_COLUMN;
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.COLUMNS, model.getColumns());
			// @ScreenEntity.screenType: default ScreenEntityType.General.class
			isPrevious = model.getScreenType().equals(entity.getEntityModel().getScreenType());
			isDefault = model.getScreenType().equals(ScreenEntityType.General.class);
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SCREEN_TYPE, model.getScreenType());
			// @ScreenEntity.validateKeys: default true
			isPrevious = model.isValidateKeys() == entity.getEntityModel().isValidateKeys();
			isDefault = model.isValidateKeys();
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.VALIDATE_KEYS, model.isValidateKeys());
			// @ScreenEntity.rightToLeft: default false
			isPrevious = model.isRightToLeft() == entity.getEntityModel().isRightToLeft();
			isDefault = !model.isRightToLeft();
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.RIGHT_TO_LEFT, model.isRightToLeft());
			// @ScreenEntity.roles: default {}
			isPrevious = PrivateMethods.compareScreenEntityRoles(model.getRoles(), entity.getEntityModel().getRoles());
			isDefault = ((model.getRoles() == null) || (model.getRoles().isEmpty()));
			// IMPORTANT: in action we need to store list as strings
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ROLES, model.getRolesAsStringList());
			// @ScreenEntity.autoMapKeyboardActions: default false
			isPrevious = model.isAutoMapKeyboardActions() == entity.getEntityModel().isAutoMapKeyboardActions();
			isDefault = !model.isAutoMapKeyboardActions();
			PrivateMethods.addRemoveScreenEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.AUTO_MAP_KEYBOARD_ACTIONS,
					model.isAutoMapKeyboardActions());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenEnumFieldActions(ScreenEntity entity, ScreenEnumFieldModel model, boolean checkPrevious) {
			boolean isPrevious = false;
			boolean isDefault = true;
			ScreenEnumFieldModel entityModel = (ScreenEnumFieldModel)entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = (ScreenEnumFieldModel)entity.getParts().get(model.getParent().getUUID()).getFields().get(
						model.getUUID());
			}
			// @ScreenField java type: default none
			if (checkPrevious) {
				isPrevious = model.getJavaTypeName().equals(entityModel.getJavaTypeName());
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenEnumFieldAction(entity, model, isPrevious, isDefault, ASTNode.FIELD_DECLARATION
					| ASTNode.SIMPLE_TYPE, Constants.JAVA_TYPE, model.getType());
			// Enum properties: default none;
			if (checkPrevious) {
				isPrevious = PrivateMethods.compareEnumEntries(model.getEntries(), entityModel.getEntries());
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenEnumFieldAction(entity, model, isPrevious, isDefault,
					ASTNode.ENUM_CONSTANT_DECLARATION, Constants.ENUM_FIELD_ENTRIES, model.getEntries());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenFieldActions(ScreenEntity entity, ScreenFieldModel model, boolean checkPrevious) {
			boolean isPrevious = false;
			boolean isDefault = true;
			ScreenFieldModel entityModel = entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = entity.getParts().get(model.getParent().getUUID()).getFields().get(model.getUUID());
			}
			// @ScreenField -> fieldName: default equals to field name from entityModel
			if (checkPrevious) {
				isPrevious = model.getFieldName().equals(entityModel.getFieldName());
			}
			isDefault = entityModel.getPreviousFieldName().equals(model.getFieldName());
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.FIELD_DECLARATION,
					Constants.FIELD_NAME, model.getFieldName());
			// @ScreenField.row: default none
			if (checkPrevious) {
				isPrevious = model.getRow() == entityModel.getRow();
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ROW, model.getRow());
			// @ScreenField.column: default none
			if (checkPrevious) {
				isPrevious = model.getColumn() == entityModel.getColumn();
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.COLUMN, model.getColumn());
			// @ScreenField.endColumn: default 0
			if (checkPrevious) {
				isPrevious = model.getEndColumn() == entityModel.getEndColumn();
			}
			isDefault = (model.getEndColumn() == null) || (model.getEndColumn() == 0);
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.END_COLUMN, model.getEndColumn());
			// @ScreenField.endRow: default 0
			if (checkPrevious) {
				isPrevious = model.getEndRow() == entityModel.getEndRow();
			}
			isDefault = (model.getEndRow() == null) || (model.getEndRow() == 0);
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.END_ROW, model.getEndRow());
			// @ScreenField.rectangle: default false
			if (checkPrevious) {
				isPrevious = model.isRectangle() == entityModel.isRectangle();
			}
			isDefault = !model.isRectangle();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.RECTANGLE, model.isRectangle());
			// @ScreenField.editable: default false
			if (checkPrevious) {
				isPrevious = model.isEditable() == entityModel.isEditable();
			}
			isDefault = !model.isEditable();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.EDITABLE, model.isEditable());
			// @ScreenField.password: default false
			if (checkPrevious) {
				isPrevious = model.isPassword() == entityModel.isPassword();
			}
			isDefault = !model.isPassword();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.PASSWORD, model.isPassword());
			// @ScreenField.fieldType: default FieldType.General.class
			if (checkPrevious) {
				isPrevious = model.getFieldTypeName().equals(entityModel.getFieldTypeName());
			}
			isDefault = model.getFieldType().equals(FieldType.General.class);
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.FIELD_TYPE, model.getFieldType());
			// @ScreenField.displayName: default none
			if (checkPrevious) {
				isPrevious = model.getDisplayName().equals(entityModel.getDisplayName());
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @ScreenField.sampleValue: default ""
			if (checkPrevious) {
				isPrevious = model.getSampleValue().equals(entityModel.getSampleValue());
			}
			isDefault = model.getSampleValue().isEmpty();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.SAMPLE_VALUE, model.getSampleValue());
			// @ScreenField.defaultValue: default ""
			if (checkPrevious) {
				isPrevious = model.getDefaultValue().equals(entityModel.getDefaultValue());
			}
			isDefault = model.getDefaultValue().isEmpty();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DEFAULT_VALUE, model.getDefaultValue());
			// @ScreenField.labelColumn: default 0
			if (checkPrevious) {
				isPrevious = model.getLabelColumn() == entityModel.getLabelColumn();
			}
			isDefault = model.getLabelColumn() == 0;
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.LABEL_COLUMN, model.getLabelColumn());
			// @ScreenField.key: default false
			if (checkPrevious) {
				isPrevious = model.isKey() == entityModel.isKey();
			}
			isDefault = !model.isKey();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.KEY, model.isKey());
			// @ScreenField.helpText: default ""
			if (checkPrevious) {
				isPrevious = model.getHelpText().equals(entityModel.getHelpText());
			}
			isDefault = model.getHelpText().isEmpty();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.HELP_TEXT, model.getHelpText());
			// @ScreenField.rightToLeft: default false
			if (checkPrevious) {
				isPrevious = model.isRightToLeft() == entityModel.isRightToLeft();
			}
			isDefault = !model.isRightToLeft();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.RIGHT_TO_LEFT, model.isRightToLeft());
			// @ScreenField.attribute: default FieldAttributeType.Value
			if (checkPrevious) {
				isPrevious = model.getAttribute().equals(entityModel.getAttribute());
			}
			isDefault = model.getAttribute().equals(FieldAttributeType.Value);
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ATTRIBUTE, model.getAttribute());
			// @ScreenField.when: default ""
			if (checkPrevious) {
				isPrevious = model.getWhen().equals(entityModel.getWhen());
			}
			isDefault = model.getWhen().isEmpty();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.WHEN, model.getWhen());
			// @ScreenField.unless: default ""
			if (checkPrevious) {
				isPrevious = model.getUnless().equals(entityModel.getUnless());
			}
			isDefault = model.getUnless().isEmpty();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.UNLESS, model.getUnless());
			// @ScreenField.keyIndex: default -1
			if (checkPrevious) {
				isPrevious = model.getKeyIndex() == entityModel.getKeyIndex();
			}
			isDefault = model.getKeyIndex() == -1;
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.KEY_INDEX, model.getKeyIndex());
			// @ScreenField.internal: default false
			if (checkPrevious) {
				isPrevious = model.isInternal() == entityModel.isInternal();
			}
			isDefault = !model.isInternal();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.INTERNAL, model.isInternal());
			// @ScreenField.global: default false
			if (checkPrevious) {
				isPrevious = model.isGlobal() == entityModel.isGlobal();
			}
			isDefault = !model.isGlobal();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.GLOBAL, model.isGlobal());
			// @ScreenField.nullValue: default none
			if (checkPrevious) {
				isPrevious = model.getNullValue().equals(entityModel.getNullValue());
			}
			isDefault = StringUtils.isEmpty(model.getNullValue());
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.NULL_VALUE, model.getNullValue());
			// @ScreenField.tableKey: default false
			if (checkPrevious) {
				isPrevious = model.isTableKey() == entityModel.isTableKey();
			}
			isDefault = !model.isTableKey();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.TABLE_KEY, model.isTableKey());
			// @ScreenField.forceUpdate: default false
			if (checkPrevious) {
				isPrevious = model.isForceUpdate() == entityModel.isForceUpdate();
			}
			isDefault = !model.isForceUpdate();
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.FORCE_UPDATE, model.isForceUpdate());
			// @ScreenField.expression: default ""
			if (checkPrevious) {
				isPrevious = StringUtils.equals(model.getExpression(), entityModel.getExpression());
			}
			isDefault = StringUtils.isEmpty(model.getExpression());
			PrivateMethods.addRemoveScreenFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.EXPRESSION, model.getExpression());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenFieldValuesActions(ScreenEntity entity, ScreenFieldValuesModel model,
				boolean checkPrevious) {
			boolean isPrevious = false;
			boolean isDefault = true;
			ScreenFieldValuesModel entityModel = (ScreenFieldValuesModel)entity.getFields().get(model.getUUID());
			if (entityModel == null) {
				entityModel = (ScreenFieldValuesModel)entity.getParts().get(model.getParent().getUUID()).getFields().get(
						model.getUUID());
			}
			// @ScreenFieldValues.sourceScreenEntity: default none;
			if (checkPrevious) {
				isPrevious = entityModel.getSourceScreenEntityName().equals(model.getSourceScreenEntityName());
			}
			isDefault = false;
			PrivateMethods.addRemoveScreenFieldValuesAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SOURCE_SCREEN_ENTITY, model.getSourceScreenEntity());
			// @ScreenFieldValues.collectAll: default false;
			if (checkPrevious) {
				isPrevious = entityModel.isCollectAll() == model.isCollectAll();
			}
			isDefault = !model.isCollectAll();
			PrivateMethods.addRemoveScreenFieldValuesAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.COLLECT_ALL, model.isCollectAll());
			// @ScreenFieldValues.provider: default ScreenRecordsProvider.class;
			if (checkPrevious) {
				isPrevious = entityModel.getProvider().equals(model.getProvider());
			}
			isDefault = model.getProvider().equals(ScreenRecordsProvider.class);
			PrivateMethods.addRemoveScreenFieldValuesAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.PROVIDER, model.getProvider());
			// @ScreenFieldValues.asWindow: default false;
			if (checkPrevious) {
				isPrevious = entityModel.isAsWindow() == model.isAsWindow();
			}
			isDefault = !model.isAsWindow();
			PrivateMethods.addRemoveScreenFieldValuesAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.AS_WINDOW, model.isAsWindow());
			// @ScreenFieldValues.autoSubmitAction: default TerminalActions.NULL.class
			if (checkPrevious) {
				isPrevious = entityModel.getAutoSubmitActionName().equals(model.getAutoSubmitActionName());
			}
			isDefault = model.getAutoSubmitAction().equals(TerminalActions.NULL.class);
			PrivateMethods.addRemoveScreenFieldValuesAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.AUTO_SUBMIT_ACTION, model.getAutoSubmitAction());
			// @ScreenFieldValues.displayFieldName: default ""
			if (checkPrevious) {
				isPrevious = entityModel.getDisplayFieldName().equals(model.getDisplayFieldName());
			}
			isDefault = model.getDisplayFieldName().isEmpty();
			PrivateMethods.addRemoveScreenFieldValuesAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.DISPLAY_FIELD_NAME, model.getDisplayFieldName());
			// @ScreenFieldValues.searchField: default ""
			if (checkPrevious) {
				isPrevious = entityModel.getSearchField().equals(model.getSearchField());
			}
			isDefault = model.getSearchField().isEmpty();
			PrivateMethods.addRemoveScreenFieldValuesAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SEARCH_FIELD, model.getSearchField());
		}

		/**
		 * @param entity
		 * @param fieldModel
		 */
		public static void generateScreenDescriptionFieldActions(ScreenEntity entity, ScreenFieldModel fieldModel,
				boolean checkPrevious) {
			boolean isPrevious = false;
			boolean isDefault = true;
			ScreenFieldModel entityFieldModel = entity.getFields().get(fieldModel.getUUID());
			if (entityFieldModel == null) {
				entityFieldModel = entity.getParts().get(fieldModel.getParent().getUUID()).getFields().get(fieldModel.getUUID());
			}
			ScreenDescriptionFieldModel entityModel = entityFieldModel.getDescriptionFieldModel();
			ScreenDescriptionFieldModel model = fieldModel.getDescriptionFieldModel();
			// if (model.getColumn() == null || model.getColumn() == 0) {
			// isPrevious = model.getColumn() == entityModel.getColumn();
			// isDefault = true;
			// PrivateMethods.addRemoveScreenDescriptionFieldAction(entity, fieldModel, isPrevious, isDefault,
			// ASTNode.NORMAL_ANNOTATION, AnnotationConstants.COLUMN, model.getColumn());
			// } else if (entityModel.getColumn() != 0 || model.getColumn() != 0) {

			// @ScreenDescriptionField.row: default 0;
			if (checkPrevious) {
				isPrevious = model.getRow() == entityModel.getRow();
			}
			isDefault = model.getRow() == 0;
			PrivateMethods.addRemoveScreenDescriptionFieldAction(entity, fieldModel, isPrevious, isDefault,
					ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR, Constants.DESC_ROW, model.getRow());
			// @ScreenDescriptionField.column: default 0;
			if (checkPrevious) {
				isPrevious = model.getColumn() == entityModel.getColumn();
			}
			isDefault = model.getColumn() == 0;
			PrivateMethods.addRemoveScreenDescriptionFieldAction(entity, fieldModel, isPrevious, isDefault,
					ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR, Constants.DESC_COLUMN, model.getColumn());
			// @ScreenDescriptionField.endColumn: default 0;
			if (checkPrevious) {
				isPrevious = model.getEndColumn() == entityModel.getEndColumn();
			}
			isDefault = model.getEndColumn() == 0;
			PrivateMethods.addRemoveScreenDescriptionFieldAction(entity, fieldModel, isPrevious, isDefault,
					ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR, Constants.DESC_END_COLUMN, model.getEndColumn());
			// }
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenIdentifiersActions(ScreenEntity entity, ScreenIdentifiersModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// add @ScreenIdentifiers annotation
			if (entity.getIdentifiersModel().getIdentifiers().isEmpty() && (model.getIdentifiers() != null)
					&& (!model.getIdentifiers().isEmpty())) {
				entity.addAction(new ScreenIdentifiersAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						ScreenAnnotationConstants.SCREEN_IDENTIFIERS_ANNOTATION, null));
			}
			// remove @ScreenIdentifiers annotation
			if (!entity.getIdentifiersModel().getIdentifiers().isEmpty()
					&& ((model.getIdentifiers() == null) || (model.getIdentifiers().isEmpty()))) {
				entity.addAction(new ScreenIdentifiersAction(model.getUUID(), model, ActionType.REMOVE,
						ASTNode.NORMAL_ANNOTATION, ScreenAnnotationConstants.SCREEN_IDENTIFIERS_ANNOTATION, null));
			}
			// remove addAnnotation action if all of identifiers were deleted
			if ((entity.getIdentifiersModel().getIdentifiers().isEmpty() && (model.getIdentifiers() == null || model.getIdentifiers().isEmpty()))
					|| (!entity.getIdentifiersModel().getIdentifiers().isEmpty() && model.getIdentifiers() != null && !model.getIdentifiers().isEmpty())) {
				entity.removeAction(model.getUUID(), ScreenAnnotationConstants.SCREEN_IDENTIFIERS_ANNOTATION);
			}
			// @ScreenIdentifiers.identifiers: default {}
			isPrevious = PrivateMethods.compareIdentifierModels(model.getIdentifiers(),
					entity.getIdentifiersModel().getIdentifiers());
			isDefault = ((model.getIdentifiers() == null) || (model.getIdentifiers().isEmpty()));
			PrivateMethods.addRemoveScreenIdentifiersAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.IDENTIFIERS, model.getIdentifiers());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenNavigationActions(ScreenEntity entity, ScreenNavigationModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// add @ScreenNavigation annotation
			if (entity.getNavigationModel().getAccessedFromEntityName().isEmpty()
					&& (entity.getNavigationModel().getAccessedFrom() == null) && !model.getAccessedFromEntityName().isEmpty()
					&& (model.getAccessedFrom() != null)) {
				entity.addAction(new ScreenNavigationAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						ScreenAnnotationConstants.SCREEN_NAVIGATION_ANNOTATION, null));
			} else if (model.getAccessedFromEntityName().isEmpty()) {
				// remove @ScreenNavigation annotation
				entity.addAction(new ScreenNavigationAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						ScreenAnnotationConstants.SCREEN_NAVIGATION_ANNOTATION, null));
			}
			// @ScreenNavigation.accessedFrom: default none
			isPrevious = model.getAccessedFromEntityName().equals(entity.getNavigationModel().getAccessedFromEntityName());
			isDefault = false;
			PrivateMethods.addRemoveScreenNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ACCESSED_FROM, model.getAccessedFrom());
			// @ScreenNavigation.terminalAction: default TerminalActions.ENTER.class
			isPrevious = model.getTerminalAction().equals(entity.getNavigationModel().getTerminalAction());
			isDefault = model.getTerminalAction().equals(TerminalActions.ENTER.class);
			PrivateMethods.addRemoveScreenNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.TERMINAL_ACTION, model.getTerminalAction());
			// @ScreenNavigation.exitAction: default TerminalActions.F3.class
			isPrevious = model.getExitAction().equals(entity.getNavigationModel().getExitAction());
			isDefault = model.getExitAction().equals(TerminalActions.F3.class);
			PrivateMethods.addRemoveScreenNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.EXIT_ACTION, model.getExitAction());
			// @ScreenNavigation.additionalKey: default AdditionalKey.NONE
			isPrevious = model.getAdditionalKey().equals(entity.getNavigationModel().getAdditionalKey());
			isDefault = model.getAdditionalKey().equals(AdditionalKey.NONE);
			PrivateMethods.addRemoveScreenNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ADDITIONAL_KEY, model.getAdditionalKey());
			// @ScreenNavigation.exitAdditionalKey: default AdditionalKey.NONE
			isPrevious = model.getExitAdditionalKey().equals(entity.getNavigationModel().getExitAdditionalKey());
			isDefault = model.getExitAdditionalKey().equals(AdditionalKey.NONE);
			PrivateMethods.addRemoveScreenNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.EXIT_ADDITIONAL_KEY, model.getExitAdditionalKey());
			// @ScreenNavigation.requiresParameter: default false
			isPrevious = model.isRequiresParameters() == entity.getNavigationModel().isRequiresParameters();
			isDefault = !model.isRequiresParameters();
			PrivateMethods.addRemoveScreenNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.REQUIRES_PARAMETERS, model.isRequiresParameters());
			// @ScreenNavigation.drilldownValue: default ""
			isPrevious = entity.getNavigationModel().getDrilldownValue().equals(model.getDrilldownValue());
			isDefault = model.getDrilldownValue().isEmpty();
			PrivateMethods.addRemoveScreenNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.DRILLDOWN_VALUE, model.getDrilldownValue());
			// @ScreenNavigation.assignedFields: default {}
			isPrevious = PrivateMethods.compareFieldAssignDefinitions(model.getAssignedFields(),
					entity.getNavigationModel().getAssignedFields());
			isDefault = ((model.getAssignedFields() == null) || (model.getAssignedFields().isEmpty()));
			PrivateMethods.addRemoveScreenNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ASSIGNED_FIELDS, model.getAssignedFields());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenPartActions(ScreenEntity entity, ScreenPartModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			ScreenPartModel entityModel = entity.getParts().get(model.getUUID());
			// @ScreenPart className: default none;
			isPrevious = entityModel.getClassName().equals(model.getClassName());
			isDefault = false;
			PrivateMethods.addRemoveScreenPartAction(entity, model, isPrevious, isDefault, ASTNode.TYPE_DECLARATION
					| ASTNode.SIMPLE_NAME, Constants.JAVA_TYPE_NAME, model.getClassName());
			// add action to remove aspectJ file if className was changed
			PrivateMethods.addRemoveScreenPartRemoveAspectAction(entity, model, isPrevious, Constants.ASPECTJ_FILE_KEY);
			// @ScreenPart.supportTerminalData: default false;
			isPrevious = entityModel.isSupportTerminalData() == model.isSupportTerminalData();
			isDefault = !model.isSupportTerminalData();
			PrivateMethods.addRemoveScreenPartAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA, model.isSupportTerminalData());
			// @ScreenPart.name: default "";
			isPrevious = entityModel.getName().equals(model.getName());
			isDefault = model.getName().isEmpty() || model.getClassName().equals(model.getName());
			PrivateMethods.addRemoveScreenPartAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.NAME, model.getName());
			// @ScreenPart.displayName: default "";
			isPrevious = entityModel.getDisplayName().equals(model.getDisplayName());
			isDefault = model.getDisplayName().isEmpty();
			PrivateMethods.addRemoveScreenPartAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// add @PartPosition annotation if one of attributes do not default and is different from entity model
			// refs #235 - to avoid duplicate of @PartPosition annotation we should check that model from entity is equal to
			// default values
			if ((!model.getPartPosition().isDefaultValues())
					&& (!entityModel.getPartPosition().equals(model.getPartPosition()) && entityModel.getPartPosition().isDefaultValues())) {
				entity.addAction(new PartPositionAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						ScreenAnnotationConstants.PART_POSITION_ANNOTATION, null));
			}
			// refs #235 - to avoid duplicate of @PartPosition annotation we should to remove it if annotation attributes is
			// default
			if (model.getPartPosition().isDefaultValues()) {
				entity.addAction(new PartPositionAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						null, null));
			}
			// @PartPosition.row: default 0;
			isPrevious = entityModel.getPartPosition().getRow() == model.getPartPosition().getRow();
			isDefault = model.getPartPosition().getRow() == 0;
			PrivateMethods.addRemovePartPositionAction(entity, model.getPartPosition(), isPrevious, isDefault,
					ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ROW,
					model.getPartPosition().getRow());
			// @PartPosition.column: default 0;
			isPrevious = entityModel.getPartPosition().getColumn() == model.getPartPosition().getColumn();
			isDefault = model.getPartPosition().getColumn() == 0;
			PrivateMethods.addRemovePartPositionAction(entity, model.getPartPosition(), isPrevious, isDefault,
					ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.COLUMN,
					model.getPartPosition().getColumn());
			// @PartPostition.width: default 80;
			isPrevious = entityModel.getPartPosition().getWidth() == model.getPartPosition().getWidth();
			isDefault = model.getPartPosition().getWidth() == 0;
			PrivateMethods.addRemovePartPositionAction(entity, model.getPartPosition(), isPrevious, isDefault,
					ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.WIDTH,
					model.getPartPosition().getWidth());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateScreenTableActions(ScreenEntity entity, ScreenTableModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			ScreenTableModel entityModel = entity.getTables().get(model.getUUID());
			// @ScreenTable className: default none;
			isPrevious = entityModel.getClassName().equals(model.getClassName());
			isDefault = false;
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.TYPE_DECLARATION
					| ASTNode.SIMPLE_NAME, Constants.JAVA_TYPE_NAME, model.getClassName());
			// @ScreenTable.startRow: default none;
			isPrevious = entityModel.getStartRow() == model.getStartRow();
			isDefault = false;
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.START_ROW, model.getStartRow());
			// @ScreenTable.endRow: default none;
			isPrevious = entityModel.getEndRow() == model.getEndRow();
			isDefault = false;
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.END_ROW, model.getEndRow());
			// @ScreenTable.name: default "";
			isPrevious = entityModel.getName().equals(model.getName());
			isDefault = model.getName().isEmpty();
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.NAME, model.getName());
			// @ScreenTable.nextScreenAction: default TerminalActions.PAGE_DOWN.class;
			isPrevious = entityModel.getNextScreenActionName().equals(model.getNextScreenActionName());
			isDefault = model.getNextScreenActionName().equals(TerminalActions.PAGE_DOWN.class.getSimpleName());
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.NEXT_SCREEN_ACTION, model.getNextScreenAction());
			// @ScreenTable.previousScreenAction: default TerminalActions.PAGE_UP.class;
			isPrevious = entityModel.getPreviousScreenActionName().equals(model.getPreviousScreenActionName());
			isDefault = model.getPreviousScreenActionName().equals(TerminalActions.PAGE_UP.class.getSimpleName());
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.PREV_SCREEN_ACTION, model.getPreviousScreenAction());
			// @ScreenTable.supportTerminalData: default false;
			isPrevious = entityModel.isSupportTerminalData() == model.isSupportTerminalData();
			isDefault = !model.isSupportTerminalData();
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA, model.isSupportTerminalData());
			// @ScreenTable.scrollable: default true;
			isPrevious = entityModel.isScrollable() == model.isScrollable();
			isDefault = model.isScrollable();
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SCROLLABLE, model.isScrollable());
			// @ScreenTable.tableCollector: default ScreenTableCollector.class;
			isPrevious = entityModel.getTableCollectorName().equals(model.getTableCollectorName());
			isDefault = model.getTableCollectorName().equals(ScreenTableCollector.class.getSimpleName());
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.TABLE_COLLECTOR, model.getTableCollector());
			// @ScreenTable.rowGaps: default 1;
			isPrevious = entityModel.getRowGaps() == model.getRowGaps();
			isDefault = model.getRowGaps() == 1;
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ROW_GAPS, model.getRowGaps());
			// @ScreenTable.screensCount: default 1;
			isPrevious = entityModel.getScreensCount() == model.getScreensCount();
			isDefault = model.getScreensCount() == 1;
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.SCREENS_COUNT, model.getScreensCount());
			// @ScreenTable.filterExpression: default ""
			isPrevious = StringUtils.equals(entityModel.getFilterExpression(), model.getFilterExpression());
			isDefault = StringUtils.isEmpty(model.getFilterExpression());
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.FILTER_EXPRESSION, model.getFilterExpression());
			// @ScreenTable.rightToLeft: default false
			isPrevious = entityModel.isRightToLeft() == model.isRightToLeft();
			isDefault = !model.isRightToLeft();
			PrivateMethods.addRemoveScreenTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.RIGHT_TO_LEFT, model.isRightToLeft());
		}

		/**
		 * @param entity
		 * @param model
		 */
		public static void generateTableActionActions(ScreenEntity entity, TableActionModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			TableActionModel entityModel = entity.getTables().get(model.getParent().getUUID()).getActions().get(model.getUUID());
			// @TableAction.action: default EnterDrilldownAction.class;
			isPrevious = entityModel.getAction().equals(model.getAction());
			isDefault = model.getAction().equals(EnterDrilldownAction.class);
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ACTION, model.getAction());
			// @TableAction.defaultAction: default false;
			isPrevious = entityModel.isDefaultAction() == model.isDefaultAction();
			isDefault = !model.isDefaultAction();
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DEFAULT_ACTION, model.isDefaultAction());
			// @TableAction.actionValue: default "";
			isPrevious = entityModel.getActionValue().equals(model.getActionValue());
			isDefault = model.getActionValue().isEmpty();
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ACTION_VALUE, model.getActionValue());
			// @TableAction.displayName: default none;
			isPrevious = entityModel.getDisplayName().equals(model.getDisplayName());
			isDefault = false;
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @TableAction.alias: default "";
			isPrevious = entityModel.getAlias().equals(model.getAlias());
			isDefault = model.getAlias().isEmpty();
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ALIAS, model.getAlias());
			// @TableAction.targetEntity: default org.openlegacy.terminal.ScreenEntity.NONE.class;
			isPrevious = entityModel.getTargetEntityName().equals(model.getTargetEntityName());
			isDefault = model.getTargetEntity().equals(org.openlegacy.terminal.ScreenEntity.NONE.class);
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.TARGET_ENTITY, model.getTargetEntity());
			// @TableAction.row: default 0
			isPrevious = entityModel.getRow() == model.getRow();
			isDefault = model.getRow() == 0;
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.ROW, model.getRow());
			// @TableAction.column: default 0
			isPrevious = entityModel.getColumn() == model.getColumn();
			isDefault = model.getColumn() == 0;
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.COLUMN, model.getColumn());
			// @TableAction.length: default 0
			isPrevious = entityModel.getLength() == model.getLength();
			isDefault = model.getLength() == 0;
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.LENGTH, model.getLength());
			// @TableAction.when: default ".*"
			isPrevious = StringUtils.equals(entityModel.getWhen(), model.getWhen());
			isDefault = StringUtils.equals(model.getWhen(), ".*");
			PrivateMethods.addRemoveTableActionAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, ScreenAnnotationConstants.WHEN, model.getWhen());
		}

		public static void generateScreenBooleanFieldActions(ScreenEntity entity, ScreenBooleanFieldModel model) {
			generateScreenBooleanFieldActions(entity, model, true);
		}

		public static void generateScreenDateFieldActions(ScreenEntity entity, ScreenDateFieldModel model) {
			generateScreenDateFieldActions(entity, model, true);
		}

		public static void generateScreenEnumFieldActions(ScreenEntity entity, ScreenEnumFieldModel model) {
			generateScreenEnumFieldActions(entity, model, true);
		}

		public static void generateScreenFieldActions(ScreenEntity entity, ScreenFieldModel model) {
			generateScreenFieldActions(entity, model, true);
		}

		public static void generateScreenFieldValuesActions(ScreenEntity entity, ScreenFieldValuesModel model) {
			generateScreenFieldValuesActions(entity, model, true);
		}

		public static void generateScreenDescriptionFieldActions(ScreenEntity entity, ScreenFieldModel fieldModel) {
			generateScreenDescriptionFieldActions(entity, fieldModel, true);
		}

		public static void generateScreenColumnActions(ScreenEntity entity, ScreenColumnModel model) {
			generateScreenColumnActions(entity, model, true);
		}
	}

	private static class PrivateMethods {

		private static void addRemovePartPositionAction(ScreenEntity entity, PartPositionModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new PartPositionAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new PartPositionAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenActionsAction(ScreenEntity entity, ScreenActionsModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenActionsAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenActionsAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenBooleanFieldAction(ScreenEntity entity, ScreenBooleanFieldModel model,
				boolean isPrevious, boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenBooleanFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenBooleanFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenColumnAction(ScreenEntity entity, ScreenColumnModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenColumnAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenColumnAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenDateFieldAction(ScreenEntity entity, ScreenDateFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenDateFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenDateFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenEntityAction(ScreenEntity entity, ScreenEntityModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenEntityAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenEntityAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenEnumFieldAction(ScreenEntity entity, ScreenEnumFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenEnumFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenEnumFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenFieldAction(ScreenEntity entity, ScreenFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenDescriptionFieldAction(ScreenEntity entity, ScreenFieldModel model,
				boolean isPrevious, boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenDescriptionFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenDescriptionFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenFieldValuesAction(ScreenEntity entity, ScreenFieldValuesModel model,
				boolean isPrevious, boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenFieldValuesAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenFieldValuesAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenIdentifiersAction(ScreenEntity entity, ScreenIdentifiersModel model,
				boolean isPrevious, boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenIdentifiersAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenIdentifiersAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenNavigationAction(ScreenEntity entity, ScreenNavigationModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenNavigationAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenNavigationAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenPartAction(ScreenEntity entity, ScreenPartModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenPartAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenPartAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenPartRemoveAspectAction(ScreenEntity entity, ScreenPartModel model, boolean isPrevious,
				String key) {
			if (!isPrevious) {
				entity.addAction(new ScreenPartRemoveAspectAction(model.getUUID(), model, ActionType.REMOVE,
						Constants.ASPECTJ_FILE, key, entity.getEntityModel().getClassName()));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveScreenTableAction(ScreenEntity entity, ScreenTableModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new ScreenTableAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new ScreenTableAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveTableActionAction(ScreenEntity entity, TableActionModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new TableActionAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new TableActionAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
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
						|| !aClass.getAdditionalKey().equals(bClass.getAdditionalKey())
						|| !StringUtils.equals(aClass.getWhen(), bClass.getWhen()) || aClass.getRow() != bClass.getRow()
						|| aClass.getColumn() != bClass.getColumn() || aClass.getLength() != bClass.getLength()
						|| !StringUtils.equals(aClass.getFocusField(), bClass.getFocusField())
						|| !aClass.getType().equals(bClass.getType())
						|| !aClass.getTargetEntityClassName().equals(bClass.getTargetEntityClassName())
						|| aClass.getSleep() != bClass.getSleep() || aClass.isGlobal() != bClass.isGlobal()
						|| !aClass.getKeyboardKeyName().equals(bClass.getKeyboardKeyName())) {
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

		private static boolean compareFieldAssignDefinitions(List<FieldAssignDefinition> a, List<FieldAssignDefinition> b) {
			if (a.size() != b.size()) {
				return false;
			}
			for (int i = 0; i < a.size(); i++) {
				FieldAssignDefinition aClass = a.get(i);
				FieldAssignDefinition bClass = b.get(i);
				String aClassValue = aClass.getValue() == null ? "" : aClass.getValue();
				String bClassValue = bClass.getValue() == null ? "" : bClass.getValue();
				if (!(aClass.getName().equals(bClass.getName())) || !(aClassValue.equals(bClassValue))) {
					return false;
				}
			}
			return true;
		}

		private static boolean compareIdentifierModels(List<IdentifierModel> a, List<IdentifierModel> b) {
			if (a.size() != b.size()) {
				return false;
			}
			for (int i = 0; i < a.size(); i++) {
				IdentifierModel aClass = a.get(i);
				IdentifierModel bClass = b.get(i);
				if (aClass.getRow() != bClass.getRow() || aClass.getColumn() != bClass.getColumn()
						|| !aClass.getText().equals(bClass.getText())
						|| (aClass.getAttribute() != null && !aClass.getAttribute().equals(bClass.getAttribute()))) {
					return false;
				}
			}
			return true;
		}

		private static boolean compareScreenEntityRoles(List<ScreenEntityRole> a, List<ScreenEntityRole> b) {
			if (a.size() != b.size()) {
				return false;
			}
			for (int i = 0; i < a.size(); i++) {
				ScreenEntityRole aItem = a.get(i);
				ScreenEntityRole bItem = b.get(i);
				if (!aItem.getRole().equals(bItem.getRole())) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Returns list of actions that equal to <b>actionClass</b>
	 * 
	 * @param entity
	 * @param actionClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getActionList(ScreenEntity entity, Class<T> actionClass) {
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
		return (List<T>)ScreenEntityActionsSorter.INSTANCE.sort((List<AbstractAction>)list);
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
	public static <T> List<T> getActionList(ScreenEntity entity, int target, ActionType[] actionTypes) {
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
		return (List<T>)ScreenEntityActionsSorter.INSTANCE.sort((List<AbstractAction>)list);
	}

	public static Map<UUID, ScreenFieldModel> getFieldsModels(List<ScreenFieldDefinition> list,
			List<ScreenFieldModel> sortedFields, NamedObject parent) {
		Map<UUID, ScreenFieldModel> map = new HashMap<UUID, ScreenFieldModel>();
		if (list == null) {
			return map;
		}
		sortedFields.clear();
		for (ScreenFieldDefinition definition : list) {
			ScreenFieldModel model = null;
			if (definition.getFieldTypeDefinition() instanceof BooleanFieldTypeDefinition) {
				model = new ScreenBooleanFieldModel(parent);
			} else if (definition.getFieldTypeDefinition() instanceof DateFieldTypeDefinition) {
				model = new ScreenDateFieldModel(parent);
			} else if (definition.getFieldTypeDefinition() instanceof FieldWithValuesTypeDefinition) {
				model = new ScreenFieldValuesModel(parent);
			} else if (definition.getFieldTypeDefinition() instanceof EnumFieldTypeDefinition) {
				model = new ScreenEnumFieldModel(parent);
			} else {
				// string field by default
				model = new ScreenFieldModel(parent);
				if (((SimpleScreenFieldDefinition)definition).getJavaTypeName().equals(Integer.class.getSimpleName())) {
					// integer field
					model = new ScreenIntegerFieldModel(parent);
				}
			}
			if (model != null) {
				model.init(definition);
				map.put(model.getUUID(), model);
				sortedFields.add(model.clone());
			}
		}
		return map;
	}

	/**
	 * @param entityDefinition
	 * @return
	 */
	public static Map<UUID, ScreenPartModel> getPartsModels(ScreenEntityDefinition entityDefinition,
			List<ScreenPartModel> sortedParts) {
		Map<UUID, ScreenPartModel> map = new HashMap<UUID, ScreenPartModel>();
		Collection<PartEntityDefinition<ScreenFieldDefinition>> values = entityDefinition.getPartsDefinitions().values();
		for (PartEntityDefinition<ScreenFieldDefinition> screenPartEntityDefinition : values) {
			if (screenPartEntityDefinition instanceof CodeBasedScreenPartDefinition) {
				ScreenPartModel model = new ScreenPartModel();
				model.init((CodeBasedScreenPartDefinition)screenPartEntityDefinition);
				map.put(model.getUUID(), model);
				sortedParts.add(model.clone());
			}
		}
		return map;
	}

	/**
	 * @param entityDefinition
	 * @return
	 */
	public static ScreenActionsModel getScreenActionsModel(ScreenEntityDefinition entityDefinition) {
		ScreenActionsModel model = new ScreenActionsModel();
		model.init((CodeBasedScreenEntityDefinition)entityDefinition);
		return model;
	}

	/**
	 * @param entityDefinition
	 * @return
	 */
	public static ScreenEntityModel getScreenEntityModel(ScreenEntityDefinition entityDefinition) {
		ScreenEntityModel model = new ScreenEntityModel();
		model.init((CodeBasedScreenEntityDefinition)entityDefinition);
		return model;
	}

	/**
	 * @param entityDefinition
	 * @return
	 */
	public static ScreenIdentifiersModel getScreenIdentifiersModel(ScreenEntityDefinition entityDefinition) {
		ScreenIdentifiersModel model = new ScreenIdentifiersModel();
		model.init((CodeBasedScreenEntityDefinition)entityDefinition);
		return model;
	}

	/**
	 * @param entityDefinition
	 * @return
	 */
	public static ScreenNavigationModel getScreenNavigationModel(ScreenEntityDefinition entityDefinition) {
		ScreenNavigationModel model = new ScreenNavigationModel();
		model.init((CodeBasedScreenEntityDefinition)entityDefinition);
		return model;
	}

	public static Map<UUID, ScreenTableModel> getTablesModels(Map<String, ScreenTableDefinition> tableDefinitions,
			List<ScreenTableModel> sortedTablesModels) {
		Map<UUID, ScreenTableModel> map = new HashMap<UUID, ScreenTableModel>();

		Set<String> keySet = tableDefinitions.keySet();
		for (String key : keySet) {
			ScreenTableDefinition definition = tableDefinitions.get(key);
			ScreenTableModel model = new ScreenTableModel();
			model.init((CodeBasedScreenTableDefinition)definition);
			map.put(model.getUUID(), model);
			sortedTablesModels.add(model.clone());
		}
		return map;
	}

	/**
	 * @param childEntitiesDefinitions
	 * @param sortedChildEntities
	 * @return
	 */
	public static Map<UUID, ChildEntityModel> getChildEntityModels(List<EntityDefinition<?>> childEntitiesDefinitions,
			List<ChildEntityModel> sortedChildEntities) {

		Map<UUID, ChildEntityModel> map = new HashMap<UUID, ChildEntityModel>();
		for (EntityDefinition<?> entityDefinition : childEntitiesDefinitions) {
			ChildEntityModel model = new ChildEntityModel();
			model.init((CodeBasedScreenEntityDefinition)entityDefinition);
			map.put(model.getUUID(), model.clone());
			sortedChildEntities.add(model);
		}
		if (!sortedChildEntities.isEmpty()) {
			Collections.sort(sortedChildEntities, new Comparator<ChildEntityModel>() {

				@Override
				public int compare(ChildEntityModel o1, ChildEntityModel o2) {
					return o1.getClassName().compareTo(o2.getClassName());
				}
			});
		}
		return map;
	}

}
