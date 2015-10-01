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

package org.openlegacy.annoations.entity.support;

import org.openlegacy.annotations.entity.Active;
import org.openlegacy.annotations.entity.Deprecated;
import org.openlegacy.annotations.entity.Invalid;
import org.openlegacy.annotations.entity.Paused;

import java.lang.annotation.Annotation;

public enum Status {
	DEFAULT,
	ACTIVE,
	DEPRECATED,
	INVALID,
	PAUSED;

	public boolean isDefault() {
		return this == DEFAULT;
	}

	public boolean isActive() {
		return isDefault() || this == ACTIVE;
	}

	public boolean isExcluded() {
		return this == PAUSED || this == INVALID;
	}

	public boolean isLoggable() {
		return this == DEPRECATED;
	}

	public String getAsString() {
		switch (this) {
			case DEPRECATED:
				return Deprecated.class.getSimpleName();
			case INVALID:
				return Invalid.class.getSimpleName();
			case PAUSED:
				return Paused.class.getSimpleName();
			default:
				return Active.class.getSimpleName();
		}
	}

	public Class<? extends Annotation> getAsClass() {
		switch (this) {
			case DEPRECATED:
				return Deprecated.class;
			case INVALID:
				return Invalid.class;
			case PAUSED:
				return Paused.class;
			default:
				return Active.class;
		}
	}
}
