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
import org.apache.commons.lang.StringUtils;
import org.openlegacy.designtime.DesigntimeConstants;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.enums.BackendSolution;
import org.openlegacy.designtime.mains.GenerateServiceRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
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
	private static final String CONTROLLER_SUFFIX = "Controller.java";

	private static final String WS_SERVICE_TEMPLATE = "jax-ws/Service.java.template";
	private static final String WS_SERVICE_TEST_TEMPLATE = "jax-ws/ServiceTest.java.template";
	private static final String WS_SCREEN_SERVICE_IMPL_TEMPLATE = "jax-ws/ScreenServiceImpl.java.template";
	private static final String WS_RPC_SERVICE_IMPL_TEMPLATE = "jax-ws/RpcServiceImpl.java.template";
	private static final String WS_DB_SERVICE_IMPL_TEMPLATE = "jax-ws/DbServiceImpl.java.template";

	private static final String RS_SERVICE_TEMPLATE = "jax-rs/Service.java.template";
	private static final String RS_SERVICE_TEST_TEMPLATE = "jax-rs/ServiceTest.java.template";
	private static final String RS_SCREEN_SERVICE_IMPL_TEMPLATE = "jax-rs/ScreenServiceImpl.java.template";
	private static final String RS_RPC_SERVICE_IMPL_TEMPLATE = "jax-rs/RpcServiceImpl.java.template";
	private static final String RS_DB_SERVICE_IMPL_TEMPLATE = "jax-rs/DbServiceImpl.java.template";
	private static final String RS_CONTROLLER_TEMPLATE = "jax-rs/ControllerTemplate.java.template";

	private static final String TEST_CONTEXT_RELATIVE_PATH = "src/main/resources/META-INF/spring/applicationContext-test.xml";

	private static final String REGISTER_WS_START = "<!-- Register WS place-holder start";
	private static final String REGISTER_WS_END = "Register WS place-holder end -->";
	private static final String EXISTING_SERVICE_PLACE_HOLDER_START = "<!-- auto generated service {0} start-->";
	private static final String EXISTING_SERVICE_PLACE_HOLDER_END = "<!-- auto generated service {0} end -->";

	@Inject
	private GenerateUtil generateUtil;

	@Override
	public void generateService(GenerateServiceRequest generateServiceRequest, Boolean supportRestFulService)
			throws GenerationException {
		if (supportRestFulService) {
			generateRestService(generateServiceRequest);
		} else {
			generateWsService(generateServiceRequest);
		}
	}

	@Override
	public boolean isSupportServiceGeneration(File projectPath, Boolean supportRestFulService) {
		if (supportRestFulService) {
			return true;
		}
		return new File(projectPath, DesigntimeConstants.SERVICE_CONTEXT_RELATIVE_PATH).exists();
	}

	public GenerateUtil getGenerateUtil() {
		return generateUtil;
	}

	private void generateWsService(GenerateServiceRequest generateServiceRequest) {
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
				getGenerateUtil().generate(generateServiceRequest, fos, WS_SERVICE_TEMPLATE);
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
				String serviceImplTemplate = generateServiceRequest.getServiceType().equals(BackendSolution.SCREEN) ? WS_SCREEN_SERVICE_IMPL_TEMPLATE
						: (generateServiceRequest.getServiceType().equals(BackendSolution.RPC) ? WS_RPC_SERVICE_IMPL_TEMPLATE
								: WS_DB_SERVICE_IMPL_TEMPLATE);
				getGenerateUtil().generate(generateServiceRequest, fos, serviceImplTemplate);
			}

			File serviceContextFile = new File(generateServiceRequest.getProjectPath(),
					DesigntimeConstants.SERVICE_CONTEXT_RELATIVE_PATH);

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
				getGenerateUtil().generate(generateServiceRequest, fos, WS_SERVICE_TEST_TEMPLATE);
			}
		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	private void generateRestService(GenerateServiceRequest generateServiceRequest) {

		UserInteraction userInteraction = generateServiceRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {
			// 1. generate interface
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
				getGenerateUtil().generate(generateServiceRequest, fos, RS_SERVICE_TEMPLATE);
			}
			// 2. generate implementation
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
				String serviceImplTemplate = generateServiceRequest.getServiceType().equals(BackendSolution.SCREEN) ? RS_SCREEN_SERVICE_IMPL_TEMPLATE
						: (generateServiceRequest.getServiceType().equals(BackendSolution.RPC) ? RS_RPC_SERVICE_IMPL_TEMPLATE
								: RS_DB_SERVICE_IMPL_TEMPLATE);
				getGenerateUtil().generate(generateServiceRequest, fos, serviceImplTemplate);
			}
			// 3. generate controller
			generate = true;
			File controllerFile = new File(sourceDir + "/controllers", serviceName + CONTROLLER_SUFFIX);
			if (controllerFile.exists()) {
				boolean override = userInteraction.isOverride(controllerFile);
				if (!override) {
					generate = false;
				}
			}
			if (generate) {
				controllerFile.getParentFile().mkdirs();
				fos = new FileOutputStream(controllerFile);
				getGenerateUtil().generate(generateServiceRequest, fos, RS_CONTROLLER_TEMPLATE);
			}
			// 4. generate test
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
				getGenerateUtil().generate(generateServiceRequest, fos, RS_SERVICE_TEST_TEMPLATE);
			}
			// 5. process applicationContext.xml
			File defaultSpringContextFile = new File(generateServiceRequest.getProjectPath(),
					DesigntimeConstants.DEFAULT_SPRING_CONTEXT_FILE);
			FileInputStream fis = new FileInputStream(defaultSpringContextFile);
			String contextFileContent = IOUtils.toString(fis);
			IOUtils.closeQuietly(fis);

			int indexOf = contextFileContent.indexOf("</beans>");
			StringBuilder sb = new StringBuilder(contextFileContent);
			contextFileContent = sb.insert(
					indexOf,
					MessageFormat.format("\t<bean id=\"{0}Service\" class=\"{1}.{2}ServiceImpl\" />\n",
							StringUtils.uncapitalize(serviceName), generateServiceRequest.getPackage(), serviceName)).toString();
			FileUtils.write(contextFileContent, defaultSpringContextFile);
		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}
}
