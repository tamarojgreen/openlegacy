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
package org.openlegacy.designtime.formatters;

public class ReplaceTextConfiguration {

	private String originalText;
	private String newText;

	private boolean prefix;
	private boolean suffix;

	private String sourcePropertyName;
	private String targetPropertyName;

	public String getOriginalText() {
		return originalText;
	}

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

	public String getNewText() {
		return newText;
	}

	public void setNewText(String newText) {
		this.newText = newText;
	}

	public boolean isPrefix() {
		return prefix;
	}

	public void setPrefix(boolean prefix) {
		this.prefix = prefix;
	}

	public boolean isSuffix() {
		return suffix;
	}

	public void setSuffix(boolean suffix) {
		this.suffix = suffix;
	}

	public String getSourcePropertyName() {
		return sourcePropertyName;
	}

	public void setSourcePropertyName(String sourcePropertyName) {
		this.sourcePropertyName = sourcePropertyName;
	}

	public String getTargetPropertyName() {
		return targetPropertyName;
	}

	public void setTargetPropertyName(String targetPropertyName) {
		this.targetPropertyName = targetPropertyName;
	}

}
