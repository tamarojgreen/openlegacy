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

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.support.AbstractFieldDefinition;

public class SimpleDbFieldDefinition extends AbstractFieldDefinition<DbFieldDefinition> implements DbFieldDefinition {

	private static final long serialVersionUID = 1L;

	public SimpleDbFieldDefinition(String name) {
		super(name, null);
	}

	@Override
	public int compareTo(FieldDefinition o) {
		return 0;
	}

	@Override
	public DbOneToManyDefinition getOneToManyDefinition() {
		return null;
	}

	@Override
	public String getExpression() {
		return null;
	}

	@Override
	public int getKeyIndex() {
		return 1;
	}

	@Override
	public boolean isMainDisplayField() {
		return false;
	}

	@Override
	public boolean isUnique() {
		return false;
	}

	@Override
	public boolean isNullable() {
		return false;
	}

	@Override
	public boolean isInsertable() {
		return false;
	}

	@Override
	public boolean isUpdatable() {
		return false;
	}

	@Override
	public String getColumnDefinition() {
		return null;
	}

	@Override
	public String getTable() {
		return null;
	}

	@Override
	public int getLength() {
		return 0;
	}

	@Override
	public int getPrecision() {
		return 0;
	}

	@Override
	public int getScale() {
		return 0;
	}
}
