package com.zz.app.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.json.JSONArray;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.app.util.CommonFun;
import com.zz.app.util.DbConUtil;

public class SaveRecord {
	private Connection conn;
	private PreparedStatement ps;

	public SaveRecord() {
		conn = DbConUtil.getConn();
		if (conn != null) {
			System.out.println("mysql connected successfully.");
		}
	}

	public void insertNewRecordAndAssignTask(String phoneNum, String type, String category, String record_time,
			String desc, String longitude, String latitude, String location, JSONArray jsonArray) {

		int longitude_new = (int) (Float.parseFloat(longitude) * 3600000);
		int latitude_new = (int) (Float.parseFloat(latitude) * 3600000);
		String record_path = "";
		String record_path_db = "";

		// save files on disk
		if (jsonArray != null && jsonArray.length() > 0) {
			record_path = CommonFun.getRecordPath(jsonArray.length());
			System.out.println(record_path);
			ObjectMapper mapper = new ObjectMapper();
			for (int i = 0; i < jsonArray.length(); i++) {
				// save each file on disk
				String eachImage = jsonArray.get(i).toString();
				System.out.println("each: " + eachImage);
				try {
					Map<String, Object> fileMap = mapper.readValue(eachImage, Map.class);
					if (fileMap != null && fileMap.size() > 0) {
						String content = fileMap.get("content").toString();
						System.out.println(content);
						System.out.println(record_path.split("#")[i]);
						int len = record_path.split("#")[i].length();
						//beginIdx 2 identifies delete D:
						//beginIdx 4 identifies delete /opt
						record_path_db += (record_path.split("#")[i]).substring(4, len);
						if (i != (jsonArray.length() - 1)) {
							record_path_db += "#";
						}
						CommonFun.GenerateImageFromBase64(content, record_path.split("#")[i]);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		System.out.println(record_path_db);

		// insert one record into report_record_orig table
		String record_id = "r_" + System.currentTimeMillis();
		String task_id = "t_" + System.currentTimeMillis();
		// String record_id = "r_" + UUID.randomUUID().toString();
		// String task_id = "t_" + UUID.randomUUID().toString();
		String sql_record = "insert into report_record_orig VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		System.out.println(record_id + ",   " + task_id);
		try {
			ps = conn.prepareStatement(sql_record);
			ps.setString(1, record_id);
			ps.setString(2, task_id);
			ps.setString(3, phoneNum);
			ps.setInt(4, Integer.parseInt(type));
			ps.setInt(5, Integer.parseInt(category));
			ps.setString(6, record_time);
			ps.setString(7, desc);
			ps.setString(8, record_path_db);
			ps.setInt(9, longitude_new);
			ps.setInt(10, latitude_new);
			ps.setString(11, location);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// assign a task for this new record
		String sql_task = "insert into record_task_tb VALUES (?, ?, ?, null, null, ?, ?)";
		String create_time = CommonFun.getCurrentTime();
		System.out.println("task create time: " + create_time);
		// String dispatch_time = "";
		// String end_time = "";
		String comments = "comments here";
		int status = 0; // 0: 待处理, 1: 处理中, 2: 已完成
		try {
			ps = conn.prepareStatement(sql_task);
			ps.setString(1, task_id);
			ps.setString(2, phoneNum);
			ps.setString(3, create_time);
			// ps.setString(4, dispatch_time);
			// ps.setString(5, end_time);
			ps.setInt(4, status);
			ps.setString(5, comments);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void queryRecordType() {
		try {
			ps = conn.prepareStatement("select * from record_type_tb");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("record_type") + ", " + rs.getString("record_type_desc"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void queryRecordCategory() {
		try {
			ps = conn.prepareStatement("select * from record_category_tb");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("record_category") + ", " + rs.getString("record_category_desc"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void queryRecord() {
		try {
			ps = conn.prepareStatement("select * from report_record_orig");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("record_id") + ", " + rs.getString("task_id") + ", "
						+ rs.getString("report_phone") + "," + rs.getString("record_type") + ","
						+ rs.getString("record_category") + "," + rs.getString("record_time") + ","
						+ rs.getString("record_desc") + "," + rs.getString("record_path") + ","
						+ rs.getString("longitude") + "," + rs.getString("latitude") + "," + rs.getString("location"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void queryTask() {
		try {
			ps = conn.prepareStatement("select * from record_task_tb");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("task_id") + ", " + rs.getString("report_phone") + ", "
						+ rs.getString("create_time") + "," + rs.getString("dispatch_time") + ","
						+ rs.getString("end_time") + "," + rs.getString("status") + "," + rs.getString("comment"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
