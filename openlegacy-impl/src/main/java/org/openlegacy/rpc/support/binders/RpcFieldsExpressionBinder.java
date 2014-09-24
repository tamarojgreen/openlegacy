package org.openlegacy.rpc.support.binders;

import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcPojoFieldAccessor;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;
import org.openlegacy.utils.ExpressionUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Expression binder implementation for applying expressions to fields of a screenEntity based on expression in
 * {@link @RpcField) annotation settings
 * 
 * @author eberlyrh
 *
 */
public class RpcFieldsExpressionBinder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Inject
	private ExpressionParser expressionParser;

	public void populateEntity(Object rpcEntity, RpcResult result) {

		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());
		Collection<RpcFieldDefinition> fieldsDefinitions = rpcDefinition.getFieldsDefinitions().values();

		RpcPojoFieldAccessor fieldAccessor = null;
		List<RpcField> rpcFields = result.getRpcFields();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {
			if (StringUtil.isEmpty(rpcFieldDefinition.getExpression())) {
				continue;
			}
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
			}
			final String expression = rpcFieldDefinition.getExpression();
			final RpcField rpcField = rpcFields.get(rpcFieldDefinition.getOrder());
			if (ExpressionUtils.isRegularExpression(expression)) {
				final Object value = ExpressionUtils.applyRegularExpression(expression.trim(),
						((SimpleRpcFlatField)rpcField).getValue());
				fieldAccessor.setFieldValue(rpcFieldDefinition.getShortName(), value);
			} else {
				final Expression expr = expressionParser.parseExpression(expression);

				final EvaluationContext evaluationContext = ExpressionUtils.createEvaluationContext(rpcEntity, rpcField);

				final Object value = expr.getValue(evaluationContext, rpcFieldDefinition.getJavaType());
				fieldAccessor.setFieldValue(rpcFieldDefinition.getShortName(), value);
			}
		}
	}
}
