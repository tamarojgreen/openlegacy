package org.openlegacy.loaders.support;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.terminal.definitions.SimpleColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleTableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 1)
public class ScreenTableAnnotationLoader implements ClassAnnotationsLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenTable.class;
	}

	public void load(HostEntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		ScreenTable screenTableAnnotation = (ScreenTable)annotation;

		SimpleTableDefinition tableDefinition = new SimpleTableDefinition(containingClass);
		tableDefinition.setStartRow(screenTableAnnotation.startRow());
		tableDefinition.setEndRow(screenTableAnnotation.endRow());
		tableDefinition.setScrollable(screenTableAnnotation.scrollable());

		tableDefinition.setNextScreenAction(ReflectionUtil.newInstance(screenTableAnnotation.nextScreenAction()));
		tableDefinition.setPreviousScreenAction(ReflectionUtil.newInstance(screenTableAnnotation.previousScreenAction()));

		collectColumnsMetadata(containingClass, tableDefinition);
		screenEntitiesRegistry.addTable(tableDefinition);

	}

	private static void collectColumnsMetadata(final Class<?> rowClass, final SimpleTableDefinition tableDefinition) {
		ReflectionUtils.doWithFields(rowClass, new FieldCallback() {

			public void doWith(Field field) {

				if (!field.isAnnotationPresent(ScreenColumn.class)) {
					return;
				}
				ScreenColumn screenColumnAnnotation = field.getAnnotation(ScreenColumn.class);

				SimpleColumnDefinition columnDefinition = new SimpleColumnDefinition(field.getName());
				columnDefinition.setStartColumn(screenColumnAnnotation.startColumn());
				columnDefinition.setEndColumn(screenColumnAnnotation.endColumn());
				columnDefinition.setKey(screenColumnAnnotation.key());
				columnDefinition.setEditable(screenColumnAnnotation.editable());
				tableDefinition.getColumnDefinitions().add(columnDefinition);

				if (screenColumnAnnotation.selectionField()) {
					Assert.isNull(tableDefinition.getRowSelectionDefinition().getSelectionField(),
							"Table can contain only a single selection field:" + rowClass);

					tableDefinition.getRowSelectionDefinition().setSelectionField(field.getName());
				}
			}
		});

	}
}
