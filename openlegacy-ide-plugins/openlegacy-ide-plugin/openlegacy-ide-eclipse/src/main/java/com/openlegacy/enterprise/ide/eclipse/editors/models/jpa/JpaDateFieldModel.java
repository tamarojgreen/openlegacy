package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaDateFieldModel extends JpaFieldModel {

	public JpaDateFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.date");//$NON-NLS-1$
	}

	public JpaDateFieldModel(UUID uuid, NamedObject parent) {
		super(uuid, parent);
		this.javaTypeName = Messages.getString("type.date");//$NON-NLS-1$
	}

	@Override
	public JpaFieldModel clone() {
		JpaDateFieldModel model = new JpaDateFieldModel(uuid, parent);
		model.setModelName(this.getModelName());
		model.setFieldName(this.getFieldName());
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setName(getName());
		model.setUnique(isUnique());
		model.setNullable(isNullable());
		model.setInsertable(isInsertable());
		model.setUpdatable(isUpdatable());
		model.setColumnDefinition(getColumnDefinition());
		model.setTable(getTable());
		model.setLength(getLength());
		model.setPrecision(getPrecision());
		model.setScale(getScale());
		model.setKey(isKey());
		model.setGeneratedValue(isGeneratedValue());

		model.setDisplayName(getDisplayName());
		model.setPassword(isPassword());
		model.setSampleValue(getSampleValue());
		model.setDefaultValue(getDefaultValue());
		model.setHelpText(getHelpText());
		model.setRightToLeft(isRightToLeft());
		model.setInternal(isInternal());
		model.setMainDisplayFiled(isMainDisplayFiled());

		model.initialized = isInitialized();
		return model;
	}

}
