/**
 * 
 */
package com.cg.timer;

import java.net.InetAddress;

import org.core.AESCrypto;
import org.core.ProprietarySignalling;
import org.lib.model.SignalingBean;
import org.net.udp.UDPEngine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cg.commonclass.CallDispatcher;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;


public class KeepAliveReceiver extends BroadcastReceiver {

	public static int count;
	public static boolean isGotKeyOneResponse = true;

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			Log.i("alarm123","Comes into KeepAliveReceiver ");
			SingleInstance.printLog(null, "Comes into KeepAliveReceiver ", "INFO", null);

			SingleInstance.mainContext.setLogWriterForCall("Timer Fired User=" + CallDispatcher.LoginUser);

			if (CallDispatcher.LoginUser != null) {
				ProprietarySignalling pSignalling = AppMainActivity.commEngine
						.getProprietarySignalling();
				UDPEngine udpEngine = pSignalling.getUDPEngine();

				if (udpEngine != null) {
					if (count == 3) {
						count = 0;

						byte[] data = AESCrypto.encrypt(
								ProprietarySignalling.seceretKey,
								getKeyOneXML().getBytes());
						isGotKeyOneResponse = false;
                        ContactsFragment.isKeyOneNotReceived=false;
                        SingleInstance.mainContext.setLogWriterForCall("SEND Keepalive : User="+CallDispatcher.LoginUser+",Key=1");
						SingleInstance.printLog(null, "SEND Keepalive : User=" + CallDispatcher.LoginUser + ",Key=1", "INFO", null);
						udpEngine.sendUDP(data, InetAddress
										.getByName(pSignalling.getRouterip()),
								pSignalling.getRouteport());

					} else {
						if (!isGotKeyOneResponse) {
							ContactsFragment.isKeyOneNotReceived=true;

							isGotKeyOneResponse = true;
							SignalingBean sb = new SignalingBean();
							sb.setType("heartbeat");
							sb.setCallType("");

							SingleInstance.mainContext.setLogWriterForCall("Request SEND HeartBeat : User=" + CallDispatcher.LoginUser);
							SingleInstance.printLog(null, "SEND HeartBeat : User=" + CallDispatcher.LoginUser, "INFO", null);
							pSignalling.getCallSessionListener()
									.notifyProprietyResponse(sb);

						}

						count++;
						byte[] data = AESCrypto.encrypt(
								ProprietarySignalling.seceretKey,
								getKeyZeroXML().getBytes());

						SingleInstance.mainContext.setLogWriterForCall("SEND Keepalive : User="+CallDispatcher.LoginUser+",Key=0");
						SingleInstance.printLog(null, "SEND Keepalive : User="+CallDispatcher.LoginUser+",Key=0", "INFO", null);
						udpEngine.sendUDP(data, InetAddress
								.getByName(pSignalling.getRouterip()),
								pSignalling.getRouteport());
					}
				} else {
					Log.d("PORT_REFERESH", "UDP Engine is Null");
					SingleInstance.printLog(null, "PORT_REFERESH, UDP Engine is Null", "INFO", null);
					SingleInstance.mainContext.setLogWriterForCall("PORT_REFERESH, UDP Engine is Null");
				}
			} else {
				Log.d("PORT_REFERESH", "LoginUser is NULL");
				SingleInstance.printLog(null, "PORT_REFERESH,LoginUser is NULL", "INFO", null);


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getKeyOneXML() {

		return "<?xml version=\"1.0\"?><com type=\"20\" userid=\""
				+ CallDispatcher.LoginUser + "\" key=\"1\" macid=\""+CallDispatcher.getDeviceId()+"\"></com>";
	}

	private String getKeyZeroXML() {

		return "<?xml version=\"1.0\"?><com type=\"20\" userid=\""
				+ CallDispatcher.LoginUser + "\" key=\"0\" macid=\""+CallDispatcher.getDeviceId()+"\"></com>";
	}

}
