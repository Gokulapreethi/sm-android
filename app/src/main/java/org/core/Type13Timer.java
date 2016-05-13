package org.core;

import java.net.InetAddress;
import java.util.Date;
import java.util.TimerTask;

import org.lib.model.SignalingBean;

import android.util.Log;

public class Type13Timer extends TimerTask {

	ProprietarySignalling udpengine = null;
	byte[] dataToSend = null;
	int count = 0;
	SignalingBean sb = null;
	CallsOverInternet callsoverInternet = null;
	boolean isRelayConfirm = false;

	public Type13Timer(ProprietarySignalling udpEngine, byte[] data,
			SignalingBean sb, CallsOverInternet callsOverInternet) {
		this.udpengine = udpEngine;
		this.dataToSend = data;
		this.sb = sb;
		this.callsoverInternet = callsOverInternet;

	}

	public void setRelayConfirmation(boolean relayConfirm) {
		isRelayConfirm = relayConfirm;
	}

	public boolean getRelayConfirmation() {
		return isRelayConfirm;
	}

	@Override
	public void run() {
		Log.d("call", new Date() + " " + "address " + sb.getTolocalip() + " "
				+ sb.getToSignalPort());

		Log.d("call", new Date() + " " + "Executed " + count);

		try {

			if (count == 3) {
				Log.d("call", new Date() + "Type13 Timer Expireddddddddd");

				if (!isRelayConfirm) {
					callsoverInternet.type13Expiration();
				} else {
					callsoverInternet.type13ExpirationOnRelay();
				}
				this.cancel();

			} else {
				// byte[]
				// data=AESCrypto.encrypt(ProprietarySignalling.seceretKey,dataToSend);
				count++;

				udpengine.sendType13FromTimer(dataToSend,
						InetAddress.getByName(sb.getTolocalip()),
						Integer.parseInt(sb.getToSignalPort()));

			}

		} catch (Exception e) {
			// Log.d("PORTREFERESH","Type 20 on exception "+e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
