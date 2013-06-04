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
import org.openlegacy.designtime.rpc.generators.RpcPojosAjGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import japa.parser.ParseException;

import java.io.File;
import java.io.IOException;

@Component
public class RpcPojosRecursiveAjGenerator extends AbstractPojosRecursiveAjGenerator {

	public static void main(String[] args) throws ParseException, IOException, TemplateException {
		if (args.length == 0) {
			System.out.println("Usage:\njava RpcEntityRecursiveAjGenerator java-source-folder");
			return;
		}
		String root = args[0];

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:/openlegacy-basic-designtime-context.xml");

		RpcPojosRecursiveAjGenerator generator = applicationContext.getBean(RpcPojosRecursiveAjGenerator.class);
		generator.generateAll(new File(root));
	}

	@Override
	protected PojosAjGenerator getGenerator() {
		return applicationContext.getBean(RpcPojosAjGenerator.class);
	}

}
