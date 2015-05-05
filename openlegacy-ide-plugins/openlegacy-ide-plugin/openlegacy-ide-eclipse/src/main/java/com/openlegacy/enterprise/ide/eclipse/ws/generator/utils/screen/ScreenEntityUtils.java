package com.openlegacy.enterprise.ide.eclipse.ws.generator.utils.screen;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenValuesFieldModel;

import org.openlegacy.definitions.BooleanFieldTypeDefinition;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.definitions.EnumFieldTypeDefinition;
import org.openlegacy.definitions.FieldWithValuesTypeDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenTableDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenEntityUtils {

	public static List<ScreenFieldModel> getFields(List<ScreenFieldDefinition> fields, AbstractNamedModel parent) {
		List<ScreenFieldModel> list = new ArrayList<ScreenFieldModel>();
		for (ScreenFieldDefinition definition : fields) {
			ScreenFieldModel model = null;
			if (definition.getFieldTypeDefinition() instanceof BooleanFieldTypeDefinition) {
				model = new ScreenBooleanFieldModel(definition, parent);
			} else if (definition.getFieldTypeDefinition() instanceof DateFieldTypeDefinition) {
				model = new ScreenDateFieldModel(definition, parent);
			} else if (definition.getFieldTypeDefinition() instanceof FieldWithValuesTypeDefinition) {
				model = new ScreenValuesFieldModel(definition, parent);
			} else if (definition.getFieldTypeDefinition() instanceof EnumFieldTypeDefinition) {
				model = new ScreenEnumFieldModel(definition, parent);
			} else {
				if (((SimpleScreenFieldDefinition) definition).getJavaTypeName().equals(Integer.class.getSimpleName())) {
					model = new ScreenIntegerFieldModel(definition, parent);
				} else {
					// string field by default
					model = new ScreenFieldModel(definition, parent);
				}
			}
			if (model != null) {
				list.add(model);
			}
		}
		return list;
	}

	public static List<ScreenPartModel> getParts(CodeBasedScreenEntityDefinition entityDefinition, AbstractNamedModel parent) {
		List<ScreenPartModel> list = new ArrayList<ScreenPartModel>();
		Collection<PartEntityDefinition<ScreenFieldDefinition>> values = entityDefinition.getPartsDefinitions().values();
		for (PartEntityDefinition<ScreenFieldDefinition> partEntityDefinition : values) {
			if (partEntityDefinition instanceof CodeBasedScreenPartDefinition) {
				ScreenPartModel model = new ScreenPartModel((CodeBasedScreenPartDefinition) partEntityDefinition, parent);
				list.add(model);
			}
		}
		return list;
	}

	public static List<ScreenTableModel> getTables(CodeBasedScreenEntityDefinition entityDefinition, AbstractNamedModel parent) {
		List<ScreenTableModel> list = new ArrayList<ScreenTableModel>();
		Collection<ScreenTableDefinition> values = entityDefinition.getTableDefinitions().values();
		for (ScreenTableDefinition screenTableDefinition : values) {
			if (screenTableDefinition instanceof CodeBasedScreenTableDefinition) {
				ScreenTableModel model = new ScreenTableModel((CodeBasedScreenTableDefinition) screenTableDefinition, parent);
				list.add(model);
			}
		}
		return list;
	}

}
