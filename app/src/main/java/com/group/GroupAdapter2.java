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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.GroupChatPermissionBean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class GroupAdapter2 extends ArrayAdapter<GroupBean> {

	private Context context;
	private Typeface tf_regular = null;

	private Typeface tf_bold = null;

	public GroupAdapter2(Context context, int textViewResourceId,
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

				holder.header = (RelativeLayout) row
						.findViewById(R.id.header_container);
				holder.listContainer = (RelativeLayout) row
						.findViewById(R.id.list_container);
				holder.grouplist = (TextView) row.findViewById(R.id.group_name);
				holder.editgroup = (ImageView) row.findViewById(R.id.editgroup);
				holder.openchat=(ImageView)row.findViewById(R.id.chat);
				holder.im_view=(Button)row.findViewById(R.id.im_view);
				row.setTag(holder);
							row.setTag(holder);
							
			} else {
				holder = (ViewHolder) row.getTag();
			}
			final GroupBean groupBean = (GroupBean)ContactsFragment.getBuddyGroupList().get(position);
			if (groupBean.getMessageCount() > 0) {
				holder.im_view.setVisibility(View.VISIBLE);
				holder.im_view.setText(String
						.valueOf(groupBean
								.getMessageCount()));
			}else {
				holder.im_view.setVisibility(View.GONE);
			}

			if (groupBean.getMode() != null) {
				holder.header.setVisibility(View.GONE);
				holder.listContainer.setVisibility(View.GONE);
			} else {
				// if (WebServiceReferences.contextTable
				// .containsKey("fromgroupactivity")) {
				// String CountOfMembers=((GroupActivity)
				// WebServiceReferences.contextTable
				// .get("fromgroupactivity")).getMembersCount();
				// //String Members=CountOfMembers.getMembersCount();
				// Log.d("Test","@@@@Members Count"+CountOfMembers);
				// }else
				// {
				// Log.d("Test","@@@@Out sideMembers Count");
				//
				// }
				//
				Log.i("Test", "GroupAdapter 22222 ELSE>>>>>>>");

				holder.header.setVisibility(View.GONE);
				holder.listContainer.setVisibility(View.VISIBLE);
				TextView tv_groupName = (TextView) row
						.findViewById(R.id.group_name);
				tv_groupName.setText(groupBean.getGroupName());
				tv_groupName.setTypeface(tf_regular);
				holder.grouplist.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						try {
							// ContactsFragment contactsFragment =
							// ContactsFragment
							// .getInstance(context);
							// contactsFragment.showGroupChatDialog(groupBean);

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
						Log.d("Test",
								"Inside the editgroup--GroupAdapter_________");
						GroupBean groupBean1 = (GroupBean) ContactsFragment.getBuddyGroupList().get(position);

						Intent intent = new Intent(context
								.getApplicationContext(), ViewGroups.class);
						intent.putExtra("id", groupBean.getGroupId());
						context.startActivity(intent);

					}
				});
				holder.deletegroup.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						try {
							ContactsFragment contactsFragment = ContactsFragment
									.getInstance(context);
							// TODO Auto-generated method stub
							if (SingleInstance.mainContext
									.isNetworkConnectionAvailable()) {
								if (groupBean.getOwnerName().equalsIgnoreCase(
										CallDispatcher.LoginUser))
									contactsFragment
											.deleteGroup(groupBean,
													"Are you sure you want to delete this group ");
								else {
									contactsFragment
											.deleteGroup(groupBean,
													"Are you sure you want to leave this group ");
								}
								// contactsFragment.dialog.dismiss();
								// TODO Auto-generated catch block
							} else {
								contactsFragment
										.showAlert1("Info",
												"Check internet connection Unable to connect server");

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.d("Tag",e.toString());
						}
					}
				});
				holder.audiocall.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							CallDispatcher calldisp = null;
							if (calldisp == null) {
								calldisp = new CallDispatcher(context);
							}
							if (groupBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(groupBean);
								if (gcpBean.getAudioConference()
										.equalsIgnoreCase("1")) {
									GroupBean gb = calldisp.getdbHeler(context)
											.getGroupAndMembers(
													"select * from groupdetails where groupid="
															+ groupBean.getGroupId());
									if (gb.getOwnerName() != null
											&& gb.getActiveGroupMembers() != null
											&& gb.getActiveGroupMembers()
													.length() > 0) {
										if (!CallDispatcher.isWifiClosed) {
											String passUser = gb.getOwnerName()
													+ ","
													+ gb.getActiveGroupMembers();
											calldisp.requestAudioConference(passUser);
										} else
											Toast.makeText(
													context,
													"Please check your internet connection before make conference call",
													1).show();
									} else {
										if (gb.getActiveGroupMembers() == null) {
											Toast.makeText(
													context,
													"Sorry no members in the group",
													1).show();
										} else if (gb
												.getActiveGroupMembers()
												.length() == 0) {
											Toast.makeText(
													context,
													"Sorry no online members to call",
													1).show();
										}
									}
								} else {
									Toast.makeText(
											context,
											"Sorry you dont have permission",
											1).show();
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.d("Tag",e.toString());
						}
					}
				});
				holder.openchat.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						try {
							ContactsFragment contactsFragment = ContactsFragment
									.getInstance(context);
							contactsFragment.showGroupChatDialog(groupBean);
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
		ImageView audiocall;		
		ImageView deletegroup;		
		ImageView openchat;			
		TextView grouplist;
		Button im_view;
	}

}