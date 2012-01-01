package org.openlegacy.providers.applinx.web;

import com.sabratec.applinx.baseobject.GXIScreen;
import com.sabratec.applinx.presentation.GXRenderConfig;
import com.sabratec.applinx.presentation.internal.GXIRenderer;
import com.sabratec.applinx.presentation.internal.GXRendererFactory;
import com.sabratec.applinx.presentation.internal.tags.GXTagRenderer;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;

public class ApxTerminalSnapshotHtmlRenderer implements TerminalSnapshotHtmlRenderer {

	public String render(TerminalSnapshot terminalSnapshot) {
		GXIScreen screen = (GXIScreen)terminalSnapshot.getDelegate();
		GXRenderConfig renderConfig = new GXRenderConfig();
		GXIRenderer renderer = GXRendererFactory.instance().getRenderer(GXRendererFactory.HTML_RENDERER, screen, null,
				renderConfig, GXTagRenderer.RENDER_ALL_FIELDS);
		return renderer.render();
	}
}
