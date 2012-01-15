package org.h3270.render;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alphonse Bendt
 * @version $Id: H3270Configuration.java,v 1.9 2008/11/21 14:48:32 spiegel Exp $
 */
public class H3270Configuration extends org.apache.avalon.framework.configuration.DefaultConfiguration {

	private final List colorSchemes = new ArrayList();
	private final String colorSchemeDefault;
	private final Map validFonts = new HashMap();
	private final String fontnameDefault;
	private final String logicalUnitBuilderClass;

	private static final Pattern FILENAME_PATTERN = Pattern.compile("file:(.*?)/WEB-INF/h3270-config\\.xml.*");

	private H3270Configuration(Configuration data) throws ConfigurationException {
		super(data.getName());

		final Configuration colorschemeConfig = data.getChild("colorschemes");
		createColorSchemes(colorschemeConfig);
		colorSchemeDefault = createColorschemeDefault(colorschemeConfig, colorSchemes);

		final Configuration fontConfig = data.getChild("fonts");
		createValidFonts(fontConfig);
		fontnameDefault = createFontnameDefault(fontConfig, validFonts);

		// If exec-path points into WEB-INF, convert it into an absolute path now.
		DefaultConfiguration c = (DefaultConfiguration)data.getChild("exec-path");
		String execPath = c.getValue("");
		if (execPath.startsWith("WEB-INF")) {
			Matcher m = FILENAME_PATTERN.matcher(getLocation());
			if (m.find()) {
				execPath = m.group(1) + "/" + execPath;
				c.setValue(execPath);
			}
		}
		logicalUnitBuilderClass = createLogicalUnitBuilderClass(data.getChild("logical-units"));
	}

	public List getColorSchemes() {
		return colorSchemes;
	}

	public String getColorSchemeDefault() {
		return colorSchemeDefault;
	}

	public ColorScheme getColorScheme(String name) {
		for (Iterator i = colorSchemes.iterator(); i.hasNext();) {
			ColorScheme cs = (ColorScheme)i.next();
			if (cs.getName().equals(name)) {
				return cs;
			}
		}
		return null;
	}

	public Map getValidFonts() {
		return validFonts;
	}

	private void createColorSchemes(Configuration config) throws ConfigurationException {
		Configuration[] cs = config.getChildren();
		for (int x = 0; x < cs.length; ++x) {
			ColorScheme scheme = new ColorScheme(cs[x].getAttribute("name"), cs[x].getAttribute("pnfg"),
					cs[x].getAttribute("pnbg"), cs[x].getAttribute("pifg"), cs[x].getAttribute("pibg"),
					cs[x].getAttribute("phfg"), cs[x].getAttribute("phbg"), cs[x].getAttribute("unfg"),
					cs[x].getAttribute("unbg"), cs[x].getAttribute("uifg"), cs[x].getAttribute("uibg"),
					cs[x].getAttribute("uhfg"), cs[x].getAttribute("uhbg"));
			colorSchemes.add(scheme);
		}
	}

	private String createColorschemeDefault(Configuration config, List colorSchemes) throws ConfigurationException {
		String colorSchemeDefault = config.getAttribute("default", null);
		if (colorSchemeDefault == null) {
			if (colorSchemes.isEmpty()) {
				throw new ConfigurationException("need to specify at least one colorscheme");
			}
			// default to first colorscheme
			colorSchemeDefault = ((ColorScheme)colorSchemes.get(0)).getName();
		}
		return colorSchemeDefault;
	}

	private void createValidFonts(Configuration config) throws ConfigurationException {
		Configuration[] fonts = config.getChildren();
		for (int x = 0; x < fonts.length; ++x) {
			String fontName = fonts[x].getAttribute("name");
			String fontDescription = fonts[x].getAttribute("description", fontName);
			validFonts.put(fontName, fontDescription);
		}
	}

	private String createFontnameDefault(Configuration config, Map validFonts) throws ConfigurationException {
		String fontnameDefault = config.getAttribute("default", null);
		if (fontnameDefault == null) {
			if (validFonts.isEmpty()) {
				throw new ConfigurationException("need to specify at least one font");
			}
			// default to random fontname
			fontnameDefault = validFonts.keySet().iterator().next().toString();
		}
		return fontnameDefault;
	}

	public String getFontnameDefault() {
		return fontnameDefault;
	}

	public String createLogicalUnitBuilderClass(Configuration config) throws ConfigurationException {
		boolean usePool = config.getChild("use-pool").getValueAsBoolean();
		if (usePool) {
			return config.getChild("lu-builder").getValue();
		}
		return null;
	}

	public static H3270Configuration create(String filename) {
		try {
			return create(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static H3270Configuration create(InputStream in) {
		try {
			DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
			Configuration data = builder.build(in);
			return new H3270Configuration(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public String getLogicalUnitBuilderClass() {
		return logicalUnitBuilderClass;
	}
}