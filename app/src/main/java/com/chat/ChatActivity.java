package com.chat;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.lib.model.PermissionBean;
import org.lib.model.SignalingBean;
import org.util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ConnectionBrokerServerBean;
import com.bean.GroupChatBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.HandSketchActivity;
import com.cg.commongui.MultimediaUtils;
import com.cg.ftpprocessor.FTPBean;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.NotePickerScreen;
import com.cg.locations.buddyLocation;
import com.ftp.ChatFTPBean;
import com.ftp.FTPPoolManager;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ViewProfileFragment;
import com.util.CustomVideoCamera;
import com.util.FullScreenImage;
import com.util.SingleInstance;
import com.util.Utils;
import com.util.VideoPlayer;

public class ChatActivity extends Activity implements OnClickListener {
	private Button btnBack;
	private Button callMenu;
	private ImageView profilePic;
	private TextView buddyStatus;
	private TextView buddyName;
	private ChatAdapter adapter = null;
	private ListView lv = null;
	private Vector<GroupChatBean> chatList;
	private Utility utility = new Utility();
	private String sessionid = null;
	public String buddy = null;
	private String status = null;
	private EditText message = null;
	private ImageView sendBtn = null;
	private ImageView attachBtn = null;
	private CallDispatcher callDisp = null;
	private Context context = null;
	private Handler handler = new Handler();
	private String strIPath = null;
	private LinearLayout attachment_layout;
	private Bitmap senderImageBitmap = null;
	private Bitmap receiverImageBitmap = null;
	private String fileHistoryName = null;
	private ProgressDialog dialog = null;
	AppMainActivity appMainActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chatscreen);
		sessionid = getIntent().getStringExtra("sessionid");
		buddy = getIntent().getStringExtra("buddy");
		status = getIntent().getStringExtra("status");
		WebServiceReferences.session = sessionid;
		WebServiceReferences.SelectedBuddy = buddy;
		appMainActivity = (AppMainActivity) (SingleInstance.contextTable
				.get("MAIN"));
		context = this;
		SingleInstance.contextTable.put("chatactivity", context);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;
		CallDispatcher.pdialog = new ProgressDialog(context);

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;

		btnBack = (Button) findViewById(R.id.btn_cancel);
		btnBack.setOnClickListener(this);

		callMenu = (Button) findViewById(R.id.callmenu);
		profilePic = (ImageView) findViewById(R.id.buddypic);
		buddyStatus = (TextView) findViewById(R.id.buddystatus);
		buddyName = (TextView) findViewById(R.id.buddyname);
		buddyName.setText(buddy);

		fileHistoryName = CallDispatcher.LoginUser + buddy + ".xml";

		createProfileImageFileAndFolder();
		createFilesAndFolder(fileHistoryName);
		loadProfilePic();
		lv = (ListView) findViewById(R.id.chat_listview);
		message = (EditText) findViewById(R.id.txt_msg);
		message.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				processAttachmentView();
				return false;
			}
		});

		sendBtn = (ImageView) findViewById(R.id.send_image);

		attachBtn = (ImageView) findViewById(R.id.attach);
		attachBtn.setTag(0);
		attachBtn.setOnClickListener(this);

		RelativeLayout btn_image = (RelativeLayout) findViewById(R.id.btn_image);
		btn_image.setOnClickListener(this);
		RelativeLayout btn_handsketch = (RelativeLayout) findViewById(R.id.btn_handsketch);
		btn_handsketch.setOnClickListener(this);
		RelativeLayout btn_audio = (RelativeLayout) findViewById(R.id.btn_audio);
		btn_audio.setOnClickListener(this);

		RelativeLayout btn_video = (RelativeLayout) findViewById(R.id.btn_video);
		btn_video.setOnClickListener(this);
		// RelativeLayout btn_location = (RelativeLayout)
		// findViewById(R.id.btn_location);
		// btn_location.setOnClickListener(this);

		attachment_layout = (LinearLayout) findViewById(R.id.attachment_layout);
		attachment_layout.setVisibility(View.GONE);
		if (loadChatHistory() != null)
			chatList = loadChatHistory();
		if (SingleInstance.chatHistory.get(buddy) != null) {
			chatList = SingleInstance.chatHistory.get(buddy);
		} else {
			if (chatList == null)
				chatList = new Vector<GroupChatBean>();
			SingleInstance.chatHistory.put(buddy, chatList);
		}

		adapter = new ChatAdapter(this, chatList);
		CallDispatcher.pdialog = new ProgressDialog(context);
		lv.setAdapter(adapter);
		removeReadNotification();

		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (message.getText().toString().trim().length() > 0) {
					sendMsg(message.getText().toString().trim(), null, "MTP");
					message.setText("");
				}
			}
		});
		callMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCallMenuDialog();
			}
		});
		profilePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showBuddyDialog();
			}
		});
		Log.i("chat", "chat123 end of oncreate");
	}

	@Override
	public void onBackPressed() {
		if (attachBtn.getTag().equals(1)) {
			processAttachmentView();
			return;
		}
		super.onBackPressed();
	}

	private void sendMsg(String message, String localpath, String type) {

		// ChatBean chatBean = new ChatBean();
		// chatBean.setFromUser(CallDispatcher.LoginUser);
		// chatBean.setMessageType(type);
		// chatBean.setToUser(buddy);
		// chatBean.setUsername(CallDispatcher.LoginUser + buddy);
		// chatBean.setSender(true);
		// chatBean.setTimeDate(callDisp.getCurrentDateandTime());
		// chatBean.setSessionId(sessionid);
		// if (type.equalsIgnoreCase("MTP")) {
		// chatBean.setMessage(message);
		// SingleInstance.getChatProcesser().getQueue().addObject(chatBean);
		// CallDispatcher.adapterToShow.notifyDataSetChanged();
		// ExchangesFragment exchanges = ExchangesFragment
		// .newInstance(context);
		// if (exchanges != null)
		// exchanges.adapter.notifyDataSetChanged();
		// } else {
		// // if (chatBean.getMessageType().equalsIgnoreCase("MPP")
		// // || chatBean.getMessageType().equalsIgnoreCase("MHP")) {
		// // Bitmap bitMap = callDisp.ResizeImage(localpath);
		// // bitMap = Bitmap.createScaledBitmap(bitMap, 200, 150, false);
		// // chatBean.setProfilePic(bitMap);
		// // }
		// chatBean.setMessage(localpath);
		// uploadFile(chatBean.clone());
		// }
		// chatList.add(chatBean);

		GroupChatBean gcBean = new GroupChatBean();
		gcBean.setFrom(CallDispatcher.LoginUser);
		gcBean.setType("100");
		gcBean.setMimetype(type);
		if (type.equals("text")) {
			gcBean.setMessage(message);
		} else {
			gcBean.setMessage(message);
			gcBean.setMediaName(localpath);
			gcBean.setFtpUsername(CallDispatcher.LoginUser);
			gcBean.setFtpPassword(CallDispatcher.Password);
		}
		gcBean.setTo(buddy);
		gcBean.setSessionid(CallDispatcher.LoginUser + buddy);
		gcBean.setGroupId(CallDispatcher.LoginUser + buddy);
		gcBean.setSignalid(Utility.getSessionID());
		gcBean.setSenttime(callDisp.getCurrentDateandTime());
		gcBean.setSenttimez("GMT");
		gcBean.setCategory("G");
		if (type.equals("text")) {
			SingleInstance.getGroupChatProcesser().getQueue().addObject(gcBean);
		} else {
			uploadFile(gcBean);
		}
		if (gcBean.getSubCategory() != null
				&& (gcBean.getSubCategory().equalsIgnoreCase("gdi") || gcBean
						.getSubCategory().equalsIgnoreCase("grbi"))
				&& gcBean.getParentId() != null
				&& gcBean.getParentId().length() > 0
				&& !gcBean.getParentId().equalsIgnoreCase("null")) {
			Vector<GroupChatBean> deadLineList = new Vector<GroupChatBean>();
			for (int i = 0; i < chatList.size(); i++) {
				GroupChatBean gcBean1 = chatList.get(i);
				if (gcBean1 != null
						&& gcBean1.getSignalid().equals(gcBean.getParentId())) {
					Vector<GroupChatBean> existingMsgList = callDisp
							.getdbHeler(context).getDeadLineGroupChatBean(
									gcBean.getParentId());
					int size = 0;
					if (existingMsgList != null) {
						size = existingMsgList.size();
					}
					deadLineList.add(gcBean);
					chatList.addAll(i + size + 1, deadLineList);
					break;
				}

			}

		} else
			chatList.add(gcBean);

		adapter.notifyDataSetChanged();

		hideKeyboard();

	}

	private void uploadFile(GroupChatBean chatBean) {

		try {
			if (CallDispatcher.LoginUser != null) {

				AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
						.get("MAIN");

				GroupChatBean gcBean = (GroupChatBean) chatBean.clone();
				File file = new File(gcBean.getMediaName());
				gcBean.setMediaName(file.getName());
				ConnectionBrokerServerBean cBean = ((AppMainActivity) SingleInstance.contextTable
						.get("MAIN")).cBean;
				ChatFTPBean chatFTPBean = new ChatFTPBean();
				chatFTPBean.setServerIp(cBean.getRouter().split(":")[0]);
				chatFTPBean.setServerPort(40400);
				chatFTPBean.setUsername(CallDispatcher.LoginUser);
				chatFTPBean.setPassword(CallDispatcher.Password);
				chatFTPBean.setInputFile(chatBean.getMediaName());
				chatFTPBean.setOperation("UPLOAD");
				chatFTPBean.setSourceObject(gcBean);
				chatFTPBean.setCallback(appMainActivity);

				FTPPoolManager.processRequest(chatFTPBean);

				/*
				 * FTPBean ftpBean = new FTPBean();
				 * ftpBean.setServer_ip(callDisp.getRouter().split(":")[0]);
				 * ftpBean.setServer_port(40400);
				 * ftpBean.setFtp_username(CallDispatcher.LoginUser);
				 * ftpBean.setFtp_password(CallDispatcher.Password);
				 * ftpBean.setFile_path(gBean.getMediaName());
				 * ftpBean.setOperation_type(1); ftpBean.setReq_object(gBean);
				 * ftpBean.setRequest_from(gBean.getType());
				 * 
				 * if (callDisp.getFtpNotifier() != null)
				 * callDisp.getFtpNotifier().addTasktoExecutor(ftpBean);
				 */
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showToast(String msg) {
		try {
			Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (SingleInstance.contextTable.containsKey("chatactivity"))
			SingleInstance.contextTable.remove("chatactivity");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	public void updateUI(final ChatBean chatBean) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// chatList.add(chatBean);
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(message.getWindowToken(), 0);

		// imm.showSoftInput(ed, 0);
	}

	private Vector<GroupChatBean> loadChatHistory() {
		// String filePath = Environment.getExternalStorageDirectory()
		// + "/COMMedia/Chat/" + CallDispatcher.LoginUser + buddy + ".xml";
		// File file = new File(filePath);
		// if (file.exists()) {
		// String xml = readChatHistroy(filePath);
		// XMLParser xmlParser = new XMLParser();
		// Vector<ChatBean> chatList = xmlParser.parseChatHistory(xml);
		// return chatList;
		// } else {
		// try {
		// file.createNewFile();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		try {
			Vector<GroupChatBean> chatList = callDisp.getdbHeler(context)
					.getGroupChatHistory(CallDispatcher.LoginUser + buddy,
							false,0);
			return chatList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private String readChatHistroy(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			StringBuilder builder = new StringBuilder();
			builder.append("<?xml version=\"1.0\" ?><imchat>");
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			}
			reader.close();
			reader = null;
			builder.append("</imchat>");
			return builder.toString();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return null;
	}

	private void multimediaType(String strMMType) {
		Log.i("clone", "===> inside message response");

		try {
			if (strMMType.equals("Gallery")) {
				Log.i("clone", "====> inside gallery");
				if (Build.VERSION.SDK_INT < 19) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, 30);
				} else {
					Log.i("img", "sdk is above 19");
					Log.i("clone", "====> inside gallery");
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(intent, 31);
				}

			} else if (strMMType.equals("Photo")) {
				Log.i("clone", "====> inside photo");
				Long free_size = callDisp.getExternalMemorySize();
				Log.d("IM", free_size.toString());
				if (free_size > 0 && free_size >= 5120) {
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/MPD_" + callDisp.getFileName()
							+ ".jpg";
					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", strIPath);
					intent.putExtra("requestCode", 32);
					intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra("createOrOpen", "create");
					startActivity(intent);

				} else {
					showToast("InSufficient Memory...");
				}
			} else if (strMMType.equals("Handsketch")) {
				Intent intent = new Intent(context, HandSketchActivity.class);
				startActivityForResult(intent, 36);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("clone", "=======>" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void photochat() {
		try {
			final String[] items = new String[] { "Gallery", "Photo",
					"Handsketch" };
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0)
						multimediaType(items[0]);
					else if (which == 1)
						multimediaType(items[1]);
					else if (which == 2)
						multimediaType(items[2]);
				}

			});

			builder.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.cancel();
				}
			});
			builder.show();
		} catch (Exception e) {
			Log.e("profile", "===> " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void showCallMenuDialog() {
		try {
			CharSequence[] m_type = { "Audio Call", "Video Call",
					"Audio Unicast", "Video Unicast" };
			AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
			alert_builder.setTitle("Select Call Type");
			final PermissionBean permissionBean = callDisp.getdbHeler(context)
					.selectPermissions(
							"select * from setpermission where userid='"
									+ buddyName.getText().toString()
									+ "' and buddyid='"
									+ CallDispatcher.LoginUser + "'",
							buddyName.getText().toString(),
							CallDispatcher.LoginUser);
			alert_builder.setSingleChoiceItems(m_type, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int pos) {
							// TODO Auto-generated method stub
							if (pos == 0) {
								if (permissionBean.getAudio_call().equals("1"))
									callDisp.MakeCall(1, buddyName.getText()
											.toString(), context);
								else
									callDisp.showAlert("Response",
											"Access Denied");
								dialog.dismiss();
							} else if (pos == 1) {
								if (permissionBean.getVideo_call().equals("1"))
									callDisp.MakeCall(2, buddyName.getText()
											.toString(), context);
								else
									callDisp.showAlert("Response",
											"Access Denied");
								dialog.dismiss();

							} else if (pos == 2) {
								if (permissionBean.getAUC().equals("1"))
									callDisp.MakeCall(3, buddyName.getText()
											.toString(), context);
								else
									callDisp.showAlert("Response",
											"Access Denied");
								dialog.dismiss();
							} else if (pos == 3) {
								if (permissionBean.getVUC().equals("1"))
									callDisp.MakeCall(4, buddyName.getText()
											.toString(), context);
								else
									callDisp.showAlert("Response",
											"Access Denied");
								dialog.dismiss();

							}
						}
					});
			alert_builder.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				if (requestCode == 30) {
					Uri selectedImageUri = data.getData();
					strIPath = callDisp.getRealPathFromURI(selectedImageUri);
					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);

					if (length <= 2) {

						Bitmap img = null;
						img = callDisp.ResizeImage(strIPath);

						if (img != null) {
							img = Bitmap
									.createScaledBitmap(img, 100, 75, false);

							final String path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ callDisp.getFileName()
									+ ".jpg";

							BufferedOutputStream stream;
							Bitmap bitmap = null;
							try {
								bitmap = callDisp.ResizeImage(strIPath);
								if (bitmap != null) {
									stream = new BufferedOutputStream(
											new FileOutputStream(new File(path)));
									bitmap.compress(CompressFormat.JPEG, 100,
											stream);
									strIPath = path;
								}

								if (bitmap != null) {
									bitmap = Bitmap.createScaledBitmap(bitmap,
											200, 150, false);
								}
								scaleImage(strIPath);
								sendMsg("", strIPath, "MPP");

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				} else if (requestCode == 31) {

					Uri selectedImageUri = data.getData();
					final int takeFlags = data.getFlags()
							& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					getContentResolver().takePersistableUriPermission(
							selectedImageUri, takeFlags);
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + "MPD_" + callDisp.getFileName()
							+ ".jpg";
					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);
					if (length <= 2) {
						new bitmaploader().execute(selectedImageUri);
					} else {
						showToast("Kindly Select someother image,this image is too large");
					}
				} else if (requestCode == 32) {

					File fileCheck = new File(strIPath);
					if (fileCheck.exists()) {
						Bitmap bitmap = scaleImage(strIPath);
						if (bitmap != null) {
							sendMsg("", strIPath, "MPP");
						} else
							Toast.makeText(context,
									"Can not save this picture",
									Toast.LENGTH_LONG).show();
					}
				} else if (requestCode == 34) {
					Bundle bun = data.getBundleExtra("share");
					if (bun != null) {
						String path = bun.getString("filepath");
						String type = bun.getString("type");
						File file = null;
						if (type.equalsIgnoreCase("audio")) {
							file = new File(path);
							if (file.exists())
								sendMsg("", path, "MAP");
						} else if (type.equalsIgnoreCase("video")) {
							file = new File(path + ".mp4");
							if (file.exists()) {
								sendMsg("", path + ".mp4", "MVP");
							}

						}
					}

				} else if (requestCode == 40) {
					File file = new File(strIPath);
					if (file.exists())
						sendMsg("", strIPath, "MVP");
				} else if (requestCode == 36) {
					Bundle bun = data.getBundleExtra("sketch");
					strIPath = bun.getString("path");
					File fileCheck = new File(strIPath);
					if (fileCheck.exists()) {
						Bitmap bitmap = scaleImage(strIPath);
						if (bitmap != null) {
							sendMsg("", strIPath, "MHP");
						} else
							Toast.makeText(context,
									"Can not save this picture",
									Toast.LENGTH_LONG).show();
					}
				}

			}
			if (requestCode == 33) {
				File file = new File(strIPath);
				if (file.exists())
					sendMsg("", strIPath, "MAP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Bitmap scaleImage(String strIPath) {

		Bitmap bitmap = null;

		bitmap = callDisp.ResizeImage(strIPath);
		if (bitmap != null) {
			String path = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + "MPD_" + callDisp.getFileName() + ".jpg";

			BufferedOutputStream stream;
			try {
				stream = new BufferedOutputStream(new FileOutputStream(
						new File(path)));
				bitmap.compress(CompressFormat.JPEG, 100, stream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bitmap != null) {
				bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);
			}
		}

		return bitmap;
	}

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			try {
				super.onPostExecute(result);
				Log.d("image", "came to post execute for image");

				Bitmap img = null;
				if (strIPath != null)
					img = callDisp.ResizeImage(strIPath);
				if (img != null) {
					scaleImage(strIPath);
					sendMsg("", strIPath, "MPP");
					callDisp.cancelDialog();
					Log.d("OnActivity", "_____On Activity Called______");
				} else {
					strIPath = null;
				}

			} catch (Exception e) {
				Log.e("profile", "====> " + e.getMessage());
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			try {
				super.onPreExecute();
				ProgressDialog dialog = new ProgressDialog(context);
				callDisp.showprogress(dialog, context);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("profile", "====> " + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			try {
				for (Uri uri : params) {
					Log.d("image", "came to doin backgroung for image");
					FileInputStream fin = (FileInputStream) getContentResolver()
							.openInputStream(uri);
					ByteArrayOutputStream straam = new ByteArrayOutputStream();
					byte[] content = new byte[1024];
					int bytesread;
					while ((bytesread = fin.read(content)) != -1) {
						straam.write(content, 0, bytesread);
					}
					byte[] bytes = straam.toByteArray();
					FileOutputStream fout = new FileOutputStream(strIPath);
					straam.flush();
					straam.close();
					straam = null;
					fin.close();
					fin = null;
					fout.write(bytes);
					fout.flush();
					fout.close();
					fout = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	public void uploadCompleted(FTPBean bean) {
		if (bean != null) {
			ChatBean chatBean = (ChatBean) bean.getReq_object();
			chatBean.setMessage(bean.getFilename());
			SingleInstance.getChatProcesser().getQueue().addObject(chatBean);
		}

	}

	private void showAudioMessageDialog() {
		try {
			AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
			String[] type = { "New Audio", "Existing Audio" };

			alert_builder.setSingleChoiceItems(type, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int pos) {
							// TODO Auto-generated method stub
							if (pos == 0) {
								strIPath = Environment
										.getExternalStorageDirectory()
										+ "/COMMedia/"
										+ "MAD_"
										+ callDisp.getFileName() + ".mp4";
								Intent intent = new Intent(context,
										MultimediaUtils.class);
								intent.putExtra("filePath", strIPath);
								intent.putExtra("requestCode", 33);
								intent.putExtra("action", "audio");
								intent.putExtra("createOrOpen", "create");
								startActivity(intent);
								dialog.dismiss();
							} else if (pos == 1) {
								Intent i = new Intent(context,
										NotePickerScreen.class);
								i.putExtra("note", "audio");
								startActivityForResult(i, 34);
								dialog.dismiss();

							}
						}
					});
			alert_builder.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private void showVideoMessageDialog() {
		try {
			AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
			String[] type = { "New Video", "Existing Video" };

			alert_builder.setSingleChoiceItems(type, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int pos) {
							// TODO Auto-generated method stub
							if (pos == 0) {
								strIPath = Environment
										.getExternalStorageDirectory()
										+ "/COMMedia/"
										+ "MVD_"
										+ callDisp.getFileName() + ".mp4";
								Intent intent = new Intent(context,
										CustomVideoCamera.class);
								intent.putExtra("filePath", strIPath);
								// intent.putExtra("requestCode", 35);
								// intent.putExtra("action",
								// MediaStore.ACTION_VIDEO_CAPTURE);
								// intent.putExtra("createOrOpen", "create");
								startActivityForResult(intent, 40);
								dialog.dismiss();
							} else if (pos == 1) {
								Intent i = new Intent(context,
										NotePickerScreen.class);
								i.putExtra("note", "video");
								startActivityForResult(i, 34);
								dialog.dismiss();

							}
						}
					});
			alert_builder.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private String setProfileImage(String userName) {

		try {

			String profilePicPath = Environment.getExternalStorageDirectory()
					+ "/COMMedia/profile/" + userName + ".png";
			File file = new File(profilePicPath);
			if (file.exists())
				file.delete();
			String profilePict = Environment.getExternalStorageDirectory()
					+ "/COMMedia/"
					+ callDisp.getdbHeler(context).getProfilePic(userName);
			Log.i("chat", "chat123 :: profile pic path " + profilePict);
			Bitmap bmp = callDisp.ResizeImage(profilePict);
			FileOutputStream out = new FileOutputStream(profilePicPath);
			bmp.compress(Bitmap.CompressFormat.PNG, 50, out);
			out.flush();
			out.close();
			return profilePicPath;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return null;

	}

	private void createProfileImageFileAndFolder() {

		String sdCard = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/COMMedia/profile";

		File file = new File(sdCard);
		if (!file.exists())
			file.mkdirs();
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	private void createFilesAndFolder(String chatFileName) {
		String sdCard = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/COMMedia/chat";
		File file = new File(sdCard);
		if (!file.exists())
			file.mkdirs();
		file = new File(sdCard + "/" + chatFileName);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_image:
			photochat();
			break;
		case R.id.btn_handsketch:
			Intent intent = new Intent(context, HandSketchActivity.class);
			startActivityForResult(intent, 36);
			break;
		case R.id.btn_audio:
			showAudioMessageDialog();
			break;

		case R.id.btn_video:
			showVideoMessageDialog();
			break;
		case R.id.btn_location:
			if (CallDispatcher.latitude == 0.0
					&& CallDispatcher.longitude == 0.0) {
				showToast("Sorry! Turn On Location Service ");
			} else {
				sendMsg("Latitude:" + CallDispatcher.latitude + ","
						+ "Longitude:" + CallDispatcher.longitude, "", "MTP");
			}
			break;
		case R.id.attach: {
			hideKeyboard();
			if (attachBtn.getTag().equals(0)) {
				attachBtn.setTag(1);
				attachment_layout.setVisibility(View.VISIBLE);
			} else {
				attachBtn.setTag(0);
				attachment_layout.setVisibility(View.GONE);
			}
		}
			break;
		case R.id.btn_cancel:
			finish();
			break;

		default:
			break;
		}

	}

	private void processAttachmentView() {
		if (attachBtn.getTag().equals(1)) {
			attachBtn.setTag(0);
			attachment_layout.setVisibility(View.GONE);
		}
	}

	class ChatAdapter extends ArrayAdapter<ChatBean> {

		/*********** Declare Used Variables *********/
		private Context context;
		private Vector<GroupChatBean> chatList;
		private LayoutInflater inflater = null;
		ChatBean tempValues = null;
		ImageLoader imageLoader;
		int i = 0;

		/************* CustomAdapter Constructor *****************/
		public ChatAdapter(Context context, Vector<GroupChatBean> chatList) {

			super(context, R.layout.chat_view);
			/********** Take passed values **********/
			this.context = context;
			this.chatList = chatList;

			imageLoader = new ImageLoader(context.getApplicationContext());

			/*********** Layout inflator to call external xml layout () ***********/
		}

		public long getItemId(int position) {
			return position;
		}

		/******
		 * Depends upon data size called for each row , Create each ListView row
		 *****/
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.chat_view, null);
			}

			if (chatList.size() != 0) {

				final GroupChatBean chatBean = chatList.get(position);

				TextView message, dateTime;
				// ImageView buddyIcon;
				final ImageView multimediaIcon, locationIcon;
				RelativeLayout senderLayout = (RelativeLayout) convertView
						.findViewById(R.id.sender_view);
				RelativeLayout receiverLayout = (RelativeLayout) convertView
						.findViewById(R.id.receiver_view);
				if (chatBean.getFrom().equals(CallDispatcher.LoginUser)) {
					message = (TextView) convertView
							.findViewById(R.id.sender_text_msg);
					multimediaIcon = (ImageView) convertView
							.findViewById(R.id.sender_multi_msg);
					dateTime = (TextView) convertView
							.findViewById(R.id.sender_datetime);
					// buddyIcon = (ImageView) convertView
					// .findViewById(R.id.sender_buddy_icon);
					// if (senderImageBitmap != null)
					// buddyIcon.setImageBitmap(senderImageBitmap);
					locationIcon = (ImageView) convertView
							.findViewById(R.id.sender_loc_icon);
					receiverLayout.setVisibility(View.GONE);
					senderLayout.setVisibility(View.VISIBLE);

				} else {
					message = (TextView) convertView
							.findViewById(R.id.receiver_text_msg);
					dateTime = (TextView) convertView
							.findViewById(R.id.receiver_datetime);
					// buddyIcon = (ImageView) convertView
					// .findViewById(R.id.receiver_buddy_icon);
					// if (receiverImageBitmap != null)
					// buddyIcon.setImageBitmap(receiverImageBitmap);
					multimediaIcon = (ImageView) convertView
							.findViewById(R.id.receiver_multi_msg);
					locationIcon = (ImageView) convertView
							.findViewById(R.id.receiver_loc_icon);
					senderLayout.setVisibility(View.GONE);
					receiverLayout.setVisibility(View.VISIBLE);

				}
				if (chatBean.getMimetype().equalsIgnoreCase("text")) {
					message.setVisibility(View.VISIBLE);
					multimediaIcon.setVisibility(View.GONE);
					message.setText(chatBean.getMessage());
					if (chatBean.getMessage().startsWith("Latitude:")) {
						locationIcon.setVisibility(View.VISIBLE);
						locationIcon.setTag(chatBean.getMessage());
					} else {
						locationIcon.setVisibility(View.GONE);
					}
				} else {
					multimediaIcon.setVisibility(View.VISIBLE);
					Log.i("chat", "caht 123 file path " + chatBean.getMessage());
					if (chatBean.getMimetype().equalsIgnoreCase("image")) {
						// Bitmap bitMap = callDisp.ResizeImage(chatBean
						// .getMessage());
						// multimediaIcon.setImageBitmap(chatBean.getProfilePic());
						imageLoader.DisplayImage(chatBean.getMessage(),
								multimediaIcon, R.drawable.refresh);
					} else {
						Bitmap bitMap = null;
						if (chatBean.getMimetype().equalsIgnoreCase("audio"))
							multimediaIcon.setImageResource(R.drawable.audio2);
						else
							multimediaIcon
									.setImageResource(R.drawable.videoview1);
					}
					multimediaIcon.setTag(chatBean.getMessage());
					multimediaIcon
							.setContentDescription(chatBean.getMimetype());
					message.setVisibility(View.GONE);
					locationIcon.setVisibility(View.GONE);
					// Bitmap bmp = ((BitmapDrawable)
					// multimediaIcon.getDrawable())
					// .getBitmap();
					// if (bmp != null && !bmp.isRecycled()) {
					// Log.i("chat", "chat :: " + bmp);
					// }
					// bmp.recycle();
				}

				dateTime.setText(chatBean.getSenttime());

				multimediaIcon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						File file = new File(v.getTag().toString());

						if (file.exists()) {
							if (v.getContentDescription().toString()
									.equalsIgnoreCase("audio")) {
								Intent intent = new Intent(context,
										MultimediaUtils.class);
								intent.putExtra("filePath", v.getTag()
										.toString());
								intent.putExtra("action", "audio");
								intent.putExtra("createOrOpen", "open");
								SipNotificationListener.getCurrentContext()
										.startActivity(intent);
							} else if (v.getContentDescription().toString()
									.equalsIgnoreCase("video")) {
								Intent intentVPlayer = new Intent(context,
										VideoPlayer.class);
								// intentVPlayer.putExtra("File_Path",
								// v.getTag()
								// .toString());
								// intentVPlayer.putExtra("Player_Type",
								// "Video Player");
								// intentVPlayer.putExtra("time", 0);
								intentVPlayer.putExtra("video", v.getTag()
										.toString());

								SipNotificationListener.getCurrentContext()
										.startActivity(intentVPlayer);
							} else if (v.getContentDescription().toString()
									.equalsIgnoreCase("image")) {
								// Intent in = new Intent(context,
								// PhotoZoomActivity.class);
								// in.putExtra("Photo_path",
								// v.getTag().toString());
								// in.putExtra("type", "true");
								// SipNotificationListener.getCurrentContext()
								// .startActivity(in);
								Intent intent = new Intent(context,
										FullScreenImage.class);
								intent.putExtra("image", v.getTag().toString());
								startActivity(intent);
							}
						}
					}
				});

				locationIcon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (v.getTag().toString() != null) {
							Intent intent = new Intent(context,
									buddyLocation.class);
							showprogress();
							String locs[] = callDisp.getBuddyLocation(v
									.getTag().toString());
							intent.putExtra("latitude", locs[0]);
							intent.putExtra("longitude", locs[1]);
							SipNotificationListener.getCurrentContext()
									.startActivity(intent);
						}
					}
				});
			}
			return convertView;
		}

		/********* Called when Item click in ListView ************/
		private class OnItemClickListener implements OnClickListener {
			private int mPosition;

			OnItemClickListener(int position) {
				mPosition = position;
			}

			@Override
			public void onClick(View arg0) {

			}
		}

	}

	private void loadProfilePic() {
		Thread profileThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String senderImagePath = setProfileImage(CallDispatcher.LoginUser);
				String receiverImagePath = setProfileImage(buddy);
				senderImageBitmap = BitmapFactory.decodeFile(senderImagePath);
				receiverImageBitmap = BitmapFactory
						.decodeFile(receiverImagePath);
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (receiverImageBitmap != null)
							profilePic.setImageBitmap(Utils
									.getRoundedShape(receiverImageBitmap));
						else {
							BitmapFactory.decodeResource(
									context.getResources(),
									R.drawable.icon_buddy_aoffline);
							profilePic.setImageBitmap(BitmapFactory
									.decodeResource(context.getResources(),
											R.drawable.icon_buddy_aoffline));
						}
						adapter.notifyDataSetChanged();
					}
				});

			}
		});
		profileThread.start();
	}

	private void showBuddyDialog() {
		try {
			AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
			alert_builder.setTitle("Select Buddy Info");
			CharSequence[] b_type = { "View Profile", "ClearAll", "Cancel" };
			alert_builder.setSingleChoiceItems(b_type, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int pos) {
							// TODO Auto-generated method stub
							if (pos == 0) {
								callDisp.doViewProfile(buddy);
								dialog.dismiss();
							} else if (pos == 1) {
								dialog.dismiss();
								clearAllAlert();
							} else if (pos == 2) {
								dialog.dismiss();

							}
						}
					});
			alert_builder.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private void viewProfile() {

		try {

			ArrayList<String> profileList = DBAccess.getdbHeler().getProfile(
					buddy);
			if (profileList.size() > 0) {
				ViewProfileFragment viewProfileFragment = ViewProfileFragment
						.newInstance(context);

				viewProfileFragment.setBuddyName(buddy);
				FragmentManager fragmentManager =SingleInstance.mainContext.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment,
						viewProfileFragment);
				fragmentTransaction.commit();

			} else {
				if (appMainActivity.isNetworkConnectionAvailable()) {
					ProgressDialog dialog = new ProgressDialog(context);
					showprogress();
					CallDispatcher.isFromCallDisp = false;
					String modifiedDate = DBAccess.getdbHeler()
							.getModifiedDate(
									"select max(modifieddate) from profilefieldvalues where userid='"
											+ buddy + "'");
					if (modifiedDate == null) {
						modifiedDate = "";
					} else if (modifiedDate.trim().length() == 0) {
						modifiedDate = "";
					}
					String[] profileDetails = new String[3];
					profileDetails[0] = buddy;
					profileDetails[1] = "5";
					profileDetails[2] = modifiedDate;
					WebServiceReferences.webServiceClient
							.getStandardProfilefieldvalues(profileDetails);
				} else
					showToast("Kindly check your network ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void clearAllAlert() {

		try {
			AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
			myAlertDialog.setTitle("Clear Chat History");
			myAlertDialog
					.setMessage("Are you sure, you want to clear chat history of "
							+ buddy + "?");
			myAlertDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							clearAll();
						}
					});
			myAlertDialog.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int arg1) {
							// do something when the Cancel button is clicked

							dialog.cancel();
						}
					});
			myAlertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}



	public void clearAll() {

		// TODO Auto-generated method stub
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/COMMedia/chat/" + fileHistoryName);
		if (file.exists()) {
			file.delete();
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				chatList.clear();
				Vector<GroupChatBean> chatHistory = SingleInstance.chatHistory
						.get(buddy);
				chatHistory.clear();
				adapter.notifyDataSetChanged();
			}
		});

	}

	private void removeReadNotification() {
		if (WebServiceReferences.Imcollection.size() > 0) {

			if (WebServiceReferences.Imcollection.containsKey(sessionid)) {
				ArrayList<SignalingBean> value = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
						.get(sessionid);
				if (WebServiceReferences.tempIMCount != 0) {
					WebServiceReferences.tempIMCount = WebServiceReferences.tempIMCount
							- value.size();
				}
				value.clear();
				if (WebServiceReferences.Imcollection.containsKey(sessionid)) {
					WebServiceReferences.Imcollection.remove(sessionid);
				}
			}

			else if (WebServiceReferences.activeSession.containsKey(buddy)) {
				String key = WebServiceReferences.activeSession.get(buddy);

				if (WebServiceReferences.Imcollection.containsKey(key)) {
					ArrayList<SignalingBean> value = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
							.get(key);
					value.clear();
					if (WebServiceReferences.Imcollection.containsKey(key)) {
						WebServiceReferences.Imcollection.remove(key);
					}
				}
			}
			// CallDispatcher.adapterToShow.notifyDataSetChanged();
		}
	}

	private void showprogress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

	}

	public void cancelDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

}
