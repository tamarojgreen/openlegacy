package org.openlegacy.db.spa;

import freemarker.template.TemplateException;

import org.openlegacy.spa.AbstractAngularController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AngularDbController extends AbstractAngularController {

	@RequestMapping(value = "app/views/{entityName}/{entityName}New.html", method = RequestMethod.GET)
	public void getViewNew(@PathVariable("entityName") String entityName, HttpServletResponse response) throws IOException,
			TemplateException {
		String asd = "";
		asd = "123";
		asd.length();
	}

	@RequestMapping(value = "app/views/{entityName}/{entityName}.html", method = RequestMethod.GET)
	public void getViewList(@PathVariable("entityName") String entityName, HttpServletResponse response) throws IOException,
			TemplateException {
		String asd = "";
		asd = "123";
		asd.length();
	}
}
