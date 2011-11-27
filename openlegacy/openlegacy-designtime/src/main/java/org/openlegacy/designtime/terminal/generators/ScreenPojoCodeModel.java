package org.openlegacy.designtime.terminal.generators;

import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Field;

import java.util.Collection;

/**
 * An interface which model the code model of screen classes annotation with @ScreenEntity, @ScreenPart, @ScreenTable
 * 
 * 
 */
public interface ScreenPojoCodeModel {

	boolean isRelevant();

	boolean isSupportTerminalData();

	String getClassName();

	String getFormattedClassName();

	Collection<Field> getFields();

}