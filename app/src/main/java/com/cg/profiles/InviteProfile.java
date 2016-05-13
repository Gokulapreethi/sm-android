package com.cg.profiles;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListView;
import com.main.AppMainActivity;

public class InviteProfile extends Activity implements OnClickListener,
		SlideMenuInterface.OnSlideMenuItemClickListener {

	private Context context;

	private EditText selected_edittext;

	SlideMenu slidemenu;
	Button btn_notification = null;
	CallDispatcher callDisp = null;
	String type = null;
	String EmailId = null;
	private EditText emailid = null;
	private Button btn_cancel1 = null;
	private Button btn_cmp;
	private Button sendinvite;
	private String profileValues = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");

		TextView title = (TextView) findViewById(R.id.note_date);
		title.setText("Invite Friends");

		btn_cancel1 = (Button) findViewById(R.id.settings);
		btn_cancel1.setBackgroundResource(R.drawable.ic_action_back);
		btn_cancel1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_cmp = (Button) findViewById(R.id.btncomp);
		btn_cmp.setText("Send");

		btn_cmp.setOnClickListener(this);

		if (CallDispatcher.LoginUser == null) {
			Intent notes = new Intent(context, CompleteListView.class);
			startActivity(notes);
		} else {

			ShowList();

		}

		setContentView(R.layout.invitefriends);

		WebServiceReferences.contextTable.put("inviteprofile", context);

		emailid = (EditText) findViewById(R.id.ed_emailId);
		sendinvite = (Button) findViewById(R.id.but_inviteprof);
		sendinvite.setOnClickListener(this);
		EmailId = emailid.getText().toString().trim();
		profileValues = getIntent().getStringExtra("profilevalues");

	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	private String getContentForMail() {
		try {
			String headLine = CallDispatcher.LoginUser
					+ "  has shared information with you using "
					+ context.getResources().getString(R.string.app_name)
					+ "\n\n";
			String Info1 = "Would you like to share your information with "
					+ CallDispatcher.LoginUser + "? \n ";
			String Info2 = "Download "
					+ context.getResources().getString(R.string.app_name)
					+ " from any of the links below and be connected\n";
			String Info3 = "http://www.googleplaystore.com \n\n or\n Send a mail to customercare@"
					+ context.getResources().getString(R.string.app_name)
					+ ".com \n";

			String ContentMessage = headLine + profileValues + "\n" + Info1
					+ Info2 + Info3;
			return ContentMessage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.but_inviteprof:

			inviteBuddy();

			break;
		case R.id.btncomp:
			inviteBuddy();

			break;
		case R.id.settings:
			finish();
			break;

		default:
			break;
		}
	}

	private void inviteBuddy() {
		// TODO Auto-generated method stub
		String content = getContentForMail();
		if (emailid.getText().toString() != null
				&& emailid.getText().toString().length() != 0) {
			if (isEmailValid(emailid.getText().toString())) {
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { emailid.getText().toString() });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"Profile");
				emailIntent
						.putExtra(android.content.Intent.EXTRA_TEXT, content);
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				finish();

			} else {

				showToast("Invalid Email ID");
			}
		} else {
			showToast("Enter Email Id");

		}

	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
		Log.d("lg", "########### Show IM Called"
				+ WebServiceReferences.Imcollection.size());

	}

	public void ShowList() {
		setContentView(R.layout.history_container);

		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		ArrayList<Slidebean> datas = new ArrayList<Slidebean>();

		callDisp.composeList(datas);
		slidemenu.init(InviteProfile.this, datas, InviteProfile.this, 100);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();

			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("inviteprofile");
		super.onDestroy();
	}

	public void ShowError(String Title, String Message) {
		AlertDialog confirmation = new AlertDialog.Builder(this).create();
		confirmation.setTitle(Title);
		confirmation.setMessage(Message);
		confirmation.setCancelable(true);
		confirmation.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		confirmation.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == 100) && (resultCode == -10)) {
			try {
				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("filepath");
					selected_edittext.setText(path);

				}

			} catch (NullPointerException e) {

			} catch (Exception e) {

			}
		}
	}

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub

	}

}
