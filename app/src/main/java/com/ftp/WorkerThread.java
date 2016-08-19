/**
 * 
 */
package com.ftp;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Base64;
import android.util.Log;

import com.bean.GroupChatBean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.NotePickerScreen;
import com.crypto.AESFileCrypto;
import com.main.AppMainActivity;
import com.util.SingleInstance;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.lib.xml.XmlComposer;
import org.lib.xml.XmlParser;
import org.net.AndroidInsecureKeepAliveHttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author GopalaKrishnan D
 * @contact gopallcse@gmail.com
 * 
 */
public class WorkerThread implements Runnable {

	private ChatFTPBean ftpBean;
	private CallDispatcher callDisp = null;

	FTPClient ftpClient;

	Builder mBuilder = null;

	long actualSize;
	long totalReceived = 0;

	Object context;

	public WorkerThread(ChatFTPBean ftpBean) {
		this.ftpBean = ftpBean;
	}

	@Override
	public void run() {

		try {

			context = (Context) ftpBean.getCallback();
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher((Context) context);

			Log.d("FTP_STATUS", "Operation :" + ftpBean.getOperation());

			if (ftpBean.getOperation().equals("UPLOAD")) {
				ftpUpload();
			} else if (ftpBean.getOperation().equals("DOWNLOAD")) {
				ftpDownload();
			} else {
				Log.d("FTP_STATUS",
						"UN-Known Operation Type :" + ftpBean.getOperation());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " End.");
	}

	public void ftpUpload() {
		Log.i("check","Inside ftpUploadwebservice workerThread");

		final int notificationID = getNotificationID();
		try {

			String username = CallDispatcher.LoginUser;
			String password = CallDispatcher.Password;
            GroupChatBean gBean= (GroupChatBean) ftpBean.getSourceObject();
			String filename = ftpBean.getInputFile();
			String path = filename;
			Log.i("FileUploadIM1", "path3" +filename);

			Log.i("FileUpload1", "type--->" + gBean.getMimetype());
			SingleInstance.getGroupChatHistoryWriter().getQueue()
					.addObject(gBean);

			if((gBean.getMimetype().equals("mixedfile"))||gBean.getMimetype().equalsIgnoreCase("image")) {
                String[] paths=filename.split(",");
                if(paths.length>0) {
                    for (int i = 0; i < paths.length; i++) {
                        if(paths[i].endsWith(".jpg")){
                            Bitmap bitmap = BitmapFactory.decodeFile(paths[i]);
                            String base64 = encodeTobase64(bitmap);
                            String fname = paths[i].split("/")[5];
                            uploadFile(username, password, "photo", fname, base64, filename,SingleInstance.mainContext,ftpBean);
                        }else
                        {
                            String type=gBean.getMimetype();
                            if (paths[i].split("COMMedia/")[1].endsWith("mp4")) {
                                type="video";
                            }else if(paths[i].split("COMMedia/")[1].endsWith("mp3")
									|| paths[i].split("COMMedia/")[1].endsWith("amr")){
                                type="audio";
                            }else{
                                type="document";
                            }
                                String base64 = encodeAudioVideoToBase64(paths[i]);
                                String fname = paths[i].split("/")[5];
                               uploadFile(username, password, type, fname, base64, filename,SingleInstance.mainContext,ftpBean);

                        }
                    }
                }else{
                    Log.i("FileUploadIM", "IF PHOTO||Handsketch--->");
                    Log.i("FileUploadIM", "path" +filename);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    String base64 = encodeTobase64(bitmap);
                    String fname = filename.split("/")[5];
                    Log.i("FileUploadIM", "path2" +filename);
                    Log.i("FileUpload1", "fname--->" + fname);
                    Log.i("FileUpload1", "uname--->" + username);
                    Log.i("FileUpload1", "password--->" + password);
                    Log.i("FileUpload1", "type--->" + gBean.getMimetype());
                    Log.i("FileUpload1", "base64--->" + base64);
                    uploadFile(username, password, "photo", fname, base64, filename,SingleInstance.mainContext,ftpBean);
                }
			}
			else if(gBean.getMimetype().equalsIgnoreCase("audio")||gBean.getMimetype().equalsIgnoreCase("video"))
			{
//                encodeAudioVideoToBase64
				Log.i("FileUpload1", "ELSE IF AUDIO||Video--->" );
				Log.i("FileUpload1", "type--->" + gBean.getMimetype());
				Log.i("FileUploadIM", "path" +filename);
				String base64 = encodeAudioVideoToBase64(path);
				String fname = filename.split("/")[5];
				Log.i("FileUpload1", "fname--->" + fname);
				Log.i("FileUpload1", "uname--->" + username);
				Log.i("FileUpload1", "password--->" + password);
				Log.i("FileUpload1", "type--->" + gBean.getMimetype());
				Log.i("FileUpload1", "base64--->" + base64);

				uploadFile(username, password, gBean.getMimetype(), fname, base64,filename,SingleInstance.mainContext,ftpBean);
			}
			else if(gBean.getMimetype().equalsIgnoreCase("document")){
				String fname = filename.split("/")[5];
				String base64 = encodeAudioVideoToBase64(path);
				uploadFile(username, password, gBean.getMimetype(), fname, base64,filename,SingleInstance.mainContext,ftpBean);
			}
			notifyStatus(true);

            //end changes thendral renu 23-12-15


//            final AppMainActivity activity = ((AppMainActivity) SingleInstance.contextTable
//                    .get("MAIN"));
//             // start 07-10-15 changes //
//            // GK added for file encryption
//
////            if (ftpBean.getInputFile() != null && (ftpBean.getInputFile().endsWith("png") || ftpBean.getInputFile().endsWith("jpg")))
//            if(!NotePickerScreen.isOld) {
//                AESFileCrypto.encryptFile(ftpBean.getInputFile());
//            }else{
//                NotePickerScreen.isOld = false;
//            }
//
//            // end 07-10-15 changes //
//            // file encryption ended
//
//            File inputFile = new File(ftpBean.getInputFile());
//
//			final String fileName = inputFile.getName();
//
//			ftpClient = new FTPClient();
//
//			ftpClient.setAutoNoopTimeout(240000);
//
//			ftpClient.connect(ftpBean.getServerIp(), ftpBean.getServerPort());
//
//			Log.d("FTP_UPLOAD", "Connected" + fileName);
//			ftpClient.login(ftpBean.getUsername(), ftpBean.getPassword());
//			Log.d("FTP_UPLOAD", "Logged In" + fileName);
//			ftpClient.setType(FTPClient.TYPE_BINARY);
//			ftpClient.setPassive(true);
//
//			actualSize = inputFile.length();
//
//			try {
//				FTPFile[] serverFiles = ftpClient.list(fileName);
//				if (serverFiles.length != 0) {
//					for (FTPFile ftpFile : serverFiles) {
//						if (ftpFile.getSize() == inputFile.length()) {
//							Log.d("FTP_UPLOAD", fileName
//									+ " is available in Server");
//							activity.chatProgress(ftpBean, 100);
//							notifyStatus(true);
//							return;
//
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			mBuilder = showNotification("File Uploading", "", notificationID);
//
//			ftpClient.upload(inputFile, new FTPDataTransferListener() {
//
//				@Override
//				public void transferred(int byteSize) {
//
//					totalReceived = totalReceived + byteSize;
//					long bt = totalReceived * 100;
//					int tmp = (int) (bt / actualSize);
//
//					Log.d("FTP_UPLOAD", "Upload FileName : " + fileName
//							+ ",Total Received : " + totalReceived
//							+ ", transferred :" + byteSize + ", Per :" + tmp);
//
//					if (notificationManager != null && mBuilder != null) {
//
//						// mBuilder.setContentText(fileName + " -" + tmp + "%");
//						mBuilder.setContentText((actualSize / 1024) + " kb - "
//								+ fileName + " -" + tmp + "%");
//						mBuilder.setProgress(100, tmp, false);
//						notificationManager.notify(notificationID,
//								mBuilder.build());
//						if (ftpBean.getSourceObject() instanceof GroupChatBean) {
//							GroupChatBean gcBean = (GroupChatBean) ftpBean
//									.getSourceObject();
//							Log.i("value", "" + tmp);
//							gcBean.setProgress(tmp);
//							activity.chatProgress(ftpBean, tmp);
//						}
//						if (!SingleInstance.notificationBarIDMap
//								.contains(notificationID)) {
//							Log.i("notify123", "ID : " + notificationID);
//							SingleInstance.notificationBarIDMap
//									.add(notificationID);
//						}
//					}
//
//				}
//
//				@Override
//				public void started() {
//					Log.d("FTP_UPLOAD", "Started : " + fileName);
//
//				}
//
//				@Override
//				public void failed() {
//					Log.d("FTP_UPLOAD", "Failed : " + fileName);
//					if (notificationManager != null)
//						notificationManager.cancel(notificationID);
//					activity.chatProgress(ftpBean, 100);
//					notifyStatus(false);
//					if (SingleInstance.notificationBarIDMap
//							.contains(notificationID))
//						SingleInstance.notificationBarIDMap
//								.remove(notificationID);
//
//				}
//
//				@Override
//				public void completed() {
//					Log.d("FTP_UPLOAD", "Completed : " + fileName);
//					if (notificationManager != null)
//						notificationManager.cancel(notificationID);
//					notifyStatus(true);
//					if (SingleInstance.notificationBarIDMap
//							.contains(notificationID))
//						SingleInstance.notificationBarIDMap
//								.remove(notificationID);
//
//				}
//
//				@Override
//				public void aborted() {
//					Log.d("FTP_UPLOAD", "Aborted : " + fileName);
//					if (notificationManager != null)
//						notificationManager.cancel(notificationID);
//					activity.chatProgress(ftpBean, 100);
//					notifyStatus(false);
//					if (SingleInstance.notificationBarIDMap
//							.contains(notificationID))
//						SingleInstance.notificationBarIDMap
//								.remove(notificationID);
//
//				}
//			});
//
//			ftpClient.logout();
//			ftpClient.disconnect(true);
//
//			Log.d("FTP_STATUS", "Upload COMPLETED : " + fileName);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				ftpClient.disconnect(true);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if (notificationManager != null && mBuilder != null) {
				mBuilder.setAutoCancel(true);
				mBuilder.setContentTitle("File Download failed");
				notificationManager.notify(notificationID, mBuilder.build());
				notificationManager.cancel(notificationID);
				if (SingleInstance.notificationBarIDMap
						.contains(notificationID))
					SingleInstance.notificationBarIDMap.remove(notificationID);
			}
			Log.d("FTP_UPLOAD", "Exception : " + ftpBean.getInputFile());
			if (actualSize != 0 && totalReceived != 0
					&& (actualSize == totalReceived)) {
				notifyStatus(true);
				Log.i("FileUpload1UDP", "ELSE IF AUDIO||Video--->");
			} else
				notifyStatus(false);
		}
	}

