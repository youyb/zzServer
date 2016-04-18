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
import com.zz.app.util.MinBoundaryRect;

public class SaveRecord {
	private Connection conn;
	private PreparedStatement ps;

	public SaveRecord() {
		conn = DbConUtil.getConn();
		if (conn != null) {
			System.out.println("mysql connected successfully.");
		}
	}

	public void insertNewRecordAndAssignTask(String phoneNum, String type, String category, String report_time,
			String desc, String longitude, String latitude, String location, JSONArray jsonArray) {

		int longitude_new = (int) (Float.parseFloat(longitude) * 3600000);
		int latitude_new = (int) (Float.parseFloat(latitude) * 3600000);
		String record_path = "";
		String record_path_db = "";
		String task_id_tmp = "";

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
						// beginIdx 2 identifies delete D:
						// beginIdx 4 identifies delete /opt
						record_path_db += (record_path.split("#")[i]).substring(2, len);
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

		// get nearby 3km MBR
		boolean flag_task = true; // default create task for new record
		MinBoundaryRect mbr = new MinBoundaryRect(Double.parseDouble(latitude), Double.parseDouble(longitude), 3.0);
		System.out.println("nearby 3km Latitude: " + mbr.minLatitude + ", " + mbr.maxLatitude);
		System.out.println("nearby 3km Longitude: " + mbr.minLongitude + ", " + mbr.maxLongitude);
		int iRatio = 3600000;
		int minLatitude_new = (int) (mbr.minLatitude * iRatio);
		int maxLatitude_new = (int) (mbr.maxLatitude * iRatio);
		int minLongitude_new = (int) (mbr.minLongitude * iRatio);
		int maxLongitude_new = (int) (mbr.maxLongitude * iRatio);
		System.out.println("nearby 3km Latitude: " + minLatitude_new + ", " + maxLatitude_new);
		System.out.println("nearby 3km Longitude: " + minLongitude_new + ", " + maxLongitude_new);
		String sql_if_create_task = "SELECT task_id from report_record_orig WHERE ( (record_category = " + category
				+ ") " + "AND (longitude>" + minLongitude_new + " AND longitude<" + maxLongitude_new + " AND latitude>"
				+ minLatitude_new + " AND latitude<" + maxLatitude_new + ") "
				+ "AND (UNIX_TIMESTAMP(record_time) > UNIX_TIMESTAMP('" + report_time + "')-3600) );";
		System.out.println(sql_if_create_task);
		try {
			ps = conn.prepareStatement(sql_if_create_task);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println(
						"The task for this record has been created, so just save this record, not asssign new task.");
				flag_task = false;
				// should get task_id from exist record
				task_id_tmp = rs.getString("task_id");
				System.out.println("task_id_tmp:" + task_id_tmp);
			} else {
				System.out.println("Asssign a new task for this record.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("flag_task:" + flag_task);

		// insert one record into report_record_orig table
		String record_id = "r_" + System.currentTimeMillis();
		String task_id;
		if (flag_task) {
			task_id = "t_" + System.currentTimeMillis();
		} else {
			task_id = task_id_tmp; // use exist task_id
		}
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
			ps.setString(6, report_time);
			ps.setString(7, desc);
			ps.setString(8, record_path_db);
			ps.setInt(9, longitude_new);
			ps.setInt(10, latitude_new);
			ps.setString(11, location);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (flag_task) {
			System.out.println("create a new task for report record...");
			// assign a task for this new record
			String sql_task = "insert into record_task_tb VALUES (?, ?, null, ?, null, null, ?, ?, ?)";
			String create_time = CommonFun.getCurrentTime();
			System.out.println("task create time: " + create_time);
			// String dispatch_time = "";
			// String end_time = "";
			String comments = "comments here";
			int status = 0; // 0: 待处理, 1: 处理中, 2: 已完成
			try {
				ps = conn.prepareStatement(sql_task);
				ps.setString(1, task_id);
				ps.setInt(2, Integer.parseInt(category));
				ps.setString(3, create_time);
				// ps.setString(4, dispatch_time);
				// ps.setString(5, end_time);
				ps.setInt(4, status);
				ps.setInt(5, 1); // initial record count for each task
				ps.setString(6, comments);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("use exist task_id for report record and update record count.");
			String sql_update_count = "update record_task_tb set record_count = record_count + 1 WHERE task_id = '"
					+ task_id + "'";
			System.out.println(sql_update_count);
			// int status = 0; // 0: 待处理, 1: 处理中, 2: 已完成
			try {
				ps = conn.prepareStatement(sql_update_count);
				ps.executeUpdate();
				System.out.println("update record_count++ ok...");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// auto update task status according each task's max record count,
			int iThreshold = 5;
			System.out.println("update task status from 0 to 1 if record count up to threshold.");
			String sql_update_status = "update record_task_tb set status = 1 WHERE task_id = '" + task_id
					+ "' and record_count > " + iThreshold;
			System.out.println(sql_update_status);
			try {
				ps = conn.prepareStatement(sql_update_status);
				ps.executeUpdate();
				System.out.println("update status from 0 to 1 ok...");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// send task status push notification to client if status == 1
			System.out.println("send task status push notification to client.");
			String sql_query_status = "select status from record_task_tb WHERE task_id = '" + task_id + "'";
			System.out.println(sql_query_status);
			try {
				ps = conn.prepareStatement(sql_query_status);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					int task_status_tmp = Integer.parseInt(rs.getString("status"));
					System.out.println("current task status: " + task_status_tmp);
					if (task_status_tmp == 1) {
						System.out.println("call script to send notification......");
						// 1. send push notification

						// 2. update dispatch_time in table

					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
				System.out.println(rs.getString("task_id") + ", " + rs.getString("task_type") + ", "
						+ rs.getString("company_id") + ", " + rs.getString("create_time") + ","
						+ rs.getString("dispatch_time") + "," + rs.getString("end_time") + "," + rs.getString("status")
						+ rs.getString("record_count") + "," + rs.getString("comment"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
