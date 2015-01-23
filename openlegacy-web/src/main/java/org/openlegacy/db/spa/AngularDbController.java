package org.openlegacy.db.spa;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.layout.PageBuilder;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.spa.AbstractAngularController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AngularDbController extends AbstractAngularController {

	@Inject
	private ServletContext servletContext;

	@Inject
	private EntitiesRegistry<?, ?, ?> entitiesRegistry;

	@Inject
	private PageBuilder<EntityDefinition<?>, ?> pageBuilder;

	private static final String GENERIC_HTML_LIST_TEMPLATE = "app/views/generic.html.list.template";
	private static final String GENERIC_HTML_EDIT_TEMPLATE = "app/views/generic.html.edit.template";

	@Override
	@RequestMapping(value = "app/views/{entityName}.html", method = RequestMethod.GET)
	public void getView(@PathVariable("entityName") String entityName, HttpServletResponse response) throws IOException,
	TemplateException {
		int idx = entityName.indexOf(".");
		if (idx == -1) {
			super.getView(entityName, response);
		} else {
			String[] partsArray = entityName.split("\\.");
			String mode = partsArray[partsArray.length - 1];

			URL resource = servletContext.getResource(MessageFormat.format(HTML_VIEW_PATH, entityName));

			if (resource != null) {
				String content = IOUtils.toString(resource, CharEncoding.UTF_8);
				IOUtils.write(content, response.getOutputStream(), CharEncoding.UTF_8);
			} else {
				Template template = null;
				if (mode.equals("list")) {
					template = initTemplate(GENERIC_HTML_LIST_TEMPLATE);
				} else {
					template = initTemplate(GENERIC_HTML_EDIT_TEMPLATE);
				}

				EntityDefinition<?> entityDefinition = entitiesRegistry.get(entityName);

				PageDefinition pageDefinition = pageBuilder.build(entityDefinition);

				writeTemplate(template, pageDefinition, response.getWriter());
			}
		}
	}
}
