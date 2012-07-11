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
package org.openlegacy.terminal.modules.trail;

import org.apache.commons.io.IOUtils;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TrailUtil {

	public static void saveTrail(TerminalSession terminalSession, TrailWriter trailWriter, String trailPath)
			throws FileNotFoundException {

		if (!terminalSession.isConnected()) {
			return;
		}

		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
		OutputStream trailOut = null;

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
		try {
			File trailFile = new File(trailPath, MessageFormat.format("{0}_{1}.trail.xml", terminalSession.getSessionId(),
					dateFormat.format(cal.getTime())));
			if (!trailFile.getParentFile().exists()) {
				trailFile.getParentFile().mkdirs();
			}
			trailOut = new FileOutputStream(trailFile);
			trailWriter.write(trail, trailOut);
		} finally {
			IOUtils.closeQuietly(trailOut);
		}
	}
}
