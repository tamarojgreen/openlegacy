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
package org.openlegacy.providers.applinx;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.openlegacy.terminal.Color;

import com.sabratec.applinx.baseobject.GXBaseObjectConstants;
import com.sabratec.applinx.common.designtime.message.GXImportRequest;
import com.sabratec.applinx.common.designtime.model.GXIApplicationContext;

public class ApxUtils {

	public static void importRepository(ApxServerLoader apxServerLoader, URL importFile) throws Exception {
		GXIApplicationContext apxApplicationContext = apxServerLoader.getServer().getApplications().get(0);
		GXImportRequest importRequest = new GXImportRequest();
		importRequest.setApplicationName(apxApplicationContext.getName());
		importRequest.setFileContent(IOUtils.toByteArray(importFile.openStream()));
		apxApplicationContext.importEntities(importRequest, null);
	}

	public static Color convertForeColor(int apxColor) {
		Color color = convertColor(apxColor);
		return (color != null ? color : Color.GREEN);
	}

	public static Color convertBackColor(int apxColor) {
		Color color = convertColor(apxColor);
		return (color != null ? color : Color.BLACK);
	}

	private static Color convertColor(int apxColor) {
		switch (apxColor) {
			case GXBaseObjectConstants.GX_FIELD_COLOR_AQUA:
				return Color.AQUA;
			case GXBaseObjectConstants.GX_FIELD_COLOR_BLACK:
				return Color.BLACK;
			case GXBaseObjectConstants.GX_FIELD_COLOR_BLUE:
				return Color.BLUE;
			case GXBaseObjectConstants.GX_FIELD_COLOR_BROWN:
				return Color.BROWN;
			case GXBaseObjectConstants.GX_FIELD_COLOR_GRAY:
				return Color.GRAY;
			case GXBaseObjectConstants.GX_FIELD_COLOR_GREEN:
				return Color.GREEN;
			case GXBaseObjectConstants.GX_FIELD_COLOR_LIGHT_AQUA:
				return Color.LIGHT_AQUA;
			case GXBaseObjectConstants.GX_FIELD_COLOR_LIGHT_BLUE:
				return Color.BLUE;
			case GXBaseObjectConstants.GX_FIELD_COLOR_LIGHT_GREEN:
				return Color.LIGHT_GREEN;
			case GXBaseObjectConstants.GX_FIELD_COLOR_LIGHT_PURPLE:
				return Color.PINK;
			case GXBaseObjectConstants.GX_FIELD_COLOR_LIGHT_RED:
				return Color.LIGHT_RED;
			case GXBaseObjectConstants.GX_FIELD_COLOR_LIGHT_WHITE:
				return Color.LIGHT_WHITE;
		}
		return null;
	}
}
