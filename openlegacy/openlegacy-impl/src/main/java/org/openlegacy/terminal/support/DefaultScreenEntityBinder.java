package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.terminal.utils.ScreenNavigationUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Data binder implementation for building a screenEntity from a given terminal screen, and build fields to send from a given
 * screenEntity
 * 
 */
@Scope("sesssion")
// since performing action on terminalSession
public class DefaultScreenEntityBinder implements ScreenEntityBinder {

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private FieldMappingsDefinitionProvider fieldMappingsProvider;

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private List<ScreenEntityDataInjector<?>> screenEntityDataInjectors;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityBinder.class);

	@SuppressWarnings("unchecked")
	public <T> T buildScreenEntity(Class<T> screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			ScreenEntityNotAccessibleException {

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);

		ScreenNavigationUtil.validateCurrentScreen(screenEntity, matchedScreenEntity);

		return (T)buildScreenEntityInner(matchedScreenEntity, terminalScreen, true);
	}

	public Object buildScreenEntity(TerminalScreen terminalScreen, boolean deep) {
		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);
		return buildScreenEntityInner(matchedScreenEntity, terminalScreen, deep);
	}

	private Object buildScreenEntityInner(Class<?> screenEntityClass, TerminalScreen terminalScreen, boolean deep) {
		try {
			Object screenEntity = applicationContext.getBean(screenEntityClass);

			ScreenEntityFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(screenEntity);

			fieldAccessor.setTerminalScreen(terminalScreen);

			for (ScreenEntityDataInjector<?> screenEntityDataInjector : screenEntityDataInjectors) {
				screenEntityDataInjector.inject(fieldAccessor, screenEntityClass, terminalScreen, deep);
			}

			return screenEntity;
		} catch (Exception e) {
			throw (new IllegalArgumentException(e));
		}

	}

	public Map<ScreenPosition, String> buildSendFields(TerminalScreen terminalScreen, Object screenEntity) {
		Map<ScreenPosition, String> fieldValues = new LinkedHashMap<ScreenPosition, String>();

		if (screenEntity == null) {
			return fieldValues;
		}

		Collection<FieldMappingDefinition> fieldsInfo = fieldMappingsProvider.getFieldsMappingDefinitions(terminalScreen,
				screenEntity.getClass());

		if (fieldsInfo == null) {
			return fieldValues;
		}

		ScreenEntityFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(screenEntity);
		for (FieldMappingDefinition fieldMapping : fieldsInfo) {

			if (fieldMapping.isEditable()) {
				Object value = fieldAccessor.getFieldValue(fieldMapping.getName());
				ScreenPosition hostScreenPosition = fieldMapping.getScreenPosition();

				if (value != null) {
					fieldValues.put(hostScreenPosition, value.toString());
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format(
								"Field {0} was set with value \"{1}\" to send fields for screenEntity {2}",
								fieldMapping.getName(), value, screenEntity));
					}
				}
			}

		}
		return fieldValues;
	}

}
