package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.utils.screen.ScreenEntityUtils;

import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenPartModel extends AbstractPartModel {

	private CodeBasedScreenPartDefinition definition = null;

	public ScreenPartModel(CodeBasedScreenPartDefinition partEntityDefinition, AbstractNamedModel parent) {
		super(partEntityDefinition.getPartName(), parent);
		this.definition = partEntityDefinition;

		// populate children
		List<ScreenFieldDefinition> sortedFields = partEntityDefinition.getSortedFields();
		if (sortedFields != null && !sortedFields.isEmpty()) {
			children.addAll(ScreenEntityUtils.getFields(sortedFields, this));
		}
	}

	public CodeBasedScreenPartDefinition getDefinition() {
		return definition;
	}

}
