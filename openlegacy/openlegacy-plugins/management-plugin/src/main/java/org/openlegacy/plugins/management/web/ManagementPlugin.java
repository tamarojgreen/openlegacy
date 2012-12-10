package org.openlegacy.plugins.management.web;

public class ManagementPlugin {

	private boolean enableDisconnect = true;
	private boolean enableSessionViewer = true;

	public boolean isEnableDisconnect() {
		return enableDisconnect;
	}

	public void setEnableDisconnect(boolean enableDisconnect) {
		this.enableDisconnect = enableDisconnect;
	}

	public boolean isEnableSessionViewer() {
		return enableSessionViewer;
	}

	public void setEnableSessionViewer(boolean enableSessionViewer) {
		this.enableSessionViewer = enableSessionViewer;
	}

}
