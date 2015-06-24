package org.openlegacy.providers.wsrpc.example;

public class Structure {

	private String Name, LastName;

	public String getName() {
		return Name;
	}

	public Structure setName(String name) {
		Name = name;
		return this;
	}

	public String getLastName() {
		return LastName;
	}

	public Structure setLastName(String lastName) {
		LastName = lastName;
		return this;
	}
}
