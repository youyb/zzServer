package com.zz.app.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.zz.app.util.CommonFun;
import com.zz.app.util.DbConUtil;
import com.zz.app.util.ImgCompress;

public class QueryRecord {
	private Connection conn;
	private PreparedStatement ps;

	public QueryRecord() {
		conn = DbConUtil.getConn();
		if (conn != null) {
			System.out.println("mysql connected successfully.");
		}
	}

	public Map<String, Object> queryRecordByMBR(double minLatitude, double maxLatitude, double minLongitude,
			double maxLongitude) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		int iRatio = 3600000;
		int minLatitude_new = (int) (minLatitude * iRatio);
		int maxLatitude_new = (int) (maxLatitude * iRatio);
		int minLongitude_new = (int) (minLongitude * iRatio);
		int maxLongitude_new = (int) (maxLongitude * iRatio);
		String sql = "SELECT * from report_record_orig WHERE longitude>" + minLongitude_new + " AND longitude<"
				+ maxLongitude_new + " and latitude>" + minLatitude_new + " and latitude<" + maxLatitude_new;
		String prefix = (CommonFun.flag_debug == true) ? "D:" : "/opt";
		System.out.println("prefix: " + prefix);
		System.out.println("queryRecordByMBR: " + sql);
		try {
			int flag = 1;
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (flag > 10) {
					break;
				}
				Map<String, Object> successMap = new LinkedHashMap<String, Object>();
				System.out.println(rs.getString("record_id") + ", " + rs.getString("task_id") + ", "
						+ rs.getString("record_desc") + ", " + rs.getString("longitude") + ","
						+ rs.getString("latitude") + "," + rs.getString("location"));
				successMap.put("record_category", rs.getString("record_category"));
				successMap.put("record_desc", rs.getString("record_desc"));
				successMap.put("longitude", rs.getString("longitude"));
				successMap.put("latitude", rs.getString("latitude"));
				successMap.put("location", rs.getString("location"));
				// get task create_time by task_id
				String task_create_time = queryCreateTimeById(rs.getString("task_id"));
				System.out.println("task_create_time: " + task_create_time);
				successMap.put("task_create_time", task_create_time);
				// only send one if more
				System.out.println(rs.getString("record_path").split("#")[0]);
				String tmpPath = prefix + rs.getString("record_path").split("#")[0];
				System.out.println(tmpPath);
				ImgCompress imgCom = new ImgCompress(tmpPath);
				imgCom.resizeFix(150, 150); // compress to 150 x 150
				String image_small = tmpPath.substring(0, tmpPath.length() - 4) + "_s.jpg";
				System.out.println(flag + "~~~~: " + image_small);
				String strBase64 = CommonFun.GenerateBase64FromImage(image_small);
				successMap.put("content", strBase64);
				resultMap.put(rs.getString("record_id"), successMap);
				flag++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultMap;
	}

	private String queryCreateTimeById(String id) {
		String retTime = "";
		String sql_create_time = "SELECT create_time from record_task_tb WHERE task_id = '" + id + "'";
		System.out.println("queryCreateTimeById: " + sql_create_time);
		try {
			ps = conn.prepareStatement(sql_create_time);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				retTime = rs.getString("create_time");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retTime;
	}

}
