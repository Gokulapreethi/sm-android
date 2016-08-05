package com.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.Calendar.DateView;
import com.cg.DB.DBAccess;
import com.cg.rounding.RoundingTaskAdapter;
import com.cg.rounding.TaskDateComparator;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;

import org.lib.model.TaskDetailsBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;


public class CalendarActivity extends Activity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.round_task);
        context=this;

        RelativeLayout status = (RelativeLayout) findViewById(R.id.header);
        RelativeLayout dateheader = (RelativeLayout) findViewById(R.id.dateheader);
        ImageView plusBtn = (ImageView) findViewById(R.id.plusBtn);
        ListView tasklistView = (ListView) findViewById(R.id.listview_task);
        status.setVisibility(View.GONE);
        plusBtn.setVisibility(View.GONE);
        dateheader.setVisibility(View.VISIBLE);
        String date=getIntent().getStringExtra("date");
        String groupid=getIntent().getStringExtra("groupid");
        String  strQuery = "select * from taskdetails where groupid='" + groupid + "'and duedate='" + date + "'";
        Log.i("AAAA","selected date--- "+strQuery);
        Vector<TaskDetailsBean> tasklist = DBAccess.getdbHeler().getAllTaskDetails(strQuery);
        Collections.sort(tasklist, new TaskDateComparator());
        Vector<TaskDetailsBean> taskList = tasklist;
        RoundingTaskAdapter taskAdapter = new RoundingTaskAdapter(context, R.layout.round_task_row, taskList);
        tasklistView.setAdapter(taskAdapter);

        TextView date_tv = (TextView) findViewById(R.id.date_tv);
        Button back = (Button) findViewById(R.id.cancel);
        date_tv.setText(date);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
