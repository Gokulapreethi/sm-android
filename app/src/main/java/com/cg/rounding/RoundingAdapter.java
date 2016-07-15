package com.cg.rounding;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.GroupRequestFragment;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;

import java.util.Vector;

public class RoundingAdapter extends ArrayAdapter<GroupBean> {
    private Context context;
    private Typeface tf_regular = null;

    private Typeface tf_bold = null;
    ImageLoader imageLoader;
    Vector<GroupBean> grouplist;

    public RoundingAdapter(Context context, int textViewResourceId,
                        Vector<GroupBean> groupList) {

        super(context, R.layout.grouplist, groupList);
        this.context = context;
        imageLoader=new ImageLoader(SingleInstance.mainContext);
        grouplist=groupList;

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
                holder.accept_lay = (LinearLayout) row
                        .findViewById(R.id.ll_accept);
                holder.reject_lay = (LinearLayout) row
                        .findViewById(R.id.ll_reject);
                holder.grouplist = (TextView) row.findViewById(R.id.group_name);
                holder.members = (TextView) row.findViewById(R.id.members);
                holder.invite = (TextView) row.findViewById(R.id.invite);
                holder.contact_history = (LinearLayout) row.findViewById(R.id.contact_history);
                holder.inreq = (LinearLayout) row.findViewById(R.id.inreq);
                holder.buddy_icon = (ImageView) row.findViewById(R.id.iv_icon);
                holder.edit_img = (ImageView) row.findViewById(R.id.capture_image_view);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            final GroupBean groupBean = (GroupBean) grouplist.get(position);
            TextView tv_groupName = (TextView) row
                    .findViewById(R.id.group_name);
            tv_groupName.setText(groupBean.getGroupName());
            if (tv_groupName == null) {
                holder.listContainer.setVisibility(View.GONE);
            }
            tv_groupName.setTypeface(tf_regular);
            holder.edit_img.setVisibility(View.VISIBLE);

            final GroupBean gBean = DBAccess.getdbHeler() .getGroupAndMembers(
                    "select * from groupdetails where groupid=" + groupBean.getGroupId());
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
                    holder.invite.setVisibility(View.VISIBLE);
                    holder.invite.setText("invite from " + Buddyname(groupBean.getOwnerName()) );
                } else {
                    holder.invite.setVisibility(View.GONE);
                    holder.inreq.setVisibility(View.GONE);
                }

            if (groupBean.getMessageCount() > 0&& !groupBean.getStatus().equalsIgnoreCase("request")) {
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

            holder.listContainer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        if(groupBean.getStatus().equalsIgnoreCase("request")){
                            GroupRequestFragment groupRequestFragment = GroupRequestFragment.newInstance(SingleInstance.mainContext);
                            groupRequestFragment.setGroupBean(gBean);
                            groupRequestFragment.setRequestFrom(true);
                            groupRequestFragment.setGroupName(groupBean.getGroupName());
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, groupRequestFragment)
                                    .commitAllowingStateLoss();
                        }else {
                            RoundingFragment roundingFragment = RoundingFragment
                                    .newInstance(context);
                            roundingFragment.showGroupChatDialog(groupBean);
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });
            holder.accept_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupRequestFragment.newInstance(SingleInstance.mainContext).showDialog();
                    GroupRequestFragment.newInstance(SingleInstance.mainContext).setRequestFrom(true);
                    WebServiceReferences.webServiceClient.AcceptRejectGroupmember(groupBean.getGroupId(), CallDispatcher.LoginUser, "1", SingleInstance.mainContext);
                }
            });
            holder.reject_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupRequestFragment.newInstance(SingleInstance.mainContext).showDialog();
                    GroupRequestFragment.newInstance(SingleInstance.mainContext).setRequestFrom(true);
                    WebServiceReferences.webServiceClient.AcceptRejectGroupmember(groupBean.getGroupId(), CallDispatcher.LoginUser, "0",SingleInstance.mainContext);
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return row;
    }

    public static class ViewHolder {
        LinearLayout listContainer,accept_lay,reject_lay;
        TextView grouplist,members,invite;
        LinearLayout contact_history;
        LinearLayout inreq;
        ImageView buddy_icon,edit_img;
    }
    public String Buddyname(String bname) {
        String name = null;
                for (BuddyInformationBean buddyInformationBean:ContactsFragment.getBuddyList()) {
                    if (bname.equals(buddyInformationBean.getEmailid())) {
                        name = buddyInformationBean.getFirstname() + " " + buddyInformationBean.getLastname();
                    }
                }
        return name;
    }
}
