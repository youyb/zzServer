package com.zz.app.util;

import java.net.*;

public class UDPServer {
	private static final int PORT = 4561;
	private static DatagramSocket dataSocket;
	private static DatagramPacket dataPacket;

	public static void receiveTaskInfo() {
		try {
			dataSocket = new DatagramSocket(PORT);
			byte receiveByte[] = new byte[1024];
			dataPacket = new DatagramPacket(receiveByte, receiveByte.length);
			String receiveStr = "";
			dataSocket.close();
			int i = 0;
			System.out.println("UDP server listening...");
			while (i == 0) {
				dataSocket.receive(dataPacket);
				i = dataPacket.getLength();
				System.out.println("UDPServer: receiveTaskInfos packet len: " + i);
				if (i > 0) {
					receiveStr = new String(receiveByte, 0, dataPacket.getLength());
					System.out.println(receiveStr);
					i = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// UDPServer.receiveTaskInfo();
	}

}
