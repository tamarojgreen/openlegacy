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
package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;

public class OpenLegacyParseException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	private ParseResults parseResults;

	public OpenLegacyParseException(String message) {
		super(message);
	}

	public OpenLegacyParseException(String message, Exception e) {
		super(message, e);
	}

	public OpenLegacyParseException(String message, ParseResults parseResults) {
		this(message);
		this.parseResults = parseResults;
	}

	public OpenLegacyParseException(Exception e, ParseResults parseResults) {
		super(e);
		this.parseResults = parseResults;
	}

	public ParseResults getParseResults() {
		return parseResults;
	}
}
