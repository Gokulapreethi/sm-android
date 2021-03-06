package com.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.NotifyListAdapter;
import com.bean.NotifyListBean;
import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.CallConnectingScreen;
import com.cg.callservices.inCommingCallAlert;
import com.cg.callservices.VideoCallScreen;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.DateComparator;
import com.cg.files.CompleteListBean;
import com.cg.files.ComponentCreator;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.util.PieChart;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.FileDetailsBean;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

public class DashBoardFragment extends Fragment {

    private static DashBoardFragment dashBoardFragment;
    private static Context mainContext;
    private ImageLoader imageLoader;
    public View view;
    LinearLayout rl;
    FrameLayout fl;
    Calendar c1;
    TextView t1,call_tv,chat_tv,file_tv,exfile_tv;
    ImageView img,search;
    private PieChart piechart;
    private TextView freeSpace;
    private TextView totalSize;
    LinearLayout ll_warn, ll_msg, ll_call, ll_file;
    EditText ed_search;
    CalendarView calendarView;
    RelativeLayout rl_notify;
    TextView tv_rl_notify;
    Button bt_rl_notify;
    RelativeLayout.LayoutParams param1, param2;
    LinearLayout header;
    private Boolean isSearch=false;
    public   String setType = new String();
    private Button plusBtn, clearAllBtn;
    public ListView notifylistview;
    public NotifyListAdapter notifyAdapter=null;
    public Vector<NotifyListBean> notifyList = new Vector<NotifyListBean>();
    public Vector<NotifyListBean> tempnotifylist = new Vector<NotifyListBean>();
    public Vector<NotifyListBean> seacrhnotifylist = new Vector<NotifyListBean>();
    public static CallDispatcher callDisp;
    int audio=0, videos=0,other=0,image=0,chat=0;
    Vector<FileDetailsBean> fBeanList;
    RelativeLayout mainHeader;
    public static ProgressDialog pDialog;
    public Vector<NotifyListBean> fileList=new Vector<>();

    public static DashBoardFragment newInstance(Context context) {
        try {
            if (dashBoardFragment == null) {
                mainContext = context;
                dashBoardFragment = new DashBoardFragment();
                callDisp = CallDispatcher.getCallDispatcher(context);
            }
            return dashBoardFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return dashBoardFragment;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            Button select = (Button) getActivity().findViewById(R.id.btn_brg);
            select.setVisibility(View.GONE);
            final TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setText("DASHBOARD");
            title.setVisibility(View.VISIBLE);
            AppReference.bacgroundFragment=dashBoardFragment;

            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);

            mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.VISIBLE);
            LinearLayout contact_layout = (LinearLayout) getActivity()
                    .findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);
            plusBtn = (Button) getActivity().findViewById(R.id.add_group);
            ed_search=(EditText) getActivity().findViewById(R.id.search_box);
            clearAllBtn = (Button) getActivity().findViewById(R.id.btn_settings);
            plusBtn.setVisibility(View.GONE);
            ed_search.setVisibility(View.GONE);
            clearAllBtn.setVisibility(View.GONE);
            clearAllBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_refresh));
            clearAllBtn.setText("");
            plusBtn.setBackgroundResource(R.drawable.navigation_search);
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        if(!isSearch) {
                            plusBtn.setBackgroundResource(R.drawable.navigation_close);
                            header.setVisibility(View.VISIBLE);
                            title.setVisibility(View.GONE);
                            clearAllBtn.setVisibility(View.GONE);
                            ed_search.setVisibility(View.VISIBLE);
                            isSearch=true;
                        } else {
//                            LoadFilesList(CallDispatcher.LoginUser);
                            notifyAdapter = new NotifyListAdapter(mainContext,tempnotifylist);
                            notifyAdapter.isFromOther(true);
                            notifylistview.setAdapter(notifyAdapter);
                            notifyAdapter.notifyDataSetChanged();
                            plusBtn.setBackgroundResource(R.drawable.navigation_search);
                            header.setVisibility(View.VISIBLE);
                            title.setVisibility(View.VISIBLE);
//                            clearAllBtn.setVisibility(View.VISIBLE);
                            ed_search.setText("");
                            ed_search.setVisibility(View.GONE);
                            InputMethodManager imm = (InputMethodManager)mainContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
                            isSearch=false;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            RelativeLayout audio_minimize = (RelativeLayout)getActivity().findViewById(R.id.audio_minimize);
            RelativeLayout video_minimize = (RelativeLayout)getActivity().findViewById(R.id.video_minimize);
            audio_minimize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainHeader.setVisibility(View.GONE);
                    addShowHideListener(true);
                }
            });
            video_minimize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainHeader.setVisibility(View.GONE);
                    addShowHideListener(false);
                }
            });
            ImageView min_incall=(ImageView)getActivity().findViewById(R.id.min_incall);
            ImageView min_outcall=(ImageView)getActivity().findViewById(R.id.min_outcall);
            min_incall.setOnClickListener(new View.OnClickListener() {
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
            min_outcall.setOnClickListener(new View.OnClickListener() {
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
            final AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");

            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.GONE);
            imageLoader = new ImageLoader(mainContext);
            SingleInstance.instanceTable.put("dashboard",dashBoardFragment);

            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.tabcontent, null);
                try {
                    final LinearLayout summary=(LinearLayout)view.findViewById(R.id.summary);
                    final LinearLayout notification=(LinearLayout)view.findViewById(R.id.notification);
                    final ImageView im_summary = (ImageView)view.findViewById(R.id.im_summary);
                    final ImageView im_notification = (ImageView)view.findViewById(R.id.im_notification);
                    final TextView tv_summary = (TextView)view.findViewById(R.id.tv_summary);
                    final TextView tv_notification = (TextView)view.findViewById(R.id.tv_notification);
                    final View view_summary = (View)view.findViewById(R.id.view_summary);
                    final View view_notification = (View)view.findViewById(R.id.view_notification);
                    header=(LinearLayout)view.findViewById(R.id.header);
                    rl = (LinearLayout) view.findViewById(R.id.i1);
                    fl = new FrameLayout(mainContext);
                    rl.removeAllViews();
                    plusBtn.setVisibility(View.GONE);
                    clearAllBtn.setVisibility(View.GONE);
                    LayoutInflater layoutInflater = (LayoutInflater)mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v1 = layoutInflater.inflate(R.layout.summary, rl);
                    call_tv=(TextView)v1.findViewById(R.id.call_tv);
                    chat_tv=(TextView)v1.findViewById(R.id.chat_tv);
                    file_tv=(TextView)v1.findViewById(R.id.file_tv);
                    exfile_tv=(TextView)v1.findViewById(R.id.exfile_tv);
                    ll_warn = (LinearLayout)v1.findViewById(R.id.ll_warn);
                    ll_msg = (LinearLayout)v1.findViewById(R.id.ll_msg);
                    ll_call = (LinearLayout)v1.findViewById(R.id.ll_call);
                    ll_file = (LinearLayout)v1.findViewById(R.id.ll_file);
                    piechart = (PieChart) v1.findViewById(R.id.piechart);
                    freeSpace= (TextView) v1.findViewById(R.id.freeSpace);
                    totalSize= (TextView) v1.findViewById(R.id.totalSize);
                    showMemoryControl();
                    ll_warn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setType = "ll_warn";
                            notification.performClick();
                        }
                    });
                    ll_msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setType = "ll_msg";
                            notification.performClick();
