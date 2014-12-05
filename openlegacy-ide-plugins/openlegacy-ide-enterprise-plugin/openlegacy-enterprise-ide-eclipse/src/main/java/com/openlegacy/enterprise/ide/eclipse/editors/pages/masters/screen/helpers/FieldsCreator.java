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
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenPartRemoveAspectAction;
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.openlegacy.ide.eclipse.preview.screen.ScreenPreview;
import org.openlegacy.ide.eclipse.preview.screen.SelectedObject;

import java.text.MessageFormat;
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

	private int getSeedForNewField(NamedObject parent) {
		int seed = 0;
		List<ScreenFieldModel> sortedFields = null;
		if (parent instanceof ScreenEntityModel) {
			sortedFields = ((ScreenEntity)callback.getAbstractEntity()).getSortedFields();
		} else if (parent instanceof ScreenPartModel) {
			sortedFields = ((ScreenPartModel)parent).getSortedFields();
		}
		if (sortedFields == null) {
			return ++seed;
		}
		Pattern p = Pattern.compile(Messages.getString("Field.new") + "(\\d+)$");
		for (ScreenFieldModel field : sortedFields) {
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

	private int getSeedForNewPart() {
		int seed = 0;
		List<ScreenPartModel> parts = ((ScreenEntity)callback.getAbstractEntity()).getSortedParts();
		Pattern p = Pattern.compile("(\\d+)$");
		for (ScreenPartModel part : parts) {
			Matcher matcher = p.matcher(part.getClassName());
			if (matcher.find()) {
				String stringSeed = matcher.group(1);
				if (Integer.parseInt(stringSeed) > seed) {
					seed = Integer.parseInt(stringSeed);
				}
			}
		}
		return ++seed;
	}

	private NamedObject getParentOfSelectedModel() {
		IStructuredSelection selection = callback.getMasterBlockViewerSelection();
		NamedObject parent = ((ScreenEntity)callback.getAbstractEntity()).getEntityModel();
		if (selection.size() == 1) {
			NamedObject namedObject = (NamedObject)selection.getFirstElement();
			if (namedObject instanceof ScreenPartModel) {
				parent = namedObject;
			} else if (namedObject instanceof ScreenFieldModel) {
				parent = ((ScreenFieldModel)namedObject).getParent();
			}
		}
		return parent;
	}

	private void fillNewModel(ScreenFieldModel model) {
		model.setFieldName(Messages.getString("Field.new") + getSeedForNewField(model.getParent()));//$NON-NLS-1$
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			SelectedObject selectedObject = screenPreview.getSelectedObject();
			if (selectedObject != null) {
				model.setFieldName((selectedObject.getFieldName() != null) && !selectedObject.getFieldName().trim().isEmpty() ? selectedObject.getFieldName()
						: model.getFieldName());
				model.setDisplayName((selectedObject.getDisplayName() != null)
						&& !selectedObject.getDisplayName().trim().isEmpty() ? selectedObject.getDisplayName()
						: model.getDisplayName());
				model.setLabelColumn(selectedObject.getLabelColumn() != null ? selectedObject.getLabelColumn() : 0);
				model.setEditable(selectedObject.isEditable());
				if (selectedObject.getFieldRectangle() != null) {
					model.setRow(selectedObject.getFieldRectangle().getRow());
					model.setColumn(selectedObject.getFieldRectangle().getColumn());
					model.setEndRow(selectedObject.getFieldRectangle().getRow() != selectedObject.getFieldRectangle().getEndRow() ? selectedObject.getFieldRectangle().getEndRow()
							: 0);
					model.setEndColumn(selectedObject.getFieldRectangle().getEndColumn());
					model.setSampleValue(selectedObject.getFieldRectangle().getValue());
					model.setRectangle(selectedObject.getFieldRectangle().getRow() != selectedObject.getFieldRectangle().getEndRow());
				}
			}
		}
		if (model instanceof ScreenBooleanFieldModel) {
			((ScreenBooleanFieldModel)model).setTrueValue("");//$NON-NLS-1$
			((ScreenBooleanFieldModel)model).setFalseValue("");//$NON-NLS-1$
		}
	}

	public void createNewField(Class<?> modelClass) {
		if (modelClass == null) {
			return;
		}
		NamedObject parent = getParentOfSelectedModel();
		ScreenEntity entity = ((ScreenEntity)callback.getAbstractEntity());
		ScreenFieldModel newModel = null;
		if (ScreenBooleanFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new ScreenBooleanFieldModel(parent);
		} else if (ScreenDateFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new ScreenDateFieldModel(parent);
		} else if (ScreenEnumFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new ScreenEnumFieldModel(parent);
		} else if (ScreenIntegerFieldModel.class.isAssignableFrom(modelClass)) {
			newModel = new ScreenIntegerFieldModel(parent);
		} else if (ScreenFieldValuesModel.class.isAssignableFrom(modelClass)) {
			newModel = new ScreenFieldValuesModel(parent);
		} else {
			newModel = new ScreenFieldModel(parent);
		}

		if (parent instanceof ScreenEntityModel) {
			entity.addScreenFieldModel(newModel);
		} else if (parent instanceof ScreenPartModel) {
			entity.addScreenFieldModelToPart(newModel);
		}

		fillNewModel(newModel);

		if (ScreenBooleanFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new ScreenBooleanFieldAction(newModel.getUUID(), (ScreenBooleanFieldModel)newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenBooleanFieldActions(entity, (ScreenBooleanFieldModel)newModel);
		} else if (ScreenDateFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new ScreenDateFieldAction(newModel.getUUID(), (ScreenDateFieldModel)newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenDateFieldActions(entity, (ScreenDateFieldModel)newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, newModel);
		} else if (ScreenEnumFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new ScreenEnumFieldAction(newModel.getUUID(), (ScreenEnumFieldModel)newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			// add action that responsible for creating a new enum class
			entity.addAction(new ScreenEnumFieldAction(newModel.getUUID(), (ScreenEnumFieldModel)newModel, ActionType.ADD,
					ASTNode.ENUM_DECLARATION, Constants.ENUM_FIELD_NEW_TYPE_DECLARATION, null));
			ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenEnumFieldActions(entity, (ScreenEnumFieldModel)newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, newModel);
		} else if (ScreenIntegerFieldModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new ScreenIntegerFieldAction(newModel.getUUID(), (ScreenIntegerFieldModel)newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, newModel);
		} else if (ScreenFieldValuesModel.class.isAssignableFrom(modelClass)) {
			entity.addAction(new ScreenFieldValuesAction(newModel.getUUID(), (ScreenFieldValuesModel)newModel, ActionType.ADD,
					ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
			ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenFieldValuesActions(entity, (ScreenFieldValuesModel)newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, newModel);
		} else {
			entity.addAction(new ScreenFieldAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.FIELD_DECLARATION,
					newModel.getFieldName(), null));
			ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, newModel);
			ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, newModel);
		}

		callback.reassignMasterBlockViewerInput(newModel.getUUID());
	}

	public ScreenPartModel createNewScreenPartModel() {
		ScreenEntity entity = ((ScreenEntity)callback.getAbstractEntity());
		int seed = getSeedForNewPart();
		ScreenPartModel newModel = new ScreenPartModel(MessageFormat.format("{0}{1}{2}",//$NON-NLS-1$
				entity.getEntityModel().getClassName(), Messages.getString("ScreenPart.new"), seed));//$NON-NLS-1$

		entity.addScreenPartModel(newModel);
		entity.addAction(new ScreenPartAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.TYPE_DECLARATION,
				newModel.getClassName(), null));
		return newModel;
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
				ScreenEntity entity = ((ScreenEntity)callback.getAbstractEntity());
				if (model instanceof ScreenFieldModel) {
					// remove validation markers
					callback.removeValidationMarkers(model.getUUID());

					NamedObject parent = ((ScreenFieldModel)model).getParent();
					if (parent instanceof ScreenEntityModel) {
						entity.removeScreenFieldModel((ScreenFieldModel)model);
					} else if (parent instanceof ScreenPartModel) {
						entity.removeScreenFieldModelFromPart((ScreenFieldModel)model);
					}
					// try to remove actions
					entity.removeActionsSet(model.getUUID());

					if (((ScreenFieldModel)model).isInitialized()) {
						entity.addAction(new ScreenFieldAction(model.getUUID(), (ScreenFieldModel)model, ActionType.REMOVE,
								ASTNode.FIELD_DECLARATION, Constants.FIELD_DECLARATION, null));
						if (model instanceof ScreenEnumFieldModel) {
							entity.addAction(new ScreenEnumFieldAction(model.getUUID(), (ScreenEnumFieldModel)model,
									ActionType.REMOVE, ASTNode.ENUM_DECLARATION, Constants.ENUM_DECLARATION, null));
						}
					}
				} else if (model instanceof ScreenPartModel) {
					// remove validation markers
					callback.removePartsValidationMarkers();

					entity.removeScreenPartsModel((ScreenPartModel)model);
					// try to remove actions
					entity.removeActionsSet(model.getUUID());
					if (((ScreenPartModel)model).isInitialized()) {
						entity.addAction(new ScreenPartAction(model.getUUID(), (ScreenPartModel)model, ActionType.REMOVE,
								ASTNode.TYPE_DECLARATION, Constants.TYPE_DECLARATION, null));
					}
					entity.addAction(new ScreenPartRemoveAspectAction(model.getUUID(), (ScreenPartModel)model, ActionType.REMOVE,
							Constants.ASPECTJ_FILE, Constants.ASPECTJ_FILE_KEY, entity.getEntityModel().getClassName()));
				}

				callback.reassignMasterBlockViewerInput(null);
			}
		}
	}

}
