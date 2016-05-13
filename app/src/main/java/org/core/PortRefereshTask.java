package org.core;

import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import org.lib.xml.XmlComposer;
import org.net.udp.UDPEngine;

import android.util.Log;

import com.cg.hostedconf.AppReference;

public class PortRefereshTask extends TimerTask {
	private XmlComposer mXmlComposer = null;
	private String mMessageKeyZero = null;
	private String mMessageKeyOne = null;
	private UDPEngine mUdpEngine = null;
	private ProprietarySignalling mSignalling = null;
	private int mCount = 0;

	private Timer mTimer = null;

	public PortRefereshTask(String mMessageKeyZero, String mMessageKeyOne,
			UDPEngine udpEngine, ProprietarySignalling proprietarySignalling) {
		this.mMessageKeyZero = mMessageKeyZero;
		this.mMessageKeyOne = mMessageKeyOne;
		this.mUdpEngine = udpEngine;
		this.mSignalling = proprietarySignalling;
	}

	@Override
	public void run() {
		try {

			if (mCount == 3) {
				Log.d("PORTREFERESH", "mCount .." + mCount);
				mCount = 0;
				mSignalling.sendKeyOneMessage(mMessageKeyOne);
				Log.d("PORTREFERESH", "Type 20 key 1 xml" + mMessageKeyOne);
				Log.d("PORTREFERESH", "Type 20 key 1 sent");
				Log.i("KA123", "calling : Type 20 key 1 xml " + mMessageKeyOne);
				Log.i("KA123", "calling : Type 20 key 1 sent");
				/**
				 * For writing logs in log file
				 */
				if (AppReference.isWriteInFile) {
					try {
						AppReference.getLogger().debug(
								"KA123" + String.valueOf(mCount));
						AppReference.getLogger().debug(
								"KA123 Type 20 key 1 xml" + mMessageKeyOne);
						AppReference.getLogger().debug(
								"KA123 Type 20 key 1 sent");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				/**
				 * 
				 */
				// mCount=0;
			} else {
				byte[] data = AESCrypto.encrypt(
						ProprietarySignalling.seceretKey,
						mMessageKeyZero.getBytes());
				mCount++;
				mUdpEngine.sendUDP(data,
						InetAddress.getByName(mSignalling.getRouterip()),
						mSignalling.getRouteport());
				// mCount++;
				Log.d("PORTREFERESH", "Type 20 key 0 xml" + mMessageKeyZero);
				Log.d("PORTREFERESH", "Type 20 key 0 sent");
				Log.i("KA123", "calling : call count : " + mCount);
				Log.i("KA123", "calling : Type 20 key 0 xml " + mMessageKeyZero);
				Log.i("KA123", "calling : Type 20 key 0 sent");
				/**
				 * For writing logs in log file
				 */
				try {
					if (AppReference.isWriteInFile) {
						AppReference.getLogger().debug(
								"KA123" + String.valueOf(mCount));
						AppReference.getLogger().debug(
								"KA123 Type 20 key 0 xml" + mMessageKeyZero);
						AppReference.getLogger().debug(
								"KA123 Type 20 key 0 sent");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/**
				 * 
				 */
			}

		} catch (Exception e) {
			Log.d("KA123", "Type 20 on exception " + e);
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile) {
				try {
					AppReference.getLogger().error(e.getMessage(), e);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}

	// public void sendKeyOneMessage(){
	// mTimer=new Timer();
	// mTimer.schedule(new TimerTask() {
	//
	// @Override
	// public void run() {
	// try{
	// byte[]
	// data=AESCrypto.encrypt(ProprietarySignalling.seceretKey,mMessageKeyOne.getBytes());
	// mUdpEngine.sendUDP(data,
	// InetAddress.getByName(mSignalling.getRouterip()),mSignalling.getRouteport());
	//
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	//
	// }
	// },0,500);
	// }
	//
	// public void stopKeyOneTimer(){
	// if(mTimer!=null){
	// mTimer.cancel();
	// mTimer=null;
	// }
	// }

}
