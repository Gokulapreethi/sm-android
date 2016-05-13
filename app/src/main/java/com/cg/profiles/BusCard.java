package com.cg.profiles;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.lib.model.FieldTemplateBean;
import org.lib.model.SignalingBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.ftpprocessor.FTPBean;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.IMNotifier;
import com.main.AppMainActivity;
import com.util.SingleInstance;
import com.util.VideoPlayer;

@SuppressWarnings({ "deprecation" })
public class BusCard extends Activity implements OnClickListener,
		SlideMenuInterface.OnSlideMenuItemClickListener, IMNotifier {

	private Context context = null;

	private CallDispatcher callDisp = null;

	private Button btn_back = null;

	private TextView heading = null;

	private Button btn_save = null;

	public LinearLayout fieldContainer = null;

	public Vector<FieldTemplateBean> profileFieldList;

	private Vector<FieldTemplateBean> getFieldValuesList = new Vector<FieldTemplateBean>();

	private HashMap<String, String> getMultimediaFieldValues = new HashMap<String, String>();

	private MediaPlayer profile_player;

	private MediaPlayer audio_player;

	private int position = 0;

	private String type = null;

	private String strIPath = null;

	private boolean isrecording = false;

	private boolean isPlaying = false;

	private Bitmap bitmap, img = null;

	private Handler wservice_handler = null;

	private Button btn_reset = null;

	private SlideMenu slidemenu = null;

	private HashMap<String, FieldTemplateBean> fieldValuesMap = new HashMap<String, FieldTemplateBean>();

	private int CAMERA = 32;

	private int VIDEO = 33;

	private int AUDIO = 34;

	private SharedPreferences preferences;

	private Button btn_im = null;

	private Button btn_share = null;

	private boolean isProfileInServerSide;

	private HashMap<String, String> fileHmap = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			WebServiceReferences.contextTable.put("IM", this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			ShowList();
			setContentView(R.layout.buscard);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			context = this;
			WebServiceReferences.contextTable.put("buscard", this);
			btn_im = (Button) findViewById(R.id.sec_im);
			btn_im.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					callDisp.openReceivedIm(v, context);
				}
			});

			wservice_handler = new Handler();
			profile_player = new MediaPlayer();
			audio_player = new MediaPlayer();

			btn_back = (Button) findViewById(R.id.btn_Settings);
			btn_back.setOnClickListener(this);

			heading = (TextView) findViewById(R.id.heading);
			heading.setText(SingleInstance.mainContext.getResources().getString(R.string.my_profile));

			btn_reset = (Button) findViewById(R.id.reset);
			btn_reset.setVisibility(View.VISIBLE);
			btn_reset.setOnClickListener(this);

			btn_share = (Button) findViewById(R.id.share);
			btn_share.setVisibility(View.GONE);
			// btn_share.setOnClickListener(this);

			btn_save = (Button) findViewById(R.id.save);
			btn_save.setOnClickListener(this);

			CallDispatcher.pdialog = new ProgressDialog(context);

			if (fieldValuesMap.size() > 0)
				fieldValuesMap.clear();

			if (fileHmap.size() > 0)
				fileHmap.clear();

			fieldContainer = (LinearLayout) findViewById(R.id.fieldContainer);
			getProfileFields();
			loadProfileFieldValues();

			if (getMultimediaFieldValues.size() > 0)
				getMultimediaFieldValues.clear();

			getMultimediaFieldValues = callDisp
					.getdbHeler(context)
					.getMultimediaFieldValues(
							"SELECT fieldid,fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
									+ CallDispatcher.LoginUser + "'");
		} catch (Exception e) {
			Log.e("profile", "====> " + e.getMessage());
		}
	}

	public void loadProfileFieldValues() {
		if (profileFieldList.size() > 0) {
			if (fieldContainer != null)
				fieldContainer.removeAllViews();
			for (FieldTemplateBean fieldtemplate : profileFieldList) {
				inflateFields(0, fieldtemplate);
				Log.d("My Profile", "Field ID : " + fieldtemplate.getFieldId()
						+ ", Field Name : " + fieldtemplate.getFieldName()
						+ ", Type :" + fieldtemplate.getFieldType());

			}
		} else {
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setCancelable(true);
			callDisp.showprogress(dialog, context);
		}
	}

	/*
	 * private void playAudio(View v, final ImageView imageValue) { try { if (v
	 * != null) { String filepath = (String) v.getTag(); if (filepath != null) {
	 * if (!profile_player.isPlaying()) {
	 * 
	 * LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	 * 55, 55); imageValue.setBackgroundResource(R.drawable.v_stop);
	 * profile_player.setDataSource(Environment .getExternalStorageDirectory() +
	 * "/COMMedia/" + filepath); profile_player.prepare();
	 * profile_player.start(); }
	 * 
	 * } profile_player .setOnCompletionListener(new OnCompletionListener() {
	 * 
	 * @Override public void onCompletion(MediaPlayer player) { try { // TODO
	 * Auto-generated method stub player.reset(); imageValue
	 * .setBackgroundResource(R.drawable.v_play); } catch (Exception e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * } }); } } catch (Exception e) { e.printStackTrace(); } }
	 */

	protected void ShowList() {
		// TODO Auto-generated method stub

		setContentView(R.layout.history_container);

		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		ArrayList<Slidebean> datas = new ArrayList<Slidebean>();

		callDisp.composeList(datas);
		slidemenu.init(BusCard.this, datas, BusCard.this, 100);

	}

	/*
	 * public void notifyUploadStatus(final String filepath, final String
	 * req_object, final boolean status) { Log.d("Profile",
	 * "came to notifyUploadStatus" + wservice_handler);
	 * wservice_handler.post(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * Log.d("Profile", "Inside the handler..." + status); if (status) { String
	 * file = Environment.getExternalStorageDirectory() .getAbsolutePath() +
	 * "/COMMedia/" + filepath; if
	 * (CallDispatcher.uploading_map.containsKey(file))
	 * CallDispatcher.uploading_map.remove(file);
	 * 
	 * if (CallDispatcher.uploading_map.size() == 0) { if
	 * (getFieldValuesList.size() > 0) { String[] params = new String[2]; if
	 * (CallDispatcher.LoginUser != null) params[0] = CallDispatcher.LoginUser;
	 * else { SharedPreferences preferences = PreferenceManager
	 * .getDefaultSharedPreferences(getApplicationContext()); params[0] =
	 * preferences .getString("uname", null); } params[1] = "5";
	 * WebServiceReferences.webServiceClient
	 * .setStandardProfileFieldValues(params, getFieldValuesList); } } } else {
	 * String file = Environment.getExternalStorageDirectory()
	 * .getAbsolutePath() + "/COMMedia/" + filepath; if
	 * (CallDispatcher.uploading_map.containsKey(file))
	 * CallDispatcher.uploading_map.remove(file); if (req_object != null) {
	 * String[] info = req_object.split(","); for (int i = 0; i <
	 * getFieldValuesList.size(); i++) { FieldTemplateBean fieldTemplateBean =
	 * getFieldValuesList .get(i); if
	 * (fieldTemplateBean.getFieldId().equals(info[0])) {
	 * getFieldValuesList.remove(i); break; } } } if
	 * (CallDispatcher.uploading_map.size() == 0) { callDisp.cancelDialog(); } }
	 * } });
	 * 
	 * }
	 */

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			if (profile_player != null) {
				if (profile_player.isPlaying()) {
					profile_player.stop();
					profile_player.reset();
				}
				profile_player.release();
			}
			if (audio_player != null) {
				if (audio_player.isPlaying()) {
					audio_player.stop();
					audio_player.reset();
				}
				audio_player.release();
			}

			if (fieldValuesMap.size() > 0)
				fieldValuesMap.clear();

			if (getFieldValuesList.size() > 0)
				getFieldValuesList.clear();

			if (WebServiceReferences.contextTable.containsKey("buscard"))
				WebServiceReferences.contextTable.remove("buscard");

			super.onDestroy();
		} catch (Exception e) {
			Log.e("profile", "===> " + e.getMessage());
		}
	}

	public void getProfileFields() {
		try {
			profileFieldList = callDisp.getdbHeler(context).getProfileFields();
			Log.i("profile", "==========> " + profileFieldList.size());
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public void notifyFileDownloaded(final String fieldid,
			final String filename, final String ownername) {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("Profile", "Owner name  :" + ownername);
				Log.d("Profile", "login user name :" + CallDispatcher.LoginUser);
				if (ownername.equals(CallDispatcher.LoginUser)) {
					getProfileFields();
					loadProfileFieldValues();
				}
			}

		});

	}

	public void inflateFields(int mode, FieldTemplateBean bean) {
		try {
			View view = getLayoutInflater().inflate(
					R.layout.buscardadapter, fieldContainer, false);
			view.setTag(bean.getFieldId());
			TextView fieldName = (TextView) view.findViewById(R.id.fieldname);
			if (bean.getFieldName() != null)
				fieldName.setText(bean.getFieldName());

			if (bean.getFieldType().equalsIgnoreCase("photo")
					|| bean.getFieldType().equalsIgnoreCase("video")
					|| bean.getFieldType().equalsIgnoreCase("audio")) {
				Button options = (Button) view.findViewById(R.id.options);
				options.setTag(fieldContainer.getChildCount());
				options.setContentDescription(bean.getFieldType());
				fieldValuesMap.put(bean.getFieldName(), bean);
				options.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!isrecording && !isPlaying
								&& !profile_player.isPlaying()) {
							int pos = (Integer) v.getTag();
							Log.d("Profile", "On click of option menu");
							showMultimediaOptions(pos, v
									.getContentDescription().toString());
						} else {
							if (isrecording)
								showToast("Please Stop existing recording");
							else if (isPlaying || profile_player.isPlaying())
								showToast("Kindly stop the audio player");
						}
					}
				});

				final ImageView imageValue = (ImageView) view
						.findViewById(R.id.imageValue);
				imageValue.setPadding(10, 10, 10, 10);
				imageValue.setTag(bean.getFiledvalue());
				imageValue.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String filename = (String) v.getTag();
						Log.d("Profile", "My File name---->" + filename);
						if (filename != null) {
							filename = callDisp.containsLocalPath(filename);
							if (filename.contains("MPD_")) {
								if (!isrecording && !isPlaying
										&& !profile_player.isPlaying()) {
									Intent in = new Intent(context,
											PhotoZoomActivity.class);
									in.putExtra("Photo_path", filename);
									in.putExtra("type", false);
									in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(in);
								} else {
									if (isrecording)
										showToast("Please Stop existing recording");
									else if (isPlaying
											|| profile_player.isPlaying())
										showToast("Kindly stop the audio player");

								}
							} else if (filename.contains("MVD_")) {
								if (!isrecording && !isPlaying
										&& !profile_player.isPlaying()) {
									if (!filename.endsWith(".mp4"))
										filename = filename + ".mp4";
									Intent intentVPlayer = new Intent(context,
											VideoPlayer.class);
//									intentVPlayer.putExtra("File_Path",
//											filename);
//									intentVPlayer.putExtra("Player_Type",
//											"Video Player");
									
									intentVPlayer.putExtra("video", filename);
									startActivity(intentVPlayer);
								} else {

									if (isrecording)
										showToast("Please Stop existing recording");
									else if (isPlaying
											|| profile_player.isPlaying())
										showToast("Kindly stop the audio player");

								}
							} else if (filename.contains("MAD_")) {

								if (!isrecording && !isPlaying) {
									if (new File(filename).exists()) {
										Intent intent = new Intent(context,
												MultimediaUtils.class);
										intent.putExtra("filePath", filename);
										intent.putExtra("requestCode", AUDIO);
										intent.putExtra("action", "audio");
										intent.putExtra("createOrOpen", "open");
										startActivity(intent);
									}
								} else if (isrecording)
									showToast("Kinly stop the recording");
								else if (isPlaying)
									showToast("Kindly stop Audio player");

							}

						}
					}
				});

				options.setVisibility(View.VISIBLE);
				imageValue.setVisibility(View.VISIBLE);
				imageValue.setTag(bean.getFiledvalue());
				if (mode == 0) {
					if (bean.getFiledvalue() != null) {
						if (bean.getFiledvalue().contains("MPD_")) {
							imageValue.setImageBitmap(callDisp
									.setProfilePicture(bean.getFiledvalue(),
											R.drawable.broken));
						} else if (bean.getFiledvalue().contains("MAD_")
								|| bean.getFiledvalue().contains("MVD_")) {
							imageValue.setImageBitmap(BitmapFactory
									.decodeResource(getResources(),
											R.drawable.v_play));
						}
						// addFieldValues.add(bean.getFiledvalue());
					}
				}
			} else {

				final EditText fieldValue = (EditText) view
						.findViewById(R.id.fieldvalue);
				fieldValue.setVisibility(View.VISIBLE);
				fieldValue.setTag(bean.getFieldType());
				fieldValue.setContentDescription(bean.getFieldId());
				fieldValuesMap.put(bean.getFieldName(), bean);
				fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				if (bean.getFieldType() != null) {
					if (bean.getFieldName().equalsIgnoreCase("Birth Day")) {
						fieldValue.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									changeType(fieldValue);
									return true;
								} else {
									return false;
								}
							}
						});

					} else if (bean.getFieldType().equalsIgnoreCase("number"))
						fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
					else if (bean.getFieldType().equalsIgnoreCase("email"))
						fieldValue
								.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				}

				if (bean.getFiledvalue() != null) {
					fieldValue.setText(bean.getFiledvalue());
					// if (bean.getFiledvalue().length() > 0) {
					// addFieldValues.add(bean.getFiledvalue());
					// }
				}
			}

			fieldContainer.addView(view);
			callDisp.cancelDialog();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	private void showMultimediaOptions(final int tag, String rtype) {
		try {
			if (rtype != null) {
				position = tag;
				Log.d("clone", "----came to showresponse dialog---- tag = "
						+ tag + " rtype = " + rtype);
				if (rtype.equalsIgnoreCase("photo")) {
					if (!isrecording && !isPlaying
							&& !profile_player.isPlaying()) {
						position = tag;
						type = "photo";
						photochat();
					} else {

						if (isrecording)
							showToast("Please Stop existing recording");
						else if (isPlaying || profile_player.isPlaying())
							showToast("Kindly stop the audio player");

					}
				} else if (rtype.equalsIgnoreCase("audio")) {
					if (!isrecording && !isPlaying
							&& !profile_player.isPlaying()) {
						position = tag;
						type = "audio";
						// recordresponseAudio(tag);
						strIPath = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + "MAD_"
								+ callDisp.getFileName() + ".mp4";

						fileHmap.put("Audio", strIPath);
						Intent intent = new Intent(context,
								MultimediaUtils.class);
						intent.putExtra("filePath", strIPath);
						intent.putExtra("requestCode", AUDIO);
						intent.putExtra("action", "audio");
						intent.putExtra("createOrOpen", "create");
						startActivity(intent);
					} else {

						if (isrecording)
							showToast("Please Stop existing recording");
						else if (isPlaying || profile_player.isPlaying())
							showToast("Kindly stop the audio player");

					}
				} else if (rtype.equalsIgnoreCase("video")) {
					if (!isrecording && !isPlaying
							&& !profile_player.isPlaying()) {
						type = "video";
						position = tag;
						type = "video";
						strIPath = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + "MVD_"
								+ callDisp.getFileName() + ".mp4";

						// Intent intent = new Intent(context,
						// CamsampleActivity.class);
						// intent.putExtra("video_name",
						// strIPath.subSequence(0, strIPath.length() - 4));
						// intent.putExtra("auto", false);
						// startActivityForResult(intent, 33);
						Intent intent = new Intent(context,
								MultimediaUtils.class);
						intent.putExtra("filePath", strIPath);
						intent.putExtra("requestCode", VIDEO);
						intent.putExtra("action",
								MediaStore.ACTION_VIDEO_CAPTURE);
						intent.putExtra("createOrOpen", "create");
						startActivity(intent);

					} else {
						if (isrecording)
							showToast("Please Stop existing recording");
						else if (isPlaying || profile_player.isPlaying())
							showToast("Kindly stop the audio player");
					}
				} else if (rtype.equalsIgnoreCase("multimedia")) {
					if (!isrecording && !isPlaying
							&& !profile_player.isPlaying()) {
						type = "multimedia";
						position = tag;
						showBlobSelector();
					}

					else {
						if (isrecording)
							showToast("Please Stop existing recording");
						else if (isPlaying || profile_player.isPlaying())
							showToast("Kindly stop the audio player");
					}
				}
			}
		} catch (Exception e) {
			Log.e("profile", "==> " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void photochat() {
		try {
			final String[] items = new String[] { "Gallery", "Photo" };
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0)
						multimediaType(items[0]);
					else if (which == 1)
						multimediaType(items[1]);
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
					// Intent i = new Intent(context, Custom_Camara.class);
					// strIPath = Environment.getExternalStorageDirectory()
					// + "/COMMedia/MPD_" + callDisp.getFileName()
					// + ".jpg";
					// i.putExtra("Image_Name", strIPath);
					// Log.d("File Path", strIPath);
					// startActivityForResult(i, 32);

					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/MPD_" + callDisp.getFileName()
							+ ".jpg";
					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", strIPath);
					intent.putExtra("requestCode", CAMERA);
					intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra("createOrOpen", "create");
					startActivity(intent);

				} else {
					showToast("InSufficient Memory...");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("clone", "=======>" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void showBlobSelector() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			builder.setTitle("Multimedia Options");

			final CharSequence[] choiceList = { "From Gallery", "From Camera",
					"Audio", "Video" };
			int selected = -1;
			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int index) {

							OpenBlobInterface(index);
							dialog.cancel();
						}
					});
			builder.show();
		} catch (Exception e) {
			Log.e("profile", "===> " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void OpenBlobInterface(int selected_option) {
		Log.i("thread", "came to open blob interface");
		try {
			switch (selected_option) {
			case 0:
				if (!isrecording && !isPlaying) {
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
				} else {
					if (isrecording)
						showToast("Please Stop existing recording");
					else if (isPlaying)
						showToast("Kindly stop the audio player");
				}
				break;
			case 1:
				Log.i("clone", "====> inside photo");
				Long free_size = callDisp.getExternalMemorySize();
				Log.d("IM", free_size.toString());
				if (!isrecording && !isPlaying) {
					if (free_size > 0 && free_size >= 5120) {
						strIPath = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + "MPD_"
								+ callDisp.getFileName() + ".jpg";
						Intent intent = new Intent(context,
								MultimediaUtils.class);
						intent.putExtra("filePath", strIPath);
						intent.putExtra("requestCode", CAMERA);
						intent.putExtra("action",
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra("createOrOpen", "create");
						startActivity(intent);
					} else {
						showToast("InSufficient Memory...");
					}
				} else {
					if (isrecording)
						showToast("Please Stop existing recording");
					else if (isPlaying)
						showToast("Kindly stop the audio player");
				}
				break;
			case 2:
				if (!profile_player.isPlaying()) {
					// recordresponseAudio(position);
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + "MAD_" + callDisp.getFileName()
							+ ".mp4";
					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", strIPath);
					intent.putExtra("requestCode", AUDIO);
					intent.putExtra("action", "audio");
					intent.putExtra("createOrOpen", "create");
					startActivity(intent);
				} else
					showToast("Kindly Stop the audio player");

				break;
			case 3:
				Log.i("thread", "case 3");
				if (!isrecording && !isPlaying) {
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + "MVD_" + callDisp.getFileName()
							+ ".mp4";
					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", strIPath);
					intent.putExtra("requestCode", VIDEO);
					intent.putExtra("action", MediaStore.ACTION_VIDEO_CAPTURE);
					intent.putExtra("createOrOpen", "create");
					startActivity(intent);
				} else {
					if (isrecording)
						showToast("Please Stop existing recording");
					else if (isPlaying)
						showToast("Kindly stop the audio player");
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.e("profile", "===> " + e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * private void recordresponseAudio(int tag) { try { View view =
	 * getLayoutInflater().inflate(R.layout.audio_recording, fieldContainer,
	 * false); LinearLayout rec_container = (LinearLayout) view
	 * .findViewById(R.id.audio_rec_container); rec_container.setTag(tag);
	 * ImageButton btn_startrec = (ImageButton) view
	 * .findViewById(R.id.rec_start); btn_startrec.setTag(0); ProgressBar
	 * progress = (ProgressBar) view .findViewById(R.id.progressBar_dwn);
	 * progress.setVisibility(View.GONE); LinearLayout player_container =
	 * (LinearLayout) view .findViewById(R.id.audio_player_container);
	 * player_container.setTag(tag); final ImageView btn_play = (ImageView) view
	 * .findViewById(R.id.play_start); btn_play.setTag(0);
	 * 
	 * rec_container.setVisibility(View.VISIBLE);
	 * player_container.setVisibility(View.GONE);
	 * 
	 * LinearLayout field_settings = (LinearLayout) fieldContainer
	 * .getChildAt(tag); LinearLayout audio_layout = (LinearLayout)
	 * field_settings .findViewById(R.id.audio_layout); ImageView iv_play =
	 * (ImageView) field_settings .findViewById(R.id.imageValue);
	 * iv_play.setVisibility(View.GONE); audio_layout.removeAllViews();
	 * audio_layout.addView(view); audio_layout.setVisibility(View.VISIBLE);
	 * 
	 * btn_startrec.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { int tag = (Integer) v.getTag();
	 * 
	 * try { // TODO Auto-generated method stub
	 * 
	 * LinearLayout parent_layot = (LinearLayout) v .getParent(); ProgressBar
	 * progress = (ProgressBar) parent_layot .getChildAt(1); if (tag == 0) { if
	 * (!isrecording && !isPlaying) { media_recorder = new MediaRecorder();
	 * 
	 * int p_tag = (Integer) parent_layot.getTag(); ImageButton recorder_button
	 * = (ImageButton) v; recorder_button.setTag(1); recorder_button
	 * .setBackgroundResource(R.drawable.stop); media_recorder
	 * .setAudioSource(MediaRecorder.AudioSource.MIC); media_recorder
	 * .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); media_recorder
	 * .setAudioEncoder(MediaRecorder.AudioEncoder.AAC); String audio_path =
	 * Environment .getExternalStorageDirectory() + "/COMMedia/" + "MAD_" +
	 * callDisp.getFileName() + ".mp4"; fileHmap.put(p_tag, audio_path);
	 * media_recorder.setOutputFile(audio_path); media_recorder.prepare();
	 * media_recorder.start(); progress.setVisibility(View.VISIBLE); isrecording
	 * = true; recordAudioMap.put(tag, true);
	 * 
	 * } else { if (isrecording) showToast("Please Stop existing recording");
	 * else if (isPlaying) showToast("Kindly stop the audio player"); } } else {
	 * v.setTag(0); media_recorder.stop(); media_recorder.reset();
	 * media_recorder.release(); media_recorder = null;
	 * progress.setVisibility(View.GONE); isrecording = false; LinearLayout lay
	 * = (LinearLayout) parent_layot .getParent(); LinearLayout l_child =
	 * (LinearLayout) lay .getChildAt(1); l_child.setVisibility(View.VISIBLE);
	 * parent_layot.setVisibility(View.GONE);
	 * 
	 * } } catch (IllegalStateException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * } });
	 * 
	 * btn_play.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * try { // TODO Auto-generated method stub LinearLayout parent_layot =
	 * (LinearLayout) v .getParent(); int p_tag = (Integer)
	 * parent_layot.getTag(); int view_tag = (Integer) v.getTag(); if (view_tag
	 * == 0) { if (!isrecording && !isPlaying && !profile_player.isPlaying()) {
	 * 
	 * if (fileHmap.containsKey(p_tag)) {
	 * 
	 * btn_play.setBackgroundResource(R.drawable.v_stop); btn_play.setTag(1);
	 * String path = fileHmap.get(p_tag); audio_player.reset();
	 * audio_player.setDataSource(path); audio_player.setLooping(false);
	 * audio_player.prepare(); audio_player.start(); isPlaying = true;
	 * 
	 * } else Log.d("LM", "----path not available-----");
	 * 
	 * } else { if (isrecording) showToast("Please Stop existing recording");
	 * else if (isPlaying) showToast("Kindly stop the audio player"); } } else {
	 * btn_play.setBackgroundResource(R.drawable.v_play); btn_play.setTag(0);
	 * isPlaying = false; audio_player.stop(); audio_player.reset();
	 * 
	 * } } catch (IllegalArgumentException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } catch (SecurityException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (IllegalStateException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * }); audio_player.setOnCompletionListener(new OnCompletionListener() {
	 * 
	 * @Override public void onCompletion(MediaPlayer player) { // TODO
	 * Auto-generated method stub player.reset(); if ((Integer)
	 * btn_play.getTag() == 1) {
	 * btn_play.setBackgroundResource(R.drawable.v_play); } btn_play.setTag(0);
	 * isPlaying = false; } }); } catch (Exception e) { Log.e("profile", "===> "
	 * + e.getMessage()); e.printStackTrace(); } }
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode >= 30) {
				LinearLayout parent_view1 = (LinearLayout) fieldContainer
						.getChildAt(position);
				final ImageView value_img;

				value_img = (ImageView) parent_view1
						.findViewById(R.id.imageValue);

				if (requestCode == 30) {

					if (resultCode == Activity.RESULT_CANCELED) {

						value_img.setVisibility(View.GONE);

					} else {
						Uri selectedImageUri = data.getData();
						strIPath = callDisp
								.getRealPathFromURI(selectedImageUri);
						File selected_file = new File(strIPath);
						int length = (int) selected_file.length() / 1048576;
						Log.d("busy", "........ size is------------->" + length);

						if (length <= 2) {

							if (img != null) {

							}
							img = null;
							img = callDisp.ResizeImage(strIPath);
							img = Bitmap
									.createScaledBitmap(img, 100, 75, false);

							if (img != null) {
								bitmap = null;

								bitmap = callDisp.ResizeImage(strIPath);
								String path = Environment
										.getExternalStorageDirectory()
										+ "/COMMedia/"
										+ "MPD_"
										+ callDisp.getFileName() + ".jpg";

								BufferedOutputStream stream;
								try {
									stream = new BufferedOutputStream(
											new FileOutputStream(new File(path)));
									bitmap.compress(CompressFormat.JPEG, 100,
											stream);
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (bitmap != null) {
									bitmap = Bitmap.createScaledBitmap(bitmap,
											200, 150, false);
								}
								fileHmap.put("Photo", path);

								if (parent_view1 != null) {

									if (bitmap != null) {
										value_img.setImageBitmap(bitmap);
										value_img.setVisibility(View.VISIBLE);
										value_img.setTag(new File(path)
												.getName());
										value_img.setPadding(10, 10, 10, 10);
									}
								}
							}
						}

					}

				} else if (requestCode == 31) {

					if (resultCode == Activity.RESULT_CANCELED) {

						value_img.setVisibility(View.GONE);

					} else {

						Uri selectedImageUri = data.getData();
						final int takeFlags = data.getFlags()
								& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
						getContentResolver().takePersistableUriPermission(
								selectedImageUri, takeFlags);
						strIPath = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + "MPD_"
								+ callDisp.getFileName() + ".jpg";
						fileHmap.put("Photo", strIPath);
						File selected_file = new File(strIPath);
						int length = (int) selected_file.length() / 1048576;
						Log.d("busy", "........ size is------------->" + length);
						if (length <= 2) {
							img = null;
							new bitmaploader().execute(selectedImageUri);
						} else {
							showToast("Kindly Select someother image,this image is too large");
						}

					}
				} else if (requestCode == CAMERA) {

					if (resultCode == Activity.RESULT_CANCELED) {
						// value_img.setVisibility(View.GONE);

					} else {
						File fileCheck = new File(strIPath);
						if (fileCheck.exists()) {
							if (bitmap != null && !bitmap.isRecycled())
								bitmap.recycle();
							System.gc();
							bitmap = callDisp.ResizeImage(strIPath);
							if (bitmap != null) {
								callDisp.changemyPictureOrientation(bitmap,
										strIPath);
								if (bitmap != null && !bitmap.isRecycled())
									bitmap.recycle();
								bitmap = null;
								bitmap = callDisp.ResizeImage(strIPath);
								if (bitmap != null)
									bitmap = Bitmap.createScaledBitmap(bitmap,
											200, 150, false);

								fileHmap.put("Photo", strIPath);

								if (parent_view1 != null) {

									if (bitmap != null) {
										value_img.setVisibility(View.VISIBLE);
										value_img.setImageBitmap(bitmap);
										value_img.setTag(new File(strIPath)
												.getName());
										value_img.setPadding(10, 10, 10, 10);
									}
								}
							} else
								Toast.makeText(context,
										"Can not save this picture",
										Toast.LENGTH_LONG).show();
						}
					}

				} else if (requestCode == VIDEO) {
					if (resultCode == Activity.RESULT_CANCELED) {

					} else {
						if (strIPath != null) {
							fileHmap.put("Video", strIPath);

							if (parent_view1 != null) {

								if (type.equalsIgnoreCase("video")
										|| type.equalsIgnoreCase("multimedia")) {
									bitmap = BitmapFactory.decodeResource(
											getResources(), R.drawable.v_play);

									value_img.setImageBitmap(bitmap);
									value_img.setVisibility(View.VISIBLE);

									Log.i("bug", "inside video for profile"
											+ value_img);
									if (!strIPath.endsWith(".mp4")) {
										strIPath = strIPath + ".mp4";
									}

									value_img.setTag(strIPath);
									final File vfileCheck = new File(strIPath);
									value_img
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													// TODO Auto-generated
													// method
													// stub
													if (!isrecording
															&& !isPlaying
															&& !profile_player
																	.isPlaying()) {
														if (vfileCheck.exists()) {

															Intent intentVPlayer = new Intent(
																	context,
																	VideoPlayer.class);
//															intentVPlayer
//																	.putExtra(
//																			"File_Path",
//																			value_img
//																					.getTag()
//																					.toString());
//															intentVPlayer
//																	.putExtra(
//																			"Player_Type",
//																			"Video Player");
															intentVPlayer.putExtra("video", value_img
																					.getTag()
																					.toString());
															startActivity(intentVPlayer);
														}

													}
												}
											});
								}
							}
						}
					}
				} else if (type.equalsIgnoreCase("audio")
						|| (requestCode == 34)) {

					fileHmap.put("Audio", strIPath);

					if (parent_view1 != null) {

						if (type.equalsIgnoreCase("audio")
								|| (requestCode == 34)) {
							bitmap = BitmapFactory.decodeResource(
									getResources(), R.drawable.v_play);

							value_img.setImageBitmap(bitmap);
							value_img.setVisibility(View.VISIBLE);
							value_img.setTag(strIPath);
							final File vfileCheck = new File(strIPath);
							value_img.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated
									// method
									// stub
									if (!isrecording && !isPlaying) {
										if (vfileCheck.exists()) {
											Intent intent = new Intent(context,
													MultimediaUtils.class);
											intent.putExtra("filePath", v
													.getTag().toString());
											intent.putExtra("requestCode",
													AUDIO);
											intent.putExtra("action", "audio");
											intent.putExtra("createOrOpen",
													"open");
											startActivity(intent);
										}
									}
								}
							});
						}
					}

				}
			}
		} catch (Exception e) {
			Log.e("profile", "====> " + e.getMessage());
			e.printStackTrace();
		}

		if (WebServiceReferences.contextTable.containsKey("multimediautils"))
			((MultimediaUtils) WebServiceReferences.contextTable
					.get("multimediautils")).finish();
	}

	public void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			switch (v.getId()) {
			case R.id.btn_Settings:
				slidemenu.show();
				break;
			case R.id.save:
				saveorUpdateFields();
				break;
			case R.id.reset:
				showDeleteAlert("5");
				/*
				 * if (addFieldValues.size() > 0) { showDeleteAlert("5"); } else
				 * { profileFieldList.clear(); profileFieldList =
				 * callDisp.getdbHeler(context) .getProfileFields();
				 * fieldContainer.removeAllViews(); if (profileFieldList.size()
				 * > 0) { if (fieldContainer != null)
				 * fieldContainer.removeAllViews(); for (FieldTemplateBean
				 * fieldtemplate : profileFieldList) { inflateFields(0,
				 * fieldtemplate); } }
				 * 
				 * }
				 */
				// finish();
				break;
			case R.id.share:
				StringBuffer buffer = new StringBuffer();
				if (profileFieldList.size() > 0) {
					for (FieldTemplateBean fieldtemplate : profileFieldList) {
						if (fieldtemplate.getFiledvalue() != null
								&& fieldtemplate.getFiledvalue().length() > 0
								&& !fieldtemplate.getFieldType()
										.equalsIgnoreCase("multimedia")
								&& !fieldtemplate.getFieldType()
										.equalsIgnoreCase("photo")
								&& !fieldtemplate.getFieldType()
										.equalsIgnoreCase("video")
								&& !fieldtemplate.getFieldType()
										.equalsIgnoreCase("audio")) {
							String fieldname = fieldtemplate.getFieldName();
							String fieldValue = fieldtemplate.getFiledvalue();
							String text = fieldname + " : " + fieldValue;
							buffer.append(text);
							buffer.append("\n\n");
						}

					}
				}
				if (buffer.toString().length() > 0) {
					Intent shareIntent = new Intent(context,
							InviteProfile.class);
					shareIntent.putExtra("profilevalues", buffer.toString());
					startActivity(shareIntent);
				} else
					showToast("No profile text values to share");

			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("profile", "====> " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void saveorUpdateFields() {
		try {
			Vector<FieldTemplateBean> fields_list = new Vector<FieldTemplateBean>();

			for (int i = 0; i < fieldContainer.getChildCount(); i++) {
				LinearLayout child_view = (LinearLayout) fieldContainer
						.getChildAt(i);

				FieldTemplateBean bean = profileFieldList.get(i);

				if (bean != null) {
					if (bean.getFieldType().equalsIgnoreCase("photo")
							|| bean.getFieldType().equalsIgnoreCase("audio")
							|| bean.getFieldType().equalsIgnoreCase("video")) {
						String type = bean.getFieldType();
						if (fileHmap.containsKey(type)) {

							FieldTemplateBean fieldTemplateBean = new FieldTemplateBean();
							fieldTemplateBean.setFieldId(bean.getFieldId());
							fieldTemplateBean.setFieldName(bean.getFieldName());
							fieldTemplateBean.setFieldType(bean.getFieldType());
							fieldTemplateBean.setFiledvalue(new File(fileHmap
									.get(type)).getName());
							fields_list.add(fieldTemplateBean);
						}
					} else {
						EditText ed_fieldvalue = (EditText) child_view
								.findViewById(R.id.fieldvalue);
						if (ed_fieldvalue.getText().toString().trim().length() != 0) {
							FieldTemplateBean fieldTemplateBean = new FieldTemplateBean();
							fieldTemplateBean.setFieldId(bean.getFieldId());

							fieldTemplateBean.setFieldName(bean.getFieldName());

							fieldTemplateBean.setFieldType(bean.getFieldType());

							if (ed_fieldvalue.getText().toString().trim()
									.length() > 0) {
								fieldTemplateBean.setFiledvalue(ed_fieldvalue
										.getText().toString().trim());
								fields_list.add(fieldTemplateBean);
							}
						}
					}
				}
			}

			String[] params = new String[2];
			if (CallDispatcher.LoginUser != null)
				params[0] = CallDispatcher.LoginUser;
			else {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				params[0] = preferences.getString("uname", null);
			}
			params[1] = "5";
			if (getFieldValuesList.size() > 0)
				getFieldValuesList.clear();

			boolean isUpdateNow = true;

			Vector<FieldTemplateBean> fieldValuesList = new Vector<FieldTemplateBean>();

			for (FieldTemplateBean fieldTemplate : fields_list) {
				FieldTemplateBean fBean = fieldValuesMap.get(fieldTemplate
						.getFieldName());
				Log.i("profile123", "fieldName : " + fBean.getFieldName()
						+ "fieldValue : " + fBean.getFiledvalue());
				if (fBean.getFiledvalue() == null) {
					fBean.setFiledvalue("");
				}
				if (!fBean.getFiledvalue().equalsIgnoreCase(
						fieldTemplate.getFiledvalue())) {
					getFieldValuesList.add(fieldTemplate);
					if (fBean.getFieldType().equalsIgnoreCase("photo")
							|| fBean.getFieldType().equalsIgnoreCase("audio")
							|| fBean.getFieldType().equalsIgnoreCase("video")) {
						Log.i("profile123",
								"fieldName : " + fBean.getFieldName()
										+ "fieldValue : "
										+ fBean.getFiledvalue());
						fBean.setUploaddownloadStatus(1);
						uploadProfileFiles(fileHmap.get(fBean.getFieldType()),
								fBean, "profile uploading");
						isUpdateNow = false;
						String fileName = getMultimediaFieldValues.get(fBean
								.getFieldId());
						deleteOldFile(fileName);

					} else
						fieldValuesList.add(fieldTemplate);
					fieldTemplate.setCreatedDate("");
					fieldTemplate.setModifiedDate("");
					callDisp.getdbHeler(context).saveOrUpdateProfileField(
							fieldTemplate);
				}
			}

			if (isUpdateNow) {
				WebServiceReferences.webServiceClient
						.setStandardProfileFieldValues(params,
								getFieldValuesList);
				fieldValuesList = null;
			}

			if (fieldValuesList != null) {
				WebServiceReferences.webServiceClient
						.setStandardProfileFieldValues(params, fieldValuesList);
			}

			if (getFieldValuesList.size() == 0) {
				showToast("Sorry, you didn't update any fields");
			} else {
				notifyPositiveResponse();
			}

			/*
			 * if (CallDispatcher.uploading_map.size() == 0 &&
			 * getFieldValuesList.size() > 0) {
			 * WebServiceReferences.webServiceClient
			 * .setStandardProfileFieldValues(params, getFieldValuesList); }
			 * else if (CallDispatcher.uploading_map.size() == 0 &&
			 * getFieldValuesList.size() == 0) { callDisp.cancelDialog(); //
			 * showToast("Sorry, you didn't update any fields");
			 * callDisp.showAlert("Alert", "Fill Some Field"); }
			 */
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	private void deleteOldFile(String fileName) {
		if (fileName != null) {
			String filePath = callDisp.containsLocalPath(fileName);
			File file = new File(filePath);
			if (file.exists())
				file.delete();
		}
	}

	private void uploadProfileFiles(String path, FieldTemplateBean field_info,
			String from) {
		AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
				.get("MAIN");
		if (path != null) {
			if (CallDispatcher.LoginUser != null) {
				String username = preferences.getString("ftpusername", null);
				String password = preferences.getString("ftppassword", null);
				if (username != null && password != null) {
					FTPBean ftpBean = new FTPBean();
					ftpBean.setServer_ip(appMainActivity.cBean.getRouter()
							.split(":")[0]);
					ftpBean.setServer_port(40400);
					ftpBean.setFtp_username(username);
					ftpBean.setFtp_password(password);
					if (path.contains("MVD_")) {
						if (path.contains(".mp4"))
							ftpBean.setFile_path(path);
						else
							ftpBean.setFile_path(path + ".mp4");
					} else if (path.contains("MAD_"))
						ftpBean.setFile_path(path);
					else
						ftpBean.setFile_path(path);
					ftpBean.setOperation_type(1);
					ftpBean.setReq_object(field_info);
					ftpBean.setRequest_from(from);

					if (appMainActivity.getFtpNotifier() != null)
						appMainActivity.getFtpNotifier().addTasktoExecutor(ftpBean);
				}
			}
		}
	}

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			try {
				super.onPostExecute(result);
				Log.d("image", "came to post execute for image");
				callDisp.cancelDialog();
				if (strIPath != null)
					img = callDisp.ResizeImage(strIPath);
				if (img != null) {

					File fle = new File(strIPath);
					fle.createNewFile();
					FileOutputStream fout = new FileOutputStream(strIPath);
					img.compress(CompressFormat.JPEG, 75, fout);

					img = Bitmap.createScaledBitmap(img, 100, 75, false);
					Log.d("OnActivity", "_____On Activity Called______");

					bitmap = null;

					bitmap = callDisp.ResizeImage(strIPath);
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);
					LinearLayout parent_view = (LinearLayout) fieldContainer
							.getChildAt(position);
					ImageView value_img;

					if (parent_view != null) {
						value_img = (ImageView) parent_view
								.findViewById(R.id.imageValue);
						if (bitmap != null) {
							value_img.setVisibility(View.VISIBLE);
							value_img.setImageBitmap(bitmap);
							value_img.setPadding(10, 10, 10, 10);

						}
					}
				} else {
					strIPath = null;
				}

			} catch (Exception e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
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
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
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
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}

			return null;
		}

	}

	private void showDeleteAlert(final String profileid) {
		try {
			AlertDialog.Builder buider = new AlertDialog.Builder(context);
			buider.setMessage("Are you sure, You want to Delete this Profile ?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									boolean delete = callDisp.getdbHeler(
											context).deleteProfile(
											CallDispatcher.LoginUser);

									if (delete) {
										profileFieldList.clear();
										fieldContainer.removeAllViews();
										profileFieldList = callDisp.getdbHeler(
												context).getProfileFields();
										if (profileFieldList.size() > 0) {
											for (FieldTemplateBean fieldtemplate : profileFieldList) {
												inflateFields(0, fieldtemplate);
											}
										}
									}
									isProfileInServerSide = true;
									if (!WebServiceReferences.running)
										callDisp.startWebService(
												getResources().getString(
														R.string.service_url),
												"80");

									WebServiceReferences.webServiceClient
											.DeleteProfile(
													CallDispatcher.LoginUser,
													profileid);
									// addFieldValues.clear();

								}

							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
			AlertDialog alert = buider.create();
			alert.show();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public void notifyWebServiceDelete(final Object obj) {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (obj instanceof String[]) {
						// String[] responses = (String[]) obj;
						//
						// if (responses[0].equalsIgnoreCase("1")) {
						// String profileid = responses[2];
						// deleteProfile(profileid);
						// }
						showToast("Profile deleted successfully.");
						if (isProfileInServerSide)
							isProfileInServerSide = false;

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;
						showAlert(service_bean.getText());
					}
				}

			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public void clearAllViews() {
		if (fieldContainer != null)
			fieldContainer.removeAllViews();
	}

	/*
	 * private void deleteProfile(String profileid) { try { // TODO
	 * Auto-generated method stub
	 * 
	 * Log.i("UDPPROF", "inside doProfileDelete in calldispatcher"); boolean
	 * delete = callDisp.getdbHeler(context).deleteProfile(
	 * CallDispatcher.LoginUser); if
	 * callDisp.cancelDialog(); }
	 * 
	 * if (delete) { profileFieldList.clear(); profileFieldList =
	 * callDisp.getdbHeler(context) .getProfileFields();
	 * fieldContainer.removeAllViews(); if (profileFieldList.size() > 0) { if
	 * (fieldContainer != null) fieldContainer.removeAllViews(); for
	 * (FieldTemplateBean fieldtemplate : profileFieldList) { inflateFields(0,
	 * fieldtemplate); } } } // finish(); } catch (Exception e) { if
	 * (AppReference.isWriteInFile) AppReference.logger.error(e.getMessage(),
	 * e); else e.printStackTrace(); }
	 * 
	 * }
	 */

	public void showAlert(String message) {
		try {
			final AlertDialog alertDialog = new AlertDialog.Builder(
					BusCard.this).create();

			alertDialog.setTitle("Response");

			alertDialog.setMessage(message);

			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					alertDialog.dismiss();

				}
			});

			alertDialog.show();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	private void changeType(final EditText field) {

		try {
			Button btnSet;
			final DatePicker dp;
			final TimePicker tp;
			final AlertDialog alertReminder = new AlertDialog.Builder(context)
					.create();

			ScrollView tblDTPicker = (ScrollView) View.inflate(context,
					R.layout.date_time_picker, null);

			btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
			dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
			tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);
			tp.setVisibility(View.GONE);

			btnSet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String DOB = dp.getYear()
							+ "-"
							+ WebServiceReferences.setLength2((dp.getMonth() + 1))
							+ "-"
							+ WebServiceReferences.setLength2(dp
									.getDayOfMonth());
					field.setText(DOB);
					alertReminder.dismiss();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String currentDateandTime = sdf.format(new Date());
					if (DOB.equals(currentDateandTime)) {
						showToast("Pls Enter Valid Details");
					}

				}
			});
			alertReminder.setTitle("Date");
			alertReminder.setView(tblDTPicker);
			alertReminder.show();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	/*
	 * public void notifyWebserviceResponse(String fieldId, String createdDate,
	 * String modifiedDate) { try { ContentValues cv = new ContentValues(); for
	 * (FieldTemplateBean fieldTemplateBean : getFieldValuesList) {
	 * Log.i("profile", "field value == > " +
	 * fieldTemplateBean.getFiledvalue()); if
	 * (fieldTemplateBean.getFieldId().equalsIgnoreCase(fieldId)) { if
	 * (fieldTemplateBean.getFiledvalue() != null &&
	 * !fieldTemplateBean.getFiledvalue() .equalsIgnoreCase("null") &&
	 * fieldTemplateBean.getFiledvalue().length() > 0) { cv.put("fieldid",
	 * fieldTemplateBean.getFieldId()); cv.put("userid",
	 * CallDispatcher.LoginUser); cv.put("fieldvalue",
	 * fieldTemplateBean.getFiledvalue()); cv.put("createddate", createdDate);
	 * cv.put("modifieddate", modifiedDate); cv.put("flag", 0); } if
	 * (!callDisp.getdbHeler(context).isRecordExists(
	 * "select * from profilefieldvalues where fieldid=" + fieldId +
	 * " and userid='" + CallDispatcher.LoginUser + "'")) { if
	 * (callDisp.getdbHeler(context) .insertProfileFieldValues(cv) > 0) { if
	 * (fieldTemplateBean.getFiledvalue() != null &&
	 * !fieldTemplateBean.getFiledvalue() .equalsIgnoreCase("null")) { if
	 * (fieldTemplateBean.getFieldType() .equalsIgnoreCase("photo") ||
	 * fieldTemplateBean.getFieldType() .equalsIgnoreCase("audio") ||
	 * fieldTemplateBean.getFieldType() .equalsIgnoreCase("video") ||
	 * fieldTemplateBean.getFieldType() .equalsIgnoreCase("multimedia")) { if
	 * (getMultimediaFieldValues .containsKey(fieldTemplateBean .getFieldId()))
	 * { String fieldValue = getMultimediaFieldValues .get(fieldTemplateBean
	 * .getFieldId()); if (!fieldValue .equalsIgnoreCase(fieldTemplateBean
	 * .getFiledvalue())) { File file = new File( Environment
	 * .getExternalStorageDirectory() .getAbsolutePath() + "/COMMedia/" +
	 * fieldValue);
	 * 
	 * if (file.exists()) file.delete();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } } } } else { cv.remove("fieldid"); cv.remove("userid"); if
	 * (fieldTemplateBean.getFiledvalue() != null &&
	 * !fieldTemplateBean.getFiledvalue() .equalsIgnoreCase("null")) if
	 * (callDisp.getdbHeler(context) .updateProfileFieldValues( cv, "fieldid=" +
	 * fieldId + " and userid='" + CallDispatcher.LoginUser + "'") > 0) { if
	 * (fieldTemplateBean.getFiledvalue() != null &&
	 * !fieldTemplateBean.getFiledvalue() .equalsIgnoreCase("null")) { if
	 * (fieldTemplateBean.getFieldType() .equalsIgnoreCase("photo") ||
	 * fieldTemplateBean.getFieldType() .equalsIgnoreCase("audio") ||
	 * fieldTemplateBean.getFieldType() .equalsIgnoreCase("video") ||
	 * fieldTemplateBean.getFieldType() .equalsIgnoreCase( "multimedia")) { if
	 * (getMultimediaFieldValues .containsKey(fieldTemplateBean .getFieldId()))
	 * { String fieldValue = getMultimediaFieldValues .get(fieldTemplateBean
	 * .getFieldId()); if (!fieldValue .equalsIgnoreCase(fieldTemplateBean
	 * .getFiledvalue())) { File file = new File( Environment
	 * .getExternalStorageDirectory() .getAbsolutePath() + "/COMMedia/" +
	 * fieldValue);
	 * 
	 * if (file.exists()) file.delete();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } } } } } } } catch (Exception e) { if (AppReference.isWriteInFile)
	 * AppReference.logger.error(e.getMessage(), e); else e.printStackTrace(); }
	 * }
	 */
	public void notifyPositiveResponse() {
		showToast("Saved sucessfully");
		Intent i = new Intent(context, ViewProfiles.class);
		i.putExtra("fromactivity", "buscard");
		i.putExtra("buddyname", CallDispatcher.LoginUser);
		startActivity(i);
		finish();
	}

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.USERPROFILE:

			break;
		case WebServiceReferences.UTILITY:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.NOTES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.APPS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.CLONE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.SETTINGS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;

		case WebServiceReferences.QUICK_ACTION:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.FORMS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.FEEDBACK:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.EXCHANGES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK))
			callDisp.showKeyDownAlert(context);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.Imcollection.size() == 0)
			btn_im.setVisibility(View.GONE);
		else
			btn_im.setVisibility(View.VISIBLE);
		super.onResume();
	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				btn_im.setVisibility(View.VISIBLE);
				btn_im.setEnabled(true);

				btn_im.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
					callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});

	}

}