//                            DBAccess.getdbHeler(mainContext)
//                                    .setReadAllCount(CallDispatcher.LoginUser, "");
                        }
                    });
                    ll_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setType = "ll_call";
                            notification.performClick();
                            DBAccess.getdbHeler(mainContext)
                                    .setReadAllCount(CallDispatcher.LoginUser, "call");
                        }
                    });
                    ll_file.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setType = "ll_file";
                            notification.performClick();
                            DBAccess.getdbHeler(mainContext)
                                    .setReadAllCount(CallDispatcher.LoginUser, "file");
                        }
                    });
                    piechart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MemoryControlFragment memoryControlFragment = MemoryControlFragment.newInstance(mainContext);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, memoryControlFragment)
                                    .commitAllowingStateLoss();
                        }
                    });
                    c1 = Calendar.getInstance();
                    updateCount();

                    Calendar first = (Calendar) c1.clone();
                    first.add(Calendar.DAY_OF_WEEK,
                            first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
                    String today=dateWithoutTime.toString();
                    String[] todayDate=today.split(" ");
                    String[] weekdays=new String[7];
                    TextView tv1= (TextView)v1.findViewById(R.id.tv1);
                    TextView tv2= (TextView)v1.findViewById(R.id.tv2);
                    TextView tv3= (TextView)v1.findViewById(R.id.tv3);
                    TextView tv4= (TextView)v1.findViewById(R.id.tv4);
                    TextView tv5= (TextView)v1.findViewById(R.id.tv5);
                    TextView tv6= (TextView)v1.findViewById(R.id.tv6);
                    TextView tv7= (TextView)v1.findViewById(R.id.tv7);
                    LinearLayout tv11= (LinearLayout)v1.findViewById(R.id.tv11);
                    LinearLayout tv12= (LinearLayout)v1.findViewById(R.id.tv12);
                    LinearLayout tv13= (LinearLayout)v1.findViewById(R.id.tv13);
                    LinearLayout tv14= (LinearLayout)v1.findViewById(R.id.tv14);
                    LinearLayout tv15= (LinearLayout)v1.findViewById(R.id.tv15);
                    LinearLayout tv16= (LinearLayout)v1.findViewById(R.id.tv16);
                    LinearLayout tv17= (LinearLayout)v1.findViewById(R.id.tv17);

                    for (int i = 0; i < 7; i++) {
                        dateWithoutTime = first.getTime();
                        String day=dateWithoutTime.toString();
                        String[] days=day.split(" ");
                        weekdays[i]=days[2];
                        first.add(Calendar.DAY_OF_WEEK, 1);
                    }
                    tv1.setText(weekdays[0]);
                    tv2.setText(weekdays[1]);
                    tv3.setText(weekdays[2]);
                    tv4.setText(weekdays[3]);
                    tv5.setText(weekdays[4]);
                    tv6.setText(weekdays[5]);
                    tv7.setText(weekdays[6]);
                    if(todayDate[0].equalsIgnoreCase("sun")) {
                        tv11.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                    }
                    else if(todayDate[0].equalsIgnoreCase("Mon")) {
                        tv12.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                    }
                    else if(todayDate[0].equalsIgnoreCase("Tue")) {
                        tv13.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                    }
                    else if(todayDate[0].equalsIgnoreCase("Wed")) {
                        tv14.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                    }
                    else if(todayDate[0].equalsIgnoreCase("Thu")) {
                        tv15.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                    }
                    else if(todayDate[0].equalsIgnoreCase("Fri")) {
                        tv16.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                    }
                    else if(todayDate[0].equalsIgnoreCase("Sat")) {
                        tv17.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                    }

                    summary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rl.removeAllViews();
                            plusBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_search));
                            if (isSearch=false){
                            isSearch=false;}
                            if (view != null){
                                InputMethodManager imm=(InputMethodManager)mainContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);}
                            plusBtn.setVisibility(View.GONE);
                            clearAllBtn.setVisibility(View.GONE);
                            ed_search.setVisibility(View.GONE);
                            title.setText("DASHBOARD");
                            title.setVisibility(View.VISIBLE);
                            ed_search.setText("");
                            im_summary.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_summary_white));
                            im_notification.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_notification));
                            tv_summary.setTextColor(getResources().getColor(R.color.white));
                            tv_notification.setTextColor(getResources().getColor(R.color.black));
                            view_summary.setVisibility(View.VISIBLE);
                            view_notification.setVisibility(View.GONE);
                            LayoutInflater layoutInflater = (LayoutInflater)mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v1 = layoutInflater.inflate(R.layout.summary, rl);
                            call_tv=(TextView)v1.findViewById(R.id.call_tv);
                            chat_tv=(TextView)v1.findViewById(R.id.chat_tv);
                            file_tv=(TextView)v1.findViewById(R.id.file_tv);
                            exfile_tv=(TextView)v1.findViewById(R.id.exfile_tv);
                            ll_warn = (LinearLayout)v1.findViewById(R.id.ll_warn);
                            ll_msg = (LinearLayout)v1.findViewById(R.id.ll_msg);
                            ll_call = (LinearLayout)v1.findViewById(R.id.ll_call);
                            ll_file = (LinearLayout)v1.findViewById(R.id.ll_file);
                            piechart = (PieChart) v1.findViewById(R.id.piechart);
                            freeSpace= (TextView) v1.findViewById(R.id.freeSpace);
                            totalSize= (TextView) v1.findViewById(R.id.totalSize);
                            showMemoryControl();
                            ll_warn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setType = "ll_warn";
                                    notification.performClick();
                                }
                            });
                            ll_msg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setType = "ll_msg";
                                    notification.performClick();
