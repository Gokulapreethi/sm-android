package com.cg.account;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.PermissionBean;
import org.lib.model.SignalingBean;
import org.lib.model.WebServiceBean;
import org.util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.SlideMenu.SlideMenu;
import com.cg.avatar.AnsweringMachineActivity;
import com.cg.callservices.CallConnectingScreen;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.Databean;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.BuddyNoteList;
import com.cg.files.ComponentCreator;
import com.cg.hostedconf.AppReference;
import com.cg.hostedconf.ContactConference;
import com.cg.instancemessage.IMNotifier;
import com.cg.permissions.PermissionsActivity;
import com.cg.profiles.ViewProfiles;
import com.chat.ChatActivity;
import com.group.GroupActivity;
import com.group.ViewGroups;
import com.group.chat.GroupChatActivity;
import com.util.SingleInstance;

/**
 * This class is used to show the BuddyList.Using this screen we can find
 * friends list and also their Online Status.
 * 
 * 
 * 
 */
public class buddiesList extends Activity implements
		 IMNotifier, Serializable {

	private Context context = null;

	private Handler handler = null;

	private CallDispatcher calldisp = null;

	private boolean isPendingshowing = false;

	private TextView title = null;

	private Button IMRequest;

	private Button btnReceiveCall;

	private Button btn_notification = null;

	private Button btn_addcontact = null;

	private SlideMenu slidemenu = null;

	private ListView lv = null;

	private TextView tvRequest;

	private TextView SwipeRequest;

	private Button conf_contact = null;

	private Button all_contacts = null;

	private Button pending_contacts = null;

	private Button btn_cancel = null;

	private ArrayList<String> alBuddies = new ArrayList<String>();

	private LinearLayout llayListItems;

	private String selectedBuddy;

	private int selectedposition;

	private int view = 0;

	private PermissionBean permissionBean = null;

	private ProgressDialog dialog = null;

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		context = this;
//		handler = new Handler();
//		WebServiceReferences.contextTable.put("IM", context);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//		setContentView(R.layout.custom_title1);
//		isPendingshowing = false;
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.custom_title1);
//		title = (TextView) findViewById(R.id.note_date);
//		title.setText("Contacts");
//		IMRequest = (Button) findViewById(R.id.im);
//		btn_addcontact = (Button) findViewById(R.id.btn_addcontact);
//		btn_addcontact.setVisibility(View.GONE);
//		btnReceiveCall = (Button) findViewById(R.id.btncomp);
//		btnReceiveCall.setVisibility(View.INVISIBLE);
//		btn_notification = (Button) findViewById(R.id.notification);
//
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//		int noScrHeight = displaymetrics.heightPixels;
//		int noScrWidth = displaymetrics.widthPixels;
//
//		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
//			calldisp = (CallDispatcher) WebServiceReferences.callDispatch
//					.get("calldisp");
//		else
//			calldisp = new CallDispatcher(context);
//
//		calldisp.setNoScrHeight(noScrHeight);
//		calldisp.setNoScrWidth(noScrWidth);
//		displaymetrics = null;
//
//		IMRequest.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				calldisp.openReceivedIm(arg0, context);
//			}
//		});
//
//		WebServiceReferences.contextTable.put("buddiesList", this);
//		btnReceiveCall.setVisibility(View.VISIBLE);
//		btnReceiveCall.setBackgroundResource(R.drawable.icon_add_notes);
//		btnReceiveCall.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!isPendingshowing) {
//					Intent intent = new Intent(context, Searchpeople.class);
//					startActivity(intent);
//				} else {
//					Intent intent = new Intent(context, GroupActivity.class);
//					startActivity(intent);
//				}
//
//			}
//		});
//
//		btn_cancel = (Button) findViewById(R.id.settings);
//		btn_cancel.setBackgroundResource(R.drawable.icon_sidemenu);
//		btn_cancel.setVisibility(View.VISIBLE);
//		btn_cancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				slidemenu.show();
//			}
//
//		});
//
//		setContentView(R.layout.sliddingdrawer);
//		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
//		ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
//		calldisp.composeList(datas);
//		slidemenu.init(buddiesList.this, datas, buddiesList.this, 100);
//
//		lv = (ListView) findViewById(R.id.contact_listview);
//
//		if (!isPendingshowing)
////			lv.setAdapter(CallDispatcher.adapterToShow);
////		else
////			lv.setAdapter(GroupActivity.groupAdapter);
//
//		final SwipeDetector swipeDetector = new SwipeDetector();
//		lv.setOnTouchListener(swipeDetector);
//		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int pos, long id) {
//
//				selectedposition = pos;
//				String state = null;
//				if (!isPendingshowing) {
//					selectedBuddy = CallDispatcher.adapterToShow.getUser(pos);
//					final Databean db = (Databean) CallDispatcher.adapterToShow
//							.getItem(selectedposition);
//
//					state = db.getStatus();
//					if (!state.equalsIgnoreCase("Pending")) {
//						permissionBean = calldisp.getdbHeler(context)
//								.selectPermissions(
//										"select * from setpermission where userid='"
//												+ selectedBuddy
//												+ "' and buddyid='"
//												+ CallDispatcher.LoginUser
//												+ "'", selectedBuddy,
//										CallDispatcher.LoginUser);
//						if (permissionBean.getMMchat().equals("1")) {
//							doMultiMMChat(state);
//						} else
//							ShowError("MM Chat", "Access Denied", context);
//					}
//
//					else if (state.equalsIgnoreCase("Pending")) {
//						runOnUiThread(new Runnable() {
//							public void run() {
//								doDeleteContact();
//							}
//						});
//
//					}
//
//				}
//
//				return true;
//			}
//		});
//
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				try {
//					// TODO Auto-generated method stub
//					selectedposition = arg2;
//					if (!isPendingshowing) {
//						Databean bean = (Databean) CallDispatcher.adapterToShow
//								.getItem(arg2);
//						selectedBuddy = CallDispatcher.adapterToShow
//								.getUser(arg2);
//						if (bean.getStatus() != null
//								&& !bean.getStatus()
//										.equalsIgnoreCase("pending")) {
//							if (swipeDetector.swipeDetected()) {
//								permissionBean = calldisp
//										.getdbHeler(context)
//										.selectPermissions(
//												"select * from setpermission where userid='"
//														+ selectedBuddy
//														+ "' and buddyid='"
//														+ CallDispatcher.LoginUser
//														+ "'", selectedBuddy,
//												CallDispatcher.LoginUser);
//								if (swipeDetector.getAction() == Action.RL) {
//
//									if (permissionBean.getVideo_call().equals(
//											"1")) {
//										processCallRequest(2);
//
//									} else
//										calldisp.showAlert("Repsonse",
//												"Access Denied");
//
//								} else if (swipeDetector.getAction() == Action.LR) {
//
//									if (permissionBean.getAudio_call().equals(
//											"1")) {
//										processCallRequest(1);
//									} else
//										calldisp.showAlert("Repsonse",
//												"Access Denied");
//								}
//							} else
//								showdialog(bean.getStatus());
//
//						} else
//							doDeleteContact();
//					} else {
//						// GroupBean groupManagementBean =
//						// CallDispatcher.groupAdapter
//						// .getItem(arg2);
//						// showGroupDialog(groupManagementBean);
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					e.printStackTrace();
//				}
//			}
//		});
//
//		tvRequest = (TextView) findViewById(R.id.tvRequest);
//		tvRequest.setBackgroundColor(Color.parseColor("#FFFFFF"));
//		tvRequest.setTextColor(Color.WHITE);
//		SwipeRequest = (TextView) findViewById(R.id.SwipeRequest);
//		SwipeRequest.setBackgroundColor(Color.parseColor("#FFFFFF"));
//		SwipeRequest.setTextColor(Color.parseColor("#ff4d4d"));
//		SwipeRequest.setVisibility(View.VISIBLE);
//		SwipeRequest.setEllipsize(TruncateAt.MARQUEE);
//		SwipeRequest.setSelected(true);
//		SwipeRequest.setEnabled(true);
//		SwipeRequest.setHorizontallyScrolling(true);
//		conf_contact = (Button) findViewById(R.id.cont_list);
//		all_contacts = (Button) findViewById(R.id.all_contacts);
//		pending_contacts = (Button) findViewById(R.id.pending_contacts);
//		llayListItems = (LinearLayout) findViewById(R.id.laylistitems);
//
//		all_contacts.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				CallDispatcher.adapterToShow.notifyDataSetChanged();
//				lv.setAdapter(CallDispatcher.adapterToShow);
//				all_contacts
//						.setBackgroundResource(R.drawable.rounded_border_custom_profile);
//				pending_contacts
//						.setBackgroundResource(R.drawable.rounded_border_custom_profile_2);
//				all_contacts.setTextColor(Color.WHITE);
//				pending_contacts.setTextColor(Color.parseColor("#FF494D"));
//				isPendingshowing = false;
//
//			}
//		});
//
//		pending_contacts.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				all_contacts
//						.setBackgroundResource(R.drawable.rounded_border_custom_profile_2);
//				pending_contacts
//						.setBackgroundResource(R.drawable.rounded_border_custom_profile);
//				pending_contacts.setTextColor(Color.WHITE);
//				all_contacts.setTextColor(Color.parseColor("#FF494D"));
//				lv.setAdapter(GroupActivity.groupAdapter);
//				isPendingshowing = true;
//			}
//		});
//
//		conf_contact.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (!CallDispatcher.isWifiClosed) {
//					if (calldisp.getOnlineBuddysOnly().size() > 0) {
//			
//						Intent intent = new Intent(context,
//								ContactConference.class);
//						startActivity(intent);
//					} else {
//						Toast.makeText(context, "Sorry no online users", 1)
//								.show();
//					}
//				} else
//					Toast.makeText(
//							context,
//							"Please check your internet connection before make conference call",
//							1).show();
//
//			}
//		});
//
//		addRequestBuddiesList();
//		setCount();
//
//		if (alBuddies.size() > 0) {
//			tvRequest.setVisibility(View.VISIBLE);
//		} else {
//			tvRequest.setVisibility(View.GONE);
//		}
//
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		if (WebServiceReferences.Imcollection.size() == 0)
//			IMRequest.setVisibility(View.GONE);
//		else
//			IMRequest.setVisibility(View.VISIBLE);
//		super.onResume();
//	}

//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		WebServiceReferences.contextTable.remove("buddiesList");
//		super.onDestroy();
//	}

//	@Override
//	public void onSlideMenuItemClick(int itemId, View v, Context context) {
//		// TODO Auto-generated method stub
//		switch (itemId) {
//		case WebServiceReferences.CONTACTS:
//
//			break;
//		case WebServiceReferences.USERPROFILE:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			if (CallDispatcher.LoginUser != null)
//				finish();
//
//			break;
//		case WebServiceReferences.UTILITY:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			if (CallDispatcher.LoginUser != null)
//				finish();
//			break;
//		case WebServiceReferences.NOTES:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			finish();
//			break;
//		case WebServiceReferences.APPS:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			finish();
//			break;
//		case WebServiceReferences.CLONE:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			if (CallDispatcher.LoginUser != null)
//				finish();
//
//			break;
//		case WebServiceReferences.SETTINGS:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			finish();
//			break;
//
//		case WebServiceReferences.QUICK_ACTION:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			if (CallDispatcher.LoginUser != null)
//				finish();
//			break;
//		case WebServiceReferences.FORMS:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			finish();
//			break;
//		case WebServiceReferences.FEEDBACK:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			finish();
//			break;
//		case WebServiceReferences.EXCHANGES:
//			calldisp.onSlideMenuItemClick(itemId, v, context);
//			finish();
//			break;
//
//		default:
//			break;
//		}
//	}

//	private void addRequestBuddiesList() {
//		try {
//			try {
//				if (CallDispatcher.showBuddies != null) {
//					if (CallDispatcher.showBuddies.size() >= 1) {
//						for (int i = 0; i < CallDispatcher.showBuddies.size(); i++) {
//							String text = (String) CallDispatcher.showBuddies
//									.get(i);
//							if (!alBuddies.contains(text)
//									&& !CallDispatcher.adapterToShow
//											.ContainsUser(text)) {
//
//								Log.e("buddy", "Comes to add to UI :" + text);
//								final RelativeLayout linearrow = returnRow();
//
//								TextView userName = (TextView) linearrow
//										.findViewById(R.id.txtView01);
//								alBuddies.add(text);
//								userName.setTextColor(Color.BLACK);
//								userName.setText(text);
//								Button accept = (Button) linearrow
//										.findViewById(R.id.btnAccept);
//								accept.setTag(text);
//								final Button viewProfile = (Button) linearrow
//										.findViewById(R.id.btnViewProfile);
//								viewProfile.setTag(text);
//								accept.setOnClickListener(new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										if (CallDispatcher.isConnected) {
//											String name = (String) v.getTag();
//
//											if (WebServiceReferences.reqbuddyList
//													.containsKey(name))
//												WebServiceReferences.reqbuddyList
//														.remove(name);
//
//											Toast.makeText(context,
//													"Accept " + name,
//													Toast.LENGTH_SHORT).show();
//
//											if (!WebServiceReferences.running) {
//												calldisp.startWebService(
//														getResources()
//																.getString(
//																		R.string.service_url),
//														"80");
//											}
//
////											WebServiceReferences.webServiceClient
////													.acceptRejectPeople(
////															CallDispatcher.LoginUser,
////															name, "1");
//
//											llayListItems.removeView(linearrow);
//											CallDispatcher.showBuddies
//													.remove(name);
//											alBuddies.remove(name);
//											setCount();
//											viewProfile
//													.setVisibility(View.GONE);
//										} else
//											Toast.makeText(
//													context,
//													"Kindly check your network ",
//													Toast.LENGTH_SHORT).show();
//									}
//								});
//
//								Button reject = (Button) linearrow
//										.findViewById(R.id.btnReject);
//								reject.setTag(text);
//								reject.setOnClickListener(new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										if (CallDispatcher.isConnected) {
//											String name = (String) v.getTag();
//
//											Toast.makeText(context,
//													"Reject " + name,
//													Toast.LENGTH_SHORT).show();
//
//											if (WebServiceReferences.reqbuddyList
//													.containsKey(name))
//												WebServiceReferences.reqbuddyList
//														.remove(name);
//
//											calldisp.getdbHeler(context)
//													.deleteProfile(name);
//
//											if (!WebServiceReferences.running) {
//												calldisp.startWebService(
//														getResources()
//																.getString(
//																		R.string.service_url),
//														"80");
//											}
//
////											WebServiceReferences.webServiceClient
////													.acceptRejectPeople(
////															CallDispatcher.LoginUser,
////															name, "0");
//
//											llayListItems.removeView(linearrow);
//											alBuddies.remove(name);
//											setCount();
//										} else
//											Toast.makeText(
//													context,
//													"Kindly check your network ",
//													Toast.LENGTH_SHORT).show();
//
//									}
//								});
//
//								viewProfile
//										.setOnClickListener(new OnClickListener() {
//
//											@Override
//											public void onClick(View v) {
//												// TODO Auto-generated method
//												if (v.getTag() != null) {
//													doViewProfile(
//															false,
//															(String) v.getTag(),
//															"buddyrequest");
//													view = 1;
//												}
//											}
//										});
//
//								llayListItems.addView(linearrow);
//								Log.e("buddy", "Added to UI");
//							}
//						}
//						llayListItems.setVisibility(View.VISIBLE);
//					}
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

//	private RelativeLayout returnRow() {
//		try {
//			LayoutInflater inflateLayout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			RelativeLayout uniqueUser = (RelativeLayout) inflateLayout.inflate(
//					R.layout.buddy_request_row, null);
//			return uniqueUser;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK))
			calldisp.showKeyDownAlert(context);

		return super.onKeyDown(keyCode, event);
	}

