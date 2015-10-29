/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.rpc.utils;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.RpcPojoFieldAccessor;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.ClassUtils.FindInClassProcessor;

import java.lang.reflect.Field;

public class KeyPartFieldAccessor extends SimpleHierarchyRpcPojoFieldAccessor {

	private static final int FIELD_NAME = 0;
	private static final int PART = 1;
	private FindInClassProcessor processor = new FindInClassProcessor() {

		@Override
		public Object process(Class<?> clazz, Object... args) {
			return localProcess(clazz, args);
		}

	};

	public KeyPartFieldAccessor(Object entity) {
		super(entity);
	}

	@Override
	public RpcPojoFieldAccessor getPartAccessor(String fullPath) {
		try {
			return super.getPartAccessor(fullPath);
		} catch (Exception e) {
			return getPartAccessorViaReflection(fullPath);
		}
	}

	private RpcPojoFieldAccessor getPartAccessorViaReflection(String fullPath) {
		String partName = org.springframework.util.StringUtils.uncapitalize(fullPath.substring(0, fullPath.indexOf('.')));
		Object part = ClassUtils.findInClass(entity.getClass(), processor, partName, entity);
		if (part != null) {
			return new SimpleRpcPojoFieldAccessor(part);
		} else {
			throw (new OpenLegacyRuntimeException(String.format("Unable To find part with %s name", partName)));
		}
	}

	public Object localProcess(Class<?> clazz, Object... args) {
		Field field = null;
		try {
			field = clazz.getField((String)args[FIELD_NAME]);
			if (field != null) {
				return getValue(field, args[PART]);
			}
		} catch (Exception e) {
			try {
				field = clazz.getDeclaredField((String)args[FIELD_NAME]);
				if (field != null) {
					return getValue(field, args[PART]);
				}
			} catch (Exception e1) {
				for (Field partField : ClassUtils.getDeclaredFields(clazz)) {
					args[PART] = getValue(partField, args[PART]);
					Object result = ClassUtils.findInClass(partField.getType(), processor, args);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	private Object getValue(Field field, Object obj) {
		try {
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}
}
