/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support.binders;

import org.openlegacy.FieldFormatter;
import org.openlegacy.definitions.ListFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleListFieldTypeDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.providers.ScreenFieldsDefinitionProvider;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class ListFieldsBinder implements ScreenEntityBinder {

	@Inject
	private ScreenFieldsDefinitionProvider fieldMappingsProvider;

	@Inject
	private FieldFormatter fieldFormatter;

	public void populateEntity(Object screenEntity, TerminalSnapshot snapshot) {

		ScreenPojoFieldAccessor fieldAccessor = null;

		Class<? extends Object> class1 = ProxyUtil.getOriginalClass(screenEntity.getClass());

		Collection<ScreenFieldDefinition> fieldDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(snapshot, class1);

		for (ScreenFieldDefinition fieldDefinition : fieldDefinitions) {
			if (fieldDefinition.getJavaType() != List.class && fieldDefinition.getJavaType() != String[].class) {
				continue;
			}
			// lazy creation - mostly not used
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			}

			SimpleListFieldTypeDefinition fieldTypeDefinition = (SimpleListFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
			Assert.notNull(fieldTypeDefinition, "A field of type List is defined without @ScreenListField annotation");
			int fieldsInList = fieldTypeDefinition.getCount();
			int[] gapBetweenFields = fieldTypeDefinition.getGaps();
			TerminalPosition position = fieldDefinition.getPosition();

			int skip = gapBetweenFields.length == 1 ? 0 : 1;
			List<String> members = new ArrayList<String>();

			for (int i = 0; i < fieldsInList - 1; i++) {
				members.add(snapshot.getText(position, fieldTypeDefinition.getFieldLength()).trim());
				position = SnapshotUtils.moveBy(position, (gapBetweenFields[i * skip]), snapshot.getSize());
			}
			String text = snapshot.getText(position, fieldTypeDefinition.getFieldLength());
			members.add(fieldFormatter.format(text));
			if (fieldDefinition.getJavaType() == List.class) {
				fieldAccessor.setFieldValue(fieldDefinition.getName(), members);
			} else {
				fieldAccessor.setFieldValue(fieldDefinition.getName(), members.toArray());
			}

		}

	}

	@SuppressWarnings("unchecked")
	public void populateSendAction(TerminalSendAction sendAction, TerminalSnapshot snapshot, Object entity) {
		if (entity == null) {
			return;
		}
		Assert.isTrue(entity instanceof ScreenEntity, "screen entity must implement ScreenEntity interface");

		ScreenEntity screenEntity = (ScreenEntity)entity;

		Collection<ScreenFieldDefinition> fieldDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(snapshot,
				screenEntity.getClass());

		if (fieldDefinitions == null) {
			return;
		}

		ScreenPojoFieldAccessor fieldAccessor = null;

		for (ScreenFieldDefinition fieldDefinition : fieldDefinitions) {
			// lazy creation - mostly not used
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			}

			if (fieldDefinition.getJavaType() != List.class && fieldDefinition.getJavaType() != String[].class) {
				continue;
			}
			ListFieldTypeDefinition fieldTypeDefinition = (ListFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
			Assert.notNull(fieldTypeDefinition, "A field of type List is defined without @ScreenListField annotation");
			TerminalPosition position = fieldDefinition.getPosition();

			int gaps[] = fieldTypeDefinition.getGaps();
			int skip = gaps.length == 1 ? 0 : 1;
			List<String> fieldValue;

			if (fieldDefinition.getJavaType() == List.class) {
				fieldValue = (List<String>)fieldAccessor.getFieldValue(fieldDefinition.getName());
			} else {
				fieldValue = Arrays.asList((String[])fieldAccessor.getFieldValue(fieldDefinition.getName()));
			}

			for (int i = 0; i < fieldTypeDefinition.getCount(); i++) {
				TerminalField field = snapshot.getField(SimpleTerminalPosition.newInstance(position.getRow(),
						position.getColumn()));

				field.setValue(fieldFormatter.format(fieldValue.get(i)));
				sendAction.getModifiedFields().add(field);

				if (i < fieldTypeDefinition.getCount() - 1) {
					position = SnapshotUtils.moveBy(position, (gaps[i * skip]), snapshot.getSize());
				}
			}

		}

	}
}
