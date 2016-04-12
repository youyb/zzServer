package com.zz.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class CommonFun {
	private static String imgDir = "D:/zzfile";
	// private static String imgDir = "/opt/zzfile";

	public static String getCurrentTime() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dt);
	}

	public static String getRecordPath(int count) {
		System.out.println("build path image count: " + count);
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		File file = new File(imgDir + File.separator + date.get(Calendar.YEAR) + File.separator
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

	public static String GenerateBase64FromImage(String imgFilePath) {
		byte[] data = null;

		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

	public static boolean GenerateImageFromBase64(String imgStr, String imgFilePath) {
		if (imgStr == null) {
			return false;
		}

		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
			// create image file
			File file = new File(imgFilePath);
			if (!file.exists()) {
				file = new File(imgFilePath);
			}
			// write image file
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) {
		// String tmp =
		// GenerateBase64FromImage("D:/zzfile/2016/4/12/15/1460445670748-1.jpg");
		// System.out.println("--------------");
		// System.out.println(tmp);
		// GenerateImageFromBase64(tmp, "D:/zzfile/tmp.jpg");
	}
}
