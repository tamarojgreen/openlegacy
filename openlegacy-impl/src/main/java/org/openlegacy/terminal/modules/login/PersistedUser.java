package org.openlegacy.terminal.modules.login;

import org.openlegacy.modules.login.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class PersistedUser implements User {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private String userName;

	@XmlJavaTypeAdapter(MapAdapter.class)
	@XmlElement
	private Map<String, Object> properties;

	public PersistedUser() {}

	public PersistedUser(String userName) {
		this.userName = userName;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public Map<String, Object> getProperties() {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"userName\":\"").append(userName).append("\"");
		if (properties != null && !properties.isEmpty()) {
			sb.append(", \"properties\":[");
			Iterator<String> iterator = properties.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				sb.append("{\"").append(key).append("\":\"").append(properties.get(key)).append("\"}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
			sb.append("]");
		}
		return sb.append("}").toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PersistedUser)) {
			return false;
		}
		return ((PersistedUser)other).getUserName().equalsIgnoreCase(getUserName());
	}

	@Override
	public int hashCode() {
		return getUserName().toLowerCase().hashCode();
	}
}
