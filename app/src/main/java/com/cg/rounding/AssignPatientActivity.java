package com.cg.rounding;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.group.chat.GroupChatActivity;
import com.util.SingleInstance;

import org.lib.PatientDetailsBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

public class AssignPatientActivity extends Activity{

    private Context context;
    private TextView selectedCount;
    private int presentpatientcount=0;
    Handler handler = new Handler();
    public Vector<UserBean> membersList = new Vector<UserBean>();
    private boolean fromMyPatient=false;
    public boolean isSearch=false;
    private ProgressDialog progress = null;
    private int PatientSelectedCount;
    private int Countofcheckeditems;
    CheckBox btn_selectall;

    Vector<PatientDetailsBean> PatientList;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.groupaddcontact);
        context = this;
        WebServiceReferences.contextTable.put("assignpatient", context);

        TextView header=(TextView)findViewById(R.id.tx_headingaddcontact);
        btn_selectall=(CheckBox)findViewById(R.id.btn_selectall);
        selectedCount=(TextView)findViewById(R.id.selected);
        final Button assign=(Button)findViewById(R.id.btn_done);
        final Button back=(Button)findViewById(R.id.btn_backaddcontact);
        final Button unassign=(Button)findViewById(R.id.btn_unassign);
        final LinearLayout unassign_lay=(LinearLayout)findViewById(R.id.unassign_lay);
        assign.setText("ASSIGN MEMBERS");
        ListView listView=(ListView)findViewById(R.id.lv_buddylist);
        PatientList=new Vector<PatientDetailsBean>();
        PatientList.clear();

        final String groupid=getIntent().getStringExtra("groupid");
        String groupname=getIntent().getStringExtra("groupname");
        fromMyPatient=getIntent().getBooleanExtra("fromMyPatient",false);
        header.setText(groupname);
        String strGetQry=null;
        if(GroupChatActivity.patientType.equalsIgnoreCase("mypatient"))
        strGetQry="select * from patientdetails where groupid='"
                + groupid + "' and assignedmembers LIKE '%" + CallDispatcher.LoginUser + "%'";

        else
            strGetQry="select * from patientdetails where groupid='" + groupid + "' and assignedmembers=''";

        PatientList=DBAccess.getdbHeler().getAllPatientDetails(strGetQry);
        Log.i("BBB","patient isMyPatient@@@@@@@@@$$$$$  =="+GroupChatActivity.patientType);


        if(GroupChatActivity.patientType.equalsIgnoreCase("mypatient")){
            unassign_lay.setVisibility(View.VISIBLE);
            assign.setText("ASSIGN MEMBERS");
            unassign.setText("UNASSIGN FROM ME");
        }

        Collections.sort(PatientList, new PatientNameComparator());
        GroupChatActivity.patientType="name";
        final RoundingPatientAdapter adapter=new RoundingPatientAdapter(context,R.layout.rouding_patient_row,PatientList);
        listView.setAdapter(adapter);
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(PatientSelectedCount>0) {
                    Intent intent = new Intent(getApplicationContext(),
                            AddGroupMembers.class);
                    ArrayList<String> buddylist = new ArrayList<String>();
                    for (UserBean userBean : membersList) {
                        buddylist.add(userBean.getBuddyName());
                    }
                    intent.putExtra("fromRounding", true);
                    intent.putExtra("groupid", groupid);
                    intent.putStringArrayListExtra("buddylist", buddylist);
                    Log.i("AAAA", "members list " + buddylist.size());
                    startActivityForResult(intent, 3);
                    isSearch = false;
                }else
                    showToast("Please select any patient");
            }
        });
        unassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isselect=false;
                for(PatientDetailsBean bean: PatientList){
                    if(bean.isSelected()) {
                        isselect=true;
                        String members=bean.getAssignedmembers().replace(CallDispatcher.LoginUser,"");
                        Log.i("AAAA","selected members "+members);
                        bean.setAssignedmembers(members);
                        WebServiceReferences.webServiceClient.SetPatientRecord(bean, context);
                        DBAccess.getdbHeler().insertorUpdatePatientDetails(bean);
                    }
                }
                if(isselect)
                showprogress();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_selectall.setTag(true);
        btn_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((Boolean) btn_selectall.isChecked()) {
                        for (PatientDetailsBean Bean : PatientList) {
                            Bean.setSelected(true);
                        }
                        btn_selectall.setTag(false);
                        countofcheckbox(PatientList.size());
                    } else {
                        for (PatientDetailsBean Bean : PatientList) {
                            Bean.setSelected(false);
                        }
                        btn_selectall.setTag(true);
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
        PatientSelectedCount=count;
        Countofcheckeditems=count;
        selectedCount.setText(Integer.toString(count) + " selected");
        if (count == PatientList.size())
            btn_selectall.setChecked(true);
        else
            btn_selectall.setChecked(false);
    }
    private void showToast(String msg) {
        try {
            Toast.makeText(AssignPatientActivity.this, msg, Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
    public void setSelectall()
    {
        if(PatientList.size()==Countofcheckeditems)
            btn_selectall.setChecked(true);
        else if(Countofcheckeditems<PatientList.size())
            btn_selectall.setChecked(false);
    }
    public void showprogress() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progress = new ProgressDialog(AssignPatientActivity.this);
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
    public void notifySetPatientRecord(Object obj)
    {
        cancelDialog();
        if(obj instanceof String[]){
            Log.i("patientdetails", "notifySetPatientRecord ");
            GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
                    .get("groupchat");
            if(gChat!=null) {
                Log.i("patientdetails", "notifySetPatientRecord call to refresh");
                gChat.refreshPatient();
            }
            killActivity();
        }
    }
    private void killActivity() {
        Log.i("patientdetails", "!!!!!!!!!!!killActivity *****");
        if (SingleInstance.contextTable.containsKey("groupchat"))
        {
            Log.i("patientdetails", "****call to refresh");
            GroupChatActivity groupChatActivity =(GroupChatActivity)SingleInstance.contextTable.get("groupchat");
            groupChatActivity.refreshPatient();
        }
        finish();
    }
    public static boolean useList(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            // check if the request code is same as what is passed here it is 2
            if (requestCode == 3) {
                Log.i("AAAA", "request code 1343454356");
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    ArrayList<UserBean> list = (ArrayList<UserBean>) bundle
                            .get("list");
                    String addedMembers="";
                    for (UserBean temp : list) {
                        if(temp.getBuddyName()!=null)
                            if(addedMembers!=null && addedMembers.length()>0)
                                addedMembers = addedMembers +","+ temp.getBuddyName();
                            else
                                addedMembers=temp.getBuddyName();
                    }
                    boolean isSelect = false;

                    for (PatientDetailsBean bean : PatientList) {
                        String[] member_split = addedMembers.split(",");
                        String[] addedMember_split = bean.getAssignedmembers().split(",");
                        if (bean.isSelected()) {
                            isSelect = true;
                            for(int i=0;i<member_split.length;i++) {
                                if (!useList(addedMember_split, member_split[i])) {
                                    if (bean.getAssignedmembers() != null && bean.getAssignedmembers().length() > 0)

                                        bean.setAssignedmembers(bean.getAssignedmembers() + "," +  member_split[i]);
                                    else
                                        bean.setAssignedmembers( member_split[i] );
                                }else
                                    Log.i("AAAA", "Assigned Member Duplication true=======>");

                            }

                            WebServiceReferences.webServiceClient.SetPatientRecord(bean, context);
                            DBAccess.getdbHeler().insertorUpdatePatientDetails(bean);
                        }
                    }
                    if (isSelect)
                        showprogress();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
