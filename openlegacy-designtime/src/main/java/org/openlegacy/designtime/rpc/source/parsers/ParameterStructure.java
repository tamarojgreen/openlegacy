package org.openlegacy.designtime.rpc.source.parsers;

import java.util.List;

/**
 * ParameterStructure interface is a uniform objects that hold information about the data structure of parameters
 * 
 * VariableDeclaration - hold the original deceleration of this field Level 01 for simple objects, for structures it holds the
 * level of this field from the root SubFields - list of the parameters list
 */

public interface ParameterStructure {

	String getVariableDeclaration();

	int getLevel();

	String getFieldName();

	int getCount();

	List<ParameterStructure> getSubFields();

	boolean isSimple();

}