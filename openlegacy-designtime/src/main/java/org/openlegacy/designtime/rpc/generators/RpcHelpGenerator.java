package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.layout.PageDefinition;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

public class RpcHelpGenerator {

	@Inject
	private GenerateUtil generateUtil;

	public void generate(PageDefinition pageDefinition, OutputStream out) throws TemplateException, IOException {

		generateUtil.generate(pageDefinition, out, "RpcEntityHelpPage.html.template");

	}
}
