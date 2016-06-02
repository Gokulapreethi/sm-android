package com.main;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Vector;

import org.lib.model.FieldTemplateBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cg.DB.DBAccess;
import com.cg.account.ChangePassword;
import com.cg.account.GenerateInviteCode;
import com.cg.account.PinAndTouchId;
import com.cg.account.SecurityQuestions;
import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.CallConnectingScreen;
import com.cg.callservices.VideoCallScreen;
import com.cg.callservices.inCommingCallAlert;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.files.CompleteListView;
import com.cg.settings.About;
import com.cg.settings.UserSettingsBean;
import com.image.utils.ImageLoader;
import com.process.MemoryProcessor;
import com.util.SingleInstance;

public class SettingsFragment extends Fragment implements OnClickListener {
	int pos = 0;
	TextView tv_date;
	SharedPreferences sPreferences;
	public long mLastClickTime = 0;
	private static SettingsFragment settingsFragment;

	private static Context mainContext;
	public static String tonename;
	private Button changePicture = null;
	public static boolean autoplaystr;
	// private Button plus = null; // this button plus hide in this page,this
	// button create fragment xml

	private CallDispatcher calldisp = null;
	private Typeface tf_regular = null;

	private Typeface tf_bold = null;

	private String strIPath, uname;

	private Handler handler = new Handler();

	private ToggleButton btn_location, btn_rempass, btn_show_loc,
			btn_autologin, btn_autoplay, btn_sharetone, btn_automaticCall;

	private boolean rememberpassword;

	private boolean autologinstate;

	private TextView status1;

	private TextView status;

	private AlertDialog alert = null;

	private ImageView userIcon;

	private Bitmap bitmap;

	private Fragment fragment = this;

	private ImageView userimage;

	private AppMainActivity appMainActivity = null;

	public View view;

	private ImageLoader imageLoader;

	private RelativeLayout configureStore;

	private ImageView avatarlay;

	private ImageView getproflay;

	private ImageView FeedbackLay;

	private RelativeLayout selectLanguage;

	private TextView selectedLanguage;

	private UserSettingsBean sett_bean = null;
	private ProgressDialog progressDialog = null;
	public static String[] questions;

