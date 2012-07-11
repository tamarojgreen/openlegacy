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
package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.internal.GXClientScreen;
import com.sabratec.applinx.common.runtime.GXScreenPosition;
import com.sabratec.applinx.common.runtime.field.GXIField;
import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractSnapshot;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ApxTerminalSnapshot extends AbstractSnapshot {

	private static final long serialVersionUID = 1L;

	private final GXRuntimeScreen screen;

	public ApxTerminalSnapshot(GXRuntimeScreen screen) {
		this.screen = screen;
	}

	@Override
	protected String initText() {
		return new String(screen.getBuffers().getTextBuffer());
	}

	public Object getDelegate() {
		return new GXClientScreen(screen);
	}

	@Override
	protected ScreenSize initScreenSize() {
		return new SimpleScreenSize(screen.getSize().getHeight(), screen.getSize().getWidth());
	}

	@Override
	protected List<TerminalPosition> initFieldSeperators() {
		@SuppressWarnings("unchecked")
		List<GXScreenPosition> attributes = screen.getAttributePositions();

		List<TerminalPosition> fieldSeperators = new ArrayList<TerminalPosition>();
		for (Object object : attributes) {
			fieldSeperators.add(ApxPositionUtil.toScreenPosition((GXScreenPosition)object));
		}
		return fieldSeperators;

	}

	@Override
	protected TerminalPosition initCursorPosition() {
		return ApxPositionUtil.toScreenPosition(screen.getCursorPosition());
	}

	public Integer getSequence() {
		return screen.getSequentialNumber();
	}

	public String getCommand() {
		return null;
	}

	@Override
	protected List<TerminalField> initFields() {
		List<TerminalField> fields = new ArrayList<TerminalField>();

		@SuppressWarnings("unchecked")
		Collection<GXIField> apxFields = screen.getFields();

		for (Iterator<GXIField> iterator = apxFields.iterator(); iterator.hasNext();) {
			GXIField apxField = iterator.next();

			ApxTerminalField field = new ApxTerminalField(apxField);

			List<TerminalPosition> fieldSeperators = getFieldSeperators();

			// gather all read-only fields which has no separator between them.
			// the snapshot should not split read-only field unless defined that way by the host
			TerminalPosition fieldEndPosition = calcFieldEndAttribute(apxField);
			while (!fieldSeperators.contains(fieldEndPosition)) {
				if (fieldEndPosition.getColumn() >= screen.getSize().getWidth()) {
					break;
				}
				if (!iterator.hasNext()) {
					break;
				}
				if (field.isEditable() != !apxField.isProtected()) {
					break;
				}
				if (apxField.getPosition().getRow() != field.getPosition().getRow()) {
					break;
				}

				apxField = iterator.next();
				fieldEndPosition = calcFieldEndAttribute(apxField);
				field.setValue(field.getValue() + apxField.getContent(), false);
				field.setLength(field.getValue().length());
			}
			fields.add(field);
		}

		return fields;
	}

	private static SimpleTerminalPosition calcFieldEndAttribute(GXIField apxField) {
		return new SimpleTerminalPosition(apxField.getPosition().getRow(), apxField.getPosition().getColumn()
				+ apxField.getLength());
	}

}
