package com.main;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.util.SingleInstance;

public class CustomAdapter extends BaseAdapter {
	Context context;
	List<RowItem> rowItem;
	private int groupCount,messageCount,fileCount,dashCount;

	Handler handler = new Handler();

	CustomAdapter(Context context, List<RowItem> rowItem) {
		this.context = context;
		this.rowItem = rowItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_tem, null);
		}

//		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		final Button count1 = (Button) convertView.findViewById(R.id.count1);
		final Button count2 = (Button) convertView.findViewById(R.id.count2);
		RowItem row_pos = rowItem.get(position);

		// setting the image resource and title
//		imgIcon.setImageResource(row_pos.getIcon());
		txtTitle.setText(row_pos.getTitle());
		if(row_pos.getTitle()!=null) {
			if (row_pos.getTitle().equalsIgnoreCase(
					SingleInstance.mainContext.getResources().getString(
							R.string.conversations))) {
				count2.setVisibility(View.GONE);
				 groupCount = DBAccess.getdbHeler().getUnreadMsgCount(
						CallDispatcher.LoginUser);
				Log.i("gchat123", "group count : " + groupCount);
				if (groupCount > 0) {
					count1.setVisibility(View.VISIBLE);
					count1.setText(Integer.toString(groupCount));
				} else {
					count1.setVisibility(View.GONE);
				}
			} else if (row_pos.getTitle().equalsIgnoreCase(
					SingleInstance.mainContext.getResources().getString(
							R.string.contacts))) {
				count2.setVisibility(View.GONE);
				 messageCount = DBAccess.getdbHeler()
						.getUnreadMsgCountIndividualChat(CallDispatcher.LoginUser);
				Log.i("contacts123", "unread message count : " + messageCount);
				if (messageCount > 0) {
					count1.setVisibility(View.VISIBLE);
					count1.setText(Integer.toString(messageCount));
				} else {
					count1.setVisibility(View.GONE);
				}
				int buddyRequestCount = ContactsFragment.buddyRequestCount.size();
				Log.i("contacts123", "buddyreq message count: " + buddyRequestCount);
				if (buddyRequestCount > 0) {
					count2.setVisibility(View.VISIBLE);
					count2.setText(Integer.toString(buddyRequestCount));
				} else {
					count2.setVisibility(View.GONE);
				}
			} else if (row_pos.getTitle().equalsIgnoreCase(
					SingleInstance.mainContext.getResources().getString(
							R.string.sec_contacts))) {
				count2.setVisibility(View.GONE);
				int messageCount = DBAccess.getdbHeler()
						.getUnreadMsgCountIndividualChat(CallDispatcher.LoginUser);
				Log.i("contacts123", "unread message count : " + messageCount);
				if (messageCount > 0) {
					count1.setVisibility(View.VISIBLE);
					count1.setText(Integer.toString(messageCount));
				} else {
					count1.setVisibility(View.GONE);
				}

			} else if (row_pos.getTitle().equalsIgnoreCase("SNAZBOX FILES")) {
				if (CallDispatcher.LoginUser != null) {
					count2.setVisibility(View.GONE);
					 fileCount = DBAccess.getdbHeler().getUnreadnotesSize(
							CallDispatcher.LoginUser);
					Log.i("contacts123", "message count : " + fileCount);

					if (fileCount > 0) {
						count1.setVisibility(View.VISIBLE);
						count1.setText(Integer.toString(fileCount));
					} else {
						count1.setVisibility(View.GONE);
					}
				}

			}
			else if (row_pos.getTitle().equalsIgnoreCase("DASHBOARD")) {
				int callCount=DBAccess.getdbHeler()
						.getUnreadCallCount(CallDispatcher.LoginUser);
				dashCount=groupCount+messageCount+fileCount+callCount;
				if (dashCount > 0) {
					count1.setVisibility(View.VISIBLE);
					count1.setText(Integer.toString(dashCount));
				} else {
					count1.setVisibility(View.GONE);
				}
			}else
			 {
				count1.setVisibility(View.GONE);
				count2.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return rowItem.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItem.indexOf(getItem(position));
	}
}
