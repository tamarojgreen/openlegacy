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

public class TrailUtil {

	public static void saveTrail(TerminalSession terminalSession, TrailWriter trailWriter, String trailPath)
			throws FileNotFoundException {

		if (terminalSession.getSessionId() == null) {
			return;
		}

		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
		OutputStream trailOut = null;
		try {
			File trailFile = new File(trailPath, terminalSession.getSessionId() + ".trail.xml");
			if (!trailFile.getParentFile().exists()) {
				trailFile.getParentFile().mkdirs();
			}
			terminalSession.getSessionId();
			trailOut = new FileOutputStream(trailFile);
			trailWriter.write(trail, trailOut);
		} finally {
			IOUtils.closeQuietly(trailOut);
		}
	}
}
