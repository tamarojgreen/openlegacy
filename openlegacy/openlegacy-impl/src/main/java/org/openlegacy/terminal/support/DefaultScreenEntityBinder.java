package org.openlegacy.terminal.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostAction;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.FieldComparator;
import org.openlegacy.terminal.HostActionMapper;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.exceptions.SendActionException;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.ScreenNavigationUtil;
import org.openlegacy.terminal.utils.SimpleScreenEntityFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Data binder implementation for building a screenEntity from a given terminal screen, and build fields to send from a given
 * screenEntity
 * 
 */
public class DefaultScreenEntityBinder implements ScreenEntityBinder {

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private FieldMappingsDefinitionProvider fieldMappingsProvider;

	@Inject
	private List<ScreenEntityDataInjector> screenEntityDataInjectors;

	@Inject
	private HostActionMapper hostActionMapper;

	@Inject
	private FieldComparator fieldComparator;

	@Inject
	private ApplicationContext applicationContext;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityBinder.class);

	@SuppressWarnings("unchecked")
	public <T> T buildScreenEntity(Class<T> screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			ScreenEntityNotAccessibleException {

		Assert.notNull(screenEntity);
		Assert.notNull(terminalScreen);

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);

		if (matchedScreenEntity == null) {
			return null;
		}

		ScreenNavigationUtil.validateCurrentScreen(screenEntity, matchedScreenEntity);

		return (T)buildScreenEntityInner(matchedScreenEntity, terminalScreen);
	}

	public <S extends ScreenEntity> S buildScreenEntity(TerminalScreen terminalScreen) {
		Assert.notNull(terminalScreen);

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);
		if (matchedScreenEntity == null) {
			return null;
		}
		return buildScreenEntityInner(matchedScreenEntity, terminalScreen);
	}

	private <S extends ScreenEntity> S buildScreenEntityInner(Class<?> screenEntityClass, TerminalScreen terminalScreen) {

		@SuppressWarnings("unchecked")
		S screenEntity = (S)createEntity((Class<? extends ScreenEntity>)screenEntityClass);

		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(screenEntity);

		fieldAccessor.setTerminalScreen(terminalScreen);

		for (ScreenEntityDataInjector screenEntityDataInjector : screenEntityDataInjectors) {
			screenEntityDataInjector.inject(fieldAccessor, screenEntityClass, terminalScreen);
		}

		return screenEntity;

	}

	private Object createEntity(Class<? extends ScreenEntity> screenEntityClass) {
		ScreenEntity screenEntity = ReflectionUtil.newInstance(screenEntityClass);
		ScreenEntityMethodInterceptor screenEntityMethodInterceptor = applicationContext.getBean(ScreenEntityMethodInterceptor.class);
		ProxyFactory proxyFactory = new ProxyFactory(ScreenEntity.class, screenEntityMethodInterceptor);

		proxyFactory.setTarget(screenEntity);
		proxyFactory.setProxyTargetClass(true);
		Object personProxy = proxyFactory.getProxy();

		return personProxy;
	}

	public TerminalSendAction buildSendAction(TerminalScreen terminalScreen, HostAction hostAction, ScreenEntity screenEntity) {
		List<TerminalField> modifiedfields = new ArrayList<TerminalField>();

		SimpleTerminalSendAction sendAction = new SimpleTerminalSendAction(modifiedfields,
				hostActionMapper.getCommand(hostAction.getClass()), null);

		if (screenEntity == null) {
			return sendAction;
		}

		Collection<FieldMappingDefinition> fieldMappingsDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntity.getClass());

		if (fieldMappingsDefinitions == null) {
			return sendAction;
		}

		String focusField = screenEntity.getFocusField();

		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(screenEntity);

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingsDefinitions) {

			ScreenPosition fieldPosition = fieldMappingDefinition.getScreenPosition();
			String fieldName = fieldMappingDefinition.getName();
			Object value = fieldAccessor.getFieldValue(fieldName);

			TerminalField terminalField = terminalScreen.getField(fieldPosition);
			if (value != null) {
				boolean fieldModified = fieldComparator.isFieldModified(screenEntity, fieldName, terminalField.getValue(), value);
				if (fieldModified) {
					if (fieldMappingDefinition.isEditable()) {
						terminalField.setValue(value.toString());
						modifiedfields.add(terminalField);
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format(
									"Field {0} was set with value \"{1}\" to send fields for screen entity {2}", fieldName,
									value, screenEntity));
						}
					} else {
						throw (new SendActionException(MessageFormat.format(
								"Field {0} in screen {1} was modified with value {2}, but is not defined as editable", fieldName,
								screenEntity, value)));

					}
				}
			}
			if (fieldName.equals(focusField)) {
				sendAction.setCursorPosition(fieldPosition);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Cursor was set at position {0} from field {1}", fieldPosition, focusField));
				}
			}

		}

		if (!StringUtils.isEmpty(focusField) && sendAction.getCursorPosition() == null) {
			throw (new SendActionException(MessageFormat.format("Cursor field {0} was not found in screen {1}", focusField,
					ProxyUtil.getOriginalClass(screenEntity.getClass()))));
		}
		return sendAction;
	}
}
