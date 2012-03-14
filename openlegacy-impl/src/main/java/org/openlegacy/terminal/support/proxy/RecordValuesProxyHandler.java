package org.openlegacy.terminal.support.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;
import org.openlegacy.definitions.AutoCompleteFieldTypeDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.PropertyUtil;

import java.util.Map;

import javax.inject.Inject;

public class RecordValuesProxyHandler implements ScreenEntityProxyHandler {

	private static final String VALUES = "Values";

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	public Object invoke(TerminalSession terminalSession, MethodInvocation invocation) throws OpenLegacyRuntimeException {
		Object target = invocation.getThis();
		String methodName = invocation.getMethod().getName();
		if (methodName.endsWith(VALUES)) {
			methodName = methodName.substring(0, methodName.length() - VALUES.length());
		}
		String propertyName = PropertyUtil.getPropertyNameIfGetter(methodName);

		Class<?> entityClass = target.getClass();
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(entityClass);
		ScreenFieldDefinition fieldDefinition = screenEntityDefinition.getFieldsDefinitions().get(propertyName);
		AutoCompleteFieldTypeDefinition fieldTypeDefinition = (AutoCompleteFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
		RecordsProvider<Session, Object> recordsProvider = fieldTypeDefinition.getRecordsProvider();

		ScreenTableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				fieldTypeDefinition.getSourceEntityClass()).getValue();

		@SuppressWarnings("unchecked")
		Map<Object, Object> records = recordsProvider.getRecords(terminalSession, fieldTypeDefinition.getSourceEntityClass(),
				(Class<Object>)tableDefinition.getTableClass(), fieldTypeDefinition.isCollectAll(), null);
		return records;
	}

}
