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

	public long getWaitInterval() {
		return waitInterval;
	}

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
