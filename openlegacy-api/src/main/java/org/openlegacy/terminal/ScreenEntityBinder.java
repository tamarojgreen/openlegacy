/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal;

import org.openlegacy.EntityBinder;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.terminal.services.TerminalSendAction;

/**
 * Defines a binder between a screen entity instance from a {@link TerminalSnapshot} and to a {@link TerminalSendAction},
 * typically using field mappings defined using {@link ScreenField} annotations.
 * 
 * @author Roi Mor
 * 
 */
public interface ScreenEntityBinder extends EntityBinder<TerminalSnapshot, TerminalSendAction> {

}
