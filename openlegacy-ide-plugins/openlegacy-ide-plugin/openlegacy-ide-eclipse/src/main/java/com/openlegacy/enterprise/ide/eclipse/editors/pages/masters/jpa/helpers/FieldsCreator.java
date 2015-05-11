package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.jpa.helpers;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaByteFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaListFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaManyToOneAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaManyToOneFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaByteFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaManyToOneFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.helpers.IPartsMasterBlockCallback;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa.JpaEntityUtils;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsCreator {

	private IPartsMasterBlockCallback callback;

	public FieldsCreator(IPartsMasterBlockCallback callback) {
		this.callback = callback;
	}

	public void createNewField(Class<?> modelClass) {
		if (modelClass == null) {
			return;
		}
		NamedObject parent = getParentOfSelectedModel();
		JpaEntity entity = (JpaEntity) callback.getAbstractEntity();
		JpaFieldModel newModel = null;
		// create new model
		if (JpaBooleanFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new JpaBooleanFieldModel(parent);
		} else if (JpaByteFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new JpaByteFieldModel(parent);
		} else if (JpaIntegerFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new JpaIntegerFieldModel(parent);
		} else if (JpaDateFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new JpaDateFieldModel(parent);
		} else if (JpaListFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new JpaListFieldModel(parent);
		} else if (JpaManyToOneFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new JpaManyToOneFieldModel(parent);
		} else if (JpaEnumFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new JpaEnumFieldModel(parent);
		} else {
			newModel = new JpaFieldModel(parent);
		}
		// add to parent
		if (parent instanceof JpaEntityModel) {
			entity.addJpaFieldModel(newModel);
		}
		// populate new model
		fillNewModel(newModel);
		// generate relevant actions
		if (JpaBooleanFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new JpaBooleanFieldAction(newModel.getUUID(), (JpaBooleanFieldModel) newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, newModel);
		} else if (JpaByteFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new JpaByteFieldAction(newModel.getUUID(), (JpaByteFieldModel) newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, newModel);
		} else if (JpaIntegerFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new JpaIntegerFieldAction(newModel.getUUID(), (JpaIntegerFieldModel) newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, newModel);
		} else if (JpaDateFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new JpaDateFieldAction(newModel.getUUID(), (JpaDateFieldModel) newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, newModel);
		} else if (JpaListFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new JpaListFieldAction(newModel.getUUID(), (JpaListFieldModel) newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			entity.addAction(new JpaListFieldAction(newModel.getUUID(), (JpaListFieldModel) newModel, ActionType.ADD,
					ASTNode.NORMAL_ANNOTATION, DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION, null));
			JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, newModel);
			JpaEntityUtils.ActionGenerator.generateJpaListFieldActions(entity, (JpaListFieldModel) newModel);
		} else if (JpaManyToOneFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new JpaManyToOneFieldAction(newModel.getUUID(), (JpaManyToOneFieldModel) newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			entity.addAction(new JpaManyToOneAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.NORMAL_ANNOTATION,
					DbAnnotationConstants.DB_MANY_TO_ONE_ANNOTATION, null));
			JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, newModel);
			JpaEntityUtils.ActionGenerator.generateJpaManyToOneAction(entity, newModel.getManyToOneModel());
			JpaEntityUtils.ActionGenerator.generateJpaJoinColumnAction(entity, newModel.getJoinColumnModel());
		} else if (JpaEnumFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new JpaEnumFieldAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.FIELD_DECLARATION,
					newModel.getFieldName(), null));
			// add action that responsible for creating a new enum class
			entity.addAction(new JpaEnumFieldAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.ENUM_DECLARATION,
					Constants.ENUM_FIELD_NEW_TYPE_DECLARATION, null));
			JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, newModel);
			JpaEntityUtils.ActionGenerator.generateJpaEnumFieldActions(entity, (JpaEnumFieldModel) newModel);
		} else {
			entity.addAction(new JpaFieldAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.FIELD_DECLARATION,
					newModel.getFieldName(), null));
			JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, newModel);
		}
		// reassign table viewer
		callback.reassignMasterBlockViewerInput(newModel.getUUID());
	}

	@SuppressWarnings("unchecked")
	public void removeSelectedElements() {
		IStructuredSelection selection = callback.getMasterBlockViewerSelection();
		if (selection.isEmpty()) {
			return;
		}
		Iterator<NamedObject> iterator = selection.iterator();
		while (iterator.hasNext()) {
			NamedObject model = iterator.next();
			if (model != null) {
				JpaEntity entity = ((JpaEntity) callback.getAbstractEntity());
				if (model instanceof JpaFieldModel) {
					// remove validation markers
					callback.removeValidationMarkers(model.getUUID());

					NamedObject parent = ((JpaFieldModel) model).getParent();
					if (parent instanceof JpaEntityModel) {
						entity.removeJpaFieldModel((JpaFieldModel) model);
					}
					// try to remove actions
					entity.removeActionsSet(model.getUUID());

					if (((JpaFieldModel) model).isInitialized()) {
						entity.addAction(new JpaFieldAction(model.getUUID(), model, ActionType.REMOVE, ASTNode.FIELD_DECLARATION,
								Constants.FIELD_DECLARATION, null));
					}
				}

				callback.reassignMasterBlockViewerInput(null);
			}
		}
	}

	private NamedObject getParentOfSelectedModel() {
		IStructuredSelection selection = callback.getMasterBlockViewerSelection();
		NamedObject parent = ((JpaEntity) callback.getAbstractEntity()).getEntityModel();
		if (selection.size() == 1) {
			NamedObject namedObject = (NamedObject) selection.getFirstElement();
			if (namedObject instanceof JpaFieldModel) {
				parent = ((JpaFieldModel) namedObject).getParent();
			}
		}
		return parent;
	}

	private int getSeedForNewField(NamedObject parent) {
		int seed = 0;
		List<JpaFieldModel> sortedFields = null;
		if (parent instanceof JpaEntityModel) {
			sortedFields = ((JpaEntity) callback.getAbstractEntity()).getSortedFields();
		}
		if (sortedFields == null) {
			return ++seed;
		}
		Pattern p = Pattern.compile(Messages.getString("Field.new") + "(\\d+)$");
		for (JpaFieldModel field : sortedFields) {
			Matcher matcher = p.matcher(field.getFieldName());
			if (matcher.find()) {
				String stringSeed = matcher.group(1);
				if (Integer.parseInt(stringSeed) > seed) {
					seed = Integer.parseInt(stringSeed);
				}
			}
		}
		return ++seed;
	}

	private void fillNewModel(JpaFieldModel model) {
		model.setFieldName(Messages.getString("Field.new") + getSeedForNewField(model.getParent()));//$NON-NLS-1$
	}

}
