package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;

import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenFieldModel extends AbstractNamedModel {

	private ScreenFieldDefinition definition = null;

	public ScreenFieldModel(ScreenFieldDefinition fieldDefinition, AbstractNamedModel parent) {
		super(fieldDefinition.getName(), parent);
		this.definition = fieldDefinition;
	}

	public ScreenFieldDefinition getDefinition() {
		return definition;
	}

}
