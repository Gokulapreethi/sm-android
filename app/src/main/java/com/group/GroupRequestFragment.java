package com.group;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.rounding.RoundingFragment;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.webservice.WebServiceClientProcess;

import java.io.File;
import java.util.Vector;


public class GroupRequestFragment extends Fragment {
    private static GroupRequestFragment groupRequestFragment;
    private Context context;
    private static CallDispatcher calldisp = null;
    View _rootView;
    private GroupBean groupBean=new GroupBean();
    private String groupname;
    int membercount;
    Boolean isFromRounding=false;
    private ProgressDialog progressDialog = null;private Handler handler = new Handler();
    public static GroupRequestFragment newInstance(Context context) {
        try {
            if (groupRequestFragment == null) {
                groupRequestFragment = new GroupRequestFragment();
                groupRequestFragment.setContext(context);
                calldisp = CallDispatcher.getCallDispatcher(context);

            }
            return groupRequestFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return groupRequestFragment;
        }
    }

    public void setContext(Context cxt) {
        this.context = cxt;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppReference.bacgroundFragment=groupRequestFragment;
        Button select = (Button) getActivity().findViewById(R.id.btn_brg);
        select.setVisibility(View.GONE);
        RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
        mainHeader.setVisibility(View.VISIBLE);
        LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
        contact_layout.setVisibility(View.GONE);

        Button imVw = (Button) getActivity().findViewById(R.id.im_view);
        imVw.setVisibility(View.GONE);

        final Button search1 = (Button) getActivity().findViewById(R.id.btn_settings);
        search1.setVisibility(View.GONE);

        final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
        plusBtn.setVisibility(View.GONE);

        TextView title = (TextView) getActivity().findViewById(
                R.id.activity_main_content_title);
        title.setVisibility(View.VISIBLE);
        title.setText(groupname.toUpperCase());

        Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFromRounding) {
                    RoundingFragment roundingFragment = RoundingFragment.newInstance(context);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, roundingFragment)
                            .commitAllowingStateLoss();
                }else{
                    ContactsFragment contactsFragment = ContactsFragment.getInstance(context);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, contactsFragment)
                            .commitAllowingStateLoss();
                }
            }
        });
        _rootView = null;
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.group_request, null);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            try {
                TextView groupName=(TextView)_rootView.findViewById(R.id.groupname);
                TextView groupdesc=(TextView)_rootView.findViewById(R.id.groupdesc);
                TextView members=(TextView)_rootView.findViewById(R.id.members);
                LinearLayout members_list=(LinearLayout)_rootView.findViewById(R.id.lv_buddylist);
                LinearLayout accept=(LinearLayout)_rootView.findViewById(R.id.accept);
                LinearLayout reject=(LinearLayout)_rootView.findViewById(R.id.reject);
                groupName.setText(groupname);
                groupdesc.setText(groupBean.getGroupdescription());
                final Vector<BuddyInformationBean> memberslist=new Vector<BuddyInformationBean>();
                if (groupBean != null) {
                    if (groupBean.getActiveGroupMembers() != null
                            && groupBean.getActiveGroupMembers().length() > 0) {
                        String[] mlist = (groupBean.getActiveGroupMembers())
                                .split(",");
                        BuddyInformationBean bean=new BuddyInformationBean();
                        ProfileBean pbean=SingleInstance.myAccountBean;
                        if(groupBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)){
                            bean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                        }else {
                            for(BuddyInformationBean bib: ContactsFragment.getBuddyList()){
                                if(bib.getName().equalsIgnoreCase(groupBean.getOwnerName())) {
                                    bean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                                    break;
                                }else
                                    bean.setFirstname(groupBean.getOwnerName());
                            }
                        }
                        bean.setName(groupBean.getOwnerName());
                        bean.setSelected(true);
                        memberslist.add(bean);
                        for (String tmp : mlist) {
                            BuddyInformationBean uBean = new BuddyInformationBean();
                            for(BuddyInformationBean bib:ContactsFragment.getBuddyList()){
                                if(bib.getName().equalsIgnoreCase(tmp)) {
                                    uBean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                                    break;
                                }else if(tmp.equalsIgnoreCase(CallDispatcher.LoginUser))
                                    uBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                                else
                                    uBean.setFirstname(tmp);
                            }
                            uBean.setName(tmp);
                            memberslist.add(uBean);
                        }
                    }
                }
                membercount=memberslist.size();
                members.setText("  ("+membercount+")");
                MembersAdapter adapter=new MembersAdapter(SingleInstance.mainContext,R.layout.find_people_item,memberslist);
                final int adapterCount = adapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = adapter.getView(i, null, null);
                    members_list.addView(item);
                }
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog();
                        WebServiceReferences.webServiceClient.AcceptRejectGroupmember(groupBean.getGroupId(), CallDispatcher.LoginUser, "1",SingleInstance.mainContext);
                    }
                });
                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog();
                        WebServiceReferences.webServiceClient.AcceptRejectGroupmember(groupBean.getGroupId(), CallDispatcher.LoginUser, "0",SingleInstance.mainContext);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        return _rootView;
    }
    public void setGroupBean(GroupBean bean) {
        groupBean = bean;
    }
    public void setRequestFrom(Boolean fromGroup) {
        isFromRounding = fromGroup;
    }
    public Boolean getRequestFrom(){
        return isFromRounding;
    }
    public void setGroupName(String name) {
        groupname = name;
    }
    public class MembersAdapter extends ArrayAdapter<BuddyInformationBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<BuddyInformationBean> result;

        public MembersAdapter(Context context, int resource, Vector<BuddyInformationBean> objects) {
            super(context, resource, objects);
            imageLoader=new ImageLoader(context);
            result = new Vector<BuddyInformationBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if(convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.find_people_item, null);
                    holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.memberRole = (TextView) convertView.findViewById(R.id.member_role);
                    convertView.setTag(holder);
                }else
                    holder = (ViewHolder) convertView.getTag();
                final BuddyInformationBean bib = result.get(i);
                if(bib!=null) {
                    if (bib.getProfile_picpath() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" + bib.getProfile_picpath();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.GONE);
                    holder.selectUser.setVisibility(View.GONE);
                    holder.memberRole.setVisibility(View.GONE);
                    if(bib.getStatus()!=null) {
                        if (bib.getStatus().equals("0")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.n_offline);
                        } else if (bib.getStatus().equals("1" )) {
                            holder.statusIcon.setBackgroundResource(R.drawable.n_online);
                        } else if (bib.getStatus().equals("2")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.m_busy);
                        } else if (bib.getStatus().equals("3")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.m_away);
                        } else {
                            holder.statusIcon.setBackgroundResource(R.drawable.n_offline);
                        }
                    }
//                    if(bib.getOccupation()!=null)
//                     holder.occupation.setText(bib.getOccupation());
                    ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(bib.getName());
                    holder.buddyName.setText(bib.getFirstname()+", "+pbean.getProfession());

                    if(pbean!=null)
                        holder.occupation.setText(pbean.getSpeciality());
                    if(bib.isSelected()) {
                        holder.occupation.setText("Owner");
                        holder.occupation.setTextColor(getResources().getColor(R.color.green));
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.d("RRRR", "Error FindpeopleAdapter.java => " + e.toString());
            }
            return convertView;
        }
    }
    public static class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon;
        ImageView statusIcon;
        TextView buddyName;
        TextView occupation;
        TextView header_title,memberRole;
    }
    public void showDialog() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progressDialog = new ProgressDialog(groupRequestFragment.context);
                    if (progressDialog != null) {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Progress ...");
                        progressDialog
                                .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);
                        progressDialog.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }
    public void cancelDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
