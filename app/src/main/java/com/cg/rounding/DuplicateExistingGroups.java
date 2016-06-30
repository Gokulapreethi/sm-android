package com.cg.rounding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.commonclass.CallDispatcher;
import com.cg.snazmed.R;
import com.group.GroupActivity;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.model.GroupBean;

import java.util.Vector;

/**
 * Created by Rajalakshmi gurunath on 10-06-2016.
 */
public class DuplicateExistingGroups extends Activity {

    private Context context = null;
    private ListView groupListView;
    private ExistingGroupAdapter groupAdapter;
    private Vector<GroupBean> groupslist=new Vector<GroupBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.groupaddcontact);
            context = this;
            SingleInstance.contextTable.put("duplicateexistinggroup", context);
            final TextView txtView01 = (TextView) findViewById(R.id.tx_headingaddcontact);
            RelativeLayout chkbox_lay=(RelativeLayout)findViewById(R.id.chbox_lay);
            Button btn_done=(Button)findViewById(R.id.btn_done);
            Button backBtn=(Button)findViewById(R.id.btn_backaddcontact);
            groupListView=(ListView)findViewById(R.id.lv_buddylist);
            btn_done.setVisibility(View.GONE);
            chkbox_lay.setVisibility(View.GONE);
            txtView01.setText("GROUP(S)");
            for(GroupBean gBean: GroupActivity.groupList){
                if(gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                    groupslist.add(gBean);
                }
            }
            groupAdapter=new ExistingGroupAdapter(this,R.layout.grouplist,groupslist);
            groupListView.setAdapter(groupAdapter);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    GroupBean groupBean = (GroupBean) groupAdapter.getItem(position);
                    Intent intent = new Intent(DuplicateExistingGroups.this,
                            RoundingGroupActivity.class);
                    intent.putExtra("isEdit", true);
                    intent.putExtra("id", groupBean.getGroupId());
                    intent.putExtra("isduplicate",true);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class ExistingGroupAdapter extends ArrayAdapter<GroupBean> {

        private Context context;
        private Typeface tf_regular = null;
        ImageLoader imageLoader;
        Vector<GroupBean> groupslist=new Vector<GroupBean>();

        public ExistingGroupAdapter(Context context, int textViewResourceId,
                            Vector<GroupBean> groupList) {
            super(context, R.layout.grouplist, groupList);
            this.context = context;
            groupslist=groupList;
            imageLoader=new ImageLoader(SingleInstance.mainContext);
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
                    holder.listContainer = (LinearLayout) row
                            .findViewById(R.id.list_container);
                    holder.grouplist = (TextView) row.findViewById(R.id.group_name);
                    holder.header_title = (TextView) row.findViewById(R.id.header_title);
                    holder.members = (TextView) row.findViewById(R.id.members);
                    holder.contact_history = (LinearLayout) row.findViewById(R.id.contact_history);
                    holder.inreq = (LinearLayout) row.findViewById(R.id.inreq);
                    holder.buddy_icon = (ImageView) row.findViewById(R.id.iv_icon);
                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }
                final GroupBean groupBean = (GroupBean) groupslist.get(position);
                TextView tv_groupName = (TextView) row
                        .findViewById(R.id.group_name);
                tv_groupName.setText(groupBean.getGroupName());
                if (tv_groupName == null) {
                    holder.listContainer.setVisibility(View.GONE);
                }
                holder.header_title.setVisibility(View.VISIBLE);
                String name="";
                    name=String.valueOf(groupBean.getGroupName().charAt(0));
                    holder.header_title.setText(name.toUpperCase());
                if(position>0){
                    GroupBean gBean=(GroupBean) groupslist.get(position-1);
                        String name2=String.valueOf(gBean.getGroupName().charAt(0));
                        if(name.equalsIgnoreCase(name2))
                            holder.header_title.setVisibility(View.GONE);
                        else
                            holder.header_title.setVisibility(View.VISIBLE);
                }

                CallDispatcher callDisp=new CallDispatcher(SingleInstance.mainContext);
                final GroupBean gBean = callDisp.getdbHeler(context) .getGroupAndMembers(
                        "select * from groupdetails where groupid=" + groupBean.getGroupId());

                if(gBean.getActiveGroupMembers()!=null && !gBean.getActiveGroupMembers().equalsIgnoreCase("")) {
                    String[] mlist = (gBean.getActiveGroupMembers())
                            .split(",");
                    int membercount = mlist.length + 1;
                    holder.members.setText(Integer.toString(membercount) + " members");
                }else
                    holder.members.setText(Integer.toString(1) + " members");

                if (groupBean.getMessageCount() > 0) {
                    holder.contact_history.setVisibility(View.VISIBLE);
                } else {
                    holder.contact_history.setVisibility(View.GONE);
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
        private   class ViewHolder {
            LinearLayout listContainer;
            TextView grouplist,members,header_title;
            LinearLayout contact_history;
            LinearLayout inreq;
            ImageView buddy_icon;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(SingleInstance.contextTable.containsKey("duplicateexistinggroup")) {
            SingleInstance.contextTable.remove("duplicateexistinggroup");
        }
    }
}
