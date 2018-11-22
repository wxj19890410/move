package com.move.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtil {
	/**
	 * 获取数据库连接
	 * 
	 * @return Connection 对象
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://127.0.0.1:1433; DatabaseName=WanHuaHuoliDb";
			String username = "sa";
			String password = "ServerSa";
			// String url = "jdbc:sqlserver://127.0.0.1:1433;
			// DatabaseName=huoli";
			// String username = "sa";
			// String password = "123456";
			conn = DriverManager.getConnection(url, username, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
