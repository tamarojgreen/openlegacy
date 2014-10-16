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
package org.openlegacy.designtime.db.generators.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

public class DbAnnotationConstants {

	public static final String DB_ENTITY_ANNOTATION = Entity.class.getSimpleName();
	public static final String DB_ENTITY_SUPER_CLASS_ANNOTATION = MappedSuperclass.class.getSimpleName();
	// TODO
	public static final String DB_ACTIONS_ANNOTATION = null;
	public static final String DB_FIELD_ANNOTATION = null;

	public static final String DB_TABLE_ANNOTATION = Table.class.getSimpleName();
	public static final String DB_COLUMN_ANNOTATION = Column.class.getSimpleName();
	public static final String DB_ONE_TO_MANY_ANNOTATION = OneToMany.class.getSimpleName();
	public static final String DB_ID_ANNOTATION = Id.class.getSimpleName();

	// @Entity
	public static final String NAME = "name";
	// @Table
	public static final String CATALOG = "catalog";
	public static final String SCHEMA = "schema";
	public static final String UNIQUE_CONSTRAINTS = "uniqueConstraints";
	// @UniqueConstraint
	public static final String COLUMN_NAMES = "columnNames";
	// @Column
	public static final String UNIQUE = "unique";
	public static final String NULLABLE = "nullable";
	public static final String INSERTABLE = "insertable";
	public static final String UPDATABLE = "updatable";
	public static final String COLUMN_DEFINITION = "columnDefinition";
	public static final String TABLE = "table";
	public static final String LENGTH = "length";
	public static final String PRECISION = "precision";
	public static final String SCALE = "scale";
	// @OneToMany
	public static final String CASCADE = "cascade";
	public static final String FETCH = "fetch";
	public static final String MAPPED_BY = "mappedBy";
	public static final String ORPHAN_REMOVAL = "orphanRemoval";
	public static final String TARGET_ENTITY = "targetEntity";
}
