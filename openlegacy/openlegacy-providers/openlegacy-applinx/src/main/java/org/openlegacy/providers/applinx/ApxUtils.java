package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXBaseObjectConstants;
import com.sabratec.applinx.common.designtime.message.GXImportRequest;
import com.sabratec.applinx.common.designtime.model.GXIApplicationContext;

import org.aspectj.util.FileUtil;
import org.openlegacy.terminal.Color;

import java.net.URL;

public class ApxUtils {

	public static void importRepository(ApxServerLoader apxServerLoader, URL importFile) throws Exception {
		GXIApplicationContext apxApplicationContext = apxServerLoader.getServer().getApplications().get(0);
		GXImportRequest importRequest = new GXImportRequest();
		importRequest.setApplicationName(apxApplicationContext.getName());
		importRequest.setFileContent(FileUtil.readAsByteArray(importFile.openStream()));
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
