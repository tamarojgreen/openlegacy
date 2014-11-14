package org.openlegacy.rpc.support.binders;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.definitions.RpcDateFieldTypeDefinition;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcPojoFieldAccessor;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class RpcDateFieldsPatternBinder implements RpcEntityBinder, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(RpcDateFieldsPatternBinder.class);

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Inject
	private FieldFormatter fieldFormatter;

	@Override
	public void populateEntity(Object rpcEntity, RpcResult result) {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());
		Collection<RpcFieldDefinition> fieldsDefinitions = rpcDefinition.getFieldsDefinitions().values();

		RpcPojoFieldAccessor fieldAccessor = null;
		List<RpcField> rpcFields = result.getRpcFields();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {
			if (!(fieldMatch(rpcFieldDefinition))) {
				continue;
			}
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
			}

			RpcFlatField dateField = (RpcFlatField)rpcFields.get(rpcFieldDefinition.getOrder());
			if (dateField != null) {
				Object apiFieldValue = toApi(rpcFieldDefinition, dateField.getValue());
				fieldAccessor.setFieldValue(rpcFieldDefinition.getShortName(), apiFieldValue);
			}
		}
	}

	@Override
	public void populateAction(RpcInvokeAction remoteAction, Object entity) {
		SimpleRpcPojoFieldAccessor fieldAccessor = null;

		Assert.isTrue(entity instanceof RpcEntity, "rpc entity must implement RpcEntity interface");
		RpcEntity rpcEntity = (RpcEntity)entity;
		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());

		Collection<RpcFieldDefinition> fieldsDefinitions = rpcEntityDefinition.getFieldsDefinitions().values();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {

			if (!fieldMatch(rpcFieldDefinition)) {
				continue;
			}
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
			}

			Object dateApiFieldValue = fieldAccessor.evaluateFieldValue(rpcFieldDefinition.getShortName());
			RpcFlatField dateField = RpcFieldsBinder.getRpcFlatField(rpcFieldDefinition, fieldAccessor, null);
			// Object datelegacyFieldValue =toLegacy(rpcFieldDefinition,dateApiFieldValue,dateField);

			dateField.setValue(toLegacy(rpcFieldDefinition, dateApiFieldValue, dateField));

			remoteAction.getFields().add(dateField);
		}
	}

	@Override
	public boolean fieldMatch(RpcFieldDefinition rpcFieldDefinition) {
		if (rpcFieldDefinition.getJavaType() != Date.class) {
			return false;
		}
		return true;
	}

	@Override
	public Object toApi(RpcFieldDefinition rpcFieldDefinition, Object fiieldValue) {

		Date apiFieldValue = null;
		RpcDateFieldTypeDefinition fieldTypeDefinition = (RpcDateFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
		Assert.notNull(fieldTypeDefinition, "A field of type date is defined without @DateField annotation");
		String pattern = fieldTypeDefinition.getPattern();

		SimpleDateFormat dateFormater = new SimpleDateFormat(pattern, new Locale(fieldTypeDefinition.getLocale()));
		try {
			String value = (String)fiieldValue;
			if (!StringUtils.isBlank(value)) {

				value = fieldFormatter.format(value);
				if (!value.equals(rpcFieldDefinition.getNullValue())) {
					apiFieldValue = dateFormater.parse(value);

					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Date field: {0} was set with: {1}", rpcFieldDefinition.getName(),
								apiFieldValue));
					}
				}
			}

		} catch (ParseException e) {
			logger.warn(MessageFormat.format("Unable to bind date field:{0}. {1}", rpcFieldDefinition.getName(), e.getMessage()));
		}
		return apiFieldValue;
	}

	@Override
	public Object toLegacy(RpcFieldDefinition rpcFieldDefinition, Object apiFieldValue, RpcFlatField dateField) {

		String legacyFieldValue = "";
		RpcDateFieldTypeDefinition fieldTypeDefinition = (RpcDateFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
		Assert.notNull(fieldTypeDefinition, "A field of type Date is defined without @RpcDateField annotation");

		String pattern = fieldTypeDefinition.getPattern();

		SimpleDateFormat dateFormater = new SimpleDateFormat(pattern, new Locale(fieldTypeDefinition.getLocale()));

		if (apiFieldValue != null && !apiFieldValue.equals("")) {
			legacyFieldValue = dateFormater.format(apiFieldValue);

		} else {
			if (rpcFieldDefinition.getNullValue() != null) {
				legacyFieldValue = rpcFieldDefinition.getNullValue();
			}
		}
		return legacyFieldValue;
	}

}
