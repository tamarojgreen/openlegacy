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
package org.openlegacy.modules.trail;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.Session;
import org.openlegacy.Snapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

public class TrailUtil {

	private String testTrail = "test.trail";

	private final static Log logger = LogFactory.getLog(TrailUtil.class);

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Inject
	private TrailWriter trailWriter;

	public File saveTrail(Session session) {

		String trailPath = openLegacyProperties.getProperty(OpenLegacyProperties.TRAIL_FOLDER_PATH);

		if (trailPath == null) {
			return null;
		}

		if (session == null || !session.isConnected()) {
			return null;
		}

		SessionTrail<? extends Snapshot> trail = session.getModule(Trail.class).getSessionTrail();
		OutputStream trailOut = null;

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmm");
		File trailFile = null;
		try {
			String sessionId = session.getProperties().getId();
			trailFile = new File(trailPath, MessageFormat.format("{0}{1}.trail", dateFormat.format(cal.getTime()),
					sessionId != null ? "_" + sessionId : ""));
			if (!trailFile.getParentFile().exists()) {
				trailFile.getParentFile().mkdirs();
			}
			trailFile.getParentFile().mkdirs();
			trailOut = new FileOutputStream(trailFile);
			trailWriter.write(trail, trailOut);
			logger.info("*** Trail file create in:" + trailFile.getAbsolutePath());
		} catch (IOException e) {
			logger.fatal("Failed to save trail file", e);
		} finally {
			IOUtils.closeQuietly(trailOut);
		}
		return trailFile;
	}

	public void saveTestTrail(Session session) {
		if (!session.isConnected()) {
			return;
		}
		FileOutputStream out = null;
		try {
			String testClass = Thread.currentThread().getStackTrace()[2].getClassName();
			Class<?> clazz = Class.forName(testClass);
			String classLocation = clazz.getResource(".").getFile();
			File parentFile = new File(classLocation);
			while (!parentFile.getName().equals("target")) {
				parentFile = parentFile.getParentFile();
			}
			parentFile = parentFile.getParentFile();
			out = new FileOutputStream(parentFile + "/" + testTrail);
			trailWriter.write(session.getModule(Trail.class).getSessionTrail(), out);
		} catch (FileNotFoundException e) {
			throw (new RuntimeException(e));
		} catch (ClassNotFoundException e) {
			throw (new RuntimeException(e));
		} finally {
			IOUtils.closeQuietly(out);

		}
		logger.info(MessageFormat.format("A new trail file for test {0} was created in project root", testTrail));
	}

	public void setTestTrail(String testTrail) {
		this.testTrail = testTrail;
	}
}
