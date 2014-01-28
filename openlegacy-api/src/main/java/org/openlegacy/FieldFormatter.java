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
package org.openlegacy;

/**
 * A field formatter format the content of a field text. typically removes certain chars: ' ', '-', etc according to the
 * implementing bean configuration
 * 
 * @author Roi Mor
 */
public interface FieldFormatter {

	String format(String s);
}
