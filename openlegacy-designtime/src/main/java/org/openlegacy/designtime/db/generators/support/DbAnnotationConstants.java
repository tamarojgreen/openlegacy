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

import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.annotations.db.DbEntity;
import org.openlegacy.annotations.db.DbNavigation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

public class DbAnnotationConstants {

	public static final String DB_JPA_ENTITY_ANNOTATION = Entity.class.getSimpleName();
	public static final String DB_ENTITY_ANNOTATION = DbEntity.class.getSimpleName();
	public static final String DB_ENTITY_SUPER_CLASS_ANNOTATION = MappedSuperclass.class.getSimpleName();
	public static final String DB_ACTIONS_ANNOTATION = DbActions.class.getSimpleName();
	// TODO
	public static final String DB_FIELD_ANNOTATION = null;

	public static final String DB_TABLE_ANNOTATION = Table.class.getSimpleName();
	public static final String DB_JPA_COLUMN_ANNOTATION = Column.class.getSimpleName();
	public static final String DB_COLUMN_ANNOTATION = DbColumn.class.getSimpleName();
	public static final String DB_ONE_TO_MANY_ANNOTATION = OneToMany.class.getSimpleName();
	public static final String DB_ID_ANNOTATION = Id.class.getSimpleName();
	public static final String DB_GENERATED_VALUE_ANNOTATION = GeneratedValue.class.getSimpleName();

	public static final String DB_NAVIGATION_ANNOTATION = DbNavigation.class.getSimpleName();

	public static final String DB_MANY_TO_ONE_ANNOTATION = ManyToOne.class.getSimpleName();
	public static final String DB_JOIN_COLUMN_ANNOTATION = JoinColumn.class.getSimpleName();

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
	// @DbNavigation
	public static final String CATEGORY = "category";
	public static final String ACTIONS = "actions";
	// @Action
	public static final String ACTION = "action";
	public static final String DISPLAY_NAME = "displayName";
	public static final String GLOBAL = "global";
	public static final String ALIAS = "alias";

	public static final String WINDOW = "window";
	public static final String CHILD = "child";

	public static final String SAMPLE_VALUE = "sampleValue";
	public static final String DEFAULT_VALUE = "defaultValue";
	public static final String HELP_TEXT = "helpText";
	public static final String PASSWORD = "password";
	public static final String RIGHT_TO_LEFT = "rightToLeft";
	public static final String INTERNAL = "internal";
	public static final String MAIN_DISPLAY_FIELD = "mainDisplayField";

	public static final String OPTIONAL = "optional";
	public static final String REFERENCED_COLUMN_NAME = "referencedColumnName";

}
