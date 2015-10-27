package org.openlegacy.modules.login;

import java.io.Serializable;
import java.util.Map;

public interface User extends Serializable {

	String getUserName();

	Map<String, Object> getProperties();
}
