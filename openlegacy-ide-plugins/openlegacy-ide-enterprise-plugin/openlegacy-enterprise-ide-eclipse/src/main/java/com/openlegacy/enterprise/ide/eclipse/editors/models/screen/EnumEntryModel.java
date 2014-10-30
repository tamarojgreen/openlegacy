package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class EnumEntryModel extends ScreenNamedObject {

	private String name = "";
	private String value = "";
	private String displayName = "";

	public EnumEntryModel(NamedObject parent) {
		super(EnumEntryModel.class.getSimpleName());
		this.parent = parent;
	}

	public EnumEntryModel(UUID uuid, NamedObject parent) {
		super(EnumEntryModel.class.getSimpleName());
		this.uuid = uuid;
		this.parent = parent;
	}

	/**
	 * Method <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> is not supported for this model. Set values
	 * using setters.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedScreenEntityDefinition entityDefinition) is not supported for this model. Set values using setters.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Set values using
	 * setters.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Set values using setters.");//$NON-NLS-1$
	}

	@Override
	public EnumEntryModel clone() {
		EnumEntryModel model = new EnumEntryModel(this.uuid, this.parent);
		model.setName(this.name);
		model.setValue(this.value);
		model.setDisplayName(this.displayName);
		return model;
	}

	public boolean isAllowToSave() {
		return (this.name != null) && !this.name.isEmpty() && (this.value != null) && !this.value.isEmpty()
				&& (this.displayName != null) && !this.displayName.isEmpty();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