	public void ftpDownload() {
		Log.i("check","Inside ftpdownloadwebservice workerThread");

		if(AppMainActivity.isLogin) {
            final int notificationID = getNotificationID();
            final String fileName = ftpBean.getInputFile();
            try {
				Log.d("input","inputfile"+ftpBean.getInputFile());
				String[] paths=this.ftpBean.getInputFile().split(",");
				for(String filename:paths){
					Log.d("input", "inputfile177777777 " + filename);
					AppMainActivity.imFiles.put(filename, this.ftpBean);
				}

//				AppMainActivity.imFiles.put(this.ftpBean.getInputFile(), this.ftpBean);

				Log.d("input", "inputfile1" + ftpBean.getInputFile());

				callDisp.downloadFile(CallDispatcher.LoginUser,CallDispatcher.Password,ftpBean.getInputFile());

				Log.d("XP WSD", "inputfile2" + ftpBean.getInputFile());

				Log.i("download1","newdownload"+fileName);
//                final AppMainActivity activity = ((AppMainActivity) SingleInstance.contextTable
//                        .get("MAIN"));
//
//			Log.d("FTP_DOWNLOAD", "FileName :" + fileName);
//
//			ftpClient = new FTPClient();
//
//			ftpClient.setAutoNoopTimeout(240000);
//
//			Log.d("FTP_DOWNLOAD", "Timeout :" + ftpClient.getAutoNoopTimeout());
//			ftpClient.connect(ftpBean.getServerIp(), ftpBean.getServerPort());
//
//			Log.d("FTP_DOWNLOAD", "Connected" + fileName);
//
//			ftpClient.login(ftpBean.getUsername(), ftpBean.getPassword());
//
//			try {
//				if (ftpClient.isConnected())
//					ftpClient.noop();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			Log.d("FTP_DOWNLOAD", "Logged In :" + fileName);
//			ftpClient.setType(FTPClient.TYPE_BINARY);
//			ftpClient.setPassive(true);
//
//			try {
//				FTPFile[] serverFiles = ftpClient.list(fileName);
//				if (serverFiles.length == 0) {
//					Log.d("FTP_DOWNLOAD", fileName
//							+ " is not available in Server");
//				} else {
//					for (FTPFile ftpFile : serverFiles) {
//						actualSize = ftpFile.getSize();
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			Log.d("FTP_DOWNLOAD", "Actual Size :" + actualSize);
//
//			mBuilder = showNotification("File Downloading", "", notificationID);
//
//			File outputFile = new File(ftpBean.getOutputFile());
//			ftpClient.download(ftpBean.getInputFile(), outputFile,
//					new FTPDataTransferListener() {
//
//						public void transferred(int byteSize) {
//
//							totalReceived = totalReceived + byteSize;
//							long bt = totalReceived * 100;
//
//							int tmp = (int) (bt / actualSize);
//
//							Log.d("FTP_DOWNLOAD", "Download FileName : "
//									+ fileName + ",Total Received : "
//									+ totalReceived + ", transferred :"
//									+ byteSize + ", Per :" + tmp);
//							if (notificationManager != null && mBuilder != null) {
//								mBuilder.setContentText((actualSize / 1024)
//										+ " kb - " + fileName + " -" + tmp
//										+ "%");
//								mBuilder.setProgress(100, tmp, false);
//								notificationManager.notify(notificationID,
//										mBuilder.build());
//
//								activity.chatProgress(ftpBean, tmp);
//							}
//						}
//
//						public void started() {
//							Log.d("FTP_DOWNLOAD", "Started : " + fileName);
//						}
//
//						public void failed() {
//							if (notificationManager != null)
//								notificationManager.cancel(notificationID);
//
//							Log.d("FTP_DOWNLOAD", "Failed : " + fileName);
//							activity.chatProgress(ftpBean, 100);
//							notifyStatus(false);
//						}
//
//						public void completed() {
//							if (notificationManager != null)
//								notificationManager.cancel(notificationID);
//
//							Log.d("FTP_DOWNLOAD", "Completed : " + fileName);
//							notifyStatus(true);
//
//						}
//
//						public void aborted() {
//							if (notificationManager != null)
//								notificationManager.cancel(notificationID);
//
//							Log.d("FTP_DOWNLOAD", "Aborted : " + fileName);
//							activity.chatProgress(ftpBean, 100);
//							notifyStatus(false);
//
//						}
//					});
//
//			ftpClient.logout();
//			ftpClient.disconnect(true);
//
//			Log.d("FTP_STATUS", "Downlaod COMPLETED : " + fileName);

		} catch (Exception e) {

			Log.d("FTP_DOWNLOAD", "Error Exception : " + fileName);
			e.printStackTrace();
			if (notificationManager != null && mBuilder != null) {
				mBuilder.setAutoCancel(true);
				mBuilder.setContentTitle("File Download failed");
				notificationManager.notify(notificationID, mBuilder.build());
				notificationManager.cancel(notificationID);
			}
			try {
				ftpClient.disconnect(true);
			} catch (Exception e2) {
				notifyStatus(false);
				e2.printStackTrace();

                }
                notifyStatus(false);
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

			String appName = ((Context) context).getResources().getString(
					R.string.app_name);

			title = appName + " - " + title;

			Context context = (AppMainActivity) this.context;

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
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return null;
	}

	private int getNotificationID() {
		try {
			long time = new Date().getTime();
			return (int) time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 100;
	}

	private void notifyStatus(final boolean status) {
		Log.i("check","Inside notifyStatus workerThread");

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					
						if (ftpBean != null) {
							((AppMainActivity) context).notifyChatFTPStatus(
									ftpBean, status);
						}
				
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("FTP_STATUS",
							"Unable to notify FTP :" + ftpBean.getInputFile());
				}
			}
		}).start();
	}




	private String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}

	private String encodeAudioVideoToBase64(String path){
		String strFile=null;
		File file=new File(path);
		try {
			FileInputStream file1=new FileInputStream(file);
			byte[] Bytearray=new byte[(int)file.length()];
			file1.read(Bytearray);
			strFile = Base64.encodeToString(Bytearray, Base64.NO_WRAP);//Convert byte array into string

		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("FileUpload", "audioVideoEncode========"+strFile);
		return strFile;
	}
	public void uploadFile(String username, String password,String componenttype,
						   String filename, String contents,String componentpath,Context context1, Object obj)
	{
		Log.i("FileUpload", "Inside CallDisp_UploadFile---> " +componentpath);

		String[] temp = new String[7];
		temp[0]=username;
		temp[1]=password;
		temp[2]=componenttype;
		temp[3]=filename;
		temp[4]=contents;
		File file = new File(componentpath);
		long length = (int) file.length();
		if(!componenttype.equalsIgnoreCase("photo"))
			length = length/1024;
		temp[5]="im";
		temp[6]= String.valueOf(length);
		Log.i("FileUpload", "Inside CallDisp_UploadFile---> " + temp[6]);
//		WebServiceReferences.webServiceClient.FileUpload(temp,context1,obj);
		ChatLoadWebservice taskrunner=new ChatLoadWebservice();
		taskrunner.execute(temp,obj);
	}
	public class ChatLoadWebservice extends
			AsyncTask<Object, Object, String> {

		@Override
		protected String doInBackground(Object... params) {
			try {
				String[] param=(String[])params[0];
				ChatFTPBean chatFTPBean=(ChatFTPBean)params[1];

				String parse="";
				String url= SingleInstance.mainContext.getResources().getString(R.string.service_url1);
				String loginIP = url.substring(url.indexOf("://") + 3);
				loginIP = loginIP.substring(0, loginIP.indexOf(":"));
				loginIP = loginIP.trim();

				String urlPort = url.substring(url.indexOf("://") + 3);
				urlPort = urlPort.substring(urlPort.indexOf(":") + 1);
				urlPort = urlPort.substring(0, urlPort.indexOf("/"));

				String server_ip = loginIP;
				int connect_ort = Integer.parseInt(urlPort);
				String namespace = "http://ltws.com/";
				String wsdl_link = url.trim()+"?wsdl";
				String quotes = "\"";

				parse= wsdl_link.substring(wsdl_link.indexOf("://") + 3);
				parse = parse.substring(parse.indexOf(":") + 1);
				parse = parse.substring(parse.indexOf("/"),
						parse.indexOf("?"));

				AndroidInsecureKeepAliveHttpsTransportSE androidHttpTransport = new AndroidInsecureKeepAliveHttpsTransportSE(
						server_ip, connect_ort, parse, 30000);

				SoapObject mRequest = new SoapObject(namespace, "FileUpload");
				XmlComposer xmlComposer = new XmlComposer();
				String fuploadxml = xmlComposer.fileUploadXml(param);

				HashMap<String,String> propert_map = new HashMap<String,String>();
				propert_map.put("uploadxml", fuploadxml);

				if (propert_map != null) {
					for (Map.Entry<String, String> set : propert_map.entrySet()) {
						mRequest.addProperty(set.getKey().trim(), set
								.getValue().trim());
					}
				}

				Log.d("webservice", "My Server Request  :" + mRequest);

				SoapSerializationEnvelope mEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				mEnvelope.setOutputSoapObject(mRequest);

				androidHttpTransport.call(quotes + namespace + "FileUpload" + quotes, mEnvelope);


				SoapPrimitive mSp = (SoapPrimitive) mEnvelope.getResponse();
				XmlParser mParser = new XmlParser();
				boolean mChk = false;
				mChk = mParser.getResult(mSp.toString());
				Log.d("webservice", "My Server Resopnse  :" + mSp.toString());
				if(mChk){
					SingleInstance.mainContext.showToast("File upload successfully");
					SingleInstance.mainContext.notifyFileUploadResponse(chatFTPBean);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPreExecute() {
		}

	}
}