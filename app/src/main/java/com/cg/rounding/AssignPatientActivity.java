package com.cg.rounding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.group.chat.GroupChatActivity;

import org.lib.PatientDetailsBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class AssignPatientActivity extends Activity{

    private Context context;
    private TextView selectedCount;
    private int presentpatientcount=0;
    Handler handler = new Handler();
    public Vector<UserBean> membersList = new Vector<UserBean>();
    private boolean fromMyPatient=false;
    public boolean isSearch=false;

    Vector<PatientDetailsBean> PatientList;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.groupaddcontact);
        context = this;
        WebServiceReferences.contextTable.put("assignpatient", context);

        TextView header=(TextView)findViewById(R.id.tx_headingaddcontact);
        final CheckBox btn_selectall=(CheckBox)findViewById(R.id.btn_selectall);
        selectedCount=(TextView)findViewById(R.id.selected);
        final Button assign=(Button)findViewById(R.id.btn_done);
        final Button back=(Button)findViewById(R.id.btn_backaddcontact);
        final Button unassign=(Button)findViewById(R.id.btn_unassign);
        final LinearLayout unassign_lay=(LinearLayout)findViewById(R.id.unassign_lay);
        assign.setText("ASSIGN PATIENTS TO...");
        ListView listView=(ListView)findViewById(R.id.lv_buddylist);
        PatientList=new Vector<PatientDetailsBean>();
        PatientList.clear();
//        UserBean bean=new UserBean();
//        bean.setBuddyName(CallDispatcher.LoginUser);
//        membersList.add(bean);

        final String groupid=getIntent().getStringExtra("groupid");
        String groupname=getIntent().getStringExtra("groupname");
        fromMyPatient=getIntent().getBooleanExtra("fromMyPatient",false);
        header.setText(groupname);
        String strGetQry=null;
            strGetQry = "select * from patientdetails where groupid='"
                    + groupid + "' and assignedmembers=''";
        PatientList=DBAccess.getdbHeler().getAllPatientDetails(strGetQry);
            if(PatientList.size()==0) {
                strGetQry = "select * from patientdetails where groupid='"
                        + groupid + "' and assignedmembers='"+CallDispatcher.LoginUser+"'";
                isSearch=true;
                PatientList=DBAccess.getdbHeler().getAllPatientDetails(strGetQry);
                assign.setText("ASSIGN PATIENTS TO");
                unassign.setText("UNASSIGN FROM ME");
            }
        Collections.sort(PatientList, new PatientNameComparator());
        GroupChatActivity.patientType="name";
        final RoundingPatientAdapter adapter=new RoundingPatientAdapter(context,R.layout.rouding_patient_row,PatientList);
        listView.setAdapter(adapter);
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                    Intent intent = new Intent(getApplicationContext(),
                            AddGroupMembers.class);
                    ArrayList<String> buddylist = new ArrayList<String>();
                    for (UserBean userBean : membersList) {
                        buddylist.add(userBean.getBuddyName());
                    }
                    intent.putExtra("fromRounding",true);
                    intent.putExtra("groupid",groupid);
                    intent.putStringArrayListExtra("buddylist", buddylist);
                    Log.i("AAAA", "members list " + buddylist.size());
                    startActivityForResult(intent, 3);
                    isSearch=false;
//                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                Log.i("AAAA", "select all");
                try {
                    if (isChecked) {
                        for (int i = 0; i < PatientList.size(); i++) {
                            adapter.getItem(i).setSelected(true);
                        }
                        countofcheckbox(PatientList.size());
                    } else {
                        for (int i = 0; i < PatientList.size(); i++) {
                            adapter.getItem(i).setSelected(false);
                        }
                        countofcheckbox(0);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    if (AppReference.isWriteInFile)
                        AppReference.logger.error(e.getMessage(), e);
                    else
                        e.printStackTrace();
                }

            }
        });

    }
    public void countofcheckbox(int count)
    {
        Log.i("asdf", "count" + count);
        selectedCount.setText(Integer.toString(count) + " selected");
    }
}
