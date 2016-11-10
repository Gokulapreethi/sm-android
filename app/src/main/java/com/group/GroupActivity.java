package com.group;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lib.model.BuddyInformationBean;
import org.lib.model.FileDetailsBean;
import org.lib.model.GroupBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Fingerprint.MainActivity;
import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.account.PinSecurity;
import com.cg.callservices.MyAbsoluteLayout;
import com.cg.commonclass.GroupListComparator;
import com.cg.hostedconf.AppReference;
import com.cg.quickaction.User;
import com.cg.rounding.RoundingFragment;
import com.cg.snazmed.R;
import com.cg.account.ShareByProfile;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.ExchangesFragment;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;

public class GroupActivity extends Activity implements OnClickListener {
	private Button btn_back, btn_create, btn_delete, btn_addprofile,
			btn_addMemberFromContact, btn_update_member;

	private EditText ed_groupname,ed_groupdesc;

	private LinearLayout ll_addcontact;

	private TextView title;

	private Context context;
	
	private Typeface tf_regular = null;
	
	private Typeface tf_bold = null;

	private LinearLayout lv_buddylist;
	LinearLayout member_lay,member_lay1;

	private BuddyAdapter adapter = null;
	LinearLayout lv_memberList;
	private MembersAdapter memberAdapter=null;

	private CallDispatcher callDisp = null;
	private ImageLoader imageLoader;
	

	// private ArrayList<String> buddylist = null;

	private TextView dateTime = null;

	private TextView memberCount = null,memberAcceptedCount;

	private boolean isEdit = false;

	private GroupBean groupBean = null;

	public Vector<UserBean> membersList = new Vector<UserBean>();

    public Vector<UserBean> membersAcceptedList = new Vector<UserBean>();

	public HashMap<String, GroupBean> buddyInfo = new HashMap<String, GroupBean>();

	public static GroupAdapter groupAdapter;

	public static GroupAdapter2 groupAdapter2;
	
	
	int progressCount = 0;

	private boolean isExchanges = false;
	private boolean isUpdateMembers = false;
	private boolean isModify = false,fromRounding=false;

	Handler handler = new Handler();

	private ProgressDialog progress = null;
	
	public static GroupAdapter1 groupAdapter1;
	private ImageView profile_pic,edit_pic;
	String strIPath;
	String groupid;
	AppMainActivity appMainActivity;
	private LinearLayout Linearlay_info;

	private boolean editgroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.group_new);
		if(SingleInstance.mainContext.getResources()
				.getString(R.string.screenshot).equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.yes))){
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
		}
		WebServiceReferences.contextTable.put("groupactivity", context);

		context = this;
		tf_regular = Typeface.createFromAsset(context.getAssets(),
				getResources().getString(R.string.fontfamily));
        tf_bold = Typeface.createFromAsset(context.getAssets(),
				getResources().getString(R.string.fontfamilybold));


		btn_back = (Button) findViewById(R.id.btn_back);

		btn_create = (Button) findViewById(R.id.save_group);
		btn_create.setOnClickListener(this);

		imageLoader = new ImageLoader(context);
		title = (TextView) findViewById(R.id.tx_heading);
		btn_addMemberFromContact = (Button) findViewById(R.id.btn_addcontact);


		appMainActivity = (AppMainActivity) SingleInstance.contextTable
				.get("MAIN");

		ll_addcontact = (LinearLayout) findViewById(R.id.ly_addcontact);
		ed_groupname = (EditText) findViewById(R.id.ed_creategroup);
		ed_groupdesc= (EditText) findViewById(R.id.ed_gpdesc);
		profile_pic = (ImageView)findViewById(R.id.riv1);
		edit_pic = (ImageView)findViewById(R.id.capture_image_view);
		Linearlay_info = (LinearLayout)findViewById(R.id.tv);
		final TextView tv_gpname=(TextView)findViewById(R.id.tv_gpname);
		final TextView tv_gpdesc=(TextView)findViewById(R.id.tv_gpdesc);
		memberCount = (TextView) findViewById(R.id.members_count);
		lv_buddylist = (LinearLayout) findViewById(R.id.lv_buddylist);
		memberAcceptedCount = (TextView) findViewById(R.id.members_count1);
		lv_memberList = (LinearLayout) findViewById(R.id.lv_memberlist);
		isEdit = getIntent().getBooleanExtra("isEdit", false);
		editgroup=getIntent().getBooleanExtra("editgroup",false);
		member_lay=(LinearLayout)findViewById(R.id.member_lay);
		member_lay1=(LinearLayout)findViewById(R.id.member_lay1);
