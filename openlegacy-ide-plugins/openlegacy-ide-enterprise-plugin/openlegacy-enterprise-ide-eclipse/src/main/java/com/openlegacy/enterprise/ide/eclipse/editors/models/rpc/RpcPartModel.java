package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.support.rpc.RpcFieldModelNameComparator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcEntityUtils;

import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcPartDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcPartModel extends RpcNamedObject {

	private String className = "";
	private String previousClassName = "";
	private Map<UUID, RpcFieldModel> fields = new HashMap<UUID, RpcFieldModel>();
	private List<RpcFieldModel> sortedFields = new ArrayList<RpcFieldModel>();
	private Map<UUID, RpcPartModel> parts = new HashMap<UUID, RpcPartModel>();
	private List<RpcPartModel> sortedParts = new ArrayList<RpcPartModel>();

	private RpcActionsModel actionsModel;

	// annotation attributes
	private String displayName = "";
	private String name = "";
	private int count = 1;

	public RpcPartModel(RpcNamedObject parent) {
		super(RpcPart.class.getSimpleName(), parent);
	}

	public RpcPartModel(UUID uuid, RpcNamedObject parent) {
		super(RpcPart.class.getSimpleName(), parent);
		this.uuid = uuid;
	}

	public RpcPartModel(String className, RpcNamedObject parent) {
		super(RpcPart.class.getSimpleName(), parent);
		this.className = className;
		this.previousClassName = className;
	}

	@Override
	public void init(CodeBasedRpcPartDefinition partDefinition) {
		if (partDefinition == null) {
			return;
		}
		this.name = partDefinition.getPartName();
		this.displayName = partDefinition.getDisplayName();
		this.count = partDefinition.getCount();

		this.fields = RpcEntityUtils.getRpcFieldsModels(this, partDefinition.getFieldsDefinitions(), this.sortedFields);
		this.parts = RpcEntityUtils.<RpcPartEntityDefinition> getRpcPartsModels(this, partDefinition.getInnerPartsDefinitions(),
				this.sortedParts);

		actionsModel = new RpcActionsModel(this);
		actionsModel.init(partDefinition);

		this.className = partDefinition.getClassName();
		this.previousClassName = this.className;
	}

	@Override
	public RpcPartModel clone() {
		// when cloning, innerBranchesCount should not be modified in parent
		int count = ((RpcNamedObject)this.parent).getInnerBranchesCount();

		RpcPartModel model = new RpcPartModel(this.uuid, (RpcNamedObject)this.parent);
		((RpcNamedObject)this.parent).setInnerBranchesCount(count);
		model.setTreeLevel(this.getTreeLevel());
		model.setTreeBranch(this.getTreeBranch());
		model.className = this.className;
		model.previousClassName = this.previousClassName;
		model.setModelName(this.modelName);
		model.setDisplayName(this.displayName);
		model.setName(this.name);
		model.setCount(this.count);
		model.setActionsModel(this.actionsModel.clone());

		for (RpcFieldModel field : this.sortedFields) {
			RpcFieldModel clone = field.clone();
			model.getFields().put(clone.getUUID(), clone);
			model.getSortedFields().add(clone);
		}
		for (RpcPartModel part : this.sortedParts) {
			RpcPartModel clone = part.clone();
			model.getParts().put(clone.getUUID(), clone);
			model.getSortedParts().add(clone);
		}
		return model;
	}

	public void addRpcFieldModel(RpcFieldModel newModel) {
		this.fields.put(newModel.getUUID(), newModel.clone());
		this.sortedFields.add(newModel);
	}

	public void removeRpcFieldModel(RpcFieldModel model) {
		if (this.fields.containsKey(model.getUUID())) {
			this.fields.remove(model.getUUID());
			this.sortedFields.remove(model);
		}
	}

	public void addRpcPartModel(RpcPartModel model) {
		this.parts.put(model.getUUID(), model.clone());
		this.sortedParts.add(model);
	}

	public void removeRpcPartModel(RpcPartModel model) {
		if (this.parts.containsKey(model.getUUID())) {
			this.parts.remove(model.getUUID());
			this.sortedParts.remove(model);
		}
	}

	public Map<UUID, RpcFieldModel> getFields() {
		return this.fields;
	}

	public List<RpcFieldModel> getSortedFields() {
		Collections.sort(this.sortedFields, RpcFieldModelNameComparator.INSTANCE);
		return this.sortedFields;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPreviousClassName() {
		return previousClassName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public void setActionsModel(RpcActionsModel actionsModel) {
		this.actionsModel = actionsModel;
	}

}
