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
package org.openlegacy.rpc.definitions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.annotations.rpc.ActionProperty;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

public class SimpleRpcActionDefinition extends SimpleActionDefinition implements RpcActionDefinition, Serializable {

	private static final long serialVersionUID = 1L;
	private String programPath;
	private Map<QName, String> properties = null;
	private final static Log logger = LogFactory.getLog(SimpleRpcActionDefinition.class);

	public SimpleRpcActionDefinition(SessionAction<? extends Session> action, String displayName) {
		super(action, displayName);
	}

	public SimpleRpcActionDefinition(String actionName, String displayName) {
		super(actionName, displayName);
	}

	@Override
	public String getProgramPath() {
		return programPath;
	}

	public void setProgramPath(String programPath) {
		this.programPath = programPath;
	}

	public Map<QName, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<QName, String> properties) {
		this.properties = properties;
	}

	public void loadProperties(ActionProperty[] actionProperties) {

		properties = new HashMap<QName, String>();

		for (ActionProperty actionProperty : actionProperties) {
			properties.put(new QName(actionProperty.name()), actionProperty.value());
		}

		// if (logger.isDebugEnabled() && properties.size() > 0) {
		// logger.debug(MessageFormat.format("Action properties from file {0} where added to {1} - \"{2}\" ",
		// actionProperties.toString(), getActionName(), getDisplayName()));
		// }

	}
}