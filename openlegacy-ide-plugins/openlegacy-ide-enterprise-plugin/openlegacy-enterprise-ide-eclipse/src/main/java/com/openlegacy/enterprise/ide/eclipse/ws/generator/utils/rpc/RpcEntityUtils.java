package com.openlegacy.enterprise.ide.eclipse.ws.generator.utils.rpc;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcBigIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcPartModel;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.definitions.BooleanFieldTypeDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcPartDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntityUtils {

	public static List<RpcFieldModel> getFields(List<RpcFieldDefinition> fields, AbstractNamedModel parent) {
		List<RpcFieldModel> list = new ArrayList<RpcFieldModel>();
		for (RpcFieldDefinition definition : fields) {
			RpcFieldModel model = null;
			if (definition.getFieldTypeDefinition() instanceof BooleanFieldTypeDefinition) {
				model = new RpcBooleanFieldModel(definition, parent);
			} else {
				if (StringUtils.equalsIgnoreCase(((SimpleRpcFieldDefinition)definition).getJavaTypeName(),
						Integer.class.getSimpleName())) {
					model = new RpcIntegerFieldModel(definition, parent);
				} else if (StringUtils.equalsIgnoreCase(((SimpleRpcFieldDefinition)definition).getJavaTypeName(),
						BigInteger.class.getSimpleName())) {
					model = new RpcBigIntegerFieldModel(definition, parent);
				} else {
					model = new RpcFieldModel(definition, parent);
				}
			}
			if (model != null) {
				list.add(model);
			}
		}
		return list;
	}

	public static List<RpcPartModel> getParts(CodeBasedRpcEntityDefinition entityDefinition, AbstractNamedModel parent) {
		List<RpcPartModel> list = new ArrayList<RpcPartModel>();
		Collection<PartEntityDefinition<RpcFieldDefinition>> values = entityDefinition.getPartsDefinitions().values();
		for (PartEntityDefinition<RpcFieldDefinition> partEntityDefinition : values) {
			if (partEntityDefinition instanceof CodeBasedRpcPartDefinition) {
				list.add(new RpcPartModel((CodeBasedRpcPartDefinition)partEntityDefinition, parent));
			}
		}
		return list;
	}

}
