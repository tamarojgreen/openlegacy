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
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

@Component
public class ScreenEntityJavaGenerator {

	@Inject
	private GenerateUtil generateUtil;

	public void generate(ScreenEntityDefinition screenEntityDefinition, OutputStream out) throws TemplateException, IOException {
		String typeName = screenEntityDefinition.getTypeName();
		generateUtil.generate(screenEntityDefinition, out, "ScreenEntity.java.template", typeName);
	}
}
