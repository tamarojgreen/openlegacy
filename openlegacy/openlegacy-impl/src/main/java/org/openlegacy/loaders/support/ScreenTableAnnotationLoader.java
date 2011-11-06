package org.openlegacy.loaders.support;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.terminal.definitions.SimpleColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleTableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

public class ScreenTableAnnotationLoader implements FieldAnnotationsLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenTable.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(HostEntitiesRegistry entitiesRegistry, String fieldName, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		ScreenTable screenTableAnnotation = (ScreenTable)annotation;

		Class<?> rowClass = null;
		try {
			Field field = containingClass.getDeclaredField(fieldName);
			rowClass = ReflectionUtil.getListType(field);
			if (rowClass == null) {
				throw (new IllegalArgumentException(MessageFormat.format("Row class not declared for List {0}, class:{1}",
						fieldName, containingClass)));
			}
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

		SimpleTableDefinition tableDefinition = new SimpleTableDefinition(rowClass, fieldName);
		tableDefinition.setStartRow(screenTableAnnotation.startRow());
		tableDefinition.setEndRow(screenTableAnnotation.endRow());

		populateColumnsMetadata(rowClass, tableDefinition);
		screenEntitiesRegistry.get(containingClass).getTableDefinitions().put(rowClass, tableDefinition);

	}

	private static void populateColumnsMetadata(Class<?> rowClass, final SimpleTableDefinition tableDefinition) {
		ReflectionUtils.doWithFields(rowClass, new FieldCallback() {

			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

				if (!field.isAnnotationPresent(ScreenColumn.class)) {
					return;
				}
				ScreenColumn screenColumnAnnotation = field.getAnnotation(ScreenColumn.class);

				SimpleColumnDefinition columnDefinition = new SimpleColumnDefinition(field.getName());
				columnDefinition.setStartColumn(screenColumnAnnotation.startColumn());
				columnDefinition.setEndColumn(screenColumnAnnotation.endColumn());
				tableDefinition.getColumnDefinitions().add(columnDefinition);
			}
		});

	}
}
