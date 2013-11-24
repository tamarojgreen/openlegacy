package org.openlegacy.designtime.rpc.source.parsers;

/**
 * Build an RpcEntityDefinition from list of parameters.
 * 
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldType.General;
import org.openlegacy.designtime.formatters.DefinitionFormatter;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcPartEntityDefinition;
import org.openlegacy.utils.StringUtil;

import java.util.List;
import java.util.Map;

public class RpcEntityDefinitionBuilderImpl implements RpcEntityDefinitionBuilder {

	private final static Log logger = LogFactory.getLog(RpcEntityDefinitionBuilderImpl.class);

	private FieldInformationFactory fieldInformationFactory;

	private DefinitionFormatter definitionFormatter;

	public RpcEntityDefinitionBuilderImpl(FieldInformationFactory fieldInformationFactory) {
		this.fieldInformationFactory = fieldInformationFactory;
	}

	private RpcFieldDefinition buildRpcFieldDefinition(ParameterStructure parameter, int order) {

		FieldInformation fieldInformation = fieldInformationFactory.getObject(parameter.getVariableDeclaration(),
				parameter.getCount());

		String javaFieldName = StringUtil.toJavaFieldName(parameter.getFieldName().toLowerCase());

		SimpleRpcFieldDefinition rpcFieldDefinition = new SimpleRpcFieldDefinition(javaFieldName, General.class);
		rpcFieldDefinition.setOriginalName(parameter.getFieldName());
		rpcFieldDefinition.setOrder(order);
		rpcFieldDefinition.setLength(fieldInformation.getLength());

		rpcFieldDefinition.setJavaType(fieldInformation.getJavaType());
		rpcFieldDefinition.setFieldTypeDefinition(fieldInformation.getType());

		definitionFormatter.format(rpcFieldDefinition);
		return rpcFieldDefinition;
	}

	public RpcPartEntityDefinition buildRpcPartDefinition(String name, List<ParameterStructure> partFieldList, int order,
			int count) {

		SimpleRpcPartEntityDefinition rpcPartEntityDefinition = new SimpleRpcPartEntityDefinition(null);
		rpcPartEntityDefinition.setOriginalName(name);
		rpcPartEntityDefinition.setPartName(name);
		rpcPartEntityDefinition.setDisplayName(StringUtil.toDisplayName(name));
		rpcPartEntityDefinition.getCount(count);

		rpcPartEntityDefinition.setOrder(order);
		Map<String, RpcFieldDefinition> rpcFieldsMap = rpcPartEntityDefinition.getFieldsDefinitions();
		Map<String, RpcPartEntityDefinition> rpcPartInnerParts = rpcPartEntityDefinition.getInnerPartsDefinitions();
		for (int internalOrder = 0; internalOrder < partFieldList.size(); internalOrder++) {
			ParameterStructure partField = partFieldList.get(internalOrder);
			String partFieldName = partField.getFieldName();
			if (partField.isSimple()) {
				RpcFieldDefinition rpcFieldDefinition = buildRpcFieldDefinition(partField, internalOrder);
				rpcFieldsMap.put(rpcFieldDefinition.getName(), rpcFieldDefinition);
			} else {
				RpcPartEntityDefinition subPartEntityDefinition = buildRpcPartDefinition(partFieldName, partField.getSubFields(),
						internalOrder, partField.getCount());

				rpcPartInnerParts.put(subPartEntityDefinition.getPartName(), subPartEntityDefinition);
			}
		}

		definitionFormatter.format(rpcPartEntityDefinition);
		return rpcPartEntityDefinition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.RpcEntityDefinitionBuilder#build(java.lang.String, java.util.List)
	 */
	public void build(List<ParameterStructure> paramtersNodes, RpcEntityDefinition entityDefinition) {

		for (int parameterOrder = 0; parameterOrder < paramtersNodes.size(); parameterOrder++) {
			ParameterStructure interfaceParmeter = paramtersNodes.get(parameterOrder);

			if (interfaceParmeter.isSimple()) {

				RpcFieldDefinition rpcFieldDefinition = buildRpcFieldDefinition(interfaceParmeter, parameterOrder);
				entityDefinition.getFieldsDefinitions().put(rpcFieldDefinition.getName(), rpcFieldDefinition);
			} else {
				RpcPartEntityDefinition rpcPartEntityDefinition = buildRpcPartDefinition(interfaceParmeter.getFieldName(),
						interfaceParmeter.getSubFields(), parameterOrder, interfaceParmeter.getCount());

				entityDefinition.getPartsDefinitions().put(rpcPartEntityDefinition.getPartName(), rpcPartEntityDefinition);

			}
			logger.debug(interfaceParmeter.toString());
		}
	}

	public void setDefinitionFormatter(DefinitionFormatter definitionFormatter) {
		this.definitionFormatter = definitionFormatter;
	}
}
