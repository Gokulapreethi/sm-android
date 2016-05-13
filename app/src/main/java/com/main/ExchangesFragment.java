package com.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.lib.model.GroupBean;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.SlideMenu.Slidebean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.chat.ChatBean;
import com.exchanges.ExchangesAdapter;
import com.group.GroupActivity;
import com.group.GroupAdapter;
import com.group.chat.GroupChatActivity;
import com.util.ExchangesComparator;
import com.util.SingleInstance;

public class ExchangesFragment extends Fragment {

	private ListView listView;
	public ExchangesAdapter adapter;
	public Vector<GroupBean> exchangesList = new Vector<GroupBean>();
	private static CallDispatcher callDisp;
	// private Button btnBack;
	// private Button addGroup;
	private Handler handler = new Handler();
	private boolean isLaunch = false;

	private PopupWindow popupWindowMenus;

	private static ExchangesFragment exchangesFragment;

	private static Context mainContext;

	private ProgressDialog progress = null;

	public View _rootView;
	private Typeface tf_regular = null;
	
	private Typeface tf_bold = null;
	public boolean isPendingshowing = false;


	private TextView title = null; // this Textview used fragment title change

	public static ExchangesFragment newInstance(Context context) {
		try {
			if (exchangesFragment == null) {
				mainContext = context;
				exchangesFragment = new ExchangesFragment();
				callDisp = CallDispatcher.getCallDispatcher(mainContext);
			}

			return exchangesFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return exchangesFragment;
		}
	}

	public void setLaunch(boolean isLaunch) {
		this.isLaunch = isLaunch;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			 final AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN"); 
			tf_regular = Typeface.createFromAsset(mainContext.getAssets(),
					getResources().getString(R.string.fontfamily));
	        tf_bold = Typeface.createFromAsset(mainContext.getAssets(),
					getResources().getString(R.string.fontfamilybold));

			Button select = (Button) getActivity().findViewById(R.id.btn_brg);
			select.setVisibility(View.GONE);
			RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
			mainHeader.setVisibility(View.VISIBLE);
			LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
			contact_layout.setVisibility(View.VISIBLE);
			final Button all_contact = (Button) getActivity().findViewById(
					R.id.all_contacts);
			all_contact.setText(SingleInstance.mainContext.getResources().getString(R.string.chat_history_tab));
			final Button pending_contact = (Button) getActivity().findViewById(
					R.id.pending_contacts);
			pending_contact.setText(SingleInstance.mainContext.getResources().getString(R.string.call_history_tab));

			Button imVw = (Button) getActivity().findViewById(R.id.im_view);
			imVw.setVisibility(View.GONE);

			Button setting = (Button) getActivity().findViewById(
					R.id.btn_settings);
			setting.setVisibility(View.GONE);
			setting.setText("");

			Button plusBtn = (Button) getActivity()
					.findViewById(R.id.add_group);
			plusBtn.setVisibility(View.GONE);
			plusBtn.setBackgroundResource(R.drawable.toolbar_buttons_plus);
			plusBtn.setText("");
//			plusBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					try {
//						Intent intent = new Intent(mainContext,
//								GroupActivity.class);
//						mainContext.startActivity(intent);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
			Button clearall = (Button) getActivity().findViewById(R.id.btn_settings);
			clearall.setVisibility(View.GONE);
			Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
			backBtn.setVisibility(View.GONE);
			TextView title = (TextView) getActivity().findViewById(
					R.id.activity_main_content_title);
			title.setText(getResources().getString(R.string.conversations));
			title.setVisibility(View.GONE);
			all_contact.setTextColor(R.color.title);
			pending_contact.setTextColor(R.color.title);
			
			all_contact.setBackgroundResource(R.drawable.rounded_border_custom_profile_2);
			pending_contact.setBackgroundResource(R.drawable.rounded_border_custom_profile);
			
			all_contact.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// CallDispatcher.adapterToShow.notifyDataSetChanged();
					// lv.setAdapter(CallDispatcher.adapterToShow);
//					if(isPendingshowing){
						Log.i("Test", "ExchangesFragment ........");
						appMainActivity.exchangeFragment();
						all_contact.setBackgroundResource(R.drawable.rounded_border_custom_profile_2);
						pending_contact.setBackgroundResource(R.drawable.rounded_border_custom_profile);
						isPendingshowing = false;
//					}
				}
			});

			pending_contact.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if(!isPendingshowing){
						Log.i("Test", "ExchangesFragment ........");
						appMainActivity.historyfragment();
					    all_contact.setBackgroundResource(R.drawable.rounded_border_custom_profile);
						pending_contact	.setBackgroundResource(R.drawable.rounded_border_custom_profile_2);
						isPendingshowing = true;
					}
