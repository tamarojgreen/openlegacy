package org.openlegacy.terminal.mvc.web;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.ui.Model;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class ScreenBindUtils {

	private final static Log logger = LogFactory.getLog(ScreenBindUtils.class);

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	/**
	 * Collects multiple screens table, and sets it to the view
	 * 
	 * @param entity
	 * @param uiModel
	 * @param session
	 * @param definition
	 */
	public void bindCollectTable(TerminalSession session, Object entity, Model uiModel) {
		Entry<String, ScreenTableDefinition> definitionEntry = ScrollableTableUtil.getSingleScrollableTableDefinition(
				tablesDefinitionProvider, entity.getClass());
		if (definitionEntry != null && definitionEntry.getValue().getScreensCount() > 1) {
			ScreenTableDefinition tableDefinition = definitionEntry.getValue();
			String screenEntityName = ProxyUtil.getOriginalClass(entity.getClass()).getSimpleName();
			List<?> rows = session.getModule(Table.class).collect(entity.getClass(), tableDefinition.getTableClass(),
					tableDefinition.getScreensCount());
			ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);
			String tableFieldName = tableDefinition.getTableEntityName() + "s";
			if (fieldAccessor.isExists(tableFieldName)) {
				fieldAccessor.setFieldValue(tableFieldName, rows);
			}
			uiModel.addAttribute(StringUtils.uncapitalize(screenEntityName), entity);
		}
	}

	/**
	 * Bind table input fields from http request to a screen entity
	 * 
	 * @param request
	 * @param entityClass
	 * @param screenEntity
	 */
	public void bindTables(HttpServletRequest request, Class<?> entityClass, ScreenEntity screenEntity) {
		ScreenEntityDefinition entityDefinition = entitiesRegistry.get(entityClass);
		Collection<ScreenTableDefinition> tableDefinitions = entityDefinition.getTableDefinitions().values();
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		for (ScreenTableDefinition tableDefinition : tableDefinitions) {
			List<ScreenColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
			String fieldName = tableDefinition.getTableEntityName() + "s";
			if (!fieldAccessor.isExists(fieldName)) {
				logger.warn(MessageFormat.format("Unable to find field named:{0} for binding http request", fieldName));
				continue;
			}
			List<?> rows = (List<?>)fieldAccessor.getFieldValue(fieldName);
			int rowNumber = 0;
			for (Object row : rows) {
				ScreenPojoFieldAccessor rowFieldAccessor = new SimpleScreenPojoFieldAccessor(row);
				for (ScreenColumnDefinition columnDefinition : columnDefinitions) {
					if (columnDefinition.isEditable()) {

						String[] parameterValues = request.getParameterValues(columnDefinition.getName());
						if (parameterValues != null && parameterValues.length > rowNumber) {
							String value = parameterValues[rowNumber];
							if (!StringUtils.isEmpty(value)) {
								rowFieldAccessor.setFieldValue(columnDefinition.getName(), value);
							}
						}
					}

				}
				rowNumber++;
			}
		}
	}
}
