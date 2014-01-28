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
package org.openlegacy.web.tags;

import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

public class SpanTag extends AbstractHtmlElementTag {

	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("span");
		writeDefaultAttributes(tagWriter);
		tagWriter.appendValue(getDisplayString(getBoundValue()));
		tagWriter.endTag();
		return EVAL_BODY_INCLUDE;
	}
}
