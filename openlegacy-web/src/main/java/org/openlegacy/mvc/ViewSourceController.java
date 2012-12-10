/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.mvc;

import org.apache.commons.io.IOUtils;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/viewSource")
public class ViewSourceController {

	private boolean enabled = false;
	private String htmlOutput = "<html><body><pre>{0}</pre></body></html>";

	@RequestMapping(method = RequestMethod.GET)
	public void viewSource(@RequestParam("file") String file, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (!enabled) {
			throw (new OpenLegacyRuntimeException("View source controller is not enabled"));
		}

		ServletContext servletContext = request.getSession().getServletContext();

		InputStream resource = servletContext.getResourceAsStream(file);
		if (resource == null) {
			throw (new OpenLegacyRuntimeException("Specified resource doesn''t exists"));
		}
		String content = IOUtils.toString(resource);
		content = HtmlUtils.htmlEscape(content);
		response.getWriter().write(MessageFormat.format(htmlOutput, content));
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setHtmlOutput(String htmlOutput) {
		this.htmlOutput = htmlOutput;
	}
}
