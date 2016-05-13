package com.cg.hostedconf;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.Databean;
import com.cg.commonclass.WebServiceReferences;

public class ContactArrayAdapter extends ArrayAdapter<Databean> {

	private final Context context;
	private final ArrayList<Databean> values;
	CallDispatcher callDisp = null;

	public ContactArrayAdapter(Context context,
			ArrayList<Databean> callerBean_Array) {
		super(context, R.layout.sipcall, callerBean_Array);
		this.context = context;
		values = callerBean_Array;
		callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");
	}

	public ArrayList<Databean> getUpdatedContactArrayList() {
		return values;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = convertView;
		if (rowView == null)
			rowView = inflater.inflate(R.layout.sipcall, parent, false);

		final Databean contactBean = (Databean) values.get(position);
		final TextView userName = (TextView) rowView
				.findViewById(R.id.txtViewTitle);
		TextView userStatus = (TextView) rowView
				.findViewById(R.id.txtViewStatus);

		userName.setText(contactBean.getname());
		// userInfo_tv.setTag(userName);

		CheckBox checkBox = (CheckBox) rowView
				.findViewById(R.id.select_checkBox);
		final ImageView img = (ImageView) rowView.findViewById(R.id.icon2);
		img.setVisibility(View.VISIBLE);

		checkBox.setChecked(false);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					userName.setTextColor(context.getResources().getColor(
							R.color.blue_light_bg));
					img.setBackgroundResource(R.drawable.icon_buddy_available);
					contactBean.setSelected(true);
				} else {
					userName.setTextColor(context.getResources().getColor(
							R.color.black));
					img.setBackgroundResource(R.drawable.icon_buddyoffline);
					contactBean.setSelected(false);
				}
			}

		});

		userName.setTextColor(Color.BLACK);

		Log.e("buddy", "Buddy:" + userName.getText() + " Status :"
				+ contactBean.getStatus());
		if (contactBean.getStatus().startsWith("Off")) {
			userStatus.setText("Disconnected");
		} else if (contactBean.getStatus().equalsIgnoreCase("Virtual")) {
			rowView.setBackgroundColor(Color.GRAY);
			userStatus.setText("");
		} else if (contactBean.getStatus().startsWith("Onli")) {
			userStatus.setText("Available");
		} else if (contactBean.getStatus().startsWith("Away")) {
			userStatus.setText("Away");
		} else if (contactBean.getStatus().startsWith("Ste")) {
			userStatus.setText("Stealth");
		} else if (contactBean.getStatus().startsWith("Airport")) {
			userStatus.setText("Airport");
		} else if (contactBean.getStatus().equalsIgnoreCase("Pending")) {
			userStatus.setText("Pending");
		}

		return rowView;
	}

}
