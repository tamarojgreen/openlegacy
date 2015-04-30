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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Abstract class for generating web service related content
 * 
 * @author roi
 * 
 */
public abstract class AbstractEntityWsGenerator implements EntityServiceGenerator {

	private static final String WS_SERVICE_TEMPLATE = "jax-ws/Service.java.template";
	private static final String WS_SERVICE_TEST_TEMPLATE = "jax-ws/ServiceTest.java.template";
	private static final String WS_SCREEN_SERVICE_IMPL_TEMPLATE = "jax-ws/ScreenServiceImpl.java.template";
	private static final String WS_RPC_SERVICE_IMPL_TEMPLATE = "jax-ws/RpcServiceImpl.java.template";
	private static final String WS_DB_SERVICE_IMPL_TEMPLATE = "jax-ws/DbServiceImpl.java.template";

	private static final String WS_TERMINAL_INIT_ACTION_TEMPLATE = "jax-ws/terminal/InitAction.java.template";
	private static final String WS_TERMINAL_CLEANUP_ACTION_TEMPLATE = "jax-ws/terminal/CleanupAction.java.template";
	private static final String WS_TERMINAL_KEEP_ALIVE_ACTION_TEMPLATE = "jax-ws/terminal/KeepAliveAction.java.template";
	private static final String WS_RPC_INIT_ACTION_TEMPLATE = "jax-ws/rpc/InitAction.java.template";
	private static final String WS_RPC_CLEANUP_ACTION_TEMPLATE = "jax-ws/rpc/CleanupAction.java.template";
	private static final String WS_RPC_KEEP_ALIVE_ACTION_TEMPLATE = "jax-ws/rpc/KeepAliveAction.java.template";

	private static final String RS_SERVICE_TEMPLATE = "jax-rs/Service.java.template";
	private static final String RS_SERVICE_TEST_TEMPLATE = "jax-rs/ServiceTest.java.template";
	private static final String RS_SCREEN_SERVICE_IMPL_TEMPLATE = "jax-rs/ScreenServiceImpl.java.template";
	private static final String RS_RPC_SERVICE_IMPL_TEMPLATE = "jax-rs/RpcServiceImpl.java.template";
	private static final String RS_DB_SERVICE_IMPL_TEMPLATE = "jax-rs/DbServiceImpl.java.template";
	private static final String RS_CONTROLLER_TEMPLATE = "jax-rs/ControllerTemplate.java.template";

	private static final String RS_TERMINAL_INIT_ACTION_TEMPLATE = "jax-rs/terminal/InitAction.java.template";
	private static final String RS_TERMINAL_CLEANUP_ACTION_TEMPLATE = "jax-rs/terminal/CleanupAction.java.template";
	private static final String RS_TERMINAL_KEEP_ALIVE_ACTION_TEMPLATE = "jax-rs/terminal/KeepAliveAction.java.template";
	private static final String RS_RPC_INIT_ACTION_TEMPLATE = "jax-rs/rpc/InitAction.java.template";
	private static final String RS_RPC_CLEANUP_ACTION_TEMPLATE = "jax-rs/rpc/CleanupAction.java.template";
	private static final String RS_RPC_KEEP_ALIVE_ACTION_TEMPLATE = "jax-rs/rpc/KeepAliveAction.java.template";
	private static final String RS_SCREEN_BEANS_TEMPLATE = "jax-rs/beans.screen.template";
	private static final String RS_RPC_BEANS_TEMPLATE = "jax-rs/beans.rpc.template";

	private static final String TEST_CONTEXT_RELATIVE_PATH = "src/main/resources/META-INF/spring/applicationContext-test.xml";
	private static final String APPLICATION_PROPERTIES = "src/main/resources/application.properties";

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
			BackendSolution serviceType = generateServiceRequest.getServiceType();

