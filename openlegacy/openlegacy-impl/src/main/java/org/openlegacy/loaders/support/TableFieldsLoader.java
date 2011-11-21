package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class TableFieldsLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		Class<?> listType = ReflectionUtil.getListType(field);
		if (listType == null) {
			return false;
		}
		return (screenEntitiesRegistry.getTable(listType) != null);
	}

	@SuppressWarnings("rawtypes")
	public void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		Class<?> listType = ReflectionUtil.getListType(field);

		TableDefinition tableDefinition = screenEntitiesRegistry.getTable(listType);
		if (tableDefinition != null) {
			screenEntitiesRegistry.get(containingClass).getTableDefinitions().put(field.getName(), tableDefinition);
		}

	}
}
