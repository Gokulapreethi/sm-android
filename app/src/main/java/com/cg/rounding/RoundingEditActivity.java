package com.cg.rounding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupMemberBean;

import java.util.ArrayList;
import java.util.HashMap;

public class RoundingEditActivity extends Activity {
    private String status="0";
    private String buddyname,firstname;
    private String groupid;
    private Context context;
    private String role;
    private ImageLoader imageLoader;

    Handler handler = new Handler();
    private ProgressDialog progress = null;
    private boolean isTitle=false;

    private HashMap<String,Object> current_open_activity_detail = new HashMap<String,Object>();
    private boolean save_state = false;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.roun_edit_role);
        context=this;
        WebServiceReferences.contextTable.put("roundingEdit", context);
        final AutoCompleteTextView rights = (AutoCompleteTextView) findViewById(R.id.rights);
        Button cancel=(Button)findViewById(R.id.cancel);
        Button save=(Button)findViewById(R.id.save);
        TextView buddy=(TextView)findViewById(R.id.buddy);
        TextView profession=(TextView)findViewById(R.id.profession);
        ImageView buddypic=(ImageView)findViewById(R.id.riv1);
        ImageView statusIcon=(ImageView)findViewById(R.id.imgstatus);
        final CheckBox ch=(CheckBox)findViewById(R.id.chbox1);
        final ImageView title_img=(ImageView)findViewById(R.id.title_img);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buddyname=getIntent().getStringExtra("buddyname");
        firstname=getIntent().getStringExtra("firstname");
        groupid=getIntent().getStringExtra("groupid");

        current_open_activity_detail = new HashMap<String,Object>();
        current_open_activity_detail.put("activtycontext",context);
        current_open_activity_detail.put("buddyname",buddyname);
        current_open_activity_detail.put("firstname",firstname);
        current_open_activity_detail.put("groupid",groupid);

        buddy.setText(firstname);
        GroupMemberBean bean=DBAccess.getdbHeler().getMemberDetails(groupid,buddyname);
        if(bean.getAdmin()!=null){
            if(bean.getAdmin().equalsIgnoreCase("1")) {
                ch.setChecked(true);
                status="1";
            }
            else {
                ch.setChecked(false);
                status="0";
            }
        }
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ch.isChecked())
                    status = "1";
                else
                    status = "0";
            }
        });
        String profilepic = null,status1 = null;
        ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(buddyname);
        profilepic= pBean.getPhoto();
        imageLoader = new ImageLoader(SingleInstance.mainContext);
        if(buddyname.equalsIgnoreCase(CallDispatcher.LoginUser))
            status1=CallDispatcher.myStatus;
        else
        for(BuddyInformationBean bib: ContactsFragment.getBuddyList()){
            if(bib.getName().equalsIgnoreCase(buddyname)){
                status1=bib.getStatus();
                break;
            }
        }
        if (profilepic != null) {
            String profilePic = profilepic;
            if (profilePic != null && profilePic.length() > 0) {
                if (!profilePic.contains("COMMedia")) {
                    profilePic = Environment
                            .getExternalStorageDirectory()
                            + "/COMMedia/" + profilePic;
                }
                imageLoader.DisplayImage(profilePic, buddypic,
                        R.drawable.img_user);
            }
        }
        if(status1!=null) {
            if (status1.equalsIgnoreCase("online")) {
                statusIcon.setBackgroundResource(R.drawable.online_icon);
            } else if (status1.equalsIgnoreCase("offline")) {
                statusIcon.setBackgroundResource(R.drawable.offline_icon);
            } else if (status1.equalsIgnoreCase("Away")) {
                statusIcon.setBackgroundResource(R.drawable.busy_icon);
            } else if (status1.equalsIgnoreCase("Stealth")) {
                statusIcon.setBackgroundResource(R.drawable.invisibleicon);
            } else if (status1.equalsIgnoreCase("Airport")) {
                statusIcon.setBackgroundResource(R.drawable.busy_icon);
            } else {
                statusIcon.setBackgroundResource(R.drawable.offline_icon);
            }
        }
        ArrayList<String> list = new ArrayList<String>();
        list.add("Fellow");
        list.add("Attending");
        list.add("Chief Residence");
        list.add("Resident");
        list.add("Med Student");
        list.add("Add New Role");

        CustomAdapter dataAdapter = new CustomAdapter(this, R.layout.memberrights, list);
        rights.setAdapter(dataAdapter);
        rights.setThreshold(30);
        rights.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equalsIgnoreCase("fellow")||charSequence.toString().equalsIgnoreCase("Attending")
                        || charSequence.toString().equalsIgnoreCase("Resident") || charSequence.toString().equalsIgnoreCase("Chief Residence")
                        || charSequence.toString().equalsIgnoreCase("Med Student"))
                    rights.setEnabled(false);
                else
                    rights.setEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        if(bean.getRole()!=null && bean.getRole().length()>0){
            rights.setText(bean.getRole());
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showprogress();
               role= rights.getText().toString();
                WebServiceReferences.webServiceClient.SetMemberRights(buddyname, groupid, status,
                        role, context);
            }
        });
        title_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (isTitle) {
                    isTitle = false;
                    rights.dismissDropDown();
                    title_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                } else {
                    isTitle = true;
                    title_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                    rights.showDropDown();
                }
            }
        });
    }

    public void showprogress() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progress = new ProgressDialog(RoundingEditActivity.this);
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

    public void notifyOwnership(Object obj)
    {
        cancelDialog();
        if (obj instanceof String) {
            String result = (String) obj;
            if (result.equalsIgnoreCase("updated")) {
                showToast("Successfully  updated");
                finish();
                RoundingGroupActivity roundingGroup = (RoundingGroupActivity) SingleInstance.contextTable
                        .get("roundingGroup");
                if(status.equalsIgnoreCase("1")) {
                    Log.i("AAAA", "Rounding 1");
                    ContentValues cv = new ContentValues();
                    cv.put("adminmembers", buddyname);
                    DBAccess.getdbHeler(getApplicationContext())
                            .updateGroupMembers(cv, "groupid=" + groupid);
                    GroupMemberBean bean=new GroupMemberBean();
                    bean.setGroupid(groupid);
                    bean.setMembername(buddyname);
                    bean.setRole(role);
                    bean.setAdmin("1");
                    DBAccess.getdbHeler().insertorUpdateMemberDetails(bean);
                    roundingGroup.refreshInviteMembers();
                }else {
                    Log.i("AAAA","Rounding status 0");
                    GroupMemberBean bean=new GroupMemberBean();
                    bean.setGroupid(groupid);
                    bean.setMembername(buddyname);
                    bean.setRole(role);
                    bean.setAdmin("0");
                    DBAccess.getdbHeler().insertorUpdateMemberDetails(bean);
                    roundingGroup.refreshInviteMembers();
                }
            }else
                showToast(result);

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
    public class CustomAdapter extends ArrayAdapter<String> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ArrayList data;

        public CustomAdapter(Context context, int resource, ArrayList objects) {
            super(context, resource, objects);
            data=objects;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if(convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.memberrights, null);
                    holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                    holder.rights = (TextView) convertView.findViewById(R.id.rights);
                    convertView.setTag(holder);
                }else
                    holder = (ViewHolder) convertView.getTag();
                 String list = (String) data.get(i);
                holder.rights.setText(list);
                if(list.equalsIgnoreCase("Add New Role")) {
                    holder.rights.setTextColor(getResources().getColor(R.color.blue2));
                    holder.icon.setVisibility(View.VISIBLE);
                }else {
                    holder.rights.setTextColor(getResources().getColor(R.color.white));
                    holder.icon.setVisibility(View.GONE);
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.d("RRRR","Error FindpeopleAdapter.java => "+e.toString());
            }
            return convertView;
        }
        public View getDropDownView(int position, View convertView, ViewGroup
                parent){
            return getView(position,convertView,parent);

        }
    }
    public static class ViewHolder {
        ImageView icon;
        TextView rights;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(save_state){
            SingleInstance.current_open_activity_detail.putAll(this.current_open_activity_detail);
            save_state = false;
        } else {
            SingleInstance.current_open_activity_detail.clear();
        }
    }

    public void finishScreenforCallRequest(){
        save_state = true;
        this.finish();
    }
}
