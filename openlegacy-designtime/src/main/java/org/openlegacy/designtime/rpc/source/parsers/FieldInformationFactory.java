package org.openlegacy.designtime.rpc.source.parsers;

/**
 * FieldInformationFactory is interface for factory object that generate FieldInformation, from string that holds on token of the
 * variable declaration. Each programming language should have different factory.
 * 
 */

public interface FieldInformationFactory {

	FieldInformation getObject(String flatPicture, int occurs);

}