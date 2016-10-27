package com.cg.rounding;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.model.TaskDetailsBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class RoundingTaskAdapter extends ArrayAdapter<TaskDetailsBean> {

    private Context context;
    ImageLoader imageLoader;
    Vector<TaskDetailsBean> tasklist;
    Handler handler = new Handler();


    public RoundingTaskAdapter(Context context, int textViewResourceId,
                               Vector<TaskDetailsBean> taskList) {

        super(context, R.layout.rouding_patient_row, taskList);
        this.context = context;
        tasklist = new Vector<TaskDetailsBean>();
        tasklist.addAll(taskList);

    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        View row = view;

        try {
            final ViewHolder holder;


            if (row == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.round_task_row, null, false);
                holder.patientname = (TextView) row.findViewById(R.id.patientname);
                holder.doctorname = (TextView) row.findViewById(R.id.doctor_name);
                holder.taskname = (TextView) row.findViewById(R.id.taskname);
                holder.pending_hours = (TextView) row.findViewById(R.id.pending_time);
                holder.header = (RelativeLayout) row.findViewById(R.id.task_header);
                holder.headertext = (TextView) row.findViewById(R.id.taskheader_text);
                holder.linear_list = (LinearLayout) row.findViewById(R.id.linear_list);
                holder.chbox1 = (CheckBox) row.findViewById(R.id.chbox1);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            final TaskDetailsBean tBean = tasklist.get(position);
            imageLoader = new ImageLoader(SingleInstance.mainContext);
            if (tBean != null) {
                if (tBean.getTaskdesc() != null)
                    holder.taskname.setText(tBean.getTaskdesc());
                if (tBean.getPatientname() != null)
                    holder.patientname.setText(tBean.getPatientname());
                Log.i("taskdetails", "adpater " + tBean.getAssignedMembers());
                if (tBean.getAssignedMembers() != null && !tBean.getAssignedMembers().equalsIgnoreCase("") &&
                        !tBean.getAssignedMembers().equalsIgnoreCase("null")) {
                    String[] list = tBean.getAssignedMembers().split(",");
                    String names = "";
                    for (String tmp : list) {
                        ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(tmp);
                        names = names + pBean.getTitle() + " " + pBean.getFirstname() + ", ";
                    }
                    holder.doctorname.setText(names.substring(0, names.length() - 2));
                } else
                    holder.doctorname.setText("Unassigned");
                if(tBean.getHeadercode()!=null) {
                    if (tBean.getHeadercode().equalsIgnoreCase("1")) {
                        holder.pending_hours.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.pink));
                    } else if (tBean.getHeadercode().equalsIgnoreCase("2")) {
                        holder.pending_hours.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.green));
                    } else if (tBean.getHeadercode().equalsIgnoreCase("3")) {
                        holder.pending_hours.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.white));
                    }
                }

                if (tBean.getDuedate() != null) {
                    String[] split = tBean.getCrtDuetime().split(" ");
                    int Duemin = Integer.parseInt(split[1]);
                    int DueHour=Integer.parseInt(split[0]);
                    Log.i("ppp", "adapter getDuedate" + tBean.getDuedate());

                    if (DueHour <=0 && Duemin <=0) {
                        DateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy");
                        DateFormat outputFormat = new SimpleDateFormat("MMM dd");
                        Date date = inputFormat.parse(tBean.getDuedate());
                        String outputDateStr = outputFormat.format(date);
                        Log.i("ppp", "duee date and time" + outputDateStr+""+tBean.getDuetime());
                        Log.i("ppp", "_________remaining time_________" +split[0]+"hours "+split[1]+"min");
                        holder.pending_hours.setText(outputDateStr+" "+tBean.getDuetime());
                    } else if(DueHour!=0 && Duemin!=0)
                        holder.pending_hours.setText(DueHour + " hours "+Duemin+" min");
                    else if(DueHour==0 && Duemin!=0)
                        holder.pending_hours.setText(Duemin + " min ");
                    else if(DueHour!=0 && Duemin==0)
                        holder.pending_hours.setText(DueHour+" hours");

                }
                if (tBean.getHeader() != null) {
                    holder.headertext.setText(tBean.getHeader());
                    holder.header.setVisibility(View.VISIBLE);
                }
                if (tBean.getTaskstatus().equalsIgnoreCase("1")) {
                    Log.i("calender", "task status" + tBean.getPatientname()+"-=> "+tBean.getTaskstatus());
                    holder.chbox1.setChecked(true);
                    holder.chbox1.setEnabled(false);
                } else {
                    Log.i("calender", "task status" + tBean.getPatientname()+"-=> "+tBean.getTaskstatus());

                    holder.chbox1.setChecked(false);
                }
                holder.chbox1
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton arg0,
                                                         boolean isChecked) {
                                if (holder.doctorname.getText().toString().equalsIgnoreCase("Unassigned")) {
                                    Log.i("ppp", "else Unassigned");
                                    showToast("Please assigned the task to the member ");
                                    tBean.setTaskstatus("0");

                                } else {
                                    Log.i("ppp", "Unassigned");
                                    if (isChecked) {
                                        holder.chbox1.setEnabled(false);
                                        tBean.setTaskstatus("1");
                                        DBAccess.getdbHeler().insertorUpdatTaskDetails(tBean);
                                        WebServiceReferences.webServiceClient.SetTaskRecord(tBean, SingleInstance.mainContext);
                                    }
                                }
                            }
                        });

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return row;
    }


    private void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ViewHolder {
        TextView patientname;
        TextView doctorname;
        TextView taskname;
        TextView pending_hours;
        RelativeLayout header;
        TextView headertext;
        LinearLayout linear_list;
        CheckBox chbox1;
    }

}
