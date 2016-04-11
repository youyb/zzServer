package com.zz.app.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImgCompress {
	private Image img;
	private int width;
	private int height;
	private String path;

	public ImgCompress(String fileName) throws IOException {
		File file = new File(fileName);
		img = ImageIO.read(file);
		width = img.getWidth(null);
		height = img.getHeight(null);
		path = file.getAbsolutePath();
	}

	// public static void main(String[] args) throws IOException {
	// System.out.println("begin£º" + new CommonFun().getCurrentTime());
	// ImgCompress imgCom = new ImgCompress("D:/upload/1.jpg");
	// imgCom.resizeFix(100, 100);
	// System.out.println("end£º" + new CommonFun().getCurrentTime());
	// }

	public void resizeFix(int w, int h) throws IOException {
		if (width / height > w / h) {
			resizeByWidth(w);
		} else {
			resizeByHeight(h);
		}
	}

	private void resizeByHeight(int h) throws IOException {
		int w = (int) (width * h / height);
		resize(w, h);
	}

	private void resizeByWidth(int w) throws IOException {
		int h = (int) (height * w / width);
		resize(w, h);
	}

	private void resize(int w, int h) throws IOException {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().drawImage(img, 0, 0, w, h, null);
		File destFile = new File(path.substring(0, path.length() - 4) + "_s.jpg");
		FileOutputStream out = new FileOutputStream(destFile);
		ImageIO.write(image, "jpg", out);
		out.close();
	}
}
