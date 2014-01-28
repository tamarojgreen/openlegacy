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
package org.openlegacy.recognizers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.ScreensRecognizer;
import org.openlegacy.utils.SpringUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.context.ApplicationContext;

import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

/***
 * Pattern based screen recognizer. Looks for fields in the given screen in the given positions. If one of the fields content
 * matches a screen name in the Screens Registry, then a matching screen class is returned The found content is
 * 
 * @author RoiM
 * 
 */
public class PatternBasedScreensRecognizer implements ScreensRecognizer {

	@Inject
	private transient ApplicationContext applicationContext;

	private List<TerminalPosition> positions;

	private final static Log logger = LogFactory.getLog(PatternBasedScreensRecognizer.class);

	private char[] ignoreChars = new char[] { ' ' };

	public Class<?> match(TerminalSnapshot terminalSnapshot) {
		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		if (positions == null) {
			return null;
		}
		for (TerminalPosition position : positions) {
			TerminalField field = terminalSnapshot.getField(position);
			String patternFromScreen = StringUtil.ignoreChars(field.getValue(), ignoreChars);
			if (patternFromScreen.length() > 0) {
				Class<?> screenModel = screenEntitiesRegistry.getEntityClass(patternFromScreen);
				if (screenModel != null) {
					logger.debug(MessageFormat.format("Found matched screen. Found pattern \"{0}\" in position {1}:",
							patternFromScreen, position));
					return screenModel;
				}
			}
		}
		logger.debug("Didn't found any matched screen");
		return null;
	}

	public void setPositions(List<TerminalPosition> positions) {
		this.positions = positions;
	}

	public void setIgnoreChars(char[] ignoreChars) {
		this.ignoreChars = ignoreChars;
	}

}
