package com.group.chat;


import java.util.Vector;

import org.lib.model.GroupBean;

import com.bean.UserBean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.group.BuddyAdapter;
import com.main.AppMainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;


public class MemberListActivity extends Activity implements OnClickListener {
	
	private Vector<UserBean> membersList = new Vector<UserBean>();
	private BuddyAdapter adapter;
	private Context context;
	private CallDispatcher callDisp;
	private ListView memberListView;
	private Button sendmembers;
	private Button cancelmembers;
	String groupId = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.member_list);
		context = this;
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);
		memberListView = (ListView) findViewById(R.id.listViews1);
		adapter = new BuddyAdapter(MemberListActivity.this, membersList);
		groupId = getIntent().getStringExtra("groupid");
		loadGroupMembers(groupId);
		memberListView.setAdapter(adapter);
		sendmembers=(Button)findViewById(R.id.sendmember);
		cancelmembers=(Button)findViewById(R.id.cancelmember);



		cancelmembers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub


//				Toast.makeText(MemberListActivity.this, "thiss",Toast.LENGTH_LONG).show();

				setResult(RESULT_CANCELED);
			       finish();


			}
		});


		sendmembers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(MemberListActivity.this, "send",Toast.LENGTH_LONG).show();

				String selectedMembers = "";
			       for (UserBean userBean : membersList) {
			        if (!userBean.getBuddyName().equalsIgnoreCase(
			          CallDispatcher.LoginUser)) {
			         if (userBean.isSelected()) {
			          selectedMembers = selectedMembers
			            + userBean.getBuddyName() + ",";
			         }

			        }
			       }
			       if (selectedMembers.length() > 0) {
			        selectedMembers = selectedMembers.substring(0,
			          selectedMembers.length() - 1);
			       }
			       Intent resultIntent = new Intent();
			       resultIntent.putExtra("SELECTED_MEMBERS", selectedMembers);
			       setResult(RESULT_OK, resultIntent);
			       finish();
			}
		});

		
		
		}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private void loadGroupMembers(String groupId) {
		try {
			Log.i("list", "groupId "+groupId);

			
			GroupBean gBean = callDisp.getdbHeler(context).getGroupAndMembers(
					"select * from groupdetails where groupid=" + groupId);
			if (gBean != null) {
				membersList.clear();
				UserBean selfUser = getSelfUserBean(gBean);
				if (selfUser != null)
					membersList.add(selfUser);
				if (gBean.getActiveGroupMembers() != null
						&& gBean.getActiveGroupMembers().length() > 0) {
					String[] list = (gBean.getActiveGroupMembers()).split(",");
					for (String tmp : list) {
						if (!tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							UserBean userBean = new UserBean();
							userBean.setBuddyName(tmp);
							userBean.setSelected(false);
							membersList.add(userBean);
						}
					}
				}
				if (gBean.getInActiveGroupMembers() != null
						&& gBean.getInActiveGroupMembers().length() > 0) {
					String[] list = (gBean.getInActiveGroupMembers())
							.split(",");
					for (String tmp : list) {
						if (!tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							UserBean userBean = new UserBean();
							userBean.setBuddyName(tmp);
							userBean.setSelected(false);
							membersList.add(userBean);
						}
					}
				}
				Log.i("list", "membersList.size(): "+membersList.size());
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserBean getSelfUserBean(GroupBean groupBean) {
		try {
			if (!groupBean.getOwnerName().equalsIgnoreCase(
					CallDispatcher.LoginUser)) {
				UserBean uBean = new UserBean();
				uBean.setBuddyName(groupBean.getOwnerName());
				uBean.setSelected(false);
				return uBean;
			} else
				return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		
		
	}
	

}