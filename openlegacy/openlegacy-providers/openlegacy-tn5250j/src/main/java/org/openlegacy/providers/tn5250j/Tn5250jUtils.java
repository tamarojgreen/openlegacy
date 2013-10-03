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
package org.openlegacy.providers.tn5250j;

import org.openlegacy.terminal.Color;

public class Tn5250jUtils {

	public static void applyAttributeToField(Tn5250jTerminalField field, int attribute) {

		Color color = Color.GREEN;
		Color backColor = Color.BLACK;
		boolean underline = false;
		switch (attribute) {
			case 32:
				color = Color.GREEN;
				break;
			case 33:
				backColor = Color.GREEN;
				break;
			case 34:
				color = Color.WHITE;
				break;
			case 35:
				backColor = Color.WHITE;
				break;
			case 36:
				color = Color.GREEN;
				underline = true;
				break;
			case 37:
				backColor = Color.GREEN;
				underline = true;
				break;
			case 38:
				color = Color.WHITE;
				underline = true;
				break;
			case 39:
				color = Color.BLACK;
				break;
			case 40:
			case 42:
				color = Color.RED;
				break;
			case 41:
			case 43:
				backColor = Color.WHITE;
				break;
			case 44:
			case 46:
				color = Color.RED;
				underline = true;
				break;
			case 45:
				backColor = Color.RED;
				underline = true;
				break;
			case 47:
				color = Color.BLACK;
				break;
			case 48:
				color = Color.PURPLE;
				break;
			case 49:
				backColor = Color.AQUA;
				break;
			case 50:
				color = Color.YELLOW;
				break;
			case 51:
				backColor = Color.YELLOW;
				break;
			case 52:
				color = Color.AQUA;
				underline = true;
				break;
			case 53:
				backColor = Color.AQUA;
				underline = true;
				break;
			case 54:
				color = Color.YELLOW;
				underline = true;
				break;
			case 55:
				color = Color.BLACK;
				break;
			case 56:
				color = Color.PINK;
				break;
			case 57:
				backColor = Color.PINK;
				break;
			case 58:
				color = Color.BLUE;
				break;
			case 59:
				backColor = Color.BLUE;
				break;
			case 60:
				color = Color.PINK;
				underline = true;
				break;
			case 61:
				backColor = Color.PINK;
				underline = true;
				break;
			case 62:
				color = Color.BLUE;
				underline = true;
				break;
			case 63:
				color = Color.BLACK;
				break;
			default:
				color = Color.GREEN;
		}
		if (backColor != Color.BLACK) {
			color = Color.BLACK;
		}
		field.setColor(color);
		field.setBackColor(backColor);
		field.setUnderline(underline);
	}

}
