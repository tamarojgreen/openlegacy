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
package org.openlegacy.designtime.rpc.source.parsers;

/**
 * FieldInformationFactory is interface for factory object that generate FieldInformation, from string that holds on token of the
 * variable declaration. Each programming language should have different factory.
 * 
 */

public interface FieldInformationFactory {

	FieldInformation getFieldInformation(Object variableDeclaration, int count);

}