			File sourceDir = new File(generateServiceRequest.getSourceDirectory(), generateServiceRequest.getPackageDirectory());
			String serviceName = generateServiceRequest.getServiceName();
			File serviceInterfaceFile = new File(sourceDir, serviceName + DesigntimeConstants.SERVICE_SUFFIX);
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
			File serviceImplFile = new File(sourceDir, serviceName + DesigntimeConstants.SERVICE_IMPL_SUFFIX);
			if (serviceImplFile.exists()) {
				boolean override = userInteraction.isOverride(serviceImplFile);
				if (!override) {
					generate = false;
				}
			}
			if (generate) {
				fos = new FileOutputStream(serviceImplFile);
				String serviceImplTemplate = serviceType.equals(BackendSolution.SCREEN) ? WS_SCREEN_SERVICE_IMPL_TEMPLATE
						: (serviceType.equals(BackendSolution.RPC) ? WS_RPC_SERVICE_IMPL_TEMPLATE : WS_DB_SERVICE_IMPL_TEMPLATE);
				getGenerateUtil().generate(generateServiceRequest, fos, serviceImplTemplate);
			}

			generateActionFiles(generateServiceRequest, serviceName, serviceType, false);

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
						+ "Service" + DesigntimeConstants.TEST_SUFFIX);
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
			BackendSolution serviceType = generateServiceRequest.getServiceType();

			// 1. generate interface
			File sourceDir = new File(generateServiceRequest.getSourceDirectory(), generateServiceRequest.getPackageDirectory());
			String serviceName = generateServiceRequest.getServiceName();
			File serviceInterfaceFile = new File(sourceDir, serviceName + DesigntimeConstants.SERVICE_SUFFIX);
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
			File serviceImplFile = new File(sourceDir, serviceName + DesigntimeConstants.SERVICE_IMPL_SUFFIX);
			if (serviceImplFile.exists()) {
				boolean override = userInteraction.isOverride(serviceImplFile);
				if (!override) {
					generate = false;
				}
			}
			if (generate) {
				fos = new FileOutputStream(serviceImplFile);
				String serviceImplTemplate = serviceType.equals(BackendSolution.SCREEN) ? RS_SCREEN_SERVICE_IMPL_TEMPLATE
						: (serviceType.equals(BackendSolution.RPC) ? RS_RPC_SERVICE_IMPL_TEMPLATE : RS_DB_SERVICE_IMPL_TEMPLATE);
				getGenerateUtil().generate(generateServiceRequest, fos, serviceImplTemplate);
			}
			// 3. generate controller
			generate = true;
			File controllerFile = new File(sourceDir + "/controllers", serviceName + DesigntimeConstants.CONTROLLER_SUFFIX);
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
						+ "Service" + DesigntimeConstants.TEST_SUFFIX);
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
			// 5. added action files
			generateActionFiles(generateServiceRequest, serviceName, serviceType, true);

			// 6. process applicationContext.xml
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
			// 7. add TerminalSessionPoolFactory
			if (generateServiceRequest.isGeneratePool()) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String beansTemplate = serviceType.equals(BackendSolution.SCREEN) ? RS_SCREEN_BEANS_TEMPLATE
						: RS_RPC_BEANS_TEMPLATE;
				getGenerateUtil().generate(generateServiceRequest, baos, beansTemplate);
				String generatedBeans = new String(baos.toByteArray());
				indexOf = contextFileContent.indexOf("</beans>");
				sb = new StringBuilder(contextFileContent);
				contextFileContent = sb.insert(indexOf, generatedBeans).toString();
			}

			FileUtils.write(contextFileContent, defaultSpringContextFile);
		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	private void generateActionFile(GenerateServiceRequest gsr, String fileName, String template) throws FileNotFoundException {
		File actionsSourceDir = new File(gsr.getSourceDirectory(), gsr.getPackageDirectory());
		if (!actionsSourceDir.exists()) {
			actionsSourceDir.mkdirs();
		}

		File actionFile = new File(actionsSourceDir, fileName);
		boolean generate = true;
		if (actionFile.exists()) {
			boolean override = gsr.getUserInteraction().isOverride(actionFile);
			if (!override) {
				generate = false;
			}
		}
		if (generate) {
			FileOutputStream fos = new FileOutputStream(actionFile);
			getGenerateUtil().generate(gsr, fos, template);
		}

	}

	private static GenerateServiceRequest copyGenerateServiceRequest(GenerateServiceRequest request) {
		GenerateServiceRequest gsr = new GenerateServiceRequest();
		gsr.setGenerateAspectJ(request.isGenerateAspectJ());
		gsr.setGeneratePool(request.isGeneratePool());
		gsr.setGenerateTest(request.isGenerateTest());
		gsr.setPackageDirectory(request.getPackageDirectory());
		gsr.setProjectPath(request.getProjectPath());
		gsr.setServiceName(request.getServiceName());
		gsr.setServiceType(request.getServiceType());
		gsr.setSourceDirectory(request.getSourceDirectory());
		gsr.setTemplatesDirectory(request.getTemplatesDirectory());
		gsr.setUserInteraction(request.getUserInteraction());
		return gsr;
	}

	private void generateActionFiles(GenerateServiceRequest generateServiceRequest, String serviceName,
			BackendSolution serviceType, boolean supportRestFulService) throws IOException {
		if (generateServiceRequest.isGeneratePool()) {
			// generate actions
			GenerateServiceRequest gsr = copyGenerateServiceRequest(generateServiceRequest);
			gsr.setPackageDirectory(gsr.getPackageDirectory() + "/actions");

			if (supportRestFulService) {
				generateActionFile(gsr, serviceName + DesigntimeConstants.INIT_ACTION,
						serviceType.equals(BackendSolution.SCREEN) ? RS_TERMINAL_INIT_ACTION_TEMPLATE
								: RS_RPC_INIT_ACTION_TEMPLATE);
				generateActionFile(gsr, serviceName + DesigntimeConstants.CLEANUP_ACTION,
						serviceType.equals(BackendSolution.SCREEN) ? RS_TERMINAL_CLEANUP_ACTION_TEMPLATE
								: RS_RPC_CLEANUP_ACTION_TEMPLATE);
				generateActionFile(gsr, serviceName + DesigntimeConstants.KEEP_ALIVE_ACTION,
						serviceType.equals(BackendSolution.SCREEN) ? RS_TERMINAL_KEEP_ALIVE_ACTION_TEMPLATE
								: RS_RPC_KEEP_ALIVE_ACTION_TEMPLATE);
			} else {
				generateActionFile(gsr, serviceName + DesigntimeConstants.INIT_ACTION,
						serviceType.equals(BackendSolution.SCREEN) ? WS_TERMINAL_INIT_ACTION_TEMPLATE
								: WS_RPC_INIT_ACTION_TEMPLATE);
				generateActionFile(gsr, serviceName + DesigntimeConstants.CLEANUP_ACTION,
						serviceType.equals(BackendSolution.SCREEN) ? WS_TERMINAL_CLEANUP_ACTION_TEMPLATE
								: WS_RPC_CLEANUP_ACTION_TEMPLATE);
				generateActionFile(gsr, serviceName + DesigntimeConstants.KEEP_ALIVE_ACTION,
						serviceType.equals(BackendSolution.SCREEN) ? WS_TERMINAL_KEEP_ALIVE_ACTION_TEMPLATE
								: WS_RPC_KEEP_ALIVE_ACTION_TEMPLATE);
			}
			// check user/password in application.properties
			File appPropertiesFile = new File(gsr.getProjectPath(), APPLICATION_PROPERTIES);
			if (appPropertiesFile.exists()) {
				String appPropertiesFileContent = IOUtils.toString(new FileInputStream(appPropertiesFile));

				String userProperty = MessageFormat.format("user=", StringUtils.uncapitalize(serviceName));
				String passwordProperty = MessageFormat.format("password=", StringUtils.uncapitalize(serviceName));

				if (!appPropertiesFileContent.contains(userProperty)) {
					StringBuilder sb = new StringBuilder(appPropertiesFileContent);
					sb.append("\n").append(userProperty);
					appPropertiesFileContent = sb.toString();
				}
				if (!appPropertiesFileContent.contains(passwordProperty)) {
					StringBuilder sb = new StringBuilder(appPropertiesFileContent);
					sb.append("\n").append(passwordProperty);
					appPropertiesFileContent = sb.toString();
				}
				FileUtils.write(appPropertiesFileContent, appPropertiesFile);
			}
		}
	}
}
