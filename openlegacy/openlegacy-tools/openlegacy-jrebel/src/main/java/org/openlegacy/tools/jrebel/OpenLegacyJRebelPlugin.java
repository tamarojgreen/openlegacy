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
package org.openlegacy.tools.jrebel;

import org.zeroturnaround.javarebel.ClassEventListener;
import org.zeroturnaround.javarebel.ClassResourceSource;
import org.zeroturnaround.javarebel.Plugin;
import org.zeroturnaround.javarebel.ReloaderFactory;
import org.zeroturnaround.javarebel.RequestIntegrationFactory;
import org.zeroturnaround.javarebel.RequestListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * A JRebel plug-in which supports reloading changes in screen entities into memory with reload
 */
public class OpenLegacyJRebelPlugin implements Plugin {

	private static Object springApplicationContext;

	public void preinit() {

		registerListener();
	}

	private static void registerListener() {
		log("OpenLegacy - Registering JRebel class reload listener");
		ReloaderFactory.getInstance().addClassReloadListener(new ClassEventListener() {

			@SuppressWarnings("rawtypes")
			public void onClassEvent(int eventType, Class klass) {

				if (!isRequiresReload(klass)) {
					return;
				}

				log("OpenLegacy - class event reload:" + klass.getName());

				Object registry = invoke(springApplicationContext, "getBean", new String[] { "java.lang.String" },
						"screensRegistry");
				Object registryLoader = invoke(springApplicationContext, "getBean", new String[] { "java.lang.String" },
						"registryLoader");
				Class[] innerClasses = klass.getDeclaredClasses();
				for (Class class1 : innerClasses) {
					ReloaderFactory.getInstance().checkAndReload(class1);
					try {
						if (isScreenEntity(klass)) {
							invoke(registryLoader, "loadSingleClass", new String[] { "org.openlegacy.EntitiesRegistry",
									"java.lang.Class" }, registry, class1);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (isScreenEntity(klass)) {
					invoke(registryLoader, "loadSingleClass",
							new String[] { "org.openlegacy.EntitiesRegistry", "java.lang.Class" }, registry, klass);
				}
			}

			private boolean isRequiresReload(Class<?> klass) {
				// AspectJ class file
				if (klass.getName().endsWith("Aspect")) {
					return false;
				}
				Class<?>[] interfaces = klass.getInterfaces();
				// Proxy class for screen entity created in DefaultTerminalSession
				for (Class<?> interface1 : interfaces) {
					if (interface1.getName().equals("org.springframework.aop.SpringProxy")) {
						return false;
					}
				}
				// Other proxy class
				if (klass.getName().contains("CGLIB")) {
					return false;
				}
				return true;
			}

			private boolean isScreenEntity(Class klass) {
				boolean found = false;
				Annotation[] annotations = klass.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation.annotationType().getName().equals("org.openlegacy.annotations.screen.ScreenEntity")
							|| annotation.annotationType().getName().equals("org.openlegacy.annotations.screen.ScreenPart")
							|| annotation.annotationType().getName().equals("org.openlegacy.annotations.screen.ScreenTable")) {
						found = true;
						break;
					}
				}
				return found;
			}

			public int priority() {
				return 0;
			}
		});

		RequestIntegrationFactory.getInstance().addRequestListener(new RequestListener() {

			public void requestFinally() {}

			public boolean rawRequest(Object application, Object arg1, Object arg2) {
				if (springApplicationContext == null) {
					log("OpenLegacy - saving Spring applicationContext");
					springApplicationContext = invoke(application, "getAttribute", new String[] { "java.lang.String" },
							"org.springframework.web.context.WebApplicationContext.ROOT");
				}
				return false;
			}

			public void beforeRequest() {}
		});
	}

	private static void log(String message) {
		System.out.println(message);

	}

	private static Object invoke(Object object, String methodName, String[] paramaterClasses, Object... methodValue) {
		Method theMethod = findMethod(object, methodName, paramaterClasses);
		try {
			Object invoke = theMethod.invoke(object, methodValue);
			return invoke;
		} catch (Exception e) {
			throw (new RuntimeException(e));
		}
	}

	/**
	 * Finds a method on a given object with the given name and parameters class names. NOTE: Not using Class parameters as JRebel
	 * plug-in class loader is not able to initialize OpenLegacy API classes
	 * 
	 * @param object
	 * @param methodName
	 * @param parameterClassesNames
	 * @return
	 */
	private static Method findMethod(Object object, String methodName, String[] parameterClassesNames) {
		Method theMethod = null;
		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName) && parameterClassesNames.length == method.getParameterTypes().length) {
				Class<?>[] parameters = method.getParameterTypes();
				boolean paramatersMatch = true;

				for (int i = 0; i < parameters.length; i++) {
					Class<?> paramaterType = parameters[i];
					if (!paramaterType.getName().equals(parameterClassesNames[i])) {
						paramatersMatch = false;
					}
				}
				if (paramatersMatch) {
					theMethod = method;
					break;
				}

			}
		}
		if (theMethod == null) {
			throw (new RuntimeException(MessageFormat.format("Method {0} not found", methodName)));
		}
		return theMethod;
	}

	public boolean checkDependencies(ClassLoader classLoader, ClassResourceSource classResourceSource) {
		return true;
	}

	public String getId() {
		return "openlegacy_plugin";
	}

	public String getName() {
		return "OpenLegacy JRebel Plugin";
	}

	public String getDescription() {
		return "Reload screen entitities after modifications.";
	}

	public String getAuthor() {
		return null;
	}

	public String getWebsite() {
		return null;
	}

	public String getSupportedVersions() {
		return null;
	}

	public String getTestedVersions() {
		return null;
	}
}
