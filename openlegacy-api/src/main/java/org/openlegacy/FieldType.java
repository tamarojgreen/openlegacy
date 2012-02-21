package org.openlegacy;

/**
 * Field type define the business purpose of the field. Example usage: User field, password field, menu field, etc
 * 
 * FieldType may be used by session modules which are interested of understand the legacy application fields, by querying the
 * registry
 * 
 * It is possible to define more field types by implementing this interface
 */
public interface FieldType {

	/**
	 * The default field type is general
	 */
	public static class General implements FieldType {
	}
}
