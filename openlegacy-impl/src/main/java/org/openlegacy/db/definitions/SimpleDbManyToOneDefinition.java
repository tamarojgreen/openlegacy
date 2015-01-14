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

package org.openlegacy.db.definitions;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * @author Ivan Bort
 * 
 */
public class SimpleDbManyToOneDefinition implements DbManyToOneDefinition {

	// annotation attrs
	private Class<?> targetEntity = void.class;
	private CascadeType[] cascade = {};
	private FetchType fetch = FetchType.EAGER;
	private boolean optional = true;

	// other
	private String targetEntityClassName = void.class.getSimpleName();

	@Override
	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	@Override
	public CascadeType[] getCascade() {
		return cascade;
	}

	@Override
	public FetchType getFetch() {
		return fetch;
	}

	@Override
	public boolean isOptional() {
		return optional;
	}

	@Override
	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public void setCascade(CascadeType[] cascade) {
		this.cascade = cascade;
	}

	public void setFetch(FetchType fetch) {
		this.fetch = fetch;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

}