//				}
			});

			
			if (_rootView == null) {
				_rootView = inflater.inflate(R.layout.exchanges, null);
				getActivity().getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
				try {
					Button settings = (Button) getActivity().findViewById(
							R.id.btn_settings);
					settings.setVisibility(View.GONE);
					Button selectall = (Button) getActivity().findViewById(
							R.id.btn_brg);
					selectall.setVisibility(View.GONE);

					if (WebServiceReferences.callDispatch
							.containsKey("calldisp"))
						callDisp = (CallDispatcher) WebServiceReferences.callDispatch
								.get("calldisp");
					else
						callDisp = new CallDispatcher(mainContext);

					progress = new ProgressDialog(mainContext);

					// isLaunch = getArguments().getBoolean("isLaunch", false);

					GroupActivity.getAllGroups();
					GroupActivity.groupAdapter = new GroupAdapter(mainContext,
							R.layout.grouplist, GroupActivity.groupList);
					adapter = new ExchangesAdapter(mainContext, exchangesList);

					WebServiceReferences.contextTable.put("exchanges",
							mainContext);
					ShowList();
					listView = (ListView) _rootView
							.findViewById(R.id.exchangeslist);
					listView.setAdapter(adapter);
					if (isLaunch) {
						// loadExchanges();
						showprogress();
					} else {
						Vector<GroupBean> gList = callDisp.getdbHeler(
								mainContext).getAllGroups(
								CallDispatcher.LoginUser,"group");
						for (GroupBean gBean : gList) {
							gBean.setMessageCount(DBAccess.getdbHeler()
									.getUnreadMsgCountById(gBean.getGroupId(),CallDispatcher.LoginUser));
						}
						Vector<GroupBean> chatList = callDisp.getdbHeler(
								mainContext).getAllIndividualChat(
								CallDispatcher.LoginUser);
						if (gList != null && chatList != null)
							Log.i("chat123", "grouplist size : " + gList.size()
									+ " and chatlist size : " + chatList.size());
						for (GroupBean gBean : chatList) {
							gBean.setMessageCount(DBAccess.getdbHeler()
									.getUnreadMsgCountById(gBean.getGroupId(),CallDispatcher.LoginUser));
						}
						if (gList != null && gList.size() > 0) {
							exchangesList.clear();
							exchangesList.addAll(gList);
							exchangesList.addAll(chatList);
							Collections.sort(exchangesList,
									new ExchangesComparator());
						}

						adapter.notifyDataSetChanged();
					}

					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							Object obj = adapter.getItem(position);
							if (obj instanceof GroupBean) {
								GroupBean groupBean = (GroupBean) adapter
										.getItem(position);
								if (groupBean.getCategory().equalsIgnoreCase(
										"G")) {
									GroupBean gBean = DBAccess.getdbHeler(
											mainContext).getGroupAndMembers(
											"select * from groupdetails where groupid="
													+ groupBean.getGroupId());
									if (gBean != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
													.length() > 0) {
										Intent intent = new Intent(mainContext,
												GroupChatActivity.class);
										intent.putExtra("isGroup", true);
										intent.putExtra("groupid",
												groupBean.getGroupId());
										startActivity(intent);

									} else {
										callDisp.showToast(mainContext,
												"Sorry no members to chat");
									}
								} else if (groupBean.getCategory()
										.equalsIgnoreCase("I")) {

											Intent intent = new Intent(mainContext,
													GroupChatActivity.class);
											intent.putExtra("groupid",
													groupBean.getGroupName());
											intent.putExtra("isGroup", false);
											intent.putExtra("buddy",
													groupBean.getGroupId());
											startActivity(intent);
										}

									}

							}

					});

					listView.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
													   View arg1, int position, long arg3) {
							// TODO Auto-generated method stub

							Object obj = adapter.getItem(position);
							if (obj instanceof GroupBean) {
								GroupBean groupBean = (GroupBean) adapter
										.getItem(position);
								popupWindowMenus = popupWindowMenus(groupBean);
								popupWindowMenus.showAsDropDown(
										arg1.findViewById(R.id.image_arroq),
										-5, 0);

							} else if (obj instanceof ChatBean) {
							}

							return true;
						}
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
			} else {
				((ViewGroup) _rootView.getParent()).removeView(_rootView);
			}
			return _rootView;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return _rootView;
		}
	}

	private PopupWindow popupWindowMenus(final GroupBean gBean) {
		try {
			PopupWindow popupWindow = new PopupWindow(mainContext);

			ListView listViewMenus = new ListView(mainContext);
			final String popUpContents[] = { "Delete" };
			listViewMenus.setAdapter(callDisp.menusAdapter(popUpContents,
					mainContext));
			listViewMenus.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					popupWindowMenus.dismiss();

					if (popUpContents[position].equals("Delete")) {
						callDisp.getdbHeler(mainContext)
								.deleteGroupChatEntryLocally(
										gBean.getGroupId(),
										CallDispatcher.LoginUser);
						callDisp.getdbHeler(mainContext).deleteAllIndividualChat(gBean
								.getGroupId());
						if (SingleInstance.groupChatHistory.containsKey(gBean
								.getGroupId())) {
							SingleInstance.groupChatHistory.remove(gBean
									.getGroupId());

						}
						gBean.setLastMsg("");
						if(gBean.getCategory().equalsIgnoreCase("G"))
						callDisp.getdbHeler(mainContext).saveOrUpdateGroup(gBean);
						else
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
	public void onDestroy() {
		try {
			// TODO Auto-generated method stub
			super.onDestroy();
			// MemoryProcessor.getInstance().unbindDrawables(_rootView);
			// _rootView = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Override
	// public void onClick(View v) {
	// try {
	// switch (v.getId()) {
	// case R.id.add_group:
	// Intent intent = new Intent(getActivity().getApplicationContext(),
	// GroupActivity.class);
	// getActivity().startActivity(intent);
	// default:
	// break;
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	protected void ShowList() {
		try {
			// TODO Auto-generated method stub
			ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
			callDisp.composeList(datas);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	public void notifyGroupList() {
		try {
			Vector<GroupBean> gList = DBAccess.getdbHeler(mainContext)
					.getAllGroups(CallDispatcher.LoginUser,"group");
			// Vector<ChatBean> chatList = callDisp.getdbHeler(mainContext)
			// .getLoadChatHistoryForExchanges();
			for (GroupBean gBean : gList) {
				gBean.setMessageCount(DBAccess.getdbHeler()
						.getUnreadMsgCountById(gBean.getGroupId(),CallDispatcher.LoginUser));
			}
			Vector<GroupBean> chatList = DBAccess.getdbHeler()
					.getAllIndividualChat(CallDispatcher.LoginUser);
			for (GroupBean gBean : chatList) {
				gBean.setMessageCount(DBAccess.getdbHeler()
						.getUnreadMsgCountById(gBean.getGroupId(),CallDispatcher.LoginUser));
			}
			if (gList != null && gList.size() > 0) {
				exchangesList.clear();
				exchangesList.addAll(gList);
				exchangesList.addAll(chatList);
				Collections.sort(exchangesList, new ExchangesComparator());
			}
			notifyUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyUI() {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (adapter != null)
						adapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showprogress() {

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (progress != null) {
						Log.i("register", "--progress bar start exchange-----");
						progress.setCancelable(true);
						progress.setMessage("Progress ...");
						progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progress.setProgress(0);
						progress.setMax(100);
						progress.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void cancelDialog() {

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (progress != null) {
						Log.i("register", "--progress bar end exchange-----");
						progress.dismiss();
						progress = null;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public View getParentView() {
		return _rootView;
	}

	private void loadOtherInfo() {
		/* For getting Profile template info */

		String modifiedDate = DBAccess
				.getdbHeler()
				.getModifiedDate(
						"select profiletimestamp from profiletemplate where profileid=5");
		if (modifiedDate == null) {
			modifiedDate = "";
		} else if (modifiedDate.trim().length() == 0) {
			modifiedDate = "";
		}
		// WebServiceReferences.webServiceClient.getProfileTemplate(modifiedDate);
		/*
		 * Ends here
		 */

		/* For getting Avatar configs */
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String dateTime = DBAccess.getdbHeler()
						.getMaxDateofOfflineConfig(CallDispatcher.LoginUser);

				if (dateTime == null) {
					dateTime = "\"\"";
				}

				String params[] = new String[2];
				params[0] = CallDispatcher.LoginUser;
				params[1] = dateTime;
				// if (WebServiceReferences.running) {
				// WebServiceReferences.webServiceClient
				// .GetMyConfigurationDetails(params);
				// }
			}
		});
		/*
		 * Ends here
		 */
		/*
		 * For getting Utility Items
		 */
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				// if (WebServiceReferences.running) {
				// WebServiceReferences.webServiceClient
				// .getBlockedBuddyList(CallDispatcher.LoginUser);
				//
				// Vector<UtilityBean> utility_list = DBAccess.getdbHeler()
				// .SelectUtilityRecords(
				// "select * from utility where userid='"
				// + CallDispatcher.LoginUser + "'");
				// // For temp, server will not consider
				// // utilityName, So hardcoded -
			
				// String utilityName = "buysell";
				// WebServiceReferences.webServiceClient
				// .syncUtilityItems(CallDispatcher.LoginUser,
				// utilityName, utility_list);
				// utilityName = "serviceneededprovider";
				// WebServiceReferences.webServiceClient
				// .syncUtilityItems(CallDispatcher.LoginUser,
				// utilityName, utility_list);
				//
				// }
			}

		});
	}
	public void clearUI(String selectedBuddy)
	{
		callDisp.getdbHeler(mainContext)
				.deleteGroupChatEntryLocally(
						selectedBuddy,
						CallDispatcher.LoginUser);

		if (SingleInstance.groupChatHistory.containsKey(selectedBuddy)) {
			SingleInstance.groupChatHistory.remove(selectedBuddy);

		}
		for (GroupBean gBean : exchangesList) {
			if(gBean.getGroupId().equals(selectedBuddy))
				if(gBean.getGroupId()!=null)
				exchangesList.remove(gBean.getGroupId());
		}
		adapter.notifyDataSetChanged();
	}

}
