package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.annotations.screen.ScreenBooleanField;
import org.openlegacy.definitions.BooleanFieldTypeDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.utils.StringUtil;

import java.util.UUID;

/**
 * Represents @ScreenBooleanField annotation
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenBooleanFieldModel extends ScreenFieldModel {

	private String trueValue = null;
	private String falseValue = null;
	private boolean treatEmptyAsNull = false;

	public ScreenBooleanFieldModel(NamedObject parent) {
		super(ScreenBooleanField.class.getSimpleName(), parent);
		this.javaTypeName = Messages.getString("type.boolean");//$NON-NLS-1$
	}

	/**
	 * @param uuid
	 */
	public ScreenBooleanFieldModel(UUID uuid, NamedObject parent) {
		super(ScreenBooleanField.class.getSimpleName(), parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.boolean");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> is not supported for this model. Use
	 * <code>init(ScreenFieldDefinition screenFieldDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedScreenEntityDefinition entityDefinition) is not supported for this model. Use init(ScreenFieldDefinition screenFieldDefinition) instead.");//$NON-NLS-1$
	}

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		super.init(screenFieldDefinition);
		if (super.isInitialized() && (screenFieldDefinition.getFieldTypeDefinition() instanceof BooleanFieldTypeDefinition)) {
			BooleanFieldTypeDefinition definition = (BooleanFieldTypeDefinition)screenFieldDefinition.getFieldTypeDefinition();
			this.trueValue = StringUtil.stripQuotes(definition.getTrueValue());
			this.falseValue = StringUtil.stripQuotes(definition.getFalseValue());
			this.treatEmptyAsNull = definition.isTreatNullAsEmpty();
		}
	}

	@Override
	public ScreenBooleanFieldModel clone() {
		ScreenBooleanFieldModel model = new ScreenBooleanFieldModel(this.uuid, this.parent);
		fillModel(model);

		model.setTrueValue(this.trueValue);
		model.setFalseValue(this.falseValue);
		model.setTreatEmptyAsNull(this.isTreatEmptyAsNull());
		return model;
	}

	public String getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(String trueValue) {
		this.trueValue = trueValue;
	}

	public String getFalseValue() {
		return falseValue;
	}

	public void setFalseValue(String falseValue) {
		this.falseValue = falseValue;
	}

	public boolean isTreatEmptyAsNull() {
		return treatEmptyAsNull;
	}

	public void setTreatEmptyAsNull(boolean treatEmptyAsNull) {
		this.treatEmptyAsNull = treatEmptyAsNull;
	}

	@Override
	public ScreenFieldModel convertFrom(ScreenFieldModel model) {
		super.convertFrom(model);
		trueValue = model.getFieldValue("trueValue") != null ? (String)model.getFieldValue("trueValue") : "";
		falseValue = model.getFieldValue("falseValue") != null ? (String)model.getFieldValue("falseValue") : "";
		treatEmptyAsNull = model.getFieldValue("treatEmptyAsNull") != null ? (Boolean)model.getFieldValue("treatEmptyAsNull")
				: false;
		return this;
	}

	@Override
	public void fillFieldsMap() {
		super.fillFieldsMap();
		fieldsMap.put("trueValue", trueValue);
		fieldsMap.put("falseValue", falseValue);
		fieldsMap.put("treatEmptyAsNull", treatEmptyAsNull);
	}

}
