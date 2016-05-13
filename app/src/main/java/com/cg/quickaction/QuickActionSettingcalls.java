package com.cg.quickaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.forms.Alert;
import com.cg.ftpprocessor.FTPBean;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class QuickActionSettingcalls extends Activity {

	Context context;
	public TextView tv_title, tv_scheduletask, trigg;
	Button btn_cancel, btn_done;
	public Button IMRequest;
	Button btn_notification;
	ListView lv_buddylist;
	String title, action, label, description, filepath, buddyname;
	public String owner;
	ImageView scheduleSetting, triggerSetting, callLogo;
	EditText et_label, etdescription;
	TextView ftpFile, fileText;
	LinearLayout ftpFileLayout;
	RelativeLayout scheduleLayout, triggerLayout, fileContainer;
	private ContactLogicbean beanObj = null;
	String fMode, freq, type;
	CallDispatcher callDisp = null;
	private Handler viewHandler = new Handler();
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calloptions_two);
		WebServiceReferences.contextTable.put("QuickActionSettingcalls", this);

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		CallDispatcher.pdialog = new ProgressDialog(context);
		et_label = (EditText) findViewById(R.id.etlabel);
		etdescription = (EditText) findViewById(R.id.et_description);
		ftpFile = (TextView) findViewById(R.id.ftpFile);
		fileContainer = (RelativeLayout) findViewById(R.id.fileContainer);
		ftpFileLayout = (LinearLayout) findViewById(R.id.ftpFileLayout);
		fileText = (TextView) findViewById(R.id.file);
		tv_title = (TextView) findViewById(R.id.tv_texttitle);
		btn_done = (Button) findViewById(R.id.btn_done);
		trigg = (TextView) findViewById(R.id.trigge);
		btn_done.setBackgroundResource(R.drawable.ic_action_save);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		scheduleSetting = (ImageView) findViewById(R.id.call_setting);
		callLogo = (ImageView) findViewById(R.id.calllogo);
		beanObj = (ContactLogicbean) getIntent().getParcelableExtra("qabean");
		Log.i("QAA", beanObj.toString());
		title = beanObj.getLabel();
		action = beanObj.getAction();
		buddyname = beanObj.getEditToUser();
		filepath = beanObj.getFtpPath();
		tv_scheduletask = (TextView) findViewById(R.id.scheduletask);
		scheduleLayout = (RelativeLayout) findViewById(R.id.rlay_schedule);
		triggerSetting = (ImageView) findViewById(R.id.trigcall_setting);
		triggerLayout = (RelativeLayout) findViewById(R.id.llayoutnew);
		if (action.equalsIgnoreCase("ABC") || action.equalsIgnoreCase("VBC")) {
			callLogo.setBackgroundResource(R.drawable.icons_broadcast);
			tv_title.setText(SingleInstance.mainContext.getResources().getString(R.string.broadcast_call));
			fileContainer.setVisibility(View.GONE);
			ftpFileLayout.setVisibility(View.GONE);
		} else if (action.equalsIgnoreCase("ST")
				|| action.equalsIgnoreCase("ST")
				|| action.equalsIgnoreCase("SP")
				|| action.equalsIgnoreCase("SA")
				|| action.equalsIgnoreCase("SV")
				|| action.equalsIgnoreCase("SHS")) {
			callLogo.setBackgroundResource(R.drawable.icons_share);
			tv_title.setText(SingleInstance.mainContext.getResources().getString(R.string.share_notes));
			fileContainer.setVisibility(View.VISIBLE);
			ftpFile.setText(filepath);
			type = getIntent().getStringExtra("type");
			ftpFileLayout.setVisibility(View.VISIBLE);
		} else if (action.equalsIgnoreCase("AC")
				|| action.equalsIgnoreCase("VC")
				|| action.equalsIgnoreCase("ACF")
				|| action.equalsIgnoreCase("VCF")
				|| action.equalsIgnoreCase("HC")) {
			callLogo.setBackgroundResource(R.drawable.icons_call);
			tv_title.setText(SingleInstance.mainContext.getResources().getString(R.string.start_a_call));
			fileContainer.setVisibility(View.GONE);
			ftpFileLayout.setVisibility(View.GONE);
		} else if (action.equalsIgnoreCase("Show Results Form")) {
			callLogo.setBackgroundResource(R.drawable.icons_report);
			tv_title.setText(SingleInstance.mainContext.getResources().getString(R.string.generate_report));
			fileContainer.setVisibility(View.GONE);
			ftpFileLayout.setVisibility(View.GONE);

		}

		if (CallDispatcher.triggerQuery.toString().length() > 0) {
			trigg.setText(CallDispatcher.triggerQuery);

		}

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		btn_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.i("QAA", "action===>" + action);
				if (!action.equalsIgnoreCase("Show results form")) {

					validateQA();

				} else {

					if (!trigg.getText().toString().startsWith("No")) {
						validateQA();

					} else {

						Toast.makeText(context,
								SingleInstance.mainContext.getResources().getString(R.string.trigger_configure_is_mandatory),
								Toast.LENGTH_LONG).show();
					}
				}

			}
		});
		fileContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.i("QAA", "Edit Note Type==>" + type);
					Intent i = new Intent(context, ShareNotePicker.class);
					i.putExtra("qabean", beanObj);
					i.putExtra("editFile", true);
					i.putExtra("note", type);
					startActivityForResult(i, 13);
					return true;
				} else {
					return false;
				}

			}
		});
		scheduleLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					label = et_label.getText().toString().trim();
					description = etdescription.getText().toString().trim();
					Log.i("welcome", "Label printing-->" + label);
					Log.i("welcome", "description printing-->" + description);
					Intent intent = new Intent(context,
							QuickActionCallsSchedule.class);
					intent.putExtra("mode", "New");
					intent.putExtra("title", title);
					intent.putExtra("action", action);
					intent.putExtra("buddyname", buddyname);
					intent.putExtra("label", label);
					intent.putExtra("description", description);
					intent.putExtra("qabean", beanObj);
					startActivityForResult(intent, 3);
					return true;
				} else {
					return false;
				}

			}
		});

		scheduleSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				label = et_label.getText().toString().trim();
				description = etdescription.getText().toString().trim();
				Log.i("welcome", "Label printing-->" + label);
				Log.i("welcome", "description printing-->" + description);
				Intent intent = new Intent(context,
						QuickActionCallsSchedule.class);
				intent.putExtra("mode", "New");
				intent.putExtra("title", title);
				intent.putExtra("action", action);
				intent.putExtra("buddyname", buddyname);
				intent.putExtra("label", label);
				intent.putExtra("description", description);
				intent.putExtra("qabean", beanObj);
				startActivityForResult(intent, 3);
			}
		});

		triggerSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DBAccess dbAccess = DBAccess.getdbHeler();
				int formsize = dbAccess.LookUpRecords(null,
						CallDispatcher.LoginUser).size();
				if (formsize > 0) {
					Intent i = new Intent(context, TriggerMain.class);
					i.putExtra("report", false);
					i.putExtra("qabean", beanObj);
					startActivityForResult(i, 5);
				} else if (formsize == 0) {
					Toast.makeText(context, "No forms are created",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		triggerLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				DBAccess dbAccess = DBAccess.getdbHeler(context);
				int formsize = dbAccess.LookUpRecords(null,
						CallDispatcher.LoginUser).size();
				if (formsize > 0) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						Intent i = new Intent(context, TriggerMain.class);
						i.putExtra("report", false);
						i.putExtra("qabean", beanObj);
						startActivityForResult(i, 5);
						return true;
					} else {
						return false;
					}
				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.no_forms_are_created),
							Toast.LENGTH_SHORT).show();
				}

				return false;
			}
		});

		//
		// // Intent intent= new Intent(context,QuickActionCallsSchedule.class);
		// // intent.putExtra("mode","New");
		// // intent.putExtra("title",title);
		// // intent.putExtra("action", action);
		// // intent.putExtra("buddyname",buddyname);
		// // intent.putExtra("label",label);
		// // intent.putExtra("description",description);
		// // intent.putExtra("qabean", beanObj);
		// // startActivity(intent);
		// // finish();
		// return true;
		// }
		// });

		btn_notification = (Button) findViewById(R.id.notification);
		btn_notification.setVisibility(View.GONE);
		btn_notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		IMRequest = (Button) findViewById(R.id.im);

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.one);

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    public void validateQA() {

		if (!et_label.getText().toString().trim().equals("")) {

			beanObj.setEditlable(et_label.getText().toString());
			beanObj.setDesc(etdescription.getText().toString());
			beanObj.setSchedule(tv_scheduletask.getText().toString());
			beanObj.setEdittime(tv_scheduletask.getText().toString());
			beanObj.setModeTime(fMode);
			beanObj.setFreqMode(freq);
			beanObj.setEditToUser(buddyname);
			beanObj.setFtpPath(filepath);
			beanObj.setAction(action);
			beanObj.setIssave(true);

			if (CallDispatcher.timeMode.equalsIgnoreCase("manually")) {
				beanObj.setRunMode("RM");
			} else if (CallDispatcher.timeMode.equalsIgnoreCase("specific")) {
				beanObj.setRunMode("RS");
			} else if (CallDispatcher.timeMode.equalsIgnoreCase("Repeat")) {
				beanObj.setRunMode("RP");
			}
			beanObj.setRunMode(CallDispatcher.timeMode);
			beanObj.setNew(true);
			if (!trigg.getText().toString().startsWith("No")) {
				beanObj.setEditconditon(trigg.getText().toString());
			} else {
				beanObj.setEditconditon("");

			}

			if (filepath != null && filepath.contains("COMMedia"))
				uploadConfiguredNote(false);
			else {

				callDisp.getdbHeler(context).updateSecLogicbean(beanObj,
						"", 0);
				String id = callDisp.getdbHeler(context).getquickactionid();
				Alert.writeString(getApplicationContext(), Alert.id, id);
				Intent intent = new Intent(context, QuickActionTitlecalls.class);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("edit", false);
				intent.putExtra("type", type);
				intent.putExtra("qid", id);
				startActivity(intent);
				finish();
			}

		} else {
			Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.kindly_enter_the_title),
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		WebServiceReferences.contextTable.remove("QuickActionSettingcalls");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
				}
			} catch (NullPointerException e) {

			} catch (Exception e) {

			}
		}
		if ((requestCode == 5) && (resultCode == -5)) {
			try {
				Bundle bun = data.getBundleExtra("quick");
				if (bun != null) {
					String condition = bun.getString("query");
					condition = CallDispatcher.triggerQuery;
					Log.i("Condition::", "condition ::" + condition);
					if (condition != null || condition != "") {
						trigg.setText(condition);
						beanObj.setCondition(condition);
					}
				}
			} catch (NullPointerException e) {

			} catch (Exception e) {

			}
		}
	}

	public void changeQuery(final String query) {
		viewHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("QAA", "Query :: " + query);
				trigg.setText(query);

			}
		});
	}

	private void uploadConfiguredNote(boolean runNow) {
		// TODO Auto-generated method stub
		if (filepath.trim().length() > 0) {
			if (CallDispatcher.LoginUser != null) {
				AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
						.get("MAIN");
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

	public void saveQA(boolean isNew, boolean save, String filename,
			ContactLogicbean bLogic) {
		if (isNew && save) {
			callDisp.getdbHeler(SipNotificationListener.getCurrentContext())
					.updateSecLogicbean(bLogic, filename, 0);

			String id = callDisp.getdbHeler(
					SipNotificationListener.getCurrentContext())
					.getquickactionid();
			Alert.writeString(SipNotificationListener.getCurrentContext(),
					Alert.id, id);
			callDisp.cancelDialog();
			Intent intent = new Intent(
					SipNotificationListener.getCurrentContext(),
					QuickActionTitlecalls.class);
			bLogic.setFtpPath(filename);
			intent.putExtra("qabean", bLogic);
			intent.putExtra("edit", false);
			intent.putExtra("type", type);
			intent.putExtra("qid", id);
			startActivity(intent);
			finish();
		}
	}
}
