package com.cg.commongui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.avatar.AnsweringMachineActivity;
import com.cg.avatar.AvatarActivity;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;
import com.main.AvatarFragment;
import com.util.SingleInstance;

public class CloneTextinput extends Activity implements OnClickListener {

	private TextView btn_back;

	private TextView btn_save;

	private EditText ed_txtcontent;

	private Context context;

	private AvatarFragment cloneActivity;

	private AnsweringMachineActivity answeringMachineActivity;
	private AvatarActivity avatarActivity;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.text_clonexml);
		context = this;

		btn_back = (TextView) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_save = (TextView) findViewById(R.id.btn_savetext);
		btn_save.setOnClickListener(this);
		ed_txtcontent = (EditText) findViewById(R.id.ed_txtcontent);

		if (WebServiceReferences.contextTable.containsKey("clone"))
			cloneActivity = AvatarFragment.newInstance(context);
		if (WebServiceReferences.contextTable.containsKey("answeringmachine"))
			answeringMachineActivity = (AnsweringMachineActivity) WebServiceReferences.contextTable
					.get("answeringmachine");
		if (WebServiceReferences.contextTable.containsKey("avatarset")) {
			position = getIntent().getIntExtra("position", 0);
			avatarActivity = AvatarActivity.newInstance(context);
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
		// if (cloneActivity != null) {
		// if (cloneActivity.getComponentPath != null) {
		// if (!cloneActivity.getComponentPath.endsWith(".txt"))
		// cloneActivity.getComponentPath = null;
		// }
		// }
		// if (answeringMachineActivity != null) {
		// if (answeringMachineActivity.getComponentPath != null) {
		// if (!answeringMachineActivity.getComponentPath.endsWith(".txt"))
		// answeringMachineActivity.getComponentPath = null;
		// }
		// }
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			// resignKeypad();

			if (ed_txtcontent.getText().toString().trim().length() != 0)
				showAlert("Do you want to save this content");
			else
				finish();

			break;
		case R.id.btn_savetext:
			// resignKeypad();
			if (ed_txtcontent.getText().toString().trim().length() == 0)
				showToast("Empty file can not be saved");
			else
				saveText();

			break;

		default:
			break;
		}
	}

	// private void resignKeypad() {
	// InputMethodManager imm = (InputMethodManager)
	// getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(ed_txtcontent.getWindowToken(), 0);
	// }

	private void showAlert(String message) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		myAlertDialog.setTitle("Clone");
		myAlertDialog.setMessage(message);
		myAlertDialog.setPositiveButton("Save",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						saveText();
					}
				});
		myAlertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int arg1) {
						// do something when the Cancel button is clicked
						finish();
						dialog.cancel();
					}
				});
		myAlertDialog.show();
	}

	private String getFileName() {
		String strFilename;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHH_mm_ss");
		Date date = new Date();
		strFilename = dateFormat.format(date);
		return strFilename;
	}

	private void saveText() {

		GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
				.get("groupchat");
		if (gChat != null) {
			Intent i = new Intent();
			i.putExtra("msg", ed_txtcontent.getText().toString().trim());
			setResult(RESULT_OK, i);
			finish();
		} else {

			String filename = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + getFileName() + ".txt";
			TextNoteDatas text_creator = new TextNoteDatas();
			text_creator.checkAndCreate(filename, ed_txtcontent.getText()
					.toString().trim());
			if (cloneActivity != null) {
				cloneActivity.getComponentPath = filename;
				Intent i = new Intent();
				i.putExtra("filename", filename);
				setResult(RESULT_OK, i);
			}
			if (answeringMachineActivity != null) {
				answeringMachineActivity.getComponentPath = filename;
			}
			if (avatarActivity != null) {
				// Log.i("avatar_i",
				// "clone Text"+dataBean+" file nama "+filename+"  dataBean.getMessage()"+dataBean.getMessage());
				avatarActivity.getComponentPath = filename;
				// dataBean.getMessage();
				// dataBean.setPath(filename);
				Intent i = new Intent();
				i.putExtra("filename", filename);
				i.putExtra("position", position);
				// i.putExtra("databean", dataBean);
				// Log.i("avatar_i",
				// "clone Text"+dataBean+" file nama "+filename);
				setResult(RESULT_OK, i);
			}

			finish();
		}
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

}
