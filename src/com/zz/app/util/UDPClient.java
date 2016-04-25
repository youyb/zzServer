package com.zz.app.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {
	private static final int PORT = 4561;
	private static DatagramSocket socket;
	private static DatagramPacket packet;

	public static void sendTaskInfo(String taskStr) {
		try {
			socket = new DatagramSocket(PORT + 1);
			byte buf[] = new byte[256];
			buf = taskStr.getBytes();
			packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.0.207"), PORT);
			socket.send(packet);
			socket.close();
			System.out.println("UDPClient: send task info to WEB UI ok...");
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
}