//                                    DBAccess.getdbHeler(mainContext)
//                                            .setReadAllCount(CallDispatcher.LoginUser, "");
                                }
                            });
                            ll_call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setType = "ll_call";
                                    notification.performClick();
                                    DBAccess.getdbHeler(mainContext)
                                            .setReadAllCount(CallDispatcher.LoginUser, "call");
                                }
                            });
                            ll_file.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setType = "ll_file";
                                    notification.performClick();
                                    DBAccess.getdbHeler(mainContext)
                                            .setReadAllCount(CallDispatcher.LoginUser, "file");
                                }
                            });
                            piechart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MemoryControlFragment memoryControlFragment = MemoryControlFragment.newInstance(mainContext);
                                    FragmentManager fragmentManager = SingleInstance.mainContext
                                            .getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(
                                            R.id.activity_main_content_fragment, memoryControlFragment)
                                            .commitAllowingStateLoss();
                                }
                            });
                            c1 = Calendar.getInstance();
                            updateCount();

                            Calendar first = (Calendar) c1.clone();
                            first.add(Calendar.DAY_OF_WEEK,
                                    first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                            Date dateWithoutTime = null;
                            try {
                                dateWithoutTime = sdf1.parse(sdf1.format(new Date()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String today=dateWithoutTime.toString();
                            String[] todayDate=today.split(" ");
                            String[] weekdays=new String[7];
                            TextView tv1= (TextView)v1.findViewById(R.id.tv1);
                            TextView tv2= (TextView)v1.findViewById(R.id.tv2);
                            TextView tv3= (TextView)v1.findViewById(R.id.tv3);
                            TextView tv4= (TextView)v1.findViewById(R.id.tv4);
                            TextView tv5= (TextView)v1.findViewById(R.id.tv5);
                            TextView tv6= (TextView)v1.findViewById(R.id.tv6);
                            TextView tv7= (TextView)v1.findViewById(R.id.tv7);
                            LinearLayout tv11= (LinearLayout)v1.findViewById(R.id.tv11);
                            LinearLayout tv12= (LinearLayout)v1.findViewById(R.id.tv12);
                            LinearLayout tv13= (LinearLayout)v1.findViewById(R.id.tv13);
                            LinearLayout tv14= (LinearLayout)v1.findViewById(R.id.tv14);
                            LinearLayout tv15= (LinearLayout)v1.findViewById(R.id.tv15);
                            LinearLayout tv16= (LinearLayout)v1.findViewById(R.id.tv16);
                            LinearLayout tv17= (LinearLayout)v1.findViewById(R.id.tv17);

                            for (int i = 0; i < 7; i++) {
                                dateWithoutTime = first.getTime();
                                String day=dateWithoutTime.toString();
                                String[] days=day.split(" ");
                                weekdays[i]=days[2];
                                first.add(Calendar.DAY_OF_WEEK, 1);
                            }
                            tv1.setText(weekdays[0]);
                            tv2.setText(weekdays[1]);
                            tv3.setText(weekdays[2]);
                            tv4.setText(weekdays[3]);
                            tv5.setText(weekdays[4]);
                            tv6.setText(weekdays[5]);
                            tv7.setText(weekdays[6]);
                            if(todayDate[0].equalsIgnoreCase("sun")) {
                                tv11.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                            }
                            else if(todayDate[0].equalsIgnoreCase("Mon")) {
                                tv12.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                            }
                            else if(todayDate[0].equalsIgnoreCase("Tue")) {
                                tv13.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                            }
                            else if(todayDate[0].equalsIgnoreCase("Wed")) {
                                tv14.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                            }
                            else if(todayDate[0].equalsIgnoreCase("Thu")) {
                                tv15.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                            }
                            else if(todayDate[0].equalsIgnoreCase("Fri")) {
                                tv16.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                            }
                            else if(todayDate[0].equalsIgnoreCase("Sat")) {
                                tv17.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
                            }
                            updateCount();
                        }
                    });

                    notification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                rl.removeAllViews();
                                SingleInstance.mainContext.notifyUI();

//                                clearAllBtn.setVisibility(View.VISIBLE);
                                im_summary.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_summary));
                                im_notification.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_notification_white));
                                tv_summary.setTextColor(getResources().getColor(R.color.black));
                                tv_notification.setTextColor(getResources().getColor(R.color.white));
                                view_summary.setVisibility(View.GONE);
                                view_notification.setVisibility(View.VISIBLE);
                                LayoutInflater layoutInflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View v2 = layoutInflater.inflate(R.layout.notification, rl);
                                LinearLayout no_notify=(LinearLayout)v2.findViewById(R.id.no_notify);
                                notifylistview = (ListView) v2.findViewById(R.id.notify_list);
//                                LoadFilesList(CallDispatcher.LoginUser);
//                                notifyAdapter = new NotifyListAdapter(mainContext, notificationList());
//                                notifylistview.setAdapter(notifyAdapter);
//                                notifyAdapter.isFromOther(true);
//                                notifyAdapter.notifyDataSetChanged();
                                clearAllBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        DBAccess.getdbHeler(mainContext)
                                                .setReadAllCount(CallDispatcher.LoginUser,"");
                                        notifyAdapter.notifyDataSetChanged();
                                    }
                                });
                                rl_notify = (RelativeLayout) v2.findViewById(R.id.rl_notify);
                                tv_rl_notify = (TextView) v2.findViewById(R.id.tv_rl_notify);
                                bt_rl_notify = (Button) v2.findViewById(R.id.bt_rl_notify);
                                rl_notify.setVisibility(View.VISIBLE);
                                tv_rl_notify.setVisibility(View.VISIBLE);
                                bt_rl_notify.setVisibility(View.VISIBLE);

