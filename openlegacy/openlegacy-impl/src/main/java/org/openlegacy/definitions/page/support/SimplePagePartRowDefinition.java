package org.openlegacy.definitions.page.support;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.layout.PagePartRowDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimplePagePartRowDefinition implements PagePartRowDefinition {

	private List<FieldDefinition> fields = new ArrayList<FieldDefinition>();

	public List<FieldDefinition> getFields() {
		return fields;
	}

}
