package org.openlegacy.mvc;

public class SimpleOpenLegacyWebProperties implements OpenLegacyWebProperties {

	private String fallbackUrl;
	private boolean fallbackUrlOnError = true;

	public String getFallbackUrl() {
		return fallbackUrl;
	}

	public void setFallbackUrl(String fallbackUrl) {
		this.fallbackUrl = fallbackUrl;
	}

	public boolean isFallbackUrlOnError() {
		return fallbackUrlOnError;
	}

	public void setFallbackUrlOnError(boolean fallbackUrlOnError) {
		this.fallbackUrlOnError = fallbackUrlOnError;
	}

}
