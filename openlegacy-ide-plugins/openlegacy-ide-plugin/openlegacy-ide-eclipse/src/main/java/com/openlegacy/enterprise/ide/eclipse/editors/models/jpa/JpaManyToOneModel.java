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

package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.db.definitions.DbManyToOneDefinition;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @author Ivan Bort
 * 
 */
public class JpaManyToOneModel extends JpaNamedObject implements IJpaNamedObject {

	// annotation attributes
	private Class<?> targetEntity = void.class;
	private CascadeType[] cascade = {};
	private FetchType fetch = FetchType.EAGER;
	private boolean optional = true;

	// other
	private String targetEntityClassName = void.class.getSimpleName();

	public JpaManyToOneModel(NamedObject parent) {
		super(ManyToOne.class.getSimpleName());
		this.parent = parent;
	}

	public JpaManyToOneModel(UUID uuid, NamedObject parent) {
		super(ManyToOne.class.getSimpleName());
		this.parent = parent;
		this.uuid = uuid;
	}

	public void init(DbManyToOneDefinition manyToOneDefinition) {
		if (manyToOneDefinition == null) {
			return;
		}
		targetEntity = manyToOneDefinition.getTargetEntity() == null ? void.class : manyToOneDefinition.getTargetEntity();
		targetEntityClassName = manyToOneDefinition.getTargetEntityClassName();
		cascade = manyToOneDefinition.getCascade();
		fetch = manyToOneDefinition.getFetch();
		optional = manyToOneDefinition.isOptional();

	}

	@Override
	public JpaManyToOneModel clone() {
		JpaManyToOneModel model = new JpaManyToOneModel(uuid, parent);
		model.setTargetEntity(targetEntity);
		model.setTargetEntityClassName(targetEntityClassName);
		model.setCascade(cascade.clone());
		model.setFetch(fetch);
		model.setOptional(optional);
		return model;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public CascadeType[] getCascade() {
		return cascade;
	}

	public void setCascade(CascadeType[] cascade) {
		this.cascade = cascade;
	}

	public FetchType getFetch() {
		return fetch;
	}

	public void setFetch(FetchType fetch) {
		this.fetch = fetch;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

	@Override
	public boolean isDefaultAttrs() {
		return (void.class.getSimpleName().equalsIgnoreCase(targetEntityClassName) || StringUtils.isEmpty(targetEntityClassName))
				&& cascade.length == 0 && FetchType.EAGER.equals(fetch) && optional;
	}

	@Override
	public boolean equalsAttrs(IJpaNamedObject object) {
		if (object instanceof JpaManyToOneModel) {
			JpaManyToOneModel model = (JpaManyToOneModel)object;
			boolean cascadeEqual = isCascadeEqual(model.getCascade());
			return cascadeEqual && StringUtils.equals(targetEntityClassName, model.getTargetEntityClassName())
					&& fetch.equals(model.getFetch()) && optional == model.isOptional();
		}
		return false;
	}

	private boolean isCascadeEqual(CascadeType[] modelCascade) {
		if (cascade.length != modelCascade.length) {
			return false;
		}
		for (int i = 0; i < cascade.length; i++) {
			if (!StringUtils.equals(cascade[i].toString(), modelCascade[i].toString())) {
				return false;
			}
		}
		return true;
	}

}
