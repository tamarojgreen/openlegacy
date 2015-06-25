package com.example.mysql_stored_proc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	public static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/rpc_test";
		String user = "rpc_test";
		String password = "password";

		return DriverManager.getConnection(url, user, password);
	}
}
