package com.group.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Fingerprint.MainActivity;
import com.adapter.NotifyListAdapter;
import com.bean.ConnectionBrokerServerBean;
import com.bean.GroupChatBean;
import com.bean.GroupChatPermissionBean;
import com.bean.GroupTempBean;
import com.bean.ProfileBean;
import com.bean.SpecialMessageBean;
import com.bean.UserBean;
import com.callHistory.CallHistoryActivity;
import com.cg.Calendar.CalendarViewClass;
import com.cg.DB.DBAccess;
import com.cg.account.PinSecurity;
import com.cg.commonclass.BuddyListComparator;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.HandSketchActivity2;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.listswipe.SwipeMenu;
import com.cg.commongui.listswipe.SwipeMenuCreator;
import com.cg.commongui.listswipe.SwipeMenuItem;
import com.cg.commongui.listswipe.SwipeMenuListView;
import com.cg.files.CompleteListBean;
import com.cg.files.CompleteListView;
import com.cg.files.ComponentCreator;
import com.cg.files.FileInfoFragment;
import com.cg.files.FilePicker;
import com.cg.files.FilesAdapter;
import com.cg.forms.AddNewForm;
import com.cg.forms.FormPermissionViewer;
import com.cg.ftpprocessor.FTPBean;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.NotePickerScreen;
import com.cg.locations.buddyLocation;
import com.cg.profiles.ViewProfiles;
import com.cg.rounding.AssignPatientActivity;
import com.cg.rounding.LinksAdapter;
import com.cg.rounding.OwnershipActivity;
import com.cg.rounding.PatientLocationComparator;
import com.cg.rounding.PatientNameComparator;
import com.cg.rounding.PatientRoundingFragment;
import com.cg.rounding.PatientStatusComparator;
import com.cg.rounding.RolesManagementFragment;
import com.cg.rounding.RoundNewPatientActivity;
import com.cg.rounding.RoundingFragment;
import com.cg.rounding.RoundingGroupActivity;
import com.cg.rounding.RoundingPatientAdapter;
import com.cg.rounding.RoundingTaskAdapter;
import com.cg.rounding.TaskCreationActivity;
import com.cg.rounding.TaskDateComparator;
import com.cg.snazmed.R;
import com.chat.ChatBean;
import com.ftp.ChatFTPBean;
import com.ftp.FTPPoolManager;
import com.group.BuddyAdapter;
import com.group.GroupActivity;
import com.group.ViewGroups;
import com.image.utils.ImageLoader;
import com.image.utils.ImageUtils;
import com.image.utils.ImageViewer;
import com.main.AppMainActivity;
import com.main.CalendarActivity;
import com.main.ContactsFragment;
import com.main.DashBoardFragment;
import com.main.ExchangesFragment;
import com.main.FilesFragment;
import com.main.FormsFragment;
import com.process.BGProcessor;
import com.util.CustomVideoCamera;
import com.util.FullScreenImage;
import com.util.SingleInstance;
import com.util.Utils;
import com.util.VideoPlayer;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.lib.PatientDetailsBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.PermissionBean;
import org.lib.model.RecordTransactionBean;
import org.lib.model.RoleAccessBean;
import org.lib.model.RolePatientManagementBean;
import org.lib.model.TaskDetailsBean;
import org.lib.model.UdpMessageBean;
import org.lib.model.WebServiceBean;
import org.lib.xml.XmlComposer;
import org.lib.xml.XmlParser;
import org.net.AndroidInsecureKeepAliveHttpsTransportSE;
import org.util.Utility;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class GroupChatActivity extends FragmentActivity implements OnClickListener ,TextWatcher {
    private Button btnBack;
    private Button settingsMenu;
    private ImageView profilePic;
    boolean set = false;
    private ListView memlist_splmsg, listViewPatient;
    int finalTime, startTime, swipeposition;
    String tick = "&#x2713";
    private MediaRecorder mRecorder = null;
    public static Vector<SendListUIBean> SendListUI;
    public static SendChatListAdapter sendlistadapter;
    private TextView buddyName;
    MediaPlayer mediaPlayer = new MediaPlayer();
    Handler history_handler;
    private double timeElapsed = 0;
    public GroupChatAdapter adapter = null;
    public BuddyAdapter mem_adapter = null;
    private Vector<UserBean> membersList = new Vector<UserBean>();
    public SwipeMenuListView lv = null;
    public Vector<GroupChatBean> chatList;
    private String sessionid = null;
    int lastcount = 0, msgStatus = 0;
    public String buddy = null, nickname = null;
    public EditText message = null;
    private Button sendBtn = null;
    private ImageView attachBtn = null;
    int click = 0;
    private RelativeLayout sendLay, rel_quoted;
    private ImageView settingsBtn = null;
    private CallDispatcher callDisp = null;
    public static Context context = null;
    private Handler handler = new Handler();
    private String strIPath = null;
    private LinearLayout attachment_layout;
    // private String fileHistoryName = null;
    private ProgressDialog dialog = null;
    public GroupBean groupBean = null;
    public static TextView typingstatus;
    public String groupId;
    public boolean isconfirmclicked = false;
    public boolean isReplyclicked = false;
    public boolean isprivateclicked = false;
    public boolean isurgentclicked = false;
    public boolean isReplyBack = false;
    public boolean isconfirmBack = false;
    public boolean isUrgent = false;
    private String privateMembers;
    private String pId;
    public boolean isGroup, isRounding,minimize;
    public ProgressBar progress, progressBar1;
    public LinearLayout list_all,multi_send;
    private RelativeLayout audio_layout, ad_play, selectAll_container, settingnotifications, forwardlay;
    public static RelativeLayout relative_send_layout;
    public TextView tvquoted_msg, percentage, sender_status, receiver_status, txt_time,countofselection;
    public CheckBox selectAll_buddy, selectInvidual_buddy;
    private ImageView btn_grid, xbutton, im_forward;
    private Button audio_call;
    private Button video_call;
    private int progressStates;
    private int presentbuddiescount = 0;

    RoundingTaskAdapter taskAdapter;
    SwipeMenuListView listView = null;
    // start 07-10-15 changes

    ImageViewer imageViewer;

    // ended 07-10-15 changes
    SeekBar seekBar;

    private boolean isProfileRequested = false;
    private boolean isDeadLine = false;
    private GroupChatBean tempGCBean = null;
    private TextView status;
    private String temp, buddy_temp;
    private PermissionBean permissionBean = null;
    private boolean isUnderPatient = false;
    private LinearLayout msgoptionview,LL_privateReply,ll_privateReplyclose;
    private TextView schedule_confirmation;
    private TextView private1;
    private TextView deadline_urgent;
    private TextView replayback;
    private TextView tv_privateReplyUsername;
    protected String parentId;
    private LinearLayout dateLayout;
    private TextView dateTime;
    private String isOpen = "C";
    private CallDispatcher calldisp;
    LinearLayout atachlay;
    Boolean isGrid = false;
    boolean forward = false;
    private static GroupChatActivity groupChatActivity;
    AppMainActivity appMainActivity;
    ImageView Sender_img;
    ImageLoader imageLoader;
    TextView tv_chat, tv_profie, tv_info, tv_file, tv_links;
    ImageView chat_img, profile_img, info_img, file_img, links_img;
    View view_chat, view_profile, view_info, view_snazbox, view_links;
    public static HashMap<String, String> buddyStatus = new HashMap<String, String>();
    public static String patientType = "name";
    public String sorting = "online";
    public Vector<PatientDetailsBean> PatientList;
    public Vector<PatientDetailsBean> tempPatientList = new Vector<PatientDetailsBean>();
    RoundingPatientAdapter patientadapter;
    RelativeLayout search_header;
    EditText ed_search;
    String statusMode = "ALL", assignedMode = "ALL";
    ListView tasklistView;
    String strQuery = null;
    TextView tv_status, tv_assigned;
    ImageView img_status;
    private LinearLayout header;
    private String signalid="";
    private static int membercount = 0;
    private static int checkBoxCounter = 0;
    public static Vector<CompleteListBean> filesList = new Vector<CompleteListBean>();
    FilesAdapter filesAdapter = null;
    Button cancel_button, cancel;
    ImageView sidemenu;
    Button dot;
    boolean isatoz=true;
    private RecordTransactionBean rBean=null;
    private RolePatientManagementBean rolePatientManagementBean;
    private RoleAccessBean roleAccessBean;
    private GroupMemberBean memberbean;
    Integer itemID;
    private String SelectedtaskMember;

    //For this boolean used for private button click
    boolean isPrivateBack=false;
    View PrivateReply_view=null;
    String privateParentID=null;
    String privateReply_username=null;
    boolean isForward=false;
    private boolean mypatient=true;
    public boolean isMemberTab=false;
    Button search;
    DrawerLayout mDrawerLayout;
    LinearLayout menu_side;

    private HashMap<String,Object> current_open_activity_detail = new HashMap<String,Object>();
    private boolean save_state = false;
    ImageView btMenu;
    private RelativeLayout mainHeader;
    boolean chattemplate=false;
    private String finalPlayFile;
    GroupChatBean finalPlayBean;
    SeekBar multiaudio_seekbar;


    //For chatSearch
    EditText searchet;
    TextView txtView01;
    RelativeLayout rl_search,rl_titleheader;
    Button btn_searchClose;
    ImageView iv_searchclear;
    boolean search_enable=false;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_chat_profile2);
        txtView01 = (TextView) findViewById(R.id.txtView01);

        search_enable=true;
        rl_titleheader=(RelativeLayout)findViewById(R.id.header);
        searchet = (EditText) findViewById(R.id.searchet);
        rl_search=(RelativeLayout)findViewById(R.id.search_prnt);
        btn_searchClose=(Button)findViewById(R.id.btn_close_search);
        iv_searchclear=(ImageView)findViewById(R.id.iv_searchclear);

        final LinearLayout chat = (LinearLayout) findViewById(R.id.chat);
        final LinearLayout profilechat = (LinearLayout) findViewById(R.id.profilechat);
        dot = (Button) findViewById(R.id.dot);
        search = (Button) findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        final LinearLayout snazbox_chat = (LinearLayout) findViewById(R.id.snazbox_chat);
        final LinearLayout link_chat = (LinearLayout) findViewById(R.id.link_chat);
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        cancel = (Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        minimize = getIntent().getBooleanExtra("minimize",false);
        isGroup = getIntent().getBooleanExtra("isGroup", false);
        groupId = getIntent().getStringExtra("groupid");
        sessionid = getIntent().getStringExtra("sessionid");
        isRounding = getIntent().getBooleanExtra("isRounding", false);
        buddy = getIntent().getStringExtra("buddy");
        buddy_temp = buddy;
        nickname = getIntent().getStringExtra("nickname");
        temp = getIntent().getStringExtra("buddystatus");
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        tv_profie = (TextView) findViewById(R.id.tv_profie);
        chat_img = (ImageView) findViewById(R.id.chat_img);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        view_chat = (View) findViewById(R.id.view_chat);
        view_profile = (View) findViewById(R.id.view_profile);
        view_info = (View) findViewById(R.id.view_info);
        view_snazbox = (View) findViewById(R.id.view_snazbox);
        view_links = (View) findViewById(R.id.view_links);
        header = (LinearLayout) findViewById(R.id.header1);
        LinearLayout info_lay = (LinearLayout) findViewById(R.id.info);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_file = (TextView) findViewById(R.id.tv_file);
        tv_links = (TextView) findViewById(R.id.tv_links);
        info_img = (ImageView) findViewById(R.id.info_img);
        file_img = (ImageView) findViewById(R.id.file_img);
        links_img = (ImageView) findViewById(R.id.links_img);
        if (getIntent().getStringExtra("isReq") != null) {
            isOpen = getIntent().getStringExtra("isReq");
            current_open_activity_detail.put("isReq",isOpen);
        }
        appMainActivity = (AppMainActivity) SingleInstance.contextTable
                .get("MAIN");
        context = GroupChatActivity.this;

        current_open_activity_detail = new HashMap<String,Object>();
        current_open_activity_detail.put("activtycontext",context);
        current_open_activity_detail.put("isGroup",isGroup);
        current_open_activity_detail.put("groupid",groupId);
        current_open_activity_detail.put("sessionid",sessionid);
        current_open_activity_detail.put("isRounding",isRounding);
        current_open_activity_detail.put("buddy",buddy);
        current_open_activity_detail.put("nickname",nickname);
        current_open_activity_detail.put("buddystatus",temp);


        sidemenu = (ImageView) findViewById(R.id.side_menu);
        mediaPlayer = new MediaPlayer();

        history_handler = new Handler();
        imageLoader = new ImageLoader(context);

        if (WebServiceReferences.callDispatch.containsKey("calldisp"))
            this.calldisp = (CallDispatcher) WebServiceReferences.callDispatch
                    .get("calldisp");
        else
            this.calldisp = new CallDispatcher(context);
        groupBean = DBAccess.getdbHeler().getGroup(
                "select * from grouplist where groupid='" + groupId + "'");
        if(isRounding){
            RoundingFragment changePassword = RoundingFragment.newInstance(context);
            FragmentManager fragmentManager = SingleInstance.mainContext
                    .getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(
                    R.id.activity_main_content_fragment, changePassword)
                    .commitAllowingStateLoss();

            memberbean = DBAccess.getdbHeler().getMemberDetails(groupBean.getGroupId(), CallDispatcher.LoginUser);
            roleAccessBean = DBAccess.getdbHeler().getRoleAccessDetails(groupBean.getGroupId(), memberbean.getRole());
            rolePatientManagementBean = DBAccess.getdbHeler().getRolePatientManagement(groupBean.getGroupId(), memberbean.getRole());
        }
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                txtView01.setVisibility(View.GONE);
//                searchet.setVisibility(View.VISIBLE);
                if(search_enable) {
                    search_enable=false;
                    chatprocess();
                }else {
                    adapter = new GroupChatAdapter(GroupChatActivity.this, chatList);
                    lv.setAdapter(adapter);
                }
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                searchet.setText("");
                btn_grid.setEnabled(false);
                sendBtn.setEnabled(false);
                audio_call.setEnabled(false);
                message.setEnabled(false);
                profilechat.setEnabled(false);
                snazbox_chat.setEnabled(false);
                link_chat.setEnabled(false);
                chat.setEnabled(false);


                rl_titleheader.setVisibility(View.GONE);
                rl_search.setVisibility(View.VISIBLE);
            }
        });

        btn_searchClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_grid.setEnabled(true);
                sendBtn.setEnabled(true);
                audio_call.setEnabled(true);
                message.setEnabled(true);
                profilechat.setEnabled(true);
                snazbox_chat.setEnabled(true);
                link_chat.setEnabled(true);
                chat.setEnabled(true);
                rl_titleheader.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);
                if(adapter!=null){
                    adapter.filter("");
                }
            }
        });

        iv_searchclear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchet!=null){
                    searchet.setText("");
                }
                if(adapter!=null){
                    adapter.filter("");
                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        menu_side = (LinearLayout) findViewById(R.id.menu_side);
        notifyUI();

        dot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isGroup && !isRounding) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.group_dialog);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.horizontalMargin = 15;
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        window.setAttributes(lp);
                        window.setGravity(Gravity.BOTTOM);
                        dialog.show();

                        TextView sync_chat = (TextView) dialog.findViewById(R.id.edit_grp);
                        sync_chat.setVisibility(View.VISIBLE);
                        sync_chat.setText("SYNC CHAT");
                        TextView sms = (TextView) dialog.findViewById(R.id.invite_grp);
                        sms.setText("Invite User to Group");
                        TextView clear = (TextView) dialog.findViewById(R.id.leave_grp);
                        clear.setText("Clear all Mesages");
                        TextView delet_user = (TextView) dialog.findViewById(R.id.delete_grp);
                        delet_user.setText("Delete User from Contacts");
                        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        sync_chat.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                chatsync(false);
                            }
                        });
                        delet_user.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                ContactsFragment.getInstance(context).doDeleteContact(buddy);
                            }
                        });
                        clear.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                try {
                                    dialog.dismiss();
                                    if (chatList == null || chatList.size() == 0) {
                                        showToast(SingleInstance.mainContext.getResources().getString(R.string.cannot_clear_empty));
                                    } else {
                                        clearAllAlert(buddy);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else if (isRounding) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.roundeditmanagement);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.horizontalMargin = 15;
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawableResource(R.color.lblack);
//                            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        window.setAttributes(lp);
                        window.setGravity(Gravity.BOTTOM);
                        dialog.show();
                        TextView edit = (TextView) dialog.findViewById(R.id.round_edit);
                        TextView role = (TextView) dialog.findViewById(R.id.round_role);
                        TextView newTask = (TextView) dialog.findViewById(R.id.roun_new_task);
                        TextView newPatient = (TextView) dialog.findViewById(R.id.roun_new_patient);
                        TextView ownership = (TextView) dialog.findViewById(R.id.roun_ownership);
                        TextView delete = (TextView) dialog.findViewById(R.id.roun_delete);
                        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        if (!groupBean.getOwnerName().equalsIgnoreCase(
                                CallDispatcher.LoginUser)) {
                            delete.setEnabled(false);
                            delete.setBackgroundColor(context.getResources().getColor(R.color.black));
                            ownership.setEnabled(false);
                            ownership.setBackgroundColor(context.getResources().getColor(R.color.black));
                            edit.setEnabled(false);
                            edit.setBackgroundColor(context.getResources().getColor(R.color.black));
                        }
                        if(!(groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                                (memberbean.getAdmin()!=null && memberbean.getAdmin().equalsIgnoreCase("1"))||
                                roleAccessBean.getTaskmanagement()!=null && roleAccessBean.getTaskmanagement().equalsIgnoreCase("1"))){
                            newTask.setEnabled(false);
                            newTask.setBackgroundColor(context.getResources().getColor(R.color.black));
                        }
                        if(!(groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                                (memberbean.getAdmin()!=null && memberbean.getAdmin().equalsIgnoreCase("1")) ||
                                rolePatientManagementBean.getAdd()!=null && rolePatientManagementBean.getAdd().equalsIgnoreCase("1"))){
                            newPatient.setEnabled(false);
                            newPatient.setBackgroundColor(context.getResources().getColor(R.color.black));
                        }
                        if(!groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                                (memberbean.getAdmin()!=null && memberbean.getAdmin().equalsIgnoreCase("0")) ){
                            role.setEnabled(false);
                            role.setBackgroundColor(context.getResources().getColor(R.color.black));
                        }
                        edit.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    Intent intent = new Intent(GroupChatActivity.this,
                                            RoundingGroupActivity.class);
                                    intent.putExtra("isEdit", true);
                                    intent.putExtra("id", groupBean.getGroupId());
                                    startActivity(intent);
                                    dialog.dismiss();
                            }
                        });
                        newTask.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, TaskCreationActivity.class);
                                intent.putExtra("groupid", groupBean.getGroupId());
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        role.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    RolesManagementFragment rolesManagementFragment = RolesManagementFragment.newInstance(SingleInstance.mainContext);
                                    rolesManagementFragment.setGroupName(groupBean.getGroupName());
                                    rolesManagementFragment.setGroupId(groupBean.getGroupId());
                                    FragmentManager fragmentManager = SingleInstance.mainContext
                                            .getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(
                                            R.id.activity_main_content_fragment, rolesManagementFragment)
                                            .commitAllowingStateLoss();
                                    dialog.dismiss();
                                    finish();
                            }
                        });
                        ownership.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GroupChatActivity.this, OwnershipActivity.class);
                                intent.putExtra("groupid", groupBean.getGroupId());
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        newPatient.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, RoundNewPatientActivity.class);
                                intent.putExtra("groupid", groupBean.getGroupId());
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        delete.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ContactsFragment contactsFragment = ContactsFragment
                                        .getInstance(context);
                                // TODO Auto-generated method stub
                                if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                                        contactsFragment.deleteGroup(groupBean,
                                                "Are you sure you want to delete this group ");
                                } else {
                                    contactsFragment.showAlert1("Info",
                                            "Check internet connection Unable to connect server");
                                }
                                dialog.dismiss();
                            }
                        });
                    } else if (isGroup && !isRounding) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.group_dialog);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.horizontalMargin = 15;
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawableResource(R.color.lblack);
                        window.setAttributes(lp);
                        window.setGravity(Gravity.BOTTOM);
                        dialog.show();
                        TextView edit_grp = (TextView) dialog.findViewById(R.id.edit_grp);
                        TextView invite_grp = (TextView) dialog.findViewById(R.id.invite_grp);
                        TextView leave_grp = (TextView) dialog.findViewById(R.id.leave_grp);
                        TextView delete_grp = (TextView) dialog.findViewById(R.id.delete_grp);
                        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                        TextView sync_chat = (TextView)dialog.findViewById(R.id.sync_chat);
                        if (groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                            delete_grp.setVisibility(View.VISIBLE);
                            leave_grp.setVisibility(View.GONE);
                            edit_grp.setVisibility(View.VISIBLE);
                        } else {
                            edit_grp.setVisibility(View.GONE);
                            delete_grp.setVisibility(View.GONE);
                            leave_grp.setVisibility(View.VISIBLE);
                        }
                        sync_chat.setVisibility(View.VISIBLE);
                        sync_chat.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                chatsync(true);
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        invite_grp.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                try {
                                    if (groupBean.getOwnerName().equalsIgnoreCase(
                                            CallDispatcher.LoginUser)) {
                                        Intent intent = new Intent(context.getApplicationContext(),
                                                GroupActivity.class);
                                        intent.putExtra("isEdit", true);
                                        intent.putExtra("id", groupBean.getGroupId());
                                        context.startActivity(intent);
                                    }else
                                    showToast("Your are not owner of the group");
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        edit_grp.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                try {
                                    if (groupBean.getOwnerName().equalsIgnoreCase(
                                            CallDispatcher.LoginUser)) {
                                        Intent intent = new Intent(context.getApplicationContext(),
                                                GroupActivity.class);
                                        intent.putExtra("isEdit", true);
                                        intent.putExtra("id", groupBean.getGroupId());
                                        context.startActivity(intent);
                                    }
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        leave_grp.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ContactsFragment contactsFragment = ContactsFragment
                                        .getInstance(context);
                                if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                                    if (!groupBean.getOwnerName().equalsIgnoreCase(
                                            CallDispatcher.LoginUser))
                                        contactsFragment
                                                .deleteGroup(groupBean,
                                                        "Are you sure you want to leave this group");
                                    // TODO Auto-generated catch block
                                } else {
                                    contactsFragment.showAlert1("Info",
                                            "Check internet connection Unable to connect server");

                                }

                            }
                        });
                        delete_grp.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                try {
                                    ContactsFragment contactsFragment = ContactsFragment
                                            .getInstance(context);
                                    // TODO Auto-generated method stub
                                    if (SingleInstance.mainContext
                                            .isNetworkConnectionAvailable()) {
                                        if (groupBean.getOwnerName().equalsIgnoreCase(
                                                CallDispatcher.LoginUser))
                                            contactsFragment
                                                    .deleteGroup(groupBean,
                                                            "Are you sure you want to delete this group ");
                                        // TODO Auto-generated catch block
                                    } else {
                                        contactsFragment.showAlert1("Info",
                                                "Check internet connection Unable to connect server");
                                    }
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        search.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
        BuddyStatus();
        if (!isGroup && !isRounding) {
            if(nickname==null) {
                ProfileBean bean = DBAccess.getdbHeler().getProfileDetails(buddy);
                nickname=bean.getFirstname()+" "+bean.getLastname();
            }
            txtView01.setText(nickname);
        }else if (isGroup && !isRounding) {
            txtView01.setText(groupBean.getGroupName().toUpperCase());
            header.setWeightSum(5);
            info_lay.setVisibility(View.VISIBLE);
            tv_profie.setText("Members");
            profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members));
        } else if (isRounding) {
            if (groupBean.getGroupName() != null)
                txtView01.setText(groupBean.getGroupName().toUpperCase());
            header.setWeightSum(5);
            info_lay.setVisibility(View.VISIBLE);
            search.setVisibility(View.GONE);
            tv_info.setText("Calendar");
            tv_profie.setText("Members");
            tv_file.setText("Patients");
            tv_links.setText("Tasks");
            file_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_patients));
            info_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_calendar));
            links_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_tasks));
            profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members));
        }

        if (isOpen.equalsIgnoreCase("C")) {
            chatprocess();

        }else if (isOpen.equalsIgnoreCase("F")) {
            setDefault();
            tv_file.setTextColor(getResources().getColor(R.color.white));
            view_snazbox.setVisibility(View.VISIBLE);
            FilesProcess();
            file_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_snazbox_white));
        } else if (isOpen.equalsIgnoreCase("A") || isOpen.equalsIgnoreCase("B")) {
            try {
                profileProcess();
            } catch (Exception e) {
                e.printStackTrace();
            }
            profilechat.setVisibility(View.GONE);
            snazbox_chat.setVisibility(View.GONE);
            link_chat.setVisibility(View.GONE);
            chat.setVisibility(View.GONE);
            dot.setVisibility(View.GONE);
            search.setVisibility(View.GONE);
        }
        if(isRounding) {
            setDefault();
            file_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_patients_white));
            tv_file.setTextColor(getResources().getColor(R.color.white));
            view_snazbox.setVisibility(View.VISIBLE);
            PatientDetails();
            if(!isOpen.equalsIgnoreCase("p"))
                showprogress();
            if(minimize) {
                cancelDialog();
            }
        }

        String noanswerReplyType = getIntent().getStringExtra("noanswerreply");
        if(noanswerReplyType != null) {
            if (noanswerReplyType.equalsIgnoreCase("text")) {
                message.requestFocus();
            } else if (noanswerReplyType.equalsIgnoreCase("audio")) {
                if (!CallDispatcher.isCallInitiate) {
                    atachlay.setVisibility(View.GONE);
                    audio_layout.setVisibility(View.VISIBLE);
                } else
                    showToast("Please Try again...call in progress");
            } else if (noanswerReplyType.equalsIgnoreCase("video")) {
                showVideoMessageDialog();
            }
        }

        profilechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefault();
                search.setVisibility(View.GONE);
                isMemberTab=true;
                if (!isGroup && !isRounding)
                    profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_profile_white));
                else
                    profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members_white));
                tv_profie.setTextColor(getResources().getColor(R.color.white));
                view_profile.setVisibility(View.VISIBLE);
                try {
                    if (!isGroup && !isRounding)
                        profileProcess();
                    else if (isGroup && !isRounding)
                        MembersProcess();
                    else
                        RoundingMember();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        snazbox_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefault();
                search.setVisibility(View.GONE);
                isMemberTab=false;
                if (isRounding) {
                    PatientDetails();
                    file_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_patients_white));
                } else {
                    FilesProcess();
                    file_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_snazbox_white));
                }
                tv_file.setTextColor(getResources().getColor(R.color.white));
                view_snazbox.setVisibility(View.VISIBLE);
            }
        });
        link_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefault();
                search.setVisibility(View.GONE);
                isMemberTab=false;
                if (isRounding) {
                    TaskProcess();
                    links_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_tasks_white));
                    tv_links.setTextColor(getResources().getColor(R.color.white));
                    view_links.setVisibility(View.VISIBLE);
                }else{
                    links_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_links_white));
                    tv_links.setTextColor(getResources().getColor(R.color.white));
                    view_links.setVisibility(View.VISIBLE);
                    linkProcess();
                }
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefault();
                isMemberTab=false;
                chat_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat_white));
                tv_chat.setTextColor(getResources().getColor(R.color.white));
                view_chat.setVisibility(View.VISIBLE);
                if(search.getVisibility() == View.GONE)
                search.setVisibility(View.VISIBLE);
                chatprocess();
            }
        });
        info_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                search.setVisibility(View.GONE);
                isMemberTab=false;
                if (isGroup) {
                    info_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_info_white));
                    GroupInfo();
                }else {
                    info_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_calendar_white));
                    calendarProcess();
                }
                    tv_info.setTextColor(getResources().getColor(R.color.white));
                    view_info.setVisibility(View.VISIBLE);
            }
        });



            if (SendListUI != null && sendlistadapter != null && list_all != null && AppReference.getValuesinChat!=null && AppReference.getValuesinChat.size()>0) {
                HashMap<String, Object> objectHashMap = AppReference.getValuesinChat;
                if (objectHashMap.containsKey("message")) {
                    String msg = (String) objectHashMap.get("message");
                    message.setText(msg);
                }
                if (objectHashMap.containsKey("firstimage1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("firstimage1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("firstimage2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("firstimage2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("firstimage3")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("firstimage3");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("secondimage1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("secondimage1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("secondimage2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("secondimage2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("secondimage3")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("secondimage3");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("thirdimage1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("thirdimage1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("thirdimage2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("thirdimage2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("thirdimage3")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("thirdimage3");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("audio")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("audio");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("audio1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("audio1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("audio2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("audio2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("firstvideo1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("firstvideo1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("firstvideo2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("firstvideo2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("firstvideo3")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("firstvideo3");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("secondvideo1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("secondvideo1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("secondvideo2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("secondvideo2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("secondvideo3")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("secondvideo3");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("thirdvideo1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("thirdvideo1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("thirdvideo2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("thirdvideo2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("thirdvideo3")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("thirdvideo3");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("sketch1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("sketch1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("sketch2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("sketch2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("sketch3")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("sketch3");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("document1")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("document1");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("document2")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("document2");
                    SendListUI.add(sendListUI);
                }
                if (objectHashMap.containsKey("document3")) {
                    SendListUIBean sendListUI = (SendListUIBean) objectHashMap.get("document3");
                    SendListUI.add(sendListUI);
                }

                sendlistadapter.notifyDataSetChanged();
                list_all.removeAllViews();
                final int adapterCount = sendlistadapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = sendlistadapter.getView(i, null, null);
                    list_all.addView(item);
                }
                if (adapterCount >= 2) {
                    multi_send.getLayoutParams().height = 280;
                }

                audio_call.setBackgroundResource(R.drawable.chat_send);
                audio_call.setTag(1);
            }

    }

    public void refreshPatient() {
        handler.post(new Runnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                try {
                    String strGetQry = "select * from patientdetails where groupid='"
                            + groupBean.getGroupId() + "'";
                    PatientList = DBAccess.getdbHeler().getAllPatientDetails(strGetQry);
                    Log.d("patient123", "RoundNewPatientact.class GroupID==========" + groupBean.getGroupId());
                    for(PatientDetailsBean bean:PatientList){
                        bean.setIsFromPatienttab(true);
                    }

                    patientadapter = new RoundingPatientAdapter(context, R.layout.rouding_patient_row, PatientList);
                    listViewPatient.setAdapter(null);
                    listViewPatient.setAdapter(patientadapter);
                    patientadapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refreshTask() {
        handler.post(new Runnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                taskSorting(groupBean.getGroupId());
            }
        });
    }

    public void profileProcess() {
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.profile, content);
        ProfileBean pb = DBAccess.getdbHeler().getProfileDetails(buddy);
        if (pb != null) {
            ImageView profilePic = (ImageView) v1.findViewById(R.id.riv1);
            TextView username = (TextView) v1.findViewById(R.id.username);
            TextView profession = (TextView) v1.findViewById(R.id.profession);
            TextView tileName = (TextView) v1.findViewById(R.id.tileName);
            TextView fistName = (TextView) v1.findViewById(R.id.fistName);
            TextView lastName = (TextView) v1.findViewById(R.id.lastName);
            TextView nickName = (TextView) v1.findViewById(R.id.nickName);
            TextView mf = (TextView) v1.findViewById(R.id.mf);
            TextView atten_phys = (TextView) v1.findViewById(R.id.atten_phys);
            TextView statof = (TextView) v1.findViewById(R.id.statof);
            TextView profess = (TextView) v1.findViewById(R.id.profess);
            TextView spec = (TextView) v1.findViewById(R.id.spec);
            TextView medical = (TextView) v1.findViewById(R.id.medical);
            TextView residency = (TextView) v1.findViewById(R.id.residency);
            TextView felloship = (TextView) v1.findViewById(R.id.felloship);
            TextView officeaddre = (TextView) v1.findViewById(R.id.officeaddre);
            TextView hospitalspec = (TextView) v1.findViewById(R.id.hospitalspec);
            TextView citation = (TextView) v1.findViewById(R.id.citation);
            TextView association = (TextView) v1.findViewById(R.id.association);
            ImageView Statusicon = (ImageView)v1.findViewById(R.id.status_icon);
            TextView status = (TextView) findViewById(R.id.status);
            if(temp!=null) {
                status.setText(temp);
                if (temp.equalsIgnoreCase("online")) {
                    Statusicon.setBackgroundResource(R.drawable.online_icon);
                } else if (temp.equalsIgnoreCase("offline")) {
                    Statusicon.setBackgroundResource(R.drawable.offline_icon);
                } else if (temp.equalsIgnoreCase("Away")) {
                    Statusicon.setBackgroundResource(R.drawable.busy_icon);
                } else if (temp.equalsIgnoreCase("Stealth")) {
                    Statusicon.setBackgroundResource(R.drawable.invisibleicon);
                } else if (temp.equalsIgnoreCase("Airport")) {
                    Statusicon.setBackgroundResource(R.drawable.busy_icon);
                } else {
                    Statusicon.setBackgroundResource(R.drawable.offline_icon);
                }
            }
            LinearLayout footer1=(LinearLayout)v1.findViewById(R.id.footer1);
            footer1.setVisibility(View.GONE);
            if (!pb.getPhoto().equals(null)) {
                String directory_path = Environment
                        .getExternalStorageDirectory()
                        .getAbsolutePath()
                        + "/COMMedia/" + pb.getPhoto();
                imageLoader.DisplayImage(directory_path, profilePic,
                        R.drawable.icon_buddy_aoffline);
            }
            if (pb.getUsername() != null && pb.getUsername().length() > 0)
                username.setText(pb.getFirstname() + " " + pb.getLastname());
            if (pb.getProfession() != null && pb.getProfession().length() > 0)
                profession.setText(pb.getProfession());
            if (pb.getTitle() != null && pb.getTitle().length() > 0)
                tileName.setText(pb.getTitle());
            if (pb.getFirstname() != null && pb.getFirstname().length() > 0)
                fistName.setText(pb.getFirstname());
            if (pb.getLastname() != null && pb.getLastname().length() > 0)
                lastName.setText(pb.getLastname());
            if (pb.getNickname() != null && pb.getNickname().length() > 0)
                nickName.setText(pb.getNickname());

                if(pb.getDob()!=null&& pb.getDob().length()>0) {
                    String birthdate=pb.getDob();
                    Log.i("sss1", "Current birthdate" + birthdate);
                    if(birthdate.contains("/")){
                        birthdate=birthdate.replace("/","-");
                        Log.d("sss1","replace brithdate"+birthdate);
                    }
                    String[] str = birthdate.split("-");
                    int Currentyear = Calendar.getInstance().get(Calendar.YEAR);
                    Log.i("sss1","Current year"+Currentyear);

                    String BirthYear;
                    if(str[2].length()>2)
                        BirthYear=str[2];
                    else
                        BirthYear=str[0];
                    int age=Currentyear-(Integer.parseInt(BirthYear));
                    Log.i("sss1","Current age"+age);

                    mf.setText(String.valueOf(age));
                }
//                mf.setText(pb.getDob());
            if (pb.getUsertype() != null && pb.getUsertype().length() > 0)
                atten_phys.setText(pb.getUsertype());
            if (pb.getState() != null && pb.getState().length() > 0)
                statof.setText(pb.getState());
            if (pb.getProfession() != null && pb.getProfession().length() > 0)
                profess.setText(pb.getProfession());
            if (pb.getSpeciality() != null && pb.getSpeciality().length() > 0)
                spec.setText(pb.getSpeciality());
            if (pb.getMedicalschool() != null && pb.getMedicalschool().length() > 0)
                medical.setText(pb.getMedicalschool());
            if (pb.getResidencyprogram() != null && pb.getResidencyprogram().length() > 0)
                residency.setText(pb.getResidencyprogram());
            if (pb.getFellowshipprogram() != null && pb.getFellowshipprogram().length() > 0)
                felloship.setText(pb.getFellowshipprogram());
            if (pb.getOfficeaddress() != null && pb.getOfficeaddress().length() > 0)
                officeaddre.setText(pb.getOfficeaddress());
            if (pb.getHospitalaffiliation() != null && pb.getHospitalaffiliation().length() > 0)
                hospitalspec.setText(pb.getHospitalaffiliation());
            if (pb.getOrganizationmembership() != null && pb.getOrganizationmembership().length() > 0)
                association.setText(pb.getOrganizationmembership());
            if (pb.getCitationpublications() != null && pb.getCitationpublications().length() > 0)
                citation.setText(pb.getCitationpublications());

        }



    }

    public void chatprocess() {
            final LinearLayout content = (LinearLayout) findViewById(R.id.content);
            content.removeAllViews();
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v1 = layoutInflater.inflate(R.layout.group_chat_activity, content);
            try {
                if (SingleInstance.mainContext
                        .getResources()
                        .getString(R.string.screenshot)
                        .equalsIgnoreCase(
                                SingleInstance.mainContext.getResources()
                                        .getString(R.string.yes))) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE);
                }

                // Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(
                // this, GroupChatActivity.class));
                context = GroupChatActivity.this;
                SingleInstance.contextTable.put("groupchat", context);

                swipeContainer = (SwipeRefreshLayout) v1.findViewById(R.id.swipeContainer);

                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Your code to refresh the list here.
                        // Make sure you call swipeContainer.setRefreshing(false)
                        // once the network request has completed successfully.

                        if(isGroup || isRounding) {
                            String minDate=DBAccess.getdbHeler().getminDateandTimeFromChat(groupId);
                            if(minDate!=null){
                                try {
                                    Log.i("CHATSYNC","DB minDate-->"+minDate);
//                                    SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
//                                    Date date = day.parse(minDate);
//                                    DateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//                                    Log.i("CHATSYNC","minDate-->"+serverFormat.format(date));
                                    WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "5", groupId, minDate, "");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else {
                                WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "1", groupId, "", "");
                            }
                        }
                        else {
                            String minDate=DBAccess.getdbHeler().getminDateandTimeFromChat(buddy);
                            if(minDate!=null){
                                try {
                                    Log.i("CHATSYNC","DB minDate-->"+minDate);
//                                    SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
//                                    Date date = day.parse(minDate);
//                                    DateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//                                    Log.i("CHATSYNC","minDate-->"+serverFormat.format(date));
                                    WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "5", buddy, minDate, "");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else {
                                WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "1", buddy, "", "");
                            }

                        }

                    }
                });

                swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);




                settingsBtn = (ImageView) v1.findViewById(R.id.settings_image);
                typingstatus = (TextView) v1.findViewById(R.id.typing);
                typingstatus.setVisibility(View.GONE);
//                tick="&#x2713";            &#10003
                String pen = "&#x270D";
//                pen="&#128393";
//                pen= "\uD83D\uDD89";
                typingstatus.setText(nickname + " is typing " + Html.fromHtml(pen));
                msgoptionview = (LinearLayout) v1.findViewById(R.id.splmsglay1);
                LL_privateReply=(LinearLayout) v1.findViewById(R.id.rl_private);
                settingsBtn.setOnClickListener(this);
                status = (TextView) v1.findViewById(R.id.buddy_status);
                status.setText(temp);
                RelativeLayout btn_image = (RelativeLayout) v1.findViewById(R.id.btn_image);
                relative_send_layout = (RelativeLayout) v1.findViewById(R.id.relative_send_layout);
                RelativeLayout btn_audio = (RelativeLayout) v1.findViewById(R.id.btn_audio);
                audio_layout = (RelativeLayout) v1.findViewById(R.id.audio_layout);
                progressBar1 = (ProgressBar) v1.findViewById(R.id.progressBar1);
                final ImageView audio_rec = (ImageView) v1.findViewById(R.id.audio_rec);
                RelativeLayout btn_video = (RelativeLayout) v1.findViewById(R.id.btn_video);
                RelativeLayout btn_sketch = (RelativeLayout) v1.findViewById(R.id.btn_sketch);
                RelativeLayout btn_videocall = (RelativeLayout) v1.findViewById(R.id.btn_videocall);
                RelativeLayout btn_file = (RelativeLayout) v1.findViewById(R.id.btn_file);
                RelativeLayout btn_broadcast = (RelativeLayout) v1.findViewById(R.id.btn_broadcast);
                countofselection = (TextView) v1.findViewById(R.id.selected);
                btn_broadcast.setVisibility(View.GONE);
                tvquoted_msg = (TextView) v1.findViewById(R.id.tvquoted_msg);
//                ImageView play_button = (ImageView) v1.findViewById(R.id.play_button);
//                xbutton= (ImageView) v1.findViewById(R.id.xbutton);
//                txt_time = (TextView) v1.findViewById(R.id.txt_time);
//                seekBar= (SeekBar) v1.findViewById(R.id.seekBar1);
//                ad_play=(RelativeLayout)v1.findViewById(R.id.ad_play);
                im_forward = (ImageView) v1.findViewById(R.id.forward);
                im_forward.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean selectmembers = false;

                        for (int i = 0; i < chatList.size(); i++) {
                            final GroupChatBean b = chatList.get(i);
                            if (b.isSelect()) {
                                selectmembers = true;
                            }
                        }
                        if (selectmembers) {
                            Intent intent = new Intent(context,
                                    ForwardUserSelect.class);
                            intent.putExtra("fromfile",false);
                            startActivityForResult(intent, 112);
                        } else {
                            showToast("Please select one chat to forward");
                        }
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        try {
                            Log.e("view",
                                    "........... completed----->..............");
                            if (strIPath != null) {
                                progressBar1.setProgress(0);
                                mediaPlayer.reset();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//                        // TODO Auto-generated method stub
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar,
//                                                  int progress, boolean fromUser) {
//                        // TODO Auto-generated method stub
//                        if (fromUser) mediaPlayer.seekTo(progress);
//                    }
//                });

                btn_image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if(!CallDispatcher.isCallInitiate)
                        if(SendListUI.size()<5) {
                            photochat();
                        }
                        else
                            showToast("Please attach only 5 files");
                    }
                });
//
                audio_rec.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        //your stuff
                        strIPath = Environment
                                .getExternalStorageDirectory()
                                + "/COMMedia/"
                                + "MAD_"
                                + callDisp.getFileName() + ".mp3";
                        audio_rec.setBackgroundResource(R.drawable.circle_au_rec);
                        startRecording(strIPath);
                        return true;
                    }
                });
                audio_rec.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        switch (event.getAction()) {
//                            case MotionEvent.ACTION_DOWN:
//                                strIPath = Environment
//                                        .getExternalStorageDirectory()
//                                        + "/COMMedia/"
//                                        + "MAD_"
//                                        + callDisp.getFileName() + ".mp4";
//                                startRecording(strIPath);
//                                break;
                            case MotionEvent.ACTION_UP:
                                stopRecording();
                                audio_rec.setBackgroundResource(R.drawable.circle_grey_rec);
                                break;
                        }
                        return false;
                    }
                });

                btn_audio.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showAudioMessageDialog();
                        if(!CallDispatcher.isCallInitiate) {
                            if(SendListUI.size()<5) {
                                atachlay.setVisibility(View.GONE);
                                audio_layout.setVisibility(View.VISIBLE);
                            }
                            else
                                showToast("Please attach only 5 files");
                        }else
                            showToast("Please Try again...call in progress");
//                        animation.start();
                    }
                });
                btn_video.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if(!CallDispatcher.isCallInitiate)
                        if(SendListUI.size()<5) {
                            showVideoMessageDialog();
                        }
                        else
                            showToast("Please attach only 5 files");

//                        else
//                            showToast("Please Try again...call in progress");
                    }
                });
                btn_sketch.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            if (SendListUI.size() < 5) {
                                handsketch();
                            } else
                                showToast("Please attach only 5 files");
                    }
                });
                btn_videocall.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(CallDispatcher.isCallignored) {
                            showToast("Please Try again... Ignored call in progress");
                        } else {
                            if (isGroup || isRounding) {
                                Log.d("Test", "Inside Group VideoConference onclick");
                                if (!CallDispatcher.isCallInitiate) {
                                    if(!CallDispatcher.GSMCallisAccepted) {
                                        groupCallMenu(2);
                                    } else {
                                        showToast("Please Try again... GSM call in progress");
                                    }
                                } else {
                                    showToast("Please try...Call in progress");
                                }
                            } else {
                                if (!CallDispatcher.isCallInitiate) {
                                    if (!CallDispatcher.GSMCallisAccepted) {
                                        individualCallMenu(1);
                                    } else {
                                        showToast("Please Try again... GSM call in progress");
                                    }

                                } else {
                                    showToast("Please Try again... call in progress");
                                }

                            }
                        }
                    }
                });
                btn_broadcast.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        individualCallMenu(4);
                        showDialogCall();
                    }
                });
                btn_file.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SendListUI.size() < 5) {
                            openFolder();
                        } else
                            showToast("Please attach only 5 files");
                    }
                });

                if (temp != null) {
                    if (temp.equalsIgnoreCase("online")) {
                        status.setText(SingleInstance.mainContext.getResources().getString(
                                R.string.online));
                        Log.d("status1.get", " status1.get : " + SingleInstance.mainContext.getResources().getString(
                                R.string.online));
                    } else if (temp.equalsIgnoreCase("busy")) {
                        status.setText(SingleInstance.mainContext.getResources().getString(
                                R.string.busy));
                    } else if (temp.equalsIgnoreCase("away")) {
                        status.setText(SingleInstance.mainContext.getResources().getString(
                                R.string.away));
                    } else if (temp.equalsIgnoreCase("stealth")) {
                        status.setText(SingleInstance.mainContext.getResources().getString(
                                R.string.stealth));
                    }
                }
                SingleInstance.contextTable.put("groupchat", context);
                // groupBean = (GroupBean) getIntent().getSerializableExtra(
                // "groupBean");
                groupBean = DBAccess.getdbHeler().getGroup(
                        "select * from grouplist where groupid='" + groupId + "'");
                // start 07-10-15 changes

                imageViewer = new ImageViewer(context);

                // ended 07-10-15 changes

                isDeadLine = getIntent().getBooleanExtra
                        ("deadLine", false);


                if (isDeadLine) {
                    GroupChatBean gcBean1 = (GroupChatBean) getIntent()
                            .getSerializableExtra("gcBean");
                    deadLineReplyDialog(gcBean1);
                    tempGCBean = gcBean1;
                }

                if (isGroup || isRounding) {
                    settingsBtn.setVisibility(View.GONE);
                    loadTotalChatHistory(groupBean.getGroupId());
                    //For chatrecentUpdate
                    //Start
                    Log.i("recent","id--->"+groupBean.getGroupId());
                    DBAccess.getdbHeler().updateChatRecentList(groupBean.getGroupId(),true);
                    //End
                    if (SingleInstance.unreadCount.containsKey(groupBean
                            .getGroupId())) {
                        SingleInstance.unreadCount.remove(groupBean.getGroupId());
                    }
                    DBAccess.getdbHeler().updateUnreadMsgForGroupChat(1,
                            groupBean.getGroupId(), CallDispatcher.LoginUser);
                    for (GroupBean bBean : ContactsFragment.getGroupList()) {
                        if (bBean.getGroupId().equalsIgnoreCase(groupBean.getGroupId())) {
                            bBean.setMessageCount(0);
                            break;
                        }
                    }
                    for (GroupBean bBean : ContactsFragment.getBuddyGroupList()) {
                        if (bBean.getGroupId().equalsIgnoreCase(groupBean.getGroupId())) {
                            bBean.setMessageCount(0);
                            break;
                        }
                    }
                    ContactsFragment.getGroupAdapter().notifyDataSetChanged();
                } else {
                    // settingsBtn.setVisibility(View.GONE);
                    settingsBtn.setVisibility(View.GONE);
                    loadTotalChatHistory(buddy);
                    Log.i("chatrecent","buddyname-->"+buddy);
                    //For chatrecentUpdate
                    //Start
                    DBAccess.getdbHeler().updateChatRecentList(buddy,false);
                    //End
                    if (SingleInstance.individualMsgUnreadCount.containsKey(buddy)) {
                        SingleInstance.individualMsgUnreadCount.remove(buddy);
                    }
                    DBAccess.getdbHeler().updateUnreadMsgForGroupChat(1, buddy,
                            CallDispatcher.LoginUser);
                    for (BuddyInformationBean bBean : ContactsFragment
                            .getBuddyList()) {
                        if (!bBean.isTitle()) {
                            if (bBean.getName().equalsIgnoreCase(buddy)) {
                                bBean.setMessageCount(0);
                                break;
                            }
                        }
                    }
                    ContactsFragment.getContactAdapter().notifyDataSetChanged();
                }
                Log.i("gchat123", "session id : " + sessionid);
                DashBoardFragment dashboard = DashBoardFragment
                        .newInstance(context);
                if (dashboard != null && dashboard.notifyAdapter != null && dashboard.tempnotifylist.size() > 0) {
                    dashboard.LoadFilesList(CallDispatcher.LoginUser);
                    dashboard.notifyAdapter = new NotifyListAdapter(context, dashboard.tempnotifylist);
                    dashboard.notifylistview.setAdapter(dashboard.notifyAdapter);
                    int i =  dashboard.notifylistview.getAdapter().getCount();
                    Log.d("Valueofcount", "listvalue"+i);
                    dashboard.notifyAdapter.notifyDataSetChanged();
                    dashboard.tempnotifylist.size();
                    dashboard.updateCount();
                    Log.d("listsize","value------->"+dashboard.tempnotifylist.size());
                }

                ExchangesFragment exchanges = ExchangesFragment
                        .newInstance(context);
                if (exchanges != null && exchanges.adapter != null && exchanges.exchangesList.size() > 0) {
                    for (GroupBean gbean : exchanges.exchangesList) {
                        if (isGroup) {
                            if (gbean.getGroupId().equalsIgnoreCase(groupId)) {
                                gbean.setMessageCount(0);
                                break;
                            }
                        } else {
                            Log.i("AAAA", "GROUP CHAT SINGLE");
                            if (gbean.getGroupId().equalsIgnoreCase(buddy)) {
                                gbean.setMessageCount(0);
                                Log.i("AAAA", "GROUP CHAT SINGLE BUDDY" + gbean.getMessageCount());
                                break;
                            }
                        }
                    }
                    exchanges.adapter.notifyDataSetChanged();
                }
                SingleInstance.mainContext.notifyMessageCount();

                // status = getIntent().getStringExtra("status");
                WebServiceReferences.session = sessionid;
                WebServiceReferences.SelectedBuddy = buddy;


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int noScrHeight = displaymetrics.heightPixels;
                int noScrWidth = displaymetrics.widthPixels;
                CallDispatcher.pdialog = new ProgressDialog(context);

                if (WebServiceReferences.callDispatch.containsKey("calldisp"))
                    callDisp = (CallDispatcher) WebServiceReferences.callDispatch
                            .get("calldisp");
                else
                    callDisp = new CallDispatcher(context);

                callDisp.setNoScrHeight(noScrHeight);
                callDisp.setNoScrWidth(noScrWidth);
                displaymetrics = null;
                Log.i("groupchat123", "log enabled :: "
                        + AppReference.isWriteInFile);
                btnBack = (Button) v1.findViewById(R.id.btn_cancel);
                btnBack.setOnClickListener(this);
                settingsMenu = (Button) v1.findViewById(R.id.settingsmenu);
                settingsMenu.setOnClickListener(this);
                profilePic = (ImageView) v1.findViewById(R.id.buddypic);
                buddyName = (TextView) v1.findViewById(R.id.buddyname);
                btn_grid = (ImageView) v1.findViewById(R.id.gridview);
                btn_grid.setOnClickListener(this);

                audio_call = (Button) v1.findViewById(R.id.c_audio_call);
                audio_call.setOnClickListener(this);
                audio_call.setTag(0);
                video_call = (Button) v1.findViewById(R.id.c_video_call);
                video_call.setOnClickListener(this);
                schedule_confirmation = (TextView) v1.findViewById(R.id.schedule);
                private1 = (TextView) v1.findViewById(R.id.private_chk);
                tv_privateReplyUsername=(TextView)v1.findViewById(R.id.tv_privateUsername);
                ll_privateReplyclose=(LinearLayout)v1.findViewById(R.id.ll_close);
                replayback = (TextView) v1.findViewById(R.id.reply_back);
                deadline_urgent = (TextView) v1.findViewById(R.id.deadLine);
                dateLayout = (LinearLayout) v1.findViewById(R.id.date_layout);

//			audioCallBtn1 = (ImageView) v1.findViewById(R.id.audio_call1);
//			audioCallBtn1.setOnClickListener(this);
//			audioCallBtn2 = (ImageView) v1.findViewById(R.id.audio_call2);
//			audioCallBtn2.setOnClickListener(this);
//			videoCallBtn1 = (ImageView) v1.findViewById(R.id.video_call1);
//			videoCallBtn1.setOnClickListener(this);
//			videoCallBtn2 = (ImageView) v1.findViewById(R.id.video_call2);
//			videoCallBtn2.setOnClickListener(this);
                if (isGroup || isRounding) {
//				audioCallBtn1.setBackgroundResource(R.drawable.g_audiocall);
//				audioCallBtn2.setBackgroundResource(R.drawable.g_audioconf);
//				videoCallBtn1.setBackgroundResource(R.drawable.g_videocall);
//				videoCallBtn2.setBackgroundResource(R.drawable.g_videoconf);
                    buddyName.setText(groupBean.getGroupName());
                    if (groupBean.getOwnerName().equalsIgnoreCase(
                            CallDispatcher.LoginUser)) {
                        settingsMenu.setVisibility(View.VISIBLE);
                    } else {
                        settingsMenu.setVisibility(View.GONE);
                    }
                } else {
//				audioCallBtn1.setBackgroundResource(R.drawable.i_audiocall);
//				audioCallBtn2.setBackgroundResource(R.drawable.i_audioconf);
//				videoCallBtn1.setBackgroundResource(R.drawable.i_videocall);
//				videoCallBtn2.setBackgroundResource(R.drawable.i_videoconf);
                    buddyName.setText(buddy);
                    settingsMenu.setVisibility(View.GONE);
                }
                createProfileImageFileAndFolder();
//					loadProfilePic();
                // lv = (ListView) v1.findViewById(R.id.chat_listview);
                lv = (SwipeMenuListView) v1.findViewById(R.id.chat_listview);
                list_all = (LinearLayout) v1.findViewById(R.id.list_sll);
                multi_send=(LinearLayout) v1.findViewById(R.id.sendlay);
                selectAll_container = (RelativeLayout) v1.findViewById(R.id.selectAll_container);
                settingnotifications = (RelativeLayout) v1.findViewById(R.id.settingnotifications);
                selectAll_container.setVisibility(View.GONE);
                selectAll_buddy = (CheckBox) v1.findViewById(R.id.selectAll_buddy);
                countofselection = (TextView)v1.findViewById(R.id.selected);
                forwardlay = (RelativeLayout) v1.findViewById(R.id.forwardlay);
                forwardlay.setVisibility(View.GONE);
                SendListUI = new Vector<SendListUIBean>();
                sendlistadapter = new SendChatListAdapter(GroupChatActivity.this, SendListUI);
                final int adapterCount = sendlistadapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = sendlistadapter.getView(i, null, null);
                    list_all.addView(item);
                }
                if(adapterCount>=2){
                    multi_send.getLayoutParams().height=280;
                }
//                list_all.setAdapter(sendlistadapter);
                lv.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
                    @Override
                    public void onSwipeStart(int position) {
//                        final GroupChatBean gcBean = chatList.get(position);
//                        if (gcBean.getFrom().equals(CallDispatcher.LoginUser)) {
//                            swipeposition = 0;
//                        }else{
//                            swipeposition = 1;
//                        }
                        Log.d("Swiselect", "onSwipeStart : " + position);

                    }

                    @Override
                    public void onSwipeEnd(int position) {

                    }
                });
                SwipeMenuCreator creator = new SwipeMenuCreator() {

                    @Override
                    public void create(SwipeMenu menu) {
                        // Create different menus depending on the view type

                        switch (menu.getViewType()) {
                            case 0:
                                Log.d("Swiselect", " case 0 : " + swipeposition);
                                createMenu1(menu);
                                break;
                            case 1:
                                Log.d("Swiselect", " case 1 : " + swipeposition);
                                createMenu2(menu);
                                break;
                            case 2:
                                Log.d("Swiselect", " case 2 : " + swipeposition);
                                createMenu3(menu);
                                break;

                        }
                    }

                    private void createMenu1(SwipeMenu menu) {
                        SwipeMenuItem openItem = new SwipeMenuItem(context);
                        openItem.setBackground(R.color.grey1);
                        openItem.setWidth(dp2px(90));
                        openItem.setIcon(R.drawable.withdraw_line_white);
                        openItem.setTitleSize(9);
                        openItem.setTitle("WITHDRAW");
                        openItem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(openItem);


                        SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                        deleteItem.setBackground(R.color.green);
                        deleteItem.setWidth(dp2px(90));
                        deleteItem.setTitleSize(9);
                        deleteItem.setTitle("INFO");
                        deleteItem.setTitleColor(Color.WHITE);
                        deleteItem.setIcon(R.drawable.info_line_white);
                        menu.addMenuItem(deleteItem);

                        SwipeMenuItem forward = new SwipeMenuItem(context);
                        forward.setBackground(R.color.blue2);
                        forward.setWidth(dp2px(90));
                        forward.setIcon(R.drawable.forward_line_white);
                        forward.setTitleSize(9);
                        forward.setTitle("FORWARD");
                        forward.setTitleColor(Color.WHITE);
                        menu.addMenuItem(forward);
                    }

                    private void createMenu2(SwipeMenu menu) {
                        SwipeMenuItem forwared = new SwipeMenuItem(context);
                        forwared.setBackground(R.color.blue2);
                        forwared.setWidth(dp2px(90));
                        forwared.setIcon(R.drawable.forward_line_white);
                        forwared.setTitleSize(10);
                        forwared.setTitle("FORWARD");
                        forwared.setTitleColor(Color.WHITE);
                        menu.addMenuItem(forwared);
                    }

                    private void createMenu3(SwipeMenu menu) {
                        SwipeMenuItem calllay = new SwipeMenuItem(context);
                        menu.addMenuItem(calllay);
                    }


                };

                selectAll_buddy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("chatselected", "selection"+buddy);
                        for (int i = 0; i < chatList.size(); i++) {
                            final GroupChatBean b = chatList.get(i);
                            b.setSelect(selectAll_buddy.isChecked());
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        if (selectAll_buddy.isChecked()) {
                            int count = 0;
                            for (GroupChatBean Gcb : chatList) {
                                if (Gcb.isSelect())
                                    count++;
                            }
                            checkBoxCounter = count;
                            countofselection.setText(count + " Selected");
                        }else {
                            checkBoxCounter = 0;
                            countofselection.setText(0 + " Selected");
                        }
                    }
                });

                sidemenu.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cancel.getVisibility()==v.VISIBLE){
                            mDrawerLayout.openDrawer(menu_side);
                        }
                        selectAll_buddy.setChecked(false);
                        forward = false;
                        for(GroupChatBean bean:chatList){
                            bean.setSelect(false);
                            bean.setIsforward(false);
                        }
                        adapter.notifyDataSetChanged();
                        selectAll_container.setVisibility(View.GONE);
                        sendLay.setVisibility(View.VISIBLE);
                        header.setVisibility(View.VISIBLE);
                        audio_call.setVisibility(View.VISIBLE);
                        sidemenu.setBackgroundResource(R.drawable.navigation_menu);
                        cancel.setVisibility(View.VISIBLE);
                        dot.setVisibility(View.VISIBLE);
                        forwardlay.setVisibility(View.GONE);

                    }
                });
                // set creator
                lv.setMenuCreator(creator);


                lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position,
                                                   SwipeMenu menu, int index) {

                        try {
                            final GroupChatBean b = chatList.get(position);
                            if (!b.getFrom().equals(CallDispatcher.LoginUser)) {
                                GroupChatBean gcBean2 = adapter.getItem(position);
                                if (gcBean2.getWithdrawn() != null && gcBean2.getWithdrawn().equals("1"))
                                    showToast("You cannot forward withdrawn message");
                                else {
                                    forward = true;
                                    for(GroupChatBean gbean:adapter.getAllitem()){
                                        gbean.setIsforward(true);
                                    }
                                    adapter.notifyDataSetChanged();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
//                                            swipeposition = 2;
                                            forwardlay.setVisibility(View.VISIBLE);
                                            audio_call.setVisibility(View.GONE);
                                            settingnotifications.getLayoutParams().height = 100;
                                            selectAll_container.setVisibility(View.VISIBLE);
                                            sendLay.setVisibility(View.GONE);
                                            header.setVisibility(View.GONE);
                                            sidemenu.setBackgroundResource(R.drawable.navigation_close);
                                            cancel.setVisibility(View.GONE);
                                            dot.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }

                            } else {
                                switch (index) {
                                    case 0:
                                        Log.i("select", "position 0");
                                        GroupChatBean gcBean1 = adapter.getItem(position);
                                        if (gcBean1.getWithdrawn() != null && gcBean1.getWithdrawn().equals("1"))
                                        showToast("Message already withdrawn");
                                        else {
                                            gcBean1.setWithdraw(true);
                                            adapter.notifyDataSetChanged();
                                        }

                                        break;
                                    case 1:
                                        GroupChatBean gc = adapter.getItem(position);
                                        Intent intent = new Intent(context,
                                                ChatInfo.class);
                                        intent.putExtra("chatlistindividual", gc);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        GroupChatBean gcBean2 = adapter.getItem(position);
                                        if (gcBean2.getWithdrawn() != null && gcBean2.getWithdrawn().equals("1"))
                                            showToast("You cannot forward withdrawn message");
                                        else {
                                            GroupChatBean groupChatBean = adapter.getItem(position);
                                            groupChatBean.setSelect(true);
                                            for(GroupChatBean gbean:adapter.getAllitem()){
                                                gbean.setIsforward(true);
                                            }
                                            forward = true;
                                            adapter.notifyDataSetChanged();
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    forwardlay.setVisibility(View.VISIBLE);
                                                    audio_call.setVisibility(View.GONE);
                                                    settingnotifications.getLayoutParams().height = 100;
                                                    selectAll_container.setVisibility(View.VISIBLE);
                                                    sendLay.setVisibility(View.GONE);
                                                    header.setVisibility(View.GONE);
                                                    sidemenu.setBackgroundResource(R.drawable.navigation_close);
                                                    cancel.setVisibility(View.GONE);
                                                    dot.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }


                                        break;
                                }
                            }
                            return false;
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            return false;
                        }
                    }
                });
                message = (EditText) v1.findViewById(R.id.txt_msg);
                message.addTextChangedListener(GroupChatActivity.this);
                message.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            if(isPrivateBack){
                                LL_privateReply.setVisibility(View.GONE);
                            }else {
                                msgoptionview.setVisibility(View.GONE);
                            }
                            audio_call.setTag(0);
                            audio_call.setBackgroundResource(R.drawable.dashboard_call_white);
                            schedule_confirmation.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                            private1.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                            replayback.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                            deadline_urgent.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        }

                        int count = message.getLineCount();

//                        int height = relative_send_layout.getMeasuredHeight();
//                        if (count > lastcount && count > 1 && count < 6) {
//                            relative_send_layout.getLayoutParams().height = height + 30;
//                        } else if (count < lastcount && count > 1) {
//                            relative_send_layout.getLayoutParams().height = height - 30;
//                        if (count == 0 || count == 1) {
//                            relative_send_layout.getLayoutParams().height = 110;
//                            if (SendListUI.size() > 0) {
//                                relative_send_layout.getLayoutParams().height = 400;
//                            }
//                        }

                        lastcount = count;
                        UdpMessageBean bean = new UdpMessageBean();
                        GroupChatBean gcBean = new GroupChatBean();
                        bean.setType("105");
                        gcBean.setSenttime(getCurrentDateandTime());
                        if (isGroup || isRounding) {
                            gcBean.setTo(groupBean.getGroupId());
                            gcBean.setSessionid(groupBean.getGroupId());
                            gcBean.setGroupId(groupBean.getGroupId());
                            gcBean.setCategory("G");
                        } else {
                            gcBean.setTo(buddy);
                            gcBean.setSessionid(CallDispatcher.LoginUser + buddy);
                            gcBean.setGroupId(buddy);
                            gcBean.setCategory("I");
                            gcBean.setFrom(CallDispatcher.LoginUser);
                            gcBean.setTo(buddy);
                        }

                        if (message.getText().length() > 0) {
                            gcBean.setResult("0");
//                           SingleInstance.mainContext.ReadMessageAck(bean);

                        } else {
                            gcBean.setResult("1");
                        }
                        bean.setResponseObject(gcBean);
                        SingleInstance.mainContext.ReadMessageAck(bean);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
//                        Log.d("txtxhng","beforeTextChanged ");
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
//                        Log.d("txtxhng","onTextChanged ");
                        if (s.length() != 0) {
                            audio_call.setBackgroundResource(R.drawable.chat_send);
                            audio_call.setTag(1);
                            if(isPrivateBack){
                                LL_privateReply.setVisibility(View.VISIBLE);
                            }else {
                                msgoptionview.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                });
                message.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
//                        Log.d("txtxhng","onTouch ");
//
//					if (chatList != null && chatList.size() > 0) {
//						mainListPositionForSpecialMessage(chatList.size() - 1);
//					}
                        btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
                        atachlay.setVisibility(View.GONE);
                        audio_layout.setVisibility(View.GONE);
                        isGrid = false;
                        lv.setSelection(lv.getAdapter().getCount() - 1);
                        processAttachmentView();
                        return false;
                    }
                });

                sendBtn = (Button) v1.findViewById(R.id.send_image);
                atachlay = (LinearLayout) v1.findViewById(R.id.attachment_layout);
                sendLay = (RelativeLayout) v1.findViewById(R.id.send_layout);
                rel_quoted = (RelativeLayout) v1.findViewById(R.id.rel_quoted);
                TextView noaccess = (TextView) v1.findViewById(R.id.noaccess);
                if (isGroup || isRounding) {
                    GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                            .getGroupChatPermission(groupBean);
                    if (gcpBean.getChat().equalsIgnoreCase("1")) {
                        atachlay.setVisibility(View.VISIBLE);
                        sendLay.setVisibility(View.VISIBLE);
                        noaccess.setVisibility(View.GONE);
                    } else {
                        atachlay.setVisibility(View.GONE);
                        sendLay.setVisibility(View.GONE);
                        noaccess.setVisibility(View.VISIBLE);
                    }
                }
                final LinearLayout msgoptionview = (LinearLayout) v1.findViewById(R.id.splmsglay1);

                attachBtn = (ImageView) v1.findViewById(R.id.attach);
                attachBtn.setTag(0);
                if (isGroup || isRounding) {
                    GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                            .getGroupChatPermission(groupBean);
                    if (gcpBean.getChat().equalsIgnoreCase("1"))
                        attachBtn.setVisibility(View.GONE);
                    else
                        attachBtn.setVisibility(View.INVISIBLE);
                }
                attachBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final CharSequence[] attach = {(SingleInstance.mainContext.getResources().getString(R.string.image_from_camera)), (SingleInstance.mainContext.getResources().getString(R.string.audio_recording)), (SingleInstance.mainContext.getResources().getString(R.string.video_recording)), (SingleInstance.mainContext.getResources().getString(R.string.draw_sketch)), (SingleInstance.mainContext.getResources().getString(R.string.forms)), (SingleInstance.mainContext.getResources().getString(R.string.current_loc)), (SingleInstance.mainContext.getResources().getString(R.string.cancel))};

                        AlertDialog.Builder builder = new AlertDialog.Builder(GroupChatActivity.this);
                        builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.make_your_selection));
                        builder.setItems(attach, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
//				                mDoneButton.setText(items[item]);
                                NotePickerScreen.isOld = false;
                                if (item == 0) {
                                    if (isGroup || isRounding) {
                                        GroupChatPermissionBean gcpBean = SingleInstance.mainContext.getGroupChatPermission(groupBean);
                                        if (gcpBean.getChat().equalsIgnoreCase("1")) {
                                            photochat();
                                        } else {
                                            showToast("Sorry you dont have permission");
                                        }
                                    } else {
                                        photochat();
                                    }
                                } else if (item == 1) {

                                    if (isGroup || isRounding) {
                                        GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                                                .getGroupChatPermission(groupBean);
                                        if (gcpBean.getChat().equalsIgnoreCase("1")) {
                                            showAudioMessageDialog();
                                        } else {
                                            showToast("Sorry you dont have permission");
                                        }
                                    } else {
                                        showAudioMessageDialog();
                                    }

                                } else if (item == 2) {


                                    if (isGroup || isRounding) {
                                        GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                                                .getGroupChatPermission(groupBean);
                                        if (gcpBean.getChat().equalsIgnoreCase("1")) {
                                            showVideoMessageDialog();
                                        } else {
                                            showToast("Sorry you dont have permission");
                                        }
                                    } else {
                                        showVideoMessageDialog();
                                    }

                                } else if (item == 3) {

                                    if (isGroup || isRounding) {
                                        GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                                                .getGroupChatPermission(groupBean);
                                        if (gcpBean.getChat().equalsIgnoreCase("1")) {
                                            // Intent intent = new Intent(context,
                                            // HandSketchActivity.class);
                                            Intent intent = new Intent(context,
                                                    HandSketchActivity2.class);
                                            startActivityForResult(intent, 36);
                                        } else {
                                            showToast("Sorry you dont have permission");
                                        }
                                    } else {
                                        // Intent intent = new Intent(context,
                                        // HandSketchActivity.class);
                                        Intent intent = new Intent(context,
                                                HandSketchActivity2.class);
                                        startActivityForResult(intent, 36);
                                    }
                                } else if (item == 4) {
                                    if (isGroup || isRounding) {
                                        showFormDialog();
                                    } else {
                                        showFormDialog();
                                    }

                                } else if (item == 5) {

                                    if (CallDispatcher.LoginUser != null) {
                                        if (CallDispatcher.latitude == 0.0
                                                && CallDispatcher.longitude == 0.0) {
                                            showToast("Sorry! Turn On Location Service ");
                                        } else {
                                            sendMsg("Latitude:" + CallDispatcher.latitude + ","
                                                            + "Longitude:" + CallDispatcher.longitude,
                                                    null, "location", null);
                                        }
                                    } else {
                                        showToast("Sorry! can not send Message");
                                    }
                                } else if (item == 6) {

                                    dialog.cancel();

                                }


                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                });
//			RelativeLayout btn_image = (RelativeLayout) v1.findViewById(R.id.btn_image);
//			btn_image.setOnClickListener(this);
//			RelativeLayout btn_handsketch = (RelativeLayout) v1.findViewById(R.id.btn_handsketch);
//			btn_handsketch.setOnClickListener(this);
//			RelativeLayout btn_audio = (RelativeLayout) v1.findViewById(R.id.btn_audio);
//			btn_audio.setOnClickListener(this);
//			RelativeLayout btn_video = (RelativeLayout) v1.findViewById(R.id.btn_video);
//			btn_video.setOnClickListener(this);
//			RelativeLayout btn_location = (RelativeLayout) v1.findViewById(R.id.btn_location);
//			btn_location.setOnClickListener(this);
//			RelativeLayout btn_forms = (RelativeLayout) v1.findViewById(R.id.btn_forms);
//			btn_forms.setOnClickListener(this);
                attachment_layout = (LinearLayout) v1.findViewById(R.id.attachment_layout);
                attachment_layout.setVisibility(View.GONE);
                memlist_splmsg = (ListView) v1.findViewById(R.id.memlist_splmsg);
                memlist_splmsg.setVisibility(View.GONE);
                message.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        memlist_splmsg.setVisibility(View.GONE);
                    }
                });

                adapter = new GroupChatAdapter(GroupChatActivity.this, chatList);
                CallDispatcher.pdialog = new ProgressDialog(context);
                lv.setAdapter(adapter);

                searchet.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        if(searchet!=null && searchet.getText().toString().length()>0 && adapter!=null) {
                            Log.i("filter","afterTextChanged");
                            String text = searchet.getText().toString().toLowerCase(Locale.getDefault());
                            adapter.filter(text);
                            iv_searchclear.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub
                        adapter.filter("");
                        iv_searchclear.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                        // TODO Auto-generated method stub
                    }
                });




                loadGroupMembers(groupId);
                mem_adapter = new BuddyAdapter(GroupChatActivity.this, membersList);
                memlist_splmsg.setAdapter(mem_adapter);

                lv.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
                    @Override
                    public void onSwipeStart(int position) {
                        itemID = position;
                        InputMethodManager imm = (InputMethodManager) getSystemService( INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        Log.d("Swiselect1", "onSwipeStart : " + position);

                    }

                    @Override
                    public void onSwipeEnd(int position) {
                        Log.d("Swiselect1", "onSwipeEnd : " + position);
                    }
                });

                sendBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            if(isPrivateBack){
                                LL_privateReply.setVisibility(View.GONE);
                            }else {
                                msgoptionview.setVisibility(View.GONE);
                            }

                            // TODO Auto-generated method stub


//                            if (SendListUI.size() == 1) {
//                                Log.i("audioplay", "path--->" + strIPath);
//                                SendListUIBean bean = SendListUI.get(0);
//                                sendMsg(message.getText().toString().trim(),
//                                        bean.getPath(), bean.getType(), null);
//                                message.setVisibility(View.VISIBLE);
//                                SendListUI.remove(0);
//                                if (SendListUI.size() == 0) {
//                                    SendListUI.clear();
//                                }
//                                sendlistadapter.notifyDataSetChanged();
//                                list_all.removeAllViews();
//                                final int adapterCount = sendlistadapter.getCount();
//
//                                for (int i = 0; i < adapterCount; i++) {
//                                    View item = sendlistadapter.getView(i, null, null);
//                                    list_all.addView(item);
//                                }
////                                relative_send_layout.getLayoutParams().height = 90;
//                            } else if (SendListUI.size() > 1) {
//                                String path = null;
//                                for (int i = 0; i < SendListUI.size(); i++) {
//                                    SendListUIBean bean = SendListUI.get(i);
//                                    if (path == null) {
//                                        path = bean.getPath();
//                                    } else {
//                                        path = path + "," + bean.getPath();
//                                    }
//                                }
//
//
//                                sendMsg(message.getText().toString().trim(),
//                                        path, "mixedfile", null);
//                                message.setVisibility(View.VISIBLE);
//                                SendListUI.remove(0);
//                                if (SendListUI.size() > 0) {
//                                    SendListUI.clear();
//                                }
//                                sendlistadapter.notifyDataSetChanged();
//                                list_all.removeAllViews();
//                                final int adapterCount = sendlistadapter.getCount();
//
//                                for (int i = 0; i < adapterCount; i++) {
//                                    View item = sendlistadapter.getView(i, null, null);
//                                    list_all.addView(item);
//                                }
////                                relative_send_layout.getLayoutParams().height = 90;
//
//
//                            } else {
//                                if (message.getText().toString().trim().length() > 0) {
//                                    if (CallDispatcher.LoginUser != null) {
//
//                                        if (message.getText().toString().length() > 700) {
//                                            showToast("Text exceeds 700 characters");
//                                        } else {
//                                            sendMsg(message.getText().toString().trim(),
//                                                    null, "text", null);
//                                            message.setText("");
//                                        }
//
//                                    } else {
//                                        showAlert1("Info", "Check Internet Connection");
//                                        // Toast.makeText(getApplicationContext(),
//                                        // "Kindly login", Toast.LENGTH_LONG)
//                                        // .show();
//                                    }
//
//                                } else {

//
                               isGrid = true;
                               atachlay.setVisibility(View.GONE);
                               if (DBAccess.getdbHeler(context).getChatTemplates() != null) {
                                   Intent intent = new Intent(context, ChatTemplateActivity.class);
                                   startActivityForResult(intent, 12);
                               } else {
                                   showAlert1("Info", "No templates Values In DB");
                               }

                                    // showToast("Sorry, cant able to send empty message");
//                                }
//                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
                profilePic.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        if (!isGroup) {
                            showBuddyDialog();
                        } else {
                            showBuddyDialog();

                        }

                    }
                });
                Log.i("chat", "chat123 end of oncreate");
                if (chatList != null && chatList.size() > 0) {
                    mainListPositionForSpecialMessage(chatList.size() - 1);
                }

                replayback.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // Toast.makeText(GroupChatActivity.this,"thisss",
                        // Toast.LENGTH_LONG).show();
                        schedule_confirmation.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        private1.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        replayback.setBackgroundColor(context.getResources().getColor(R.color.blue2));
                        deadline_urgent.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        isReplyclicked = true;
                        isconfirmclicked = false;
                        isprivateclicked = false;
                        isurgentclicked = false;

                    }

                });

                schedule_confirmation.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // Toast.makeText(GroupChatActivity.this,"schedule",
                        // Toast.LENGTH_LONG).show();
                        schedule_confirmation.setBackgroundColor(context.getResources().getColor(R.color.blue2));
                        private1.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        replayback.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        deadline_urgent.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        isconfirmclicked = true;
                        isprivateclicked = false;
                        isurgentclicked = false;
                        isReplyclicked = false;

                    }
                });


                private1.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        schedule_confirmation.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        private1.setBackgroundColor(context.getResources().getColor(R.color.blue2));
                        replayback.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        deadline_urgent.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        isprivateclicked = true;
                        isconfirmclicked = false;
                        isurgentclicked = false;
                        isReplyclicked = false;
                        memlist_splmsg.setVisibility(View.VISIBLE);
                        if(membersList!=null && membersList.size()>0){
                            for(UserBean userBean:membersList){
                                userBean.setSelected(false);
                            }
                        }

                        if(mem_adapter!=null){
                            mem_adapter.notifyDataSetChanged();
                        }
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
                        } catch (Exception e) {
                        }
//					Toast.makeText(GroupChatActivity.this,"private", Toast.LENGTH_LONG).show();
//					Intent intent = new Intent(context,
//							MemberListActivity.class);
//					intent.putExtra("groupid", groupId);
//					startActivityForResult(intent, 101);
                    }
                });

                ll_privateReplyclose.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isPrivateBack=false;
                        PrivateReply_view=null;
                        privateParentID=null;
                        privateReply_username=null;
                        LL_privateReply.setVisibility(View.GONE);
                    }
                });


                deadline_urgent.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        schedule_confirmation.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        private1.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        replayback.setBackgroundColor(context.getResources().getColor(R.color.snazgray));
                        deadline_urgent.setBackgroundColor(context.getResources().getColor(R.color.blue2));
                        isurgentclicked = true;
                        isprivateclicked = false;
                        isconfirmclicked = false;
                        isReplyclicked = false;
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
                        } catch (Exception e) {
                        }
//                    if (isGroup) {
//                        Intent intent = new Intent(context,
//                                MemberListActivity.class);
//                        intent.putExtra("groupid", groupId);
//                        startActivityForResult(intent, 104);
//                    } else
//                        sendWithDeadline(buddy);
                    }
                });


                if (!isGroup && !isRounding) {
                    private1.setVisibility(View.GONE);

                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }

    public void updateBuddy(final String userName, final String bibStatus) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("DCBA", "Alpha beeta gama " + userName + bibStatus);
                Log.i("status","updateBuddy method username-->"+userName+"  bibstatus---->"+bibStatus);
                buddyStatus.put(userName, bibStatus);

                if (userName!=null &&userName.equalsIgnoreCase(buddy)) {
                    if (img_status != null) {
                        if (bibStatus.equals("0")) {
                            img_status.setBackgroundResource(R.drawable.offline_icon);
                        } else if (bibStatus.equals("1")) {
                            img_status.setBackgroundResource(R.drawable.online_icon);

                        } else if (bibStatus.equals("2")) {
                            img_status.setBackgroundResource(R.drawable.busy_icon);

                        } else if (bibStatus.equals("3")) {
                            img_status.setBackgroundResource(R.drawable.invisibleicon);

                        } else if (bibStatus.equals("4")) {
                            img_status.setBackgroundResource(R.drawable.busy_icon);
                        } else {
                            img_status.setBackgroundResource(R.drawable.offline_icon);
                        }
                        adapter.notifyDataSetChanged();
                    }


                }
            }
        });
    }

    public void handsketch() {
        if (isGroup || isRounding) {
            GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                    .getGroupChatPermission(groupBean);
            if (gcpBean.getChat().equalsIgnoreCase("1")) {
                // Intent intent = new Intent(context,
                // HandSketchActivity.class);
                Intent intent = new Intent(context,
                        HandSketchActivity2.class);
                startActivityForResult(intent, 36);
            } else {
                showToast("Sorry you dont have permission");
            }
        } else {
            // Intent intent = new Intent(context,
            // HandSketchActivity.class);
            Intent intent = new Intent(context,
                    HandSketchActivity2.class);
            startActivityForResult(intent, 36);
        }
    }
    protected void showAlert1(String string) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        try {
//			if (attachBtn.getTag().equals(1)) {
//				processAttachmentView();
//				return;
//			}
            super.onBackPressed();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendMsg(String message, String localpath, String type,
                        SpecialMessageBean spBean) {

        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isPrivateBack) {
                        LL_privateReply.setVisibility(View.GONE);
                    } else {
                        msgoptionview.setVisibility(View.GONE);
                    }
                    rel_quoted.setVisibility(View.GONE);

                    audio_call.setTag(0);
                    audio_call.setBackgroundResource(R.drawable.dashboard_call_white);
                }
            });

            if (CallDispatcher.LoginUser != null
                    && SingleInstance.mainContext
                    .isNetworkConnectionAvailable()) {
                Log.i("group0123", "message1 : " + message);
                GroupChatBean gcBean = new GroupChatBean();
                gcBean.setFrom(CallDispatcher.LoginUser);
                gcBean.setType("100");
                gcBean.setMimetype(type);
                gcBean.setUnreadStatus(1);
                if (forward) {
                    gcBean.setUnview("1");
                }

                if (type.equals("text") || type.equals("location")) {

                    final Pattern urlPattern = Pattern.compile(
                            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                    String [] parts = message.split("\\s+");
                    for( String item : parts ) {
                        if(urlPattern.matcher(item).matches()) {
                            gcBean.setMimetype("link");
                            break;
                        }
                    }
                    if (isGroup || isRounding) {
                        GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                                .getGroupChatPermission(groupBean);
                        if (!gcpBean.getChat().equalsIgnoreCase("1")) {
                            Log.i("new", "GROUP CHAT ACTIVITY" + gcpBean.getTextMessage());
                            showToast("Sorry you dont have permission");
                            return;
                        }
                    }
                    if (isReplyBack) {
                        Log.i("AAAA","Reply back ");
                        if (spBean == null) {
                            spBean = new SpecialMessageBean();
                        }
                        if (pId != null) {
                            spBean.setParentId(pId);
                        }
                        spBean.setSubcategory("grb");
                        if (privateMembers != null) {
                            spBean.setMembers(privateMembers);
                        }
                    }
                    gcBean.setMessage(message);
                    if (isconfirmBack) {
                        isconfirmBack = false;

                        if (spBean == null) {
                            spBean = new SpecialMessageBean();
                        }
                        if (pId != null) {
                            spBean.setParentId(pId);
                        }
                        gcBean.setUnview("1");
                        spBean.setSubcategory("gc");
                        if (privateMembers != null) {
                            spBean.setMembers(privateMembers);
                        }
                    }
                    Log.i("group0123", "message11 : " + gcBean.getMessage());
                } else {
                    gcBean.setMessage(message);
                    gcBean.setMediaName(localpath);
                    gcBean.setFtpUsername(CallDispatcher.LoginUser);
                    gcBean.setFtpPassword(CallDispatcher.Password);
                    gcBean.setProgress(0);
                    gcBean.setStatus(0);
                }
                gcBean.setSignalid(Utility.getSessionID());
                gcBean.setSenttime(getCurrentDateandTime());
                gcBean.setSenttimez("GMT");
                gcBean.setDateandtime(getCurrentDateandTime());
                if (isGroup || isRounding) {

                    gcBean.setTo(groupBean.getGroupId());
                    gcBean.setSessionid(groupBean.getGroupId());
                    gcBean.setGroupId(groupBean.getGroupId());
                    gcBean.setCategory("G");
                } else {
                    gcBean.setTo(buddy);
//                    gcBean.setSessionid(CallDispatcher.LoginUser + buddy);
                    TreeSet<String> treeSet=new TreeSet<>();
                    treeSet.add(CallDispatcher.LoginUser);
                    treeSet.add(buddy);String chatnames=null;
                    for(String s:treeSet){
                        Log.i("name","name-->"+s);
                        if(chatnames==null){
                            chatnames=s;
                        }else{
                            chatnames=chatnames+s;
                        }}
                    Log.i("name","chatnames-->"+chatnames);
                    if(chatnames!=null)gcBean.setSessionid(chatnames);
                    gcBean.setGroupId(buddy);
                    gcBean.setCategory("I");
                    gcBean.setSubCategory(null);
                }
                if (isReplyBack) {
                    Log.i("AAAA", "Reply back mixed file ");
                    if (spBean == null) {
                        spBean = new SpecialMessageBean();
                    }
                    if (pId != null) {
                        spBean.setParentId(pId);
                    }
                    spBean.setSubcategory("grb");
                    if (privateMembers != null) {
                        spBean.setMembers(privateMembers);
                    }
                }
                if (isconfirmBack) {
                    isconfirmBack = false;

                    if (spBean == null) {
                        spBean = new SpecialMessageBean();
                    }
                    if (pId != null) {
                        spBean.setParentId(pId);
                    }
                    gcBean.setUnview("1");
                    spBean.setSubcategory("gc");
                    if (privateMembers != null) {
                        spBean.setMembers(privateMembers);
                    }
                }
                if (spBean != null) {
                    if (spBean.getSubcategory() != null) {
                        gcBean.setSubCategory(spBean.getSubcategory());
                        if (spBean.getMembers() != null) {
                            gcBean.setPrivateMembers(spBean.getMembers());
                        }
                        if (spBean.getSubcategory().equalsIgnoreCase("gs")) {
                            gcBean.setReminderTime(spBean.getRemindertime());
                        } else if (spBean.getSubcategory().equalsIgnoreCase(
                                "gdi")
                                || spBean.getSubcategory().equalsIgnoreCase(
                                "gd")
                                || (spBean.getSubcategory()
                                .equalsIgnoreCase("grb"))) {

                            gcBean.setReminderTime(spBean.getRemindertime());
                            if (spBean.getParentId() != null) {
                                gcBean.setParentId(spBean.getParentId());
                            } else
                                gcBean.setParentId(Utility.getSessionID());
                        } else if (spBean.getSubcategory().equalsIgnoreCase("gc")) {
                            if (spBean.getParentId() != null) {
                                gcBean.setParentId(spBean.getParentId());
                            } else
                                gcBean.setParentId(Utility.getSessionID());
                        } else if (spBean.getSubcategory().equalsIgnoreCase("gu")) {
                            if (spBean.getParentId() != null) {
                                gcBean.setParentId(spBean.getParentId());
                            } else
                                gcBean.setParentId(Utility.getSessionID());
                        }else if(spBean.getSubcategory().equalsIgnoreCase("gp")){
                            if (spBean.getParentId() != null) {
                                gcBean.setParentId(spBean.getParentId());
                            } else
                                gcBean.setParentId(Utility.getSessionID());
                        }
                    }
                }
                if (spBean != null && spBean.getSubcategory() != null && spBean.getSubcategory().equalsIgnoreCase("grb")) {
                    for (int i = 0; i < chatList.size(); i++) {
                        Log.i("AAAA","reply back mixed file grb_r  000000");
                        GroupChatBean gcBean1 = chatList.get(i);
                        if (gcBean1.getParentId()!=null&&gcBean1.getParentId().equals(gcBean.getParentId())) {
                            Log.i("AAAA","reply back mixed file grb_r");
                            gcBean.setSubCategory("GRB_R");
                            gcBean.setComment(getReplyMessage(gcBean1));
                            gcBean1.setSubCategory("GRB_R");
                            gcBean.setReply("GRB_R");
                            gcBean1.setReply("GRB_R");
                            int row = DBAccess.getdbHeler(
                                    SipNotificationListener.getCurrentContext())
                                    .updateChatReplied(gcBean1);
                            gcBean1.setReplied("reply");
                        }
                    }
                } else if (spBean != null && spBean.getSubcategory() != null && spBean.getSubcategory().equalsIgnoreCase("gc")) {
                    for (int i = 0; i < chatList.size(); i++) {
                        GroupChatBean gcBean1 = chatList.get(i);
                        if (gcBean1.getParentId()!=null&&gcBean1.getParentId().equals(gcBean.getParentId())) {
                            gcBean.setSubCategory("gc_r");
                            gcBean1.setSubCategory("gc_r");
                            gcBean.setReply("gc_r");
                            gcBean1.setReply("gc_r");
                            gcBean1.setReplied("reply");
                        }
                    }
                }else if (spBean != null && spBean.getSubcategory() != null && spBean.getSubcategory().equalsIgnoreCase("gp_r")) {
                    if(privateParentID!=null)
                    gcBean.setParentId(privateParentID);
                    gcBean.setReply("gp_r");
                    for (int i = 0; i < chatList.size(); i++) {
                        GroupChatBean gcBean1 = chatList.get(i);
                        if (gcBean1.getParentId()!=null&&gcBean1.getParentId().equalsIgnoreCase(gcBean.getParentId())) {
                            gcBean1.setReply("gp_r");
//                            int row = DBAccess.getdbHeler(
//                                    SipNotificationListener.getCurrentContext())
//                                    .updatePrivateReply(gcBean1);
                            break;

                        }
                    }

                }
                if (type.equals("text") || type.equals("location")) {
                    Log.i("group0123", "message2 : " + gcBean.getMessage());
                    SingleInstance.getGroupChatProcesser().getQueue()
                            .addObject(gcBean);
                } else {
                    Log.d("group123", "file path " + gcBean.getMediaName());

                    // start GK 07.10.2015 changes

                    imageViewer.addImage(gcBean.getMediaName());

                    // ended 07-10-15 changes

                    uploadFile(gcBean);
                }


                if (gcBean.getSubCategory() != null) {
                    if (gcBean.getSubCategory().equalsIgnoreCase("grb") || gcBean.getSubCategory().equalsIgnoreCase("GRB_R")) {
                        if (gcBean.getParentId() != null
                                && gcBean.getParentId().length() > 0
                                && !gcBean.getParentId().equalsIgnoreCase(
                                "null")) {
                            //Old Code cmd
//                            int position = -1;
//                            for (int i = 0; i < chatList.size(); i++) {
//                                GroupChatBean gcBean1 = chatList.get(i);
//
//                                if (gcBean1 != null
//                                        && gcBean1.getParentId() != null
//                                        && gcBean1.getParentId().equals(
//                                        gcBean.getParentId())) {
//                                    position = i;
//                                }
//
//                            }
//                            if (position == -1) {
                                chatList.add(gcBean);
                                adapter.notifyDataSetChanged();
                                maintainListPosition();
//                            } else {
//                                chatList.add(position + 1, gcBean);
//                                adapter.notifyDataSetChanged();
//                                lv.setSelection(position);
//                                maintainListPosition();
//                            }
                            //Old Code cmd

                        } else {
                            chatList.add(gcBean);
                            maintainListPosition();
                        }
                    } else if (gcBean.getSubCategory().equalsIgnoreCase("gdi")) {
                        if (gcBean.getParentId() != null
                                && gcBean.getParentId().length() > 0
                                && !gcBean.getParentId().equalsIgnoreCase(
                                "null")) {
                            Vector<GroupChatBean> deadLineList = new Vector<GroupChatBean>();
                            int position = -1;
                            for (int i = 0; i < chatList.size(); i++) {
                                GroupChatBean gcBean1 = chatList.get(i);
                                if (gcBean1 != null
                                        && gcBean1.getParentId() != null
                                        && gcBean1.getParentId().equals(
                                        gcBean.getParentId())) {
                                    String msg = gcBean1.getMessage()
                                            + "\nStatus : "
                                            + gcBean.getMessage();
//									gcBean1.setMessage(msg);
                                    lv.setSelection(i);
                                    break;
                                }
                            }
                        } else {
                            chatList.add(gcBean);
                            maintainListPosition();
                        }

                    } else {
                        chatList.add(gcBean);
                        maintainListPosition();
                    }
                } else {
                    chatList.add(gcBean);
                    maintainListPosition();
                }
                if (gcBean.getUnview() != null && gcBean.getUnview().equals("1")) {
                    chatList.remove(gcBean);
                }
                if (SingleInstance.groupChatHistory.containsKey(gcBean
                        .getGroupId())) {
                    Log.i("AAAA","group chat remove");

                    SingleInstance.groupChatHistory.remove(gcBean.getGroupId());
                    // SingleInstance.groupChatHistory.get(gcBean.getGroupId())
                    // .add(gcBean);
                    SingleInstance.groupChatHistory.put(gcBean.getGroupId(),
                            chatList);
                } else {
                    SingleInstance.groupChatHistory.put(gcBean.getGroupId(),
                            chatList);
                }
                adapter.notifyDataSetChanged();
                maintainListPosition();
            } else {
                if (!SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                    showToast("Sorry no internet connection");
                } else {
                    showToast("Kindly login to send message");
                }
            }
            this.message.setText("");
            // hideKeyboard();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        pId = null;
        privateMembers = null;
        isReplyBack = false;
        isUrgent=false;
        isconfirmBack=false;
        isPrivateBack=false;
        PrivateReply_view=null;
        privateParentID=null;
        privateReply_username=null;

        if(current_open_activity_detail.containsKey("message")){
            current_open_activity_detail.remove("message");
        }
        if(current_open_activity_detail.containsKey("firstimage1")) {
            current_open_activity_detail.remove("firstimage1");
        }if(current_open_activity_detail.containsKey("firstimage2")){
            current_open_activity_detail.remove("firstimage2");
        }if(current_open_activity_detail.containsKey("firstimage3")){
            current_open_activity_detail.remove("firstimage3");
        }if(current_open_activity_detail.containsKey("secondimage1")) {
            current_open_activity_detail.remove("secondimage1");
        }if(current_open_activity_detail.containsKey("secondimage2")){
            current_open_activity_detail.remove("secondimage2");
        }if(current_open_activity_detail.containsKey("secondimage3")){
            current_open_activity_detail.remove("secondimage3");
        }if(current_open_activity_detail.containsKey("thirdimage1")) {
            current_open_activity_detail.remove("thirdimage1");
        }if(current_open_activity_detail.containsKey("thirdimage2")){
            current_open_activity_detail.remove("thirdimage2");
        }if(current_open_activity_detail.containsKey("thirdimage3")){
            current_open_activity_detail.remove("thirdimage3");
        }if(current_open_activity_detail.containsKey("audio")) {
            current_open_activity_detail.remove("audio");
        }if(current_open_activity_detail.containsKey("audio1")){
            current_open_activity_detail.remove("audio1");
        }if(current_open_activity_detail.containsKey("audio2")){
            current_open_activity_detail.remove("audio2");
        }if(current_open_activity_detail.containsKey("firstvideo1")) {
            current_open_activity_detail.remove("firstvideo1");
        }if(current_open_activity_detail.containsKey("firstvideo2")){
            current_open_activity_detail.remove("firstvideo1");
        }if(current_open_activity_detail.containsKey("firstvideo3")){
            current_open_activity_detail.remove("firstvideo3");
        }if(current_open_activity_detail.containsKey("secondvideo1")) {
            current_open_activity_detail.remove("secondvideo1");
        }if(current_open_activity_detail.containsKey("secondvideo2")){
            current_open_activity_detail.remove("secondvideo2");
        }if(current_open_activity_detail.containsKey("secondvideo3")){
            current_open_activity_detail.remove("secondvideo3");
        }if(current_open_activity_detail.containsKey("thirdvideo1")) {
            current_open_activity_detail.remove("thirdvideo1");
        }if(current_open_activity_detail.containsKey("thirdvideo2")){
            current_open_activity_detail.remove("thirdvideo2");
        }if(current_open_activity_detail.containsKey("thirdvideo3")){
            current_open_activity_detail.remove("thirdvideo3");
        }if(current_open_activity_detail.containsKey("sketch1")) {
            current_open_activity_detail.remove("sketch1");
        }if(current_open_activity_detail.containsKey("sketch2")){
            current_open_activity_detail.remove("sketch2");
        }if(current_open_activity_detail.containsKey("sketch3")){
            current_open_activity_detail.remove("sketch3");
        }if(current_open_activity_detail.containsKey("document1")) {
            current_open_activity_detail.remove("document1");
        }if(current_open_activity_detail.containsKey("document2")){
            current_open_activity_detail.remove("document2");
        }if(current_open_activity_detail.containsKey("document3")){
            current_open_activity_detail.remove("document3");
        }
        msgoptionview.setVisibility(View.GONE);

    }

    private void uploadFile(GroupChatBean gBean) {

        try {
            if (CallDispatcher.LoginUser != null) {

                AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
                        .get("MAIN");
                String fpath = null;
                GroupChatBean gcBean = (GroupChatBean) gBean.clone();
                String[] paths = gcBean.getMediaName().split(",");
                if (paths.length > 0) {
                    for (int i = 0; i < paths.length; i++) {
                        File file = new File(paths[i]);
                        {
                            if (fpath == null) {
                                fpath = file.getName();
                            } else {
                                fpath = fpath + "," + file.getName();
                            }
                        }
                        gcBean.setMediaName(fpath);
                    }
                    fpath = null;
                } else {
                    File file = new File(gcBean.getMediaName());
                    gcBean.setMediaName(file.getName());
                }
                ConnectionBrokerServerBean cBean = ((AppMainActivity) SingleInstance.contextTable
                        .get("MAIN")).cBean;
                ChatFTPBean chatFTPBean = new ChatFTPBean();
                chatFTPBean.setServerIp(cBean.getRouter().split(":")[0]);
                chatFTPBean.setServerPort(40400);
                chatFTPBean.setUsername(CallDispatcher.LoginUser);
                chatFTPBean.setPassword(CallDispatcher.Password);
                chatFTPBean.setInputFile(gBean.getMediaName());
                chatFTPBean.setOperation("UPLOAD");
                chatFTPBean.setSourceObject(gcBean);
                chatFTPBean.setCallback(appMainActivity);
                SingleInstance.mainContext.insertOrUpdateUploadOrDownload(
                        chatFTPBean, "0", "groupchat");
//                FTPPoolManager.processRequest(chatFTPBean);
                ftpUpload(chatFTPBean);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }

    private void showToast(String msg) {
        try {
            Toast.makeText(GroupChatActivity.this, msg, Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        Log.i("reopen", "GroupChatActivity onDestroy");
        stopPlayback();
        sendlistadapter.stopPlayback();
        UdpMessageBean bean = new UdpMessageBean();
        GroupChatBean gcBean = new GroupChatBean();
        bean.setType("105");
        gcBean.setSenttime(getCurrentDateandTime());
        if (isGroup || isRounding) {
            gcBean.setTo(groupBean.getGroupId());
            gcBean.setSessionid(groupBean.getGroupId());
            gcBean.setGroupId(groupBean.getGroupId());
            gcBean.setCategory("G");
        } else {
            gcBean.setSessionid(CallDispatcher.LoginUser + buddy);
            gcBean.setGroupId(buddy);
            gcBean.setCategory("I");
            gcBean.setFrom(CallDispatcher.LoginUser);
            gcBean.setTo(buddy);
        }
        gcBean.setResult("1");
        bean.setResponseObject(gcBean);
        SingleInstance.mainContext.ReadMessageAck(bean);
        if (SingleInstance.contextTable.containsKey("groupchat")) {
            SingleInstance.contextTable.remove("groupchat");
        }
//        SingleInstance.current_open_activity_detail.clear();
        Object cur_context_object = null;
        Iterator it0 = SingleInstance.current_open_activity_detail.entrySet().iterator();
        while (it0.hasNext()) {
            Map.Entry pair0 = (Map.Entry) it0.next();
            Object cur_context_object0 = pair0.getKey();
            if(cur_context_object0 instanceof GroupChatActivity) {
                cur_context_object =cur_context_object0;
            }
        }
        if(cur_context_object != null) {
            Log.i("reopen", "GroupChatActivity containsKey0");
            SingleInstance.current_open_activity_detail.remove(cur_context_object);
        }
        if(save_state){
            if(SingleInstance.current_open_activity_detail.containsKey(context)) {
                Log.i("reopen", "GroupChatActivity containsKey1");
                SingleInstance.current_open_activity_detail.remove(context);
            }
            if(message!=null && message.getText().toString().trim().length()>0){
                current_open_activity_detail.put("message", message.getText().toString().trim());
            }
            SingleInstance.current_open_activity_detail.put(context,this.current_open_activity_detail);
            save_state = false;
        } else {
            if(SingleInstance.current_open_activity_detail.containsKey(context)) {
                Log.i("reopen", "GroupChatActivity containsKey");
                SingleInstance.current_open_activity_detail.remove(context);
            }
        }
        AppReference.getValuesinChat.clear();

        mediaPlayer.stop();
        isPrivateBack=false;
        PrivateReply_view=null;
        privateReply_username=null;
        super.onDestroy();
    }

    public void finishScreenforCallRequest(){
        save_state = true;
        this.finish();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AppMainActivity.inActivity = this;
        AppReference.fileOpen=false;
        context = this;
        if(AppReference.mainContext.isPinEnable) {
            if (AppReference.mainContext.openPinActivity) {
                AppReference.mainContext.openPinActivity=false;
                if(Build.VERSION.SDK_INT>20 && AppReference.mainContext.isTouchIdEnabled) {
                    Intent i = new Intent(GroupChatActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(GroupChatActivity.this, PinSecurity.class);
                    startActivity(i);
                }
            } else {
                AppReference.mainContext.count=0;
                AppReference.mainContext.registerBroadcastReceiver();
            }
        }
    }

    public void notifyUI(final GroupChatBean gcBean) {

        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        String groupId = null;
                        if (gcBean.getCategory().equalsIgnoreCase("G")) {
                            groupId = groupBean.getGroupId();
                        } else {
                            groupId = buddy;
                        }
                        for (GroupChatBean gcBeanTemp : chatList) {
                            if (gcBeanTemp.getSignalid()!=null&&gcBeanTemp.getSignalid().equals(gcBean.getSignalid()))
                                return;
                        }
                        Log.i("groupchat123",
                                "received groupid " + gcBean.getGroupId()
                                        + " local groupid " + groupId);
                        if (gcBean.getGroupId().equals(groupId)) {
                            if (gcBean.getType().equals("100")) {
                                if ((gcBean.getSubCategory() != null)) {
                                    if ((gcBean.getSubCategory()
                                            .equalsIgnoreCase("GRB_R") || gcBean.getSubCategory()
                                            .equalsIgnoreCase("grb")
                                            && gcBean.getParentId() != null
                                            && gcBean.getParentId().length() > 0 && !gcBean
                                            .getParentId().equalsIgnoreCase(
                                                    "null"))) {
                                        //Old Code cmd start
//                                        int position = -1;
//                                        for (int i = 0; i < chatList.size(); i++) {
//                                            GroupChatBean gcBean1 = chatList
//                                                    .get(i);
//                                            if (gcBean1 != null
//                                                    && gcBean1.getParentId() != null
//                                                    && gcBean1
//                                                    .getParentId()
//                                                    .equals(gcBean
//                                                            .getParentId())) {
//                                                position = i;
//                                                if (gcBean
//                                                        .getSubCategory()
//                                                        .equalsIgnoreCase("GRB")) {
////													showToast("Received Chain from "
////															+ gcBean.getFrom());
//                                                }
//                                            }
//                                        }

//                                        if (position == -1) {
                                            chatList.add(gcBean);
                                        Collections.sort(chatList, new GroupMessageComparator());
                                            adapter.notifyDataSetChanged();
                                        maintainListPosition();
//                                            maintainListPosition();
//                                        } else {
//                                            chatList.add(position + 1, gcBean);
//                                            for (int i = 0; i < chatList.size(); i++) {
//                                                GroupChatBean gcBean1 = chatList
//                                                        .get(i);
//                                                if (gcBean1.getParentId().equals(gcBean.getParentId())) {
//
//                                                }
//
//                                            }
//
//                                            // adapter = new GroupChatAdapter(
//                                            // context, chatList);
//                                            // adapter.notifyDataSetChanged();
//                                            // lv.setAdapter(adapter);
//                                            // lv.setSelection(position);
//                                            mainListPositionForSpecialMessage(position + 1);
//
//                                        }

                                    } else if (gcBean.getSubCategory()
                                            .equalsIgnoreCase("gdi")) {
                                        if (gcBean.getParentId() != null
                                                && gcBean.getParentId()
                                                .length() > 0
                                                && !gcBean.getParentId()
                                                .equalsIgnoreCase(
                                                        "null")) {
                                            for (int i = 0; i < chatList.size(); i++) {
                                                Log.i("chatmsg123",
                                                        "received dmsg : "
                                                                + gcBean.getParentId());
                                                GroupChatBean gcBean1 = chatList
                                                        .get(i);
                                                Log.i("chatmsg123",
                                                        "iterate dmsg : "
                                                                + i
                                                                + " "
                                                                + gcBean1
                                                                .getParentId());
                                                final int pos = i;
                                                if (gcBean1 != null
                                                        && gcBean1
                                                        .getParentId() != null
                                                        && gcBean1
                                                        .getParentId()
                                                        .equals(gcBean
                                                                .getParentId())) {
                                                    String msg = gcBean1
                                                            .getMessage()
                                                            + "\nStatus : "
                                                            + gcBean.getMessage();
                                                    gcBean1.setMessage(msg);
                                                    showToast("Received Todo reply from "
                                                            + gcBean.getFrom());
                                                    // adapter = new
                                                    // GroupChatAdapter(
                                                    // context, chatList);
                                                    // adapter.notifyDataSetChanged();
                                                    // lv.setAdapter(adapter);
                                                    // lv.setSelection(pos);
                                                    mainListPositionForSpecialMessage(pos);
                                                    break;
                                                }

                                            }
                                        } else {
                                            chatList.add(gcBean);
                                            Collections.sort(chatList, new GroupMessageComparator());
                                            adapter.notifyDataSetChanged();
                                            maintainListPosition();
                                        }
                                    }
                                    else if (!gcBean.getFrom().equals(CallDispatcher.LoginUser) && gcBean.getSubCategory() != null
                                            && gcBean.getSubCategory().equalsIgnoreCase("gc_r")) {
                                        for (int i = 0; i < chatList.size(); i++) {
                                            GroupChatBean gChat = chatList.get(i);
                                            if (gChat.getParentId()!=null &&gChat.getParentId().equals(gcBean.getParentId())) {
                                                Log.i("AAAA","chat confirm requst if part ");
                                                gChat.setReply("gc_r");
                                                chatList.remove(gcBean);
                                                adapter.notifyDataSetChanged();
                                                maintainListPosition();
                                            }
                                        }
                                    }
                                    else {
                                        chatList.add(gcBean);
                                        Collections.sort(chatList, new GroupMessageComparator());
                                        adapter.notifyDataSetChanged();
                                        maintainListPosition();
                                    }
                                } else {
                                    chatList.add(gcBean);
                                    Collections.sort(chatList, new GroupMessageComparator());
                                    adapter.notifyDataSetChanged();
                                    maintainListPosition();
                                }

                                DBAccess.getdbHeler()
                                        .updateUnreadMsgForGroupChat(1,
                                                gcBean.getSessionid(),
                                                CallDispatcher.LoginUser);
                            } else if (gcBean.getType().equals("104")) {
                                for (int i = 0; i < chatList.size(); i++) {
                                    GroupChatBean gChat = chatList.get(i);
                                    if (gChat != null
                                            &&gChat.getSignalid()!=null && gcBean.getpSingnalId()!=null && gChat.getSignalid().equals(
                                            gcBean.getpSingnalId())) {
                                        //For withdrraw message
                                        //start
                                        gChat.setMimetype("text");
                                        gChat.setMessage("Message withdrawn @"+getCurrentTime());
                                        gChat.setWithdrawn("1");
                                        //End
                                        //chatList.remove(i);
                                        adapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }

                        }
                        if (!gcBean.getFrom().equals(CallDispatcher.LoginUser) && gcBean.getSubCategory() != null && gcBean.getSubCategory().equalsIgnoreCase("gc_r")) {
                            Log.i("AAAA", "chat confirm requst ");
                            for (int i = 0; i < chatList.size(); i++) {
                                GroupChatBean gChat = chatList.get(i);
                                if (gChat.getParentId()!=null &&gChat.getParentId().equalsIgnoreCase(gcBean.getParentId())) {
                                    Log.i("AAAA","chat confirm requst if part ");
                                    gChat.setReply("gc_r");
                                    chatList.remove(gcBean);
                                    adapter.notifyDataSetChanged();
                                    maintainListPosition();
                                }
                            }
                        }
                        if (gcBean.getSubCategory() != null && gcBean.getSubCategory().equalsIgnoreCase("GRB_R")) {
                            for (int i = 0; i < chatList.size(); i++) {
                                GroupChatBean gChat = chatList.get(i);
                                if (gChat.getParentId()!=null&&gChat.getParentId().equals(gcBean.getParentId())) {
                                    gChat.setReply("GRB_R");
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        maintainListPosition();
                        Log.i("groupchat123",
                                "chatlist size " + chatList.size());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void updateThumbs(String sid) {
        Log.d("abcdef", "IN 101 signal " + sid);
        DBAccess.getdbHeler().updateThumbs(sid);
        for (GroupChatBean gcb : chatList) {
            if (gcb.getSignalid()!=null&&gcb.getSignalid().equalsIgnoreCase(sid)) {
                gcb.setThumb(1);
            }
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(adapter!=null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void maintainListPosition() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lv.setSelection(lv.getCount() - 1);
            }
        });

    }

    private void mainListPositionForSpecialMessage(final int position) {

        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lv.setSelection(position);
            }
        });

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(message.getWindowToken(), 0);

        // imm.showSoftInput(ed, 0);
    }

    private Vector<GroupChatBean> loadChatHistory(String groupId) {

        try {
            Vector<GroupChatBean> groupChatList = DBAccess.getdbHeler()
                    .getGroupChatHistory(groupId, true);
            Vector<GroupChatBean> gcList = DBAccess.getdbHeler().loadHistoryGC(
                    groupChatList);
            return gcList;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
            return null;
        }

    }

    private Vector<GroupChatBean> loadIndividualChatHistory(String groupId) {
        try {
            Vector<GroupChatBean> chatList = callDisp.getdbHeler(context)
                    .getGroupChatHistory(groupId, false);
            return chatList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private void multimediaType(String strMMType) {
        Log.i("clone", "===> inside message response");

        try {
            if (strMMType.equals(SingleInstance.mainContext.getResources().getString(R.string.select_from_gallery))) {
                Log.i("clone", "====> inside gallery");
                if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 30);
                } else {
                    Log.i("img", "sdk is above 19");
                    Log.i("clone", "====> inside gallery");
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, 31);
                }

            } else if (strMMType.equals(SingleInstance.mainContext.getResources().getString(R.string.open_camera))) {
                Log.i("clone", "====> inside photo");
                Long free_size = callDisp.getExternalMemorySize();
                Log.d("IM", free_size.toString());
                if (free_size > 0 && free_size >= 5120) {
                    strIPath = Environment.getExternalStorageDirectory()
                            + "/COMMedia/MPD_" + callDisp.getFileName()
                            + ".jpg";
                    // Intent intent = new Intent(context,
                    // MultimediaUtils.class);
                    // intent.putExtra("filePath", strIPath);
                    // intent.putExtra("requestCode", 32);
                    // intent.putExtra("action",
                    // MediaStore.ACTION_IMAGE_CAPTURE);
                    // intent.putExtra("createOrOpen", "create");
                    // startActivity(intent);
                    Intent intent = new Intent(context, CustomVideoCamera.class);
                    intent.putExtra("filePath", strIPath);
                    intent.putExtra("isPhoto", true);
                    startActivityForResult(intent, 32);

                } else {
                    showToast("InSufficient Memory...");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }
    }

    public void photochat() {
        try {
            final String[] items = new String[]{SingleInstance.mainContext.getResources().getString(R.string.open_camera),
                    SingleInstance.mainContext.getResources().getString(R.string.select_from_gallery)
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Multi", "which : " + which);
                    if( CallDispatcher.isCallInitiate && which == 0 ) {
                        showToast("Please Try again...call  in progress");
                    } else {
                        multimediaType(items[which]);
                    }

                }

            });

            builder.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });
            builder.show();
        } catch (Exception e) {

            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }
    }
    private void chatsync(final boolean isfromgroup){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.group_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 15;
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();

        TextView days = (TextView) dialog.findViewById(R.id.edit_grp);
        days.setVisibility(View.GONE);
        TextView oneday = (TextView) dialog.findViewById(R.id.invite_grp);
        oneday.setText("1 Day");
        TextView threedays = (TextView) dialog.findViewById(R.id.leave_grp);
        threedays.setText("3 Days");
        threedays.setBackgroundColor(getResources().getColor(R.color.blue2));
        TextView all_chat = (TextView) dialog.findViewById(R.id.delete_grp);
        all_chat.setText("Load More");
        all_chat.setBackgroundColor(getResources().getColor(R.color.blue2));
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        all_chat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showprogress();
                String mindate=DBAccess.getdbHeler().getminDateandTime();
                if(mindate!=null) {
                    String[] date = mindate.split(" ");
                    if (!isfromgroup)
                        WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "3", buddy, date[0],"");
                    else
                        WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "3", groupId, date[0],"");
                }else{
                    if (!isfromgroup)
                        WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "1", buddy, "","");
                    else
                        WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "1", groupId, "","");
                }
            }
        });

        oneday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dialog.dismiss();
                showprogress();
                if(!isfromgroup)
                WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext,"1",buddy,"","");
                else
                    WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext,"1",groupId,"","");
            }
        });

        threedays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                showprogress();
                if(!isfromgroup)
                    WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext,"2",buddy,"","");
                else
                    WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext,"2",groupId,"","");
            }
        });
    }

    private void individualCallMenu(int pos) {
        try {

            final PermissionBean permissionBean = callDisp.getdbHeler(context)
                    .selectPermissions(
                            "select * from setpermission where userid='"
                                    + buddyName.getText().toString()
                                    + "' and buddyid='"
                                    + CallDispatcher.LoginUser + "'",
                            buddyName.getText().toString(),
                            CallDispatcher.LoginUser);

            if (pos == 0) {
                if (permissionBean.getAudio_call().equals("1"))
                    callDisp.MakeCall(1, buddyName.getText().toString(),
                            context);
                else
                    callDisp.showAlert("Response", "Access Denied");

            } else if (pos == 1) {
                if (permissionBean.getVideo_call().equals("1"))
                    callDisp.MakeCall(2, buddyName.getText().toString(),
                            context);
                else
                    callDisp.showAlert("Response", "Access Denied");

            } else if (pos == 2) {
                if (permissionBean.getAUC().equals("1"))
                    callDisp.MakeCall(3, buddyName.getText().toString(),
                            context);
                else
                    callDisp.showAlert("Response", "Access Denied");
            } else if (pos == 3) {
                if (permissionBean.getVUC().equals("1"))
                    callDisp.MakeCall(4, buddyName.getText().toString(),
                            context);
            } else if (pos == 4) {
                if (permissionBean.getVUC().equals("1"))
                    callDisp.MakeCall(5, buddyName.getText().toString(),
                            context);
                else
                    callDisp.showAlert("Response", "Access Denied");

            } else if (pos == 5) {
                if (permissionBean.getVBC().equals("1"))
                    callDisp.MakeCall(6, buddyName.getText().toString(),
                            context);
                else
                    callDisp.showAlert("Response", "Access Denied");

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void groupCallMenu(int pos) {
        try {
            final GroupBean gBean = DBAccess.getdbHeler().getGroupAndMembers(
                    "select * from groupdetails where groupid="
                            + groupBean.getGroupId());
            String members = getMembers(gBean.getOwnerName() + ","
                    + gBean.getInviteMembers());
            Log.i("group123", "members " + members);

//            String activeMembers =

            Log.i("group123", "members " + members);
            CallDispatcher.chatId=groupBean.getGroupId();

            if (pos == 0) {
                if (members != null && members.length() > 0) {
                    callDisp.requestAudioConference(members);
                } else {
                    showToast("Sorry no members to call");
                }
            } else if (pos == 1) {
                if (members != null && members.length() > 0) {
                    callDisp.requestAudioBroadCast(members);
                } else {
                    showToast("Sorry no members to call");
                }

            } else if (pos == 2) {
                if (members != null && members.length() > 0) {
                    callDisp.requestVideoConference(members);
                } else {
                    showToast("Sorry no members to call");
                }
            } else if (pos == 3) {
                if (members != null && members.length() > 0) {
                    callDisp.requestVideoBroadCast(members);
                } else {
                    showToast("Sorry no members to call");
                }
                // callDisp.MakeCall(6, buddyName.getText().toString(),
                // context);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // TODO Auto-generated method stub
            super.onActivityResult(requestCode, resultCode, data);
            String parentId = pId;
            if (resultCode == 12) {
                if (data != null) {
                    String mgs = data.getStringExtra("MESSAGE");
                    message.setText(mgs);
                    if(mgs!=null){
                        message.setSelection(message.getText().toString().length());
                    }
                }
            }
            attachment_layout.setVisibility(View.GONE);
            btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
            atachlay.setVisibility(View.GONE);
            isGrid = false;
            if (resultCode == RESULT_OK) {
                if (requestCode == 30) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        strIPath = callDisp
                                .getRealPathFromURI(selectedImageUri);
                        File selected_file = new File(strIPath);
                        int length = (int) selected_file.length() / 1048576;
                        Log.d("busy", "........ size is------------->" + length);

                        if (length <= 2) {

                            Bitmap img = null;
                            img = callDisp.ResizeImage(strIPath);

                            if (img != null) {
                                img = Bitmap.createScaledBitmap(img, 100, 75,
                                        false);

                                final String path = Environment
                                        .getExternalStorageDirectory()
                                        + "/COMMedia/"
                                        + callDisp.getFileName()
                                        + ".jpg";

                                BufferedOutputStream stream;
                                Bitmap bitmap = null;
                                try {
                                    bitmap = callDisp.ResizeImage(strIPath);
                                    if (bitmap != null) {
                                        stream = new BufferedOutputStream(
                                                new FileOutputStream(new File(
                                                        path)));
                                        bitmap.compress(CompressFormat.JPEG,
                                                100, stream);
                                        strIPath = path;
                                    }

                                    if (bitmap != null) {
                                        bitmap = Bitmap.createScaledBitmap(
                                                bitmap, 200, 150, false);
                                    }
//                                    relative_send_layout.getLayoutParams().height = 400;
                                    SendListUIBean uIbean = new SendListUIBean();
                                    uIbean.setType("image");
                                    uIbean.setPath(strIPath);
                                    uIbean.setUser(buddy);
                                    SendListUI.add(uIbean);

                                    if(!current_open_activity_detail.containsKey("firstimage1")) {
                                        current_open_activity_detail.put("firstimage1", uIbean);
                                    }else if(!current_open_activity_detail.containsKey("firstimage2")){
                                        current_open_activity_detail.put("firstimage2", uIbean);
                                    }else if(!current_open_activity_detail.containsKey("firstimage3")){
                                        current_open_activity_detail.put("firstimage3", uIbean);
                                    }

                                    sendlistadapter.notifyDataSetChanged();
                                    list_all.removeAllViews();
                                    final int adapterCount = sendlistadapter.getCount();

                                    for (int i = 0; i < adapterCount; i++) {
                                        View item = sendlistadapter.getView(i, null, null);
                                        list_all.addView(item);
                                    }
                                    if(adapterCount>=2){
                                        multi_send.getLayoutParams().height=280;
                                    }
                                    if(isPrivateBack){
                                        LL_privateReply.setVisibility(View.VISIBLE);
                                    }else {
                                        msgoptionview.setVisibility(View.VISIBLE);
                                    }
                                    audio_call.setBackgroundResource(R.drawable.chat_send);
                                    audio_call.setTag(1);
                                    // scaleImage(strIPath);
//									Intent pMsgIntent = new Intent(context,
//											PrivateMessageActivity.class);
//									pMsgIntent.putExtra("groupid", groupId);
//									pMsgIntent.putExtra("buddyname", buddy);
//									pMsgIntent.putExtra("type", "image");
//									pMsgIntent.putExtra("localpath", strIPath);
//									pMsgIntent.putExtra("replyback",
//											isReplyBack);
//									pMsgIntent.putExtra("pMembers",
//											privateMembers);
//									pMsgIntent.putExtra("parentid", parentId);
//									startActivity(pMsgIntent);
                                    // sendMsg("", strIPath, "image", null);

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else if (requestCode == 31) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getContentResolver().takePersistableUriPermission(
                                selectedImageUri, takeFlags);
                        strIPath = Environment.getExternalStorageDirectory()
                                + "/COMMedia/" + "MPD_"
                                + callDisp.getFileName() + ".jpg";
                        File selected_file = new File(strIPath);
                        int length = (int) selected_file.length() / 1048576;
                        Log.d("busy", "........ size is------------->" + length);
                        if (length <= 2) {
                            btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
                            atachlay.setVisibility(View.GONE);
                            audio_layout.setVisibility(View.GONE);
                            isGrid = false;
                            new bitmaploader().execute(selectedImageUri);
                        } else {
                            showToast("Kindly Select someother image,this image is too large");
                        }
                    }
                } else if (requestCode == 32) {
                    if (strIPath != null) {
                        File fileCheck = new File(strIPath);
                        if (fileCheck.exists()) {
                            String orientation=null;
                            if (data != null) {
                                orientation=data.getStringExtra("orientation");
                                Log.i("orientation","chatActivity orientation-->"+orientation);
                            }
//                            if(orientation!=null && orientation.equalsIgnoreCase("0")) {
//                                btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
//                                atachlay.setVisibility(View.GONE);
//                                audio_layout.setVisibility(View.GONE);
//                                isGrid = false;
////                            relative_send_layout.getLayoutParams().height = 400;
//                                SendListUIBean uIbean = new SendListUIBean();
//                                uIbean.setType("image");
//                                uIbean.setPath(strIPath);
//                                uIbean.setUser(buddy);
//                                SendListUI.add(uIbean);
//                                sendlistadapter.notifyDataSetChanged();
//
//
//                                if (!current_open_activity_detail.containsKey("thirdimage1")) {
//                                    current_open_activity_detail.put("thirdimage1", uIbean);
//                                } else if (!current_open_activity_detail.containsKey("thirdimage2")) {
//                                    current_open_activity_detail.put("thirdimage2", uIbean);
//                                } else if (!current_open_activity_detail.containsKey("thirdimage3")) {
//                                    current_open_activity_detail.put("thirdimage3", uIbean);
//                                }
//
//                                list_all.removeAllViews();
//                                final int adapterCount = sendlistadapter.getCount();
//
//                                for (int i = 0; i < adapterCount; i++) {
//                                    View item = sendlistadapter.getView(i, null, null);
//                                    list_all.addView(item);
//                                }
//                                if (adapterCount >= 2) {
//                                    multi_send.getLayoutParams().height = 280;
//                                }
//                                if (isPrivateBack) {
//                                    LL_privateReply.setVisibility(View.VISIBLE);
//                                } else {
//                                    msgoptionview.setVisibility(View.VISIBLE);
//                                }
//                                audio_call.setBackgroundResource(R.drawable.chat_send);
//                                audio_call.setTag(1);
////                             sendMsg("", strIPath, "image", null);
////       Intent pMsgIntent = new Intent(context,
////         PrivateMessageActivity.class);
////       pMsgIntent.putExtra("groupid", groupId);
////       pMsgIntent.putExtra("type", "image");
////       pMsgIntent.putExtra("localpath", strIPath);
////       pMsgIntent.putExtra("buddyname", buddy);
////       pMsgIntent.putExtra("replyback", isReplyBack);
////       pMsgIntent.putExtra("pMembers", privateMembers);
////       pMsgIntent.putExtra("parentid", parentId);
////       startActivity(pMsgIntent);
//                            }else {
                                showprogress();
                                Log.i("AAAA", "onactivity result ");
                                new imageOrientation().execute(orientation);
//                            }
                        } else {
                            showToast("Not able to process. Please try again");
                        }
                    }
                }
                else if(requestCode==35){
                    if (data != null) {
                        Log.i("AAA","New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = callDisp.getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/COMMedia/" + callDisp.getFileName() + ".mp4";

                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                        SendListUIBean uIbean = new SendListUIBean();
                        uIbean.setType("video");
                        uIbean.setPath(strIPath);
                        SendListUI.add(uIbean);
                        sendlistadapter.notifyDataSetChanged();

                        if(!current_open_activity_detail.containsKey("firstvideo1")) {
                            current_open_activity_detail.put("firstvideo1", uIbean);
                        }else if(!current_open_activity_detail.containsKey("firstvideo2")){
                            current_open_activity_detail.put("firstvideo2", uIbean);
                        }else if(!current_open_activity_detail.containsKey("firstvideo3")){
                            current_open_activity_detail.put("firstvideo3", uIbean);
                        }


                        list_all.removeAllViews();
                        final int adapterCount = sendlistadapter.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            View item = sendlistadapter.getView(i, null, null);
                            list_all.addView(item);
                        }
                        if(adapterCount>=2){
                            multi_send.getLayoutParams().height=280;
                        }
                        if(isPrivateBack){
                            LL_privateReply.setVisibility(View.VISIBLE);
                        }else {
                            msgoptionview.setVisibility(View.VISIBLE);
                        }
                        audio_call.setBackgroundResource(R.drawable.chat_send);
                        audio_call.setTag(1);
                    }
                }else if(requestCode==39){
                    if (data != null) {
                        Log.i("AAA","New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = callDisp.getRealPathFromURI(selectedImageUri);
//
                        final String path = Environment.getExternalStorageDirectory()
                                + "/COMMedia/" + callDisp.getFileName() + ".mp4";

//                        Log.i("AAA","New activity "+strIPath);
                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);

                        FileInputStream fin = (FileInputStream) getContentResolver()
                                .openInputStream(selectedImageUri);
                        ByteArrayOutputStream straam = new ByteArrayOutputStream();
                        byte[] content = new byte[1024];
                        int bytesread;
                        while ((bytesread = fin.read(content)) != -1) {
                            straam.write(content, 0, bytesread);
                        }
                        byte[] bytes = straam.toByteArray();
                        FileOutputStream fout = new FileOutputStream(strIPath);
                        straam.flush();
                        straam.close();
                        straam = null;
                        fin.close();
                        fin = null;
                        fout.write(bytes);
                        fout.flush();
                        fout.close();
                        fout = null;

                        File file = new File(strIPath);
//
// Get length of file in bytes
                        long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        long fileSizeInMB = fileSizeInKB / 1024;
                        Log.i("filesize","Files size bytes--->"+fileSizeInBytes);
                        Log.i("filesize","Files size kb--->"+fileSizeInKB);
                        Log.i("filesize","Files size mb--->"+fileSizeInMB);
                        if (fileSizeInMB <10) {
                            SendListUIBean uIbean = new SendListUIBean();
                            uIbean.setType("video");
                            uIbean.setPath(strIPath);
                            SendListUI.add(uIbean);
                            sendlistadapter.notifyDataSetChanged();

                            if(!current_open_activity_detail.containsKey("secondvideo1")) {
                                current_open_activity_detail.put("secondvideo1", uIbean);
                            }else if(!current_open_activity_detail.containsKey("secondvideo2")){
                                current_open_activity_detail.put("secondvideo2", uIbean);
                            }else if(!current_open_activity_detail.containsKey("secondvideo3")){
                                current_open_activity_detail.put("secondvideo3", uIbean);
                            }

                            list_all.removeAllViews();
                            final int adapterCount = sendlistadapter.getCount();

                            for (int i = 0; i < adapterCount; i++) {
                                View item = sendlistadapter.getView(i, null, null);
                                list_all.addView(item);
                            }
                            if(adapterCount>=2){
                                multi_send.getLayoutParams().height=280;
                            }
                            if(isPrivateBack){
                                LL_privateReply.setVisibility(View.VISIBLE);
                            }else {
                                msgoptionview.setVisibility(View.VISIBLE);
                            }
                            audio_call.setBackgroundResource(R.drawable.chat_send);
                            audio_call.setTag(1);
                        }else{
                            showToast("Pick less than 10 MB files");
                        }



                    }
                }else if (requestCode == 34) {
                    Bundle bun = data.getBundleExtra("share");
                    if (bun != null) {
                        String path = bun.getString("filepath");
                        String type = bun.getString("type");
                        File file = null;
                        if (type.equalsIgnoreCase("audio")) {
                            file = new File(path);
                            if (file.exists()) {
                                // sendMsg("", path, "audio", null);
                                Intent pMsgIntent = new Intent(context,
                                        PrivateMessageActivity.class);
                                pMsgIntent.putExtra("groupid", groupId);
                                pMsgIntent.putExtra("type", "audio");
                                pMsgIntent.putExtra("localpath", path);
                                pMsgIntent.putExtra("replyback", isReplyBack);
                                pMsgIntent.putExtra("parentid", parentId);
                                pMsgIntent.putExtra("buddyname", buddy);
                                pMsgIntent.putExtra("pMembers", privateMembers);
                                startActivity(pMsgIntent);
                            }
                        } else if (type.equalsIgnoreCase("video")) {
                            Log.i("groupchat123", "video path : " + path);
                            if (!path.endsWith(".mp4")) {
                                path = path + ".mp4";
                            }
                            file = new File(path);
                            if (file.exists()) {
                                // sendMsg("", path + ".mp4", "video",
                                // null);
                                Intent pMsgIntent = new Intent(context,
                                        PrivateMessageActivity.class);
                                pMsgIntent.putExtra("groupid", groupId);
                                pMsgIntent.putExtra("type", "video");
                                pMsgIntent.putExtra("localpath", path);
                                pMsgIntent.putExtra("replyback", isReplyBack);
                                pMsgIntent.putExtra("buddyname", buddy);
                                pMsgIntent.putExtra("pMembers", privateMembers);
                                pMsgIntent.putExtra("parentid", parentId);
                                startActivity(pMsgIntent);
                            }

                        }
                    }

                } else if (requestCode == 40) {
                    if (strIPath != null) {
                        File file = new File(strIPath);
                        if (file.exists()) {
                            btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
                            atachlay.setVisibility(View.GONE);
                            audio_layout.setVisibility(View.GONE);
                            isGrid = false;
//                            relative_send_layout.getLayoutParams().height = 400;
                            SendListUIBean uIbean = new SendListUIBean();
                            uIbean.setType("video");
                            uIbean.setPath(strIPath);
                            uIbean.setUser(buddy);
                            SendListUI.add(uIbean);
                            sendlistadapter.notifyDataSetChanged();

                            if(!current_open_activity_detail.containsKey("thirdvideo1")) {
                                current_open_activity_detail.put("thirdvideo1", uIbean);
                            }else if(!current_open_activity_detail.containsKey("thirdvideo2")){
                                current_open_activity_detail.put("thirdvideo2", uIbean);
                            }else if(!current_open_activity_detail.containsKey("thirdvideo3")){
                                current_open_activity_detail.put("thirdvideo3", uIbean);
                            }

                            list_all.removeAllViews();
                            final int adapterCount = sendlistadapter.getCount();

                            for (int i = 0; i < adapterCount; i++) {
                                View item = sendlistadapter.getView(i, null, null);
                                list_all.addView(item);
                            }
                            if(adapterCount>=2){
                                multi_send.getLayoutParams().height=280;
                            }
                            if(isPrivateBack){
                                LL_privateReply.setVisibility(View.VISIBLE);
                            }else {
                                msgoptionview.setVisibility(View.VISIBLE);
                            }
                            audio_call.setBackgroundResource(R.drawable.chat_send);
                            audio_call.setTag(1);
                            // sendMsg("", strIPath, "video", null);
//							Intent pMsgIntent = new Intent(context,
//									PrivateMessageActivity.class);
//							pMsgIntent.putExtra("groupid", groupId);
//							pMsgIntent.putExtra("type", "video");
//							pMsgIntent.putExtra("localpath", strIPath);
//							pMsgIntent.putExtra("replyback", isReplyBack);
//							pMsgIntent.putExtra("pMembers", privateMembers);
//							pMsgIntent.putExtra("buddyname", buddy);
//							pMsgIntent.putExtra("parentid", parentId);
//							startActivity(pMsgIntent);
                        }
                    } else {
                        showToast("Not able to process. Please try again");
                    }
                } else if (requestCode == 36) {
                    Bundle bun = data.getBundleExtra("sketch");
                    strIPath = bun.getString("path");
                    File fileCheck = new File(strIPath);
                    if (fileCheck.exists()) {
                        btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
                        atachlay.setVisibility(View.GONE);
                        audio_layout.setVisibility(View.GONE);
                        isGrid = false;
//                        relative_send_layout.getLayoutParams().height = 400;
                        SendListUIBean uIbean = new SendListUIBean();
                        uIbean.setType("image");
                        uIbean.setPath(strIPath);
                        uIbean.setUser(buddy);
                        SendListUI.add(uIbean);
                        sendlistadapter.notifyDataSetChanged();

                        if(!current_open_activity_detail.containsKey("sketch1")) {
                            current_open_activity_detail.put("sketch1", uIbean);
                        }else if(!current_open_activity_detail.containsKey("sketch2")){
                            current_open_activity_detail.put("sketch2", uIbean);
                        }else if(!current_open_activity_detail.containsKey("sketch3")){
                            current_open_activity_detail.put("sketch3", uIbean);
                        }

                        list_all.removeAllViews();
                        final int adapterCount = sendlistadapter.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            View item = sendlistadapter.getView(i, null, null);
                            list_all.addView(item);
                        }
                        if(adapterCount>=2){
                            multi_send.getLayoutParams().height=280;
                        }
                        if(isPrivateBack){
                            LL_privateReply.setVisibility(View.VISIBLE);
                        }else {
                            msgoptionview.setVisibility(View.VISIBLE);
                        }
                        audio_call.setBackgroundResource(R.drawable.chat_send);
                        audio_call.setTag(1);
                        // sendMsg("", strIPath, "image", null);
//						Intent pMsgIntent = new Intent(context,
//								PrivateMessageActivity.class);
//						pMsgIntent.putExtra("groupid", groupId);
//						pMsgIntent.putExtra("type", "image");
//						pMsgIntent.putExtra("localpath", strIPath);
//						pMsgIntent.putExtra("replyback", isReplyBack);
//						pMsgIntent.putExtra("pMembers", privateMembers);
//						pMsgIntent.putExtra("buddyname", buddy);
//						pMsgIntent.putExtra("parentid", parentId);
//						startActivity(pMsgIntent);
                    }
                } else if (requestCode == 38) {
                    Log.d("strpath", "chatdoc " + strIPath);
                    strIPath = data.getStringExtra("filePath");
                    File fileCheck = new File(strIPath);
                    if (fileCheck.exists()) {
                        btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
                        atachlay.setVisibility(View.GONE);
                        audio_layout.setVisibility(View.GONE);
                        isGrid = false;
//                        relative_send_layout.getLayoutParams().height = 400;
                        SendListUIBean uIbean = new SendListUIBean();
                        uIbean.setType("document");
                        uIbean.setPath(strIPath);
                        uIbean.setUser(buddy);
                        SendListUI.add(uIbean);
                        sendlistadapter.notifyDataSetChanged();

                        if(!current_open_activity_detail.containsKey("document1")) {
                            current_open_activity_detail.put("document1", uIbean);
                        }else if(!current_open_activity_detail.containsKey("document2")){
                            current_open_activity_detail.put("document2", uIbean);
                        }else if(!current_open_activity_detail.containsKey("document3")){
                            current_open_activity_detail.put("document3", uIbean);
                        }


                        list_all.removeAllViews();
                        final int adapterCount = sendlistadapter.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            View item = sendlistadapter.getView(i, null, null);
                            list_all.addView(item);
                        }
                        if(adapterCount>=2){
                            multi_send.getLayoutParams().height=280;
                        }
                        if(isPrivateBack){
                            LL_privateReply.setVisibility(View.VISIBLE);
                        }else {
                            msgoptionview.setVisibility(View.VISIBLE);
                        }
                        audio_call.setBackgroundResource(R.drawable.chat_send);
                        audio_call.setTag(1);
                        // sendMsg("", strIPath, "image", null);
//						Intent pMsgIntent = new Intent(context,
//								PrivateMessageActivity.class);
//						pMsgIntent.putExtra("groupid", groupId);
//						pMsgIntent.putExtra("type", "document");
//						pMsgIntent.putExtra("localpath", strIPath);
//						pMsgIntent.putExtra("replyback", isReplyBack);
//						pMsgIntent.putExtra("pMembers", privateMembers);
//						pMsgIntent.putExtra("buddyname", buddy);
//						pMsgIntent.putExtra("parentid", parentId);
//						startActivity(pMsgIntent);
                    }
                }
                if (requestCode == 37) {
                    String message = data.getStringExtra("msg");
                    if (message != null && message.length() > 0) {
                        Intent pMsgIntent = new Intent(context,
                                PrivateMessageActivity.class);
                        pMsgIntent.putExtra("groupid", groupId);
                        pMsgIntent.putExtra("type", "text");
                        pMsgIntent.putExtra("message", message.trim());
                        pMsgIntent.putExtra("replyback", isReplyBack);
                        pMsgIntent.putExtra("parentid", parentId);
                        pMsgIntent.putExtra("buddyname", buddy);
                        pMsgIntent.putExtra("pMembers", privateMembers);
                        startActivity(pMsgIntent);

                    }
                }

            }
            if (requestCode == 33) {
                File file = new File(strIPath);
                if (file.exists()) {
                    // sendMsg("", strIPath, "audio", null);
                    Intent pMsgIntent = new Intent(context,
                            PrivateMessageActivity.class);
                    pMsgIntent.putExtra("groupid", groupId);
                    pMsgIntent.putExtra("type", "audio");
                    pMsgIntent.putExtra("localpath", strIPath);
                    pMsgIntent.putExtra("replyback", isReplyBack);
                    pMsgIntent.putExtra("pMembers", privateMembers);
                    pMsgIntent.putExtra("buddyname", buddy);
                    pMsgIntent.putExtra("parentid", parentId);
                    startActivity(pMsgIntent);
                }
            }

            if (requestCode == 101) {
                if (resultCode == RESULT_OK) {
                    String members = data.getStringExtra("SELECTED_MEMBERS");
                    Log.d("XYZ", "Selected Members : " + members);
                    sendSpecialMessage("GP", members);
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.GONE);
                    }else {
                        msgoptionview.setVisibility(View.GONE);
                    }
                }
            }


            if (requestCode == 102) {
                // Reply Back For Group
                if (resultCode == RESULT_OK) {
                    String members = data.getStringExtra("SELECTED_MEMBERS");
                    Log.d("XYZ", "Selected Members : " + members);
                    sendSpecialMessage("GRB", members);
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.GONE);
                    }else {
                        msgoptionview.setVisibility(View.GONE);
                    }
                }
            }
            if (requestCode == 103) {
                // Schedule For Group
                if (resultCode == RESULT_OK) {
                    String members = data.getStringExtra("SELECTED_MEMBERS");
                    Log.d("XYZ", "Selected Members : " + members);
                    sendWithSchedule(members);
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.GONE);
                    }else {
                        msgoptionview.setVisibility(View.GONE);
                    }
                }
            }
            if (requestCode == 104) {
                // Deadline For Group
                if (resultCode == RESULT_OK) {
                    String members = data.getStringExtra("SELECTED_MEMBERS");
                    Log.d("XYZ", "Selected Members : " + members);
                    sendWithDeadline(members);
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.GONE);
                    }else {
                        msgoptionview.setVisibility(View.GONE);
                    }
                }
            }
            if (requestCode == 112) {
                if (resultCode == RESULT_OK) {
                    String members = data.getStringExtra("SELECTED_MEMBERS");
                    String groupmembers = data.getStringExtra("SELECTED_GROUPS");
                    Log.i("AAAA", "forawrd user " + groupmembers+members);
                    if(members!=null) {
                        String[] buddies = members.split(",");
                        for(String temp:buddies) {
                            forwardChat(temp, false);
                        }
                    }if(groupmembers!=null) {
                        String[] groupMembers = groupmembers.split(",");
                        for(String temp:groupMembers) {
                            forwardChat(temp, true);
                        }
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            selectAll_buddy.setChecked(false);
                            forward = false;
                            audio_call.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                            forwardlay.setVisibility(View.GONE);
                            settingnotifications.getLayoutParams().height = 20;
                            selectAll_container.setVisibility(View.GONE);
                            sendLay.setVisibility(View.VISIBLE);
                            header.setVisibility(View.VISIBLE);
                            sidemenu.setBackgroundResource(R.drawable.navigation_menu);
                            cancel.setVisibility(View.VISIBLE);
                            dot.setVisibility(View.VISIBLE);
                            forwardlay.setVisibility(View.GONE);

                        }
                    });
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }
//        pId = null;

    }

    // private Bitmap scaleImage(String strIPath) {
    // Bitmap bitmap = null;
    //
    // try {
    //
    // bitmap = callDisp.ResizeImage(strIPath);
    // if (bitmap != null) {
    // String path = Environment.getExternalStorageDirectory()
    // + "/COMMedia/" + "MPD_" + callDisp.getFileName()
    // + ".jpg";
    //
    // BufferedOutputStream stream;
    // try {
    // stream = new BufferedOutputStream(new FileOutputStream(
    // new File(path)));
    // bitmap.compress(CompressFormat.JPEG, 100, stream);
    // } catch (FileNotFoundException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // if (bitmap != null) {
    // bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);
    // }
    // }
    //
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // if (AppReference.isWriteInFile)
    // AppReference.logger.error(e.getMessage(), e);
    // else
    // e.printStackTrace();
    // }
    // return bitmap;
    // }

    public class bitmaploader extends AsyncTask<Uri, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                super.onPostExecute(result);
                Log.d("image", "came to post execute for image");

                Bitmap img = null;
                if (strIPath != null)
                    img = callDisp.ResizeImage(strIPath);
                if (img != null) {
                    // sendMsg("", strIPath, "image", null);
//                    relative_send_layout.getLayoutParams().height = 400;
                    SendListUIBean uIbean = new SendListUIBean();
                    uIbean.setType("image");
                    uIbean.setPath(strIPath);
                    uIbean.setUser(buddy);
                    SendListUI.add(uIbean);
                    sendlistadapter.notifyDataSetChanged();
                    list_all.removeAllViews();
                    final int adapterCount = sendlistadapter.getCount();

                    for (int i = 0; i < adapterCount; i++) {
                        View item = sendlistadapter.getView(i, null, null);
                        list_all.addView(item);
                    }
                    if(adapterCount>=2){
                        multi_send.getLayoutParams().height=280;
                    }
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.VISIBLE);
                    }else {
                        msgoptionview.setVisibility(View.VISIBLE);
                    }
                    audio_call.setBackgroundResource(R.drawable.chat_send);
                    audio_call.setTag(1);
//					Intent pMsgIntent = new Intent(context,
//							PrivateMessageActivity.class);
//					pMsgIntent.putExtra("groupid", groupId);
//					pMsgIntent.putExtra("type", "image");
//					pMsgIntent.putExtra("localpath", strIPath);
//					pMsgIntent.putExtra("buddyname", buddy);
//					pMsgIntent.putExtra("replyback", isReplyBack);
//					pMsgIntent.putExtra("parentid", pId);
//					startActivity(pMsgIntent);
                    callDisp.cancelDialog();
                    Log.d("OnActivity", "_____On Activity Called______");
                } else {
                    strIPath = null;
                }

            } catch (Exception e) {
                Log.e("profile", "====> " + e.getMessage());
                if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            try {
                super.onPreExecute();
                ProgressDialog dialog = new ProgressDialog(context);
                callDisp.showprogress(dialog, context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("profile", "====> " + e.getMessage());
                if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Uri... params) {
            // TODO Auto-generated method stub
            try {
                for (Uri uri : params) {
                    Log.d("image", "came to doin backgroung for image");
                    FileInputStream fin = (FileInputStream) getContentResolver()
                            .openInputStream(uri);
                    ByteArrayOutputStream straam = new ByteArrayOutputStream();
                    byte[] content = new byte[1024];
                    int bytesread;
                    while ((bytesread = fin.read(content)) != -1) {
                        straam.write(content, 0, bytesread);
                    }
                    byte[] bytes = straam.toByteArray();
                    FileOutputStream fout = new FileOutputStream(strIPath);
                    straam.flush();
                    straam.close();
                    straam = null;
                    fin.close();
                    fin = null;
                    fout.write(bytes);
                    fout.flush();
                    fout.close();
                    fout = null;
                }
            } catch (Exception e) {
                if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();
            }

            return null;
        }

    }

    public void uploadCompleted(FTPBean bean, boolean status) {
        try {
            Log.i("AAAA","upload completed ******************8  ");
            if (bean != null) {
                GroupChatBean groupChatBean = ((GroupChatBean) bean
                        .getReq_object()).clone();
                if (status)
                    groupChatBean.setMediaName(bean.getFilename());
                else
                    groupChatBean.setMediaName(null);
//                if (status)
//                    SingleInstance.getGroupChatProcesser().getQueue()
//                            .addObject(groupChatBean);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }

    }

    public void showAudioMessageDialog() {
        try {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
            String[] type = {SingleInstance.mainContext.getResources().getString(R.string.record_audio), SingleInstance.mainContext.getResources().getString(R.string.choose_existing_audio1), SingleInstance.mainContext.getResources().getString(R.string.cancel)};

            alert_builder.setSingleChoiceItems(type, 0,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int pos) {
                            // TODO Auto-generated method stub
                            if (pos == 0) {
                                strIPath = Environment
                                        .getExternalStorageDirectory()
                                        + "/COMMedia/"
                                        + "MAD_"
                                        + callDisp.getFileName() + ".mp4";
                                Intent intent = new Intent(context,
                                        MultimediaUtils.class);
                                intent.putExtra("filePath", strIPath);
                                intent.putExtra("requestCode", 33);
                                intent.putExtra("action", "audio");
                                intent.putExtra("createOrOpen", "create");
                                startActivity(intent);
                                dialog.dismiss();
                            } else if (pos == 1) {
                                Intent i = new Intent(context,
                                        NotePickerScreen.class);
                                i.putExtra("note", "audio");
                                startActivityForResult(i, 34);
                                dialog.dismiss();

                            } else if (pos == 2) {
                                dialog.dismiss();
                            }
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

    public void showVideoMessageDialog() {
        try {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
            String[] type = {SingleInstance.mainContext.getResources().getString(R.string.record_video), SingleInstance.mainContext.getResources().getString(R.string.choose_existing_video1), SingleInstance.mainContext.getResources().getString(R.string.cancel)};

            alert_builder.setSingleChoiceItems(type, 0,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int pos) {
                            // TODO Auto-generated method stub
                            if (pos == 0) {
                                if(!CallDispatcher.isCallInitiate) {
                                    strIPath = Environment
                                            .getExternalStorageDirectory()
                                            + "/COMMedia/"
                                            + "MVD_"
                                            + callDisp.getFileName() + ".mp4";
                                    Intent intent = new Intent(context,
                                            CustomVideoCamera.class);
                                    intent.putExtra("filePath", strIPath);
                                    // intent.putExtra("requestCode", 35);
                                    // intent.putExtra("action",
                                    // MediaStore.ACTION_VIDEO_CAPTURE);
                                    // intent.putExtra("createOrOpen", "create");
                                    startActivityForResult(intent, 40);
                                } else {
                                    showToast("Please Try again...call  in progress");
                                }
                                dialog.dismiss();
                            } else if (pos == 1) {
                                if (Build.VERSION.SDK_INT < 19) {
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("video/*");
                                    startActivityForResult(intent, 35);
                                } else {
                                    Log.i("img", "sdk is above 19");
                                    Log.i("clone", "====> inside gallery");
                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("video/*");
                                    startActivityForResult(intent, 39);
                                }
                                dialog.dismiss();

                            } else if (pos == 2) {
                                dialog.dismiss();

                            }
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

    private void showFormDialog() {
        try {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
            String[] type = {"Create Form", "Edit Existing Form", "Cancel"};

            alert_builder.setSingleChoiceItems(type, 0,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int pos) {
                            try {
                                // TODO Auto-generated method stub
                                if (pos == 0) {
                                    if (CallDispatcher.LoginUser != null) {
                                        Intent add_intent = new Intent(context,
                                                AddNewForm.class);
                                        add_intent.putExtra("isvalid", false);
                                        add_intent.putExtra("isChat", true);
                                        context.startActivity(add_intent);
                                        dialog.dismiss();

                                    } else {

                                        showToast("Sorry, can't able to create forms in offline mode");
                                    }
                                } else if (pos == 1) {
                                    FormsFragment formsFragment = FormsFragment
                                            .newInstance(context);
                                    FragmentManager fragmentManager = SingleInstance.mainContext
                                            .getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager
                                            .beginTransaction();
                                    fragmentTransaction
                                            .replace(
                                                    R.id.activity_main_content_fragment,
                                                    formsFragment);
                                    fragmentTransaction
                                            .commitAllowingStateLoss();
                                    SingleInstance.fromGroupChat = true;
                                    Intent intent = new Intent(context,
                                            AppMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(intent);
                                    dialog.dismiss();

                                } else if (pos == 2)
                                    dialog.dismiss();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
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

    private String setProfileImage(String userName) {

        try {

            String profilePicPath = Environment.getExternalStorageDirectory()
                    + "/COMMedia/profile/" + userName + ".png";
            File file = new File(profilePicPath);
            if (file.exists())
                file.delete();
            String profilePict = Environment.getExternalStorageDirectory()
                    + "/COMMedia/"
                    + callDisp.getdbHeler(context).getProfilePic(userName);
            Log.i("chat", "chat123 :: profile pic path " + profilePict);
            Bitmap bmp = callDisp.ResizeImage(profilePict);
            if (bmp != null) {
                FileOutputStream out = new FileOutputStream(profilePicPath);
                bmp.compress(Bitmap.CompressFormat.PNG, 50, out);
                out.flush();
                out.close();
            }
            return profilePicPath;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }
        return null;

    }

    public void notifyFormEdited(String formName) {
        sendMsg(formName + " edited", null, "text", null);
    }

    private void createProfileImageFileAndFolder() {

        try {
            String sdCard = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/COMMedia/profile";

            File file = new File(sdCard);
            if (!file.exists())
                file.mkdirs();
            if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void createFilesAndFolder(String chatFileName) {
        try {
            String sdCard = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/COMMedia/chat";
            File file = new File(sdCard);
            if (!file.exists())
                file.mkdirs();
            file = new File(sdCard + "/" + chatFileName);
            if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            // TODO Auto-generated method stub
            GroupChatPermissionBean gcpBean = null;
            switch (view.getId()) {
                case R.id.btn_image:
                    if (isGroup || isRounding) {
                        gcpBean = SingleInstance.mainContext
                                .getGroupChatPermission(groupBean);
                        if (gcpBean.getChat().equalsIgnoreCase("1")) {
                            photochat();
                        } else {
                            showToast("Sorry you dont have permission");
                        }
                    } else {
                        photochat();
                    }
                    break;
                case R.id.btn_handsketch:
                    if (isGroup || isRounding) {
                        gcpBean = SingleInstance.mainContext
                                .getGroupChatPermission(groupBean);
                        if (gcpBean.getChat().equalsIgnoreCase("1")) {
                            // Intent intent = new Intent(context,
                            // HandSketchActivity.class);
                            Intent intent = new Intent(context,
                                    HandSketchActivity2.class);
                            startActivityForResult(intent, 36);
                        } else {
                            showToast("Sorry you dont have permission");
                        }
                    } else {
                        // Intent intent = new Intent(context,
                        // HandSketchActivity.class);
                        Intent intent = new Intent(context,
                                HandSketchActivity2.class);
                        startActivityForResult(intent, 36);
                    }
                    break;
                case R.id.btn_audio:
                    if (isGroup || isRounding) {
                        gcpBean = SingleInstance.mainContext
                                .getGroupChatPermission(groupBean);
                        if (gcpBean.getChat().equalsIgnoreCase("1")) {
//						showAudioMessageDialog();
                            audio_layout.setVisibility(View.VISIBLE);
                        } else {
                            showToast("Sorry you dont have permission");
                        }
                    } else {
                        audio_layout.setVisibility(View.VISIBLE);
//					showAudioMessageDialog();
                    }
                    break;
                case R.id.btn_video:
                    if (isGroup || isRounding) {
                        gcpBean = SingleInstance.mainContext
                                .getGroupChatPermission(groupBean);
                        if (gcpBean.getChat().equalsIgnoreCase("1")) {
                            showVideoMessageDialog();
                        } else {
                            showToast("Sorry you dont have permission");
                        }
                    } else {
                        showVideoMessageDialog();
                    }
                    break;
//			case R.id.btn_forms:
//				if (isGroup) {
//					showFormDialog();
//				} else {
//					showFormDialog();
//				}
//				break;
                case R.id.attach: {
                    hideKeyboard();
                    if (attachBtn.getTag().equals(0)) {
                        attachBtn.setTag(1);
                        attachment_layout.setVisibility(View.VISIBLE);
                    } else {
                        attachBtn.setTag(0);
                        attachment_layout.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
                case R.id.settings_image:
                    if (CallDispatcher.LoginUser != null) {
                        if (message.getText().toString().length() > 0) {
                            Intent pMsgIntent = new Intent(context,
                                    PrivateMessageActivity.class);
                            pMsgIntent.putExtra("groupid", groupId);
                            pMsgIntent.putExtra("buddyname", buddy);
                            pMsgIntent.putExtra("type", "text");
                            pMsgIntent.putExtra("message", message.getText()
                                    .toString());
                            startActivity(pMsgIntent);

                        }
                    }
                    break;
                case R.id.settingsmenu:
                    Intent settingIntent = new Intent(context,
                            GroupChatSettings.class);
                    settingIntent.putExtra("groupid", groupId);
                    startActivity(settingIntent);
                    break;
                case R.id.btn_cancel:
                    finish();
                    break;
                case R.id.c_audio_call:
                    if(!CallDispatcher.myStatus.equalsIgnoreCase("0")) {
                        if ((int)audio_call.getTag() == 1) {
                            if(isPrivateBack){
                                LL_privateReply.setVisibility(View.GONE);
                            }else {
                                msgoptionview.setVisibility(View.GONE);
                            }
                            if (isprivateclicked || isReplyclicked || isurgentclicked || isconfirmclicked)
                                sendSplMsg();
                            else if(isPrivateBack) {
                                if(PrivateReply_view!=null) {
                                    PrivateReplyBack(PrivateReply_view);
                                }
                            }
                            else {

                                if (SendListUI.size() == 1) {
                                    Log.i("sendlist","ifpart");
                                    Log.i("audioplay", "path--->" + strIPath);
                                    SendListUIBean bean = SendListUI.get(0);
                                    sendMsg(message.getText().toString().trim(),
                                            bean.getPath(), bean.getType(), null);
                                    message.setVisibility(View.VISIBLE);
                                    SendListUI.remove(0);
                                    if (SendListUI.size() == 0) {
                                        SendListUI.clear();
                                    }
                                    sendlistadapter.notifyDataSetChanged();
                                    list_all.removeAllViews();
                                    final int adapterCount = sendlistadapter.getCount();

                                for (int i = 0; i < adapterCount; i++) {
                                    View item = sendlistadapter.getView(i, null, null);
                                    list_all.addView(item);
                                }
                                if(adapterCount>=2){
                                    multi_send.getLayoutParams().height=280;
                                }
                                audio_call.setBackgroundResource(R.drawable.chat_send);
                                audio_call.setTag(1);
//                                relative_send_layout.getLayoutParams().height = 90;
                                } else if (SendListUI.size() > 1) {
                                    String path = null;
                                    Log.i("sendlist","elsepart");
                                    for (int i = 0; i < SendListUI.size(); i++) {
                                        SendListUIBean bean = SendListUI.get(i);
                                        Log.i("sendlist","forloop");
                                        if (path == null) {
                                            path = bean.getPath();
                                        } else {
                                            path = path + "," + bean.getPath();
                                        }
                                    }


                                    sendMsg(message.getText().toString().trim(),
                                            path, "mixedfile", null);
                                    message.setVisibility(View.VISIBLE);
                                    SendListUI.remove(0);
                                    if (SendListUI.size() > 0) {
                                        SendListUI.clear();
                                    }
                                    sendlistadapter.notifyDataSetChanged();
                                    list_all.removeAllViews();
                                    final int adapterCount = sendlistadapter.getCount();

                                for (int i = 0; i < adapterCount; i++) {
                                    View item = sendlistadapter.getView(i, null, null);
                                    list_all.addView(item);
                                }
                                if(adapterCount>=2){
                                    multi_send.getLayoutParams().height=280;
                                }
                                audio_call.setBackgroundResource(R.drawable.chat_send);
                                audio_call.setTag(1);


                            } else {
                                if (message.getText().toString().trim().length() > 0) {
                                    if (CallDispatcher.LoginUser != null) {
                                        if (message.getText().toString().length() > 700) {
                                            showToast("Text exceeds 700 characters");
                                        } else {
                                            sendMsg(message.getText().toString().trim(),
                                                    null, "text", null);
                                            message.setText("");
                                        }
                                    } else {
                                        showAlert1("Info", "Check Internet Connection");
                                    }
                                }
                            }
                        }
                        multi_send.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;
                    } else {

                            if (CallDispatcher.isCallignored) {
                                showToast("Please Try again... Ignored call in progress");
                            } else {
                                if (isGroup || isRounding) {
                                    Log.d("Test", "Inside Group audioconference onclick");
                                    gcpBean = SingleInstance.mainContext
                                            .getGroupChatPermission(groupBean);
                                    if (gcpBean.getAudioConference().equalsIgnoreCase("1")) {
                                        if (!CallDispatcher.isCallInitiate) {
                                            if (!CallDispatcher.GSMCallisAccepted) {
                                                groupCallMenu(0);
                                            } else {
                                                showToast("Please Try again... GSM call  in progress");
                                            }
                                        } else {
                                            showToast("Please Try again... call in progress");
                                        }
                                    } else {
                                        showToast("Sorry you dont have permission");
                                    }
                                } else {
                                    if (!CallDispatcher.isCallInitiate) {
                                        if (!CallDispatcher.GSMCallisAccepted) {
                                            individualCallMenu(0);
                                        } else {
                                            showToast("Please Try again... GSM call in progress");
                                        }
                                    } else {
                                        showToast("Please Try again... call in progress");
                                    }
//                            ContactsFragment.getInstance(context).sipprocessCallRequest(buddy);
                                }
                            }
                        }
                    }else
                    showToast("You are in offline");
                    break;
                case R.id.gridview:
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if(!CallDispatcher.myStatus.equalsIgnoreCase("0")) {
                        if (!isGrid) {
                            btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_close));
                            atachlay.setVisibility(View.VISIBLE);
                            isGrid = true;
                        } else {
                            btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
                            atachlay.setVisibility(View.GONE);
                            audio_layout.setVisibility(View.GONE);
                            isGrid = false;
                        }
                    }else{
                        showToast("You are in offline");
                    }
                    break;

                case R.id.c_video_call:
                    if (isGroup || isRounding) {
                        Log.d("Test", "Inside Group VideoConference onclick");
                        gcpBean = SingleInstance.mainContext
                                .getGroupChatPermission(groupBean);
                        if (gcpBean.getVideoConference().equalsIgnoreCase("1")) {
                            groupCallMenu(2);
                        } else {
                            showToast("Sorry you dont have permission");
                        }
                    } else {
                        individualCallMenu(1);
                    }
                    break;
                case R.id.video_call1:
                    if (isGroup || isRounding) {
                        gcpBean = SingleInstance.mainContext
                                .getGroupChatPermission(groupBean);
                        if (gcpBean.getVideoConference().equalsIgnoreCase("1")) {
                            groupCallMenu(2);
                        } else {
                            showToast("Sorry you dont have permission");
                        }
                    } else {
                        individualCallMenu(1);
                    }
                    break;
                case R.id.video_call2:
                    if (isGroup || isRounding) {
                        gcpBean = SingleInstance.mainContext
                                .getGroupChatPermission(groupBean);
                        if (gcpBean.getVideoBroadcast().equalsIgnoreCase("1")) {
                            groupCallMenu(3);
                        } else {
                            showToast("Sorry you dont have permission");
                        }
                    } else {
                        individualCallMenu(3);
                    }
                    break;
                case R.id.btn_location:
                    if (CallDispatcher.LoginUser != null) {
                        if (CallDispatcher.latitude == 0.0
                                && CallDispatcher.longitude == 0.0) {
                            showToast("Sorry! Turn On Location Service ");
                        } else {
                            sendMsg("Latitude:" + CallDispatcher.latitude + ","
                                            + "Longitude:" + CallDispatcher.longitude,
                                    null, "location", null);
                        }
                    } else {
                        showToast("Sorry! can not send Message");
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void processAttachmentView() {
        if (attachBtn.getTag().equals(1)) {
            attachBtn.setTag(0);
            attachment_layout.setVisibility(View.GONE);
            maintainListPosition();
        }
    }

    private void loadProfilePic() {
        try {
            Thread profileThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        // TODO Auto-generated method stub
                        String senderImagePath = setProfileImage(CallDispatcher.LoginUser);
                        final String receiverImagePath = setProfileImage(buddy);

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    // TODO Auto-generated method stub
                                    if (receiverImagePath != null) {

                                        // start 07-10-15 changes

                                        imageViewer.display(
                                                receiverImagePath, Sender_img,
                                                R.drawable.icon_buddy_aoffline);

                                        // ended 07-10-15 changes

                                    }

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });
            profileThread.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }
    }

    private void showMessageDialog(final GroupChatBean gcBean1) {
        try {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
            alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.chat_options));
            CharSequence[] b_type = {SingleInstance.mainContext.getResources().getString(R.string.delete), SingleInstance.mainContext.getResources().getString(R.string.withdraw), SingleInstance.mainContext.getResources().getString(R.string.cancel)};
            alert_builder.setSingleChoiceItems(b_type, 0,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int pos) {
                            // TODO Auto-generated method stub
                            if (pos == 0) {
                                callDisp.getdbHeler(context)
                                        .deleteGroupChatEntry(
                                                gcBean1.getSignalid(),
                                                CallDispatcher.LoginUser);
                                chatList.remove(gcBean1);
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            } else if (pos == 1) {
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            context);
                                    String message = "Do you want withdraw?";

                                    builder.setMessage(message)
                                            .setCancelable(false)
                                            .setPositiveButton(
                                                    "Withdraw",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
//                                                            ExchangesAdapter.lastMsgClear();
                                                            final GroupChatBean gcBean = gcBean1
                                                                    .clone();
                                                            gcBean.setpSingnalId(gcBean
                                                                    .getSignalid());
                                                            gcBean.setSignalid(Utility
                                                                    .getSessionID());
                                                            Log.i("group123",
                                                                    "signalid :: "
                                                                            + gcBean.getpSingnalId());
                                                            gcBean.setType("104");
                                                            if (isGroup || isRounding)
                                                                gcBean.setTo(groupBean
                                                                        .getGroupId());
                                                            else
                                                                gcBean.setTo(buddy);
                                                            GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                                                                    .getGroupChatPermission(groupBean);
                                                            if (isGroup || isRounding) {
                                                                if (gcpBean
                                                                        .getWithDrawn()
                                                                        .equalsIgnoreCase(
                                                                                "1")) {
                                                                    SingleInstance
                                                                            .getGroupChatProcesser()
                                                                            .getQueue()
                                                                            .addObject(
                                                                                    gcBean);
                                                                    chatList.remove(gcBean1);
                                                                    adapter.notifyDataSetChanged();
                                                                } else {
                                                                    showToast("Sorry you dont have permission");
                                                                }
                                                            } else {
                                                                SingleInstance
                                                                        .getGroupChatProcesser()
                                                                        .getQueue()
                                                                        .addObject(
                                                                                gcBean);
                                                                chatList.remove(gcBean1);

                                                                adapter.notifyDataSetChanged();
                                                            }
                                                            for (BuddyInformationBean bBean : ContactsFragment
                                                                    .getBuddyList()) {
                                                                if (!bBean.isTitle()) {
                                                                    if (bBean.getName().equalsIgnoreCase(buddy)) {
                                                                        bBean.setLastMessage(null);
                                                                        ContactsFragment.getContactAdapter()
                                                                                .notifyDataSetChanged();
                                                                        break;
                                                                    }
                                                                }
                                                            }

                                                        }

                                                    })
                                            .setNegativeButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            dialog.dismiss();

                                                        }
                                                    });
                                    dialog.dismiss();

                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } else if (pos == 2) {
                                dialog.dismiss();

                            }
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

    private void clearAllAlert(String groupOrBuddyName) {

        try {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
            myAlertDialog.setTitle(SingleInstance.mainContext.getResources().getString(R.string.clear_chat_history));
            myAlertDialog
                    .setMessage(SingleInstance.mainContext.getResources().getString(R.string.are_you_sure_you_want_to_delete_entire_chat_history)
                            + " " + nickname + "?");
            myAlertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            clearAll();
                        }
                    });
            myAlertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int arg1) {
                            // do something when the Cancel button is clicked

                            dialog.cancel();
                        }
                    });
            myAlertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }

    private void viewProfile(String buddy) {
        // ViewProfileFragment viewProfileFragment = ViewProfileFragment
        // .newInstance(context);
        // AppMainActivity appMainActivity = SingleInstance.mainContext;
        // viewProfileFragment.setBuddyName(buddy);
        // FragmentManager fragmentManager = appMainActivity
        // .getSupportFragmentManager();
        // FragmentTransaction fragmentTransaction = fragmentManager
        // .beginTransaction();
        // fragmentTransaction.replace(R.id.activity_main_content_fragment,
        // viewProfileFragment);
        // fragmentTransaction.commitAllowingStateLoss();
        Intent intent = new Intent(context, ViewProfiles.class);
        intent.putExtra("buddyname", buddy);
        startActivity(intent);

    }

    public void notifyViewProfile() {
        try {
            ArrayList<String> profileList = callDisp.getdbHeler(context)
                    .getProfile(buddy);
            Log.i("profile", "size of arrayList--->" + profileList.size());
            Log.i("profile1234", "isProfileRequested : " + isProfileRequested);
            if (isProfileRequested) {
                isProfileRequested = false;
                if (profileList.size() > 0) {
                    ViewProfiles viewProfiles = (ViewProfiles) WebServiceReferences.contextTable
                            .get("viewprofileactivity");
                    if (viewProfiles == null) {
                        viewProfile(buddy);
                    } else {
                        viewProfiles.initView();
                    }

                } else if (buddy != null)
                    showAlert1("Info", SingleInstance.mainContext.getResources().getString(R.string.no_profile_for1) + buddy);
                // Toast.makeText(context,
                // "No profile Assigned for this user",
                // Toast.LENGTH_SHORT).show();
                cancelDialog();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void clearAll() {

        try {
            // TODO Auto-generated method stub
            // File file = new File(Environment.getExternalStorageDirectory()
            // + "/COMMedia/chat/" + fileHistoryName);
            // if (file.exists()) {
            // file.delete();
            // }
            if (!isGroup || !isRounding) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            chatList.clear();
                            Vector<GroupChatBean> chatHistory = SingleInstance.groupChatHistory
                                    .get(buddy);
                            chatHistory.clear();
                            DBAccess.getdbHeler().deleteGroupChatEntryLocally(
                                    buddy, CallDispatcher.LoginUser);
                            DBAccess.getdbHeler().deleteIndividualChat(buddy);
                            if (SingleInstance.groupChatHistory
                                    .containsKey(buddy)) {
                                SingleInstance.groupChatHistory.remove(buddy);
                            }
                            for (BuddyInformationBean bBean : ContactsFragment
                                    .getBuddyList()) {
                                if (!bBean.isTitle()) {
                                    if (bBean.getName().equalsIgnoreCase(buddy)) {
                                        bBean.setLastMessage("");
                                        ContactsFragment.getContactAdapter()
                                                .notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }

                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            } else {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            chatList.clear();
                            if (SingleInstance.groupChatHistory
                                    .containsKey(groupId)) {
                                Vector<GroupChatBean> chatHistory = SingleInstance.groupChatHistory
                                        .get(groupId);
                                chatHistory.clear();
                                SingleInstance.groupChatHistory.remove(groupId);
                            }
                            callDisp.getdbHeler(context)
                                    .deleteGroupChatEntryLocally(
                                            groupBean.getGroupId(),
                                            CallDispatcher.LoginUser);

                            DBAccess.getdbHeler().deleteGroupChatEntryLocally(
                                    groupBean.getGroupId(), CallDispatcher.LoginUser);
                            adapter.notifyDataSetChanged();
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

    private void showprogress() {

        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Progress ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();

    }

    public void cancelDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public class GroupChatAdapter extends ArrayAdapter<GroupChatBean> {

        /***********
         * Declare Used Variables
         *********/
        private Context context;
        private Vector<GroupChatBean> chatList;
        private LayoutInflater inflater = null;
        ChatBean tempValues = null;
        int i = 0;
        ImageLoader imageLoader;

        private Vector<GroupChatBean> arraylist;

//        GroupChatBean finalPlayBean;

        /*************
         * CustomAdapter Constructor
         *****************/
        public GroupChatAdapter(Context context, Vector<GroupChatBean> chatList) {

            super(context, R.layout.group_chat_row, chatList);
            /********** Take passed values **********/
            this.context = context;
            this.chatList = chatList;
            imageLoader = new ImageLoader(context.getApplicationContext());
            this.arraylist = new Vector<>();
            this.arraylist.addAll(chatList);
            /*********** Layout inflator to call external xml layout () ***********/
        }

        public GroupChatBean getItem(int position) {
            return chatList.get(position);
        }

        public Vector<GroupChatBean> getAllitem(){
            return chatList;
        }

        public long getItemId(int position) {
            return position;
        }

//        public int getItemViewType(int position) {
//            Log.d("Swipeselect","Adapter Swipeselect : "+position);
////            // current menu type
//            GroupChatBean gcBean = chatList.get(position);
//            if(gcBean.getFrom().equals(CallDispatcher.LoginUser))
//            {
//                return 0;
//            }else{
//            return 1;
//            }
//        }


        @Override
        public int getItemViewType(int position) {
//            return super.getItemViewType(position);
//            return position % 3;
            GroupChatBean gcBean = chatList.get(position);
            if(!gcBean.isforward()) {
                if (gcBean.getCategory() != null && gcBean.getCategory().equalsIgnoreCase("call")) {
                    return 2;
                } else if (gcBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                    return 0;
                } else {
                    return 1;
                }
            }else{
                return 2;
            }
        }
//
        @Override
        public int getViewTypeCount() {
//            return super.getViewTypeCount();
            return 3;
        }

        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            try {
                if (convertView == null) {
                    inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.group_chat_row,
                            null);
                }

                RelativeLayout rr_send=(RelativeLayout) convertView.findViewById(R.id.rr_resend);
                rr_send.setVisibility(View.GONE);
                Button btn_resend=(Button)convertView.findViewById(R.id.btn_resend);
                ImageView videoPlay=(ImageView)convertView.findViewById(R.id.videoPlay);
                videoPlay.setVisibility(View.GONE);
                ImageView receiver_videoPlay=(ImageView)convertView.findViewById(R.id.receiver_videoPlay);
                receiver_videoPlay.setVisibility(View.GONE);


                ImageView thumbsUp, thumbsDown;
                final GroupChatBean gcBean = chatList.get(position);

                TextView tv_user, message=null, dateTime, deadlineReplyText, tv_urgent;
                RelativeLayout senderLayout = null,normalcontainer,receive_quotedLayout;
                LinearLayout join_lay;
                TextView tv_username,tv_missed,time,receiver_tvquoted_msg;
                Button joinBtn;
                ImageView call_img;
                RelativeLayout receiverLayout = null;
                // ImageView buddyIcon;
                ProgressBar progress, intermediateProgress;
                final ImageView multimediaIcon, locationIcon, saveMsg, scheduleMsg, retryIcon, img_status1, audio_play;
                final SeekBar audio_seekbar;
                final RelativeLayout iconContainer, audioLayout, mainlayout,rl_header;
                normalcontainer = (RelativeLayout) convertView
                        .findViewById(R.id.normalcontainer);
                join_lay = (LinearLayout) convertView
                        .findViewById(R.id.join_lay);
                senderLayout = (RelativeLayout) convertView
                        .findViewById(R.id.sender_view);
                tv_username=(TextView)convertView.findViewById(R.id.tv_user);
                tv_missed=(TextView)convertView.findViewById(R.id.tv_missed);
                time=(TextView)convertView.findViewById(R.id.time);
                joinBtn=(Button)convertView.findViewById(R.id.joinBtn);
                call_img=(ImageView)convertView.findViewById(R.id.call_img);
                Button btn_reply = (Button) convertView
                        .findViewById(R.id.btn_reply);
                Button btn_private = (Button) convertView
                        .findViewById(R.id.btn_private);
                TextView tv_replied = (TextView) convertView
                        .findViewById(R.id.tv_replied);
                TextView im_pin = (TextView) convertView
                        .findViewById(R.id.tv_pathname);
                TextView tvprivate = (TextView) convertView
                        .findViewById(R.id.tvprivate);
                Button btn_confrm = (Button) convertView
                        .findViewById(R.id.btn_confrm);
                TextView waitforconfir = (TextView) convertView
                        .findViewById(R.id.waitforconfir);
                TextView received_confirmation=(TextView) convertView.findViewById(R.id.tv_confirm);
                TextView tv_today = (TextView) convertView
                        .findViewById(R.id.viewtoday);
                TextView waitforreply = (TextView) convertView
                        .findViewById(R.id.waitforreply);
                receiverLayout = (RelativeLayout) convertView
                        .findViewById(R.id.receiver_view);
                TextView tv_senderwithdraw=(TextView)convertView.findViewById(R.id.tv_senderwithdraw);
                tv_senderwithdraw.setVisibility(View.GONE);
                LinearLayout privatelay=(LinearLayout)convertView.findViewById(R.id.privatelay);
                TextView privatename=(TextView)convertView.findViewById(R.id.privatename);
                RelativeLayout splmsgview = (RelativeLayout) convertView.findViewById(R.id.splmsgview);
                TextView audio_tv;
                TextView withdraw = (TextView) convertView.findViewById(R.id.withdrawlay);
                TextView xbutton = (TextView) convertView.findViewById(R.id.xdelete);
                mainlayout = (RelativeLayout) convertView.findViewById(R.id.receiver_layout);
                rl_header = (RelativeLayout) convertView.findViewById(R.id.rl_header);
                Log.i("group123", "gcbean from : " + gcBean.getFrom() +
                        "gcbean to:" + gcBean.getTo() + " login user :" + CallDispatcher.LoginUser
                        + " message : " + gcBean.getMessage());
                waitforreply.setVisibility(View.GONE);
                btn_reply.setVisibility(View.GONE);
                tv_replied.setVisibility(View.GONE);
                waitforconfir.setVisibility(View.GONE);
                received_confirmation.setVisibility(View.GONE);
                btn_confrm.setVisibility(View.GONE);
                btn_private.setVisibility(View.GONE);
                privatelay.setVisibility(View.GONE);
                selectInvidual_buddy = (CheckBox) convertView
                        .findViewById(R.id.selectInvidual_buddy);
                selectInvidual_buddy.setTag(gcBean);
                LinearLayout chat_view = null;
//                    selectAll_buddy.setSelected(gcBean.getSelect());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                Calendar cal = Calendar.getInstance();
                String todayDate = format.format(cal.getTime());
                if (todayDate.equals(gcBean.getSenttime().split(" ")[0])) {
                    tv_today.setText("Today");
                } else if (getYesterdayDateString(format).equals(gcBean.getSenttime().split(" ")[0])) {
                    tv_today.setText("Yesterday");
                } else {
                    Date d1 = format.parse(gcBean.getSenttime().split(" ")[0]);
                    String newdate = sdf.format(d1);
                    tv_today.setText(newdate);
                }
                tv_today.setVisibility(View.VISIBLE);
                if (position > 0) {
                    final GroupChatBean gcbn = chatList.get(position - 1);
                    if (gcbn.getSenttime().split(" ")[0].equals(gcBean.getSenttime().split(" ")[0])) {
                        tv_today.setVisibility(View.GONE);
                    } else {
                        tv_today.setVisibility(View.VISIBLE);
                    }
                }

                Log.i("selectAll_buddy", "selectAll_buddy contains status : " + gcBean.isSelect());
                if(gcBean.getCategory()!=null&&gcBean.getCategory().equalsIgnoreCase("call")) {
                    swipeposition = 2;
                    normalcontainer.setVisibility(View.GONE);
                    join_lay.setVisibility(View.VISIBLE);
                    String dat, tim;
                    dat = gcBean.getSenttime().split(" ")[1].split(":")[0] + ":" + gcBean.getSenttime().split(" ")[1].split(":")[1];
                    tim = gcBean.getSenttime().split(" ")[2].toUpperCase();

                    time.setText(dat + " " + tim);
                    join_lay.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context,
                                    CallHistoryActivity.class);
                            intent.putExtra("isviewed",true);
                            intent.putExtra("sessionid",
                                    gcBean.getSessionid());
                            startActivity(intent);
                        }
                    });
//                    String total_users = gcBean.getFtpUsername()+","+gcBean.getFtpPassword();
                    String total_users = gcBean.getFtpUsername();
                    if(gcBean.getFtpPassword() != null) {
                        total_users = total_users + "," + gcBean.getFtpPassword();
                    }
                    ArrayList<String> tot_users_arraylist = new ArrayList<String>();
                    if(total_users.contains(",")) {
                        String[] total_users_array = total_users.split(",");
                        tot_users_arraylist = new ArrayList<>(Arrays.asList(total_users_array));
                    } else {
                        tot_users_arraylist.add(total_users);
                    }

                    String callbuddies = null, call_buddy_fullnames = null;
                    for(String call_buddy : tot_users_arraylist) {
                        if(!call_buddy.equalsIgnoreCase(CallDispatcher.LoginUser)) {

                            if(callbuddies == null) {
                                callbuddies = call_buddy;
                                if( Buddyname(call_buddy) == null) {
                                    call_buddy_fullnames = call_buddy;
                                } else {
                                    call_buddy_fullnames = Buddyname(call_buddy);
                                }
                            } else {
                                callbuddies = callbuddies+","+call_buddy;
                                if( Buddyname(call_buddy) == null) {
                                    call_buddy_fullnames = call_buddy_fullnames+","+call_buddy;
                                } else {
                                    call_buddy_fullnames = call_buddy_fullnames+","+Buddyname(call_buddy);
                                }
                            }
                        }
                    }

                    if(call_buddy_fullnames == null) {

                        total_users = gcBean.getFrom()+","+gcBean.getTo();
                        String[]  total_users_array = total_users.split(",");

                        for(String call_buddy : total_users_array) {
                            if(!call_buddy.equalsIgnoreCase(CallDispatcher.LoginUser)) {

                                if(callbuddies == null) {
                                    callbuddies = call_buddy;
                                    if( Buddyname(call_buddy) == null) {
                                        call_buddy_fullnames = call_buddy;
                                    } else {
                                        call_buddy_fullnames = Buddyname(call_buddy);
                                    }
                                } else {
                                    callbuddies = callbuddies+","+call_buddy;
                                    if( Buddyname(call_buddy) == null) {
                                        call_buddy_fullnames = call_buddy_fullnames+","+call_buddy;
                                    } else {
                                        call_buddy_fullnames = call_buddy_fullnames+","+Buddyname(call_buddy);
                                    }
                                }
                            }
                        }
                    }

                    tv_username.setText(call_buddy_fullnames);
                    if(gcBean.getSubCategory()!=null &&gcBean.getSubCategory().equalsIgnoreCase("missedcall")) {
                        if (gcBean.getFtpPassword() != null) {
                            String[] mlist = (gcBean.getFtpPassword()).split(",");
                            if (mlist.length > 1) {
                                if (gcBean.isJoin()) {
                                    Log.i("AudioCall", "gcBean.isJoin() = true");
                                    joinBtn.setVisibility(View.VISIBLE);
                                } else {
                                    Log.i("AudioCall", "gcBean.isJoin() = false");
                                    joinBtn.setVisibility(View.GONE);
                                }
                                join_lay.setBackgroundColor(getResources().getColor(R.color.blue1));
                                if (gcBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                                    call_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.iconoutgoingcall));
                                } else {
                                    call_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_incoming_call));
                                }
                                tv_missed.setVisibility(View.GONE);
//                                tv_username.setText(callbuddies);
                            } else {
                                join_lay.setBackgroundColor(getResources().getColor(R.color.darkpink));
                                call_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_missed_call));
                                joinBtn.setVisibility(View.GONE);
                                tv_missed.setVisibility(View.VISIBLE);
                                tv_missed.setText("missed");
                                tv_missed.setTextColor(getResources().getColor(R.color.pink));
                                time.setVisibility(View.VISIBLE);
                            }
                        } else {
                            join_lay.setBackgroundColor(getResources().getColor(R.color.darkpink));
                            call_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_missed_call));
                            joinBtn.setVisibility(View.GONE);
                            tv_missed.setVisibility(View.VISIBLE);
                            tv_missed.setText("missed");
                            tv_missed.setTextColor(getResources().getColor(R.color.pink));
                            time.setVisibility(View.VISIBLE);
                        }
                    } else {
                        joinBtn.setVisibility(View.GONE);
                        join_lay.setBackgroundColor(getResources().getColor(R.color.grey1));
                        if (gcBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                            call_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.iconoutgoingcall));
                        } else {
                            call_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_incoming_call));
                        }
                        if (gcBean.getReminderTime() != null) {
                            tv_missed.setText(gcBean.getReminderTime());
                        } else {
                            tv_missed.setText("00:00:00");
                        }
                        tv_missed.setTextColor(getResources().getColor(R.color.blue2));
                        time.setVisibility(View.VISIBLE);

                    }
                    joinBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                int caseid;
                                if(gcBean.getMessage().equalsIgnoreCase("AC"))
                                     caseid=1;
                                else
                                    caseid=2;

                                DBAccess.getdbHeler().updateGroupCallChatEntry(gcBean);
                                gcBean.setIsJoin(false);

                                RecordTransactionBean transactionBean = new RecordTransactionBean();
                                transactionBean.setSessionid(gcBean.getSessionid());
                                transactionBean.setHost(gcBean.getFtpUsername());
                                transactionBean.setParticipants(gcBean.getFtpPassword());
                                transactionBean.setChatid(gcBean.getGroupId());

                                String total_participants = gcBean.getFtpPassword()+","+gcBean.getFtpUsername();

                                String[] total_participant_array = total_participants.split(",");
                                int con_scr_opened = 0;
                                for(int i=0;i<total_participant_array.length;i++) {
                                    String user = total_participant_array[i];
                                    Log.i("AudioCall"," Call to User : "+user);
    //                                RecordTransactionBean transactionBean = new RecordTransactionBean();
    //                                transactionBean.setSessionid(gcBean.getSessionid());
    //                                transactionBean.setHost(gcBean.getFtpUsername());
    //                                transactionBean.setParticipants(gcBean.getFtpPassword());
    //                                transactionBean.setChatid(gcBean.getGroupId());
    //                                if (transactionBean.getParticipants() != null) {
    //                                    DBAccess.getdbHeler().updateGroupCallChatEntry(gcBean);
    //                                    gcBean.setIsJoin(false);
    //                                    String[] temp = transactionBean.getParticipants().split(",");
    //                                    for (String user : temp)
                                    if(user != null && !user.equalsIgnoreCase(CallDispatcher.LoginUser)) {
                                        Log.i("AudioCall"," Call to User 1 : "+user);
                                            calldisp.MakeCallFromCallHistory(caseid,
                                                    user, context, transactionBean, con_scr_opened, "");
                                             con_scr_opened = con_scr_opened+1;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }else {
                    join_lay.setVisibility(View.GONE);
                    normalcontainer.setVisibility(View.VISIBLE);

                    if (forward) {
                        swipeposition=2;
                        selectInvidual_buddy.setVisibility(View.VISIBLE);
                        selectInvidual_buddy.setChecked(gcBean.isSelect());
                    } else {
                        selectInvidual_buddy.setVisibility(View.GONE);
                    }
                    if(gcBean.isSelect()){
                        selectInvidual_buddy.setChecked(true);
                    }else{
                        selectInvidual_buddy.setChecked(false);
                    }

                    selectInvidual_buddy.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((CheckBox) v).isChecked()) {
                                gcBean.setSelect(true);
                                checkBoxCounter++;
                                countofcheckbox(checkBoxCounter);

                            } else {
                                gcBean.setSelect(false);
                                checkBoxCounter--;
                                countofcheckbox(checkBoxCounter);
                            }
                        }
                    });
                    receive_quotedLayout=(RelativeLayout)convertView.findViewById(R.id.sen_quoted);
                    receiver_tvquoted_msg=(TextView)convertView.findViewById(R.id.sen_tvquoted_msg);
//				senderLayout.setBackgroundResource(R.color.gchat_bg);
//				receiverLayout.setBackgroundResource(R.color.gchat_bg);
                    if (gcBean.getFrom().equals(CallDispatcher.LoginUser)) {
                        Log.i("reply","loginuser");
                        swipeposition = 0;
                        msgStatus = position;
                        tv_urgent = (TextView) convertView
                                .findViewById(R.id.tv_urgent);
                        chat_view = (LinearLayout) convertView
                                .findViewById(R.id.lintest);
                        tv_user = (TextView) convertView
                                .findViewById(R.id.receiver_user);
                        message = (TextView) convertView
                                .findViewById(R.id.receiver_text_msg);
                        RelativeLayout listrel_quoted = (RelativeLayout) convertView
                                .findViewById(R.id.rel_quoted);
                        listrel_quoted.setVisibility(View.GONE);

                        dateTime = (TextView) convertView
                                .findViewById(R.id.receiver_datetime);
                        multimediaIcon = (ImageView) convertView
                                .findViewById(R.id.receiver_multi_msg);
                        multimediaIcon.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;
                        multimediaIcon.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                        iconContainer = (RelativeLayout) convertView
                                .findViewById(R.id.receiver_icon_container);
                        locationIcon = (ImageView) convertView
                                .findViewById(R.id.receiver_loc_icon);
                        scheduleMsg = (ImageView) convertView
                                .findViewById(R.id.receiver_schedule_icon);
                        TextView lvquoted_msg = (TextView) convertView
                                .findViewById(R.id.tvquoted_msg);
                        senderLayout.setVisibility(View.GONE);
                        receiverLayout.setVisibility(View.VISIBLE);
                        scheduleMsg.setVisibility(View.GONE);
                        saveMsg = (ImageView) convertView
                                .findViewById(R.id.receiver_save_btn);
                        progress = (ProgressBar) convertView
                                .findViewById(R.id.up_progressBar);
                        receiver_status = (TextView) convertView
                                .findViewById(R.id.receiver_status);
                        audioLayout = (RelativeLayout) convertView.findViewById(R.id.ad_play);
                        audio_seekbar = (SeekBar) convertView.findViewById(R.id.seekBar1);
                        audio_play = (ImageView) convertView.findViewById(R.id.play_button);
                        audio_tv = (TextView) convertView.findViewById(R.id.txt_time);
                        audio_tv.setTag(gcBean);
                        tv_urgent.setTag(position);
                        tv_urgent.setVisibility(View.GONE);

                        if (gcBean.getSent() != null && gcBean.getSent().equals("2")) {
                            receiver_status.setText("Delivered");
                        } else if (gcBean.getSent() != null && gcBean.getSent().equals("3")) {
                            receiver_status.setText("Read");
                        } else {
                            receiver_status.setText("Sent");
                        }
                        receiver_status.setVisibility(View.GONE);
                        if (chatList.size() == position + 1) {
                            receiver_status.setVisibility(View.VISIBLE);
                        }
                        thumbsUp = (ImageView) convertView.findViewById(R.id.thumbs_up);
                        thumbsDown = (ImageView) convertView.findViewById(R.id.thumbs_down);
                        // retryIcon = null;
                        // intermediateProgress = null;
                        retryIcon = (ImageView) convertView
                                .findViewById(R.id.up_retry);
                        retryIcon.setVisibility(View.GONE);
                        retryIcon.setTag(gcBean);
                        intermediateProgress = (ProgressBar) convertView
                                .findViewById(R.id.up_progressBar1);
                        intermediateProgress.setVisibility(View.GONE);
                        percentage = (TextView) convertView
                                .findViewById(R.id.up_percentage);
                        deadlineReplyText = (TextView) convertView
                                .findViewById(R.id.receiver_deadline_reply_txt);
//                    Log.d("abcdef","Thumbs retrieved value for sid="+gcBean.getSignalid()+" is "+gcBean.getThumb());
//                    if(gcBean.getThumb()==1){
//                        thumbsUp.setVisibility(View.VISIBLE);
//                        thumbsDown.setVisibility(View.GONE);
//                    }else{
//                        thumbsUp.setVisibility(View.GONE);
//                        thumbsDown.setVisibility(View.VISIBLE);
//                    }
                        if (gcBean.getSubCategory() != null) {
                            if (gcBean.getSubCategory().equalsIgnoreCase("gp")) {
//							tv_user.setText(gcBean.getPrivateMembers());

                                    privatelay.setVisibility(View.VISIBLE);
                                    convertView.setBackgroundResource(R.color.gchat_bg);
                                    deadlineReplyText.setVisibility(View.GONE);

//                                tvprivate.setText(Html.fromHtml("<font color=\"#06F235\">"
//                                        + "Private for: "
//                                        + "</font>"
//                                        + "  "
//                                        + "<font color=\"#FFFFFF\">"
//                                        + gcBean.getPrivateMembers()));
                                    if (gcBean.getPrivateMembers().contains(",")) {
                                        String[] names = gcBean.getPrivateMembers().split(",");
                                        String members = null;
                                        for (String name : names) {
                                            if (Buddyname(name) != null)
                                                if (members != null)
                                                    members = members + "," + Buddyname(name);
                                                else
                                                    members = Buddyname(name);
                                        }
                                        privatename.setText(members);
                                    } else
                                        privatename.setText(Buddyname(gcBean.getPrivateMembers()));

                            } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                    "gu")) {
                                tv_urgent.setVisibility(View.VISIBLE);
                                tv_urgent.setTextColor(Color.parseColor("#daa520"));
                                tv_urgent.setText("  Urgent  !  ");
                                privatelay.setVisibility(View.GONE);
//                                if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                        && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                    tv_urgent.setVisibility(View.GONE);
                                }else{
                                    tv_urgent.setVisibility(View.VISIBLE);
                                }
                            } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                    "gs")) {
                                scheduleMsg.setVisibility(View.VISIBLE);
                                tv_user.setText(SingleInstance.mainContext.getResources().getString(R.string.message_schedule_to)
                                        + gcBean.getPrivateMembers());
                                tv_user.setVisibility(View.VISIBLE);
                                scheduleMsg
                                        .setImageResource(R.drawable.icon_footer_reminder);
                                scheduleMsg.setTag(gcBean);
                                convertView.setBackgroundResource(R.color.gchat_bg);
                                deadlineReplyText.setVisibility(View.GONE);
                            } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                    "gd")) {
//                                receiverLayout
//                                        .setBackgroundResource(R.color.deadlingclr);
//                                convertView
//                                        .setBackgroundResource(R.color.deadlingclr);
                                scheduleMsg
                                        .setImageResource(R.drawable.icon_footer_reminder);
                                scheduleMsg.setVisibility(View.VISIBLE);
                                // tv_user.setText("DeadLine message to : "
                                // + gcBean.getPrivateMembers());
                                // tv_user.setText("DeadLine set by : "
                                // + gcBean.getFrom() + "\nAssigned to : "
                                // + gcBean.getPrivateMembers() + "\nTime : "
                                // + gcBean.getReminderTime() + "\n");
                                tv_user.setText("TODO Assigned to : "
                                        + gcBean.getPrivateMembers() + "\nAt : "
                                        + gcBean.getReminderTime() + "\n");
                                tv_user.setVisibility(View.VISIBLE);
                                scheduleMsg.setTag(gcBean);
                                if (gcBean.getMessage() != null) {
                                    String msg = gcBean.getMessage();
                                    if (gcBean.getMessage().contains("Status : ")) {
                                        Log.i("msg123",
                                                "message2 contains status : "
                                                        + gcBean.getMessage());
                                        String tempMsg = msg.substring(0,
                                                msg.indexOf("Status"));
                                        String originalMsg = msg.replace(tempMsg,
                                                "");
                                        deadlineReplyText.setText(originalMsg);
                                    } else {
                                        deadlineReplyText.setText("");
                                    }
                                } else {
                                    deadlineReplyText.setText("");
                                }
                                deadlineReplyText.setVisibility(View.VISIBLE);
                            } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                    "grb") || gcBean.getSubCategory().equalsIgnoreCase(
                                    "GRB_R")) {
//                                receiverLayout
//                                        .setBackgroundResource(R.color.lgreen);
//                                convertView.setBackgroundResource(R.color.lgreen);
                                scheduleMsg.setTag(gcBean);
//							scheduleMsg.setImageResource(R.drawable.replybg);
//							scheduleMsg.setVisibility(View.VISIBLE);
                                if (gcBean.getReply() != null && !gcBean.getReply().equals("") && gcBean.getReply().equalsIgnoreCase("GRB_R")) {
                                    if (gcBean.getSubCategory().equalsIgnoreCase(
                                            "GRB_R")) {
                                        //Old Code start
//                                        if (position > 0) {
//                                            Log.i("reply","position>0");
//                                            listrel_quoted.setVisibility(View.VISIBLE);
//                                            final GroupChatBean Bean = chatList.get(position - 1);
//                                            if(getReplyMessage(Bean)!=null) {
//                                                lvquoted_msg.setText(getReplyMessage(Bean));
//                                            }
//                                            //For withdraw message
//                                            //start
////                                            if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
////                                                    && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
//                                            if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
//                                                listrel_quoted.setVisibility(View.GONE);
//                                            }else{
//                                                listrel_quoted.setVisibility(View.VISIBLE);
//                                            }
//                                            //End
//                                        }
                                        //Old Code end

                                        //New Code Start
                                        for (int i = 0; i < chatList.size(); i++) {
                                            GroupChatBean Bean = chatList
                                                    .get(i);
                                            if (Bean != null
                                                    && Bean.getParentId() != null&& gcBean.getParentId()!=null
                                                    && Bean
                                                    .getParentId()
                                                    .equals(gcBean
                                                            .getParentId())) {
                                                listrel_quoted.setVisibility(View.VISIBLE);
                                                if(getReplyMessage(Bean)!=null) {
                                                    lvquoted_msg.setText("'' "+getReplyMessage(Bean)+" ''");
                                                }
                                                //For withdraw message
                                                //start
//                                            if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                                    && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                                if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                                    listrel_quoted.setVisibility(View.GONE);
                                                }else{
                                                    listrel_quoted.setVisibility(View.VISIBLE);
                                                }
                                            break;
                                            }
                                        }
                                        //New Code End


                                        //For Received Reply
                                        //Start
                                        waitforreply.setVisibility(View.GONE);
                                        //End
                                    }else {
                                        Log.i("reply", "button gone");
//                                    listrel_quoted.setVisibility(View.VISIBLE);
//                                    lvquoted_msg.setText(gcBean.getMessage());
                                        //old Code  waitforreply.setVisibility(View.GONE);
                                        //For Received Reply
                                        //Start
                                        if (gcBean.getWithdrawn() != null && gcBean.getWithdrawn().equalsIgnoreCase("1")) {
                                            waitforreply.setVisibility(View.GONE);
                                        } else {
                                            waitforreply.setVisibility(View.VISIBLE);
                                            waitforreply.setText("Received Reply");
                                            waitforreply.setTextColor(Color.parseColor("#00B254"));
                                        }
                                        //End
                                    }

                                } else {
                                    Log.i("reply","button Visible");
                                    listrel_quoted.setVisibility(View.GONE);
                                    waitforreply.setVisibility(View.VISIBLE);
                                    //For Received Reply
                                    //Start
                                    waitforreply.setText("Waiting for Reply");
                                    waitforreply.setTextColor(Color.parseColor("#DD2671"));
                                    //End
                                    //For withdraw message
                                    //start
//                                    if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                            && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                    if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                        waitforreply.setVisibility(View.GONE);
                                    }else{
                                        waitforreply.setVisibility(View.VISIBLE);
                                    }
                                    //End
                                }
                                deadlineReplyText.setVisibility(View.GONE);
                            } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                    "gc")) {
                                Log.i("confirm","Loginuser subCategory gc");
                                if (gcBean.getReply() != null && !gcBean.getReply().equals("") && gcBean.getReply().equals("gc_r")) {
                                    Log.i("confirm","Loginuser subCategory gc and Reply gc_r if");
                                    waitforconfir.setVisibility(View.GONE);
                                    received_confirmation.setVisibility(View.VISIBLE);
                                    //For withdraw message
                                    //start
//                                    if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                            && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                    if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                        received_confirmation.setVisibility(View.GONE);
                                    }else{
                                        received_confirmation.setVisibility(View.VISIBLE);
                                    }
                                    //End
                                } else {
                                    waitforconfir.setVisibility(View.VISIBLE);
                                    privatelay.setVisibility(View.GONE);
                                    //For withdraw message
                                    //start
//                                    if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                            && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                    if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                        waitforconfir.setVisibility(View.GONE);
                                    }else{
                                        waitforconfir.setVisibility(View.VISIBLE);
                                    }
                                    //End

                                }
                            } else {
//							tv_user.setVisibility(View.GONE);
                                scheduleMsg.setVisibility(View.GONE);
//							convertView.setBackgroundResource(R.color.gchat_bg);
                                deadlineReplyText.setVisibility(View.GONE);
                            }
                        } else {
                        /*tv user set gone*/
//						tv_user.setVisibility(View.VISIBLE);
                            tv_user.setText(CallDispatcher.LoginUser);
                            scheduleMsg.setVisibility(View.GONE);
//						convertView.setBackgroundResource(R.color.gchat_bg);
                            deadlineReplyText.setVisibility(View.GONE);
                        }
                        saveMsg.setTag(gcBean);

                    } else {
                        Log.i("reply","not loginuser");
                        swipeposition = 1;
                        chat_view = (LinearLayout) convertView
                                .findViewById(R.id.sendlintest);
                        tv_urgent = (TextView) convertView
                                .findViewById(R.id.sendtv_urgent);
                        tv_urgent.setTag(position);
                        tv_urgent.setVisibility(View.GONE);
                        Sender_img = (ImageView) convertView
                                .findViewById(R.id.userDP);
                        tv_user = (TextView) convertView
                                .findViewById(R.id.sender_user_1);
                        message = (TextView) convertView
                                .findViewById(R.id.sender_text_msg);
                        im_pin = (TextView) convertView
                                .findViewById(R.id.sendtv_pathname);
                        multimediaIcon = (ImageView) convertView
                                .findViewById(R.id.sender_multi_msg);
                        multimediaIcon.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;
                        multimediaIcon.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                        dateTime = (TextView) convertView
                                .findViewById(R.id.sender_datetime);
                        iconContainer = (RelativeLayout) convertView
                                .findViewById(R.id.sender_icon_container);
                        locationIcon = (ImageView) convertView
                                .findViewById(R.id.sender_loc_icon);
                        receiverLayout.setVisibility(View.GONE);
                        senderLayout.setVisibility(View.VISIBLE);
                        scheduleMsg = (ImageView) convertView
                                .findViewById(R.id.sender_schedule_icon);
                        scheduleMsg.setVisibility(View.GONE);
                        audioLayout = (RelativeLayout) convertView.findViewById(R.id.sendad_play);
                        progress = (ProgressBar) convertView
                                .findViewById(R.id.down_progressBar);
                        intermediateProgress = (ProgressBar) convertView
                                .findViewById(R.id.progressBar2);
                        intermediateProgress.setVisibility(View.GONE);
                        percentage = (TextView) convertView
                                .findViewById(R.id.down_percentage);
                        saveMsg = (ImageView) convertView
                                .findViewById(R.id.sender_save_btn);
                        retryIcon = (ImageView) convertView
                                .findViewById(R.id.sender_retry);
                        img_status = (ImageView) convertView
                                .findViewById(R.id.img_status);
                        audio_seekbar = (SeekBar) convertView.findViewById(R.id.sendseekBar1);
                        audio_play = (ImageView) convertView.findViewById(R.id.sendplay_button);
                        audio_tv = (TextView) convertView.findViewById(R.id.sendtxt_time);
                        audio_tv.setTag(gcBean);
                        retryIcon.setVisibility(View.GONE);

                        //For Receiver Quote
                        //Start
                        receive_quotedLayout.setVisibility(View.GONE);

                        receiver_tvquoted_msg.setVisibility(View.GONE);
                        //End



                        sender_status = (TextView) convertView
                                .findViewById(R.id.sender_status);
                        sender_status.setText("Received");
                        sender_status.setVisibility(View.GONE);
//                        if (chatList.size() == position + 1) {
//                            sender_status.setVisibility(View.VISIBLE);
//                        }
                        retryIcon.setTag(gcBean);
                        String statusicon = null;
                        if (buddyStatus != null && buddyStatus.containsKey(gcBean.getFrom())) {
                            statusicon = buddyStatus.get(gcBean.getFrom());
                            Log.i("status","getFrom-->"+gcBean.getFrom());
                            Log.i("status","status-->"+statusicon);
                        }
                        if (gcBean.getUnreadStatus() == 0) {
                            senderLayout.setBackgroundResource(R.color.greendark);
                            UdpMessageBean bean = new UdpMessageBean();
                            bean.setType("101");
                            bean.setResponseObject(gcBean);
                            SingleInstance.mainContext.ReadMessageAck(bean);
                        }
                        if (statusicon != null) {
                            if (statusicon.equalsIgnoreCase("online") || statusicon.equalsIgnoreCase("1")) {
                                img_status.setBackgroundResource(R.drawable.online_icon);
                            } else if (statusicon.equalsIgnoreCase("offline") || statusicon.equalsIgnoreCase("0")) {
                                img_status.setBackgroundResource(R.drawable.offline_icon);
                            } else if (statusicon.equalsIgnoreCase("Away") || statusicon.equalsIgnoreCase("2")) {
                                img_status.setBackgroundResource(R.drawable.busy_icon);
                            } else if (statusicon.equalsIgnoreCase("Stealth") || statusicon.equalsIgnoreCase("3")) {
                                img_status.setBackgroundResource(R.drawable.invisibleicon);
                            } else if (statusicon.equalsIgnoreCase("Airport") || statusicon.equalsIgnoreCase("4")) {
                                img_status.setBackgroundResource(R.drawable.busy_icon);
                            } else {
                                img_status.setBackgroundResource(R.drawable.offline_icon);
                            }
                        }


                        // RadioGroup deadLineReply = (RadioGroup) convertView
                        // .findViewById(R.id.deadlineOption);
                        deadlineReplyText = (TextView) convertView
                                .findViewById(R.id.sender_deadline_reply_txt);
                        ProfileBean pb = DBAccess.getdbHeler().getProfileDetails(gcBean.getFrom());
                        if (pb != null) {
                            if (pb.getPhoto() != null)
                                imageLoader.DisplayImage(
                                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" +
                                                pb.getPhoto(),
                                        Sender_img, R.drawable.icon_buddy_aoffline);
                        }

                        saveMsg.setTag(gcBean);
                        if (Buddyname(gcBean.getFrom()) != null) {
                            tv_user.setText(Buddyname(gcBean.getFrom()));
                        }
                    }
//                if(gcBean.isWithdraw())
//                {
//                    TextView listText = new TextView(MainActivity1.this);
//                }
                    if (position == mPlayingPosition) {
                        //pb.setVisibility(View.VISIBLE);
                        mProgressUpdater.mBarToUpdate = audio_seekbar;
                        mProgressUpdater.tvToUpdate = audio_tv;
                        mHandler.postDelayed(mProgressUpdater, 100);
                    } else {
                        //pb.setVisibility(View.GONE);
                        if (gcBean.getMimetype() != null && gcBean.getMimetype().equals("audio")) {
                            try {
                                audio_seekbar.setProgress(0);
                                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                                mmr.setDataSource(gcBean.getMediaName());
                                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                mmr.release();
                                String min, sec;
                                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
                                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
                                if (Integer.parseInt(min) < 10) {
                                    min = 0 + String.valueOf(min);
                                }
                                if (Integer.parseInt(sec) < 10) {
                                    sec = 0 + String.valueOf(sec);
                                }
                                audio_tv.setText(min + ":" + sec);
//                            audio_tv.setText(duration);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            audio_seekbar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                            audio_seekbar.setProgress(0);
                            if (mProgressUpdater.mBarToUpdate == audio_seekbar) {
                                //this progress would be updated, but this is the wrong position
                                mProgressUpdater.mBarToUpdate = null;
                            }
                        }
                    }

                    if (gcBean.isPlaying()) {
                        audio_play.setBackgroundResource(R.drawable.audiopause);
                    } else {
                        audio_play.setBackgroundResource(R.drawable.play);
                    }

                    audio_play.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!CallDispatcher.isCallInitiate) {
                                File newfile=new File(gcBean.getMediaName());
                            if (finalPlayBean == null) {
                                audio_play.setBackgroundResource(R.drawable.audiopause);
                                if(newfile.exists()) {
                                    playAudio(gcBean.getMediaName(), position);
                                    gcBean.setPlaying(true);
                                    finalPlayBean = gcBean;
                                }else
                                    showToast("No audio to play");
                            } else if (finalPlayBean == gcBean) {
                                if (mPlayer.isPlaying()) {
                                    mPlayer.pause();
                                    audio_play.setBackgroundResource(R.drawable.play);
                                    gcBean.setPlaying(false);
                                } else {
                                    gcBean.setPlaying(true);
                                    audio_play.setBackgroundResource(R.drawable.audiopause);
                                    mPlayer.start();

                                }
                            } else {
                                finalPlayBean.setPlaying(false);
                                finalPlayBean = gcBean;
                                finalPlayBean.setPlaying(true);
                                audio_play.setBackgroundResource(R.drawable.audiopause);
                                if(newfile.exists())
                                playAudio(gcBean.getMediaName(), position);
                                else
                                    showToast("No audio to play");

                            }
                            } else {
                                showToast("Please Try again...call  in progress");
                            }

                        }

                    });
                    message.setText(gcBean.getMessage());
                    if (gcBean.getMimetype() != null && gcBean.getMimetype().equals("text")
                            || gcBean.getMimetype().equals("location") || gcBean.getMimetype().equals("link")) {
                        message.setVisibility(View.VISIBLE);
                        multimediaIcon.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        percentage.setVisibility(View.GONE);
                        iconContainer.setVisibility(View.GONE);
                        audioLayout.setVisibility(View.GONE);
                        if (gcBean.getSubCategory() != null
                                && gcBean.getSubCategory().equalsIgnoreCase("GD")) {
                            Log.i("msg123",
                                    "subcategory : " + gcBean.getSubCategory());
                            if (gcBean.getMessage() != null) {
                                Log.i("msg123", "message : " + gcBean.getMessage());
                                String msg = gcBean.getMessage();
                                if (gcBean.getMessage().contains("Status : ")) {
                                    Log.i("msg123", "message contains status : "
                                            + gcBean.getMessage());
                                    String originalMsg = msg.substring(0,
                                            msg.indexOf("Status"));
                                    Log.i("ABCD", "Original Message" + gcBean.getMessage());
                                    message.setText(originalMsg);
                                } else {
                                    Log.i("ABCD", "else Original Message" + gcBean.getMessage());

                                    message.setText(gcBean.getMessage());
                                }
                            } else {
                                Log.i("msg123",
                                        "else received msg1 : "
                                                + gcBean.getMessage());
                                message.setText(gcBean.getMessage());
                                Log.i("ABCD", "final else Original Message" + gcBean.getMessage());

                                message.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.i("msg123",
                                    "else received msg2 : " + gcBean.getMessage());
                            message.setText(gcBean.getMessage());
                            message.setVisibility(View.VISIBLE);
                        }

                        Log.d("GROUP_CHAT", "message :" + gcBean.getMessage());
                        if (gcBean.getMimetype().equals("location")) {
                            locationIcon.setVisibility(View.VISIBLE);
                            locationIcon.setTag(gcBean.getMessage());
                        } else {
                            locationIcon.setVisibility(View.GONE);
                        }

                    } else if (gcBean.getMimetype().equals("audio")) {
//                    message.setVisibility(View.GONE);
                        iconContainer.setVisibility(View.GONE);
                        locationIcon.setVisibility(View.GONE);
                        audioLayout.setVisibility(View.VISIBLE);
//                        if (!gcBean.getFrom().equals(CallDispatcher.LoginUser)) {
//                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                                    ViewGroup.LayoutParams.WRAP_CONTENT
//                            );
//                            params.setMargins(10, 250, 0, 4);
//                            splmsgview.setLayoutParams(params);
//                        }
                        audio_play.setTag(gcBean);
                    } else {
                        if (retryIcon != null) {
                            if (gcBean.getStatus() == 1) {
                                // retryIcon.setVisibility(View.VISIBLE);
                                // downloadorUploadFile(gcBean);
                                retryIcon.setVisibility(View.GONE);
                                if (intermediateProgress != null)
                                    intermediateProgress
                                            .setVisibility(View.VISIBLE);
                                showIntermediateProgress(intermediateProgress);
                            } else {
                                if (intermediateProgress != null) {
                                    intermediateProgress.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }
                                retryIcon.setVisibility(View.GONE);
                            }
                        }
//					message.setVisibility(View.GONE);
                        audioLayout.setVisibility(View.GONE);
                        iconContainer.setVisibility(View.VISIBLE);
                        locationIcon.setVisibility(View.GONE);
                        if (gcBean.getMediaName() != null) {
                            File file = new File(gcBean.getMediaName());
                            if (!file.exists()) {
                                multimediaIcon.setImageResource(R.drawable.refresh);
                                multimediaIcon.setVisibility(View.VISIBLE);
                                if (retryIcon != null) {
                                    retryIcon.setVisibility(View.GONE);
                                    // retryIcon.setVisibility(View.VISIBLE);
                                    // downloadorUploadFile((GroupChatBean)
                                    // retryIcon
                                    // .getTag());
                                }

                            } else {
                                if (retryIcon != null && gcBean.getStatus() == 0)
                                    retryIcon.setVisibility(View.GONE);
                                if (intermediateProgress != null
                                        && gcBean.getStatus() == 2) {
                                    intermediateProgress.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }
                                multimediaIcon.setVisibility(View.VISIBLE);
                            }
                        }
                        if (gcBean.getProgress() > 0 && gcBean.getProgress() < 100) {
                            // pre_progress.setVisibility(View.GONE);
                            progress.setVisibility(View.VISIBLE);
                            percentage.setVisibility(View.VISIBLE);
                            progress.setProgress(gcBean.getProgress());
                            percentage.setText(String.valueOf(gcBean.getProgress())
                                    + "%");
                            if (intermediateProgress != null)
                                intermediateProgress.setVisibility(View.GONE);
                            retryIcon.setVisibility(View.GONE);

                        } else {
                            progress.setProgress(0);
                            percentage.setText("");
                            percentage.setVisibility(View.GONE);
                            progress.setVisibility(View.GONE);
                            if (intermediateProgress != null
                                    && gcBean.getStatus() == 2) {
                                intermediateProgress.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        if (gcBean.getMediaName() != null) {
                            File file = new File(gcBean.getMediaName());
                            if (file.exists()) {


                                if (gcBean.getMimetype().equalsIgnoreCase("image")) {

                                    // start 07-10-15 changes

                                    im_pin.setText(gcBean.getMediaName().split("COMMedia/")[1]);
                                    imageViewer.display(gcBean.getMediaName(),
                                            multimediaIcon, R.drawable.refresh);
                                    if (gcBean.getMessage() != null)
                                        message.setVisibility(View.VISIBLE);

                                    // ended 07-10-15 changes
                                    multimediaIcon.setContentDescription(gcBean
                                            .getMimetype());

                                } else if (gcBean.getMimetype().equalsIgnoreCase("sketch")) {

                                    // start 07-10-15 changes

                                    im_pin.setText(gcBean.getMediaName().split("COMMedia/")[1]);
                                    imageViewer.display(gcBean.getMediaName(),
                                            multimediaIcon, R.drawable.refresh);
                                    if (gcBean.getMessage() != null)
                                        message.setVisibility(View.VISIBLE);

                                    // ended 07-10-15 changes
                                    multimediaIcon.setContentDescription(gcBean
                                            .getMimetype());

                                } else if (!gcBean.getMimetype().equals("audio")) {
                                    multimediaIcon.setVisibility(View.VISIBLE);
                                    im_pin.setVisibility(View.VISIBLE);
                                    im_pin.setText(gcBean.getMediaName().split("COMMedia/")[1]);
                                    Log.i("entering", "mediname" + gcBean.getMediaName());
                                    Log.i("entering", "mediname1" + gcBean.getMediaName().split("COMMedia/")[1]);
                                    multimediaIcon.setPadding(2, 2, 2, 2);
                                    if (gcBean.getMimetype().equalsIgnoreCase(
                                            "video")) {
                                        Log.i("entering", "videotype" + gcBean.getMimetype());
                                        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(gcBean.getMediaName(), MediaStore.Video.Thumbnails.MICRO_KIND);
                                        Log.i("videoplay", "bitmap-->" + bMap);
                                        if(bMap==null){
                                            multimediaIcon.getLayoutParams().height = 50;
                                            multimediaIcon.getLayoutParams().width = 50;
                                            videoPlay.setVisibility(View.GONE);
                                            receiver_videoPlay.setVisibility(View.GONE);
                                            multimediaIcon
                                                    .setImageResource(R.drawable.videoview1);
                                            multimediaIcon.setContentDescription(gcBean
                                                    .getMimetype()+"bmapnull");

                                        }else{
                                            multimediaIcon.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;
                                            multimediaIcon.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                                            multimediaIcon.setImageBitmap(bMap);
                                            videoPlay.setVisibility(View.VISIBLE);
                                            receiver_videoPlay.setVisibility(View.VISIBLE);
                                            multimediaIcon.setContentDescription(gcBean
                                                    .getMimetype());

                                        }
//                                        multimediaIcon
//                                                .setImageResource(R.drawable.videoview1);
                                    }
                                    else {
                                        multimediaIcon.setVisibility(View.GONE);
//                                 multimediaIcon
//                                         .setImageResource(R.drawable.doc_chat);
                                        if (gcBean.getMessage() != null)
                                            message.setVisibility(View.VISIBLE);
                                        multimediaIcon.setContentDescription(gcBean
                                                .getMimetype());
                                    }
                                }
                            }
                            multimediaIcon.setTag(gcBean.getMediaName());
//                            multimediaIcon.setContentDescription(gcBean
//                                    .getMimetype());

                        } else {
                            // notify_icon.setVisibility(View.VISIBLE);

                        }
                    }
                    if (gcBean.getSubCategory() != null) {
                        if (gcBean.getSubCategory().equalsIgnoreCase("gp")) {
                            // deadLineReply.setVisibility(View.GONE);
                            if(gcBean.getReply()!=null && (gcBean.getReply().equalsIgnoreCase("gp_r") || gcBean.getReply().equalsIgnoreCase("gp"))){
                                btn_private.setVisibility(View.GONE);
                                tv_replied.setVisibility(View.VISIBLE);
                                tv_replied.setText("Replied " + Html.fromHtml(tick));
                            }else {
                                tv_user.setText(Buddyname(gcBean.getFrom()));
                                if (gcBean.getReply() != null && gcBean.getReply().equals("private")) {
                                    btn_private.setVisibility(View.GONE);
                                    tv_replied.setText("Private Reply to :" + gcBean.getFrom());
                                } else {
                                    btn_private.setVisibility(View.VISIBLE);
                                }
                                convertView.setBackgroundResource(R.color.gchat_bg);
                                deadlineReplyText.setVisibility(View.GONE);
                                btn_private.setTag(gcBean);
                            }
                        }else if(gcBean.getSubCategory()!=null && gcBean.getSubCategory().equalsIgnoreCase("gp_r")){
//
                        }else if (gcBean.getSubCategory().equalsIgnoreCase(
                                "gs")) {
                            // deadLineReply.setVisibility(View.GONE);
                            deadlineReplyText.setVisibility(View.GONE);
//                                convertView.setBackgroundResource(R.color.gchat_bg);
//                                senderLayout
//                                        .setBackgroundResource(R.color.gchat_bg);
                            if (SingleInstance.scheduledMsg.containsKey(gcBean
                                    .getSignalid())) {
                                senderLayout.setVisibility(View.GONE);
                            } else {
                                senderLayout.setVisibility(View.VISIBLE);
                                scheduleMsg.setVisibility(View.VISIBLE);
                                scheduleMsg
                                        .setImageResource(R.drawable.icon_footer_reminder);
                                scheduleMsg.setTag(gcBean);
                                tv_user.setText(SingleInstance.mainContext.getResources().getString(R.string.message_schedule_by)
                                        + gcBean.getFrom());
                            }
                        } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                "gd")) {
//                                senderLayout
//                                        .setBackgroundResource(R.color.deadlingclr);
//                                convertView
//                                        .setBackgroundResource(R.color.deadlingclr);
                            scheduleMsg.setVisibility(View.VISIBLE);
                            scheduleMsg
                                    .setImageResource(R.drawable.icon_footer_reminder);
                            scheduleMsg.setTag(gcBean);
                            // tv_user.setText("DeadLine set by : "
                            // + gcBean.getFrom() + "\nAssigned to : "
                            // + gcBean.getPrivateMembers() + "\nTime : "
                            // + gcBean.getReminderTime() + "\n");
                            tv_user.setText(SingleInstance.mainContext.getResources().getString(R.string.dead_line_assigned_to)
                                    + gcBean.getPrivateMembers() + "\nAt : "
                                    + gcBean.getReminderTime() + "\n");
                            if (gcBean.getMessage() != null) {
                                String msg = gcBean.getMessage();
                                Log.i("msg123", "message1 contains status : "
                                        + gcBean.getMessage());
                                if (gcBean.getMessage().contains("Status : ")) {
                                    Log.i("msg123",
                                            "message2 contains status : "
                                                    + gcBean.getMessage());
                                    String tempMsg = msg.substring(0,
                                            msg.indexOf("Status"));
                                    String originalMsg = msg.replace(tempMsg,
                                            "");
                                    Log.i("msg123", "temp msg : " + tempMsg
                                            + "original msg : " + originalMsg);
                                    deadlineReplyText.setText(originalMsg);
                                } else {
                                    deadlineReplyText.setText("");
                                }
                            } else {
                                deadlineReplyText.setText("");
                            }
                            deadlineReplyText.setVisibility(View.VISIBLE);

                            if (gcBean.getPrivateMembers() != null
                                    && gcBean.getPrivateMembers().length() > 0) {
                                String[] privateMembers = gcBean
                                        .getPrivateMembers().split(",");
                                for (String tmp : privateMembers) {
                                    if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
                                        // deadLineReply
                                        // .setVisibility(View.VISIBLE);
                                        // deadLineReply(deadLineReply, gcBean);
                                        break;
                                    } else {
                                        // deadLineReply.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                "gc") || gcBean.getSubCategory().equalsIgnoreCase(
                                "gc_r")) {
                            tv_user.setText(Buddyname(gcBean.getFrom()));
                            if (gcBean.getReply() != null && gcBean.getReply().equals("gc_r")) {
                                btn_confrm.setVisibility(View.GONE);
                                tv_replied.setVisibility(View.VISIBLE);
                                tv_replied.setText("Confirmed" + Html.fromHtml(tick));
                                //For withdraw message
                                //start
//                                    if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                            && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                    tv_replied.setVisibility(View.GONE);
                                }else{
                                    tv_replied.setVisibility(View.VISIBLE);
                                }
                                //End

                            } else {
                                btn_confrm.setVisibility(View.VISIBLE);
                                //For withdraw message
                                //start
//                                    if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                            && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                    btn_confrm.setVisibility(View.GONE);
                                }else{
                                    btn_confrm.setVisibility(View.VISIBLE);
                                }
                                //End
                            }
                        } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                "gu")) {
                            tv_urgent.setVisibility(View.VISIBLE);
                            tv_urgent.setTextColor(Color.parseColor("#daa520"));
                            tv_urgent.setText("  Urgent  !  ");
                            //For withdraw message
                            //start
//                                if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                        && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                            if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                tv_urgent.setVisibility(View.GONE);
                            }else{
                                tv_urgent.setVisibility(View.VISIBLE);
                            }
                            //End
                        } else if (gcBean.getSubCategory().equalsIgnoreCase(
                                "grb") || gcBean.getSubCategory().equalsIgnoreCase(
                                "GRB_R")) {
                            // deadLineReply.setVisibility(View.GONE);
                            Log.i("reply","receiver side grb|| GRB_R");
                            deadlineReplyText.setVisibility(View.GONE);
//                                if (gcBean.getPrivateMembers() != null
//                                        && gcBean.getPrivateMembers().length() > 0) {
//                                    String[] privateMembers = gcBean
//                                            .getPrivateMembers().split(",");
//                                    for (String tmp : privateMembers) {
//                                        if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
//										tv_user.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_message_from)
//												+ gcBean.getFrom()
//										 + "\nTo : "
//										 + gcBean.getPrivateMembers());
                            Log.i("reply","receiver side grb|| GRB_R loginuser");
                            tv_user.setText(Buddyname(gcBean.getFrom()));
//                                            senderLayout
//                                                    .setBackgroundResource(R.color.greenlight);
//                                            convertView
//                                                    .setBackgroundResource(R.color.lgreen);
                            scheduleMsg.setTag(gcBean);
                            btn_reply.setTag(gcBean);
                            scheduleMsg
                                    .setImageResource(R.drawable.replybg);
                            scheduleMsg.setVisibility(View.GONE);
                            if (gcBean.getReply() != null && !gcBean.getReply().equals("") && gcBean.getReply().equalsIgnoreCase("GRB_R")) {
                                Log.i("reply","receiver side GRB_R loginuser");
                                btn_reply.setVisibility(View.GONE);

                                //For Receiver Quote
                                //Start
//                                                if(gcBean.getMimetype()!=null && gcBean.getMimetype().equalsIgnoreCase("text") &&
//                                                        gcBean.getMessage()!=null){
                                Log.i("reply","receiver side GRB_R loginuser quote visible");
                                //Old Code start
//                                                    if(position>0){
//                                                    GroupChatBean groupChatBean=chatList.get(position-1);
//                                                    receive_quotedLayout.setVisibility(View.VISIBLE);
//                                                    receiver_tvquoted_msg.setVisibility(View.VISIBLE);
//                                                    if(getReplyMessage(groupChatBean)!=null) {
//                                                        receiver_tvquoted_msg.setText(getReplyMessage(groupChatBean));
//                                                    }
//                                                        //For withdraw message
//                                                        //start
////                                                        if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
////                                                                && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
//                                                        if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
//                                                            receive_quotedLayout.setVisibility(View.GONE);
//                                                        }else{
//                                                            receive_quotedLayout.setVisibility(View.VISIBLE);
//                                                        }
//                                                        //End
//                                                    }
                                //Old Code End


                                //New Code Start
                                for (int i = 0; i < chatList.size(); i++) {
                                    GroupChatBean groupChatBean = chatList
                                            .get(i);
                                    if (groupChatBean != null
                                            && groupChatBean.getParentId() != null&& gcBean.getParentId()!=null
                                            && groupChatBean
                                            .getParentId()
                                            .equals(gcBean
                                                    .getParentId())) {
                                        receive_quotedLayout.setVisibility(View.VISIBLE);
                                        receiver_tvquoted_msg.setVisibility(View.VISIBLE);
                                        if(getReplyMessage(groupChatBean)!=null) {
                                            receiver_tvquoted_msg.setText("'' "+getReplyMessage(groupChatBean)+" ''");
                                        }
                                        //For withdraw message
                                        //start
//                                                        if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                                                && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                        if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                            receive_quotedLayout.setVisibility(View.GONE);
                                        }else{
                                            receive_quotedLayout.setVisibility(View.VISIBLE);
                                        }
                                        //End
                                        break;
                                    }
                                }
                                //New Code End
//                                                }
                                //End
                            } else {
                                Log.i("reply","receiver side GRB_R loginuser else btn_reply visble");
                                btn_reply.setVisibility(View.VISIBLE);
                                //For withdraw message
                                //start
//                                                if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                                        && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                    btn_reply.setVisibility(View.GONE);
                                }else{
                                    btn_reply.setVisibility(View.VISIBLE);
                                }
                                //End
                            }
                            if (gcBean.getReplied() != null && gcBean.getReplied().equals("reply")) {
                                tv_replied.setVisibility(View.VISIBLE);
                                tv_replied.setText("Replied " + Html.fromHtml(tick));
                                //For Receiver Quote
                                //Start
                                receive_quotedLayout.setVisibility(View.GONE);
                                receiver_tvquoted_msg.setVisibility(View.GONE);
                                //End
                                //For withdraw message
                                //start
//                                                if(gcBean.getMimetype()!=null && gcBean.getMessage()!=null && gcBean.getMimetype().equalsIgnoreCase("text")
//                                                        && gcBean.getMessage().equalsIgnoreCase("Message withdrawn")){
                                if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1")){
                                    tv_replied.setVisibility(View.GONE);
                                }else{
                                    tv_replied.setVisibility(View.VISIBLE);
                                }
                                //End
                            }
//                                            break;
//                                        } else {
//                                            Log.i("reply","receiver side grb|| GRB_R not loginuser");
//                                            senderLayout
//                                                    .setBackgroundResource(R.color.lgreen);
//                                            scheduleMsg.setVisibility(View.GONE);
//                                            convertView
//                                                    .setBackgroundResource(R.color.lgreen);
//                                        }
//                                    }
//                                }

                        } else {
                            Log.i("reply","receiver side grb|| GRB_R else ");
                            deadlineReplyText.setVisibility(View.GONE);
                            tv_user.setText(Buddyname(gcBean.getFrom()));
                            scheduleMsg.setVisibility(View.GONE);
                        }
                    } else {
                        deadlineReplyText.setVisibility(View.GONE);
                        tv_user.setText(Buddyname(gcBean.getFrom()));
                        scheduleMsg.setVisibility(View.GONE);
                    }
                    im_pin.setTag(gcBean.getMediaName());
                    im_pin.setContentDescription(gcBean.getMimetype());
                    im_pin.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            saveFile(v.getContentDescription().toString(), v.getTag().toString());
                            return true;
                        }
                    });

                    im_pin.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (gcBean.getMimetype().equals("document")) {
                                FilesFragment.openFilesinExternalApp(gcBean.getMediaName());
                            }
                        }
                    });
                    String dat, tim;
                    dat = gcBean.getSenttime().split(" ")[1].split(":")[0] + ":" + gcBean.getSenttime().split(" ")[1].split(":")[1];
                    tim = gcBean.getSenttime().split(" ")[2].toUpperCase();

                    dateTime.setText(dat + " " + tim);
//				dateTime.setText(getCurrentDateandTime());

                    locationIcon.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (v.getTag().toString() != null) {
                                showprogress();
                                Intent intent = new Intent(context,
                                        buddyLocation.class);
                                String locs[] = ((CallDispatcher) WebServiceReferences.callDispatch
                                        .get("calldisp")).getBuddyLocation(v
                                        .getTag().toString());
                                intent.putExtra("latitude", locs[0]);
                                intent.putExtra("longitude", locs[1]);
                                startActivity(intent);
                            }
                        }
                    });

                    multimediaIcon.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                                saveFile(v.getContentDescription().toString(),v.getTag().toString());
                            return true;
                        }
                    });
                    multimediaIcon.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (v != null && v.getContentDescription()!=null) {
                                if (v.getContentDescription().toString().equalsIgnoreCase("audio") ||
                                        v.getContentDescription().toString().equalsIgnoreCase("video")) {
                                    if (!CallDispatcher.isCallInitiate)
                                        playMultimedia(v);
                                    else
                                        showToast("Please Try again...call  in progress");
                                } else
                                    playMultimedia(v);
                            }else{
                                showToast("File does not Exist");
                            }
                        }
                    });

                    btn_confrm.setTag(gcBean);
                    btn_confrm.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isconfirmBack = true;
                            GroupChatBean gcBean1 = (GroupChatBean) view
                                    .getTag();
                            privateMembers = gcBean1.getFrom();
                            pId = gcBean1.getParentId();
                            sendMsg("",
                                    null, "text", null);

                        }
                    });
                    btn_private.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("privatemsg","btn_private click");
                            isPrivateBack=true;
                            LL_privateReply.setVisibility(View.VISIBLE);
                            PrivateReply_view=view;
                            GroupChatBean gcBean1 = (GroupChatBean) view
                                    .getTag();
                            if(tv_privateReplyUsername!=null){
                                if(gcBean1.getFrom()!=null && Buddyname(gcBean1.getFrom())!=null) {
                                    tv_privateReplyUsername.setText("Private Reply : " +Buddyname(gcBean1.getFrom()));
                                }
                            }
//                            gcBean1.setReply("private");
//                            sendSpecialMessage("gp", gcBean1.getFrom());
//                            int row = DBAccess.getdbHeler(
//                                    SipNotificationListener.getCurrentContext())
//                                    .updateChatReply(gcBean1);


                        }
                    });
//scheduleMsg.
                    btn_reply.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            try {
                                // TODO Auto-generated method stub
                                GroupChatBean gcBean1 = (GroupChatBean) view
                                        .getTag();
                                if(getReplyMessage(gcBean1)!=null) {
                                    tvquoted_msg.setText("'' "+getReplyMessage(gcBean1)+" ''");
                                }
                                rel_quoted.setVisibility(View.VISIBLE);
                                if (gcBean1.getSubCategory() != null) {
                                    if (gcBean1.getSubCategory().equalsIgnoreCase(
                                            "GD")) {
                                        if (!gcBean1.getFrom().equalsIgnoreCase(
                                                CallDispatcher.LoginUser)) {
                                            deadLineMsgDialog(gcBean1);
                                        } else {
                                            deadLineMsgDialog(gcBean1);
                                        }
                                    } else if (gcBean1.getSubCategory()
                                            .equalsIgnoreCase("GRB")) {
                                        if (CallDispatcher.LoginUser != null) {
                                            Log.i("AAAA","reply back true");
                                            isReplyBack = true;
                                            // showReplyBackDialog(gcBean1);
//										showToast(SingleInstance.mainContext.getResources().getString(R.string.pressed));
                                            if (!gcBean1
                                                    .getFrom()
                                                    .equalsIgnoreCase(
                                                            CallDispatcher.LoginUser)) {
                                                privateMembers = gcBean1.getFrom();
                                            } else {
                                                privateMembers = gcBean1
                                                        .getPrivateMembers();
                                            }
                                            if (gcBean1.getParentId() != null
                                                    && !gcBean1.getParentId()
                                                    .equalsIgnoreCase(
                                                            "null")) {
                                                Log.i("AAAA","reply back true parentid");
                                                pId = gcBean1.getParentId();
                                            } else {
                                                Log.i("AAAA","reply back true signalid");
                                                pId = gcBean1.getSignalid();
                                            }
                                        }
                                    } else if (gcBean1.getSubCategory()
                                            .equalsIgnoreCase("gs")) {
                                        if (!gcBean1.getFrom().equalsIgnoreCase(
                                                CallDispatcher.LoginUser)) {
                                            scheduleMessageDialog(gcBean1);
                                        } else {
                                            scheduleMessageDialog(gcBean1);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                if (AppReference.isWriteInFile)
                                    AppReference.logger.error(e.getMessage(), e);
                                else
                                    e.printStackTrace();
                            }
                        }
                    });

                    saveMsg.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            GroupChatBean gcBean = (GroupChatBean) v.getTag();
                            String ComponentPath = "";
                            String title = "";
                            int position = 0;
                            if (gcBean.getMimetype().equalsIgnoreCase("text")
                                    || gcBean.getMimetype().equalsIgnoreCase(
                                    "location") || gcBean.getMimetype().equals("link")) {
                                ComponentPath = createTextNote(gcBean.getMessage());
                                if (gcBean.getMessage().length() >= 12) {
                                    title = gcBean.getMessage().trim()
                                            .substring(0, 11);

                                } else if (gcBean.getMessage().length() < 12) {
                                    title = gcBean
                                            .getMessage()
                                            .trim()
                                            .substring(0,
                                                    gcBean.getMessage().length());
                                }
                                position = 1;
                            } else if (gcBean.getMimetype().equalsIgnoreCase(
                                    "image")) {
                                title = "Photo";
                                ComponentPath = gcBean.getMediaName();
                                position = 2;
                            } else if (gcBean.getMimetype().equalsIgnoreCase(
                                    "Audio")) {
                                title = "Audio";
                                ComponentPath = gcBean.getMediaName();
                                position = 4;
                            } else if (gcBean.getMimetype().equalsIgnoreCase(
                                    "Video")) {
                                title = "Video";
                                ComponentPath = gcBean.getMediaName();
                                position = 5;
                            }
                            DBAccess.getdbHeler().putDBEntry(
                                    position,
                                    ComponentPath,
                                    WebServiceReferences
                                            .getNoteCreateTimeForFiles(), title,
                                    null, "");
                            FilesFragment.newInstance(context).filesListRefresh();
                            showToast("Saved successfully");

                        }

                    });

                    retryIcon.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // TODO Auto-generated method stub
                            GroupChatBean gcBean = (GroupChatBean) view.getTag();
                            Log.i("gchat123", "inside retry " + gcBean.getGroupId());
                            // downloadorUploadFile(gcBean);

                        }
                    });

                    withdraw.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final GroupChatBean gBean = gcBean
                                    .clone();
                            gBean.setpSingnalId(gcBean
                                    .getSignalid());
                            gBean.setSignalid(Utility
                                    .getSessionID());
                            Log.i("group123",
                                    "signalid :: "
                                            + gcBean.getpSingnalId());
                            gBean.setType("104");
                            if (isGroup || isRounding)
                                gBean.setTo(groupBean
                                        .getGroupId());
                            else
                                gBean.setTo(buddy);
                            GroupChatPermissionBean gcpBean = SingleInstance.mainContext
                                    .getGroupChatPermission(groupBean);
                            if (isGroup || isRounding) {
                                if (gcpBean
                                        .getWithDrawn()
                                        .equalsIgnoreCase(
                                                "1")) {
                                    SingleInstance
                                            .getGroupChatProcesser()
                                            .getQueue()
                                            .addObject(
                                                    gBean);
//                                    chatList.remove(gcBean);
                                    gcBean.setWithdrawn("1");
//                                    gcBean.setMimetype("text");
                                    gcBean.setSenderWithdraw("Message withdrawn @"+getCurrentTime());
                                    gcBean.setWithdraw(false);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    showToast("Sorry you dont have permission");
                                }
                            } else {
                                SingleInstance
                                        .getGroupChatProcesser()
                                        .getQueue()
                                        .addObject(
                                                gBean);
//                            chatList.remove(gcBean);
                                gcBean.setWithdrawn("1");
//                                gcBean.setMimetype("text");
                                gcBean.setSenderWithdraw("Message withdrawn @"+getCurrentTime());
                                gcBean.setWithdraw(false);

                                adapter.notifyDataSetChanged();
                            }
                            for (BuddyInformationBean bBean : ContactsFragment
                                    .getBuddyList()) {
                                if (!bBean.isTitle()) {
                                    if (bBean.getName().equalsIgnoreCase(buddy)) {
                                        bBean.setLastMessage(null);
                                        ContactsFragment.getContactAdapter()
                                                .notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }


                        }
                    });
                    xbutton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                        GroupChatBean gcBean1=adapter.getItem(position);
                            gcBean.setWithdraw(false);
                            adapter.notifyDataSetChanged();

                        }
                    });


                    if (gcBean.isWithdraw()) {
                        withdraw.setVisibility(View.VISIBLE);
                        xbutton.setVisibility(View.VISIBLE);
                        mainlayout.setBackgroundColor(Color.parseColor("#3D2831"));
                        withdraw.setTextColor(Color.parseColor("#99004c"));
                        xbutton.setTextColor(Color.parseColor("#99004c"));
                    } else {
                        if (gcBean.getWithdrawn() != null && gcBean.getWithdrawn().equals("1")) {
//                            message.setText("Message withdrawn");
                        }
                        withdraw.setVisibility(View.GONE);
                        xbutton.setVisibility(View.GONE);
                        mainlayout.setBackgroundColor(Color.parseColor("#3C3C3C"));
                    }
//                Vector<BuddyInformationBean> getBuddyList=ContactsFragment.getBuddyList();
                    if (gcBean.getMimetype().equalsIgnoreCase("mixedfile")) {
                        String[] fname = gcBean.getMediaName().split(",");
                        chat_view.removeAllViews();
                        View msg = inflater.inflate(R.layout.listaudiolayout, parent, false);
//                    TextView tvview=new (TextView)msg.findViewById(R.id.sendtxt_time);
                        message.setVisibility(View.GONE);
                        TextView tv_prog = new TextView(GroupChatActivity.this);
                        tv_prog.setText(gcBean.getMessage());
                        chat_view.addView(tv_prog);
                        for (int i = 0; i < fname.length; i++) {
                            View holder;
                            audioLayout.setVisibility(View.GONE);

                            iconContainer.setVisibility(View.GONE);
                            if (fname[i].split("COMMedia/")[1].endsWith("mp4")) {
                                holder = inflater.inflate(R.layout.videochatlist, parent, false);
                                ImageView receiver_multi_msg = (ImageView) holder.findViewById(R.id.receiver_multi_msg);
                                ImageView video_play=(ImageView)holder.findViewById(R.id.videoPlay);
                                video_play.setVisibility(View.VISIBLE);
                                TextView tv_pathname = (TextView) holder.findViewById(R.id.tv_pathname);
                                tv_pathname.setText(fname[i].split("COMMedia/")[1]);
//                                receiver_multi_msg
//                                        .setImageResource(R.drawable.videoview1);
                                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(fname[i], MediaStore.Video.Thumbnails.MICRO_KIND);
//                                receiver_multi_msg.setImageBitmap(bMap);
                                if(bMap==null){
                                    receiver_multi_msg.getLayoutParams().height = 50;
                                    receiver_multi_msg.getLayoutParams().width = 50;
                                    receiver_multi_msg
                                            .setImageResource(R.drawable.videoview1);
                                    receiver_multi_msg.setContentDescription("videobmapnull");

                                }else{
                                    receiver_multi_msg.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;
                                    receiver_multi_msg.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                                    receiver_multi_msg.setImageBitmap(bMap);
                                    receiver_multi_msg.setContentDescription("video");
                                }

                                final String path = fname[i];
                                receiver_multi_msg.setTag(path);
//                                receiver_multi_msg.setContentDescription(gcBean.getMimetype());
                                receiver_multi_msg.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        saveFile(v.getContentDescription().toString(),v.getTag().toString());
                                        return true;
                                    }
                                });
                                receiver_multi_msg.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(!CallDispatcher.isCallInitiate) {
                                            if (mPlayer != null && mPlayer.isPlaying())
                                                mPlayer.stop();
                                            Log.i("videoplay", "mixed file call not process");
                                            Log.i("videoplay", "mixed file call not process content-->"+view.getContentDescription().toString());
                                            if(view.getContentDescription().toString().equalsIgnoreCase("video")) {
                                                Log.i("videoplay","mixed file call not process video");
                                                Intent intent = new Intent(context, VideoPlayer.class);
                                                intent.putExtra("video", path);
                                                startActivity(intent);
                                            }
                                            else if(view.getContentDescription().toString().equalsIgnoreCase("videobmapnull")){
                                                Log.i("videoplay","mixed file call not process videobmapnull");
                                                AppReference.fileOpen=true;
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                                                intent.setDataAndType(Uri.parse(path), "video/mp4");
                                                try {
                                                    startActivity(intent);
                                                }catch (ActivityNotFoundException e){
                                                    Toast.makeText(context,
                                                            "No Application Available to Play this file",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                    } else {
                                        showToast("Please Try again...call  in progress");
                                    }

                                }
                                });
                            } else if (fname[i].split("COMMedia/")[1].endsWith("mp3")
                                    || fname[i].split("COMMedia/")[1].endsWith("amr")) {
                                holder = inflater.inflate(R.layout.listaudiolayout, parent, false);
                                multiaudio_seekbar = (SeekBar) holder.findViewById(R.id.sendseekBar1);
                                TextView tvview = (TextView) holder.findViewById(R.id.sendtxt_time);
                                final ImageView multiplay_button = (ImageView) holder.findViewById(R.id.sendplay_button);
                                View v = chat_view.getChildAt(i);
                                multiplay_button.setTag(i);
                                tvview.setTag(i);
                                multiaudio_seekbar.setTag(i);
                                final String path = fname[i];
                                final int pos = i;
                                Log.d("viwVlist", "count " + chat_view.getChildCount());


                                if(finalPlayFile != null && finalPlayFile.equals(path) && gcBean.isPlaying())
                                {
                                    multiplay_button.setBackgroundResource(R.drawable.audiopause);
                                }else
                                {
                                    multiplay_button.setBackgroundResource(R.drawable.play);
                                }
                                multiplay_button.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        File newfile=new File(path);
                                        if (finalPlayBean == null) {
                                            multiplay_button.setBackgroundResource(R.drawable.audiopause);
                                            if(newfile.exists()) {
                                                playAudio(path, position);
                                                gcBean.setPlaying(true);
                                                finalPlayBean = gcBean;
                                                finalPlayFile = path;
                                            }else
                                                showToast("No audio to play");
                                        } else if (finalPlayBean == gcBean && finalPlayFile.equals(path)) {
                                            if (mPlayer.isPlaying()) {
                                                mPlayer.pause();
                                                multiplay_button.setBackgroundResource(R.drawable.play);
                                                mHandler.postDelayed(mProgressUpdater, 100);
                                                gcBean.setPlaying(false);
                                            } else {
                                                gcBean.setPlaying(true);
                                                multiplay_button.setBackgroundResource(R.drawable.audiopause);
                                                mPlayer.start();
                                            }
                                        } else {
                                            finalPlayBean.setPlaying(false);
                                            finalPlayBean = gcBean;
                                            finalPlayBean.setPlaying(true);
                                            finalPlayFile = path;
                                            multiplay_button.setBackgroundResource(R.drawable.audiopause);
                                            if(newfile.exists())
                                                playAudio(path, position);
                                            else
                                                showToast("No audio to play");

                                        }
                                        click = pos;
                                    }
                                });
                                if (click == i) {
                                    if (position == mPlayingPosition) {
                                        int wantedPosition = position; // Whatever position you're looking for
                                        int firstPosition = lv.getFirstVisiblePosition() - lv.getHeaderViewsCount(); // This is the same as child #0
                                        int wantedChild = wantedPosition - firstPosition;
                                        if (wantedChild < 0 || wantedChild >= lv.getChildCount()) {
                                            Log.w("ddddd", "Unable to get view for desired position, because it's not being displayed on screen.");
                                        }
                                        View wantedView = chat_view.getChildAt(wantedChild);
                                        Log.d("wantedview", "wantedChild  : " + wantedChild);
                                        Log.d("wantedview", "on screen.  : " + wantedView);
                                        //pb.setVisibility(View.VISIBLE);
                                        mProgressUpdater.mBarToUpdate = multiaudio_seekbar;
                                        mProgressUpdater.tvToUpdate = tvview;
                                        mHandler.postDelayed(mProgressUpdater, 100);
                                    } else {

                                    }
                                }
                                try {
                                    multiaudio_seekbar.setProgress(0);
                                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                                    mmr.setDataSource(fname[i]);
                                    String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                    mmr.release();
                                    String min, sec;
                                    min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
                                    sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
                                    if (Integer.parseInt(min) < 10) {
                                        min = 0 + String.valueOf(min);
                                    }
                                    if (Integer.parseInt(sec) < 10) {
                                        sec = 0 + String.valueOf(sec);
                                    }
                                    tvview.setText(min + ":" + sec);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (fname[i].split("COMMedia/")[1].endsWith(".jpg")|| fname[i].split("COMMedia/")[1].endsWith(".png")) {
                                holder = inflater.inflate(R.layout.chatimagelistview, parent, false);
                                ImageView receiver_multi_msg = (ImageView) holder.findViewById(R.id.receiver_multi_msg);
                                TextView tv_pathname = (TextView) holder.findViewById(R.id.tv_pathname);
                                tv_pathname.setText(fname[i].split("COMMedia/")[1]);
                                imageViewer.display(fname[i],
                                        receiver_multi_msg, R.drawable.refresh);
                                final String path = fname[i];
                                receiver_multi_msg.setTag(path);
                                receiver_multi_msg.setContentDescription(gcBean.getMimetype());
                                receiver_multi_msg.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        saveFile(v.getContentDescription().toString(),v.getTag().toString());
                                        return true;
                                    }
                                });
                                receiver_multi_msg.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(context, FullScreenImage.class);
                                        intent.putExtra("image", path);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                holder = inflater.inflate(R.layout.chatdocumentlistview, parent, false);
                                TextView tv_pathname = (TextView) holder.findViewById(R.id.tv_pathname);
                                tv_pathname.setText(fname[i].split("COMMedia/")[1]);
                                final String path = fname[i];
                                tv_pathname.setTag(path);
                                tv_pathname.setContentDescription(gcBean.getMimetype());
                                tv_pathname.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        saveFile(v.getContentDescription().toString(), v.getTag().toString());
                                        return true;
                                    }
                                });
                                tv_pathname.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FilesFragment.openFilesinExternalApp(path);
                                    }
                                });

                            }
                            holder.setTag(gcBean);
                            chat_view.addView(holder);
                            chat_view.setVisibility(View.VISIBLE);

                        }
                    } else {
                        chat_view.setVisibility(View.GONE);
                    }
                }

                if(gcBean.getWithdrawn()!=null && gcBean.getWithdrawn().equalsIgnoreCase("1") && gcBean.getSenderWithdraw()!=null){
                    tv_senderwithdraw.setVisibility(View.VISIBLE);
                    tv_senderwithdraw.setText(gcBean.getSenderWithdraw());

                }

                if(gcBean.getThumb()==2){
                    rr_send.setVisibility(View.VISIBLE);
                    mainlayout.setBackgroundColor(Color.parseColor("#3D2831"));
                }
                btn_resend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       ResendMsgAlert(gcBean);
                    }
                });

            } catch (Exception e) {
                // TODO Auto-generated catch block
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e.getMessage(), e);
//				else
                e.printStackTrace();
            }
            return convertView;
        }


        public void filter(String charText) {
            Log.i("filter","filter mathod");
            charText = charText.toLowerCase(Locale.getDefault());
            chatList.clear();
            if (charText.length() == 0) {
                chatList.addAll(arraylist);
                Log.i("filter","filter mathod if");
            }
            else
            {
                for (GroupChatBean groupChatBean : arraylist)
                {
                    if ((groupChatBean.getMessage()!=null && groupChatBean.getMessage().toLowerCase(Locale.getDefault()).contains(charText))
                            || (groupChatBean.getMediaName()!=null && groupChatBean.getMediaName().contains("COMMedia") &&
                                groupChatBean.getMediaName().split("COMMedia/")[1].toLowerCase(Locale.getDefault()).contains(charText)))
                    {
                        Log.i("filter","filter mathod for if");
                        chatList.add(groupChatBean);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }

    public void BuddyStatus() {
        Vector<BuddyInformationBean> getBuddyList = ContactsFragment.getBuddyList();
        if (getBuddyList != null) {
            for (int i = 0; i < getBuddyList.size(); i++) {
                BuddyInformationBean buddyInformationBean = (BuddyInformationBean) ContactsFragment.getBuddyList().get(i);
                Log.i("status","BuddyStatus method username-->"+buddyInformationBean.getEmailid()+"  status---->"+buddyInformationBean.getStatus());
                buddyStatus.put(buddyInformationBean.getEmailid(), buddyInformationBean.getStatus());
                Log.i("log", "----download---");
            }
        }

    }

    public String Buddyname(String bname) {
        Vector<BuddyInformationBean> getBuddyList = ContactsFragment.getBuddyList();
        String name = null;
        if (getBuddyList != null) {
            if(bname.equalsIgnoreCase(CallDispatcher.LoginUser)){
                ProfileBean pbean=SingleInstance.myAccountBean;
                name=pbean.getFirstname()+" "+pbean.getLastname();
            }else
            for (int i = 0; i < getBuddyList.size(); i++) {
                BuddyInformationBean buddyInformationBean = (BuddyInformationBean) ContactsFragment.getBuddyList().get(i);
                if (bname.equals(buddyInformationBean.getEmailid())) {
                    name = buddyInformationBean.getFirstname() + " " + buddyInformationBean.getLastname();
                }
            }
        }
        return name;
    }

    private String getYesterdayDateString(SimpleDateFormat dateFormat) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Log.i("dateformat",
                    "yesterday format change :: "
                            + dateFormat.format(cal.getTime()));
            return dateFormat.format(cal.getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return null;
        }
    }

    private void downloadorUploadFile(GroupChatBean gcBean) {

        String fileName = Utils.removeFullPath(gcBean.getMediaName());
        if (gcBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)) {
            Log.i("log", "----upload---");
            uploadFile(gcBean);
        } else {
            Log.i("log", "----download---");
            SingleInstance.mainContext.downloadGroupChatFile(gcBean, fileName);
        }
    }

    public void playMultimedia(View v) {

        // TODO Auto-generated method stub

        String path = v.getTag().toString();
        File file = new File(path);
        Log.i("group123", "icon clicked");
        Log.i("group123", "file path :: " + file.getPath());

        if (v.getContentDescription().toString().equalsIgnoreCase("audio")) {
            Log.i("group123", "icon clicked audio");
            if (file.exists()) {
                Intent intent = new Intent(context, MultimediaUtils.class);
                intent.putExtra("filePath", path);
                intent.putExtra("action", "audio");
                intent.putExtra("createOrOpen", "open");
                startActivity(intent);
            }
        } else if (v.getContentDescription().toString()
                .equalsIgnoreCase("video")) {
            Log.i("group123", "icon clicked video");
            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.stop();
            Intent intent = new Intent(context, VideoPlayer.class);
            intent.putExtra("video", path);
            startActivity(intent);
        }else if (v.getContentDescription().toString()
                .equalsIgnoreCase("videobmapnull")) {
            Log.i("group123", "icon clicked video");
            AppReference.fileOpen=true;
            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.stop();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            intent.setDataAndType(Uri.parse(path), "video/mp4");
            try {
                startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(context,
                        "No Application Available to Play this file",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (v.getContentDescription().toString()
                .equalsIgnoreCase("image")) {
            Log.i("group123", "icon clicked image");
            Intent intent = new Intent(context, FullScreenImage.class);
            intent.putExtra("image", path);
            startActivity(intent);
        } else {
            Log.i("AAAA", "openFilesinExternalApp");
            FilesFragment.openFilesinExternalApp(path);
        }

    }

    public void scheduleMessageDialog(final GroupChatBean gcBean1) {
        if (gcBean1.getPrivateMembers() != null
                && gcBean1.getPrivateMembers().length() > 0) {
            final String[] privateMembers = gcBean1.getPrivateMembers().split(
                    ",");
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    for (String tmp : privateMembers) {
                        if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                                    context);
                            alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.schedule_message));
                            alert_builder.setMessage(SingleInstance.mainContext.getResources().getString(R.string.message_schedule_by)
                                    + gcBean1.getFrom() + " at "
                                    + gcBean1.getReminderTime());
                            alert_builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog, int arg1) {
                                            // TODO Auto-generated method
                                            // stub
                                            dialog.dismiss();
                                        }

                                    });
                            alert_builder.show();
                            break;
                        } else if (gcBean1.getFrom().equalsIgnoreCase(
                                CallDispatcher.LoginUser)) {
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                                    context);
                            alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.schedule_message));
                            alert_builder.setMessage(SingleInstance.mainContext.getResources().getString(R.string.message_schedule_to)
                                    + gcBean1.getPrivateMembers() + " at "
                                    + gcBean1.getReminderTime());
                            alert_builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog, int arg1) {
                                            // TODO Auto-generated method
                                            // stub
                                            dialog.dismiss();
                                        }

                                    });
                            alert_builder.show();
                            break;

                        } else {
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                                    context);
                            alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.schedule_message));
                            alert_builder.setMessage(SingleInstance.mainContext.getResources().getString(R.string.timed_info_from)
                                    + gcBean1.getFrom() + "\n To:"
                                    + gcBean1.getPrivateMembers() + " at "
                                    + gcBean1.getReminderTime());

                            alert_builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog, int arg1) {
                                            // TODO Auto-generated method
                                            // stub
                                            dialog.dismiss();
                                        }

                                    });
                            alert_builder.show();
                            break;

                        }

                    }

                }
            });
        }

    }

    public void deadLineMsgDialog(final GroupChatBean gcBean1) {
        try {
            if (gcBean1.getPrivateMembers() != null
                    && gcBean1.getPrivateMembers().length() > 0) {
                final String[] privateMembers = gcBean1.getPrivateMembers()
                        .split(",");

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        for (String tmp : privateMembers) {
                            if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
                                AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                                        context);
                                alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.deadline_msg));
                                alert_builder
                                        .setMessage(SingleInstance.mainContext.getResources().getString(R.string.dead_line_assigned_from)
                                                + gcBean1.getFrom() + " at "
                                                + gcBean1.getReminderTime());
                                alert_builder.setPositiveButton("Reply",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int arg1) {
                                                // TODO Auto-generated method
                                                // stub
                                                deadLineReplyDialog(gcBean1);
                                                dialog.dismiss();
                                            }

                                        });
                                alert_builder.setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.dismiss();
                                            }

                                        });
                                alert_builder.show();
                                break;

                            } else if (gcBean1.getFrom().equalsIgnoreCase(
                                    CallDispatcher.LoginUser)) {
                                AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                                        context);
                                alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.deadline_msg));
                                alert_builder
                                        .setMessage(SingleInstance.mainContext.getResources().getString(R.string.dead_line_assigned_to)
                                                + gcBean1.getPrivateMembers()
                                                + " at "
                                                + gcBean1.getReminderTime());
                                alert_builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int arg1) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.dismiss();
                                            }

                                        });
                                alert_builder.show();
                                break;

                            } else {
                                AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                                        context);
                                alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.deadline_msg));
                                alert_builder
                                        .setMessage(SingleInstance.mainContext.getResources().getString(R.string.dead_line_assigned_from)
                                                + gcBean1.getFrom() + "\n To:"
                                                + gcBean1.getPrivateMembers()
                                                + " at "
                                                + gcBean1.getReminderTime());

                                alert_builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int arg1) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.dismiss();
                                            }

                                        });
                                alert_builder.show();
                                break;

                            }

                        }
                    }
                });
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void deadLineReplyDialog(final GroupChatBean gcBean1) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                        context);
                alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.deadline_msg));
                // alert_builder.setMessage("DeadLine By "
                // + gcBean1.getFrom());
                CharSequence[] b_type = {SingleInstance.mainContext.getResources().getString(R.string._done_), SingleInstance.mainContext.getResources().getString(R.string.notdone),
                        SingleInstance.mainContext.getResources().getString(R.string.taskcancel), SingleInstance.mainContext.getResources().getString(R.string.resp_later)};
                alert_builder.setSingleChoiceItems(b_type, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                // TODO Auto-generated method stub
                                SpecialMessageBean spBean = new SpecialMessageBean();
                                spBean.setSubcategory("GDI");
                                spBean.setParentId(gcBean1.getParentId());
                                spBean.setMembers(gcBean1.getFrom());
                                if (pos == 0) {
                                    sendMsg(CallDispatcher.LoginUser
                                                    + " completed the task.", "",
                                            "text", spBean);
                                    dialog.dismiss();
                                } else if (pos == 1) {
                                    sendMsg(CallDispatcher.LoginUser
                                                    + " not yet complete the task.",
                                            "", "text", spBean);
                                    dialog.dismiss();
                                } else if (pos == 2) {
                                    sendMsg(CallDispatcher.LoginUser
                                                    + " cancel the task.", "", "text",
                                            spBean);
                                    dialog.dismiss();

                                } else if (pos == 3) {
                                    sendMsg(CallDispatcher.LoginUser
                                                    + " Respond Later", "", "text",
                                            spBean);
                                    dialog.dismiss();
                                }
                            }
                        });
                alert_builder.show();
            }
        });

    }

    private void showReplyBackDialog(final GroupChatBean gcBean1) {
        try {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
            alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.chat_options));
            CharSequence[] b_type = {"Text", "Handsketch", "Image", "Audio",
                    "Video"};
            // privateMembers = gcBean1.getPrivateMembers();
            if (!gcBean1.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                privateMembers = gcBean1.getFrom();
            } else {
                privateMembers = gcBean1.getPrivateMembers();
            }

            alert_builder.setSingleChoiceItems(b_type, 0,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int pos) {
                            // TODO Auto-generated method stub
                            if (gcBean1.getParentId() != null
                                    && !gcBean1.getParentId().equalsIgnoreCase(
                                    "null")) {
                                pId = gcBean1.getParentId();
                            } else {
                                pId = gcBean1.getSignalid();
                            }
                            if (pos == 0) {
                                // Intent intent = new Intent(context,
                                // CloneTextinput.class);
                                // startActivityForResult(intent, 37);
                                isReplyBack = true;
                                dialog.dismiss();
                            } else if (pos == 1) {
                                isReplyBack = true;
                                // Intent intent = new Intent(context,
                                // HandSketchActivity.class);
                                Intent intent = new Intent(context,
                                        HandSketchActivity2.class);
                                startActivityForResult(intent, 36);
                                dialog.dismiss();
                            } else if (pos == 2) {
                                isReplyBack = true;
                                photochat();
                                dialog.dismiss();
                            } else if (pos == 3) {
                                isReplyBack = true;
                                showAudioMessageDialog();
                                dialog.dismiss();
                            } else if (pos == 4) {

                                isReplyBack = true;
                                showVideoMessageDialog();
                                dialog.dismiss();
                            }
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

    private void loadTotalChatHistory(String groupOrBuddyName) {
        if (loadChatHistory(groupOrBuddyName) != null)
            chatList = loadChatHistory(groupOrBuddyName);
        Collections.sort(chatList, new GroupMessageComparator());
//        if (SingleInstance.groupChatHistory.get(groupOrBuddyName) != null
//                && SingleInstance.groupChatHistory.get(groupOrBuddyName).size() > 0) {
//            chatList = SingleInstance.groupChatHistory.get(groupOrBuddyName);
//            Log.i("group123", "inside chathistory hashmap" + SingleInstance.groupChatHistory.get(groupOrBuddyName).size());
//        } else {
            if (chatList == null)
                chatList = new Vector<GroupChatBean>();
            Log.i("group123", "inside else chathistory hashmap");
            if(SingleInstance.groupChatHistory.containsKey(groupOrBuddyName)) {
                SingleInstance.groupChatHistory.remove(groupOrBuddyName);
            }
            SingleInstance.groupChatHistory.put(groupOrBuddyName, chatList);
//        }
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

    private String createTextNote(String message) {
        String ComponentPath = "/sdcard/COMMedia/"
                + CompleteListView.getFileName() + ".txt";
        if (CompleteListView.textnotes == null)
            CompleteListView.textnotes = new TextNoteDatas();
        CompleteListView.textnotes.checkAndCreate(ComponentPath, message);
        return ComponentPath;

    }

    private void showBuddyDialog() {
        try {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);

            CharSequence[] b_type = {SingleInstance.mainContext.getResources().getString(R.string.view_profile), SingleInstance.mainContext.getResources().getString(R.string.clear_history1), SingleInstance.mainContext.getResources().getString(R.string.cancel)};
            CharSequence[] buddy_type = {SingleInstance.mainContext.getResources().getString(R.string.clear_all), SingleInstance.mainContext.getResources().getString(R.string.cancel)};

            if (!isGroup && !isRounding) {
                alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.select_buddy_info));
                alert_builder.setSingleChoiceItems(b_type, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                // TODO Auto-generated method stub
                                if (pos == 0) {
                                    doViewProfile(buddy);
                                    dialog.dismiss();
                                } else if (pos == 1) {
                                    dialog.dismiss();
                                    if (chatList == null || chatList.size() == 0) {
                                        showToast(SingleInstance.mainContext.getResources().getString(R.string.cannot_clear_empty));
                                    } else {
                                        clearAllAlert(buddy);
                                    }
                                } else if (pos == 2) {
                                    dialog.dismiss();

                                }

                            }
                        });
            } else {
                alert_builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.select_group_info)
                );
                alert_builder.setSingleChoiceItems(buddy_type, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                // TODO Auto-generated method stub
                                if (pos == 0) {
                                    // dialog.dismiss();
                                    Log.i("deleshow", "delete dialog");

                                    dialog.dismiss();
                                    if (chatList == null || chatList.size() == 0) {
                                        showToast(SingleInstance.mainContext.getResources().getString(R.string.cannot_clear_empty));
                                    } else {
                                        clearAllAlert(groupBean.getGroupName());
                                    }
                                } else if (pos == 1) {
                                    dialog.dismiss();

                                }

                            }
                        });

            }
            alert_builder.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    void doViewProfile(final String buddy) {

        try {

            ArrayList<String> profileList = DBAccess.getdbHeler().getProfile(
                    buddy);
            final AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");
            if (profileList.size() > 0) {
                // ViewProfileFragment viewProfileFragment = ViewProfileFragment
                // .newInstance(context);
                // viewProfileFragment.setBuddyName(buddy);
                // viewProfileFragment.setFromActivity("groupchat");
                // FragmentManager fragmentManager = appMainActivity
                // .getSupportFragmentManager();
                // FragmentTransaction fragmentTransaction = fragmentManager
                // .beginTransaction();
                // fragmentTransaction.replace(
                // R.id.activity_main_content_fragment,
                // viewProfileFragment);
                // fragmentTransaction.commitAllowingStateLoss();
                viewProfile(buddy);
            } else {
                if (appMainActivity.isNetworkConnectionAvailable()) {
                    showprogress();
                    CallDispatcher.isFromCallDisp = false;
                    String modifiedDate = DBAccess.getdbHeler()
                            .getModifiedDate(
                                    "select max(modifieddate) from profilefieldvalues where userid='"
                                            + buddy + "'");
                    if (modifiedDate == null) {
                        modifiedDate = "";
                    } else if (modifiedDate.trim().length() == 0) {
                        modifiedDate = "";
                    }
                    String[] profileDetails = new String[3];
                    profileDetails[0] = buddy;
                    profileDetails[1] = "5";
                    profileDetails[2] = modifiedDate;
                    // WebServiceReferences.webServiceClient
                    // .getStandardProfilefieldvalues(profileDetails);
                    isProfileRequested = true;
                    BGProcessor.getInstance().getStandardProfilefieldvalues(
                            profileDetails);

                } else
                    Toast.makeText(context, "Kindly check your network ",
                            Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void showIntermediateProgress(final ProgressBar pBar) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                while (progressStates < 1000) {
                    progressStates += 5;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            // TODO Auto-generated method stub
                            pBar.setProgress(progressStates);
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // private void deadLineReply(RadioGroup radioGroup, final GroupChatBean
    // gcBean) {
    // final SpecialMessageBean spBean = new SpecialMessageBean();
    // spBean.setSubcategory("GDI");
    // spBean.setParentId(gcBean.getParentId());
    // spBean.setMembers(gcBean.getFrom());
    // radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    //
    // @Override
    // public void onCheckedChanged(RadioGroup arg0, int checkedId) {
    // // TODO Auto-generated method stub
    // switch (checkedId) {
    // case R.id.not_done:
    // sendMsg(CallDispatcher.LoginUser + " couldn't complete the task.", "",
    // "text", spBean);
    // break;
    // case R.id.done:
    // sendMsg(CallDispatcher.LoginUser + " completed the task.", "",
    // "text", spBean);
    // break;
    // case R.id.cancel:
    // sendMsg(CallDispatcher.LoginUser + " task cancelled.", "",
    // "text", spBean);
    // break;
    //
    // default:
    // break;
    // }
    // }
    // });
    //
    // }

    public void notifyFormCreatedOrEdited(String tableId, String formName) {
        if (isGroup) {
            ArrayList<String[]> formAccessList = new ArrayList<String[]>();
            GroupBean gBean = DBAccess.getdbHeler().getGroupAndMembers(
                    "select * from groupdetails where groupid="
                            + groupBean.getGroupId());
            List<String> buddyList = new ArrayList<String>(Arrays.asList(gBean
                    .getActiveGroupMembers().split("\\s*,\\s*")));
            for (String buddy : buddyList) {
                String[] formDetails = new String[9];
                formDetails[0] = CallDispatcher.LoginUser;
                formDetails[1] = tableId;
                formDetails[2] = buddy;
                formDetails[3] = "A02";
                formDetails[4] = "S02";
                formDetails[5] = "";
                formDetails[6] = "";
                formDetails[7] = "new";
                formDetails[8] = formName;
                formAccessList.add(formDetails);
            }
            if (formAccessList.size() > 0) {
                WebServiceReferences.webServiceClient
                        .SaveAccessFormForMultipleEntry(context,
                                formAccessList, formAccessList.get(0), "no");
            }
        } else {
            String[] formDetails = new String[9];
            formDetails[0] = CallDispatcher.LoginUser;
            formDetails[1] = tableId;
            formDetails[2] = buddy;
            formDetails[3] = "A02";
            formDetails[4] = "S02";
            formDetails[5] = "";
            formDetails[6] = "";
            formDetails[7] = "new";
            formDetails[8] = formName;
            WebServiceReferences.webServiceClient.SaveAccessForm(
                    formDetails[0], formDetails[1], formDetails[2],
                    formDetails[3], formDetails[4], formDetails[5], "", "new",
                    context, formDetails);
        }
    }

    public void notifyShareFormBeforeAddRecords(String formName,
                                                String tableId, ArrayList<String> buddiesList) {
        ArrayList<String[]> formAccessList = new ArrayList<String[]>();
        for (String buddy : buddiesList) {
            String[] formDetails = new String[9];
            formDetails[0] = CallDispatcher.LoginUser;
            formDetails[1] = tableId;
            formDetails[2] = buddy;
            formDetails[3] = "A02";
            formDetails[4] = "S02";
            formDetails[5] = "";
            formDetails[6] = "";
            formDetails[7] = "new";
            formDetails[8] = formName;
            formAccessList.add(formDetails);
        }
        if (formAccessList.size() > 0) {
            WebServiceReferences.webServiceClient
                    .SaveAccessFormForMultipleEntry(context, formAccessList,
                            formAccessList.get(0), "yes");
        }

    }

    public void notifyWebserviceForAccessForm(final Object obj,
                                              final Object extraDetails, final String isAddRecords) {

        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    // TODO Auto-generated method stub
                    if (obj instanceof String[]) {
                        String[] response = (String[]) obj;

                        String buddy = response[0];
                        String fsid = response[1];
                        String formid = response[2];
                        String formcreatedtinme = response[3];
                        String editedtime = response[4];
                        String mode = response[5];
                        if (mode.equals("edit")) {

                            ContentValues cv = new ContentValues();

                            cv.put("settingid", fsid);
                            cv.put("buddyid", buddy);
                            cv.put("datecreated", formcreatedtinme);
                            cv.put("datemodified", editedtime);

                            callDisp.getdbHeler(context).updates(formid, cv,
                                    buddy);
                            // Intent intentfrms = new Intent(context,
                            // FormSettings.class);
                            // startActivity(intentfrms);
                            if (WebServiceReferences.contextTable
                                    .containsKey("formpermissionviewer")) {
                                FormPermissionViewer frm_activity = (FormPermissionViewer) WebServiceReferences.contextTable
                                        .get("formpermissionviewer");
                                frm_activity.refreshList();
                            }
                            finish();

                        } else if (mode.equals("new")) {
                            if (extraDetails instanceof String[]) {
                                String[] formDetails = (String[]) extraDetails;
                                if (formDetails != null
                                        && formDetails.length > 0) {
                                    String finalQuery = "";
                                    String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
                                            + "values('"
                                            + fsid
                                            + "','"
                                            + formDetails[0]
                                            + "','"
                                            + formid
                                            + "','"
                                            + buddy
                                            + "','"
                                            + formDetails[3]
                                            + "','"
                                            + formDetails[4]
                                            + "','"
                                            + finalQuery
                                            + "','"
                                            + formcreatedtinme
                                            + "','"
                                            + editedtime + "')";

                                    if (callDisp.getdbHeler(context)
                                            .ExecuteQuery(insertQuery1)) {
                                        sendMsg(formDetails[8] + " form shared",
                                                null, "text", null);
                                    }
                                } else {
                                    showToast("Form details are empty");
                                }
                            }
                        }

                    } else if (obj instanceof ArrayList) {
                        ArrayList<String[]> responseList = (ArrayList<String[]>) obj;
                        boolean isShared = false;
                        for (String[] response : responseList) {
                            String buddy = response[0];
                            String fsid = response[1];
                            String formid = response[2];
                            String formcreatedtinme = response[3];
                            String editedtime = response[4];
                            String mode = response[5];
                            if (mode.equals("edit")) {

                                ContentValues cv = new ContentValues();

                                cv.put("settingid", fsid);
                                cv.put("buddyid", buddy);
                                cv.put("datecreated", formcreatedtinme);
                                cv.put("datemodified", editedtime);

                                callDisp.getdbHeler(context).updates(formid,
                                        cv, buddy);
                                // Intent intentfrms = new Intent(context,
                                // FormSettings.class);
                                // startActivity(intentfrms);
                                if (WebServiceReferences.contextTable
                                        .containsKey("formpermissionviewer")) {
                                    FormPermissionViewer frm_activity = (FormPermissionViewer) WebServiceReferences.contextTable
                                            .get("formpermissionviewer");
                                    frm_activity.refreshList();
                                }
                                finish();

                            } else if (mode.equals("new")) {
                                if (extraDetails instanceof String[]) {
                                    String[] formDetails = (String[]) extraDetails;
                                    if (formDetails != null
                                            && formDetails.length > 0) {
                                        String finalQuery = "";
                                        String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
                                                + "values('"
                                                + fsid
                                                + "','"
                                                + formDetails[0]
                                                + "','"
                                                + formid
                                                + "','"
                                                + buddy
                                                + "','"
                                                + formDetails[3]
                                                + "','"
                                                + formDetails[4]
                                                + "','"
                                                + finalQuery
                                                + "','"
                                                + formcreatedtinme
                                                + "','"
                                                + editedtime + "')";

                                        if (callDisp.getdbHeler(context)
                                                .ExecuteQuery(insertQuery1)) {
                                            isShared = true;
                                        }
                                    } else {
                                        showToast("Form details are empty");
                                    }
                                }
                            }
                        }
                        if (isShared) {
                            String[] formDetails = null;
                            if (extraDetails instanceof String[]) {
                                formDetails = (String[]) extraDetails;
                            }
                            if (formDetails != null) {
                                sendMsg(formDetails[8] + " form shared", null,
                                        "text", null);
                                notifyFormEdited(formDetails[8]);
                            }
                        }

                    } else if (obj instanceof WebServiceBean) {
                        WebServiceBean service_bean = (WebServiceBean) obj;
                        showToast(service_bean.getText());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }

    public void showAlert1(String title, String message) {

        AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
        alertCall.setMessage(message).setTitle(title).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
        alertCall.show();
    }

    public String getGroupAndMembers() {
        String members = "";
        if (isGroup || isRounding) {
            GroupBean gBean = DBAccess.getdbHeler().getGroupAndMembers(
                    "select * from groupdetails where groupid="
                            + groupBean.getGroupId());
            if (gBean != null) {
                members = getMembers(gBean.getOwnerName() + ","
                        + gBean.getActiveGroupMembers());
            }
        } else {
            members = buddy;
        }
        return members;
    }

    public String getCurrentDateandTime() {
        try {
            // Calendar c = Calendar.getInstance();
            // SimpleDateFormat sdf = new
            // SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
            // String strDate = sdf.format(c.getTime());

            Date curDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss a");
            return sdf.format(curDate).toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public GroupTempBean setAndGetGroupDetails() {
        GroupTempBean gTempBean = new GroupTempBean();
        gTempBean.setGroup(isGroup);
        if (isGroup || isRounding) {
            gTempBean.setGroupId(groupId);
        } else {
            gTempBean.setBuddy(buddy);
            gTempBean.setGroupId(CallDispatcher.LoginUser + buddy);
        }
        return gTempBean;
    }

    public void showGroupDialog(GroupBean groupBean) {

        try {
            // TODO Auto-generated method stub
            Log.i("group123", "item clicked 3");

            if (CallDispatcher.LoginUser != null) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.groupdialog);
                dialog.setTitle("Select any service you want to make");
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

                wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
                wmlp.x = 10;   //x position
                wmlp.y = 10;   //y position
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
                Log.i("group123", "group owner : " + groupBean.getOwnerName());

                TextView deleteGroupText = (TextView) deleteGroup
                        .findViewById(R.id.delete_grp_txt);
                if (groupBean.getOwnerName() != null) {
                    if (!groupBean.getOwnerName().equalsIgnoreCase(
                            CallDispatcher.LoginUser)) {
                        deleteGroupText.setText(SingleInstance.mainContext.getResources().getString(R.string.leave_group1));
                        editGroupText.setText(SingleInstance.mainContext.getResources().getString(R.string.view_group1));
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
                            Intent settingIntent = new Intent(context
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
                            Intent intent = new Intent(context
                                    .getApplicationContext(),
                                    GroupActivity.class);
                            intent.putExtra("isEdit", true);
                            intent.putExtra("id", groupBean.getGroupId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context
                                    .getApplicationContext(), ViewGroups.class);
                            intent.putExtra("id", groupBean.getGroupId());
                            context.startActivity(intent);
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
                                            Log.i("group123", "members "
                                                    + members);
                                            if (members != null
                                                    && members.length() > 0) {
                                                callDisp.requestAudioBroadCast(members);
                                            }

                                        } else
                                            Toast.makeText(
                                                    context,
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
                                            Log.i("group123", "members "
                                                    + members);
                                            if (members != null
                                                    && members.length() > 0) {
                                                callDisp.requestVideoBroadCast(members);
                                            }
                                        } else
                                            Toast.makeText(
                                                    context,
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
                                            callDisp.requestAudioConference(gBean
                                                    .getOwnerName()
                                                    + ","
                                                    + gBean.getActiveGroupMembers());
                                        } else
                                            Toast.makeText(
                                                    context,
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
                                GroupBean groupBean = callDisp.getdbHeler(
                                        context).getGroupAndMembers(
                                        "select * from groupdetails where groupid="
                                                + gBean.getGroupId());
                                if (groupBean != null
                                        && groupBean.getActiveGroupMembers() != null
                                        && groupBean.getActiveGroupMembers()
                                        .length() > 0) {
                                    Intent intent = new Intent(context
                                            .getApplicationContext(),
                                            GroupChatActivity.class);
                                    intent.putExtra("groupid",
                                            gBean.getGroupId());
                                    intent.putExtra("isGroup", true);
                                    context.startActivity(intent);
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

    private void shareFiles(String members, String type) {

        try {
            String to = getMembers(members);
            if (to != null && to.length() > 0) {
                // Intent intentComponent = new Intent(getActivity()
                // .getApplicationContext(), HandSketchActivity2.class);
                Intent intentComponent = new Intent(context
                        .getApplicationContext(), ComponentCreator.class);
                Bundle bndl = new Bundle();
                bndl.putString("type", type);
                bndl.putBoolean("action", true);
                bndl.putBoolean("forms", false);
                bndl.putString("buddyname", to);
                bndl.putBoolean("send", true);
                intentComponent.putExtras(bndl);
                context.startActivity(intentComponent);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void deleteGroup(final GroupBean groupManagementBean, String message) {

        try {
            if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        context);
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
                                            callDisp.startWebService(
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
                                        finish();

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
            }
//			else {
//				ShowError("Info",
//						"Check Internet Connection Unable to Connect server",
//						context);
//			}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
//		Toast.makeText(GroupChatActivity.this,"thiss....",Toast.LENGTH_LONG).show();

//		if (s.toString().length()>0){
//			if (isGroup) {
//				GroupChatPermissionBean gcpBean = SingleInstance.mainContext
//						.getGroupChatPermission(groupBean);
//				if (gcpBean.getChat().equalsIgnoreCase("1"))
//					msgoptionview.setVisibility(View.VISIBLE);
//				else
//			        msgoptionview.setVisibility(View.GONE);
//			}else
//				msgoptionview.setVisibility(View.VISIBLE);
//
//		} else  {
//
//			msgoptionview.setVisibility(View.GONE);
//		}
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    public void sendWithReplayBack() {

        if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {

            SpecialMessageBean spBean = new SpecialMessageBean();
            spBean.setSubcategory("grb");

            if (isReplyBack) {
                Log.i("AAAA","reply individual");
                spBean.setSubcategory("grb");
                spBean.setParentId(pId);
                if (isGroup || isRounding) {

                    spBean.setMembers(privateMembers);

                } else {
                    spBean.setMembers(buddy);
                }


            } else {
                spBean.setMembers(buddy);
            }
            if (SendListUI.size() > 0) {
                if(SendListUI.size()==1) {
                    Log.i("audioplay", "path--->" + strIPath);
                    SendListUIBean bean = SendListUI.get(0);
                    sendMsg(message.getText().toString(), bean.getPath(), bean.getType(), spBean);
                    message.setVisibility(View.VISIBLE);
                    SendListUI.remove(0);
                    if (SendListUI.size() == 0) {
                        SendListUI.clear();
                    }
                    sendlistadapter.notifyDataSetChanged();
                    list_all.removeAllViews();
                    final int adapterCount = sendlistadapter.getCount();

                    for (int i = 0; i < adapterCount; i++) {
                        View item = sendlistadapter.getView(i, null, null);
                        list_all.addView(item);
                    }
                    if(adapterCount>=2){
                        multi_send.getLayoutParams().height=280;
                    }
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.GONE);
                    }else {
                        msgoptionview.setVisibility(View.GONE);
                    }
                    audio_call.setBackgroundResource(R.drawable.chat_send);
                    audio_call.setTag(1);
//                relative_send_layout.getLayoutParams().height = 90;
                }
                //For Multi file send with Reply back
                //start
                else if(SendListUI.size()>1){
                    String path = null;
                    for (int i = 0; i < SendListUI.size(); i++) {
                        SendListUIBean bean = SendListUI.get(i);
                        if (path == null) {
                            path = bean.getPath();
                        } else {
                            path = path + "," + bean.getPath();
                        }
                    }
//                    SendListUIBean bean = SendListUI.get(0);
                    sendMsg(message.getText().toString(), path, "mixedfile", spBean);
                    message.setVisibility(View.VISIBLE);
//                    SendListUI.remove(0);
//                    if (SendListUI.size() == 0) {
//                        SendListUI.clear();
//                    }
                    SendListUI.clear();
                    sendlistadapter.notifyDataSetChanged();
                    list_all.removeAllViews();
                    final int adapterCount = sendlistadapter.getCount();

                    for (int i = 0; i < adapterCount; i++) {
                        View item = sendlistadapter.getView(i, null, null);
                        list_all.addView(item);
                    }
                    if(adapterCount>=2){
                        multi_send.getLayoutParams().height=280;
                    }
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.GONE);
                    }else {
                        msgoptionview.setVisibility(View.GONE);
                    }
                    audio_call.setBackgroundResource(R.drawable.chat_send);
                    audio_call.setTag(1);
                }
                //End



            } else {
                sendMsg(message.getText().toString(), null, "text", spBean);
            }


        } else {
            showAlert1("Info", "Check the Internet connection unable to connect server");
        }

    }

    private void sendWithSchedule(final String members) {
        try {
//		   Button btnSet;
//		   final DatePicker dp;
//		   final TimePicker tp;
//		   final AlertDialog alertReminder = new AlertDialog.Builder(context)
//		     .create();
//		   ScrollView tblDTPicker = (ScrollView) View.inflate(context,
//		     R.layout.date_time_picker, null);
//		   btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
//		   dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
//		   tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);
//		   btnSet.setOnClickListener(new OnClickListener() {
//
//		    @Override
//		    public void onClick(View v) {
//		     try {
//
//		      final String strDateTime = dp.getYear()
//		        + "-"
//		        + WebServiceReferences.setLength2((dp
//		          .getMonth() + 1))
//		        + "-"
//		        + WebServiceReferences.setLength2(dp
//		          .getDayOfMonth())
//		        + " "
//		        + WebServiceReferences.setLength2(tp
//		          .getCurrentHour())
//		        + ":"
//		        + WebServiceReferences.setLength2(tp
//		          .getCurrentMinute());
//
//		      Log.e("timemessage", "@@@@@@@" + strDateTime);
//
//		      if (CompleteListView.CheckReminderIsValid(strDateTime)) {
//
//		       SpecialMessageBean spBean = new SpecialMessageBean();
//		       spBean.setSubcategory("gs");
//		       spBean.setRemindertime(strDateTime);
//
//		       // dateLayout.setVisibility(View.VISIBLE);
//		       // dateTime.setText(strDateTime);
//
//		       if (isReplyBack) {
//		        spBean.setSubcategory("gs");
//		        spBean.setParentId(pId);
//		        if (isGroup) {
//
//		         spBean.setMembers(privateMembers);
//
//		        } else {
//		         spBean.setMembers(buddy);
//		        }
//
//		       } else {
//		        spBean.setMembers(members);
//		       }
//
//		       sendMsg(message.getText().toString(), null, "text",
//		         spBean);
//
//		       alertReminder.cancel();
//		      } else {
//
//		       Toast.makeText(context,
//		         "Please assign future date and time",
//		         Toast.LENGTH_SHORT).show();
//		      }
//		     } catch (Exception e) {
//		      // TODO Auto-generated catch block
//		      e.printStackTrace();
//		     }
//
//		    }
//		   });
//		   alertReminder.setTitle("Date And Time");
//		   alertReminder.setView(tblDTPicker);
//		   alertReminder.show();
            if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {

                SpecialMessageBean spBean = new SpecialMessageBean();
                spBean.setSubcategory("gc");

                if (isReplyBack) {
                    spBean.setSubcategory("gc");
                    spBean.setParentId(pId);
                    if (isGroup || isRounding) {

                        spBean.setMembers(privateMembers);

                    } else {
                        spBean.setMembers(buddy);
                    }


                } else {
                    spBean.setMembers(buddy);
                }

                if (SendListUI.size() == 1) {
                    Log.i("audioplay", "path--->" + strIPath);
                    SendListUIBean bean = SendListUI.get(0);
                    sendMsg(message.getText().toString(), bean.getPath(), bean.getType(), spBean);
                    message.setVisibility(View.VISIBLE);
                    SendListUI.remove(0);
                    if (SendListUI.size() == 0) {
                        SendListUI.clear();
                    }
                    sendlistadapter.notifyDataSetChanged();
                    list_all.removeAllViews();
                    final int adapterCount = sendlistadapter.getCount();

                    for (int i = 0; i < adapterCount; i++) {
                        View item = sendlistadapter.getView(i, null, null);
                        list_all.addView(item);
                    }
                    if(adapterCount>=2){
                        multi_send.getLayoutParams().height=280;
                    }
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.VISIBLE);
                    }else {
                        msgoptionview.setVisibility(View.VISIBLE);
                    }
                    audio_call.setBackgroundResource(R.drawable.chat_send);
                    audio_call.setTag(1);
//                    relative_send_layout.getLayoutParams().height = 90;
                } else if (SendListUI.size() > 1) {
                    String path = null;
                    for (int i = 0; i < SendListUI.size(); i++) {
                        SendListUIBean bean = SendListUI.get(i);
                        if (path == null) {
                            path = bean.getPath();
                        } else {
                            path = path + "," + bean.getPath();
                        }
                    }


                    sendMsg(message.getText().toString().trim(),
                            path, "mixedfile", spBean);
                    message.setVisibility(View.VISIBLE);
                    SendListUI.remove(0);
                    if (SendListUI.size() > 0) {
                        SendListUI.clear();
                    }
                    sendlistadapter.notifyDataSetChanged();
                    list_all.removeAllViews();
                    final int adapterCount = sendlistadapter.getCount();

                    for (int i = 0; i < adapterCount; i++) {
                        View item = sendlistadapter.getView(i, null, null);
                        list_all.addView(item);
                    }
                    if(adapterCount>=2){
                        multi_send.getLayoutParams().height=280;
                    }
                    if(isPrivateBack){
                        LL_privateReply.setVisibility(View.GONE);
                    }else {
                        msgoptionview.setVisibility(View.GONE);
                    }
                    audio_call.setBackgroundResource(R.drawable.chat_send);
                    audio_call.setTag(1);
//                relative_send_layout.getLayoutParams().height = 90;


                } else {
                    sendMsg(message.getText().toString(), null, "text", spBean);
                }

            } else {
                showAlert1("Info", "Check the Internet connection unable to connect server");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void sendWithDeadline(final String members) {

//        try {
//		   Button btnSet;
//		   final DatePicker dp;
//		   final TimePicker tp;
//		   final AlertDialog alertReminder = new AlertDialog.Builder(context)
//		     .create();
//		   ScrollView tblDTPicker = (ScrollView) View.inflate(context,
//		     R.layout.date_time_picker, null);
//		   btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
//		   dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
//		   tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);
//		   btnSet.setOnClickListener(new OnClickListener() {
//
//		    @Override
//		    public void onClick(View v) {
//		     try {
//
//		      final String strDateTime = dp.getYear()
//		        + "-"
//		        + WebServiceReferences.setLength2((dp
//		          .getMonth() + 1))
//		        + "-"
//		        + WebServiceReferences.setLength2(dp
//		          .getDayOfMonth())
//		        + " "
//		        + WebServiceReferences.setLength2(tp
//		          .getCurrentHour())
//		        + ":"
//		        + WebServiceReferences.setLength2(tp
//		          .getCurrentMinute());
//
//		      Log.e("timemessage", "@@@@@@@" + strDateTime);
//
//		      if (CompleteListView.CheckReminderIsValid(strDateTime)) {
//
//		       SpecialMessageBean spBean = new SpecialMessageBean();
//		       spBean.setSubcategory("gd");
//		       spBean.setRemindertime(strDateTime);
//
//		       // dateLayout.setVisibility(View.VISIBLE);
//		       // dateTime.setText(strDateTime);
//
//		       if (isReplyBack) {
//		        spBean.setSubcategory("gd");
//		        spBean.setParentId(pId);
//		        if (isGroup) {
//
//		         spBean.setMembers(privateMembers);
//
//		        } else {
//		         spBean.setMembers(buddy);
//		        }
//
//		       } else {
//		        spBean.setMembers(members);
//		       }
//
//		       sendMsg(message.getText().toString(), null, "text",
//		         spBean);
//
//		       alertReminder.cancel();
//		      } else {
//
//		       Toast.makeText(context,
//		         "Please assign future date and time",
//		         Toast.LENGTH_SHORT).show();
//		      }
//		     } catch (Exception e) {
//		      // TODO Auto-generated catch block
//		      e.printStackTrace();
//		     }
//
//		    }
//		   });
//		   alertReminder.setTitle("Date And Time");
//		   alertReminder.setView(tblDTPicker);
//		   alertReminder.show();

            if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {

                SpecialMessageBean spBean = new SpecialMessageBean();
                spBean.setSubcategory("gu");

                if (isUrgent) {
                    spBean.setSubcategory("gu");
                    spBean.setParentId(pId);
                    if (isGroup || isRounding) {

                        spBean.setMembers(privateMembers);

                    } else {
                        spBean.setMembers(buddy);
                    }


                } else {
                    spBean.setMembers(buddy);
                }

                if (SendListUI.size() > 0) {
                    if (SendListUI.size() == 1) {
                        Log.i("audioplay", "path--->" + strIPath);
                        SendListUIBean bean = SendListUI.get(0);
                        sendMsg(message.getText().toString(), bean.getPath(), bean.getType(), spBean);
                        message.setVisibility(View.VISIBLE);
                        SendListUI.remove(0);
                        if (SendListUI.size() == 0) {
                            SendListUI.clear();
                        }
                        sendlistadapter.notifyDataSetChanged();
                        list_all.removeAllViews();
                        final int adapterCount = sendlistadapter.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            View item = sendlistadapter.getView(i, null, null);
                            list_all.addView(item);
                        }
                        if (adapterCount >= 2) {
                            multi_send.getLayoutParams().height = 280;
                        }
                        if (isPrivateBack) {
                            LL_privateReply.setVisibility(View.VISIBLE);
                        } else {
                            msgoptionview.setVisibility(View.VISIBLE);
                        }
                        audio_call.setBackgroundResource(R.drawable.chat_send);
                        audio_call.setTag(1);
//                        relative_send_layout.getLayoutParams().height = 90;
                    } else if (SendListUI.size() > 1) {
                        String path = null;
                        for (int i = 0; i < SendListUI.size(); i++) {
                            SendListUIBean bean = SendListUI.get(i);
                            if (path == null) {
                                path = bean.getPath();
                            } else {
                                path = path + "," + bean.getPath();
                            }
                        }
//                    SendListUIBean bean = SendListUI.get(0);
                        sendMsg(message.getText().toString(), path, "mixedfile", spBean);
                        message.setVisibility(View.VISIBLE);
//                    SendListUI.remove(0);
//                    if (SendListUI.size() == 0) {
//                        SendListUI.clear();
//                    }
                        SendListUI.clear();
                        sendlistadapter.notifyDataSetChanged();
                        list_all.removeAllViews();
                        final int adapterCount = sendlistadapter.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            View item = sendlistadapter.getView(i, null, null);
                            list_all.addView(item);
                        }
                        if (adapterCount >= 2) {
                            multi_send.getLayoutParams().height = 280;
                        }
                        if (isPrivateBack) {
                            LL_privateReply.setVisibility(View.GONE);
                        } else {
                            msgoptionview.setVisibility(View.GONE);
                        }
                        audio_call.setBackgroundResource(R.drawable.chat_send);
                        audio_call.setTag(1);
                    }
                    //End

                }else {
                    sendMsg(message.getText().toString(), null, "text", spBean);
                }

            } else {
                showAlert1("Info", "Check the Internet connection unable to connect server");
            }


//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }

    public void notifyDeleteGroup(Object obj) {
        try {
            if (obj instanceof GroupBean) {
                String groupId = ((GroupBean) obj).getGroupId();
                if (groupId != null) {
                    callDisp.getdbHeler(context).deleteGroupAndMembers(
                            groupId);
                    GroupBean groupBean = null;
                    for (GroupBean gBean : GroupActivity.groupList) {
                        if (gBean.getGroupId().equals(groupId)) {
                            groupBean = gBean;
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
                                GroupActivity.groupAdapter2
                                        .notifyDataSetChanged();

                            }
                        });

                    }

                    callDisp.getdbHeler(context)
                            .deleteGroupChatEntryLocally(groupId,
                                    CallDispatcher.LoginUser);
                    showToast("Group deleted Successfully");
                }
            } else {
                WebServiceBean ws_bean = (WebServiceBean) obj;
                callDisp.ShowError("Error", ws_bean.getText(), context);
            }
            ContactsFragment.getInstance(context).getList();
            cancelDialog();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendSpecialMessage(String subgatery, String members) {

        if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {

            SpecialMessageBean spBean = new SpecialMessageBean();
            spBean.setSubcategory(subgatery);

            if (isReplyBack) {
                spBean.setSubcategory("grb");
                spBean.setParentId(pId);
                if (isGroup || isRounding) {
                    spBean.setMembers(privateMembers);
                } else {
                    spBean.setMembers(buddy);
                }

            } else {
                spBean.setMembers(members);
            }


            if (SendListUI.size() == 1) {
                Log.i("audioplay", "path--->" + strIPath);
                SendListUIBean bean = SendListUI.get(0);
                sendMsg(message.getText().toString().trim(),
                        bean.getPath(), bean.getType(), spBean);
                message.setVisibility(View.VISIBLE);
                SendListUI.remove(0);
                if (SendListUI.size() == 0) {
                    SendListUI.clear();
                }
                sendlistadapter.notifyDataSetChanged();
                list_all.removeAllViews();
                final int adapterCount = sendlistadapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = sendlistadapter.getView(i, null, null);
                    list_all.addView(item);
                }
                if(adapterCount>=2){
                    multi_send.getLayoutParams().height=280;
                }
                if(isPrivateBack){
                    LL_privateReply.setVisibility(View.GONE);
                }else {
                    msgoptionview.setVisibility(View.GONE);
                }
                audio_call.setBackgroundResource(R.drawable.chat_send);
                audio_call.setTag(1);
//                relative_send_layout.getLayoutParams().height = 90;
            } else if (SendListUI.size() > 1) {
                String path = null;
                for (int i = 0; i < SendListUI.size(); i++) {
                    SendListUIBean bean = SendListUI.get(i);
                    if (path == null) {
                        path = bean.getPath();
                    } else {
                        path = path + "," + bean.getPath();
                    }
                }


                sendMsg(message.getText().toString().trim(),
                        path, "mixedfile", spBean);
                message.setVisibility(View.VISIBLE);
                SendListUI.remove(0);
                if (SendListUI.size() > 0) {
                    SendListUI.clear();
                }
                sendlistadapter.notifyDataSetChanged();
                list_all.removeAllViews();
                final int adapterCount = sendlistadapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = sendlistadapter.getView(i, null, null);
                    list_all.addView(item);
                }
                if(adapterCount>=2){
                    multi_send.getLayoutParams().height=280;
                }
                if(isPrivateBack){
                    LL_privateReply.setVisibility(View.GONE);
                }else {
                    msgoptionview.setVisibility(View.GONE);
                }
                audio_call.setBackgroundResource(R.drawable.chat_send);
                audio_call.setTag(1);
//                relative_send_layout.getLayoutParams().height = 90;


            } else {


                sendMsg(message.getText().toString(), null, "text", spBean);
            }

        } else {
            showAlert1("Info",
                    "Check the Internet connection unable to connect server");
        }

    }

    public static Vector<TaskDetailsBean> getdatelist(Vector<TaskDetailsBean> tasklist) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String inputString2 = dateFormat.format(date);
        String Today = inputString2;
        SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date date1 = null;
        Date date2 = null;
        Vector<TaskDetailsBean> tempList = new Vector<TaskDetailsBean>();
        Vector<TaskDetailsBean> overdue = new Vector<TaskDetailsBean>();
        Vector<TaskDetailsBean> newtoday = new Vector<TaskDetailsBean>();
        Vector<TaskDetailsBean> upcoming = new Vector<TaskDetailsBean>();
        for (TaskDetailsBean bean : tasklist) {
            try {
                Log.d("ppp", "%%%%%%%% assigned members for the task  " + bean.getAssignedMembers());
                date1 = myFormat.parse(Today);
                bean = setDate(bean);
                date2 = myFormat.parse(bean.getDuedate());
                Log.i("xxx", "%%%%%%%% today  " + date1);
                if (date2.compareTo(date1) > 0) {
                    Log.i("xxx", "%%%%%%%% bean date upcoming " + date2);
                    String[] split = bean.getCrtDuetime().split(" ");
                    long hour = Integer.parseInt(split[0]);
                    Log.i("xxx", "hours upcoming " + hour);
                    if (hour != 0 && hour <= 12 && hour > 0) {
                        newtoday.add(bean);
                        bean.setHeadercode("2");
                    }
                    else {
                        upcoming.add(bean);
                        bean.setHeadercode("3");
                    }
                } else if (date2.compareTo(date1) < 0) {
                    Log.i("xxx", "%%%%%%%% bean date overdue " + date2);
                    bean.setHeadercode("1");
                    overdue.add(bean);
                } else if (date2.compareTo(date1) == 0) {
                    Log.i("xxx", "%%%%%%%% bean date today " + date2);
                    newtoday.add(bean);
                    bean.setHeadercode("2");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (overdue.size() > 0) {
            overdue.get(0).setHeader("OVERDUE");
            tempList.addAll(overdue);
        }
        if (newtoday.size() > 0) {
            newtoday.get(0).setHeader("NEW AND TODAY");
            tempList.addAll(newtoday);
        }
        if (upcoming.size() > 0) {
            upcoming.get(0).setHeader("UPCOMING");
            tempList.addAll(upcoming);
        }
        return tempList;

    }

    private static TaskDetailsBean setDate(TaskDetailsBean bean) throws ParseException {
        if (bean.getDuedate() != null) {
            bean.setCrtDuetime(bean.getDuetime());
            Log.i("sss", "Due date");
            long resultDay = 0;
            long resultHours = 0;
            long resultMinutes=0;
            String Duedate = bean.getDuedate();
            String DueTime = bean.getCrtDuetime();
            String DateTime;
            if (DueTime != null)
                DateTime = Duedate + " " + DueTime;
            else
                DateTime = Duedate + " " + "00:00";
            Log.i("ppp", "DateTime :======> " + DateTime);
            Log.i("ppp", "DateTime :======> " + DueTime);

            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy h:mm aa");
            Date date = new Date();
            SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy h:mm aa");
            String inputString1 = DateTime;
            Log.i("sss", "From Date1 : " + inputString1);
            String inputString2 = dateFormat.format(date);
            Log.i("sss", "Current Date1 : " + inputString2);
            Date date1 = null;
            Date date2 = null;
            long diff;
            try {
                date1 = myFormat.parse(inputString1);
                date2 = myFormat.parse(inputString2);
                diff = date1.getTime() - date2.getTime();
                Log.i("sss", "DIFF" + diff);
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);
                System.out.print(diffDays + " days, ");
                System.out.print(diffHours + " hours, ");
                System.out.print(diffMinutes + " minutes, ");
                System.out.print(diffSeconds + " seconds.");
                Log.i("sss", "Total Days : " + diffDays + " Day " + diffHours + " Hours");
                resultDay = diffDays;
                resultHours = diffHours;
                resultMinutes=diffMinutes;
                if (diffHours <= 0 && diffMinutes<=0)
                    bean.setCrtDuetime("0"+" "+"0");
                else if(diffHours>0 && diffMinutes>0)
                    bean.setCrtDuetime(Integer.toString((int) resultHours)+" "+Integer.toString((int) resultMinutes));
                else if(diffHours >0 && diffMinutes<=0)
                    bean.setCrtDuetime(Integer.toString((int) resultHours)+" "+"0");
                else if(diffHours <=0 && diffMinutes>0)
                    bean.setCrtDuetime("0"+" "+Integer.toString((int) resultMinutes));

//                bean.setDuetime(Integer.toString((int) resultHours));

                Log.i("xxxx", "hours adapter" + resultHours);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (resultDay != 0) {
                bean.setDuedate(Duedate);
            } else {
                if (resultHours < 0)
                    bean.setDuedate(Duedate);
                else
                    bean.setCrtDuetime(resultHours +" "+resultMinutes);
            }
        }
        return bean;
    }

    public static Vector<BuddyInformationBean> getAdapterList(Vector<BuddyInformationBean> vectorBean) {
        Vector<BuddyInformationBean> tempList = new Vector<BuddyInformationBean>();
        try {
            String status = null;
            Log.i("AAAA", "group chat getAdapterList ");
            if (ContactsFragment.SortType.equalsIgnoreCase("ONLINE")) {
                Log.i("AAAA", "group chat getAdapterList online ");
                Vector<BuddyInformationBean> newlist = new Vector<BuddyInformationBean>();
                Vector<BuddyInformationBean> onlinelist = new Vector<BuddyInformationBean>();
                Vector<BuddyInformationBean> individualList = new Vector<BuddyInformationBean>();
                Vector<BuddyInformationBean> pending = new Vector<BuddyInformationBean>();
                tempList.clear();
                for (BuddyInformationBean sortlistbean : vectorBean) {
                    sortlistbean.setHeader("NULL");
                    status = sortlistbean.getStatus();
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
                        } else if (status.equalsIgnoreCase("Online")) {
                            onlinelist.add(sortlistbean);
                        } else if (status.equalsIgnoreCase("Pending")) {
                            pending.add(sortlistbean);
                        }
                    }
                }

                Collections.sort(newlist, new BuddyListComparator());
                tempList.addAll(newlist);
                Collections.sort(pending, new BuddyListComparator());
                tempList.addAll(pending);
                Collections.sort(onlinelist, new BuddyListComparator());
                tempList.addAll(onlinelist);

                for (BuddyInformationBean bBean : tempList) {
                    individualList.add(bBean);
                }
                tempList.clear();

                tempList.addAll(individualList);

            } else if (ContactsFragment.SortType.equalsIgnoreCase("ALPH")) {
                Log.i("AAAA", "group chat getAdapterList alpha ");
                Vector<BuddyInformationBean> newlist = new Vector<BuddyInformationBean>();
                Vector<BuddyInformationBean> pending = new Vector<BuddyInformationBean>();
                Vector<BuddyInformationBean> numbers = new Vector<BuddyInformationBean>();
                Vector<BuddyInformationBean> individualList = new Vector<BuddyInformationBean>();
                tempList.clear();
                for (BuddyInformationBean sortlistbean : vectorBean) {
                    sortlistbean.setHeader("NULL");
                    status = sortlistbean.getStatus();
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
                        } else if (status.equalsIgnoreCase("Pending")) {
                            pending.add(sortlistbean);
                        } else
                            numbers.add(sortlistbean);
                    }
                }
                if (newlist.size() > 0) {
                    newlist.get(0).setHeader("REQUEST");
                    tempList.addAll(newlist);
                }
                if (pending.size() > 0) {
                    if (!(newlist.size() > 0)) {
                        pending.get(0).setHeader("REQUEST");
                    }
                    tempList.addAll(pending);
                }
                if (numbers.size() > 0) {
                    Collections.sort(numbers, new BuddyListComparator());
                    if (!ContactsFragment.getInstance(context).isazsort) {
                        Collections.reverse(numbers);
                    }
                    tempList.addAll(numbers);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return tempList;
    }


    private void openFolder() {
        try {
            Intent intent = new Intent(context, FilePicker.class);
            startActivityForResult(intent, 38);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PatientDetails() {
        isUnderPatient = true;
        context = GroupChatActivity.this;
        SingleInstance.contextTable.put("groupchat", context);
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.rounding_patient, content);
        listViewPatient = (ListView) v1.findViewById(R.id.listview_patient);
        final TextView name = (TextView) v1.findViewById(R.id.name);
        final TextView location = (TextView) v1.findViewById(R.id.location);
        final TextView status = (TextView) v1.findViewById(R.id.status_img);
        final TextView mypatients = (TextView) v1.findViewById(R.id.patients);
        final ImageView name_image = (ImageView)v1.findViewById(R.id.name_image);
        name_image.setVisibility(View.VISIBLE);
        final ImageView location_image = (ImageView)v1.findViewById(R.id.location_image);
        location_image.setVisibility(View.GONE);
        final ImageView status_image = (ImageView)v1.findViewById(R.id.status_image);
        status_image.setVisibility(View.GONE);

        search_header = (RelativeLayout) v1.findViewById(R.id.search_header);
        ed_search = (EditText) v1.findViewById(R.id.ed_search);
        ed_search.setVisibility(View.GONE);
        ImageView plusBtn = (ImageView) v1.findViewById(R.id.plusBtn_patient);

        PatientList = new Vector<PatientDetailsBean>();
        PatientList.clear();
        String strGetQry = "select * from patientdetails where groupid='"
                + groupBean.getGroupId() + "'";
        PatientList = DBAccess.getdbHeler().getAllPatientDetails(strGetQry);
        Collections.sort(PatientList, new PatientNameComparator());
        tempPatientList = PatientList;
        Log.i("patientdetails", "chat  filter if " + tempPatientList.size());
        for (PatientDetailsBean bean : PatientList) {
            bean.setIsFromPatienttab(true);
        }
        patientType = "name";
        patientadapter = new RoundingPatientAdapter(context, R.layout.rouding_patient_row, PatientList);
        listViewPatient.setAdapter(patientadapter);
        plusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)||
                        (memberbean.getAdmin()!=null && memberbean.getAdmin().equalsIgnoreCase("1")) ||
                        (rolePatientManagementBean.getAdd() != null && rolePatientManagementBean.getAdd().equalsIgnoreCase("1"))) {
                    Intent intent = new Intent(context, RoundNewPatientActivity.class);
                    intent.putExtra("groupid", groupBean.getGroupId());
                    startActivity(intent);
                }else
                    showToast("You have no access to create patient ");
            }
        });
        ed_search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = ed_search.getText().toString().toLowerCase(Locale.getDefault());
                patientadapter.filter(text);
            }
        });
        listViewPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PatientRoundingFragment patientRoundingFragment = PatientRoundingFragment.newInstance(SingleInstance.mainContext);
                PatientDetailsBean bean = (PatientDetailsBean) patientadapter.getItem(i);
                patientRoundingFragment.setPatientDetailsBean(bean);
                patientRoundingFragment.setGroupBean(groupBean);
                FragmentManager fragmentManager = SingleInstance.mainContext
                        .getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(
                        R.id.activity_main_content_fragment, patientRoundingFragment)
                        .commitAllowingStateLoss();
                WebServiceReferences.webServiceClient.GetPatientDescription(bean.getPatientid(), "", SingleInstance.mainContext);
                WebServiceReferences.webServiceClient.GetPatientComments("", bean.getGroupid(), bean.getPatientid(), SingleInstance.mainContext);
                finish();
            }
        });
        listViewPatient.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int questionsCount;
                if (groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        (memberbean.getAdmin() != null && memberbean.getAdmin().equalsIgnoreCase("1")||
                                (rolePatientManagementBean.getAdd() != null || rolePatientManagementBean.getAdd().equalsIgnoreCase("0")))) {
                    if (patientType.equalsIgnoreCase("mypatient"))
                        questionsCount = DBAccess.getdbHeler().countEntryDetails("select * from patientdetails where groupid='"
                                + groupId + "' and assignedmembers LIKE '%" + CallDispatcher.LoginUser + "%'");
                    else
                        questionsCount = DBAccess.getdbHeler().countEntryDetails("select * from patientdetails where groupid='" + groupId + "' and assignedmembers=''");
                    Log.i("BBB","entry count of mypatient and others**********"+questionsCount);
                    if (questionsCount > 0 ||
                            (rolePatientManagementBean.getAdd() != null && rolePatientManagementBean.getAdd().equalsIgnoreCase("1"))) {
                        Intent intent = new Intent(context, AssignPatientActivity.class);
                        intent.putExtra("groupid", groupId);
                        intent.putExtra("groupname", groupBean.getGroupName());
                        startActivity(intent);
                    } else if(patientType.equals("mypatient"))
                    {
                        Intent intent = new Intent(context, AssignPatientActivity.class);
                        intent.putExtra("groupid", groupId);
                        intent.putExtra("groupname", groupBean.getGroupName());
                        startActivity(intent);
                    }else
                        showToast("Members already assigned to these patient ");
                } else
                    showToast("You have no access to assign members to these patient ");
                return true;
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientType = "name";
                name.setTextColor(getResources().getColor(R.color.white));
                name_image.setVisibility(View.VISIBLE);
                location.setTextColor(getResources().getColor(R.color.snazlgray));
                location_image.setVisibility(View.GONE);
                status.setTextColor(getResources().getColor(R.color.snazlgray));
                status_image.setVisibility(View.GONE);
                mypatients.setTextColor(getResources().getColor(R.color.snazlgray));
                Collections.sort(PatientList, new PatientNameComparator());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        patientadapter = new RoundingPatientAdapter(context, R.layout.rouding_patient_row, PatientList);
                        listViewPatient.setAdapter(null);
                        listViewPatient.setAdapter(patientadapter);
                        patientadapter.notifyDataSetChanged();
                    }
                });
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientType = "loc";
                name.setTextColor(getResources().getColor(R.color.snazlgray));
                name_image.setVisibility(View.GONE);
                location.setTextColor(getResources().getColor(R.color.white));
                location_image.setVisibility(View.VISIBLE);
                status.setTextColor(getResources().getColor(R.color.snazlgray));
                status_image.setVisibility(View.GONE);
                mypatients.setTextColor(getResources().getColor(R.color.snazlgray));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(PatientList, new PatientLocationComparator());
                        patientadapter = new RoundingPatientAdapter(context, R.layout.rouding_patient_row, PatientList);
                        listViewPatient.setAdapter(null);
                        listViewPatient.setAdapter(patientadapter);
                        patientadapter.notifyDataSetChanged();
                    }
                });
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientType = "status";
                status.setTextColor(getResources().getColor(R.color.white));
                status_image.setVisibility(View.VISIBLE);
                location.setTextColor(getResources().getColor(R.color.snazlgray));
                location_image.setVisibility(View.GONE);
                name.setTextColor(getResources().getColor(R.color.snazlgray));
                name_image.setVisibility(View.GONE);
                mypatients.setTextColor(getResources().getColor(R.color.snazlgray));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(PatientList, new PatientStatusComparator());
                        patientadapter = new RoundingPatientAdapter(context, R.layout.rouding_patient_row, PatientList);
                        listViewPatient.setAdapter(null);
                        listViewPatient.setAdapter(patientadapter);
                        patientadapter.notifyDataSetChanged();
                    }
                });
            }
        });

        mypatients.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mypatients.setTextColor(getResources().getColor(R.color.white));
//                patientType = "mypatient";
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        PatientList.clear();
                        String strGetQry;
                        if(mypatient){
                            mypatient=false;
                            patientType = "mypatient";
                            strGetQry = "select * from patientdetails where groupid='"
                                    + groupId + "' and assignedmembers LIKE '%" + CallDispatcher.LoginUser +"%'";
                        }else {
                            mypatient=true;
                            patientType = "name";
                            mypatients.setTextColor(getResources().getColor(R.color.snazlgray));
                            name.setTextColor(getResources().getColor(R.color.white));
                            name_image.setVisibility(View.VISIBLE);
                            strGetQry = "select * from patientdetails where groupid='"
                                    + groupBean.getGroupId() + "'";
                        }
                        PatientList = DBAccess.getdbHeler().getAllPatientDetails(strGetQry);
                        Collections.sort(PatientList, new PatientNameComparator());
//                        patientType = "name";

                        location.setTextColor(getResources().getColor(R.color.snazlgray));
                        location_image.setVisibility(View.GONE);
                        status.setTextColor(getResources().getColor(R.color.snazlgray));
                        status_image.setVisibility(View.GONE);
                        tempPatientList.clear();
                        tempPatientList = PatientList;
                        for (PatientDetailsBean bean : PatientList) {
                            bean.setIsFromPatienttab(true);
                        }
                        patientadapter = new RoundingPatientAdapter(context, R.layout.rouding_patient_row, PatientList);
                        listViewPatient.setAdapter(null);
                        listViewPatient.setAdapter(patientadapter);
                        patientadapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void RoundingMember() {
        setDefault();
        profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members_white));
        tv_profie.setTextColor(getResources().getColor(R.color.white));
        view_profile.setVisibility(View.VISIBLE);
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.roundinmembers, content);
        final LinearLayout list = (LinearLayout) v1.findViewById(R.id.memList);
        final TextView online = (TextView) v1.findViewById(R.id.online_sort);
        final TextView alpha = (TextView) v1.findViewById(R.id.alpha_sort);
        final TextView role = (TextView) v1.findViewById(R.id.role_sort);
        TextView groupname = (TextView) v1.findViewById(R.id.groupname);
        TextView groupDesc = (TextView) v1.findViewById(R.id.groupDesc);
        TextView members_count = (TextView)v1.findViewById(R.id.members_count);
//        members_count.setText(Integer.toString();


//        int member_groupcount = RoundingAdapter.membercount();
        ImageView groupIcon = (ImageView) v1.findViewById(R.id.groupicon);

        final GroupBean gBean = calldisp.getdbHeler(context).getGroupAndMembers("select * from groupdetails where groupid="
                + groupId);
        final Vector<BuddyInformationBean> memberslist = new Vector<BuddyInformationBean>();
        final Vector<BuddyInformationBean> allmemberslist = new Vector<BuddyInformationBean>();
        final BuddyInformationBean ownerbean = new BuddyInformationBean();

        groupname.setText(groupBean.getGroupName());
        groupDesc.setText(gBean.getGroupdescription());
        if (gBean.getGroupIcon() != null) {
            String profilePic = gBean.getGroupIcon();
            if (profilePic != null && profilePic.length() > 0) {
                if (!profilePic.contains("COMMedia")) {
                    profilePic = Environment
                            .getExternalStorageDirectory()
                            + "/COMMedia/" + profilePic;
                    strIPath = profilePic;
                }
                Log.i("AAAA", "MYACCOUNT " + profilePic);
                imageLoader.DisplayImage(profilePic, groupIcon,
                        R.drawable.user_photo);
            }
        }
        if (gBean != null) {
            if (gBean.getActiveGroupMembers() != null
                    && gBean.getActiveGroupMembers().length() > 0) {
                Log.i("AAAA", "RoundingMember++++");
                String[] mlist = (gBean.getActiveGroupMembers())
                        .split(",");
                ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(gBean.getOwnerName());
                if (pBean != null)
                    if (pBean.getTitle()!=null &&(pBean.getTitle().equalsIgnoreCase("Dr.")
                            || pBean.getTitle().equalsIgnoreCase("Prof.")))
                        ownerbean.setFirstname(pBean.getTitle() + pBean.getFirstname());
                    else
                        ownerbean.setFirstname(pBean.getFirstname() + " " + pBean.getLastname());
                if(pBean!=null)
                    if(pBean.getProfession()!=null)
                        ownerbean.setOccupation(pBean.getProfession());
                ownerbean.setLastname(pBean.getLastname());
                ownerbean.setName(gBean.getOwnerName());
                ownerbean.setSelected(true);
                ownerbean.setProfile_picpath(pBean.getPhoto());
                if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser))
                    ownerbean.setStatus(appMainActivity.loadCurrentStatus());
                else
                    for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                        if (bib.getName().equalsIgnoreCase(gBean.getOwnerName())) {
                            ownerbean.setStatus(bib.getStatus());
                            break;
                        }
                    }
                allmemberslist.add(ownerbean);
                for (String tmp : mlist) {
                    BuddyInformationBean uBean = new BuddyInformationBean();
                    ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(tmp);
                    if (pbean != null)
                        if (pbean.getTitle()!=null &&(pbean.getTitle().equalsIgnoreCase("Dr.")
                                || pbean.getTitle().equalsIgnoreCase("Prof.")))
                            uBean.setFirstname(pbean.getTitle() + pbean.getFirstname());
                        else
                            uBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                    if(pbean!=null)
                        if(pbean.getProfession()!=null)
                            uBean.setOccupation(pbean.getProfession());
                    uBean.setLastname(pbean.getLastname());
                    uBean.setName(tmp);
                    uBean.setStatus("offline");
                    uBean.setProfile_picpath(pbean.getPhoto());
                    if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser))
                        uBean.setStatus(appMainActivity.loadCurrentStatus());
                    else
                        for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                            if (bib.getName().equalsIgnoreCase(tmp)) {
                                uBean.setStatus(bib.getStatus());
                                break;
                            }
                        }
                    memberslist.add(uBean);
                }
                for(BuddyInformationBean bib:memberslist){
                    GroupMemberBean gbean=DBAccess.getdbHeler().getMemberDetails(gBean.getGroupId(),bib.getName());
                    bib.setRole(gbean.getRole());
                    bib.setOwner(gbean.getAdmin());
                }
                Collections.sort(memberslist, new BuddyListComparator());
                allmemberslist.addAll(memberslist);
            }
        }
        int count_memberslist = memberslist.size();
        Log.d("memberslist", "value--->" + count_memberslist);
        members_count.setText("(" + Integer.toString(count_memberslist) + ")");
        online.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                online.setTextColor(getResources().getColor(R.color.white));
                alpha.setTextColor(getResources().getColor(R.color.snazlgray));
                role.setTextColor(getResources().getColor(R.color.snazlgray));
                sorting = "online";
                MembersAdapter adapter = new MembersAdapter(context, R.layout.rounding_member_row, getOnlineList(allmemberslist));
                list.removeAllViews();
                final int adapterCount = adapter.getCount();
                for (int i = 0; i < adapterCount; i++) {
                    View item = adapter.getView(i, null, null);
                    list.addView(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
        alpha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alpha.setTextColor(getResources().getColor(R.color.white));
                online.setTextColor(getResources().getColor(R.color.snazlgray));
                role.setTextColor(getResources().getColor(R.color.snazlgray));
                sorting = "alpha";
                Collections.sort(memberslist, new BuddyListComparator());
                if(isatoz) {
                    alpha.setText("A>Z");
                    isatoz = false;
                }
                else {
                    isatoz = true;
                    alpha.setText("Z>A");
                    Collections.reverse(memberslist);
                }
                MembersAdapter adapter = new MembersAdapter(context, R.layout.rounding_member_row, allmemberslist);
                list.removeAllViews();
                final int adapterCount = adapter.getCount();
                for (int i = 0; i < adapterCount; i++) {
                    View item = adapter.getView(i, null, null);
                    list.addView(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
        role.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alpha.setTextColor(getResources().getColor(R.color.snazlgray));
                online.setTextColor(getResources().getColor(R.color.snazlgray));
                role.setTextColor(getResources().getColor(R.color.white));
                sorting = "role";
                Vector<BuddyInformationBean> templist = new Vector<BuddyInformationBean>();
                for (BuddyInformationBean bib : allmemberslist) {
                    if (bib.getRole() != null && !bib.getRole().equalsIgnoreCase(""))
                        templist.add(bib);
                }
                MembersAdapter adapter = new MembersAdapter(context, R.layout.rounding_member_row, templist);
                list.removeAllViews();
                final int adapterCount = adapter.getCount();
                for (int i = 0; i < adapterCount; i++) {
                    View item = adapter.getView(i, null, null);
                    list.addView(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
        MembersAdapter adapter = new MembersAdapter(this, R.layout.rounding_member_row, allmemberslist);
        final int adapterCount = adapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            list.addView(item);
        }
    }

    public void MembersProcess() {
        setDefault();
        profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members_white));
        tv_profie.setTextColor(getResources().getColor(R.color.white));
        view_profile.setVisibility(View.VISIBLE);
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.members_layout, content);
        final ListView list = (ListView) v1.findViewById(R.id.memberslist);
        final TextView online = (TextView) v1.findViewById(R.id.online_sort);
        final TextView alpha = (TextView) v1.findViewById(R.id.alpha_sort);
        final TextView role = (TextView) v1.findViewById(R.id.role_sort);
        role.setVisibility(View.GONE);


        GroupBean gBean = callDisp.getdbHeler(context).getGroupAndMembers(
                "select * from groupdetails where groupid="
                        + groupBean.getGroupId());
        sorting = "online";
        final Vector<BuddyInformationBean> memberslist = new Vector<BuddyInformationBean>();
        final Vector<BuddyInformationBean> allmemberslist = new Vector<BuddyInformationBean>();
        final BuddyInformationBean ownerbean = new BuddyInformationBean();
        if (gBean != null) {
            if (gBean.getActiveGroupMembers() != null
                    && gBean.getActiveGroupMembers().length() > 0) {
                String[] mlist = (gBean.getActiveGroupMembers())
                        .split(",");

                ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(gBean.getOwnerName());

                if (pbean != null)
                    if (pbean.getTitle()!=null &&(pbean.getTitle().equalsIgnoreCase("Dr.") || pbean.getTitle().equalsIgnoreCase("Prof.")))
                        ownerbean.setFirstname(pbean.getTitle() + pbean.getFirstname());
                    else
                        ownerbean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                if(pbean!=null)
                    if(pbean.getProfession()!=null)
                        ownerbean.setOccupation(pbean.getProfession());
                ownerbean.setLastname(pbean.getLastname());
                ownerbean.setSelected(true);
                ownerbean.setName(gBean.getOwnerName());
                ownerbean.setProfile_picpath(pbean.getPhoto());
                if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser))
                    ownerbean.setStatus(appMainActivity.loadCurrentStatus());
                else
                    for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                        if (bib.getName().equalsIgnoreCase(gBean.getOwnerName())) {
                            ownerbean.setStatus(bib.getStatus());
                            break;
                        }
                    }
                ownerbean.setSelected(true);
                allmemberslist.add(ownerbean);
                for (String tmp : mlist) {
                    BuddyInformationBean uBean = new BuddyInformationBean();
                    ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(tmp);
                    if (pBean != null)
                        if (pBean.getTitle()!=null &&(pBean.getTitle().equalsIgnoreCase("Dr.")
                                || pBean.getTitle().equalsIgnoreCase("Prof.")))
                            uBean.setFirstname(pBean.getTitle() + pBean.getFirstname());
                        else
                            uBean.setFirstname(pBean.getFirstname() + " " + pBean.getLastname());
                    if(pBean!=null)
                        if(pBean.getProfession()!=null)
                            uBean.setOccupation(pBean.getProfession());
                    uBean.setLastname(pBean.getLastname());
                    uBean.setName(tmp);
                    uBean.setStatus("offline");
                    uBean.setProfile_picpath(pBean.getPhoto());
                    if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser))
                        uBean.setStatus(appMainActivity.loadCurrentStatus());
                    else
                        for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                            if (bib.getName().equalsIgnoreCase(tmp)) {
                                uBean.setStatus(bib.getStatus());
                                break;
                            }
                        }
                    memberslist.add(uBean);
                }
            }
        }
        Collections.sort(memberslist, new BuddyListComparator());
        allmemberslist.addAll(memberslist);

        online.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                online.setTextColor(getResources().getColor(R.color.white));
                alpha.setTextColor(getResources().getColor(R.color.snazlgray));
                sorting = "online";
                MembersAdapter adapter = new MembersAdapter(context, R.layout.rounding_member_row, getOnlineList(allmemberslist));

                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        alpha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alpha.setTextColor(getResources().getColor(R.color.white));
                online.setTextColor(getResources().getColor(R.color.snazlgray));
                sorting = "alpha";
                Collections.sort(memberslist, new BuddyListComparator());
                if (isatoz) {
                    alpha.setText("A>Z");
                    isatoz = false;
                } else {
                    isatoz = true;
                    alpha.setText("Z>A");
                    Collections.reverse(memberslist);
                }
                allmemberslist.clear();
                allmemberslist.add(ownerbean);
                allmemberslist.addAll(memberslist);
                MembersAdapter adapter = new MembersAdapter(context, R.layout.rounding_member_row, allmemberslist);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        MembersAdapter adapter = new MembersAdapter(this, R.layout.rounding_member_row, allmemberslist);
        list.setAdapter(adapter);
    }

    public class MembersAdapter extends ArrayAdapter<BuddyInformationBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<BuddyInformationBean> result;
        int resourceLayout;

        public MembersAdapter(Context context, int resource, Vector<BuddyInformationBean> objects) {
            super(context, resource, objects);
            resourceLayout=resource;
            imageLoader = new ImageLoader(context);
            result = new Vector<BuddyInformationBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if (convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(resourceLayout, null);
                    holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.role = (TextView) convertView.findViewById(R.id.position);
                    holder.rights = (TextView) convertView.findViewById(R.id.rights);
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder) convertView.getTag();
                final BuddyInformationBean bib = result.get(i);
                if (bib != null) {
                    if (bib.getProfile_picpath() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/COMMedia/" + bib.getProfile_picpath();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.edit.setVisibility(View.GONE);
                    if (bib.getStatus() != null) {
                        Log.i("AAAA", "Buddy adapter status " + bib.getStatus());
                        if (bib.getStatus().equalsIgnoreCase("offline") || bib.getStatus().equalsIgnoreCase("stealth")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                        } else if (bib.getStatus().equalsIgnoreCase("online")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.online_icon);
                        } else if (bib.getStatus().equalsIgnoreCase("busy") || bib.getStatus().equalsIgnoreCase("airport")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.busy_icon);
                        } else if (bib.getStatus().equalsIgnoreCase("away")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.invisibleicon);
                        } else {
                            holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                        }
                    }
                    holder.header_title.setVisibility(View.GONE);
                    holder.selectUser.setVisibility(View.GONE);
                    if(bib.getFirstname()!=null && bib.getFirstname().length()>1)
                    holder.buddyName.setText(bib.getFirstname());
                    else
                        holder.buddyName.setText(bib.getName());
                    if (bib.getOccupation() != null)
                        holder.occupation.setText(bib.getOccupation());
                    if (bib.getRole() != null)
                        holder.role.setText(bib.getRole());
                    else
                        holder.role.setVisibility(View.GONE);
                    if (bib.isSelected()) {
                        holder.rights.setText("Owner");
                        holder.rights.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        if (bib.getOwner() != null) {
                            if (bib.getOwner().equalsIgnoreCase("1"))
                                holder.rights.setVisibility(View.VISIBLE);
                            else
                                holder.rights.setVisibility(View.GONE);
                        } else
                            holder.rights.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    public class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon,edit;
        ImageView statusIcon;
        TextView buddyName;
        TextView occupation,role,rights;
        TextView header_title;
    }

    private void GroupInfo() {
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.group_info, content);
        TextView groupname = (TextView) v1.findViewById(R.id.groupname);
        TextView groupdesc = (TextView) v1.findViewById(R.id.groupdesc);
        TextView createddate = (TextView) v1.findViewById(R.id.createddate);
        TextView members = (TextView) v1.findViewById(R.id.members);
        TextView owner = (TextView) v1.findViewById(R.id.owner);
        ImageView group_img = (ImageView) v1.findViewById(R.id.riv1);
        GroupBean gBean = callDisp.getdbHeler(context).getGroupAndMembers(
                "select * from groupdetails where groupid=" + groupBean.getGroupId());
        groupname.setText(groupBean.getGroupName());
        groupdesc.setText(gBean.getGroupdescription());
        String dateasOnNow = gBean.getCreatedDate();
        String[] date;
        String delimiter = " ";
        date = dateasOnNow.split(delimiter);
        Log.i("asdf", "Created Date after" + date[0]);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date d1 = null;
        try {
            d1 = dateformat.parse(date[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newdate = sdf.format(d1);
        createddate.setText(newdate);
        String[] mlist = (gBean.getActiveGroupMembers())
                .split(",");
        membercount = mlist.length + 1;
        members.setText(Integer.toString(membercount));
        Log.d("Members group","value---------->"+ membercount);
        ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(gBean.getOwnerName());
        if (pbean != null)
            owner.setText(pbean.getFirstname() + " " + pbean.getLastname());

        if (gBean.getGroupIcon() != null) {
            String profilePic = groupBean.getGroupIcon();
            if (profilePic != null && profilePic.length() > 0) {
                if (!profilePic.contains("COMMedia")) {
                    profilePic = Environment
                            .getExternalStorageDirectory()
                            + "/COMMedia/" + profilePic;
                    strIPath = profilePic;
                }
                imageLoader.DisplayImage(profilePic, group_img,
                        R.drawable.user_photo);
            }
        }
    }

    private void TaskProcess() {
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.round_task, content);
        tv_status = (TextView) v1.findViewById(R.id.tv_status);
        tv_assigned = (TextView) v1.findViewById(R.id.tv_assigned);
        LinearLayout status = (LinearLayout) v1.findViewById(R.id.status);
        LinearLayout assigned = (LinearLayout) v1.findViewById(R.id.assigned);
        ImageView plusBtn = (ImageView) v1.findViewById(R.id.plusBtn);
        tasklistView = (ListView) v1.findViewById(R.id.listview_task);
        statusMode = "ALL";
        assignedMode = "ALL";
        strQuery = "select * from taskdetails where groupid='" + groupBean.getGroupId() + "'";
        status.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                statusDialog();
            }
        });
        assigned.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedTaskDialog();
            }
        });
        plusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)||
                        (memberbean.getAdmin()!=null && memberbean.getAdmin().equalsIgnoreCase("1")) ||
                        (roleAccessBean.getTaskmanagement()!=null && roleAccessBean.getTaskmanagement().equalsIgnoreCase("1"))) {
                    Intent intent = new Intent(context, TaskCreationActivity.class);
                    intent.putExtra("groupid", groupBean.getGroupId());
                    startActivity(intent);
                }else
                    showToast("You have no access to create Task ");
            }
        });
        Vector<TaskDetailsBean> tasklist = DBAccess.getdbHeler().getAllTaskDetails(strQuery);
        Collections.sort(tasklist, new TaskDateComparator());
        Vector<TaskDetailsBean> taskList = getdatelist(tasklist);
        taskAdapter = new RoundingTaskAdapter(context, R.layout.round_task_row, taskList);
        tasklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskDetailsBean tBean = (TaskDetailsBean) taskAdapter.getItem(i);
                if ((groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        roleAccessBean.getTaskmanagement() != null && roleAccessBean.getTaskmanagement().equalsIgnoreCase("1"))&&
                        tBean.getTaskstatus().equalsIgnoreCase("0") ) {
                    Intent intent = new Intent(context, TaskCreationActivity.class);
                    intent.putExtra("groupid", tBean.getGroupid());
                    intent.putExtra("taskid", tBean.getTaskId());
                    intent.putExtra("isEdit", true);
                    SingleInstance.mainContext.startActivity(intent);
                }
            }
        });
        tasklistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskDetailsBean tBean = (TaskDetailsBean) taskAdapter.getItem(i);
                deleteTask(tBean.getTaskId(), tBean.getGroupid(), tBean.getTaskdesc());
                return true;
            }
        });
        taskSorting(groupBean.getGroupId());
    }
    public void countofcheckbox(int count)
    {
        Log.i("asdf", "count" + count);
        countofselection.setText(Integer.toString(count) + " Selected");
        if(count==chatList.size())
            selectAll_buddy.setChecked(true);
        else
            selectAll_buddy.setChecked(false);

    }

    private void setDefault() {
        if (!isGroup && !isRounding) {
            profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_profile));
            file_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_snazbox));
            links_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_links));
        } else if (isRounding) {
            profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members));
            file_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_patients));
            links_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_tasks));
            info_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_calendar));
        } else if (isGroup) {
            profile_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members));
            info_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_info));
            file_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_snazbox));
            links_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_links));
        }
        chat_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat));
        tv_profie.setTextColor(getResources().getColor(R.color.black));
        tv_chat.setTextColor(getResources().getColor(R.color.black));
        tv_file.setTextColor(getResources().getColor(R.color.black));
        tv_info.setTextColor(getResources().getColor(R.color.black));
        tv_links.setTextColor(getResources().getColor(R.color.black));
        view_profile.setVisibility(View.GONE);
        view_info.setVisibility(View.GONE);
        view_snazbox.setVisibility(View.GONE);
        view_links.setVisibility(View.GONE);
        view_chat.setVisibility(View.GONE);
    }

    int jumpTime = 0;
    Timer tm;

    private void startRecording(String mFileName) {
        final int totalProgressTime = 100;
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setMaxDuration(60000);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        Log.i("jumpTime", "jumpTime value " + jumpTime);
        try {
            mRecorder.prepare();
//            progressBar1.getProgressDrawable().setColorFilter(getResources().getColor(R.color.blue2), PorterDuff.Mode.SRC_IN);
            progressBar1.setProgress(0);
            progressBar1.setMax(60);
            tm = new Timer();
            tm.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    jumpTime += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar1.setProgress(jumpTime);
                        }
                    });

                }
            }, 1000, 1000);

        } catch (IOException e) {
        }

        mRecorder.start();
    }

    private void stopRecording() {
        try {
            audio_layout.setVisibility(View.GONE);
            btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
            if (mRecorder != null) {
                progressBar1.setProgress(0);
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                jumpTime = 0;
                tm.cancel();
                tm = null;
                Log.i("jumpTime", "jumpTime value stop " + jumpTime);
                if (strIPath != null) {
                    File fileCheck = new File(strIPath);
                    if (fileCheck.exists()) {
//                        message.setVisibility(View.GONE);
//                        ad_play.setVisibility(View.VISIBLE);
                        SendListUIBean uIbean = new SendListUIBean();
                        uIbean.setType("audio");
                        uIbean.setPath(strIPath);
                        uIbean.setUser(buddy);
                        SendListUI.add(uIbean);

                        if(!current_open_activity_detail.containsKey("audio")) {
                            current_open_activity_detail.put("audio", uIbean);
                        }else if(!current_open_activity_detail.containsKey("audio1")){
                            current_open_activity_detail.put("audio1", uIbean);
                        }else if(!current_open_activity_detail.containsKey("audio2")){
                            current_open_activity_detail.put("audio2", uIbean);
                        }

                        sendlistadapter.notifyDataSetChanged();
                        list_all.removeAllViews();
                        final int adapterCount = sendlistadapter.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            View item = sendlistadapter.getView(i, null, null);
                            list_all.addView(item);
                        }
                        if(adapterCount>=2){
                            multi_send.getLayoutParams().height=280;
                        }
                        if(isPrivateBack){
                            LL_privateReply.setVisibility(View.VISIBLE);
                        }else {
                            msgoptionview.setVisibility(View.VISIBLE);
                        }
                        audio_call.setBackgroundResource(R.drawable.chat_send);
                        audio_call.setTag(1);
//                        relative_send_layout.getLayoutParams().height = 400;
                        try {
                            mediaPlayer.setDataSource(strIPath);
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                            mediaPlayer.prepare();
                            double sTime = mediaPlayer.getDuration();
                            String min, sec;
                            min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime));
                            sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)));
                            if (Integer.parseInt(min) < 10) {
                                min = 0 + String.valueOf(min);
                            }
                            if (Integer.parseInt(sec) < 10) {
                                sec = 0 + String.valueOf(sec);
                            }
//                            txt_time.setText(min + ":" + sec);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // sendMsg("", strIPath, "image", null);
//                        Intent pMsgIntent = new Intent(context,
//                                PrivateMessageActivity.class);
//                        pMsgIntent.putExtra("groupid", groupId);
//                        pMsgIntent.putExtra("type", "audio");
//                        pMsgIntent.putExtra("localpath", strIPath);
//                        pMsgIntent.putExtra("replyback", isReplyBack);
//                        pMsgIntent.putExtra("pMembers", privateMembers);
//                        pMsgIntent.putExtra("buddyname", buddy);
//                        pMsgIntent.putExtra("parentid", parentId);
//                        startActivity(pMsgIntent);
                    } else {
                        showToast("Not able to process. Please try again");
                    }
                }
            }
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
//            if (mRecorder != null) {
//                mRecorder.stop();
//                mRecorder.release();
//                mRecorder = null;
//            }
        }
    }

    private void playAudio(String file_name) {
        try {
            if (file_name != null) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

//        seekBar.setMax((int) finalTime);
//        seekBar.setProgress((int) startTime);
        history_handler.postDelayed(UpdateSongTime, 100);
    }

    public void stopPlayer() {
        message.setVisibility(View.VISIBLE);
//        ad_play.setVisibility(View.GONE);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();


            }
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
//            seekBar.setProgress((int) startTime);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    startPlayProgressUpdater();

                }
            }, 100);

            history_handler.postDelayed(this, 100);
        }
    };


    private void startPlayProgressUpdater() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                Log.d("lg", "play progress().....");
                long milliseconds = mediaPlayer.getCurrentPosition();
                timeElapsed = mediaPlayer.getCurrentPosition();

                double timeRemaining = finalTime - timeElapsed;
                String min, sec;
                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining));
                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)));

                if (Integer.parseInt(min) < 10) {
                    min = 0 + String.valueOf(min);
                }
                if (Integer.parseInt(sec) < 10) {
                    sec = 0 + String.valueOf(sec);
                }
//                txt_time.setText(min + ":" + sec);
            }
        }
    }

    private SwipeMenuCreator sendSwipeList() {
        try {
            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem openItem = new SwipeMenuItem(context);
                    // set item background
                    openItem.setBackground(R.color.grey1);
                    // set item width
                    openItem.setWidth(dp2px(90));
                    // set item title
                    openItem.setIcon(R.drawable.withdraw_line_white);
                    // set item title fontsize
                    openItem.setTitleSize(18);
                    // set item title font color
                    openItem.setTitleColor(Color.WHITE);
                    // add to menu
                    final GroupChatBean gcBean = chatList.get(1);
                    if (gcBean.getFrom().equals(CallDispatcher.LoginUser)) {
//                        menu.addMenuItem(openItem);
                    }

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                    // set item background
//                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                            0x3F, 0x25)));
                    deleteItem.setBackground(R.color.green);
                    // set item width
                    deleteItem.setWidth(dp2px(90));

                    // set a icon
                    deleteItem.setIcon(R.drawable.info_line_white);
                    // add to menu
                    if (gcBean.getFrom().equals(CallDispatcher.LoginUser)) {
//                        menu.addMenuItem(deleteItem);
                    }

                    // create "open" item
                    SwipeMenuItem forwared = new SwipeMenuItem(context);
                    // set item background
                    forwared.setBackground(R.color.blue2);
                    // set item width
                    forwared.setWidth(dp2px(90));
                    // set item title
                    forwared.setIcon(R.drawable.forward_line_white);
                    // set item title fontsize
                    forwared.setTitleSize(18);
                    // set item title font color
                    forwared.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(forwared);
                }
            };
            return creator;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    private SwipeMenuCreator intiateSwipeList() {
        try {
            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem openItem = new SwipeMenuItem(context);
                    // set item background
                    openItem.setBackground(R.color.grey1);
                    // set item width
                    openItem.setWidth(dp2px(90));
                    // set item title
                    openItem.setIcon(R.drawable.withdraw_line_white);
                    // set item title fontsize
                    openItem.setTitleSize(18);
                    // set item title font color
                    openItem.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(openItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                    // set item background
//                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                            0x3F, 0x25)));
                    deleteItem.setBackground(R.color.green);
                    // set item width
                    deleteItem.setWidth(dp2px(90));

                    // set a icon
                    deleteItem.setIcon(R.drawable.info_line_white);
                    // add to menu
                    menu.addMenuItem(deleteItem);

                    // create "open" item
                    SwipeMenuItem forwared = new SwipeMenuItem(context);
                    // set item background
                    forwared.setBackground(R.color.blue2);
                    // set item width
                    forwared.setWidth(dp2px(90));
                    // set item title
                    forwared.setIcon(R.drawable.forward_line_white);
                    // set item title fontsize
                    forwared.setTitleSize(18);
                    // set item title font color
                    forwared.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(forwared);
                }
            };
            return creator;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private final MediaPlayer mPlayer = new MediaPlayer();
    private int mPlayingPosition = -1;
    private Handler mHandler = new Handler();

    private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();

    private class PlaybackUpdater implements Runnable {
        public SeekBar mBarToUpdate = null;
        public TextView tvToUpdate = null;

        @Override
        public void run() {
            if ((mPlayingPosition != -1) && (null != mBarToUpdate)) {
                double tElapsed = mPlayer.getCurrentPosition();
                int fTime = mPlayer.getDuration();
                double timeRemaining = fTime - tElapsed;
                double sTime = mPlayer.getCurrentPosition();

                String min, sec;

                //for decreasing
//                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining));
//                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)));

                //for increasing
                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime));
                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)));
                if (Integer.parseInt(min) < 10) {
                    min = 0 + String.valueOf(min);
                }
                if (Integer.parseInt(sec) < 10) {
                    sec = 0 + String.valueOf(sec);
                }
                tvToUpdate.setText(min + ":" + sec);
                int result= 100 * mPlayer.getCurrentPosition() / mPlayer.getDuration();
                Log.i("valueof","intvalue"+result);

                if(result>=99){
                    mProgressUpdater.mBarToUpdate.setProgress(0);
                }else{
                    mBarToUpdate.setProgress(result);
                }

//                tvToUpdate.setText(String.format("%d:%d ",TimeUnit.MILLISECONDS.toMinutes((long) fTime),TimeUnit.MILLISECONDS.toSeconds((long) fTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) fTime))));
                mHandler.postDelayed(this, 100);

            } else {
                //not playing so stop updating
            }
        }
    }

    private void stopPlayback() {
        mPlayingPosition = -1;
        mProgressUpdater.mBarToUpdate = null;
        mProgressUpdater.tvToUpdate = null;
        Log.i("hellowrld","stopplayback");
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
//            mHandler.postDelayed(mProgressUpdater, 100);
        }
    }

    private void playAudio(String fname, int position) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(fname);
            mPlayer.prepare();
            mPlayer.start();
            mPlayingPosition = position;

            mHandler.postDelayed(mProgressUpdater, 500);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    mHandler.postDelayed(mProgressUpdater, 100);
                    finalPlayFile = "";
                    finalPlayBean.setPlaying(false);
                    adapter.notifyDataSetChanged();
                }
            });

            //trigger list refresh, this will make progressbar start updating if visible
            adapter.notifyDataSetChanged();
        } catch (IOException e) {

            e.printStackTrace();
            stopPlayback();
        }
    }

    private void forwardChat(String member,Boolean fromgroup) {
        for (int i = 0; i < chatList.size(); i++) {
            final GroupChatBean b = chatList.get(i);
            if (b.isSelect()) {
                sendForwardMsg(b.getMessage(), b.getMediaName(), b.getMimetype(), null, member, fromgroup);
            }
        }

    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public  void tempSendList(int position) {
        SendListUI.remove(position);
        sendlistadapter.notifyDataSetChanged();
        list_all.removeAllViews();
        final int adapterCount = sendlistadapter.getCount();
        list_all.removeAllViews();
        for (int i = 0; i < adapterCount; i++) {
            View item = sendlistadapter.getView(i, null, null);
            list_all.addView(item);
        }
        if(adapterCount>=2){
            multi_send.getLayoutParams().height=280;
        }
        if (SendListUI.size() == 0) {
            SendListUI.clear();
            if(isPrivateBack){
                LL_privateReply.setVisibility(View.GONE);
            }else {
                msgoptionview.setVisibility(View.GONE);
            }
            audio_call.setBackgroundResource(R.drawable.dashboard_call_white);
            audio_call.setTag(0);
            sendlistadapter.notifyDataSetChanged();
            list_all.removeAllViews();
            final int adapterCount1 = sendlistadapter.getCount();

            for (int i = 0; i < adapterCount1; i++) {
                View item = sendlistadapter.getView(i, null, null);
                list_all.addView(item);
            }
            if(adapterCount>=2){
                multi_send.getLayoutParams().height=280;
            }
//            relative_send_layout.getLayoutParams().height = 90;
        }
    }

    public void statusDialog() {
        final Dialog dialog1 = new Dialog(GroupChatActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.task_status);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
        final RadioButton showall = (RadioButton) dialog1.findViewById(R.id.showall);
        final RadioButton active = (RadioButton) dialog1.findViewById(R.id.active);
        final RadioButton completed = (RadioButton) dialog1.findViewById(R.id.completed);
        Button apply = (Button) dialog1.findViewById(R.id.apply);
        RadioGroup gender = (RadioGroup) dialog1.findViewById(R.id.status_group);
        int selectedId = gender.getCheckedRadioButtonId();
        final RadioButton statusSelected = (RadioButton) dialog1.findViewById(selectedId);
        Button cancel = (Button) dialog1.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        if (statusMode.equalsIgnoreCase("ALL")) {
            showall.setChecked(true);
        } else if (statusMode.equalsIgnoreCase("active"))
            active.setChecked(true);
        else
            completed.setChecked(true);
        showall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                statusMode = "ALL";
                showall.setTextColor(Color.parseColor("#458EDB"));
                active.setTextColor(Color.parseColor("#ffffff"));
                completed.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        active.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                statusMode = "Active";
                active.setTextColor(Color.parseColor("#458EDB"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                completed.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        completed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                statusMode = "Completed";
                completed.setTextColor(Color.parseColor("#458EDB"));
                active.setTextColor(Color.parseColor("#ffffff"));
                showall.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        dialog1.show();
        apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                taskSorting(groupBean.getGroupId());
                dialog1.dismiss();
                tv_status.setText(statusMode.toUpperCase());
            }
        });
    }

    public void assignedTaskDialog() {
        final Dialog dialog1 = new Dialog(GroupChatActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.task_assigned);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
        final RadioButton showall = (RadioButton) dialog1.findViewById(R.id.showall);
        final RadioButton assignedtome = (RadioButton) dialog1.findViewById(R.id.assignedtome);
        final RadioButton assignedbyme = (RadioButton) dialog1.findViewById(R.id.assignedbyme);
        final RadioButton unassigned = (RadioButton) dialog1.findViewById(R.id.unassigned);
        final RadioButton assigntoteam = (RadioButton) dialog1.findViewById(R.id.assigntoteam);
        Button apply = (Button) dialog1.findViewById(R.id.apply);
        LinearLayout members_list = (LinearLayout) dialog1.findViewById(R.id.members_list);
        RadioGroup gender = (RadioGroup) dialog1.findViewById(R.id.assigned_group);
        Button cancel = (Button) dialog1.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        if (assignedMode.equalsIgnoreCase("ALL")) {
            showall.setChecked(true);
        } else if (assignedMode.equalsIgnoreCase("Assignedtome"))
            assignedtome.setChecked(true);
        else if (assignedMode.equalsIgnoreCase("Assignedbyme"))
            assignedbyme.setChecked(true);
        else if (assignedMode.equalsIgnoreCase("Unassigned"))
            unassigned.setChecked(true);
        else if (assignedMode.equalsIgnoreCase("Assigntoteam"))
            assigntoteam.setChecked(true);
        showall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "ALL";
                showall.setTextColor(Color.parseColor("#458EDB"));
                assignedtome.setTextColor(Color.parseColor("#ffffff"));
                assignedbyme.setTextColor(Color.parseColor("#ffffff"));
                unassigned.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        assignedtome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "Assignedtome";
                assignedtome.setTextColor(Color.parseColor("#458EDB"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                assignedbyme.setTextColor(Color.parseColor("#ffffff"));
                unassigned.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        assignedbyme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "Assignedbyme";
                assignedbyme.setTextColor(Color.parseColor("#458EDB"));
                assignedtome.setTextColor(Color.parseColor("#ffffff"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                unassigned.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        unassigned.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "Unassigned";
                unassigned.setTextColor(Color.parseColor("#458EDB"));
                assignedtome.setTextColor(Color.parseColor("#ffffff"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                assignedbyme.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        assigntoteam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "Assigntoteam";
                unassigned.setTextColor(Color.parseColor("#ffffff"));
                assignedtome.setTextColor(Color.parseColor("#ffffff"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                assignedbyme.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#458EDB"));
            }
        });
        GroupBean gBean = calldisp.getdbHeler(context).getGroupAndMembers("select * from groupdetails where groupid="
                + groupId);
        final Vector<UserBean> memberslist = new Vector<UserBean>();
        if (gBean != null) {
            if (gBean.getActiveGroupMembers() != null
                    && gBean.getActiveGroupMembers().length() > 0) {
                String[] mlist = (gBean.getActiveGroupMembers())
                        .split(",");
                UserBean bean = new UserBean();
                if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                    ProfileBean ubean = SingleInstance.myAccountBean;
                    bean.setFirstname(ubean.getFirstname() + " " + ubean.getLastname());
                } else {
                    for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                        if (bib.getName().equalsIgnoreCase(gBean.getOwnerName())) {
                            bean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                            break;
                        } else
                            bean.setFirstname(gBean.getOwnerName());
                    }
                }
                bean.setBuddyName(gBean.getOwnerName());
                bean.setFlag("0");
                memberslist.add(bean);
                for (String tmp : mlist) {
                    UserBean uBean = new UserBean();
                    for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                        if (bib.getName().equalsIgnoreCase(tmp)) {
                            uBean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                            break;
                        } else
                            uBean.setFirstname(tmp);
                    }
                    uBean.setBuddyName(tmp);
                    uBean.setFlag("0");
                    memberslist.add(uBean);
                }
            }
        }
        TeamMembersAdapter adapter = new TeamMembersAdapter(this, R.layout.rounding_member_row, memberslist);
        final int adapterCount = adapter.getCount();
        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            members_list.addView(item);
        }
        apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                taskSorting(groupBean.getGroupId());
                dialog1.dismiss();
                if (assignedMode.equalsIgnoreCase("showall")) {
                    tv_assigned.setText(" ALL");
                } else if (assignedMode.equalsIgnoreCase("assignedtome")) {
                    tv_assigned.setText(" TO ME");
                } else if (assignedMode.equalsIgnoreCase("assignedbyme")) {
                    tv_assigned.setText(" BY ME");
                } else if (assignedMode.equalsIgnoreCase("unassigned")) {
                    tv_assigned.setText(" UNASSIGNED");
                } else if (assignedMode.equalsIgnoreCase("assigntoteam")) {
                    tv_assigned.setText(" TO " +adapterCount+" MEMBERS" );
                }
//                tv_assigned.setText(assignedMode);

                String addMembers = "";

                for (UserBean bean : memberslist) {
                    if (bean.isSelected())
                        addMembers = addMembers + "" + bean.getBuddyName() + ",";
                }
                SelectedtaskMember=addMembers;
                Log.i("patientdetails", "%%%%%%%%********Memberslisted" +SelectedtaskMember);

            }
        });
        dialog1.show();
    }


    public void taskSorting(String groupid) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String inputString2 = dateFormat.format(date);
        String Today = inputString2;

        if (assignedMode.equalsIgnoreCase("ALL") && statusMode.equalsIgnoreCase("ALL"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'";
        else if (assignedMode.equalsIgnoreCase("ALL") && statusMode.equalsIgnoreCase("Active"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "0" + "'";
        else if (assignedMode.equalsIgnoreCase("ALL") && statusMode.equalsIgnoreCase("Completed"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" + "'";
        else if (assignedMode.equalsIgnoreCase("Assignedtome") && statusMode.equalsIgnoreCase("ALL"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and assignmembers LIKE '%"
                    + CallDispatcher.LoginUser + "%'";
        else if (assignedMode.equalsIgnoreCase("Assignedtome") && statusMode.equalsIgnoreCase("Active"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and duedate >'" + Today +
                    "'and assignmembers LIKE '%" + CallDispatcher.LoginUser + "%'";
        else if (assignedMode.equalsIgnoreCase("Assignedtome") && statusMode.equalsIgnoreCase("Completed"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" +
                    "'and assignmembers LIKE '%" + CallDispatcher.LoginUser + "%'";
        else if (assignedMode.equalsIgnoreCase("Assignedbyme") && statusMode.equalsIgnoreCase("ALL"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and creatorname='"
                    + CallDispatcher.LoginUser + "'";
        else if (assignedMode.equalsIgnoreCase("Assignedbyme") && statusMode.equalsIgnoreCase("Active"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "0" +
                    "'and creatorname='" + CallDispatcher.LoginUser + "'";
        else if (assignedMode.equalsIgnoreCase("Assignedbyme") && statusMode.equalsIgnoreCase("Completed"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" +
                    "'and creatorname='" + CallDispatcher.LoginUser + "'";
        else if (assignedMode.equalsIgnoreCase("Unassigned") && statusMode.equalsIgnoreCase("ALL"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and assignmembers ='" + "" + "'";
        else if (assignedMode.equalsIgnoreCase("Unassigned") && statusMode.equalsIgnoreCase("Active"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "0" +
                    "'and assignmembers ='" + "" + "'";
        else if (assignedMode.equalsIgnoreCase("Unassigned") && statusMode.equalsIgnoreCase("Completed"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" +
                    "'and assignmembers ='" + "" + "'";

        else if (assignedMode.equalsIgnoreCase("Assigntoteam") && statusMode.equalsIgnoreCase("ALL")){
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and assignmembers  NOT LIKE '%" + "" + "%'";
        Log.i("patientdetails", "team +show all======> " + statusMode + " query " + strQuery);}

        else if (assignedMode.equalsIgnoreCase("Assigntoteam") && statusMode.equalsIgnoreCase("Active")){
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "0" +
                     "'and assignmembers NOT LIKE '%" + "" + "%'";
        Log.i("patientdetails", "team +active " + statusMode + " query " + strQuery);}

        else if (assignedMode.equalsIgnoreCase("Assigntoteam") && statusMode.equalsIgnoreCase("Completed")){
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" +
                    "'and assignmembers NOT LIKE '%" + "" + "%'";
        Log.i("patientdetails", "team +completed " + statusMode + " query " + strQuery);}

        Log.i("patientdetails", "statusDialog 1" + statusMode + " query " + strQuery);
        Vector<TaskDetailsBean> tasklist = DBAccess.getdbHeler().getAllTaskDetails(strQuery);
        Collections.sort(tasklist, new TaskDateComparator());
        Vector<TaskDetailsBean> taskList = getdatelist(tasklist);
        taskAdapter = new RoundingTaskAdapter(context, R.layout.round_task_row, taskList);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(tasklistView!=null) {
                    tasklistView.setAdapter(taskAdapter);
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });
//        tasklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TaskDetailsBean tBean = (TaskDetailsBean) taskAdapter.getItem(i);
//                Intent intent = new Intent(context, TaskCreationActivity.class);
//                intent.putExtra("groupid", tBean.getGroupid());
//                intent.putExtra("taskid", tBean.getTaskId());
//                intent.putExtra("isEdit", true);
//                SingleInstance.mainContext.startActivity(intent);
//            }
//        });
//        tasklistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TaskDetailsBean tBean = (TaskDetailsBean) taskAdapter.getItem(i);
//                deleteTask(tBean.getTaskId(), tBean.getGroupid(), tBean.getTaskdesc());
//                return true;
//            }
//        });
    }

    public class TeamMembersAdapter extends ArrayAdapter<UserBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<UserBean> result;
        Boolean isClicked = false;

        public TeamMembersAdapter(Context context, int resource, Vector<UserBean> objects) {
            super(context, resource, objects);
            imageLoader = new ImageLoader(context);
            result = new Vector<UserBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                final ViewHolder1 holder;
                if (convertView == null) {
                    holder = new ViewHolder1();
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.rounding_member_row, null);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.overlay = (ImageView) convertView.findViewById(R.id.overlay);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.role = (TextView) convertView.findViewById(R.id.position);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.rights = (TextView) convertView.findViewById(R.id.rights);
                    holder.member_lay = (LinearLayout) convertView.findViewById(R.id.member_lay);
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder1) convertView.getTag();
                final UserBean bib = result.get(i);
                holder.edit.setVisibility(View.GONE);
                holder.rights.setVisibility(View.GONE);
                holder.statusIcon.setVisibility(View.GONE);
                if (bib != null) {
                    if (bib.getProfilePic() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" + bib.getProfilePic();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.GONE);
                    holder.buddyName.setText(bib.getFirstname());
                    if (bib.getOccupation() != null)
                        holder.occupation.setText(bib.getOccupation());
                    if (bib.getRole() != null) {
                        holder.role.setText(bib.getRole());
                    }
                    holder.member_lay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (bib.getFlag().equalsIgnoreCase("0")) {
                                bib.setSelected(true);
                                holder.overlay.setVisibility(View.VISIBLE);
                                bib.setFlag("1");
                            } else {
                                bib.setSelected(false);
                                holder.overlay.setVisibility(View.GONE);
                                bib.setFlag("0");
                            }
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("RRRR", "Error FindpeopleAdapter.java => " + e.toString());
            }
            return convertView;
        }
    }

    public class ViewHolder1 {
        CheckBox selectUser;
        ImageView buddyicon, overlay;
        ImageView statusIcon, edit;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
        LinearLayout member_lay;
        TextView rights, role;
    }

    public void deleteTask(final String taskid, final String groupid, String taskname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning !");
        builder.setMessage("Are you sure you want to delete "
                + taskname + " ?").setCancelable(false).setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!WebServiceReferences.running) {
                            calldisp.startWebService(getResources()
                                    .getString(R.string.service_url), "80");
                        }
                        showprogress();
                        WebServiceReferences.webServiceClient.DeleteTask(CallDispatcher.LoginUser, taskid,
                                groupid, context);

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public void notifyDeletetask(Object obj) {
        cancelDialog();
        Log.i("patientdetails", "notifyDeletetask");
        if (obj instanceof String[]) {
            String[] result = (String[]) obj;
            if (result[0].equalsIgnoreCase("Task removed Successfully")) {
                String strQuery = null;
                if (result[2].length() > 0)
                    strQuery = "DELETE from taskdetails WHERE groupid='" + result[1] + "'and taskid='" + result[2] + "'";
                else
                    strQuery = "DELETE from taskdetails WHERE groupid='" + result[1] + "'";
                DBAccess.getdbHeler().deleteTaskDetails(strQuery);
                taskSorting(result[1]);
            }
        } else if (obj instanceof WebServiceBean)
            showToast(((WebServiceBean) obj).getText());

    }

    private void showDialogCall() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.broadcastcall_select_chatui);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
        dialog.show();
        RelativeLayout audio_broadcast = (RelativeLayout) dialog.findViewById(R.id.audio_broadcast);
        RelativeLayout video_broadcast = (RelativeLayout) dialog.findViewById(R.id.video_broadcast);
        TextView cancel_tv = (TextView) dialog.findViewById(R.id.cancel_tv);
        audio_broadcast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                individualCallMenu(4);
                dialog.dismiss();
            }
        });
        video_broadcast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                individualCallMenu(5);
                dialog.dismiss();
            }
        });
        cancel_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void sendSplMsg() {
        memlist_splmsg.setVisibility(View.GONE);
        if(isPrivateBack){
            LL_privateReply.setVisibility(View.GONE);
        }else {
            msgoptionview.setVisibility(View.GONE);
        }
        String members = null;


        if (isGroup || isRounding) {
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
                members = selectedMembers;

            }
        }
        if (!isprivateclicked) {
            if (isGroup || isRounding) {
                String selectedMembers = "";
                for (UserBean userBean : membersList) {
                    if (!userBean.getBuddyName().equalsIgnoreCase(
                            CallDispatcher.LoginUser)) {

                        selectedMembers = selectedMembers
                                + userBean.getBuddyName() + ",";


                    }
                }
                if (selectedMembers.length() > 0) {
                    selectedMembers = selectedMembers.substring(0,
                            selectedMembers.length() - 1);
                    members = selectedMembers;

                }
            }
        }
        Log.d("membersspl", "members : " + members);
        if (isurgentclicked) {
            isurgentclicked = false;
            if (isGroup|| isRounding) {
                sendWithDeadline(members);
            } else {
                sendWithDeadline(buddy);
            }
        } else if (isprivateclicked) {
            isprivateclicked = false;
            if(members!=null)
                sendSpecialMessage("gp", members);
            else
                showToast("Please select members to send");

        } else if (isconfirmclicked) {
            isconfirmclicked = false;
            if (isGroup|| isRounding) {
                sendWithSchedule(members);
            } else {
                sendWithSchedule(buddy);
            }

        } else if (isReplyclicked) {
            isReplyclicked = false;
            if (isGroup|| isRounding) {
                sendSpecialMessage("GRB", members);
            } else {
                sendWithReplayBack();
            }

        }
    }

    private void loadGroupMembers(String groupId) {
        try {
            Log.i("list", "groupId " + groupId);


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
                            ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(tmp);
                            userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                            userBean.setProfilePic(pbean.getPhoto());
                            userBean.setBuddyName(tmp);
                            userBean.setSelected(false);
                            userBean.setFromchat(true);
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
                            ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(tmp);
                            userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                            userBean.setBuddyName(tmp);
                            userBean.setSelected(false);
                            userBean.setFromchat(true);
                            membersList.add(userBean);
                        }
                    }
                }
                Log.i("list", "membersList.size(): " + membersList.size());
//                mem_adapter.notifyDataSetChanged();
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
                ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(groupBean.getOwnerName());
                uBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                uBean.setProfilePic(pbean.getPhoto());
                uBean.setBuddyName(groupBean.getOwnerName());
                uBean.setSelected(false);
                uBean.setFromchat(true);
                return uBean;
            } else
                return null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static Vector<BuddyInformationBean> getOnlineList(Vector<BuddyInformationBean> vectorBean) {
        String status = null;
        Vector<BuddyInformationBean> tempList = new Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> onlinelist = new Vector<BuddyInformationBean>();
        tempList.clear();
        for (BuddyInformationBean sortlistbean : vectorBean) {
            if (sortlistbean.getStatus()!=null && sortlistbean.getStatus().equalsIgnoreCase("Online")) {
                onlinelist.add(sortlistbean);
            }
        }
        if(onlinelist.size()>0)
            tempList.addAll(onlinelist);

        return tempList;

    }
    private void linkProcess()
    {
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.members_layout, content);
        final ListView list = (ListView) v1.findViewById(R.id.memberslist);
        LinearLayout sort=(LinearLayout)v1.findViewById(R.id.sort);
        sort.setVisibility(View.GONE);
        Vector<GroupChatBean> chatlinkList=new Vector<GroupChatBean>();
        if(isGroup)
            chatlinkList=DBAccess.getdbHeler().getChatLinksHistory(groupId,true);
        else
            chatlinkList=DBAccess.getdbHeler().getChatLinksHistory(buddy,true);
        for (GroupChatBean bean : chatlinkList) {
            ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(bean.getFrom());
            bean.setFrom(pbean.getFirstname() + " " + pbean.getLastname());
            Log.d("name","stringvalue--->"+bean.getFrom());
        }
        LinksAdapter adapter=new LinksAdapter(context,R.layout.chat_links,chatlinkList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private Vector<CompleteListBean> loadFiles(String username) {

        String strCompleteListQuery=null;
        if (username.equalsIgnoreCase(CallDispatcher.LoginUser)) {
            strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where (parentid IS NULL or parentid = '') and componenttype!='IM' and componenttype!='call' and owner='"
                    + username + "'ORDER BY receiveddateandtime DESC";
        } else {
            strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where (parentid IS NULL or parentid = '') and componenttype!='IM' and componenttype!='call' and fromuser='"
                    + username + "'ORDER BY receiveddateandtime DESC";
        }
        filesList = DBAccess.getdbHeler().getCompleteListProperties(
                strCompleteListQuery);
        Log.i("files123", "files list : " + filesList.size());
        return filesList;
    }
    private Vector<CompleteListBean>  getSortType(Vector<CompleteListBean> fileList)
    {
        Vector<CompleteListBean> tempList=new Vector<CompleteListBean>();
        Vector<CompleteListBean> photoList=new Vector<CompleteListBean>();
        Vector<CompleteListBean> videoList=new Vector<CompleteListBean>();
        Vector<CompleteListBean> audioList=new Vector<CompleteListBean>();
        for(CompleteListBean cBean: fileList ) {
            if (cBean.getcomponentType().trim().equalsIgnoreCase("photo")) {
                photoList.add(cBean);
            } else if (cBean.getcomponentType().trim().equalsIgnoreCase("video")) {
                videoList.add(cBean);
            } else if (cBean.getcomponentType().trim().equalsIgnoreCase("audio")) {
                audioList.add(cBean);
            }
        }
        if(photoList.size()>0)
            tempList.addAll(photoList);
        if(videoList.size()>0)
            tempList.addAll(videoList);
        if(audioList.size()>0)
            tempList.addAll(audioList);
        return tempList;
    }
    class FilesListComparator implements Comparator<CompleteListBean> {

        @Override
        public int compare(CompleteListBean oldBean,
                           CompleteListBean newBean) {
            // TODO Auto-generated method stub
            return (oldBean.getContentName().compareToIgnoreCase(newBean.getContentName()));
        }

    }
    public void sendForwardMsg(String message, String localpath, String type,
                               SpecialMessageBean spBean,String member,Boolean fromgroup) {

        try {
            if(isPrivateBack){
                LL_privateReply.setVisibility(View.GONE);
            }else {
                msgoptionview.setVisibility(View.GONE);
            }
            rel_quoted.setVisibility(View.GONE);

            audio_call.setTag(0);
            audio_call.setBackgroundResource(R.drawable.dashboard_call_white);
            if (CallDispatcher.LoginUser != null
                    && SingleInstance.mainContext
                    .isNetworkConnectionAvailable()) {
                Log.i("group0123", "message1 : " + message);
                GroupChatBean gcBean = new GroupChatBean();
                gcBean.setFrom(CallDispatcher.LoginUser);
                gcBean.setType("100");
                gcBean.setMimetype(type);
                gcBean.setUnreadStatus(1);
                if (forward) {
                    gcBean.setUnview("1");
                }

                if (type.equals("text") || type.equals("location")) {

                    final Pattern urlPattern = Pattern.compile(
                            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                    String [] parts = message.split("\\s+");
                    for( String item : parts ) {
                        if(urlPattern.matcher(item).matches()) {
                            gcBean.setMimetype("link");
                            break;
                        }
                    }
                    if (isReplyBack) {
                        if (spBean == null) {
                            spBean = new SpecialMessageBean();
                        }
                        if (pId != null) {
                            spBean.setParentId(pId);
                        }
                        spBean.setSubcategory("grb");
                        if (privateMembers != null) {
                            spBean.setMembers(privateMembers);
                        }
                    }
                    gcBean.setMessage(message);
                    if (isconfirmBack) {
                        isconfirmBack = false;

                        if (spBean == null) {
                            spBean = new SpecialMessageBean();
                        }
                        if (pId != null) {
                            spBean.setParentId(pId);
                        }
                        gcBean.setUnview("1");
                        spBean.setSubcategory("gc");
                        if (privateMembers != null) {
                            spBean.setMembers(privateMembers);
                        }
                    }
                    Log.i("group0123", "message11 : " + gcBean.getMessage());
                } else {
                    gcBean.setMessage(message);
                    gcBean.setMediaName(localpath);
                    gcBean.setFtpUsername(CallDispatcher.LoginUser);
                    gcBean.setFtpPassword(CallDispatcher.Password);
                    gcBean.setProgress(0);
                    gcBean.setStatus(0);
                }
                gcBean.setSignalid(Utility.getSessionID());
                gcBean.setSenttime(getCurrentDateandTime());
                gcBean.setSenttimez("GMT");
                gcBean.setDateandtime(getCurrentDateandTime());
                if (fromgroup) {
                    gcBean.setTo(member);
                    gcBean.setSessionid(member);
                    gcBean.setGroupId(member);
                    gcBean.setCategory("G");
                } else {
                    gcBean.setTo(member);
                    gcBean.setSessionid(CallDispatcher.LoginUser + member);
                    gcBean.setGroupId(member);
                    gcBean.setCategory("I");
                    gcBean.setSubCategory(null);
                }
                if (spBean != null) {
                    if (spBean.getSubcategory() != null) {
                        gcBean.setSubCategory(spBean.getSubcategory());
                        if (spBean.getMembers() != null) {
                            gcBean.setPrivateMembers(spBean.getMembers());
                        }
                        if (spBean.getSubcategory().equalsIgnoreCase("gs")) {
                            gcBean.setReminderTime(spBean.getRemindertime());
                        } else if (spBean.getSubcategory().equalsIgnoreCase(
                                "gdi")
                                || spBean.getSubcategory().equalsIgnoreCase(
                                "gd")
                                || (spBean.getSubcategory()
                                .equalsIgnoreCase("grb"))) {

                            gcBean.setReminderTime(spBean.getRemindertime());
                            if (spBean.getParentId() != null) {
                                gcBean.setParentId(spBean.getParentId());
                            } else
                                gcBean.setParentId(Utility.getSessionID());
                        } else if (spBean.getSubcategory().equalsIgnoreCase("gc")) {
                            if (spBean.getParentId() != null) {
                                gcBean.setParentId(spBean.getParentId());
                            } else
                                gcBean.setParentId(Utility.getSessionID());
                        } else if (spBean.getSubcategory().equalsIgnoreCase("gu")) {
                            if (spBean.getParentId() != null) {
                                gcBean.setParentId(spBean.getParentId());
                            } else
                                gcBean.setParentId(Utility.getSessionID());
                        }
                    }
                }
                if (spBean != null && spBean.getSubcategory() != null && spBean.getSubcategory().equalsIgnoreCase("grb")) {
                    for (int i = 0; i < chatList.size(); i++) {
                        GroupChatBean gcBean1 = chatList.get(i);
                        if (gcBean1.getParentId()!=null&&gcBean1.getParentId().equals(gcBean.getParentId())) {
                            gcBean.setSubCategory("GRB_R");
                            gcBean1.setSubCategory("GRB_R");
                            gcBean.setReply("GRB_R");
                            gcBean1.setReply("GRB_R");
                            int row = DBAccess.getdbHeler(
                                    SipNotificationListener.getCurrentContext())
                                    .updateChatReplied(gcBean1);
                            gcBean1.setReplied("reply");
                        }
                    }
                } else if (spBean != null && spBean.getSubcategory() != null && spBean.getSubcategory().equalsIgnoreCase("gc")) {
                    for (int i = 0; i < chatList.size(); i++) {
                        GroupChatBean gcBean1 = chatList.get(i);
                        if (gcBean1.getParentId()!=null&&gcBean1.getParentId().equals(gcBean.getParentId())) {
                            gcBean.setSubCategory("gc_r");
                            gcBean1.setSubCategory("gc_r");
                            gcBean.setReply("gc_r");
                            gcBean1.setReply("gc_r");
                            gcBean1.setReplied("reply");
                        }
                    }
                }
                if (type.equals("text") || type.equals("location")) {
                    Log.i("group0123", "message2 : " + gcBean.getMessage());
                    SingleInstance.getGroupChatProcesser().getQueue()
                            .addObject(gcBean);
                } else {
                    Log.d("group123", "file path " + gcBean.getMediaName());

                    // start GK 07.10.2015 changes

                    imageViewer.addImage(gcBean.getMediaName());

                    // ended 07-10-15 changes

                    uploadFile(gcBean);
                }
                if (gcBean.getSubCategory() != null) {
                    if (gcBean.getSubCategory().equalsIgnoreCase("grb") || gcBean.getSubCategory().equalsIgnoreCase("GRB_R")) {
                        if (gcBean.getParentId() != null
                                && gcBean.getParentId().length() > 0
                                && !gcBean.getParentId().equalsIgnoreCase(
                                "null")) {
                            int position = -1;
                            for (int i = 0; i < chatList.size(); i++) {
                                GroupChatBean gcBean1 = chatList.get(i);

                                if (gcBean1 != null
                                        && gcBean1.getParentId() != null
                                        && gcBean1.getParentId().equals(
                                        gcBean.getParentId())) {
                                    position = i;
                                }

                            }
                            if (position == -1) {
                                chatList.add(gcBean);
                                adapter.notifyDataSetChanged();
                                maintainListPosition();
                            } else {
                                chatList.add(position + 1, gcBean);
                                adapter.notifyDataSetChanged();
                                lv.setSelection(position);
                                maintainListPosition();
                            }

                        } else {
                            chatList.add(gcBean);
                            maintainListPosition();
                        }
                    } else if (gcBean.getSubCategory().equalsIgnoreCase("gdi")) {
                        if (gcBean.getParentId() != null
                                && gcBean.getParentId().length() > 0
                                && !gcBean.getParentId().equalsIgnoreCase(
                                "null")) {
                            Vector<GroupChatBean> deadLineList = new Vector<GroupChatBean>();
                            int position = -1;
                            for (int i = 0; i < chatList.size(); i++) {
                                GroupChatBean gcBean1 = chatList.get(i);
                                if (gcBean1 != null
                                        && gcBean1.getParentId() != null
                                        && gcBean1.getParentId().equals(
                                        gcBean.getParentId())) {
                                    String msg = gcBean1.getMessage()
                                            + "\nStatus : "
                                            + gcBean.getMessage();
//									gcBean1.setMessage(msg);
                                    lv.setSelection(i);
                                    break;
                                }
                            }
                        } else {
                            chatList.add(gcBean);
                            maintainListPosition();
                        }

                    } else {
                        chatList.add(gcBean);
                        maintainListPosition();
                    }
                } else {
                    chatList.add(gcBean);
                    maintainListPosition();
                }
                if (gcBean.getUnview() != null && gcBean.getUnview().equals("1")) {
                    chatList.remove(gcBean);
                }
                if (SingleInstance.groupChatHistory.containsKey(gcBean
                        .getGroupId())) {
                    Log.i("AAAA","group chat remove");

                    SingleInstance.groupChatHistory.remove(gcBean.getGroupId());
                    // SingleInstance.groupChatHistory.get(gcBean.getGroupId())
                    // .add(gcBean);
                    SingleInstance.groupChatHistory.put(gcBean.getGroupId(),
                            chatList);
                } else {
                    SingleInstance.groupChatHistory.put(gcBean.getGroupId(),
                            chatList);
                }
                adapter.notifyDataSetChanged();
            } else {
                if (!SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                    showToast("Sorry no internet connection");
                } else {
                    showToast("Kindly login to send message");
                }
            }
            this.message.setText("");
            // hideKeyboard();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        pId = null;
        privateMembers = null;
        isReplyBack = false;

    }
    String sortorder="";
    private void FilesProcess()
    {

        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View view = layoutInflater.inflate(R.layout.files_list, content);
        try {
            final RelativeLayout rl_file=(RelativeLayout)view.findViewById(R.id.rl_file);
            final TextView tv_file=(TextView)view.findViewById(R.id.tv_file);
            final TextView alpha_sort=(TextView)view.findViewById(R.id.alpha_sort);
            final TextView date_sort=(TextView)view.findViewById(R.id.date_sort);
            final TextView type_sort=(TextView)view.findViewById(R.id.type_sort);
            date_sort.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilesFragment.sortorder = "date";
                    date_sort.setTextColor(getResources().getColor(R.color.white));
                    alpha_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                    type_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                    filesList.clear();
                    filesList = loadFiles(CallDispatcher.LoginUser);
                    Log.i("files123", "fileslist : " + filesList.size());
                    filesAdapter = new FilesAdapter(context, filesList);
                    listView.setAdapter(filesAdapter);
                    filesAdapter.notifyDataSetChanged();
                }
            });
            alpha_sort.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilesFragment.sortorder = "alpha";
                    date_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                    alpha_sort.setTextColor(getResources().getColor(R.color.white));
                    type_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                    filesList.clear();
                    filesList = loadFiles(CallDispatcher.LoginUser);
                    Collections.sort(filesList,new FilesListComparator());
                    Log.i("files123", "fileslist : " + filesList.size());
                    filesAdapter = new FilesAdapter(context, filesList);
                    listView.setAdapter(filesAdapter);
                    filesAdapter.notifyDataSetChanged();
                }
            });
            type_sort.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilesFragment.sortorder = "type";
                    date_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                    alpha_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                    type_sort.setTextColor(getResources().getColor(R.color.white));
                    filesList.clear();
                    filesList = loadFiles(CallDispatcher.LoginUser);
                    Log.i("files123", "fileslist : " + filesList.size());
                    filesAdapter = new FilesAdapter(context, getSortType(filesList));
                    listView.setAdapter(filesAdapter);
                    filesAdapter.notifyDataSetChanged();
                }
            });


            listView = (SwipeMenuListView) view.findViewById(R.id.filesList);
            listView.setTextFilterEnabled(true);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            filesList = loadFiles(CallDispatcher.LoginUser);
            Log.i("files123", "fileslist : " + filesList.size());
            filesAdapter = new FilesAdapter(context, filesList);
            listView.setAdapter(filesAdapter);
            filesAdapter.notifyDataSetChanged();

            // isSelectall = false;
            listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
                @Override
                public void onSwipeStart(int position) {
                    Log.d("Swiselect", "onSwipeStart : " + position);
                }

                @Override
                public void onSwipeEnd(int position) {

                }
            });
            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    // Create different menus depending on the view type
                    createMenu(menu);

                }
                private void createMenu(SwipeMenu menu) {
                    SwipeMenuItem forwared = new SwipeMenuItem(context);
                    forwared.setBackground(R.color.blue2);
                    forwared.setWidth(dp2px(90));
                    forwared.setIcon(R.drawable.withdraw_line_white);
                    forwared.setTitleSize(10);
                    forwared.setTitle("DELETE");
                    forwared.setTitleColor(Color.WHITE);
                    menu.addMenuItem(forwared);
                }
            };
            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position,
                                               SwipeMenu menu, int index) {

                    try {
                        CompleteListBean cBean = filesAdapter
                                .getItem(position);
                        deleteNote(cBean, context);
                        return false;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return false;
                    }
                }
            });
            listView.setMenuCreator(creator);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    try {

                        CompleteListBean cBean = filesAdapter.getItem(position);
                        FileInfoFragment fileInfoFragment = FileInfoFragment.newInstance(context);
                        FragmentManager fragmentManager = SingleInstance.mainContext
                                .getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(
                                R.id.activity_main_content_fragment, fileInfoFragment)
                                .commitAllowingStateLoss();
                        calldisp.cmp = cBean;
                        fileInfoFragment.setFileBean(cBean);
                        if(isGroup)
                            fileInfoFragment.setFrom(true,groupId,isGroup);
                        else
                            fileInfoFragment.setFrom(true, buddy, isGroup);
                        finish();

                        // }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });


            if (filesAdapter.getCount() > 0 || filesList.size() > 0) {
                Log.i("files123", "fileslist 2 : " + filesList.size());
                listView.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    public void deleteNote(final CompleteListBean cBean, Context context1) {
        try {
            String message = null;
            message = "Are you sure to delete this'" + cBean.getContentName()
                    + "'?";

            AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(
                    context1);
            deleteConfirmation.setTitle("Delete Confirmation");
            deleteConfirmation.setMessage(message);
            deleteConfirmation.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes)
                    ,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String strDeleteQry = "delete from component where componentid="
                                        + cBean.getComponentId();

                                if (callDisp.getdbHeler(context).ExecuteQuery(
                                        strDeleteQry)) {
                                    filesList.remove(cBean);
                                    if (cBean.getContentpath() != null
                                            && cBean.getContentpath() != "") {
                                        if (!cBean.getcomponentType()
                                                .equalsIgnoreCase("video")) {

                                            File f = new File(cBean
                                                    .getContentpath());
                                            if (f.exists()) {
                                                f.delete();
                                            }

                                        } else if (cBean.getcomponentType()
                                                .equalsIgnoreCase("video")) {

                                            File f = new File(cBean
                                                    .getContentpath() + ".mp4");
                                            if (f.exists()) {
                                                f.delete();
                                            }
                                            File f1 = new File(cBean
                                                    .getContentpath() + ".jpg");
                                            if (f1.exists()) {
                                                f1.delete();
                                            }

                                        }
                                    }
                                }

                            } catch (Exception e) {
                                Log.i("callhistory", "" + e.getMessage());
                                if (AppReference.isWriteInFile)
                                    AppReference.logger.error(e.getMessage(), e);

                            }
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    filesAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
            deleteConfirmation.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            deleteConfirmation.show();

            // showSingleSelectBuddy();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("pin", "Groupchatactivity Onstop");
        AppReference.mainContext.isApplicationBroughtToBackground();
        if(AppReference.fileOpen){
            Log.i("pin", "Groupchatactivity Onstop AppReference.fileOpen==true");
            AppReference.mainContext.openPinActivity=false;
        }

    }
    private void calendarProcess()
    {
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.calendar, content);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        CalendarViewClass cv = ((CalendarViewClass)findViewById(R.id.calendar_view));
        cv.updateCalendar(events);

        // assign event handler
        cv.setEventHandler(new CalendarViewClass.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                // show returned day

                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    SimpleDateFormat df1 = new SimpleDateFormat("M-dd-yyyy");
                    String selectedDate = df1.format(c.getTime());
                    Date date1 = df1.parse(selectedDate);
                    Intent intent = new Intent(context, CalendarActivity.class);
                    intent.putExtra("date", selectedDate);
                    intent.putExtra("groupid", groupId);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public String getCurrentTime() {
        try {
            Date curDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "hh:mm a");
            return sdf.format(curDate).toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private String getReplyMessage(GroupChatBean groupChatBean){
        String message=null;
        if(groupChatBean!=null){

            if(groupChatBean.getMessage()!=null && !groupChatBean.getMessage().equalsIgnoreCase("null")){
                if(message==null){
                    message=groupChatBean.getMessage();
                }else{
                    message=message+"\n"+groupChatBean.getMessage();
                }
            }
            if(groupChatBean.getMimetype()!=null && !groupChatBean.getMimetype().equalsIgnoreCase("null")){
                if(groupChatBean.getMimetype().equalsIgnoreCase("audio")){
                    if(groupChatBean.getMediaName()!=null && !groupChatBean.getMediaName().equalsIgnoreCase("null")) {
                        if (groupChatBean.getMediaName().contains("COMMedia")) {
                            if(!groupChatBean.getMediaName().split("COMMedia/")[1].equalsIgnoreCase("null")){
                                if(message==null){
                                    message=groupChatBean.getMediaName().split("COMMedia/")[1];
                                }else{
                                    message=message+"\n"+groupChatBean.getMediaName().split("COMMedia/")[1];
                                }
                            }

                        }else{
                            if(message==null){
                                message=groupChatBean.getMediaName();
                            }else{
                                message=message+"\n"+groupChatBean.getMediaName();
                            }
                        }
                    }else{
                                    if (message == null) {
                                        message = "Audio";
                                    } else {
                                        message = message + "\n" + "Audio";
                                    }
                    }
                }
                if(groupChatBean.getMimetype().equalsIgnoreCase("image")){
                    if(groupChatBean.getMediaName()!=null && !groupChatBean.getMediaName().equalsIgnoreCase("null")) {
                        if (groupChatBean.getMediaName().contains("COMMedia")) {
                            if(!groupChatBean.getMediaName().split("COMMedia/")[1].equalsIgnoreCase("null")){
                                if(message==null){
                                    message=groupChatBean.getMediaName().split("COMMedia/")[1];
                                }else{
                                    message=message+"\n"+groupChatBean.getMediaName().split("COMMedia/")[1];
                                }
                            }

                        }else{
                            if(message==null){
                                message=groupChatBean.getMediaName();
                            }else{
                                message=message+"\n"+groupChatBean.getMediaName();
                            }
                        }
                    }else{
                        if (message == null) {
                            message = "Image";
                        } else {
                            message = message + "\n" + "Image";
                        }
                    }                }
                if(groupChatBean.getMimetype().equalsIgnoreCase("sketch")){
                    if(groupChatBean.getMediaName()!=null && !groupChatBean.getMediaName().equalsIgnoreCase("null")) {
                        if (groupChatBean.getMediaName().contains("COMMedia")) {
                            if(!groupChatBean.getMediaName().split("COMMedia/")[1].equalsIgnoreCase("null")){
                                if(message==null){
                                    message=groupChatBean.getMediaName().split("COMMedia/")[1];
                                }else{
                                    message=message+"\n"+groupChatBean.getMediaName().split("COMMedia/")[1];
                                }
                            }

                        }else{
                            if(message==null){
                                message=groupChatBean.getMediaName();
                            }else{
                                message=message+"\n"+groupChatBean.getMediaName();
                            }
                        }
                    }else{
                        if (message == null) {
                            message = "Sketch";
                        } else {
                            message = message + "\n" + "Sketch";
                        }
                    }
                }
                if(groupChatBean.getMimetype().equalsIgnoreCase("document")){
                    if(groupChatBean.getMediaName()!=null && !groupChatBean.getMediaName().equalsIgnoreCase("null")) {
                        if (groupChatBean.getMediaName().contains("COMMedia")) {
                            if(!groupChatBean.getMediaName().split("COMMedia/")[1].equalsIgnoreCase("null")){
                                if(message==null){
                                    message=groupChatBean.getMediaName().split("COMMedia/")[1];
                                }else{
                                    message=message+"\n"+groupChatBean.getMediaName().split("COMMedia/")[1];
                                }
                            }

                        }else{
                            if(message==null){
                                message=groupChatBean.getMediaName();
                            }else{
                                message=message+"\n"+groupChatBean.getMediaName();
                            }
                        }
                    }else{
                        if (message == null) {
                            message = "Document";
                        } else {
                            message = message + "\n" + "Document";
                        }
                    }
                }
                if(groupChatBean.getMimetype().equalsIgnoreCase("video")){
                    if(groupChatBean.getMediaName()!=null && !groupChatBean.getMediaName().equalsIgnoreCase("null")) {
                        if (groupChatBean.getMediaName().contains("COMMedia")) {
                            if(!groupChatBean.getMediaName().split("COMMedia/")[1].equalsIgnoreCase("null")){
                                if(message==null){
                                    message=groupChatBean.getMediaName().split("COMMedia/")[1];
                                }else{
                                    message=message+"\n"+groupChatBean.getMediaName().split("COMMedia/")[1];
                                }
                            }

                        }else{
                            if(message==null){
                                message=groupChatBean.getMediaName();
                            }else{
                                message=message+"\n"+groupChatBean.getMediaName();
                            }
                        }
                    }else{
                        if (message == null) {
                            message = "Video";
                        } else {
                            message = message + "\n" + "Video";
                        }
                    }
                }
                if(groupChatBean.getMimetype().equalsIgnoreCase("mixedfile")){
                    if(groupChatBean.getMediaName()!=null && !groupChatBean.getMediaName().equalsIgnoreCase("null")) {
                        if(groupChatBean.getMediaName().contains(",")) {
                            String[] fname = groupChatBean.getMediaName().split(",");
                            for(int i=0;i<fname.length;i++) {
                                if(fname[i].contains("COMMedia")) {
                                    if (message == null) {
                                        if(!fname[i].split("COMMedia/")[1].equalsIgnoreCase("null")){
                                            message=fname[i].split("COMMedia/")[1];
                                        }
                                    } else {
                                        if(!fname[i].split("COMMedia/")[1].equalsIgnoreCase("null")) {
                                            message = message + "\n" + fname[i].split("COMMedia/")[1];
                                        }
                                    }
                                }else{
                                    if(message==null){
                                        message=fname[i];
                                    }else{
                                        message=message+"\n"+fname[i];
                                    }
                                }
                            }
                        }else{
                            if(groupChatBean.getMediaName().contains("COMMedia")){
                                if(message==null) {
                                    if(!groupChatBean.getMediaName().split("COMMedia/")[1].equalsIgnoreCase("null")) {
                                        message = groupChatBean.getMediaName().split("COMMedia/")[1];
                                    }
                                }else{
                                    if(!groupChatBean.getMediaName().split("COMMedia/")[1].equalsIgnoreCase("null")) {
                                        message = message + "\n" + groupChatBean.getMediaName().split("COMMedia/")[1];
                                    }
                                }
                            }else{
                                if(message==null){
                                    message=groupChatBean.getMediaName();
                                }else{
                                    message=message+"\n"+groupChatBean.getMediaName();
                                }
                            }
                        }
                    }
                }


            }
        }
        return message;
    }


    public void PrivateReplyBack(View view){
        GroupChatBean gcBean1 = (GroupChatBean) view
                .getTag();
//        gcBean1.setReply("private");
        Log.i("privaterpl","parentID-->"+gcBean1.getParentId());
        privateParentID=gcBean1.getParentId();
        privateReply_username=gcBean1.getFrom();
        sendSpecialMessage("gp_r", gcBean1.getFrom());
        int row = DBAccess.getdbHeler(
                SipNotificationListener.getCurrentContext())
                .updateChatReply(gcBean1);
    }


    public void ResendMsgAlert(final GroupChatBean gcBean1){

                        if (CallDispatcher.LoginUser != null
                                && SingleInstance.mainContext
                                .isNetworkConnectionAvailable()) {
                            gcBean1.setType("100");
                            if (gcBean1.getMimetype().equals("text") || gcBean1.getMimetype().equals("location")) {
                                Log.i("group0123", "message2 : " + gcBean1.getMessage());
                                SingleInstance.getGroupChatProcesser().getQueue()
                                        .addObject(gcBean1);
                            }
                            else if(gcBean1.getMimetype().equals("textfile"))
                            {
                                Log.d("AAA", "file path " + gcBean1.getMediaName());
//                                DBAccess.getdbHeler(context).insertGroupChat(gcBean);

                                uploadFile(gcBean1);

                            }
                            else {
//                                Log.d("group123", "file path " + gcBean.getMediaName());
//                                DBAccess.getdbHeler(context).insertGroupChat(gcBean);
//
//                                // start GK 07.10.2015 changes
//
//                                imageViewer.addImage(gcBean.getMediaName());
//
//                                // ended 07-10-15 changes

                                uploadFile(gcBean1);
                            }



                        }else {
                            if (!SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                                showToast("Sorry no internet connection");
                            } else {
                                showToast("Kindly login to send message");
                            }
                        }

    }
    public void notifyUI() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int dashCount = 0;
                dashCount += DBAccess.getdbHeler(context)
                        .getUnreadMsgCount(CallDispatcher.LoginUser);
                dashCount += DBAccess.getdbHeler(context)
                        .getUnreadFileCount(CallDispatcher.LoginUser);
                dashCount += DBAccess.getdbHeler(context)
                        .getUnreadCallCount(CallDispatcher.LoginUser);
                TextView dash_count = (TextView) findViewById(R.id.dash_count);
                if (dashCount > 0) {
                    dash_count.setText(Integer.toString(dashCount));
                    dash_count.setVisibility(View.VISIBLE);
                } else {
                    dash_count.setVisibility(View.GONE);
                }
                ProfileBean bean=SingleInstance.myAccountBean;
                TextView userName = (TextView) findViewById(R.id.userName);
                if(bean.getFirstname()!=null && bean.getLastname()!=null)
                    userName.setText(bean.getFirstname()+" "+bean.getLastname());
                else if(bean.getNickname()!=null)
                    userName.setText(bean.getNickname());
                else
                    userName.setText(CallDispatcher.LoginUser);
                String status_1 = SingleInstance.mainContext.loadCurrentStatus();
                TextView status = (TextView) findViewById(R.id.status);
                ImageView img_status = (ImageView) findViewById(R.id.img_status);
                if(status_1.equalsIgnoreCase("online")){
                    status.setText("Online");
                    img_status.setBackgroundResource(R.drawable.online_icon);
                }else if(status_1.equalsIgnoreCase("away")){
                    status.setText("Invisible");
                    img_status.setBackgroundResource(R.drawable.invisibleicon);
                }else if(status_1.equalsIgnoreCase("busy")){
                    status.setText("Busy");
                    img_status.setBackgroundResource(R.drawable.busy_icon);
                }else{
                    status.setText("Offline");
                    img_status.setBackgroundResource(R.drawable.offline_icon);
                }
                setProfilePic();
            }
        });
    }

    public void setProfilePic() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    ProfileBean bean=SingleInstance.myAccountBean;
                    ImageView user_image = (ImageView) findViewById(R.id.user_image);
                    if(bean.getPhoto()!=null){
                        String profilePic=bean.getPhoto();
                        if (profilePic != null && profilePic.length() > 0) {
                            if (!profilePic.contains("COMMedia")) {
                                profilePic = Environment.getExternalStorageDirectory()
                                        + "/COMMedia/" + profilePic;
                            }
                            imageLoader.DisplayImage(profilePic, user_image,
                                    R.drawable.img_user);
                        }
                    }
                } catch (Exception e) {
                    SingleInstance.printLog(null, e.getMessage(), null, e);
                }
            }
        });

    }
    private void saveFile(final String mimetype, final String mediaName){
        final AlertDialog.Builder alert = new AlertDialog.Builder(
                context);
        alert.setTitle("Save File");
        alert.setMessage("Are you sure want to save this to snazbox files?");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mimetype.equalsIgnoreCase("audio"))
                            DBAccess.getdbHeler(context).putDBEntry(4, mediaName,
                                    WebServiceReferences.getNoteCreateTimeForFiles(), "Audio Note", null, "");
                        else if (mimetype.equalsIgnoreCase("video"))
                            DBAccess.getdbHeler(context).putDBEntry(5, mediaName,
                                    WebServiceReferences.getNoteCreateTimeForFiles(), "Video Note", null, "");
                        else if (mimetype.equalsIgnoreCase("image"))
                            DBAccess.getdbHeler(context).putDBEntry(2, mediaName,
                                    WebServiceReferences.getNoteCreateTimeForFiles(), "Photo Note", null, "");
                        else if (mimetype.equalsIgnoreCase("document"))
                            DBAccess.getdbHeler(context).putDBEntry(9, mediaName,
                                    WebServiceReferences.getNoteCreateTimeForFiles(), "Document Note", null, "");
                        else if (mimetype.equalsIgnoreCase("mixedfile")) {
                            String[] paths = mediaName.split(",");
                            for (int i = 0; i < paths.length; i++) {
                                if (paths[i].endsWith(".jpg") || paths[i].endsWith(".png"))
                                    DBAccess.getdbHeler(context).putDBEntry(2, paths[i],
                                            WebServiceReferences.getNoteCreateTimeForFiles(), "Photo Note", null, "");
                                if (paths[i].endsWith("mp4")) {
                                    DBAccess.getdbHeler(context).putDBEntry(5, paths[i],
                                            WebServiceReferences.getNoteCreateTimeForFiles(), "Video Note", null, "");
                                } else if (paths[i].endsWith("mp3") || paths[i].endsWith("amr")) {
                                    DBAccess.getdbHeler(context).putDBEntry(4, paths[i],
                                            WebServiceReferences.getNoteCreateTimeForFiles(), "Audio Note", null, "");
                                } else
                                    DBAccess.getdbHeler(context).putDBEntry(9, paths[i],
                                            WebServiceReferences.getNoteCreateTimeForFiles(), "Document Note", null, "");
                            }
                        }
                    }

                });
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.show();
    }
     public void ftpUpload(ChatFTPBean ftpBean) {
        Log.i("check","Inside ftpUploadwebservice workerThread");

//        final int notificationID = getNotificationID();
        try {

            String username = CallDispatcher.LoginUser;
            String password = CallDispatcher.Password;
            GroupChatBean gBean= (GroupChatBean) ftpBean.getSourceObject();
            String filename = ftpBean.getInputFile();
            String path = filename;
            Log.i("FileUploadIM1", "path3" +filename);

            Log.i("FileUpload1", "type--->" + gBean.getMimetype());
            SingleInstance.getGroupChatHistoryWriter().getQueue()
                    .addObject(gBean);

            if((gBean.getMimetype().equals("mixedfile"))||gBean.getMimetype().equalsIgnoreCase("image")) {
                String[] paths=filename.split(",");
                if(paths.length>0) {
                    for (int i = 0; i < paths.length; i++) {
                        if(paths[i].endsWith(".jpg")){
                            Bitmap bitmap = BitmapFactory.decodeFile(paths[i]);
                            String base64 = encodeTobase64(bitmap);
                            String fname = paths[i].split("/")[5];
                            uploadFile(username, password, "photo", fname, base64, filename,SingleInstance.mainContext,ftpBean);
                        }else
                        {
                            String type=gBean.getMimetype();
                            if (paths[i].split("COMMedia/")[1].endsWith("mp4")) {
                                type="video";
                            }else if(paths[i].split("COMMedia/")[1].endsWith("mp3")
                                    || paths[i].split("COMMedia/")[1].endsWith("amr")){
                                type="audio";
                            }else{
                                type="document";
                            }
                            String base64 = encodeAudioVideoToBase64(paths[i]);
                            String fname = paths[i].split("/")[5];
                            uploadFile(username, password, type, fname, base64, filename,SingleInstance.mainContext,ftpBean);

                        }
                    }
                }else{
                    Log.i("FileUploadIM", "IF PHOTO||Handsketch--->");
                    Log.i("FileUploadIM", "path" +filename);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    String base64 = encodeTobase64(bitmap);
                    String fname = filename.split("/")[5];
                    Log.i("FileUploadIM", "path2" +filename);
                    Log.i("FileUpload1", "fname--->" + fname);
                    Log.i("FileUpload1", "uname--->" + username);
                    Log.i("FileUpload1", "password--->" + password);
                    Log.i("FileUpload1", "type--->" + gBean.getMimetype());
                    Log.i("FileUpload1", "base64--->" + base64);
                    uploadFile(username, password, "photo", fname, base64, filename,SingleInstance.mainContext,ftpBean);
                }
            }
            else if(gBean.getMimetype().equalsIgnoreCase("audio")||gBean.getMimetype().equalsIgnoreCase("video"))
            {
//                encodeAudioVideoToBase64
                Log.i("FileUpload1", "ELSE IF AUDIO||Video--->" );
                Log.i("FileUpload1", "type--->" + gBean.getMimetype());
                Log.i("FileUploadIM", "path" +filename);
                String base64 = encodeAudioVideoToBase64(path);
                String fname = filename.split("/")[5];
                Log.i("FileUpload1", "fname--->" + fname);
                Log.i("FileUpload1", "uname--->" + username);
                Log.i("FileUpload1", "password--->" + password);
                Log.i("FileUpload1", "type--->" + gBean.getMimetype());
                Log.i("FileUpload1", "base64--->" + base64);

                uploadFile(username, password, gBean.getMimetype(), fname, base64,filename,SingleInstance.mainContext,ftpBean);
            }
            else if(gBean.getMimetype().equalsIgnoreCase("document")){
                String fname = filename.split("/")[5];
                String base64 = encodeAudioVideoToBase64(path);
                uploadFile(username, password, gBean.getMimetype(), fname, base64,filename,SingleInstance.mainContext,ftpBean);
            }
//            notifyStatus(true);



        } catch (Exception e) {
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

    private String encodeAudioVideoToBase64(String path){
        String strFile=null;
        File file=new File(path);
        try {
            FileInputStream file1=new FileInputStream(file);
            byte[] Bytearray=new byte[(int)file.length()];
            file1.read(Bytearray);
            strFile = Base64.encodeToString(Bytearray, Base64.NO_WRAP);//Convert byte array into string

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("FileUpload", "audioVideoEncode========" + strFile);
        return strFile;
    }

    public void uploadFile(String username, String password,String componenttype,
                           String filename, String contents,String componentpath,Context context1, Object obj)
    {
        Log.i("FileUpload", "Inside CallDisp_UploadFile---> " +componentpath);
        Log.i("check","worker thread uploadFile ");
        String[] temp = new String[7];
        temp[0]=username;
        temp[1]=password;
        temp[2]=componenttype;
        temp[3]=filename;
        temp[4]=contents;
        File file = new File(componentpath);
        long length = (int) file.length();
        if(!componenttype.equalsIgnoreCase("photo"))
            length = length/1024;
        temp[5]="im";
        temp[6]= String.valueOf(length);
        Log.i("FileUpload", "Inside CallDisp_UploadFile---> " + temp[6]);
//  WebServiceReferences.webServiceClient.FileUpload(temp,context1,obj);
        ChatLoadWebservice taskrunner=new ChatLoadWebservice();
        taskrunner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,temp,obj);
//  taskrunner.execute(temp,obj);

//  UploadThread uploadThread=new UploadThread(temp,obj);
//  uploadThread.start();
    }


    public class ChatLoadWebservice extends
            AsyncTask<Object, Object, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("check","ChatLoadWebservice onPostExcute ");
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.i("check","ChatLoadWebservice doInBackground ");
                String[] param=(String[])params[0];
                ChatFTPBean chatFTPBean=(ChatFTPBean)params[1];

                String parse="";
                String url= SingleInstance.mainContext.getResources().getString(com.cg.snazmed.R.string.service_url1);

                String urlPort = url.substring(url.indexOf("://") + 3);
                String loginIP = url.substring(url.indexOf("://") + 3);
                loginIP = loginIP.substring(0, loginIP.indexOf(":"));
                loginIP = loginIP.trim();
                urlPort = urlPort.substring(urlPort.indexOf(":") + 1);
                urlPort = urlPort.substring(0, urlPort.indexOf("/"));

                String server_ip = loginIP;
                int connect_ort = Integer.parseInt(urlPort);
                String namespace = "http://ltws.com/";
                String wsdl_link = url.trim()+"?wsdl";
                String quotes = "\"";

                parse= wsdl_link.substring(wsdl_link.indexOf("://") + 3);
                parse = parse.substring(parse.indexOf(":") + 1);
                parse = parse.substring(parse.indexOf("/"),
                        parse.indexOf("?"));

                AndroidInsecureKeepAliveHttpsTransportSE androidHttpTransport = new AndroidInsecureKeepAliveHttpsTransportSE(
                        server_ip, connect_ort, parse, 30000);

                SoapObject mRequest = new SoapObject(namespace, "FileUpload");
//                XmlComposer xmlComposer = new XmlComposer();
//                String fuploadxml = xmlComposer.fileUploadXml(param);

                String fuploadxml ="<?xml version=\"1.0\"?>"+"<fileupload "+" " +
                        "username=" + quotes + param[0] + quotes+" password=" + quotes +
                        param[1] + quotes+" mimetype=" + quotes + param[2] + quotes+" filename=" +
                        quotes + param[3] + quotes+" content=" + quotes + param[4] + quotes+" branchtype=" +
                        quotes + param[5] + quotes+" filesize=" + quotes + param[6] + quotes+" />";

                HashMap<String,String> propert_map = new HashMap<String,String>();
                propert_map.put("uploadxml", fuploadxml);

                if (propert_map != null) {
                    for (Map.Entry<String, String> set : propert_map.entrySet()) {
                        mRequest.addProperty(set.getKey().trim(), set
                                .getValue().trim());
                    }
                }

                Log.d("webservice", "My Server Request  :" + mRequest);

                SoapSerializationEnvelope mEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                mEnvelope.setOutputSoapObject(mRequest);

                androidHttpTransport.call(quotes + namespace + "FileUpload" + quotes, mEnvelope);


                SoapPrimitive mSp = (SoapPrimitive) mEnvelope.getResponse();
                XmlParser mParser = new XmlParser();
                boolean mChk = false;
                mChk = mParser.getResult(mSp.toString());
                Log.d("webservice", "My Server Resopnse  :" + mSp.toString());
                if(mChk){
                    SingleInstance.mainContext.showToast("File upload successfully");
                    SingleInstance.mainContext.notifyFileUploadResponse(chatFTPBean);
                }

            } catch (IOException e) {
                SingleInstance.mainContext.showToast("File upload Failed");
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                SingleInstance.mainContext.showToast("File upload Failed");
                e.printStackTrace();
            }catch (Exception e){
                SingleInstance.mainContext.showToast("File upload Failed");
                e.printStackTrace();
            }
            return null;
        }
        protected void onPreExecute() {
            Log.i("check","ChatLoadWebservice onPreExecute ");

        }

    }
    public class imageOrientation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                Log.i("profiledownload", "my profile download");
                String changeorientation=urls[0];
                Bitmap bitmap = ImageUtils.decodeScaledBitmapFromSdCard(strIPath, 320, 240);
                int orientation = ImageUtils.resolveBitmapOrientation(strIPath);
                Log.i("profiledownload", "orientation--->"+orientation);
                bitmap = ImageUtils.applyOrientation(bitmap, Integer.parseInt(changeorientation));
                File file = new File(strIPath);
                if (file.exists())
                    file.delete();
                FileOutputStream fOut = null;
                try {
                    Log.d("size", "........6------------->");
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    Log.d("size", "........ after file write is------------->");
                } catch (Exception e) {
                    Log.d("size", "........7------------->");
                    e.printStackTrace();
                }
                bitmap=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            cancelDialog();
            File new_file = new File(strIPath);
            if (new_file.exists()) {
                btn_grid.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));
                atachlay.setVisibility(View.GONE);
                audio_layout.setVisibility(View.GONE);
                isGrid = false;
//                            relative_send_layout.getLayoutParams().height = 400;
                SendListUIBean uIbean = new SendListUIBean();
                uIbean.setType("image");
                uIbean.setPath(strIPath);
                uIbean.setUser(buddy);
                SendListUI.add(uIbean);
                sendlistadapter.notifyDataSetChanged();


                if(!current_open_activity_detail.containsKey("thirdimage1")) {
                    current_open_activity_detail.put("thirdimage1", uIbean);
                }else if(!current_open_activity_detail.containsKey("thirdimage2")){
                    current_open_activity_detail.put("thirdimage2", uIbean);
                }else if(!current_open_activity_detail.containsKey("thirdimage3")){
                    current_open_activity_detail.put("thirdimage3", uIbean);
                }

                list_all.removeAllViews();
                final int adapterCount = sendlistadapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = sendlistadapter.getView(i, null, null);
                    list_all.addView(item);
                }
                if(adapterCount>=2){
                    multi_send.getLayoutParams().height=280;
                }
                if(isPrivateBack){
                    LL_privateReply.setVisibility(View.VISIBLE);
                }else {
                    msgoptionview.setVisibility(View.VISIBLE);
                }
                audio_call.setBackgroundResource(R.drawable.chat_send);
                audio_call.setTag(1);
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }
    public void CancelSwipeContainer(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(swipeContainer!=null) {
                    swipeContainer.setRefreshing(false);
                }
                chatprocess();
            }
        },2000);

    }
}