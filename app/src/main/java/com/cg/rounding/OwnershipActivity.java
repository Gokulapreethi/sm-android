package com.cg.rounding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;

import java.io.File;
import java.util.Vector;

public class OwnershipActivity extends Activity {

    private Context context;
    Vector<UserBean> membersList=new Vector<UserBean>();
    Handler handler = new Handler();
    private ProgressDialog progress = null;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ownership_transfer);
        context = this;
        ListView listView=(ListView)findViewById(R.id.listview_ownership);
        Button back=(Button)findViewById(R.id.cancel);
        Button taransferBtn=(Button)findViewById(R.id.donebtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final String groupid=getIntent().getStringExtra("groupid");
        final GroupBean gBean = DBAccess.getdbHeler().getGroupAndMembers(
                "select * from groupdetails where groupid=" + groupid);
        if (gBean != null) {
            if (gBean.getActiveGroupMembers() != null
                    && gBean.getActiveGroupMembers().length() > 0) {
                String[] mlist = (gBean.getActiveGroupMembers())
                        .split(",");
                for (String tmp : mlist) {
                    UserBean uBean = new UserBean();
                    ProfileBean pbean=DBAccess.getdbHeler().getProfileDetails(tmp);
                    uBean.setFirstname(pbean.getFirstname()+" "+pbean.getLastname());
                    GroupMemberBean bean=DBAccess.getdbHeler().getMemberDetails(groupid, tmp);
                    uBean.setRole(bean.getRole());
                    uBean.setAdmin(bean.getAdmin());
                    uBean.setBuddyName(tmp);
                    membersList.add(uBean);
                }
            }
        }
        MembersAdapter adapter=new MembersAdapter(this,R.layout.rounding_member_row,membersList);
        listView.setAdapter(adapter);
        taransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showprogress();
//                WebServiceReferences.webServiceClient.SetMemberRights(buddyname, gBean.getGroupId(), status,
//                        role, context);
            }
        });
    }
    public class MembersAdapter extends ArrayAdapter<UserBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<UserBean> result;

        public MembersAdapter(Context context, int resource, Vector<UserBean> objects) {
            super(context, resource, objects);
            imageLoader=new ImageLoader(context);
            result = new Vector<UserBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
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
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/COMMedia/" + bib.getProfilePic();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.GONE);
                    holder.edit.setVisibility(View.GONE);
                    holder.statusIcon.setVisibility(View.GONE);
                    holder.sel_buddy.setVisibility(View.VISIBLE);
                    holder.buddyName.setText(bib.getFirstname());
                    if(bib.getRole()!=null)
                    holder.position.setText(bib.getRole());
                    if(bib.getAdmin()!=null) {
                        holder.sel_buddy.setChecked(true);
                        holder.ownsership.setVisibility(View.VISIBLE);
                    }else {
                        holder.sel_buddy.setChecked(false);
                        holder.ownsership.setVisibility(View.GONE);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return convertView;
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
}
