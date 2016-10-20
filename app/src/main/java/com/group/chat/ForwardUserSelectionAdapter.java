package com.group.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.group.GroupActivity;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;

import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by  on 04-03-2016.
 */



public class ForwardUserSelectionAdapter extends ArrayAdapter<BuddyInformationBean> {

    /*********** Declare Used Variables *********/
    private Context context;
    private Vector<BuddyInformationBean> userList;
    private Vector<BuddyInformationBean> originallist;
    private LayoutInflater inflater = null;

    private int checkboxcount;
    private  ForwardFilter filter;
    boolean[] checkBoxState;


    /************* CustomAdapter Constructor *****************/
    public ForwardUserSelectionAdapter(Context context, Vector<BuddyInformationBean> userList) {

        super(context, R.layout.fwd_user_selectrow, userList);
        /********** Take passed values **********/
        this.context = context;
        this.userList = new Vector<BuddyInformationBean>();
        this.userList.addAll(userList);
        originallist = new Vector<BuddyInformationBean>();
        this.originallist.addAll(userList);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkBoxState = new boolean[userList.size()];

        /*********** Layout inflator to call external xml layout () ***********/

    }



    /******
     * Depends upon data size called for each row , Create each ListView row
     *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            ViewHolder holder;

            holder = new ViewHolder();
            if(convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.fwd_user_selectrow, null);
                holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy) ;
                holder.cancel_lay = (LinearLayout) convertView.findViewById(R.id.cancel_lay);
                convertView.setTag(holder);
            }else
                holder = (ViewHolder) convertView.getTag();
            final BuddyInformationBean userBean = userList.get(position);
            if(userBean!=null) {
                Log.d("status", "---->" + userBean.getStatus());
                holder.header_title.setVisibility(View.VISIBLE);
                holder.buddyName.setText(userBean.getFirstname()+" "+userBean.getLastname());

                String cname1, cname2;
                cname1 = String.valueOf(userBean.getLastname().charAt(0));

                holder.header_title.setText(cname1.toUpperCase());

                if (position > 0) {
                    final BuddyInformationBean Binfobean = userList.get(position - 1);
                    cname2 = String.valueOf(Binfobean.getLastname().charAt(0));
                    if (cname1.equalsIgnoreCase(cname2)) {
                        holder.header_title.setVisibility(View.GONE);
                    } else {
                        holder.header_title.setVisibility(View.VISIBLE);
                    }
                }
                ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(userBean.getName());
                    holder.occupation.setText(pBean.getProfession());
                if(userBean.isSelected()){
                    holder.selectUser.setChecked(true);
                }else{
                    holder.selectUser.setChecked(false);
                }
//                holder.selectUser.setChecked(checkBoxState[position]);
                final ForwardUserSelect listmembers = (ForwardUserSelect) WebServiceReferences.contextTable
                        .get("forwarduser");
                holder.selectUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            checkBoxState[position] = true;
                            userBean.setSelected(true);
                            if (listmembers != null) {
                                listmembers.checkBoxCounter1++;
                                listmembers.countofcheckbox(listmembers.checkBoxCounter1);
                            }
                        } else {
                            checkBoxState[position] = false;
                            userBean.setSelected(false);
                            if (listmembers != null) {
                                listmembers.checkBoxCounter1--;
                                listmembers.countofcheckbox(listmembers.checkBoxCounter1);
                            }
                        }
                    }
                });
                if (userBean.getStatus() != null) {
                    Log.d("status1", "---->" + userBean.getStatus());

                    if (userBean.getStatus().equalsIgnoreCase("online")) {
                        holder.statusIcon.setBackgroundResource(R.drawable.online_icon);
                    } else if (userBean.getStatus().equalsIgnoreCase("offline")) {
                        holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                    } else if (userBean.getStatus().equalsIgnoreCase("Away")) {
                        holder.statusIcon.setBackgroundResource(R.drawable.busy_icon);
                        holder.statusIcon.setBackgroundResource(R.drawable.invisibleicon);
                    } else if (userBean.getStatus().equalsIgnoreCase("Airport")) {
                        holder.statusIcon.setBackgroundResource(R.drawable.busy_icon);
                    } else {
                        holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                    }
                }
            }



            return convertView;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
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
                Vector<BuddyInformationBean> buddyInformationBeans = new Vector<BuddyInformationBean>();
                for(int i = 0, l = originallist.size(); i < l; i++)
                {
                    BuddyInformationBean buddyInformationBean = originallist.get(i);
                    if(buddyInformationBean.getName().toLowerCase().startsWith(String.valueOf(constraint)))
                        buddyInformationBeans.add(buddyInformationBean);
                }
                buddyInformationBeans = GroupChatActivity.getAdapterList(buddyInformationBeans);
                result.count = buddyInformationBeans.size();
                result.values = buddyInformationBeans;
            } else {
                synchronized (this) {
                    originallist = GroupChatActivity.getAdapterList(originallist);
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

            userList= (Vector<BuddyInformationBean>)results.values;
            notifyDataSetChanged();
            clear();
            userList = GroupChatActivity.getAdapterList(userList);
            for(int i = 0, l = userList.size(); i < l; i++)
                add(userList.get(i));
            notifyDataSetInvalidated();

        }

    }

    class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon;
        ImageView statusIcon;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
        LinearLayout cancel_lay;
    }

}