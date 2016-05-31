package com.cg.rounding;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bean.ProfileBean;
import com.cg.Calendar.CustomTimePickerDialog;
import com.cg.DB.DBAccess;
import com.cg.DatePicker.DatePickerPopWin;
import com.cg.account.MyAlarmService;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.quickaction.Buddy;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.PatientDetailsBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.TaskDetailsBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class TaskCreationActivity extends Activity {

    AutoCompleteTextView patient;
    TextView ed_dueDate;
    private Context context;
    private SimpleDateFormat dateFormatter;
    TextView reminder;
    private String groupid,taskid;
    private int remainderTag=0;
    private  TextView ed_assignMember;
    private static ProgressDialog pDialog;
    String assignedMembers;
    private TaskDetailsBean taskbean=new TaskDetailsBean();
    Boolean isEdit=false;
    LinearLayout member_lv;
    Vector<BuddyInformationBean> selectedMembers=new Vector<BuddyInformationBean>();
    final Vector<BuddyInformationBean> members=new Vector<BuddyInformationBean>();
    MemberNamesAdapter memberadapter;
    String tick="&#x2713";
    TextView assignMember;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_task);
        context=this;

        WebServiceReferences.contextTable.put("taskcreation", context);
        Button cancel=(Button)findViewById(R.id.cancel);
        Button save_task=(Button)findViewById(R.id.save_task);
        member_lv=(LinearLayout)findViewById(R.id.list_members);

        final EditText ed_taskDesc=(EditText)findViewById(R.id.ed_taskDesc);
        final TextView taskDesc=(TextView)findViewById(R.id.taskDesc);
        final TextView dueDate=(TextView)findViewById(R.id.dueDate);
        ed_dueDate=(TextView)findViewById(R.id.ed_dueDate);
        final TextView ed_dueTime=(TextView)findViewById(R.id.ed_dueTime);
        final TextView title=(TextView)findViewById(R.id.txtView01);

        final TextView patientName=(TextView)findViewById(R.id.patientName);
         ed_assignMember=(TextView)findViewById(R.id.ed_assignMember);
        assignMember=(TextView)findViewById(R.id.assignMember);
        final TextView setReminder=(TextView)findViewById(R.id.setReminder);
        final TextView remindTime=(TextView)findViewById(R.id.remindTime);
        reminder=(TextView)findViewById(R.id.reminder);
        final ToggleButton btn_touch=(ToggleButton)findViewById(R.id.btn_touch);
        patient=(AutoCompleteTextView)findViewById(R.id.call_patient);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        groupid=getIntent().getStringExtra("groupid");
        taskid=getIntent().getStringExtra("taskid");
        isEdit=getIntent().getBooleanExtra("isEdit",false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Vector<PatientDetailsBean> PatientList;
        PatientList=DBAccess.getdbHeler().getAllPatientDetails(groupid);
        if(isEdit) {
            String strQuery="select * from taskdetails where groupid='" + groupid + "'and taskid ='" + taskid+ "'";
            title.setText("EDIT TASK");
            TaskDetailsBean tBean = DBAccess.getdbHeler().getPatientTaskDetails(strQuery);
            ed_taskDesc.setText(tBean.getTaskdesc());
            patient.setText(tBean.getPatientname());
            ed_dueTime.setText(tBean.getDuetime());
            ed_dueDate.setText(tBean.getDuedate());
            remindTime.setText(tBean.getTimetoremind());
            if(tBean.getAssignedMembers()!=null && tBean.getAssignedMembers().length()>0) {
                    String[] list=tBean.getAssignedMembers().split(",");
                    String names="";
                selectedMembers.clear();
                    for(String tmp:list) {
                        ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(tmp);
                        BuddyInformationBean bean=new BuddyInformationBean();
                        bean.setName(tmp);
                        if(pBean.getTitle()!= null && pBean.getTitle() != "") {
                            bean.setFirstname(pBean.getTitle() + " " + pBean.getFirstname());
                        }else
                            bean.setFirstname(pBean.getFirstname() + " " + pBean.getLastname());
                        selectedMembers.add(bean);
                    }
                memberadapter = new MemberNamesAdapter(context, R.layout.task_member_names, selectedMembers);
                member_lv.removeAllViews();
                final int adapterCount = memberadapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = memberadapter.getView(i, null, null);
                    member_lv.addView(item);
                }
//                    ed_assignMember.setText(names.substring(0,names.length() - 2));
                }
            if(tBean.getSetreminder().equalsIgnoreCase("1")){
                btn_touch.setChecked(true);
                remainderTag=1;
                    setReminder.setTextColor(getResources().getColor(R.color.white));
                    reminder.setVisibility(View.VISIBLE);
                    remindTime.setVisibility(View.VISIBLE);
                } else {
                btn_touch.setChecked(false);
                    remainderTag=0;
                    setReminder.setTextColor(getResources().getColor(R.color.snazlgray));
                    reminder.setVisibility(View.GONE);
                    remindTime.setVisibility(View.GONE);
                }
        }
        save_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_taskDesc.getText().toString().trim().length() > 0
                        && patient.getText().toString().trim().length() > 0
                        && ed_dueDate.getText().toString().trim().length() > 0
                        && ed_dueTime.getText().toString().trim().length() > 0
                        && remindTime.getText().toString().trim().length() > 0) {
                    taskbean.setGroupid(groupid);
                    if(isEdit)
                        taskbean.setTaskId(taskid);
                    else
                    taskbean.setTaskId("");
                    taskbean.setCreatorName(CallDispatcher.LoginUser);
                    taskbean.setTaskdesc(ed_taskDesc.getText().toString().trim());
                    taskbean.setPatientname(patient.getText().toString().trim());
                    taskbean.setDuedate(ed_dueDate.getText().toString().trim());
                    taskbean.setDuetime(ed_dueTime.getText().toString().trim());
                    taskbean.setSetreminder(Integer.toString(remainderTag));
                    taskbean.setTimetoremind(remindTime.getText().toString().trim());
                    for(PatientDetailsBean bean:PatientList){
                        String name=bean.getFirstname()+" "+bean.getLastname();
                        if(name.equalsIgnoreCase(taskbean.getPatientname())) {
                            taskbean.setPatientid(bean.getPatientid());
                            break;
                        }else
                            taskbean.setPatientid("");
                    }
                    taskbean.setAssignedMembers(assignedMembers);
                    taskbean.setTaskstatus("0");
                    WebServiceReferences.webServiceClient.SetTaskRecord(taskbean, context);
                    showprogress();
                } else {
                    Toast.makeText(context,
                            "Please enter all mandatory fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        ed_taskDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    taskDesc.setVisibility(View.VISIBLE);
                else
                    taskDesc.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        patient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    patientName.setVisibility(View.VISIBLE);
                    patient.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
                }
                else {
                    patientName.setVisibility(View.GONE);
                    patient.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.input_arrow, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ed_assignMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMembers();
            }
        });
        btn_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_touch.isChecked()) {
                    remainderTag=1;
                    setReminder.setTextColor(getResources().getColor(R.color.white));
                    reminder.setVisibility(View.VISIBLE);
                    remindTime.setVisibility(View.VISIBLE);
                } else {
                    remainderTag=0;
                    setReminder.setTextColor(getResources().getColor(R.color.snazlgray));
                    reminder.setVisibility(View.GONE);
                    remindTime.setVisibility(View.GONE);
                }
            }
        });
        final java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(TaskCreationActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int month, int day, int year, String dateDesc) {
                        Toast.makeText(TaskCreationActivity.this, dateDesc, Toast.LENGTH_SHORT).show();

                        remindTime.setText(dateDesc);
//                        SimpleDateFormat datefor = new SimpleDateFormat("yyyy/MM/dd");
//                        Date tmpDate=null;
//
//                        try {
//                            tmpDate = datefor.parse(dateDesc);
//                        }catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                        Intent intent = new Intent(getBaseContext(), MyAlarmService.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1000,
//                                intent, 0);
//                        Calendar cal  = Calendar.getInstance();
//                        cal .setTime(tmpDate);
////                        cal .set(Calendar.HOUR, Integer.parseInt(strTime.split(":")[0]));
////                        cal .set(Calendar.MINUTE, Integer.parseInt(strTime.split(":")[1]));
//
//                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//                            Log.i("schedulemanager", "build version kitkat and below");
//                            alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
//                        } else {
//                            Log.i("schedulemanager", "build version kitkat above");
//                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//                        }

                    }
                }).textConfirm("DONE") //text of confirm button
                        .textCancel("CANCEL") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .minYear(Calendar.getInstance().get(Calendar.YEAR)) //min year in loop
                        .maxYear(2550) // max year in loop
                        .dateChose(todayDate.toString()) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(TaskCreationActivity.this);

            }
        });


        ed_dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(TaskCreationActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        ed_dueDate.setText(dateDesc);
                        dueDate.setVisibility(View.VISIBLE);
//                        Toast.makeText(TaskCreationActivity.this, dateDesc, Toast.LENGTH_SHORT).show();
                    }
                }).textConfirm("DONE") //text of confirm button
                        .textCancel("CANCEL") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .minYear(Calendar.getInstance().get(Calendar.YEAR)) //min year in loop
                        .maxYear(2550) // max year in loop
                        .dateChose(todayDate.toString()) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(TaskCreationActivity.this);
            }
        });
        ed_dueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(TaskCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }

                        ed_dueTime.setText( selectedHour + ":" + selectedMinute+ " " + AM_PM);
                    }
                }, hour, minute, false);//if true ,Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        List<String> list = new ArrayList<String>();
        for(PatientDetailsBean bean:PatientList){
            list.add(bean.getFirstname()+" "+bean.getLastname());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown_list, list);

        dataAdapter.setDropDownViewResource
                (R.layout.spinner_dropdown_list);

        patient.setAdapter(dataAdapter);
        patient.setThreshold(1);
        patient.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                patient.showDropDown();
                patient.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
                return false;
            }
        });

        GroupBean gBean = DBAccess.getdbHeler().getGroupAndMembers("select * from groupdetails where groupid="
                + groupid);
        if (gBean != null) {
            if (gBean.getActiveGroupMembers() != null
                    && gBean.getActiveGroupMembers().length() > 0) {
                Log.i("AAAA","RoundingMember");
                String[] mlist = (gBean.getActiveGroupMembers())
                        .split(",");
                BuddyInformationBean bean=new BuddyInformationBean();
                ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(gBean.getOwnerName());

                if(pBean.getTitle()!= null && pBean.getTitle() != "") {
                    bean.setFirstname(pBean.getTitle() + " " + pBean.getFirstname());
                }else
                    bean.setFirstname(pBean.getFirstname() + " " + pBean.getLastname());
//                bean.setFirstname(pBean.getFirstname() + " " + pBean.getLastname());
                bean.setName(gBean.getOwnerName());
                bean.setProfile_picpath(pBean.getPhoto());
                bean.setType("0");
                members.add(bean);
                for (String tmp : mlist) {
                    BuddyInformationBean uBean = new BuddyInformationBean();
                    ProfileBean pbean=DBAccess.getdbHeler().getProfileDetails(tmp);
                    if(pbean.getTitle()!= null && pbean.getTitle() != "") {
                        uBean.setFirstname(pbean.getTitle() + " " + pbean.getFirstname());
                    }else
                        uBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
//                    uBean.setFirstname(pbean.getFirstname()+" "+pbean.getLastname());
                    uBean.setName(tmp);
                    uBean.setProfile_picpath(pbean.getPhoto());
                    uBean.setType("0");
                    members.add(uBean);
                }
            }
        }

    }
    public void notifytaskcreated(Object obj) {
        cancelDialog();
        Log.i("sss", "notifytaskcreated updated ");
        if(obj instanceof String[])
        {
            String[] result=(String[])obj;
            taskbean.setTaskId(result[0]);
            DBAccess.getdbHeler().insertorUpdatTaskDetails(taskbean);
            GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
                    .get("groupchat");
            gChat.refreshTask();

            finish();
        }
    }


    private void showMembers()
    {
        final Dialog dialog1 = new Dialog(TaskCreationActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.rounding_assign);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
        dialog1.show();
        ListView listView=(ListView)dialog1.findViewById(R.id.listview_assign);
        Button cancel=(Button)dialog1.findViewById(R.id.cancel);
        final TextView AssignMembers=(TextView)dialog1.findViewById(R.id.donebtn);


        final MembersAdapter adapter=new MembersAdapter(this,R.layout.rounding_member_row, members);
        listView.setAdapter(adapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        AssignMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addnames="";
                selectedMembers.clear();
                for(BuddyInformationBean bean:members){
                    if(bean.isSelected()) {
                        selectedMembers.add(bean);
                        assignMember.setVisibility(View.VISIBLE);
                        addnames = addnames + "" + bean.getName() + ",";
                    }
                }
                if(addnames.length()>0) {
                    memberadapter = new MemberNamesAdapter(context, R.layout.task_member_names, selectedMembers);
                    member_lv.removeAllViews();
                    final int adapterCount = memberadapter.getCount();

                    for (int i = 0; i < adapterCount; i++) {
                        View item = memberadapter.getView(i, null, null);
                        member_lv.addView(item);
                    }
                    assignedMembers = addnames.substring(0, addnames.length() - 1);
                    dialog1.dismiss();
                }else {
                    Toast.makeText(TaskCreationActivity.this, "Please select any member", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }
    public class MembersAdapter extends ArrayAdapter<BuddyInformationBean> {

        private LayoutInflater inflater = null;
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
                final ViewHolder holder;
                if(convertView == null) {
                    holder = new ViewHolder();
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.rounding_member_row, null);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.overlay = (ImageView) convertView.findViewById(R.id.overlay);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.position = (TextView) convertView.findViewById(R.id.position);
                    holder.rights = (TextView) convertView.findViewById(R.id.rights);
                    holder.sel_buddy=(CheckBox) convertView.findViewById(R.id.sel_buddy);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.member_lay=(LinearLayout)convertView.findViewById(R.id.member_lay);
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
                    holder.sel_buddy.setVisibility(View.GONE);
                    holder.header_title.setVisibility(View.GONE);
                    holder.edit.setVisibility(View.GONE);
                    holder.buddyName.setText(bib.getFirstname());
                    if(bib.getName().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                        holder.buddyName.setText("Me");
                        holder.rights.setVisibility(View.VISIBLE);
                        holder.rights.setText("attending");
                        holder.position.setText("administrator");
                        holder.position.setTextColor(getResources().getColor(R.color.blue2));
                    }else{
                        holder.rights.setVisibility(View.GONE);
                        holder.position.setTextColor(getResources().getColor(R.color.snazash));
                    }
                    holder.rights.setTextColor(getResources().getColor(R.color.snazash));
                    holder.statusIcon.setVisibility(View.GONE);
                    if (bib.getType().equalsIgnoreCase("1")) {
                        bib.setSelected(true);
                        holder.overlay.setVisibility(View.VISIBLE);
                    }
                    holder.member_lay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (bib.getType().equalsIgnoreCase("0")) {
                                bib.setSelected(true);
                                holder.overlay.setVisibility(View.VISIBLE);
                                bib.setType("1");
                            } else {
                                bib.setSelected(false);
                                holder.overlay.setVisibility(View.GONE);
                                bib.setType("0");
                            }
                        }
                    });


                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
    }
    public static class ViewHolder {
        ImageView buddyicon,overlay;
        ImageView statusIcon,edit;
        TextView buddyName;
        TextView occupation,position,rights;
        TextView header_title;
        LinearLayout member_lay;
        CheckBox sel_buddy;
    }
    private void showprogress() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Progress ...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();

    }
    public static void cancelDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    private Vector<BuddyInformationBean> result;
    public class MemberNamesAdapter extends ArrayAdapter<BuddyInformationBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;

        public MemberNamesAdapter(Context context, int resource, Vector<BuddyInformationBean> objects) {
            super(context, resource, objects);
            imageLoader=new ImageLoader(context);
            result = new Vector<BuddyInformationBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                if(convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.task_member_names, null);
                    convertView.setTag(holder);
                }
                TextView buddyName= (TextView) convertView.findViewById(R.id.membername);
                ImageView closeIcon = (ImageView) convertView.findViewById(R.id.closebtn);
                final BuddyInformationBean bib = result.get(i);
                if(bib!=null) {
                    if(bib.getFirstname()!=null)
                        buddyName.setText(bib.getFirstname());
                    closeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteUser(bib.getName());
                        }
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
    }
    public void deleteUser(String name) {
        // TODO Auto-generated method stub
        Log.i("AAAA", "deleteUser ");
        for (BuddyInformationBean bib : result) {
            if (bib.getName().equals(name)) {
                result.remove(bib);
                selectedMembers.remove(bib);
                memberadapter.notifyDataSetChanged();
                member_lv.removeAllViews();
                final int adapterCount = memberadapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = memberadapter.getView(i, null, null);
                    member_lv.addView(item);
                }
                return;
            }
        }
    }
}
