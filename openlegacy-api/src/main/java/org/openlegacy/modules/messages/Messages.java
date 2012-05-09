package org.openlegacy.modules.messages;

import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.modules.SessionModule;

import java.util.List;

/**
 * A Messages module is handling messages screens and store the messages in the module for a later usage, and skip them
 * 
 */
public interface Messages extends SessionModule {

	List<String> getMessages();

	void resetMessages();

	public static class MessagesEntity implements EntityType {
	}

	public static class MessageField implements FieldType {
	}
}
