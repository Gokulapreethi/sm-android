package com.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.EditFormBean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.util.SingleInstance;

public class EditFormFieldAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<EditFormBean> list;
	private HashMap<String, EditFormBean> eMap;

	public EditFormFieldAdapter(Context context, ArrayList<EditFormBean> list) {
		this.context = context;
		this.list = list;
		eMap = new HashMap<String, EditFormBean>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		try {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater mInflater = (LayoutInflater) context
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.edit_form_field, null);
			} else {
				holder = (ViewHolder) convertView.getTag();
				Log.i("avatar_i", "convertView != null");
			}

			holder.eBean = (EditFormBean) getItem(position);
			holder.value = (EditText) convertView.findViewById(R.id.field_name);
			holder.attId = (TextView) convertView.findViewById(R.id.field_aid);
			holder.value.setText(holder.eBean.getColumnname());
			holder.attId.setText(holder.eBean.getAttributeid());
			if (!eMap.containsKey(holder.eBean.getAttributeid())) {
				eMap.put(holder.eBean.getAttributeid(), holder.eBean);
			}
			TextWatcher textWatcher = new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					try {
						// TODO Auto-generated method stub
						int position = holder.value.getId();
						final EditText value = (EditText) holder.value;
						if (value.getText().toString().length() > 0) {
							holder.eBean.setColumnname(value.getText()
									.toString());
							list.set(position, holder.eBean);
							if (eMap.containsKey(holder.eBean.getAttributeid())) {
								eMap.remove(holder.eBean.getAttributeid());
							}
							eMap.put(holder.eBean.getAttributeid(),
									holder.eBean);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
				}
			};
			holder.value.addTextChangedListener(textWatcher);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	public class ViewHolder {
		EditText value;
		TextView attId;
		EditFormBean eBean;
	}

	public void updateFormField(String formId) {
		if (!CallDispatcher.isWifiClosed) {
			String[] params = new String[3];
			params[0] = CallDispatcher.LoginUser;
			params[1] = formId;
			params[2] = "edit";
			WebServiceReferences.webServiceClient.updateExistingFormFields(
					context, params, list);
		} else {
			showToast(SingleInstance.mainContext.getResources().getString(
					R.string.no_network_connection));
		}
	}

	private void showToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public HashMap<String, EditFormBean> getEditBeanMap() {
		return eMap;
	}

}
