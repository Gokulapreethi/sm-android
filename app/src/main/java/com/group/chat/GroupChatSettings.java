package com.group.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.lib.model.GroupBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.GroupChatPermissionBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.permissions.Permissions;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class GroupChatSettings extends Activity implements OnClickListener {

	private ExpandableListView expandableListView;
	private Context context;
	private GroupSettingsAdapter adapter;
	private LinkedHashMap<String, Vector<Permissions>> groupsList;
	private String groupId;
	private Button back;
	private Button save;
	private Vector<GroupChatPermissionBean> gcPermissionList = new Vector<GroupChatPermissionBean>();
	private GroupBean groupBean = new GroupBean();
	private Handler handler = new Handler();
	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.groupchat_settings);
		context = this;
		SingleInstance.contextTable.put("groupchatsettings", context);
		groupId = getIntent().getStringExtra("groupid");
		loadExpandListView(groupId);
		back = (Button) findViewById(R.id.btn_cancel);
		back.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		adapter = new GroupSettingsAdapter(context, expandableListView,
				groupsList);
		expandableListView.setIndicatorBounds(0, 20);
		expandableListView.setAdapter(adapter);
		// expandableListView.setOnGroupClickListener(new OnGroupClickListener()
		// {
		//
		// @Override
		// public boolean onGroupClick(ExpandableListView parent, View v,
		// int groupPosition, long id) {
		// // TODO Auto-generated method stub
		//
		// return true;
		// }
		// });
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	public class GroupSettingsAdapter extends BaseExpandableListAdapter {
		private LayoutInflater layoutInflater;
		private LinkedHashMap<String, Vector<Permissions>> groupList;
		private Vector<String> mainGroup;
		private int[] groupStatus;
		private HashMap<String, Permissions> selectedPermissions = new HashMap<String, Permissions>();
		HashMap<String, Vector<Permissions>> getSelectedPermissionsMap = new HashMap<String, Vector<Permissions>>();

		public GroupSettingsAdapter(Context context,
				ExpandableListView listView,
				LinkedHashMap<String, Vector<Permissions>> groupsList) {
			layoutInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			this.groupList = groupsList;
			groupStatus = new int[groupsList.size()];
			if (selectedPermissions.size() > 0) {
				selectedPermissions.clear();
			}
			if (getSelectedPermissionsMap.size() > 0) {
				getSelectedPermissionsMap.clear();
			}
			listView.setOnGroupExpandListener(new OnGroupExpandListener() {

				public void onGroupExpand(int groupPosition) {
					String group = mainGroup.get(groupPosition);
					if (groupList.get(group).size() > 0)
						groupStatus[groupPosition] = 1;
				}

			});

			listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				public void onGroupCollapse(int groupPosition) {
					String group = mainGroup.get(groupPosition);
					if (groupList.get(group).size() > 0)
						groupStatus[groupPosition] = 0;
				}

			});

			mainGroup = new Vector<String>();
			for (Map.Entry<String, Vector<Permissions>> mapEntry : groupList
					.entrySet()) {
				mainGroup.add(mapEntry.getKey());
			}

		}

		@Override
		public Permissions getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			String groupName = mainGroup.get(groupPosition);
			return groupList.get(groupName).get(childPosition);
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		private Vector<Permissions> getChild(String parentGroup) {
			return groupList.get(parentGroup);
		}

		private void getSelectedChild(String memberName) {
			Vector<Permissions> selectedPermissionList = new Vector<Permissions>();
			Set mapSet = (Set) selectedPermissions.entrySet();
			Iterator mapIterator = mapSet.iterator();
			while (mapIterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) mapIterator.next();
				String keyValue = (String) mapEntry.getKey();
				Permissions pbean = (Permissions) mapEntry.getValue();
				selectedPermissionList.add(pbean);
			}
			if (getSelectedPermissionsMap.containsKey(memberName)) {
				getSelectedPermissionsMap.remove(memberName);
				getSelectedPermissionsMap.put(memberName,
						selectedPermissionList);
			} else {
				getSelectedPermissionsMap.put(memberName,
						selectedPermissionList);
			}
		}

		public HashMap<String, Vector<Permissions>> getPermissions() {
			return getSelectedPermissionsMap;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final ChildHolder holder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.group_settings_child_row, null);
				holder = new ChildHolder();
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.selector);
				holder.childTitle = (TextView) convertView
						.findViewById(R.id.permissions);
				convertView.setTag(holder);
			} else {
				holder = (ChildHolder) convertView.getTag();
			}
			final Permissions child = getChild(groupPosition, childPosition);
			holder.childTitle.setText(child.getPermission_name());
			holder.checkBox.setOnCheckedChangeListener(null);
			holder.checkBox.setChecked(child.isPermission());
			Vector<Permissions> childList = getChild(getGroup(groupPosition));
			for (Permissions permissions : childList) {
				selectedPermissions.put(permissions.getPermission_name(),
						permissions);
			}
			getSelectedChild(getGroup(groupPosition));
			holder.checkBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							String parentGroup = getGroup(groupPosition);

							Vector<Permissions> childList = getChild(parentGroup);
							child.setPermission(isChecked);
							int childIndex = childList.indexOf(child);
							for (int i = 0; i < childList.size(); i++) {
								if (i != childIndex) {
									Permissions siblings = childList.get(i);
									holder.checkBox.setChecked(isChecked);
									if (!selectedPermissions
											.containsKey(siblings
													.getPermission_name())) {
										selectedPermissions.remove(siblings
												.getPermission_name());
										selectedPermissions.put(
												siblings.getPermission_name(),
												siblings);
									} else {
										selectedPermissions.put(
												siblings.getPermission_name(),
												siblings);
									}
								}
							}
							getSelectedChild(parentGroup);
							notifyDataSetChanged();

						}

					});

			return convertView;
		}

		private class GroupHolder {
			public TextView title;
			public Button dropDown;

		}

		private class ChildHolder {
			public TextView childTitle;
			public CheckBox checkBox;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			String member = mainGroup.get(groupPosition);
			return groupList.get(member).size();
		}

		@Override
		public String getGroup(int postion) {
			// TODO Auto-generated method stub
			return mainGroup.get(postion);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return mainGroup.size();
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			final GroupHolder holder;

			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.group_settings_parent_row, null);
				holder = new GroupHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.membername);
				// ImageView img_selection = (ImageView) convertView
				// .findViewById(R.id.dropdown);
				Log.i("gchat123", " is expanded : " + isExpanded);
				// if (isExpanded) {
				// img_selection
				// .setBackgroundResource(R.drawable.group_setting_down);
				// } else {
				// img_selection
				// .setBackgroundResource(R.drawable.group_setting_up);
				// }
				convertView.setTag(holder);
			} else {
				holder = (GroupHolder) convertView.getTag();
			}

			final String memberName = getGroup(groupPosition);

			holder.title.setText(memberName);

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	private ArrayList<String> loadGroupMembers(String groupId) {
		ArrayList<String> membersList = new ArrayList<String>();
		try {
			GroupBean gBean = DBAccess.getdbHeler().getGroupAndMembers(
					"select * from groupdetails where groupid=" + groupId);
			if (gBean != null) {
				membersList.clear();

				if (gBean.getActiveGroupMembers() != null
						&& gBean.getActiveGroupMembers().length() > 0) {
					String[] list = (gBean.getActiveGroupMembers()).split(",");
					for (String tmp : list) {
						if (!tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							membersList.add(tmp);
						}
					}
				}
				if (gBean.getInActiveGroupMembers() != null
						&& gBean.getInActiveGroupMembers().length() > 0) {
					String[] list = (gBean.getInActiveGroupMembers())
							.split(",");
					for (String tmp : list) {
						if (!tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							membersList.add(tmp);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return membersList;
	}

	private void loadExpandListView(String groupId) {
		ArrayList<String> membersList = loadGroupMembers(groupId);
		groupsList = new LinkedHashMap<String, Vector<Permissions>>();
		for (String member : membersList) {
			Vector<Permissions> permisssionList = loadGroupMemberChild(groupId,
					member);
			groupsList.put(member, permisssionList);
		}
	}

	private Vector<Permissions> loadGroupMemberChild(String groupId,
			String memberName) {
		Vector<Permissions> permissionlist = new Vector<Permissions>();
		try {
			GroupChatPermissionBean bean = DBAccess.getdbHeler()
					.selectPermissionsForGroup(
							"select * from setgrouppermission where userid='"
									+ CallDispatcher.LoginUser
									+ "' and groupid='" + groupId
									+ "' and groupmember='" + memberName + "'",
							CallDispatcher.LoginUser, groupId,
							CallDispatcher.LoginUser, memberName);
			Log.d("permissions", "came to getsettings" + bean);
			if (bean != null) {

				Permissions ac_permissions = new Permissions();
				ac_permissions.setPermission_name("Audio Conference");
				if (bean.getAudioConference().equals("1"))
					ac_permissions.setPermission(true);
				else
					ac_permissions.setPermission(false);
				permissionlist.add(ac_permissions);

				Permissions vc_permissions = new Permissions();
				vc_permissions.setPermission_name("Video Conference");
				if (bean.getVideoConference().equals("1"))
					vc_permissions.setPermission(true);
				else
					vc_permissions.setPermission(false);
				permissionlist.add(vc_permissions);

				Permissions abc_permissions = new Permissions();
				abc_permissions.setPermission_name("Audio BroadCast");
				if (bean.getAudioBroadcast().equals("1"))
					abc_permissions.setPermission(true);
				else
					abc_permissions.setPermission(false);
				permissionlist.add(abc_permissions);

				Permissions vbc_permissions = new Permissions();
				vbc_permissions.setPermission_name("Video BroadCast");
				if (bean.getVideoBroadcast().equals("1"))
					vbc_permissions.setPermission(true);
				else
					vbc_permissions.setPermission(false);
				permissionlist.add(vbc_permissions);

				Permissions textMsgPermission = new Permissions();
				textMsgPermission.setPermission_name("Text Message");
				if (bean.getTextMessage().equals("1"))
					textMsgPermission.setPermission(true);
				else
					textMsgPermission.setPermission(false);
				permissionlist.add(textMsgPermission);

				Permissions audioMsgPermission = new Permissions();
				audioMsgPermission.setPermission_name("Audio Message");
				if (bean.getAudioMessage().equals("1"))
					audioMsgPermission.setPermission(true);
				else
					audioMsgPermission.setPermission(false);
				permissionlist.add(audioMsgPermission);

				Permissions videoMsgPermission = new Permissions();
				videoMsgPermission.setPermission_name("Video Message");
				if (bean.getVideoMessage().equals("1"))
					videoMsgPermission.setPermission(true);
				else
					videoMsgPermission.setPermission(false);
				permissionlist.add(videoMsgPermission);

				Permissions photoMsgPermission = new Permissions();
				photoMsgPermission.setPermission_name("Photo Message");
				if (bean.getPhotoMessage().equals("1"))
					photoMsgPermission.setPermission(true);
				else
					photoMsgPermission.setPermission(false);

				permissionlist.add(photoMsgPermission);

				Permissions privateMsgPermission = new Permissions();
				privateMsgPermission.setPermission_name("Private Message");
				if (bean.getPrivateMessage().equals("1"))
					privateMsgPermission.setPermission(true);
				else
					privateMsgPermission.setPermission(false);
				permissionlist.add(privateMsgPermission);

				Permissions replyBackMsgPermission = new Permissions();
				replyBackMsgPermission.setPermission_name("Reply");
				if (bean.getReplyBackMessage().equals("1"))
					replyBackMsgPermission.setPermission(true);
				else
					replyBackMsgPermission.setPermission(false);
				permissionlist.add(replyBackMsgPermission);

				Permissions scheduleMsgPermission = new Permissions();
				scheduleMsgPermission.setPermission_name("Schedule");
				if (bean.getScheduleMessage().equals("1"))
					scheduleMsgPermission.setPermission(true);
				else
					scheduleMsgPermission.setPermission(false);
				permissionlist.add(scheduleMsgPermission);

				Permissions deadLineMsgPermission = new Permissions();
				deadLineMsgPermission.setPermission_name("Deadline");
				if (bean.getDeadLineMessage().equals("1"))
					deadLineMsgPermission.setPermission(true);
				else
					deadLineMsgPermission.setPermission(false);
				permissionlist.add(deadLineMsgPermission);

				Permissions withDrawn = new Permissions();
				withDrawn.setPermission_name("Withdraw");
				if (bean.getWithDrawn().equals("1"))
					withDrawn.setPermission(true);
				else
					withDrawn.setPermission(false);
				permissionlist.add(withDrawn);
				
				Permissions Chat = new Permissions();
				Chat.setPermission_name("Chat");
				if (bean.getChat().equals("1"))
					Chat.setPermission(true);
				else
					Chat.setPermission(false);
				permissionlist.add(Chat);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return permissionlist;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.save:
			savePermissions();
			break;
		case R.id.btn_cancel:
			finish();
			break;

		default:
			break;
		}
	}

	private void savePermissions() {
		try {
			if (gcPermissionList.size() > 0) {
				gcPermissionList.clear();
			}
			Set mapSet = (Set) adapter.getPermissions().entrySet();
			groupBean.setOwnerName(CallDispatcher.LoginUser);
			groupBean.setGroupId(groupId);
			groupBean.setType("i");
			groupBean.setGroupMembers("");
			Iterator mapIterator = mapSet.iterator();
			while (mapIterator.hasNext()) {
				Vector<GroupChatPermissionBean> gcpList = new Vector<GroupChatPermissionBean>();
				GroupChatPermissionBean gcpBean = new GroupChatPermissionBean();
				Map.Entry mapEntry = (Map.Entry) mapIterator.next();
				String keyValue = (String) mapEntry.getKey();
				Vector<Permissions> permissionValues = (Vector<Permissions>) mapEntry
						.getValue();
				gcpBean.setGroupMember(keyValue);
				for (Permissions permissions : permissionValues) {
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Audio Conference")) {
						if (permissions.isPermission()) {
							gcpBean.setAudioConference("1");
						} else {
							gcpBean.setAudioConference("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Video Conference")) {
						if (permissions.isPermission()) {
							gcpBean.setVideoConference("1");
						} else {
							gcpBean.setVideoConference("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Audio BroadCast")) {
						if (permissions.isPermission()) {
							gcpBean.setAudioBroadcast("1");
						} else {
							gcpBean.setAudioBroadcast("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Video BroadCast")) {
						if (permissions.isPermission()) {
							gcpBean.setVideoBroadcast("1");
						} else {
							gcpBean.setVideoBroadcast("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Text Message")) {
						if (permissions.isPermission()) {
							gcpBean.setTextMessage("1");
						} else {
							gcpBean.setTextMessage("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Audio Message")) {
						if (permissions.isPermission()) {
							gcpBean.setAudioMessage("1");
						} else {
							gcpBean.setAudioMessage("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Video Message")) {
						if (permissions.isPermission()) {
							gcpBean.setVideoMessage("1");
						} else {
							gcpBean.setVideoMessage("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Photo Message")) {
						if (permissions.isPermission()) {
							gcpBean.setPhotoMessage("1");
						} else {
							gcpBean.setPhotoMessage("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Private Message")) {
						if (permissions.isPermission()) {
							gcpBean.setPrivateMessage("1");
						} else {
							gcpBean.setPrivateMessage("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Reply")) {
						if (permissions.isPermission()) {
							gcpBean.setReplyBackMessage("1");
						} else {
							gcpBean.setReplyBackMessage("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Schedule")) {
						if (permissions.isPermission()) {
							gcpBean.setScheduleMessage("1");
						} else {
							gcpBean.setScheduleMessage("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Deadline")) {
						if (permissions.isPermission()) {
							gcpBean.setDeadLineMessage("1");
						} else {
							gcpBean.setDeadLineMessage("0");
						}
					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Withdraw")) {
						if (permissions.isPermission()) {
							gcpBean.setWithDrawn("1");
						} else {
							gcpBean.setWithDrawn("0");
						}

					}
					if (permissions.getPermission_name().equalsIgnoreCase(
							"Chat")) {
						if (permissions.isPermission()) {
							gcpBean.setChat("1");
						} else {
							gcpBean.setChat("0");
						}

					}
				}
				gcpList.add(gcpBean);
				gcPermissionList.addAll(gcpList);
			}
			groupBean.setCallback(context);
			WebServiceReferences.webServiceClient.setGroupChatSettings(
					groupBean, gcPermissionList);
			showDialog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyWebserviceResponse(Object obj) {
		try {
			cancelDialog();
			if (obj instanceof String[]) {
				String[] result = (String[]) obj;
				if (result[0] != null) {
					if (result[0]
							.equalsIgnoreCase("Successfully settings updated")) {
						if (groupBean != null && gcPermissionList != null
								&& gcPermissionList.size() > 0) {
							for (GroupChatPermissionBean gcpBean : gcPermissionList) {
								gcpBean.setGroupId(result[1]);
								gcpBean.setUserId(CallDispatcher.LoginUser);
								gcpBean.setGroupOwner(CallDispatcher.LoginUser);
								DBAccess.getdbHeler()
										.insertorUpdateGroupChatSettings(
												gcpBean);
							}
						}
						showToast(result[0]);
					} else {
						showToast(result[0]);
					}
				} else {
					showToast("Unable to save permission.Please try again");
				}
			} else if (obj instanceof String) {
				showToast((String) obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void cancelDialog() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				Log.i("register", "--progress bar end-----");
				progressDialog.dismiss();
				progressDialog = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showDialog() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					progressDialog = new ProgressDialog(context);
					if (progressDialog != null) {
						progressDialog.setCancelable(false);
						progressDialog.setMessage("Progress ...");
						progressDialog
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setProgress(0);
						progressDialog.setMax(100);
						progressDialog.show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	public void notifyGetGroupChatSettingsResponse() {

	}
}
