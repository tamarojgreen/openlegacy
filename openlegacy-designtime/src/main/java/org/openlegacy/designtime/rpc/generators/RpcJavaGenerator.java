package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

public class RpcJavaGenerator {

	@Inject
	private GenerateUtil generateUtil;

	public void generate(RpcEntityDefinition rpcEntityDefinition, OutputStream out) throws TemplateException, IOException {
		String typeName = rpcEntityDefinition.getTypeName();
		generateUtil.generate(rpcEntityDefinition, out, "RpcEntity.java.template", typeName);
	}
}
