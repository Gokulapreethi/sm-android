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

import com.cg.commonclass.CallDispatcher;
import com.cg.snazmed.R;
import com.group.GroupRequestFragment;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

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
                holder.grouplist = (TextView) row.findViewById(R.id.group_name);
                holder.members = (TextView) row.findViewById(R.id.members);
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

            CallDispatcher callDisp=new CallDispatcher(SingleInstance.mainContext);
            final GroupBean gBean = callDisp.getdbHeler(context) .getGroupAndMembers(
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
                } else {
                    holder.inreq.setVisibility(View.GONE);
                }

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

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return row;
    }

    public static class ViewHolder {
        LinearLayout listContainer;
        TextView grouplist,members;
        LinearLayout contact_history;
        LinearLayout inreq;
        ImageView buddy_icon,edit_img;
    }
}
