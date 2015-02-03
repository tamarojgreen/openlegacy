package com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDbColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDbEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaGeneratedValueFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaIdFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaJoinColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaListFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaManyToOneAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaManyToOneFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaByteFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaJoinColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaManyToOneFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaManyToOneModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaTableModel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;
import org.openlegacy.designtime.generators.AnnotationConstants;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntityUtils {

	/* ********************************************************* */
	/* Action Generator */
	/* ********************************************************* */
	public final static class ActionGenerator {

		public static void generateJpaEntityActions(JpaEntity entity, JpaEntityModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaEntityModel entityModel = entity.getEntityModel();
			// @Entity.name: default ""
			isPrevious = entityModel.getName().equals(model.getName());
			isDefault = StringUtils.isEmpty(model.getName());
			PrivateMethods.addRemoveJpaEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.NAME, model.getName());

			// add @DbEntity annotation
			if (entityModel.isDefaultDbEntityAttrs() && !model.isDefaultDbEntityAttrs()) {
				entity.addAction(new JpaDbEntityAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_ENTITY_ANNOTATION, null));
			}
			// remove @DbEntity annotation
			if (!entityModel.isDefaultDbEntityAttrs() && model.isDefaultDbEntityAttrs()) {
				entity.addAction(new JpaDbEntityAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_ENTITY_ANNOTATION, null));
			}
			if (entityModel.equalsDbEntityAttrs(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_ENTITY_ANNOTATION);
			}
			// if attrs are not default in editable model then delete "remove @DbEntity" action
			if (!model.isDefaultDbEntityAttrs()) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_ENTITY_ANNOTATION, ActionType.REMOVE);
			}

			// @DbEntity.displayName: default ""
			isPrevious = StringUtils.equals(entityModel.getDisplayName(), model.getDisplayName());
			isDefault = StringUtils.isEmpty(model.getDisplayName());
			PrivateMethods.addRemoveJpaDbEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @DbEntity.window: default false
			isPrevious = entityModel.isWindow() == model.isWindow();
			isDefault = !model.isWindow();
			PrivateMethods.addRemoveJpaDbEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.WINDOW, model.isWindow());
			// @DbEntity.child: default false
			isPrevious = entityModel.isChild() == model.isChild();
			isDefault = !model.isChild();
			PrivateMethods.addRemoveJpaDbEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.CHILD, model.isChild());
		}

		public static void generateJpaTableActions(JpaEntity entity, JpaTableModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaTableModel entityModel = entity.getTableModel();
			// add @Table annotation
			if (entityModel.isDefaultTableAttrs() && !model.isDefaultTableAttrs()) {
				entity.addAction(new JpaTableAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_TABLE_ANNOTATION, null));
			}
			// remove @Table annotation
			if (!entityModel.isDefaultTableAttrs() && model.isDefaultTableAttrs()) {
				entity.addAction(new JpaTableAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_TABLE_ANNOTATION, null));
			}
			if (entityModel.equalsTableAttrs(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_TABLE_ANNOTATION);
			}
			// if attrs are not default in editable model then delete "remove @Table" action
			if (!model.isDefaultTableAttrs()) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_TABLE_ANNOTATION, ActionType.REMOVE);
			}

			// @Table.name: default ""
			isPrevious = entity.getTableModel().getName().equals(model.getName());
			isDefault = StringUtils.isEmpty(model.getName());
			PrivateMethods.addRemoveJpaTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.NAME, model.getName());
			// @Table.catalog: default ""
			isPrevious = entity.getTableModel().getCatalog().equals(model.getCatalog());
			isDefault = StringUtils.isEmpty(model.getCatalog());
			PrivateMethods.addRemoveJpaTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.CATALOG, model.getCatalog());
			// @Table.schema: default ""
			isPrevious = entity.getTableModel().getSchema().equals(model.getSchema());
			isDefault = StringUtils.isEmpty(model.getSchema());
			PrivateMethods.addRemoveJpaTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.SCHEMA, model.getSchema());
			// @Table.uniqueConstraints: default {}
			isPrevious = PrivateMethods.compareUniqueConstraintsDefintions(model.getConstraints(),
					entity.getTableModel().getConstraints());
			isDefault = PrivateMethods.isDefaultUniqueConstraintsDefinition(model);
			PrivateMethods.addRemoveJpaTableAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.UNIQUE_CONSTRAINTS, model.getConstraints());
		}

		public static void generateJpaFieldActions(JpaEntity entity, JpaFieldModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaFieldModel entityModel = entity.getFields().get(model.getUUID());
			// add @Column annotation
			if (entityModel.isDefaultColumnAttrs() && !model.isDefaultColumnAttrs()) {
				entity.addAction(new JpaFieldAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_JPA_COLUMN_ANNOTATION, null));
			}
			// remove @Column annotation
			if (!entityModel.isDefaultColumnAttrs() && model.isDefaultColumnAttrs()) {
				entity.addAction(new JpaFieldAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_JPA_COLUMN_ANNOTATION, null));
			}
			// remove add/remove @Column action
			if (entityModel.equalsColumnAttrs(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_JPA_COLUMN_ANNOTATION);
			}
			// if attrs are not default in editable model then delete "remove @Column" action
			if (!model.isDefaultColumnAttrs()) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_JPA_COLUMN_ANNOTATION, ActionType.REMOVE);
			}
			// add @Id annotation
			if (!entityModel.isKey() && model.isKey()) {
				entity.addAction(new JpaIdFieldAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_ID_ANNOTATION, null));
			}
			// remove @Id annotation
			if (entityModel.isKey() && !model.isKey()) {
				entity.addAction(new JpaIdFieldAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_ID_ANNOTATION, null));
			}
			// remove add/remove @Id action
			if (entityModel.isKey() == model.isKey()) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_ID_ANNOTATION);
			}
			// add @GeneratedValue annotation
			if (!entityModel.isGeneratedValue() && model.isGeneratedValue()) {
				entity.addAction(new JpaGeneratedValueFieldAction(model.getUUID(), model, ActionType.ADD,
						ASTNode.NORMAL_ANNOTATION, DbAnnotationConstants.DB_GENERATED_VALUE_ANNOTATION, null));
			}
			// remove @GeneratedValue annotation
			if (entityModel.isGeneratedValue() && !model.isGeneratedValue()) {
				entity.addAction(new JpaGeneratedValueFieldAction(model.getUUID(), model, ActionType.REMOVE,
						ASTNode.NORMAL_ANNOTATION, DbAnnotationConstants.DB_GENERATED_VALUE_ANNOTATION, null));
			}
			// remove add/remove @GeneratedValue action
			if (entityModel.isGeneratedValue() == model.isGeneratedValue()) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_GENERATED_VALUE_ANNOTATION);
			}
			// @Column -> fieldName: default equals to field name from entityModel
			isPrevious = model.getFieldName().equals(entityModel.getFieldName());
			isDefault = entityModel.getPreviousFieldName().equals(model.getFieldName());
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.FIELD_DECLARATION,
					Constants.FIELD_NAME, model.getFieldName());
			// @Column.name: default ""
			isPrevious = StringUtils.equals(model.getName(), entityModel.getName());
			isDefault = StringUtils.isEmpty(model.getName());
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.NAME, model.getName());
			// @Column.columnDefinition: default ""
			isPrevious = StringUtils.equals(model.getColumnDefinition(), entityModel.getColumnDefinition());
			isDefault = StringUtils.isEmpty(model.getColumnDefinition());
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.COLUMN_DEFINITION, model.getColumnDefinition());
			// @Column.table: default ""
			isPrevious = StringUtils.equals(model.getTable(), entityModel.getTable());
			isDefault = StringUtils.isEmpty(model.getTable());
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.TABLE, model.getTable());
			// @Column.length: default 255
			isPrevious = model.getLength() == entityModel.getLength();
			isDefault = model.getLength() == 255;
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.LENGTH, model.getLength());
			// @Column.precision: default 0
			isPrevious = model.getPrecision() == entityModel.getPrecision();
			isDefault = model.getPrecision() == 0;
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.PRECISION, model.getPrecision());
			// @Column.scale: default 0
			isPrevious = model.getScale() == entityModel.getScale();
			isDefault = model.getScale() == 0;
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.SCALE, model.getScale());
			// @Column.unique: default false
			isPrevious = model.isUnique() == entityModel.isUnique();
			isDefault = !model.isUnique();
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.UNIQUE, model.isUnique());
			// @Column.nullabel: default true
			isPrevious = model.isNullable() == entityModel.isNullable();
			isDefault = model.isNullable();
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.NULLABLE, model.isNullable());
			// @Column.insertable: default true
			isPrevious = model.isInsertable() == entityModel.isInsertable();
			isDefault = model.isInsertable();
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.INSERTABLE, model.isInsertable());
			// @Column.updatable: default true
			isPrevious = model.isUpdatable() == entityModel.isUpdatable();
			isDefault = model.isUpdatable();
			PrivateMethods.addRemoveJpaFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.UPDATABLE, model.isUpdatable());

			// --------------- @DbColumn --------------------
			// add @DbColumn annotation
			if (entityModel.isDefaultDbColumnAttrs() && !model.isDefaultDbColumnAttrs()) {
				entity.addAction(new JpaDbColumnAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_COLUMN_ANNOTATION, null));
			}
			// remove @DbColumn annotation
			if (!entityModel.isDefaultDbColumnAttrs() && model.isDefaultDbColumnAttrs()) {
				entity.addAction(new JpaDbColumnAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_COLUMN_ANNOTATION, null));
			}
			// remove add/remove @DbColumn action
			if (entityModel.equalsDbColumnAttrs(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_COLUMN_ANNOTATION);
			}
			// if attrs are not default in editable model then delete "remove @DbColumn" action
			if (!model.isDefaultDbColumnAttrs()) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_COLUMN_ANNOTATION, ActionType.REMOVE);
			}

			// @DbColumn.displayName: default ""
			isPrevious = StringUtils.equals(entityModel.getDisplayName(), model.getDisplayName());
			isDefault = StringUtils.isEmpty(model.getDisplayName());
			PrivateMethods.addRemoveJpaDbColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.DISPLAY_NAME, model.getDisplayName());
			// @DbColumn.password: default false
			isPrevious = entityModel.isPassword() == model.isPassword();
			isDefault = !model.isPassword();
			PrivateMethods.addRemoveJpaDbColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.PASSWORD, model.isPassword());
			// @DbColumn.sampleValue: default ""
			isPrevious = StringUtils.equals(entityModel.getSampleValue(), model.getSampleValue());
			isDefault = StringUtils.isEmpty(model.getSampleValue());
			PrivateMethods.addRemoveJpaDbColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.SAMPLE_VALUE, model.getSampleValue());
			// @DbColumn.defaultValue: default ""
			isPrevious = StringUtils.equals(entityModel.getDefaultValue(), model.getDefaultValue());
			isDefault = StringUtils.isEmpty(model.getDefaultValue());
			PrivateMethods.addRemoveJpaDbColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.DEFAULT_VALUE, model.getDefaultValue());
			// @DbColumn.helpText: default ""
			isPrevious = StringUtils.equals(entityModel.getHelpText(), model.getHelpText());
			isDefault = StringUtils.isEmpty(model.getHelpText());
			PrivateMethods.addRemoveJpaDbColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.HELP_TEXT, model.getHelpText());
			// @DbColumn.rightToLeft: default false
			isPrevious = entityModel.isRightToLeft() == model.isRightToLeft();
			isDefault = !model.isRightToLeft();
			PrivateMethods.addRemoveJpaDbColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.RIGHT_TO_LEFT, model.isRightToLeft());
			// @DbColumn.internal: default false
			isPrevious = entityModel.isInternal() == model.isInternal();
			isDefault = !model.isInternal();
			PrivateMethods.addRemoveJpaDbColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.INTERNAL, model.isInternal());
			// @DbColumn.mainDisplayField: default false
			isPrevious = entityModel.isMainDisplayFiled() == model.isMainDisplayFiled();
			isDefault = !model.isMainDisplayFiled();
			PrivateMethods.addRemoveJpaDbColumnAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.MAIN_DISPLAY_FIELD, model.isMainDisplayFiled());
		}

		public static void generateJpaListFieldActions(JpaEntity entity, JpaListFieldModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaListFieldModel entityModel = (JpaListFieldModel)entity.getFields().get(model.getUUID());
			// // add @OneToMany annotation
			// if (entityModel.isDefaultOneToManyAttrs() && !model.isDefaultOneToManyAttrs()) {
			// entity.addAction(new JpaListFieldAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
			// DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION, null));
			// }
			// // remove @OneToMany annotation
			// if (!entityModel.isDefaultOneToManyAttrs() && model.isDefaultOneToManyAttrs()) {
			// entity.addAction(new JpaListFieldAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
			// DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION, null));
			// }
			// // remove add/remove action
			// if (entityModel.equalsOneToManyAttrs(model)) {
			// entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION);
			// }
			// // if attrs are not default in editable model then delete "remove @OneToMany" action
			// if (!model.isDefaultOneToManyAttrs()) {
			// entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION, ActionType.REMOVE);
			// }
			// List<?> parameter: default List<Object>
			isPrevious = StringUtils.equals(entityModel.getFieldTypeArgs(), model.getFieldTypeArgs());
			isDefault = StringUtils.equals(model.getFieldTypeArgs(), Object.class.getSimpleName());
			PrivateMethods.addRemoveJpaListFieldAction(entity, model, isPrevious, isDefault, ASTNode.FIELD_DECLARATION
					| ASTNode.PARAMETERIZED_TYPE, Constants.LIST_TYPE_ARG, model.getFieldTypeArgsClass());
			// @OneToMany.cascade: default {}
			isPrevious = PrivateMethods.compareCascadeTypes(entityModel.getCascade(), model.getCascade());
			isDefault = model.getCascade().length == 0;
			PrivateMethods.addRemoveJpaListFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.CASCADE, model.getCascade());
			// @OneToMany.fetch: default javax.persistence.FetchType.LAZY
			isPrevious = entityModel.getFetch().equals(model.getFetch());
			isDefault = FetchType.LAZY.equals(model.getFetch());
			PrivateMethods.addRemoveJpaListFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.FETCH, model.getFetch());
			// @OneToMany.mappedBy: default ""
			isPrevious = StringUtils.equals(entityModel.getMappedBy(), model.getMappedBy());
			isDefault = StringUtils.isEmpty(model.getMappedBy());
			PrivateMethods.addRemoveJpaListFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.MAPPED_BY, model.getMappedBy());
			// @OneToMany.orphalRemoval: default false
			isPrevious = entityModel.isOrphanRemoval() == model.isOrphanRemoval();
			isDefault = !model.isOrphanRemoval();
			PrivateMethods.addRemoveJpaListFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.ORPHAN_REMOVAL, model.isOrphanRemoval());
			// @OneToMany.targetEntity: default void.class
			isPrevious = StringUtils.equals(entityModel.getTargetEntityClassName(), model.getTargetEntityClassName());
			isDefault = StringUtils.isEmpty(model.getTargetEntityClassName())
					|| void.class.getSimpleName().equalsIgnoreCase(model.getTargetEntityClassName());
			PrivateMethods.addRemoveJpaListFieldAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.TARGET_ENTITY, model.getTargetEntity());
		}

		public static void generateJpaNavigationActions(JpaEntity entity, JpaNavigationModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaNavigationModel entityModel = entity.getNavigationModel();
			// add @DbNavigation annotation
			if (entityModel.isDefaultDbEntityAttrs() && !model.isDefaultDbEntityAttrs()) {
				entity.addAction(new JpaNavigationAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_NAVIGATION_ANNOTATION, null));
			}
			// remove @DbEntity annotation
			if (!entityModel.isDefaultDbEntityAttrs() && model.isDefaultDbEntityAttrs()) {
				entity.addAction(new JpaNavigationAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_NAVIGATION_ANNOTATION, null));
			}
			if (entityModel.equalsDbEntityAttrs(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_NAVIGATION_ANNOTATION);
			}
			// if attrs are not default in editable model then delete "remove @DbEntity" action
			if (!model.isDefaultDbEntityAttrs()) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_NAVIGATION_ANNOTATION, ActionType.REMOVE);
			}

			// @DbNavigation.category: default none
			isPrevious = StringUtils.equals(entityModel.getCategory(), model.getCategory());
			isDefault = StringUtils.isEmpty(model.getCategory());
			PrivateMethods.addRemoveJpaNavigationAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.CATEGORY, model.getCategory());
		}

		public static void generateJpaActionsAction(JpaEntity entity, JpaActionsModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			// add @DbActions annotation
			if (entity.getActionsModel().getActions().isEmpty() && (model.getActions() != null)
					&& (!model.getActions().isEmpty())) {
				entity.addAction(new JpaActionsAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_ACTIONS_ANNOTATION, null));
			}
			// remove @DbActions annotation
			if (!entity.getActionsModel().getActions().isEmpty()
					&& ((model.getActions() == null) || (model.getActions().isEmpty()))) {
				entity.addAction(new JpaActionsAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_ACTIONS_ANNOTATION, null));
			}
			// remove addAnnotation action if all of actions were deleted
			if ((entity.getActionsModel().getActions().isEmpty() && (model.getActions() == null || model.getActions().isEmpty()))
					|| (!entity.getActionsModel().getActions().isEmpty() && model.getActions() != null && !model.getActions().isEmpty())) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_ACTIONS_ANNOTATION);
			}
			// @RpcActions.actions: default {}
			isPrevious = PrivateMethods.compareActionsModels(model.getActions(), entity.getActionsModel().getActions());
			isDefault = ((model.getActions() == null) || (model.getActions().isEmpty()));
			PrivateMethods.addRemoveJpaActionsAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, AnnotationConstants.ACTIONS, model.getActions());
		}

		public static void generateJpaManyToOneAction(JpaEntity entity, JpaManyToOneModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaManyToOneFieldModel fieldModel = (JpaManyToOneFieldModel)model.getParent();
			JpaManyToOneFieldModel entityFieldModel = (JpaManyToOneFieldModel)entity.getFields().get(fieldModel.getUUID());
			JpaManyToOneModel entityModel = entityFieldModel.getManyToOneModel();
			// // add @ManyToOne annotation
			// if (entityModel.isDefaultAttrs() && !model.isDefaultAttrs()) {
			// entity.addAction(new JpaManyToOneAction(model.getUUID(), fieldModel, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
			// DbAnnotationConstants.DB_MANY_TO_ONE_ANNOTATION, null));
			// }
			// // remove @ManyToOne annotation
			// if (!entityModel.isDefaultAttrs() && model.isDefaultAttrs()) {
			// entity.addAction(new JpaManyToOneAction(model.getUUID(), fieldModel, ActionType.REMOVE,
			// ASTNode.NORMAL_ANNOTATION, DbAnnotationConstants.DB_MANY_TO_ONE_ANNOTATION, null));
			// }
			// // remove add/remove @ManyToOne action
			// if (entityModel.equalsAttrs(model)) {
			// entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_MANY_TO_ONE_ANNOTATION);
			// }
			// // if attrs are not default in editable model then delete "remove @ManyToOne" action
			// if (!model.isDefaultAttrs()) {
			// entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_MANY_TO_ONE_ANNOTATION, ActionType.REMOVE);
			// }

			// javaType: default none;
			isPrevious = StringUtils.equals(entityFieldModel.getJavaTypeName(), fieldModel.getJavaTypeName());
			isDefault = StringUtils.equals(entityFieldModel.getJavaTypeName(), fieldModel.getJavaTypeName());
			PrivateMethods.addRemoveJpaManyToOneFieldAction(entity, fieldModel, isPrevious, isDefault, ASTNode.TYPE_DECLARATION,
					Constants.JAVA_TYPE, fieldModel.getJavaType());

			// @ManyToOne.targetEntity: default void.class
			isPrevious = StringUtils.equals(entityModel.getTargetEntityClassName(), model.getTargetEntityClassName());
			isDefault = StringUtils.isEmpty(model.getTargetEntityClassName())
					|| void.class.getSimpleName().equalsIgnoreCase(model.getTargetEntityClassName());
			PrivateMethods.addRemoveJpaManyToOneAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.TARGET_ENTITY, model.getTargetEntity());
			// @ManyToOne.cascade: default {}
			isPrevious = PrivateMethods.compareCascadeTypes(entityModel.getCascade(), model.getCascade());
			isDefault = model.getCascade().length == 0;
			PrivateMethods.addRemoveJpaManyToOneAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.CASCADE, model.getCascade());
			// @ManyToOne.fetch: default FetchType.EAGER
			isPrevious = entityModel.getFetch().equals(model.getFetch());
			isDefault = FetchType.EAGER.equals(model.getFetch());
			PrivateMethods.addRemoveJpaManyToOneAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.FETCH, model.getFetch());
			// @ManyToOne.optional: default true
			isPrevious = entityModel.isOptional() == model.isOptional();
			isDefault = model.isOptional();
			PrivateMethods.addRemoveJpaManyToOneAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.OPTIONAL, model.isOptional());
		}

		public static void generateJpaJoinColumnAction(JpaEntity entity, JpaJoinColumnModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaFieldModel fieldModel = (JpaFieldModel)model.getParent();
			JpaJoinColumnModel entityModel = entity.getFields().get(fieldModel.getUUID()).getJoinColumnModel();
			// add @JoinColumn annotation
			if (entityModel.isDefaultAttrs() && !model.isDefaultAttrs()) {
				entity.addAction(new JpaJoinColumnAction(model.getUUID(), fieldModel, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_JOIN_COLUMN_ANNOTATION, null));
			}
			// remove @JoinColumn annotation
			if (!entityModel.isDefaultAttrs() && model.isDefaultAttrs()) {
				entity.addAction(new JpaJoinColumnAction(model.getUUID(), fieldModel, ActionType.REMOVE,
						ASTNode.NORMAL_ANNOTATION, DbAnnotationConstants.DB_JOIN_COLUMN_ANNOTATION, null));
			}
			// remove add/remove @JoinColumn action
			if (entityModel.equalsAttrs(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_JOIN_COLUMN_ANNOTATION);
			}
			// if attrs are not default in editable model then delete "remove @JoinColumn" action
			if (!model.isDefaultAttrs()) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_JOIN_COLUMN_ANNOTATION, ActionType.REMOVE);
			}

			// @JoinColumn.name: default ""
			isPrevious = StringUtils.equals(entityModel.getName(), model.getName());
			isDefault = StringUtils.isEmpty(model.getName());
			PrivateMethods.addRemoveJpaJoinColumnAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.NAME, model.getName());
			// @JoinColumn.referencedColumnName: default ""
			isPrevious = StringUtils.equals(entityModel.getReferencedColumnName(), model.getReferencedColumnName());
			isDefault = StringUtils.isEmpty(model.getReferencedColumnName());
			PrivateMethods.addRemoveJpaJoinColumnAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.REFERENCED_COLUMN_NAME, model.getReferencedColumnName());
			// @JoinColumn.unique: default false
			isPrevious = entityModel.isUnique() == model.isUnique();
			isDefault = !model.isUnique();
			PrivateMethods.addRemoveJpaJoinColumnAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.UNIQUE, model.isUnique());
			// @JoinColumn.nullable: default true
			isPrevious = entityModel.isNullable() == model.isNullable();
			isDefault = model.isNullable();
			PrivateMethods.addRemoveJpaJoinColumnAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.NULLABLE, model.isNullable());
			// @JoinColumn.insertable: default true
			isPrevious = entityModel.isInsertable() == model.isInsertable();
			isDefault = model.isInsertable();
			PrivateMethods.addRemoveJpaJoinColumnAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.INSERTABLE, model.isInsertable());
			// @JoinColumn.updatable: default true
			isPrevious = entityModel.isUpdatable() == model.isUpdatable();
			isDefault = model.isUpdatable();
			PrivateMethods.addRemoveJpaJoinColumnAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.UPDATABLE, model.isUpdatable());
			// @JoinColumn.columnDefinition: default ""
			isPrevious = StringUtils.equals(entityModel.getColumnDefinition(), model.getColumnDefinition());
			isDefault = StringUtils.isEmpty(model.getColumnDefinition());
			PrivateMethods.addRemoveJpaJoinColumnAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.COLUMN_DEFINITION, model.getColumnDefinition());
			// @JoinColumn.table: default ""
			isPrevious = StringUtils.equals(entityModel.getTable(), model.getTable());
			isDefault = StringUtils.isEmpty(model.getTable());
			PrivateMethods.addRemoveJpaJoinColumnAction(entity, fieldModel, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.TABLE, model.getTable());
		}

	}

	private static class PrivateMethods {

		public static void addRemoveJpaEntityAction(JpaEntity entity, JpaEntityModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaEntityAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaEntityAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		public static void addRemoveJpaDbEntityAction(JpaEntity entity, JpaEntityModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaDbEntityAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaDbEntityAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		public static void addRemoveJpaTableAction(JpaEntity entity, JpaTableModel model, boolean isPrevious, boolean isDefault,
				int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaTableAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaTableAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		public static void addRemoveJpaFieldAction(JpaEntity entity, JpaFieldModel model, boolean isPrevious, boolean isDefault,
				int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		public static void addRemoveJpaListFieldAction(JpaEntity entity, JpaListFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaListFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaListFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		public static void addRemoveJpaDbColumnAction(JpaEntity entity, JpaFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaDbColumnAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaDbColumnAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		public static void addRemoveJpaNavigationAction(JpaEntity entity, JpaNavigationModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaNavigationAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaNavigationAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveJpaActionsAction(JpaEntity entity, JpaActionsModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaActionsAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaActionsAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveJpaManyToOneFieldAction(JpaEntity entity, JpaManyToOneFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaManyToOneFieldAction(model.getUUID(), model, ActionType.MODIFY, target, key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaManyToOneFieldAction(model.getUUID(), model, ActionType.REMOVE, target, key, null));
			} else {
				entity.removeAction(model.getUUID(), key);
			}
		}

		private static void addRemoveJpaManyToOneAction(JpaEntity entity, JpaFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaManyToOneAction(model.getManyToOneModel().getUUID(), model, ActionType.MODIFY, target,
						key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaManyToOneAction(model.getManyToOneModel().getUUID(), model, ActionType.REMOVE, target,
						key, null));
			} else {
				entity.removeAction(model.getManyToOneModel().getUUID(), key);
			}
		}

		private static void addRemoveJpaJoinColumnAction(JpaEntity entity, JpaFieldModel model, boolean isPrevious,
				boolean isDefault, int target, String key, Object value) {
			if (!isPrevious && !isDefault) {
				entity.addAction(new JpaJoinColumnAction(model.getJoinColumnModel().getUUID(), model, ActionType.MODIFY, target,
						key, value));
			} else if (!isPrevious && isDefault) {
				entity.addAction(new JpaJoinColumnAction(model.getJoinColumnModel().getUUID(), model, ActionType.REMOVE, target,
						key, null));
			} else {
				entity.removeAction(model.getJoinColumnModel().getUUID(), key);
			}
		}

		private static boolean compareUniqueConstraintsDefintions(List<UniqueConstraintDefinition> a,
				List<UniqueConstraintDefinition> b) {
			if (a.size() != b.size()) {
				return false;
			}
			for (int i = 0; i < a.size(); i++) {
				UniqueConstraintDefinition aClass = a.get(i);
				UniqueConstraintDefinition bClass = b.get(i);
				String aJoin = StringUtils.join(aClass.getColumnNames(), ",");
				String bJoin = StringUtils.join(bClass.getColumnNames(), ",");
				if (!StringUtils.equals(aJoin, bJoin) || !StringUtils.equals(aClass.getName(), bClass.getName())) {
					return false;
				}
			}
			return true;
		}

		private static boolean isDefaultUniqueConstraintsDefinition(JpaTableModel model) {
			if (model.getConstraints() == null || model.getConstraints().isEmpty()) {
				return true;
			}
			List<UniqueConstraintDefinition> constraints = model.getConstraints();
			for (UniqueConstraintDefinition constraint : constraints) {
				if ((constraint.getColumnNames() != null && !constraint.getColumnNames().isEmpty())
						|| !StringUtils.isEmpty(constraint.getName())) {
					return false;
				}
			}
			return true;
		}

		private static boolean compareCascadeTypes(CascadeType[] a, CascadeType[] b) {
			if (a.length != b.length) {
				return false;
			}

			for (int i = 0; i < a.length; i++) {
				if (!StringUtils.equals(a[i].toString(), b[i].toString())) {
					return false;
				}
			}
			return true;
		}

		private static boolean compareActionsModels(List<ActionModel> a, List<ActionModel> b) {
			if (a.size() != b.size()) {
				return false;
			}
			for (int i = 0; i < a.size(); i++) {
				ActionModel aClass = a.get(i);
				ActionModel bClass = b.get(i);
				if (!aClass.getActionName().equals(bClass.getActionName()) || !aClass.getAlias().equals(bClass.getAlias())
						|| !aClass.getDisplayName().equals(bClass.getDisplayName()) || (aClass.isGlobal() != bClass.isGlobal())
						|| !StringUtils.equals(aClass.getTargetEntityClassName(), bClass.getTargetEntityClassName())) {
					return false;
				}
			}
			return true;
		}
	}

	public static JpaEntityModel getJpaEntityModel(DbEntityDefinition entityDefinition) {
		JpaEntityModel model = new JpaEntityModel();
		model.init((CodeBasedDbEntityDefinition)entityDefinition);
		return model;
	}

	public static JpaTableModel getJpaTableModel(DbEntityDefinition entityDefinition) {
		JpaTableModel model = new JpaTableModel();
		model.init((CodeBasedDbEntityDefinition)entityDefinition);
		return model;
	}

	/**
	 * Returns list of actions that equal to <b>actionClass</b>
	 * 
	 * @param entity
	 * @param actionClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getActionList(JpaEntity entity, Class<T> actionClass) {
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
		return (List<T>)JpaEntityActionsSorter.INSTANCE.sort((List<AbstractAction>)list);
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
	public static <T> List<T> getActionList(JpaEntity entity, int target, ActionType[] actionTypes) {
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
		return (List<T>)JpaEntityActionsSorter.INSTANCE.sort((List<AbstractAction>)list);
	}

	public static Map<UUID, JpaFieldModel> getJpaFieldsModels(JpaEntityModel parent,
			Map<String, DbFieldDefinition> fieldsDefinitions, List<JpaFieldModel> sortedFields) {
		Map<UUID, JpaFieldModel> map = new HashMap<UUID, JpaFieldModel>();
		if (fieldsDefinitions == null) {
			return map;
		}
		sortedFields.clear();
		Set<String> keySet = fieldsDefinitions.keySet();
		for (String key : keySet) {
			JpaFieldModel model = null;
			SimpleDbColumnFieldDefinition fieldDefinition = (SimpleDbColumnFieldDefinition)fieldsDefinitions.get(key);
			// skip static fields
			if (fieldDefinition.isStaticField()) {
				continue;
			}
			String javaTypeName = fieldDefinition.getJavaTypeName();
			if (javaTypeName.equalsIgnoreCase(Boolean.class.getSimpleName())) {
				model = new JpaBooleanFieldModel(parent);
			} else if (javaTypeName.equalsIgnoreCase("byte[]")) {
				model = new JpaByteFieldModel(parent);
			} else if (javaTypeName.equalsIgnoreCase(Date.class.getSimpleName())) {
				model = new JpaDateFieldModel(parent);
			} else if (javaTypeName.equalsIgnoreCase(Integer.class.getSimpleName())) {
				model = new JpaIntegerFieldModel(parent);
			} else if (javaTypeName.equalsIgnoreCase(List.class.getSimpleName())) {
				model = new JpaListFieldModel(parent);
			} else if (fieldDefinition.getManyToOneDefinition() != null) {
				model = new JpaManyToOneFieldModel(parent);
			} else {
				model = new JpaFieldModel(parent);
			}
			if (model != null) {
				model.init(fieldDefinition);
				map.put(model.getUUID(), model);
				sortedFields.add(model.clone());
			}
		}
		if (!sortedFields.isEmpty()) {
			Collections.sort(sortedFields, new Comparator<JpaFieldModel>() {

				@Override
				public int compare(JpaFieldModel o1, JpaFieldModel o2) {
					if (o1.isKey() && !o2.isKey()) {
						return -1;
					}
					if (!o1.isKey() && o2.isKey()) {
						return 1;
					}
					return o1.getFieldName().compareTo(o2.getFieldName());
				}
			});
		}
		return map;
	}

	public static JpaNavigationModel getJpaNavigationModel(DbEntityDefinition entityDefinition) {
		JpaNavigationModel model = new JpaNavigationModel();
		model.init((CodeBasedDbEntityDefinition)entityDefinition);
		return model;
	}

	public static JpaActionsModel getJpaActionsModel(DbEntityDefinition entityDefinition) {
		JpaActionsModel model = new JpaActionsModel();
		model.init((CodeBasedDbEntityDefinition)entityDefinition);
		return model;
	}

}
