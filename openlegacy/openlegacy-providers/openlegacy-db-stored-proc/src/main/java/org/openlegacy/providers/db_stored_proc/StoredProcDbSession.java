package org.openlegacy.providers.db_stored_proc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StoredProcDbSession {

	private String driverClassName;
	private String url;

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Connection getConnection(String user, String password) {
		try {
			Class.forName(driverClassName).newInstance();

			return DriverManager.getConnection(url, user, password);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
