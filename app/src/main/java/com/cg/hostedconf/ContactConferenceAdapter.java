package com.cg.hostedconf;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.WebServiceReferences;

public class ContactConferenceAdapter extends BaseAdapter implements
		OnClickListener {

	/*********** Declare Used Variables *********/
	private Activity activity;
	private ArrayList<String> data;
	private static LayoutInflater inflater = null;
	String tempValues = null;
	int i = 0;
	private HashMap<Integer, String> getBuddyNames = new HashMap<Integer, String>();

	/************* CustomAdapter Constructor *****************/
	public ContactConferenceAdapter(Activity ac, ArrayList<String> d) {

		/********** Take passed values **********/
		activity = ac;
		data = d;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public HashMap<Integer, String> getBuddys() {
		return getBuddyNames;
	}

	/******** What is the size of Passed Arraylist Size ************/
	public int getCount() {

		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	public Object getItem(int position) {
		return position;
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

				vi.setOnClickListener(new OnItemClickListener(position));
			}

			holder.checkBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub

							if (isChecked) {
								getBuddyNames.put(position, data.get(position));

								if (WebServiceReferences.contextTable
										.containsKey("contactconf")) {
									((ContactConference) WebServiceReferences.contextTable
											.get("contactconf")).callContainer
											.setVisibility(View.VISIBLE);
									Log.d("Test", "Members" + getBuddyNames);
									
								}
							} else {
								if (getBuddyNames.containsKey(position)) {
									getBuddyNames.remove(position);
								}
								buttonView.setChecked(false);
								if (getBuddyNames.size() == 0) {
									if (WebServiceReferences.contextTable
											.containsKey("contactconf")) {
										((ContactConference) WebServiceReferences.contextTable
												.get("contactconf")).callContainer
												.setVisibility(View.GONE);
									}
								}
							}
						}
					});
		} else
			holder = (ViewHolder) vi.getTag();

		return vi;
	}

	@Override
	public void onClick(View v) {
		Log.v("CustomAdapter", "=====Row button clicked=====");
	}

	/********* Called when Item click in ListView ************/
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

		}
	}
}