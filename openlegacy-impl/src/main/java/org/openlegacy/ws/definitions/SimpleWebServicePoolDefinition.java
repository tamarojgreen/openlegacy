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

package org.openlegacy.ws.definitions;

public class SimpleWebServicePoolDefinition implements WebServicePoolDefinition {

	String name;
	Class<?> poolClass;
	int maxConnections;
	long keepAliveInterval, returnSessionsInterval = 100/* def from class */;
	boolean stopThreads = false/* def from class */;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?> getPoolClass() {
		return poolClass;
	}

	@Override
	public int getMaxConnection() {
		return maxConnections;
	}

	@Override
	public long getKeepAliveInterval() {
		return keepAliveInterval;
	}

	@Override
	public long getReturnSessionsInterval() {
		return returnSessionsInterval;
	}

	@Override
	public boolean getStopThreads() {
		return stopThreads;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPoolClass(Class<?> poolClass) {
		this.poolClass = poolClass;
	}

	public void setKeepAliveInterval(long keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
	}

	public void setReturnSessionsInterval(long returnSessionsInterval) {
		this.returnSessionsInterval = returnSessionsInterval;
	}

	public void setStopThreads(boolean stopThreads) {
		this.stopThreads = stopThreads;
	}

}
