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
package org.openlegacy.terminal.wait_conditions;

/**
 * An adapter wait condition is used mainly to store wait interval and timeout
 * 
 * @author Roi Mor
 * 
 */
public abstract class WaitCoditionAdapter implements WaitCondition {

	private long waitInterval = 250;
	private long waitTimeout = 2000;

	@Override
	public long getWaitInterval() {
		return waitInterval;
	}

	@Override
	public long getWaitTimeout() {
		return waitTimeout;
	}

	public void setWaitInterval(long waitInterval) {
		this.waitInterval = waitInterval;
	}

	public void setWaitTimeout(long waitTimeout) {
		this.waitTimeout = waitTimeout;
	}
}
