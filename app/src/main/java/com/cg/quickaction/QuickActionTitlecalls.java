package com.cg.quickaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
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
import com.cg.commonclass.WebServiceReferences;
import com.cg.forms.Alert;
import com.cg.ftpprocessor.FTPBean;
import com.main.AppMainActivity;
import com.main.QuickActionFragment;
import com.util.SingleInstance;

public class QuickActionTitlecalls extends Activity {

	Context context;
	TextView tv_title, tv_username, tv_scheduletask, touser, trigg;
	ListView lv_buddylist;
	String title, action, buddyname, label, description, frequency, time,
			condition, timemode, filepath, type;
	public String owner;
	RelativeLayout scheduleLayout, triggerLayout, fileContainer;
	TextView ftpFile, fileText;
	LinearLayout ftpFileLayout;
	EditText et_label, etdescription, et_title_text;
	ImageView btn_back, btn_sett, schedule, triger, editToUser, callLogo;
	ScrollView options;
	private ContactLogicbean beanObj = new ContactLogicbean();
	CallDispatcher callDisp = null;
	String Times_Mode = null;
	String fMode, freq, runMode, noofTimes, modes;
	// String id = null;
	boolean editQA;
	Button edit, cancel, run, delete, slideButton;
	public Button IMRequest;
	Button btn_notification;
	private SharedPreferences preferences;
	private LinearLayout contact_container;
	private Handler handler = new Handler();
	String TextDesc, TextContact, TextFilepath, TextSchedule, TextTrigger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			editQA = getIntent().getExtras().getBoolean("edit", false);
			context = this;
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.calloptions_three);
			options = (ScrollView) findViewById(R.id.scroller_opt);
			options.setVisibility(View.GONE);
			WebServiceReferences.contextTable.put("QuickActionTitlecalls",
					context);
			CallDispatcher.pdialog = new ProgressDialog(context);
			etdescription = (EditText) findViewById(R.id.et_description);
			tv_username = (TextView) findViewById(R.id.username);
			ftpFile = (TextView) findViewById(R.id.ftpFile);
			fileContainer = (RelativeLayout) findViewById(R.id.fileContainer);
			ftpFileLayout = (LinearLayout) findViewById(R.id.ftpFileLayout);
			fileText = (TextView) findViewById(R.id.file);
			touser = (TextView) findViewById(R.id.et_touser);
			schedule = (ImageView) findViewById(R.id.tri_call_setting);
			triger = (ImageView) findViewById(R.id.call_setting);
			btn_sett = (ImageView) findViewById(R.id.btn_sett);
			btn_back = (ImageView) findViewById(R.id.btn_back);
			run = (Button) findViewById(R.id.btn_run);
			cancel = (Button) findViewById(R.id.btn_cancels);
			edit = (Button) findViewById(R.id.btn_edit);
			delete = (Button) findViewById(R.id.btn_delete);
			slideButton = (Button) findViewById(R.id.slideButton);
			scheduleLayout = (RelativeLayout) findViewById(R.id.llayoutone);
			scheduleLayout.setFocusableInTouchMode(false);
			trigg = (TextView) findViewById(R.id.trigg);
			schedule.setClickable(false);
			triggerLayout = (RelativeLayout) findViewById(R.id.llayoutnew);
			tv_scheduletask = (TextView) findViewById(R.id.scheduletask);
			editToUser = (ImageView) findViewById(R.id.selectCall);
			callLogo = (ImageView) findViewById(R.id.calllogo);
			tv_title = (TextView) findViewById(R.id.subtitle);
			contact_container = (LinearLayout) findViewById(R.id.contact_container);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;
			btn_sett.setVisibility(View.GONE);
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			if (editQA) {
				touser.setEnabled(false);
				schedule.setEnabled(false);
				scheduleLayout.setEnabled(false);
				editToUser.setEnabled(false);
				fileContainer.setEnabled(false);
				triggerLayout.setEnabled(false);
				triger.setEnabled(false);

				beanObj = (ContactLogicbean) getIntent().getExtras()
						.getParcelable("quickAction");
				tv_username.setText(beanObj.getEditlable());
				if (beanObj.gettimeMode() != null
						|| beanObj.gettimeMode().length() > 0
						|| beanObj.getFrequncy() != null
						|| beanObj.getFrequncy().length() > 0) {
					tv_scheduletask.setText(beanObj.getEdittime());
					noofTimes = beanObj.getModeTime();
					modes = beanObj.getFreqMode();
				} else {
					tv_scheduletask.setText(SingleInstance.mainContext
							.getResources().getString(
									R.string.this_task_will_run_manually));
				}

				condition = beanObj.getEditconditon();
				touser.setText(beanObj.getEditToUser());
				fMode = beanObj.getModeTime();
				freq = beanObj.getFreqMode();
				filepath = beanObj.getFtpPath();
				etdescription.setText(beanObj.getDesc());
				Log.i("description btn", "1111111");

				action = beanObj.getAction();
				runMode = beanObj.getRunMode();
				if (action.equalsIgnoreCase("ST")) {
					type = "note";
				} else if (action.equalsIgnoreCase("SA")) {
					type = "audio";
				} else if (action.equalsIgnoreCase("SP")) {
					type = "photo";
				} else if (action.equalsIgnoreCase("SV")) {
					type = "video";
				} else if (action.equalsIgnoreCase("SHS")) {
					type = "sketch";
				}
				if (action.equalsIgnoreCase("ABC")
						|| action.equalsIgnoreCase("VBC")) {
					callLogo.setBackgroundResource(R.drawable.icons_broadcast);
					tv_title.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.broadcast_call));

				} else if (action.equalsIgnoreCase("ST")
						|| action.equalsIgnoreCase("ST")
						|| action.equalsIgnoreCase("SP")
						|| action.equalsIgnoreCase("SA")
						|| action.equalsIgnoreCase("SV")
						|| action.equalsIgnoreCase("SHS")) {
					callLogo.setBackgroundResource(R.drawable.icons_share);
					tv_title.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.share_notes));
					fileContainer.setVisibility(View.VISIBLE);
					ftpFile.setText(filepath);
					ftpFileLayout.setVisibility(View.VISIBLE);
				} else if (action.equalsIgnoreCase("AC")
						|| action.equalsIgnoreCase("VC")
						|| action.equalsIgnoreCase("ACF")
						|| action.equalsIgnoreCase("VCF")
						|| action.equalsIgnoreCase("HC")) {
					callLogo.setBackgroundResource(R.drawable.icons_call);
					tv_title.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.start_a_call));
					fileContainer.setVisibility(View.GONE);
					ftpFileLayout.setVisibility(View.GONE);
				} else if (action.equalsIgnoreCase("Show Results Form")) {
					callLogo.setBackgroundResource(R.drawable.icons_report);
					tv_title.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.generate_report));
					fileContainer.setVisibility(View.GONE);
					ftpFileLayout.setVisibility(View.GONE);
					contact_container.setVisibility(View.GONE);
				}
			} else {
				beanObj = (ContactLogicbean) getIntent().getExtras()
						.getParcelable("qabean");
				tv_username.setText(beanObj.getEditlable());
				fMode = beanObj.getModeTime();
				freq = beanObj.getFreqMode();
				touser.setText(beanObj.getEditToUser());
				filepath = beanObj.getFtpPath();
				action = beanObj.getAction();
				condition = beanObj.getEditconditon();
				runMode = beanObj.getRunMode();
				etdescription.setText(beanObj.getDesc());
				tv_scheduletask.setText(beanObj.getSchedule());
				noofTimes = beanObj.getFreqMode();
				modes = beanObj.getModeTime();

				if (action.equalsIgnoreCase("ABC")
						|| action.equalsIgnoreCase("VBC")) {
					callLogo.setBackgroundResource(R.drawable.icons_broadcast);
					tv_title.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.broadcast_call));

				} else if (action.equalsIgnoreCase("ST")
						|| action.equalsIgnoreCase("ST")
						|| action.equalsIgnoreCase("SP")
						|| action.equalsIgnoreCase("SA")
						|| action.equalsIgnoreCase("SV")
						|| action.equalsIgnoreCase("SHS")) {
					callLogo.setBackgroundResource(R.drawable.icons_share);
					tv_title.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.share_notes));
					fileContainer.setVisibility(View.VISIBLE);
					ftpFile.setText(filepath);
					type = getIntent().getStringExtra("type");
					ftpFileLayout.setVisibility(View.VISIBLE);
				} else if (action.equalsIgnoreCase("AC")
						|| action.equalsIgnoreCase("VC")
						|| action.equalsIgnoreCase("HC")
						|| action.equalsIgnoreCase("ACF")
						|| action.equalsIgnoreCase("VCF")) {
					callLogo.setBackgroundResource(R.drawable.icons_call);
					tv_title.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.start_a_call));
					fileContainer.setVisibility(View.GONE);
					ftpFileLayout.setVisibility(View.GONE);
				} else if (action.equalsIgnoreCase("Show Results Form")) {
					callLogo.setBackgroundResource(R.drawable.icons_report);
					tv_title.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.generate_report));
					fileContainer.setVisibility(View.GONE);
					ftpFileLayout.setVisibility(View.GONE);
					contact_container.setVisibility(View.GONE);

				}
			}
			if (condition.length() > 0) {
				trigg.setText(condition);
			}

			slideButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (options.getVisibility() == View.GONE) {
						options.setVisibility(View.VISIBLE);
						slideButton.setVisibility(View.GONE);
					} else {
						options.setVisibility(View.GONE);
						slideButton.setText(SingleInstance.mainContext
								.getResources().getString(R.string.run_now));
					}
				}
			});
			run.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// beanObj.setEditlable(tv_username.getText().toString());
					// beanObj.setDesc(etdescription.getText().toString());
					// Log.i("QAA", "Buddy Before ===>"
					// + touser.getText().toString());
					// beanObj.setEditToUser(touser.getText().toString());
					// Log.i("QAA", "Buddy After===>" +
					// beanObj.getEditToUser());
					// beanObj.setModeTime(fMode);
					// beanObj.setFreqMode(freq);
					// beanObj.setEditconditon(trigg.getText().toString());
					// beanObj.setFtpPath(filepath);
					// beanObj.setSchedule(tv_scheduletask.getText().toString());
					// beanObj.setNew(false);
					// beanObj.setEdit(editQA);
					// if (filepath != null && filepath.contains("COMMedia")) {
					// beanObj.setRunNow(true);
					// if (editQA)
					// uploadConfiguredNote(true);
					// } else {
					// saveAndExceuteQuickAction("", true);
					// }
					updateQuickAction(true);

				}
			});
			triger.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {

						DBAccess dbAccess = DBAccess.getdbHeler();
						int formsize = dbAccess.LookUpRecords(null,
								CallDispatcher.LoginUser).size();
						if (formsize > 0) {
							// TODO Auto-generated method stub
							Intent i = new Intent(context, TriggerMain.class);
							i.putExtra("report", false);
							i.putExtra("qabean", beanObj);
							startActivityForResult(i, 5);
						} else {
							Toast.makeText(
									context,
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.no_forms_are_created),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}

				}
			});
			triggerLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					try {
						// TODO Auto-generated method stub
						DBAccess dbAccess = DBAccess.getdbHeler(context);
						int formsize = dbAccess.LookUpRecords(null,
								CallDispatcher.LoginUser).size();
						if (formsize > 0) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								Intent i = new Intent(context,
										TriggerMain.class);
								i.putExtra("report", false);
								i.putExtra("qabean", beanObj);
								startActivityForResult(i, 5);
								return true;
							} else {
								return false;
							}
						} else {
							Toast.makeText(
									context,
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.no_forms_are_created),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					return false;
				}

			});

			schedule.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						boolean editSchedule = true;
						Intent i = new Intent(context,
								QuickActionCallsSchedule.class);
						i.putExtra("editSchedule", editSchedule);
						beanObj.setModeTime(noofTimes);
						beanObj.setFreqMode(modes);
						beanObj.setEdittime(tv_scheduletask.getText()
								.toString());
						i.putExtra("qabean", beanObj);
						startActivityForResult(i, 3);

						Log.i("schdule btn", "inside sch");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			scheduleLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					try {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							boolean editSchedule = true;
							Intent i = new Intent(context,
									QuickActionCallsSchedule.class);
							i.putExtra("editSchedule", editSchedule);
							beanObj.setModeTime(noofTimes);
							beanObj.setFreqMode(modes);
							beanObj.setEdittime(tv_scheduletask.getText()
									.toString());
							i.putExtra("qabean", beanObj);
							startActivityForResult(i, 3);
							return true;
						} else {
							return false;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}

				}
			});
			fileContainer.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					try {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							Log.i("QAA", "Edit Note Type==>" + type);
							Intent i = new Intent(context,
									ShareNotePicker.class);
							i.putExtra("qabean", beanObj);
							i.putExtra("editFile", true);
							i.putExtra("note", type);
							startActivityForResult(i, 13);
							return true;
						} else {
							return false;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}

				}
			});

			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						boolean flag = false;
						// TODO Auto-generated method stub
						Log.d("Test", "Inside Edit");
						Log.d("Test", "description"
								+ etdescription.getText().toString());
						Log.d("Test", " Contact+++++++"
								+ touser.getText().toString());

						Log.d("Test", "Filepath+++++" + beanObj.getFtpPath());

						Log.d("Test", "Schedule++++++"
								+ tv_scheduletask.getText().toString());
						Log.d("Test", "Trigger+++++++"
								+ trigg.getText().toString());
						TextDesc = etdescription.getText().toString();
						TextContact = touser.getText().toString();
						TextFilepath = beanObj.getFtpPath();
						TextSchedule = ""
								+ tv_scheduletask.getText().toString();
						TextTrigger = trigg.getText().toString();
						btn_sett.setVisibility(View.VISIBLE);

						options.setVisibility(View.GONE);
						etdescription.setFocusableInTouchMode(true);
						touser.setEnabled(true);
						schedule.setEnabled(true);
						scheduleLayout.setEnabled(true);
						editToUser.setEnabled(true);
						fileContainer.setEnabled(true);
						triggerLayout.setEnabled(true);
						triger.setEnabled(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					slideButton.setVisibility(View.VISIBLE);
					options.setVisibility(View.GONE);
				}
			});
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						
						QuickActionFragment quickActionFragment = QuickActionFragment
								.newInstance(context);
						quickActionFragment.doDeleteQA(beanObj.getId(), context);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			btn_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						updateQuickAction(false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			btn_sett.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// try {
					// beanObj.setLabel(tv_username.getText().toString());
					// beanObj.setDesc(etdescription.getText().toString());
					// beanObj.setEditToUser(touser.getText().toString());
					// beanObj.setModeTime(fMode);
					// beanObj.setFreqMode(freq);
					// beanObj.setFtpPath(filepath);
					// beanObj.setSchedule(tv_scheduletask.getText()
					// .toString());
					// beanObj.setNew(false);
					// beanObj.setEditconditon(trigg.getText().toString());
					// beanObj.setEdit(editQA);
					// if (filepath != null && filepath.contains("COMMedia")) {
					// beanObj.setRunNow(false);
					// if (editQA)
					// uploadConfiguredNote(false);
					// } else {
					// saveAndExceuteQuickAction("", false);
					// }
					//
					// } catch (Exception e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					updateQuickAction(false);
					QuickActionFragment quickActionFragment = QuickActionFragment
							.newInstance(context);
					quickActionFragment.loadBussinesDatas();

				}
			});
			editToUser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					if (WebServiceReferences.contextTable
							.containsKey("QuickActionSelectcalls")) {
						((QuickActionSelectcalls) WebServiceReferences.contextTable
								.get("QuickActionSelectcalls")).finish();
					}

					// TODO Auto-generated method stub
					Log.i("QAA", "Action==>" + action);
					Intent i = new Intent(context, QuickActionSelectcalls.class);
					i.putExtra("editUser", true);
					i.putExtra("buddies", touser.getText().toString());
					i.putExtra("action", action);
					startActivityForResult(i, 15);
				}
			});

			btn_notification = (Button) findViewById(R.id.notification);
			btn_notification.setVisibility(View.GONE);

			IMRequest = (Button) findViewById(R.id.im);

			IMRequest.setVisibility(View.INVISIBLE);
			IMRequest.setBackgroundResource(R.drawable.one);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if ((requestCode == 3) && (resultCode == -3)) {
				try {
					Bundle bun = data.getBundleExtra("quick");
					if (bun != null) {
						String schedule = bun.getString("schedule");
						fMode = bun.getString("mode");
						freq = bun.getString("freq");
						tv_scheduletask.setText(schedule);
						beanObj.setSchedule(schedule);

					}
				} catch (NullPointerException e) {

				} catch (Exception e) {

				}
			}
			if ((requestCode == 5) && (resultCode == -5)) {
				try {
					String condition = CallDispatcher.triggerQuery;
					Log.i("Condition::", "condition ::" + condition);
					if (condition != null || condition != "") {
						trigg.setText(condition);
						beanObj.setEditconditon(condition);
					}

				} catch (NullPointerException e) {

				} catch (Exception e) {

				}
			}
			if ((requestCode == 13) && (resultCode == -13)) {
				try {
					Bundle bun = data.getBundleExtra("ftp");
					if (bun != null) {
						String filePaths = bun.getString("filepath");
						Log.i("filePath::", "filePath ::" + filePaths);
						if (filePaths != null || filePaths != "") {
							ftpFile.setText(filePaths);
							filepath = filePaths;
							beanObj.setFtpPath(filePaths);
						}
					}
				} catch (NullPointerException e) {

				} catch (Exception e) {

				}
			}
			if ((requestCode == 15) && (resultCode == -15)) {
				try {
					Bundle bun = data.getBundleExtra("quick");
					if (bun != null) {
						String selectedUser = bun.getString("selectedUser");
						Log.i("QAA::", "Selected User ::" + selectedUser);
						if (selectedUser != null || selectedUser != "") {
							touser.setText(selectedUser);
							beanObj.setTouser(selectedUser);

						}
					}
				} catch (NullPointerException e) {

				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uploadConfiguredNote(boolean runNow) {
		// TODO Auto-generated method stub
		if (filepath.trim().length() > 0) {
			AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN");

			if (CallDispatcher.LoginUser != null) {
				String username = CallDispatcher.LoginUser;
				String password = CallDispatcher.Password;
				if (username != null && password != null) {
					callDisp.showprogress(CallDispatcher.pdialog, context);
					FTPBean bean = new FTPBean();
					FTPBean ftpBean = new FTPBean();
					ftpBean.setServer_ip(appMainActivity.cBean.getRouter()
							.split(":")[0]);
					ftpBean.setServer_port(40400);
					ftpBean.setFtp_username(username);
					ftpBean.setFtp_password(password);
					bean.setRunNow(runNow);
					bean.setBeanObj(beanObj);
					bean.setNoteType(type);
					if (!action.equalsIgnoreCase("SV")) {
						filepath = filepath.trim();
					} else {
						if (!filepath.contains(".mp4"))
							filepath = filepath.trim() + ".mp4";
					}
					ftpBean.setFile_path(filepath);
					ftpBean.setOperation_type(1);
					ftpBean.setReq_object(beanObj);
					ftpBean.setRequest_from("quickaction");

					if (appMainActivity.getFtpNotifier() != null)
						appMainActivity.getFtpNotifier().addTasktoExecutor(
								ftpBean);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		try {
			// TODO Auto-generated method stub
			super.onDestroy();
			CallDispatcher.triggerQuery = "";
			WebServiceReferences.contextTable.remove("QuickActionTitlecalls");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveAndExceuteQuickAction(String ftpPath, boolean runNow) {
		int id = 0;
		int qId = getIntent().getIntExtra("qid", 0);
		if (qId > 0) {
			id = qId;
		} else {
			id = beanObj.getId();
		}
		callDisp.getdbHeler(context).updateSecLogicbean(beanObj, ftpPath,
				id);
		Log.i("FTPLOG", "value" + beanObj.getId());
		Alert.writeString(getApplicationContext(), Alert.id,
				"" + beanObj.getId());
		CallDispatcher.triggerQuery = "";
		if ((ftpPath != null && !ftpPath.contains("COMMedia"))
				|| (action.equalsIgnoreCase("Show Results Form"))
				|| (action.equalsIgnoreCase("ABC"))
				|| (action.equalsIgnoreCase("VBC"))
				|| (action.equalsIgnoreCase("AC"))
				|| (action.equalsIgnoreCase("VC"))
				|| (action.equalsIgnoreCase("ACF"))
				|| (action.equalsIgnoreCase("VCF"))
				|| (action.equalsIgnoreCase("HC"))) {

			Log.i("QAA", "FilePath::====>" + ftpPath);
			if (runNow) {
				callDisp.doAction(beanObj.getEditlable(),
						beanObj.getfromuser(), beanObj.getEditToUser(),
						ftpPath, beanObj.getcontent(), beanObj.getAction(),
						beanObj.getEditconditon());
			}
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					QuickActionFragment quickActionFragment = QuickActionFragment
							.newInstance(context);
					quickActionFragment.loadBussinesDatas();
				}
			});
			if (WebServiceReferences.contextTable
					.containsKey("QuickActionSelectcalls")) {
				((QuickActionSelectcalls) WebServiceReferences.contextTable
						.get("QuickActionSelectcalls")).finish();
			}
			if (WebServiceReferences.contextTable
					.containsKey("sharenotepicker")) {
				((ShareNotePicker) WebServiceReferences.contextTable
						.get("sharenotepicker")).finish();
			}
			if (!action.equalsIgnoreCase("Show Results Form"))
				callDisp.cancelDialog();
			finish();
		}
	}

	public void changeQuery(final String query) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				trigg.setText(query);

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			updateQuickAction(false);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void updateQuickAction(boolean runNow) {
		try {
			boolean flag = false;
			if (filepath != null && !filepath.contains("COMMedia")) {
				if (!etdescription.getText().toString().equals(TextDesc)
						|| !touser.getText().toString().equals(TextContact)
						|| !beanObj.getFtpPath().equals(TextFilepath)
						|| !tv_scheduletask.getText().toString()
								.equals(TextSchedule)
						|| !trigg.getText().toString().equals(TextTrigger)) {
					flag = true;
				}
			}else
			{
				if (!etdescription.getText().toString().equals(TextDesc)
						|| !touser.getText().toString().equals(TextContact)
						|| !tv_scheduletask.getText().toString()
								.equals(TextSchedule)
						|| !trigg.getText().toString().equals(TextTrigger)) {
					flag = true;
				}
			}
			if (flag == true) {

				try {
					beanObj.setLabel(tv_username.getText().toString());

					if (beanObj.getDesc() != null
							&& beanObj.getDesc().length() > 0) {
						Log.i("QA", "beanObj.getDesc()" + beanObj.getDesc());

						beanObj.getDesc().replace(beanObj.getDesc(),
								etdescription.getText().toString());
						Log.i("QA", "beanObj.getDesc()" + beanObj.getDesc());

					}
					// String
					// TextDesc,TextContact,TextFilepath,TextSchedule,TextTrigger;

					beanObj.setDesc(etdescription.getText().toString());
					Log.i("QA", "description"
							+ etdescription.getText().toString());
					Log.d("Test", "Inside updateQuickAction");
					Log.d("Test", "description"
							+ etdescription.getText().toString());
					Log.d("Test", " Contact+++++++"
							+ touser.getText().toString());

					Log.d("Test", "Filepath+++++" + beanObj.getFtpPath());

					Log.d("Test", "Schedule++++++"
							+ tv_scheduletask.getText().toString());
					Log.d("Test", "Trigger++++++++"
							+ trigg.getText().toString());
					beanObj.setEditToUser(touser.getText().toString());
					beanObj.setModeTime(fMode);
					beanObj.setFreqMode(freq);
					beanObj.setFtpPath(filepath);
					beanObj.setSchedule(tv_scheduletask.getText().toString());
					beanObj.setNew(false);
					beanObj.setEditconditon(trigg.getText().toString());
					editQA = true;
					beanObj.setEdit(editQA);
					beanObj.setIssave(true);
					if (filepath != null && filepath.contains("COMMedia")) {
						beanObj.setRunNow(runNow);
						if (editQA)
							uploadConfiguredNote(runNow);
					} else {
						saveAndExceuteQuickAction(filepath, runNow);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "No Details Edited",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