//
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		adapter = new BuddyAdapter(this, membersList);

		SingleInstance.contextTable.put("groupActivity", context);
		final int adapterCount = adapter.getCount();

		for (int i = 0; i < adapterCount; i++) {
			View item = adapter.getView(i, null, null);
			lv_buddylist.addView(item);
		}


		edit_pic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/MPD_" + getFileName()
						+ ".jpg";
				Intent intent = new Intent(GroupActivity.this, CustomVideoCamera.class);
				intent.putExtra("filePath", strIPath);
				intent.putExtra("isPhoto", true);
				startActivityForResult(intent, 1);
			}
		});
		ed_groupname.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence != null)
					tv_gpname.setVisibility(View.VISIBLE);
				else
					tv_gpname.setVisibility(View.GONE);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
		ed_groupdesc.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if(charSequence!=null)
					tv_gpdesc.setVisibility(View.VISIBLE);
				else
					tv_gpdesc.setVisibility(View.GONE);
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		if (isEdit) {
//			title.setText("EDIT ROUNDING GROUP");
			 groupid = getIntent().getStringExtra("id");
//			Linearlay_info.setVisibility(View.GONE);
			groupBean = callDisp.getdbHeler(context).getGroup(
					"select * from grouplist where groupid=" + groupid);
			memberCount.setVisibility(View.VISIBLE);
			memberAcceptedCount.setVisibility(View.VISIBLE);
			member_lay.setVisibility(View.VISIBLE);
			member_lay1.setVisibility(View.VISIBLE);
			edit_pic.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_edit_photo));
			refreshMembersList();
			if (groupBean != null) {
				Log.d("Test", "$$$$$GroupCreatedDate@@@@@ " + groupBean.getCreatedDate());
				btn_create.setTag(groupBean.getGroupId());
				ed_groupname.setText(groupBean.getGroupName());
				ed_groupname.setTypeface(tf_regular);
				if(groupBean.getGroupdescription()!=null)
					ed_groupdesc.setText(groupBean.getGroupdescription());
				if(groupBean.getGroupIcon()!=null){
					String profilePic=groupBean.getGroupIcon();
					if (profilePic != null && profilePic.length() > 0) {
						edit_pic.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_edit_photo));
						if (!profilePic.contains("COMMedia")) {
							profilePic = Environment.getExternalStorageDirectory()
									+ "/COMMedia/" + profilePic;
							strIPath = profilePic;
						}
						Log.i("AAAA","MYACCOUNT "+profilePic);
						imageLoader.DisplayImage(profilePic, profile_pic,
								R.drawable.icon_buddy_aoffline);
					}
				}

				GroupBean gBean = callDisp.getdbHeler(context)
						.getGroupAndMembers(
								"select * from groupdetails where groupid="
										+ groupBean.getGroupId());


                if (gBean != null) {

                    if (gBean.getInActiveGroupMembers() != null
                            && gBean.getInActiveGroupMembers().length() > 0) {
                        String[] list = (gBean.getInActiveGroupMembers())
                                .split(",");
                        for (String tmp : list) {
                            UserBean userBean = new UserBean();
                            ProfileBean pbean=DBAccess.getdbHeler().getProfileDetails(tmp);
							if(pbean!=null)
								if(pbean.getTitle().equalsIgnoreCase("Dr.") || pbean.getTitle().equalsIgnoreCase("Prof."))
									userBean.setFirstname(pbean.getTitle() +pbean.getFirstname());
								else
									userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                            userBean.setBuddyName(tmp);
							userBean.setProfilePic(pbean.getPhoto());
                            userBean.setSelected(true);
                            membersList.add(userBean);
                        }
                    }
                    membersAcceptedList.add(getSelfUserBean());


                    if (gBean.getInviteMembers() != null
                            && gBean.getInviteMembers().length() > 0) {
                        String[] list = (gBean.getInviteMembers())
                                .split(",");
                        for (String tmp : list) {
                            UserBean userBean = new UserBean();
                            ProfileBean pbean=DBAccess.getdbHeler().getProfileDetails(tmp);
							if(pbean!=null)
								if(pbean.getTitle().equalsIgnoreCase("Dr.") || pbean.getTitle().equalsIgnoreCase("Prof."))
									userBean.setFirstname(pbean.getTitle() +pbean.getFirstname());
								else
									userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                            userBean.setBuddyName(tmp);
							userBean.setProfilePic(pbean.getPhoto());
							if(editgroup) {
								userBean.setInvite(true);
							}
							for(BuddyInformationBean bib:ContactsFragment.getBuddyList()){
								if(bib.getName().equalsIgnoreCase(tmp)) {
									userBean.setStatus(bib.getStatus());
									break;
								}
							}
                            membersAcceptedList.add(userBean);
                        }
                    }
                    if (gBean.getActiveGroupMembers() != null
                            && gBean.getActiveGroupMembers().length() > 0) {
                        String[] list = (gBean.getActiveGroupMembers())
                                .split(",");
                        for (String tmp : list) {
                            UserBean userBean = new UserBean();
                            ProfileBean pbean=DBAccess.getdbHeler().getProfileDetails(tmp);
							if(pbean!=null)
								if(pbean.getTitle().equalsIgnoreCase("Dr.") || pbean.getTitle().equalsIgnoreCase("Prof."))
									userBean.setFirstname(pbean.getTitle() +pbean.getFirstname());
								else
									userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                            userBean.setBuddyName(tmp);
							userBean.setProfilePic(pbean.getPhoto());
                            userBean.setInvite(true);
                            userBean.setGroupid(groupid);
							userBean.setGroupname(groupBean.getGroupName());
                            userBean.setSelected(true);
							for(BuddyInformationBean bib:ContactsFragment.getBuddyList()){
								if(bib.getName().equalsIgnoreCase(tmp)) {
									userBean.setStatus(bib.getStatus());
									break;
								}
							}
                            membersList.add(userBean);
                            for(UserBean res:membersAcceptedList){
                                if(res.getBuddyName().equalsIgnoreCase(tmp)) {
                                    membersList.remove(userBean);
                                    break;
                                }
                            }
                        }
                    }
                }
                memberAdapter=new MembersAdapter(this,R.layout.rounding_member_row,membersAcceptedList);
                final int adapterCount1 = memberAdapter.getCount();

				for (int i = 0; i < adapterCount1; i++) {
					View item = memberAdapter.getView(i, null, null);
					lv_memberList.addView(item);
				}
