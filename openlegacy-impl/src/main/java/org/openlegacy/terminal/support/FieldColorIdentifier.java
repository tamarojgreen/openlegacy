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
package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.services.ScreenIdentifier;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * A simple implementation for a screen identifier
 * 
 */
public class FieldColorIdentifier implements ScreenIdentifier, TerminalPositionContainer, Serializable {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(FieldColorIdentifier.class);

	private TerminalPosition position;
	private Color color;

	public FieldColorIdentifier(TerminalPosition position, Color color) {
		this.position = position;
		this.color = color;
	}

	@Override
	public boolean match(TerminalSnapshot terminalSnapshot) {
		TerminalField field = terminalSnapshot.getField(position);
		if (field != null && field.getColor() == color) {
			if (logger.isTraceEnabled()) {
				logger.trace(MessageFormat.format("Found color {0} identifier on screen", color));
			}
			return true;
		}
		return false;
	}

	@Override
	public TerminalPosition getPosition() {
		return position;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
