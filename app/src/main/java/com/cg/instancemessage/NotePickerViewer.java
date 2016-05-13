package com.cg.instancemessage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.crypto.AESFileCrypto;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class NotePickerViewer extends Activity {
	private Button btn_cancel = null;

	private Button btn_done = null;
	private Button edit_note = null;
	private Button btn_accept = null;
	private Button btn_reject = null;
	private EditText tv_content = null;

	private TextView tv_title = null;

	private Context ctxt = null;

	private LinearLayout ctrl_lay = null, footer_lay = null,
			footer_lay3 = null;
	private ScrollView content_Layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title2);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title2);
		Bundle bndl = getIntent().getExtras();
		String path = bndl.getString("path");
		String title = bndl.getString("title");
		ctxt = this;
		WebServiceReferences.contextTable.put("pickerviewer", ctxt);
		btn_cancel = (Button) findViewById(R.id.btn_back);
		btn_done = (Button) findViewById(R.id.btn_save_note);
		btn_done.setVisibility(View.GONE);
		edit_note = (Button) findViewById(R.id.editnote);
		btn_done.setVisibility(View.GONE);
		edit_note.setVisibility(View.GONE);
		btn_accept = (Button) findViewById(R.id.btn_accept);
		btn_accept.setVisibility(View.GONE);
		btn_reject= (Button) findViewById(R.id.btn_reject);
		btn_reject.setVisibility(View.GONE);
		tv_content = new EditText(ctxt);
		tv_content.setFocusable(false);
		tv_title = (TextView) findViewById(R.id.tv_note_title);
		tv_title.setText(title);

		setContentView(R.layout.notescreationactivity);
		content_Layout = (ScrollView) findViewById(R.id.footerss);
		ctrl_lay = (LinearLayout) findViewById(R.id.footer1);
		ctrl_lay.setVisibility(View.GONE);
		footer_lay = (LinearLayout) findViewById(R.id.footer2);
		footer_lay.setVisibility(View.GONE);
		footer_lay3 = (LinearLayout) findViewById(R.id.footer3);
		footer_lay3.setVisibility(View.GONE);
		btn_cancel.setBackgroundResource(R.drawable.ic_action_back);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
		params.gravity = Gravity.CENTER_VERTICAL;
		btn_cancel.setLayoutParams(params);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Log.i("ACP", "PATH=====>" + path);

		showNoteContent(path);
	}
@Override
 protected void onResume() {
  super.onResume();
  AppMainActivity.inActivity = this;
 }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("pickerviewer");
		super.onDestroy();
	}

	private void showNoteContent(String path) {

		Log.i("ACP", "PATH=====>" + path);
		TextNoteDatas textnotes = new TextNoteDatas();
		String content = textnotes.getInformation(AESFileCrypto.decryptFile(SingleInstance.mainContext,path));
		textnotes = null;
		if (content != null) {
			tv_content.setText(content);
			tv_content.setBackgroundColor(Color.WHITE);
			tv_content.setTextColor(Color.BLACK);
			tv_content.setGravity(Gravity.TOP);
			tv_content.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			content_Layout.addView(tv_content);
			content_Layout.setBackgroundColor(Color.GRAY);
		}
	}

}
