package com.zz.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class CommonFun {
	public static String getCurrentTime() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dt);
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
}
