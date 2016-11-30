package com.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.GroupChatBean;
import com.bean.NotifyListBean;
import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.hostedconf.AppReference;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.CallHistoryFragment;
import com.main.ContactsFragment;
import com.main.DashBoardFragment;
import com.main.FilesFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by Rajalakshmi on 30-11-2015.
 */
public class NotifyListAdapter extends ArrayAdapter<NotifyListBean> {
    private Context context;
    private Vector<NotifyListBean> fileList;
    private LayoutInflater inflater = null;
    private String filetype=null;
    DashBoardFragment dashBoardFragment = null;
    private Boolean isEntered=false;
    private Boolean isEnter=false;
    private ImageLoader imageLoader;
    private ArrayList<String> datelist= new ArrayList<String>();
    Vector<BuddyInformationBean> buddylist = new Vector<BuddyInformationBean>();
    private ArrayList<String> usermessage = new ArrayList<String>();
    private  boolean iscontact = false;
    private Vector<NotifyListBean> searchFilter;

    public NotifyListAdapter(Context context, Vector<NotifyListBean> notifyList) {

        super(context, R.layout.notify_list_row, notifyList);
        this.context = context;
        this.fileList = notifyList;
        searchFilter=new Vector<>();
        searchFilter.addAll(notifyList);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dashBoardFragment = DashBoardFragment.newInstance(context);
        imageLoader = new ImageLoader(context);
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        try {
            if (view == null) {
                view = inflater.inflate(R.layout.notify_list_row, null,
                        false);
                holder = new ViewHolder();
                holder.time = (TextView)view.findViewById(R.id.time);
                holder.fileType = (TextView) view.findViewById(R.id.file_type);
                holder.fileName = (TextView) view.findViewById(R.id.file_txt);
                holder.buddyicon = (ImageView) view.findViewById(R.id.buddyicon);
                holder.file_type1 = (TextView)view.findViewById(R.id.file_type1);
                holder.file_txt1 = (TextView)view.findViewById(R.id.file_txt1);
                holder.fileIcon = (ImageView)view.findViewById(R.id.file_icon);
                holder.imagestatus = (ImageView) view.findViewById(R.id.imgstatus);
                holder.header = (TextView) view.findViewById(R.id.file_header);
                holder.time = (TextView) view.findViewById(R.id.time);
                holder.chat_info = (RelativeLayout)view.findViewById(R.id.chat_info);
                holder.header_container = (RelativeLayout) view.findViewById(R.id.header_container);
                holder.list_container = (LinearLayout)view.findViewById(R.id.linear);
                holder.chat_relay = (RelativeLayout)view.findViewById(R.id.relay_chat);
                holder.chat_image = (ImageView)view.findViewById(R.id.recent_chat);
                holder.chat_count = (TextView)view.findViewById(R.id.chat_count);
                holder.file_relay = (RelativeLayout)view.findViewById(R.id.relay_file);
                holder.file_image = (ImageView)view.findViewById(R.id.recent_file);
                holder.file_count = (TextView)view.findViewById(R.id.file_count);
                holder.call_relay = (RelativeLayout)view.findViewById(R.id.relay_call);
                holder.call_image = (ImageView)view.findViewById(R.id.recent_call);
                holder.call_count = (TextView)view.findViewById(R.id.call_count);
                holder.counts_relay = (RelativeLayout)view.findViewById(R.id.chat_info);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            final NotifyListBean notifyBean = fileList.get(position);
            Log.d("listcount","beanfilecount "+notifyBean.getFilecount());
            Log.d("listcount","beanchatcount "+notifyBean.getChatcount());
            Log.d("listcount","beancallcount "+notifyBean.getCallcount());

                if (notifyBean.getCallcount() != null&& notifyBean.getCallcount().length()>0) {
                    holder.call_relay.setVisibility(View.VISIBLE);
                    holder.call_count.setText(notifyBean.getCallcount());
                    Log.d("listcount", "beancallcountif " + notifyBean.getCallcount());
                }else{
                    holder.call_relay.setVisibility(View.GONE);
                }
            if (notifyBean.getFilecount() != null&& notifyBean.getFilecount().length()>0) {
                    holder.file_relay.setVisibility(View.VISIBLE);
                    holder.file_count.setText(notifyBean.getFilecount());
                    Log.d("listcount", "beanfilecountif " + notifyBean.getFilecount());
                }else{
                holder.file_relay.setVisibility(View.GONE);
            }
            if (notifyBean.getChatcount() != null&& notifyBean.getChatcount().length()>0) {
                    holder.chat_relay.setVisibility(View.VISIBLE);
                    holder.chat_count.setText(notifyBean.getChatcount());
                    Log.d("listcount", "beanchatcountif " + notifyBean.getChatcount());
                }else{
                holder.chat_relay.setVisibility(View.GONE);
            }



            holder.chat_relay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notifyBean.getCategory().equalsIgnoreCase("I")){
                        Intent intent = new Intent(context, GroupChatActivity.class);
                        intent.putExtra("isGroup", false);
                        intent.putExtra("buddy", notifyBean.getFileid());
                        context.startActivity(intent);
                    }else if(notifyBean.getCategory().equalsIgnoreCase("G")){
                        Intent intent = new Intent(context, GroupChatActivity.class);
                        intent.putExtra("isGroup", true);
                        intent.putExtra("groupid", notifyBean.getFileid());
                        context.startActivity(intent);
                    }

                }
            });
            holder.fileIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilesFragment filesFragment = FilesFragment.newInstance(context);
                    FragmentManager fragmentManager = SingleInstance.mainContext.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, filesFragment).commitAllowingStateLoss();
                }
            });