//	public void updateRequestedBuddy() {
//		try {
//			handler.post(new Runnable() {
//
//				@Override
//				public void run() {
//					addRequestBuddiesList();
//					setCount();
//
//				}
//			});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void setCount() {
		try {
			if (alBuddies.size() > 0) {
				tvRequest.setText("Requests :" + alBuddies.size());
				tvRequest.setVisibility(View.VISIBLE);
			} else {
				tvRequest.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyBuddyrequestDeleted(final String buddy_name) {
		try {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					llayListItems.removeAllViews();
//					CallDispatcher.showBuddies.remove(buddy_name);
					alBuddies.clear();
//					addRequestBuddiesList();
					setCount();

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyBuddyDeleted(final String server_msg) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				calldisp.cancelDialog();
				if (server_msg != null) {
					if (server_msg
							.equalsIgnoreCase("Contact deleted successfully")) {
//						if (WebServiceReferences.buddyList
//								.containsKey(selectedBuddy)) {
//							WebServiceReferences.buddyList
//									.remove(selectedBuddy);
//						}
						calldisp.getdbHeler(context).deleteProfile(
								selectedBuddy);
//						if (!isPendingshowing) {
//							CallDispatcher.adapterToShow
//									.removeItem(selectedposition);
//							for (int i = 0; i < calldisp.pendinglist.size(); i++) {
//								if (((Databean) calldisp.pendinglist.get(i))
//										.getname().equals(selectedBuddy)) {
//									break;
//								}
//							}
//						} else {
//							for (int i = 0; i < calldisp.mainbuddylist.size(); i++) {
//								if (((Databean) calldisp.mainbuddylist.get(i))
//										.getname().equals(selectedBuddy)) {
//									CallDispatcher.adapterToShow.removeItem(i);
//									break;
//								}
//							}
//						}
//						CallDispatcher.adapterToShow.notifyDataSetChanged();

					}
					Toast.makeText(context, server_msg, Toast.LENGTH_SHORT)
							.show();

				}
			}
		});
	}

	public void showdialog(final String status) {
		try {
			// TODO Auto-generated method stub

			final Dialog dialog = new Dialog(buddiesList.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			dialog.setContentView(R.layout.buddycontact);
			dialog.setTitle("Select any service you want to make");
			Bitmap bitmap = null;
			permissionBean = calldisp.getdbHeler(context).selectPermissions(
					"select * from setpermission where userid='"
							+ selectedBuddy + "' and buddyid='"
							+ CallDispatcher.LoginUser + "'", selectedBuddy,
					CallDispatcher.LoginUser);
			LinearLayout layout_query = (LinearLayout) dialog
					.findViewById(R.id.remove);
			RelativeLayout buddyname_layout = (RelativeLayout) dialog
					.findViewById(R.id.name_lay);
			LinearLayout profilelayout = (LinearLayout) dialog
					.findViewById(R.id.prof_lay);
			LinearLayout profi_pic = (LinearLayout) dialog
					.findViewById(R.id.profi_pic);
			RelativeLayout buddyLay = (RelativeLayout) dialog
					.findViewById(R.id.buddy_lay);
			LinearLayout answerlay = (LinearLayout) dialog
					.findViewById(R.id.answerlay);
			ImageView profilepicture = (ImageView) profi_pic
					.findViewById(R.id.pictures);
			String profilePic = calldisp.getdbHeler(context).getProfilePic(
					selectedBuddy);

			try {
				if (profilePic != null && profilePic.length() > 0) {
					Bitmap profle_bitmap = calldisp.setProfilePicture(
							profilePic, R.drawable.icon_buddy_aoffline);
					profilepicture.setImageBitmap(profle_bitmap);

				}
			} catch (Exception e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
			TextView buddyname = (TextView) buddyname_layout
					.findViewById(R.id.buddyname);
			buddyname.setText(selectedBuddy);
			TextView buddyStatus = (TextView) buddyname_layout
					.findViewById(R.id.status);
			buddyStatus.setText(status);
			buddyStatus.setTextColor(Color.BLUE);
			Button closedialog = (Button) buddyLay
					.findViewById(R.id.close_dialog);
			LinearLayout MMLay = (LinearLayout) dialog.findViewById(R.id.MMlay);
			MMLay.setTag(buddyStatus.getText().toString());
			LinearLayout audiocalllay = (LinearLayout) dialog
					.findViewById(R.id.audio_calllay);
			LinearLayout videocallay = (LinearLayout) dialog
					.findViewById(R.id.videocalllay);
			LinearLayout audioBroadLay = (LinearLayout) dialog
					.findViewById(R.id.audiobroadcast);
			LinearLayout videoBroadLay = (LinearLayout) dialog
					.findViewById(R.id.videobroadcast);
			LinearLayout audioUnicastLay = (LinearLayout) dialog
					.findViewById(R.id.audiounicast);
			LinearLayout videounicastLay = (LinearLayout) dialog
					.findViewById(R.id.videounicast);
			LinearLayout confLay = (LinearLayout) dialog
					.findViewById(R.id.conflay);
			LinearLayout inboxlay = (LinearLayout) dialog
					.findViewById(R.id.inboxlay);
			LinearLayout textmsglay = (LinearLayout) dialog
					.findViewById(R.id.textmsglay);
			LinearLayout photomsglay = (LinearLayout) dialog
					.findViewById(R.id.photomsglay);
			LinearLayout audiomsglay = (LinearLayout) dialog
					.findViewById(R.id.audiomsglay);
			LinearLayout videomsglay = (LinearLayout) dialog
					.findViewById(R.id.videomsglay);
			LinearLayout handsketchlay = (LinearLayout) dialog
					.findViewById(R.id.handsketchmsglay);
			LinearLayout withdrawlay = (LinearLayout) dialog
					.findViewById(R.id.withdrawlay);
			LinearLayout accesslay = (LinearLayout) dialog
					.findViewById(R.id.acceslay);
			LinearLayout DeletCon = (LinearLayout) dialog
					.findViewById(R.id.deletecontactlay);
			LinearLayout ViewProf = (LinearLayout) dialog
					.findViewById(R.id.viewproflay);
			ViewProf.setTag(selectedBuddy);
			LinearLayout all_files = (LinearLayout) dialog
					.findViewById(R.id.allfiles);

			MMLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getMMchat().equals("1")) {
							doMultiMMChat(v.getTag().toString());
							dialog.cancel();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			closedialog.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (dialog != null)
							dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			DeletCon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub

						doDeleteContact();
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			ViewProf.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						doViewProfile(true, v.getTag().toString(), status);
						view = 1;
						CallDispatcher.profileRequested = true;
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			inboxlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						Intent intentComponent = new Intent(context,
								BuddyNoteList.class);
						Bundle bndl = new Bundle();
						bndl.putString("buddyname", selectedBuddy);
						bndl.putInt("view_mode", 0);
						intentComponent.putExtras(bndl);
						startActivity(intentComponent);

						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});

			textmsglay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getTextMessage().equals("1")) {
							Intent intentComponent = new Intent(context,
									ComponentCreator.class);
							Bundle bndl = new Bundle();
							bndl.putString("type", "note");
							bndl.putBoolean("action", true);
							bndl.putBoolean("forms", false);
							bndl.putString("buddyname", selectedBuddy);
							bndl.putBoolean("send", true);
							intentComponent.putExtras(bndl);
							startActivity(intentComponent);
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});
			photomsglay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getPhotoMessage().equals("1")) {
							Intent intentComponent = new Intent(context,
									ComponentCreator.class);
							Bundle bndl = new Bundle();
							bndl.putString("type", "photo");
							bndl.putBoolean("action", true);
							bndl.putBoolean("forms", false);
							bndl.putBoolean("send", true);
							bndl.putString("buddyname", selectedBuddy);
							intentComponent.putExtras(bndl);
							startActivity(intentComponent);
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});
			audiomsglay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getAudioMessage().equals("1")) {
							Intent intentComponent = new Intent(context,
									ComponentCreator.class);
							Bundle bndl = new Bundle();
							bndl.putString("type", "audio");
							bndl.putBoolean("action", true);
							bndl.putBoolean("forms", false);
							bndl.putBoolean("send", true);
							bndl.putString("buddyname", selectedBuddy);
							intentComponent.putExtras(bndl);
							startActivity(intentComponent);
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});
			videomsglay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getVideoMessage().equals("1")) {
							Intent intentComponent = new Intent(context,
									ComponentCreator.class);
							Bundle bndl = new Bundle();
							bndl.putString("type", "video");
							bndl.putBoolean("action", true);
							bndl.putBoolean("forms", false);
							bndl.putString("buddyname", selectedBuddy);
							bndl.putBoolean("send", true);
							intentComponent.putExtras(bndl);
							startActivity(intentComponent);
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});

			withdrawlay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (!CallDispatcher.isWifiClosed) {
							AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(
									context);
							deleteConfirmation.setTitle("WithDrawn");
							deleteConfirmation
									.setMessage(SingleInstance.mainContext.getResources().getString(R.string.are_you_sure_you_want_to_withdraw_all_files_shared_earlier_with)
											+ selectedBuddy + "?");
							deleteConfirmation.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											try {
												CallDispatcher.pdialog = new ProgressDialog(
														context);
												if (WebServiceReferences.running) {
													calldisp.showprogress(
															CallDispatcher.pdialog,
															context);
													WebServiceReferences.webServiceClient
															.DeleteAllShares(
																	CallDispatcher.LoginUser,
																	selectedBuddy);
												}
												dialog.dismiss();

											} catch (Exception e) {
												Log.e("Exception",
														"===>" + e.getMessage());
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
												e.printStackTrace();
											}
										}

									});
							deleteConfirmation.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											try {
												dialog.dismiss();

											} catch (Exception e) {
												Log.e("Exception",
														"===>" + e.getMessage());
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
												e.printStackTrace();
											}
										}

									});
							deleteConfirmation.show();
						} else
							Toast.makeText(
									context,
									"Please check your Internet connection before withdraw",
									Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

			handsketchlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getSketchMessage().equals("1")) {
							Intent intentComponent = new Intent(context,
									ComponentCreator.class);
							Bundle bndl = new Bundle();
							bndl.putString("type", "handsketch");
							bndl.putBoolean("action", true);
							bndl.putBoolean("forms", false);
							bndl.putString("buddyname", selectedBuddy);
							bndl.putBoolean("send", true);
							intentComponent.putExtras(bndl);
							startActivity(intentComponent);
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});

			all_files.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						// TODO Auto-generated method stub
						Intent intentComponent = new Intent(context,
								BuddyNoteList.class);
						Bundle bndl = new Bundle();
						bndl.putInt("view_mode", 1);
						intentComponent.putExtras(bndl);
						startActivity(intentComponent);
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			audiocalllay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getAudio_call().equals("1")) {
							if (!CallDispatcher.isWifiClosed)
								processCallRequest(1);
							else
								Toast.makeText(
										context,
										"Please check your Internet connection before make call",
										Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});
			videocallay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (permissionBean.getVideo_call().equals("1")) {
							if (!CallDispatcher.isWifiClosed)
								processCallRequest(2);
							else
								Toast.makeText(
										context,
										"Please check your Internet connection before make call",
										Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});
			audioBroadLay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getABC().equals("1")) {
							if (!CallDispatcher.isWifiClosed)
								processCallRequest(5);
							else
								Toast.makeText(
										context,
										"Please check your Internet connection before make call",
										Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			videoBroadLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getVBC().equals("1")) {
							if (!CallDispatcher.isWifiClosed)
								processCallRequest(6);
							else
								Toast.makeText(
										context,
										"Please check your Internet connection before make call",
										Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});

			audioUnicastLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getAUC().equals("1")) {
							if (!CallDispatcher.isWifiClosed)
								processCallRequest(3);
							else
								Toast.makeText(
										context,
										"Please check your Internet connection before make call",
										Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});

			videounicastLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getVUC().equals("1")) {
							if (!CallDispatcher.isWifiClosed)
								processCallRequest(4);
							else
								Toast.makeText(
										context,
										"Please check your Internet connection before make call",
										Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			answerlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						// TODO Auto-generated method stub

						CallDispatcher.pdialog = new ProgressDialog(context);
						calldisp.showprogress(CallDispatcher.pdialog, context);
						String[] res_info = new String[3];
						res_info[0] = CallDispatcher.LoginUser;
						res_info[1] = selectedBuddy;
//						BuddyInformationBean bib = WebServiceReferences.buddyList
//								.get(selectedBuddy.trim());
						BuddyInformationBean bib=null;
						if (bib != null) {
							if (bib.getStatus().equals("Offline")
									|| bib.getStatus().equals("Stealth"))
								res_info[2] = calldisp
										.getdbHeler(context)
										.getwheninfo(
												"select cid from clonemaster where cdescription='Offline'");
							else
								res_info[2] = "";
						} else
							res_info[2] = "";
						WebServiceReferences.webServiceClient
								.OfflineCallResponse(res_info);
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			accesslay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						Intent permission_intent = new Intent(buddiesList.this,
								PermissionsActivity.class);
						permission_intent.putExtra("buddy", selectedBuddy);
						startActivity(permission_intent);
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			confLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (permissionBean.getVBC().equals("1")) {
							if (!CallDispatcher.isWifiClosed) {
								if (calldisp.getOnlineBuddysOnly().size() > 0) {
									
									Intent intent = new Intent(context,
											ContactConference.class);
									startActivity(intent);
								} else {
									Toast.makeText(context,
											"Sorry no online users", 1).show();
								}
							} else
								Toast.makeText(
										context,
										"Please check your internet connection before make conference call",
										1).show();

						} else
							calldisp.showAlert("Response", "Access Denied");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("menu dialog", "Exception :: " + e.getMessage());
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	void doViewProfile(boolean accept, String buddy, String status) {

		try {
			if (!accept && buddy != null) {
				selectedBuddy = buddy;
			}
			ArrayList<String> profileList = calldisp.getdbHeler(context)
					.getProfile(selectedBuddy);
			if (profileList.size() > 0) {
				Intent intent = new Intent(context, ViewProfiles.class);
				intent.putExtra("buddyname", selectedBuddy);
				intent.putExtra("buddystatus", status);
				startActivity(intent);

			} else {
				if (CallDispatcher.isConnected) {
					ProgressDialog dialog = new ProgressDialog(context);
					calldisp.showprogress(dialog, context);
					CallDispatcher.isFromCallDisp = false;
					String modifiedDate = calldisp.getdbHeler(context)
							.getModifiedDate(
									"select max(modifieddate) from profilefieldvalues where userid='"
											+ buddy + "'");
					if (modifiedDate == null) {
						modifiedDate = "";
					} else if (modifiedDate.trim().length() == 0) {
						modifiedDate = "";
					}
					String[] profileDetails = new String[3];
					profileDetails[0] = selectedBuddy;
					profileDetails[1] = "5";
					profileDetails[2] = modifiedDate;
					WebServiceReferences.webServiceClient
							.getStandardProfilefieldvalues(profileDetails);
				} else
					Toast.makeText(context, "Kindly check your network ",
							Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void doDeleteContact() {

		if (CallDispatcher.isConnected) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					buddiesList.this);
			builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.warning));
			builder.setMessage(
					"Are you sure you want to delete " + selectedBuddy + " ?")
					.setCancelable(false)
					.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (!WebServiceReferences.running) {
										calldisp.startWebService(
												getResources().getString(
														R.string.service_url),
												"80");
									}
									ProgressDialog progressDialog = new ProgressDialog(
											context);
									calldisp.showprogress(progressDialog,
											context);
//									WebServiceReferences.webServiceClient
//											.deletePeople(
//													CallDispatcher.LoginUser,
//													selectedBuddy);
								}
							})
					.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert1 = builder.create();
			alert1.show();
		} else {
			ShowError("Network Error",
					"No Network Connection,Can not Delete Buddy", context);
		}

	}

	public void ShowError(String Title, String Message, Context con) {
		try {
			AlertDialog confirmation = new AlertDialog.Builder(con).create();
			confirmation.setTitle(Title);
			confirmation.setMessage(Message);
			confirmation.setCancelable(true);
			confirmation.setButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});

			confirmation.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyViewProfile(boolean isrequested) {
		ArrayList<String> profileList = calldisp.getdbHeler(context)
				.getProfile(selectedBuddy);
		Log.i("profile", "size of arrayList--->" + profileList.size());

		if (profileList.size() > 0 && view == 1) {
			if (!WebServiceReferences.contextTable.containsKey("viewprofile")) {
				Intent intent = new Intent(context, ViewProfiles.class);
				intent.putExtra("buddyname", selectedBuddy);
//				if (WebServiceReferences.buddyList.containsKey(selectedBuddy)) {
//					BuddyInformationBean bean = WebServiceReferences.buddyList
//							.get(selectedBuddy);
//					intent.putExtra("buddystatus", bean.getStatus());
//				}
				view = 0;
				startActivity(intent);
			} else {
				((ViewProfiles) WebServiceReferences.contextTable
						.get("viewprofile")).initView();
			}
		} else {
			if (CallDispatcher.profileRequested) {
				Toast.makeText(context, "No profile assigned for this user",
						Toast.LENGTH_SHORT).show();
				CallDispatcher.profileRequested = false;
			}
		}
	}

	public void processCallRequest(int caseid) {
//		final Databean db = (Databean) CallDispatcher.adapterToShow
//				.getItem(selectedposition);

//		String state = db.getStatus();
		String state = null ;
//		selectedBuddy = CallDispatcher.adapterToShow.getUser(selectedposition);
		Log.d("LM", "call status--->" + state);

		if (selectedBuddy != null && state.equalsIgnoreCase("Offline")
				|| state.equals("Stealth") || state.equalsIgnoreCase("pending")
				|| state.equalsIgnoreCase("Virtual")
				|| state.equalsIgnoreCase("airport")) {
			if (WebServiceReferences.running) {
				CallDispatcher.pdialog = new ProgressDialog(context);
				calldisp.showprogress(CallDispatcher.pdialog, context);

				String[] res_info = new String[3];
				res_info[0] = CallDispatcher.LoginUser;
				res_info[1] = selectedBuddy;
				if (state.equals("Offline") || state.equals("Stealth"))
					res_info[2] = calldisp
							.getdbHeler(context)
							.getwheninfo(
									"select cid from clonemaster where cdescription='Offline'");
				else
					res_info[2] = "";

				WebServiceReferences.webServiceClient
						.OfflineCallResponse(res_info);
			}

		} else {
			if (!state.equalsIgnoreCase("pending")) {
				calldisp.MakeCall(caseid, selectedBuddy, context);
			}

		}
	}

	public void ShowConnectionScreen(SignalingBean sbean, String username) {
		try {
			Intent intent = new Intent(this, CallConnectingScreen.class);
			Bundle bundle = new Bundle();
			bundle.putString("name", username);
			bundle.putString("type", sbean.getCallType());
			bundle.putBoolean("status", false);
			bundle.putSerializable("bean", sbean);
			intent.putExtras(bundle);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyOfflineCallResponse(final Object obj) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				calldisp.cancelDialog();
				if (obj instanceof ArrayList) {
					ArrayList<Object> callresponse_list = (ArrayList<Object>) obj;
					if (callresponse_list.size() == 3) {
						String buddy_id = null;
						if (callresponse_list.get(1) instanceof String)
							buddy_id = (String) callresponse_list.get(1);
						ArrayList<OfflineRequestConfigBean> config_list = null;
						if (callresponse_list.get(2) instanceof ArrayList) {
							config_list = (ArrayList<OfflineRequestConfigBean>) callresponse_list
									.get(2);
						}
						if (config_list != null) {
							for (OfflineRequestConfigBean offlineRequestConfigBean : config_list) {

								String received_time = calldisp
										.getCurrentDateTime();

								ContentValues cv = new ContentValues();
								cv.put("config_id",
										offlineRequestConfigBean.getConfig_id());
								cv.put("fromuser", selectedBuddy);
								cv.put("messagetitle", offlineRequestConfigBean
										.getMessageTitle());
								cv.put("messagetype", offlineRequestConfigBean
										.getMessagetype());
								cv.put("responsetype", offlineRequestConfigBean
										.getResponseType());
								cv.put("response", "''");
								cv.put("url", offlineRequestConfigBean.getUrl());
								cv.put("receivedtime", received_time);
								cv.put("sendstatus", "0");
								cv.put("username", CallDispatcher.LoginUser);

								cv.put("message",
										offlineRequestConfigBean.getMessage());
								cv.put("status", 0);

								int id = calldisp.getdbHeler(context)
										.insertOfflinePendingClones(cv);
								offlineRequestConfigBean.setId(Integer
										.toString(id));
								offlineRequestConfigBean
										.setReceivedTime(received_time);
								offlineRequestConfigBean.setStatus(0);

								if (offlineRequestConfigBean.getMessage() != null) {
									String message_path = Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/"
											+ offlineRequestConfigBean
													.getMessage();
									File message_file = new File(message_path);
									if (!message_file.exists()) {
										offlineRequestConfigBean.setStatus(1);
										calldisp.downloadOfflineresponse(
												offlineRequestConfigBean
														.getMessage(),
												offlineRequestConfigBean
														.getConfig_id(),
												"answering machine", null);
									}

									message_file = null;
									message_path = null;
								}

							}
							if (!SingleInstance.instanceTable
									.containsKey("callscreen")) {
								Intent intent = new Intent(context,
										AnsweringMachineActivity.class);
								intent.putExtra("buddy", buddy_id);
								intent.putExtra("avatarlist", config_list);
								startActivity(intent);
							}
						}

					}

				} else if (obj instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) obj;
					if (WebServiceReferences.contextTable.containsKey("imtabs")) {
						Context cntxt;
						if (WebServiceReferences.contextTable
								.containsKey("notepicker")) {
							if (WebServiceReferences.contextTable
									.containsKey("zoomactivity"))
								cntxt = WebServiceReferences.contextTable
										.get("zoomactivity");
							else if (WebServiceReferences.contextTable
									.containsKey("videoplayer"))
								cntxt = WebServiceReferences.contextTable
										.get("videoplayer");
							else if (WebServiceReferences.contextTable
									.containsKey("Component")) {
								if (WebServiceReferences.contextTable
										.containsKey("handsketch")) {
									if (WebServiceReferences.contextTable
											.containsKey("zoomactivity"))
										cntxt = WebServiceReferences.contextTable
												.get("zoomactivity");
									else
										cntxt = WebServiceReferences.contextTable
												.get("handsketch");
								} else {
									if (WebServiceReferences.contextTable
											.containsKey("zoomactivity"))
										cntxt = WebServiceReferences.contextTable
												.get("zoomactivity");
									else if (WebServiceReferences.contextTable
											.containsKey("videoplayer"))
										cntxt = WebServiceReferences.contextTable
												.get("videoplayer");
									else if (WebServiceReferences.contextTable
											.containsKey("sendershare")) {
										if (WebServiceReferences.contextTable
												.containsKey("sharebudies"))
											cntxt = WebServiceReferences.contextTable
													.get("sharebudies");
										else
											cntxt = WebServiceReferences.contextTable
													.get("sendershare");
									} else
										cntxt = WebServiceReferences.contextTable
												.get("Component");
								}
							} else {
								if (WebServiceReferences.contextTable
										.containsKey("pickerviewer"))
									cntxt = WebServiceReferences.contextTable
											.get("pickerviewer");
								else
									cntxt = WebServiceReferences.contextTable
											.get("notepicker");
							}
						} else {
							if (WebServiceReferences.contextTable
									.containsKey("handsketch")) {
								if (WebServiceReferences.contextTable
										.containsKey("zoomactivity"))
									cntxt = WebServiceReferences.contextTable
											.get("zoomactivity");
								else
									cntxt = WebServiceReferences.contextTable
											.get("handsketch");
							} else if (WebServiceReferences.contextTable
									.containsKey("zoomactivity"))
								cntxt = WebServiceReferences.contextTable
										.get("zoomactivity");
							else if (WebServiceReferences.contextTable
									.containsKey("videoplayer"))
								cntxt = WebServiceReferences.contextTable
										.get("videoplayer");
							else
								cntxt = WebServiceReferences.contextTable
										.get("imtabs");
						}
						ShowError("Warning !", service_bean.getText(), cntxt);
					} else
						ShowError("Warning !", service_bean.getText(), context);

					calldisp.cancelDialog();
				}
			}
		});
	}

	void doMultiMMChat(String buddyStatus) {

		if (!CallDispatcher.isWifiClosed) {
//			Databean dbean = (Databean) CallDispatcher.adapterToShow
//					.getItem(selectedposition);
			Databean dbean =null;
			String state = dbean.getStatus();
			Utility utility = new Utility();
			WebServiceReferences.SelectedBuddy = selectedBuddy;
			SignalingBean bean = new SignalingBean();
			bean.setSessionid(utility.getSessionID());
			bean.setFrom(CallDispatcher.LoginUser);
			bean.setTo(selectedBuddy);
			bean.setConferencemember("");
			bean.setMessage("");
			bean.setCallType("MSG");

			Intent intent = new Intent(context, ChatActivity.class);
			intent.putExtra("buddy", selectedBuddy);
			intent.putExtra("status", state);
			intent.putExtra("sessionid", utility.getSessionID());

			// intent.putExtra("sb", bean);
			// intent.putExtra("fromto", true);
			// intent.putExtra("status", state);
			context.startActivity(intent);

		} else {
			ShowError("Network Error", "No Network Connection", context);
		}

	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				IMRequest.setVisibility(View.VISIBLE);
				IMRequest.setEnabled(true);

				IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!calldisp.getdbHeler(context).userChatting(sb.getFrom())) {
					calldisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});

	}

	public void notifyDeleteallshareResponse(final Object obj) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				calldisp.cancelDialog();
				if (obj instanceof String[]) {
					calldisp.getdbHeler(context).deleteAllshares(
							"DELETE from formsettings WHERE buddyid=" + "\""
									+ selectedBuddy.trim() + "\"");
					calldisp.getdbHeler(context).deleteAllshares(
							"DELETE from buddyprofile WHERE buddy=" + "\""
									+ selectedBuddy.trim() + "\"");
					calldisp.getdbHeler(context).deleteAllshares(
							"DELETE from offlinecallsettingdetails WHERE buddyid="
									+ "\"" + selectedBuddy.trim() + "\"");
					calldisp.getdbHeler(context).deleteAllshares(
							"DELETE from userprofile WHERE userid=" + "\""
									+ selectedBuddy.trim() + "\"");
					Toast.makeText(context, "Deleted Succesfully",
							Toast.LENGTH_SHORT).show();
				} else if (obj instanceof WebServiceBean) {
					calldisp.showAlert("Response",
							((WebServiceBean) obj).getText());

				}
			}
		});
	}

	public void showGroupDialog(GroupBean groupBean) {

		try {
			// TODO Auto-generated method stub

			final Dialog dialog = new Dialog(buddiesList.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			dialog.setContentView(R.layout.groupdialog);
			dialog.setTitle("Select any service you want to make");
			Bitmap bitmap = null;
			LinearLayout layout_query = (LinearLayout) dialog
					.findViewById(R.id.remove);
			RelativeLayout buddyname_layout = (RelativeLayout) dialog
					.findViewById(R.id.name_lay);
			LinearLayout profi_pic = (LinearLayout) dialog
					.findViewById(R.id.profi_pic);
			RelativeLayout buddyLay = (RelativeLayout) dialog
					.findViewById(R.id.buddy_lay);
			ImageView profilepicture = (ImageView) profi_pic
					.findViewById(R.id.pictures);
			final GroupBean gBean = calldisp.getdbHeler(context)
					.getGroupAndMembers(
							"select * from groupdetails where groupid="
									+ groupBean.getGroupId());

			TextView groupName = (TextView) buddyname_layout
					.findViewById(R.id.groupname);
			groupName.setText(groupBean.getGroupName());
			Button closedialog = (Button) buddyLay
					.findViewById(R.id.close_dialog);
			LinearLayout editGroupLay = (LinearLayout) dialog
					.findViewById(R.id.editgrouplay);
			editGroupLay.setTag(groupBean);
			TextView editGroupText = (TextView) editGroupLay
					.findViewById(R.id.tx_editgroup);
			if (!groupBean.getOwnerName().equalsIgnoreCase(
					CallDispatcher.LoginUser)) {
				editGroupText.setText("View Group");
			}
			LinearLayout audioBroadLay = (LinearLayout) dialog
					.findViewById(R.id.audio_broadcast_lay);
			LinearLayout videoBroadLay = (LinearLayout) dialog
					.findViewById(R.id.video_broadcast_lay);
			LinearLayout confLay = (LinearLayout) dialog
					.findViewById(R.id.conflay);
			LinearLayout textmsglay = (LinearLayout) dialog
					.findViewById(R.id.txtmsglay);
			LinearLayout photomsglay = (LinearLayout) dialog
					.findViewById(R.id.photomsglay);
			LinearLayout audiomsglay = (LinearLayout) dialog
					.findViewById(R.id.audiomsglay);
			LinearLayout videomsglay = (LinearLayout) dialog
					.findViewById(R.id.videomsglay);
			LinearLayout handsketchlay = (LinearLayout) dialog
					.findViewById(R.id.handsketchlay);
			LinearLayout deleteGroup = (LinearLayout) dialog
					.findViewById(R.id.deletegrouplay);
			deleteGroup.setTag(groupBean);
			LinearLayout groupChatLay = (LinearLayout) dialog
					.findViewById(R.id.groupchat_lay);
			groupChatLay.setTag(groupBean);
			Log.i("group123", "group owner : " + groupBean.getOwnerName());

			TextView deleteGroupText = (TextView) deleteGroup
					.findViewById(R.id.delete_grp_txt);
			if (!groupBean.getOwnerName().equalsIgnoreCase(
					CallDispatcher.LoginUser)) {
				deleteGroupText.setText("Leave Group");
			}
			closedialog.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (dialog != null)
							dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			editGroupLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GroupBean groupBean = (GroupBean) v.getTag();
					if (groupBean.getOwnerName().equalsIgnoreCase(
							CallDispatcher.LoginUser)) {
						Intent intent = new Intent(context, GroupActivity.class);
						intent.putExtra("isEdit", true);
						intent.putExtra("id", groupBean.getGroupId());
						startActivity(intent);
					} else {
						Intent intent = new Intent(context, ViewGroups.class);
						intent.putExtra("id", groupBean.getGroupId());
						startActivity(intent);
					}
					dialog.dismiss();
				}
			});

			deleteGroup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						GroupBean groupBean = (GroupBean) v.getTag();
						if (groupBean.getOwnerName().equalsIgnoreCase(
								CallDispatcher.LoginUser))
							deleteGroup(groupBean,
									"Are you sure you want to delete ");
						else {
							deleteGroup(groupBean,
									"Are you sure you want to leave ");
						}
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			textmsglay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						shareFiles(
								gBean.getOwnerName() + ","
										+ gBean.getActiveGroupMembers(), "note");
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});
			photomsglay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						shareFiles(
								gBean.getOwnerName() + ","
										+ gBean.getActiveGroupMembers(),
								"photo");
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});
			audiomsglay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						shareFiles(
								gBean.getOwnerName() + ","
										+ gBean.getActiveGroupMembers(),
								"audio");
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});
			videomsglay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						shareFiles(
								gBean.getOwnerName() + ","
										+ gBean.getActiveGroupMembers(),
								"video");
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			handsketchlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						shareFiles(
								gBean.getOwnerName() + ","
										+ gBean.getActiveGroupMembers(),
								"handsketch");
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});

			audioBroadLay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (!CallDispatcher.isWifiClosed) {

							String members = getMembers(gBean.getOwnerName()
									+ "," + gBean.getActiveGroupMembers());
							Log.i("group123", "members " + members);
							if (members != null && members.length() > 0) {
								calldisp.requestAudioBroadCast(members);
							}

						}

						else
							Toast.makeText(
									context,
									"Please check your Internet connection before make call",
									Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});

			videoBroadLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (!CallDispatcher.isWifiClosed) {
							String members = getMembers(gBean.getOwnerName()
									+ "," + gBean.getActiveGroupMembers());
							Log.i("group123", "members " + members);
							if (members != null && members.length() > 0) {
								calldisp.requestVideoBroadCast(members);
							}
						}

						else
							Toast.makeText(
									context,
									"Please check your Internet connection before make call",
									Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}

				}
			});

			confLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (!CallDispatcher.isWifiClosed) {
							calldisp.requestAudioConference(gBean
									.getActiveGroupMembers());
						} else
							Toast.makeText(
									context,
									"Please check your internet connection before make conference call",
									1).show();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});
			groupChatLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GroupBean gBean = (GroupBean) v.getTag();
					GroupBean groupBean = calldisp.getdbHeler(context)
							.getGroupAndMembers(
									"select * from groupdetails where groupid="
											+ gBean.getGroupId());
					if (groupBean != null
							&& groupBean.getActiveGroupMembers().length() > 0) {
						Intent intent = new Intent(context,
								GroupChatActivity.class);
						intent.putExtra("groupBean", gBean);
						intent.putExtra("isGroup",true);
						startActivity(intent);
						dialog.dismiss();
					} else {
						calldisp.showToast(context, "Sorry no members to chat");
					}
				}
			});

			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("menu dialog", "Exception :: " + e.getMessage());
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private void deleteGroup(final GroupBean groupManagementBean, String message) {

		if (CallDispatcher.isConnected) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					buddiesList.this);
			builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.warning));
			builder.setMessage(
					message + groupManagementBean.getGroupName() + " ?")
					.setCancelable(false)
					.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (!WebServiceReferences.running) {
										calldisp.startWebService(
												getResources().getString(
														R.string.service_url),
												"80");
									}
									ProgressDialog progressDialog = new ProgressDialog(
											context);
									calldisp.showprogress(progressDialog,
											context);
									if (groupManagementBean.getOwnerName()
											.equalsIgnoreCase(
													CallDispatcher.LoginUser))
										WebServiceReferences.webServiceClient
												.deleteGroup(
														CallDispatcher.LoginUser,
														groupManagementBean
																.getGroupId());
									else
										WebServiceReferences.webServiceClient.leaveGroup(
												groupManagementBean
														.getGroupId(),
												CallDispatcher.LoginUser);
								}
							})
					.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert1 = builder.create();
			alert1.show();
		} else {
			ShowError("Network Error",
					"No Network Connection,Can not Delete Buddy", context);
		}

	}

	

	private void shareFiles(String members, String type) {

		String to = getMembers(members);
		if (to != null && to.length() > 0) {
			Intent intentComponent = new Intent(context, ComponentCreator.class);
			Bundle bndl = new Bundle();
			bndl.putString("type", type);
			bndl.putBoolean("action", true);
			bndl.putBoolean("forms", false);
			bndl.putString("buddyname", to);
			bndl.putBoolean("send", true);
			intentComponent.putExtras(bndl);
			startActivity(intentComponent);
		}
	}

	private String getMembers(String to) {
		String[] list = (to).split(",");
		StringBuffer buffer = new StringBuffer();
		for (String tmp : list) {
			if (!tmp.contains(CallDispatcher.LoginUser)) {
				if (buffer.length() == 0)
					buffer.append(tmp);
				else
					buffer.append(",").append(tmp);
			}
		}
		return buffer.toString();

	}

	public void notifyDeleteGroup(Object obj) {
		// TODO Auto-generated method stub
		
	}
}
