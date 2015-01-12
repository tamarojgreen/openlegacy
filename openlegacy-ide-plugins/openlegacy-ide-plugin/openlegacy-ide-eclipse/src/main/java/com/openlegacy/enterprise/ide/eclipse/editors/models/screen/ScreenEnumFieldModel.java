package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.EnumEntryModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.IEnumFieldModel;

import org.openlegacy.DisplayItem;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenEnumFieldModel extends ScreenFieldModel implements IEnumFieldModel {

	private Class<?> type = null;
	private String prevJavaTypeName = "";
	private List<EnumEntryModel> entries = new ArrayList<EnumEntryModel>();

	public ScreenEnumFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.enum");//$NON-NLS-1$
	}

	public ScreenEnumFieldModel(UUID uuid, NamedObject parent) {
		super(parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.enum");//$NON-NLS-1$
	}

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		super.init(screenFieldDefinition);
		if (super.isInitialized()) {
			this.prevJavaTypeName = this.javaTypeName;
			SimpleEnumFieldTypeDefinition fieldTypeDefinition = (SimpleEnumFieldTypeDefinition)screenFieldDefinition.getFieldTypeDefinition();
			Map<Object, DisplayItem> map = fieldTypeDefinition.getEnums();
			Set<Object> keySet = map.keySet();
			for (Object key : keySet) {
				EnumEntryModel entryModel = new EnumEntryModel(this);
				entryModel.setName((String)key);
				entryModel.setValue((String)map.get(key).getValue());
				entryModel.setDisplayName((String)map.get(key).getDisplay());
				this.entries.add(entryModel);
			}
		}
	}

	@Override
	public ScreenEnumFieldModel clone() {
		ScreenEnumFieldModel model = new ScreenEnumFieldModel(this.uuid, this.parent);
		fillModel(model);

		model.prevJavaTypeName = this.prevJavaTypeName;

		List<EnumEntryModel> list = new ArrayList<EnumEntryModel>();
		for (EnumEntryModel entry : this.entries) {
			list.add(entry.clone());
		}
		model.getEntries().addAll(list);
		return model;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public String getPrevJavaTypeName() {
		return prevJavaTypeName;
	}

	public void setPrevJavaTypeName(String typeName) {
		if (this.prevJavaTypeName.isEmpty()) {
			this.prevJavaTypeName = typeName;
		}
	}

	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
		setPrevJavaTypeName(javaTypeName);
	}

	@Override
	public List<EnumEntryModel> getEntries() {
		return entries;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ScreenFieldModel convertFrom(ScreenFieldModel model) {
		super.convertFrom(model);
		type = model.getFieldValue("type") != null ? (Class<?>)model.getFieldValue("type") : null;
		prevJavaTypeName = model.getFieldValue("prevJavaTypeName") != null ? (String)model.getFieldValue("prevJavaTypeName") : "";
		entries.addAll(model.getFieldValue("entries") != null ? (List<EnumEntryModel>)model.getFieldValue("entries")
				: new ArrayList<EnumEntryModel>());
		return this;
	}

	@Override
	public void fillFieldsMap() {
		super.fillFieldsMap();
		fieldsMap.put("type", type);
		fieldsMap.put("prevJavaTypeName", prevJavaTypeName);
		fieldsMap.put("entries", entries);
	}

}
