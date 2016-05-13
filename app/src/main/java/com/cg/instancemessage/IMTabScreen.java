package com.cg.instancemessage;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//
//import org.lib.model.SignalingBean;
//
//import android.app.LocalActivityManager;
//import android.app.TabActivity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.TabHost;
//import android.widget.TabHost.OnTabChangeListener;
//
//import com.cg.commonclass.CallDispatcher;
//import com.cg.commonclass.WebServiceReferences;
//import com.cg.files.CompleteListView;
//import com.chat.ChatActivity;
//
///**
// * This is a Tab activity.If Tab view is already exist open the exist Tab
// * otherwise generate the new Tab and add that to the tab activity
// * 
// * 
// */
//public class IMTabScreen extends TabActivity {
//	public TabHost tabHost;
//
//	private Handler handler = new Handler();
//
//	private Context context = null;
//
//	private SignalingBean sb = null;
//
//	private boolean fromTo = false;
//
//	private CallDispatcher callDisp;
//
//	private String strPrevScreen = null;
//
//	private String buddyStatus = null;
//
//	/**
//	 * Called when the activity is starting
//	 */
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		try {
//			// TODO Auto-generated method stub
//			super.onCreate(savedInstanceState);
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//		WebServiceReferences.contextTable.put("imtabs", this);
//
//		context = this;
//		tabHost = getTabHost();
//		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
//
//			@Override
//			public void onTabChanged(String tabId) {
//
//				getWindow()
//						.setSoftInputMode(
//								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//			}
//		});
//		sb = (SignalingBean) getIntent().getSerializableExtra("sb");
//		fromTo = getIntent().getBooleanExtra("fromto", false);
//
//		buddyStatus = getIntent().getStringExtra("status");
//
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//		int noScrHeight = displaymetrics.heightPixels;
//		int noScrWidth = displaymetrics.widthPixels;
//
//		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
//			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
//					.get("calldisp");
//		else
//			callDisp = new CallDispatcher(context);
//
//		callDisp.setNoScrHeight(noScrHeight);
//		callDisp.setNoScrWidth(noScrWidth);
//		displaymetrics = null;
//
//			notifyInstantMessage(sb, fromTo);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * This will notify the details about the IM to the particular tab
//	 * 
//	 * @param sb
//	 *            - instance of SignalingBean
//	 * @param from
//	 *            - token to produce the sender or receiver ui generation
//	 */
//	public void notifyInstantMessage(final SignalingBean sb, final boolean from) {
//		try {
//			handler.post(new Runnable() {
//
//				@Override
//				public void run() {
//					Log.d("imclick", "imtab notiftim exsist");
//					Log.d("imclick", "imt " + sb.getSessionid());
//					if (WebServiceReferences.instantMessageScreen
//							.containsKey(sb.getSessionid())) {
//						InstantMessageScreen instantMessage = (InstantMessageScreen) WebServiceReferences.instantMessageScreen
//								.get(sb.getSessionid());
//
//						instantMessage.updateMsg(sb.getConferencemember(),
//								sb.getMessage(), sb.getFrom(), sb);
//					} else {
//						if (!from) {
//							if (WebServiceReferences.activeSession
//									.containsKey(sb.getFrom())
//									&& !WebServiceReferences.activeSession.get(
//											sb.getFrom()).equals(
//											sb.getSessionid())) {
//								InstantMessageScreen screen = (InstantMessageScreen) WebServiceReferences.instantMessageScreen
//										.get(WebServiceReferences.activeSession
//												.get(sb.getFrom().trim()));
//								if (screen != null) {
//									screen.changeSessionHash(sb);
//									screen.updateMsg(sb.getFrom(),
//											sb.getMessage(), sb.getFrom(), sb);
//								} else {
//									addTab(sb, from);
//								}
//							} else {
//
//								if (WebServiceReferences.contextTable
//										.get("MAIN") instanceof CompleteListView) {
//									CompleteListView mInstance = (CompleteListView) WebServiceReferences.contextTable
//											.get("MAIN");
//									Log.d("IM",
//											"###############Tab screen view");
//									if (!callDisp.getdbHeler(context)
//											.userChatting(sb.getFrom())) {
//										callDisp.PutImEntry(sb.getSessionid(),
//												sb.getFrom(), sb.getTo(), 0,
//												mInstance.owner);
//
//									}
//									addTab(sb, from);
//								}
//							}
//						} else {
//							addTab(sb, from);
//						}
//						Log.d("imclick", "msg" + sb.getMessage());
//					}
//					Log.d("imclick", "imtab notiftim finished");
//
//				}
//			});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * Add new tab to the TabActivity
//	 * 
//	 * @param sb
//	 *            - Instance of SignalingBean
//	 * @param fromMe
//	 *            - token to produce the sender or receiver ui generation
//	 */
//	public void addTab(final SignalingBean sb, final boolean fromMe) {
//		try {
//			Log.d("addtab", "add teb data");
//			Log.d("imclick", "imtab add exsist");
//			Intent intent = new Intent(context, ChatActivity.class);
//			String buddy = CallDispatcher.getUser(sb.getFrom(), sb.getTo());
//
//			Log.d("MIM", "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//
//			Log.d("MIM", "from " + sb.getFrom());
//			Log.d("MIM", "To " + sb.getTo());
//			Log.d("MIM", "buddy " + buddy);
//			Log.d("MIM", "conferencepeople " + sb.getConferencemember());
//
//			Log.d("MIM", "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//
//			TabHost.TabSpec tabSpec;
//
//			// check whether we have groupconference or one to one
//
//			if (sb.getConferencemember() == null
//					|| sb.getConferencemember().length() < 1) {
//				Log.d("see", "This is for Single Chat");
//				if (sb.getConferencemember().trim().length() == 0) {
//					Log.d("see", "This is for Single Chat if");
//					if (sb.getFrom().equals(CallDispatcher.LoginUser)) {
//						Log.d("see", "This is for Single Chat iff");
//						tabSpec = tabHost.newTabSpec(sb.getSessionid())
//								.setIndicator(sb.getTo()).setContent(intent);
//					} else {
//						Log.d("see", "This is for Single Chat elsee");
//						tabSpec = tabHost.newTabSpec(sb.getSessionid())
//								.setIndicator(sb.getFrom()).setContent(intent);
//					}
//
//				} else {
//					Log.d("see", "This is for Single Chat.......... else");
//					String temp = sb.getConferencemember().trim();
//					temp = temp.replace(CallDispatcher.LoginUser, "");
//					temp = temp.replace(",,", ",").trim();
//					if (temp.startsWith(","))
//						temp = temp.replace(",", "");
//					tabSpec = tabHost.newTabSpec(sb.getSessionid())
//							.setIndicator(temp).setContent(intent);
//				}
//
//				if (callDisp.getdbHeler(context).userChatting(buddy)) {
//
//					String sessionn = callDisp
//							.getdbHeler(context)
//							.getValueFromQuery(
//									"select componentpath from component where comment='"
//											+ buddy
//											+ "' and Reminderstatus='0' and componentType='IM'");
//					Log.d("see", "if statement");
//					if (WebServiceReferences.Imcollection.containsKey(sb
//							.getSessionid())) {
//						Log.d("see", "This is for Single Chat Imcollection");
//						ArrayList<SignalingBean> al = WebServiceReferences.Imcollection
//								.get(sb.getSessionid());
//						WebServiceReferences.Imcollection.remove(sb
//								.getSessionid());
//						WebServiceReferences.Imcollection.put(sessionn, al);
//					}
//
//					intent.putExtra("buddy", buddy);
//					intent.putExtra("msg", sb.getMessage());
//					intent.putExtra("sessionid", sessionn);
//					intent.putExtra("confmembers", sb.getConferencemember());
//					intent.putExtra("fromme", fromMe);
//					intent.putExtra("status", buddyStatus);
//					intent.putExtra("tocreate", false);
//					intent.putExtra("Signal", sb);
//					Log.d("see", "conferencepeople " + sb.getConferencemember());
//					if (!WebServiceReferences.instantMessageScreen
//							.containsKey(sessionn)) {
//
//						if (sb.getConferencemember().trim().length() == 0) {
//							if (sb.getFrom().equals(CallDispatcher.LoginUser)) {
//								tabSpec = tabHost.newTabSpec(sessionn)
//										.setIndicator(sb.getTo())
//										.setContent(intent);
//							} else {
//								tabSpec = tabHost.newTabSpec(sessionn)
//										.setIndicator(sb.getFrom())
//										.setContent(intent);
//							}
//
//						} else {
//							String temp = sb.getConferencemember().trim();
//							temp = temp.replace(CallDispatcher.LoginUser, "");
//							temp = temp.replace(",,", ",").trim();
//							if (temp.startsWith(","))
//								temp = temp.replace(",", "");
//							tabSpec = tabHost.newTabSpec(sessionn)
//									.setIndicator(temp).setContent(intent);
//						}
//
//						// this is to create view in tab to show details
//
//						Log.d("see", "tab host");
//						tabHost.setClickable(true);
//						tabHost.addTab(tabSpec);
//						tabHost.setCurrentTabByTag(sessionn);
//
//					} else {
//						Log.d("see", "tab host not created");
//						InstantMessageScreen instantMessage = (InstantMessageScreen) WebServiceReferences.instantMessageScreen
//								.get(sessionn);
//						instantMessage.updateMsg(sb.getConferencemember(),
//								sb.getMessage(), sb.getFrom(), sb);
//					}
//
//				}
//				// used new session for the buddy to chat.
//				else {
//
//					Log.d("see", "else statement");
//					intent.putExtra("buddy", buddy);
//					intent.putExtra("msg", sb.getMessage());
//					intent.putExtra("sessionid", sb.getSessionid());
//					intent.putExtra("confmembers", sb.getConferencemember());
//					intent.putExtra("fromme", fromMe);
//					intent.putExtra("tocreate", true);
//					intent.putExtra("Signal", sb);
//					intent.putExtra("status", buddyStatus);
//
//					Object object = (Object) WebServiceReferences.contextTable
//							.get("MAIN");
//
//					if (object instanceof CompleteListView) {
//						Log.d("IM", "###############complete list view");
//						CompleteListView mInstance = (CompleteListView) WebServiceReferences.contextTable
//								.get("MAIN");
//						if (fromMe
//								&& !WebServiceReferences.imViews.containsKey(sb
//										.getSessionid())) {
//							Log.d("IM", "###############complete list view");
//							callDisp.PutImEntry(sb.getSessionid(),
//									sb.getFrom(), sb.getTo(), 0,
//									mInstance.owner);
//						}
//					}
//
//					if (sb.getConferencemember().trim().length() == 0) {
//						if (!WebServiceReferences.activeSession.containsKey(sb
//								.getTo())) {
//							Log.d("hashm", "???????????????????" + sb.getFrom());
//							if (sb.getFrom().equalsIgnoreCase(
//									CallDispatcher.LoginUser)) {
//								WebServiceReferences.activeSession.put(
//										sb.getTo(), sb.getSessionid());
//							} else {
//								WebServiceReferences.activeSession.put(
//										sb.getFrom(), sb.getSessionid());
//							}
//						}
//						if (sb.getFrom().equals(CallDispatcher.LoginUser)) {
//							Log.d("IM", "############## conf iffffffffffffff");
//							tabSpec = tabHost.newTabSpec(sb.getSessionid())
//									.setIndicator(sb.getTo())
//									.setContent(intent);
//						} else {
//							Log.d("IM", "############## conf else________-");
//							tabSpec = tabHost.newTabSpec(sb.getSessionid())
//									.setIndicator(sb.getFrom())
//									.setContent(intent);
//						}
//
//					} else {
//						Log.d("IM", "############## conf else");
//						String temp = sb.getConferencemember().trim();
//						temp = temp.replace(CallDispatcher.LoginUser, "");
//						temp = temp.replace(",,", ",").trim();
//						if (temp.startsWith(","))
//							temp = temp.replace(",", "");
//						tabSpec = tabHost.newTabSpec(sb.getSessionid())
//								.setIndicator(temp).setContent(intent);
//					}
//					tabHost.setClickable(false);
//					tabHost.addTab(tabSpec);
//					Log.d("session",
//							"reloadCurrentSession................................."
//									+ sb.getSessionid());
//					tabHost.setCurrentTabByTag(sb.getSessionid());
//				}
//
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * To return names in the ordered format.(In between two names add comma(,)
//	 * )
//	 * 
//	 * @param names
//	 * @return
//	 */
//	public String getArrangedMembers(String names) {
//		try {
//			String toReturn = "";
//			String temp = "";
//			String stx[] = names.split(",");
//
//			Collections.sort(Arrays.asList(stx));
//			// String strtoReturn="";
//			for (String name : stx) {
//
//				if (!temp.equals(name)) {
//					toReturn = toReturn + name + ",";
//				}
//				temp = name;
//			}
//			return toReturn;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * Perform any final cleanup before an activity is destroyed.
//	 */
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		WebServiceReferences.instantMessageScreen.clear();
//		Log.d("see", "destroy of imtab");
//		// WebServiceReferences.Imcollection.clear();
//		WebServiceReferences.contextTable.remove("imtabs");
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		// TODO Auto-generated method stub
//
//		if (outState == null) {
//			outState = new Bundle();
//		}
//		outState.putSerializable("sb", sb);
//		outState.putBoolean("fromto", fromTo);
//		super.onSaveInstanceState(outState);
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		Log.d("lg", "......................... on pause in tabscreen....");
//
//		super.onResume();
//	}
//
//	public void notifyLogout(String Title, String Message) {
//		Log.d("lg", ".........................imtab notify logput....");
//		LocalActivityManager manager = getLocalActivityManager();
//		Log.d("lg", "......................... on pause in tabscreen....");
//		if (manager.getCurrentActivity() instanceof InstantMessageScreen) {
//			Log.d("lg", "......................... imscreen....");
//			InstantMessageScreen imscreen = (InstantMessageScreen) manager
//					.getCurrentActivity();
//			imscreen.ShowError(Title, Message);
//		}
//	}
//
//}