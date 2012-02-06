package org.openlegacy.designtime.generators;

import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedDefinitionUtils;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;

import java.util.Map;

public class CodeBasedScreenPartDefinition implements ScreenPartEntityDefinition {

	private ScreenPojoCodeModel codeModel;
	private Map<String, ScreenFieldDefinition> fields;

	public CodeBasedScreenPartDefinition(ScreenPojoCodeModel codeModel) {
		this.codeModel = codeModel;
	}

	public Class<?> getPartClass() {
		throw (new UnsupportedOperationException("Code based screen part does not support this method"));
	}

	public Map<String, ScreenFieldDefinition> getFieldsDefinitions() {
		if (fields == null) {
			fields = CodeBasedDefinitionUtils.getFieldsFromCodeModel(codeModel, codeModel.getEntityName());
		}
		return fields;
	}

	public String getPartName() {
		return codeModel.getEntityName();
	}

}
