package com.cg.forms;

import java.util.Vector;

import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.adapter.FormFieldAccessAdapter;
import com.bean.BuddyPermission;
import com.bean.FormFieldBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class FormFieldAccessActivity extends Activity implements
		OnClickListener {

	private Button back = null;
	private Button save = null;
	private ListView fieldAccessListView = null;
	private FormFieldAccessAdapter adapter = null;
	// private LinkedHashMap<FormFieldBean, Vector<BuddyPermission>> groupsList
	// = null;
	private Vector<FormFieldBean> groupsList = null;
	private Context context;
	private String formId = null;
	private String formName = null;
	private String formOwnerName = null;
	private Handler handler = new Handler();
	private ProgressDialog pDialog = null;
	public boolean isProgressStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			context = this;
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.field_access_settings);
			back = (Button) findViewById(R.id.back);
			back.setOnClickListener(this);
			save = (Button) findViewById(R.id.save);
			save.setOnClickListener(this);
			fieldAccessListView = (ListView) findViewById(R.id.field_access_settings_list);
			formId = getIntent().getExtras().getString("formid", null);
			formName = getIntent().getExtras().getString("formname", null);
			formOwnerName = getIntent().getExtras().getString("owner", null);
			if (formId != null) {
				loadExpandListView(formId, formName);
				adapter = new FormFieldAccessAdapter(context, groupsList);
				fieldAccessListView.setAdapter(adapter);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save:
			if (adapter != null) {
				adapter.saveFormFieldSettings();
			}
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	public void showProgress() {
		try {
			if (!isProgressStarted) {
				pDialog = new ProgressDialog(context);
				isProgressStarted = true;
				if (pDialog != null) {
					pDialog.setCancelable(false);
					pDialog.setMessage("Progress ...");
					pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pDialog.setProgress(0);
					pDialog.setMax(100);
					pDialog.show();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cancelProgress() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	private void loadExpandListView(String formId, String formName) {
		// Vector<FormFieldBean> fieldAccessList = DBAccess.getdbHeler()
		// .getFormInfoList(formName, formId);
		groupsList = DBAccess.getdbHeler().getFormInfoList(formName, formId);
		// groupsList = new LinkedHashMap<FormFieldBean,
		// Vector<BuddyPermission>>();
		// for (FormFieldBean formFieldBean : fieldAccessList) {
		// Vector<BuddyPermission> buddyAccessList =
		// loadBuddyAccessList(formFieldBean);
		// groupsList.put(formFieldBean, buddyAccessList);
		// }
	}

	private Vector<BuddyPermission> loadBuddyAccessList(
			FormFieldBean formFieldBean) {
		Vector<BuddyPermission> buddyAccessList = new Vector<BuddyPermission>();
		try {
			buddyAccessList = DBAccess.getdbHeler().getAccesssibleBuddies(
					formFieldBean.getFormId(), formFieldBean.getAttributeId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buddyAccessList;
	}

	private void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(SingleInstance.mainContext, message,
						Toast.LENGTH_LONG).show();
			}
		});

	}

	public void notifyFormFieldSettings(Object obj) {
		// TODO Auto-generated method stub
		if (!isProgressStarted) {
			cancelProgress();
		}
		if (obj instanceof String) {
			showToast((String) obj);
			cancelProgress();
		} else if (obj instanceof WebServiceBean) {
			WebServiceBean sbean = (WebServiceBean) obj;
			showToast(sbean.getText());
			cancelProgress();
		}
	}

}
