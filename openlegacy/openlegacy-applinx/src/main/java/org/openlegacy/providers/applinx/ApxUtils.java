package org.openlegacy.providers.applinx;

import com.sabratec.applinx.common.designtime.message.GXImportRequest;
import com.sabratec.applinx.common.designtime.model.GXIApplicationContext;

import org.aspectj.util.FileUtil;

import java.net.URL;

public class ApxUtils {

	public static void importRepository(ApxServerLoader apxServerLoader, URL importFile) throws Exception {
		GXIApplicationContext apxApplicationContext = apxServerLoader.getServer().getApplications().get(0);
		GXImportRequest importRequest = new GXImportRequest();
		importRequest.setApplicationName(apxApplicationContext.getName());
		importRequest.setFileContent(FileUtil.readAsByteArray(importFile.openStream()));
		apxApplicationContext.importEntities(importRequest, null);
	}
}
