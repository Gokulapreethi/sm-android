package com.group;

import java.util.Vector;

import org.lib.model.GroupBean;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;

public class GroupAdapter1 extends ArrayAdapter<GroupBean> {


	private Context context;
	private Typeface tf_regular = null;

	private Typeface tf_bold = null;

	public GroupAdapter1(Context context, int textViewResourceId,
			Vector<GroupBean> groupList) {

		super(context, R.layout.grouplist, groupList);
		this.context = context;
		

	}
	
	

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		View row = view;
		try {
		
		final ViewHolder holder;
		
		if (row == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.grouplist, null, false);
							
			
			holder. header = (RelativeLayout) row
					.findViewById(R.id.header_container);
			holder. listContainer = (RelativeLayout) row
					.findViewById(R.id.list_container);
			holder. grouplist = (TextView) row
					.findViewById(R.id.group_name);
			holder. editgroup=(ImageView)row.findViewById(R.id.editgroup);
			row.setTag(holder);
		}
		else {
			holder = (ViewHolder) row.getTag();
		}
			final GroupBean groupBean = (GroupBean) GroupActivity.groupList.get(position);
			

			if (groupBean.getMode() != null) {
				Log.i("Test", "GroupAdapter  22222  IF>>>>>>>");
				holder.header.setVisibility(View.VISIBLE);
				holder.listContainer.setVisibility(View.GONE);
			} else {
				Log.i("Test", "GroupAdapter 22222 ELSE>>>>>>>");
							
					
				holder.header.setVisibility(View.GONE);
				holder.listContainer.setVisibility(View.VISIBLE);
				TextView tv_groupName = (TextView) row
						.findViewById(R.id.group_name);
				tv_groupName.setText(groupBean.getGroupName());
				if(tv_groupName==null)
				{
					holder.listContainer.setVisibility(View.GONE);
				}
				tv_groupName.setTypeface(tf_regular);
				
				holder.grouplist.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						try {
//							ContactsFragment contactsFragment = ContactsFragment
//									.getInstance(context);
//							contactsFragment.showGroupChatDialog(groupBean);
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				holder.editgroup.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d("Test","Inside the editgroup--GroupAdapter_________");
						 GroupBean groupBean1 = (GroupBean) GroupActivity.groupList.get(position);
						if (groupBean1.getOwnerName().equalsIgnoreCase(
								CallDispatcher.LoginUser)) {
							Intent intent = new Intent(context
									.getApplicationContext(),
									GroupActivity.class);
							intent.putExtra("isEdit", true);
							intent.putExtra("id", groupBean.getGroupId());
							context.startActivity(intent);
						}
					}
				});

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}
	public static class ViewHolder {
		RelativeLayout header;
		RelativeLayout listContainer;		
		ImageView editgroup;		
		TextView grouplist;
	}
}
