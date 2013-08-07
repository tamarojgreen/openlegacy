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
package org.openlegacy.designtime.rpc.generators;

import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.generators.AbstractEntityMvcGenerator;
import org.openlegacy.designtime.generators.EntityWebGenerator;
import org.openlegacy.designtime.mains.GenerateControllerRequest;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;

/**
 * Generates all Spring MVC web related content
 * 
 * @author RoiM
 * 
 */
@Component
public class RpcEntityMvcGenerator extends AbstractEntityMvcGenerator implements EntityWebGenerator {

	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix) {
		String typeName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
				pageDefinition.getEntityDefinition().getTypeName());
		getGenerateUtil().generate(pageDefinition, output, "RpcEntityMvcPage.jspx.template", typeName);
	}

	public void generateController(PageDefinition pageDefinition, OutputStream output) {
		// TODO implement
	}

	public void generateControllerAspect(PageDefinition pageDefinition, OutputStream output) {
		// TODO implement
	}

	/**
	 * Generate all web page related content: jspx, controller, controller aspect file, and views.xml file
	 */
	public void generateView(GenerateViewRequest generatePageRequest, EntityDefinition<?> screenEntityDefinition)
			throws GenerationException {

		generateView(generatePageRequest, screenEntityDefinition, false);
	}

	@Override
	public void generateCompositePage(EntityDefinition<?> entityDefinition, OutputStream output, String templateDirectoryPrefix) {
		getGenerateUtil().generate(entityDefinition, output,
				templateDirectoryPrefix + "ScreenEntityMvcCompositePage.jspx.template");
	}

	public void generateController(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void generatePage(PageDefinition pageDefinition, FileOutputStream fos, String templateDirectoryPrefix) {
		// TODO Auto-generated method stub

	}

}
