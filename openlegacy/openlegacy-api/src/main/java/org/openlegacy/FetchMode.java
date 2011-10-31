package org.openlegacy;

/**
 * Determine host entities field populate strategy. When a host entity is defined as LAZY, the field will be populated when
 * calling the get method of a property
 * 
 * Relevant for @ChildScreenEntity
 * 
 */
public enum FetchMode {

	LAZY,
	EAGER
}
