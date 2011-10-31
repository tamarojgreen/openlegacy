package org.openlegacy.designtime.generators;

import org.openlegacy.designtime.generators.ScreenEntityCodeModelImpl.Field;

import java.util.Collection;

public interface ScreenEntityCodeModel {

	boolean isRelevant();

	boolean isSupportTerminalData();

	String getClassName();

	Collection<Field> getFields();

}