//                                int count1 = DBAccess.getdbHeler(mainContext)
//                                        .getUnreadMsgCount(CallDispatcher.LoginUser);
//                                int count2 = DBAccess.getdbHeler(mainContext)
//                                        .getUnreadFileCount(CallDispatcher.LoginUser);
//                                int count3 = DBAccess.getdbHeler(mainContext)
//                                        .getUnreadCallCount(CallDispatcher.LoginUser);

                                int count1=0;int count3=0;int count2=0;
                                for(NotifyListBean notifyListBean: notificationList()){
                                    if(notifyListBean.getChatcount()!=null &&
                                            !notifyListBean.getChatcount().equalsIgnoreCase("0") && !notifyListBean.getChatcount().equalsIgnoreCase("")) {
                                        if (count1 == 0) {
                                            count1 = Integer.parseInt(notifyListBean.getChatcount());
                                        } else {
                                            count1 = count1 + Integer.parseInt(notifyListBean.getChatcount());
                                        }
                                        if(notifyListBean.getCallcount()!=null &&
                                                !notifyListBean.getCallcount().equalsIgnoreCase("0") && !notifyListBean.getCallcount().equalsIgnoreCase("")) {
                                            if (count3 == 0) {
                                                count3 = Integer.parseInt(notifyListBean.getCallcount());
                                            } else {
                                                count3 = count3 + Integer.parseInt(notifyListBean.getCallcount());
                                            }
                                        }
                                        if(notifyListBean.getFilecount()!=null &&
                                                !notifyListBean.getFilecount().equalsIgnoreCase("0") && !notifyListBean.getFilecount().equalsIgnoreCase("")) {
                                            if (count2 == 0) {
                                                count2 = Integer.parseInt(notifyListBean.getFilecount());
                                            } else {
                                                count2 = count2 + Integer.parseInt(notifyListBean.getFilecount());
                                            }
                                        }
                                    }else if(notifyListBean.getCallcount()!=null &&
                                            !notifyListBean.getCallcount().equalsIgnoreCase("0") && !notifyListBean.getCallcount().equalsIgnoreCase("")) {
                                        if (count3 == 0) {
                                            count3 = Integer.parseInt(notifyListBean.getCallcount());
                                        } else {
                                            count3 = count3 + Integer.parseInt(notifyListBean.getCallcount());
                                        }

                                        if(notifyListBean.getFilecount()!=null &&
                                                !notifyListBean.getFilecount().equalsIgnoreCase("0") && !notifyListBean.getFilecount().equalsIgnoreCase("")) {
                                            if (count2 == 0) {
                                                count2 = Integer.parseInt(notifyListBean.getFilecount());
                                            } else {
                                                count2 = count2 + Integer.parseInt(notifyListBean.getFilecount());
                                            }
                                        }
                                    }else  if(notifyListBean.getFilecount()!=null &&
                                            !notifyListBean.getFilecount().equalsIgnoreCase("0") && !notifyListBean.getFilecount().equalsIgnoreCase("")) {
                                        if (count2 == 0) {
                                            count2 = Integer.parseInt(notifyListBean.getFilecount());
                                        } else {
                                            count2 = count2 + Integer.parseInt(notifyListBean.getFilecount());
                                        }
                                    }
                                }

                                if (setType.equals("ll_warn")) {
                                    rl_notify.setBackgroundColor(getResources().getColor(R.color.yellow));
                                    tv_rl_notify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dashboard_attention_white, 0, 0, 0);
                                    if(count2>1) {
                                        tv_rl_notify.setText(count2+" warnings");
                                    }else{
                                        tv_rl_notify.setText(count2+" warning");
                                    }
                                } else if (setType.equals("ll_msg")) {
                                    rl_notify.setBackgroundColor(getResources().getColor(R.color.pink));
                                    tv_rl_notify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dashboard_message_white, 0, 0, 0);
                                    if(count1>1) {
                                        tv_rl_notify.setText(count1+" unread messages");
                                    }else{
                                        tv_rl_notify.setText(count1+" unread message");
                                    }
                                    Vector<NotifyListBean> msg_list=(Vector<NotifyListBean>) notificationList().clone();
                                    tempnotifylist.clear();
                                    for(NotifyListBean notifyListBean:msg_list){
                                        if(notifyListBean.getChatcount()!=null &&
                                                !notifyListBean.getChatcount().equalsIgnoreCase("0") && !notifyListBean.getChatcount().equalsIgnoreCase("")){
                                            tempnotifylist.add(notifyListBean);
                                        }
                                    }

                                } else if (setType.equals("ll_call")) {
                                    rl_notify.setBackgroundColor(getResources().getColor(R.color.green));
                                    tv_rl_notify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dashboard_call_white, 0, 0, 0);
                                    if(count3>1) {
                                        tv_rl_notify.setText(count3+" missed calls");
                                    }else{
                                        tv_rl_notify.setText(count3 + " missed call");
                                    }
                                    Vector<NotifyListBean> call_list=(Vector<NotifyListBean>) notificationList().clone();
                                    tempnotifylist.clear();
                                    for(NotifyListBean notifyListBean:call_list){
                                        if(notifyListBean.getCallcount()!=null &&
                                                !notifyListBean.getCallcount().equalsIgnoreCase("0") && !notifyListBean.getCallcount().equalsIgnoreCase("")){
                                            tempnotifylist.add(notifyListBean);
                                        }
                                    }
                                } else if (setType.equals("ll_file")) {
                                    rl_notify.setBackgroundColor(getResources().getColor(R.color.orange));
                                    tv_rl_notify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dashboard_file_white, 0, 0, 0);
                                    if(count2>1) {
                                        tv_rl_notify.setText(count2+" new files");
                                    }else{
                                        tv_rl_notify.setText(count2+" new file");
                                    }
                                    Vector<NotifyListBean> msg_list=(Vector<NotifyListBean>) notificationList().clone();
                                    tempnotifylist.clear();
                                    for(NotifyListBean notifyListBean:msg_list){
                                        if(notifyListBean.getFilecount()!=null &&
                                                !notifyListBean.getFilecount().equalsIgnoreCase("0") && !notifyListBean.getFilecount().equalsIgnoreCase("")){
                                            tempnotifylist.add(notifyListBean);
                                        }
                                    }
                                } else {
                                    rl_notify.setVisibility(View.GONE);
                                    setType = "";
                                    tempnotifylist.clear();
                                    tempnotifylist=notificationList();

                                }
                                bt_rl_notify.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        rl_notify.setVisibility(View.GONE);
                                        setType = "";
                                        summary.performClick();
                                        LoadFilesList(CallDispatcher.LoginUser);
                                    }
                                });

                                if ( tempnotifylist != null &&  tempnotifylist.size() > 0){
                                    plusBtn.setVisibility(View.VISIBLE);}
                                else {plusBtn.setVisibility(View.GONE);}

                                if(tempnotifylist.size()>0) {
                                    notifylistview.setVisibility(View.VISIBLE);
                                    no_notify.setVisibility(View.GONE);
                                }else {
                                    notifylistview.setVisibility(View.GONE);
                                    no_notify.setVisibility(View.VISIBLE);
                                    rl_notify.setVisibility(View.GONE);
                                }
                                seacrhnotifylist.clear();
                                seacrhnotifylist=(Vector<NotifyListBean>)tempnotifylist.clone();
                                notifyAdapter = new NotifyListAdapter(mainContext, tempnotifylist);
                                notifylistview.setAdapter(notifyAdapter);
                                notifyAdapter.isFromOther(true);
                                notifyAdapter.notifyDataSetChanged();

