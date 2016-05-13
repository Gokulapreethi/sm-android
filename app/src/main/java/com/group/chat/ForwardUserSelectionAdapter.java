package com.group.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bean.UserBean;
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
    private LayoutInflater inflater = null;
    private static int checkBoxCounter = 0;
    private int checkboxcount;


    /************* CustomAdapter Constructor *****************/
    public ForwardUserSelectionAdapter(Context context, Vector<BuddyInformationBean> userList) {

        super(context, R.layout.fwd_user_selectrow, userList);
        /********** Take passed values **********/
        this.context = context;
        this.userList = userList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

//				if (userBean.getIsTitle()) {
//					holder.header_title.setVisibility(View.VISIBLE);
//					holder.header_title.setText(userBean.getHeader());
//				} else {
//					holder.header_title.setVisibility(View.GONE);
//				}
                holder.header_title.setVisibility(View.VISIBLE);
                holder.buddyName.setText(userBean.getFirstname());

                String cname1, cname2;
                cname1 = String.valueOf(userBean.getFirstname().charAt(0));

                holder.header_title.setText(cname1.toUpperCase());

                if (position > 0) {
                    final BuddyInformationBean Binfobean = userList.get(position - 1);
                    cname2 = String.valueOf(Binfobean.getFirstname().charAt(0));
                    if (cname1.equalsIgnoreCase(cname2)) {
                        holder.header_title.setVisibility(View.GONE);
                    } else {
                        holder.header_title.setVisibility(View.VISIBLE);
                    }

                }
                if (userBean.getOccupation() != null && userBean.getOccupation().length()>0 && userBean.getOccupation().equalsIgnoreCase(""))  {

                    holder.occupation.setText(userBean.getOccupation());
                }
//                Log.d("budddyname","--->");


//                if(userBean.getInvite()){
//                    holder.selectUser.setVisibility(View.GONE);
//                    holder.cancel_lay.setVisibility(View.VISIBLE);
//                    holder.occupation.setText("invite Sent");
//                }
                if(userBean.isSelected()){
                    holder.selectUser.setChecked(true);
                }else{
                    holder.selectUser.setChecked(false);
                }
                final ForwardUserSelect listmembers = (ForwardUserSelect) WebServiceReferences.contextTable
                        .get("forwarduser");

                holder.selectUser
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton arg0,
                                                         boolean isChecked) {
                                Log.d("selecteduser", "---->entering the bean");
                                if (isChecked) {
                                    Log.d("selecteduser", "---->checking the values" + isChecked);
                                    userBean.setSelected(true);
                                    checkBoxCounter++;
                                    if (listmembers != null) {
                                        Log.d("selecteduser", "---->counting values " + checkBoxCounter);
                                        listmembers.countofcheckbox(checkBoxCounter);
                                    }
                                } else {
                                    userBean.setSelected(false);
                                    checkBoxCounter--;
                                    if (listmembers != null) {
                                        Log.d("selecteduser", "---->checkboxcounter " + checkBoxCounter);
                                        listmembers.countofcheckbox(checkBoxCounter);
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