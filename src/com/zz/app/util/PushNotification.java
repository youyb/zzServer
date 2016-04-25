package com.zz.app.util;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

public class PushNotification {

	public static void main(String[] args) {
		String deviceToken = "ce4a4852a81ff322e0eeb1f816e60fe49a41737e96609055384c6751a529791b";
		PushNotification.pushMsg(deviceToken);
	}

	private static void pushMsg(String token) {
		System.out.println("Push start deviceToken: " + token);
		try {
			// define msg format
			PayLoad pl = new PayLoad();
			pl.addAlert("This record is being processed.");
			pl.addBadge(1);
			pl.addSound("default");
			// reg token
			PushNotificationManager pushManager = PushNotificationManager.getInstance();

			pushManager.addDevice("iPhone", token);
			// connect APNS
			String host = "gateway.sandbox.push.apple.com";
			int port = 2195;
			String certificatePath = "D:/work/zz/push/aps_developer_identity.p12";
			String certificatePwd = "123456";
			pushManager.initializeConnection(host, port, certificatePath, certificatePwd,
					SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
			// send push
			Device client = pushManager.getDevice("iPhone");
			System.out.println("Push msg: " + client.getToken() + "\n" + pl.toString());
			pushManager.sendNotification(client, pl);
			// close connection with APNS
			pushManager.stopConnection();
			// delete token
			pushManager.removeDevice("iPhone");
			System.out.println("Push End...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
