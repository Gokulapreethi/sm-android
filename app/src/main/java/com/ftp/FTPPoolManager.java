/**
 * 
 */
package com.ftp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

import com.main.AppMainActivity;

/**
 * @author GopalaKrishnan D
 * @contact gopallcse@gmail.com
 * 
 */
public class FTPPoolManager {

	private static ExecutorService executor;

	private static ExecutorService getExecutor() {
		if (executor == null)
			executor = Executors.newFixedThreadPool(1);
		return executor;
	}

	public static void processRequest(ChatFTPBean ftpBean) {
		try {
            if(AppMainActivity.isLogin) {
                Log.d("FTP_STATUS", "Process Request :" + ftpBean.getInputFile());
                Runnable worker = new WorkerThread(ftpBean);
                getExecutor().execute(worker);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeChatFTP() {
		try {
			if (executor != null) {
				Log.i("ftp123", "closing chat ftp");
				executor.shutdown();
				Log.i("ftp123", "closed chat ftp");
				executor = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
