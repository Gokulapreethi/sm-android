package com.exchanges;

import java.util.ArrayList;
import java.util.Vector;

import org.lib.model.GroupBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.chat.ChatBean;
import com.group.GroupActivity;
import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;

public class ExchangesActivity extends Activity implements OnClickListener,
		SlideMenuInterface.OnSlideMenuItemClickListener {
	private Context context;
	private ListView listView;
	public ExchangesAdapter adapter;
	public Vector<GroupBean> exchangesList = new Vector<GroupBean>();
	private CallDispatcher callDisp;
	private Button btnBack;
	private Button addGroup;
	private SlideMenu slidemenu;
	private Handler handler = new Handler();
	private boolean isLaunch = false;
	private PopupWindow popupWindowMenus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);
		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;
		isLaunch = getIntent().getBooleanExtra("isLaunch", false);
		CallDispatcher.pdialog = new ProgressDialog(context);
		if (isLaunch)
			callDisp.showprogress(CallDispatcher.pdialog, context);
		else {
			Vector<GroupBean> gList = callDisp.getdbHeler(context)
					.getAllGroups(CallDispatcher.LoginUser,"group");
//			Vector<String> chatList = callDisp.getdbHeler(context)
//					.getLoadChatHistoryForExchanges();
			if (gList != null && gList.size() > 0) {
				exchangesList.addAll(gList);
//				exchangesList.addAll(chatList);
			}
		}

		WebServiceReferences.contextTable.put("exchanges", context);
		ShowList();
		setContentView(R.layout.exchanges);
		listView = (ListView) findViewById(R.id.exchangeslist);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		addGroup = (Button) findViewById(R.id.add_group);
		addGroup.setOnClickListener(this);
		adapter = new ExchangesAdapter(context, exchangesList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Object obj = adapter.getItem(position);
				if (obj instanceof GroupBean) {
					GroupBean groupBean = (GroupBean) adapter.getItem(position);
					GroupBean gBean = callDisp.getdbHeler(context)
							.getGroupAndMembers(
									"select * from groupdetails where groupid="
											+ groupBean.getGroupId());
					if (gBean != null
							&& gBean.getActiveGroupMembers().length() > 0) {
						Intent intent = new Intent(context,
								GroupChatActivity.class);
						intent.putExtra("isGroup", true);
						intent.putExtra("groupBean", groupBean.getGroupId());
						startActivity(intent);
					} else {
						callDisp.showToast(context, "Sorry no members to chat");
					}
				} else if (obj instanceof String) {
//					String buddyName = (String) adapter.getItem(position);
//					Intent intent = new Intent(context, ChatActivity.class);
//					intent.putExtra("buddy", buddyName);
//					intent.putExtra("status", "");
//					intent.putExtra("sessionid", "");
//					startActivity(intent);
//					Intent intent = new Intent(context, GroupChatActivity.class);
//					intent.putExtra("groupid", CallDispatcher.LoginUser
//							+ buddyName);
//					intent.putExtra("isGroup", false);
//					intent.putExtra("buddy", buddyName);
//					startActivity(intent);
				}

			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Object obj = adapter.getItem(position);
				if (obj instanceof GroupBean) {
					GroupBean groupBean = (GroupBean) adapter.getItem(position);
					popupWindowMenus = popupWindowMenus(groupBean);
					popupWindowMenus.showAsDropDown(
							arg1.findViewById(R.id.image_arroq), -5, 0);

				} else if (obj instanceof ChatBean) {
				}

				return true;
			}
		});

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private PopupWindow popupWindowMenus(final GroupBean gBean) {
		try {
			PopupWindow popupWindow = new PopupWindow(this);

			ListView listViewMenus = new ListView(this);
			String popUpContents[] = { "Delete" };
			listViewMenus.setAdapter(callDisp.menusAdapter(popUpContents,
					context));
			listViewMenus.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					popupWindowMenus.dismiss();

					if (position == 0) {
						callDisp.getdbHeler(context)
								.deleteGroupChatEntryLocally(
										gBean.getGroupId(),
										CallDispatcher.LoginUser);
						gBean.setLastMsg("");
						callDisp.getdbHeler(context).saveOrUpdateGroup(gBean);
						exchangesList.remove(gBean);
						adapter.notifyDataSetChanged();
					}
				}

			});
			popupWindow.setFocusable(true);
			popupWindow.setWidth(220);
			popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

			popupWindow.setContentView(listViewMenus);

			return popupWindow;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			slidemenu.show();
			break;
		case R.id.add_group:
			Intent intent = new Intent(context, GroupActivity.class);
			startActivity(intent);
		default:
			break;
		}

	}

	protected void ShowList() {
		try {
			// TODO Auto-generated method stub

			setContentView(R.layout.history_container);

			slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
			ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
			callDisp.composeList(datas);
			slidemenu.init(ExchangesActivity.this, datas,
					ExchangesActivity.this, 100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.USERPROFILE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.UTILITY:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.NOTES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.APPS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.CLONE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.SETTINGS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;

		case WebServiceReferences.QUICK_ACTION:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.FORMS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.FEEDBACK:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.EXCHANGES:
			break;
		default:
			break;
		}
	}

	public void notifyUI() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				adapter.notifyDataSetChanged();
			}
		});

	}

	public void notifyGroupList(final boolean isLoaded) {

		// TODO Auto-generated method stub
		final Vector<GroupBean> gList = callDisp.getdbHeler(context)
				.getAllGroups(CallDispatcher.LoginUser,"group");
//		final Vector<String> chatList = callDisp.getdbHeler(context)
//				.getLoadChatHistoryForExchanges();

		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					if (gList != null && gList.size() > 0) {
						exchangesList.clear();
						exchangesList.addAll(gList);
//						exchangesList.addAll(chatList);
						adapter.notifyDataSetChanged();
						if (isLoaded) {
							callDisp.cancelDialog();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

}
