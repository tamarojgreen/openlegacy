package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.support.rpc.RpcFieldModelNameComparator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcEntityUtils;

import org.eclipse.core.runtime.Assert;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Entity that managed by RpcEntityEditor
 * 
 * @author Ivan Bort
 * 
 */
public class RpcEntity extends AbstractEntity {

	private RpcEntityDefinition entityDefinition;

	private RpcEntityModel entityModel;
	private RpcNavigationModel navigationModel;
	private RpcActionsModel actionsModel;

	private Map<UUID, RpcFieldModel> fields = new HashMap<UUID, RpcFieldModel>();
	private List<RpcFieldModel> sortedFields = new ArrayList<RpcFieldModel>();

	private Map<UUID, RpcPartModel> parts = new HashMap<UUID, RpcPartModel>();
	private List<RpcPartModel> sortedParts = new ArrayList<RpcPartModel>();

	public RpcEntity(RpcEntityDefinition entityDefinition) {
		Assert.isNotNull(entityDefinition, Messages.getString("rpc.error.entity.definition.is.null"));
		this.entityDefinition = entityDefinition;
		init();
	}

	private void init() {
		this.entityModel = RpcEntityUtils.getRpcEntityModel(entityDefinition);
		this.navigationModel = RpcEntityUtils.getRpcNavigationModel(entityDefinition);

		// initialize fields
		if (!this.entityDefinition.getFieldsDefinitions().isEmpty()) {
			this.fields = RpcEntityUtils.getRpcFieldsModels(this.entityModel, this.entityDefinition.getFieldsDefinitions(),
					this.sortedFields);
		}
		// initialize parts
		if (!this.entityDefinition.getPartsDefinitions().isEmpty()) {
			this.parts = RpcEntityUtils.getRpcPartsModels(this.entityModel, this.entityDefinition.getPartsDefinitions(),
					this.sortedParts);
		}

		// initialize actions model
		this.actionsModel = RpcEntityUtils.getRpcActionsModel(this.entityDefinition);
		if (this.actionsModel == null) {
			this.actionsModel = new RpcActionsModel();
		}
	}

	public void addRpcFieldModel(RpcFieldModel newModel) {
		this.fields.put(newModel.getUUID(), newModel.clone());
		this.sortedFields.add(newModel);
		newFieldsCount++;
		setDirty(true);
	}

	public void removeRpcFieldModel(RpcFieldModel model) {
		if (this.fields.containsKey(model.getUUID())) {
			this.fields.remove(model.getUUID());
			this.sortedFields.remove(model);
			newFieldsCount--;
			setDirty(!this.actions.isEmpty() || (newFieldsCount != 0));
		}
	}

	public void addRpcFieldModelToPart(RpcPartModel parent, RpcFieldModel model) {
		getPartByUUID(parent.getUUID()).addRpcFieldModel(model);
		getSortedPartByUUID(parent.getUUID()).addRpcFieldModel(model);
	}

	public void removeRpcFieldModelFromPart(RpcFieldModel model) {
		RpcPartModel parent = (RpcPartModel)model.getParent();
		// need to check in accordance to multiple deletion
		RpcPartModel part = getPartByUUID(parent.getUUID());
		if (part != null) {
			part.removeRpcFieldModel(model);
		}
		RpcPartModel sortedPart = getSortedPartByUUID(parent.getUUID());
		// need to check in accordance to multiple deletion
		if (sortedPart != null) {
			sortedPart.removeRpcFieldModel(model);
		}
	}

	public void addRpcPartModel(RpcPartModel model) {
		this.parts.put(model.getUUID(), model.clone());
		this.sortedParts.add(model);
		newPartsCount++;
		setDirty(true);
	}

	public void removeRpcPartModel(RpcPartModel model) {
		if (this.parts.containsKey(model.getUUID())) {
			this.parts.remove(model.getUUID());
			this.sortedParts.remove(model);
			newPartsCount--;
			setDirty(!this.actions.isEmpty() || (newPartsCount != 0));
		}
	}

