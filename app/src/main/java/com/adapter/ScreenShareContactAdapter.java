package com.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cg.snazmed.R;

public class ScreenShareContactAdapter extends BaseAdapter {

	/*********** Declare Used Variables *********/
	private Context activity;
	private ArrayList<String> data;
	private static LayoutInflater inflater = null;
	String tempValues = null;
//	int i = 0;

	/************* CustomAdapter Constructor *****************/
	public ScreenShareContactAdapter(Context ac, ArrayList<String> d) {

		/********** Take passed values **********/
		activity = ac;
		data = d;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/******** What is the size of Passed Arraylist Size ************/
	public int getCount() {

		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {

		public TextView text;
		public CheckBox checkBox;

	}

	/****** Depends upon data size called for each row , Create each ListView row *****/
	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.contactconference, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.buddy_name);
			holder.checkBox = (CheckBox) vi.findViewById(R.id.buddycheck);
			holder.checkBox.setVisibility(View.GONE);
			vi.setTag(holder);
			if (data.size() <= 0) {
				holder.text.setText("No Data");

			} else {
				/***** Get each Model object from Arraylist ********/
				tempValues = null;
				tempValues = (String) data.get(position);

				/************ Set Model values in Holder elements ***********/

				holder.text.setText(tempValues);

				/******** Set Item Click Listner for LayoutInflater for each row *******/

			}

		} else
			holder = (ViewHolder) vi.getTag();

		return vi;
	}

}
