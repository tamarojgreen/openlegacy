package org.openlegacy;

/**
 * An interface for customized logic of host actions
 * 
 */
public interface CustomHostAction extends HostAction {

	void perform(HostSession hostSession);
}
