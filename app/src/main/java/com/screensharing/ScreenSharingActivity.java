package com.screensharing;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ScreenShareContactAdapter;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

public class ScreenSharingActivity extends Activity {

	private TextView buddyNames = null;

	private Button back = null;

	private ScreenShareContactAdapter adapter = null;

	private ListView lv = null;

	private Context context = null;

	private CallDispatcher callDisp = null;

	private HashMap<Integer, String> selectedBuddies = new HashMap<Integer, String>();

	private Handler handler = new Handler();

	private int RES_SELECT_BUDDY = 25;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.screen_share_contact);
			context = this;

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;

			WebServiceReferences.contextTable.put("screensharecontact", context);
			buddyNames = (TextView) findViewById(R.id.buddynames);
			back = (Button) findViewById(R.id.btn_Settings);
			adapter = new ScreenShareContactAdapter(this,
					callDisp.getOnlineBuddysOnly());
			lv = (ListView) findViewById(R.id.contact_listview);
			lv.setAdapter(adapter);

			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					String userName = (String) adapter.getItem(position);
					setSelectedBuddy(userName);
				}
			});
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("screensharecontact")) {
			WebServiceReferences.contextTable.remove("screensharecontact");
		}
		super.onDestroy();
	}

	public String getBuddyNames(String buddys) {
		StringBuffer sbf = new StringBuffer();
		if (sbf.length() > 0) {
			sbf.append(",");
			sbf.append(buddys);
		} else {
			sbf.append(buddys);
		}
		System.out.println(sbf.toString());
		buddyNames.setText(sbf.toString());
		return sbf.toString();
	}

	private void setSelectedBuddy(String buddy) {
		if (buddy != null) {
			Intent intent = new Intent();
			Bundle bun = new Bundle();
			bun.putString("buddyname", buddy);
			intent.putExtra("screenshare", bun);
			setResult(RES_SELECT_BUDDY, intent);
			this.finish();
		} else {
			showToast("Please select buddy");
		}
	}

	private void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		});
	}
}
