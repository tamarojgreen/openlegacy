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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRectangle;

public class SimpleTerminalRectangle implements TerminalRectangle {

	private TerminalPosition topLeftPosition;
	private TerminalPosition buttomRightPosition;

	public SimpleTerminalRectangle(TerminalPosition topLeftPosition, TerminalPosition buttomRightPosition) {
		this.topLeftPosition = topLeftPosition;
		this.buttomRightPosition = buttomRightPosition;
	}

	public TerminalPosition getTopLeftPosition() {
		return topLeftPosition;
	}

	public TerminalPosition getButtomRightPosition() {
		return buttomRightPosition;
	}

	public boolean contains(TerminalPosition position, boolean includeBorder) {
		if (includeBorder) {
			return topLeftPosition.getRow() <= position.getRow() && topLeftPosition.getColumn() <= position.getColumn()
					&& buttomRightPosition.getRow() >= position.getRow()
					&& buttomRightPosition.getColumn() >= position.getColumn();
		} else {
			return topLeftPosition.getRow() < position.getRow() && topLeftPosition.getColumn() < position.getColumn()
					&& buttomRightPosition.getRow() > position.getRow() && buttomRightPosition.getColumn() > position.getColumn();
		}
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
