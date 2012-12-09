package org.openlegacy.support;

import org.openlegacy.SessionProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

public class SimpleSessionProperties implements SessionProperties, Serializable, Comparable<SessionProperties> {

	private static final long serialVersionUID = 1L;

	private Properties properties = new Properties();
	private String id;
	private Date startedOn;

	private Date lastActivity;

	public String getId() {
		return id;
	}

	public Object getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	public void setProperty(String propertyName, Object propertyValue) {
		properties.put(propertyName, propertyValue);

	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return id.equals(((SimpleSessionProperties)obj).id);
	}

	public Date getStartedOn() {
		return startedOn;
	}

	public void setStartedOn(Date startedOn) {
		this.startedOn = startedOn;
	}

	public int compareTo(SessionProperties other) {
		if (startedOn == null) {
			return -1;
		}
		return startedOn.compareTo(other.getStartedOn());
	}

	public Date getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}
}
