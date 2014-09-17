/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableReference;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenTableDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenTableReferenceDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;

@Component
@Order(3)
public class ScreenTableAnnotationLoader extends AbstractClassAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenTable.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		ScreenTable screenTableAnnotation = (ScreenTable)annotation;

		SimpleScreenTableDefinition tableDefinition = new SimpleScreenTableDefinition(containingClass);
		if (screenTableAnnotation.startRow() < 1) {
			throw (new RegistryException("Table " + containingClass.getSimpleName() + ", start row is outside screen bounds"));
		}
		if (screenTableAnnotation.endRow() < screenTableAnnotation.startRow()) {
			throw (new RegistryException("Table " + containingClass.getSimpleName() + ", end row is small then start row"));
		}
		tableDefinition.setStartRow(screenTableAnnotation.startRow());
		tableDefinition.setEndRow(screenTableAnnotation.endRow());
		tableDefinition.setScrollable(screenTableAnnotation.scrollable());
		tableDefinition.setRowsGap(screenTableAnnotation.rowGaps());
		tableDefinition.setScreensCount(screenTableAnnotation.screensCount());
		if (screenTableAnnotation.filterExpression().length() > 0) {
			tableDefinition.setFilterExpression(screenTableAnnotation.filterExpression());
		}

		tableDefinition.setNextScreenAction(ReflectionUtil.newInstance(screenTableAnnotation.nextScreenAction()));
		tableDefinition.setPreviousScreenAction(ReflectionUtil.newInstance(screenTableAnnotation.previousScreenAction()));

		String name = screenTableAnnotation.name().length() > 0 ? screenTableAnnotation.name()
				: StringUtil.toJavaFieldName(containingClass.getSimpleName());
		tableDefinition.setTableEntityName(name);

		tableDefinition.setTableCollector(screenTableAnnotation.tableCollector());
		collectColumnsMetadata(containingClass, tableDefinition);
		collectTablesReferenceMetadata(containingClass, tableDefinition);

		final OpenLegacyProperties olProperties = getBeanFactory().getBean(OpenLegacyProperties.class);

		Collections.sort(tableDefinition.getColumnDefinitions(), new Comparator<ScreenColumnDefinition>() {

			public int compare(ScreenColumnDefinition column1, ScreenColumnDefinition column2) {
				if (column1.getRowsOffset() != column2.getRowsOffset()) {
					return column1.getRowsOffset() - column2.getRowsOffset();
				}
				if (olProperties.isRightToLeft()) {
					return column2.getStartColumn() - column1.getStartColumn();
				}
				return column1.getStartColumn() - column2.getStartColumn();
			}
		});

		if (tableDefinition.getSelectionColumn() != null && tableDefinition.getKeyFieldNames().size() == 0) {
			throw (new RegistryException("No key column/s defined for table " + containingClass.getSimpleName()));
		}
		screenEntitiesRegistry.addTable(tableDefinition);

	}

	private static void collectTablesReferenceMetadata(final Class<?> rowClass, final SimpleScreenTableDefinition tableDefinition) {

		ReflectionUtils.doWithFields(rowClass, new FieldCallback() {

			public void doWith(Field field) {

				if (!field.isAnnotationPresent(ScreenTableReference.class)) {
					return;
				}
				SimpleScreenTableReferenceDefinition tableReferenceDefinition = new SimpleScreenTableReferenceDefinition(
						field.getName(), field.getAnnotation(ScreenTableReference.class).referredScreen(), rowClass);
				tableDefinition.getTableReferenceDefinitions().add(tableReferenceDefinition);

			}
		});

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
				columnDefinition.setRowsOffset(screenColumnAnnotation.rowsOffset());
				columnDefinition.setKey(screenColumnAnnotation.key());
				columnDefinition.setEditable(screenColumnAnnotation.editable());
				columnDefinition.setColSpan(screenColumnAnnotation.colSpan());
				columnDefinition.setSortIndex(screenColumnAnnotation.sortIndex());
				columnDefinition.setAttribute(screenColumnAnnotation.attribute());

				String displayName = screenColumnAnnotation.displayName().equals(AnnotationConstants.NULL) ? StringUtil.toDisplayName(field.getName())
						: screenColumnAnnotation.displayName();
				columnDefinition.setDisplayName(displayName);
				columnDefinition.setSampleValue(screenColumnAnnotation.sampleValue());

				columnDefinition.setSelectionField(screenColumnAnnotation.selectionField());
				if (columnDefinition.isSelectionField()) {
					columnDefinition.setEditable(true);
				}
				if (screenColumnAnnotation.targetEntity() != ScreenEntity.NONE.class) {
					columnDefinition.setTargetEntity(screenColumnAnnotation.targetEntity());
				}
				columnDefinition.setHelpText(screenColumnAnnotation.helpText());

				if (columnDefinition.isKey() && columnDefinition.isSelectionField()) {
					throw (new RegistryException(MessageFormat.format(
							"A column cannot be defined as both selection and key field. Class: {0}, Column: {1}", rowClass,
							columnDefinition.getName())));
				}

				tableDefinition.getColumnDefinitions().add(columnDefinition);

				if (screenColumnAnnotation.mainDisplayField()) {
					tableDefinition.getMainDisplayFields().add(field.getName());
				}

			}
		});
	}
}
