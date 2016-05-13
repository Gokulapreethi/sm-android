package com.cg.utilities;

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

import org.lib.model.BuddyInformationBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.SignalingBean;
import org.lib.model.UtilityBean;
import org.lib.model.UtilityResponse;
import org.lib.model.WebServiceBean;
import org.util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.files.CompleteListView;
import com.cg.files.ComponentCreator;
import com.cg.ftpprocessor.FTPBean;
import com.chat.ChatActivity;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.ViewProfileFragment;
import com.util.CustomVideoCamera;
import com.util.FullScreenImage;
import com.util.SingleInstance;
import com.util.Utils;
import com.util.VideoPlayer;

public class UtilityBuyerNew extends Activity implements OnClickListener {

	public static ArrayList<UtilityBean> staticlist = new ArrayList<UtilityBean>();

	private Typeface tf_regular = null;

	private Typeface tf_bold = null;

	private TextView tv_title, tv_addlist;

	private Context context;

	// private Vector<UtilityBean> sellers_list = null;

	private ImageView btn_back;

	private HashMap<Integer, UtilityBean> utilityMap;

	private HashMap<String, ArrayList<String>> result_slidepath;

	private boolean FROMCAMERA = false;

	private boolean FROMGALLERY = false;

	private boolean FORKITKAT = false;

	private boolean VIDEO = false;

	private int LOCATION = -5;

	private boolean AUDIO = false;

	private String image_path;

	private int selected_position = 0;

	private CallDispatcher callDisp;

	private HashMap<Integer, String> imageMap;

	private HashMap<Integer, String> audioMap;

	private HashMap<Integer, String> videoMap;

	private UtilityBean selected_utility;

	private int isPosted = 2;

	private String block_buddyname;

	private boolean isresultrequested = false;

	private HashMap<String, String> buddyDistanceMap = new HashMap<String, String>();

	private HashMap<Integer, ArrayList<UtilityBean>> selected_results;

	private int selected_resultpos = 0;

	private String[] settings_option = {
			SingleInstance.mainContext.getResources().getString(
					R.string.block_buddy),
			SingleInstance.mainContext.getResources().getString(
					R.string.block_this_ad) };

