package com.cg.callservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.bean.UserBean;
import com.callHistory.CallHistoryActivity;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.SignalingBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by jansi on 5/23/2016.
 */
public class CallActiveMembersList extends Activity {
    private Context context;
    public CallMembersList adapter;
    private CheckBox selectAll_buddy;
    private TextView selected;
    private TextView txtView01;
    private ListView searchResult;
    private EditText btn_1;
    private Button search, cancel;
    private RelativeLayout RelativeLayout2;
    Vector<BuddyInformationBean> result;
    private CallDispatcher objCallDispatcher;
    private Vector<UserBean> membersList = new Vector<UserBean>();
    private String strSessionId, host, activescreen;
    private Handler handler = new Handler();
    private boolean selfHangup = false;
    private boolean isBuddyinCall = false;
    private AlertDialog alert = null;
    private String timer;
    String calltype = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            setContentView(R.layout.callmembers_info);
            context = this;

            SingleInstance.instanceTable.remove("callactivememberslist", context);

            cancel = (Button) findViewById(R.id.cancel);


            ListView searchResult = (ListView) findViewById(R.id.searchResult);
            ImageView hangupBtn = (ImageView) findViewById(R.id.hangupBtn);
            ImageView addMembers = (ImageView) findViewById(R.id.addmembersBtn);
            strSessionId = getIntent().getStringExtra("sessionId");
            if (getIntent().getStringExtra("host") != null) {
                host = getIntent().getStringExtra("host");
                Log.i("AudioCall", "Host : " + host);
            }

            if (getIntent().getStringExtra("fromscreen") != null) {
                activescreen = getIntent().getStringExtra("fromscreen");
            }
            timer = getIntent().getStringExtra("timer");
            calltype = getIntent().getStringExtra("calltype");

            if (WebServiceReferences.callDispatch.containsKey("calldisp"))
                objCallDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
                        .get("calldisp");
            else
                objCallDispatcher = new CallDispatcher(context);

            addMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowOnlineBuddies();
                }
            });

            hangupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showHangUpAlert();
                }
            });


            Set mapSet = (Set) CallDispatcher.conferenceMember_Details.entrySet();
            Iterator mapIterator = mapSet.iterator();
            while (mapIterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                String key = (String) mapEntry.getKey();
                Log.i("AudioCall", "key : " + key);
                SignalingBean pbean = (SignalingBean) mapEntry.getValue();
            }

            Vector<BuddyInformationBean> participant_objects = new Vector<BuddyInformationBean>();
            Vector<BuddyInformationBean> total_objects = new Vector<BuddyInformationBean>();

            ProfileBean bean = SingleInstance.myAccountBean;
            String user_name = CallDispatcher.LoginUser;

            BuddyInformationBean ownbean = new BuddyInformationBean();
            ownbean.setFirstname(user_name);
            ownbean.setStatus(CallDispatcher.myStatus);
            ownbean.setMode("connected");
            ownbean.setProfile_picpath(bean.getPhoto());

            if (host.equalsIgnoreCase(CallDispatcher.LoginUser)) {
                total_objects.add(ownbean);
            } else {
                participant_objects.add(ownbean);
            }


            for (String user : CallDispatcher.conferenceMembers) {
                boolean havebuddy = false;
                for (BuddyInformationBean buddyInformationBean : ContactsFragment.getBuddyList()) {
                    if (user.equalsIgnoreCase(buddyInformationBean.getEmailid())) {
                        havebuddy = true;
                        SignalingBean sb = CallDispatcher.buddySignall.get(buddyInformationBean.getEmailid());
//                        if(sb.getType().equalsIgnoreCase("2"))
//                            buddyInformationBean.setMode("connected");
//                        else if(sb.getType().equalsIgnoreCase("1") && sb.getResult().equalsIgnoreCase("1"))
//                            buddyInformationBean.setMode("connecting...");
//                        else if(sb.getType().equalsIgnoreCase("1") && sb.getResult().equalsIgnoreCase("0"))
//                            buddyInformationBean.setMode("connecting...");
//                        else
                        buddyInformationBean.setMode("connected");
                        ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(user);
                        buddyInformationBean.setProfile_picpath(pBean.getPhoto());
                        Log.i("AudioCall", "");
                        if (user.equalsIgnoreCase(host)) {
                            total_objects.add(buddyInformationBean);
                        } else {
                            participant_objects.add(buddyInformationBean);
                        }
                    }
                }

                if (havebuddy) {

                } else {
                    BuddyInformationBean informationBean = new BuddyInformationBean();
                    informationBean.setEmailid(user);
                    informationBean.setMode("connected");
                    participant_objects.add(informationBean);
                }
            }

            if (participant_objects.size() > 0) {
                total_objects.addAll(participant_objects);
            }

            CallMembersList calladapter = new CallMembersList(context, R.layout.find_people_item, total_objects);
            searchResult.setAdapter(calladapter);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CallMembersList extends ArrayAdapter<BuddyInformationBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<BuddyInformationBean> result;
        private Vector<BuddyInformationBean> originalList;
        private ContactsFilter filter;

        public CallMembersList(Context context, int resource, Vector<BuddyInformationBean> objects) {
            super(context, resource, objects);
            imageLoader = new ImageLoader(context);
            result = new Vector<BuddyInformationBean>();
            result.addAll(objects);
            originalList = new Vector<BuddyInformationBean>();
            originalList.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if (convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.callmembers, null);
                    holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
                    holder.selectUser.setVisibility(View.GONE);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.timer = (TextView) convertView.findViewById(R.id.rights);
                    holder.position = (TextView) convertView.findViewById(R.id.position);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.overlay_image = (ImageView) convertView.findViewById(R.id.overlay);
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder) convertView.getTag();
                final BuddyInformationBean bib = result.get(i);
                if (calltype.equalsIgnoreCase("VC")) {
                    holder.overlay_image.setVisibility(View.VISIBLE);
                } else {
                    holder.overlay_image.setVisibility(View.GONE);
                }
                if (bib != null) {
                    if (bib.getProfile_picpath() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/COMMedia/" + bib.getProfile_picpath();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.VISIBLE);
                    holder.position.setVisibility(View.GONE);
                    holder.edit.setVisibility(View.GONE);
                    if (timer != null)
                        holder.timer.setText(timer);
                    else
                        holder.timer.setText("");
                    holder.timer.setTextColor(getResources().getColor(R.color.snazash));
                    if (i == 0) {
                        holder.header_title.setText("From");
                    } else if (i == 1) {
                        holder.header_title.setText("To");
                    } else {
                        holder.header_title.setVisibility(View.INVISIBLE);
                    }
                    holder.statusIcon.setVisibility(View.VISIBLE);

                    if (bib.getStatus() != null) {
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
                    } else {
                        holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                    }
                    if (bib.getFirstname() != null) {
                        if (bib.getFirstname().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                            holder.buddyName.setText("Me");
                            holder.overlay_image.setVisibility(View.GONE);
                        } else {
                            holder.buddyName.setText(bib.getFirstname() + " " + bib.getLastname());
                        }
                    } else {
                        holder.buddyName.setText(bib.getEmailid());
                    }
                    if (bib.getMode() != null) {
                        holder.occupation.setText(bib.getMode());
                        if (bib.getMode().equalsIgnoreCase("connected"))
                            holder.occupation.setTextColor(getResources().getColor(R.color.blue2));
                        else
                            holder.occupation.setTextColor(getResources().getColor(R.color.yellow));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("RRRR", "Error FindpeopleAdapter.java => " + e.toString());
            }
            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new ContactsFilter();
            }
            return filter;
        }

        private class ContactsFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();

                FilterResults result = new FilterResults();
                if (constraint != null && constraint.toString().length() > 0) {
                    Vector<BuddyInformationBean> buddyInformationBeans = new Vector<BuddyInformationBean>();
                    for (int i = 0, l = originalList.size(); i < l; i++) {
                        BuddyInformationBean buddyInformationBean = originalList.get(i);
                        if (buddyInformationBean.getName().toLowerCase().startsWith(String.valueOf(constraint)))
                            buddyInformationBeans.add(buddyInformationBean);
                    }
                    buddyInformationBeans = GroupChatActivity.getAdapterList(buddyInformationBeans);
                    result.count = buddyInformationBeans.size();
                    result.values = buddyInformationBeans;
                } else {
                    synchronized (this) {
                        originalList = GroupChatActivity.getAdapterList(originalList);
                        result.values = originalList;
                        result.count = originalList.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                result = (Vector<BuddyInformationBean>) results.values;
                notifyDataSetChanged();
                clear();
                result = GroupChatActivity.getAdapterList(result);
                for (int i = 0, l = result.size(); i < l; i++)
                    add(result.get(i));
                notifyDataSetInvalidated();

            }

        }
    }

    public static class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon, edit;
        ImageView statusIcon, overlay_image;
        TextView buddyName, timer, position;
        TextView occupation;
        TextView header_title;
    }

    public void ShowOnlineBuddies() {

        try {
            String[] members = objCallDispatcher.getOnlineBuddys();
            ArrayList<String> memberslist = new ArrayList<String>();
            for (int i = 0; i < members.length; i++) {
                if (!CallDispatcher.conferenceMembers.contains(members[i])) {
                    memberslist.add(members[i]);
                }
            }

            if (memberslist.size() > 0) {
                Intent intent = new Intent(context,
                        AddGroupMembers.class);
                intent.putExtra("fromcall", true);
                intent.putStringArrayListExtra("buddylist", memberslist);
                startActivityForResult(intent, 3);
//

            } else {
                Toast.makeText(
                        context,
                        SingleInstance.mainContext.getResources().getString(
                                R.string.sorry_no_online_users), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            // check if the request code is same as what is passed here it is 2
            if (requestCode == 3) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    ArrayList<UserBean> list = (ArrayList<UserBean>) bundle
                            .get("list");
                    HashMap<String, UserBean> membersMap = new HashMap<String, UserBean>();
                    for (UserBean userBean : membersList) {
                        membersMap.put(userBean.getBuddyName(), userBean);
                    }
                    for (UserBean userBean : list) {
                        if (!membersMap.containsKey(userBean.getBuddyName())) {
                            membersList.add(userBean);
                        }
                    }
                    for (UserBean bib : membersList) {
                        if (CallDispatcher.conferenceMembers.size() < 3) {

                            if (objCallDispatcher != null) {
                                SignalingBean sb = objCallDispatcher.callconfernceUpdate(
                                        bib.getBuddyName(),
                                        calltype, strSessionId);
                                // june04-Implementation
                                CallDispatcher.conferenceRequest
                                        .put(bib.getBuddyName(), sb);
                            }
                        } else
                            Toast.makeText(
                                    context,
                                    SingleInstance.mainContext
                                            .getResources()
                                            .getString(
                                                    R.string.max_conf_members),
                                    Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showHangUpAlert() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(CallActiveMembersList.this);
                String ask = SingleInstance.mainContext.getResources().getString(
                        R.string.need_call_hangup);

                builder.setMessage(ask)
                        .setCancelable(false)
                        .setPositiveButton(
                                SingleInstance.mainContext.getResources().getString(
                                        R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (activescreen.equalsIgnoreCase("audiocallscreen")) {
                                            if (SingleInstance.instanceTable.containsKey("callscreen")) {
                                                AudioCallScreen audioCallScreen = (AudioCallScreen) SingleInstance.instanceTable.get("callscreen");
                                                audioCallScreen.hangupCallFromCallActiveMembers();
                                            }
                                        } else if (activescreen.equalsIgnoreCase("videocallscreen")) {
                                            if (SingleInstance.instanceTable.containsKey("callscreen")) {
                                                VideoCallScreen videoCallScreen = (VideoCallScreen) SingleInstance.instanceTable.get("callscreen");
                                                videoCallScreen.hangupCallFromCallActiveMembers();
                                            }
                                        }
                                        dialog.cancel();
                                        finishActivity();
                                    }
                                })
                        .setNegativeButton(
                                SingleInstance.mainContext.getResources().getString(
                                        R.string.no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                alert = builder.create();
                alert.show();

            }
        });

    }

    private String[] returnBuddies() {

        String arr[] = CallDispatcher.conferenceMembers
                .toArray(new String[CallDispatcher.conferenceMembers.size()]);
        return arr;
    }

    public String getCurrentDateandTime() {
        try {
            Date curDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss ");
            return sdf.format(curDate).toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private void showCallHistory() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.call_record_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
        dialog.show();
        Button save = (Button) dialog.findViewById(R.id.save);
        Button delete = (Button) dialog.findViewById(R.id.delete);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intentComponent = new Intent(context,
                        CallHistoryActivity.class);
                intentComponent.putExtra("buddyname",
                        CallDispatcher.sb.getFrom());
                intentComponent.putExtra("individual", true);
                intentComponent.putExtra("isDelete", false);
                intentComponent.putExtra("sessionid",
                        CallDispatcher.sb.getSessionid());
                context.startActivity(intentComponent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intentComponent = new Intent(context,
                        CallHistoryActivity.class);
                intentComponent.putExtra("buddyname",
                        CallDispatcher.sb.getFrom());
                intentComponent.putExtra("isDelete", true);
                intentComponent.putExtra("individual", true);
                intentComponent.putExtra("sessionid",
                        CallDispatcher.sb.getSessionid());
                context.startActivity(intentComponent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (SingleInstance.instanceTable.containsKey("callactivememberslist")) {
            SingleInstance.instanceTable.remove("callactivememberslist");
        }
    }

    public void finishActivity() {
        this.finish();
    }
}
