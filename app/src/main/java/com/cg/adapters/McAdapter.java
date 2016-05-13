package com.cg.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bean.fiBean;

public class McAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<fiBean> p_list;
	public String[] text;
	private ArrayAdapter<String> adapter = null;

	public McAdapter(Context context, int resource,
			ArrayList<fiBean> p_list) {
		// super(context, resource, list);
		// TODO Auto-generated constructor stub

		this.context = context;
		// this.itemname=itemname;
		this.p_list = p_list;
	}

	public class ViewHolder {

		fiBean bean;
		EditText txtvalues;
		Spinner spinner2;
		TextView txtTitle;
		LayoutInflater inflater;
		LinearLayout button;
	}

	public View getView(int position, View rowView, ViewGroup parent) {
		// // TextView txtTitle=null;
		// final ViewHolder holder;
		// rowView=null;
		// 
		// if (rowView == null) {
		// holder = new ViewHolder();
		// try{
		// 
		// holder.inflater = (LayoutInflater) context
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//
		// rowView = holder.inflater.inflate(R.layout.microlist, null);
		// if (holder.bean.getValidation() == null) {
		// holder.bean.setValidation("letters");
		// }
		// if (holder.bean.getTypespace() == null) {
		//
		// holder.bean.setTypespace("normal");
		// }
		// if (holder.bean.getArralist() == null) {
		//
		// holder.bean.setArralist("0");
		// }
		// // holder.
		// holder.txtTitle = (TextView) rowView.findViewById(R.id.item);
		// holder.txtTitle.setTag(position);
		// holder.spinner2 = (Spinner) rowView.findViewById(R.id.spinner);
		// holder.spinner2.setVisibility(View.GONE);
		// holder.spinner2.setTag(position);
		// // final EditText
		// holder.txtvalues = (EditText) rowView.findViewById(R.id.editem);
		// holder.txtvalues.setVisibility(View.GONE);
		// holder.txtvalues.setTag(position);
		// holder.button = (LinearLayout)
		// rowView.findViewById(R.id.surveylayout);
		// holder.button.setVisibility(View.GONE);
		// rowView.setTag(holder);
		// } catch (NotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }}
		// else {
		// holder = (ViewHolder) rowView.getTag();
		// }
		// List<String> spinnerArray = new ArrayList<String>();
		//
		// int ed_gsm_position = (Integer) holder.txtvalues.getTag();
		// holder.txtvalues.setId(ed_gsm_position);
		//
		// TextWatcher textWatcher = new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence arg0, int arg1, int arg2,
		// int arg3) {
		// try {
		//
		// final int position1 = holder.txtvalues.getId();
		// final EditText title = (EditText) holder.txtvalues;
		//
		// if (title.getText().toString().length() > 0) {
		// holder.bean.setMessageTitle(title.getText().toString());
		// p_list.set(position1, holder.bean);
		// }
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence arg0, int arg1,
		// int arg2, int arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable arg0) {
		// // TODO Auto-generated method stub
		// }
		// };
		// holder.txtvalues.addTextChangedListener(textWatcher);
		//
		// if (holder.bean.getPermission_name().equals(
		// "Inspector / Assistant inspector Name")) {
		// holder.button.setVisibility(View.VISIBLE);
		//
		// }
		// if (holder.bean.getTypespace().equals("bold")) {
		//
		// holder.txtTitle.setTextSize(15);
		// holder.txtTitle.setTypeface(null, Typeface.BOLD);
		//
		// }
		//
		// if (holder.bean.getArralist().equals("1")) {
		// spinnerArray.add("Yes");
		// spinnerArray.add("No");
		// holder.bean.setSpinnerArray(spinnerArray);
		// holder.txtTitle.setText(holder.bean.getPermission_name());
		// holder.spinner2.setVisibility(View.VISIBLE);
		// holder.txtvalues.setVisibility(View.GONE);
		//
		// } else if (holder.bean.getPermission_name().equals("Type")) {
		// spinnerArray.add("Male");
		// spinnerArray.add("Female");
		// holder.txtTitle.setText(holder.bean.getPermission_name());
		// holder.spinner2.setVisibility(View.VISIBLE);
		// holder.txtvalues.setVisibility(View.GONE);
		//
		// } else if (holder.bean.getPermission_name().equals(
		// "Applicant's Father/Husband Details")) {
		//
		// spinnerArray.add("Father");
		// spinnerArray.add("Husband");
		// holder.txtTitle.setText(holder.bean.getPermission_name());
		// holder.spinner2.setVisibility(View.VISIBLE);
		// holder.txtvalues.setVisibility(View.GONE);
		// } else if (holder.bean.getPermission_name().equals("Types of House"))
		// {
		// spinnerArray.add("Mud");
		// spinnerArray.add("Tin");
		// spinnerArray.add("Half concrete");
		// spinnerArray.add("concrete");
		// spinnerArray.add("others");
		// holder.txtTitle.setText(holder.bean.getPermission_name());
		// holder.spinner2.setVisibility(View.VISIBLE);
		// holder.txtvalues.setVisibility(View.GONE);
		//
		// } else if (holder.bean.getType().equals("1")
		// && holder.bean.getValidation().equals("letters")) {
		// holder.txtTitle.setText(holder.bean.getPermission_name());
		// holder.txtvalues.setText(holder.bean.getMessageTitle());
		// holder.txtvalues.setVisibility(View.VISIBLE);
		// } else if (holder.bean.getType().equals("1")
		// && holder.bean.getValidation().equals("number")) {
		// holder.txtTitle.setText(holder.bean.getPermission_name());
		// holder.txtvalues.setInputType(InputType.TYPE_CLASS_NUMBER);
		// holder.txtvalues.setText(holder.bean.getMessageTitle());
		// holder.txtvalues.setVisibility(View.VISIBLE);
		// }else if (holder.bean.getType().equals("0"))
		// {
		//
		// holder.txtTitle.setText(holder.bean.getPermission_name());
		// }
		//
		// adapter = new ArrayAdapter<String>(context,
		// android.R.layout.simple_spinner_item, spinnerArray);
		//
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
		// holder.spinner2.setAdapter(adapter);
		// // holder.spinner2.setSelection(getBuddyPosition(bean
		// // .getPermission_name()));
		return rowView;

	};

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return p_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		fiBean bean = (fiBean) p_list.get(arg0);
		return bean;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public int getBuddyPosition(String b_name) {
		Log.d("clone", "---->" + b_name);
		// if (b_name != null) {
		// if (b_name.equals("default"))
		// b_name = "All Users";
		// }
		int i = 0;
		for (i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).trim().equals(b_name.trim())) {

				break;
			}
		}
		Log.d("clone", "------> i value in buddy" + i);
		return i;
	}
}