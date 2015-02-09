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
package org.openlegacy.designtime.generators;

import org.apache.commons.io.IOUtils;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.enums.BackendSolution;
import org.openlegacy.designtime.mains.GenerateServiceRequest;
import org.openlegacy.exceptions.GenerationException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Abstract class for generating web service related content
 * 
 * @author roi
 * 
 */
public abstract class AbstractEntityWsGenerator implements EntityServiceGenerator {

	private static final String SERVICE_IMPL_SUFFIX = "ServiceImpl.java";
	private static final String SERVICE_SUFFIX = "Service.java";
	private static final String TEST_SUFFIX = "Test.java";
	private static final String SERVICE_TEMPLATE = "ws/Service.java.template";
	private static final String SERVICE_TEST_TEMPLATE = "ws/ServiceTest.java.template";
	private static final String SCREEN_SERVICE_IMPL_TEMPLATE = "ws/ScreenServiceImpl.java.template";
	private static final String RPC_SERVICE_IMPL_TEMPLATE = "ws/RpcServiceImpl.java.template";
	private static final String DB_SERVICE_IMPL_TEMPLATE = "ws/DbServiceImpl.java.template";
	private static final String SERVICE_CONTEXT_RELATIVE_PATH = "src/main/resources/META-INF/spring/serviceContext.xml";
	private static final String TEST_CONTEXT_RELATIVE_PATH = "src/main/resources/META-INF/spring/applicationContext-test.xml";

	private static final String REGISTER_WS_START = "<!-- Register WS place-holder start";
	private static final String REGISTER_WS_END = "Register WS place-holder end -->";
	private static final String EXISTING_SERVICE_PLACE_HOLDER_START = "<!-- auto generated service {0} start-->";
	private static final String EXISTING_SERVICE_PLACE_HOLDER_END = "<!-- auto generated service {0} end -->";

	@Inject
	private GenerateUtil generateUtil;

	@Override
	public void generateService(GenerateServiceRequest generateServiceRequest) throws GenerationException {

		getGenerateUtil().setTemplateDirectory(generateServiceRequest.getTemplatesDirectory());

		UserInteraction userInteraction = generateServiceRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			File sourceDir = new File(generateServiceRequest.getSourceDirectory(), generateServiceRequest.getPackageDirectory());
			String serviceName = generateServiceRequest.getServiceName();
			File serviceInterfaceFile = new File(sourceDir, serviceName + SERVICE_SUFFIX);
			boolean generate = true;
			if (serviceInterfaceFile.exists()) {
				boolean override = userInteraction.isOverride(serviceInterfaceFile);
				if (!override) {
					generate = false;
				}
			}
			if (generate) {
				serviceInterfaceFile.getParentFile().mkdirs();
				fos = new FileOutputStream(serviceInterfaceFile);
				getGenerateUtil().generate(generateServiceRequest, fos, SERVICE_TEMPLATE);
			}

			generate = true;
			File serviceImplFile = new File(sourceDir, serviceName + SERVICE_IMPL_SUFFIX);
			if (serviceImplFile.exists()) {
				boolean override = userInteraction.isOverride(serviceImplFile);
				if (!override) {
					generate = false;
				}
			}
			if (generate) {
				fos = new FileOutputStream(serviceImplFile);
				String serviceImplTemplate = generateServiceRequest.getServiceType().equals(BackendSolution.SCREEN) ? SCREEN_SERVICE_IMPL_TEMPLATE
						: (generateServiceRequest.getServiceType().equals(BackendSolution.RPC) ? RPC_SERVICE_IMPL_TEMPLATE
								: DB_SERVICE_IMPL_TEMPLATE);
				getGenerateUtil().generate(generateServiceRequest, fos, serviceImplTemplate);
			}

			File serviceContextFile = new File(generateServiceRequest.getProjectPath(), SERVICE_CONTEXT_RELATIVE_PATH);

			GenerateUtil.replicateTemplate(serviceContextFile, generateServiceRequest, REGISTER_WS_START, REGISTER_WS_END,
					MessageFormat.format(EXISTING_SERVICE_PLACE_HOLDER_START, serviceName),
					MessageFormat.format(EXISTING_SERVICE_PLACE_HOLDER_END, serviceName));

			File testContextFile = new File(generateServiceRequest.getProjectPath(), TEST_CONTEXT_RELATIVE_PATH);
			GenerateUtil.replicateTemplate(testContextFile, generateServiceRequest, REGISTER_WS_START, REGISTER_WS_END,
					MessageFormat.format(EXISTING_SERVICE_PLACE_HOLDER_START, serviceName),
					MessageFormat.format(EXISTING_SERVICE_PLACE_HOLDER_END, serviceName));

			if (generateServiceRequest.isGenerateTest()) {
				File testFile = new File(generateServiceRequest.getProjectPath(), "src/test/java/tests/" + serviceName
						+ "Service" + TEST_SUFFIX);
				if (testFile.exists()) {
					boolean override = userInteraction.isOverride(testFile);
					if (!override) {
						return;
					}
				}
				testFile.getParentFile().mkdirs();
				fos = new FileOutputStream(testFile);
				getGenerateUtil().generate(generateServiceRequest, fos, SERVICE_TEST_TEMPLATE);
			}
		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}

	@Override
	public boolean isSupportServiceGeneration(File projectPath) {
		return new File(projectPath, SERVICE_CONTEXT_RELATIVE_PATH).exists();
	}

	public GenerateUtil getGenerateUtil() {
		return generateUtil;
	}
}