	public static SettingsFragment newInstance(Context context) {
		try {
			if (settingsFragment == null) {
				mainContext = context;
				settingsFragment = new SettingsFragment();

			}

			return settingsFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return settingsFragment;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			Button select = (Button) getActivity().findViewById(R.id.btn_brg);
			select.setVisibility(View.GONE);
			final RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
			mainHeader.setVisibility(View.VISIBLE);
			LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
			contact_layout.setVisibility(View.GONE);

			Button imVw = (Button) getActivity().findViewById(R.id.im_view);
			imVw.setVisibility(View.GONE);

			final Button search1 = (Button) getActivity().findViewById(R.id.btn_settings);
			search1.setVisibility(View.GONE);

			final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
			plusBtn.setVisibility(View.GONE);

			TextView title = (TextView) getActivity().findViewById(
					R.id.activity_main_content_title);
			title.setVisibility(View.VISIBLE);
			title.setText("SETTINGS");

			Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
			backBtn.setVisibility(View.GONE);
			imageLoader = new ImageLoader(mainContext);
			RelativeLayout audio_minimize = (RelativeLayout)getActivity().findViewById(R.id.audio_minimize);
			RelativeLayout video_minimize = (RelativeLayout)getActivity().findViewById(R.id.video_minimize);
			audio_minimize.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mainHeader.setVisibility(View.GONE);
					addShowHideListener(true);
				}
			});
			video_minimize.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mainHeader.setVisibility(View.GONE);
					addShowHideListener(false);
				}
			});
			ImageView min_incall=(ImageView)getActivity().findViewById(R.id.min_incall);
			ImageView min_outcall=(ImageView)getActivity().findViewById(R.id.min_outcall);
			min_incall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mainHeader.setVisibility(View.GONE);
					inCommingCallAlert incommingCallAlert = inCommingCallAlert.getInstance(SingleInstance.mainContext);
					FragmentManager fragmentManager = SingleInstance.mainContext
							.getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(
							R.id.activity_main_content_fragment, incommingCallAlert)
							.commitAllowingStateLoss();
				}
			});
			min_outcall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mainHeader.setVisibility(View.GONE);
					CallConnectingScreen callConnectingScreen = CallConnectingScreen.getInstance(SingleInstance.mainContext);
					FragmentManager fragmentManager = SingleInstance.mainContext
							.getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(
							R.id.activity_main_content_fragment, callConnectingScreen)
							.commitAllowingStateLoss();
				}
			});
			view = null;
			if (view == null) {
				view = inflater.inflate(R.layout.settings_new, null);
				try {
					TextView password=(TextView)view.findViewById(R.id.password);
					TextView resetpin=(TextView)view.findViewById(R.id.resetpin);
					TextView secQues=(TextView)view.findViewById(R.id.secQues);
					TextView invite=(TextView)view.findViewById(R.id.invite);
					TextView endorse=(TextView)view.findViewById(R.id.endrose);
					TextView userid=(TextView)view.findViewById(R.id.userid);
					TextView feedback = (TextView)view.findViewById(R.id.feedback);
					userid.setText(CallDispatcher.LoginUser);
					password.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							ChangePassword changePassword = ChangePassword.newInstance(mainContext);
							FragmentManager fragmentManager = SingleInstance.mainContext
									.getSupportFragmentManager();
							fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
							fragmentManager.beginTransaction().replace(
									R.id.activity_main_content_fragment, changePassword)
									.commitAllowingStateLoss();
						}
					});
					resetpin.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {

							PinAndTouchId pinAndTouchId = PinAndTouchId.newInstance(mainContext);
							FragmentManager fragmentManager = SingleInstance.mainContext
									.getSupportFragmentManager();
							fragmentManager.beginTransaction().replace(
									R.id.activity_main_content_fragment, pinAndTouchId)
									.commitAllowingStateLoss();
						}
					});
					secQues.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							WebServiceReferences.webServiceClient.GetMySecretQuestion(CallDispatcher.LoginUser, settingsFragment);
				showDialog();

						}
					});
					invite.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {

							InviteCodeFragment inviteCodeFragment = InviteCodeFragment.newInstance(mainContext);
							FragmentManager fragmentManager = SingleInstance.mainContext
									.getSupportFragmentManager();
							fragmentManager.beginTransaction().replace(
									R.id.activity_main_content_fragment, inviteCodeFragment)
									.commitAllowingStateLoss();
						}
					});
					endorse.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {

							EndorseFragment endorseFragment = EndorseFragment.newInstance(mainContext);
							FragmentManager fragmentManager = SingleInstance.mainContext
									.getSupportFragmentManager();
							fragmentManager.beginTransaction().replace(
									R.id.activity_main_content_fragment, endorseFragment)
									.commitAllowingStateLoss();
						}
					});

					feedback.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View view) {
							FeedbackFragment feedbackFragment = FeedbackFragment.newInstance(mainContext);
							FragmentManager fragmentManager = SingleInstance.mainContext
									.getSupportFragmentManager();
							fragmentManager.beginTransaction().replace(
									R.id.activity_main_content_fragment, feedbackFragment)
									.commitAllowingStateLoss();

						}
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				((ViewGroup) view.getParent()).removeView(view);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	public void onClick(View v) {
	}




	private void showToast(String msg) {
		Toast.makeText(mainContext, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			Log.i("onresult123", "Received");

			// super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == 30) {

				if (resultCode != Activity.RESULT_CANCELED) {
					Uri selectedImageUri = data.getData();
					strIPath = calldisp.getRealPathFromURI(selectedImageUri);
					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);

					if (length <= 2) {
						bitmap = calldisp.ResizeImage(strIPath);

						if (bitmap != null) {
							String path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ "MPD_"
									+ CompleteListView.getFileName() + ".jpg";
							BufferedOutputStream stream;
							try {
								stream = new BufferedOutputStream(
										new FileOutputStream(new File(path)));
								bitmap.compress(CompressFormat.JPEG, 100,
										stream);
								strIPath = path;

							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);

							if (bitmap != null)
								userIcon.setImageBitmap(bitmap);
							userimage.setImageBitmap(bitmap);
							Log.i("result",
									"ID"
											+ calldisp.getdbHeler(mainContext)
													.getProfilePicFieldId(
															"Picture"));
							if (calldisp.getdbHeler(mainContext)
									.getProfilePicFieldId("Picture") == 2) {
								profilePicture(strIPath);
							}
							updateProile(strIPath);
							appMainActivity.chageMyStatus();
							calldisp.uploadofflineResponse(strIPath, false, 0,
									"menupage");
						} else {
							strIPath = null;
						}
					}
				}

			} else if (requestCode == 31) {

				if (resultCode == Activity.RESULT_CANCELED) {

				} else {

					Uri selectedImageUri = data.getData();
					final int takeFlags = data.getFlags()
							& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					mainContext.getContentResolver()
							.takePersistableUriPermission(selectedImageUri,
									takeFlags);
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + "MPD_" + calldisp.getFileName()
							+ ".jpg";

					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);
					if (length <= 2) {
						new bitmaploader().execute(selectedImageUri);
					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.large_image));
					}

				}
			} else if (requestCode == 32) {

				if (resultCode == Activity.RESULT_OK) {
					Log.d("result", "........ response if result------------->");

					File fileCheck = new File(strIPath);
					if (fileCheck.exists()) {
						bitmap = calldisp.ResizeImage(strIPath);
						calldisp.changemyPictureOrientation(bitmap, strIPath);
						if (bitmap != null && !bitmap.isRecycled())
							bitmap.recycle();
						bitmap = null;
						bitmap = calldisp.ResizeImage(strIPath);
						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
								false);

						if (bitmap != null) {
							userIcon.setImageBitmap(bitmap);
							userimage.setImageBitmap(bitmap);
							// value_img.setTag(new File(strIPath).getName());
							// value_img.setPadding(10, 10, 10, 10);
							if (calldisp.getdbHeler(mainContext)
									.getProfilePicFieldId("Picture") == 2) {
								profilePicture(strIPath);
							}
							updateProile(strIPath);
							appMainActivity.chageMyStatus();
							calldisp.uploadofflineResponse(strIPath, false, 0,
									"menupage");
						}
					}
				} else {
					Log.d("result",
							"........ response else result------------->");
				}

			}
		} catch (Exception e) {

		}

	}


	private void updateProile(String filePath) {

		// TODO Auto-generated method stub
		try {
			int fieldId = calldisp.getdbHeler(mainContext)
					.getProfilePicFieldId("Picture");
			if (fieldId > 0) {
				ContentValues cv = new ContentValues();
				Vector<FieldTemplateBean> fields_list = calldisp.getdbHeler(
						mainContext).getProfileFields();
				for (FieldTemplateBean fieldTemplateBean : fields_list) {
					if (fieldId == Integer.parseInt(fieldTemplateBean
							.getFieldId())) {
						cv.put("fieldvalue", filePath);
						fieldTemplateBean.setFiledvalue(new File(filePath)
								.getName());

						cv.put("fieldid", fieldTemplateBean.getFieldId());
						cv.put("userid", CallDispatcher.LoginUser);
						calldisp.getdbHeler(mainContext)
								.updateProfileFieldValues(cv, "fieldid="
												+ fieldTemplateBean.getFieldId()
												+ " and userid='" + CallDispatcher.LoginUser
												+ "'");
						// }
						break;
					}
				}

			} else {
				Toast.makeText(
						mainContext,
						SingleInstance.mainContext.getResources().getString(
								R.string.pls_create_profile), Toast.LENGTH_LONG)
						.show();
			}
			calldisp.cancelDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void notifyProfilePictureDownloaded() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					String profilePic = DBAccess.getdbHeler().getProfilePic(
							CallDispatcher.LoginUser);
					if (profilePic != null && profilePic.length() > 0) {
						userIcon.setImageBitmap(calldisp.setProfilePicture(
								profilePic, R.drawable.icon_buddy_aoffline));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Log.d("image", "came to post execute for image");
				calldisp.cancelDialog();
				if (strIPath != null)
					bitmap = calldisp.ResizeImage(strIPath);
				if (bitmap != null) {
					Log.d("OnActivity", "_____On Activity Called______");
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);

					ImageView value_img;

					value_img = (ImageView) userIcon
							.findViewById(R.id.user_icon);

					if (bitmap != null) {
						value_img.setVisibility(View.VISIBLE);
						value_img.setImageBitmap(bitmap);
						value_img.setPadding(10, 10, 10, 10);
						userimage.setImageBitmap(bitmap);
						Log.i("result", "ID"
								+ calldisp.getdbHeler(mainContext)
										.getProfilePicFieldId("Picture"));
						if (calldisp.getdbHeler(mainContext)
								.getProfilePicFieldId("Picture") == 2) {
							profilePicture(strIPath);
						}
						appMainActivity.chageMyStatus();
						calldisp.uploadofflineResponse(strIPath, false, 0,
								"menupage");

					}
				} else {
					strIPath = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			ProgressDialog dialog = new ProgressDialog(mainContext);

			calldisp.showprogress(dialog, mainContext);
		}

		@Override
		protected Void doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			try {
				for (Uri uri : params) {
					Log.d("image", "came to doin backgroung for image");
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/MPD_" + calldisp.getFileName() + ".jpg";
					FileInputStream fin = (FileInputStream) mainContext
							.getContentResolver().openInputStream(uri);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}


	public View getParentView() {
		return view;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SingleInstance.contextTable.remove("settings", mainContext);
					MemoryProcessor.getInstance().unbindDrawables(view);
//					view = null;
//					saveSettings();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	public void profilePicture(String filepath) {
		try {
			int fieldId = calldisp.getdbHeler(mainContext)
					.getProfilePicFieldId("Picture");
			if (fieldId > 0) {
				Vector<FieldTemplateBean> fields_list = calldisp.getdbHeler(
						mainContext).getProfileFields();
				for (FieldTemplateBean fieldTemplateBean : fields_list) {
					if (fieldId == Integer.parseInt(fieldTemplateBean
							.getFieldId())) {

						fieldTemplateBean.getFieldId();
						fieldTemplateBean.getFieldName();
						fieldTemplateBean.getFieldType();
						fieldTemplateBean.setFiledvalue(new File(filepath)
								.getName());
						calldisp.getdbHeler(mainContext)
								.saveOrUpdateProfileField(fieldTemplateBean);

					}
				}
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void onResume() {
		try {
			// TODO Auto-generated method stub
			super.onResume();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void notifyMySecretQuestion( final Object obj)
	{
		cancelDialog();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (obj instanceof String[]) {
					String[] result = (String[]) obj;
					Log.i("AAAA", "notifyMySecretQuestion value " + result[0]);
					questions = Arrays.copyOf(result, result.length);
					SecurityQuestions securityQuestions = SecurityQuestions.newInstance(mainContext);
					FragmentManager fragmentManager = SingleInstance.mainContext
							.getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(
							R.id.activity_main_content_fragment, securityQuestions)
							.commitAllowingStateLoss();
				}
			}
			});
	}
	private void showDialog() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					progressDialog = new ProgressDialog(mainContext);
					if (progressDialog != null) {
						progressDialog.setCancelable(false);
						progressDialog.setMessage("Progress ...");
						progressDialog
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setProgress(0);
						progressDialog.setMax(100);
						progressDialog.show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}
	public void cancelDialog() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				Log.i("register", "--progress bar end-----");
				progressDialog.dismiss();
				progressDialog = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	void addShowHideListener( final Boolean isAudio) {
		if(isAudio) {
			AudioCallScreen audioCallScreen = AudioCallScreen.getInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, audioCallScreen)
					.commitAllowingStateLoss();
		}else {
			VideoCallScreen videoCallScreen = VideoCallScreen.getInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, videoCallScreen)
					.commitAllowingStateLoss();
		}
	}
}
