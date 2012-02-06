package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.definitions.SimpleScreenColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenTableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 1)
public class ScreenTableAnnotationLoader extends AbstractClassAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenTable.class;
	}

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		ScreenTable screenTableAnnotation = (ScreenTable)annotation;

		SimpleScreenTableDefinition tableDefinition = new SimpleScreenTableDefinition(containingClass);
		tableDefinition.setStartRow(screenTableAnnotation.startRow());
		tableDefinition.setEndRow(screenTableAnnotation.endRow());
		tableDefinition.setScrollable(screenTableAnnotation.scrollable());

		tableDefinition.setNextScreenAction(ReflectionUtil.newInstance(screenTableAnnotation.nextScreenAction()));
		tableDefinition.setPreviousScreenAction(ReflectionUtil.newInstance(screenTableAnnotation.previousScreenAction()));

		String name = screenTableAnnotation.name().length() > 0 ? screenTableAnnotation.name()
				: StringUtil.toJavaFieldName(containingClass.getSimpleName());
		tableDefinition.setTableEntityName(name);

		tableDefinition.setTableCollector(screenTableAnnotation.tableCollector());
		collectColumnsMetadata(containingClass, tableDefinition);
		screenEntitiesRegistry.addTable(tableDefinition);

	}

	private static void collectColumnsMetadata(final Class<?> rowClass, final SimpleScreenTableDefinition tableDefinition) {
		ReflectionUtils.doWithFields(rowClass, new FieldCallback() {

			public void doWith(Field field) {

				if (!field.isAnnotationPresent(ScreenColumn.class)) {
					return;
				}
				ScreenColumn screenColumnAnnotation = field.getAnnotation(ScreenColumn.class);

				SimpleScreenColumnDefinition columnDefinition = new SimpleScreenColumnDefinition(field.getName());
				columnDefinition.setStartColumn(screenColumnAnnotation.startColumn());
				columnDefinition.setEndColumn(screenColumnAnnotation.endColumn());
				columnDefinition.setKey(screenColumnAnnotation.key());
				columnDefinition.setEditable(screenColumnAnnotation.editable());
				String displayName = screenColumnAnnotation.displayName().length() > 0 ? screenColumnAnnotation.displayName()
						: StringUtil.toDisplayName(field.getName());
				columnDefinition.setDisplayName(displayName);
				columnDefinition.setSampleValue(screenColumnAnnotation.sampleValue());

				columnDefinition.setSelectionField(screenColumnAnnotation.selectionField());

				tableDefinition.getColumnDefinitions().add(columnDefinition);

			}
		});
		Collections.sort(tableDefinition.getColumnDefinitions());
	}
}
