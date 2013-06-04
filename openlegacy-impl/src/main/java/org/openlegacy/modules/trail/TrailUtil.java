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
package org.openlegacy.modules.trail;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.Session;
import org.openlegacy.Snapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

public class TrailUtil {

	private final static Log logger = LogFactory.getLog(TrailUtil.class);

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Inject
	private TrailWriter trailWriter;

	public void saveTrail(Session session) {

		String trailPath = openLegacyProperties.getProperty(OpenLegacyProperties.TRAIL_FOLDER_PATH);

		if (trailPath == null) {
			return;
		}

		if (!session.isConnected()) {
			return;
		}

		SessionTrail<? extends Snapshot> trail = session.getModule(Trail.class).getSessionTrail();
		OutputStream trailOut = null;

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
		try {
			File trailFile = new File(trailPath, MessageFormat.format("{0}_{1}.trail", session.getProperties().getId(),
					dateFormat.format(cal.getTime())));
			if (!trailFile.getParentFile().exists()) {
				trailFile.getParentFile().mkdirs();
			}
			trailOut = new FileOutputStream(trailFile);
			trailWriter.write(trail, trailOut);
		} catch (IOException e) {
			logger.fatal("Failed to save trail file", e);
		} finally {
			IOUtils.closeQuietly(trailOut);
		}
	}
}
