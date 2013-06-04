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
package org.openlegacy.designtime.mains;

import freemarker.template.TemplateException;

import org.openlegacy.designtime.generators.PojosAjGenerator;
import org.openlegacy.utils.FileCommand;
import org.openlegacy.utils.FileCommandExecuter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public abstract class AbstractPojosRecursiveAjGenerator {

	@Inject
	protected ApplicationContext applicationContext;

	public void generateAll(File root) throws IOException, TemplateException {
		AspectGeneratorCommand command = applicationContext.getBean(AspectGeneratorCommand.class);
		command.setGenerator(getGenerator());
		FileCommandExecuter.execute(root, command);
	}

	protected abstract PojosAjGenerator getGenerator();

	@Component
	public static class AspectGeneratorCommand implements FileCommand {

		private PojosAjGenerator pojosAjGenerator;

		public boolean accept(File file) {
			if (file.getName().endsWith("aj")) {
				return false;
			}
			if (file.isDirectory()) {
				return true;
			}
			if (file.getName().endsWith("java")) {
				return true;
			}

			return false;
		}

		public void setGenerator(PojosAjGenerator generator) {
			pojosAjGenerator = generator;
		}

		public void doCommand(File file) {
			try {
				pojosAjGenerator.generate(file);
			} catch (Exception e) {
				throw (new RuntimeException(e));
			}

		}

	}

}
