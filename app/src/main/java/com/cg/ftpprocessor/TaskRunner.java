package com.cg.ftpprocessor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.io.Util;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.cg.snazmed.R;
import com.cg.commonclass.SipNotificationListener;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.BackgroundNotification;
import com.main.AppMainActivity;

public class TaskRunner implements Runnable {

	private FTPClient ftpclientObj;

	private int tmp = 0;

    private FTPBean ftpBean;
    private String path;

	private FtpListener callback;

	private BackgroundNotification notifier = null;

	@Override
	public void run() {
		try {
			ftpBean = (FTPBean) FTPNotifier.ftpQueue.getMsg();
			Log.i("onresult123", "Came to ftp queue");
			if (ftpBean != null) {
				switch (ftpBean.getOperation_type()) {
				case 1:
					upload(ftpBean);
					break;

                    case 2:
                        if(AppMainActivity.isLogin){
                            download(ftpBean);
                        }
                        else{
                            Log.d("ABCD","download failed "+ftpBean.getRequest_from());
                        }
                        break;

				case 3:
					deleteFile(ftpBean);
					break;

				default:
					Log.d("FTP", "Came to default case");
					break;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setCallback(FtpListener listener) {
		this.callback = listener;
	}

	private boolean initializeFTP(String serverip, String username,
			String password, int connect_port) {
		try {
			ftpclientObj = new FTPClient();
			ftpclientObj.connect(serverip, connect_port);

			if (FTPReply.isPositiveCompletion(ftpclientObj.getReplyCode())) {
				ftpclientObj.enterLocalPassiveMode();
				boolean status = ftpclientObj.login(username, password);
				ftpclientObj.setFileType(FTP.BINARY_FILE_TYPE);
				return status;

			} else
				return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void ftpDisconnect() {
		try {
			if (ftpclientObj != null) {
				if (ftpclientObj.isConnected()) {
					ftpclientObj.logout();
					ftpclientObj.disconnect();
					ftpclientObj = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public FTPBean getBean() {
		return this.ftpBean;
	}

	Builder mBuilder = null;

	public void upload(final FTPBean ftpBean) {

		final int notificationID = getNotificationID();

		mBuilder = showNotification("File Uploading", "", notificationID);

		try {
			FTPClient ftpClient = new FTPClient();
//			ftpClient.setDataTimeout(30000);
//			ftpClient.setConnectTimeout(30000);
//			// ftpClient.setSoTimeout(30000);
//			// ftpClient.setDefaultTimeout(30000);
//			ftpClient.connect(ftpBean.getServer_ip(), ftpBean.getServer_port());
//			ftpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
//			ftpClient.login(ftpBean.getFtp_username(),
//					ftpBean.getFtp_password());
//
//			ftpClient.enterLocalPassiveMode();
//			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//			// ftpClient.setSendBufferSize(470386961);
//			ftpClient.setBufferSize(1024 * 1024);

            File srcFile = new File(ftpBean.getFile_path());
            Log.d("AES_Log", "1.Decrypt Files for uploading");
            String storeFileName = srcFile.getName();
            final long srcFileSize = srcFile.length();
            ftpBean.setFilename(storeFileName);

			FileInputStream inputStream = new FileInputStream(srcFile);
			Log.i("ftp", "store file name : " + storeFileName);
//			FTPFile[] files = ftpClient.listFiles(storeFileName);
//			for (FTPFile ftpFile : files) {
//				Log.d("FTP", "File : " + ftpFile.getName() + " size : "
//						+ ftpFile.getSize());
//
//				if (ftpFile.getName().equalsIgnoreCase(storeFileName)) {
//					if (ftpFile.getSize() != srcFileSize) {
//						inputStream.skip(ftpFile.getSize());
//						ftpClient.setRestartOffset(ftpFile.getSize());
//						Log.d("FTP", "File : " + ftpFile.getName() + " size : "
//								+ ftpFile.getSize());
//						break;
//					} else if (ftpFile.getSize() == srcFileSize) {
//						ftpBean.setFilename(ftpFile.getName());
//						callback.FTPUploaded(ftpBean);
//						if (notificationManager != null)
//							notificationManager.cancel(notificationID);
//						Log.d("FTP", "File : " + ftpFile.getName() + " size : "
//								+ ftpFile.getSize()
//								+ " Already Available in Server.");
//						return;
//					}
//				}
//
//			}

//			OutputStream ftpOut = ftpClient.appendFileStream(storeFileName);
//			OutputStream output = new BufferedOutputStream(ftpOut);

//			CopyStreamListener copylistener = new CopyStreamListener() {
//				public void bytesTransferred(final long totalBytesTransferred,
//						int bytesTransferred, final long streamSize) {
//					long bt = totalBytesTransferred * 100;
//					tmp = (int) (bt / srcFileSize);
//
//					Log.d("FTP", "Progress : " + "Total Size = " + srcFileSize
//							+ " Total Transferred = " + totalBytesTransferred
//							+ " CurrentTransfer = " + bytesTransferred
//							+ " Percentage = " + tmp + "%");
//					if (notificationManager != null && mBuilder != null) {
//						mBuilder.setContentText(ftpBean.getFilename() + " -"
//								+ tmp + "%");
//						mBuilder.setProgress(100, tmp, false);
//						notificationManager.notify(notificationID,
//								mBuilder.build());
//					}
//				}
//
//				@Override
//				public void bytesTransferred(CopyStreamEvent arg0) {
//					// TODO Auto-generated method stub
//				}
//			};

			// input.skip(207698944);

//			Log.d("FTP", storeFileName + " Before COPY Stream");
//
//			Util.copyStream(inputStream, output, ftpClient.getBufferSize(),
//					srcFileSize, copylistener);
//
//			Log.d("FTP", storeFileName + " After COPY Stream");
//			inputStream.close();
//			Log.d("FTP", storeFileName + " Input closed");
//			output.close();
//			Log.d("FTP", storeFileName + " Output closed");
//			// ftpClient.logout();
//			Log.d("FTP", storeFileName + " Logged Out");
//			ftpClient.disconnect();
//
//			Log.d("FTP", storeFileName + " FTP Disconnect");
//
//			inputStream = null;
//			output = null;
//
//			if (tmp == 100) {
//				Log.d("FTP", storeFileName + "FTP upload status : TRUE");
				callback.FTPUploaded(ftpBean);
//				if (notificationManager != null)
//					notificationManager.cancel(notificationID);
//                Log.d("AES_Log","2.Decrypted files are transferred");
//			} else {
//				Log.d("FTP", storeFileName + "FTP upload status : FALSE");
//				callback.FTPUploaingFailed(ftpBean,
//						"Upload failed due to some unknown reason");
//
//				if (notificationManager != null && mBuilder != null) {
//					mBuilder.setAutoCancel(true);
//					mBuilder.setContentTitle("File Upload failed");
//					notificationManager
//							.notify(notificationID, mBuilder.build());
//				}
//                Log.d("AES_Log","2.Decrypted files are transferred");
//			}

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();

			if (notificationManager != null && mBuilder != null) {
				mBuilder.setAutoCancel(true);
				mBuilder.setContentTitle("File upload failed");
				notificationManager.notify(notificationID, mBuilder.build());
			}
            Log.d("AES_Log","Error ==> "+e.toString());
			callback.FTPUploaingFailed(ftpBean,
					"Upload failed due to some unknown reason");
		}
	}

	public void download(final FTPBean bean) throws Exception {

        final int notificationID = getNotificationID();
        Log.d("ABCD","File DownLoading");
        mBuilder = showNotification("File Downloading", "", notificationID);
        try {

			Thread.sleep(5000);
			String dir = createFolderIfNotExist();

			final FTPClient ftpClient = new FTPClient();
//			ftpClient.setDataTimeout(30000);
//			ftpClient.setConnectTimeout(30000);
//			// ftpClient.setSoTimeout(30000);
//			ftpClient.setDefaultTimeout(30000);
//			ftpClient.connect(bean.getServer_ip(), bean.getServer_port());
//			ftpClient.login(bean.getFtp_username(), bean.getFtp_password());
//
//			ftpClient.enterLocalPassiveMode();
//			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//			// ftpClient.setSendBufferSize(470386961);
//			ftpClient.setBufferSize(2048);
//
			String dstPath = dir + "/" + bean.getFile_path();
			if (bean.getFile_path().endsWith(".txt")) {
				File file = new File(dstPath);
				if (file.exists()) {
					dstPath = dir + "/" + AppMainActivity.getFileName()
							+ ".txt";
				}
			}
			File dstFile = new File(dstPath);
			// try {
			// if (dstFile.exists());
			// // dstFile.delete();
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			// if (!dstFile.exists()) {
			// dstFile.createNewFile();
			// }

//			FTPFile[] files = ftpClient.listFiles(bean.getFile_path());

			// for (FTPFile ftpFile : files) {
			// System.out.println("File : " + ftpFile.getName()+ " size : " +
			// ftpFile.getSize());
			// }

//			OutputStream desFileStream = new FileOutputStream(dstFile, true);
//
//			OutputStream output = new BufferedOutputStream(desFileStream);
//
//			InputStream input = ftpClient.retrieveFileStream(bean
//					.getFile_path());
//
//			Log.d("FTP", "getFile_path : " + bean.getFile_path());
//
//			long size = 0;

//			for (FTPFile ftpFile : files) {
//
//				Log.d("FTP", "Ftp Name : " + ftpFile.getName()
//						+ ", Received FileName :" + bean.getFile_path());
//				if (ftpFile.getName().equalsIgnoreCase(bean.getFile_path())) {
//
//					size = ftpFile.getSize();
//					// Log.d("FTP",
//					// "Ftp Size : "+ftpFile.getSize()+", Received File Size :"+dstFile.length());
//					// if (ftpFile.getSize() != dstFile.length()) {
//					// size = ftpFile.getSize();
//					// input.skip(dstFile.length());
//					// ftpClient.setRestartOffset(dstFile.length());
//					// Log.d("FTP", "File : " + ftpFile.getName()
//					// + " size : " + ftpFile.getSize());
//					//
//					// }
//
//					break;
//				}
//			}
//
			bean.setFilename(dstFile.getName());

//			final long siz = size;
//			CopyStreamListener copylistener = new CopyStreamListener() {
//				@Override
//				public void bytesTransferred(final long totalBytesTransferred,
//						int bytesTransferred, final long streamSize) {
//
//					long bt = totalBytesTransferred * 100;
//
//					// long siz = getFTPFileSize();
//					tmp = (int) (bt / siz);
//					Log.d("FTP", "Download :" + "Total file length--->" + siz
//							+ "  tot tranfer-->" + totalBytesTransferred
//							+ "  per-->" + tmp);
//					if (notificationManager != null && mBuilder != null) {
//						mBuilder.setContentText(bean.getFilename() + " -" + tmp
//								+ "%");
//						mBuilder.setProgress(100, tmp, false);
//						notificationManager.notify(notificationID,
//								mBuilder.build());
//					}
//				}
//
//				private long getFTPFileSize() {
//					try {
//						FTPFile[] files = ftpClient.listFiles(bean
//								.getFile_path());
//						for (FTPFile ftpFile : files) {
//							Log.d("FTP", "Size is : " + ftpFile.getSize());
//							return ftpFile.getSize();
//						}
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//					return 0;
//				}
//
//				@Override
//				public void bytesTransferred(CopyStreamEvent arg0) {
//					// TODO Auto-generated method stub
//				}
//			};

//			Log.d("FTP", dstFile + " before Copy Stream");
//
//			Util.copyStream(input, output, ftpClient.getBufferSize(), 0,
//					copylistener);
//
//			Log.d("FTP", dstFile.getName() + " after Copy Stream");
//			input.close();
//			Log.d("FTP", dstFile.getName() + " input closed");
//			desFileStream.close();
//			Log.d("FTP", dstFile.getName() + " output closed");
//			// ftpClient.logout();
//			ftpClient.disconnect();
//			Log.d("FTP", dstFile.getName() + " Disconnected.");
//
//			bean.setProgress(tmp);
//            String Temp = dstFile.getName();
//			if (tmp == 100) {
				callback.Downloaded(bean);
//				Log.d("FTP", "FTP Download status100 TRUE ");
//                Log.d("AES_Log", "4.Encrypt the downloaded files");
//                Log.d("AES_Log", "File recieved with extension".concat(Temp.substring(Temp.lastIndexOf(".")+1,Temp.length())));
//                if (notificationManager != null)
//                    notificationManager.cancel(notificationID);
//
//			} else {
//
//				if (notificationManager != null)
//					notificationManager.cancel(notificationID);
//				callback.Downloaded(bean);
//
//				// callback.DownloadFailure(bean,
//				// "Downloading failed due to some unknown reason");
//				Log.d("FTP", "FTP Download status False ");
//				// if (notificationManager != null && mBuilder != null) {
//				// mBuilder.setAutoCancel(true);
//				// mBuilder.setContentTitle("File Download failed");
//				// notificationManager
//				// .notify(notificationID, mBuilder.build());
//				// }
//			}
		} catch (Exception e) {
			e.printStackTrace();
			if (notificationManager != null && mBuilder != null) {
				mBuilder.setAutoCancel(true);
				mBuilder.setContentTitle("File Download failed");
				notificationManager.notify(notificationID, mBuilder.build());
				notificationManager.cancel(notificationID);
			}
            Log.d("ABCD","Produced Error => "+e.toString());
            Log.d("ABCD","Failed to download file");
            Log.d("ABCD","File Name => " + bean.getFilename());
            Log.d("ABCD","FTP User Name => " + bean.getFtp_username());

			callback.Downloaded(bean);
		}

	}

	private String createFolderIfNotExist() {
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/COMMedia/");
		if (!folder.exists()) {
			folder.mkdir();
		}
		return folder.getAbsolutePath();
	}

	public int getNotificationID() {
		try {

			long time = new Date().getTime();
			return (int) time;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 100;
	}

	public void deleteFile(FTPBean bean) {

		boolean result = false;
		try {
			boolean isconnected = initializeFTP(bean.getServer_ip(),
					bean.getFtp_username(), bean.getFtp_password(),
					bean.getServer_port());
			if (isconnected) {
				if (ftpclientObj.listFiles(bean.getFilename()).length > 0) {
					result = ftpclientObj.deleteFile(bean.getFilename());
					Log.d("FTP",
							"File deleted from ftp server :"
									+ bean.getFilename());

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		} finally {
			try {
				if (ftpclientObj != null) {
					if (ftpclientObj.isConnected()) {
						ftpclientObj.logout();
						ftpclientObj.disconnect();
						ftpclientObj = null;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	NotificationManager notificationManager;

	private Builder showNotification(String title, String contentText,
			int notificationID) {
		try {

			int icon;

			if (title.equalsIgnoreCase("File Uploading"))

				icon = R.drawable.upload_animate;
			else
				icon = R.drawable.download_animate;

			Context context = SipNotificationListener.getCurrentContext();

			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Builder mBuilder = new NotificationCompat.Builder(
					context.getApplicationContext());
			mBuilder.setContentTitle(title);
			mBuilder.setContentText(contentText);
			mBuilder.setProgress(0, 0, true);
			mBuilder.setSmallIcon(icon);
			mBuilder.setAutoCancel(false);

			notificationManager.notify(notificationID, mBuilder.build());
			return mBuilder;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

}
