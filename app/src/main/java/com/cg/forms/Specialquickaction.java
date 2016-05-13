package com.cg.forms;

import org.lib.model.BuddyInformationBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class Specialquickaction extends Activity {
	TextView version;
	Context context;

	EditText selectcondition, contact;
	Spinner makeaction;
	CheckBox datainsert;
	String[] myServices = { "Audio Call", "Video Call", "Audio Broadcast",
			"Video Broadcast", "Audio Conference", "Video Conference" };
	String action = null;
	Button execute;
	Button IMRequest;
	CallDispatcher callDisp;
	Boolean checked = false;
	AlertDialog alert = null;
	private Handler viewHandler = new Handler();

	String to = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);
		context = this;

		TextView title = (TextView) findViewById(R.id.note_date);
		title.setText(SingleInstance.mainContext.getResources().getString(R.string.special_quick_action));
		Button btn_notification = (Button) findViewById(R.id.notification);
		btn_notification.setVisibility(View.GONE);

		IMRequest = (Button) findViewById(R.id.im);

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.one);
		// IMRequest.setWidth(70);
		Button btncomp = (Button) findViewById(R.id.btncomp);
		btncomp.setVisibility(View.GONE);

		Button btn_cancel = (Button) findViewById(R.id.settings);
		btn_cancel.setText("Back");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		btn_cancel.setLayoutParams(params);

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		setContentView(R.layout.specialquickaction);
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

		WebServiceReferences.contextTable.put("special", this);

		CallDispatcher.conConference.clear();
		CallDispatcher.conferenceMembers.clear();
		CallDispatcher.contConferencemembers.clear();

		selectcondition = (EditText) findViewById(R.id.etlabel);
		datainsert = (CheckBox) findViewById(R.id.etcondtion);

		makeaction = (Spinner) findViewById(R.id.action_Spinner);

		contact = (EditText) findViewById(R.id.etTo);
		execute = (Button) findViewById(R.id.execute);

		contact.setFocusableInTouchMode(false);
		selectcondition.setFocusableInTouchMode(false);

		execute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (validation()) {
					Boolean success = callDisp.getdbHeler(context)
							.isValidQuery(selectcondition.getText().toString(),
									CallDispatcher.LoginUser);
					if (success && checked) {
						Intent i = new Intent();
						Bundle bun = new Bundle();
						bun.putString("query", "success");
						i.putExtra("share", bun);
						setResult(-1, i);

						doaction();
						finish();

					} else if (success && !checked) {
						doaction();
						finish();

					} else {

						Toast.makeText(context, "Condition Wrong", 1).show();
					}
				}
			}

		});
		selectcondition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, QueryActivity.class);
				i.putExtra("action", "Dynamic Audio Call");
				startActivityForResult(i, 1);

			}
		});
		datainsert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox) v).isChecked()) {
					checked = true;

				}

				else {
					checked = false;
				}
			}

		});
		makeaction = (Spinner) findViewById(R.id.action_Spinner);
		ArrayAdapter<String> ad = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, myServices);
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		makeaction.setAdapter(ad);

		makeaction
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						action = (String) makeaction.getItemAtPosition(arg2);
						String type = null;

						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

						if (action.equals("Audio Call")) {
							action = "AC";

						} else if (action.equals("Video Call")) {
							action = "VC";

						} else if (action.equals("Audio Broadcast")) {
							action = "ABC";

						} else if (action.equals("Video Broadcast")) {
							action = "VBC";

						} else if (action.equals("Audio Conference")) {
							action = "ACF";

						} else if (action.equals("Video Conference")) {
							action = "VCF";

						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					}
				});

		contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// TODO Auto-generated method stub

					if (action.equalsIgnoreCase("AC")
							|| action.equalsIgnoreCase("vc")) {
						CallDispatcher callDisp = CallDispatcher
								.getCallDispatcher(context);
						final CharSequence[] choiceList = callDisp
								.getmyBuddysForSpecialQA();
						if (choiceList.length > 0) {
							showSingleSelectBuddy(choiceList);

						} else {
							Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.sorry_no_online_users),
									Toast.LENGTH_LONG).show();
						}
					} else {

						CallDispatcher callDisp = CallDispatcher
								.getCallDispatcher(context);
						final String[] choiceList = callDisp
								.getmyBuddysForSpecialQA();
						if (choiceList.length > 0) {
							showMultiselectBuddys(choiceList);

						} else {
							Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.sorry_no_online_users),
									Toast.LENGTH_LONG).show();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == 1) && (resultCode == -1)) {

			Bundle bun = data.getBundleExtra("share");
			if (bun != null) {
				String path = bun.getString("squery");
				selectcondition.setText(path);
			}

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();

			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private Boolean validation() {
		String condtion = selectcondition.getText().toString();
		String touser = contact.getText().toString();

		if (condtion.length() == 0) {
			Toast.makeText(context, "Condtion is mandatory...",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (touser.length() == 0) {
			Toast.makeText(context, "User is mandatory...", Toast.LENGTH_LONG)
					.show();
			return false;

		} else {
			return true;
		}
	}

	void showMultiselectBuddys(final String[] elements) {
		to = null;
		AlertDialog.Builder d = new AlertDialog.Builder(context);

		d.setMultiChoiceItems(elements, null, new OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				if (isChecked) {
					String str = elements[which];
					// Toast.makeText(context,
					// "you have selected"+str,
					// Toast.LENGTH_LONG).show();
				}
			}
		});

		d.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				ListView list = ((AlertDialog) dialog).getListView();

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < list.getCount(); i++) {
					boolean checked = list.isItemChecked(i);

					if (checked) {
						if (sb.length() > 0)
							sb.append(",");
						sb.append(list.getItemAtPosition(i));
					}
				}

				contact.setText(sb.toString());

				to = sb.toString();

			}
		});
		d.show();
	}

	private void doaction() {
		// TODO Auto-generated method stub
		String toAddress = contact.getText().toString().trim();
		Log.i("CALLL", "ACtion==>" + action);
		if (action.equalsIgnoreCase("AC")) {
			CallDispatcher.conConference.add(toAddress);

			
			callDisp.ConMadeConference("AC", context);
		} else if (action.equalsIgnoreCase("VC")) {
			CallDispatcher.conConference.add(toAddress);

			callDisp.ConMadeConference("VC", context);
		} else if (action.equalsIgnoreCase("AbC")) {

			String[] str = toAddress.split(",");
			String offlinenames = null;
			// ArrayList< String> datas=new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}
				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				//
				// }

			}

			callDisp.ConMadeConference("ABC", context);
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames +SingleInstance.mainContext.getResources().getString(R.string.is_on_offline),
						Toast.LENGTH_LONG).show();
			}

		} else if (action.equalsIgnoreCase("VBC")) {
			String[] str = toAddress.split(",");
			String offlinenames = null;
			// ArrayList< String> datas=new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}
				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			callDisp.ConMadeConference("VBC", context);
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + SingleInstance.mainContext.getResources().getString(R.string.is_on_offline),
						Toast.LENGTH_LONG).show();
			}
		} else if (action.equalsIgnoreCase("ACF")) {

			String[] str = toAddress.split(",");
			String offlinenames = null;
			// ArrayList< String> datas=new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}

							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			callDisp.ConMadeConference("AC", context);
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + SingleInstance.mainContext.getResources().getString(R.string.is_on_offline),
						Toast.LENGTH_LONG).show();
			}
		} else if (action.equalsIgnoreCase("VCF")) {
			String[] str = toAddress.split(",");
			String offlinenames = null;
			// ArrayList< String> datas=new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			callDisp.ConMadeConference("VC", context);
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + SingleInstance.mainContext.getResources().getString(R.string.is_on_offline),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	void showSingleSelectBuddy(final CharSequence[] choiceLists) {
		try {
			to = null;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();
			// builder.setTitle("Add");

			// System.out.println(choiceList);

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceLists, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							// System.out.println("choiceList[which] :"
							// + choiceList[which]);
							// objCallDispatcher.callconfernce(
							// choiceList[which].
							// toString(), callType);
							contact.setText(choiceLists[which].toString());
							to = choiceLists[which].toString();
							alert.dismiss();
						}
					});
			alert = builder.create();
			if (choiceLists != null) {
				if (choiceLists.length != 0) {
					alert.show();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("special");
		super.onDestroy();
	}

}
