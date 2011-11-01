package org.openlegacy;

/**
 * Host entity type define the business purpose of the entity. Example usage: Login screen, menu screen, etc
 * 
 * EntityType may be used by session modules which are interested of understand the legacy application entities/screens, by
 * querying the registry
 * 
 * It is possible to define more host entity types by implementing this interface
 */
public interface EntityType {

	public static class General implements EntityType {
	}
}
