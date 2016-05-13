package com.cg.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.EditFormFieldAdapter;
import com.bean.EditForm;
import com.bean.EditFormBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

public class EditFormFields extends Activity implements OnClickListener {

	private ListView listView;
	private Context context;
	private CallDispatcher callDisp;
	private EditFormFieldAdapter adapter;
	private ArrayList<EditFormBean> list;
	private ProgressDialog dialog;
	private String tableName;
	Handler handler = new Handler();
	private ArrayList<InputsFields> oldList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.custom_title1);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title1);

			context = this;
			tableName = getIntent().getStringExtra("tablename");
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
			TextView title = (TextView) findViewById(R.id.note_date);
			title.setText("Edit Form Field");
			Button btn_cancel1 = (Button) findViewById(R.id.settings);

			btn_cancel1.setBackgroundResource(R.drawable.ic_action_back);

			Button btn_cmp = (Button) findViewById(R.id.btncomp);
			btn_cmp.setBackgroundResource(R.drawable.ic_action_save);

			btn_cmp.setOnClickListener(this);
			btn_cancel1.setOnClickListener(this);

			setContentView(R.layout.edit_forms);
			listView = (ListView) findViewById(R.id.form_field_listview);
			list = (ArrayList<EditFormBean>) getIntent().getExtras()
					.getSerializable("eList");
			adapter = new EditFormFieldAdapter(context, list);
			listView.setAdapter(adapter);
			oldList = CallDispatcher.inputFieldList;
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
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btncomp:
			if (CallDispatcher.LoginUser != null) {
				if (tableName != null) {
					String[] tables = tableName.split("_");
					showProgress();
					adapter.updateFormField(tables[1]);
				} else {
					showToast("Please check your form id");
				}
			}
			break;
		case R.id.settings:
			finish();
			break;
		default:
			break;
		}
	}

	private void showProgress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

	}

	public void cancelDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public void notifyWebserviceResponse(Servicebean sBean) {
		try {
			Object object = sBean.getObj();
			if (object instanceof String) {
				showToast((String) object);
			} else if (object instanceof WebServiceBean) {
				WebServiceBean wsBean = (WebServiceBean) object;
				showToast(wsBean.getText());
			} else if (object instanceof EditForm) {
				EditForm eForm = (EditForm) object;
				if (eForm.getText().equals("Successfully updated")) {
					HashMap<String, EditFormBean> eMap = adapter
							.getEditBeanMap();
					ArrayList<InputsFields> tempList = new ArrayList<InputsFields>();
					for (InputsFields iFields : CallDispatcher.inputFieldList) {
						if (eMap.containsKey(iFields.getAttributeId())) {
							EditFormBean eBean = eMap.get(iFields
									.getAttributeId());
							iFields.setFieldName(eBean.getColumnname());
						}
						tempList.add(iFields);
					}
					if (DBAccess.getdbHeler().updateTableColumn(tableName,
							tempList, oldList)) {
						for (InputsFields iFields : tempList) {
							ContentValues cv = new ContentValues();
							cv.put("column", "[" + iFields.getFieldName() + "]");
							if (iFields.getAttributeId() != null) {
								if (DBAccess.getdbHeler().isRecordExists(
										"select * from forminfo where attributeid='"
												+ iFields.getAttributeId()
												+ "'")) {
									DBAccess.getdbHeler().updateFormAttribute(
											cv,
											"attributeid='"
													+ iFields.getAttributeId()
													+ "'");
								}
							}
						}

						showToast(eForm.getText());
						refreshFieldList();
						finish();

					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cancelDialog();
		}
	}

	private void refreshFieldList() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					Vector<InputsFields> IFList = DBAccess.getdbHeler()
							.getFormFields(tableName);
					CallDispatcher.inputFieldList.clear();
					CallDispatcher.inputFieldList.addAll(IFList);
					AddNewForm newForm = (AddNewForm) WebServiceReferences.contextTable
							.get("frmcreator");
					if (newForm != null) {
						newForm.customAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

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
