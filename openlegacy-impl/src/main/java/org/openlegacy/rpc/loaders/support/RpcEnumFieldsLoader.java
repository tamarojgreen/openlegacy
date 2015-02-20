package org.openlegacy.rpc.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.loaders.support.EnumFieldsLoader;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.MessageFormat;

@Component
public class RpcEnumFieldsLoader extends EnumFieldsLoader {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		EntityDefinition entityDefintion = entitiesRegistry.get(containingClass);
		if (entityDefintion != null) {
			super.load(entitiesRegistry, field, containingClass, fieldOrder);
		} else {
			// look in RPC entities parts
			RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;
			RpcPartEntityDefinition rpcPart = rpcEntitiesRegistry.getPart(containingClass);
			if (rpcPart != null) {
				String fieldName = MessageFormat.format("{0}.{1}", rpcPart.getPartName(), field.getName());
				FieldDefinition fieldDefinition = rpcPart.getFieldsDefinitions().get(fieldName);
				loadFieldDefinition(fieldDefinition, field);
			}
		}
	}

}
