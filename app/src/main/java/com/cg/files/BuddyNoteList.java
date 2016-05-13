package com.cg.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StatFs;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.util.SingleInstance;

/**
 * This screen will populate all the event entries made by the user, it may be
 * notes or call history. From here user can create notes and they can make the
 * calls too
 * 
 * 
 * 
 */
public class BuddyNoteList extends FragmentActivity {
	FrameLayout historyContainer;
	ViewStub viewStub;
	private String strCompleteListQuery = null;
	private Context context;
	public CompleteListBean cmp = null;
	SharedPreferences p = null;
	private com.cg.files.CompleteListView.BeatListAdapter adapter;
	private String strActionFrom;
	private Handler StopVideoHandler = new Handler();
	String buddyname = null;
	public Button IMRequest;
	private CallDispatcher callDisp;
	private Handler handleInComingAlert = new Handler();
	private Button btn_notification = null;
	private EditText filterEditText = null;
	private AlertDialog confirmation = null;
	List<CompleteListBean> tempHistoryList = null;
	private int view_mode = 0;
	private String owner;
	ListView historyListView;
	LinearLayout linearlayoutclv;
	List<CompleteListBean> historyList = null;
	private KeyguardManager keyguardManager;
	private KeyguardLock lock;
	protected PowerManager.WakeLock mWakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		WebServiceReferences.contextTable.put("buddynote", this);

		if (!WebServiceReferences.callDispatch.containsKey("calldisp")) {
			callDisp = new CallDispatcher(this);
		} else {

			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		}

