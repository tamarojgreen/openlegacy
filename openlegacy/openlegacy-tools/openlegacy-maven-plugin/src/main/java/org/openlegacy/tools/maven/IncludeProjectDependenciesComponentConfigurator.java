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
package org.openlegacy.tools.maven;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.configurator.AbstractComponentConfigurator;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.composite.ObjectWithFieldsConverter;
import org.codehaus.plexus.component.configurator.converters.special.ClassRealmConverter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom ComponentConfigurator which adds the project's runtime classpath elements to the
 * 
 * @author Brian Jackson
 * @since Aug 1, 2008 3:04:17 PM
 * 
 * @plexus.component role="org.codehaus.plexus.component.configurator.ComponentConfigurator"
 *                   role-hint="include-project-dependencies"
 * @plexus.requirement role="org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup" role-hint="default"
 */
public class IncludeProjectDependenciesComponentConfigurator extends AbstractComponentConfigurator {

	// private static final Logger LOGGER = Logger.getLogger(IncludeProjectDependenciesComponentConfigurator.class);

	@Override
	public void configureComponent(Object component, PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator,
			ClassRealm containerRealm, ConfigurationListener listener) throws ComponentConfigurationException {

		addProjectDependenciesToClassRealm(expressionEvaluator, containerRealm);

		converterLookup.registerConverter(new ClassRealmConverter(containerRealm));

		ObjectWithFieldsConverter converter = new ObjectWithFieldsConverter();

		converter.processConfiguration(converterLookup, component, containerRealm.getClassLoader(), configuration,
				expressionEvaluator, listener);
	}

	@SuppressWarnings("unchecked")
	private static void addProjectDependenciesToClassRealm(ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm)
			throws ComponentConfigurationException {
		List<String> runtimeClasspathElements;
		try {
			// noinspection unchecked
			runtimeClasspathElements = (List<String>)expressionEvaluator.evaluate("${project.runtimeClasspathElements}");
		} catch (ExpressionEvaluationException e) {
			throw new ComponentConfigurationException("There was a problem evaluating: ${project.runtimeClasspathElements}", e);
		}

		// Add the project dependencies to the ClassRealm
		final URL[] urls = buildURLs(runtimeClasspathElements);
		for (URL url : urls) {
			containerRealm.addConstituent(url);
		}
	}

	private static URL[] buildURLs(List<String> runtimeClasspathElements) throws ComponentConfigurationException {
		// Add the projects classes and dependencies
		List<URL> urls = new ArrayList<URL>(runtimeClasspathElements.size());
		for (String element : runtimeClasspathElements) {
			try {
				final URL url = new File(element).toURI().toURL();
				urls.add(url);
				// if (LOGGER.isDebugEnabled()) {
				// LOGGER.debug("Added to project class loader: " + url);
				// }
			} catch (MalformedURLException e) {
				throw new ComponentConfigurationException("Unable to access project dependency: " + element, e);
			}
		}

		// Add the plugin's dependencies (so Trove stuff works if Trove isn't on
		return urls.toArray(new URL[urls.size()]);
	}

}