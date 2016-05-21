package com.callHistory;

import java.io.File;
import java.util.ArrayList;

import org.lib.model.RecordTransactionBean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class CallHistoryActivity extends Activity {
	private Context context;

	Handler handler = new Handler();
	private String centername = null;
	String product = null;
	private CallHistoryAdapter callHistoryAdapter;
	private ArrayList<RecordTransactionBean> mlist;
	public static boolean isEdit = false;

	public static String selectedBuddy;
	private String sessionId = "";
	private int position;
	private CallDispatcher callDisp;
	private Boolean isViewed=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Log.i("Test", "CallHistoryActivity>>>>>>>>>>");
			context = this;
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.call_history_row);
			if (SingleInstance.mainContext
					.getResources()
					.getString(R.string.screenshot)
					.equalsIgnoreCase(
							SingleInstance.mainContext.getResources()
									.getString(R.string.yes))) {
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
						WindowManager.LayoutParams.FLAG_SECURE);
			}
			WebServiceReferences.contextTable.put("ordermenuactivity", this);
			mlist = new ArrayList<RecordTransactionBean>();
			ImageView back = (ImageView) findViewById(R.id.back_button);
			TextView calltype = (TextView) findViewById(R.id.ctype_title);

			TextView callcontent = (TextView) findViewById(R.id.ccontent_title);

			TextView from = (TextView) findViewById(R.id.from_title);
			TextView to = (TextView) findViewById(R.id.touser_title);
			TextView date = (TextView) findViewById(R.id.date_title);
			TextView duration = (TextView) findViewById(R.id.duration_title);

			TextView callstate = (TextView) findViewById(R.id.cstate_title);
			ImageView preview = (ImageView) findViewById(R.id.play_button);

			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SingleInstance.orderToastShow = false;
					// TODO Auto-generated method stub
					finish();
				}
			});

			sessionId = getIntent().getStringExtra("sessionid");
			isViewed=getIntent().getBooleanExtra("isviewed",false);
		
			position = 0;

			try {
				String query = "";

				query = "select * from recordtransactiondetails where sessionid='"
						+ sessionId + "'";

				Log.i("CH", "Query" + query);
				mlist = DBAccess.getdbHeler().getcallhistorydetails(query);
				RecordTransactionBean recordTransactionBean = mlist
						.get(position);

				if (recordTransactionBean.getCalltype() != null) {
					String callType = "";
					if (recordTransactionBean.getCalltype().equalsIgnoreCase(
							"AC")) {
						callType = "Audio Call";
					} else if (recordTransactionBean.getCalltype()
							.equalsIgnoreCase("VC")) {
						callType = "Video Call";
					} else if (recordTransactionBean.getCalltype()
							.equalsIgnoreCase("ABC")) {
						callType = "Audio Broadcast";
					} else if (recordTransactionBean.getCalltype()
							.equalsIgnoreCase("VBC")) {
						callType = "Video Broadcast";
					} else if (recordTransactionBean.getCalltype()
							.equalsIgnoreCase("AP")) {
						callType = "Audio Unicast";
					} else if (recordTransactionBean.getCalltype()
							.equalsIgnoreCase("VP")) {
						callType = "Video Unicast";
					}
					calltype.setText(callType);
				}
				if (recordTransactionBean.getCalltype() != null) {
					callcontent.setText(recordTransactionBean.getCalltype());
				}
				if (recordTransactionBean.getFromName() != null) {
					from.setText(recordTransactionBean.getFromName());
				}
				if (recordTransactionBean.getToName() != null) {
					to.setText(recordTransactionBean.getToName());
				}
				if (recordTransactionBean.getStartTime() != null) {
					date.setText(recordTransactionBean.getStartTime());
				}
				if (recordTransactionBean.getCallDuration() != null) {					
						duration.setText(recordTransactionBean
								.getCallDuration());

					}
				
				if (recordTransactionBean.getSessionid() != null) {
					preview.setTag(recordTransactionBean.getSessionid());
				}

				if(isViewed){
					String strUpdateNote = "update recordtransactiondetails set status='" + 1
							+ "' where sessionid='" + sessionId
							+ "'";
					if (DBAccess.getdbHeler().ExecuteQuery(strUpdateNote)) {
						recordTransactionBean.setStatus(1);
					}
				}

				preview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							File file = new File(Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/CallRecording/"
									+ v.getTag().toString() + ".wav");
							int CountFiles = new File(Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/CallRecording/").listFiles().length;
							Log.d("Test", "Length of the files@@@----->"
									+ CountFiles);

							if (file.exists()) {
								Intent intent = new Intent(context,
										MultimediaUtils.class);
								intent.putExtra("filePath", file.getPath());
								intent.putExtra("requestCode", 4);
								intent.putExtra("action", "audio");
								intent.putExtra("createOrOpen", "open");
								context.startActivity(intent);
							} else {
								// Toast.makeText(context,
								// "Sorry file not available",
								// Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    public int getCount() {
		return mlist.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		RecordTransactionBean bean = (RecordTransactionBean) mlist.get(arg0);
		return bean;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
