package com.test;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.addHeader("Content-Type", "text/javascript; charset=utf-8");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (req.getRequestURI().contains("credit")) {
			IOUtils.copy(getClass().getResourceAsStream("credit.json"), baos);
		} else {
			IOUtils.copy(getClass().getResourceAsStream("dummy.json"), baos);
		}

		String content = new String(baos.toByteArray(), "UTF-8");
		content = content.replace("JSON_CALLBACK", req.getParameter("callback"));
		resp.getWriter().write(content);
		resp.flushBuffer();
	}
}