//                                notifylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                                        NotifyListBean notifyBean = (NotifyListBean) notifyAdapter
//                                                .getItem(position);
//                                        Log.i("AAAA", "NOTIFY LIST from user1 " + notifyBean.getFileid());
//                                        if (notifyBean.getNotifttype().equalsIgnoreCase("C")) {
////                                            appMainActivity.historyfragment();
//                                        } else if (notifyBean.getNotifttype().equalsIgnoreCase("I")) {
////                                            rl_notify.setVisibility(View.GONE);
//                                            if(notifyBean.getCategory().equalsIgnoreCase("G")){
//                                                Intent intent = new Intent(mainContext,
//                                                        GroupChatActivity.class);
//                                                intent.putExtra("isGroup", true);
//                                                intent.putExtra("groupid",
//                                                        notifyBean.getFileid());
//                                                startActivity(intent);
//                                            }else {
//                                                Intent intent = new Intent(mainContext,
//                                                        GroupChatActivity.class);
//                                                intent.putExtra("isGroup", false);
//                                                intent.putExtra("buddy",
//                                                        notifyBean.getFileid());
//                                                startActivity(intent);
//                                            }
//                                        } else if (notifyBean.getNotifttype().equalsIgnoreCase("F")) {
//                                            appMainActivity.fileFragment();
//                                        }
//                                    }
//                                });
                                ed_search.addTextChangedListener(new TextWatcher() {

                                    public void afterTextChanged(Editable s) {
                                    }

                                    public void beforeTextChanged(CharSequence s, int start,
                                                                  int count, int after) {
                                    }

                                    public void onTextChanged(CharSequence s, int start,
                                                              int before, int count) {

                                        String text = ed_search.getText().toString()
                                                .toLowerCase(Locale.getDefault());
                                        notifyAdapter.filter(text);
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final TextView title = (TextView) getActivity().findViewById(
                R.id.activity_main_content_title);
        title.setVisibility(View.VISIBLE);
        ed_search.setVisibility(View.GONE);
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static float getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize)/1024;
    }

    public static float getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return (totalBlocks * blockSize)/1024;
    }

    public static float getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return (availableBlocks * blockSize)/1024;
        } else {
            return 0;
        }
    }

    public static float getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return (totalBlocks * blockSize)/1024;
        } else {
            return 0;
        }
    }

    public static String formatSize(float size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " MB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " GB";
                size /= 1024;
            }
        }

        String resultBuffer = String.format("%.2f", size);

        if (suffix != null) resultBuffer += suffix;
        return resultBuffer.toString();
    }

    public Vector<NotifyListBean> LoadFilesList(String username)
    {
        Log.i("Multi", "username : " + username);
//        tempnotifylist.clear();
        fileList.clear();
        notifyList = DBAccess.getdbHeler()
                .getNotifyFilesList(username);
        //Old Code for All entries
        //Start
       /* if(notifyList!=null) {
            if (setType.equals("ll_warn")) {
                for(NotifyListBean nBean:notifyList) {
                    Log.i("AAAA","NOTIFY LIST from user "+nBean.getNotifttype()+" , "+nBean.getSortdate()+" , "+nBean.getFrom());
                    Log.d("AAAA", "Notifyid"+nBean.getFileid());
                    if(nBean.getViewed()==0 && nBean.getNotifttype().equals("F")) {
                        ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                        nBean.setProfilePic(pBean.getPhoto());
                        if(pBean!=null)
                            nBean.setUsername(pBean.getFirstname()+" "+pBean.getLastname());
                        tempnotifylist.add(nBean);
                        seacrhnotifylist.add(nBean);
                    }
                }
            } else if (setType.equals("ll_msg")) {
                for(NotifyListBean nBean:notifyList) {
                    Log.i("AAAA","NOTIFY LIST from user "+nBean.getNotifttype()+" , "+nBean.getSortdate()+" , "+nBean.getFrom());
                    Log.d("AAAA", "Notifyid" + nBean.getFileid());
                    if(nBean.getViewed()==0 && nBean.getNotifttype().equals("I")) {
                        if(!nBean.getCategory().equalsIgnoreCase("call")) {
                            ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                            nBean.setProfilePic(pBean.getPhoto());
                            if (pBean != null)
                                nBean.setUsername(pBean.getFirstname() + " " + pBean.getLastname());
                            tempnotifylist.add(nBean);
                            seacrhnotifylist.add(nBean);
                        }
                    }
                }
            } else if (setType.equals("ll_call")) {
                for(NotifyListBean nBean:notifyList) {
                    Log.i("AAAA","NOTIFY LIST from user "+nBean.getNotifttype()+" , "+nBean.getSortdate()+" , "+nBean.getFrom());
                    Log.d("AAAA", "Notifyid" + nBean.getFileid());
                    if(nBean.getViewed()==0 && nBean.getNotifttype().equals("C")) {
                        ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                        nBean.setProfilePic(pBean.getPhoto());
                        if(pBean!=null)
                            nBean.setUsername(pBean.getFirstname()+" "+pBean.getLastname());
                        tempnotifylist.add(nBean);
                        seacrhnotifylist.add(nBean);
                    }
                }
            } else if (setType.equals("ll_file")) {
                for(NotifyListBean nBean:notifyList) {
                    Log.i("AAAA","NOTIFY LIST from user "+nBean.getNotifttype()+" , "+nBean.getSortdate()+" , "+nBean.getFrom());
                    Log.d("AAAA", "Notifyid" + nBean.getFileid());
                    if(nBean.getViewed()==0 && nBean.getNotifttype().equals("F")) {
                        ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                        nBean.setProfilePic(pBean.getPhoto());
                        if(pBean!=null)
                            nBean.setUsername(pBean.getFirstname()+" "+pBean.getLastname());
                        tempnotifylist.add(nBean);
                        seacrhnotifylist.add(nBean);
                    }
                }
            } else {
                for(NotifyListBean nBean:notifyList) {
                    Log.i("AAAA","NOTIFY LIST from user "+nBean.getNotifttype()+" , "+nBean.getSortdate()+" , "+nBean.getFrom());
                    Log.d("AAAA", "Notifyid" + nBean.getFileid());
//                    if(nBean.getViewed()==0) {
//                        if(nBean.getCategory()!=null&&!nBean.getCategory().equalsIgnoreCase("call")) {
//                            ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
//                            nBean.setProfilePic(pBean.getPhoto());
//                            if (pBean != null)
//                                nBean.setUsername(pBean.getFirstname() + " " + pBean.getLastname());
//                            tempnotifylist.add(nBean);
//                            seacrhnotifylist.add(nBean);
//                        }
//                    }
                    if(nBean.getViewed()==0 && nBean.getNotifttype().equals("F")) {
                        ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                        nBean.setProfilePic(pBean.getPhoto());
                        if(pBean!=null)
                            nBean.setUsername(pBean.getFirstname()+" "+pBean.getLastname());
                        tempnotifylist.add(nBean);
						seacrhnotifylist.add(nBean);
                    }
                    if(nBean.getViewed()==0 && nBean.getNotifttype().equals("I")) {
                        if(!nBean.getCategory().equalsIgnoreCase("call")) {
                            ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                            nBean.setProfilePic(pBean.getPhoto());
                            if (pBean != null)
                                nBean.setUsername(pBean.getFirstname() + " " + pBean.getLastname());
                            tempnotifylist.add(nBean);
							seacrhnotifylist.add(nBean);
                        }else if(nBean.getCategory().equalsIgnoreCase("call")){
                            ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                            nBean.setProfilePic(pBean.getPhoto());
                            if (pBean != null)
                                nBean.setUsername(pBean.getFirstname() + " " + pBean.getLastname());
                            tempnotifylist.add(nBean);
                            seacrhnotifylist.add(nBean);
                        }
                    }
                    if(nBean.getViewed()==0 && nBean.getNotifttype().equals("C")) {
                        ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                        nBean.setProfilePic(pBean.getPhoto());
                        if(pBean!=null)
                            nBean.setUsername(pBean.getFirstname()+" "+pBean.getLastname());
                        tempnotifylist.add(nBean);
						seacrhnotifylist.add(nBean);
                    }
                }
            }
        }
        Collections.sort(tempnotifylist, new DateComparator());
        Collections.sort(seacrhnotifylist, new DateComparator());
            return listcount();*/
        //End

        //New enrty for File
        //Start
        if(notifyList!=null){
            for(NotifyListBean nBean:notifyList) {
                Log.i("AAAA","NOTIFY LIST from user "+nBean.getNotifttype()+" , "+nBean.getSortdate()+" , "+nBean.getFrom());
                Log.d("AAAA", "Notifyid" + nBean.getFileid());
                if(nBean.getViewed()==0 && nBean.getNotifttype().equalsIgnoreCase("F")) {
                    ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(nBean.getFrom());
                    if(pBean!=null) {
                        nBean.setUsername(pBean.getFirstname() + " " + pBean.getLastname());
                        nBean.setProfilePic(pBean.getPhoto());
                    }
                    fileList.add(nBean);
                }
            }
        }
        return fileList;
        //End

    }

    private void viewFileInfo(CompleteListBean cBean, int position) {
        try {
            if (cBean.getViewmode() == 0) {
                String strUpdateNote = "update component set viewmode='" + 1
                        + "' where componentid='" + cBean.getComponentId()
                        + "'";
                Log.d("qry", strUpdateNote);

                if (DBAccess.getdbHeler().ExecuteQuery(strUpdateNote)) {
                    cBean.setViewMode(1);
                    // filesAdapter.changeReadStatus();
                    // filesAdapter.notifyDataSetChanged();
                }
            }
            Intent intentComponent = new Intent(mainContext, ComponentCreator.class);
            Bundle bndl = new Bundle();
            bndl.putString("type", cBean.getcomponentType());
            bndl.putBoolean("action", false);
            bndl.putInt("position", position);
            intentComponent.putExtra("viewBean", cBean);
            intentComponent.putExtras(bndl);
            callDisp.cmp = cBean;
            startActivity(intentComponent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }
    }

    public View getParentView() {
        return view;
    }

    public void updateCount()
    {
        int count1=0, count2=0, count3=0;
//        count1 = DBAccess.getdbHeler(mainContext)
//                .getUnreadMsgCount(CallDispatcher.LoginUser);
//        count2 = DBAccess.getdbHeler(mainContext)
//                .getUnreadFileCount(CallDispatcher.LoginUser);
//        count3 = DBAccess.getdbHeler(mainContext)
//                .getUnreadCallCount(CallDispatcher.LoginUser);

        //New Count Entry
        //Start
        Vector<NotifyListBean> countlist=notificationList();
        for(NotifyListBean notifyListBean: countlist){
            if(notifyListBean.getChatcount()!=null &&
                    !notifyListBean.getChatcount().equalsIgnoreCase("0") && !notifyListBean.getChatcount().equalsIgnoreCase("")) {
                if (count1 == 0) {
                    count1 = Integer.parseInt(notifyListBean.getChatcount());
                } else {
                    count1 = count1 + Integer.parseInt(notifyListBean.getChatcount());
                }
                if(notifyListBean.getCallcount()!=null &&
                        !notifyListBean.getCallcount().equalsIgnoreCase("0") && !notifyListBean.getCallcount().equalsIgnoreCase("")) {
                    if (count3 == 0) {
                        count3 = Integer.parseInt(notifyListBean.getCallcount());
                    } else {
                        count3 = count3 + Integer.parseInt(notifyListBean.getCallcount());
                    }
                }
                if(notifyListBean.getFilecount()!=null &&
                        !notifyListBean.getFilecount().equalsIgnoreCase("0") && !notifyListBean.getFilecount().equalsIgnoreCase("")) {
                    if (count2 == 0) {
                        count2 = Integer.parseInt(notifyListBean.getFilecount());
                    } else {
                        count2 = count2 + Integer.parseInt(notifyListBean.getFilecount());
                    }
                }
            }else if(notifyListBean.getCallcount()!=null &&
                    !notifyListBean.getCallcount().equalsIgnoreCase("0") && !notifyListBean.getCallcount().equalsIgnoreCase("")) {
                if (count3 == 0) {
                    count3 = Integer.parseInt(notifyListBean.getCallcount());
                } else {
                    count3 = count3 + Integer.parseInt(notifyListBean.getCallcount());
                }
                if(notifyListBean.getFilecount()!=null &&
                        !notifyListBean.getFilecount().equalsIgnoreCase("0") && !notifyListBean.getFilecount().equalsIgnoreCase("")) {
                    if (count2 == 0) {
                        count2 = Integer.parseInt(notifyListBean.getFilecount());
                    } else {
                        count2 = count2 + Integer.parseInt(notifyListBean.getFilecount());
                    }
                }
            }else  if(notifyListBean.getFilecount()!=null &&
                    !notifyListBean.getFilecount().equalsIgnoreCase("0") && !notifyListBean.getFilecount().equalsIgnoreCase("")) {
                if (count2 == 0) {
                    count2 = Integer.parseInt(notifyListBean.getFilecount());
                } else {
                    count2 = count2 + Integer.parseInt(notifyListBean.getFilecount());
                }
            }
        }
        //End

        if(count3 > 0) {
            call_tv.setText(Integer.toString(count3));
            ll_call.setVisibility(View.VISIBLE);
        }else{
            ll_call.setVisibility(View.GONE);
        }
        if(count2 > 0) {
            file_tv.setText(Integer.toString(count2));
            ll_file.setVisibility(View.VISIBLE);
        }else{
            ll_file.setVisibility(View.GONE);
        }
        if(count1 > 0) {
            chat_tv.setText(Integer.toString(count1));
            ll_msg.setVisibility(View.VISIBLE);
        }else{
            ll_msg.setVisibility(View.GONE);
        }
        ll_warn.setVisibility(View.GONE);






    }
    protected void ShowToast(String string) {
        // TODO Auto-generated method stub
        Toast.makeText(mainContext, string, Toast.LENGTH_SHORT).show();
    }
    private int getMemorySize()
    {
        fBeanList=SingleInstance.fileDetails;
        for(FileDetailsBean fBean:fBeanList) {
            if (fBean.getBranchtype().equalsIgnoreCase("file")) {
                if (fBean.getAudiofiles() != null)
                    audio = Integer.parseInt(fBean.getAudiofiles());
                if (fBean.getVideofiles() != null)
                    videos = Integer.parseInt(fBean.getVideofiles());
                if (fBean.getImagefiles() != null)
                    image = Integer.parseInt(fBean.getImagefiles());
            }else if(fBean.getBranchtype().equalsIgnoreCase("im")) {
                if (fBean.getTotalsize() != null)
                    chat = Integer.parseInt(fBean.getTotalsize());
            }else if(fBean.getBranchtype().equalsIgnoreCase("other")) {
                if (fBean.getTotalsize() != null)
                    other = Integer.parseInt(fBean.getTotalsize());
            }
        }
        int total=audio+videos+image+other+chat;
        Log.i("AAAA", "Omorycontrol &************ size " + total);
        return total;
    }
    String bytesToSize(float bytes) {
        float kilobyte = 1024;
        float megabyte = kilobyte * 1024;
        float gigabyte = megabyte * 1024;
        float terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return  new BigDecimal(String.valueOf(bytes)).setScale(2, BigDecimal.ROUND_DOWN) + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return  new BigDecimal(String.valueOf((bytes / kilobyte))).setScale(2, BigDecimal.ROUND_DOWN) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return  new BigDecimal(String.valueOf((bytes / megabyte))).setScale(2, BigDecimal.ROUND_DOWN) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return  new BigDecimal(String.valueOf((bytes / gigabyte))).setScale(2, BigDecimal.ROUND_DOWN) + " GB";

        } else if (bytes >= terabyte) {
            return  new BigDecimal(String.valueOf((bytes / terabyte))).setScale(2, BigDecimal.ROUND_DOWN) + " TB";

        } else {
            return  new BigDecimal(String.valueOf(bytes)).setScale(2, BigDecimal.ROUND_DOWN) + " B";
        }
    }
    void addShowHideListener( Boolean isAudio ) {
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
    public void showMemoryControl()
    {
        if(getMemorySize()>0) {
            float total = 1048576L;
            float free = total-getMemorySize();
            float num = 5368709120L;
            float temp1 = num - getMemorySize();
            freeSpace.setText(bytesToSize(temp1));
            float temp = free / total;
            piechart.setPercentage(temp * 100);
        }
    }

    private Vector<NotifyListBean> listcount(){
        HashMap<String, Integer> fileslist = new HashMap<String, Integer>();
        HashMap<String, Integer> chatlist = new HashMap<String, Integer>();
        HashMap<String, Integer> calllist = new HashMap<String, Integer>();

        int i = 0, j = 0, k = 0;
        HashMap<String, NotifyListBean> usernotifylist = new HashMap<String, NotifyListBean>();
        for (NotifyListBean nbean : tempnotifylist) {
            if (nbean.getViewed() == 0) {
                if (nbean.getNotifttype().equalsIgnoreCase("F")) {
                    if (fileslist.containsKey(nbean.getFrom()))
                        fileslist.put(nbean.getFrom(), ++i);
                    else {
                        i = 0;
                        fileslist.put(nbean.getFrom(), ++i);
                    }
                } else if (nbean.getNotifttype().equalsIgnoreCase("I")) {
                    Log.d("chatlist", "entry");
                    if (nbean.getCategory()!=null && (nbean.getCategory().equalsIgnoreCase("I") ||nbean.getCategory().equalsIgnoreCase("g"))) {
                        if (chatlist.containsKey(nbean.getFrom())) {
                            Log.d("chatlist", "entries" + nbean.getFrom());
                            Log.d("chatlist", "entry1");
                            chatlist.put(nbean.getFrom(), ++j);
                        } else {
                            Log.d("chatlist", "entry2");
                            if (nbean.getUnreadchat() != null && !nbean.getUnreadchat().equalsIgnoreCase("0")) {
                                j = (Integer.parseInt(nbean.getUnreadchat()) - 1);
                            } else {
                                j = 0;
                            }
                            chatlist.put(nbean.getFrom(), ++j);
                        }
                    }
                    //For this add call count in Indijual
                    else if (nbean.getCategory().equalsIgnoreCase("call")) {
                        if (calllist.containsKey(nbean.getFrom())) {
                            calllist.put(nbean.getFrom(), ++k);
                        } else {
                            k = 0;
                            calllist.put(nbean.getFrom(), ++k);
                        }
                    }
                } else if (nbean.getNotifttype().equalsIgnoreCase("C")) {
                    if (calllist.containsKey(nbean.getFrom())) {
                        calllist.put(nbean.getFrom(), ++k);
                    } else {
                        k = 0;
                        calllist.put(nbean.getFrom(), ++k);
                    }
                }
            }
//            if(!usernotifylist.containsKey(nbean.getFrom()))
                usernotifylist.put(nbean.getFrom(), nbean);
        }
        Vector<NotifyListBean> templist=new Vector<NotifyListBean>();
        templist.addAll(usernotifylist.values());
        for (NotifyListBean bean : templist) {
            if (chatlist.get(bean.getFrom()) != null)
                bean.setChatcount(String.valueOf(chatlist.get(bean.getFrom())));
            if (calllist.get(bean.getFrom()) != null)
                bean.setCallcount(String.valueOf(calllist.get(bean.getFrom())));
            if (fileslist.get(bean.getFrom()) != null)
                bean.setFilecount(String.valueOf(fileslist.get(bean.getFrom())));
        }
        Log.d("listcount","fileslist"+fileslist.size());
        Log.d("listcount","chatlist"+chatlist.size());
        Log.d("listcount","calllist"+calllist.size());
        return templist;
    }


    public Vector<NotifyListBean> notificationList(){
        tempnotifylist.clear();
        if(DBAccess.getdbHeler().getChatRecentList(CallDispatcher.LoginUser,false)!=null) {
            Vector<NotifyListBean> templist=DBAccess.getdbHeler().getChatRecentList(CallDispatcher.LoginUser,false);
            for(NotifyListBean notifyListBean:templist){
                if(notifyListBean.getChatcount()!=null &&
                        !notifyListBean.getChatcount().equalsIgnoreCase("0") && !notifyListBean.getChatcount().equalsIgnoreCase("")){
                    tempnotifylist.add(notifyListBean);
                }else if(notifyListBean.getCallcount()!=null &&
                        !notifyListBean.getCallcount().equalsIgnoreCase("0") && !notifyListBean.getCallcount().equalsIgnoreCase("")){
                    tempnotifylist.add(notifyListBean);
                }
            }
        }

        if(LoadFilesList(CallDispatcher.LoginUser)!=null && LoadFilesList(CallDispatcher.LoginUser).size()>0) {
            HashMap<String, NotifyListBean> fileEntry = new HashMap<>();
            for (NotifyListBean notifyListBean : (Vector<NotifyListBean>) tempnotifylist.clone()) {
                fileEntry.put(notifyListBean.getFileid(), notifyListBean);
            }
            HashMap<String, Integer> fileslistcpunt = new HashMap<String, Integer>();
            int i = 0;
            for (NotifyListBean notifyListBean1 : fileList) {
                if (notifyListBean1.getFrom() != null) {
                    if (fileEntry.containsKey(notifyListBean1.getFrom())) {
                        NotifyListBean notifyListBean = fileEntry.get(notifyListBean1.getFrom());
                        tempnotifylist.remove(notifyListBean);
                        if (fileslistcpunt.containsKey(notifyListBean1.getFrom())) {
                            fileslistcpunt.put(notifyListBean1.getFrom(), ++i);
                        } else {
                            i = 0;
                            fileslistcpunt.put(notifyListBean1.getFrom(), ++i);
                        }
                        notifyListBean.setFilecount(String.valueOf(fileslistcpunt.get(notifyListBean1.getFrom())));
                        tempnotifylist.add(notifyListBean);
                    } else {
                        if (fileslistcpunt.containsKey(notifyListBean1.getFrom())) {
                            fileslistcpunt.put(notifyListBean1.getFrom(), ++i);
                            tempnotifylist.remove(notifyListBean1);
                            notifyListBean1.setFilecount(String.valueOf(fileslistcpunt.get(notifyListBean1.getFrom())));
                            tempnotifylist.add(notifyListBean1);
                        } else {
                            i = 0;
                            fileslistcpunt.put(notifyListBean1.getFrom(), ++i);
                            notifyListBean1.setFilecount(String.valueOf(fileslistcpunt.get(notifyListBean1.getFrom())));
                            tempnotifylist.add(notifyListBean1);
                        }
                    }
                }
            }
        }

        Vector<BuddyInformationBean> buddylist=ContactsFragment.getBuddyList();
        Vector<NotifyListBean> onlineStatus=(Vector<NotifyListBean>)tempnotifylist.clone();
        for (NotifyListBean bean : onlineStatus) {
            innerloop:
            for(BuddyInformationBean buddyInformationBean:buddylist) {
                if(bean.getNotifttype()!=null && bean.getNotifttype().equalsIgnoreCase("I")) {
                    if (buddyInformationBean.getName() != null && bean.getFileid() != null &&
                            buddyInformationBean.getName().equalsIgnoreCase(bean.getFileid())) {
                        if (bean.getFileid() != null && bean.getFileid().contains("@")) {
                            tempnotifylist.remove(bean);
                            if (buddyInformationBean.getStatus() != null)
                                bean.setSetStatus(buddyInformationBean.getStatus());
                            tempnotifylist.add(bean);
                        }
                        break innerloop;
                    }
                }else if(bean.getNotifttype()!=null && bean.getNotifttype().equalsIgnoreCase("F")){
                    if (buddyInformationBean.getName() != null && bean.getFrom() != null &&
                            buddyInformationBean.getName().equalsIgnoreCase(bean.getFrom())) {
                        tempnotifylist.remove(bean);
                        if (buddyInformationBean.getStatus() != null)
                            bean.setSetStatus(buddyInformationBean.getStatus());
                        tempnotifylist.add(bean);
                        break innerloop;
                    }

                }
            }
        }



        Collections.sort(tempnotifylist, new DateComparator());
        return tempnotifylist;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setType="";
        SingleInstance.instanceTable.remove("dashboard");
    }

    public void showprogress() {
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

    @Override
    public void onResume() {

        super.onResume();
        if(tempnotifylist!=null && tempnotifylist.size()==0){
            if(rl_notify!=null){
                rl_notify.setVisibility(View.GONE);
            }
            LayoutInflater layoutInflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v2 = layoutInflater.inflate(R.layout.notification, rl);
            LinearLayout no_notify=(LinearLayout)v2.findViewById(R.id.no_notify);
            no_notify.setVisibility(View.VISIBLE);
            if(plusBtn!=null)
                plusBtn.setVisibility(View.GONE);
            if(notifylistview!=null)
                notifylistview.setVisibility(View.GONE);
        }
    }
}
