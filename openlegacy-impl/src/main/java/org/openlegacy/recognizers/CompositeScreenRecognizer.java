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
package org.openlegacy.recognizers;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.services.ScreensRecognizer;

import java.util.List;

/**
 * A composite pattern screen recognizer which accept multiple recognizers and activates them one after the other
 * 
 */
public class CompositeScreenRecognizer implements ScreensRecognizer {

	private List<ScreensRecognizer> screensRecognizers;

	public Class<?> match(TerminalSnapshot terminalSnapshot) {
		for (ScreensRecognizer screensRecognizer : screensRecognizers) {
			Class<?> matchedScreen = screensRecognizer.match(terminalSnapshot);
			if (matchedScreen != null) {
				return matchedScreen;
			}
		}
		return null;
	}

	public void setScreensRecognizers(List<ScreensRecognizer> screensRecognizers) {
		this.screensRecognizers = screensRecognizers;
	}

}
