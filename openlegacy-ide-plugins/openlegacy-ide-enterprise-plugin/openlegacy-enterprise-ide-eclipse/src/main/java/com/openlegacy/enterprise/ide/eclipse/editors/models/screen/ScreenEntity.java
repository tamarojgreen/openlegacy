package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.support.screen.ScreenFieldModelPositionComparator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.screen.ScreenEntityUtils;

import org.eclipse.core.runtime.Assert;
import org.openlegacy.EntityDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Entity that managed by ScreenEntityEditor
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenEntity extends AbstractEntity {

	private ScreenEntityDefinition entityDefinition = null;

	private ScreenEntityModel entityModel;
	private ScreenNavigationModel navigationModel;
	private ScreenIdentifiersModel identifiersModel;
	private ScreenActionsModel actionsModel;

	private Map<UUID, ScreenFieldModel> fields = new HashMap<UUID, ScreenFieldModel>();
	private List<ScreenFieldModel> sortedFields = new ArrayList<ScreenFieldModel>();

	private Map<UUID, ScreenTableModel> tables = new HashMap<UUID, ScreenTableModel>();
	private List<ScreenTableModel> sortedTables = new ArrayList<ScreenTableModel>();

	private Map<UUID, ScreenPartModel> parts = new HashMap<UUID, ScreenPartModel>();
	private List<ScreenPartModel> sortedParts = new ArrayList<ScreenPartModel>();

	private Map<UUID, ChildEntityModel> childEntities = new HashMap<UUID, ChildEntityModel>();
	private List<ChildEntityModel> sortedChildEntities = new ArrayList<ChildEntityModel>();

	// need for drag&drop
	private Map<UUID, ScreenFieldModel> initialFields = new HashMap<UUID, ScreenFieldModel>();
	private Map<UUID, ScreenPartModel> initialParts = new HashMap<UUID, ScreenPartModel>();

	public ScreenEntity(ScreenEntityDefinition screenEntityDefinition) {
		Assert.isNotNull(screenEntityDefinition, Messages.getString("error.entity.creation.definition.is.null"));
		this.entityDefinition = screenEntityDefinition;
		init();
	}

	private void init() {
		this.entityModel = ScreenEntityUtils.getScreenEntityModel(this.entityDefinition);
		this.navigationModel = ScreenEntityUtils.getScreenNavigationModel(this.entityDefinition);
		if (this.navigationModel == null) {
			this.navigationModel = new ScreenNavigationModel();
		}

		if (!this.entityDefinition.getSortedFields().isEmpty()) {
			this.fields = ScreenEntityUtils.getFieldsModels(this.entityDefinition.getSortedFields(), this.sortedFields,
					this.entityModel);
			for (ScreenFieldModel fieldModel : fields.values()) {
				initialFields.put(fieldModel.getUUID(), fieldModel.clone());
			}
		}
		// initialize identifiers model
		this.identifiersModel = ScreenEntityUtils.getScreenIdentifiersModel(this.entityDefinition);
		if (this.identifiersModel == null) {
			this.identifiersModel = new ScreenIdentifiersModel();
		}
		// initialize actions model
		this.actionsModel = ScreenEntityUtils.getScreenActionsModel(this.entityDefinition);
		if (this.actionsModel == null) {
			this.actionsModel = new ScreenActionsModel();
		}
		// initialize tables models
		if (!this.entityDefinition.getTableDefinitions().isEmpty()) {
			this.tables = ScreenEntityUtils.getTablesModels(this.entityDefinition.getTableDefinitions(), this.sortedTables);
		}

		// initialize parts
		if (!this.entityDefinition.getPartsDefinitions().isEmpty()) {
			this.parts = ScreenEntityUtils.getPartsModels(this.entityDefinition, this.sortedParts);
			for (ScreenPartModel partModel : parts.values()) {
				initialParts.put(partModel.getUUID(), partModel.clone());
			}
		}
		// retrieve child entities
		List<EntityDefinition<?>> childEntitiesDefinitions = this.entityDefinition.getChildEntitiesDefinitions();
		if (!childEntitiesDefinitions.isEmpty()) {
			this.childEntities = ScreenEntityUtils.getChildEntityModels(childEntitiesDefinitions, sortedChildEntities);
		}
	}

	/**
	 * @param model
	 */
	public void removeScreenFieldModel(ScreenFieldModel model) {
		if (this.fields.containsKey(model.getUUID())) {
			this.fields.remove(model.getUUID());
			this.sortedFields.remove(model);
			newFieldsCount--;
			setDirty(!this.actions.isEmpty() || (newFieldsCount != 0));
		}
	}

	/**
	 * @param newModel
	 */
	public void addScreenFieldModel(ScreenFieldModel newModel) {
		this.fields.put(newModel.getUUID(), newModel.clone());
		this.sortedFields.add(newModel);
		newFieldsCount++;
		setDirty(true);
	}

	public void addScreenFieldModelToPart(ScreenFieldModel model) {
		ScreenPartModel parent = (ScreenPartModel)model.getParent();
		getParts().get(parent.getUUID()).addScreenFieldModel(model);

		List<ScreenPartModel> sortedParts = getSortedParts();
		for (ScreenPartModel screenPartModel : sortedParts) {
			if (screenPartModel.getUUID().equals(parent.getUUID())) {
				screenPartModel.addScreenFieldModel(model);
				break;
			}
		}
	}

	/**
	 * the second parameter should be a clone of the first parameter and should be filled with using model.convertFrom(...) method
	 * */
	public void addConvertedScreenFieldModel(ScreenFieldModel baseModel, ScreenFieldModel convertedModel) {
		this.fields.put(baseModel.getUUID(), baseModel);
		this.sortedFields.add(convertedModel);
		newFieldsCount++;
		setDirty(true);
	}

	/**
	 * the second parameter should be a clone of the first parameter and should be filled with using model.convertFrom(...) method
	 * */
	public void addConvertedScreenFieldModelToPart(ScreenFieldModel baseModel, ScreenFieldModel convertedModel) {
		ScreenPartModel parent = (ScreenPartModel)baseModel.getParent();
		getParts().get(parent.getUUID()).addConvertedScreenFieldModel(baseModel, convertedModel);

		List<ScreenPartModel> sortedParts = getSortedParts();
		for (ScreenPartModel screenPartModel : sortedParts) {
			if (screenPartModel.getUUID().equals(parent.getUUID())) {
				screenPartModel.addConvertedScreenFieldModel(baseModel, convertedModel);
				break;
			}
		}
	}

	public void removeScreenFieldModelFromPart(ScreenFieldModel model) {
		ScreenPartModel parent = (ScreenPartModel)model.getParent();
		ScreenPartModel partModel = getParts().get(parent.getUUID());
		// need to check in accordance to multiple deletion
		if (partModel != null) {
			partModel.removeScreenFieldModel(model);
		}

		List<ScreenPartModel> sortedParts = getSortedParts();
		for (ScreenPartModel screenPartModel : sortedParts) {
			if (screenPartModel.getUUID().equals(parent.getUUID())) {
				screenPartModel.removeScreenFieldModel(model);
				break;
			}
		}
	}

	public void addScreenColumnModel(ScreenColumnModel newModel) {
		ScreenTableModel parent = (ScreenTableModel)newModel.getParent();
		ScreenTableModel tableModel = this.tables.get(parent.getUUID());
		tableModel.getColumns().put(newModel.getUUID(), newModel.clone());
		tableModel.getSortedColumns().add(newModel);
		for (ScreenTableModel screenTableModel : this.sortedTables) {
			if (screenTableModel.getUUID().equals(tableModel.getUUID())) {
				screenTableModel.getColumns().put(newModel.getUUID(), newModel.clone());
				screenTableModel.getSortedColumns().add(newModel);
				break;
			}
		}

		newColumnsCount++;
		setDirty(true);
	}

	public void removeScreenColumnModel(ScreenColumnModel model) {
		ScreenTableModel parent = (ScreenTableModel)model.getParent();
		ScreenTableModel tableModel = tables.get(parent.getUUID());
		if (tableModel != null) {
			if (tableModel.getColumns().containsKey(model.getUUID())) {
				tableModel.getColumns().remove(model.getUUID());
				tableModel.getSortedColumns().remove(model);
				for (ScreenTableModel screenTableModel : this.sortedTables) {
					if (screenTableModel.getUUID().equals(tableModel.getUUID())) {
						screenTableModel.getColumns().remove(model.getUUID());
						screenTableModel.getSortedColumns().remove(model);
						break;
					}
				}
				newColumnsCount--;
				setDirty(!this.actions.isEmpty() || (newColumnsCount != 0));
			}
		}
	}

	public void addTableActionModel(TableActionModel newModel) {
		ScreenTableModel parent = (ScreenTableModel)newModel.getParent();
		ScreenTableModel tableModel = this.tables.get(parent.getUUID());
		tableModel.getActions().put(newModel.getUUID(), newModel.clone());
		tableModel.getSortedActions().add(newModel);
		for (ScreenTableModel screenTableModel : this.sortedTables) {
			if (screenTableModel.getUUID().equals(tableModel.getUUID())) {
				screenTableModel.getActions().put(newModel.getUUID(), newModel.clone());
				screenTableModel.getSortedActions().add(newModel);
				break;
			}
		}
		newTableActionsCount++;
		setDirty(true);
	}

	public void removeTableActionModel(TableActionModel model) {
		ScreenTableModel parent = (ScreenTableModel)model.getParent();
		ScreenTableModel tableModel = tables.get(parent.getUUID());
		if (tableModel != null) {
			if (tableModel.getActions().containsKey(model.getUUID())) {
				tableModel.getActions().remove(model.getUUID());
				tableModel.getSortedActions().remove(model);
				for (ScreenTableModel screenTableModel : this.sortedTables) {
					if (screenTableModel.getUUID().equals(tableModel.getUUID())) {
						screenTableModel.getActions().remove(model.getUUID());
						screenTableModel.getSortedActions().remove(model);
						break;
					}
				}
				newTableActionsCount--;
				setDirty(!this.actions.isEmpty() || (newTableActionsCount != 0));
			}
		}
	}

	public void addScreenPartModel(ScreenPartModel model) {
		this.parts.put(model.getUUID(), model.clone());
		this.sortedParts.add(model);
		newPartsCount++;
		setDirty(true);
	}

	public void removeScreenPartsModel(ScreenPartModel model) {
		if (this.parts.containsKey(model.getUUID())) {
			this.parts.remove(model.getUUID());
			this.sortedParts.remove(model);
			newPartsCount--;
			setDirty(!this.actions.isEmpty() || (newPartsCount != 0));
		}
	}

	public void addScreenTableModel(ScreenTableModel model) {
		this.tables.put(model.getUUID(), model.clone());
		this.sortedTables.add(model);
		setDirty(true);
	}

	public void removeScreenTableModel(ScreenTableModel model) {
		if (this.tables.containsKey(model.getUUID())) {
			this.tables.remove(model.getUUID());
			this.sortedTables.remove(model);
			setDirty(!this.actions.isEmpty());
		}
	}

	@Override
	public String getEntityFullyQualifiedName() {
		if (entityDefinition != null) {
			return MessageFormat.format("{0}.{1}", entityDefinition.getPackageName(), entityDefinition.getEntityClassName());
		}
		return "";
	}

	public ScreenEntityModel getEntityModel() {
		return entityModel;
	}

	public ScreenNavigationModel getNavigationModel() {
		return navigationModel;
	}

	public Map<UUID, ScreenFieldModel> getFields() {
		return fields;
	}

	public List<ScreenFieldModel> getSortedFields() {
		Collections.sort(sortedFields, ScreenFieldModelPositionComparator.INSTANCE);
		return sortedFields;
	}

	public ScreenEntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

	public ScreenIdentifiersModel getIdentifiersModel() {
		return identifiersModel;
	}

	public ScreenActionsModel getActionsModel() {
		return actionsModel;
	}

	public Map<UUID, ScreenTableModel> getTables() {
		return tables;
	}

	public List<ScreenTableModel> getSortedTables() {
		return sortedTables;
	}

	public Map<UUID, ScreenPartModel> getParts() {
		return parts;
	}

	public List<ScreenPartModel> getSortedParts() {
		return this.sortedParts;
	}

	public Map<UUID, ChildEntityModel> getChildEntities() {
		return this.childEntities;
	}

	public List<ChildEntityModel> getSortedChildEntities() {
		return this.sortedChildEntities;
	}

	public Map<UUID, ScreenFieldModel> getInitialFields() {
		return initialFields;
	}

	public Map<UUID, ScreenPartModel> getInitialParts() {
		return initialParts;
	}

}
