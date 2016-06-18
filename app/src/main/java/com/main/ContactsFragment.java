package com.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ContactAdapter;
import com.adapter.NotifyListAdapter;
import com.bean.GroupChatPermissionBean;
import com.bean.NotifyListBean;
import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.account.AMAVerification;
import com.cg.avatar.AnsweringMachineActivity;
import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.CallConnectingScreen;
import com.cg.callservices.inCommingCallAlert;
import com.cg.callservices.SipCallConnectingScreen;
import com.cg.callservices.VideoCallScreen;
import com.cg.commonclass.BuddyListComparator;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListBean;
import com.cg.files.ComponentCreator;
import com.cg.hostedconf.AppReference;
import com.cg.hostedconf.ContactConference;
import com.cg.permissions.PermissionsActivity;
import com.cg.profiles.ViewProfiles;
import com.cg.rounding.RoundingFragment;
import com.cg.rounding.RoundingGroupActivity;
import com.cg.snazmed.R;
import com.group.GroupActivity;
import com.group.GroupAdapter;
import com.group.GroupAdapter2;
import com.group.ViewGroups;
import com.group.chat.GroupChatActivity;
import com.group.chat.GroupChatSettings;
import com.group.chat.ShortMessage;
import com.image.utils.ImageLoader;
import com.screensharing.ScreenSharingFragment;
import com.thread.CommunicationBean;
import com.thread.SipCommunicator;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.FieldTemplateBean;
import org.lib.model.GroupBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.PermissionBean;
import org.lib.model.SignalingBean;
import org.lib.model.UtilityBean;
import org.lib.model.UtilityResponse;
import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * This class is used to show the BuddyList.Using this screen we can find
 * friends list and also their Online Status.
 *
 *
 *
 */
public class ContactsFragment extends Fragment{

	// private Context context = null;

	private Handler handler = new Handler();
	public static Vector<BuddyInformationBean> groupList;
	private static CallDispatcher calldisp = null;
	private String status;

	public boolean isPendingshowing = false;
	public boolean groupstatus = false;
	public boolean contactrecent = false;
	private boolean grouprecent = true;
	public boolean isazsort = true;

	private AlertDialog alert = null;
	private ImageLoader imageLoader;
	public static Boolean isKeyOneNotReceived=false;

	// private TextView title = null;

	// private Button IMRequest;

	// private Button btnReceiveCall;

	// private Button btn_notification = null;

	// private Button btn_addcontact = null;

	// private SlideMenu slidemenu = null;

	public static ArrayList<Integer> buddyRequestCount = new ArrayList<Integer>();

	//	public IndexableListView lv = null;
	public ListView lv,lv2 = null;

	private ArrayList<UtilityBean> mlist = new ArrayList<UtilityBean>();
	public Vector<NotifyListBean> seacrhnotifylist = new Vector<NotifyListBean>();
	private TextView tvRequest;

	private TextView SwipeRequest;

	private Button conf_contact = null;

	private Button all_contact = null;

	private Button pending_contact = null;

	private ArrayList<String> alBuddies = new ArrayList<String>();

	private LinearLayout llayListItems;

	private UtilityResponse response;

	public String selectedBuddy;
//	private Typeface tf_regular = null;


	private int selectedposition;

	public int view = 0;

	private PermissionBean permissionBean = null;

	private ProgressDialog dialog = null;

	private static ContactsFragment contactsFragment;
	private boolean isContact = false;
	public Context mainContext;
	private  GroupBean groupManagementBean;
	private String quotes = "\"";
	public static String SortType="ALPH";
	boolean searchClick = false;



	// public ArrayList<Databean> contactList = new ArrayList<Databean>();

	private static ProgressDialog pDialog;

	public View _rootView;

	private Button plus = null; // this button plus hide in this page,this
	// button create fragment xml
	public static Vector<BuddyInformationBean> buddyList = new Vector<BuddyInformationBean>();

	private static ContactAdapter contactAdapter;
	public NotifyListAdapter notifyAdapter;
	public Vector<NotifyListBean> tempnotifylist = new Vector<NotifyListBean>();
	public Vector<NotifyListBean> contactrecentlist = new Vector<NotifyListBean>();
	public Vector<NotifyListBean> grouprecentlist = new Vector<NotifyListBean>();


	private Vector<FieldTemplateBean> OtherDetails = new Vector<FieldTemplateBean>();

	private long mLastClickTime = 0;
	private AudioManager am = null;
	public static Vector<GroupBean> mygroupList = new Vector<GroupBean>();
	public static Vector<GroupBean> buddygroupList = new Vector<GroupBean>();
	public static synchronized Vector<GroupBean> getGroupList() {

		return mygroupList;
	}
	public static synchronized Vector<GroupBean> getBuddyGroupList() {

		return buddygroupList;
	}

	public static synchronized ContactAdapter getContactAdapter() {

		return contactAdapter;
	}
	public static synchronized GroupAdapter getGroupAdapter() {

		return GroupActivity.groupAdapter;
	}



	public static synchronized Vector<BuddyInformationBean> getBuddyList() {

		return buddyList;
	}



	private AppMainActivity appMainActivity;

	public static ContactsFragment getInstance(Context context) {
		try {
			if (contactsFragment == null) {
				contactsFragment = new ContactsFragment();
				contactsFragment.setContext(context);
				calldisp = CallDispatcher.getCallDispatcher(context);

			}

			return contactsFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return contactsFragment;
		}
	}

	public void setContext(Context cxt) {
		this.mainContext = cxt;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
//		tf_regular = Typeface.createFromAsset(mainContext.getAssets(),
//				getResources().getString(R.string.fontfamily));

		AppReference.bacgroundFragment=contactsFragment;
		appMainActivity = SingleInstance.mainContext;
		SingleInstance.instanceTable.put("contactspage", contactsFragment);
		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.GONE);
		final RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
		mainHeader.setVisibility(View.VISIBLE);
		LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);

		final Button search = (Button) getActivity().findViewById(R.id.btn_settings);
		search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_search));
		search.setVisibility(View.VISIBLE);
		search.setText("");

		final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
		plusBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_add_contact));
		plusBtn.setVisibility(View.VISIBLE);

		imageLoader = new ImageLoader(mainContext);

		Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
		backBtn.setVisibility(View.GONE);
		final EditText search_box = (EditText)getActivity().findViewById(R.id.search_box);
		search_box.setVisibility(View.GONE);

		TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setVisibility(View.VISIBLE);
		title.setText("CONTACTS");
		title.setTextSize(20);
