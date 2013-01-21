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
package org.openlegacy.terminal.support.wait_conditions;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.wait_conditions.WaitCoditionAdapter;
import org.openlegacy.terminal.wait_conditions.WaitConditionFactory;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class DefaultWaitConditionFactory implements WaitConditionFactory, Serializable {

	private static final long serialVersionUID = 1L;

	private long defaultWaitInterval;
	private long defaultWaitTimeout;

	/**
	 * Invoke the given waitClass constructor which matches the matching argument types
	 */
	@SuppressWarnings("unchecked")
	public <T extends WaitCoditionAdapter> T create(Class<T> waitClass, Object... args) {
		WaitCoditionAdapter instance;
		try {
			Class<?>[] argTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			Constructor<T> constructor = waitClass.getConstructor(argTypes);
			instance = constructor.newInstance(args);
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}
		instance.setWaitInterval(defaultWaitInterval);
		instance.setWaitTimeout(defaultWaitTimeout);

		return (T)instance;
	}

	public void setDefaultWaitInterval(long defaultWaitInterval) {
		this.defaultWaitInterval = defaultWaitInterval;
	}

	public void setDefaultWaitTimeout(long defaultWaitTimeout) {
		this.defaultWaitTimeout = defaultWaitTimeout;
	}
}
