package com.cg.hostedconf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.lib.model.BuddyInformationBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class ContactConference extends Activity {

	private TextView buddyNames = null;

	public Button audioCall = null;

	private Button aConf = null;

	private Button vConf = null;

	private Button back = null;
	private Button videoConf=null;

	private ContactConferenceAdapter adapter = null;

	private ListView lv = null;

	public LinearLayout callContainer = null;

	private Context context = null;

	private CallDispatcher callDisp = null;

	private HashMap<Integer, String> selectedBuddies = new HashMap<Integer, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.conferencecontact);
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

		WebServiceReferences.contextTable.put("contactconf", context);
		buddyNames = (TextView) findViewById(R.id.buddynames);
		audioCall = (Button) findViewById(R.id.call);
		aConf = (Button) findViewById(R.id.audioconf);
		vConf = (Button) findViewById(R.id.videoconf);
		videoConf=(Button) findViewById(R.id.videoconfer);
		back = (Button) findViewById(R.id.btn_Settings);
		callContainer = (LinearLayout) findViewById(R.id.footer);
		callContainer.setVisibility(View.GONE);
		ArrayList<String> onlineBuddies = new ArrayList<String>();
		Vector<BuddyInformationBean> Buddies = ContactsFragment.getBuddyList();
		if(Buddies.size()>0) {
			for (BuddyInformationBean bib : Buddies) {
				if (bib.getStatus().equalsIgnoreCase("online")) {
					onlineBuddies.add(bib.getName());
				}
			}
		}
		adapter = new ContactConferenceAdapter(this, onlineBuddies);
		lv = (ListView) findViewById(R.id.contact_listview);
		lv.setAdapter(adapter);
		
		aConf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedBuddies = adapter.getBuddys();
				
				Set mapSet = (Set) selectedBuddies.entrySet();
				Iterator mapIterator = mapSet.iterator();
				StringBuffer buddyNames = new StringBuffer();
				while (mapIterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) mapIterator.next();
					Integer keyValue = (Integer) mapEntry.getKey();
					String buddy = (String) mapEntry.getValue();
					if (buddyNames.length() > 0) {
						buddyNames.append(",");
						buddyNames.append(buddy);
					} else {
						buddyNames.append(buddy);
					}
				}
				doConferenceCall(buddyNames.toString(), "ABC");
			}
		});
		videoConf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedBuddies = adapter.getBuddys();
				Set mapSet = (Set) selectedBuddies.entrySet();
				Iterator mapIterator = mapSet.iterator();
				StringBuffer buddyNames = new StringBuffer();
				while (mapIterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) mapIterator.next();
					Integer keyValue = (Integer) mapEntry.getKey();
					String buddy = (String) mapEntry.getValue();
					if (buddyNames.length() > 0) {
						buddyNames.append(",");
						buddyNames.append(buddy);
					} else {
						buddyNames.append(buddy);
					}
				}
				AppMainActivity.connectedbuddies=buddyNames.toString();
				doConferenceCall(buddyNames.toString(), "VC");
			}

		});

		vConf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedBuddies = adapter.getBuddys();
				Set mapSet = (Set) selectedBuddies.entrySet();
				Iterator mapIterator = mapSet.iterator();
				StringBuffer buddyNames = new StringBuffer();
				while (mapIterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) mapIterator.next();
					Integer keyValue = (Integer) mapEntry.getKey();
					String buddy = (String) mapEntry.getValue();
					if (buddyNames.length() > 0) {
						buddyNames.append(",");
						buddyNames.append(buddy);
					} else {
						buddyNames.append(buddy);
					}
				}
				doConferenceCall(buddyNames.toString(), "VBC");
			}

		});

		audioCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedBuddies = adapter.getBuddys();
				Set mapSet = (Set) selectedBuddies.entrySet();
				Iterator mapIterator = mapSet.iterator();
				StringBuffer buddyNames = new StringBuffer();
				while (mapIterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) mapIterator.next();
					Integer keyValue = (Integer) mapEntry.getKey();
					String buddy = (String) mapEntry.getValue();
					if (buddyNames.length() > 0) {
						buddyNames.append(",");
						buddyNames.append(buddy);
					} else {
						buddyNames.append(buddy);
					}
				}
				Log.i("AAAA","CONTACT CONFERENCE  "+buddyNames.toString());
				AppMainActivity.connectedbuddies=buddyNames.toString();
				doConferenceCall(buddyNames.toString(), "AC");
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("contactconf")) {
			WebServiceReferences.contextTable.remove("contactconf");
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

	private void doConferenceCall(String buddyNames, String callType) {
		String[] str = null;

		if (buddyNames.contains(",")) {
			str = buddyNames.split(",");
		} else {
			str = new String[1];
			str[0] = buddyNames;
		}
		for (int i = 0; i < str.length; i++) {
			// BuddyInformationBean bib = WebServiceReferences.buddyList
			// .get(str[i]);
			String bName = "";
			Log.i("AAAA","CONTACT CONFERENCE  doConferenceCall 1: ");
			for (String name : callDisp.getOnlineBuddysOnly()) {
				Log.i("AAAA","CONTACT CONFERENCE  doConferenceCall 1: "+name+" , "+str[i]);
				if (name.equalsIgnoreCase(str[i])) {
					bName = name;
					Log.i("AAAA","CONTACT CONFERENCE  doConferenceCall 2: "+bName);
					break;
				}
			}
			BuddyInformationBean bib = null;
			if (!SingleInstance.isbcontacts) {
				for (BuddyInformationBean temp : ContactsFragment
						.getBuddyList()) {
					if (!temp.isTitle()) {
						if (temp.getName().equalsIgnoreCase(bName)) {
							bib = temp;
							break;
						}
					}
				}
			} else {

				
			}
			if (bib != null) {
				if (bib.getStatus().startsWith("Onli")) {
					CallDispatcher.conConference.add(str[i]);

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
		}
		if (CallDispatcher.conConference.size() > 0)
			callDisp.ConMadeConference(callType, context);

	}

	public void ShowToast(String string, int i) {
		if (i == 0)
			Toast.makeText(context, string, Toast.LENGTH_LONG).show();
		else
			Toast.makeText(context, string, Toast.LENGTH_LONG).show();

	}
}