//		title.setTypeface(tf_regular);
		loadCurrentStatus();
		RelativeLayout audio_minimize = (RelativeLayout)getActivity().findViewById(R.id.audio_minimize);
		RelativeLayout video_minimize = (RelativeLayout)getActivity().findViewById(R.id.video_minimize);
		audio_minimize.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainHeader.setVisibility(View.GONE);
				addShowHideListener(true);
			}
		});
		video_minimize.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainHeader.setVisibility(View.GONE);
				addShowHideListener(false);
			}
		});
		ImageView min_incall=(ImageView)getActivity().findViewById(R.id.min_incall);
		ImageView min_outcall=(ImageView)getActivity().findViewById(R.id.min_outcall);
		min_incall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainHeader.setVisibility(View.GONE);
				inCommingCallAlert incommingCallAlert = inCommingCallAlert.getInstance(SingleInstance.mainContext);
				FragmentManager fragmentManager = SingleInstance.mainContext
						.getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(
						R.id.activity_main_content_fragment, incommingCallAlert)
						.commitAllowingStateLoss();
			}
		});
		min_outcall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainHeader.setVisibility(View.GONE);
				CallConnectingScreen callConnectingScreen = CallConnectingScreen.getInstance(SingleInstance.mainContext);
				FragmentManager fragmentManager = SingleInstance.mainContext
						.getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(
						R.id.activity_main_content_fragment, callConnectingScreen)
						.commitAllowingStateLoss();
			}
		});
		min_incall.setOnTouchListener(new com.cg.commonclass.Touch());
		video_minimize.setOnTouchListener(new com.cg.commonclass.Touch());
		min_outcall.setOnTouchListener(new com.cg.commonclass.Touch());
		audio_minimize.setOnTouchListener(new com.cg.commonclass.Touch());


		_rootView = null;
		if (_rootView == null) {

			_rootView = inflater.inflate(R.layout.sliddingdrawer1, null);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			try {
				Button selectall = (Button) getActivity().findViewById(
						R.id.btn_brg);
				selectall.setVisibility(View.GONE);

				// TODO Auto-generated method stub
				WebServiceReferences.contextTable.put("IM", mainContext);
				isPendingshowing = false;

				WebServiceReferences.contextTable.put("buddiesList",
						mainContext);

//				lv = (IndexableListView) _rootView.findViewById(R.id.listview_coontact);
				lv = (ListView) _rootView.findViewById(R.id.listview_coontact);
				lv2 = (ListView) _rootView.findViewById(R.id.listview_group);

				contactAdapter = new ContactAdapter(mainContext, ContactsFragment.getBuddyList());
				isContact = true;

				SortList();

				GroupActivity.getAllGroups();
				loadRecents();



				GroupActivity.groupAdapter = new GroupAdapter(mainContext,
						R.layout.grouplist,getGroupList());
				GroupActivity.groupAdapter.notifyDataSetChanged();
				Log.i("Test", "OWNER LIST>>>>>>" + getGroupList());
				Log.i("Test", "OWNER LIST>>>>>>" + getBuddyGroupList());

				GroupActivity.groupAdapter2 = new GroupAdapter2(mainContext,
						R.layout.grouplist, getBuddyGroupList());
				GroupActivity.groupAdapter2.notifyDataSetChanged();

				Log.i("Test", "CONTACT>>>>>>>>>" + groupList);
				lv.setAdapter(contactAdapter);
				lv.setDivider(null);

				final LinearLayout sort_lay = (LinearLayout) _rootView.findViewById(R.id.sort_lay);
				final LinearLayout group_sort = (LinearLayout) _rootView.findViewById(R.id.group_sort);
				final LinearLayout main_search = (LinearLayout) _rootView.findViewById(R.id.main_search);
				final Button online_sort = (Button) _rootView.findViewById(R.id.online_sort);
				final Button alph_sort = (Button) _rootView.findViewById(R.id.alpha_sort);
				final EditText myFilter = (EditText)_rootView.findViewById(R.id.searchtext);
				search.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						if(isContact) {
							Intent i = new Intent(getActivity(), AMAVerification.class);
							startActivity(i);
						}
					}
				});
				myFilter.addTextChangedListener(new TextWatcher() {

					public void afterTextChanged(Editable s){}

					public void beforeTextChanged(CharSequence s, int start, int count, int after){}

					public void onTextChanged(CharSequence s, int start, int before, int count){
						if(s!=null && s!="")
							contactAdapter.getFilter().filter(s);
					}
				});

				if(SortType.equalsIgnoreCase("ONLINE")){
					online_sort.setTextColor(getResources().getColor(R.color.white));
					alph_sort.setTextColor(getResources().getColor(R.color.snazlgray));
				}else if(SortType.equalsIgnoreCase("ALPH")){
					online_sort.setTextColor(getResources().getColor(R.color.snazlgray));
					alph_sort.setTextColor(getResources().getColor(R.color.white));
				}

				online_sort.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						online_sort.setTextColor(getResources().getColor(R.color.white));
						alph_sort.setTextColor(getResources().getColor(R.color.snazlgray));
						SortType = "ONLINE";
						SortList();
					}
				});

				alph_sort.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						online_sort.setTextColor(getResources().getColor(R.color.snazlgray));
						alph_sort.setTextColor(getResources().getColor(R.color.white));
						if(isazsort) {

							isazsort = false;
						}else {
							isazsort = true;
						}
						SortType = "ALPH";
							SortList();

					}
				});

				final LinearLayout tv11 = (LinearLayout) _rootView.findViewById(R.id.my_contacts);
				final LinearLayout tv12 = (LinearLayout) _rootView.findViewById(R.id.my_group);
				final TextView contacts = (TextView) _rootView.findViewById(R.id.contacts);
				final TextView list_1 = (TextView) _rootView.findViewById(R.id.list_1);
				final TextView groups = (TextView) _rootView.findViewById(R.id.groups);
				final TextView list_2 = (TextView) _rootView.findViewById(R.id.list_2);
				final View view_mycontact = (View) _rootView.findViewById(R.id.view_mycontact);
				final View view_mygroup = (View) _rootView.findViewById(R.id.view_mygroup);






				//On Click Listeners
				tv11.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

							try {
								if(contactrecent) {
								isContact = true;
								contactrecent = false;
									grouprecent = true;
								if (buddyList == null || buddyList.size() == 0)
									showToast("No Contacts");
								lv.setAdapter(null);
								lv.setAdapter(contactAdapter);
								lv2.setVisibility(View.GONE);
								lv.setVisibility(View.VISIBLE);
								contactAdapter.notifyDataSetChanged();
								EditText myFilter = (EditText) _rootView.findViewById(R.id.searchtext);
								myFilter.setText("");
								contacts.setTextColor(getResources().getColor(R.color.white));
									list_1.setText("LIST");
								list_1.setTextColor(getResources().getColor(R.color.white));
								groups.setTextColor(getResources().getColor(R.color.black));
								list_2.setTextColor(getResources().getColor(R.color.black));
								view_mycontact.setVisibility(View.VISIBLE);
								view_mygroup.setVisibility(View.GONE);
								plusBtn.setVisibility(View.VISIBLE);
								sort_lay.setVisibility(View.VISIBLE);
								group_sort.setVisibility(View.GONE);
								plusBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_add_contact));
								search.setVisibility(View.VISIBLE);
								}else{
									loadRecents();
									contactrecent = true;
									grouprecent = true;
									lv2.setVisibility(View.GONE);
									lv.setVisibility(View.VISIBLE);
									EditText myFilter = (EditText) _rootView.findViewById(R.id.searchtext);
									myFilter.setText("");

									lv.setAdapter(null);
									notifyAdapter = new NotifyListAdapter(mainContext, contactrecentlist);
									notifyAdapter.isFromOther(true);
									lv.setAdapter(notifyAdapter);
									Log.d("Stringadapter", "values" + notifyAdapter);
									notifyAdapter.notifyDataSetChanged();
									contacts.setTextColor(getResources().getColor(R.color.pale_white));
									list_1.setText("RECENT");
									list_1.setTextColor(getResources().getColor(R.color.snazgray));
									groups.setTextColor(getResources().getColor(R.color.black));
									list_2.setTextColor(getResources().getColor(R.color.black));
									view_mycontact.setVisibility(View.VISIBLE);
									sort_lay.setVisibility(View.GONE);
									group_sort.setVisibility(View.GONE);
									view_mygroup.setVisibility(View.GONE);
									plusBtn.setVisibility(View.VISIBLE);
									plusBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_add_contact));
									search.setVisibility(View.VISIBLE);

								}
							} catch (Exception e) {
								e.printStackTrace();
							}

					}
				});

				tv12.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							if(grouprecent) {
								isContact = false;
								contactrecent = true;
								grouprecent = false;
								lv2.setAdapter(null);
								lv2.setAdapter(GroupActivity.groupAdapter);
								lv.setVisibility(View.GONE);
								lv2.setVisibility(View.VISIBLE);
								GroupActivity.groupAdapter.notifyDataSetChanged();
								groups.setTextColor(getResources().getColor(R.color.white));
								plusBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_pluswhite));
								list_2.setTextColor(getResources().getColor(R.color.white));
								list_2.setText("LIST");
								contacts.setTextColor(getResources().getColor(R.color.black));
								list_1.setTextColor(getResources().getColor(R.color.black));
								view_mycontact.setVisibility(View.GONE);
								view_mygroup.setVisibility(View.VISIBLE);
								plusBtn.setVisibility(View.VISIBLE);
								sort_lay.setVisibility(View.GONE);
								group_sort.setVisibility(View.VISIBLE);
								main_search.setVisibility(View.GONE);
								search.setVisibility(View.VISIBLE);
							}else{
								loadRecents();
								isContact = false;
								contactrecent = true;
								grouprecent = true;
								lv2.setAdapter(null);
								lv.setVisibility(View.GONE);
								lv2.setVisibility(View.VISIBLE);

								notifyAdapter = new NotifyListAdapter(mainContext, grouprecentlist);
								notifyAdapter.isFromOther(true);
								lv2.setAdapter(notifyAdapter);
								Log.d("Stringadapter", "values" + notifyAdapter);
								notifyAdapter.notifyDataSetChanged();

								groups.setTextColor(getResources().getColor(R.color.white));
								plusBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_pluswhite));
								list_2.setTextColor(getResources().getColor(R.color.pale_white));
								list_2.setText("RECENT");
								contacts.setTextColor(getResources().getColor(R.color.black));
								list_1.setTextColor(getResources().getColor(R.color.black));
								view_mycontact.setVisibility(View.GONE);
								view_mygroup.setVisibility(View.VISIBLE);
								plusBtn.setVisibility(View.VISIBLE);
								sort_lay.setVisibility(View.GONE);
								group_sort.setVisibility(View.GONE);
								main_search.setVisibility(View.GONE);
								search.setVisibility(View.VISIBLE);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				plusBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							if(isContact) {
								SearchPeopleFragment searchPeopleFragment = SearchPeopleFragment.newInstance(mainContext);
								FragmentManager fragmentManager = SingleInstance.mainContext
										.getSupportFragmentManager();
								fragmentManager.beginTransaction().replace(
										R.id.activity_main_content_fragment, searchPeopleFragment)
										.commitAllowingStateLoss();
							}else{
								Intent intent = new Intent(getActivity().getApplicationContext(),
										GroupActivity.class);
								getActivity().startActivity(intent);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				all_contact = (Button)getActivity()
						.findViewById(R.id.all_contacts);
				pending_contact = (Button)getActivity()
						.findViewById(R.id.pending_contacts);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			try {

				// Log.d("register", "buddy list Size : "
				// + ContactsFragment.getBuddyList().size());

				ContactsFragment.getContactAdapter().notifyDataSetChanged();
				ContactsFragment.getGroupAdapter().notifyDataSetChanged();

			} catch (Exception e) {
				e.printStackTrace();
			}

			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		return _rootView;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void SortList() {
		try {
			Log.d("AAA", "Shortlist loaded.");
			handler.post(new Runnable() {
				@Override
				public void run() {
					contactAdapter = new ContactAdapter(mainContext, GroupChatActivity.getAdapterList(ContactsFragment.getBuddyList()));
					lv.setAdapter(null);
					lv.setAdapter(contactAdapter);
					ContactsFragment.contactAdapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getList()
	{
		GroupActivity.getAllGroups();
		ContactsFragment.getGroupList().clear();
		ContactsFragment.getBuddyGroupList().clear();
		Vector<GroupBean> tempList=new Vector<GroupBean>();
		synchronized (ContactsFragment.getGroupList()) {
			ContactsFragment.getGroupList().addAll(GroupActivity.groupList);
		}
		if (ContactsFragment.getGroupAdapter() != null) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					try {
						// TODO Auto-generated method stub
						lv2.setAdapter(null);
						GroupActivity.groupAdapter = new GroupAdapter(mainContext,
								R.layout.grouplist,getGroupList());
						lv2.setAdapter(GroupActivity.groupAdapter);
						ContactsFragment.getGroupAdapter()
								.notifyDataSetChanged();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}
//		if (ContactsFragment.getGroupAdapter2() != null) {
//			handler.post(new Runnable() {
//
//				@Override
//				public void run() {
//					try {
//						// TODO Auto-generated method stub
//						lv.setAdapter(null);
//						GroupActivity.groupAdapter2 = new GroupAdapter2(mainContext,
//								R.layout.grouplist,getBuddyGroupList());
//						lv.setAdapter(GroupActivity.groupAdapter2);
//						ContactsFragment.getGroupAdapter2()
//								.notifyDataSetChanged();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			});
//
//		}

	}


	public View getParentView() {
		return _rootView;
	}

	@Override
	public void onDestroy() {
		try {
			SingleInstance.isbcontacts = false;
			// TODO Auto-generated method stub
			WebServiceReferences.contextTable.remove("buddiesList");
			super.onDestroy();
			Button search = (Button) getActivity().findViewById(R.id.btn_settings);
			search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_search));
			search.setVisibility(View.GONE);
			SingleInstance.instanceTable.remove("contactspage");
			// MemoryProcessor.getInstance().unbindDrawables(_rootView);
			// _rootView = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyBuddyDeleted(final String server_msg) {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					try {
						// TODO Auto-generated method stub
						cancelDialog();
						if (server_msg != null) {
							if (server_msg
									.equalsIgnoreCase("Contact deleted successfully")) {
								ContactsFragment.getContactAdapter().deleteUser(selectedBuddy);
								calldisp.getdbHeler(mainContext).deleteBuddyProfile(selectedBuddy);
								DBAccess.getdbHeler()
										.deleteGroupChatEntryLocally(selectedBuddy,
												CallDispatcher.LoginUser);
								DBAccess.getdbHeler().deleteIndividualChat(selectedBuddy);
								if (DBAccess.getdbHeler().isRecordExists(
										"select * from autoacceptcalls where username='"
												+ selectedBuddy + "' and owner='" + CallDispatcher.LoginUser + "'")) {
									DBAccess.getdbHeler().deleteBuddyFromAutomacticCall(
													selectedBuddy, CallDispatcher.LoginUser);
								}
							}
							Toast.makeText(mainContext, server_msg,
									Toast.LENGTH_SHORT).show();
                            SortList();
                            contactAdapter.notifyDataSetChanged();

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showdialog(final String status, final Context context) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					// TODO Auto-generated method stub

					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

					dialog.setContentView(R.layout.buddycontact);
					dialog.setTitle("Select any service you want to make");
					WindowManager.LayoutParams wmlp = dialog.getWindow()
							.getAttributes();

					wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
					wmlp.x = 10; // x position
					wmlp.y = 10; // y position
					Bitmap bitmap = null;
					permissionBean = calldisp.getdbHeler(mainContext)
							.selectPermissions(
									"select * from setpermission where userid='"
											+ selectedBuddy + "' and buddyid='"
											+ CallDispatcher.LoginUser + "'",
									selectedBuddy, CallDispatcher.LoginUser);
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
					// LinearLayout answerlay = (LinearLayout) dialog
					// .findViewById(R.id.answerlay);
					Button answerLay = (Button) dialog
							.findViewById(R.id.answerlay_btn);
					ImageView screenShare = (ImageView) dialog
							.findViewById(R.id.screen);
					ImageView profilepicture = (ImageView) profi_pic
							.findViewById(R.id.pictures);
					String profilePic = calldisp.getdbHeler(mainContext)
							.getProfilePic(selectedBuddy);

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
//					buddyname.setTypeface(tf_regular);
					TextView buddyStatus = (TextView) buddyname_layout
							.findViewById(R.id.status);
//					buddyStatus.setTypeface(tf_regular);
					buddyStatus.setText(status);
					buddyStatus.setTextColor(Color.BLUE);
					Button closedialog = (Button) buddyLay
							.findViewById(R.id.close_dialog);
					LinearLayout MMLay = (LinearLayout) dialog
							.findViewById(R.id.MMlay);
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
					Button withdrawBtn = (Button) dialog
							.findViewById(R.id.withdrawlay_btn);
					LinearLayout sipcall = (LinearLayout) dialog
							.findViewById(R.id.sipcalllay);
					Button accessBtn = (Button) dialog
							.findViewById(R.id.acceslay_btn);
					Button all_filesBtn = (Button) dialog
							.findViewById(R.id.allfiles_btn);
					LinearLayout DeletCon = (LinearLayout) dialog
							.findViewById(R.id.deletecontactlay);
					LinearLayout ViewProf = (LinearLayout) dialog
							.findViewById(R.id.viewproflay);
					Button messagelay = (Button) dialog
							.findViewById(R.id.answerlay_btn_5);
					ViewProf.setTag(selectedBuddy);
					messagelay.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								// TODO Auto-generated method stub
								Intent smsintent = new Intent(context,
										ShortMessage.class);
								smsintent
										.putExtra("Grid",true);
								startActivity(smsintent);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);
								e.printStackTrace();
							}
						}
					});


					MMLay.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
//								// TODO Auto-generated method stub
//								if (permissionBean.getMMchat().equals("1")) {
//									doMultiMMChat(v.getTag().toString());
//									dialog.cancel();
//								} else
//									calldisp.showAlert("Response",
//											"Access Denied");
								dialog.dismiss();
								if (CallDispatcher.LoginUser != null) {
									if (CallDispatcher.latitude == 0.0
											&& CallDispatcher.longitude == 0.0) {
										showToast("Sorry! Turn On Location Service ");
									} else {
										GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
												.get("groupchat");
										gChat.sendMsg("Latitude:" + CallDispatcher.latitude + ","
														+ "Longitude:" + CallDispatcher.longitude,
												null, "location", null);
									}
								} else {
									showToast("Sorry! can not send Message");
								}
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
					screenShare.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								// TODO Auto-generated method stub
								if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
									Intent intentComponent = new Intent(
											getActivity()
													.getApplicationContext(),
											ScreenSharingFragment.class);
									getActivity()
											.startActivity(intentComponent);
								} else {
									showToast("Sorry, your android OS version not supported");
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

					DeletCon.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								// TODO Auto-generated method stub
								if (SingleInstance.mainContext
										.isNetworkConnectionAvailable()) {
                                    Log.d("R4J1","Delete contact on click from chat.");
									doDeleteContact("");
									dialog.dismiss();
								} else {
									showAlert1("Info",
											"Check internet connection Unable to connect server");

								}
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
								if (permissionBean.getViewProfile().equals("1")) {
									doViewProfile(true, v.getTag().toString());
									view = 1;
									CallDispatcher.profileRequested = true;
									dialog.dismiss();
								} else {
									showAlert1("Response",
											"Access Denied");
									// showToast("Access Denied");
								}
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
								// Intent intentComponent = new Intent(
								// getActivity().getApplicationContext(),
								// BuddyNoteList.class);
								// Bundle bndl = new Bundle();
								// bndl.putString("buddyname", selectedBuddy);
								// bndl.putInt("view_mode", 0);
								// intentComponent.putExtras(bndl);
								// getActivity().startActivity(intentComponent);

								Vector<CompleteListBean> filesList = loadFiles(selectedBuddy);
								// Log.i("files123",
								// "buddyyyyyy   " + selectedBuddy
								// + "    " + filesList.size());
								// filesAdapter = new FilesAdapter(mainContext,
								// filesList);
								if (filesList.size() > 0) {
									GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
											.get("groupchat");
									gChat.finish();

									FilesFragment filesFragment = FilesFragment
											.newInstance(GroupChatActivity.context);
									filesFragment.setFromContacts(true);
									filesFragment.getUsername(selectedBuddy);
									// filesFragment.filesListRefresh();
									SingleInstance.myOrder = false;
									AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
											.get("MAIN");
									FragmentManager fragmentManager = appMainActivity
											.getSupportFragmentManager();
									FragmentTransaction fragmentTransaction = fragmentManager
											.beginTransaction();
									fragmentTransaction
											.replace(
													R.id.activity_main_content_fragment,
													filesFragment);
									fragmentTransaction.commitAllowingStateLoss();

								} else {

									showToast("No Files available for this Buddy");

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
								if (permissionBean.getTextMessage().equals("1")) {
									Intent intentComponent = new Intent(
											getActivity()
													.getApplicationContext(),
											ComponentCreator.class);
									Bundle bndl = new Bundle();
									bndl.putString("type", "note");
									bndl.putBoolean("action", true);
									bndl.putBoolean("forms", false);
									bndl.putString("buddyname", selectedBuddy);
									bndl.putBoolean("send", true);
									intentComponent.putExtras(bndl);
									getActivity()
											.startActivity(intentComponent);
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
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
								if (permissionBean.getPhotoMessage()
										.equals("1")) {
//									Intent intentComponent = new Intent(
//											getActivity()
//													.getApplicationContext(),
//											ComponentCreator.class);
//									Bundle bndl = new Bundle();
//									bndl.putString("type", "photo");
//									bndl.putBoolean("action", true);
//									bndl.putBoolean("forms", false);
//									bndl.putBoolean("send", true);
//									bndl.putString("buddyname", selectedBuddy);
//									intentComponent.putExtras(bndl);
//									getActivity()
//											.startActivity(intentComponent);
									GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
											.get("groupchat");
									gChat.photochat();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
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
								if (permissionBean.getAudioMessage()
										.equals("1")) {
									GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
											.get("groupchat");
									gChat.showAudioMessageDialog();
//									Intent intentComponent = new Intent(
//											getActivity()
//													.getApplicationContext(),
//											ComponentCreator.class);
//									Bundle bndl = new Bundle();
//									bndl.putString("type", "audio");
//									bndl.putBoolean("action", true);
//									bndl.putBoolean("forms", false);
//									bndl.putBoolean("send", true);
//									bndl.putString("buddyname", selectedBuddy);
//									intentComponent.putExtras(bndl);
//									getActivity()
//											.startActivity(intentComponent);
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
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
								if (permissionBean.getVideoMessage()
										.equals("1")) {
									GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
											.get("groupchat");
									gChat.showVideoMessageDialog();
//									Intent intentComponent = new Intent(
//											getActivity()
//													.getApplicationContext(),
//											ComponentCreator.class);
//									Bundle bndl = new Bundle();
//									bndl.putString("type", "video");
//									bndl.putBoolean("action", true);
//									bndl.putBoolean("forms", false);
//									bndl.putString("buddyname", selectedBuddy);
//									bndl.putBoolean("send", true);
//									intentComponent.putExtras(bndl);
//									getActivity()
//											.startActivity(intentComponent);
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
							} catch (Exception e) {
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);
								e.printStackTrace();
							}

						}
					});
					withdrawBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// Log.i("onclick123", "clicked withdraw");
							try {
								// TODO Auto-generated method stub
								if (appMainActivity
										.isNetworkConnectionAvailable()) {
									// Log.i("onclick123",
									// "inside network condition");
									AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(
											GroupChatActivity.context);
									deleteConfirmation.setTitle("WithDrawn");
									deleteConfirmation
											.setMessage("Are you sure, you want to withdraw all your share with "
													+ selectedBuddy + "?");
									deleteConfirmation
											.setPositiveButton(
													SingleInstance.mainContext.getResources().getString(R.string.yes)
													,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															try {
																CallDispatcher.pdialog = new ProgressDialog(
																		mainContext);
																if (WebServiceReferences.running) {
																	calldisp.showprogress(
																			CallDispatcher.pdialog,
																			mainContext);
																	WebServiceReferences.webServiceClient
																			.DeleteAllShares(
																					CallDispatcher.LoginUser,
																					selectedBuddy);
																}
																dialog.dismiss();

															} catch (Exception e) {
																Log.e("Exception",
																		"===>"
																				+ e.getMessage());
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															}
															dialog.dismiss();
														}

													});
									deleteConfirmation
											.setNegativeButton(
													"No",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															try {
																dialog.dismiss();

															} catch (Exception e) {
																Log.e("Exception",
																		"===>"
																				+ e.getMessage());
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															}
														}

													});
									deleteConfirmation.show();
									dialog.dismiss();
								} else {
									// Log.i("onclick123",
									// "else network condition");
									Toast.makeText(
											mainContext,
											"Please check your Internet connection before withdraw",
											Toast.LENGTH_SHORT).show();
								}
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
								if (permissionBean.getSketchMessage().equals(
										"1")) {
//									Intent intentComponent = new Intent(
//											getActivity()
//													.getApplicationContext(),
//											ComponentCreator.class);
//									Bundle bndl = new Bundle();
//									bndl.putString("type", "handsketch");
//									bndl.putBoolean("action", true);
//									bndl.putBoolean("forms", false);
//									bndl.putString("buddyname", selectedBuddy);
//									bndl.putBoolean("send", true);
//									intentComponent.putExtras(bndl);
//									getActivity()
//											.startActivity(intentComponent);
									GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
											.get("groupchat");
									gChat.handsketch();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);
								e.printStackTrace();
							}

						}
					});

					all_filesBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							try {
								// TODO Auto-generated method stub
								// Intent intentComponent = new Intent(
								// getActivity().getApplicationContext(),
								// BuddyNoteList.class);
								// Bundle bndl = new Bundle();
								// bndl.putInt("view_mode", 1);
								// intentComponent.putExtras(bndl);
								// getActivity().startActivity(intentComponent);
								Vector<CompleteListBean> filesList = loadFiles(CallDispatcher.LoginUser);
								if (filesList.size() > 0) {
									GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
											.get("groupchat");
									gChat.finish();
									Log.d("Test", "Files size=========="
											+ filesList.size());
									FilesFragment filesFragment = FilesFragment
											.newInstance(GroupChatActivity.context);
									filesFragment.setFromContacts(true);
									filesFragment
											.getUsername(CallDispatcher.LoginUser);
									AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
											.get("MAIN");
									FragmentManager fragmentManager = appMainActivity
											.getSupportFragmentManager();
									FragmentTransaction fragmentTransaction = fragmentManager
											.beginTransaction();
									fragmentTransaction
											.replace(
													R.id.activity_main_content_fragment,
													filesFragment);
									//fragmentTransaction.commit();
									fragmentTransaction.commitAllowingStateLoss();
									dialog.dismiss();
								} else {
									showToast("Sorry no files");
								}
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
									if (appMainActivity
											.isNetworkConnectionAvailable())
										processCallRequest(1, selectedBuddy);
									else
										Toast.makeText(
												mainContext,
												"Please check your Internet connection before make call",
												Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);
								e.printStackTrace();
							}
						}
					});
					sipcall.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								// TODO Auto-generated method stub
								if (permissionBean.getAudio_call().equals("1")) {
									if (appMainActivity
											.isNetworkConnectionAvailable())
										sipprocessCallRequest(selectedBuddy);
									else
										Toast.makeText(
												mainContext,
												"Please check your Internet connection before make call",
												Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
							} catch (Exception e) {
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
									if (appMainActivity
											.isNetworkConnectionAvailable())
										processCallRequest(2, selectedBuddy);
									else
										Toast.makeText(
												mainContext,
												"Please check your Internet connection before make call",
												Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
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
									if (appMainActivity
											.isNetworkConnectionAvailable())
										processCallRequest(5, selectedBuddy);
									else
										Toast.makeText(
												mainContext,
												"Please check your Internet connection before make call",
												Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
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
									if (appMainActivity
											.isNetworkConnectionAvailable())
										processCallRequest(6, selectedBuddy);
									else
										Toast.makeText(
												mainContext,
												"Please check your Internet connection before make call",
												Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
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
									if (appMainActivity
											.isNetworkConnectionAvailable())
										processCallRequest(3, selectedBuddy);
									else
										Toast.makeText(
												mainContext,
												"Please check your Internet connection before make call",
												Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
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
									if (appMainActivity
											.isNetworkConnectionAvailable())
										processCallRequest(4, selectedBuddy);
									else
										Toast.makeText(
												mainContext,
												"Please check your Internet connection before make call",
												Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								} else
									calldisp.showAlert("Response",
											"Access Denied");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);
								e.printStackTrace();
							}
						}
					});

					answerLay.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							try {
//								// TODO Auto-generated method stub
								if (permissionBean.getAvtaar().equals("1")) {
									CallDispatcher.pdialog = new ProgressDialog(
											GroupChatActivity.context);
									// calldisp.showprogress(CallDispatcher.pdialog,
									// mainContext);
									showprogressavatar();
									String[] res_info = new String[3];
									res_info[0] = CallDispatcher.LoginUser;
									res_info[1] = selectedBuddy;

									BuddyInformationBean bib = null;
									for (BuddyInformationBean temp : ContactsFragment
											.getBuddyList()) {
										if (!temp.isTitle()) {
											if (temp.getName()
													.equalsIgnoreCase(
															selectedBuddy
																	.trim())) {
												bib = temp;
												break;
											}
										}
									}
									// BuddyInformationBean bib =
									// WebServiceReferences.buddyList
									// .get(selectedBuddy.trim());
									if (bib != null) {
										if (bib.getStatus().equals("Offline")
												|| bib.getStatus().equals(
												"Stealth"))
											res_info[2] = calldisp
													.getdbHeler(mainContext)
													.getwheninfo(
															"select cid from clonemaster where cdescription='Offline'");
										else
											res_info[2] = "";
									} else
										res_info[2] = "";
									WebServiceReferences.webServiceClient
											.OfflineCallResponse(res_info);
									dialog.dismiss();
								} else {
									calldisp.showAlert("Response",
											"Access Denied");
								}
//                                Intent i = new Intent(context, AnsweringMachineActivity.class);
//                                i.putExtra("buddy",selectedBuddy);
//                                startActivity(i);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);
								e.printStackTrace();
							}
						}
					});

					accessBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								// TODO Auto-generated method stub
								Intent permission_intent = new Intent(
										mainContext, PermissionsActivity.class);
								permission_intent.putExtra("buddy",
										selectedBuddy);
								getActivity().startActivity(permission_intent);
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
									if (appMainActivity
											.isNetworkConnectionAvailable()) {
										Vector<BuddyInformationBean> onlineBuddies = new Vector<BuddyInformationBean>();
										Vector<BuddyInformationBean> Buddies = getBuddyList();
										if(Buddies.size()>0) {
											for (BuddyInformationBean bib : Buddies) {
												if (bib.getStatus().equalsIgnoreCase("online")) {
													onlineBuddies.add(bib);
												}
											}
										}
										if (onlineBuddies.size() > 0) {

											Intent intent = new Intent(
													getActivity()
															.getApplicationContext(),
													ContactConference.class);
											getActivity().startActivity(intent);
										} else {
											Toast.makeText(mainContext,
													"Sorry no online users", Toast.LENGTH_SHORT)
													.show();
										}
									} else
										Toast.makeText(
												mainContext,
												"Please check your internet connection before make conference call",
												1).show();

								} else
									calldisp.showAlert("Response",
											"Access Denied");
								dialog.dismiss();
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
		});
	}

	public void showOrderDialog(final String menuName) {}

	public Vector<CompleteListBean> loadFiles(String username) {
		Log.d("Test","Inside LoadFiles");
		Vector<CompleteListBean> filesList = new Vector<CompleteListBean>();
		if (username != null) {
			if (username.equalsIgnoreCase(CallDispatcher.LoginUser)) {
				String strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where  (parentid is NULL or parentid='') and componenttype!='IM' and componenttype!='call' and owner='"
						+ username + "'";
				filesList = DBAccess.getdbHeler().getCompleteListProperties(
						strCompleteListQuery);

			} else {
				String strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where  (parentid is NULL or parentid='') and componenttype!='IM' and componenttype!='call' and fromuser='"
						+ username + "'";
				filesList = DBAccess.getdbHeler().getCompleteListProperties(
						strCompleteListQuery);
			}
		}
		return filesList;
	}

	private Vector<CompleteListBean> loadFilesByParentId(String parentId) {
		Vector<CompleteListBean> filesList = new Vector<CompleteListBean>();
		if (parentId != null) {
			String strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where fromuser = '"
					+ selectedBuddy
					+ "' OR owner='"
					+ CallDispatcher.LoginUser
					+ "' AND parentid!=''";
			filesList = DBAccess.getdbHeler().getCompleteListProperties(
					strCompleteListQuery);
			// Log.i("orderbyfiles", "filesize : " + filesList.size());
		}
		return filesList;
	}

	public void doViewProfile(boolean accept, String buddy) {

		try {
			if (!accept && buddy != null) {
				selectedBuddy = buddy;
			}
			ArrayList<String> profileList = calldisp.getdbHeler(mainContext)
					.getProfile(selectedBuddy);
			if (profileList.size() > 0) {
				Intent intent = new Intent(mainContext, ViewProfiles.class);
				intent.putExtra("buddyname", buddy);
				startActivity(intent);

			} else {
				if (appMainActivity.isNetworkConnectionAvailable()) {
					showprogress();
					CallDispatcher.isFromCallDisp = false;
					String modifiedDate = calldisp.getdbHeler(mainContext)
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
					// Toast.makeText(mainContext, "Kindly check your network ",
					// Toast.LENGTH_SHORT).show();
					showAlert1("Info",
							"Check Internet Connection,Unable to Connect Server");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doDeleteContact(final String buddy) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					selectedBuddy=buddy;
					if (appMainActivity.isNetworkConnectionAvailable()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
						builder.setTitle("Warning !");
						builder.setMessage("Are you sure you want to delete "
										+ buddy + " ?").setCancelable(false).setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												if (!WebServiceReferences.running) {
													calldisp.startWebService(
															getResources()
																	.getString(
																			R.string.service_url), "80");
												}
												showprogress();
												WebServiceReferences.webServiceClient.deletePeople(
																CallDispatcher.LoginUser, buddy, contactsFragment);

											}

										})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												dialog.cancel();
											}
										});
						AlertDialog alert1 = builder.create();
						alert1.show();
					} else {
						ShowError(
								"Info",
								"Check internet connection Unable to connect server",
								mainContext);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
                    Log.d("R4J1","ERROR -> "+e.toString());
				}
			}
		});

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
		try {
			ArrayList<String> profileList = calldisp.getdbHeler(mainContext)
					.getProfile(selectedBuddy);
			// Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0 && view == 1) {
				if (!WebServiceReferences.contextTable
						.containsKey("viewprofile")) {
					viewProfile(selectedBuddy);
					view = 0;
				} else {
					Intent intent = new Intent(mainContext, ViewProfiles.class);
					intent.putExtra("buddyname", selectedBuddy);
					startActivity(intent);
				}
			} else {
				if (CallDispatcher.profileRequested) {
					// Toast.makeText(mainContext,
					// "No profile assigned for this user",
					// Toast.LENGTH_SHORT).show();
//					if (selectedBuddy != null)
//						showAlert1("Info",
//								SingleInstance.mainContext.getResources()
//										.getString(R.string.no_profile_for1)
//										+ selectedBuddy);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cancelDialog();
			calldisp.cancelDialog();
			CallDispatcher.profileRequested = false;
		}
	}

	private void viewProfile(String buddy) {
		Intent intent = new Intent(mainContext, ViewProfiles.class);
		intent.putExtra("buddyname", buddy);
		startActivity(intent);

	}public void sipprocessCallRequest(String buddy) {
		try {
			// final Databean db = (Databean) CallDispatcher.adapterToShow
			// .getItem(selectedposition);
			final BuddyInformationBean bean = ContactsFragment
					.getContactAdapter().getItem(selectedposition);
			String state = bean.getStatus();
			selectedBuddy=buddy;
//			selectedBuddy = ContactsFragment.getContactAdapter().getUser(
//					selectedposition);
			// Log.d("LM", "call status--->" + state);

			if (selectedBuddy != null && state.equalsIgnoreCase("Offline")
					|| state.equals("Stealth")
					|| state.equalsIgnoreCase("pending")
					|| state.equalsIgnoreCase("Virtual")
					|| state.equalsIgnoreCase("airport")) {
				if (WebServiceReferences.running) {
					CallDispatcher.pdialog = new ProgressDialog(mainContext);
					calldisp.showprogress(CallDispatcher.pdialog, mainContext);

					String[] res_info = new String[3];
					res_info[0] = CallDispatcher.LoginUser;
					res_info[1] = selectedBuddy;
					if (state.equals("Offline") || state.equals("Stealth"))
						res_info[2] = calldisp
								.getdbHeler(mainContext)
								.getwheninfo(
										"select cid from clonemaster where cdescription='Offline'");
					else
						res_info[2] = "";

//					WebServiceReferences.webServiceClient
//							.OfflineCallResponse(res_info);
				}

			} else {
				if (!state.equalsIgnoreCase("pending")) {
					//calldisp.MakeCall(caseid, selectedBuddy, mainContext);
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							CommunicationBean cb = new CommunicationBean();
							cb.setAcc_id(3);
							String buddy=selectedBuddy.replace("@","_");
							String endPoint = "sip:" + buddy + "@"+AppMainActivity.relam;
							cb.setSipEndpoint(endPoint);
							Log.d("droid123", "CF SIP relam ==> " + AppMainActivity.relam);
							Log.d("droid123", "CF SIP endpoint ==> "+"sip:" + selectedBuddy + "@" + AppMainActivity.relam);
							cb.setOperationType(SipCommunicator.sip_operation_types.MAKE_CALL);
							AppReference.sipQueue.addMsg(cb);



						}
					});

					Intent callscreen = new Intent(mainContext,
							SipCallConnectingScreen.class);
					callscreen.putExtra("host", true);
					callscreen.putExtra("fromname", selectedBuddy);

					startActivity(callscreen);

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void processCallRequest(int caseid, String User) {
		try {
			// final Databean db = (Databean) CallDispatcher.adapterToShow
			// .getItem(selectedposition);
			final BuddyInformationBean bean = ContactsFragment
					.getContactAdapter().getItem(selectedposition);
			String state = bean.getStatus();
//			selectedBuddy = ContactsFragment.getContactAdapter().getUser(
//					selectedposition);
			selectedBuddy = User;
			 Log.d("LM", "call status--->" + state);

			if (selectedBuddy != null && state.equalsIgnoreCase("Offline")
					|| state.equals("Stealth")
					|| state.equalsIgnoreCase("pending")
					|| state.equalsIgnoreCase("Virtual")
					|| state.equalsIgnoreCase("airport")) {
				if (WebServiceReferences.running) {
					CallDispatcher.pdialog = new ProgressDialog(mainContext);
					calldisp.showprogress(CallDispatcher.pdialog, mainContext);

					String[] res_info = new String[3];
					res_info[0] = CallDispatcher.LoginUser;
					res_info[1] = selectedBuddy;
					if (state.equals("Offline") || state.equals("Stealth"))
						res_info[2] = calldisp
								.getdbHeler(mainContext)
								.getwheninfo(
										"select cid from clonemaster where cdescription='Offline'");
					else
						res_info[2] = "";

//					WebServiceReferences.webServiceClient
//							.OfflineCallResponse(res_info);
				}

			} else {
				if (!state.equalsIgnoreCase("pending")) {
					Log.d("LM", "call status--->2 make call" + state);
					calldisp.MakeCall(caseid, selectedBuddy, mainContext);

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ShowConnectionScreen(SignalingBean sbean, String username) {
		try {
			Intent intent = new Intent(getActivity().getApplicationContext(),
					CallConnectingScreen.class);
			Bundle bundle = new Bundle();
			bundle.putString("name", username);
			bundle.putString("type", sbean.getCallType());
			bundle.putBoolean("status", false);
			bundle.putSerializable("bean", sbean);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyOfflineCallResponse(final Object obj) {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
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
											offlineRequestConfigBean
													.getConfig_id());
									cv.put("fromuser", selectedBuddy);
									cv.put("messagetitle",
											offlineRequestConfigBean
													.getMessageTitle());
									cv.put("messagetype",
											offlineRequestConfigBean
													.getMessagetype());
									cv.put("responsetype",
											offlineRequestConfigBean
													.getResponseType());
									cv.put("response", "''");
									cv.put("url",
											offlineRequestConfigBean.getUrl());
									cv.put("receivedtime", received_time);
									cv.put("sendstatus", "0");
									cv.put("username", CallDispatcher.LoginUser);

									cv.put("message", offlineRequestConfigBean
											.getMessage());
									cv.put("status", 0);

									int id = calldisp.getdbHeler(mainContext)
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
										File message_file = new File(
												message_path);
										if (!message_file.exists()) {
											offlineRequestConfigBean
													.setStatus(1);
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
									Intent intent = new Intent(getActivity()
											.getApplicationContext(),
											AnsweringMachineActivity.class);
									intent.putExtra("buddy", buddy_id);
									intent.putExtra("avatarlist", config_list);
									getActivity().startActivity(intent);
								}
							}

						}

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;
						if (WebServiceReferences.contextTable
								.containsKey("imtabs")) {
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
							ShowError("Warning !", service_bean.getText(),
									cntxt);
						} else
							ShowError("Warning !", service_bean.getText(),
									mainContext);

						calldisp.cancelDialog();
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void doMultiMMChat(String buddyStatus) {

		try {
			if (appMainActivity.isNetworkConnectionAvailable()) {

				// BuddyInformationBean bibbean = (BuddyInformationBean)
				// ContactsFragment
				// .getContactAdapter().getItem(selectedposition);
				//
				// String state = bibbean.getStatus();
				// Utility utility = new Utility();
				// WebServiceReferences.SelectedBuddy = selectedBuddy;
				// SignalingBean bean = new SignalingBean();
				// bean.setSessionid(utility.getSessionID());
				// bean.setFrom(CallDispatcher.LoginUser);
				// bean.setTo(selectedBuddy);
				// bean.setConferencemember("");
				// bean.setMessage("");
				// bean.setCallType("MSG");

				// Intent intent = new Intent(getActivity()
				// .getApplicationContext(), ChatActivity.class);
				// intent.putExtra("buddy", selectedBuddy);
				// intent.putExtra("status", state);
				// intent.putExtra("ssionid", utility.getSessionID());

				// intent.putExtra("sb", bean);
				// intent.putExtra("fromto", true);
				// intent.putExtra("status", state);
				// getActivity().startActivity(intent);
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), GroupChatActivity.class);
				intent.putExtra("groupid", CallDispatcher.LoginUser
						+ selectedBuddy);
				intent.putExtra("isGroup", false);
				intent.putExtra("buddy", selectedBuddy);
				intent.putExtra("buddystatus", buddyStatus);
				mainContext.startActivity(intent);

			} else {
				ShowError("Network Error", "No Network Connection", mainContext);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyDeleteallshareResponse(final Object obj) {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					calldisp.cancelDialog();
					if (obj instanceof String[]) {
						calldisp.getdbHeler(mainContext).deleteAllshares(
								"DELETE from formsettings WHERE buddyid="
										+ "\"" + selectedBuddy.trim() + "\"");
						calldisp.getdbHeler(mainContext).deleteAllshares(
								"DELETE from buddyprofile WHERE buddy=" + "\""
										+ selectedBuddy.trim() + "\"");
						calldisp.getdbHeler(mainContext).deleteAllshares(
								"DELETE from offlinecallsettingdetails WHERE buddyid="
										+ "\"" + selectedBuddy.trim() + "\"");
						calldisp.getdbHeler(mainContext).deleteAllshares(
								"DELETE from userprofile WHERE userid=" + "\""
										+ selectedBuddy.trim() + "\"");
						Toast.makeText(mainContext, "Deleted Succesfully",
								Toast.LENGTH_SHORT).show();
					} else if (obj instanceof WebServiceBean) {
						calldisp.showAlert("Response",
								((WebServiceBean) obj).getText());

					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteGroup(final GroupBean groupManagementBean, String message) {

		try {
			if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GroupChatActivity.context);
				builder.setTitle("Warning !");
				builder.setMessage(
						message + groupManagementBean.getGroupName() + " ?")
						.setCancelable(false)
						.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes)
								,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										if (!WebServiceReferences.running) {
											calldisp.startWebService(
													getResources()
															.getString(
																	R.string.service_url),
													"80");
										}
										showprogress();
										if (groupManagementBean
												.getOwnerName()
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
										GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
												.get("groupchat");
										gChat.finish();

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert1 = builder.create();
				alert1.show();
			} else {
				ShowError("Info",
						"Check Internet Connection Unable to Connect server",
						mainContext);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyDeleteGroup(Object obj) {
		Log.i("AAAA","Delaete group");
		cancelDialog();
		try {
			if (obj instanceof GroupBean) {
				String groupId = ((GroupBean) obj).getGroupId();
				if (groupId != null) {
					calldisp.getdbHeler(mainContext).deleteGroupAndMembers(
							groupId);
					GroupBean groupBean = null;
					for (GroupBean gBean : GroupActivity.groupList) {
						if (gBean.getGroupId().equals(groupId)) {
							groupBean = gBean;
							break;
						}
					}
					GroupBean rgroupBean = null;
					for (GroupBean gBean : RoundingGroupActivity.RoundingList) {
						if (gBean.getGroupId().equals(groupId)) {
							rgroupBean = gBean;
							break;
						}
					}
					if (groupBean != null) {
						GroupActivity.groupList.remove(groupBean);
						handler.post(new Runnable() {
							@Override
							public void run() {
								GroupActivity.groupAdapter
										.notifyDataSetChanged();

							}
						});

					}
					if (rgroupBean != null) {
						RoundingGroupActivity.RoundingList.remove(rgroupBean);
						handler.post(new Runnable() {

							@Override
							public void run() {
								RoundingFragment.getRoundingAdapter().notifyDataSetChanged();
							}
						});

					}

					calldisp.getdbHeler(mainContext)
							.deleteGroupChatEntryLocally(groupId,
									CallDispatcher.LoginUser);
					showToast("Group deleted Successfully");
				}
			} else {
				WebServiceBean ws_bean = (WebServiceBean) obj;
				calldisp.ShowError("Error", ws_bean.getText(), mainContext);
			}
			cancelDialog();
			getList();
			RoundingFragment.newInstance(mainContext).getList();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void shareFiles(String members, String type) {

		try {
			String to = getMembers(members);
			if (to != null && to.length() > 0) {

				Intent intentComponent = new Intent(getActivity()
						.getApplicationContext(), ComponentCreator.class);
				Bundle bndl = new Bundle();
				bndl.putString("type", type);
				bndl.putBoolean("action", true);
				bndl.putBoolean("forms", false);
				bndl.putString("buddyname", to);
				bndl.putBoolean("send", true);
				intentComponent.putExtras(bndl);
				getActivity().startActivity(intentComponent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(mainContext, message, 3000).show();
			}
		});

	}
	private void showprogressavatar() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(GroupChatActivity.context);
		pDialog.setCancelable(false);
		pDialog.setMessage("Progress ...");
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setProgress(0);
		pDialog.setMax(100);
		pDialog.show();

	}

	private void showprogress() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(mainContext);
		pDialog.setCancelable(false);
		pDialog.setMessage("Progress ...");
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setProgress(0);
		pDialog.setMax(100);
		pDialog.show();

	}

	public static void cancelDialog() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	public void removeContact(final String name) {

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});

	}

	public void showGroupDialog(GroupBean groupBean) {

		try {
			// TODO Auto-generated method stub
			// Log.i("group123", "item clicked 3");
			if (CallDispatcher.LoginUser != null) {
				final Dialog dialog = new Dialog(getActivity());
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
				LinearLayout chatRow = (LinearLayout) dialog
						.findViewById(R.id.last_row_chat);
				final GroupBean gBean = DBAccess.getdbHeler()
						.getGroupAndMembers(
								"select * from groupdetails where groupid="
										+ groupBean.getGroupId());

				TextView groupName = (TextView) buddyname_layout
						.findViewById(R.id.groupname);
				groupName.setText(groupBean.getGroupName());
//				groupName.setTypeface(tf_regular);
				Button closedialog = (Button) buddyLay
						.findViewById(R.id.close_dialog);
				LinearLayout editGroupLay = (LinearLayout) dialog
						.findViewById(R.id.editgrouplay);
				editGroupLay.setTag(groupBean);
				ImageView editOrViewGroup = (ImageView) dialog
						.findViewById(R.id.editgroup);
				TextView editGroupText = (TextView) editGroupLay
						.findViewById(R.id.tx_editgroup);

				LinearLayout audioBroadLay = (LinearLayout) dialog
						.findViewById(R.id.audio_broadcast_lay);
				audioBroadLay.setTag(gBean);
				LinearLayout videoBroadLay = (LinearLayout) dialog
						.findViewById(R.id.video_broadcast_lay);
				videoBroadLay.setTag(gBean);
				LinearLayout confLay = (LinearLayout) dialog
						.findViewById(R.id.conflay);
				confLay.setTag(gBean);
				LinearLayout textmsglay = (LinearLayout) dialog
						.findViewById(R.id.txtmsglay);
				textmsglay.setTag(gBean);
				LinearLayout photomsglay = (LinearLayout) dialog
						.findViewById(R.id.photomsglay);
				photomsglay.setTag(gBean);
				LinearLayout audiomsglay = (LinearLayout) dialog
						.findViewById(R.id.audiomsglay);
				audiomsglay.setTag(gBean);
				LinearLayout videomsglay = (LinearLayout) dialog
						.findViewById(R.id.videomsglay);
				videomsglay.setTag(gBean);
				LinearLayout handsketchlay = (LinearLayout) dialog
						.findViewById(R.id.handsketchlay);
				handsketchlay.setTag(gBean);
				LinearLayout deleteGroup = (LinearLayout) dialog
						.findViewById(R.id.deletegrouplay);
				deleteGroup.setTag(groupBean);
				LinearLayout groupChatLay = (LinearLayout) dialog
						.findViewById(R.id.groupchat_lay);
				groupChatLay.setTag(gBean);
				LinearLayout groupAccess = (LinearLayout) dialog
						.findViewById(R.id.groupaccesslay);
				groupAccess.setTag(gBean);
				// Log.i("group123", "group owner : " +
				// groupBean.getOwnerName());

				TextView deleteGroupText = (TextView) deleteGroup
						.findViewById(R.id.delete_grp_txt);
				if (groupBean.getOwnerName() != null) {
					if (!groupBean.getOwnerName().equalsIgnoreCase(
							CallDispatcher.LoginUser)) {
						deleteGroupText.setText("Leave Group");
						editGroupText.setText("View Group");
						editOrViewGroup.setImageResource(R.drawable.viewgroup);
					} else {
						chatRow.setWeightSum(4f);
						groupAccess.setVisibility(View.VISIBLE);
						editOrViewGroup.setImageResource(R.drawable.editgroup);
					}
				}
				groupAccess.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) view.getTag();
							Intent settingIntent = new Intent(getActivity()
									.getApplicationContext(),
									GroupChatSettings.class);
							settingIntent.putExtra("groupid",
									gBean.getGroupId());
							startActivity(settingIntent);
							dialog.dismiss();
						} catch (Exception e) {
							// TODO Auto-generated catch block
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

				editGroupLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						GroupBean groupBean = (GroupBean) v.getTag();
						if (groupBean.getOwnerName().equalsIgnoreCase(
								CallDispatcher.LoginUser)) {
							Intent intent = new Intent(getActivity()
									.getApplicationContext(),
									GroupActivity.class);
							intent.putExtra("isEdit", true);
							intent.putExtra("id", groupBean.getGroupId());
							getActivity().startActivity(intent);
						} else {
							Intent intent = new Intent(getActivity()
									.getApplicationContext(), ViewGroups.class);
							intent.putExtra("id", groupBean.getGroupId());
							getActivity().startActivity(intent);
						}
						dialog.dismiss();
					}
				});

				deleteGroup.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (SingleInstance.mainContext
								.isNetworkConnectionAvailable()) {

							try {
								// TODO Auto-generated method stub
								GroupBean groupBean = (GroupBean) v.getTag();
								if (groupBean.getOwnerName().equalsIgnoreCase(
										CallDispatcher.LoginUser))
									deleteGroup(groupBean,
											"Are you sure you want to delete this group ");
								else {
									deleteGroup(groupBean,
											"Are you sure you want to leave this group");
								}
								dialog.dismiss();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);
								e.printStackTrace();
							}
						} else {
							showAlert1("Info",
									"Check internet connection Unable to connect server");

						}
					}
				});

				textmsglay.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(gBean);
								if (gcpBean.getTextMessage().equalsIgnoreCase(
										"1")) {
									if (gBean.getOwnerName() != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
											.length() > 0) {
										shareFiles(
												gBean.getOwnerName()
														+ ","
														+ gBean.getActiveGroupMembers(),
												"note");
									} else {
										if (gBean.getActiveGroupMembers() == null) {
											showToast("members null");
										} else if (gBean
												.getActiveGroupMembers()
												.length() == 0) {
											showToast("Sorry no members to chat");
										}
									}
								} else {
									showToast("Sorry you dont have permission");
								}
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
				photomsglay.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(gBean);
								if (gcpBean.getPhotoMessage().equalsIgnoreCase(
										"1")) {
									if (gBean.getOwnerName() != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
											.length() > 0) {
										shareFiles(
												gBean.getOwnerName()
														+ ","
														+ gBean.getActiveGroupMembers(),
												"photo");
									} else {
										if (gBean.getActiveGroupMembers() == null) {
											showToast("members null");
										} else if (gBean
												.getActiveGroupMembers()
												.length() == 0) {
											showToast("Sorry no members to chat");
										}
									}
								} else {
									showToast("Sorry you dont have permission");
								}
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
				audiomsglay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(gBean);
								if (gcpBean.getAudioMessage().equalsIgnoreCase(
										"1")) {
									if (gBean.getOwnerName() != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
											.length() > 0) {
										shareFiles(
												gBean.getOwnerName()
														+ ","
														+ gBean.getActiveGroupMembers(),
												"audio");
									} else {
										if (gBean.getActiveGroupMembers() == null) {
											showToast("members null");
										} else if (gBean
												.getActiveGroupMembers()
												.length() == 0) {
											showToast("Sorry no members to chat");
										}
									}
								} else {
									showToast("Sorry you dont have permission");
								}
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
				videomsglay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(gBean);
								if (gcpBean.getVideoMessage().equalsIgnoreCase(
										"1")) {
									if (gBean.getOwnerName() != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
											.length() > 0) {
										shareFiles(
												gBean.getOwnerName()
														+ ","
														+ gBean.getActiveGroupMembers(),
												"video");
									} else {
										if (gBean.getActiveGroupMembers() == null) {
											showToast("members null");
										} else if (gBean
												.getActiveGroupMembers()
												.length() == 0) {
											showToast("Sorry no members to chat");
										}
									}
								} else {
									showToast("Sorry you dont have permission");
								}
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

				handsketchlay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(gBean);
								if (gcpBean.getPhotoMessage().equalsIgnoreCase(
										"1")) {

									if (gBean.getOwnerName() != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
											.length() > 0) {
										shareFiles(
												gBean.getOwnerName()
														+ ","
														+ gBean.getActiveGroupMembers(),
												"handsketch");
									} else {
										if (gBean.getActiveGroupMembers() == null) {
											showToast("members null");
										} else if (gBean
												.getActiveGroupMembers()
												.length() == 0) {
											showToast("Sorry no members to chat");
										}
									}
								} else {
									showToast("Sorry you dont have permission");
								}
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

				audioBroadLay.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(gBean);
								if (gcpBean.getAudioBroadcast()
										.equalsIgnoreCase("1")) {
									if (gBean.getOwnerName() != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
											.length() > 0) {
										if (!CallDispatcher.isWifiClosed) {
											String members = getMembers(gBean
													.getOwnerName()
													+ ","
													+ gBean.getActiveGroupMembers());
											// Log.i("group123", "members "
											// + members);
											if (members != null
													&& members.length() > 0) {
												calldisp.requestAudioBroadCast(members);
											}

										} else
											Toast.makeText(
													mainContext,
													"Please check your Internet connection before make call",
													Toast.LENGTH_SHORT).show();
									} else {
										if (gBean.getActiveGroupMembers() == null) {
											showToast("members null");
										} else if (gBean
												.getActiveGroupMembers()
												.length() == 0) {
											showToast("Sorry no members to chat");
										}
									}
								} else {
									showToast("Sorry you dont have permission");
								}
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

				videoBroadLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(gBean);
								if (gcpBean.getVideoBroadcast()
										.equalsIgnoreCase("1")) {
									if (gBean.getOwnerName() != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
											.length() > 0) {
										if (!CallDispatcher.isWifiClosed) {
											String members = getMembers(gBean
													.getOwnerName()
													+ ","
													+ gBean.getActiveGroupMembers());
											// Log.i("group123", "members "
											// + members);
											if (members != null
													&& members.length() > 0) {
												calldisp.requestVideoBroadCast(members);
											}
										}

										else
											Toast.makeText(
													mainContext,
													"Please check your Internet connection before make call",
													Toast.LENGTH_SHORT).show();
									} else {
										if (gBean.getActiveGroupMembers() == null) {
											showToast("members null");
										} else if (gBean
												.getActiveGroupMembers()
												.length() == 0) {
											showToast("Sorry no members to chat");
										}
									}
								} else {
									showToast("Sorry you dont have permission");
								}
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

				confLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupChatPermissionBean gcpBean = SingleInstance.mainContext
										.getGroupChatPermission(gBean);
								if (gcpBean.getAudioConference()
										.equalsIgnoreCase("1")) {
									if (gBean.getOwnerName() != null
											&& gBean.getActiveGroupMembers() != null
											&& gBean.getActiveGroupMembers()
											.length() > 0) {
										if (!CallDispatcher.isWifiClosed) {
											calldisp.requestAudioConference(gBean
													.getOwnerName()
													+ ","
													+ gBean.getActiveGroupMembers());
										} else
											Toast.makeText(
													mainContext,
													"Please check your internet connection before make conference call",
													1).show();
									} else {
										if (gBean.getActiveGroupMembers() == null) {
											showToast("members null");
										} else if (gBean
												.getActiveGroupMembers()
												.length() == 0) {
											showToast("Sorry no members to chat");
										}
									}
								} else {
									showToast("Sorry you dont have permission");
								}
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
				groupChatLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							GroupBean gBean = (GroupBean) v.getTag();
							if (gBean != null) {
								GroupBean groupBean = calldisp.getdbHeler(
										mainContext).getGroupAndMembers(
										"select * from groupdetails where groupid="
												+ gBean.getGroupId());
								if (groupBean != null
										&& groupBean.getActiveGroupMembers() != null
										&& groupBean.getActiveGroupMembers()
										.length() > 0) {
									Intent intent = new Intent(getActivity()
											.getApplicationContext(),
											GroupChatActivity.class);
									intent.putExtra("groupid",
											gBean.getGroupId());
									intent.putExtra("isGroup", true);
									mainContext.startActivity(intent);
									dialog.dismiss();
								} else {
									showToast("Sorry no members to chat");
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				dialog.show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("menu dialog", "Exception :: " + e.getMessage());
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	public void notifyAcceptRejectRequest(final Object response,
										  boolean acceptReject) {

		try {
			ContactsFragment.getContactAdapter().cancelDialog();

			if (response instanceof Servicebean) {
				Servicebean servicebean = (Servicebean) response;
				if (servicebean.getObj() instanceof ArrayList) {
					ArrayList<BuddyInformationBean> bList = (ArrayList<BuddyInformationBean>) servicebean
							.getObj();
					final BuddyInformationBean buddyInformationBean = bList
							.get(0);
					if (buddyInformationBean != null) {
						for (BuddyInformationBean bBean : ContactsFragment
								.getBuddyList()) {
							if (!bBean.isTitle()) {
								if (bBean.getName().equalsIgnoreCase(
										buddyInformationBean.getName())) {
									ContactsFragment.getBuddyList().remove(
											bBean);
									// Log.i("contacts123",
									// "accepted buddy name : "
									// + buddyInformationBean
									// .getName()
									// + " status : "
									// + buddyInformationBean
									// .getStatus()
									// + " occupation : "
									// + buddyInformationBean
									// .getOccupation());
									buddyInformationBean
											.setStatus(ContactsFragment
                                                    .getStatusString(buddyInformationBean
                                                            .getStatus()));
									ContactsFragment.getBuddyList().add(
                                            buddyInformationBean);
									break;
								}
							}
						}
						handler.post(new Runnable() {
							@Override
							public void run() {
								try {
									// TODO Auto-generated method stub
									Toast.makeText(
											mainContext,
											"You Accepted "
													+ buddyInformationBean
													.getName(),
											Toast.LENGTH_LONG).show();
                                    SortList();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				} else if (servicebean.getObj() instanceof WebServiceBean) {
					final WebServiceBean bean = (WebServiceBean) servicebean
							.getObj();
					if (bean != null) {
						if (bean.getResult().equals("1")) {
							for (BuddyInformationBean buddyInformationBean : getBuddyList()) {
								if (!buddyInformationBean.isTitle()) {
									if (buddyInformationBean.getName()
											.equalsIgnoreCase(bean.getText())) {
										synchronized (getBuddyList()) {
											getBuddyList().remove(
													buddyInformationBean);
										}

										break;
									}
								}
							}
							handler.post(new Runnable() {
								@Override
								public void run() {
									try {
										// TODO Auto-generated method stub
										Toast.makeText(
												mainContext,
												"You Rejected "
														+ bean.getText(),
												Toast.LENGTH_LONG).show();
                                        SortList();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								try {
									// TODO Auto-generated method stub
									// Toast.makeText(
									// mainContext,
									// "Unable to process contact request",
									// Toast.LENGTH_SHORT).show();
									showAlert1("Info",
											"Unable to add contact.Try again");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}
			} else if (response instanceof WebServiceBean) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(mainContext,
								((WebServiceBean) response).getText(),
								Toast.LENGTH_LONG).show();

					}
				});

			} else if (response instanceof String) {
				showToast((String) response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getStatusString(String status) {
		String temp = "";
		if (status.equals("0") || status.equals("4")) {
			temp = "Offline";
		} else if (status.equals("1")) {
			temp = "Online";
		} else if (status.equals("2")) {
			temp = "Airport";
		} else if (status.equals("3")) {
			temp = "Away";
		}
		return temp;
	}

	public void notifywebserviceReponseForUtilityItemResponse(Object obj) {
		// Log.i("utility123", "inside success response UI notification");
		try {
			cancelDialog();
			if (obj instanceof UtilityResponse) {
				response = (UtilityResponse) obj;
				if (response.getResult_list().size() > 0) {

				} else {
					showToast("No MenuList for this buddy");
				}
			} else if (obj instanceof WebServiceBean) {
				WebServiceBean service_bean = (WebServiceBean) obj;
				showToast(service_bean.getText());
			} else if (obj instanceof String) {
				showToast((String) obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BuddyInformationBean addHeaderForContactList(String title) {
		BuddyInformationBean bBean = new BuddyInformationBean();
		bBean.setSeparation(title);
		bBean.setTitle(true);
		return bBean;
	}

	public void showsecDialog() {
		try {

			AlertDialog.Builder alert_builder = new AlertDialog.Builder(
					getActivity());
			final CharSequence[] b_type = {"Order", "Feedback", "Service"};

			// alert_builder.setTitle("Select Buddy Info");
			alert_builder.setSingleChoiceItems(b_type, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int pos) {
							// TODO Auto-generated method stub
							String heading = b_type[pos].toString();
							if (pos == 0) {
								showOrderDialog(heading);
							} else if (pos == 1) {
								showOrderDialog(heading);
							} else if (pos == 2) {
								showOrderDialog(heading);
							}
							dialog.dismiss();

						}
					});
			alert_builder.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void showAlert1(String title, String message) {

		try {
			AlertDialog.Builder alertCall = new AlertDialog.Builder(mainContext);
			alertCall
					.setMessage(message)
					.setTitle(title)
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

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	protected void ShowView(final View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
		builder.create();

		builder.setTitle(SingleInstance.mainContext.getResources().getString(
				R.string.change_status));
		final CharSequence[] choiceList = {
				SingleInstance.mainContext.getResources().getString(
						R.string.online),
				SingleInstance.mainContext.getResources().getString(
						R.string.busy),
				SingleInstance.mainContext.getResources().getString(
						R.string.away),
				SingleInstance.mainContext.getResources().getString(
						R.string.stealth) };

		builder.setItems(choiceList, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				changeFieldType(choiceList[which].toString(), v);
				alert.cancel();
			}
		});
		alert = builder.create();
		alert.show();
	}

	private void changeFieldType(String type, View v) {}

	public void showGroupChatDialog(GroupBean groupBean) {
		try {
			// TODO Auto-generated method stub

			if (groupBean != null) {
				GroupBean groupBean1 = calldisp.getdbHeler(mainContext)
						.getGroupAndMembers(
								"select * from groupdetails where groupid="
										+ groupBean.getGroupId());
				if (groupBean1 != null
						&& groupBean1.getActiveGroupMembers() != null
						&& groupBean1.getActiveGroupMembers().length() > 0) {
					Intent intent = new Intent(getActivity()
							.getApplicationContext(), GroupChatActivity.class);
					intent.putExtra("groupid", groupBean.getGroupId());
					intent.putExtra("isGroup", true);
					mainContext.startActivity(intent);
//					dialog.dismiss();
				} else {
					showToast("Sorry no members to chat");
					if (groupBean.getOwnerName().equalsIgnoreCase(
							CallDispatcher.LoginUser)) {
						Intent intent = new Intent(getActivity()
								.getApplicationContext(),
								GroupActivity.class);
						intent.putExtra("isEdit", true);
						intent.putExtra("id", groupBean.getGroupId());
						getActivity().startActivity(intent);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void loadCurrentStatus() {}
	public void showChatScreen(int pos) {

		try {
			// TODO Auto-generated method stub
			selectedposition = pos;
			String state = null;
			if (!isPendingshowing) {
				selectedBuddy = ContactsFragment.contactAdapter.getUser(pos);

				// selectedBuddy = CallDispatcher.adapterToShow
				// .getUser(pos);
				final BuddyInformationBean bIB = (BuddyInformationBean) ContactsFragment.contactAdapter
						.getItem(selectedposition);
				if (!bIB.isTitle()) {
					state = bIB.getStatus();
					if (!state.equalsIgnoreCase("Pending")) {
						permissionBean = calldisp.getdbHeler(mainContext)
								.selectPermissions(
										"select * from setpermission where userid='"
												+ selectedBuddy
												+ "' and buddyid='"
												+ CallDispatcher.LoginUser
												+ "'", selectedBuddy,
										CallDispatcher.LoginUser);
						if (permissionBean.getMMchat().equals("1")) {
							doMultiMMChat(state);
						} else
							ShowError("MM Chat", "Access Denied", mainContext);
					}

					else if (state.equalsIgnoreCase("Pending")) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								doDeleteContact("");
							}
						});

					}

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	private class BGContactLoad extends
			AsyncTask<Servicebean, Servicebean, Servicebean> {
		Servicebean ser = null;

		protected void onPreExecute() {
		}

		@Override
		protected Servicebean doInBackground(Servicebean... params) {
			ser = params[0];
			synchronized (ContactsFragment.getBuddyList()) {
				BackGroundSilent();
				BGGroupLoad();
			}
			return (null);
		}
	}

	void postExecuteSilent(Servicebean ser) {
		// BackGroundSilent();
		//  BGGroupLoad();

	}

	void BackGroundSilent() {
		Log.d("AAA","BackGroundSilent=========");
		Vector<BuddyInformationBean> newlist = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> onlinelist = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> offlinelist = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> airplanelist = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> stealthlist = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> awaylist = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> pendinglist = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> individualList = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> secList = new Vector<BuddyInformationBean>();
		Vector<BuddyInformationBean> tempList = new Vector<BuddyInformationBean>();

		try {


			tempList.addAll(newlist);
			tempList.addAll(onlinelist);
			tempList.addAll(airplanelist);
			tempList.addAll(awaylist);
			tempList.addAll(stealthlist);
			tempList.addAll(offlinelist);
			tempList.addAll(pendinglist);
			for (BuddyInformationBean sortlistbean : ContactsFragment
					.getBuddyList()) {

				status = sortlistbean.getStatus();
				// Log.i("contacts123", "buddy name : " +
				// sortlistbean.getName());
				// Log.i("contacts123",
				// "buddy status : " + sortlistbean.getStatus());

				// Log.d("contact123-check", "Buddy :" + sortlistbean.getName()
				// + " -" + sortlistbean.getStatus());
				String profilePic = "";
				profilePic = DBAccess.getdbHeler().getProfilePic(
						sortlistbean.getName());
				GroupBean gBean = DBAccess.getdbHeler()
						.getAllIndividualChatByBuddyName(
								CallDispatcher.LoginUser,
								sortlistbean.getName());
				if (gBean.getLastMsg() != null
						&& gBean.getLastMsg().equalsIgnoreCase("null")) {
					sortlistbean.setLastMessage(gBean.getLastMsg());
				}
				if (profilePic != null && !profilePic.contains("/COMMedia/")
						&& profilePic.length() > 0) {
					profilePic = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + profilePic;
				}
				if (profilePic != null) {
					sortlistbean.setProfile_picpath(profilePic);
				}
				if (!sortlistbean.isTitle()) {
					if (status.equalsIgnoreCase("new")) {
						newlist.add(sortlistbean);
						Collections.sort(newlist, new BuddyListComparator());
					} else if (status.equalsIgnoreCase("Online")) {
						// Log.d("contact123-check",
						// "Buddy :" + sortlistbean.getName() + " -"
						// + sortlistbean.getStatus());
						onlinelist.add(sortlistbean);
						Collections.sort(onlinelist, new BuddyListComparator());
					} else if (status.equalsIgnoreCase("Offline")
							|| status.equalsIgnoreCase("Stealth")) {
						sortlistbean.setStatus("Offline");
						offlinelist.add(sortlistbean);
						Collections
								.sort(offlinelist, new BuddyListComparator());
					} else if (status.equalsIgnoreCase("Airport")) {
						airplanelist.add(sortlistbean);
						Collections.sort(airplanelist,
								new BuddyListComparator());
					} else if (status.equalsIgnoreCase("Stealth")) {

					} else if (status.equalsIgnoreCase("Away")) {
						awaylist.add(sortlistbean);
						Collections.sort(awaylist, new BuddyListComparator());
					} else if (status.equalsIgnoreCase("Pending")) {
						pendinglist.add(sortlistbean);
						Collections
								.sort(pendinglist, new BuddyListComparator());
					}
				}
			}
			for (BuddyInformationBean bBean : tempList) {

				individualList.add(bBean);

			}
			tempList.clear();
			tempList.addAll(individualList);
			ContactsFragment.getBuddyList().clear();
			ContactsFragment.getBuddyList().addAll(tempList);
			if (ContactsFragment.getContactAdapter() != null) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						try {
							// TODO Auto-generated method stub
							ContactsFragment.getContactAdapter()
									.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO Auto-generated catch block

							e.printStackTrace();
						}
					}
				});

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	void BGGroupLoad() {
		try {


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyProfilePictureDownloaded() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					if (ContactsFragment.getContactAdapter() != null) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								try {
									// TODO Auto-generated method stub
									ContactsFragment.getContactAdapter()
											.notifyDataSetChanged();
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
			}
		});

	}

	public void notifyGetAllProfile(Object obj) {
		try {
			calldisp.cancelDialog();
			if (obj instanceof Vector) {
				Vector<ProfileBean> gcpList = (Vector<ProfileBean>) obj;
				Log.i("AAAA","notifyGetAllProfile "+gcpList.size());
				for(ProfileBean pBean: gcpList) {
					DBAccess.getdbHeler().insertorupdateProfileDetails(pBean);
					if(!(pBean.getPhoto().equals(null) || pBean.getPhoto().equals(""))) {
						String[] param = new String[3];
						param[0] = CallDispatcher.LoginUser;
						param[1] = CallDispatcher.Password;
						param[2] = pBean.getPhoto();
						WebServiceReferences.webServiceClient.FileDownload(param);
					}
				}
			}
			RequestFragment requestFragment = RequestFragment.newInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(

					R.id.activity_main_content_fragment, requestFragment)
					.commitAllowingStateLoss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void addShowHideListener( final Boolean isAudio) {
		if(isAudio) {
			AudioCallScreen audioCallScreen = AudioCallScreen.getInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, audioCallScreen)
					.commitAllowingStateLoss();
		}else {
			VideoCallScreen videoCallScreen = VideoCallScreen.getInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, videoCallScreen)
					.commitAllowingStateLoss();
		}
	}
	private void loadRecents()
	{
		tempnotifylist.clear();
		contactrecentlist.clear();
		grouprecentlist.clear();
		tempnotifylist = DashBoardFragment.newInstance(mainContext).LoadFilesList(CallDispatcher.LoginUser);
		for(NotifyListBean bean:tempnotifylist){
			if(bean.getNotifttype().equalsIgnoreCase("F"))
				contactrecentlist.add(bean);
			else if(bean.getNotifttype().equalsIgnoreCase("C")){
				if(isNumeric(bean.getFileid()))
					grouprecentlist.add(bean);
				else
					contactrecentlist.add(bean);
			}else if(bean.getNotifttype().equalsIgnoreCase("I")) {
				if (bean.getCategory().equalsIgnoreCase("G"))
					grouprecentlist.add(bean);
				else if(bean.getCategory().equalsIgnoreCase("I"))
					contactrecentlist.add(bean);
			}
		}

		for(BuddyInformationBean bean:ContactsFragment.getBuddyList() ){
			if(bean.getStatus().equalsIgnoreCase("new")) {
				NotifyListBean nBean=new NotifyListBean();
				nBean.setUsername(bean.getFirstname()+" "+bean.getLastname());
				nBean.setNotifttype("Invite");
				nBean.setType("contact");
				contactrecentlist.add(nBean);
			}
		}
		for(GroupBean gbean: GroupActivity.groupList){
			if(gbean.getStatus().equalsIgnoreCase("request")){
				NotifyListBean nbean = new NotifyListBean();
				nbean.setUsername(gbean.getOwnerName());
				nbean.setNotifttype("Invite");
				nbean.setType("group");
				grouprecentlist.add(nbean);
			}
		}
	}
	public void deleteContact(ArrayList<String> buddynames){
		showprogress();
		for(String name:buddynames) {
			WebServiceReferences.webServiceClient.deletePeople(
					CallDispatcher.LoginUser, name, contactsFragment);
		}
	}
}
