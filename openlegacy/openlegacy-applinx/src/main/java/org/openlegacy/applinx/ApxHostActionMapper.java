package org.openlegacy.applinx;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.HostActionMapper;

import java.text.MessageFormat;

public class ApxHostActionMapper implements HostActionMapper {

	public String map(HostAction hostAction) {
		return MessageFormat.format("[{0}]", hostAction.getCommand().toString().toLowerCase());
	}

}
