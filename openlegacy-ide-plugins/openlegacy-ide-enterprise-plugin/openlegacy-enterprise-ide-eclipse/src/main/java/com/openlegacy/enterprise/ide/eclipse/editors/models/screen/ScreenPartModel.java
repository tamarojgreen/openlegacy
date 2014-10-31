package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.support.screen.ScreenFieldModelPositionComparator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.screen.ScreenEntityUtils;

import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

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
public class ScreenPartModel extends ScreenNamedObject {

	// annotation attributes
	private boolean supportTerminalData = false;
	private String name = "";
	private String displayName = "";
	// other
	private String className = "";
	private String previousClassName = "";
	private Map<UUID, ScreenFieldModel> fields = new HashMap<UUID, ScreenFieldModel>();
	private List<ScreenFieldModel> sortedFields = new ArrayList<ScreenFieldModel>();
	private PartPositionModel partPositionModel = null;
	private boolean initialized = false;

	public ScreenPartModel() {
		super(ScreenPart.class.getSimpleName());
		this.partPositionModel = new PartPositionModel(this);
	}

	public ScreenPartModel(UUID uuid) {
		super(ScreenPart.class.getSimpleName());
		this.uuid = uuid;
		this.partPositionModel = new PartPositionModel(this);
	}

	public ScreenPartModel(String className) {
		super(ScreenPart.class.getSimpleName());
		this.className = className;
		this.previousClassName = className;
		this.partPositionModel = new PartPositionModel(this);
	}

	@Override
	public void init(CodeBasedScreenPartDefinition partDefinition) {
		if (partDefinition == null) {
			return;
		}
		this.supportTerminalData = partDefinition.isSupportTerminalData();
		this.name = partDefinition.getPartName();
		this.displayName = partDefinition.getDisplayName();

		List<ScreenFieldDefinition> list = new ArrayList<ScreenFieldDefinition>();
		list.addAll(partDefinition.getSortedFields());
		this.fields = ScreenEntityUtils.getFieldsModels(list, this.sortedFields, this);

		this.className = partDefinition.getClassName();
		this.previousClassName = this.className;
		this.partPositionModel = new PartPositionModel(this);
		this.partPositionModel.init(partDefinition);
		initialized = true;
	}

	@Override
	public ScreenPartModel clone() {
		ScreenPartModel model = new ScreenPartModel(this.uuid);
		model.className = this.className;
		model.previousClassName = this.previousClassName;
		model.setModelName(this.modelName);
		model.setDisplayName(this.displayName);
		model.setName(this.name);
		model.setSupportTerminalData(this.supportTerminalData);
		for (ScreenFieldModel field : this.sortedFields) {
			ScreenFieldModel clone = field.clone();
			model.getFields().put(clone.getUUID(), clone);
			model.getSortedFields().add(clone);
		}
		model.partPositionModel = new PartPositionModel(model);
		model.partPositionModel.setRow(this.partPositionModel.getRow());
		model.partPositionModel.setColumn(this.partPositionModel.getColumn());
		model.partPositionModel.setWidth(this.partPositionModel.getWidth());
		model.initialized = this.initialized;
		return model;
	}

	/**
	 * @param model
	 */
	public void removeScreenFieldModel(ScreenFieldModel model) {
		if (this.fields.containsKey(model.getUUID())) {
			this.fields.remove(model.getUUID());
			this.sortedFields.remove(model);
		}
	}

	/**
	 * @param newModel
	 */
	public void addScreenFieldModel(ScreenFieldModel newModel) {
		this.fields.put(newModel.getUUID(), newModel.clone());
		this.sortedFields.add(newModel);
	}

	public void addConvertedScreenFieldModel(ScreenFieldModel baseModel, ScreenFieldModel convertedModel) {
		this.fields.put(baseModel.getUUID(), baseModel);
		this.sortedFields.add(convertedModel);
	}

	public boolean isSupportTerminalData() {
		return supportTerminalData;
	}

	public void setSupportTerminalData(boolean supportTerminalData) {
		this.supportTerminalData = supportTerminalData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getClassName() {
		return className;
	}

	public Map<UUID, ScreenFieldModel> getFields() {
		return fields;
	}

	public void setFields(Map<UUID, ScreenFieldModel> fields) {
		this.fields = fields;
	}

	public List<ScreenFieldModel> getSortedFields() {
		Collections.sort(sortedFields, ScreenFieldModelPositionComparator.INSTANCE);
		return sortedFields;
	}

	public void setSortedFields(List<ScreenFieldModel> sortedFields) {
		this.sortedFields = sortedFields;
	}

	public PartPositionModel getPartPosition() {
		return this.partPositionModel;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPreviousClassName() {
		return previousClassName;
	}

	public boolean isInitialized() {
		return initialized;
	}

}
