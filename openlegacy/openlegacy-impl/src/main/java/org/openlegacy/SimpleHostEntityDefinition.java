package org.openlegacy;

public class SimpleHostEntityDefinition implements HostEntityDefinition {

	private String hostEntityName;
	private Class<?> hostEntityClass;

	public SimpleHostEntityDefinition(String hostEntityName, Class<?> screenEntityClass) {
		this.hostEntityName = hostEntityName;
		this.hostEntityClass = screenEntityClass;
	}

	public String getHostEntityName() {
		return hostEntityName;
	}

	public void setName(String hostEntityName) {
		this.hostEntityName = hostEntityName;
	}

	public Class<?> getHostEntityClass() {
		return hostEntityClass;
	}

	public void setHostEntityClass(Class<?> hostEntityClass) {
		this.hostEntityClass = hostEntityClass;
	}

}
