package com.group;

import java.util.Vector;

import org.lib.model.GroupBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class ViewGroups extends Activity implements OnClickListener {
	private Context context;
	private Button btn_back = null;
	private TextView ed_groupname = null;
	private ListView lv_buddylist = null;
	private TextView dateTime = null;
	private Vector<UserBean> membersList = new Vector<UserBean>();
	private CallDispatcher callDisp = null;
	private BuddyAdapter adapter = null;
	private Button leaveGroup = null;
	private GroupBean groupBean;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.groupviewcontact);
		context = this;
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		ed_groupname = (TextView) findViewById(R.id.ed_creategroup);
		lv_buddylist = (ListView) findViewById(R.id.lv_buddylist);
		dateTime = (TextView) findViewById(R.id.tx_datetime);
		leaveGroup = (Button) findViewById(R.id.leave_group);
		leaveGroup.setOnClickListener(this);
		WebServiceReferences.contextTable.put("viewgroup", context);
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		adapter = new BuddyAdapter(this, membersList);
		lv_buddylist.setAdapter(adapter);
		CallDispatcher.pdialog = new ProgressDialog(context);

		String groupid = getIntent().getStringExtra("id");
		groupBean = callDisp.getdbHeler(context).getGroup(
				"select * from grouplist where groupid=" + groupid);
		if (groupBean != null) {
			dateTime.setText(groupBean.getModifiedDate());
			dateTime.setVisibility(View.VISIBLE);
			ed_groupname.setText(groupBean.getGroupName());

			GroupBean gBean = callDisp.getdbHeler(context).getGroupAndMembers(
					"select * from groupdetails where groupid="
							+ groupBean.getGroupId());

			membersList.add(getSelfUserBean());
			if (gBean != null) {
				if (gBean.getActiveGroupMembers() != null
						&& gBean.getActiveGroupMembers().length() > 0) {
					String[] list = (gBean.getActiveGroupMembers()).split(",");
					for (String tmp : list) {
						UserBean userBean = new UserBean();
						userBean.setBuddyName(tmp);
						userBean.setSelected(true);
						userBean.setAllowChecking(false);
						membersList.add(userBean);
					}
				}

				if (gBean.getInActiveGroupMembers() != null
						&& gBean.getInActiveGroupMembers().length() > 0) {
					String[] list = (gBean.getInActiveGroupMembers())
							.split(",");
					for (String tmp : list) {
						UserBean userBean = new UserBean();
						userBean.setBuddyName(tmp);
						userBean.setSelected(true);
						userBean.setAllowChecking(false);
						membersList.add(userBean);
					}
				}
			}

			adapter.notifyDataSetChanged();

		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("viewgroup");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.leave_group:
			if (SingleInstance.mainContext.isNetworkConnectionAvailable()){

			callDisp.showprogress(CallDispatcher.pdialog, context);
			WebServiceReferences.webServiceClient.leaveGroup(
					groupBean.getGroupId(), CallDispatcher.LoginUser);
			}
			else
			{
				showAlert1("Info","Check internet connection Unable to connect server");
			}
		default:
			break;
		}

	}
	public void showAlert1(String title,String message) {

		try {
			AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
			alertCall
					.setMessage(message).setTitle(title)
					.setCancelable(false)
					.setNegativeButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									try {

									} catch (Exception e) {
										if (AppReference.isWriteInFile)
											AppReference.logger.error(
													e.getMessage(), e);
										else
											e.printStackTrace();
									}
								}
							});
			alertCall.show();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}
	public void notifyDeleteGroup(Object obj) {
		if (obj instanceof GroupBean) {
			String groupId = ((GroupBean) obj).getGroupId();
			if (groupId != null) {
				DBAccess.getdbHeler().deleteGroupAndMembers(groupId);
				for (GroupBean gBean : GroupActivity.groupList) {
					if (gBean.getGroupId().equals(groupId)) {
						GroupActivity.groupList.remove(gBean);
						handler.post(new Runnable() {

							@Override
							public void run() {
								GroupActivity.groupAdapter
										.notifyDataSetChanged();
								GroupActivity.groupAdapter2
								.notifyDataSetChanged();
							}
						});
						break;
					}
				}
				callDisp.getdbHeler(context).deleteGroupChatEntryLocally(
						groupId, CallDispatcher.LoginUser);
				finish();
			}
		} else {
			WebServiceBean ws_bean = (WebServiceBean) obj;
			callDisp.ShowError("Error", ws_bean.getText(), context);
		}
		ContactsFragment.getInstance(context).getList();
		callDisp.cancelDialog();
	}

	private UserBean getSelfUserBean() {
		UserBean uBean = new UserBean();
		uBean.setBuddyName(groupBean.getOwnerName());
		uBean.setSelected(true);
		uBean.setOwner(true);
		uBean.setAllowChecking(false);
		return uBean;
	}

}
