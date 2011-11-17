package org.openlegacy.terminal.modules.table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostAction;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.util.Assert;

import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Default terminal session table scroller. Perform scroll down until the given row keys are matched. Uses TableStopCondition to
 * decide if the scrolling is not affective any more (e.g: reach to bottom)
 * 
 */
public class DefaultTableScroller implements TableScroller<TerminalSession> {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	private HostAction defaultNextAction;

	private final static Log logger = LogFactory.getLog(DefaultTableScroller.class);

	@SuppressWarnings("unchecked")
	public <T> T scroll(TerminalSession terminalSession, Class<T> entityClass,
			TableScrollStopConditions tableScrollStopConditions, Object... rowKeys) {

		T beforeScrolllEntity = terminalSession.getEntity(entityClass);

		if (tableScrollStopConditions.shouldStop(beforeScrolllEntity)) {
			logger.debug(MessageFormat.format("Table stop condition met for {0}. stopping scroll", entityClass));
			return null;
		}

		TableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider, entityClass).getValue();

		HostAction nextAction = tableDefinition.getNextScreenAction() != null ? tableDefinition.getNextScreenAction()
				: defaultNextAction;

		Assert.notNull(
				nextAction,
				MessageFormat.format("Next action not defined either as default nor at screen entity {0}",
						tableDefinition.getTableClass()));

		Object afterScrolllEntity = terminalSession.doAction(nextAction, null);

		if (tableScrollStopConditions.shouldStop(beforeScrolllEntity, afterScrolllEntity)) {
			logger.debug(MessageFormat.format("Table stop condition met for {0}. stopping scroll", entityClass));
			return null;
		}

		return (T)afterScrolllEntity;

	}

	public void setDefaultNextAction(Class<? extends HostAction> defaultNextAction) {
		this.defaultNextAction = ReflectionUtil.newInstance(defaultNextAction);
	}
}