		/** GetDisply Screen Height and Width */
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);
		btn_notification = (Button) findViewById(R.id.notification);
		btn_notification.setVisibility(View.GONE);
		btn_notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		IMRequest = (Button) findViewById(R.id.im);
		IMRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

		final Button btnSett = (Button) findViewById(R.id.settings);

		btnSett.setBackgroundResource(R.drawable.ic_action_back);

		btnSett.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		Bundle bndl = getIntent().getExtras();
		buddyname = bndl.getString("buddyname");
		view_mode = getIntent().getIntExtra("view_mode", 0);

		if (view_mode == 0)
			strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype from component where componenttype!='IM' and componenttype!='call' and fromuser='"
					+ buddyname + "'";
		else {
			strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype from component where componenttype!='IM' and componenttype!='call' and owner='"
					+ CallDispatcher.LoginUser + "'";
		}
		historyList = callDisp.getdbHeler(context).getCompleteListProperties(
				strCompleteListQuery);

		ShowList();

	}

	/**
	 * To generate the view when user received the im alert from them buddies
	 * 
	 * @param name
	 * @param id
	 * @param remove
	 */

	public void ShowList() {
		setContentView(R.layout.history_container);
		historyContainer = (FrameLayout) findViewById(R.id.historyContainerLayout);
		filterEditText = (EditText) findViewById(R.id.filter_text);
		filterEditText.setSingleLine();
		filterEditText.setFocusable(true);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(filterEditText.getWindowToken(), 0);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		filterEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {

				if (filterEditText.getText().toString().trim().length() == 0) {
					if (tempHistoryList != null) {
						tempHistoryList.clear();
						tempHistoryList = null;
					}
				}

				handleInComingAlert.post(new Runnable() {

					@Override
					public void run() {
						try {
							historyContainer.removeAllViews();
							tempHistoryList = new ArrayList<CompleteListBean>();
							if (historyList != null) {
								tempHistoryList.addAll(historyList);
							} else {
								historyList = callDisp.getdbHeler(context)
										.getCompleteListProperties(
												strCompleteListQuery);
								tempHistoryList.addAll(historyList);
							}

							for (int idx = 0; idx < historyList.size(); idx++) {
								CompleteListBean obj = historyList.get(idx);
								String data = "";

								data = obj.getContentName();
								if (!data.matches("(?i).*" + s.toString()
										+ ".*")) {
									tempHistoryList.remove(obj);

									if (tempHistoryList.isEmpty()) {
										Toast.makeText(context,
												SingleInstance.mainContext.getResources().getString(R.string.no_such_items),
												Toast.LENGTH_SHORT).show();
									}
								}

							}

							viewStub = new ViewStub(BuddyNoteList.this,
									R.layout.history_schedule);

							viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
								public void onInflate(ViewStub stub,
										View inflated) {

									setUIElements(inflated, tempHistoryList);
								}
							});
							historyContainer.addView(viewStub);
							viewStub.inflate();

						} catch (Exception e) {
							Log.i("callhistory", e.getMessage());
						}
					}
				});

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		setViewStub();
	}

	private void setViewStub() {

		viewStub = new ViewStub(BuddyNoteList.this, R.layout.history_schedule);
		viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
			public void onInflate(ViewStub stub, View inflated) {

				setUIElements(inflated, historyList);
			}
		});
		historyContainer.addView(viewStub);
		viewStub.inflate();
		refreshList();
	}

	public void refreshList() {

		if (view_mode == 0)
			strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype from component where componenttype!='IM' and componenttype!='call' and fromuser='"
					+ buddyname + "'";
		else
			strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype from component where componenttype!='IM' and componenttype!='call' and owner='"
					+ CallDispatcher.LoginUser + "'";
		if (historyList != null) {
			historyList.clear();
			historyList = callDisp.getdbHeler(context)
					.getCompleteListProperties(strCompleteListQuery);
			historyScheduleData.clear();
			if (historyList != null) {
				historyScheduleData.addAll(historyList);
			}
			adapter.notifyDataSetChanged();
		} else {
			historyScheduleData.clear();
			historyList = callDisp.getdbHeler(context)
					.getCompleteListProperties(strCompleteListQuery);
			if (historyList != null) {
				historyScheduleData.addAll(historyList);
			}
			adapter.notifyDataSetChanged();
		}
	}

	LinearLayout llayCmptListContainer;

	private void setUIElements(View v, List<CompleteListBean> historyLists) {

		if (v != null) {
			try {
				historyScheduleData.clear();
				historyScheduleData.addAll(historyLists);
				llayCmptListContainer = (LinearLayout) findViewById(R.id.complete_List_container);
				llayCmptListContainer.removeView(historyListView);
				llayCmptListContainer.setWeightSum(10f);
				historyListView = new ListView(context);
				historyListView.setCacheColorHint(Color.TRANSPARENT);

				Log.d("thread", "came to else.....");
				LinearLayout.LayoutParams llayListParms = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				adapter = new com.cg.files.CompleteListView.BeatListAdapter(
						context, false);
				adapter.notifyDataSetChanged();
				historyListView.setAdapter(adapter);
				llayCmptListContainer.addView(historyListView, llayListParms);
				linearlayoutclv = (LinearLayout) findViewById(R.id.linearLayoutclv);

				historyListView
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								viewComponent(arg2);

							}
						});

			} catch (Exception e) {
				e.printStackTrace();
				Log.d("thread", "came to exception....." + e.toString());
			}
		}
	}

	public static Vector<CompleteListBean> historyScheduleData = new Vector<CompleteListBean>();

	/**
	 * Show Toast message .
	 * 
	 * @param string
	 * @param i
	 */
	public void ShowToast(String string, int i) {
		if (i == 0)
			Toast.makeText(context, string, Toast.LENGTH_LONG).show();
		else
			Toast.makeText(context, string, Toast.LENGTH_LONG).show();

	}

	static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {

			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return (availableBlocks * blockSize) / 1024;

		} else {
			return -1;
		}
	}

	@Override
	protected void onDestroy() {

		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("buddynote")) {
			WebServiceReferences.contextTable.remove("buddynote");
		}
		super.onDestroy();
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		try {
			super.onDetachedFromWindow();
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			Log.i("callhistory", e.getMessage());
		}
	}

	public String getOwner() {
		return this.owner;
	}

	public void viewComponent(int position) {
		try {
			CompleteListBean clBean = historyScheduleData.get(position);
			String strQuery = "select * from component where componentid="
					+ clBean.getComponentId();
			Components obj = callDisp.getdbHeler(context)
					.getComponent(strQuery);
			clBean.setContentPath(obj.getContentPath());
			clBean.setContentName(obj.getContentName());
			clBean.setIsresponsed(Integer.toString(obj.getViewMode()));
			clBean.setReminderTime(obj.getRemDateAndTime());

			if (obj != null && obj.getcomponentType() != null) {
				long freemem = getAvailableExternalMemorySize();
				if (freemem > 0 && freemem >= 5210) {
					File fl = null;
					if (!clBean.getcomponentType().equalsIgnoreCase("video")) {
						fl = new File(clBean.getContentpath());
					} else {
						fl = new File(clBean.getContentpath() + ".mp4");
					}
					if (fl.exists()) {
						Intent intentComponent = new Intent(context,
								ComponentCreator.class);
						Bundle bndl = new Bundle();
						bndl.putString("type", clBean.getcomponentType());
						bndl.putBoolean("action", false);
						intentComponent.putExtras(bndl);
						callDisp.cmp = clBean;
						startActivity(intentComponent);

					} else {
						callDisp.showAlert(SingleInstance.mainContext.getResources().getString(R.string.component_error),SingleInstance.mainContext.getResources().getString(R.string.sorry_component_error));
					}
				} else {
					callDisp.showAlert(SingleInstance.mainContext.getResources().getString(R.string.component_error), SingleInstance.mainContext.getResources().getString(R.string.insufficient_memory));
				}

			}
		} catch (Exception e) {
			Log.i("callhistory", e.getMessage());
		}
	}

}
