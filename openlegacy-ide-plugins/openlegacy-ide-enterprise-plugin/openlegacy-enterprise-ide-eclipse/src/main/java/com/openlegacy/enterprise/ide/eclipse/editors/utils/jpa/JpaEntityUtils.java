package com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaIdFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaListFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaByteFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaTableModel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
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
			// @Entity.name: default ""
			isPrevious = entity.getEntityModel().getName().equals(model.getName());
			isDefault = StringUtils.isEmpty(model.getName());
			PrivateMethods.addRemoveJpaEntityAction(entity, model, isPrevious, isDefault, ASTNode.NORMAL_ANNOTATION
					| ASTNode.MEMBER_VALUE_PAIR, DbAnnotationConstants.NAME, model.getName());
		}

		public static void generateJpaTableActions(JpaEntity entity, JpaTableModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaTableModel entityModel = entity.getTableModel();
			// add @Table annotation
			if (entityModel.isDefaultModel() && !model.isDefaultModel()) {
				entity.addAction(new JpaTableAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_TABLE_ANNOTATION, null));
			}
			// remove @Table annotation
			if (!entityModel.isDefaultModel() && model.isDefaultModel()) {
				entity.addAction(new JpaTableAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_TABLE_ANNOTATION, null));
			}
			if (entityModel.isModelEqual(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_TABLE_ANNOTATION);
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
			if (entityModel.isDefaultModel() && !model.isDefaultModel()) {
				entity.addAction(new JpaFieldAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_COLUMN_ANNOTATION, null));
			}
			// remove @Column annotation
			if (!entityModel.isDefaultModel() && model.isDefaultModel()) {
				entity.addAction(new JpaFieldAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_COLUMN_ANNOTATION, null));
			}
			// remove add/remove @Column action
			if (entityModel.isModelEqual(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_COLUMN_ANNOTATION);
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
		}

		public static void generateJpaListFieldActions(JpaEntity entity, JpaListFieldModel model) {
			boolean isPrevious = true;
			boolean isDefault = true;
			JpaListFieldModel entityModel = (JpaListFieldModel)entity.getFields().get(model.getUUID());
			// add @OneToMany annotation
			if (entityModel.isDefaultModel() && !model.isDefaultModel()) {
				entity.addAction(new JpaListFieldAction(model.getUUID(), model, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION, null));
			}
			// remove @OneToMany annotation
			if (!entityModel.isDefaultModel() && model.isDefaultModel()) {
				entity.addAction(new JpaListFieldAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.NORMAL_ANNOTATION,
						DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION, null));
			}
			// remove add/remove action
			if (entityModel.isModelEqual(model)) {
				entity.removeAction(model.getUUID(), DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION);
			}
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
			} else {
				model = new JpaFieldModel(parent);
			}
			if (model != null) {
				model.init(fieldDefinition);
				map.put(model.getUUID(), model);
				sortedFields.add(model.clone());
			}
		}
		return map;
	}

}