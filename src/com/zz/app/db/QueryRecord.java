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
		Map<String, Object> successMap = new LinkedHashMap<String, Object>();
		int minLatitude_new = (int) (minLatitude * 3600000);
		int maxLatitude_new = (int) (maxLatitude * 3600000);
		int minLongitude_new = (int) (minLongitude * 3600000);
		int maxLongitude_new = (int) (maxLongitude * 3600000);
		String sql = "SELECT * from report_record_orig WHERE longitude>" + minLongitude_new + " AND longitude<"
				+ maxLongitude_new + " and latitude>" + minLatitude_new + " and latitude<" + maxLatitude_new;
		String prefix = "D:";
		// String prefix = "/opt";
		System.out.println("queryRecordByMBR: " + sql);
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("record_id") + ", " + rs.getString("task_id") + ", "
						+ rs.getString("longitude") + "," + rs.getString("latitude") + "," + rs.getString("location"));
				successMap.put("record_category", rs.getString("record_category"));
				successMap.put("longitude", rs.getString("longitude"));
				successMap.put("latitude", rs.getString("latitude"));
				successMap.put("location", rs.getString("location"));
				System.out.println("compress image begin£º" + CommonFun.getCurrentTime());
				System.out.println("only compress one image£º" + rs.getString("record_path").split("#")[0]);
				// only send one if more
				System.out.println(rs.getString("record_path").split("#")[0]);
				String tmpPath = prefix + rs.getString("record_path").split("#")[0];
				System.out.println(tmpPath);
				ImgCompress imgCom = new ImgCompress(tmpPath);
				imgCom.resizeFix(100, 100); // compress to 100 x 100
				System.out.println("compress image end£º" + CommonFun.getCurrentTime());
				String image_small = tmpPath.substring(0, tmpPath.length() - 4) + "_s.jpg";
				String strBase64 = CommonFun.GenerateBase64FromImage(image_small);
				successMap.put("content", strBase64);
				resultMap.put(rs.getString("record_id"), successMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultMap;
	}

}
