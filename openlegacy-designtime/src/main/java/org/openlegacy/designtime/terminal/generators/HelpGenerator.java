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
package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.layout.PageDefinition;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

/**
 * A generator which generates an html help file for a given screen entity
 */
@Component
public class HelpGenerator {

	@Inject
	private GenerateUtil generateUtil;

	public void generate(PageDefinition pageDefinition, OutputStream out) throws TemplateException, IOException {

		generateUtil.generate(pageDefinition, out, "ScreenEntityHelpPage.html.template");

	}
}