//				lv_memberList.setAdapter(memberAdapter);
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						memberCount.setText(" (" + String.valueOf(membersList.size()) + ")");
						memberAcceptedCount.setText( " ("
								+ String.valueOf(membersAcceptedList.size()) + ")");
					}
				});
				adapter.notifyDataSetChanged();

			}

		}
		btn_back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String groupName = ed_groupname.getText().toString();

				if (isEdit) {
					if (isModify
							&& isUpdateMembers
							|| !groupName.equalsIgnoreCase(groupBean
									.getGroupName()))
					{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder.setTitle("Info");
						alertDialogBuilder
								.setMessage(
										"You may Loss the data. Are you sure want to Go Back")
								.setCancelable(false)
								.setPositiveButton("Go Back",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												finish();

											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {

                                            }
                                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else
                        finish();
                } else if(groupName.length()>0){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    alertDialogBuilder.setTitle("Info");
                    alertDialogBuilder
                            .setMessage(
                                    "You may Loss the data. Are you sure want to Go Back")
                            .setCancelable(false)
                            .setPositiveButton("Go Back",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int id) {
                                            finish();

                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int id) {

                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else
                    finish();

			}
		});
//		btn_delete.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
//
//					deleteGroup(groupBean);
//				} else {
//					showAlert("Info",
//							"Check internet connection Unable to connect server");
//				}
//			}
//		});

//		btn_delete.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
//
//					deleteGroup(groupBean);
//				} else {
//					showAlert("Info",
//							"Check internet connection Unable to connect server");
//				}
//			}
//		});
//		btn_addprofile.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(context, ShareByProfile.class);
//				intent.putExtra("activity", "note");
//				intent.putExtra("group", true);
//				ArrayList<String> buddylist = new ArrayList<String>();
//				for (UserBean userBean : membersList) {
//					buddylist.add(userBean.getBuddyName());
//				}
//				intent.putStringArrayListExtra("buddylist", buddylist);
//				startActivityForResult(intent, 8);
//			}
//		});
		btn_addMemberFromContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				v.setEnabled(false);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						v.setEnabled(true);

					}
				}, 1000);
				Intent intent = new Intent(getApplicationContext(),
						AddGroupMembers.class);
				ArrayList<String> buddylist = new ArrayList<String>();
				for (UserBean userBean : membersList) {
					buddylist.add(userBean.getBuddyName());
				}
				for(UserBean bean:membersAcceptedList){
					buddylist.add(bean.getBuddyName());
				}
				intent.putStringArrayListExtra("buddylist", buddylist);
				intent.putExtra("fromcall", false);
				intent.putExtra("groupid",groupid);
				startActivityForResult(intent, 3);
			}
		});

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
		if(AppReference.mainContext.isPinEnable) {
			if (AppReference.mainContext.openPinActivity) {
				AppReference.mainContext.openPinActivity=false;
				if(Build.VERSION.SDK_INT>20 && AppReference.mainContext.isTouchIdEnabled) {
					Intent i = new Intent(GroupActivity.this, MainActivity.class);
					startActivity(i);
				}else {
					Intent i = new Intent(GroupActivity.this, PinSecurity.class);
					startActivity(i);
				}
			} else {
				AppReference.mainContext.count=0;
				AppReference.mainContext.registerBroadcastReceiver();
			}
		}
    }
	@Override
	protected void onStop() {
		super.onStop();
		AppReference.mainContext.isApplicationBroughtToBackground();

	}

    // protected String getMembersCount()
    // {
    // return String.valueOf(membersList.size());
    // }
    private UserBean getSelfUserBean() {
        UserBean uBean = new UserBean();
        ProfileBean pbean=DBAccess.getdbHeler().getProfileDetails(groupBean.getOwnerName());
		if(pbean!=null)
			if(pbean.getTitle().equalsIgnoreCase("Dr.") || pbean.getTitle().equalsIgnoreCase("Prof."))
				uBean.setFirstname(pbean.getTitle() +pbean.getFirstname());
			else
				uBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
		uBean.setBuddyName(groupBean.getOwnerName());
		uBean.setProfilePic(pbean.getPhoto());
		String status_1 = appMainActivity.loadCurrentStatus();
		uBean.setStatus(status_1);
        uBean.setBuddyName(groupBean.getOwnerName());
        uBean.setSelected(true);
        return uBean;
    }

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);

			// check if the request code is same as what is passed here it is 2
			memberCount.setVisibility(View.VISIBLE);
			if (requestCode == 3) {
				if (data != null) {
					Bundle bundle = data.getExtras();
					ArrayList<UserBean> list = (ArrayList<UserBean>) bundle
							.get("list");
					HashMap<String, UserBean> membersMap = new HashMap<String, UserBean>();
					for (UserBean userBean : membersList) {
						membersMap.put(userBean.getBuddyName(), userBean);
					}
					for (UserBean userBean : membersAcceptedList) {
						membersMap.put(userBean.getBuddyName(), userBean);
					}
					for (UserBean userBean : list) {
						if (!membersMap.containsKey(userBean.getBuddyName())) {
							userBean.setAllowChecking(false);
							membersList.add(userBean);
						}
					}
					if (data != null) {
						isModify = true;
					}
					refreshMembersList();
				}
			} else if ((requestCode == 8) && (resultCode == -1)) {

				Log.i("IOS", "INSIDE ONACTIVITY RESULT=====>");
				Bundle bun = data.getBundleExtra("share");

				Log.i("123", "---Group chat acti----" + bun);
				if (bun != null) {
					String ss[] = bun.getStringArray("userid");
					HashMap<String, UserBean> membersMap = new HashMap<String, UserBean>();
					for (UserBean userBean : membersList) {
						membersMap.put(userBean.getBuddyName(), userBean);
					}
					for (int i = 0; i < ss.length; i++) {
						Log.i("123", "---i value----" + ss[i]);
						UserBean bean = new UserBean();
						bean.setInvite(true);
						bean.setGroupid(groupid);
						bean.setGroupname(groupBean.getGroupName());
						bean.setBuddyName(ss[i]);
						bean.setSelected(true);
						if (!membersMap.containsKey(bean.getBuddyName())) {
							membersList.add(bean);
						}
						if (bun != null) {
							isModify = true;
						}

					}
					refreshMembersList();
				}

			}
			else if (requestCode == 1) {

				File new_file = new File(strIPath);
				if(new_file.exists()) {
					ImageLoader imageLoader;
					imageLoader = new ImageLoader(GroupActivity.this);
					imageLoader.DisplayImage(strIPath, profile_pic, R.drawable.userphoto);
					String[] param=new String[7];
					param[0]=CallDispatcher.LoginUser;
					param[1]=CallDispatcher.Password;
					param[2]="image";
					File file=new File(strIPath);
					param[3]=file.getName();
					long length = (int) file.length();
					length = length/1024;
					param[5]="other";
					param[6]= String.valueOf(length);
					if(file.exists()) {
						param[4] = encodeTobase64(BitmapFactory.decodeFile(file.getPath()));
						WebServiceReferences.webServiceClient.FileUpload(param,GroupActivity.this,"");
						FileDetailsBean fBean=new FileDetailsBean();
						fBean.setFilename(param[3]);
						fBean.setFiletype("image");
						fBean.setFilecontent(param[4]);
						fBean.setServicetype("Upload");
						SingleInstance.fileDetailsBean=fBean;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyCreateGroup(Object obj) {
		if (obj instanceof GroupBean) {
			isEdit = true;
			final GroupBean groupBean = (GroupBean) obj;

			groupBean.setUserName(CallDispatcher.LoginUser);
			groupBean.setOwnerName(CallDispatcher.LoginUser);
			memberCount.setVisibility(View.VISIBLE);

			if (DBAccess.getdbHeler().saveOrUpdateGroup(groupBean) > 0) {

				if (groupList != null) {
					GroupBean gBean = new GroupBean();

//					groupList.clear();
					groupList.add(gBean);
				}
				/**
				 * To notify exchanges list after group creation
				 */
				for (GroupBean gBean : groupList) {
					if (gBean.getGroupId().equals(groupBean.getGroupId())) {
						gBean.setGroupName(groupBean.getGroupName());
						break;
					}
				}

				Vector<GroupBean> gList = DBAccess.getdbHeler(context)
						.getAllGroups(CallDispatcher.LoginUser,"group");
				if (WebServiceReferences.contextTable.containsKey("exchanges")) {
					final ExchangesFragment exchanges = ExchangesFragment
							.newInstance(context);
					exchanges.exchangesList.clear();
					if (gList != null && gList.size() > 0) {
						exchanges.exchangesList.addAll(gList);
					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							exchanges.adapter.notifyDataSetChanged();
						}
					});

				}
				/**
				 * Ends here
				 */


				this.groupBean = groupBean;
				membersList.clear();
				membersAcceptedList.clear();


				membersAcceptedList.add(getSelfUserBean());
				if (groupBean.getInviteMembers() != null
						&& groupBean.getInviteMembers().length() > 0) {
					String[] list = (groupBean.getInviteMembers())
							.split(",");
					for (String tmp : list) {
						UserBean userBean = new UserBean();
						userBean.setBuddyName(tmp);
						membersAcceptedList.add(userBean);
					}
				}
				if (groupBean.getActiveGroupMembers() != null
						&& groupBean.getActiveGroupMembers().length() > 0) {
					memberCount.setVisibility(View.VISIBLE);
					String[] activeList = groupBean.getActiveGroupMembers()
							.split(",");
					for (String tmp : activeList) {
						UserBean uBean = new UserBean();
						uBean.setBuddyName(tmp);
						uBean.setInvite(true);
						uBean.setGroupid(groupid);
						uBean.setGroupname(groupBean.getGroupName());
						uBean.setSelected(true);
						membersList.add(uBean);
						for(UserBean res:membersAcceptedList){
							if(res.getBuddyName().equalsIgnoreCase(tmp)) {
								membersList.remove(uBean);
								break;
							}
						}
					}
				}

				if (groupBean.getInActiveGroupMembers() != null
						&& groupBean.getInActiveGroupMembers().length() > 0) {
					String[] inActiveList = groupBean.getInActiveGroupMembers()
							.split(",");
					for (String tmp : inActiveList) {
						UserBean userBean = new UserBean();
						userBean.setBuddyName(tmp);
						userBean.setSelected(true);
						membersList.add(userBean);
					}
				}
				callDisp.getdbHeler(context).insertorUpdateGroupMembers(
						groupBean);
				refreshMembersList();
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						GroupActivity.groupAdapter.notifyDataSetChanged();
//						GroupActivity.groupAdapter2.notifyDataSetChanged();
//						ll_addcontact.setVisibility(View.VISIBLE);
//						btn_delete.setVisibility(View.VISIBLE);
						// btn_create.setText(SingleInstance.mainContext
						// .getResources()
						// .getString(R.string.modify_group));
//						dateTime.setText(groupBean.getModifiedDate());
//						dateTime.setVisibility(View.VISIBLE);
					}
				});
				ContactsFragment.getInstance(context).getList();

				handler.post(new Runnable() {
					@Override
					public void run() {

						showToast(SingleInstance.mainContext
								.getString(R.string.group_created_success));
						adapter.notifyDataSetChanged();
					}
				});

			}
			cancelDialog();
			finish();
		} else if (obj instanceof String) {
			showToast((String) obj);

		// callDisp.cancelDialog();
		cancelDialog();
		} else {
			cancelDialog();
			showAlert(
					SingleInstance.mainContext.getResources().getString(
							R.string.response_group),
					((WebServiceBean) obj).getText());
		}
	}
	private void showAlert(final String title, final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					AlertDialog confirmation = new AlertDialog.Builder(context)
							.create();
					confirmation.setTitle(SingleInstance.mainContext
							.getResources().getString(R.string.response_group));
					confirmation.setMessage(message);
					confirmation.setCancelable(true);
					confirmation.setButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});

					confirmation.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	public void notifyGroupModify(Object obj) {
		try {
			if (obj instanceof GroupBean) {
				final GroupBean groupBean = (GroupBean) obj;
				groupBean.setOwnerName(CallDispatcher.LoginUser);
				groupBean.setUserName(CallDispatcher.LoginUser);
				callDisp.getdbHeler(context).saveOrUpdateGroup(groupBean);

				for (GroupBean gBean : groupList) {
					if (gBean.getGroupId().equals(groupBean.getGroupId())) {
						gBean.setGroupName(groupBean.getGroupName());
						break;
					}
				}

				this.groupBean = groupBean;
				handler.post(new Runnable() {
					@Override
					public void run() {

						try {
//							dateTime.setText(groupBean.getCreatedDate());
							GroupActivity.groupAdapter.notifyDataSetChanged();
//							GroupActivity.groupAdapter2.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				membersList.clear();
				membersAcceptedList.clear();
				membersAcceptedList.add(getSelfUserBean());

				if (groupBean.getActiveGroupMembers() != null
						&& groupBean.getActiveGroupMembers().length() > 0) {
					String[] activeList = groupBean.getActiveGroupMembers()
							.split(",");
					for (String tmp : activeList) {
						UserBean uBean = new UserBean();
						uBean.setBuddyName(tmp);
						uBean.setSelected(true);
						membersList.add(uBean);
					}
				}

				if (groupBean.getInActiveGroupMembers() != null
						&& groupBean.getInActiveGroupMembers().length() > 0) {
					String[] inActiveList = groupBean.getInActiveGroupMembers()
							.split(",");
					for (String tmp : inActiveList) {
						UserBean userBean = new UserBean();
						userBean.setBuddyName(tmp);
						userBean.setSelected(true);
						membersList.add(userBean);
					}
				}
				callDisp.getdbHeler(context).insertorUpdateGroupMembers(
						groupBean);
				refreshMembersList();
				if (groupBean.getResult().equalsIgnoreCase(
						"Successfully Group Modified")) {
					showToast("Group Modified Successfully");
				} else {
					showToast(groupBean.getResult());
				}
			} else if (obj instanceof String) {
				showToast((String) obj);
			} else {
				callDisp.showAlert(SingleInstance.mainContext.getResources()
						.getString(R.string.response_group),
						((WebServiceBean) obj).getText());
			}
			// callDisp.cancelDialog();
			ContactsFragment.getInstance(context).getList();
			cancelDialog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyDeleteGroup(Object obj) {
		if (obj instanceof GroupBean) {
			String groupId = ((GroupBean) obj).getGroupId();
			if (groupId != null) {
				callDisp.getdbHeler(context).deleteGroupAndMembers(groupId);
				for (GroupBean gBean : groupList) {
					if (gBean.getGroupId().equals(groupId)) {
						groupList.remove(gBean);
						handler.post(new Runnable() {

							@Override
							public void run() {
								GroupActivity.groupAdapter
										.notifyDataSetChanged();
//								GroupActivity.groupAdapter2.notifyDataSetChanged();
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
		// callDisp.cancelDialog();

		cancelDialog();
	}

	private void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, message, 1).show();
			}
		});

	}

	private void deleteGroup(final GroupBean groupManagementBean) {
		if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {

			if (CallDispatcher.isConnected) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Warning !");
				builder.setMessage(
						"Are you sure you want to delete this group "
								+ groupManagementBean.getGroupName() + " ?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										if (!WebServiceReferences.running) {
											callDisp.startWebService(
													getResources()
															.getString(
																	R.string.service_url),
													"80");
										}
										showprogress();
										String groupId = groupManagementBean
												.getGroupId();
										WebServiceReferences.webServiceClient
												.deleteGroup(
														CallDispatcher.LoginUser,
														groupId);
										Toast.makeText(getApplicationContext(),
												"Successfully Group deleted",
												Toast.LENGTH_SHORT).show();
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
				callDisp.ShowError("Info",
						"Check internet connection Unable to connect server",
						context);
			}
		} else {
			showAlert("Info",
					"Check internet connection Unable to connect server");
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("creategroup"))
			WebServiceReferences.contextTable.remove("creategroup");
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		String groupName = ed_groupname.getText().toString();
		switch (view.getId()) {

		case R.id.save_group:
			if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {

				if (groupName.length() > 0 && groupName.length() <= 20
						&& isGroupNameValidate(groupName) && membersList.size()>0) {
					hideKeyboard();
					// callDisp.showprogress(CallDispatcher.pdialog, context);
					groupBean=new GroupBean();
					String groupname = ed_groupname.getText().toString();
					groupBean.setOwnerName(CallDispatcher.LoginUser);
					groupBean.setGroupName(groupname);
					groupBean.setGroupdescription(ed_groupdesc.getText().toString());
					if (strIPath != null) {
						File f = new File(strIPath);
						if (f.exists())
							groupBean.setGroupIcon(f.getName());
					}
						showprogress();

						modifyGroupWebService();
						
						
//					}
				} else {
					if (!isGroupNameValidate(groupName)) {
						showToast("Special Characters -/. Are only allowed");
					} else if (groupName.length() > 20) {
						showToast("Groupname must be 1-15 characters");
					} else if(membersAcceptedList.size()==0){
						showToast("Please select group members");
					}else {
						Toast.makeText(getApplicationContext(),
								"Please Enter The Group Name", 1).show();
					}
				}

			} else {
				showAlert("Info",
						"Check internet connection Unable to connect server");
			}
		}
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ed_groupname.getWindowToken(), 0);

		// imm.showSoftInput(ed, 0);
	}

	private void modifyGroupWebService() {
		try {
			if (groupBean != null) {
				if(groupid!=null)
				groupBean.setGroupId(groupid);
				groupBean.setGroupStatus("1");
				if (membersList.size() != 0) {
					String deleteMembers = "";
					String addMembers = "";
					for (UserBean userBean : membersList) {
						if (!userBean.getBuddyName().equalsIgnoreCase(
								CallDispatcher.LoginUser)) {
							if (userBean.isSelected()) {
								addMembers = addMembers
										+ userBean.getBuddyName() + ",";
								Log.d("Test", "GroupMembersAdd@@@ "
										+ addMembers + " Length### "
										+ addMembers.length());
							} else
								deleteMembers = deleteMembers
										+ userBean.getBuddyName() + ",";
							Log.d("Test", "GroupMembersDelete@@@ "
									+ deleteMembers + " Length### "
									+ deleteMembers.length());

						}
					}
					if (addMembers.length() > 0)
						groupBean.setGroupMembers(addMembers.substring(0,
								addMembers.length() - 1));
					if (deleteMembers.length() > 0)
						groupBean.setDeleteGroupMembers(deleteMembers
								.substring(0, deleteMembers.length() - 1));
				}else{
					groupBean.setGroupMembers("");
					groupBean.setDeleteGroupMembers("");
				}
				groupBean.setCallback(this);
					WebServiceReferences.webServiceClient.createGroup(groupBean, this);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Vector<GroupBean> groupList;

	public static Vector<GroupBean> getAllGroups() {
		
		loadNewGroupsFromDB();
		groupList=getGroupList(groupList);
		return groupList;
	}

	public static Vector<GroupBean> loadNewGroupsFromDB() {
		try {
			groupList = new Vector<GroupBean>();
			Vector<GroupBean> gList = DBAccess.getdbHeler()
					.getAllGroups(CallDispatcher.LoginUser,"group");

			if (gList != null && gList.size() > 0) {

				groupList.clear();
				groupList.addAll(gList);
			}
			return groupList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupList;

	}
	public static Vector<GroupBean> getGroupList(Vector<GroupBean> groupList){
		Vector<GroupBean> tempList = new Vector<GroupBean>();
		Vector<GroupBean> requestList = new Vector<GroupBean>();
		Vector<GroupBean> acceptedList = new Vector<GroupBean>();
		for(GroupBean bean:groupList) {
			GroupBean gBean = DBAccess.getdbHeler() .getGroupAndMembers(
					"select * from groupdetails where groupid=" + bean.getGroupId());
			if (!gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)) {
				if (gBean.getInviteMembers() != null) {
					if(!gBean.getInviteMembers().contains(CallDispatcher.LoginUser)){
						bean.setStatus("request");
						requestList.add(bean);
					}else {
						bean.setStatus("accepted");
						acceptedList.add(bean);
					}
				} else {
					bean.setStatus("request");
					requestList.add(bean);
				}
			}else {
				bean.setStatus("accepted");
				acceptedList.add(bean);
			}
		}
		tempList.addAll(requestList);
		Collections.sort(acceptedList, new GroupListComparator());
		tempList.addAll(acceptedList);
		return tempList;

	}

	private boolean isGroupNameValidate(String groupName) {
		String REGEX = "^[a-zA-Z0-9_ ]*$";
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(groupName);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void refreshMembersList() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(membersList.size()>0)
					member_lay.setVisibility(View.VISIBLE);
				if(membersAcceptedList.size()>0)
				member_lay1.setVisibility(View.VISIBLE);
				memberCount.setText(" ("
						+ String.valueOf(membersList.size()) + ")");
				memberAcceptedCount.setText(" ("
						+ String.valueOf(membersAcceptedList.size()) + ")");
				lv_buddylist.removeAllViews();
				adapter = new BuddyAdapter(GroupActivity.this, membersList);
				final int adapterCount = adapter.getCount();

				for (int i = 0; i < adapterCount; i++) {
					View item = adapter.getView(i, null, null);
					lv_buddylist.addView(item);
			}
				adapter.notifyDataSetChanged();
				if(memberAdapter!=null)
				memberAdapter.notifyDataSetChanged();
			}
		});

	}

	public void showprogress() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					progress = new ProgressDialog(context);
					if (progress != null) {
						progress.setCancelable(false);
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

	private boolean isWifi() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);

		// For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		// For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();

		System.out.println(is3g + " net " + isWifi);

		if (!is3g && !isWifi) {
			return false;
			// Toast.makeText(getApplicationContext(),"Please make sure your Network Connection is ON ",Toast.LENGTH_LONG).show();
		} else {
			return true;
			// " Your method what you want to do "
		}
	}

	public void cancelDialog() {
		try {
			if (progress != null && progress.isShowing()) {
				Log.i("register", "--progress bar end-----");
				progress.dismiss();
				progress = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}
	public String getFileName() {
		String strFilename = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date date = new Date();
			strFilename = dateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strFilename;
	}
	public class MembersAdapter extends ArrayAdapter<UserBean> {

		private LayoutInflater inflater = null;
		private ViewHolder holder;
		private ImageLoader imageLoader;
		private Vector<UserBean> result;

		public MembersAdapter(Context context, int resource, Vector<UserBean> objects) {
			super(context, resource, objects);
			imageLoader=new ImageLoader(context);
			result = new Vector<UserBean>();
			result.addAll(objects);
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			try {
				holder = new ViewHolder();
				if(convertView == null) {
					inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.find_people_item, null);
					holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
					holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
					holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
					holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
					holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
					holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
					holder.ll_cancel=(LinearLayout) convertView.findViewById(R.id.cancel_lay);
					convertView.setTag(holder);
				}else
					holder = (ViewHolder) convertView.getTag();
				final UserBean bib = result.get(i);
				if(bib!=null) {
                    if (bib.getProfilePic() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" + bib.getProfilePic();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
					holder.header_title.setVisibility(View.GONE);
					holder.selectUser.setVisibility(View.GONE);
					if(bib.getStatus()!=null) {
						Log.i("AAAA","Buddy adapter status "+bib.getStatus());
						if (bib.getStatus().equalsIgnoreCase("offline") || bib.getStatus().equalsIgnoreCase("stealth")) {
							holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
						} else if (bib.getStatus().equalsIgnoreCase("online")) {
							holder.statusIcon.setBackgroundResource(R.drawable.online_icon);
						} else if (bib.getStatus().equalsIgnoreCase("busy")|| bib.getStatus().equalsIgnoreCase("airport")) {
							holder.statusIcon.setBackgroundResource(R.drawable.busy_icon);
						} else if (bib.getStatus().equalsIgnoreCase("away")) {
							holder.statusIcon.setBackgroundResource(R.drawable.invisibleicon);
						} else {
							holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
						}
					}
					holder.buddyName.setText(bib.getFirstname());

					if(bib.isSelected()) {
						holder.occupation.setText("Owner");
						holder.occupation.setTextColor(getResources().getColor(R.color.green));
					}else {
						holder.occupation.setText("Prof.Designation");
						if(bib.getOccupation()!=null)
							holder.occupation.setText(bib.getOccupation());
						holder.occupation.setTextColor(getResources().getColor(R.color.snazlgray));
					}

					if(bib.getInvite()){
						holder.ll_cancel.setVisibility(View.VISIBLE);
					}else {
						holder.ll_cancel.setVisibility(View.GONE);
					}
                    holder.ll_cancel.setTag(bib.getBuddyName());
					holder.ll_cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
                             editMembers(view);
						}
					});

				}
			}catch(Exception e){
				e.printStackTrace();
				Log.d("RRRR","Error FindpeopleAdapter.java => "+e.toString());
			}
			return convertView;
		}
	}
	public static class ViewHolder {
		CheckBox selectUser;
		ImageView buddyicon;
		ImageView statusIcon;
		TextView buddyName;
		TextView occupation;
		TextView header_title;
		LinearLayout ll_cancel;
	}


	public void editMembers(View view){

				Vector<UserBean> tempList=(Vector<UserBean>)membersAcceptedList.clone();
		        Log.i("removemember","buddyy remove--->"+view.getTag().toString());
				for(int i=0;i<tempList.size();i++){
					UserBean bean=membersAcceptedList.get(i);
					if(bean.getBuddyName().equalsIgnoreCase(view.getTag().toString())) {
						membersAcceptedList.remove(bean);
						break;
					}
				}
		memberAdapter=new MembersAdapter(this,R.layout.rounding_member_row,membersAcceptedList);
		final int adapterCount1 = memberAdapter.getCount();
		lv_memberList.removeAllViews();

		for (int i = 0; i < adapterCount1; i++) {
			View item = memberAdapter.getView(i, null, null);
			lv_memberList.addView(item);
		}

		memberAcceptedCount.setText( " ("
				+ String.valueOf(membersAcceptedList.size()) + ")");


			Vector<UserBean> getdeleteAndAddMembers=(Vector<UserBean>)membersList.clone();
//			getdeleteAndAddMembers.addAll(membersList);
//			membersList.clear();
		Vector<UserBean> deleteMembers=(Vector<UserBean>)membersAcceptedList.clone();
//		for(UserBean bean:deleteMembers){
//			bean.setSelected(true);
//			membersList.add(bean);
//		}
//		UserBean bean=new UserBean();
//		bean.setSelected(false);
//		bean.setBuddyName(view.getTag().toString());
//		membersList.add(bean);
//		    membersList.addAll(getdeleteAndAddMembers);
		membersList.clear();

		HashMap<String,UserBean> beanHashMap=new HashMap<>();
		for(UserBean bean:getdeleteAndAddMembers){
			beanHashMap.put(bean.getBuddyName(),bean);
		}
		for(UserBean bean:deleteMembers){
			bean.setSelected(true);
			beanHashMap.put(bean.getBuddyName(),bean);
		}
		UserBean bean=new UserBean();
		bean.setSelected(false);
		bean.setBuddyName(view.getTag().toString());
		beanHashMap.put(bean.getBuddyName(),bean);
		Iterator iterator1 = beanHashMap.entrySet()
				.iterator();

		while (iterator1.hasNext()) {

			Map.Entry mapEntry = (Map.Entry) iterator1.next();

			UserBean userBean = (UserBean) mapEntry.getValue();
			Log.i("removemember","buddyyName--->"+userBean.getBuddyName());
			Log.i("removemember","buddyySelected--->"+userBean.isSelected());
			membersList.add(userBean);
		}


	}
}
