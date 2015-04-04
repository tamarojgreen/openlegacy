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
package org.openlegacy.terminal.support.wait_conditions;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.terminal.wait_conditions.WaitCoditionAdapter;
import org.openlegacy.utils.ProxyUtil;

import java.io.Serializable;

public class WaitForNonEmptyField extends WaitCoditionAdapter implements Serializable {

	private static final long serialVersionUID = 1L;
	private Class<?> entityClass;
	private String fieldName;

	public WaitForNonEmptyField(Class<?> entityClass, String fieldName) {
		this.entityClass = entityClass;
		this.fieldName = fieldName;

	}

	@Override
	public boolean continueWait(TerminalSession terminalSession) {

		ScreenEntity currentEntity = terminalSession.getEntity();
		if (currentEntity == null) {
			return false;
		}
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);
		if (ProxyUtil.isClassesMatch(currentEntity.getClass(), entityClass)) {
			String fieldValue = (String)fieldAccessor.getFieldValue(fieldName);
			if (StringUtils.isEmpty(fieldValue)) {
				return true;
			}
		}
		return false;
	}

}
