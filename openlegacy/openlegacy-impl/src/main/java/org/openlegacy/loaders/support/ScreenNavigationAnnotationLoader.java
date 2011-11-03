package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.terminal.definitions.SimpleFieldAssignDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenNavigationDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;

import java.lang.annotation.Annotation;

public class ScreenNavigationAnnotationLoader implements ClassAnnotationsLoader {

	private final static Log logger = LogFactory.getLog(ScreenNavigationAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenNavigation.class;
	}

	public void load(HostEntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenNavigation screenNavigation = (ScreenNavigation)annotation;
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		SimpleScreenNavigationDefinition navigationDefinition = new SimpleScreenNavigationDefinition();
		navigationDefinition.setAccessedFrom(screenNavigation.accessedFrom());
		try {
			navigationDefinition.setHostAction(screenNavigation.hostAction().newInstance());
			navigationDefinition.setExitAction(screenNavigation.exitAction().newInstance());
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

		AssignedField[] assignedFields = screenNavigation.assignedFields();
		for (AssignedField assignedField : assignedFields) {
			navigationDefinition.getAssignedFields().add(
					new SimpleFieldAssignDefinition(assignedField.field(), assignedField.value()));
		}

		SimpleScreenEntityDefinition screenEntityDefinition = (SimpleScreenEntityDefinition)screenEntitiesRegistry.get(containingClass);
		screenEntityDefinition.setNavigationDefinition(navigationDefinition);
		logger.info("Added screen navigation information to the registry for:" + containingClass);
	}
}
