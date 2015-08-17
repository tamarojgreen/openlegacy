/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.support;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.WebServicesRegistry;
import org.openlegacy.annotations.ws.Service;
import org.openlegacy.annotations.ws.ServiceMethod;
import org.openlegacy.loaders.WebServicesRegistryLoader;
import org.openlegacy.loaders.WsClassAnnotationsLoader;
import org.openlegacy.loaders.WsMethodAnnotationsLoader;
import org.openlegacy.loaders.WsMethodParamLoader;
import org.openlegacy.utils.BeanUtils;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.ws.definitions.SimpleWebServiceDefinition;
import org.openlegacy.ws.definitions.SimpleWebServiceMethodDefinition;
import org.openlegacy.ws.definitions.SimpleWebServicePoolDefinition;
import org.openlegacy.ws.definitions.SimpleWebServicePoolInitActionDefinition;
import org.openlegacy.ws.definitions.WebServicePoolDefinition;
import org.openlegacy.ws.definitions.WebServicePoolInitActionDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultWebServicesRegistryLoader implements WebServicesRegistryLoader {

	private final static Log logger = LogFactory.getLog(DefaultWebServicesRegistryLoader.class);

	private List<WsClassAnnotationsLoader> classAnnotationsLoaders;
	private List<WsMethodAnnotationsLoader> methodAnnotationsLoaders;
	private List<WsMethodParamLoader> methodParamLoaders;

	private String serviceContext = "";
	private boolean isRest = false;

	public DefaultWebServicesRegistryLoader() {
		// rest service hasn`t serviceContext.xml
		try {
			serviceContext = IOUtils.toString(getClass().getResourceAsStream("/META-INF/spring/serviceContext.xml"));
			isRest = false;
		} catch (Exception e) {
			try {
				serviceContext = IOUtils.toString(getClass().getResourceAsStream("/META-INF/spring/applicationContext.xml"));
				isRest = true;
			} catch (Exception e1) {

			}
		}
	}

	public void setServiceContext(String serviceContext) {
		this.serviceContext = serviceContext;
	}

	@Override
	public void load(WebServicesRegistry registry) {
		Assert.notNull(classAnnotationsLoaders);
		Assert.notNull(methodAnnotationsLoaders);
		Assert.notNull(methodParamLoaders);

		List<String> packages = registry.getPackages();
		if (packages == null) {
			logger.warn("Not packages defined for entity registry " + registry.getClass());
			return;
		}

		Resource[] resources;
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

		for (String thePackage : packages) {
			try {
				String packagePath = org.openlegacy.utils.ClassUtils.packageToResourcePath(thePackage);

				resources = resourcePatternResolver.getResources("classpath*:" + packagePath + "/*.class");
				for (Resource resource : resources) {
					Class<?> beanClass = DefaultRegistryLoader.getClassFromResource(packagePath, resource);
					loadClass(registry, beanClass);
				}
			} catch (Exception e) {
				throw (new RuntimeException(e));
			}
		}
	}

	public void setClassAnnotationsLoaders(Collection<WsClassAnnotationsLoader> classAnnotationsLoaders) {
		this.classAnnotationsLoaders = new ArrayList<WsClassAnnotationsLoader>(classAnnotationsLoaders);
		Collections.sort(this.classAnnotationsLoaders);
	}

	public void setMethodAnnotationsLoaders(Collection<WsMethodAnnotationsLoader> methodAnnotationsLoaders) {
		this.methodAnnotationsLoaders = new ArrayList<WsMethodAnnotationsLoader>(methodAnnotationsLoaders);
		Collections.sort(this.methodAnnotationsLoaders);
	}

	public void setMethodParamLoaders(Collection<WsMethodParamLoader> methodParamLoaders) {
		this.methodParamLoaders = new ArrayList<WsMethodParamLoader>(methodParamLoaders);
		Collections.sort(this.methodParamLoaders);
	}

	@Override
	public void loadClass(WebServicesRegistry registry, Class<?> clazz) {
		if (AnnotationUtils.findAnnotation(clazz, Service.class) == null || !serviceContext.contains(clazz.getName())) {
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Processing %s service", clazz.getSimpleName()));
		}

		SimpleWebServiceDefinition wsDef = new SimpleWebServiceDefinition();
		wsDef.setWebServiceClass(clazz);
		wsDef.setRest(isRest);

		for (WsClassAnnotationsLoader classAnnotationLoader : classAnnotationsLoaders) {
			Annotation annotation = AnnotationUtils.findAnnotation(clazz, classAnnotationLoader.getAnnotation());
			if (annotation != null) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Running %s loader", classAnnotationLoader.getClass().getSimpleName()));
				}
				classAnnotationLoader.load(wsDef, annotation);
			}
		}

		for (Method method : clazz.getMethods()) {
			if (AnnotationUtils.findAnnotation(method, ServiceMethod.class) == null) {
				continue; // all ws operation must have this annotation!
			}
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Processing %s method", method.getName()));
			}
			SimpleWebServiceMethodDefinition mDef = new SimpleWebServiceMethodDefinition();
			// for method we can`t find all annotations
			// so, attempts to find annotation by need class from loader
			// process method annotations
			for (WsMethodAnnotationsLoader methodAnnotationLoader : methodAnnotationsLoaders) {
				Annotation annotation = AnnotationUtils.findAnnotation(method, methodAnnotationLoader.getAnnotation());
				if (annotation != null) {
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Running %s loader", methodAnnotationLoader.getClass().getSimpleName()));
					}
					methodAnnotationLoader.load(mDef, annotation);
				}
			}

			// process method params
			for (WsMethodParamLoader methodParamLoader : methodParamLoaders) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Running %s loader", methodParamLoader.getClass().getSimpleName()));
				}
				methodParamLoader.load(mDef, method);
			}
			wsDef.getMethods().add(mDef);
		}

		wsDef.setPool(getPoolDefinition(StringUtil.toJavaFieldName(wsDef.getName()) + "Pool"));
		if (wsDef.getPool() != null) {
			((SimpleWebServicePoolDefinition)wsDef.getPool()).setInitActionDefinition(getInitActionDefinition(StringUtil.toJavaFieldName(wsDef.getName())
					+ "InitAction"));
		}

		registry.getWebServices().add(wsDef);
	}

	private WebServicePoolDefinition getPoolDefinition(String beanName) {
		if (beanName == null || beanName.trim().length() == 0) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Processing %s pool definition", beanName));
		}

		BeanDefinition beanDef = null;
		try {
			beanDef = ((DefaultListableBeanFactory)WebServiceAnnotationProcessor.getBeanFactory()).getBeanDefinition(beanName);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error(String.format("Can`t find %s bean", beanName));
			}
		}
		Class<?> beanClass = null;
		try {
			beanClass = Class.forName(beanDef.getBeanClassName());
		} catch (Exception e) {
			return null;
		}

		SimpleWebServicePoolDefinition pDef = new SimpleWebServicePoolDefinition();
		pDef.setName(beanName);
		pDef.setPoolClass(beanClass);
		BeanUtils.fillFromBeanDef(beanDef, beanClass, pDef);
		return pDef;
	}

	private WebServicePoolInitActionDefinition getInitActionDefinition(String beanName) {
		if (beanName == null || beanName.trim().length() == 0) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Processing %s init action definition", beanName));
		}

		BeanDefinition beanDef = null;
		try {
			beanDef = ((DefaultListableBeanFactory)WebServiceAnnotationProcessor.getBeanFactory()).getBeanDefinition(beanName);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error(String.format("Can`t find %s bean", beanName));
			}
		}
		Class<?> beanClass = null;
		try {
			beanClass = Class.forName(beanDef.getBeanClassName());
		} catch (Exception e) {
			return null;
		}

		SimpleWebServicePoolInitActionDefinition iaDef = new SimpleWebServicePoolInitActionDefinition();
		iaDef.setName(beanName);
		iaDef.setInitActionClass(beanClass);
		BeanUtils.fillFromBeanDef(beanDef, beanClass, iaDef);
		return iaDef;
	}
}