	public void addRpcPartModelToPart(RpcPartModel parent, RpcPartModel model) {
		getPartByUUID(parent.getUUID()).addRpcPartModel(model);
		getSortedPartByUUID(parent.getUUID()).addRpcPartModel(model);
	}

	public void removeRpcPartModelFromPart(RpcPartModel model) {
		getPartByUUID(model.getParent().getUUID()).removeRpcPartModel(model);
		getSortedPartByUUID(model.getParent().getUUID()).removeRpcPartModel(model);
	}

	@Override
	public String getEntityFullyQualifiedName() {
		if (entityDefinition != null) {
			return MessageFormat.format("{0}.{1}", entityDefinition.getPackageName(), entityDefinition.getEntityClassName());
		}
		return "";
	}

	public RpcPartModel getSortedPartByUUID(UUID uuid) {
		return findPartInList(this.sortedParts, uuid);
	}

	public RpcPartModel getPartByUUID(UUID uuid) {
		return findPartInMap(this.parts, uuid);
	}

	public RpcEntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

	public RpcEntityModel getEntityModel() {
		return entityModel;
	}

	public RpcNavigationModel getNavigationModel() {
		return navigationModel;
	}

	public Map<UUID, RpcFieldModel> getFields() {
		return fields;
	}

	public List<RpcFieldModel> getSortedFields() {
		Collections.sort(sortedFields, RpcFieldModelNameComparator.INSTANCE);
		return sortedFields;
	}

	public Map<UUID, RpcPartModel> getParts() {
		return parts;
	}

	public List<RpcPartModel> getSortedParts() {
		return sortedParts;
	}

	public RpcActionsModel getActionsModel() {
		return actionsModel;
	}

	public void setActionsModel(RpcActionsModel rpcActionsModel) {
		this.actionsModel = rpcActionsModel;
	}

	private static RpcPartModel findPartInList(List<RpcPartModel> list, UUID uuid) {
		RpcPartModel model = null;
		for (RpcPartModel part : list) {
			if (part.getUUID().equals(uuid)) {
				return part;
			} else {
				model = findPartInList(part.getSortedParts(), uuid);
				if (model != null) {
					return model;
				}
			}
		}
		return model;
	}

	private static RpcPartModel findPartInMap(Map<UUID, RpcPartModel> map, UUID uuid) {
		RpcPartModel model = null;
		for (RpcPartModel part : map.values()) {
			if (part.getUUID().equals(uuid)) {
				return part;
			} else {
				model = findPartInMap(part.getParts(), uuid);
			}
			if (model != null) {
				return model;
			}
		}
		return model;
	}

	/**
	 * the second parameter should be a clone of the first parameter and should be filled with using model.convertFrom(...) method
	 * */
	public void addConvertedRpcFieldModel(RpcFieldModel baseModel, RpcFieldModel convertedModel) {
		this.fields.put(baseModel.getUUID(), baseModel);
		this.sortedFields.add(convertedModel);
		newFieldsCount++;
		setDirty(true);
	}

	/**
	 * the second parameter should be a clone of the first parameter and should be filled with using model.convertFrom(...) method
	 * */
	public void addConvertedRpcFieldModelToPart(RpcFieldModel baseModel, RpcFieldModel convertedModel) {
		RpcPartModel parent = (RpcPartModel)baseModel.getParent();
		getParts().get(parent.getUUID()).addConvertedRpcFieldModel(baseModel, convertedModel);

		List<RpcPartModel> sortedParts = getSortedParts();
		for (RpcPartModel rpcPartModel : sortedParts) {
			if (rpcPartModel.getUUID().equals(parent.getUUID())) {
				rpcPartModel.addConvertedRpcFieldModel(baseModel, convertedModel);
				break;
			}
		}
	}

}
