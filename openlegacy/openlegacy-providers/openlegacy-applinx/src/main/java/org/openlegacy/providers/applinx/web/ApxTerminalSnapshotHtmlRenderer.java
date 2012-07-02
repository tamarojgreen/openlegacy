package org.openlegacy.providers.applinx.web;

import com.sabratec.applinx.baseobject.GXIScreen;
import com.sabratec.applinx.presentation.GXRenderConfig;
import com.sabratec.applinx.presentation.internal.GXIRenderer;
import com.sabratec.applinx.presentation.internal.GXRendererFactory;
import com.sabratec.applinx.presentation.internal.tags.GXTagNamesUtil;
import com.sabratec.applinx.presentation.internal.tags.GXTagRenderer;

import org.apache.commons.io.IOUtils;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;

import java.io.IOException;
import java.io.InputStream;

public class ApxTerminalSnapshotHtmlRenderer implements TerminalSnapshotHtmlRenderer {

	private static final String TERMINAL_HTML = "TERMINAL_HTML";
	private static final String APX_CURSOR = "APX_CURSOR";

	private String templateResourceName = "apxHtmlEmulationTemplate.html";
	private boolean includeTemplate = true;

	public String render(TerminalSnapshot terminalSnapshot) {
		GXIScreen screen = (GXIScreen)terminalSnapshot.getDelegate();
		GXRenderConfig renderConfig = new GXRenderConfig();
		GXIRenderer renderer = GXRendererFactory.instance().getRenderer(GXRendererFactory.HTML_RENDERER, screen, null,
				renderConfig, GXTagRenderer.RENDER_ALL_FIELDS);

		String htmlContent = renderer.render();

		if (includeTemplate) {
			return renderWithTemplate(screen, htmlContent);
		} else {
			return htmlContent;
		}
	}

	private String renderWithTemplate(GXIScreen screen, String htmlContent) {
		InputStream htmlEmulationStream = ApxTerminalSnapshotHtmlRenderer.class.getResourceAsStream(templateResourceName);
		String htmlEmulationContent;
		try {
			htmlEmulationContent = IOUtils.toString(htmlEmulationStream);
		} catch (IOException e) {
			throw (new RuntimeException(e));
		}
		htmlEmulationContent = htmlEmulationContent.replace(TERMINAL_HTML, htmlContent);

		String hostFieldName = GXTagNamesUtil.getHostFieldName(screen.getCursorPosition(), screen.getColumns());
		htmlEmulationContent = htmlEmulationContent.replace(APX_CURSOR, hostFieldName);
		return htmlEmulationContent;
	}

	public void setTemplateResourceName(String templateResourceName) {
		this.templateResourceName = templateResourceName;
	}

	public void setIncludeTemplate(boolean includeTemplate) {
		this.includeTemplate = includeTemplate;
	}
}
