package com.cg.rounding;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bean.ProfileBean;
import com.cg.Calendar.CustomTimePickerDialog;
import com.cg.DB.DBAccess;
import com.cg.DatePicker.DatePickerPopWin;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.PatientDetailsBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.TaskDetailsBean;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

public class TaskCreationActivity extends Activity {

    AutoCompleteTextView patient;
    TextView ed_dueDate;
    private Context context;
    private SimpleDateFormat dateFormatter;
    TextView reminder;
    private String groupid,taskid,patientid;
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
    private TextView remindTime;
    int edit_Year , edit_day, edit_month;
    Vector<PatientDetailsBean> PatientList;

    private HashMap<String,Object> current_open_activity_detail = new HashMap<String,Object>();
    private boolean save_state = false;

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
        final TextView dueTime=(TextView)findViewById(R.id.dueTime);
        ed_dueDate=(TextView)findViewById(R.id.ed_dueDate);
        final TextView ed_dueTime=(TextView)findViewById(R.id.ed_dueTime);
        final TextView title=(TextView)findViewById(R.id.txtView01);

        final TextView patientName=(TextView)findViewById(R.id.patientName);
         ed_assignMember=(TextView)findViewById(R.id.ed_assignMember);
        assignMember=(TextView)findViewById(R.id.assignMember);
        final TextView setReminder=(TextView)findViewById(R.id.setReminder);
        remindTime=(TextView)findViewById(R.id.remindTime);
        reminder=(TextView)findViewById(R.id.reminder);
        final ToggleButton btn_touch=(ToggleButton)findViewById(R.id.btn_touch);
        patient=(AutoCompleteTextView)findViewById(R.id.call_patient);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        groupid=getIntent().getStringExtra("groupid");
        taskid=getIntent().getStringExtra("taskid");
        isEdit=getIntent().getBooleanExtra("isEdit", false);
        patientid=getIntent().getStringExtra("patientid");

