package org.openlegacy.providers.db_stored_proc.procs;

import org.openlegacy.rpc.RpcField;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public abstract class StoredProcEntity {

	public static class StoredProcResultSet {
	}

	protected String className;
	StoredProcResultSet results;

	public void fetchFields(List<RpcField> fields) {

	}

	public void updateFields(List<RpcField> fields) {

	}

	abstract public void invokeStoredProc();

	public StoredProcResultSet unrollResult() {
		return results;
	}

	public Connection getConnection() throws SQLException {
		Properties prop = new Properties();
		String propFileName = "database.properties";

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String url = prop.getProperty("url");
		String user = prop.getProperty("username");
		String password = prop.getProperty("password");

		try {
			// The newInstance() call is a work around for some
			// broken Java implementations

			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			// handle the error
		}

		return DriverManager.getConnection(url, user, password);
	}
}
