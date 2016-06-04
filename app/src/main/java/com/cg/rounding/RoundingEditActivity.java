package com.cg.rounding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.util.SingleInstance;

import org.lib.model.GroupMemberBean;

import java.util.ArrayList;
import java.util.List;

public class RoundingEditActivity extends Activity {
    private String status="0";
    private String buddyname,firstname;
    private String groupid;
    private Context context;
    private String role;

    Handler handler = new Handler();
    private ProgressDialog progress = null;

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
        final CheckBox ch=(CheckBox)findViewById(R.id.chbox1);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buddyname=getIntent().getStringExtra("buddyname");
        firstname=getIntent().getStringExtra("firstname");
        groupid=getIntent().getStringExtra("groupid");
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
        ArrayList<String> list = new ArrayList<String>();
        list.add("Fellow");
        list.add("Attending");
        list.add("Chief Residence");
        list.add("Resident");
        list.add("Med Student");
        list.add("Add New Role");

        CustomAdapter dataAdapter = new CustomAdapter(this, R.layout.memberrights, list);
        rights.setAdapter(dataAdapter);

        if(bean.getRole()!=null){

            rights.setSelection(dataAdapter.getPosition(bean.getRole()));
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
}
