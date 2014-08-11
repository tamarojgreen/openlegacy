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
package org.openlegacy.terminal.mvc;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.SnapshotPersistanceDTO;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/sessionViewer")
public class SessionViewerController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TerminalSnapshotImageRenderer imageRenderer;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	String viewer(HttpServletRequest request) throws IOException {
		String style = "";
		if (request.getParameter("small") != null) {
			style = "style=\"width:300px;height;200px\";";
		}
		String prevClick = "document.getElementById('sessionImage').src='sessionViewer/image/-1?x=' + (new Date().getTime());";
		String nextClick = "document.getElementById('sessionImage').src='sessionViewer/image/1?x=' + (new Date().getTime());";

		return MessageFormat.format(
				"<html><body><button onclick=\"{3}\"><</button> <button onclick=\"{4}\">></button><br/><img id=\"sessionImage\" src=\"{0}/sessionViewer/image?x={1}\"/ {2}></body></html>",
				request.getContextPath(), System.currentTimeMillis(), style, prevClick, nextClick);
	}

	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public void lastImage(HttpServletResponse response) throws IOException {
		if (terminalSession.isConnected()) {
			imageRenderer.render(terminalSession.getSnapshot(), response.getOutputStream());
		}
	}

	@RequestMapping(value = "/image/{steps}", method = RequestMethod.GET)
	public void imageFromTrail(HttpServletResponse response, @PathVariable("steps") int steps) throws IOException {
		if (terminalSession.isConnected()) {
			TerminalSnapshot current;
			if (steps == 0) {
				current = terminalSession.getSnapshot();
			} else {
				SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
				trail.advanceCurrent(steps);
				current = (TerminalSnapshot)trail.getCurrent();
			}
			response.setContentType("image/jpeg");
			TerminalPersistedSnapshot transformSnapshot = SnapshotPersistanceDTO.transformSnapshot(current);
			imageRenderer.render(transformSnapshot, response.getOutputStream());
		}
	}

}