	private String[] settings_buddyopt = {
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_call),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_call),
			SingleInstance.mainContext.getResources()
					.getString(R.string.mmchat),
			SingleInstance.mainContext.getResources().getString(
					R.string.photo_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.sketch_message) };

	private String[] common_options = {
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_conference),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_conference),
			SingleInstance.mainContext.getResources().getString(
					R.string.photo_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.sketch_message) };

	private HashMap<String, UtilityBean> resposne_items = null;

	private String latitude = null;

	private String longitude = null;

	private String location = null;

	private Button btn_block = null;

	private ScrollView child_scroll;

	private SharedPreferences preferences;

	private Handler wservice_handler = null;

	AppMainActivity appMainActivity = null;

	private UtilityAdapter adapter;

	private Vector<UtilityBean> buyerList = new Vector<UtilityBean>();

	private ListView lv;

	ImageLoader imageLoader;

	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			context = this;
			utilityMap = new HashMap<Integer, UtilityBean>();
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;
			appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN");
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
			imageMap = new HashMap<Integer, String>();
			audioMap = new HashMap<Integer, String>();
			videoMap = new HashMap<Integer, String>();
			result_slidepath = new HashMap<String, ArrayList<String>>();
			selected_results = new HashMap<Integer, ArrayList<UtilityBean>>();
			resposne_items = new HashMap<String, UtilityBean>();
			WebServiceReferences.contextTable.put("utilitybuyer", context);
			wservice_handler = new Handler();
			// sellers_list = new Vector<UtilityBean>();
			tf_regular = Typeface.createFromAsset(context.getAssets(),
					"fonts/ARIAL.TTF");
			tf_bold = Typeface.createFromAsset(context.getAssets(),
					"fonts/ARIALBD.TTF");
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.utility_title1);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.utility_title1);
			tv_title = (TextView) findViewById(R.id.tv_title);
			tv_title.setTypeface(tf_bold);
			tv_title.setText("My Buying List");
			tv_addlist = (TextView) findViewById(R.id.ib_addlist);
			tv_addlist.setTypeface(tf_bold);
			tv_addlist.setOnClickListener(this);
			btn_back = (ImageView) findViewById(R.id.iv_utility_back);
			btn_back.setOnClickListener(this);
			btn_block = (Button) findViewById(R.id.block_button);
			btn_block.setVisibility(View.VISIBLE);
			btn_block.setOnClickListener(this);
			imageLoader = new ImageLoader(context);
			setContentView(R.layout.utility_layout);
			lv = (ListView) findViewById(R.id.listView);
			buyerList = callDisp.getdbHeler(context).SelectUtilityRecords(
					"select * from utility where userid='"
							+ CallDispatcher.LoginUser
							+ "' and utility_name='buy'");
			adapter = new UtilityAdapter(context, buyerList);
			lv.setAdapter(adapter);
		} catch (Exception e) {
			Log.i("buyer", "===>" + e.getMessage());
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private String getCurrentDateTime() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		return sdf.format(curDate);
	}

	public void showMenu(final int position, final String oldPath) {
		try {
			final CharSequence[] items = {
					SingleInstance.mainContext.getResources().getString(
							R.string.from_gallery),
					SingleInstance.mainContext.getResources().getString(
							R.string.from_camera) };
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if (item == 0)
						openGalery(position);
					else if (item == 1) {
						openCamera(position, oldPath);
					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void openGalery(int position) {

		try {
			if (Build.VERSION.SDK_INT < 19) {
				FROMGALLERY = true;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				imageMap.put(position, "");
				startActivityForResult(intent, position);
			} else {
				Log.i("img", "sdk is above 19");
				FORKITKAT = true;
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				imageMap.put(position, "");
				startActivityForResult(intent, position);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void openCamera(int pos, String oldImagePath) {

		try {
			Long free_size = callDisp.getExternalMemorySize();
			if (free_size > 0 && free_size >= 5120) {
				image_path = CompleteListView.getFileName() + ".jpg";
				String strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/MPD_" + image_path;
				image_path = strIPath;
				// Intent intent = new Intent(context, MultimediaUtils.class);
				// intent.putExtra("filePath", image_path);
				// intent.putExtra("requestCode", FROMCAMERA);
				// intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
				// intent.putExtra("createOrOpen", "create");
				// startActivity(intent);
				Intent intent = new Intent(context, CustomVideoCamera.class);
				intent.putExtra("filePath", image_path);
				intent.putExtra("isPhoto", true);
				imageMap.put(pos, oldImagePath);
				FROMCAMERA = true;
				startActivityForResult(intent, pos);

			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.insufficient_memory),
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			super.onActivityResult(requestCode, resultCode, data);

			if (FROMCAMERA) {
				if (imageMap.containsKey(requestCode)
						&& resultCode == RESULT_OK) {
					File file = new File(image_path);
					if (file.exists()) {
						imageMap.remove(requestCode);
						imageMap.put(requestCode, image_path);
						UtilityBean utilityBean = (UtilityBean) adapter
								.getItem(requestCode);
						utilityBean.setImag_filename(image_path);
						adapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(context, "Result canceled",
							Toast.LENGTH_SHORT).show();

				}
				FROMCAMERA = false;
			} else if (FROMGALLERY) {
				if (imageMap.containsKey(requestCode)
						&& resultCode == RESULT_OK) {
					Uri selectedImageUri = data.getData();
					image_path = callDisp.getRealPathFromURI(selectedImageUri);
					File selected_file = new File(image_path);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);
					if (length <= 2) {
						Bitmap bmp = callDisp.ResizeImage(image_path);
						if (bmp != null) {
							final String path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/MPD_"
									+ CompleteListView.getFileName() + ".jpg";

							BufferedOutputStream stream;
							try {
								stream = new BufferedOutputStream(
										new FileOutputStream(new File(path)));
								bmp.compress(CompressFormat.JPEG, 100, stream);

								image_path = path;
								bmp.recycle();
								bmp = null;
								File file = new File(image_path);
								if (file.exists()) {
									imageMap.remove(requestCode);
									imageMap.put(requestCode, image_path);
									UtilityBean utilityBean = (UtilityBean) adapter
											.getItem(requestCode);
									utilityBean.setImag_filename(image_path);
									adapter.notifyDataSetChanged();
								}

							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					} else
						Toast.makeText(
								context,
								SingleInstance.mainContext.getResources()
										.getString(R.string.large_image),
								Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(context, "Result canceled",
							Toast.LENGTH_SHORT).show();

				}
				FROMGALLERY = false;
			} else if (FORKITKAT) {
				if (imageMap.containsKey(requestCode)
						&& resultCode == RESULT_OK) {
					Uri selectedImageUri = data.getData();
					final int takeFlags = data.getFlags()
							& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					getContentResolver().takePersistableUriPermission(
							selectedImageUri, takeFlags);
					image_path = selectedImageUri.getPath();
					File selected_file = new File(image_path);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);

					if (length <= 2) {

						new bitmaploader().execute(selectedImageUri);
						File file = new File(image_path);
						if (file.exists()) {
							imageMap.remove(requestCode);
							imageMap.put(requestCode, image_path);
							UtilityBean utilityBean = (UtilityBean) adapter
									.getItem(requestCode);
							utilityBean.setImag_filename(image_path);
							adapter.notifyDataSetChanged();

						}

					} else {
						Toast.makeText(context, "Result canceled",
								Toast.LENGTH_SHORT).show();
					}
				}
				FORKITKAT = false;
			} else if (AUDIO) {
				if (audioMap.containsKey(requestCode)) {
					File file = new File(image_path);
					if (file.exists()) {
						audioMap.remove(requestCode);
						audioMap.put(requestCode, image_path);
						UtilityBean utilityBean = (UtilityBean) adapter
								.getItem(requestCode);
						utilityBean.setAudiofilename(image_path);
						adapter.notifyDataSetChanged();
					}
				}
				AUDIO = false;
			} else if (VIDEO) {
				if (videoMap.containsKey(requestCode)
						&& resultCode == RESULT_OK) {
					File file = new File(image_path);
					if (file.exists()) {
						videoMap.remove(requestCode);
						videoMap.put(requestCode, image_path);
						UtilityBean utilityBean = (UtilityBean) adapter
								.getItem(requestCode);
						utilityBean.setVideofilename(image_path);
						adapter.notifyDataSetChanged();
					}
				}

			}

		} catch (Exception e) {
			Log.e("buyer", "===> " + e.getMessage());
			e.printStackTrace();
		}
	}

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Log.d("image", "came to post execute for image");
				callDisp.cancelDialog();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			try {
				// TODO Auto-generated method stub
				super.onPreExecute();
				CallDispatcher.pdialog = new ProgressDialog(context);
				callDisp.showprogress(CallDispatcher.pdialog, context);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			try {
				for (Uri uri : params) {
					Log.d("image", "came to doin backgroung for image");
					String strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/MPD_" + CompleteListView.getFileName()
							+ ".jpg";
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
					image_path = strIPath;

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	}

	@Override
	protected void onDestroy() {
		try {
			// TODO Auto-generated method stub

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (imm != null && getCurrentFocus() != null)
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);

			if (WebServiceReferences.contextTable.containsKey("utilitybuyer"))
				WebServiceReferences.contextTable.remove("utilitybuyer");
			super.onDestroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processCallRequest(String username, String state, int caseid) {

		try {
			if (username != null && state.equalsIgnoreCase("Offline")
					|| state.equals("Stealth")
					|| state.equalsIgnoreCase("pending")
					|| state.equalsIgnoreCase("Virtual")
					|| state.equalsIgnoreCase("airport")) {
				if (WebServiceReferences.running) {
					CallDispatcher.pdialog = new ProgressDialog(context);
					callDisp.showprogress(CallDispatcher.pdialog, context);

					String[] res_info = new String[3];
					res_info[0] = CallDispatcher.LoginUser;
					res_info[1] = username;
					if (state.equals("Offline") || state.equals("Stealth"))
						res_info[2] = callDisp
								.getdbHeler(context)
								.getwheninfo(
										"select cid from clonemaster where cdescription='Offline'");
					else
						res_info[2] = "";

					WebServiceReferences.webServiceClient
							.OfflineCallResponse(res_info);
				}

			} else {
				if (!state.equalsIgnoreCase("pending")) {
					callDisp.MakeCall(caseid, username, context);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String profile_buddy;

	private String profile_buddystatus;

	private ProgressDialog pDialog;

	void doViewProfile(boolean accept, String buddy, String status) {

		try {

			profile_buddy = buddy;

			profile_buddystatus = status;

			ArrayList<String> profileList = callDisp.getdbHeler(context)
					.getProfile(buddy);
			Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0) {
				// Intent intent = new Intent(context, ViewProfiles.class);
				// intent.putExtra("buddyname", buddy);
				// intent.putExtra("buddystatus", status);
				// startActivity(intent);
				viewProfile(buddy);

			} else {
				Log.i("profile", "VIEW PROFILE------>" + buddy
						+ "---->GetProfileDetails");
				CallDispatcher.pdialog = new ProgressDialog(context);
				callDisp.showprogress(CallDispatcher.pdialog, context);
				CallDispatcher.isFromCallDisp = false;
				String modifiedDate = callDisp.getdbHeler(context)
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
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void viewProfile(String buddy) {
		ViewProfileFragment viewProfileFragment = ViewProfileFragment
				.newInstance(context);
		// Bundle bundle = new Bundle();
		// bundle.putString("buddyname", buddy);
		// viewProfileFragment.setArguments(bundle);
		viewProfileFragment.setBuddyName(buddy);
		FragmentManager fragmentManager = appMainActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.activity_main_content_fragment,
				viewProfileFragment);
		fragmentTransaction.commit();

	}

	public void notifyViewProfile() {
		try {
			ArrayList<String> profileList = callDisp.getdbHeler(context)
					.getProfile(profile_buddy);
			Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0) {
				if (!WebServiceReferences.contextTable
						.containsKey("viewprofile")) {
					// Intent intent = new Intent(context, ViewProfiles.class);
					// intent.putExtra("buddyname", profile_buddy);
					// intent.putExtra("buddystatus", profile_buddystatus);
					// startActivity(intent);
					viewProfile(profile_buddy);
				} else {
					(ViewProfileFragment.newInstance(context)).initView();
				}

			} else
				Toast.makeText(context, "No profile assigned for this user",
						Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void downloadConfiguredNote(String path, int selected_position,
			int res_position) {
		try {
			// TODO Auto-generated method stub

			if (CallDispatcher.LoginUser != null) {
				if (path != null && path.trim().length() > 0
						&& !path.equalsIgnoreCase("null")) {
					FTPBean ftpBean = new FTPBean();
					ftpBean.setServer_ip(appMainActivity.cBean.getRouter()
							.split(":")[0]);
					ftpBean.setServer_port(40400);
					ftpBean.setFtp_username("ftpadmin");
					ftpBean.setFtp_password("ftppassword");
					ftpBean.setFile_path(path);
					ftpBean.setOperation_type(2);
					ftpBean.setReq_object(Integer.toString(selected_position)
							+ "_" + Integer.toString(res_position));
					ftpBean.setRequest_from("utility_buyer");
					appMainActivity.getFtpNotifier().addTasktoExecutor(ftpBean);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uploadConfiguredNote(String path, UtilityBean bean) {
		try {
			// TODO Auto-generated method stub

			if (path != null) {
				if (CallDispatcher.LoginUser != null) {
					String username = preferences
							.getString("ftpusername", null);
					String password = preferences
							.getString("ftppassword", null);
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
					ftpBean.setReq_object(bean);
					ftpBean.setRequest_from("utility_buyer");
					if (appMainActivity.getFtpNotifier() != null)
						appMainActivity.getFtpNotifier().addTasktoExecutor(
								ftpBean);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyResultDownloaded(FTPBean bean) {
	}

	private void forwardbackwardaction(RelativeLayout result_parent,
			ArrayList<String> path_list, int postion, boolean isinitial,
			boolean isdownloading) {
		try {
			ImageView iv_resultimg = (ImageView) result_parent
					.findViewById(R.id.img_slide);
			LinearLayout lay_out = (LinearLayout) result_parent
					.findViewById(R.id.lay_fwdbwd);
			ImageView iv_bwd = (ImageView) result_parent
					.findViewById(R.id.iv_bwd);
			if (isinitial) {
				if (path_list != null) {
					if (path_list.size() > 0) {
						String path = path_list.get(0);
						if (path.contains("MPD_")) {
							Bitmap bmp = callDisp.ResizeImage(path);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(path);

						} else {
							Bitmap bmp = BitmapFactory.decodeResource(
									getResources(), R.drawable.v_play);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(path);
						}
						if (path_list.size() > 1) {
							lay_out.setVisibility(View.VISIBLE);
							iv_bwd.setVisibility(View.INVISIBLE);
						} else
							lay_out.setVisibility(View.INVISIBLE);
					} else {
						if (isdownloading) {
							Bitmap bmp = BitmapFactory.decodeResource(
									getResources(), R.drawable.download_result);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(null);
						} else {
							Bitmap bmp = BitmapFactory.decodeResource(
									getResources(), R.drawable.no_image);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(null);
						}
						lay_out.setVisibility(View.INVISIBLE);
					}
				} else {
					if (isdownloading) {
						Bitmap bmp = BitmapFactory.decodeResource(
								getResources(), R.drawable.download_result);
						iv_resultimg.setImageBitmap(bmp);
						iv_resultimg.setTag(null);
					} else {
						Bitmap bmp = BitmapFactory.decodeResource(
								getResources(), R.drawable.no_image);
						iv_resultimg.setImageBitmap(bmp);
						iv_resultimg.setTag(null);
					}
					lay_out.setVisibility(View.INVISIBLE);
				}
			} else {
				if (path_list != null) {
					if (path_list.size() > 0) {
						String path = path_list.get(postion);
						File fle = new File(path);
						if (fle.exists()) {
							if (path.contains("MPD_")) {
								Bitmap bmp = callDisp.ResizeImage(path);
								iv_resultimg.setImageBitmap(bmp);
								iv_resultimg.setTag(path);

							} else {
								Bitmap bmp = BitmapFactory.decodeResource(
										getResources(), R.drawable.v_play);
								iv_resultimg.setImageBitmap(bmp);
								iv_resultimg.setTag(path);
							}
						} else {
							Bitmap bmp = BitmapFactory.decodeResource(
									getResources(), R.drawable.broken);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(path);
						}

					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doMultiMMChat(String selectedBuddy) {

		try {
			if (callDisp.isConnected) {

				Log.d("MIM",
						"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ condition satisfied");
				Utility utility = new Utility();
				WebServiceReferences.SelectedBuddy = selectedBuddy;
				// BuddyInformationBean buddyBean =
				// WebServiceReferences.buddyList
				// .get(selectedBuddy.trim());

				BuddyInformationBean buddyBean = null;
				for (BuddyInformationBean temp : ContactsFragment
						.getBuddyList()) {
					if (!temp.isTitle()) {
						if (temp.getName().equalsIgnoreCase(
								selectedBuddy.trim())) {
							buddyBean = temp;
							break;
						}
					}
				}

				SignalingBean bean = new SignalingBean();
				bean.setSessionid(utility.getSessionID());
				bean.setFrom(CallDispatcher.LoginUser);
				bean.setTo(selectedBuddy);
				bean.setConferencemember("");
				bean.setMessage("");
				bean.setCallType("MSG");
				// Intent intent = new Intent(context, IMTabScreen.class);
				// intent.putExtra("sb", bean);
				// intent.putExtra("fromto", true);
				// context.startActivity(intent);
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("buddy", selectedBuddy);
				intent.putExtra("status", buddyBean.getStatus());
				intent.putExtra("sessionid", utility.getSessionID());
				// CallDispatcher.commEngine.makeCall(CallDispatcher.sb);

			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.network_err), Toast.LENGTH_LONG)
						.show();
			}
			//
			// Toast.makeText(context, selectedBuddy
			// + " and IDX:" + arg2,
			// Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyBuddyblockunblock(Object res) {
	}

	private void doCommonMenuAction(ArrayList<UtilityBean> buddyList) {
		try {
			if (buddyList != null) {
				final StringBuffer sb = new StringBuffer();
				for (UtilityBean bean : buddyList) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(bean.getUsername());

				}
				new AlertDialog.Builder(this)
						.setSingleChoiceItems(common_options, 0, null)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
										int selectedPosition = ((AlertDialog) dialog)
												.getListView()
												.getCheckedItemPosition();

										switch (selectedPosition) {
										case 0:
											doConferenceCall(sb.toString(),
													"AC");
											break;
										case 1:
											doConferenceCall(sb.toString(),
													"VC");
											break;
										case 2:
											Intent photoInent = new Intent(
													context,
													ComponentCreator.class);
											Bundle photoBndl = new Bundle();
											photoBndl
													.putString("type", "photo");
											photoBndl
													.putBoolean("action", true);
											photoBndl
													.putBoolean("forms", false);
											photoBndl.putString("buddyname",
													sb.toString());
											photoBndl.putBoolean("send", true);
											photoInent.putExtras(photoBndl);
											startActivity(photoInent);
											break;
										case 3:
											Intent intentComponent = new Intent(
													context,
													ComponentCreator.class);
											Bundle bndl = new Bundle();
											bndl.putString("type", "audio");
											bndl.putBoolean("action", true);
											bndl.putBoolean("forms", false);
											bndl.putString("buddyname",
													sb.toString());
											bndl.putBoolean("send", true);
											intentComponent.putExtras(bndl);
											startActivity(intentComponent);
											break;
										case 4:
											Intent videoIntent = new Intent(
													context,
													ComponentCreator.class);
											Bundle videoBndl = new Bundle();
											videoBndl
													.putString("type", "video");
											videoBndl
													.putBoolean("action", true);
											videoBndl
													.putBoolean("forms", false);
											videoBndl.putString("buddyname",
													sb.toString());
											videoBndl.putBoolean("send", true);
											videoIntent.putExtras(videoBndl);
											startActivity(videoIntent);
											break;
										case 5:
											Intent sketchcomponent = new Intent(
													context,
													ComponentCreator.class);
											Bundle sketchBndl = new Bundle();
											sketchBndl.putString("type",
													"handsketch");
											sketchBndl.putBoolean("action",
													true);
											sketchBndl.putBoolean("forms",
													false);
											sketchBndl.putString("buddyname",
													sb.toString());
											sketchBndl.putBoolean("send", true);
											sketchcomponent
													.putExtras(sketchBndl);
											startActivity(sketchcomponent);
											break;

										default:
											break;
										}

									}
								}).show();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doConferenceCall(String buddyNames, String callType) {
		try {
			String[] str = null;

			if (buddyNames.contains(",")) {
				str = buddyNames.split(",");
			} else {
				str = new String[1];
				str[0] = buddyNames;
			}
			for (int i = 0; i < str.length; i++) {

				BuddyInformationBean bib = null;
				for (BuddyInformationBean temp : ContactsFragment
						.getBuddyList()) {
					if (!temp.isTitle()) {
						if (temp.getName().equalsIgnoreCase(str[i])) {
							bib = temp;
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				if (bib.getStatus().startsWith("Onli")) {
					CallDispatcher.conConference.add(str[i]);
					callDisp.ConMadeConference(callType,
							SipNotificationListener.getCurrentContext());
				} else {
					if (WebServiceReferences.running) {
						CallDispatcher.pdialog = new ProgressDialog(context);
						callDisp.showprogress(CallDispatcher.pdialog, context);

						String[] res_info = new String[3];
						res_info[0] = CallDispatcher.LoginUser;
						res_info[1] = bib.getName();
						if (bib.getStatus().equals("Offline")
								|| bib.getStatus().equals("Stealth"))
							res_info[2] = callDisp
									.getdbHeler(context)
									.getwheninfo(
											"select cid from clonemaster where cdescription='Offline'");
						else
							res_info[2] = "";

						WebServiceReferences.webServiceClient
								.OfflineCallResponse(res_info);
					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyOfflineCallResponse(final Object obj) {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					callDisp.cancelDialog();
					if (obj instanceof ArrayList) {
						ArrayList<Object> callresponse_list = (ArrayList<Object>) obj;
						if (callresponse_list.size() == 3) {
							String user_id = null;
							String buddy_id = null;
							if (callresponse_list.get(0) instanceof String)
								user_id = (String) callresponse_list.get(0);
							if (callresponse_list.get(1) instanceof String)
								buddy_id = (String) callresponse_list.get(1);
							if (callresponse_list.get(2) instanceof ArrayList)
								;
							ArrayList<OfflineRequestConfigBean> config_list = (ArrayList<OfflineRequestConfigBean>) callresponse_list
									.get(2);

							if (config_list != null) {
								for (OfflineRequestConfigBean offlineRequestConfigBean : config_list) {
									ContentValues cv = new ContentValues();
									cv.put("config_id",
											offlineRequestConfigBean.getId());
									cv.put("fromuser", offlineRequestConfigBean
											.getBuddyId());
									cv.put("messagetitle",
											offlineRequestConfigBean
													.getMessageTitle());
									cv.put("messagetype",
											offlineRequestConfigBean
													.getMessagetype());
									cv.put("message", offlineRequestConfigBean
											.getMessage());
									cv.put("responsetype",
											offlineRequestConfigBean
													.getResponseType());
									cv.put("response", "''");
									cv.put("url",
											offlineRequestConfigBean.getUrl());
									cv.put("receivedtime", CompleteListView
											.getNoteCreateTimeForFiles());
									cv.put("sendstatus", "0");
									cv.put("username", CallDispatcher.LoginUser);

									callDisp.getdbHeler(context)
											.insertOfflinePendingClones(cv);
									callDisp.downloadOfflineresponse(
											offlineRequestConfigBean
													.getMessage(),
											offlineRequestConfigBean.getId(),
											"answering machine", null);

								}
							}

						}

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;
						showAlert(service_bean.getText());
						callDisp.cancelDialog();
					}
				}

			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showAlert(String message) {
		try {
			final AlertDialog alertDialog = new AlertDialog.Builder(context)
					.create();

			// Setting Dialog Title
			alertDialog.setTitle("Response");

			// Setting Dialog Message
			alertDialog.setMessage(message);

			// Setting Icon to Dialog
			// alertDialog.setIcon(R.drawable.tick);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					alertDialog.dismiss();
					Log.i("profile", "INSIDE insertRecords------? ");

					// CustomAddOnFragment addOnFragment=new
					// CustomAddOnFragment();
					// addOnFragment.insertRecords();
					//
					callDisp.cancelDialog();

					alertDialog.dismiss();
					// finish();
				}
			});

			// Showing Alert Message
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void notifyUploadStatus(final String filepath,
			final String req_object, final boolean status) {
		Log.d("Profile", "came to notifyUploadStatus" + wservice_handler);
		wservice_handler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("Profile", "Inside the handler..." + status);
				if (status) {

				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (imageMap.size() > 0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				if (imm != null && getCurrentFocus() != null)
					imm.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(), 0);
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	private void showprogress() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(context);
		pDialog.setCancelable(false);
		pDialog.setMessage("Progress ...");
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setProgress(0);
		pDialog.setMax(100);
		pDialog.show();

	}

	public void cancelDialog() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.ib_addlist:
			addNewItem();
			break;

		default:
			break;
		}

	}

	private void addNewItem() {

		// TODO Auto-generated method stub

		int listSize = staticlist.size();
		UtilityBean dataBean = new UtilityBean();
		if (listSize == 0) {
			dataBean.setListPosition(0);
		} else {
			dataBean.setListPosition(listSize + 1);
		}
		dataBean.setAddNewList(true);
		buyerList.add(dataBean);
		adapter.notifyDataSetChanged();

	}

	class UtilityAdapter extends BaseAdapter {
		// LayoutInflater mInflater;
		Vector<UtilityBean> list;

		Context context;

		public UtilityAdapter(Context context, Vector<UtilityBean> list2) {
			// TODO Auto-generated constructor stub
			this.list = list2;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			UtilityBean utilityBean = list.get(position);
			return utilityBean;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public Object getItemById(int Id) {
			for (UtilityBean utilityBean : list) {
				if (utilityBean.getId() == Id) {
					return utilityBean;
				}

			}
			return null;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			try {
				final ViewHolder holder;
				convertView = null;
				if (convertView == null) {
					holder = new ViewHolder();
					holder.bean = (UtilityBean) list.get(position);

					holder.mInflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = holder.mInflater.inflate(
							R.layout.utility_adapter, null);

					holder.linearLayout = (LinearLayout) convertView
							.findViewById(R.id.LinearLayout2);
					holder.linearLayout.setTag(position);

					holder.save = (TextView) convertView
							.findViewById(R.id.save);
					holder.save.setTag(position);
					holder.delete = (TextView) convertView
							.findViewById(R.id.Delete);
					holder.delete.setTag(position);

					holder.camera = (ImageView) convertView
							.findViewById(R.id.photo);
					holder.camera.setTag(position);
					holder.video = (ImageView) convertView
							.findViewById(R.id.video);
					holder.video.setTag(position);
					holder.mic = (ImageView) convertView.findViewById(R.id.mic);
					holder.mic.setTag(position);
					holder.cameraClose = (ImageView) convertView
							.findViewById(R.id.photo_close);
					holder.cameraClose.setTag(position);
					holder.micClose = (ImageView) convertView
							.findViewById(R.id.mic_close);
					holder.micClose.setTag(position);
					holder.videoClose = (ImageView) convertView
							.findViewById(R.id.video_close);
					holder.videoClose.setTag(position);
					holder.postorHide = (TextView) convertView
							.findViewById(R.id.tv_psthide);
					holder.postorHide.setTag(position);

					// date
					holder.date = (TextView) convertView
							.findViewById(R.id.Date);
					holder.date.setTag(position);

					if (holder.bean.getId() > 0) {
						holder.date.setText(holder.bean.getPosted_date());
					} else {
						holder.date.setText(SingleInstance.mainContext
								.getCurrentDateTime24Format());
					}

					// show/hide results

					holder.showresults = (TextView) convertView
							.findViewById(R.id.showResult);
					holder.showresults.setTag(position);
					holder.resultCount = (Button) convertView
							.findViewById(R.id.resultCount);
					if (holder.bean.getResultcount() > 0) {
						holder.resultCount.setVisibility(View.VISIBLE);
					} else {
						holder.resultCount.setVisibility(View.GONE);
					}

					// buyer
					holder.nameorOrg = (EditText) convertView
							.findViewById(R.id.BuyerNameET);
					holder.nameorOrg.setTag(position);
					if (list.get(position).getNameororg() != null) {
						holder.nameorOrg.setText(list.get(position)
								.getNameororg().toString());
					}

					// holder.buyer.setFocusable(true);
					// UtilityActivity.list.add(saveBean.setBuyerName(list.get(position).getBuyerName().toString()));

					// product
					holder.product = (EditText) convertView
							.findViewById(R.id.ProductNameET);
					holder.product.setTag(position);
					if (list.get(position).getProduct_name() != null) {
						holder.product.setText(list.get(position)
								.getProduct_name().toString());
					}
					// holder.product.setFocusable(true);

					// quantity
					holder.quantity = (EditText) convertView
							.findViewById(R.id.QuantityET);
					holder.quantity.setTag(position);
					if (list.get(position).getQty() != null) {
						holder.quantity.setText(list.get(position).getQty()
								.toString());
					}

					// price
					holder.price = (EditText) convertView
							.findViewById(R.id.productPrice);
					holder.price.setTag(position);
					if (list.get(position).getPrice() != null) {
						holder.price.setText(list.get(position).getPrice()
								.toString());
					}

					// location
					holder.location = (EditText) convertView
							.findViewById(R.id.location);
					holder.location.setTag(position);
					if (list.get(position).getLocation() != null) {
						holder.location.setText(list.get(position)
								.getLocation().toString());
					}

					// address
					holder.address = (EditText) convertView
							.findViewById(R.id.Address);
					holder.address.setTag(position);
					if (list.get(position).getAddress() != null) {
						holder.address.setText(list.get(position).getAddress()
								.toString());
					}

					// country
					holder.country = (EditText) convertView
							.findViewById(R.id.country);
					holder.country.setTag(position);
					if (list.get(position).getCountry() != null) {
						holder.country.setText(list.get(position).getCountry()
								.toString());
					}
					// state
					holder.state = (EditText) convertView
							.findViewById(R.id.State);
					holder.state.setTag(position);
					if (list.get(position).getState() != null) {
						holder.state.setText(list.get(position).getState()
								.toString());
					}
					// city
					holder.city = (EditText) convertView
							.findViewById(R.id.District);
					holder.city.setTag(position);
					if (list.get(position).getCityordist() != null) {
						holder.city.setText(list.get(position).getCityordist()
								.toString());
					}
					// pin
					holder.pin = (EditText) convertView.findViewById(R.id.pin);
					holder.pin.setTag(position);
					if (list.get(position).getPin() != null) {
						holder.pin.setText(list.get(position).getPin()
								.toString());
					}
					// email
					holder.email = (EditText) convertView
							.findViewById(R.id.Email);
					holder.email.setTag(position);
					if (list.get(position).getEmail() != null)
						holder.email.setText(list.get(position).getEmail()
								.toString());

					// cnCode
					holder.cnCode = (EditText) convertView
							.findViewById(R.id.code);
					holder.cnCode.setTag(position);
					if (list.get(position).getCnCode() != null) {
						holder.cnCode.setText(list.get(position).getCnCode()
								.toString());
					}
					// cnNumber
					holder.cnNumber = (EditText) convertView
							.findViewById(R.id.codaNumber);
					holder.cnNumber.setTag(position);
					if (list.get(position).getC_no() != null) {
						holder.cnNumber.setText(list.get(position).getC_no()
								.toString());
					}
					convertView.setTag(holder);

				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				int buyer_position = (Integer) holder.nameorOrg.getTag();
				holder.nameorOrg.setId(buyer_position);

				int product_position = (Integer) holder.product.getTag();
				holder.product.setId(product_position);

				int quantity_position = (Integer) holder.quantity.getTag();
				holder.quantity.setId(quantity_position);

				int price_position = (Integer) holder.price.getTag();
				holder.price.setId(price_position);

				int location_position = (Integer) holder.location.getTag();
				holder.location.setId(location_position);

				int address_position = (Integer) holder.address.getTag();
				holder.address.setId(address_position);

				int country_position = (Integer) holder.country.getTag();
				holder.country.setId(country_position);

				int state_position = (Integer) holder.state.getTag();
				holder.state.setId(state_position);

				int city_position = (Integer) holder.city.getTag();
				holder.city.setId(city_position);

				int pin_position = (Integer) holder.pin.getTag();
				holder.pin.setId(pin_position);

				int email_position = (Integer) holder.email.getTag();
				holder.email.setId(email_position);

				int cnCode_position = (Integer) holder.cnCode.getTag();
				holder.cnCode.setId(cnCode_position);

				int cnNumber_position = (Integer) holder.cnNumber.getTag();
				holder.cnNumber.setId(cnNumber_position);

				TextWatcher textWatcher = new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
						final int position2 = holder.nameorOrg.getId();
						final EditText buyer1 = (EditText) holder.nameorOrg;
						if (buyer1.getText().toString().length() > 0) {
							holder.bean.setNameororg(buyer1.getText()
									.toString());
							list.set(position2, holder.bean);
						} else {
							list.set(position2, holder.bean);
						}

						final int position3 = holder.product.getId();
						final EditText product = (EditText) holder.product;
						if (product.getText().toString().length() > 0) {
							holder.bean.setProduct_name(product.getText()
									.toString());
							list.set(position3, holder.bean);
						} else {
							list.set(position3, holder.bean);
						}

						final int position4 = holder.quantity.getId();
						final EditText quantity = (EditText) holder.quantity;
						if (quantity.getText().toString().length() > 0) {
							holder.bean.setQty(quantity.getText().toString());
							list.set(position4, holder.bean);
						} else {
							list.set(position4, holder.bean);
						}

						final int position5 = holder.price.getId();
						final EditText price = (EditText) holder.price;
						if (price.getText().toString().length() > 0) {
							holder.bean.setPrice(price.getText().toString());
							list.set(position5, holder.bean);
						} else {
							list.set(position5, holder.bean);
						}

						final int position6 = holder.location.getId();
						final EditText location = (EditText) holder.location;
						if (location.getText().toString().length() > 0) {
							holder.bean.setLocation(location.getText()
									.toString());
							list.set(position6, holder.bean);
						} else {
							list.set(position6, holder.bean);
						}

						final int position7 = holder.address.getId();
						final EditText address = (EditText) holder.address;
						if (address.getText().toString().length() > 0) {
							holder.bean
									.setAddress(address.getText().toString());
							list.set(position7, holder.bean);
						} else {
							list.set(position7, holder.bean);
						}

						final int position8 = holder.country.getId();
						final EditText country = (EditText) holder.country;
						if (country.getText().toString().length() > 0) {
							holder.bean
									.setCountry(country.getText().toString());
							list.set(position8, holder.bean);
						} else {
							list.set(position8, holder.bean);
						}

						final int position9 = holder.state.getId();
						final EditText state = (EditText) holder.state;
						if (state.getText().toString().length() > 0) {
							holder.bean.setState(state.getText().toString());
							list.set(position9, holder.bean);
						} else {
							list.set(position9, holder.bean);
						}

						final int position10 = holder.city.getId();
						final EditText city = (EditText) holder.city;
						if (city.getText().toString().length() > 0) {
							holder.bean
									.setCityordist(city.getText().toString());
							list.set(position10, holder.bean);
						} else {
							list.set(position10, holder.bean);
						}

						final int position11 = holder.pin.getId();
						final EditText pin = (EditText) holder.pin;
						if (pin.getText().toString().length() > 0) {
							holder.bean.setPin(pin.getText().toString());
							list.set(position11, holder.bean);
						} else {
							list.set(position11, holder.bean);
						}

						final int position12 = holder.email.getId();
						final EditText email = (EditText) holder.email;
						if (email.getText().toString().length() > 0) {
							holder.bean.setEmail(email.getText().toString());
							list.set(position12, holder.bean);
						} else {
							list.set(position12, holder.bean);
						}

						final int position13 = holder.cnCode.getId();
						final EditText cnCode = (EditText) holder.cnCode;
						if (cnCode.getText().toString().length() > 0) {
							holder.bean.setCnCode(cnCode.getText().toString());
							list.set(position13, holder.bean);
						} else {
							list.set(position13, holder.bean);
						}

						final int position14 = holder.cnNumber.getId();
						final EditText cnNumber = (EditText) holder.cnNumber;
						if (cnNumber.getText().toString().length() > 0) {
							holder.bean.setC_no(cnNumber.getText().toString());
							list.set(position14, holder.bean);
						} else {
							list.set(position14, holder.bean);
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
					}
				};
				holder.nameorOrg.addTextChangedListener(textWatcher);
				holder.product.addTextChangedListener(textWatcher);
				holder.quantity.addTextChangedListener(textWatcher);
				holder.price.addTextChangedListener(textWatcher);
				holder.location.addTextChangedListener(textWatcher);
				holder.address.addTextChangedListener(textWatcher);
				holder.country.addTextChangedListener(textWatcher);
				holder.state.addTextChangedListener(textWatcher);
				holder.city.addTextChangedListener(textWatcher);
				holder.pin.addTextChangedListener(textWatcher);
				holder.email.addTextChangedListener(textWatcher);
				holder.cnCode.addTextChangedListener(textWatcher);
				holder.cnNumber.addTextChangedListener(textWatcher);

				if (holder.bean.getId() > 0) {
					holder.save.setText(SingleInstance.mainContext
							.getResources().getString(R.string.update));
					holder.postorHide.setVisibility(View.VISIBLE);
				} else {
					holder.save.setText(SingleInstance.mainContext
							.getResources().getString(R.string.save));
					holder.postorHide.setVisibility(View.GONE);
				}

				if (holder.bean.getMode() == 1) {
					holder.postorHide.setText(SingleInstance.mainContext
							.getResources().getString(R.string.hide));
				} else {
					holder.postorHide.setText(SingleInstance.mainContext
							.getResources().getString(R.string.post));
				}

				holder.save.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						try {
							int position = (Integer) view.getTag();
							isPosted = 0;
							saveUtility(false, position, 0);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

				holder.showresults.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						int position = (Integer) view.getTag();
						isresultrequested = true;
						isPosted = 0;
						saveUtility(true, position, 0);
					}
				});
				holder.delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						int position = (Integer) view.getTag();
						UtilityBean utilityBean = (UtilityBean) adapter
								.getItem(position);
						showDeleteAlert(utilityBean);
					}
				});
				holder.postorHide.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						int position = (Integer) view.getTag();
						isresultrequested = false;
						if (holder.postorHide.getText().toString()
								.equalsIgnoreCase("post")) {
							if (isPosted == 1) {
								saveUtility(false, position, 1);
							} else {
								saveUtility(false, position, 0);
							}
						} else if (holder.postorHide.getText().toString()
								.equalsIgnoreCase("hide")) {
							if (isPosted == 2) {
								// bean.setMode(0);
								saveUtility(false, position, 0);
							} else {
								// bean.setMode(1);
								saveUtility(false, position, 1);
							}
						}

					}
				});
				if (holder.bean.getImag_filename() != null
						&& holder.bean.getImag_filename().length() > 0) {
					imageMap.put(position, holder.bean.getImag_filename());
					imageLoader.DisplayImage(holder.bean.getImag_filename(),
							holder.camera, R.drawable.ic_action_camera_low);
					holder.cameraClose.setVisibility(View.VISIBLE);
				} else {
					holder.cameraClose.setVisibility(View.GONE);
				}

				holder.camera.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						if (holder.bean.getImag_filename() != null
								&& holder.bean.getImag_filename().length() > 0) {
							File file = new File(holder.bean.getImag_filename());
							if (file.exists()) {
								Intent in = new Intent(context,
										FullScreenImage.class);
								in.putExtra("image",
										holder.bean.getImag_filename());
								startActivity(in);
							}
						} else {
							showMenu((Integer) view.getTag(),
									holder.bean.getImag_filename());
						}
					}
				});

				holder.cameraClose.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						UtilityBean utilityBean = (UtilityBean) getItem(position);

						if (holder.bean.getImag_filename() != null
								&& holder.bean.getImag_filename().length() > 0) {
							File file = new File(holder.bean.getImag_filename());
							if (file.exists()) {
								file.delete();
							}
							if (imageMap.containsKey(holder.bean
									.getImag_filename())) {
								imageMap.remove(holder.bean.getImag_filename());
							}
							utilityBean.setImag_filename("");
							notifyDataSetChanged();
						}
					}
				});
				if (holder.bean.getVideofilename() != null
						&& holder.bean.getVideofilename().length() > 0) {
					holder.video.setBackgroundResource(R.drawable.btn_play_new);
					videoMap.put(position, holder.bean.getVideofilename());
					holder.videoClose.setVisibility(View.VISIBLE);
				} else {
					holder.video
							.setBackgroundResource(R.drawable.ic_action_video_low);
					holder.videoClose.setVisibility(View.GONE);
				}
				holder.video.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						if (holder.bean.getVideofilename() != null
								&& holder.bean.getVideofilename().length() > 0) {
							Intent intentVPlayer = new Intent(context,
									VideoPlayer.class);
							intentVPlayer.putExtra("video",
									holder.bean.getVideofilename());
							startActivity(intentVPlayer);
						} else {
							image_path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/MVD_"
									+ CompleteListView.getFileName() + ".mp4";
							Intent intent = new Intent(context,
									CustomVideoCamera.class);
							intent.putExtra("filePath", image_path);
							VIDEO = true;
							videoMap.put(position, "");
							startActivityForResult(intent,
									(Integer) view.getTag());

						}

					}
				});

				holder.videoClose.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						UtilityBean utilityBean = (UtilityBean) getItem(position);
						if (holder.bean.getVideofilename() != null
								&& holder.bean.getVideofilename().length() > 0) {
							File file = new File(holder.bean.getVideofilename());
							if (file.exists()) {
								file.delete();
							}
							if (videoMap.containsKey(holder.bean
									.getVideofilename())) {
								videoMap.remove(holder.bean.getVideofilename());
							}
							utilityBean.setVideofilename("");
							notifyDataSetChanged();
						}

					}
				});
				if (holder.bean.getAudiofilename() != null
						&& holder.bean.getAudiofilename().length() > 0) {
					String audio = CallDispatcher.containsLocalPath(holder.bean
							.getAudiofilename());
					holder.mic.setBackgroundResource(R.drawable.btn_play_new);
					audioMap.put(position, audio);
					holder.micClose.setVisibility(View.VISIBLE);
				} else {
					holder.mic
							.setBackgroundResource(R.drawable.ic_action_mic_grey);
					holder.micClose.setVisibility(View.GONE);
				}
				holder.mic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (holder.bean.getAudiofilename() != null
								&& holder.bean.getAudiofilename().length() > 0) {
							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath",
									holder.bean.getAudiofilename());
							intent.putExtra("isAudio", true);
							intent.putExtra("action", "audio");
							intent.putExtra("requestCode", position);
							intent.putExtra("createOrOpen", "open");
							startActivity(intent);
						} else {
							image_path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ "MAD_audio"
									+ CompleteListView.getFileName() + ".mp4";
							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath", image_path);
							intent.putExtra("requestCode", position);
							intent.putExtra("isAudio", true);
							intent.putExtra("action", "audio");
							intent.putExtra("createOrOpen", "create");
							audioMap.put(position, image_path);
							AUDIO = true;
							startActivity(intent);
						}
					}
				});

				holder.micClose.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						UtilityBean utilityBean = (UtilityBean) getItem(position);
						if (holder.bean.getAudiofilename() != null
								&& holder.bean.getAudiofilename().length() > 0) {
							File file = new File(holder.bean.getAudiofilename());
							if (file.exists()) {
								file.delete();
							}
							if (audioMap.containsKey(holder.bean
									.getAudiofilename())) {
								audioMap.remove(holder.bean.getAudiofilename());
							}
							utilityBean.setAudiofilename("");
							notifyDataSetChanged();
						}

					}
				});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

		public class ViewHolder {
			ImageView camera, video, mic, cameraClose, videoClose, micClose;
			LinearLayout linearLayout;
			UtilityBean bean;
			LayoutInflater mInflater;
			EditText nameorOrg, product, quantity, price, location, address,
					country, state, city, pin, email, cnCode, cnNumber;
			TextView save, delete, date, showresults, postorHide;
			Button resultCount;
		}

	}

	private void saveUtility(boolean withResult, int position, int mode) {
		try {
			if (CallDispatcher.LoginUser != null) {
				UtilityBean utilityBean = (UtilityBean) adapter
						.getItem(position);
				selected_utility = utilityBean;
				selected_position = position;
				utilityBean.setUsername(CallDispatcher.LoginUser);
				utilityBean.setUtility_name("buy");
				if (withResult) {
					utilityBean.setResult("1");
				} else {
					utilityBean.setResult("0");
				}
				if (utilityBean.getId() > 0) {
					utilityBean.setType("edit");
					utilityBean.setExistingPostedDate(utilityBean
							.getPosted_date());
					utilityBean.setPosted_date(SingleInstance.mainContext
							.getCurrentDateTime24Format());
				} else {
					utilityBean.setType("new");
					utilityBean.setId(0);
					utilityBean.setPosted_date(SingleInstance.mainContext
							.getCurrentDateTime24Format());
				}
				if (utilityBean.getVideofilename() != null) {
					File videoFile = new File(utilityBean.getVideofilename());
					if (videoFile.exists()) {
						if (!DBAccess.getdbHeler().isRecordExists(
								"select * from utility where video_file ='"
										+ utilityBean.getVideofilename() + "'")) {
							uploadConfiguredNote(
									utilityBean.getVideofilename(), utilityBean);
						}
					}
				}

				if (utilityBean.getAudiofilename() != null) {
					File audioFile = new File(utilityBean.getAudiofilename());
					if (audioFile.exists()) {
						if (utilityBean.getAudiofilename() != null) {
							if (!DBAccess.getdbHeler().isRecordExists(
									"select * from utility where voice ='"
											+ utilityBean.getAudiofilename()
											+ "'")) {
								uploadConfiguredNote(
										utilityBean.getAudiofilename(),
										utilityBean);
							}
						}
					}
				}
				if (utilityBean.getImag_filename() != null) {
					File imageFile = new File(utilityBean.getImag_filename());
					if (imageFile.exists()) {
						if (utilityBean.getImag_filename() != null) {
							if (!DBAccess.getdbHeler().isRecordExists(
									"select * from utility where img_file ='"
											+ utilityBean.getImag_filename()
											+ "'")) {
								uploadConfiguredNote(
										utilityBean.getImag_filename(),
										utilityBean);
							}
						}
					}
				}
				utilityBean.setMode(mode);
				UtilityBean webUtility = getUtilityClone(utilityBean);
				if (WebServiceReferences.running) {
					// CallDispatcher.pdialog = new ProgressDialog(context);
					// callDisp.showprogress(CallDispatcher.pdialog, context);
					utilityMap.put(position, utilityBean);
					showprogress();
					WebServiceReferences.webServiceClient.getsetUtility(
							webUtility, context);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private UtilityBean getUtilityClone(UtilityBean utilityBean) {
		try {
			UtilityBean webUtility = (UtilityBean) utilityBean.clone();
			if (utilityBean.getAudiofilename() != null) {
				String audioPath = Utils.removeFullPath(utilityBean
						.getAudiofilename());
				webUtility.setAudiofilename(audioPath);
			}
			if (utilityBean.getImag_filename() != null) {
				String imagePath = Utils.removeFullPath(utilityBean
						.getImag_filename());
				webUtility.setImag_filename(imagePath);
			}
			if (utilityBean.getVideofilename() != null) {
				String videoPath = Utils.removeFullPath(utilityBean
						.getVideofilename());
				webUtility.setVideofilename(videoPath);
			}
			return webUtility;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void insertorupdateutility(UtilityBean bean) {
		ContentValues cv = new ContentValues();
		cv.put("userid", bean.getUsername());
		cv.put("org_name", bean.getNameororg());
		cv.put("product_name", bean.getProduct_name());
		cv.put("quantity", bean.getQty());
		cv.put("price", bean.getPrice());
		cv.put("video_file", bean.getVideofilename());
		cv.put("img_file", bean.getImag_filename());
		cv.put("voice", bean.getAudiofilename());
		cv.put("location", bean.getLocation());
		cv.put("address", bean.getAddress());
		cv.put("country", bean.getCountry());
		cv.put("state", bean.getState());
		cv.put("city", bean.getCityordist());
		cv.put("pin", bean.getPin());
		cv.put("email", bean.getEmail());
		cv.put("c_no", bean.getC_no());
		cv.put("entry_mode", bean.getMode());
		cv.put("utility_name", "buy");
		cv.put("posted_date", bean.getPosted_date());
		cv.put("id", bean.getId());
		if (!DBAccess.getdbHeler().isRecordExists(
				"select * from utility where id=" + bean.getId())) {
			long id = callDisp.getdbHeler(context).insertUtility(cv);
			Log.d("utility", "Inserted row id-->" + id);
		} else {
			long id = callDisp.getdbHeler(context).UpdateUtility(cv,
					bean.getExistingPostedDate());
			Log.d("utility", "updated row id-->" + id);
		}

	}

	public void notifywebserviceReponse(Object obj) {
		try {
			if (obj instanceof UtilityResponse) {
				cancelDialog();
				UtilityResponse response = (UtilityResponse) obj;
				if (response != null) {
					if (response.getEditedutility_id() != null) {
						selected_utility.setPosted_date(response
								.getPosted_date());
						selected_utility.setId(Integer.parseInt(response
								.getEditedutility_id()));
						insertorupdateutility(selected_utility);
						Log.d("utility",
								"Received id is" + response.getUtility_id());

						if (!isresultrequested) {
							for (int i = 0; i < buyerList.size(); i++) {
								if (i == selected_position) {
									UtilityBean utilityBean = buyerList
											.get(selected_position);
									utilityBean.setMode(isPosted);
									break;
								}
							}
							if (isPosted == 2) {
								showToast("Your Add hided succesfully");
							} else if (isPosted == 1) {
								showToast("Your Add posted succesfully");
							} else {
								showToast("Updated succesfully");
							}
						} else {
							isresultrequested = false;
							if (response.getResult_list() != null) {
								if (response.getResult_list().size() > 0) {
									LinearLayout lay_result = null;
									if (buddyDistanceMap.size() > 0) {
										buddyDistanceMap.clear();
									}

									if (callDisp != null)
										callDisp.compareutilityresponse(
												response.getResult_list(),
												context, lay_result);
									for (int i = 0; i < buyerList.size(); i++) {
										if (i == selected_position) {
											UtilityBean utilityBean = buyerList
													.get(selected_position);
											utilityBean.setResultcount(response
													.getResult_list().size());
											break;
										}
									}

									handler.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											adapter.notifyDataSetChanged();
										}
									});

								} else
									showToast(SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.no_sellers_found_for_this_add));
							} else {
								showToast(SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.no_sellers_found_for_this_add));
							}
						}
					} else if (response.getDeletedutility_id() != null) {
						DBAccess.getdbHeler().deleteUtility(
								response.getDeletedutility_id());
						String audioFile = selected_utility.getAudiofilename();
						if (audioFile != null) {
							File file = new File(audioFile);
							if (file.exists()) {
								file.delete();
							}
						}
						String imageFile = selected_utility.getImag_filename();
						if (imageFile != null) {
							File file = new File(imageFile);
							if (file.exists()) {
								file.delete();
							}
						}
						String videoFile = selected_utility.getVideofilename();
						if (videoFile != null) {
							File file = new File(videoFile);
							if (file.exists()) {
								file.delete();
							}
						}
						buyerList.remove(selected_utility);
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								adapter.notifyDataSetChanged();
							}
						});
						showToast(response.getMessage());
					} else {

						selected_utility.setPosted_date(response
								.getPosted_date());
						selected_utility.setId(Integer.parseInt(response
								.getUtility_id()));
						insertorupdateutility(selected_utility);
						if (!isresultrequested) {
							for (int i = 0; i < buyerList.size(); i++) {
								if (i == selected_position) {
									UtilityBean utilityBean = buyerList
											.get(selected_position);
									utilityBean.setMode(0);
									break;
								}
							}
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.saved_successfully));
						} else {

						}
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								adapter.notifyDataSetChanged();
							}
						});
					}

				}
			} else if (obj instanceof WebServiceBean) {
				cancelDialog();
				WebServiceBean service_bean = (WebServiceBean) obj;
				// Toast.makeText(context, service_bean.getText(),
				// Toast.LENGTH_SHORT).show();
				showToast(service_bean.getText());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showDeleteAlert(final UtilityBean utilityBean) {
		try {
			AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
			myAlertDialog.setTitle("Delete Add");
			myAlertDialog.setMessage(SingleInstance.mainContext.getResources()
					.getString(R.string.are_you_sure_you_want_to_delete));
			myAlertDialog.setPositiveButton("Delete",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							try {
								UtilityBean bean = new UtilityBean();
								bean.setType("delete");
								bean.setUsername(CallDispatcher.LoginUser);
								bean.setId(utilityBean.getId());
								bean.setUtility_name("buy");
								bean.setProduct_name(utilityBean
										.getProduct_name());
								bean.setPosted_date(utilityBean
										.getPosted_date());
								selected_utility = utilityBean;
								if (WebServiceReferences.running) {
									showprogress();
									WebServiceReferences.webServiceClient
											.getsetUtility(bean, context);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
			myAlertDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int arg1) {
							// do something when the Cancel button is clicked
							dialog.cancel();
						}
					});
			myAlertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
