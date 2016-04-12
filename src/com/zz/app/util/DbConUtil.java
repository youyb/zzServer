package com.zz.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConUtil {
	public static Connection getConn() {
		Connection conn = null;
		String propertyPath = "config/jdbc.properties";
		Thread t = Thread.currentThread();
		ClassLoader cld = t.getContextClassLoader();
		InputStream is = cld.getResourceAsStream(propertyPath);
		Properties pro = new Properties();
		try {
			pro.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String driverClassName = pro.getProperty("jdbc.driverClassName");
		String username = pro.getProperty("jdbc.username");
		String password = pro.getProperty("jdbc.password");
		String url = pro.getProperty("jdbc.url") + "?characterEncoding=utf-8";
		try {
			Class.forName(driverClassName);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Connection conn = getConn();
		if (conn != null) {
			System.out.println("mysql connected ok.");
		}
	}
}
