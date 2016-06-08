package com.group.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.GroupChatBean;
import com.bean.UserBean;
import com.cg.commonclass.BuddyListComparator;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.GroupListComparator;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.group.BuddyAdapter;
import com.group.GroupActivity;
import com.group.GroupAdapter;
import com.group.GroupAdapter2;
import com.group.GroupRequestFragment;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;


import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by on 04-03-2016.
 */
public class ForwardUserSelect extends Activity {

    Handler handler = new Handler();
    private Context context = null;
    private TextView countofselection, countofselection_group;
    private  int presentbuddiescount=0;
    private ForwardUserSelectionAdapter adapter;
    private ForwardGroupAdapter forwardGroupAdapter;
    private CheckBox selectAll, selectgroup;
    private int total_count = 0;
    public View _rootView;
    private  GroupBean groupManagementBean;
    private int total_groupcount = 0,count=0;
    private boolean contact=true;
    private boolean fromfiles = false;


    Vector<BuddyInformationBean> buddylist = new Vector<BuddyInformationBean>();
    Vector<GroupBean> buddygroupList = new Vector<GroupBean>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fwd_user_select);
        context = this;
        final Bundle bndl = getIntent().getExtras();
        fromfiles = bndl.getBoolean("fromfile",false);


        final Button search = (Button) findViewById(R.id.btnRegisterOk);
        final EditText searchet = (EditText) findViewById(R.id.searchet);
        selectAll = (CheckBox) findViewById(R.id.btn_selectall);
        final TextView txtView01 = (TextView) findViewById(R.id.txtView01);
        Button send = (Button) findViewById(R.id.Sendbtn);
        ImageView tick_mark = (ImageView)findViewById(R.id.tick_mark);


        if(fromfiles){
            txtView01.setText("SHARE WITH");
            send.setText("DONE");
            tick_mark.setVisibility(View.VISIBLE);

        }else{
            txtView01.setText("FORWARD TO");
            send.setText("SEND");
            tick_mark.setVisibility(View.GONE);
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtView01.getVisibility() == View.VISIBLE) {
                    txtView01.setVisibility(View.GONE);
                    searchet.setVisibility(View.VISIBLE);
                    search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_close));
                } else {
                    txtView01.setVisibility(View.VISIBLE);
                    searchet.setVisibility(View.GONE);
                    search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_search));
                }
            }
        });





        Button close = (Button) findViewById(R.id.cancel);
        LinearLayout Mycontact_forward = (LinearLayout) findViewById(R.id.Mycontact_forward);
        LinearLayout Mygroups_forward = (LinearLayout) findViewById(R.id.Mygroups_forward);

        send.setOnClickListener(new View.OnClickListener() {


            public void onClick(View arg0) {
                boolean isSelectContact = false;

                if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                    String members="";
                    for (BuddyInformationBean Binfobean : buddylist) {
                        if (Binfobean.isSelected() ) {
                            isSelectContact = true;
                           members=members+Binfobean.getEmailid()+",";
                        }
                    }
                    String groupMembers="";
                    for (GroupBean gBean : buddygroupList) {
                        if (gBean.isSelected() ) {
                            isSelectContact = true;
                            groupMembers=groupMembers+gBean.getGroupId()+",";
                        }
                    }
                    Intent resultIntent = new Intent();
                    if(members.length()>0)
                    resultIntent.putExtra("SELECTED_MEMBERS", members.substring(0,members.length()-1));
                    if(groupMembers.length()>0)
                    resultIntent.putExtra("SELECTED_GROUPS", groupMembers.substring(0,groupMembers.length()-1));
                    setResult(RESULT_OK, resultIntent);

                    if (!isSelectContact) {
                        showToast("Buddy not selected");

                    } else {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.message_info_dialog);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                        dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
                        dialog.show();
                        TextView members_selected = (TextView) dialog.findViewById(R.id.tv_ThridLine);
                        TextView group_selected = (TextView)dialog.findViewById(R.id.tv_SecondLine);
                        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                        if (total_groupcount == 1) {
                            group_selected.setText("to " + total_groupcount + " group &");
                        } else {
                            group_selected.setText("to " + total_groupcount + " groups &");
                        }
                        if(total_count == 1){
                            members_selected.setText(total_count + " user");
                        }
                        else {
                            members_selected.setText(total_count + " users");
                        }
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });

                    }
                } else {
                    showAlert1("Info", "Check internet connection Unable to connect server");
                    finish();

                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ImageView im_contact = (ImageView) findViewById(R.id.im_contact);
        final TextView tv_contact = (TextView) findViewById(R.id.tv_contact);
        final View view_contact = (View) findViewById(R.id.view_contact);
        final ImageView im_Group = (ImageView) findViewById(R.id.im_Group);
        final TextView tv_Group = (TextView) findViewById(R.id.tv_Group);
        final View view_Group = (View) findViewById(R.id.view_Group);
        final ListView memberListView = (ListView) findViewById(R.id.listViews1);
        final RelativeLayout chbox_lay = (RelativeLayout) findViewById(R.id.chbox_lay);


        Collections.sort(ContactsFragment.getBuddyList(), new BuddyListComparator());
        for(BuddyInformationBean bib:ContactsFragment.getBuddyList()){
            if(!bib.getStatus().equalsIgnoreCase("pending") && !bib.getStatus().equalsIgnoreCase("new"))
                buddylist.add(bib);
        }

        Collections.sort(GroupActivity.groupList, new GroupListComparator());
        for(GroupBean gBean:GroupActivity.groupList){
            if(!gBean.getStatus().equalsIgnoreCase("request"))
                buddygroupList.add(gBean);
        }

        adapter = new ForwardUserSelectionAdapter(ForwardUserSelect.this, buddylist);
        memberListView.setAdapter(adapter);

        countofselection = (TextView) findViewById(R.id.selected);


        WebServiceReferences.contextTable.put("forwarduser", context);




        selectAll.setTag(true);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (contact == true) {


                    Log.d("inside selection", "---->item position");

                    if ((Boolean) selectAll.getTag()) {
                        for (BuddyInformationBean buddyBean : buddylist) {
                            buddyBean.setSelected(true);
                        }
                        selectAll.setTag(false);
                        countofcheckbox(count);
                        total_count = count;
                    } else {
                        for (BuddyInformationBean buddyBean : buddylist) {
                            buddyBean.setSelected(false);
                        }
                        selectAll.setTag(true);
                        countofcheckbox(0);
                        total_count = count;
                    }
                    adapter.notifyDataSetChanged();


                } else {

                    Log.d("inside selection", "---->item position");

                    if ((Boolean) selectAll.getTag()) {
                        for (GroupBean gbean : buddygroupList) {
                            gbean.setSelected(true);

                        }
                        selectAll.setTag(false);
                        groupcheckbox(count);
                        total_groupcount = count;
                    } else {
                        for (GroupBean groupBean : buddygroupList) {
                            groupBean.setSelected(false);

                        }
                        selectAll.setTag(true);
                        groupcheckbox(0);
                        total_groupcount = count;
                    }
                    forwardGroupAdapter.notifyDataSetChanged();


                }
            }

        });


