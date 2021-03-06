package com.cg.rounding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Fingerprint.MainActivity;
import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.account.PinSecurity;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.WebServiceBean;

import java.io.File;
import java.util.Vector;

public class OwnershipActivity extends Activity {

    private Context context;
    Vector<UserBean> membersList=new Vector<UserBean>();
    Handler handler = new Handler();
    private ProgressDialog progress = null;
    MembersAdapter adapter;
    String groupid,groupowner;
    private GroupBean gBean;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ownership_transfer);
        context = this;
        ListView listView=(ListView)findViewById(R.id.listview_ownership);
        Button back=(Button)findViewById(R.id.cancel);
        Button taransferBtn=(Button)findViewById(R.id.donebtn);
        final TextView title=(TextView)findViewById(R.id.txtView01);
        final EditText ed_search=(EditText)findViewById(R.id.ed_search);
        final Button search=(Button)findViewById(R.id.search);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        groupid=getIntent().getStringExtra("groupid");
        gBean = DBAccess.getdbHeler().getGroupAndMembers(
                "select * from groupdetails where groupid=" + groupid);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( ed_search.getVisibility()==View.VISIBLE){
                    title.setVisibility(View.VISIBLE);
                    ed_search.setVisibility(View.GONE);
                    search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_search));
                }else {
                    title.setVisibility(View.GONE);
                    ed_search.setVisibility(View.VISIBLE);
                    ed_search.setText("");
                    search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_close));
                }
            }
        });
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s != "")
                    adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (gBean != null) {
            if (gBean.getInviteMembers() != null
                    && gBean.getInviteMembers().length() > 0) {
                String[] mlist = (gBean.getInviteMembers())
                        .split(",");

                for (String tmp : mlist) {
                    UserBean uBean = new UserBean();
                    for(BuddyInformationBean bib: ContactsFragment.getBuddyList()){
                        if(bib.getName().equalsIgnoreCase(tmp)){
                            uBean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                            uBean.setProfilePic(bib.getProfile_picpath());
                            break;
                        }
                    }
                    GroupMemberBean bean=DBAccess.getdbHeler().getMemberDetails(groupid, tmp);
                    uBean.setRole(bean.getRole());
                    uBean.setAdmin(bean.getAdmin());
                    uBean.setBuddyName(tmp);
                    uBean.setGroupid(gBean.getGroupId());
                    membersList.add(uBean);
                }
            }
        }
        adapter=new MembersAdapter(this,R.layout.rounding_member_row,membersList);
        listView.setAdapter(adapter);
        taransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showprogress();
                for(UserBean uBean:membersList){
                    Log.i("AAAA", "Rounding status 1" + uBean.isSelected());
                    if(uBean.isSelected()) {
                        groupowner=uBean.getBuddyName();
                        WebServiceReferences.webServiceClient.TransferOwnership(CallDispatcher.LoginUser, gBean.getGroupId(),
                                uBean.getBuddyName(), gBean.getOwnerName(), context);
                    }
                }
            }
        });
    }
    public class MembersAdapter extends ArrayAdapter<UserBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<UserBean> result;
        private Vector<UserBean> originalList;
        private  ContactsFilter filter;
        int selected_position = -1;

        public MembersAdapter(Context context, int resource, Vector<UserBean> objects) {
            super(context, resource, objects);
            imageLoader=new ImageLoader(context);
            result = new Vector<UserBean>();
            result.addAll(objects);
            originalList = new Vector<UserBean>();
            originalList.addAll(objects);
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if(convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.rounding_member_row, null);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.position = (TextView) convertView.findViewById(R.id.position);
                    holder.ownsership = (TextView) convertView.findViewById(R.id.rights);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.sel_buddy=(CheckBox)convertView.findViewById(R.id.sel_buddy);
                    convertView.setTag(holder);
                }else
                    holder = (ViewHolder) convertView.getTag();
                final UserBean bib = result.get(i);
                if(bib!=null) {
                    if (bib.getProfilePic() != null) {
                        Log.i("AAAA","profile ======"+bib.getProfilePic());
                        String pic_Path =bib.getProfilePic();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.GONE);
                    holder.edit.setVisibility(View.GONE);
                    holder.statusIcon.setVisibility(View.GONE);
                    holder.sel_buddy.setVisibility(View.VISIBLE);
                    if(bib.getFirstname()!=null && bib.getFirstname().length()>0)
                    holder.buddyName.setText(bib.getFirstname());
                    else
                        holder.buddyName.setText(bib.getBuddyName());
                    if(bib.getRole()!=null)
                    holder.position.setText(bib.getRole());
                    Log.i("AAAA", "admin " + bib.getAdmin());
                    if(bib.getAdmin()!=null && bib.getAdmin().equalsIgnoreCase("1")) {
                        holder.sel_buddy.setChecked(true);
                        holder.ownsership.setVisibility(View.VISIBLE);
                    }else {
                        holder.sel_buddy.setChecked(false);
                        holder.ownsership.setVisibility(View.GONE);
                    }
                    if(selected_position==i) {
                        holder.sel_buddy.setChecked(true);
                    } else {
                        holder.sel_buddy.setChecked(false);
                    }
                    holder.sel_buddy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((CheckBox) v).isChecked()) {
                                bib.setSelected(true);
                                selected_position = i;
                            } else {
                                bib.setSelected(false);
                                selected_position = -1;
                            }
                            notifyDataSetChanged();
                        }
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
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
                    Vector<UserBean> buddyInformationBeans = new Vector<UserBean>();
                        for(int i = 0, l = originalList.size(); i < l; i++)
                        {
                            UserBean buddyInformationBean = originalList.get(i);
                            String buddyname=buddyInformationBean.getFirstname();
                            if(buddyname.toLowerCase().contains(String.valueOf(constraint)))
                                buddyInformationBeans.add(buddyInformationBean);
                        }

                    result.count = buddyInformationBeans.size();
                    result.values = buddyInformationBeans;
                } else {
                    synchronized (this) {
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

                result= (Vector<UserBean>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = result.size(); i < l; i++)
                    add(result.get(i));
                notifyDataSetInvalidated();

            }

        }
    }
    public static class ViewHolder {
        ImageView buddyicon;
        ImageView statusIcon,edit;
        TextView buddyName;
        TextView occupation,position,ownsership;
        TextView header_title;
        CheckBox sel_buddy;
    }
    public void showprogress() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progress = new ProgressDialog(OwnershipActivity.this);
                    if (progress != null) {
                        progress.setCancelable(false);
                        progress.setMessage("Progress ...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setProgress(0);
                        progress.setMax(100);
                        progress.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void showToast(final String message) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public void notifyOwnership(Object obj)
    {
        cancelDialog();
        if (obj instanceof String) {
            String result = (String) obj;
            if (result.equalsIgnoreCase("updated")) {
                showToast("Successfully  updated");
                String activemembers=gBean.getActiveGroupMembers().replace(groupowner, CallDispatcher.LoginUser);
                String invitemembers=gBean.getInviteMembers().replace(groupowner,CallDispatcher.LoginUser);
                ContentValues cv=new ContentValues();
                cv.put("groupowner",groupowner);
                DBAccess.getdbHeler().updateGroup(cv, groupid);
                cv.put("active_members",activemembers);
                cv.put("invitemembers",invitemembers);
                DBAccess.getdbHeler().updateGroupMembers(cv,groupid);
                finish();
            }
        }else if(obj instanceof WebServiceBean) {
            WebServiceBean server_response = (WebServiceBean) obj;
            if (server_response.getText() != null) {
                showToast(server_response.getText());
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        AppReference.mainContext.isApplicationBroughtToBackground();

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AppMainActivity.inActivity = this;
        context = this;
        if(AppReference.mainContext.isPinEnable) {
            if (AppReference.mainContext.openPinActivity) {
                AppReference.mainContext.openPinActivity=false;
                if(Build.VERSION.SDK_INT>20 && AppReference.mainContext.isTouchIdEnabled) {
                    Intent i = new Intent(OwnershipActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(OwnershipActivity.this, PinSecurity.class);
                    startActivity(i);
                }
            } else {
                AppReference.mainContext.count=0;
                AppReference.mainContext.registerBroadcastReceiver();
            }
        }
    }


}
