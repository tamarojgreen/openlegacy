package org.openlegacy.terminal.definitions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.support.SimpleEntityDefinition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentification;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SimpleScreenEntityDefinition extends SimpleEntityDefinition<ScreenFieldDefinition> implements ScreenEntityDefinition {

	private ScreenIdentification screenIdentification = new SimpleScreenIdentification();
	private NavigationDefinition navigationDefinition;
	private Map<String, ScreenTableDefinition> tableDefinitions = new HashMap<String, ScreenTableDefinition>();
	private Map<String, ScreenPartEntityDefinition> partDefinitions = new HashMap<String, ScreenPartEntityDefinition>();
	private TerminalSnapshot snapshot;
	private List<ActionDefinition> actions = new ArrayList<ActionDefinition>();
	private boolean window;
	private boolean child;

	private TerminalSnapshot accessedFromSnapshot;
	private ScreenEntityDefinition accessedFromScreenDefinition;
	private ScreenSize screenSize;
	private List<ScreenEntityDefinition> childScreensDefinitions = new ArrayList<ScreenEntityDefinition>();

	private final static Log logger = LogFactory.getLog(SimpleScreenEntityDefinition.class);

	public SimpleScreenEntityDefinition(String entityName, Class<?> entityClass) {
		super(entityName, entityClass);
	}

	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	public void setScreenIdentification(ScreenIdentification screenIdentification) {
		this.screenIdentification = screenIdentification;
	}

	public NavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	public void setNavigationDefinition(NavigationDefinition navigationDefinition) {
		this.navigationDefinition = navigationDefinition;
	}

	public Map<String, ScreenTableDefinition> getTableDefinitions() {
		return tableDefinitions;
	}

	public Map<String, ScreenPartEntityDefinition> getPartsDefinitions() {
		return partDefinitions;
	}

	public TerminalSnapshot getSnapshot() {
		return this.snapshot;
	}

	public void setSnapshot(TerminalSnapshot snapshot) {
		this.snapshot = snapshot;
	}

	public String getPackageName() {
		return getEntityClass().getPackage().getName();
	}

	public List<ActionDefinition> getActions() {
		return actions;
	}

	public boolean isWindow() {
		return window;
	}

	public void setWindow(boolean window) {
		this.window = window;
	}

	public TerminalSnapshot getOriginalSnapshot() {
		return getSnapshot();
	}

	public TerminalSnapshot getAccessedFromSnapshot() {
		return accessedFromSnapshot;
	}

	public void setAccessedFromSnapshot(TerminalSnapshot accessedFromSnapshot) {
		this.accessedFromSnapshot = accessedFromSnapshot;
	}

	public ScreenEntityDefinition getAccessedFromScreenDefinition() {
		return this.accessedFromScreenDefinition;
	}

	public void setAccessedFromScreenDefinition(ScreenEntityDefinition accessedFromScreenDefinition) {
		this.accessedFromScreenDefinition = accessedFromScreenDefinition;
	}

	public ScreenSize getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(ScreenSize screenSize) {
		this.screenSize = screenSize;
	}

	public List<ScreenEntityDefinition> getChildScreensDefinitions() {
		return childScreensDefinitions;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public boolean isChild() {
		return child;
	}

	public Set<ScreenEntityDefinition> getAllChildScreensDefinitions() {
		Set<ScreenEntityDefinition> childs = new TreeSet<ScreenEntityDefinition>();
		childs.addAll(getChildScreensDefinitions());
		for (ScreenEntityDefinition childScreenDefinition : childs) {
			Set<ScreenEntityDefinition> childScreensDefinitions = childScreenDefinition.getAllChildScreensDefinitions();
			if (childScreensDefinitions.size() > 0) {
				logger.info(MessageFormat.format("Adding child screens to list all child screens. Adding: {0}",
						childScreensDefinitions));
				childs.addAll(childScreensDefinitions);
			}
		}
		return childs;
	}

	public List<ScreenFieldDefinition> getSortedFields() {
		Collection<ScreenFieldDefinition> fields = getFieldsDefinitions().values();

		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
		return sortedFields;
	}
}
