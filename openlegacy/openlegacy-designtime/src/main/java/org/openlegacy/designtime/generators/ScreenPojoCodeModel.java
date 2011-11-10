package org.openlegacy.designtime.generators;

import org.openlegacy.designtime.generators.ScreenPojoCodeModelImpl.Field;

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

	Collection<Field> getFields();

}