//            holder.call_relay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CallHistoryFragment callhistoryFragment = CallHistoryFragment.newInstance(context);
//                    FragmentManager fragmentManager = SingleInstance.mainContext.getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, callhistoryFragment).commitAllowingStateLoss();
//                }
//            });
            if(notifyBean!=null) {
                Log.d("String","otifyid"+notifyBean.getFileid());
                if (notifyBean.getProfilePic() != null && notifyBean.getProfilePic().length()>0) {
                    String pic_Path = Environment.getExternalStorageDirectory() + "/COMMedia/" + notifyBean.getProfilePic();
                    Log.d("picpath","value---->"+pic_Path);
                    File pic = new File(pic_Path);
                    if (pic.exists()) {
                        imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                    }
                }
                if (notifyBean.getSetStatus() != null) {
                    Log.i("AAAA", "Buddy adapter status " + notifyBean.getSetStatus());
                    if (notifyBean.getSetStatus().equalsIgnoreCase("offline") || notifyBean.getSetStatus().equalsIgnoreCase("stealth")) {
                        holder.imagestatus.setBackgroundResource(R.drawable.offline_icon);
                    } else if (notifyBean.getSetStatus().equalsIgnoreCase("online")) {
                        holder.imagestatus.setBackgroundResource(R.drawable.online_icon);
                    } else if (notifyBean.getSetStatus().equalsIgnoreCase("busy") || notifyBean.getSetStatus().equalsIgnoreCase("airport")) {
                        holder.imagestatus.setBackgroundResource(R.drawable.busy_icon);
                    } else if (notifyBean.getSetStatus().equalsIgnoreCase("away")) {
                        holder.imagestatus.setBackgroundResource(R.drawable.invisibleicon);
                    } else {
                        holder.imagestatus.setBackgroundResource(R.drawable.offline_icon);
                    }
                }
            }



            if(iscontact){
                holder.buddyicon.setVisibility(View.VISIBLE);
               holder.fileIcon.setVisibility(View.GONE);
                holder.chat_info.setVisibility(View.VISIBLE);
                if(notifyBean.getFileid()!=null && notifyBean.getFileid().contains("@")) {
                    holder.imagestatus.setVisibility(View.VISIBLE);
                }else{
                    holder.imagestatus.setVisibility(View.INVISIBLE);
                }
                holder.list_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(iscontact){
                            if(notifyBean.getNotifttype()!=null && notifyBean.getNotifttype().equalsIgnoreCase("I")) {
                                if (notifyBean.getFileid() != null && notifyBean.getFileid().contains("@")) {
                                    if (!DBAccess.getdbHeler().ChatEntryAvailableOrNot(notifyBean.getFileid())) {
                                        Log.i("syncchat", "chatentry not available");
                                        if (SingleInstance.instanceTable.containsKey("contactspage")) {
                                            Log.i("syncchat", "Appmain contactspage available");
                                            ContactsFragment contactsFragment = (ContactsFragment) SingleInstance.instanceTable.get("contactspage");
                                            contactsFragment.showprogress();
                                        }
                                        if (SingleInstance.instanceTable.containsKey("dashboard")) {
                                            Log.i("syncchat", "Appmain contactspage available");
                                            DashBoardFragment dashBoardFragment = (DashBoardFragment) SingleInstance.instanceTable.get("dashboard");
                                            dashBoardFragment.showprogress();
                                        }
                                        AppReference.chatRecentSync = true;
                                        AppReference.chatRecentSyncBuddyOrGroupid = notifyBean.getFileid();
//                                    list_position = position;
//                                    chatsync_contactlist=true;
                                        WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "0", notifyBean.getFileid(), "", "");
                                    } else {
                                        Intent intent = new Intent(context, GroupChatActivity.class);
                                        intent.putExtra("isGroup", false);
                                        intent.putExtra("buddy", notifyBean.getFileid());
                                        context.startActivity(intent);
                                    }
                                } else if (notifyBean.getFileid() != null && !notifyBean.getFileid().contains("@")) {
                                    GroupBean groupBean = DBAccess.getdbHeler().getGroup(
                                            "select * from grouplist where groupid='" + notifyBean.getFileid() + "'");
                                    if (groupBean != null) {
                                        if (!DBAccess.getdbHeler().ChatEntryAvailableOrNot(notifyBean.getFileid())) {
                                            Log.i("syncchat", "chatentry not available");
                                            if (SingleInstance.instanceTable.containsKey("contactspage")) {
                                                Log.i("syncchat", "Appmain contactspage available");
                                                ContactsFragment contactsFragment = (ContactsFragment) SingleInstance.instanceTable.get("contactspage");
                                                contactsFragment.showprogress();
                                            }
                                            if (SingleInstance.instanceTable.containsKey("dashboard")) {
                                                Log.i("syncchat", "Appmain contactspage available");
                                                DashBoardFragment dashBoardFragment = (DashBoardFragment) SingleInstance.instanceTable.get("dashboard");
                                                dashBoardFragment.showprogress();
                                            }
                                            AppReference.chatRecentSync = true;
                                            AppReference.chatRecentSyncBuddyOrGroupid = notifyBean.getFileid();
//                                    AppReference.Beginsync_chat = true;
//                                    list_position = position;
//                                    chatsync_contactlist=true;
                                            WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext, "0", notifyBean.getFileid(), "", "");
                                        } else {
                                            Intent intent = new Intent(context, GroupChatActivity.class);
                                            intent.putExtra("isGroup", true);
                                            intent.putExtra("groupid", notifyBean.getFileid());
                                            context.startActivity(intent);
                                        }
                                    } else {
                                        Toast.makeText(context, "You are Not Member in this Group", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }else if(notifyBean.getNotifttype()!=null && notifyBean.getNotifttype().equalsIgnoreCase("F")){
                                FilesFragment filesFragment = FilesFragment.newInstance(context);
                                FragmentManager fragmentManager = SingleInstance.mainContext.getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, filesFragment).commitAllowingStateLoss();
                            }
                        }else {
                            if (notifyBean.getCategory().equalsIgnoreCase("I")) {
                                Intent intent = new Intent(context, GroupChatActivity.class);
                                intent.putExtra("isGroup", false);
                                intent.putExtra("buddy", notifyBean.getFileid());
                                context.startActivity(intent);
                            } else if (notifyBean.getCategory().equalsIgnoreCase("G")) {
                                Intent intent = new Intent(context, GroupChatActivity.class);
                                intent.putExtra("isGroup", true);
                                intent.putExtra("groupid", notifyBean.getFileid());
                                context.startActivity(intent);
                            }
                        }
                    }
                });
            }else {
                holder.buddyicon.setVisibility(View.VISIBLE);
                holder.fileIcon.setVisibility(View.GONE);
                holder.chat_info.setVisibility(View.VISIBLE);
                holder.imagestatus.setVisibility(View.VISIBLE);
            }
            if(notifyBean!=null) {
                Log.d("String", "otifyid" + notifyBean.getFileid());
                Log.i("dateformat", "Format" + CallDispatcher.dateFormat);
                SimpleDateFormat df = new SimpleDateFormat(CallDispatcher.dateFormat + " hh:mm aa");
                SimpleDateFormat df2 = new SimpleDateFormat(
                        CallDispatcher.dateFormat);
                if (notifyBean.getSortdate() != null) {
                    String[] receivedTimes = notifyBean.getSortdate().split(" ");
                    Date receivedDate = null;

                    if (receivedTimes[0].contains("/")
                            && CallDispatcher.dateFormat.contains("-")) {
                        SimpleDateFormat userDateFormat = new SimpleDateFormat(
                                "MM/dd/yyyy");
                        SimpleDateFormat dateFormatNeeded = new SimpleDateFormat(
                                CallDispatcher.dateFormat);
                        Date date = userDateFormat.parse(receivedTimes[0]);
                        String convertedDate = dateFormatNeeded.format(date);
                        receivedDate = dateFormatNeeded.parse(convertedDate);
                    } else if (receivedTimes[0].contains("-")

                            && CallDispatcher.dateFormat.contains("/")) {
                        SimpleDateFormat userDateFormat = new SimpleDateFormat(
                                "dd-MM-yyyy");
                        SimpleDateFormat dateFormatNeeded = new SimpleDateFormat(
                                CallDispatcher.dateFormat);
                        Date date = userDateFormat.parse(receivedTimes[0]);
                        String convertedDate = dateFormatNeeded.format(date);
                        receivedDate = df2.parse(convertedDate);
                    } else {
                        receivedDate = df2.parse(receivedTimes[0]);
                    }
                    Calendar cal = Calendar.getInstance();
                    String[] todayDate = df.format(cal.getTime()).split(" ");
                    for (int i = 0; i < todayDate.length; i++) {
                        Log.i("t1", "----" + todayDate[i]);
                    }
                    Date today = df2.parse(todayDate[0]);
                    String[] yesterdayDate = getYesterdayDateString(df,-1).split(" ");
                    Date yesterday = df2.parse(yesterdayDate[0]);
                    Log.i("dateformat", "receivedDate :: " + receivedDate);
                    Log.i("dateformat", "today :: " + today);
                    Log.i("dateformat", "yesterday :: " + yesterday);

                    if (receivedDate.compareTo(today) == 0) {
                        if (!isEntered) {
                            holder.header_container.setVisibility(View.VISIBLE);
                            holder.header.setText("TODAY");
                            isEntered = true;
                        } else
                            holder.header_container.setVisibility(View.GONE);
                    } else if (receivedDate.compareTo(yesterday) == 0) {
                        if (!isEnter) {
                            holder.header_container.setVisibility(View.VISIBLE);
                            holder.header.setText("YESTERDAY");
                            isEnter = true;
                        } else
                            holder.header_container.setVisibility(View.GONE);

                    } else {
                        holder.header_container.setVisibility(View.VISIBLE);
                        Log.i("abcd", "=========notifyBean.getSortdate()" + notifyBean.getSortdate());
                        String formattedDate = null;
                        //Input date in String format
                        String input = notifyBean.getSortdate();
                        //Date/time pattern of input date
                        DateFormat df1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                        //Date/time pattern of desired output date
                        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                        Date convertdate = null;
                        String output = null;
                        try {
                            //Conversion of input String to date
                            convertdate = df1.parse(input);
                            //old date format to new date format
                            output = outputformat.format(convertdate);
                            Log.i("abcd", "24 hours to 12hours" + output);
                            formattedDate = output;
                        } catch (ParseException pe) {
                            pe.printStackTrace();
                            Log.i("abcd", "24 hours to 12hours exceptioon" + formattedDate);

                            formattedDate = notifyBean.getSortdate();
                        }


                        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
//                        SimpleDateFormat printFormat = new SimpleDateFormat("EEEE MMMM dd");
                        SimpleDateFormat printFormat = new SimpleDateFormat("dd MMM yyyy");
                        Date date = new Date();
                        try {
                            date = parseFormat.parse(formattedDate);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        Log.i("AAA", "XML PARSER DATE:" + printFormat.format(date));
                        String senttime = printFormat.format(date);
                        if (datelist != null) {
                            for (String dates : datelist) {
                                if (dates.equalsIgnoreCase(senttime))
                                    holder.header_container.setVisibility(View.GONE);
                                else
                                    holder.header_container.setVisibility(View.VISIBLE);
                            }
                        }
                        datelist.add(senttime);
                        if(iscontact) {
//                            if (senttime != null && senttime.contains(" ")) {
//                                holder.header.setText(senttime.split(" ")[0].toUpperCase());
//                            }
                            holder.header.setText(senttime);
                        }else{
                            holder.header.setText(senttime);
                        }
                    }
                    String time = notifyBean.getSortdate().split(" ")[1];
                    String[] times = time.split(":");
                    holder.time.setText(times[0] + ":" + times[1]);
                    Log.i("AAAA", "NOTIFYLIST ADAPTER Values " + time);


                    if(iscontact) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                        SimpleDateFormat DayFormat = new SimpleDateFormat("EEEE MMMM dd");
                        Calendar cal1 = Calendar.getInstance();
                        String todayDate1 = format.format(cal1.getTime());
                        if (todayDate1.equals(notifyBean.getSortdate().split(" ")[0])) {
                            holder.header.setText("Today");
                        } else if (getYesterdayDateString(format,-1).equals(notifyBean.getSortdate().split(" ")[0])) {
                            holder.header.setText("Yesterday");
                        } else if (getYesterdayDateString(format,-2).equals(notifyBean.getSortdate().split(" ")[0])) {
                            Date d1 = format.parse(notifyBean.getSortdate().split(" ")[0]);
                            String newdate = DayFormat.format(d1);
                            holder.header.setText(newdate.split(" ")[0]);
                        }else if (getYesterdayDateString(format,-3).equals(notifyBean.getSortdate().split(" ")[0])) {
                            Date d1 = format.parse(notifyBean.getSortdate().split(" ")[0]);
                            String newdate = DayFormat.format(d1);
                            holder.header.setText(newdate.split(" ")[0]);
                        }else if (getYesterdayDateString(format,-4).equals(notifyBean.getSortdate().split(" ")[0])) {
                            Date d1 = format.parse(notifyBean.getSortdate().split(" ")[0]);
                            String newdate = DayFormat.format(d1);
                            holder.header.setText(newdate.split(" ")[0]);
                        }else if (getYesterdayDateString(format,-5).equals(notifyBean.getSortdate().split(" ")[0])) {
                            Date d1 = format.parse(notifyBean.getSortdate().split(" ")[0]);
                            String newdate = DayFormat.format(d1);
                            holder.header.setText(newdate.split(" ")[0]);
                        }else if (getYesterdayDateString(format,-6).equals(notifyBean.getSortdate().split(" ")[0])) {
                            Date d1 = format.parse(notifyBean.getSortdate().split(" ")[0]);
                            String newdate = DayFormat.format(d1);
                            holder.header.setText(newdate.split(" ")[0]);
                        }else {
                            Date d1 = format.parse(notifyBean.getSortdate().split(" ")[0]);
                            String newdate = sdf.format(d1);
                            holder.header.setText(newdate);
                        }
                        holder.header_container.setVisibility(View.VISIBLE);
                        if (position > 0) {
                            final NotifyListBean bean = fileList.get(position - 1);
                            if (bean.getSortdate().split(" ")[0].equals(notifyBean.getSortdate().split(" ")[0])) {
                                holder.header_container.setVisibility(View.GONE);
                            } else {
                                holder.header_container.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }


                    if (notifyBean.getNotifttype() != null) {
                        Log.i("AAAA", "NOTIFYLIST ADAPTER type " + notifyBean.getNotifttype());
                        if (notifyBean.getNotifttype().trim().equalsIgnoreCase("F")) {
                            if (iscontact) {
                                holder.buddyicon.setVisibility(View.VISIBLE);
//                                holder.fileIcon.setBackgroundResource(R.drawable.recent_files);
                            } else {
                                holder.buddyicon.setVisibility(View.VISIBLE);
                                holder.fileIcon.setVisibility(View.GONE);
                            }

                            if (notifyBean.getType().trim().equalsIgnoreCase("audio"))
                                holder.fileName.setText("Audio file received");
                            else if (notifyBean.getType().trim().equalsIgnoreCase("video"))
                                holder.fileName.setText("Video file received");
                            else if (notifyBean.getType().trim().equalsIgnoreCase("photo"))
                                holder.fileName.setText("Photo file received");
                            else if (notifyBean.getType().trim().equalsIgnoreCase("note"))
                                holder.fileName.setText(" Note file received");
                            else if (notifyBean.getType().trim().equalsIgnoreCase("sketch"))
                                holder.fileName.setText("Sketch file received");
                            else if (notifyBean.getType().trim().equalsIgnoreCase("document"))
                                holder.fileName.setText("Document file received");
                            if (notifyBean.getContent() != null)
                                holder.fileType.setText(notifyBean.getUsername());
                        } else if (notifyBean.getNotifttype().trim().equalsIgnoreCase("C")) {
                            holder.fileType.setVisibility(View.GONE);
                            holder.file_type1.setVisibility(View.VISIBLE);
                            holder.file_txt1.setVisibility(View.VISIBLE);
                            holder.fileName.setVisibility(View.GONE);
                            if (iscontact) {
                                holder.buddyicon.setVisibility(View.VISIBLE);
//                                holder.fileIcon.setBackgroundResource(R.drawable.recent_calls);
                            } else {
                                holder.buddyicon.setVisibility(View.VISIBLE);
                                holder.fileIcon.setVisibility(View.GONE);
                            }

                            if (notifyBean.getType().trim().equalsIgnoreCase("AC"))
                                holder.file_txt1.setText(notifyBean.getUsername());
                            else if (notifyBean.getType().trim().equalsIgnoreCase("VC"))
                                holder.file_txt1.setText(notifyBean.getUsername() + "");
                            else if (notifyBean.getType().trim().equalsIgnoreCase("ABC"))
                                holder.file_txt1.setText(notifyBean.getUsername());
                            else if (notifyBean.getType().trim().equalsIgnoreCase("VBC"))
                                holder.file_txt1.setText(notifyBean.getUsername());
                            else if (notifyBean.getType().trim().equalsIgnoreCase("AP"))
                                holder.file_txt1.setText(notifyBean.getUsername());
                            else if (notifyBean.getType().trim().equalsIgnoreCase("VP"))
                                holder.file_txt1.setText(notifyBean.getUsername());
                        if(notifyBean.getContent()!=null)
                            holder.fileName.setText(notifyBean.getContent());
                        } else if (notifyBean.getNotifttype().trim().equalsIgnoreCase("Invite")) {

                            holder.file_type1.setVisibility(View.GONE);
                            holder.file_txt1.setVisibility(View.GONE);
                            holder.fileName.setVisibility(View.VISIBLE);
                            holder.fileType.setVisibility(View.VISIBLE);
                            if (iscontact) {
                                holder.buddyicon.setVisibility(View.VISIBLE);
//                                holder.fileIcon.setVisibility(View.VISIBLE);

                            } else {
                                holder.buddyicon.setVisibility(View.VISIBLE);
                                holder.fileIcon.setVisibility(View.GONE);
                            }

                            if (notifyBean.getType().equalsIgnoreCase("contact")) {
                                holder.fileIcon.setBackgroundResource(R.drawable.dashboard_invite_blue_3);
//                                holder.fileType.setText(notifyBean.getFrom());
                                NotifyListBean Nbean = new NotifyListBean();

                                    Log.d("contact", "value--->");
                                    holder.fileType.setText(notifyBean.getUsername());
                                   holder.fileName.setText("invites you to contact list");
                            } else if (notifyBean.getType().trim().equalsIgnoreCase("group")) {
                                holder.fileIcon.setBackgroundResource(R.drawable.dashboard_group_blue);
                                Log.d("group","value--->");
                                holder.file_txt1.setVisibility(View.VISIBLE);
                                holder.fileType.setText(notifyBean.getUsername());
//                                holder.time.setText();
                                holder.fileName.setText("invites you to group");
                                holder.file_txt1.setText(notifyBean.getOwner());
                                holder.file_txt1.setTextColor(context.getResources().getColor(R.color.blue2));
                            }

                        } else if (notifyBean.getNotifttype().trim().equalsIgnoreCase("I")) {
                            if (iscontact) {
                                holder.buddyicon.setVisibility(View.VISIBLE);
                                //For chatRecentlist
                                //Start
//                                if(notifyBean.getCategory().equalsIgnoreCase("call")){
//                                    holder.fileType.setText(notifyBean.getUsername());
//                                    holder.fileName.setText("Missed Call");
//                                }
////
//                                if(notifyBean.getType()!=null && !notifyBean.getType().trim().equalsIgnoreCase("text")){
//                                    holder.fileName.setText(notifyBean.getType());
//                                }
                                //End
//                                holder.fileIcon.setBackgroundResource(R.drawable.recent_message);
                            } else {
                                holder.buddyicon.setVisibility(View.VISIBLE);
                                holder.fileIcon.setVisibility(View.GONE);
                            }
                            Log.i("nameof","username"+notifyBean.getOwner());
                            if (notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("image"))
                                if(notifyBean.getCategory().equalsIgnoreCase("I")){
                                    holder.fileType.setText(notifyBean.getUsername());
                                }else if(notifyBean.getCategory()!=null && notifyBean.getCategory().equalsIgnoreCase("g")){
                                    if(DBAccess.getdbHeler().getGroupName(
                                            "select * from grouplist where groupid='" + notifyBean.getFileid() + "'")!=null) {
                                        holder.fileType.setText(DBAccess.getdbHeler().getGroupName(
                                                "select * from grouplist where groupid='" + notifyBean.getFileid() + "'"));
                                    }
                                }
                                else
                                    holder.fileType.setText(notifyBean.getOwner());
                            else if (notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("text"))
                                if(notifyBean.getCategory().equalsIgnoreCase("I")){
                                    holder.fileType.setText(notifyBean.getUsername());
                                }else if(notifyBean.getCategory()!=null && notifyBean.getCategory().equalsIgnoreCase("g")){
                                    if(DBAccess.getdbHeler().getGroupName(
                                            "select * from grouplist where groupid='" + notifyBean.getFileid() + "'")!=null) {
                                        holder.fileType.setText(DBAccess.getdbHeler().getGroupName(
                                                "select * from grouplist where groupid='" + notifyBean.getFileid() + "'"));
                                    }
                                }
                                else
                                    holder.fileType.setText(notifyBean.getOwner());
                            else if (notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("audio"))
                                if(notifyBean.getCategory().equalsIgnoreCase("I")){
                                    holder.fileType.setText(notifyBean.getUsername());
                                }else if(notifyBean.getCategory()!=null && notifyBean.getCategory().equalsIgnoreCase("g")){
                                    if(DBAccess.getdbHeler().getGroupName(
                                            "select * from grouplist where groupid='" + notifyBean.getFileid() + "'")!=null) {
                                        holder.fileType.setText(DBAccess.getdbHeler().getGroupName(
                                                "select * from grouplist where groupid='" + notifyBean.getFileid() + "'"));
                                    }
                                }
                                else
                                    holder.fileType.setText(notifyBean.getOwner());
                            else if (notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("video"))
                                if(notifyBean.getCategory().equalsIgnoreCase("I")){
                                    holder.fileType.setText(notifyBean.getUsername());
                                }else if(notifyBean.getCategory()!=null && notifyBean.getCategory().equalsIgnoreCase("g")){
                                    if(DBAccess.getdbHeler().getGroupName(
                                            "select * from grouplist where groupid='" + notifyBean.getFileid() + "'")!=null) {
                                        holder.fileType.setText(DBAccess.getdbHeler().getGroupName(
                                                "select * from grouplist where groupid='" + notifyBean.getFileid() + "'"));
                                    }
                                }
                                else
                                    holder.fileType.setText(notifyBean.getOwner());
                            else if (notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("sketch"))
                                if(notifyBean.getCategory().equalsIgnoreCase("I")){
                                    holder.fileType.setText(notifyBean.getUsername());
                                }else if(notifyBean.getCategory()!=null && notifyBean.getCategory().equalsIgnoreCase("g")){
                                    if(DBAccess.getdbHeler().getGroupName(
                                            "select * from grouplist where groupid='" + notifyBean.getFileid() + "'")!=null) {
                                        holder.fileType.setText(DBAccess.getdbHeler().getGroupName(
                                                "select * from grouplist where groupid='" + notifyBean.getFileid() + "'"));
                                    }
                                }
                                else
                                    holder.fileType.setText(notifyBean.getOwner());
                            else if(notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("document"))
                                if(notifyBean.getCategory().equalsIgnoreCase("I")){
                                    holder.fileType.setText(notifyBean.getUsername());
                                }else if(notifyBean.getCategory()!=null && notifyBean.getCategory().equalsIgnoreCase("g")){
                                    if(DBAccess.getdbHeler().getGroupName(
                                            "select * from grouplist where groupid='" + notifyBean.getFileid() + "'")!=null) {
                                        holder.fileType.setText(DBAccess.getdbHeler().getGroupName(
                                                "select * from grouplist where groupid='" + notifyBean.getFileid() + "'"));
                                    }
                                }
                                else
                                    holder.fileType.setText(notifyBean.getOwner());
                            else if(notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("mixedfile"))
                                if(notifyBean.getCategory().equalsIgnoreCase("I")){
                                    holder.fileType.setText(notifyBean.getUsername());
                                }else if(notifyBean.getCategory()!=null && notifyBean.getCategory().equalsIgnoreCase("g")){
                                    if(DBAccess.getdbHeler().getGroupName(
                                            "select * from grouplist where groupid='" + notifyBean.getFileid() + "'")!=null) {
                                        holder.fileType.setText(DBAccess.getdbHeler().getGroupName(
                                                "select * from grouplist where groupid='" + notifyBean.getFileid() + "'"));
                                    }
                                }
                                else
                                    holder.fileType.setText(notifyBean.getOwner());
                            if (notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("text")) {
                                if (notifyBean.getContent() != null)
                                    holder.fileName.setText(notifyBean.getContent());
                            } else {
                                if (notifyBean.getMedia() != null) {
                                    File newFile = new File(notifyBean.getMedia());
                                    if (newFile.exists())
                                        holder.fileName.setText(notifyBean.getType() + " received");
                                    else {
                                        if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("image")) {
                                            holder.fileName.setText("Image File Received");
                                        } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("audio")) {
                                            holder.fileName.setText("Audio File Received");
                                        } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("video")) {
                                            holder.fileName.setText("Video File Received");
                                        } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("sketch")) {
                                            holder.fileName.setText("Sketch File Received");
                                        } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("document")) {
                                            holder.fileName.setText("Document File Received");
                                        } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("mixedfile")) {
                                            holder.fileName.setText("Mixed File Received");
                                        } else {
                                            holder.fileName.setText("Files Received");
                                        }
                                    }
                                }
                                else {
                                    if (iscontact) {
                                        holder.fileName.setText("Missed Call");
                                    } else{
                                        holder.fileName.setText("File Not Found");
                                     }
                                }
                            }

                            if(iscontact){
//                                if(notifyBean.getCategory()!=null && notifyBean.getFileid()!=null &&
//                                       !notifyBean.getFileid().contains("@")&& notifyBean.getCategory().equalsIgnoreCase("call")){
//                                    holder.fileType.setText(notifyBean.getUsername());
////                                    holder.fileName.setText("Missed call");
//                                    holder.fileName.setText(notifyBean.getContent());
//                                }
//                                if(notifyBean.getType()!=null && notifyBean.getContent()!=null){
//                                    holder.fileName.setText(notifyBean.getContent());
//                                }
                                if(notifyBean.getUsername()!=null){
                                    holder.fileType.setText(notifyBean.getUsername());
                                }else if(notifyBean.getFrom()!=null){
                                    holder.fileType.setText(notifyBean.getFrom());
                                }

                                if (notifyBean.getType()!=null && notifyBean.getType().trim().equalsIgnoreCase("text")) {
                                    if (notifyBean.getContent() != null)
                                        holder.fileName.setText(notifyBean.getContent());
                                }else{
                                    if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("image")) {
                                        if(notifyBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
                                            holder.fileName.setText("Image sent");
                                        }else {
                                            holder.fileName.setText("Image received");
                                        }
                                    } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("audio")) {
                                        if(notifyBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
                                            holder.fileName.setText("Audio sent");
                                        }else {
                                            holder.fileName.setText("Audio received");
                                        }
                                    } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("video")) {
                                        if(notifyBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
                                            holder.fileName.setText("Video sent");
                                        }else {
                                            holder.fileName.setText("Video received");
                                        }
                                    } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("sketch")) {
                                        if(notifyBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
                                            holder.fileName.setText("Sketch sent");
                                        }else {
                                            holder.fileName.setText("Sketch received");
                                        }
                                    } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("document")) {
                                        if(notifyBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
                                            holder.fileName.setText("Document sent");
                                        }else {
                                            holder.fileName.setText("Document received");
                                        }
                                    } else if (notifyBean.getType() != null && notifyBean.getType().trim().equalsIgnoreCase("mixedfile")) {
                                        if(notifyBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
                                            holder.fileName.setText("Mixed sent");
                                        }else {
                                            holder.fileName.setText("Mixed received");
                                        }
                                    } else if(notifyBean.getCategory()!=null && notifyBean.getCategory().equalsIgnoreCase("call")){
                                        if (notifyBean.getContent() != null){
                                            holder.fileName.setText(notifyBean.getContent());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }


    public static class ViewHolder {
        TextView fileName;
        TextView fileType;
        ImageView fileIcon;
        TextView header,time;
        LinearLayout list_container;
        RelativeLayout header_container,chat_relay,file_relay,call_relay,counts_relay;
        ImageView imagestatus,chat_image,file_image,call_image;
        ImageView buddyicon;
        RelativeLayout chat_info;
        TextView file_type1,chat_count,call_count,file_count;
        TextView file_txt1;

    }
    public void filter(String charText) {
        // TODO Auto-generated method stub
        charText = charText.toLowerCase(Locale.getDefault());
        Log.i("AAAA", "DASHBORAD FILTER IF PART " + dashBoardFragment.notifyList.size());
        dashBoardFragment.tempnotifylist.clear();
        if (charText.length() == 0) {
            fileList.addAll(dashBoardFragment.seacrhnotifylist);
        } else {
            for (NotifyListBean storedData : dashBoardFragment.seacrhnotifylist) {
                if (storedData.getUsername()
                        .toLowerCase(Locale.getDefault()).contains(charText) && storedData.getViewed()==0) {
                    fileList.add(storedData);
                }

            }
        }
        notifyDataSetChanged();
    }
    private String getYesterdayDateString(SimpleDateFormat dateFormat,int value) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, value);
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
    private String time(SimpleDateFormat dateFormat) {
        try {
            Calendar cal = Calendar.getInstance();
            String[] date = dateFormat.format(cal.getTime()).split(" ");
            return date[0];
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return null;
        }
    }
    private void dateConversion()
    {

    }
    public void isFromOther(Boolean isOther){
        iscontact=isOther;
    }

    public void SearchFilter(String charText) {
        Log.i("filter","filter mathod");
        Log.i("filter","Search Fiter-->"+searchFilter.size());
        charText = charText.toLowerCase(Locale.getDefault());
        fileList.clear();
        if (charText.length() == 0) {
            fileList.addAll(searchFilter);
            Log.i("filter","filter mathod if");
        }
        else
        {
            for (NotifyListBean notifyListBean : searchFilter)
            {
                if (notifyListBean.getUsername()!=null && notifyListBean.getUsername().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    Log.i("filter","filter mathod for if");
                    fileList.add(notifyListBean);
                }
            }
        }
        notifyDataSetChanged();
    }

    }



