package org.openlegacy.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.loaders.RegistryLoader;
import org.openlegacy.loaders.support.RegistryReloader;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.File;
import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class RegistryReloaderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static Log logger = LogFactory.getLog(RegistryReloaderServlet.class);

	private boolean cont = true;

	@SuppressWarnings("rawtypes")
	@Override
	public void init(ServletConfig config) throws ServletException {

		final long compileInterval = Long.parseLong(config.getInitParameter("compileInterval"));
		final long reloadAttemptInterval = Long.parseLong(config.getInitParameter("reloadAttemptInterval"));
		String apiSourcesDir = config.getInitParameter("apiSourcesPath");

		String enabled = System.getProperty(OpenLegacyProperties.DESIGN_TIME);
		if (enabled == null || enabled.equals("false")) {
			System.out.println("API classes reloading is not enabled");
			return;
		} else {
			System.out.println("API classes reloading is running");
		}

		final ServletContext servletContext = config.getServletContext();
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		final Collection<EntitiesRegistry> registries = context.getBeansOfType(EntitiesRegistry.class).values();
		final RegistryLoader registryLoader = context.getBean(RegistryLoader.class);

		final RegistryReloader registryReloader = new RegistryReloader(apiSourcesDir, compileInterval);
		Thread runner = new Thread() {

			@Override
			public void run() {

				while (cont) {
					File webAppRootDir = new File(servletContext.getRealPath(""));
					for (EntitiesRegistry registry : registries) {
						registryReloader.scanForChanges(webAppRootDir, registry, registryLoader);
					}
					try {
						Thread.sleep(reloadAttemptInterval);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}

		};
		runner.start();

	}

	@Override
	public void destroy() {
		cont = false;
	}
}
