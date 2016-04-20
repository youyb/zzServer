package com.zz.app.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zz.app.util.DbConUtil;

public class SaveUserInfo {
	private Connection conn;
	private PreparedStatement ps;

	public SaveUserInfo() {
		conn = DbConUtil.getConn();
		if (conn != null) {
			System.out.println("mysql connected successfully.");
		}
	}

	public void queryUserInfo() {
		try {
			ps = conn.prepareStatement("select * from user_info_tb");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("report_phone") + ", " + rs.getString("phone_type") + ", "
						+ rs.getString("token"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveAll(String num, String type, String token) {
		String sql_user = "insert into user_info_tb VALUES (?, ?, ?)";
		try {
			ps = conn.prepareStatement(sql_user);
			ps.setString(1, num);
			ps.setInt(2, Integer.parseInt(type));
			ps.setString(3, token);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
