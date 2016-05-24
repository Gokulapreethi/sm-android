package com.cg.callservices;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.cg.commonclass.CallDispatcher;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;

import java.io.File;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            setContentView(R.layout.callmembers_info);
            context = this;

            cancel = (Button) findViewById(R.id.cancel);


            ListView searchResult = (ListView)findViewById(R.id.searchResult);

            Vector<BuddyInformationBean> objects = new Vector<BuddyInformationBean>();

            ProfileBean bean= SingleInstance.myAccountBean;
            String user_name = CallDispatcher.LoginUser;

            BuddyInformationBean ownbean = new BuddyInformationBean();
            ownbean.setFirstname(user_name);
            ownbean.setProfile_picpath(bean.getPhoto());
            objects.add(ownbean);


            for(String user : CallDispatcher.conferenceMembers){
                for (BuddyInformationBean buddyInformationBean : ContactsFragment.getBuddyList()){
                    if(user.equalsIgnoreCase(buddyInformationBean.getEmailid())){
                        objects.add(buddyInformationBean);
                    }
                }
            }
            CallMembersList calladapter = new CallMembersList(context, R.layout.find_people_item, objects);
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
        private  ContactsFilter filter;

        public CallMembersList(Context context, int resource, Vector<BuddyInformationBean> objects) {
            super(context, resource, objects);
            imageLoader=new ImageLoader(context);
            result = new Vector<BuddyInformationBean>();
            result.addAll(objects);
            originalList = new Vector<BuddyInformationBean>();
            originalList.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if(convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.find_people_item, null);
                    holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
                    holder.selectUser.setVisibility(View.GONE);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    convertView.setTag(holder);
                }else
                    holder = (ViewHolder) convertView.getTag();
                final BuddyInformationBean bib = result.get(i);

                if(bib!=null) {
                    if (bib.getProfile_picpath() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/COMMedia/" + bib.getProfile_picpath();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.VISIBLE);
                    String cname1, cname2;
                    cname1 = String.valueOf(bib.getFirstname().charAt(0));
                    if(i == 0) {
                        holder.header_title.setText("From");
                    } else if (i == 1){
                        holder.header_title.setText("To");
                    }


                    if (bib.isSelected()) {
                        holder.selectUser.setChecked(true);
                    } else {
                        holder.selectUser.setChecked(false);
                    }
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
                    }
                    if(bib.getFirstname().equalsIgnoreCase(CallDispatcher.LoginUser)){
                        holder.buddyName.setText("Me");
                    } else {
                        holder.buddyName.setText(bib.getFirstname() + " " + bib.getLastname());
                    }
                    holder.occupation.setText(bib.getOccupation());
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.d("RRRR", "Error FindpeopleAdapter.java => " + e.toString());
            }
            return convertView;
        }
        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new ContactsFilter();
            }
            return filter;
        }
        private class ContactsFilter extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();

                FilterResults result = new FilterResults();
                if (constraint != null && constraint.toString().length() > 0) {
                    Vector<BuddyInformationBean> buddyInformationBeans = new Vector<BuddyInformationBean>();
                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        BuddyInformationBean buddyInformationBean = originalList.get(i);
                        if(buddyInformationBean.getName().toLowerCase().startsWith(String.valueOf(constraint)))
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

                result= (Vector<BuddyInformationBean>)results.values;
                notifyDataSetChanged();
                clear();
                result = GroupChatActivity.getAdapterList(result);
                for(int i = 0, l = result.size(); i < l; i++)
                    add(result.get(i));
                notifyDataSetInvalidated();

            }

        }
    }
    public static class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon;
        ImageView statusIcon;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
    }
}
