package org.openlegacy.terminal;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.definitions.ActionDefinition;

import java.util.List;

/**
 * A container class for screen entity which includes additional elements
 * 
 * @author Roi Mor
 * 
 */
public interface ScreenEntityWrapper {

	ScreenEntity getScreenEntity();

	List<EntityDescriptor> getPaths();

	List<ActionDefinition> getActions();
}