//        if (!contact) {

            Mycontact_forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contact =true;
                    selectAll.setChecked(false);
                    count = 0;
                    for(BuddyInformationBean bib : buddylist) {
                        if (bib != null) {
                            count++;
                        }
                    }
                    memberListView.setVisibility(View.VISIBLE);
                    im_contact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_profile_white));
                    tv_contact.setTextColor(getResources().getColor(R.color.white));
                    view_contact.setVisibility(View.VISIBLE);
                    im_Group.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members));
                    tv_Group.setTextColor(getResources().getColor(R.color.black));
                    view_Group.setVisibility(View.GONE);
                    chbox_lay.setVisibility(View.VISIBLE);

                    adapter = new ForwardUserSelectionAdapter(ForwardUserSelect.this, buddylist);
                    memberListView.setAdapter(null);
                    memberListView.setAdapter(adapter);
                    
                }
            });

            Mygroups_forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forwardGroupAdapter = new ForwardGroupAdapter(ForwardUserSelect.this, R.layout.group_list, buddygroupList);
                    memberListView.setAdapter(null);
                    memberListView.setAdapter(forwardGroupAdapter);
                    contact = false;
                    selectAll.setChecked(false);
                    count = 0;
                    for (GroupBean groupBean : buddygroupList) {
                        if (groupBean != null) {
                            count++;
                        }
                    }

                    im_contact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_profile));
                    tv_contact.setTextColor(getResources().getColor(R.color.black));
                    view_contact.setVisibility(View.GONE);
                    im_Group.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members_white));
                    tv_Group.setTextColor(getResources().getColor(R.color.white));
                    view_Group.setVisibility(View.VISIBLE);

                    memberListView.setVisibility(View.VISIBLE);
                    chbox_lay.setVisibility(View.VISIBLE);


                }
            });
        searchet.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s != "")
                    if(contact == true) {
                        adapter.getFilter().filter(s);
                    }
                else if(contact==false){
                        forwardGroupAdapter.getFilter().filter(s);
                    }
            }

        });

