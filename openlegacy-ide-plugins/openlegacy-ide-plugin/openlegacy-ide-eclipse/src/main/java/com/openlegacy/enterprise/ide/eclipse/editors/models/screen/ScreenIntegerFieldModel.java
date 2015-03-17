package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.definitions.NumericFieldTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenIntegerFieldModel extends ScreenFieldModel {

	private String pattern = "#";

	public ScreenIntegerFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.integer");//$NON-NLS-1$
	}

	/**
	 * @param uuid
	 */
	public ScreenIntegerFieldModel(UUID uuid, NamedObject parent) {
		super(parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.integer");//$NON-NLS-1$
	}

	@Override
	public ScreenIntegerFieldModel clone() {
		ScreenIntegerFieldModel model = new ScreenIntegerFieldModel(this.uuid, this.parent);
		fillModel(model);

		model.setPattern(this.pattern);
		return model;
	}

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		super.init(screenFieldDefinition);
		if (super.isInitialized() && (screenFieldDefinition.getFieldTypeDefinition() instanceof NumericFieldTypeDefinition)) {
			NumericFieldTypeDefinition definition = (NumericFieldTypeDefinition)screenFieldDefinition.getFieldTypeDefinition();
			this.setPattern(definition.getPattern());
		}
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public NamedObject convertFrom(NamedObject model) {
		if (!(model instanceof ScreenFieldModel)) {
			return null;
		}
		super.convertFrom(model);
		ScreenFieldModel screenModel = (ScreenFieldModel)model;
		pattern = screenModel.getFieldValue("pattern") != null ? (String)screenModel.getFieldValue("pattern") : "#";
		return this;
	}

	@Override
	public void fillFieldsMap() {
		super.fillFieldsMap();
		fieldsMap.put("pattern", pattern);
	}

	public boolean isAttrsHasDefaultValues() {
		return pattern.equals("#");
	}
}