        current_open_activity_detail.put("groupid",groupid);
        current_open_activity_detail.put("taskid",taskid);
        current_open_activity_detail.put("isEdit",isEdit);
        current_open_activity_detail.put("patientid",patientid);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                InputMethodManager imm = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                if(imm!=null)
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });

        String strGetQry = "select * from patientdetails where groupid='"
                + groupid + "'";
        PatientList=DBAccess.getdbHeler().getAllPatientDetails(strGetQry);
        if(patientid!=null)
        for(PatientDetailsBean pbean:PatientList){
            if(pbean.getPatientid().equalsIgnoreCase(patientid))
                patient.setText(pbean.getFirstname()+" "+pbean.getLastname());
        }
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
                }
            loadMembers();
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
                    if (isEdit)
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
                    for (PatientDetailsBean bean : PatientList) {
                        String name = bean.getFirstname() + " " + bean.getLastname();
                        if (name.equalsIgnoreCase(taskbean.getPatientname())) {
                            taskbean.setPatientid(bean.getPatientid());
                            break;
                        } else
                            taskbean.setPatientid("");
                    }
                    taskbean.setAssignedMembers(assignedMembers);
                    taskbean.setTaskstatus("0");
                    if(remainderTag==0 && isEdit)
                         Toast.makeText(context, "Please set reminder...", Toast.LENGTH_SHORT).show();
                    else{
                    WebServiceReferences.webServiceClient.SetTaskRecord(taskbean, context);
                    showprogress();}
                }
                else if(ed_taskDesc.getText().toString().trim().length() == 0) {
                    Toast.makeText(context,
                            "Please fill task description...", Toast.LENGTH_SHORT).show();
                } else if(patient.getText().toString().trim().length() == 0) {
                    Toast.makeText(context,
                            "Please assign patient...", Toast.LENGTH_SHORT).show();
                }else if(ed_dueDate.getText().toString().trim().length() == 0) {
                    Toast.makeText(context,
                            "Please select due date ...", Toast.LENGTH_SHORT).show();
                } else if(ed_dueTime.getText().toString().trim().length() == 0) {
                    Toast.makeText(context,
                            "Please select due time...", Toast.LENGTH_SHORT).show();
                } else if(remindTime.getText().toString().trim().length() == 0) {
                    Toast.makeText(context,
                            "Please choose reminder time...", Toast.LENGTH_SHORT).show();
                } else if(PatientList.size() == 0){
                    Toast.makeText(context, "Please create patient before creating new task...", Toast.LENGTH_SHORT).show();
                } else if(remainderTag == 0){
                    Toast.makeText(context, "Please set reminder...", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,
                            "Please enter all mandatory fields", Toast.LENGTH_SHORT).show();
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
                    loadMembers();
                    patientName.setVisibility(View.VISIBLE);
                    patient.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_arrow_up, 0);
                } else {
                    patientName.setVisibility(View.GONE);
                    patient.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.input_arrow, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (PatientList.size() == 0) {
                    patient.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.input_arrow, 0);
                }
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
                    if (ed_dueDate.getText().length() > 0 && ed_dueTime.getText().length()>0) {
                        if (btn_touch.isChecked()) {
                            remainderTag = 1;
                            setReminder.setTextColor(getResources().getColor(R.color.white));
                            reminder.setVisibility(View.VISIBLE);
                            remindTime.setVisibility(View.VISIBLE);
                        } else {
                            remainderTag = 0;
                            setReminder.setTextColor(getResources().getColor(R.color.snazlgray));
                            reminder.setVisibility(View.GONE);
                            remindTime.setVisibility(View.GONE);
                        }
                    }else{
                        btn_touch.setChecked(false);
                        remainderTag = 0;
                        Toast.makeText(context, "Please Select the Due date and time to set reminder", Toast.LENGTH_SHORT).show();
                    }

                }

            });

        final java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
        Log.d("string", "todaydate" + Calendar.getInstance().getTime());
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy h:mm aa");
        final SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy h:mm aa");
        final SimpleDateFormat forPrevDate = new SimpleDateFormat("MM-dd-yyyy");
        final SimpleDateFormat OnlyDate=new SimpleDateFormat(("MM-dd-yyyy"));
        final String datewithNoTime = OnlyDate.format(date);
        final String DateAs_Now = dateFormat.format(date);


        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(TaskCreationActivity.this,true, new DatePickerPopWin.OnDatePickedListener() {
                        @Override
                        public void onDatePickCompleted(int month, int day, int year, int hour, int minute, String am, String dateDesc) {
                            Log.d("string", "selected date" + dateDesc);
                            Log.d("string", "Date Now......" + DateAs_Now);
                            Log.d("string", "Due Date and TIme......" +ed_dueDate.getText().toString()+" "+ed_dueTime.getText().toString());
                            String end_time=ed_dueDate.getText().toString()+" "+ed_dueTime.getText().toString();
                            String start_time=DateAs_Now;
                            Log.d("string", "Date......" + start_time.compareTo(dateDesc));
                            Log.d("string", "bool Date......" + dateDesc.compareTo(end_time));
                            Log.d("string", "Date......" + datewithNoTime);
                            Log.d("string", "user Date......" + ed_dueDate.getText().toString());


                            Date date1 = null;
                            Date date2 = null;
                            Date date3=null;
                            Date currentDateNotime=null;

                            Date UserGivenDate=null;
                            try{
                                date1=myFormat.parse(start_time);
                                date2=myFormat.parse(end_time);
                                date3=myFormat.parse(dateDesc);
                                currentDateNotime=forPrevDate.parse(datewithNoTime);
                                UserGivenDate=forPrevDate.parse(ed_dueDate.getText().toString());
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            if(UserGivenDate.after(currentDateNotime)|| UserGivenDate.equals(currentDateNotime)) {
                                if (date3.after(date1) && date3.before(date2)) {
                                    remindTime.setText(dateDesc);
                                    Log.d("string", "falls between start and end , go to screen 1 ");
                                } else {
                                    Log.d("string", "does not fall between start and end , go to screen 2 ");
                                    Toast.makeText(context, "Please set correct reminder...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }else
                                Toast.makeText(context, "Please select correct date",
                                        Toast.LENGTH_SHORT).show();
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
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(TaskCreationActivity.this, false, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, int hour, int minute, String am, String dateDesc) {
                        edit_Year = year;
                        edit_month = month;
                        edit_day = day;

                        ed_dueDate.setText(dateDesc);
                        Log.d("boolean","valueof1"+dateDesc);
                        dueDate.setVisibility(View.VISIBLE);

//                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
//                        String inputString2 = dateFormat.format(date);
//                        String Today = inputString2;
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date date1 = null;
                        Date date2 = null;
                        String []datevalue= dateDesc.split(" ");
                        try {
                            date1 = myFormat.parse(date);
                            date2 = myFormat.parse(String.valueOf(datevalue[0]));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                            if (date1.compareTo(date2) >0) {
                                ed_dueDate.setText(date);

                            } else {
                                ed_dueDate.setText(datevalue[0]);
                            }
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
                dueTime.setVisibility(View.VISIBLE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(TaskCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

//                        hour = selectedHour;
//                        minute = selectedMinute;
                        String timeSet = "";
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            timeSet = "PM";
                        } else if (selectedHour == 0) {
                            selectedHour += 12;
                            timeSet = "AM";
                        } else if (selectedHour == 12)
                            timeSet = "PM";
                        else
                            timeSet = "AM";

                        String min = "";
                        if (selectedMinute < 10)
                            min = "0" + selectedMinute ;
                        else
                            min = String.valueOf(selectedMinute);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(selectedHour).append(':')
                                .append(min ).append(" ").append(timeSet).toString();
                        ed_dueTime.setText(aTime);
//                        String AM_PM ;
//                        if(selectedHour < 12) {
//                            AM_PM = "AM";
//                        } else {
//                            AM_PM = "PM";
//                        }
//
//                        ed_dueTime.setText( selectedHour + ":" + selectedMinute+ " " + AM_PM);
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
                if(patientid==null)
                patient.showDropDown();
                if(PatientList.size() == 0){
                    Toast.makeText(context, "Please create patient before creating new task...", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });



    }
        public void notifytime(String value) throws Exception {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            Date date = parseFormat.parse(value);
            System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
        }

    public void notifytaskcreated(Object obj) {
        cancelDialog();
        Log.i("sss", "notifytaskcreated updated ");
        if(obj instanceof String[])
        {
            String[] result=(String[])obj;
            SimpleDateFormat datefor = new SimpleDateFormat("MM-dd-yyyy");;
            Date tmpDate=null;
            String timevalue="";
            String alarmtime = remindTime.getText().toString();
            String[] reminder = remindTime.getText().toString().split(" ");
            String Timevalue = reminder[1]+" "+reminder[2];
            Log.d("Valueof","datefor"+reminder[0]);
            Log.d("Valueof","datefor"+reminder[1]);
            Log.d("Valueof","datefor"+remindTime.getText().toString());

            try {
                tmpDate = datefor.parse(reminder[0]);
                Log.d("timevalue","date1"+tmpDate);
            }catch (Exception e) {
                e.printStackTrace();
                Log.d("Valueof", "date1" + e.toString());
            }
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            try {
                Date date = parseFormat.parse(reminder[1] + " " + reminder[2]);
                timevalue = displayFormat.format(date);
                Log.d("timevalue","finaltime"+timevalue);
                Log.d("timevalue","Intialtime"+reminder[1]+reminder[2]);
            }catch (Exception e){
                e.printStackTrace();
            }
            String[] time=timevalue.split(":");
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ScheduleManager.class);
            intent.putExtra("patientname", patient.getText().toString());
            intent.putExtra("Duedate",ed_dueDate.getText().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,
                    intent, 0);
            Calendar cal  = Calendar.getInstance();
            cal.setTime(tmpDate);


            cal.set(Calendar.HOUR, Integer.parseInt(time[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            Log.i("timevalue", "valuehour" + Integer.parseInt(time[0]));
            Log.i("timevalue","valueminute"+Integer.parseInt(time[1]));
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                Log.i("schedulemanager", "build version kitkat and below");
                alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
            } else {
                Log.i("schedulemanager", "build version kitkat above");
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.i("Calender", "value2" + cal.getTimeInMillis());
            }
            taskbean.setTaskId(result[0]);
            DBAccess.getdbHeler().insertorUpdatTaskDetails(taskbean);
            GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
                    .get("groupchat");
            if(gChat!=null) {
                gChat.refreshTask();
            }

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
        if(patientid!=null)
            loadMembers();
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
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/COMMedia/" + bib.getProfile_picpath();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.sel_buddy.setVisibility(View.GONE);
                    holder.header_title.setVisibility(View.GONE);
                    holder.edit.setVisibility(View.GONE);
                    ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(bib.getName());
                    if(pbean!=null)
                        holder.occupation.setText(pbean.getSpeciality());
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
                    GroupBean gmembersbean = DBAccess.getdbHeler().getGroupAndMembers(
                            "select * from groupdetails where groupid="
                                    + groupid);
                    Log.i("AAAA", "#########active Mmber " + gmembersbean.getActiveGroupMembers());
                    Log.i("AAAA", "######### Mmber name " + bib.getName());

                    if (gmembersbean != null) {
                        if (gmembersbean.getActiveGroupMembers() != null
                                && gmembersbean.getActiveGroupMembers().length() > 0) {
                            String[] listRole = (gmembersbean.getActiveGroupMembers())
                                    .split(",");
                            Log.i("AAAA", "#########Mmber " + listRole);

                            for (String tmp : listRole) {
                                GroupMemberBean bean12 = DBAccess.getdbHeler().getMemberDetails(groupid, tmp);
                                boolean found = false;
                                for (String element : listRole) {
                                    if (tmp.equals(bib.getName())) {
                                        Log.i("AAAA", "#########string Role " + bean12.getRole() + " element " + element);
                                        found = true;
                                        break;
                                    }
                                }
                                if (found) {
                                    holder.rights.setVisibility(View.VISIBLE);
                                    holder.position.setVisibility(View.GONE );
                                    holder.rights.setText(bean12.getRole());
                                }
                            }
                        }
                    }

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
    private void loadMembers()
    {
        Log.i("task111", "loadMembers");
        String memberslist = new String();
        members.clear();
        for (PatientDetailsBean bean : PatientList) {
            String name = bean.getFirstname() + " " + bean.getLastname();
            if (name.equalsIgnoreCase(patient.getText().toString())) {
                if (bean.getAssignedmembers() != null)
                    memberslist = bean.getAssignedmembers();
            }
        }
        Log.i("task111", "loadMembers" + memberslist);
        if (memberslist != null && memberslist.length() > 0) {
            String[] mlist = memberslist.split(",");
            for (String tmp : mlist) {
                if (tmp.toString().length() > 0) {
                    BuddyInformationBean uBean = new BuddyInformationBean();
                    ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(tmp);
                    uBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
                    Log.i("task111", "name " + tmp);
                    uBean.setName(tmp);
                    uBean.setProfile_picpath(pbean.getPhoto());
                    uBean.setType("0");
                    members.add(uBean);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if( WebServiceReferences.contextTable.containsKey("taskcreation")) {
                WebServiceReferences.contextTable.remove("taskcreation");
            }

            Object cur_context_object = null;
            Iterator it0 = SingleInstance.current_open_activity_detail.entrySet().iterator();
            while (it0.hasNext()) {
                Map.Entry pair0 = (Map.Entry) it0.next();
                Object cur_context_object0 = pair0.getKey();
                if(cur_context_object0 instanceof TaskCreationActivity) {
                    cur_context_object =cur_context_object0;
                }
            }
            if(cur_context_object != null) {
                Log.i("reopen", "GroupChatActivity containsKey0");
                SingleInstance.current_open_activity_detail.remove(cur_context_object);
            }
            if(save_state){
                if(SingleInstance.current_open_activity_detail.containsKey(context)) {
                    SingleInstance.current_open_activity_detail.remove(context);
                }
                SingleInstance.current_open_activity_detail.put(context,this.current_open_activity_detail);
                save_state = false;
            } else {
                if(SingleInstance.current_open_activity_detail.containsKey(context)) {
                    SingleInstance.current_open_activity_detail.remove(context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishScreenforCallRequest(){
        save_state = true;
        this.finish();
    }
}
