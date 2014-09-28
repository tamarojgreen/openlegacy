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

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

public class DbAnnotationConstants {

	public static final String DB_ENTITY_ANNOTATION = Entity.class.getSimpleName();
	public static final String DB_ENTITY_SUPER_CLASS_ANNOTATION = MappedSuperclass.class.getSimpleName();
	// TODO
	public static final String DB_ACTIONS_ANNOTATION = null;
	public static final String DB_FIELD_ANNOTATION = null;
}
