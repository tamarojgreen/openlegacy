package org.openlegacy.spa;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.layout.PageBuilder;
import org.openlegacy.layout.PageDefinition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractAngularController {

	protected static final String HTML_VIEW_PATH = "/app/views/{0}.html";

	private static final String CONTROLLER_JS_PATH = "/app/js/app/controllers.js";

	private static final String ENTITY_CONTROLLER_JS_PATH = "/app/js/app/{0}.js";

	private static final String APP_JS_PATH = "/app/js/app/app.js";

	private static final String GENERIC_HTML_TEMPLATE = "/app/views/generic.html.template";

	@Inject
	private ServletContext servletContext;

	@Inject
	private EntitiesRegistry<?, ?, ?> entitiesRegistry;

	@Inject
	private PageBuilder<EntityDefinition<?>, ?> pageBuilder;

	@RequestMapping(value = "app/views/{entityName}.html", method = RequestMethod.GET)
	public void getView(@PathVariable("entityName") String entityName, HttpServletResponse response) throws IOException,
			TemplateException {

		URL resource = servletContext.getResource(MessageFormat.format(HTML_VIEW_PATH, entityName));
		if (resource != null) {
			String content = IOUtils.toString(resource, CharEncoding.UTF_8);
			IOUtils.write(content, response.getOutputStream(), CharEncoding.UTF_8);
		} else {
			Template template = initTemplate(GENERIC_HTML_TEMPLATE);
			EntityDefinition<?> entityDefinition = entitiesRegistry.get(entityName);

			PageDefinition pageDefinition = pageBuilder.build(entityDefinition);

			writeTemplate(template, pageDefinition, response.getWriter());
		}

	}

	@RequestMapping(value = "app/js/app/controllers.js_ng", method = RequestMethod.GET)
	public void getController(HttpServletResponse response) throws IOException, TemplateException {
		Template template = initTemplate(CONTROLLER_JS_PATH);
		response.setContentType("application/javascript");
		writeTemplate(template, entitiesRegistry, response.getWriter());
	}

	@RequestMapping(value = "app/js/app/app.js_ng", method = RequestMethod.GET)
	public void getAppJs(HttpServletResponse response) throws IOException, TemplateException {
		Template template = initTemplate(APP_JS_PATH);
		response.setContentType("application/javascript");
		writeTemplate(template, entitiesRegistry, response.getWriter());
	}

	@RequestMapping(value = "app/js/app/{entityName}.js_ng", method = RequestMethod.GET)
	public void getEntityController(@PathVariable("entityName") String entityName, HttpServletResponse response)
			throws IOException, TemplateException {
		URL resource = servletContext.getResource(MessageFormat.format(ENTITY_CONTROLLER_JS_PATH, entityName));
		if (resource != null) {
			String content = IOUtils.toString(resource, CharEncoding.UTF_8);
			IOUtils.write(content, response.getOutputStream(), CharEncoding.UTF_8);
		}
	}

	protected static void writeTemplate(Template template, Object model, PrintWriter writer) throws TemplateException,
			IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter output = new OutputStreamWriter(baos, CharEncoding.UTF_8);

		template.process(model, output);
		byte[] bytes = baos.toByteArray();
		String result = new String(bytes, "UTF-8");
		IOUtils.write(result, writer);

	}

	protected Template initTemplate(String resourceName) throws MalformedURLException, IOException {
		URL genericTemplate = servletContext.getResource(resourceName);
		String templateString = IOUtils.toString(genericTemplate, CharEncoding.UTF_8);
		Template template = new Template("Template", new StringReader(templateString), new Configuration(
				Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
		return template;
	}
}
