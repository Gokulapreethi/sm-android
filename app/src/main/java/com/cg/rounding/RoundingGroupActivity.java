package com.cg.rounding;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.BuddyListComparator;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.quickaction.User;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.group.BuddyAdapter;
import com.group.GroupAdapter;
import com.group.GroupAdapter1;
import com.group.GroupAdapter2;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.ExchangesFragment;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.FileDetailsBean;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.WebServiceBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoundingGroupActivity extends Activity implements View.OnClickListener {
    private Button btn_back, btn_create, btn_delete, btn_addprofile,
            btn_addMemberFromContact, btn_update_member;

    private EditText ed_groupname,ed_groupdesc;

    private LinearLayout ll_addcontact;

    private TextView title;

    private Context context;

    private Typeface tf_regular = null;

    private Typeface tf_bold = null;

    private LinearLayout lv_buddylist;

    private BuddyAdapter adapter = null;
    LinearLayout lv_memberList;
    public static MembersAdapter memberAdapter=null;

    private CallDispatcher callDisp = null;
    private ImageLoader imageLoader;
    public String sorting = "online";


    // private ArrayList<String> buddylist = null;

    private TextView dateTime = null;

    private TextView memberCount = null,memberAcceptedCount;

    private boolean isEdit = false;

    private GroupBean groupBean = null;

    public Vector<UserBean> membersList = new Vector<UserBean>();

    public Vector<UserBean> membersAcceptedList = new Vector<UserBean>();

    public HashMap<String, GroupBean> buddyInfo = new HashMap<String, GroupBean>();

    int progressCount = 0;

    private boolean isExchanges = false;
    private boolean isUpdateMembers = false;
    private boolean isModify = false,fromRounding=false;

    Handler handler = new Handler();
    LinearLayout member_lay,member_lay1,sort;

    private ProgressDialog progress = null;
    private ImageView profile_pic,edit_pic;
    String strIPath;
    String groupid;
    GroupBean gBean;
    AppMainActivity appMainActivity;

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

        ll_addcontact = (LinearLayout) findViewById(R.id.ly_addcontact);
        ed_groupname = (EditText) findViewById(R.id.ed_creategroup);
        ed_groupdesc= (EditText) findViewById(R.id.ed_gpdesc);
        profile_pic = (ImageView)findViewById(R.id.riv1);
        edit_pic = (ImageView)findViewById(R.id.capture_image_view);
        final TextView tv_gpname=(TextView)findViewById(R.id.tv_gpname);
        final TextView tv_gpdesc=(TextView)findViewById(R.id.tv_gpdesc);
        final LinearLayout tv_text=(LinearLayout)findViewById(R.id.tv);
        memberCount = (TextView) findViewById(R.id.members_count);
        lv_buddylist = (LinearLayout) findViewById(R.id.lv_buddylist);
        memberAcceptedCount = (TextView) findViewById(R.id.members_count1);
        lv_memberList = (LinearLayout) findViewById(R.id.lv_memberlist);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        fromRounding=getIntent().getBooleanExtra("fromRounding",false);
        SingleInstance.contextTable.put("roundingGroup", context);
        member_lay=(LinearLayout)findViewById(R.id.member_lay);
        member_lay1=(LinearLayout)findViewById(R.id.member_lay1);
        sort=(LinearLayout)findViewById(R.id.sort);
        final Button online=(Button)findViewById(R.id.online_sort);
        final Button alpha=(Button)findViewById(R.id.alpha_sort);
        final Button role=(Button)findViewById(R.id.role_sort);

        if (WebServiceReferences.callDispatch.containsKey("calldisp"))
            callDisp = (CallDispatcher) WebServiceReferences.callDispatch
                    .get("calldisp");
        else
            callDisp = new CallDispatcher(context);

        adapter = new BuddyAdapter(this, membersList);
        final int adapterCount = adapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            lv_buddylist.addView(item);
        }
        appMainActivity = (AppMainActivity) SingleInstance.contextTable
                .get("MAIN");

        edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strIPath = Environment.getExternalStorageDirectory()
                        + "/COMMedia/MPD_" + getFileName()
                        + ".jpg";
                Intent intent = new Intent(RoundingGroupActivity.this, CustomVideoCamera.class);
                intent.putExtra("filePath", strIPath);
                intent.putExtra("isPhoto", true);
                startActivityForResult(intent, 1);
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                online.setTextColor(getResources().getColor(R.color.white));
                alpha.setTextColor(getResources().getColor(R.color.snazlgray));
                role.setTextColor(getResources().getColor(R.color.snazlgray));
                sorting = "online";
                Vector<UserBean> buddylist=new Vector<UserBean>();
                buddylist=getOnlineList(membersAcceptedList);
                memberAdapter = new MembersAdapter(context,R.layout.rounding_member_row,buddylist);
                lv_memberList.removeAllViews();
                final int adapterCount = memberAdapter.getCount();
                for (int i = 0; i < adapterCount; i++) {
                    View item = memberAdapter.getView(i, null, null);
                    lv_memberList.addView(item);
                }
                memberAdapter.notifyDataSetChanged();
            }
        });
        alpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alpha.setTextColor(getResources().getColor(R.color.white));
                online.setTextColor(getResources().getColor(R.color.snazlgray));
                role.setTextColor(getResources().getColor(R.color.snazlgray));
                sorting = "alpha";
                Collections.sort(membersAcceptedList, new BuddiesListComparator());
                memberAdapter = new MembersAdapter(context,R.layout.rounding_member_row,membersAcceptedList);
                lv_memberList.removeAllViews();
                final int adapterCount = memberAdapter.getCount();
                for (int i = 0; i < adapterCount; i++) {
                    View item = memberAdapter.getView(i, null, null);
                    lv_memberList.addView(item);
                }
                memberAdapter.notifyDataSetChanged();
            }
        });
        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alpha.setTextColor(getResources().getColor(R.color.snazlgray));
                online.setTextColor(getResources().getColor(R.color.snazlgray));
                role.setTextColor(getResources().getColor(R.color.white));
                sorting = "role";
                Vector<UserBean> templist=new Vector<UserBean>();
                for(UserBean bib:membersAcceptedList){
                    if(bib.getRole()!=null && !bib.getRole().equalsIgnoreCase(""))
                        templist.add(bib);
                }
                memberAdapter = new MembersAdapter(context,R.layout.rounding_member_row,templist);
                lv_memberList.removeAllViews();
                final int adapterCount = memberAdapter.getCount();
                for (int i = 0; i < adapterCount; i++) {
                    View item = memberAdapter.getView(i, null, null);
                    lv_memberList.addView(item);
                }
                memberAdapter.notifyDataSetChanged();
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
            title.setText("EDIT ROUNDING GROUP");
            tv_text.setVisibility(View.GONE);
            groupid = getIntent().getStringExtra("id");
            groupBean = callDisp.getdbHeler(context).getGroup(
                    "select * from grouplist where groupid=" + groupid);
            sort.setVisibility(View.VISIBLE);
            memberAcceptedCount.setVisibility(View.VISIBLE);
            member_lay.setVisibility(View.VISIBLE);
            member_lay1.setVisibility(View.VISIBLE);
            refreshMembersList();
            if (groupBean != null) {Log.d("Test",
                    "$$$$$GroupCreatedDate@@@@@ " + groupBean.getCreatedDate());
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
                            profilePic = Environment
                                    .getExternalStorageDirectory()
                                    + "/COMMedia/" + profilePic;
                            strIPath = profilePic;
                        }
                        Log.i("AAAA","MYACCOUNT "+profilePic);
                        imageLoader.DisplayImage(profilePic, profile_pic,
                                R.drawable.icon_buddy_aoffline);
                    }
                }

                gBean = callDisp.getdbHeler(context)
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
                                if(pbean.getTitle()!=null &&pbean.getTitle().equalsIgnoreCase("Dr.") || pbean.getTitle().equalsIgnoreCase("Prof."))
                                    userBean.setFirstname(pbean.getTitle() +pbean.getFirstname());
                                else
                                    userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                            userBean.setProfilePic(pbean.getPhoto());
                            userBean.setBuddyName(tmp);
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
                                if(pbean.getTitle()!=null &&pbean.getTitle().equalsIgnoreCase("Dr.")
                                        || pbean.getTitle().equalsIgnoreCase("Prof."))
                                    userBean.setFirstname(pbean.getTitle() +pbean.getFirstname());
                                else
                                    userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                            userBean.setBuddyName(tmp);
                            userBean.setProfilePic(pbean.getPhoto());
                            GroupMemberBean bean=DBAccess.getdbHeler().getMemberDetails(groupid,tmp);
                            userBean.setAdmin(bean.getAdmin());
                            userBean.setRole((bean.getRole()));
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
                                if(pbean.getTitle()!=null &&pbean.getTitle().equalsIgnoreCase("Dr.") || pbean.getTitle().equalsIgnoreCase("Prof."))
                                    userBean.setFirstname(pbean.getTitle() +pbean.getFirstname());
                                else
                                    userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                            userBean.setBuddyName(tmp);
                            userBean.setProfilePic(pbean.getPhoto());
                            userBean.setInvite(true);
                            userBean.setGroupid(groupid);
                            for(BuddyInformationBean bib:ContactsFragment.getBuddyList()){
                                if(bib.getName().equalsIgnoreCase(tmp)) {
                                    userBean.setStatus(bib.getStatus());
                                    break;
                                }
                            }
                            userBean.setGroupname(groupBean.getGroupName());
                            userBean.setSelected(true);
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
                        memberCount.setText( " (" + String.valueOf(membersList.size()) + ")");
                        memberAcceptedCount.setText( " ("
                                + String.valueOf(membersAcceptedList.size()) + ")");
                    }
                });
                adapter.notifyDataSetChanged();

            }

        }
        btn_back.setOnClickListener(new View.OnClickListener() {

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
        btn_addMemberFromContact.setOnClickListener(new View.OnClickListener() {

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
                intent.putExtra("invite", true);
                intent.putExtra("groupid",groupid);
                startActivityForResult(intent, 3);
            }
        });

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
                    String[] invitelist = (gBean.getInviteMembers()).split(",");
                    for (String temp : invitelist) {
                        if (!temp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
                            bean.setStatus("request");
                            requestList.add(bean);
                        } else {
                            bean.setStatus("accepted");
                            acceptedList.add(bean);
                            break;
                        }
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
        tempList.addAll(acceptedList);
        return tempList;

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    // protected String getMembersCount()
    // {
    // return String.valueOf(membersList.size());
    // }
    private UserBean getSelfUserBean() {
        UserBean uBean = new UserBean();
        ProfileBean pbean=DBAccess.getdbHeler().getProfileDetails(groupBean.getOwnerName());
        if(pbean!=null && pbean.getTitle()!=null)
            if(pbean.getTitle().equalsIgnoreCase("Dr.") || pbean.getTitle().equalsIgnoreCase("Prof."))
                uBean.setFirstname(pbean.getTitle() +pbean.getFirstname());
            else
                uBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
        uBean.setBuddyName(groupBean.getOwnerName());
        uBean.setProfilePic(pbean.getPhoto());
        String status_1 = appMainActivity.loadCurrentStatus();
        uBean.setStatus(status_1);
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
                            userBean.setInvite(true);
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
                    imageLoader = new ImageLoader(RoundingGroupActivity.this);
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
                        WebServiceReferences.webServiceClient.FileUpload(param,RoundingGroupActivity.this);
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

    private void showToast(final String message) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(context, message, 1).show();
            }
        });

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

                    if (groupName.length() > 0 && groupName.length() <= 15
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
                    } else {
                        if (!isGroupNameValidate(groupName)) {
                            showToast("Special Characters -/. Are only allowed");
                        } else if (groupName.length() > 15) {
                            showToast("Groupname must be 1-15 characters");
                        } else if(membersAcceptedList.size()==0){
                            showToast("Please select group members");
                        } else {
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
                if(groupid!=null) groupBean.setGroupId(groupid);
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
                WebServiceReferences.webServiceClient.createRoundingGroup(groupBean, this);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static  Vector<GroupBean> RoundingList ;


    public  static Vector<GroupBean> getallRoundingGroups()
    {
        loadNewGroupsFromDB();
        RoundingList=getGroupList(RoundingList);
        return RoundingList;

    }
    public static Vector<GroupBean> loadNewGroupsFromDB() {
        try {
            RoundingList = new Vector<GroupBean>();
            Vector<GroupBean> gList = DBAccess.getdbHeler()
                    .getAllGroups(CallDispatcher.LoginUser,"Rounding");

            if (gList != null && gList.size() > 0) {
                RoundingList.clear();
                RoundingList.addAll(gList);
            }
            return RoundingList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RoundingList;

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
                memberCount.setText("("
                        + String.valueOf(membersList.size()) + ")");
                memberAcceptedCount.setText("("
                        + String.valueOf(membersAcceptedList.size()) + ")");
                lv_buddylist.removeAllViews();
                adapter = new BuddyAdapter(RoundingGroupActivity.this, membersList);
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
            result = new Vector<UserBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if(convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.rounding_member_row, null);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.delete_mark = (ImageView) convertView.findViewById(R.id.delete_mark);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.role = (TextView) convertView.findViewById(R.id.position);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.rights = (TextView) convertView.findViewById(R.id.rights);
                    convertView.setTag(holder);
                }else
                    holder = (ViewHolder) convertView.getTag();
                final UserBean bib = result.get(i);
                imageLoader=new ImageLoader(context);
                if(bib!=null) {
                    if (bib.getProfilePic() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/COMMedia/" + bib.getProfilePic();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.statusIcon.setVisibility(View.GONE);
                    holder.header_title.setVisibility(View.GONE);
                    holder.delete_mark.setVisibility(View.VISIBLE);
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

                    if(bib.getBuddyName().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                        holder.rights.setText("Owner");
                        holder.rights.setTextColor(getResources().getColor(R.color.green));
                    }else {
                        if(bib.getAdmin()!=null){
                            if(bib.getAdmin().equalsIgnoreCase("1"))
                                holder.rights.setVisibility(View.VISIBLE);
                            else
                                holder.rights.setVisibility(View.GONE);
                        } else
                            holder.rights.setVisibility(View.GONE);
                        holder.occupation.setTextColor(getResources().getColor(R.color.snazlgray));
                    }
                    if(bib.getOccupation()!=null)
                        holder.occupation.setText(bib.getOccupation());
                    if(bib.getRole()!=null){
                        holder.role.setText(bib.getRole());
                    }else
                        holder.role.setVisibility(View.GONE);
                    holder.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            if(!bib.isSelected()) {
                            Intent intent = new Intent(context, RoundingEditActivity.class);
                            intent.putExtra("buddyname", bib.getBuddyName());
                            intent.putExtra("firstname", bib.getFirstname());
                            intent.putExtra("groupid", groupid);
                            startActivity(intent);
//                            }
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
        ImageView buddyicon,delete_mark;
        ImageView statusIcon,edit;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
        TextView rights,role;
    }
    public void notifyCreateRoundingGroup(Object obj) {
        if (obj instanceof GroupBean) {
            isEdit = true;
            final GroupBean groupBean = (GroupBean) obj;

            groupBean.setUserName(CallDispatcher.LoginUser);
            groupBean.setOwnerName(CallDispatcher.LoginUser);
//			memberCount.setVisibility(View.VISIBLE);
//			memberAcceptedCount.setVisibility(View.VISIBLE);

            if (DBAccess.getdbHeler().saveOrUpdateGroup(groupBean) > 0) {

                if (RoundingList != null) {
                    GroupBean gBean = new GroupBean();
                    RoundingList.add(gBean);
                }
                for (GroupBean gBean : RoundingList) {
                    if (gBean.getGroupId().equals(groupBean.getGroupId())) {
                        gBean.setGroupName(groupBean.getGroupName());
                        break;
                    }
                }

                this.groupBean = groupBean;
                membersList.clear();
                membersAcceptedList.clear();

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
                    }
                }
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
                if (groupBean.getInActiveGroupMembers() != null
                        && groupBean.getInActiveGroupMembers().length() > 0) {
                    String[] inActiveList = groupBean.getInActiveGroupMembers()
                            .split(",");
                    for (String tmp : inActiveList) {
                        UserBean userBean = new UserBean();
                        userBean.setBuddyName(tmp);
                        userBean.setSelected(true);
                        membersList.add(userBean);
                        for(UserBean res:membersAcceptedList){
                            if(res.getBuddyName().equalsIgnoreCase(tmp)) {
                                membersList.remove(userBean);
                                break;
                            }
                        }
                    }
                }
                callDisp.getdbHeler(context).insertorUpdateGroupMembers(groupBean);
                refreshMembersList();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        RoundingFragment.getRoundingAdapter().notifyDataSetChanged();
                    }
                });
                RoundingFragment.newInstance(context).getList();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(SingleInstance.mainContext
                                .getString(R.string.group_created_success));
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        } else if (obj instanceof String) {
            showToast((String) obj);
        } else {
            showAlert(SingleInstance.mainContext.getResources().getString(R.string.response_group),
                    ((WebServiceBean) obj).getText());
        }
        cancelDialog();
        finish();
    }
    public  void refreshInviteMembers()
    {
        if (gBean.getInviteMembers() != null
                && gBean.getInviteMembers().length() > 0) {
            Log.i("AAAA", "refreshInviteMembers*********************");
            String[] list = (gBean.getInviteMembers())
                    .split(",");
            membersAcceptedList.clear();
            membersAcceptedList.add(getSelfUserBean());
            for (String tmp : list) {
                UserBean userBean = new UserBean();
                userBean.setBuddyName(tmp);
                ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(tmp);
                userBean.setFirstname(pBean.getFirstname()+" "+pBean.getLastname());
                GroupMemberBean bean=DBAccess.getdbHeler().getMemberDetails(groupid,tmp);
                Log.i("AAAA","refreshInviteMembers***************** "+bean.getRole());
                userBean.setAdmin(bean.getAdmin());
                userBean.setRole((bean.getRole()));
                membersAcceptedList.add(userBean);
            }
        }
        memberAdapter=new MembersAdapter(this,R.layout.rounding_member_row,membersAcceptedList);
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                final int adapterCount1 = memberAdapter.getCount();
                lv_memberList.removeAllViews();
                for (int i = 0; i < adapterCount1; i++) {
                    View item = memberAdapter.getView(i, null, null);
                    lv_memberList.addView(item);
                }
                if (memberAdapter != null) {
                    memberAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private class BuddiesListComparator implements Comparator<UserBean> {

        @Override
        public int compare(UserBean oldBean,
                           UserBean newBean) {
            // TODO Auto-generated method stub
            return (oldBean.getFirstname().compareToIgnoreCase(newBean.getFirstname()));
        }

    }
    public static Vector<UserBean> getOnlineList(Vector<UserBean> vectorBean) {
        String status = null;
        Vector<UserBean> tempList = new Vector<UserBean>();
        Vector<UserBean> onlinelist = new Vector<UserBean>();
        tempList.clear();
        for (UserBean sortlistbean : vectorBean) {
            status = sortlistbean.getStatus();
            if (status.equalsIgnoreCase("Online")) {
                onlinelist.add(sortlistbean);
            }
        }
        if(onlinelist.size()>0)
            tempList.addAll(onlinelist);

        return tempList;

    }
}
