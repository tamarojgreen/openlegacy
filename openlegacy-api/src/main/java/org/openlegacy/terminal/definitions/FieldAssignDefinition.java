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
package org.openlegacy.terminal.definitions;

/**
 * Field assign definition defines fields which should be send to the terminal, in the given context (navigation, etc).
 * 
 * @author Roi Mor
 * 
 */
public interface FieldAssignDefinition {

	String getName();

	String getValue();
}
