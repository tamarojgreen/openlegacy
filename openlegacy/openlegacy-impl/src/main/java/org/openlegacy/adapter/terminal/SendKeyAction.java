package org.openlegacy.adapter.terminal;

import org.openlegacy.HostAction;

/**
 * SendKey implementation for an HostAction  
 *
 */
public class SendKeyAction implements HostAction{

	private String hostKey;

	public SendKeyAction(String hostKey){
		this.hostKey = hostKey;
	}
	
	public String getCommand() {
		return hostKey;
	}

}