//        }
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
    public void showAlert1(String title,String message) {

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

    public class ForwardGroupAdapter extends ArrayAdapter<GroupBean> {

        private Context context;
        private Typeface tf_regular = null;

        private Typeface tf_bold = null;
        ImageLoader imageLoader;
        private int checkBoxCounter = 0;
        private  ForwardFilter filter;
        Vector<GroupBean> grouplist = new Vector<GroupBean>();
        Vector<GroupBean> originallist;

        public ForwardGroupAdapter(Context context, int textViewResourceId,
                            Vector<GroupBean> groupList) {

            super(context, R.layout.grouplist, groupList);
            this.context = context;
            imageLoader=new ImageLoader(SingleInstance.mainContext);
            grouplist.addAll(groupList);
            originallist = new Vector<GroupBean>();
            originallist.addAll(groupList);

        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            View row = view;

            try {

                final ViewHolder holder;


                if (row == null) {
                    holder = new ViewHolder();
                    LayoutInflater inflater = (LayoutInflater) this.context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.grouplist, null, false);
                    holder.listContainer = (LinearLayout) row.findViewById(R.id.list_container);
                    holder.grouplist = (TextView) row.findViewById(R.id.group_name);
                    holder.members = (TextView) row.findViewById(R.id.members);
                    holder.header_title = (TextView) row.findViewById(R.id.header_title);
                    holder.header_title.setVisibility(View.VISIBLE);
                    holder.contact_history = (LinearLayout) row.findViewById(R.id.contact_history);
                    holder.inreq = (LinearLayout) row.findViewById(R.id.inreq);
                    holder.buddy_icon = (ImageView) row.findViewById(R.id.iv_icon);
                    holder.sel_buddygroup = (CheckBox) row.findViewById(R.id.sel_buddygroup);
                    holder.sel_buddygroup.setVisibility(View.VISIBLE);
                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }
                final GroupBean groupBean = (GroupBean) grouplist.get(position);
                TextView tv_groupName = (TextView) row
                        .findViewById(R.id.group_name);
                tv_groupName.setText(groupBean.getGroupName());

                String cname1, cname2;
                cname1 = String.valueOf(groupBean.getGroupName().charAt(0));

                holder.header_title.setText(cname1.toUpperCase());

                if (position > 0) {
                    final GroupBean groupbean1 = ContactsFragment.getGroupList().get(position - 1);
                    cname2 = String.valueOf(groupbean1.getGroupName().charAt(0));
                    if (cname1.equalsIgnoreCase(cname2)) {
                        holder.header_title.setVisibility(View.GONE);
                    } else {
                        holder.header_title.setVisibility(View.VISIBLE);
                    }

                }

                if(groupBean.isSelected()){
                    holder.sel_buddygroup.setChecked(true);
                }else{
                    holder.sel_buddygroup.setChecked(false);
                }


                holder.sel_buddygroup
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton arg0,
                                                         boolean isChecked) {
                                Log.d("selecteduser", "---->entering the bean");
                                if (isChecked) {
                                    Log.d("selecteduser", "---->checking the values" + isChecked);
                                    groupBean.setSelected(true);
                                    checkBoxCounter++;
                                        groupcheckbox(checkBoxCounter);

                                } else {
                                    groupBean.setSelected(false);
                                    checkBoxCounter--;
                                        Log.d("selecteduser", "---->checkboxcounter" + checkBoxCounter);
                                        groupcheckbox(checkBoxCounter);

                                }
                            }

                        });
                if (tv_groupName == null) {
                    holder.listContainer.setVisibility(View.GONE);
                }
                tv_groupName.setTypeface(tf_regular);

                CallDispatcher callDisp=new CallDispatcher(SingleInstance.mainContext);
                final GroupBean gBean = callDisp.getdbHeler(context) .getGroupAndMembers(
                        "select * from groupdetails where groupid=" + groupBean.getGroupId());
                Log.i("AAA","Adapter invite list "+gBean.getInviteMembers());
                if(gBean.getActiveGroupMembers()!=null && !gBean.getActiveGroupMembers().equalsIgnoreCase("")) {
                    String[] mlist = (gBean.getActiveGroupMembers())
                            .split(",");
                    Log.i("AAA","Adapter getActiveGroupMembers "+mlist.length);
                    int membercount = mlist.length + 1;
                    holder.members.setText(Integer.toString(membercount) + " members");
                }else
                    holder.members.setText(Integer.toString(1) + " members");
                if (groupBean.getStatus().equalsIgnoreCase("request")) {
                    holder.inreq.setVisibility(View.VISIBLE);
                } else {
                    holder.inreq.setVisibility(View.GONE);
                }
                if(groupBean.getGroupIcon()!=null) {
                    Log.i("AAA","Adapter icon "+groupBean.getGroupIcon());
                    imageLoader.DisplayImage(
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" +
                                    groupBean.getGroupIcon(),
                            holder.buddy_icon,
                            R.drawable.user_photo);
                }



            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return row;
        }
        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new ForwardFilter();
            }
            return filter;
        }

        private class ForwardFilter extends Filter
        {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();

                Filter.FilterResults result = new Filter.FilterResults();
                if (constraint != null && constraint.toString().length() > 0) {
                    Vector<GroupBean> gBeans = new Vector<GroupBean>();
                    for(int i = 0, l = originallist.size(); i < l; i++)
                    {
                        GroupBean gBean = originallist.get(i);
                        if(gBean.getGroupName().toLowerCase().startsWith(String.valueOf(constraint)))
                            gBeans.add(gBean);
                    }
                    gBeans =GroupActivity.getGroupList(gBeans);
                    result.count = gBeans.size();
                    result.values = gBeans;
                } else {
                    synchronized (this) {
                        originallist = GroupActivity.getGroupList(originallist);
                        result.values = originallist;
                        result.count = originallist.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          Filter.FilterResults results) {

                grouplist= (Vector<GroupBean>)results.values;
                notifyDataSetChanged();
                clear();
                grouplist = GroupActivity.getGroupList(grouplist);
                for(int i = 0, l = grouplist.size(); i < l; i++)
                    add(grouplist.get(i));
                notifyDataSetInvalidated();

            }

        }


        private class ViewHolder {
            LinearLayout listContainer;
            TextView grouplist,members, header_title;
            LinearLayout contact_history;
            LinearLayout inreq;
            ImageView buddy_icon;
            CheckBox sel_buddygroup;
        }

    }
    public void countofcheckbox(int count)
    {
        Log.i("asdf","count"+count);
        countofselection.setText(Integer.toString(count) + " Selected");
//        countofselection_group.setText(Integer.toOctalString(groupcount)+ " Selected");
        total_count = count;

    }
    public void groupcheckbox(int groupcount)
    {
        Log.i("asdf", "count" + groupcount);
        countofselection.setText(Integer.toString(groupcount) + " Selected");
//        countofselection_group.setText(Integer.toOctalString(groupcount)+ " Selected");
        total_groupcount = groupcount;

    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        WebServiceReferences.contextTable.remove("forwarduser");
        super.onDestroy();
    }


}
