package com.zz.app.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONArray;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.app.util.CommonFun;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class SaveRecord {
	private String dbDriver;
	private String dbURL;
	private String dbUser;
	private String dbPassword;
	private Connection con;
	private PreparedStatement ps;

	public SaveRecord() {
		dbDriver = "com.mysql.jdbc.Driver";
		dbURL = "jdbc:mysql://localhost:3306/zzdb";
		dbUser = "root";
		dbPassword = "root";
		initDB();
	}

	public SaveRecord(String strDriver, String strURL, String strUser, String strPwd) {
		dbDriver = strDriver;
		dbURL = strURL;
		dbUser = strUser;
		dbPassword = strPwd;
		initDB();
	}

	public void initDB() {
		try {
			// Load Driver
			Class.forName(dbDriver).newInstance();
			// Get connection
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertNewRecordAndAssignTask(String phoneNum, String type, String category, String record_time,
			String desc, String longitude, String latitude, String location, JSONArray jsonArray) {

		int longitude_new = (int) (Float.parseFloat(longitude) * 3600000);
		int latitude_new = (int) (Float.parseFloat(latitude) * 3600000);
		String record_path = "";

		// save files on disk
		if (jsonArray != null && jsonArray.length() > 0) {
			record_path = getRecordPath(jsonArray.length());
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
						CommonFun.GenerateImageFromBase64(content, record_path.split("#")[i]);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		// insert one record into report_record_orig table
		String record_id = "r_" + System.currentTimeMillis();
		String task_id = "t_" + System.currentTimeMillis();
		// String record_id = "r_" + UUID.randomUUID().toString();
		// String task_id = "t_" + UUID.randomUUID().toString();
		String sql_record = "insert into report_record_orig VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		System.out.println(record_id + ",   " + task_id);
		try {
			ps = con.prepareStatement(sql_record);
			ps.setString(1, record_id);
			ps.setString(2, task_id);
			ps.setString(3, phoneNum);
			ps.setInt(4, Integer.parseInt(type));
			ps.setInt(5, Integer.parseInt(category));
			ps.setString(6, record_time);
			ps.setString(7, desc);
			ps.setString(8, record_path);
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
			ps = con.prepareStatement(sql_task);
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

	private String getRecordPath(int count) {
		System.out.println("build path image count: " + count);
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		String rootDir = "D:/upload";
		// String rootDir = "/home/zzfile";
		File file = new File(rootDir + File.separator + date.get(Calendar.YEAR) + File.separator
				+ (date.get(Calendar.MONTH) + 1) + File.separator + date.get(Calendar.DAY_OF_MONTH) + File.separator
				+ date.get(Calendar.HOUR_OF_DAY));
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = "";
		if (count > 1) {
			// more than one image
			for (int i = 1; i <= count; i++) {
				path += file.getPath() + File.separator + System.currentTimeMillis() + "-" + i + ".jpg";
				if (i != count) {
					path += "#";
				}
			}
		} else {
			// each image
			path = file.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
		}
		return path;
	}

	public void queryRecordType() {
		try {
			ps = con.prepareStatement("select * from record_type_tb");
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
			ps = con.prepareStatement("select * from record_category_tb");
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
			ps = con.prepareStatement("select * from report_record_orig");
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
			ps = con.prepareStatement("select * from record_task_tb");
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
